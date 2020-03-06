package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.PDListAdapter;
import com.fangzuo.assist.Adapter.PDMainSpAdapter;
import com.fangzuo.assist.Adapter.PiciSpForSubAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter1;
import com.fangzuo.assist.Adapter.StorageSpAdapter;
import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Adapter.WaveHouseSpAdapter;
import com.fangzuo.assist.Beans.CheckInOutBean;
import com.fangzuo.assist.Beans.CodeCheckBackDataBean;
import com.fangzuo.assist.Beans.CodeCheckBean;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.PDListReturnBean;
import com.fangzuo.assist.Beans.PDSubRequestBean;
import com.fangzuo.assist.Beans.PDsubReturnBean;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.PDMain;
import com.fangzuo.assist.Dao.PDSub;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.Unit;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.CommonMethod;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.DataModel;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.MediaPlayer;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.assist.widget.MyWaveHouseSpinner;
import com.fangzuo.assist.widget.SpinnerUnit;
import com.fangzuo.assist.zxing.CustomCaptureActivity;
import com.fangzuo.assist.zxing.ScanManager;
import com.fangzuo.assist.zxing.activity.CaptureActivity;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.PDMainDao;
import com.fangzuo.greendao.gen.PDSubDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.loopj.android.http.AsyncHttpClient;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fangzuo.assist.R.id.isAutoAdd;

public class PD42Activity  extends BaseActivity {


    @BindView(R.id.btn_delete)
    RelativeLayout btnDelete;
    @BindView(R.id.sp_pdplan)
    Spinner spPdplan;
    @BindView(R.id.sp_storage)
    Spinner spStorage;
    @BindView(R.id.sp_wavehouse)
    MyWaveHouseSpinner spWavehouse;
    @BindView(R.id.scanbyCamera)
    RelativeLayout scanbyCamera;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.search)
    RelativeLayout search;
    @BindView(R.id.tv_goodName)
    TextView tvGoodName;
    @BindView(R.id.tv_num_onServer)
    TextView tvNumOnServer;
    @BindView(R.id.tv_ypnum)
    TextView tvYtnum;
    @BindView(R.id.ed_pdnum)
    EditText edPdnum;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.btn_backorder)
    Button btnBackorder;
    @BindView(R.id.btn_checkorder)
    Button btnCheckorder;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.btn_downloadall)
    Button btnDownloadall;
    @BindView(R.id.btn_downloadchoosen)
    Button btnDownloadchoosen;
    @BindView(R.id.cb_isClear)
    CheckBox cbIsClear;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.lv_pdlist)
    ListView lvPdlist;
    @BindView(R.id.mDrawer)
    DrawerLayout mDrawer;
    @BindView(R.id.sp_unit)
    SpinnerUnit spUnit;
    @BindView(R.id.ed_pihao)
    EditText edPihao;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(isAutoAdd)
    CheckBox autoAdd;
    @BindView(R.id.ishebing)
    CheckBox cbHebing;
    @BindView(R.id.sp_pihao)
    Spinner spPihao;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_model)
    TextView tvModel;
    @BindView(R.id.tv_storage)
    TextView tvStorage;
    @BindView(R.id.tv_wavehouse)
    TextView tvWavehouse;
    @BindView(R.id.zxing_barcode_scanner)
    DecoratedBarcodeView zxingBarcodeScanner;
    @BindView(R.id.ly_scan)
    RelativeLayout lyScan;
    private Gson gson;
    private List<PDMain> mainContainer;
    private List<Boolean> isCheck;
    private PDListAdapter pdListAdapter;
    private ArrayList<String> choice;
    private ArrayList<String> choiceAll;
    private DaoSession daoSession;
    private CommonMethod method;
    private PDMainSpAdapter pdMainSpAdapter;
    private String fid;
    private List<Product> products;
    private UnitSpAdapter unitAdapter;
    private String fprocessID;
    private StorageSpAdapter storageAdapter;
    private PDSub pdsubChoice;
    private Storage storage;
    private String storageId;
    private String storageName;
    private WaveHouseSpAdapter waveHouseAdapter;
    private String wavehouseID = "0";
    private String wavehouseName;
    private String unitId;
    private String unitName;
    private double unitrate;
    private PDSubDao pdSubDao;
    private PDMainDao pdMainDao;
    private ProductselectAdapter productselectAdapter;
    private ProductselectAdapter1 productselectAdapter1;
    //    private Product product;
    private String default_unitID;
    private boolean fBatchManager;
    private String FInStoreDate;
    private String FSpplierID;
    private String FInStoreOrderID;
    private boolean isClear = false;
    private PDMain pdMain;
    private List<PDMain> choiceContainer;
    private boolean isHebing = true;
    private boolean isAuto;

    private String pihao = "";
    private PiciSpForSubAdapter piciSpAdapter;
    private PDSub pdSub;
    private String wavehouseAutoString = "";
    private CodeCheckBackDataBean codeCheckBackDataBean;
    private int activity = Config.PD42Activity;
    private long ordercode;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_pd42);
        mContext = this;
        ButterKnife.bind(this);
        initDrawer(mDrawer);
        gson = new Gson();
        daoSession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        pdSubDao = daoSession.getPDSubDao();
        pdMainDao = daoSession.getPDMainDao();
