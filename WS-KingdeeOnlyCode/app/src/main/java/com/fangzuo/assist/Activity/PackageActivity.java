package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CodeCheckBackDataBean;
import com.fangzuo.assist.Beans.CodeCheckBean;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.MainLockData;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.Dao.Unit;
import com.fangzuo.assist.Dao.WaveHouse;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.DataModel;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.LocDataUtil;
import com.fangzuo.assist.Utils.MediaPlayer;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.databinding.ActivityPackageBinding;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.assist.widget.SpinnerUnit;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PackageActivity extends BaseActivity {
    private ActivityPackageBinding binding;
    private ShareUtil share;
    private long ordercode;
    private CodeCheckBackDataBean codeCheckBackDataBean;
    private Product product;
    private Unit unit;
    private DaoSession daoSession;
    private T_DetailDao t_detailDao;
    private T_mainDao t_mainDao;
    private int year;
    private int month;
    private int day;
    private int activity = Config.PackageActivity;
    private boolean isAuto;
    private Storage storage;
    private MainLockData mainLockData;
    private WaveHouse waveHouse;
//    private String wavehouseID;
//    private String wavehouseName;
    private boolean fBatchManager;

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg){
            case EventBusInfoCode.CodeCheck_OK:
                codeCheckBackDataBean = (CodeCheckBackDataBean)event.postEvent;
                Lg.e("条码检查："+codeCheckBackDataBean.toString());
                LoadingUtil.dismiss();
                if (binding.etZz.getText().toString().equals(barcodeStr)){
                    Toast.showText(mContext,"箱码与SN码不能一致");
                    MediaPlayer.getInstance(mContext).error();
                    lockScan(0);
                    return;
                }
                binding.edSn.setText(barcodeStr);
//                binding.tvPihao.setText(codeCheckBackDataBean.FBatchNo);
//                binding.etNum.setText(codeCheckBackDataBean.FQty);
                if (isAuto){
                    AddorderBefore();
                }else{
                    lockScan(0);
                }
//                getProduct();
                break;
            case EventBusInfoCode.CodeCheck_Error:
                lockScan(0);
                LoadingUtil.dismiss();
                codeCheckBackDataBean = (CodeCheckBackDataBean)event.postEvent;
                Toast.showText(mContext, codeCheckBackDataBean.FTip);
                break;
            case EventBusInfoCode.CodeInsert_OK:
                Addorder();
                break;
            case EventBusInfoCode.CodeInsert_Error:
                lockScan(0);
                codeCheckBackDataBean = (CodeCheckBackDataBean)event.postEvent;
                Toast.showText(mContext, codeCheckBackDataBean.FTip);
                break;
            case EventBusInfoCode.Upload_OK:
                MediaPlayer.getInstance(mContext).ok();
                Toast.showText(mContext, "上传成功");
                t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(
                        T_DetailDao.Properties.Activity.eq(activity)
                ).build().list());
                t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(
                        T_mainDao.Properties.Activity.eq(activity)
                ).build().list());
                ordercode++;
                share.setOrderCode(PackageActivity.this, ordercode);
                reCheckUi();
                LoadingUtil.dismiss();
                break;
            case EventBusInfoCode.Upload_Error:
                String error = (String)event.postEvent;
                MediaPlayer.getInstance(mContext).error();
                Toast.showText(mContext, error);
                LoadingUtil.dismiss();
                break;
            case EventBusInfoCode.PRODUCTRETURN:
                product = (Product) event.postEvent;
                Hawk.put(Config.Product+activity,product);
                dealProduct();
                setfocus(binding.edSn);
                break;
        }
    }
    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_package);
        binding.activityTop.tvTopTitle.setText("组装单");
        share = ShareUtil.getInstance(mContext);
        daoSession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        t_detailDao = daoSession.getT_DetailDao();
        t_mainDao = daoSession.getT_mainDao();
        mainLockData = new MainLockData();
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        setfocus(binding.spUnit);
        isAuto =share.getPackageAuto();
        binding.activityTop.isAutoAdd.setChecked(isAuto);
        binding.activityTop.ishebing.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        ordercode = CommonUtil.createOrderCode(this);
        binding.tvDate.setText(getTime(true));
        binding.spWhichStorage.setAutoSelection(Config.Storage+activity, "",true);
        reCheckUi();
    }


    private void reCheckUi(){
        boolean state;
        if (LocDataUtil.checkHasMain(mContext,ordercode)){
            state=false;
            binding.edCode.setEnabled(state);binding.edCode.setFocusable(state);
            binding.edCode.setFocusableInTouchMode(state);
            binding.etZz.setEnabled(state);binding.etZz.setFocusable(state);
//            binding.edSn.setEnabled(state);binding.edSn.setFocusable(state);
            binding.search.setEnabled(state);
            binding.scanbyCamera.setEnabled(state);
            binding.spWhichStorage.setEnable(state);
            binding.spWavehouse.setEnable(state);
            binding.spUnit.setEnable(state);
            binding.edPihao.setText(Hawk.get(Config.Batch+activity,""));
            binding.etZz.setText(Hawk.get(Config.BoxCode+activity,""));
            EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.PRODUCTRETURN,Hawk.get(Config.Product+activity,new Product())));
            setfocus(binding.edSn);
            binding.tvBoxNum.setText("当前装箱数：" +LocDataUtil.getDetailNum(mContext,ordercode));
        }else{
            state = true;
            setfocus(binding.etZz);
            binding.edCode.setEnabled(state);binding.edCode.setFocusable(state);
            binding.edCode.setFocusableInTouchMode(state);
            binding.etZz.setEnabled(state);binding.etZz.setFocusable(state);
//            binding.edSn.setEnabled(state);binding.edSn.setFocusable(state);
            binding.search.setEnabled(state);
            binding.scanbyCamera.setEnabled(state);
            binding.spWhichStorage.setEnable(state);
            binding.spWavehouse.setEnable(state);
            binding.spUnit.setEnable(state);
            binding.edCode.setText("");binding.tvGoodName.setText("");binding.tvModel.setText("");
            binding.etZz.setText("");
            binding.edSn.setText("");
            binding.tvGoodcode.setText("");
            binding.tvBoxNum.setText("当前装箱数：0");

        }
    }

    @Override
    protected void initListener() {
        binding.activityTop.isAutoAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAuto = b;
                share.setPackageAuto(b);
            }
        });
        binding.tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(binding.tvDate);
            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddorderBefore();
            }
        });
        binding.btnFinishorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishOrder();
            }
        });
        binding.btnBackorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DataModel.checkHasDetail(mContext,activity)){
                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                    ab.setTitle("是否回单");
                    ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            upload();
                        }
                    });
                    ab.setNegativeButton("取消",null);
                    ab.create().show();
                }else{
                    Toast.showText(mContext,"无单据信息");
                }
            }
        });
        binding.btnCheckorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putInt("activity", activity);
                startNewActivity(Table4Activity.class, 0, 0, false, b);
            }
        });
        binding.spUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unit = (Unit) binding.spUnit.getAdapter().getItem(i);
                Hawk.put(Config.Unit+activity,unit.FMeasureUnitID);
