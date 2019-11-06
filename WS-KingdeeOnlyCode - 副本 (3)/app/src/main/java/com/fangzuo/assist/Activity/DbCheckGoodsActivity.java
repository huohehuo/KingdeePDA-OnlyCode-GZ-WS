package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.BatchNoSpAdapter;
import com.fangzuo.assist.Adapter.FidNoAdapter;
import com.fangzuo.assist.Adapter.FidNoShowAdapter;
import com.fangzuo.assist.Adapter.PushDownSubListAdapter;
import com.fangzuo.assist.Adapter.StorageSpAdapter;
import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Adapter.WaveHouseSpAdapter;
import com.fangzuo.assist.Beans.CodeCheckBackDataBean;
import com.fangzuo.assist.Beans.CodeCheckBean;
import com.fangzuo.assist.Beans.CommonBean;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.FidNoShowBean;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.BarCode;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.PushDownMain;
import com.fangzuo.assist.Dao.PushDownSub;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.Unit;
import com.fangzuo.assist.Dao.WaveHouse;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.CommonMethod;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.DataModel;
import com.fangzuo.assist.Utils.DoubleUtil;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.MediaPlayer;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.assist.widget.MyWaveHouseSpinner;
import com.fangzuo.greendao.gen.BarCodeDao;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.ProductDao;
import com.fangzuo.greendao.gen.PushDownMainDao;
import com.fangzuo.greendao.gen.PushDownSubDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.fangzuo.greendao.gen.UnitDao;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DbCheckGoodsActivity extends BaseActivity {
    private int tag = 24;
    private int activity = Config.DbCheckGoodsActivity;

    @BindView(R.id.ishebing)
    CheckBox ishebing;
    @BindView(R.id.isAutoAdd)
    CheckBox isAutoAdd;
    @BindView(R.id.lv_pushsub)
    ListView lvPushsub;
    @BindView(R.id.sp_storage)
    Spinner spStorage;
    @BindView(R.id.sp_wavehouse)
    MyWaveHouseSpinner spWavehouse;
    @BindView(R.id.productName)
    TextView productName;
    @BindView(R.id.sp_unit)
    Spinner spUnit;
    @BindView(R.id.ed_batchNo)
    EditText edBatchNo;
    @BindView(R.id.ed_num)
    EditText edNum;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.btn_backorder)
    Button btnBackorder;
    @BindView(R.id.btn_checkorder)
    Button btnCheckorder;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private DaoSession daoSession;
    boolean isAuto = true;
    private CommonMethod method;
    //    private StorageSpAdapter storageSpinner;
    private BatchNoSpAdapter batchNoSpAdapter;
    private WaveHouseSpAdapter waveHouseAdapter;
    //    private String storageID;