//摄像头初始化
        mCaptureManager = new ScanManager(this, zxingBarcodeScanner);
        mCaptureManager.initializeFromIntent(getIntent(), savedInstanceState);
        isAuto = share.getPDisAuto();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg) {
            case EventBusInfoCode.ScanResult://
                BarcodeResult res = (BarcodeResult) event.postEvent;
                //                if (cbScaning.isChecked()){
//                }else{
//                lyScan.setVisibility(View.GONE);
//                }

                OnReceive(res.getResult().getText());
                mCaptureManager.onPause();
                mCaptureManager.onResume();
                mCaptureManager.decode();
                break;
//            case EventBusInfoCode.PRODUCTRETURN:
//                product = (Product)event.postEvent;
//                getSubQty();
//                setDATA("", true);
//                break;
            case EventBusInfoCode.CodeCheck_OK://检测条码成功
                codeCheckBackDataBean = (CodeCheckBackDataBean) event.postEvent;
                Lg.e("条码检查：" + codeCheckBackDataBean.toString());
//                edPihao.setText(codeCheckBackDataBean.FBatchNo);
//                edPdnum.setText(codeCheckBackDataBean.FQty);
                LoadingUtil.dismiss();
//                setDATA(codeCheckBackDataBean.FItemID, false);
                getSubQty();
                break;
            case EventBusInfoCode.CodeCheck_Error://检测条码失败
                LoadingUtil.dismiss();
                codeCheckBackDataBean = (CodeCheckBackDataBean) event.postEvent;
                Toast.showText(mContext, codeCheckBackDataBean.FTip);
                lockScan(0);
                break;
//            case EventBusInfoCode.CodeInsert_OK://写入条码唯一表成功
//                AddOrder();
//                break;
//            case EventBusInfoCode.CodeInsert_Error://写入条码唯一表失败
//                codeCheckBackDataBean = (CodeCheckBackDataBean)event.postEvent;
//                Toast.showText(mContext, codeCheckBackDataBean.FTip);
//                break;
            case EventBusInfoCode.Upload_OK://回单成功
                t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(
                        T_DetailDao.Properties.Activity.eq(activity)
                ).build().list());
//                t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(
//                        T_mainDao.Properties.Activity.eq(activity)
//                ).build().list());
                ordercode++;
                share.setOrderCode(this,ordercode);
                btnBackorder.setClickable(true);
                LoadingUtil.dismiss();
                MediaPlayer.getInstance(mContext).ok();
                break;
            case EventBusInfoCode.Upload_Error://回单失败
                String error = (String) event.postEvent;
                Toast.showText(mContext, error);
                btnBackorder.setClickable(true);
                LoadingUtil.dismiss();
                MediaPlayer.getInstance(mContext).error();
                break;
            case EventBusInfoCode.Insert_result://写入临时表结果
                DownloadReturnBean dBean3 = (DownloadReturnBean) event.postEvent;
                if (dBean3.inOutBeans.get(0).FTip.equals("OK")) {
                    AddOrder();
                } else {
                    lockScan(0);
                    Toast.showText(mContext, dBean3.inOutBeans.get(0).FTip);
                }
                break;
        }
    }

    @Override
    protected void initData() {
        cbHebing.setChecked(isHebing);
        autoAdd.setChecked(share.getPDisAuto());
        method = CommonMethod.getMethod(mContext);
        getBasicData();
        mainContainer = new ArrayList<>();
        isCheck = new ArrayList<>();
        choice = new ArrayList<>();
        choiceAll = new ArrayList<>();
        choiceContainer = new ArrayList<>();
        pdListAdapter = new PDListAdapter(mContext, mainContainer, isCheck);
        lvPdlist.setAdapter(pdListAdapter);
        getList();

        ordercode = CommonUtil.createOrderCode(this);

    }

    private void getBasicData() {
//        storageAdapter = method.getStorageSpinner(spStorage);
        pdMainSpAdapter = method.getpdmain(spPdplan);
    }

    @Override
    protected void initListener() {
        cbHebing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isHebing = b;
            }
        });
        autoAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAuto = b;
                share.setPDisAuto(b);
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(true);
                getList();
                refresh.setRefreshing(false);
                refresh.setRefreshing(false);
            }
        });
        cbIsClear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isClear = b;
            }
        });

//        edCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                    setDATA(edCode.getText().toString(), false);
//                    setfocus(edCode);
//                }
//                return true;
//            }
//        });
//        edPihao.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                    setfocus(edPdnum);
//                }
//                return true;
//            }
//        });

//        spPihao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                PDSub pdSub = (PDSub) piciSpAdapter.getItem(i);
//                pihao = pdSub.FBatchNo;
//                Lg.e("批次："+pihao);
////                getInstorageNum(product);
//                getSubQty();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        spUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Unit unit = (Unit) spUnit.getAdapter().getItem(i);
                unitId = unit.FMeasureUnitID;
                unitName = unit.FName;
                unitrate = Double.parseDouble(unit.FCoefficient);
                Log.e("点击单位", unit.toString());


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lvPdlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isCheck.get(i)) {
                    isCheck.set(i, false);
                    Log.e("isCheck", isCheck.get(i) + "");
                    for (int j = 0; j < choice.size(); j++) {
                        if (choice.get(j).equals(mainContainer.get(i).FID)) {
                            choice.remove(j);
                            choiceContainer.remove(j);
                        }
                    }
                } else {
                    isCheck.set(i, true);
                    Log.e("isCheck", isCheck.get(i) + "");
                    choice.add(mainContainer.get(i).FID);
                    choiceContainer.add(mainContainer.get(i));

                }
                pdListAdapter.notifyDataSetChanged();
            }
        });

        spPdplan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pdMain = (PDMain) pdMainSpAdapter.getItem(i);
                Lg.e("盘点方案",pdMain);
                fid = pdMain.FID;
                fprocessID = pdMain.FProcessId;
                List<PDSub> pdSubs = pdSubDao.queryBuilder().where(
                        PDSubDao.Properties.FID.eq(fid)
                ).build().list();
                Lg.e("盘点方案的明细"+pdSubs.size());