//                if (unit != null) {
//                    unitId = unit.FMeasureUnitID;
//                    unitName = unit.FName;
//                    unitrate = Double.parseDouble(unit.FCoefficient);
//                }
                Log.e("选取单位：", unit.toString() + "");

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spWhichStorage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                storage = (Storage) binding.spWhichStorage.getAdapter().getItem(i);
                Hawk.put(Config.Storage+activity,storage.FName);
//                wavehouseID = "0";
                waveHouse =null;
                binding.spWavehouse.setAuto(mContext, storage, Hawk.get(Config.WaveHouse+activity,""));
                Lg.e("仓库", storage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spWavehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                waveHouse = (WaveHouse) binding.spWavehouse.getAdapter().getItem(i);
                Hawk.put(Config.WaveHouse+activity,waveHouse.FSPID);
//                wavehouseID = waveHouse.FSPID;
//                wavehouseName = waveHouse.FName;
                Lg.e("仓位", waveHouse);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("search", binding.edCode.getText().toString());
                b.putInt("where", Info.SEARCHPRODUCT);
                startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.SEARCHFORRESULT, b);
            }
        });

    }

    //    private String barcode = "";
    @Override
    protected void OnReceive(final String code) {
        //当扫的是装箱码时
        if (binding.etZz.hasFocus()){
            LoadingUtil.showDialog(mContext,"正在检测装箱码...");
            //查询条码唯一表
            CodeCheckBean bean = new CodeCheckBean(barcodeStr,barcodeStr + "",BasicShareUtil.getInstance(mContext).getIMIE());
            bean.FScanType="0";
            App.getRService().doIOAction(WebApi.CodeCheck, gson.toJson(bean), new MySubscribe<CommonResponse>() {
                @Override
                public void onNext(CommonResponse commonResponse) {
                    super.onNext(commonResponse);
                    LoadingUtil.dismiss();
                    lockScan(0);
                    if (!commonResponse.state)return;
                    DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                    if (dBean.codeCheckBackDataBeans.get(0).FTip.equals("OK")){
                        binding.etZz.setText(barcodeStr);
//                        setfocus(binding.edSn);
                    }else if (dBean.codeCheckBackDataBeans.get(0).FTip.equals("OK2")){
                        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                        ab.setTitle("提示改该箱码已装箱,是否继续追加");
                        ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                binding.etZz.setText(barcodeStr);
                            }
                        });
                        ab.setNegativeButton("取消",  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                binding.etZz.setText("");
                            }
                        });
                        ab.create().show();

                    }else{
                        Toast.showText(mContext,dBean.codeCheckBackDataBeans.get(0).FTip);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    lockScan(0);
                    LoadingUtil.dismiss();
                    binding.etZz.setText("");
                    Toast.showText(mContext,e.toString());
                }
            });
            return;
        }
        if (!binding.etZz.getText().toString().equals("")){
            LoadingUtil.showDialog(mContext,"正在查找...");
            //查询条码唯一表
            CodeCheckBean bean = new CodeCheckBean(barcodeStr,binding.etZz.getText().toString() + "",BasicShareUtil.getInstance(mContext).getIMIE());
            bean.FScanType="1";
            DataModel.codeCheck(gson.toJson(bean));
        }else{
            lockScan(0);
            Toast.showText(mContext,"请先扫描装箱码");
        }

    }

    private void dealProduct(){
        if (product ==null)return;
        Lg.e("获得物料：",product);
        binding.edCode.setText(product.FNumber);
        binding.tvGoodName.setText(product.FName);
        binding.tvGoodcode.setText(product.FNumber);
        binding.tvModel.setText(product.FModel);
        binding.spUnit.setAuto(mContext,product.FUnitGroupID,Hawk.get(Config.Unit+activity,""), SpinnerUnit.Id);
        if ((product.FBatchManager) != null && (product.FBatchManager).equals("1")) {
            fBatchManager = true;
            setfocus(binding.edPihao);
            binding.edPihao.setEnabled(true);
        } else {
            binding.edPihao.setEnabled(false);
            fBatchManager = false;
        }

    }

    //查找物料
    private void getProduct() {
        LoadingUtil.showDialog(mContext,"获取物料信息...");
        App.getRService().getProductForId(codeCheckBackDataBean.FItemID, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                LoadingUtil.dismiss();
                final DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (dBean.products.size() > 0) {
                    product = dBean.products.get(0);
                    Lg.e("获得物料："+product.toString());
                    binding.spUnit.setAuto(mContext,product.FUnitGroupID,codeCheckBackDataBean.FUnitID, SpinnerUnit.Id);
                    binding.tvGoodName.setText(product.FName);
                    binding.tvGoodcode.setText(product.FNumber);
                    binding.tvModel.setText(product.FModel);
                    if (isAuto){
                        AddorderBefore();
                    }else{
                        lockScan(0);
                    }
                } else {
                    lockScan(0);
                    Toast.showText(mContext, "未找到物料信息");
                }
            }

            @Override
            public void onError(Throwable e) {
                LoadingUtil.dismiss();
                lockScan(0);
                Toast.showText(mContext, "物料信息获取失败" + e.toString());
            }
        });
    }

    private void AddorderBefore() {
        if (product == null) {
            Toast.showText(mContext, "未选择物料");
            MediaPlayer.getInstance(mContext).error();
            lockScan(0);
            return;
        }
        if ("".equals(barcodeStr)){
            lockScan(0);
            return;
        }
        if (binding.etZz.getText().toString().equals("")){
            Toast.showText(mContext, "装箱码不能为空");
            MediaPlayer.getInstance(mContext).error();
            lockScan(0);
            return;
        }
        if (binding.edSn.getText().toString().equals("")){
            Toast.showText(mContext, "SN码不能为空");
            MediaPlayer.getInstance(mContext).error();
            lockScan(0);
            return;
        }
        if (fBatchManager && binding.edPihao.getText().toString().equals("")){
            Toast.showText(mContext, "批号不能为空");
            MediaPlayer.getInstance(mContext).error();
            lockScan(0);
            return;
        }
//        if (binding.etNum.getText().toString().equals("")){
//            Toast.showText(mContext, "数量不能为空");
//            MediaPlayer.getInstance(mContext).error();
//            lockScan(0);
//            return;
//        }
        if (null == storage){
            Toast.showText(mContext, "仓库不能为空");
            MediaPlayer.getInstance(mContext).error();
            lockScan(0);
            return;
        }
        List<T_Detail> list = t_detailDao.queryBuilder().where(
                T_DetailDao.Properties.DataPush.eq(binding.etZz.getText().toString()),
                T_DetailDao.Properties.FBarcode.eq(barcodeStr)
        ).build().list();
        if (list.size()>0){
            Toast.showText(mContext,"本地已存在单据信息");
            lockScan(0);
            return;
        }
        //插入条码唯一临时表
//        CodeCheckBean bean = new CodeCheckBean(barcodeStr,binding.etZz.getText().toString() + "",BasicShareUtil.getInstance(mContext).getIMIE());
        CodeCheckBean bean = new CodeCheckBean();
        bean.FBarCode = barcodeStr;
        bean.FOrderID = binding.etZz.getText().toString();
        bean.FPDAID =BasicShareUtil.getInstance(mContext).getIMIE();
        bean.FItemID = product.FItemID;bean.FUnitID = unit.FMeasureUnitID;
        bean.FStockID = storage.FItemID;bean.FStockPlaceID = waveHouse==null?"0":waveHouse.FSPID;
        bean.FBatch=binding.tvPihao.getText().toString();
        DataModel.codeInsert(gson.toJson(bean));
    }

    private void Addorder(){
        String second = getTimesecond();

        t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(T_mainDao.Properties.OrderId.eq(ordercode)).build().list());
        T_main t_main = new T_main();
        t_main.FIndex = second;
        t_main.MakerId = share.getsetUserID();
        t_main.DataInput = binding.tvDate.getText().toString();
        t_main.DataPush = binding.tvDate.getText().toString();
        t_main.IMIE = getIMIE();
        t_main.activity = activity;
        t_main.orderId = ordercode;
        t_main.Rem = binding.etZz.getText().toString();
        long insert1 = t_mainDao.insert(t_main);

        T_Detail t_detail = new T_Detail();
        t_detail.FIndex = second;
        t_detail.FBarcode = barcodeStr;
        t_detail.MakerId = share.getsetUserID();
        t_detail.DataInput = binding.tvDate.getText().toString();