//    private String storageName;
//    private String waveHouseID;
//    private String waveHouseName;
    private ArrayList<PushDownSub> container;           //用于存储订单详情信息
    private ArrayList<String> fidcontainer;
    private PushDownSubDao pushDownSubDao;
    private PushDownMainDao pushDownMainDao;
    private PushDownSubListAdapter pushDownSubListAdapter;
    private Product product;
    private PushDownSub pushDownSub;
    private List<Product> products;
    private String batchNo;
    private String productID;
    private String fid;
    private String fentryid;
    private String fprice;
    private UnitSpAdapter unitAdapter;
    private String unitId;
    private String unitName;
    private double unitrate;
    private double unitrateSub;
    private String billNo;
    private ArrayList<String> fidc;
    private ArrayList<String> fidno;
    private String FstorageID;
    private String FwaveHouseID = "";
    //    private String Batch;
    private String default_unitID;
    private boolean fromScan = false;
    private String wavehouseAutoString = "";
    //    private Storage storage;
    private CodeCheckBackDataBean codeCheckBackDataBean;

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg) {
//            case EventBusInfoCode.PRODUCTRETURN:
////                product = (Product)event.postEvent;
////                setDATA("", true);
////                break;
//            case EventBusInfoCode.CheckBillNO://用于检测当前列表是否存在与扫描条码的单号不一样的物料（物料一样的情况)
//                edPihao.setText(codeCheckBackDataBean.FBatchNo);
//                edNum.setText(codeCheckBackDataBean.FQty);
//                LoadingUtil.dismiss();
//                ScanBarCode(codeCheckBackDataBean.FItemID);
//                break;
            case EventBusInfoCode.CodeCheck_OK://检测条码成功
                codeCheckBackDataBean = (CodeCheckBackDataBean) event.postEvent;
                Lg.e("条码检查：" + codeCheckBackDataBean.toString());
//                checkBillNO(codeCheckBackDataBean.FBillNo);
                edBatchNo.setText(codeCheckBackDataBean.FBatchNo);
                edNum.setText(codeCheckBackDataBean.FQty);
                LoadingUtil.dismiss();
                ScanBarCode(codeCheckBackDataBean.FItemID);
//                setDATA(codeCheckBackDataBean.FItemID,false);
                break;
            case EventBusInfoCode.CodeCheck_Error://检测条码失败
                LoadingUtil.dismiss();
                codeCheckBackDataBean = (CodeCheckBackDataBean) event.postEvent;
                Toast.showText(mContext, codeCheckBackDataBean.FTip);
                canClick = false;
                lockScan(0);
                break;
            case EventBusInfoCode.CodeInsert_OK://写入条码唯一表成功
                Addorder();
                break;
            case EventBusInfoCode.CodeInsert_Error://写入条码唯一表失败
                codeCheckBackDataBean = (CodeCheckBackDataBean) event.postEvent;
                Toast.showText(mContext, codeCheckBackDataBean.FTip);
                lockScan(0);
                break;
//            case EventBusInfoCode.Upload_OK://回单成功
//                t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(
//                        T_DetailDao.Properties.Activity.eq(activity)
//                ).build().list());
//                t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(
//                        T_mainDao.Properties.Activity.eq(activity)
//                ).build().list());
//                for (int i = 0; i < fidc.size(); i++) {
//                    List<PushDownSub> pushDownSubs = pushDownSubDao.queryBuilder().where(
//                            PushDownSubDao.Properties.FInterID.eq(fidc.get(i))
//                    ).build().list();
//                    pushDownSubDao.deleteInTx(pushDownSubs);
//                    List<PushDownMain> pushDownMains = pushDownMainDao.queryBuilder().where(
//                            PushDownMainDao.Properties.FInterID.eq(fidc.get(i))
//                    ).build().list();
//                    pushDownMainDao.deleteInTx(pushDownMains);
//                }
//                MediaPlayer.getInstance(mContext).ok();
//                btnBackorder.setClickable(true);
//                LoadingUtil.dismiss();
//                Bundle b = new Bundle();
//                b.putInt("123",tag);
//                startNewActivity(PushDownPagerActivity.class,0,0,true,b);
//
////                btnBackorder.setClickable(true);
////                LoadingUtil.dismiss();
//                break;
//            case EventBusInfoCode.Upload_Error://回单失败
//                String error = (String)event.postEvent;
//                Toast.showText(mContext, error);
//                btnBackorder.setClickable(true);
//                LoadingUtil.dismiss();
//                MediaPlayer.getInstance(mContext).error();
//                break;
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_db_check_goods);
        mContext = this;
        ButterKnife.bind(this);
        daoSession = GreenDaoManager.getmInstance(mContext).getDaoSession();

        isAutoAdd.setChecked(isAuto);
        method = CommonMethod.getMethod(mContext);
    }

    private long ordercode;

    @Override
    protected void initData() {
        ordercode = CommonUtil.createOrderCode(this);
        LoadBasicData();
        container = new ArrayList<PushDownSub>();
        //获取到跳转数据
        fidcontainer = getIntent().getExtras().getStringArrayList("fid");
        getList();
        List<PushDownMain> list1 = pushDownMainDao.queryBuilder().where(PushDownMainDao.Properties.FInterID.eq(fidcontainer.get(0))).build().list();
        if (list1.size() > 0) {
            billNo = list1.get(0).FBillNo;
        }
    }

    //初始化仓库Spinner
    private void LoadBasicData() {
//        storageSpinner = method.getStorageSpinner(spStorage);
    }

    private void getList() {
        container.clear();
        pushDownSubDao = daoSession.getPushDownSubDao();
        pushDownMainDao = daoSession.getPushDownMainDao();
        //根据跳转的数据，查找并添加所有的订单信息
        for (int i = 0; i < fidcontainer.size(); i++) {
            QueryBuilder<PushDownSub> qb = pushDownSubDao.queryBuilder();
            List<PushDownSub> list = qb.where(
                    PushDownSubDao.Properties.FInterID.eq(fidcontainer.get(i))
            ).build().list();
            container.addAll(list);
        }
        if (container.size() > 0) {
            Log.e("OCGA", "getList获取pushSub：" + container.toString());
            pushDownSubListAdapter = new PushDownSubListAdapter(mContext, container);
            lvPushsub.setAdapter(pushDownSubListAdapter);
            pushDownSubListAdapter.notifyDataSetChanged();
        } else {
            Toast.showText(mContext, "未查询到数据");
        }
    }

    @Override
    protected void initListener() {
        isAutoAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAuto = b;
            }
        });