//                if (product != null && fid != null) {
//                    final List<PDSub> pdSubs = pdSubDao.queryBuilder().where(
//                            PDSubDao.Properties.FID.eq(fid),
//                            PDSubDao.Properties.FItemID.eq(product.FItemID)
//                    ).build().list();
//                    tvYtnum.setText(pdSubs.get(0).FCheckQty);
//                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        spStorage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                storage = (Storage) storageAdapter.getItem(i);
//                wavehouseID = "0";
////                waveHouseAdapter = CommonMethod.getMethod(mContext).getWaveHouseAdapter(storage, spWavehouse);
//                spWavehouse.setAuto(mContext, storage, wavehouseAutoString);
//
//                storageId = storage.FItemID;
//                storageName = storage.FName;
//                getSubQty();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        spWavehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                WaveHouse waveHouse = (WaveHouse) spWavehouse.getAdapter().getItem(i);
//                wavehouseID = waveHouse.FSPID;
//                wavehouseName = waveHouse.FName;
//                Log.e("wavehouseName", wavehouseName);
//                getSubQty();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

    }

    private void getSubQty() {
//        pihao = edPihao.getText().toString();
//        if (fid == null || product == null || storageId == null || wavehouseID == null || pihao == null) {
//            Lg.e("getSubQty:查询数据不完整");
//            return;
//        }
        if (null==pdMain)return;
        List<PDSub> pdSubs = pdSubDao.queryBuilder().where(
                PDSubDao.Properties.FID.eq(pdMain.FID),
                PDSubDao.Properties.FBarCode.eq(barcode)
        ).build().list();
        Lg.e("getSubQty:",pdSubs);
        if (pdSubs.size() > 0) {
            pdsubChoice = pdSubs.get(0);
            tvYtnum.setText(pdsubChoice.FCheckQty);
            tvNumOnServer.setText(pdsubChoice.FQty);
            tvStorage.setText(pdsubChoice.FStockName);
            tvWavehouse.setText(pdsubChoice.FSPName);
            tvModel.setText(pdsubChoice.FModel);
            tvNumber.setText(pdsubChoice.FNumber);
            tvGoodName.setText(pdsubChoice.FName);
            edCode.setText(pdsubChoice.FNumber);
            edPihao.setText(pdsubChoice.FBatchNo);

            if (autoAdd.isChecked()){
                edPdnum.setText("1");
                AddorderBefore();
            }else{
                lockScan(0);
            }

        } else {
            edCode.setText("");
            edPihao.setText("");
            tvStorage.setText("");
            tvWavehouse.setText("");
            tvModel.setText("");
            tvNumber.setText("");
            tvGoodName.setText("");
            tvYtnum.setText("0");
            tvNumOnServer.setText("0");
            Toast.showText(mContext,"未找到商品数据");
            lockScan(0);
        }

    }

//    //获取批次,根据调出仓库
//    private void getPici() {
//        List<PDSub> container1 = new ArrayList<>();
//        piciSpAdapter = new PiciSpForSubAdapter(mContext, container1);
//        spPihao.setAdapter(piciSpAdapter);
//        if (product == null) {
//            return;
//        }
//        if (fBatchManager) {
//            Log.e("查找pici：", "in...");
//            spPihao.setEnabled(true);
//            piciSpAdapter = CommonMethod.getMethod(mContext).getPici(pdsubChoice, spPihao);
//            piciSpAdapter.notifyDataSetChanged();
////            if (!BasicShareUtil.getInstance(mContext).getIsOL()) {
////                piciSpAdapter = CommonMethod.getMethod(mContext).getPici(storage, wavehouseID, product, spPihao);
////            } else {
////                final List<InStorageNum> container = new ArrayList<>();
////                GetBatchNoBean bean = new GetBatchNoBean();
////                bean.ProductID=product.FItemID;
////                bean.StorageID=storageId;
////                bean.WaveHouseID=wavehouseID;
////                String json = new Gson().toJson(bean);
////                Log.e(TAG, "getPici批次提交："+json);
////                Asynchttp.post(mContext, getBaseUrl() + WebApi.GETPICI, json, new Asynchttp.Response() {
////                    @Override
////                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
////                        Log.e(TAG,"getPici获取数据："+cBean.returnJson);
////                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
////                        if(dBean.InstorageNum!=null){
////                            for (int i = 0; i < dBean.InstorageNum.size(); i++) {
////                                if (dBean.InstorageNum.get(i).FQty != null
////                                        && Double.parseDouble(dBean.InstorageNum.get(i).FQty) > 0) {
////                                    Log.e(TAG,"有库存的批次："+dBean.InstorageNum.get(i).toString());
////                                    container.add(dBean.InstorageNum.get(i));
////                                }
////                            }
////                            piciSpAdapter = new PiciSpAdapter(mContext, container);
////                            spPihao.setAdapter(piciSpAdapter);
////                        }
////                        piciSpAdapter.notifyDataSetChanged();
////                    }
////
////                    @Override
////                    public void onFailed(String Msg, AsyncHttpClient client) {
////                        Log.e(TAG,"getPici获取数据错误："+Msg);
////                        Toast.showText(mContext, Msg);
////                    }
////                });
////            }
//        } else {
//            spPihao.setEnabled(false);
//        }
//    }

    private String barcode = "";
    @Override
    protected void OnReceive(String code) {
        if (null== pdMain){
            Toast.showText(mContext,"请先选择盘点方案");
            lockScan(0);
            return;
        }
        barcode = code;
        LoadingUtil.showDialog(mContext, "正在查找...");
        //查询条码唯一表
        CodeCheckBean bean = new CodeCheckBean(barcode);
        bean.FOrderID = pdMain.FID;
        DataModel.codeCheckForPD(gson.toJson(bean));
//            Log.e("CODE",code+":获得的code");
//            FInStoreDate = "";
//            FInStoreOrderID = "";
//            Log.e("code", code);
////            if (edPihao.hasFocus()) {
////                edPihao.setText(code);
////                setfocus(edPdnum);
////            } else {
////                String[] split = code.split("/");
////                if (split.length == 5) {
////                    FSpplierID = split[1];
////                    FInStoreDate = split[2];
////                    FInStoreOrderID = split[3];
////                    setDATA(split[0], false);
////                } else {
////                    edCode.setText(code);
////                    setDATA(code, false);
////                }
////            }
//
//        String[] split = code.split("/");
//                if (split.length == 5) {
//                    FSpplierID = split[1];
//                    FInStoreDate = split[2];
//                    FInStoreOrderID = split[3];
//                    setDATA(split[0], false);
//                } else {
//                    edCode.setText(code);
//                    setDATA(code, false);
//                }


    }


    //获取盘点方案list
    private void getList() {
        App.getRService().doIOAction(WebApi.PDMAINLIST, "", new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                super.onNext(commonResponse);
                if (!commonResponse.state)return;
                mainContainer.clear();
                isCheck.clear();
                choice.clear();
                choiceAll.clear();
                PDListReturnBean pBean = gson.fromJson(commonResponse.returnJson, PDListReturnBean.class);
                if (pBean.items != null) {
                    mainContainer.addAll(pBean.items);
                    for (int i = 0; i < mainContainer.size(); i++) {
                        isCheck.add(false);
                        choiceAll.add(mainContainer.get(i).FID);
                        Log.e("isCheck", isCheck.size() + "");
                    }
                }
                pdListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
//                super.onError(e);
                Toast.showText(mContext,e.getMessage());
            }
        });
    }


    @OnClick({R.id.btn_delete, R.id.scanbyCamera, R.id.search, R.id.btn_add, R.id.btn_backorder, R.id.btn_checkorder, R.id.btn_downloadall, R.id.btn_downloadchoosen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_delete:
                if ( null!= pdMain) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                    ab.setTitle("删除此计划么");
                    ab.setMessage("确认删除盘点计划?");
                    ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (!checkHasDetail()){
                                pdSubDao.deleteInTx(pdSubDao.queryBuilder().where(
                                        PDSubDao.Properties.FID.eq(pdMain.FID)
                                ).build().list());
                                pdMainDao.deleteInTx(pdMain);
                                pdMain =null;
//                                List<PDSub> list = pdSubDao.queryBuilder().where(
//                                        PDSubDao.Properties.FID.eq(pdMain.FID)
//                                ).build().list();
//                                for (int j = 0; j < list.size(); j++) {
//                                    pdSubDao.delete(list.get(j));
//                                }
//                                if (pdMain != null) {
//                                    pdMainDao.delete(pdMain);
//                                }
                                method.getpdmain(spPdplan);
                                Toast.showText(mContext, "删除成功");
//                            startNewActivity(MiddleActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, true, null);
                            }else{
                                Toast.showText(mContext,"不能删除正在盘点的方案");
                            }

                        }
                    }).setNegativeButton("取消", null).create().show();
                }

                break;
            case R.id.scanbyCamera:
                if (lyScan.getVisibility()==View.VISIBLE){
                    lyScan.setVisibility(View.GONE);
//                    mCaptureManager.onPause();
                }else{
                    mCaptureManager.onResume();
                    lyScan.setVisibility(View.VISIBLE);
                    mCaptureManager.decode();
                }

