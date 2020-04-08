package com.fangzuo.assist.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
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
import com.fangzuo.assist.Adapter.EmployeeSpAdapter;
import com.fangzuo.assist.Adapter.PayMethodSpAdapter;
import com.fangzuo.assist.Adapter.PushDownSubListAdapter;
import com.fangzuo.assist.Adapter.StorageSpAdapter;
import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Adapter.WaveHouseSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.GetBatchNoBean;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.BarCode;
import com.fangzuo.assist.Dao.Employee;
import com.fangzuo.assist.Dao.InStorageNum;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.PurchaseMethod;
import com.fangzuo.assist.Dao.PushDownMain;
import com.fangzuo.assist.Dao.PushDownSub;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.Dao.Unit;
import com.fangzuo.assist.Dao.Wanglaikemu;
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
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.MediaPlayer;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.assist.widget.MyWaveHouseSpinner;
import com.fangzuo.assist.widget.SpinnerGProduct;
import com.fangzuo.assist.widget.SpinnerStorage;
import com.fangzuo.assist.widget.SpinnerUnit;
import com.fangzuo.greendao.gen.BarCodeDao;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.InStorageNumDao;
import com.fangzuo.greendao.gen.ProductDao;
import com.fangzuo.greendao.gen.PushDownMainDao;
import com.fangzuo.greendao.gen.PushDownSubDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.fangzuo.greendao.gen.UnitDao;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PdProductGetCheckActivity extends BaseActivity {
    private int tag =29;
    private int activity = Config.PdProductGetCheckActivity;

    @BindView(R.id.lv_pushsub)
    ListView lvPushsub;
    @BindView(R.id.sp_storage)
    SpinnerStorage spStorage;
    @BindView(R.id.sp_wavehouse)
    MyWaveHouseSpinner spWavehouse;
    @BindView(R.id.productName)
    TextView productName;
    @BindView(R.id.tv_model)
    TextView tvModel;
    @BindView(R.id.sp_unit)
    SpinnerUnit spUnit;
    @BindView(R.id.sp_batchNo)
    Spinner spBatchNo;
    @BindView(R.id.ed_num)
    EditText edNum;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.btn_backorder)
    Button btnBackorder;
    @BindView(R.id.btn_checkorder)
    Button btnCheckorder;
//    @BindView(R.id.cb_isStorage)
//    CheckBox cbIsStorage;
    @BindView(R.id.cb_isAuto)
    CheckBox cbIsAuto;
//    @BindView(R.id.tv_date)
//    TextView tvDate;
//    @BindView(R.id.tv_date_pay)
//    TextView tvDatePay;
//    @BindView(R.id.sp_purchaseMethod)
//    Spinner spPurchaseMethod;
//    @BindView(R.id.sp_capture_person)
//    Spinner spCapturePerson;
//    @BindView(R.id.sp_sign_person)
//    Spinner spSignPerson;
//    @BindView(R.id.sp_manager)
//    Spinner spManager;
//    @BindView(R.id.sp_wanglaikemu)
//    Spinner spWanglaikemu;
//    @BindView(R.id.ed_zhaiyao)
//    EditText edZhaiyao;
//    @BindView(R.id.sp_capture)
//    Spinner spCapture;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.ed_pihao)
    EditText edPihao;
//    @BindView(R.id.sp_gproduct)
//    SpinnerGProduct spGproduct;
    private DaoSession daosession;
    private CommonMethod method;
//    private int year;
//    private int month;
//    private int day;
    private ArrayList<PushDownSub> container;
    private ArrayList<String> fidcontainer;
//    private StorageSpAdapter storageSpinner;
//    private EmployeeSpAdapter employeeAdapter;
    private PushDownSubDao pushDownSubDao;
    private PushDownMainDao pushDownMainDao;
    private String fwanglaiUnit;
    private String employeeId;
    private String departmentId;
    private PushDownSubListAdapter pushDownSubListAdapter;
//    private PayMethodSpAdapter payMethodSpAdapter;
//    private EmployeeSpAdapter employeeSpAdapter;
    private List<Wanglaikemu> wanglaikemuList;