//        spStorage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                storage = (Storage) storageSpinner.getItem(i);
//                storageID = storage.FItemID;
//                storageName = storage.FName;
//                //根据仓库，带出仓位信息
//                waveHouseID="0";
////                waveHouseAdapter = CommonMethod.getMethod(mContext).getWaveHouseAdapter(storage, spWavehouse);
//                spWavehouse.setAuto(mContext,storage,FwaveHouseID);
//
////                if (FwaveHouseID != null) {
////                    for (int j = 0; j < waveHouseAdapter.getCount(); j++) {
////                        if (((WaveHouse) waveHouseAdapter.getItem(j)).FSPID.equals(FwaveHouseID)) {
////                            spWavehouse.setSelection(j);
////                        }
////                    }
////                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        //仓位信息Spinner
//        spWavehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                WaveHouse waveHouse = (WaveHouse) spWavehouse.getAdapter().getItem(i);
//                waveHouseID = waveHouse.FSPID;
//                waveHouseName = waveHouse.FName;
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        //单位Spinner
        spUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Unit unit = (Unit) unitAdapter.getItem(i);
                if (unit != null) {
                    Log.e(TAG, "获取到Unit：" + unit.toString());
                    unitId = unit.FMeasureUnitID;
                    unitName = unit.FName;
                    unitrate = Double.parseDouble(unit.FCoefficient);
                    Log.e("取得单位unitId：", unitId + "");
                    Log.e("取得单位unitName：", unitName + "");
                    Log.e("取得单位unitrate：", unitrate + "");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lvPushsub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pushDownSub = (PushDownSub) pushDownSubListAdapter.getItem(i);
                getUnitrateSub(pushDownSub);
                ProductDao productDao = daoSession.getProductDao();
                if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                    Log.e(TAG, "ListView点击事件联网");
                    Asynchttp.post(mContext, getBaseUrl() + WebApi.PRPDUCTSEARCHWHERE, pushDownSub.FItemID, new Asynchttp.Response() {
                        @Override
                        public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                            final DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                            Log.e("product.size", dBean.products.size() + "");
                            if (dBean.products.size() > 0) {
                                product = dBean.products.get(0);
                                Log.e("product.size", product + "");
                                clickList(product);
                            }
                        }

                        @Override
                        public void onFailed(String Msg, AsyncHttpClient client) {
                            Toast.showText(mContext, Msg);
                        }
                    });
                } else {
                    Log.e(TAG, "ListView点击事件--不-联网");
                    products = productDao.queryBuilder().where(
                            ProductDao.Properties.FItemID.eq(pushDownSub.FItemID)
                    ).build().list();
                    if (products.size() > 0) {
                        product = products.get(0);
                        clickList(product);
                    }
                }

            }
        });

        //防止ScrollView与ListView滑动冲突
        lvPushsub.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
    }

    //获取明细里面的单位的换算率
    private void getUnitrateSub(PushDownSub pushDownSub) {
        UnitDao unitDao = daoSession.getUnitDao();
        List<Unit> units = unitDao.queryBuilder().where(
                UnitDao.Properties.FMeasureUnitID.eq(pushDownSub.FUnitID)
        ).build().list();
        if (units.size() > 0) {
            unitrateSub = Double.parseDouble(units.get(0).FCoefficient);
            Lg.e("获得明细换算率：" + unitrateSub);
        } else {
            unitrateSub = 1;
            Lg.e("获得明细换算率失败：" + unitrateSub);
        }
    }

    //获取到Product详情
    private void clickList(Product product) {
        Log.e(TAG, "获取product:\n" + product.toString());
        FstorageID = pushDownSub.FDCStockID;
        FwaveHouseID = pushDownSub.FDCSPID;
//        Batch = pushDownSub.FBatchNo;
//        edBatchNo.setText(batchNo);

//        if (FstorageID != null) {
//            for (int i = 0; i < storageSpinner.getCount(); i++) {
//                if (((Storage) storageSpinner.getItem(i)).FItemID.equals(FstorageID)) {
//                    spStorage.setSelection(i);
//                }
//            }
//        }
        productName.setText(product.FName);
        productID = pushDownSub.FItemID;
        fid = pushDownSub.FInterID;
        fentryid = pushDownSub.FEntryID;
        fprice = pushDownSub.FAuxPrice;
        unitAdapter = CommonMethod.getMethod(mContext).getUnitAdapter(product.FUnitGroupID, spUnit);
        if (fromScan) {
            chooseUnit(default_unitID);
        } else {
            chooseUnit(pushDownSub.FUnitID);
        }
        fromScan = false;
        if (isAuto) {
//            edNum.setText("1.0");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AddorderBefore();

                }
            }, 100);
        }
    }

    //定位单位
    private void chooseUnit(String str) {
        if (str != null) {
            for (int i = 0; i < unitAdapter.getCount(); i++) {
                if (((Unit) unitAdapter.getItem(i)).FMeasureUnitID.equals(str)) {
                    Lg.e("定位单位：" + unitAdapter.getItem(i).toString());
                    spUnit.setSelection(i);
                }
            }
        }

    }

    private String barcode = "";

    @Override
    protected void OnReceive(String code) {
        //获取扫码数据
//        ScanBarCode(code);
        barcode = code;
//        edNum.setEnabled(!barcode.contains("ZZ"));
        LoadingUtil.showDialog(mContext, "正在查找...");
        //查询条码唯一表
        CodeCheckBean bean = new CodeCheckBean(barcode);
        DataModel.codeCheckForOut(gson.toJson(bean));
    }

    //扫码
    private void ScanBarCode(String barcode) {
        product = null;
        ProductDao productDao = daoSession.getProductDao();
        BarCodeDao barCodeDao = daoSession.getBarCodeDao();
        if (BasicShareUtil.getInstance(mContext).getIsOL()) {
            App.getRService().getProductForId(codeCheckBackDataBean.FItemID, new MySubscribe<CommonResponse>() {
                @Override
                public void onNext(CommonResponse commonResponse) {
                    LoadingUtil.dismiss();
                    final DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                    if (dBean.products.size() > 0) {
                        product = dBean.products.get(0);
                        Lg.e("获得物料：", product);
//                        getProductOL(dBean, 0);
                        default_unitID = dBean.products.get(0).FUnitID;
                        fromScan = true;
                        setProduct(product);
                    } else {
                        lockScan(0);
                        canClick = false;
                        Toast.showText(mContext, "未找到物料信息");
                    }
                }

                @Override
                public void onError(Throwable e) {
                    LoadingUtil.dismiss();
                    lockScan(0);
                    canClick = false;
                    Toast.showText(mContext, "物料信息获取失败" + e.toString());
                }
            });

//            Log.e(TAG,"ScanBarCode事件联网");
//            Asynchttp.post(mContext, getBaseUrl() + WebApi.SEARCHPRODUCTS, barcode, new Asynchttp.Response() {
//                @Override
//                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                    final DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
//                    if (dBean.products.size() > 0) {
//                        product = dBean.products.get(0);
//                        default_unitID = dBean.products.get(0).FUnitID;
//                        fromScan = true;
//                        setProduct(product);
//                    }
//                }
//
//                @Override
//                public void onFailed(String Msg, AsyncHttpClient client) {
//                    Toast.showText(mContext, Msg);
//                }
//            });
        } else {
            Log.e(TAG, "ScanBarCode事件--不-联网");
            final List<BarCode> barCodes = barCodeDao.queryBuilder().where(
                    BarCodeDao.Properties.FBarCode.eq(barcode)
            ).build().list();
            if (barCodes.size() > 0) {
                List<Product> products = productDao.queryBuilder().where(
                        ProductDao.Properties.FItemID.eq(barCodes.get(0).FItemID)
                ).build().list();
                if (products != null && products.size() > 0) {
                    Log.e("OCGA", "获取条码对应的product信息：" + products.get(0));
                    product = products.get(0);
                    default_unitID = barCodes.get(0).FUnitID;
                    fromScan = true;
                    setProduct(product);
                }
            } else {
                Toast.showText(mContext, "条码不存在");
                MediaPlayer.getInstance(mContext).error();
            }
        }

    }

    //扫码后设置product数据
    private void setProduct(Product product) {
        Log.e("OCGA", "获取setProduct对应的product信息：" + product);
        if (product != null) {
            boolean flag = true;
            boolean hasUnit = false;
            for (int j = 0; j < pushDownSubListAdapter.getCount(); j++) {
                PushDownSub pushDownSub1 = (PushDownSub) pushDownSubListAdapter.getItem(j);
                if (product.FItemID.equals(pushDownSub1.FItemID)) {
                    if (Double.parseDouble(pushDownSub1.FAuxQty) == Double.parseDouble(pushDownSub1.FQtying)) {
                        flag = true;
                        continue;
                    } else {
//                        for (int i = 0; i < pushDownSubListAdapter.getCount(); i++) {
//                            PushDownSub pushDownSub = (PushDownSub) pushDownSubListAdapter.getItem(i);
                        if (!"".equals(default_unitID)) {
                            if (default_unitID.equals(pushDownSub1.FUnitID)) {
                                hasUnit = true;
                                flag = false;
                                lvPushsub.setSelection(j);
                                lvPushsub.performItemClick(lvPushsub.getChildAt(j), j, lvPushsub.getItemIdAtPosition(j));
                                break;
                            }
                        } else {
                            hasUnit = true;
                            flag = false;
                            lvPushsub.setSelection(j);
                            lvPushsub.performItemClick(lvPushsub.getChildAt(j), j, lvPushsub.getItemIdAtPosition(j));
                            break;
                        }

//                        }
//                        if (!hasUnit){
//                            lvPushsub.setSelection(j);
//                            lvPushsub.performItemClick(lvPushsub.getChildAt(j), j, lvPushsub.getItemIdAtPosition(j));
//                        }


//                        flag = false;
//                        lvPushsub.setSelection(j);
//                        lvPushsub.performItemClick(lvPushsub.getChildAt(j), j, lvPushsub.getItemIdAtPosition(j));
//                        clickList(product);
//                        break;
                    }
                }
            }

            if (flag) {
                Toast.showText(mContext, "商品不存在");
                MediaPlayer.getInstance(mContext).error();

            }
        } else {
            Toast.showText(mContext, "列表中不存在商品");
        }
    }

    @OnClick({R.id.btn_add, R.id.btn_backorder, R.id.btn_checkorder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                AddorderBefore();
                break;
            case R.id.btn_backorder:
                btnBackorder.setClickable(false);
                LoadingUtil.show(mContext, "正在回单...");
                upload();
                break;
            case R.id.btn_checkorder:
                Bundle b = new Bundle();
                b.putInt("activity", activity);
                startNewActivity(Table3Activity.class, 0, 0, false, b);
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("resume", "resume");
        getList();
    }

    private void AddorderBefore() {
        String num = edNum.getText().toString();

        if (null == product) {
            Toast.showText(mContext, "未选择物料");
            lockScan(0);
            return;
        }
        if (edNum.getText().toString().equals("") || edNum.getText().toString().equals("0")) {
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "请输入数量");
            lockScan(0);
            return;
        }
        if (fid == null) {
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "请选择单据");
            lockScan(0);
            return;
        }
        if (null == codeCheckBackDataBean) {
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "请扫码添加");
            lockScan(0);
            return;
        }
        Lg.e("比较1", codeCheckBackDataBean);
        Lg.e("比较2", pushDownSub);
        if (!codeCheckBackDataBean.FStockID.equals(pushDownSub.FSCStockID)) {
            MediaPlayer.getInstance(mContext).error();
            LoadingUtil.showAlter(mContext, "提示", "条码验证的仓库与列表明细不符合");
            lockScan(0);
            return;
        }
        if (!codeCheckBackDataBean.FBatchNo.equals(pushDownSub.FBatchNo)) {
            MediaPlayer.getInstance(mContext).error();
            LoadingUtil.showAlter(mContext, "提示", "条码验证的批号与列表批号不符合");
            lockScan(0);
            return;
        }
        if (Double.parseDouble(pushDownSub.FAuxQty) < ((Double.parseDouble(num) * unitrate) / unitrateSub + Double.parseDouble(pushDownSub.FQtying))) {
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "大兄弟,您的数量超过我的想象");
            lockScan(0);
            return;
        }
        //插入条码唯一临时表
        CodeCheckBean bean = new CodeCheckBean(barcode, ordercode + "", pushDownSub.FDCStockID, pushDownSub.FDCSPID, BasicShareUtil.getInstance(mContext).getIMIE());
        DataModel.codeInsertForIn(gson.toJson(bean));

    }

    //添加
    private void Addorder() {
        String discount = "";
        //数量
        String num = edNum.getText().toString();
        if (edNum.getText().toString().equals("")) {
            Toast.showText(mContext, "请输入数量");
            return;
        }
        batchNo = edBatchNo.getText().toString();
        /*boolean isHebing = true;
        if (isHebing) {
            List<T_Detail> detailhebing = t_detailDao.queryBuilder().where(
                    T_DetailDao.Properties.Activity.eq(activity),
                    T_DetailDao.Properties.FInterID.eq(fid),
                    T_DetailDao.Properties.FUnitId.eq(unitId),
                    T_DetailDao.Properties.FProductId.eq(product.FItemID),
                    T_DetailDao.Properties.FStorageId.eq(pushDownSub.FSCStockID),
                    T_DetailDao.Properties.FPositionId.eq(pushDownSub.FSCSPID),
                    T_DetailDao.Properties.FEntryID.eq(fentryid),
                    T_DetailDao.Properties.FBatch.eq(batchNo == null ? "" : batchNo)
            ).build().list();
            if (detailhebing.size() > 0) {
                for (int i = 0; i < detailhebing.size(); i++) {
                    num = (Double.parseDouble(num) + Double.parseDouble(detailhebing.get(i).FQuantity)) + "";
                    t_detailDao.delete(detailhebing.get(i));
                }
            }
        }*/

        String second = getTimesecond();
        T_Detail t_detail = new T_Detail();
        t_detail.FBatch = batchNo == null ? "" : batchNo;
        t_detail.FOrderId = ordercode;
        t_detail.FBarcode = barcode;
        t_detail.IMIE = BasicShareUtil.getInstance(mContext).getIMIE();
        t_detail.FProductId = product.FItemID;
        t_detail.FProductName = product.FName;
        t_detail.FProductCode = product.FNumber;
        t_detail.FIndex = second;
        t_detail.FUnitId = unitId == null ? "" : unitId;
        t_detail.FUnit = unitName == null ? "" : unitName;
        t_detail.FStorage = pushDownSub.FStorageIn;
        t_detail.FStorageId = pushDownSub.FSCStockID;
        t_detail.FPosition = pushDownSub.FSCSPID;
        t_detail.FPositionId = pushDownSub.FSCSPID;
        t_detail.activity = activity;
        t_detail.FDiscount = ShareUtil.getInstance(mContext).getsetUserID();
        t_detail.FQuantity = num;
        t_detail.unitrate = unitrate;
        t_detail.FBillNo = billNo;
        t_detail.FTaxUnitPrice = fprice == null ? "" : fprice;
        t_detail.FEntryID = fentryid == null ? "" : fentryid;
        t_detail.FInterID = fid == null ? "" : fid;

        long insert = t_detailDao.insert(t_detail);
        Log.e(TAG, "添加条数：" + insert);
        Log.e(TAG, "添加了数据：" + t_detail.toString());

        if (insert > 0) {
            //更新订单详情的已验收数量
            pushDownSub.FQtying = DoubleUtil.sum(Double.parseDouble(pushDownSub.FQtying),
                    (Double.parseDouble(edNum.getText().toString()) * unitrate) / unitrateSub) + "";
            pushDownSubDao.update(pushDownSub);
            Toast.showText(mContext, "添加成功");
            MediaPlayer.getInstance(mContext).ok();
            edNum.setText("");
            codeCheckBackDataBean = null;
            pushDownSubListAdapter.notifyDataSetChanged();
            lockScan(0);
        } else {
            Toast.showText(mContext, "添加失败，请重试");
//                    MediaPlayer.getInstance(mContext).error();
        }

    }

    private ArrayList<FidNoShowBean> fidNoShowBeans;
    private ArrayList<CommonBean> backDataList;

    //回单
    private void upload() {
        fidc = new ArrayList<>();
        fidno = new ArrayList<>();
        fidNoShowBeans = new ArrayList<>();
        backDataList = new ArrayList<>();
        T_Detail t_detail;
        int q = 0;
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        PurchaseInStoreUploadBean.purchaseInStore puBean = pBean.new purchaseInStore();
        ArrayList<String> detailContainer = new ArrayList<>();
        String detail = "";
        final ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data = new ArrayList<>();
        List<PushDownMain> mainList = DataModel.getPdMain4DbCG(mContext, tag + "");
        Lg.e("本地存在单据" + mainList.size(), mainList);
        for (int i = 0; i < mainList.size(); i++) {
            List<PushDownSub> list = DataModel.getPdSub4DbCG(mContext, mainList.get(i).FInterID);
            Lg.e("得到分组后的sub" + list.size(), list);//只会有一个
            for (int k = 0; k < list.size(); k++) {
                boolean allDone = true;
                if (MathUtil.toD(list.get(k).FQtying) != MathUtil.toD(list.get(k).FAuxQty)) {
                    fidNoShowBeans.add(new FidNoShowBean(list.get(k).FBillNo, "", "未完成"));
                    allDone = false;
                    fidno.add(list.get(k).FBillNo);
                } else {
                    fidNoShowBeans.add(new FidNoShowBean(list.get(k).FBillNo, "", "已完成"));
                    List<T_Detail> details = DataModel.getDetail4DbCG(mContext, activity + "", list.get(k).FInterID);
                    Lg.e("得到分组后的明细" + details.size(), details);//只会有一个
                    t_detail = details.get(0);
                    fidc.add(t_detail.FInterID);
                    detail = detail +
                            t_detail.FDiscount + "|" +
                            t_detail.FInterID + "|" +
                            t_detail.FOrderId + "|" +
                            t_detail.IMIE;
                    Log.e("detail", detail);
                    detailContainer.add(detail);
                    puBean.detail = detailContainer;
                    data.add(puBean);
                    pBean.list = data;
                    backDataList.add(new CommonBean(t_detail.FInterID, gson.toJson(pBean), true));
                }
            }
        }
        if (fidno.size() > 0) {
            LoadingUtil.dismiss();
            btnBackorder.setClickable(true);
            AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
            ab.setTitle("以下单据未完成是否继续回单");
            View v = LayoutInflater.from(mContext).inflate(R.layout.pd_alert, null);
            ListView lv = v.findViewById(R.id.lv_alert);
            FidNoShowAdapter fidNoAdapter = new FidNoShowAdapter(mContext, fidNoShowBeans);
            lv.setAdapter(fidNoAdapter);
            ab.setView(v);
            ab.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    LoadingUtil.show(mContext, "正在回单...");
                    postToServer();

                }
            });
            ab.setNegativeButton("否", null);
            ab.create().show();
        } else {
            postToServer();
        }