//                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                // 设置自定义扫描Activity
//                intentIntegrator.setCaptureActivity(CustomCaptureActivity.class);
//                intentIntegrator.initiateScan();

//                Intent in = new Intent(mContext, CaptureActivity.class);
//                startActivityForResult(in, 0);
                break;
            case R.id.search:
                Log.e("search", "onclick");
                Bundle b1 = new Bundle();
                b1.putString("search", edCode.getText().toString());
                b1.putInt("where", Info.SEARCHPRODUCT);
                startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.SEARCHFORRESULT, b1);
                break;
            case R.id.btn_add:
                AddorderBefore();
                break;
            case R.id.btn_backorder:
                if (DataModel.checkHasDetail(mContext, activity)) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                    ab.setTitle("是否回单");
                    ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            btnBackorder.setClickable(false);
                            LoadingUtil.show(mContext, "正在回单...");
                            upload();                        }
                    });
                    ab.setNegativeButton("取消",null);
                    ab.create().show();

                } else {
                    Toast.showText(mContext, "无单据信息");
                }
                break;
            case R.id.btn_checkorder:
                Bundle b2 = new Bundle();
                b2.putInt("activity", activity);
                startNewActivity(Table2Activity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, false, b2);
                break;
            case R.id.btn_downloadall:
                PDSubRequestBean pdSubRequestBean = new PDSubRequestBean();
                pdSubRequestBean.isClear = isClear;
                pdSubRequestBean.Fid = choiceAll;
                download(gson.toJson(pdSubRequestBean), true);
                break;
            case R.id.btn_downloadchoosen:
                if (choice.size() < 1) {
                    Toast.showText(mContext, "没有选择盘点方案");
                } else {
                    PDSubRequestBean pdSubRequestBean1 = new PDSubRequestBean();
                    pdSubRequestBean1.Fid = choice;
                    pdSubRequestBean1.isClear = isClear;
                    download(gson.toJson(pdSubRequestBean1), false);
                }
                break;
        }
    }

    //本地扫码唯一判断
    private boolean checkHasBarCode() {
        List<T_Detail> list = t_detailDao.queryBuilder().where(
                T_DetailDao.Properties.Activity.eq(activity),
                T_DetailDao.Properties.FBarcode.eq(barcode)
        ).build().list();
        if (list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
    //本地扫码唯一判断
    private boolean checkHasDetail() {
        if (null == pdMain)return false;
        List<T_Detail> list = t_detailDao.queryBuilder().where(
                T_DetailDao.Properties.Activity.eq(activity),
                T_DetailDao.Properties.FInterID.eq(pdMain.FID)
        ).build().list();
        if (list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void AddorderBefore(){
        if (checkHasBarCode()) {
            Toast.showText(mContext, "该数据已存在，请勿重复添加");
            MediaPlayer.getInstance(mContext).error();
            lockScan(0);
            return;
        }
        if (tvGoodName.getText().toString().equals("")) {
            Toast.showText(mContext, "请扫描物料");
            MediaPlayer.getInstance(mContext).error();
            lockScan(0);
            return;
        }
//        if (edCode.getText().toString().equals("")) {
//            Toast.showText(mContext, "请输入物料编号");
//            lockScan(0);
//            return;
//        }
        if (MathUtil.toD(edPdnum.getText().toString())<=0) {
            Toast.showText(mContext, "请输入盘点数量");
            lockScan(0);
            return;
        }

        //添加进临时表
        DataModel.InsertForInOutY(WebApi.InsertForPd, gson.toJson(new CheckInOutBean("",
                pdMain.FProcessId, barcode, "", pdsubChoice.FStockID, pdsubChoice.FStockPlaceID,
                BasicShareUtil.getInstance(mContext).getIMIE())));

    }

    private void AddOrder() {

        pihao = edPihao.getText().toString();
        String num = edPdnum.getText().toString();
//            if (isHebing) {
//                List<T_Detail> detailhebing = t_detailDao.queryBuilder().where(
//                        T_DetailDao.Properties.Activity.eq(activity),
//                        T_DetailDao.Properties.FProductId.eq(product.FItemID),
//                        T_DetailDao.Properties.FBatch.eq(pihao == null ? "" : pihao),
//                        T_DetailDao.Properties.FUnitId.eq(unitId),
//                        T_DetailDao.Properties.FStorageId.eq(storageId),
//                        T_DetailDao.Properties.FPositionId.eq(wavehouseID == null ? "0" : wavehouseID)).build().list();
//                if (detailhebing.size() > 0) {
//                    for (int i = 0; i < detailhebing.size(); i++) {
//                        num = (Double.parseDouble(edPdnum.getText().toString()) + Double.parseDouble(detailhebing.get(i).FQuantity)) + "";
//                        t_detailDao.delete(detailhebing.get(i));
//                    }
//                }
//            }
        T_Detail t_detail = new T_Detail();
//            t_detail.FIndex = second;
        t_detail.FBarcode = barcode;
        t_detail.MakerId = share.getsetUserID();
//            t_detail.DataInput = tvDate.getText().toString();
//            t_detail.DataPush = tvDate.getText().toString();
        t_detail.IMIE = getIMIE();
        t_detail.activity = activity;
        t_detail.FOrderId = ordercode;
        t_detail.FRate = pdMain.FProcessId;
        t_detail.IsAssemble = barcode.contains("ZZ") ? barcode : "";
        t_detail.FKFDate = codeCheckBackDataBean == null ? "" : codeCheckBackDataBean.FKFDate;
        t_detail.FKFPeriod = codeCheckBackDataBean == null ? "" : codeCheckBackDataBean.FKFPeriod;

        t_detail.FBatch = pihao == null ? "" : pihao;
        t_detail.FProductCode = edCode.getText().toString();
        t_detail.FProductId = pdsubChoice.FItemID;
        t_detail.model = pdsubChoice.FModel;
        t_detail.FProductName = pdsubChoice.FName;
        t_detail.FUnitId = pdsubChoice.FUnitID;
        t_detail.FUnit = unitName == null ? "" : unitName;
        t_detail.FStorage = pdsubChoice.FStockName;
        t_detail.FStorageId = pdsubChoice.FStockID;
        t_detail.FInterID = pdMain.FID;
        t_detail.FPosition = pdsubChoice.FSPName;
        t_detail.FPositionId = pdsubChoice.FStockPlaceID;
        t_detail.unitrate = unitrate;
        t_detail.FIndex = getTimesecond();
        t_detail.FIdentity = "0";
        t_detail.FQuantity = num;
        long insert = t_detailDao.insert(t_detail);
        if (insert > 0) {
            MediaPlayer.getInstance(mContext).ok();
            Toast.showText(mContext, "添加成功");
            if (pdsubChoice != null) {
                pdsubChoice.FCheckQty = MathUtil.sum(MathUtil.toD(pdsubChoice.FCheckQty)+"", MathUtil.toD(edPdnum.getText().toString())+"") + "";
                pdSubDao.update(pdsubChoice);
                ResetAll();
            } else {
                PDSub pdSub = new PDSub();
                pdSub.FAdjQty = "0";
                pdSub.FCheckQty = edPdnum.getText().toString();
                pdSub.FItemID = pdsubChoice.FItemID;
                pdSub.FStockPlaceID = wavehouseID;
                pdSub.FBatchNo = pihao;
                pdSub.FID = pdMain.FID;
                pdSub.FStockID = storageId;
                pdSub.FName = pdsubChoice.FName;
                pdSubDao.insert(pdSub);
                ResetAll();
            }
        } else {
            lockScan(0);
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "添加失败");
        }
    }

    private void upload() {
//        String main = "";
        ArrayList<String> container = new ArrayList<>();
        ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data = new ArrayList<>();
        final List<T_Detail> details = DataModel.getPdUpload(mContext,activity+"");
        for (int i = 0; i < details.size(); i++) {
            container = new ArrayList<>();
            String main = "";
            main =details.get(i).FInterID + "|" +
                    details.get(i).MakerId + "|" +
                    details.get(i).FRate + "|" +
                    details.get(i).IMIE ;
            container.add(main);
            uploadBean pBean = new uploadBean();
            pBean.items = container;
//        DataModel.upload(mContext, getBaseUrl() + WebApi.PDUPLOAD, gson.toJson(pBean));
//        postToServer(container);
            final int finalI = i;
            App.getRService().doIOAction(WebApi.PD2UPLOAD, gson.toJson(pBean), new MySubscribe<CommonResponse>() {
                @Override
                public void onNext(CommonResponse commonResponse) {
                    super.onNext(commonResponse);
                    if (commonResponse.state){
                        t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(
                                T_DetailDao.Properties.FInterID.eq(details.get(finalI).FInterID),
                                T_DetailDao.Properties.Activity.eq(activity)
                        ).build().list());
                    }
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Upload_Error,e.toString()));
                }
            });
        }
        ordercode++;
        share.setOrderCode(PD42Activity.this,ordercode);
        btnBackorder.setClickable(true);
        LoadingUtil.dismiss();
        MediaPlayer.getInstance(mContext).ok();
        lockScan(0);

//        List<T_Detail> details = t_detailDao.queryBuilder().where(
//                T_DetailDao.Properties.Activity.eq(activity)
//        ).build().list();

//        for (int i = 0; i < details.size(); i++) {
//            if (i != 0 && (i + 1) % 50 == 0) {
//                T_Detail t_main = details.get(i);
//                main += t_main.FInterID + "|" +
//                        t_main.MakerId + "|" +
//                        t_main.FRate + "|" +
//                        t_main.IMIE + "|";
//                container.add(main);
//                main = "";
//            } else {
//                T_Detail t_main = details.get(i);
//                main += t_main.FInterID + "|" +
//                        t_main.MakerId + "|" +
//                        t_main.FRate + "|" +
//                        t_main.IMIE + "|";
//            }
//
//        }
//        if (main.length() > 0) {
//            main = main.subSequence(0, main.length() - 1).toString();
//        }
//        container.add(main);
//        uploadBean pBean = new uploadBean();
//        pBean.items = container;
//        DataModel.upload(mContext, getBaseUrl() + WebApi.PDUPLOAD, gson.toJson(pBean));
//        postToServer(container);
    }

    private void postToServer(ArrayList<String> data) {
        uploadBean pBean = new uploadBean();
        pBean.items = data;
        Gson gson = new Gson();
        Asynchttp.post(mContext, getBaseUrl() + WebApi.PDUPLOAD, gson.toJson(pBean), new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                MediaPlayer.getInstance(mContext).ok();
                Toast.showText(mContext, "上传成功");
                List<T_Detail> list = t_detailDao.queryBuilder().where(T_DetailDao.Properties.Activity.eq(activity)).build().list();
                for (int i = 0; i < list.size(); i++) {
                    t_detailDao.delete(list.get(i));
                }
                btnBackorder.setClickable(true);
                LoadingUtil.dismiss();
            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
                MediaPlayer.getInstance(mContext).error();
                btnBackorder.setClickable(true);
                Toast.showText(mContext, Msg);
                LoadingUtil.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("code", requestCode + "" + "    " + resultCode);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                String message = b.getString("result");
                edCode.setText(message);
//                Toast.showText(mContext, message);
                OnReceive(message);
                edCode.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
            }
        }
    }