//    private boolean isGetDefaultStorage;
//    private boolean isAuto;
    private PushDownSub pushDownSub;
    private List<Product> products;
    private String ManagerId;
    private String ManagerName;
    private String productID;
    private List<Storage> storages;
    private String fid;
    private String fentryid;
    private String fprice;
    private UnitSpAdapter unitAdapter;
    private String storageID="";
    private String storageName="";
    private WaveHouseSpAdapter waveHouseAdapter;
    private String unitId;
    private String unitName;
    private double unitrate;
    private double unitrateSub;
    private String waveHouseID;
    private String waveHouseName;
    private String batchNo="";
    private String wanglaikemuId;
    private String wanglaikemuName;
    private String datePay;
    private String date;
    private T_mainDao t_mainDao;
    private T_DetailDao t_detailDao;
    private BatchNoSpAdapter batchNoSpAdapter;
    private String departmentName;
    private String employeeName;
    private Product product;
    private String capturePersonId;
    private String capyurePersonName;
    private String signPersonId;
    private String signPersonName;
    private String PurchaseMethodId;
    private String purchaseMethodName;
    private String captureID;
    private String captureName;
    private String billNo;
    private ArrayList<String> detailContainer;
    private ArrayList<String> fidc;
    private boolean fBatchManager;
    private String default_unitID;
    private boolean fromScan=false;
    private String wavehouseAutoString="";
    private Storage storage;
    private Long ordercode;
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg){
//            case EventBusInfoCode.PRODUCTRETURN:
////                product = (Product)event.postEvent;
////                setDATA("", true);
////                break;

            case EventBusInfoCode.Upload_OK://回单成功
                t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(
                        T_DetailDao.Properties.Activity.eq(activity)
                ).build().list());
                t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(
                        T_mainDao.Properties.Activity.eq(activity)
                ).build().list());
                for (int i = 0; i < fidc.size(); i++) {
                    List<PushDownSub> pushDownSubs = pushDownSubDao.queryBuilder().where(
                            PushDownSubDao.Properties.FInterID.eq(fidc.get(i))
                    ).build().list();
                    pushDownSubDao.deleteInTx(pushDownSubs);
                    List<PushDownMain> pushDownMains = pushDownMainDao.queryBuilder().where(
                            PushDownMainDao.Properties.FInterID.eq(fidc.get(i))
                    ).build().list();
                    pushDownMainDao.deleteInTx(pushDownMains);
                }
                MediaPlayer.getInstance(mContext).ok();
                btnBackorder.setClickable(true);
                LoadingUtil.dismiss();
                Bundle b = new Bundle();
                b.putInt("123",tag);
                startNewActivity(PushDownPagerActivity.class,0,0,true,b);

//                btnBackorder.setClickable(true);
//                LoadingUtil.dismiss();
                break;
            case EventBusInfoCode.Upload_Error://回单失败
                String error = (String)event.postEvent;
                Toast.showText(mContext, error);
                btnBackorder.setClickable(true);
                LoadingUtil.dismiss();
                MediaPlayer.getInstance(mContext).error();
                break;
        }
    }
    @Override
    protected void initView() {
        setContentView(R.layout.activity_pd_product_get_check);
        ButterKnife.bind(this);
        mContext = this;
        daosession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        method = CommonMethod.getMethod(mContext);
//        year = Calendar.getInstance().get(Calendar.YEAR);
//        month = Calendar.getInstance().get(Calendar.MONTH);
//        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        cbIsAuto.setChecked(Hawk.get(Config.autoAdd+activity,false));
//        isAuto = share.getSLTZisAuto();
        edPihao.setEnabled(false);
    }

    @Override
    protected void initData() {
        LoadBasicData();
        container = new ArrayList<>();
        fidcontainer = getIntent().getExtras().getStringArrayList("fid");
        getList();
        List<PushDownMain> list1 = pushDownMainDao.queryBuilder().where(PushDownMainDao.Properties.FInterID.eq(fidcontainer.get(0))).build().list();
        if (list1.size() > 0) {
            fwanglaiUnit = list1.get(0).FSupplyID;
            employeeId = list1.get(0).FEmpID;
            departmentId = list1.get(0).FDeptID;
            billNo = list1.get(0).FBillNo;
        }
        ordercode = DataModel.findOrderCode(mContext,activity,fidcontainer);
    }

    private void getList() {
        container = new ArrayList<>();
        pushDownSubDao = daosession.getPushDownSubDao();
        pushDownMainDao = daosession.getPushDownMainDao();
        for (int i = 0; i < fidcontainer.size(); i++) {
            QueryBuilder<PushDownSub> qb = pushDownSubDao.queryBuilder();
            List<PushDownSub> list = qb.where(
                    PushDownSubDao.Properties.Tag.eq(tag),
                    PushDownSubDao.Properties.FInterID.eq(fidcontainer.get(i))).build().list();
            container.addAll(list);
        }
        if (container.size() > 0) {
            pushDownSubListAdapter = new PushDownSubListAdapter(mContext, container);
            lvPushsub.setAdapter(pushDownSubListAdapter);
            pushDownSubListAdapter.notifyDataSetChanged();
        } else {
            Toast.showText(mContext, "未查询到数据");
        }
    }

    private void LoadBasicData() {
//        tvDate.setText(getTime(true));
//        tvDatePay.setText(getTime(true));
        spStorage.setAutoSelection(Config.Storage+activity,"");
//        storageSpinner = method.getStorageSpinner(spStorage);
//        payMethodSpAdapter = CommonMethod.getMethod(mContext).getPayMethodSpinner(spPurchaseMethod);
//        employeeSpAdapter = CommonMethod.getMethod(mContext).getEmployeeAdapter(spCapturePerson);
//        wanglaikemuList = CommonMethod.getMethod(mContext).getwlkmAdapter(spWanglaikemu);
//        spSignPerson.setAdapter(employeeSpAdapter);
//        spManager.setAdapter(employeeSpAdapter);
//        employeeSpAdapter.notifyDataSetChanged();
//        employeeAdapter = method.getEmployeeAdapter(spManager);
//        spCapture.setAdapter(employeeAdapter);
//        spGproduct.setAutoSelection(Config.People5 + activity, "");


//        spPurchaseMethod.setSelection(share.getSLTZpurchaseMethod());
//        spCapturePerson.setSelection(share.getSLTZcapturePerson());
//        spSignPerson.setSelection(share.getSLTZSignPerson());
//        spWanglaikemu.setSelection(share.getSLTZZwlkm());
//        spManager.setSelection(share.getSLTZZManager());

    }

    @Override
    protected void initListener() {
        cbIsAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Hawk.put(Config.autoAdd+activity,b);
            }
        });