//        List<PushDownMain> list1 = pushDownMainDao.queryBuilder().where(
//                PushDownMainDao.Properties.Tag.eq(tag)).build().list();
//        for (int i = 0; i < list1.size(); i++) {
//            boolean flag = true;
//            List<PushDownSub> list = pushDownSubDao.queryBuilder().where(
//                    PushDownSubDao.Properties.FInterID.eq(list1.get(i).FInterID)
//            ).build().list();
//            for (int k = 0; k < list.size(); k++) {
//                if (Double.parseDouble(list.get(k).FQtying) != Double.parseDouble(list.get(k).FAuxQty)) {
//                    flag = false;
//                }
//            }
//            List<T_Detail> details = t_detailDao.queryBuilder().where(
//                    T_DetailDao.Properties.Activity.eq(activity),
//                    T_DetailDao.Properties.FInterID.eq(list1.get(i).FInterID)).build().list();
//            if(details.size()>0){
//                t_detail = details.get(0);
//                if (flag) {
//                    fidc.add(t_detail.FInterID);
//                    detail = detail +
//                            t_detail.FDiscount + "|" +
//                            t_detail.FInterID + "|" +
//                            t_detail.FOrderId + "|" +
//                            t_detail.IMIE + "|";
//                }else{
//                    fidno.add(t_detail.FBillNo);
//                }
//            }else{
//                fidno.add(list1.get(0).FBillNo);
//            }
//
//        }
//
//        if (detail.length() > 0) {
//            detail = detail.subSequence(0, detail.length() - 1).toString();
//        }
//        Log.e("detail", detail);
//        detailContainer.add(detail);
//        puBean.detail = detailContainer;
//        data.add(puBean);
//        if (fidno.size() > 0) {
//            LoadingUtil.dismiss();
//            btnBackorder.setClickable(true);
//            AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
//            ab.setTitle("以下单据未完成是否继续回单");
//            View v = LayoutInflater.from(mContext).inflate(R.layout.pd_alert, null);
//            ListView lv = v.findViewById(R.id.lv_alert);
//            FidNoAdapter fidNoAdapter = new FidNoAdapter(mContext, fidno);
//            lv.setAdapter(fidNoAdapter);
//            ab.setView(v);
//            ab.setPositiveButton("继续", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    LoadingUtil.show(mContext,"正在回单...");
//                    postToServer(data);
//
//                }
//            });
//            ab.setNegativeButton("否", null);
//            ab.create().show();
//        } else {
//            postToServer(data);
//        }

    }

    //回单请求
    private void postToServer() {
        if (null == backDataList || backDataList.size() <= 0) {
            Toast.showText(mContext, "本地不存在待回单数据");
            MediaPlayer.getInstance(mContext).error();
            LoadingUtil.dismiss();
            lockScan(0);
            return;
        }
        for (int i = 0; i < backDataList.size(); i++) {
            final CommonBean commonBean = backDataList.get(i);
            App.getRService().doIOAction(WebApi.OCUPLOAD, commonBean.FStandby2, new MySubscribe<CommonResponse>() {
                @Override
                public void onNext(CommonResponse commonResponse) {
                    super.onNext(commonResponse);
                    if (!commonResponse.state) return;
                    t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(
                            T_DetailDao.Properties.FInterID.eq(commonBean.FStandby1),
                            T_DetailDao.Properties.Activity.eq(activity)
                    ).build().list());
                    pushDownSubDao.deleteInTx(pushDownSubDao.queryBuilder().where(
                            PushDownSubDao.Properties.FInterID.eq(commonBean.FStandby1)).build().list());
                    pushDownMainDao.deleteInTx(pushDownMainDao.queryBuilder().where(
                            PushDownMainDao.Properties.FInterID.eq(commonBean.FStandby1)).build().list());
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    Toast.showText(mContext, commonBean.FStandby1 + "回单失败" + e.getMessage());
                    btnBackorder.setClickable(true);
                    MediaPlayer.getInstance(mContext).error();
                    LoadingUtil.dismiss();
                }
            });
            if (i==backDataList.size()-1){
                Toast.showText(mContext,"回单成功");
                ordercode++;
                Log.e("ordercode", ordercode + "");
                MediaPlayer.getInstance(mContext).ok();
                share.setOrderCode(DbCheckGoodsActivity.this, ordercode);
                btnBackorder.setClickable(true);
                LoadingUtil.dismiss();
                Bundle b = new Bundle();
                b.putInt("123", tag);
                startNewActivity(PushDownPagerActivity.class, 0, 0, true, b);
            }
        }

    }