//    private void setDATA(String fnumber, boolean flag) {
////        Log.e(TAG,"setDATA--product:"+product.toString()+" flag:"+flag);
//        default_unitID = null;
////        edPihao.setText("");
//        edCode.setText(fnumber);
//        if (flag) {
////            default_unitID = product.FUnitID;
//            tvorIsAuto(product);
//        } else {
//            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
//                App.getRService().getProductForId(codeCheckBackDataBean.FItemID, new MySubscribe<CommonResponse>() {
//                    @Override
//                    public void onNext(CommonResponse commonResponse) {
//                        LoadingUtil.dismiss();
//                        final DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
//                        if (dBean.products.size() > 0) {
//                            product = dBean.products.get(0);
//                            Lg.e("获得物料：" + product.toString());
//                            getProductOL(dBean, 0);
//                        } else {
//                            lockScan(0);
//                            Toast.showText(mContext, "未找到物料信息");
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LoadingUtil.dismiss();
//                        lockScan(0);
//                        Toast.showText(mContext, "物料信息获取失败" + e.toString());
//                    }
//                });
////                Asynchttp.post(mContext, getBaseUrl() + WebApi.SEARCHPRODUCTS, fnumber, new Asynchttp.Response() {
////                    @Override
////                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
////                        final DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
////                        if (dBean.products.size() == 1) {
////                            getProductOL(dBean, 0);
////                            default_unitID = dBean.products.get(0).FUnitID;
////                            chooseUnit(default_unitID);
////                        } else if (dBean.products.size() > 1) {
////                            AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
////                            ab.setTitle("请选择物料");
////                            View v = LayoutInflater.from(mContext).inflate(R.layout.pd_alert, null);
////                            ListView lv = v.findViewById(R.id.lv_alert);
////                            productselectAdapter1 = new ProductselectAdapter1(mContext, dBean.products);
////                            lv.setAdapter(productselectAdapter);
////                            ab.setView(v);
////                            final AlertDialog alertDialog = ab.create();
////                            alertDialog.show();
////                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                                @Override
////                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                                    getProductOL(dBean, i);
////                                    default_unitID = dBean.products.get(i).FUnitID;
////                                    chooseUnit(default_unitID);
////                                    alertDialog.dismiss();
////                                }
////                            });
////                        }
////                    }
////
////                    @Override
////                    public void onFailed(String Msg, AsyncHttpClient client) {
////                        Toast.showText(mContext, Msg);
////                    }
////                });
//            } else {
////                final ProductDao productDao = daoSession.getProductDao();
////                BarCodeDao barCodeDao = daoSession.getBarCodeDao();
////                final List<BarCode> barCodes = barCodeDao.queryBuilder().where(BarCodeDao.Properties.FBarCode.eq(fnumber)).build().list();
////                if (barCodes.size() > 0) {
////                    if (barCodes.size() == 1) {
////                        products = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(barCodes.get(0).FItemID)).build().list();
////                        default_unitID = barCodes.get(0).FUnitID;
////                        product = products.get(0);
////                        tvorIsAuto(product);
////
////                    } else {
////                        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
////                        ab.setTitle("请选择物料");
////                        View v = LayoutInflater.from(mContext).inflate(R.layout.pd_alert, null);
////                        ListView lv = v.findViewById(R.id.lv_alert);
////                        productselectAdapter = new ProductselectAdapter(mContext, barCodes);
////                        lv.setAdapter(productselectAdapter);
////                        ab.setView(v);
////                        final AlertDialog alertDialog = ab.create();
////                        alertDialog.show();
////                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                            @Override
////                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                                BarCode barCode = (BarCode) productselectAdapter.getItem(i);
////                                products = productDao.queryBuilder().where(
////                                        ProductDao.Properties.FItemID.eq(barCode.FItemID)
////                                ).build().list();
////                                default_unitID = barCode.FUnitID;
////                                chooseUnit(default_unitID);
////                                product = products.get(0);
////                                tvorIsAuto(product);
////                                alertDialog.dismiss();
////                            }
////                        });
////
////                    }
////                } else {
////                    Toast.showText(mContext, "未找到物料");
////                    MediaPlayer.getInstance(mContext).error();
////                    edCode.setText("");
////                }
//            }
//
//        }
//
//    }