//        cbIsStorage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                isGetDefaultStorage = b;
//            }
//        });

        //批号输入监听
        edPihao.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.e(TAG,"批号监听。。。");
                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                    getInstorageNum(product);
                    setfocus(edPihao);
                }
                return true;
            }
        });

//
//        spPurchaseMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                PurchaseMethod item = payMethodSpAdapter.getChild(i);
//                PurchaseMethodId = item.FItemID;
//                purchaseMethodName = item.FName;
////                share.setSLTZpurchaseMethod(i);
//                if (isFirst2){
//                    share.setSLTZpurchaseMethod(i);
//                    spPurchaseMethod.setSelection(i);
//                }
//                else{
//                    spPurchaseMethod.setSelection(share.getSLTZpurchaseMethod());
//                    isFirst2=true;
//                }
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
                if (unit != null) {
                    unitId = unit.FMeasureUnitID;
                    unitName = unit.FName;
                    unitrate = MathUtil.toD(unit.FCoefficient);
                    Log.e("1111", unitrate + "");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spStorage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                storage = (Storage) spStorage.getAdapter().getItem(i);
                waveHouseID="0";
                storageID = storage.FItemID;
                storageName = storage.FName;
//                waveHouseAdapter = CommonMethod.getMethod(mContext).getWaveHouseAdapter(storage, spWavehouse);
                spWavehouse.setAuto(mContext,storage,wavehouseAutoString);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spWavehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WaveHouse waveHouse = (WaveHouse) spWavehouse.getAdapter().getItem(i);
                waveHouseID = waveHouse.FSPID;
                waveHouseName = waveHouse.FName;
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
                ProductDao productDao = daosession.getProductDao();
                if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                    Asynchttp.post(mContext, getBaseUrl() + WebApi.PRPDUCTSEARCHWHERE, pushDownSub.FItemID, new Asynchttp.Response() {
                        @Override
                        public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                            final DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                            Log.e("product.size", dBean.products.size() + "");
                            if (dBean.products.size() > 0) {
                                product = dBean.products.get(0);
                                Log.e("product.size", product + "");
                                clickList(product);
                            }else{

                                lockScan(0);
                            }
                        }

                        @Override
                        public void onFailed(String Msg, AsyncHttpClient client) {
                            Toast.showText(mContext, Msg);
                            lockScan(0);
                        }
                    });
                } else {
                    products = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(pushDownSub.FItemID)).build().list();
                    if (products.size() > 0) {
                        product = products.get(0);
                        clickList(product);
                    }else{

                        lockScan(0);
                    }
                }

            }
        });
//防止ScrollView与ListView滑动冲突
//        lvPushsub.setOnTouchListener(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                    scrollView.requestDisallowInterceptTouchEvent(true);
//                }
//                return false;
//            }
//        });