//    //回单请求
//    private void postToServer(ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data) {
//        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
//        pBean.list = data;
//        Gson gson = new Gson();
//        Asynchttp.post(mContext, getBaseUrl() + WebApi.OCUPLOAD, gson.toJson(pBean), new Asynchttp.Response() {
//            @Override
//            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                MediaPlayer.getInstance(mContext).ok();
//                Toast.showText(mContext, "上传成功");
//                for (int j = 0; j < fidc.size(); j++) {
//                    List<T_Detail> list = t_detailDao.queryBuilder().where(
//                            T_DetailDao.Properties.FInterID.eq(fidc.get(j)),
//                            T_DetailDao.Properties.Activity.eq(activity)
//                    ).build().list();
//                    for (int i = 0; i < list.size(); i++) {
//                        t_detailDao.delete(list.get(i));
//                    }
//
//                    for (int i = 0; i < fidc.size(); i++) {
//                        Log.e("fidc", fidc.size() + "");
//                        List<PushDownSub> pushDownSubs = pushDownSubDao.queryBuilder().where(PushDownSubDao.Properties.FInterID.eq(fidc.get(i))).build().list();
//                        pushDownSubDao.deleteInTx(pushDownSubs);
//                        List<PushDownMain> pushDownMains = pushDownMainDao.queryBuilder().where(PushDownMainDao.Properties.FInterID.eq(fidc.get(i))).build().list();
//                        pushDownMainDao.deleteInTx(pushDownMains);
//                    }
//                }
//                ordercode++;
//                Log.e("ordercode", ordercode + "");
//                share.setOrderCode(DbCheckGoodsActivity.this, ordercode);
//                btnBackorder.setClickable(true);
//                LoadingUtil.dismiss();
//                Bundle b = new Bundle();
//                b.putInt("123", tag);
//                startNewActivity(PushDownPagerActivity.class, 0, 0, true, b);
//            }
//
//            @Override
//            public void onFailed(String Msg, AsyncHttpClient client) {
//                Toast.showText(mContext, Msg);
//                btnBackorder.setClickable(true);
//                MediaPlayer.getInstance(mContext).error();
//                LoadingUtil.dismiss();
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bundle b = new Bundle();
        b.putInt("123", tag);
        startNewActivity(PushDownPagerActivity.class, 0, 0, true, b);
    }
}