//    //定位到指定单位
//    private void chooseUnit(final String unitId){
//        if (unitId != null && !"".equals(unitId)) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    for (int i = 0; i < unitAdapter.getCount(); i++) {
//                        if (unitId.equals(((Unit) unitAdapter.getItem(i)).FMeasureUnitID)) {
//                            spUnit.setSelection(i);
//                            Log.e(TAG,"定位了单位："+((Unit) unitAdapter.getItem(i)).toString());
//                        }
//                    }
//                }
//            }, 100);
//        }
//    }


    private void tvorIsAuto(final Product product) {
        if (codeCheckBackDataBean == null) {
            lockScan(0);
            return;
        }
        edCode.setText(product.FNumber);
        tvGoodName.setText(product.FName);
        pdSubDao = daoSession.getPDSubDao();
        wavehouseAutoString = product.FSPID;
        if ((product.FBatchManager) != null && (product.FBatchManager).equals("1")) {
            fBatchManager = true;
//            setfocus(edPihao);
//            setfocus(edPihao);
//            edPihao.setEnabled(true);
        } else {
//            edPihao.setEnabled(false);
            fBatchManager = false;
        }

        spUnit.setAuto(mContext, product.FUnitGroupID, codeCheckBackDataBean.FUnitID, SpinnerUnit.Id);
//        unitAdapter = CommonMethod.getMethod(mContext).getUnitAdapter(product.FUnitGroupID, spUnit);
//        chooseUnit(default_unitID);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < unitAdapter.getCount(); i++) {
//                    if (default_unitID.equals(((Unit) unitAdapter.getItem(i)).FMeasureUnitID)) {
//                        spUnit.setSelection(i);
//                    }
//                }
//            }
//        }, 100);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fid != null) {
                    final List<PDSub> pdSubs = pdSubDao.queryBuilder().where(
                            PDSubDao.Properties.FID.eq(fid),
                            PDSubDao.Properties.FItemID.eq(product.FItemID),
                            PDSubDao.Properties.FStockID.eq(storageId)
                    ).build().list();
                    if (pdSubs.size() > 0) {
//                        if (pdSubs.size() == 1) {
                        pdsubChoice = pdSubs.get(0);
//                            getPici();
//                            edPihao.setText(pdsubChoice.FBatchNo);
                        tvYtnum.setText(pdsubChoice.FCheckQty);
                        tvNumOnServer.setText(pdsubChoice.FQty);
                        if (isAuto) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                                        edPdnum.setText("1.0");
                                    AddOrder();
                                }
                            }, 100);

                        } else {
                            lockScan(0);
                        }