//        spBatchNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                InStorageNum inStorageNum = (InStorageNum) batchNoSpAdapter.getItem(i);
//                batchNo = inStorageNum.FBatchNo;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
    }
    //获取明细里面的单位的换算率
    private void getUnitrateSub(PushDownSub pushDownSub){
        UnitDao unitDao = daosession.getUnitDao();
        List<Unit> units = unitDao.queryBuilder().where(
                UnitDao.Properties.FMeasureUnitID.eq(pushDownSub.FUnitID)
        ).build().list();
        if (units.size()>0){
            unitrateSub=MathUtil.toD(units.get(0).FCoefficient);
            Lg.e("获得明细换算率："+unitrateSub);
        }else{
            unitrateSub=1;
            Lg.e("获得明细换算率失败："+unitrateSub);
        }
    }
    private void clickList(final Product product) {
        productName.setText(product.FName);
        tvModel.setText(product.FModel);
        productID = pushDownSub.FItemID;
//        wavehouseAutoString=product.FSPID;
        if ((product.FBatchManager) != null && (product.FBatchManager).equals("1")) {
            fBatchManager = true;
            spBatchNo.setEnabled(true);
//            edPihao.setEnabled(true);
            edPihao.setHint("请输入批次");
        } else {
            edPihao.setHint("未开启批次");
            edPihao.setText("");
            edPihao.setEnabled(false);
//            spBatchNo.setEnabled(false);
            fBatchManager = false;
        }
//        if (isGetDefaultStorage) {
//            for (int j = 0; j < storageSpinner.getCount(); j++) {
//                if (((Storage)storageSpinner.getItem(j)).FItemID.equals(product.FDefaultLoc)) {
//                    spStorage.setSelection(j);
//                    break;
//                }
//            }
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    spWavehouse.setAuto(mContext,storage,wavehouseAutoString);
//
////                    for (int j = 0; j < waveHouseAdapter.getCount(); j++) {
////                        if (((WaveHouse)waveHouseAdapter.getItem(j)).FSPID.equals(product.FSPID)) {
////                            spWavehouse.setSelection(j);
////                            break;
////                        }
////                    }
//                }
//            },50);
//        }
        fid = pushDownSub.FInterID;
        fentryid = pushDownSub.FEntryID;
        fprice = pushDownSub.FAuxPrice;
        spUnit.setAuto(mContext, product.FUnitGroupID,product.FUnitID, SpinnerUnit.Id);
//        unitAdapter = CommonMethod.getMethod(mContext).getUnitAdapter(product.FUnitGroupID, spUnit);
//
//        if (fromScan){
//            chooseUnit(default_unitID);
//        }else{
//            chooseUnit(pushDownSub.FUnitID);
//        }
//        fromScan = false;
//
//        getBatchNo();
        if (cbIsAuto.isChecked()) {
            if ("".equals(edNum.getText().toString()))edNum.setText("1.0");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Addorder();

                }
            }, 100);
        }else{
            lockScan(0);
        }
    }