//        t_detail.DataPush = binding.tvDate.getText().toString();
        t_detail.DataPush = binding.etZz.getText().toString();
        t_detail.IMIE = getIMIE();
        t_detail.activity = activity;
        t_detail.FOrderId = ordercode;
        t_detail.FUnit=unit==null?"":unit.FName;
        t_detail.FUnitId=unit==null?"":unit.FMeasureUnitID;
        t_detail.FProductCode=product==null?"":product.FNumber;
        t_detail.FProductName=product==null?"":product.FName;
        t_detail.model=product==null?"":product.FModel;
        t_detail.FQuantity="1";
        long insert = t_detailDao.insert(t_detail);
        if (insert1 > 0 && insert > 0) {
            Toast.showText(mContext, "添加成功");
            MediaPlayer.getInstance(mContext).ok();
            resetAll();
        } else {
            lockScan(0);
            Toast.showText(mContext, "添加失败，请重试");
            MediaPlayer.getInstance(mContext).error();
        }
    }

    private void upload() {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        PurchaseInStoreUploadBean.purchaseInStore puBean = pBean.new purchaseInStore();
        ArrayList<String> detailContainer = new ArrayList<>();
        ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data = new ArrayList<>();
        List<T_main> mains = t_mainDao.queryBuilder().where(
                T_mainDao.Properties.Activity.eq(activity))
                .orderAsc(T_mainDao.Properties.OrderId).build().list();
        for (int i = 0; i < mains.size(); i++) {
//            if (i > 0 && mains.get(i).orderId == mains.get(i - 1).orderId) {
            if (i > 0 && mains.get(i).Rem.equals(mains.get(i - 1).Rem) ) {

            } else {
                detailContainer = new ArrayList<>();
                puBean = pBean.new purchaseInStore();
                String main;
                String detail = "";
                T_main t_main = mains.get(i);
                main = t_main.MakerId + "|" +
                        t_main.DataInput + "|" +
                        binding.tvDate.getText().toString() + "|" +
                        t_main.IMIE + "|" +
                        t_main.Rem + "|";
                puBean.main = main;
//                List<T_Detail> details = t_detailDao.queryBuilder().where(
//                        T_DetailDao.Properties.Activity.eq(activity),
//                        T_DetailDao.Properties.DataPush.eq(t_main.Rem)
//                ).build().list();
//                for (int j = 0; j < details.size(); j++) {
//                    if (j != 0 && j % 49 == 0) {
//                        Log.e("j%49", j % 49 + "");
//                        T_Detail t_detail = details.get(j);
//                        detail = detail +
//                                t_detail.FBarcode + "|" ;
//                        detail = detail.subSequence(0, detail.length() - 1).toString();
//                        detailContainer.add(detail);
//                        detail = "";
//                    } else {
//                        Log.e("j", j + "");
//                        Log.e("details.size()", details.size() + "");
//                        T_Detail t_detail = details.get(j);
//                        detail = detail +
//                                t_detail.FBarcode + "|";
//                        Log.e("detail1", detail);
//                    }
//                }
//                if (detail.length() > 0) {
//                    detail = detail.subSequence(0, detail.length() - 1).toString();
//                }
//
//                Log.e("detail", detail);
                detailContainer.add(detail);
                puBean.detail = detailContainer;
                data.add(puBean);
            }
        }
        pBean.list = data;
        DataModel.upload(WebApi.PackageUpload,gson.toJson(pBean));
    }

    public void finishOrder() {
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle("确认使用完单");
        ab.setMessage("确认？");
        ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                lockScan(0);
                ordercode++;
                Log.e("ordercode", ordercode + "");
//                share.setPackageCode(ordercode);
                share.setOrderCode(PackageActivity.this, ordercode);
                reCheckUi();

            }
        });
        ab.setNegativeButton("取消", null);
        ab.create().show();

    }

    private void resetAll() {
//        mainLockData.FOrderID = ordercode;
//        mainLockData.FBoxCode = binding.etZz.getText().toString();
//        mainLockData.FBatchNO = binding.edPihao.getText().toString();
//        daoSession.getMainLockDataDao().save(mainLockData);
        Hawk.put(Config.BoxCode+activity,binding.etZz.getText().toString());
        Hawk.put(Config.Batch+activity,binding.edPihao.getText().toString());
        lockScan(0);
        reCheckUi();
        binding.edSn.setText("");
//        product = null;
//        binding.tvPihao.setText("");
//        binding.etNum.setText("");
//        binding.tvGoodName.setText("");
//        binding.tvGoodcode.setText("");
//        binding.tvModel.setText("");
    }

    @Override
    protected void onDestroy() {
        binding.unbind();
        EventBus.getDefault().removeAllStickyEvents();
        super.onDestroy();
    }
}