//                        } else {
//
//                            ////////////////////////////////////////////////////////////////
//                            AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
//                            ab.setTitle("请选择盘点明细");
//                            View v = LayoutInflater.from(mContext).inflate(R.layout.pd_alert, null);
//                            ListView lv = v.findViewById(R.id.lv_alert);
//                            AlertLvAdapter alertLvAdapter = new AlertLvAdapter(mContext, pdSubs);
//                            lv.setAdapter(alertLvAdapter);
//                            ab.setView(v);
//                            final AlertDialog alertDialog = ab.create();
//                            alertDialog.show();
//                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                    pdsubChoice = pdSubs.get(i);
////                                    edPihao.setText(pdsubChoice.FBatchNo);
//                                    for (int j = 0; j < storageAdapter.getCount(); j++) {
//                                        Storage storage = (Storage) storageAdapter.getItem(j);
//                                        if (storage.FItemID.equals(pdsubChoice.FStockID)) {
//                                            spStorage.setSelection(j);
//                                        }
//                                    }
//                                    if (!pdsubChoice.FStockPlaceID.equals("0")) {
//                                        for (int j = 0; j < waveHouseAdapter.getCount(); j++) {
//                                            WaveHouse waveHouse = (WaveHouse) waveHouseAdapter.getItem(j);
//                                            if (waveHouse.FSPID.equals(pdsubChoice.FStockPlaceID)) {
//                                                spWavehouse.setSelection(j);
//                                            }
//                                        }
//                                    }
//                                    tvYtnum.setText(pdsubChoice.FCheckQty);
//                                    tvNumOnServer.setText(pdsubChoice.FQty);
//                                    if (isAuto) {
//                                        edPdnum.setText("1.0");
//                                        AddOrder();
//                                    }
//                                    alertDialog.dismiss();
//                                }
//                            });
//                        }


                    } else {
                        lockScan(0);
                        Toast.showText(mContext, "未找到盘点明细");
//                        if (isAuto && fid != null) {
//                            edPdnum.setText("1.0");
//                            AddOrder();
//                        }
                    }
                } else {
                    lockScan(0);
                    Toast.showText(mContext, "请下载盘点方案");
                }

            }
        }, 100);

    }

//    private void getProductOL(DownloadReturnBean dBean, int j) {
//        product = dBean.products.get(j);
//        tvorIsAuto(product);
////        boolean isAuto = false;
////        if (isAuto) {
////            new Handler().postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    edPdnum.setText("1.0");
////                    AddOrder();
////                }
////            }, 150);
////
////        }
//    }

    private void download(final String Json, final boolean downloadall) {
        LoadingUtil.show(mContext, "请稍后...");
        App.getRService().doIOAction(WebApi.PDSUBLIST, Json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                super.onNext(commonResponse);
                if (!commonResponse.state)return;
                PDsubReturnBean pBean = gson.fromJson(commonResponse.returnJson, PDsubReturnBean.class);
                for (int i = 0; i < pBean.items.size(); i++) {
                    List<PDSub> list = pdSubDao.queryBuilder().where(
                            PDSubDao.Properties.FID.eq(pBean.items.get(i).FID),
                            PDSubDao.Properties.FBarCode.eq(pBean.items.get(i).FBarCode)
                    ).build().list();
                    if (list.size() == 0) {
                        pdSubDao.insertInTx(pBean.items.get(i));
//                        pdSubDao.deleteInTx(list);
//                        long insert = pdSubDao.insert(pBean.items.get(i));
                    } else {
                        List<T_Detail> details = t_detailDao.queryBuilder().where(
                                T_DetailDao.Properties.Activity.eq(activity),
                                T_DetailDao.Properties.FInterID.eq(pBean.items.get(i).FID)
                        ).build().list();
                        if (details.size() > 0) {

                        } else {
                            pdSubDao.deleteInTx(list);
                            long insert = pdSubDao.insert(pBean.items.get(i));
                        }
                    }


//                    Lg.e("本地TDetail"+list.size());
//                        List<PDSub> subs = pdSubDao.queryBuilder().where(
//                                PDSubDao.Properties.FID.eq(pBean.items.get(i).FID),
//                                PDSubDao.Properties.FItemID.eq(pBean.items.get(i).FItemID)
//                        ).build().list();
//                        if (subs.size()>0){
//
//                        }else{
//                            pdSubDao.deleteInTx(pBean.items.get(i));
//                            pdSubDao.insertInTx(pBean.items.get(i));
//                        }

//                    }
//                    List<PDSub> list = pdSubDao.queryBuilder().where(
//                            PDSubDao.Properties.FID.eq(pBean.items.get(i).FID),
//                            PDSubDao.Properties.FStockPlaceID.eq(pBean.items.get(i).FStockPlaceID),
//                            PDSubDao.Properties.FStockID.eq(pBean.items.get(i).FStockID),
//                            PDSubDao.Properties.FBatchNo.eq(pBean.items.get(i).FBatchNo),
//                            PDSubDao.Properties.FItemID.eq(pBean.items.get(i).FItemID)
//                    ).build().list();
//                    if (list.size() == 0) {
//                        pdSubDao.deleteInTx(list);
//                        long insert = pdSubDao.insert(pBean.items.get(i));
//                    } else {
//                        pdSubDao.deleteInTx(list);
//                        long insert = pdSubDao.insert(pBean.items.get(i));
//                    }
                }
//                PDMainDao pdMainDao = daoSession.getPDMainDao();
//                if (downloadall) {
//                    pdMainDao.insertOrReplaceInTx(mainContainer);
////                    for (int i = 0; i < mainContainer.size(); i++) {
////                        PDMain p = pdMainDao.queryBuilder().where(PDMainDao.Properties.FID.eq(mainContainer.get(i).FID)).build().unique();
////                        if (p != null) {
////                            pdMainDao.delete(p);
////                        }
////                        try {
////                            pdMainDao.insert(mainContainer.get(i));
////                        } catch (SQLiteConstraintException e) {
////                            continue;
////                        }
////                    }
//                } else {
//                    for (int i = 0; i < mainContainer.size(); i++) {
//                        for (int j = 0; j < choice.size(); j++) {
//                            if (choice.get(j).equals(mainContainer.get(i).FID)) {
//                                pdMainDao.insertOrReplaceInTx(mainContainer.get(i));
////                                PDMain p = pdMainDao.queryBuilder().where(PDMainDao.Properties.FID.eq(mainContainer.get(i).FID)).build().unique();
////                                if (p != null) {
////                                    pdMainDao.delete(p);
////                                }
////                                try {
////                                    pdMainDao.insert(mainContainer.get(i));
////                                } catch (SQLiteConstraintException e) {
////                                    continue;
////                                }
//                            }
//                        }
//                    }
//                }
                if (downloadall){
                    pdMainDao.deleteAll();
                    pdMainDao.insertInTx(mainContainer);
                }else{
                    for (int j = 0; j < mainContainer.size(); j++) {
                        for (int k = 0; k < choice.size(); k++) {
                            if (choice.get(k).equals(mainContainer.get(j).FID)) {
                                pdMainDao.deleteInTx(pdMainDao.queryBuilder().where(PDMainDao.Properties.FID.eq(choice.get(k))).build().list());
                                pdMainDao.insertInTx(mainContainer.get(j));
                            }
                        }
                    }
                }
                lockScan(0);
                Toast.showText(mContext, "下载完成");
                pdMainSpAdapter = method.getpdmain(spPdplan);
                LoadingUtil.dismiss();
            }

            @Override
            public void onError(Throwable e) {
//                super.onError(e);
                lockScan(0);
                Toast.showText(mContext, e.getMessage());
                LoadingUtil.dismiss();
            }
        });