//    private void chooseUnit(String str){
//        if (str!=null){
//            for(int i = 0;i<unitAdapter.getCount();i++){
//                if(((Unit)unitAdapter.getItem(i)).FMeasureUnitID.equals(str)){
//                    Lg.e("定位单位："+unitAdapter.getItem(i).toString());
//                    spUnit.setSelection(i);
//                }
//            }
//        }
//
//    }

    @Override
    protected void OnReceive(String code) {
        List<String> list = CommonUtil.ScanBack(code);
        if (list.size()>0){
            edNum.setText(list.get(3));
            edPihao.setText(list.get(1));
            ScanBarCode(list.get(0));
        }else{
            lockScan(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("resume", "resume");
        getList();
    }

    @OnClick({R.id.btn_add, R.id.btn_backorder, R.id.btn_checkorder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                Addorder();
                break;
            case R.id.btn_backorder:
                btnBackorder.setClickable(false);
                LoadingUtil.show(mContext,"正在回单...");
                upload();
                break;
            case R.id.btn_checkorder:
                Bundle b = new Bundle();
                b.putInt("activity", activity);
                startNewActivity(ReViewForCheckActivity.class, 0, 0, false, b);
                break;
        }
    }

    private void ScanBarCode(String barcode) {
        product = null;
        ProductDao productDao = daosession.getProductDao();
        BarCodeDao barCodeDao = daosession.getBarCodeDao();
        if (BasicShareUtil.getInstance(mContext).getIsOL()) {
            Asynchttp.post(mContext, getBaseUrl() + WebApi.SEARCHPRODUCTS, barcode, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    final DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                    Log.e("product.size", dBean.products.size() + "");
                    if (dBean.products.size() > 0) {
                        product = dBean.products.get(0);
                        Log.e("product.size", product + "");
                        default_unitID = dBean.products.get(0).FUnitID;
                        fromScan = true;
                        setProduct(product);
                    }else{
                        Toast.showText(mContext,"无法找到相关物料数据");
                        lockScan(0);
                    }
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Toast.showText(mContext, Msg);
                    lockScan(0);
                }
            });
        } else {
            final List<BarCode> barCodes = barCodeDao.queryBuilder().where(BarCodeDao.Properties.FBarCode.eq(barcode)).build().list();
            if (barCodes.size() > 0) {
                List<Product> products = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(barCodes.get(0).FItemID)).build().list();
                if (products != null && products.size() > 0) {
                    product = products.get(0);
                    default_unitID = barCodes.get(0).FUnitID;
                    fromScan = true;
                    setProduct(product);
                }else{
                    lockScan(0);
                }
            } else {
                lockScan(0);
                Toast.showText(mContext, "条码不存在");
                MediaPlayer.getInstance(mContext).error();
            }
        }

    }

    private void setProduct(Product product) {
        if (product != null) {
            boolean flag = true;
            boolean hasUnit = false;
            boolean errorStorage = false;
            boolean errorWave = false;
            boolean errorBatch = false;
            for (int j = 0; j < pushDownSubListAdapter.getCount(); j++) {
                PushDownSub pushDownSub1 = (PushDownSub) pushDownSubListAdapter.getItem(j);
                if (product.FItemID.equals(pushDownSub1.FItemID)) {
                    if (MathUtil.toD(pushDownSub1.FAuxQty) == MathUtil.toD(pushDownSub1.FQtying)) {
                        flag = true;
                        continue;
                    } else {
//                        for (int i = 0; i < pushDownSubListAdapter.getCount(); i++) {
//                            PushDownSub pushDownSub = (PushDownSub) pushDownSubListAdapter.getItem(i);
//                        if (!"".equals(default_unitID)){
//                            if (default_unitID.equals(pushDownSub1.FUnitID)){
//                                flag = false;
//                                hasUnit = true;
//                                lvPushsub.setSelection(j);
//                                lvPushsub.performItemClick(lvPushsub.getChildAt(j), j, lvPushsub.getItemIdAtPosition(j));
//                                break;
//                            }
//                        }else{
//                            if (storageID.equals(pushDownSub1.FDCStockID)){
//                                if (waveHouseID.equals(pushDownSub1.FDCSPID)){
                                    if (edPihao.getText().toString().equals(pushDownSub1.FBatchNo)){
                                        flag = false;
                                        hasUnit = true;
                                        lvPushsub.setSelection(j);
                                        lvPushsub.performItemClick(lvPushsub.getChildAt(j), j, lvPushsub.getItemIdAtPosition(j));
                                        break;
                                    }else{
                                        errorBatch = true;
                                    }
//                                }else{
//                                    errorWave = true;
//                                }
//                            }else{
//                                errorStorage = true;
//                            }

//                        }
//                        }
//                        if (!hasUnit){
//                            lvPushsub.setSelection(j);
//                            lvPushsub.performItemClick(lvPushsub.getChildAt(j), j, lvPushsub.getItemIdAtPosition(j));
//                        }

//                        break;
                    }

                }
            }

            if (flag) {
                lockScan(0);
//                if (errorStorage)Toast.showText(mContext, "商品不存在,请检查仓库是否正确");
//                if (errorWave)Toast.showText(mContext, "商品不存在,请检查仓位是否正确");
                if (errorBatch)Toast.showText(mContext, "商品不存在,请检查批号是否正确");
                MediaPlayer.getInstance(mContext).error();

            }
        } else {
            lockScan(0);
            Toast.showText(mContext, "列表中不存在商品");
        }
    }


    private void Addorder() {
        if(product!=null){
            batchNo=edPihao.getText().toString();

            String discount = "";
            String num = edNum.getText().toString();
            T_DetailDao t_detailDao = daosession.getT_DetailDao();
            T_mainDao t_mainDao = daosession.getT_mainDao();
            if (fBatchManager && "".equals(batchNo)){
                Toast.showText(mContext, "请输入批次");
                lockScan(0);
                return;
            }
            if (!pushDownSub.FBatchNo.equals(batchNo)){
                Toast.showText(mContext, "批次不符，请重新检查");
                lockScan(0);
                return;
            }

            if (edNum.getText().toString().equals("")||edNum.getText().toString().equals("0")) {
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
            if (MathUtil.toD(pushDownSub.FAuxQty) < ((MathUtil.toD(num)*unitrate)/unitrateSub + MathUtil.toD(pushDownSub.FQtying))) {
                MediaPlayer.getInstance(mContext).error();
                Toast.showText(mContext, "大兄弟,您的数量超过我的想象");
                lockScan(0);
                return;
            }
            LoadingUtil.showDialog(mContext,"正在添加...");


            boolean isHebing = true;
            if (isHebing) {
                List<T_Detail> detailhebing = t_detailDao.queryBuilder().where(
                        T_DetailDao.Properties.Activity.eq(activity),
                        T_DetailDao.Properties.FInterID.eq(fid),
//                        T_DetailDao.Properties.FUnitId.eq(unitId),
                        T_DetailDao.Properties.FProductId.eq(product.FItemID),
//                        T_DetailDao.Properties.FStorageId.eq(storageID),
//                        T_DetailDao.Properties.FPositionId.eq(waveHouseID==null?"0":waveHouseID),
                        T_DetailDao.Properties.FEntryID.eq(fentryid),
                        T_DetailDao.Properties.FBatch.eq(batchNo == null ? "" : batchNo)
                ).build().list();
                if (detailhebing.size() > 0) {
                    for (int i = 0; i < detailhebing.size(); i++) {
                        num = (MathUtil.toD(num) + MathUtil.toD(detailhebing.get(i).FCheckNum)) + "";
//                        List<T_main> t_mainList = t_mainDao.queryBuilder().where(T_mainDao.Properties.FIndex.eq(detailhebing.get(i).FIndex)).build().list();
//                        if (t_mainList.size() > 0) {
//                            t_mainDao.delete(t_mainList.get(0));
//                        }
                        t_detailDao.delete(detailhebing.get(i));
                    }
                }
            }

//            T_main t_main = new T_main();
//            t_main.FDepartment = departmentName == null ? "" : departmentName;
//            t_main.FDepartmentId = departmentId == null ? "" : departmentId;
//            t_main.tag = tag;
//            t_main.FIndex = second;
//            t_main.FPaymentDate = tvDatePay.getText().toString();
//            t_main.orderId = 0;
//            t_main.orderDate = tvDate.getText().toString();
//            t_main.FPurchaseUnit = "";
//            t_main.FSalesMan = employeeName == null ? "" : employeeName;
//            t_main.FSalesManId = employeeId == null ? "" : employeeId;
//            t_main.FMaker = share.getUserName();
//            Log.e("AFMaker", share.getUserName());
//            t_main.FMakerId = share.getsetUserID();
//            Log.e("AFMakerId", share.getsetUserID());
//            t_main.FDirector = ManagerName == null ? "" : ManagerName;
//            t_main.FDirectorId = ManagerId == null ? "" : ManagerId;
//            t_main.FPaymentType = signPersonName == null ? "" : signPersonName;
//            t_main.FPaymentTypeId = signPersonId == null ? "" : signPersonId;
//            t_main.saleWayId = capturePersonId == null ? "" : capturePersonId;
//            t_main.saleWay = capyurePersonName == null ? "" : capyurePersonName;
//            t_main.FDeliveryAddress = "";
//            t_main.FRemark = PurchaseMethodId == null ? "" : PurchaseMethodId;
//            t_main.FCustody = ManagerName == null ? "" : ManagerName;
//            t_main.FCustodyId = ManagerId == null ? "" : ManagerId;
//            t_main.FAcount = wanglaikemuName == null ? "" : wanglaikemuName;
//            t_main.FAcountID = wanglaikemuId == null ? "" : wanglaikemuId;
//            t_main.Rem = spGproduct.getEmployeeId();
//            t_main.supplier = captureID == null ? "" : captureID;
//            Log.e("captureID", captureID == null ? "" : captureID);
//            t_main.supplierId = fwanglaiUnit == null ? "" : fwanglaiUnit;
//            t_main.FSendOutId = "";
//            t_main.FDeliveryType = fid == null ? "" : fid;
//            t_main.activity = activity;
//            t_main.sourceOrderTypeId = "";
//            t_main.IMIE =  getIMIE();
//            long insert1 = t_mainDao.insert(t_main);

            String second = getTimesecond();
            T_Detail t_detail = new T_Detail();
            t_detail.tag = tag;
            t_detail.FBillNo = billNo;
            t_detail.FBatch = batchNo == null ? "" : batchNo;
            t_detail.FOrderId = 0;
            t_detail.FProductId = product.FItemID;
            t_detail.FProductName = product.FName;
            t_detail.FProductCode = product.FNumber;
            t_detail.FIndex = second;
            t_detail.FUnitId = unitId == null ? "" : unitId;
            t_detail.FUnit = unitName == null ? "" : unitName;
            t_detail.FStorage = storageName == null ? "" : storageName;
            t_detail.FStorageId = storageID == null ? "" : storageID;
            t_detail.FPosition = waveHouseName == null ? "" : waveHouseName;
            t_detail.FPositionId = waveHouseID == null ? "0" : waveHouseID;
            t_detail.activity = activity;
            t_detail.model = product.FModel==null?"":product.FModel;
            t_detail.FDiscount = discount;
            t_detail.FQuantity = num;
            t_detail.FCheckNum = num;
            t_detail.unitrate = unitrate;
            t_detail.FTaxUnitPrice = fprice == null ? "" : fprice;
            t_detail.FEntryID = fentryid == null ? "" : fentryid;
            t_detail.FInterID = fid == null ? "" : fid;
            long insert = t_detailDao.insert(t_detail);

            lockScan(0);
            if (insert > 0) {
                pushDownSub.FQtying = DoubleUtil.sum(MathUtil.toD(pushDownSub.FQtying) , (MathUtil.toD(edNum.getText().toString())*unitrate)/unitrateSub ) + "";
                pushDownSubDao.update(pushDownSub);
                Toast.showText(mContext, "添加成功");
                MediaPlayer.getInstance(mContext).ok();
                edNum.setText("");
                pushDownSubListAdapter.notifyDataSetChanged();
                resetAll();
                LoadingUtil.dismiss();
            } else {
                Toast.showText(mContext, "添加失败，请重试");
                MediaPlayer.getInstance(mContext).error();
                LoadingUtil.dismiss();
            }

        }else{
            Toast.showText(mContext,"未选择物料");
        }


    }

    private void getBatchNo() {
        if (waveHouseID == null) {
            waveHouseID = "0";
        }
        if (fBatchManager) {
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                final List<InStorageNum> container = new ArrayList<>();
                GetBatchNoBean gBean = new GetBatchNoBean();
                gBean.ProductID = productID;
                gBean.StorageID = storageID;
                gBean.WaveHouseID = waveHouseID;
                String json = new Gson().toJson(gBean);
                Asynchttp.post(mContext, getBaseUrl() + WebApi.GETPICI, json, new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        Log.e(TAG, "getPici获取数据：" + cBean.returnJson);
                        DownloadReturnBean downloadReturnBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                        if (downloadReturnBean.InstorageNum != null) {
                            container.addAll(downloadReturnBean.InstorageNum);
                            batchNoSpAdapter = new BatchNoSpAdapter(mContext, container);
                            spBatchNo.setAdapter(batchNoSpAdapter);
                            batchNoSpAdapter.notifyDataSetChanged();
                        } else {

                        }
//                    DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
//                    if(dBean.InstorageNum!=null){
//                        for (int i = 0; i < dBean.InstorageNum.size(); i++) {
//                            if (dBean.InstorageNum.get(i).FQty != null
//                                    && MathUtil.toD(dBean.InstorageNum.get(i).FQty) > 0) {
//                                Log.e(TAG,"有库存的批次："+dBean.InstorageNum.get(i).toString());
//                                container.add(dBean.InstorageNum.get(i));
//                            }
//                        }
//                        pici = new PiciSpAdapter(mContext, container);
//                        spPihao.setAdapter(pici);
//                    }
//                    pici.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        Log.e(TAG, "getPici获取数据错误：" + Msg);
                        Toast.showText(mContext, Msg);
                    }
                });
            } else {
                InStorageNumDao inStorageNumDao = daosession.getInStorageNumDao();
                List<InStorageNum> inStorageNa = inStorageNumDao.queryBuilder().where(
                        InStorageNumDao.Properties.FStockID.eq(storageID),
                        InStorageNumDao.Properties.FStockPlaceID.eq(waveHouseID),
                        InStorageNumDao.Properties.FItemID.eq(productID)
                ).build().list();
                if (inStorageNa.size() > 0) {
                    batchNoSpAdapter = new BatchNoSpAdapter(mContext, inStorageNa);
                    spBatchNo.setAdapter(batchNoSpAdapter);
                    batchNoSpAdapter.notifyDataSetChanged();
                }
            }
        }


    }
    private void resetAll(){
        edNum.setText("");
        productName.setText("");
        product=null;
    }

    private void upload() {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        t_mainDao = daosession.getT_mainDao();
        t_detailDao = daosession.getT_DetailDao();
        fidc = new ArrayList<>();
        fidc.add(fid);
        ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data = new ArrayList<>();
//        List<T_main> mainsTemp = t_mainDao.queryBuilder().where(T_mainDao.Properties.Activity.eq(activity)).build().list();
//        Lg.e(mainsTemp.size()+"");
//        TreeSet<String> getFids=new TreeSet<>();
//        for (int i = 0; i < mainsTemp.size(); i++) {
//            getFids.add(mainsTemp.get(i).FDeliveryType);
//        }
//        for (String str:getFids) {
//            List<T_main> mains = t_mainDao.queryBuilder().where(
//                    T_mainDao.Properties.Activity.eq(activity),
//                    T_mainDao.Properties.FDeliveryType.eq(str)
//            ).build().list();
//            for (int i = 0; i < mains.size(); i++) {
//                if (i > 0 && mains.get(i).FDeliveryType.equals(mains.get(i - 1).FDeliveryType)) {

//                } else {
                    PurchaseInStoreUploadBean.purchaseInStore puBean = pBean.new purchaseInStore();
                    detailContainer = new ArrayList<>();
                    String main;
                    String detail = "";
//                    T_main t_main = mains.get(i);
//                    fidc.add(t_main.FDeliveryType);
                    main = share.getsetUserID() + "|" +
                            getTime(true) + "|" ;
                    puBean.main = main;
//                    List<T_Detail> details = t_detailDao.queryBuilder().where(
//                            T_DetailDao.Properties.FInterID.eq(t_main.FDeliveryType),
//                            T_DetailDao.Properties.Activity.eq(activity)
//                    ).build().list();

                    for (int j = 0; j < container.size(); j++) {
                        if (j != 0 && j % 49 == 0) {
                            Log.e("j%49", j % 49 + "");
                            PushDownSub t_detail = container.get(j);
                            if (MathUtil.toD(t_detail.FQtying)!=0){
                                detail = detail +
                                        t_detail.FInterID + "|" +
                                        t_detail.FEntryID + "|" +
                                        t_detail.FQtying + "|" ;
                                detail = detail.subSequence(0, detail.length() - 1).toString();
                            }
                            detailContainer.add(detail);
                            detail = "";
                        } else {
                            Log.e("j", j + "");
//                            Log.e("details.size()", details.size() + "");
                            PushDownSub t_detail = container.get(j);
                            if (MathUtil.toD(t_detail.FQtying)!=0) {
                                detail = detail +
                                        t_detail.FInterID + "|" +
                                        t_detail.FEntryID + "|" +
                                        t_detail.FQtying + "|" ;
                                Log.e("detail1", detail);
                            }
                        }

                    }
                    if (detail.length() > 0) {
                        detail = detail.subSequence(0, detail.length() - 1).toString();
                    }

                    Log.e("detail", detail);
                    detailContainer.add(detail);
                    puBean.detail = detailContainer;
                    data.add(puBean);
//                }

//            }
//        }
        if (detailContainer.size()<=0 || "".equals(detailContainer.get(0))){
            Toast.showText(mContext,"不存在验货数据");
            btnBackorder.setClickable(true);
            LoadingUtil.dismiss();
        }else{
            postToServer(data);
        }
    }

    private void postToServer(ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data) {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        pBean.list = data;
        Gson gson = new Gson();
        App.getRService().doIOAction(WebApi.PdProductGetCheckActivityUpload, gson.toJson(pBean), new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                super.onNext(commonResponse);
                if (!commonResponse.state)return;
                Toast.showText(mContext, "上传成功");


                for (int i = 0; i < fidc.size(); i++) {
                    t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(
                            T_DetailDao.Properties.Activity.eq(activity),
                            T_DetailDao.Properties.FInterID.eq(fidc.get(i))
                    ).build().list());
//                    t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(
//                            T_mainDao.Properties.Activity.eq(activity)
//                    ).build().list());
                    List<PushDownSub> pushDownSubs = pushDownSubDao.queryBuilder().where(
                            PushDownSubDao.Properties.Tag.eq(tag),
                            PushDownSubDao.Properties.FInterID.eq(fidc.get(i))
                    ).build().list();
                    pushDownSubDao.deleteInTx(pushDownSubs);
                    List<PushDownMain> pushDownMains = pushDownMainDao.queryBuilder().where(
                            PushDownMainDao.Properties.Tag.eq(tag),
                            PushDownMainDao.Properties.FInterID.eq(fidc.get(i))
                    ).build().list();
                    pushDownMainDao.deleteInTx(pushDownMains);
                }
                MediaPlayer.getInstance(mContext).ok();
                btnBackorder.setClickable(true);
                LoadingUtil.dismiss();
                Bundle b = new Bundle();
                b.putInt("123",tag);
                startNewActivity(PushDownPagerActivity.class,0,0,true,b);
            }

            @Override
            public void onError(Throwable e) {
//                super.onError(e);
                Toast.showText(mContext, e.getMessage());
                btnBackorder.setClickable(true);
                MediaPlayer.getInstance(mContext).error();
                LoadingUtil.dismiss();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bundle b = new Bundle();
        b.putInt("123",tag);
        startNewActivity(PushDownPagerActivity.class,0,0,true,b);
    }

    //用于adpater首次更新时，不存入默认值，而是选中之前的选项
    private boolean isFirst=false;
    private boolean isFirst2=false;
    private boolean isFirst3=false;
    private boolean isFirst4=false;
    private boolean isFirst5=false;
    private boolean isFirst6=false;
    private boolean isFirst7=false;
    private boolean isFirst8=false;
}