//        Asynchttp.post(mContext, getBaseUrl() + WebApi.PDSUBLIST, Json, new Asynchttp.Response() {
//            @Override
//            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                PDsubReturnBean pBean = gson.fromJson(cBean.returnJson, PDsubReturnBean.class);
//                PDSubDao pdSubDao = daoSession.getPDSubDao();
//                for (int i = 0; i < pBean.items.size(); i++) {
//                    List<PDSub> list = pdSubDao.queryBuilder().where(
//                            PDSubDao.Properties.FID.eq(pBean.items.get(i).FID),
//                            PDSubDao.Properties.FStockPlaceID.eq(pBean.items.get(i).FStockPlaceID),
//                            PDSubDao.Properties.FStockID.eq(pBean.items.get(i).FStockID),
//                            PDSubDao.Properties.FBatchNo.eq(pBean.items.get(i).FBatchNo),
//                            PDSubDao.Properties.FItemID.eq(pBean.items.get(i).FItemID)
//                    ).build().list();
//                    if (list.size() == 0) {
//                        pdSubDao.deleteInTx(list);
//                        long insert = pdSubDao.insert(pBean.items.get(i));
//                    } else {
//                        pdSubDao.deleteInTx(list);
//                        long insert = pdSubDao.insert(pBean.items.get(i));
//                    }
//                }
//                PDMainDao pdMainDao = daoSession.getPDMainDao();
//                if (downloadall) {
//                    pdMainDao.insertOrReplaceInTx(mainContainer);
////                    for (int i = 0; i < mainContainer.size(); i++) {
////                        PDMain p = pdMainDao.queryBuilder().where(PDMainDao.Properties.FID.eq(mainContainer.get(i).FID)).build().unique();
////                        if (p != null) {
////                            pdMainDao.delete(p);
////                        }
////                        try {
////                            pdMainDao.insert(mainContainer.get(i));
////                        } catch (SQLiteConstraintException e) {
////                            continue;
////                        }
////                    }
//                } else {
//                    for (int i = 0; i < mainContainer.size(); i++) {
//                        for (int j = 0; j < choice.size(); j++) {
//                            if (choice.get(j).equals(mainContainer.get(i).FID)) {
//                                pdMainDao.insertOrReplaceInTx(mainContainer.get(i));
////                                PDMain p = pdMainDao.queryBuilder().where(PDMainDao.Properties.FID.eq(mainContainer.get(i).FID)).build().unique();
////                                if (p != null) {
////                                    pdMainDao.delete(p);
////                                }
////                                try {
////                                    pdMainDao.insert(mainContainer.get(i));
////                                } catch (SQLiteConstraintException e) {
////                                    continue;
////                                }
//                            }
//                        }
//                    }
//                }
//
//
//                lockScan(0);
//                Toast.showText(mContext, "下载完成");
//                pdMainSpAdapter = method.getpdmain(spPdplan);
//                LoadingUtil.dismiss();
//
//            }
//
//            @Override
//            public void onFailed(String Msg, AsyncHttpClient client) {
//                lockScan(0);
//                Toast.showText(mContext, Msg);
//                LoadingUtil.dismiss();
//            }
//        });
    }

    private void ResetAll() {
        lockScan(0);
//        product = null;
        tvModel.setText("");
        tvGoodName.setText("");
        tvNumOnServer.setText("");
        tvYtnum.setText("");
//        edPihao.setText("");
//        List<PDSub> container = new ArrayList<>();
//        piciSpAdapter = new PiciSpForSubAdapter(mContext, container);
//        spPihao.setAdapter(piciSpAdapter);
        edCode.setText("");
        edPdnum.setText("");
    }

    private class uploadBean {
        public ArrayList<String> items;
    }

    //用于adpater首次更新时，不存入默认值，而是选中之前的选项
    private boolean isFirst = false;
    private boolean isFirst2 = false;
    private boolean isFirst3 = false;
    private boolean isFirst4 = false;
    private boolean isFirst5 = false;
    private boolean isFirst6 = false;
    private boolean isFirst7 = false;
}

