package com.fangzuo.assist.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
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
import com.fangzuo.assist.Beans.CodeCheckBackDataBean;
import com.fangzuo.assist.Beans.CodeCheckBean;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
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
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.DataModel;
import com.fangzuo.assist.Utils.DoubleUtil;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.MediaPlayer;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.assist.widget.MyWaveHouseSpinner;
import com.fangzuo.assist.widget.SpinnerPeople;
import com.fangzuo.assist.widget.SpinnerStorage;
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

public class OutsourcingOrdersISActivity extends BaseActivity {
    private int tag =11;
    private int activity = Config.OutsourcingOrdersISActivity;


    @BindView(R.id.lv_pushsub)
    ListView lvPushsub;
    @BindView(R.id.sp_storage)
    SpinnerStorage spStorage;
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
    @BindView(R.id.cb_isStorage)
    CheckBox cbIsStorage;
    @BindView(R.id.cb_isAuto)
    CheckBox cbIsAuto;
    @BindView(R.id.tv_date)
    TextView tvDate;
//    @BindView(R.id.sp_weiwaileixing)
//    Spinner spWeiwaileixing;
    @BindView(R.id.sp_capture_person)
    SpinnerPeople spCapturePerson;
    @BindView(R.id.sp_sign_person)
    SpinnerPeople spSignPerson;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private DaoSession daosession;
    private CommonMethod method;
    private int year;
    private int month;
    private int day;
    private ArrayList<PushDownSub> container;
    private ArrayList<String> fidcontainer;
//    private StorageSpAdapter storageSpinner;
    private EmployeeSpAdapter employeeAdapter;
    private PushDownSubDao pushDownSubDao;
    private PushDownMainDao pushDownMainDao;
    private String fwanglaiUnit;
    private String employeeId;
    private String departmentId;
    private PushDownSubListAdapter pushDownSubListAdapter;
    private PayMethodSpAdapter payMethodSpAdapter;
    private EmployeeSpAdapter employeeSpAdapter;
    private List<Wanglaikemu> wanglaikemuList;
    private boolean isGetDefaultStorage;
    private boolean isAuto;
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
    private String storageID;
    private String storageName;
    private WaveHouseSpAdapter waveHouseAdapter;
    private String unitId;
    private String unitName;
    private double unitrate;
    private double unitrateSub;
    private String waveHouseID;
    private String waveHouseName;
    private String batchNo;
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
    private String billNo;
    private ArrayList<String> detailContainer;
    private ArrayList<String> fidc;
//    private PayMethodSpAdapter payMethodSpAdapter1;
    private String weiwaiId;
    private boolean fBatchManager;
    private String default_unitID;
    private boolean fromScan = false;
    private String wavehouseAutoString="";
    private Storage storage;
    private CodeCheckBackDataBean codeCheckBackDataBean;
    private Long ordercode;
    private String autoCode;

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
            case EventBusInfoCode.CodeCheck_OK://检测条码成功
                codeCheckBackDataBean = (CodeCheckBackDataBean)event.postEvent;
                Lg.e("条码检查："+codeCheckBackDataBean.toString());
                edBatchNo.setText(codeCheckBackDataBean.FBatchNo);
                edNum.setText(codeCheckBackDataBean.FQty);
                LoadingUtil.dismiss();
                ScanBarCode(codeCheckBackDataBean.FItemID);
//                setDATA(codeCheckBackDataBean.FItemID,false);
                break;
            case EventBusInfoCode.CodeCheck_Error://检测条码失败
                LoadingUtil.dismiss();
                codeCheckBackDataBean = (CodeCheckBackDataBean)event.postEvent;
                Toast.showText(mContext, codeCheckBackDataBean.FTip);
                canClick =false;
                lockScan(0);
                break;
            case EventBusInfoCode.CodeInsert_OK://写入条码唯一表成功
                Addorder();
                break;
            case EventBusInfoCode.CodeInsert_Error://写入条码唯一表失败
                codeCheckBackDataBean = (CodeCheckBackDataBean)event.postEvent;
                Toast.showText(mContext, codeCheckBackDataBean.FTip);
                lockScan(0);
                break;
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
        setContentView(R.layout.activity_outsourcing_orders_is);
        ButterKnife.bind(this);
        mContext = this;
        edBatchNo.setEnabled(false);
        daosession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        method = CommonMethod.getMethod(mContext);
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        cbIsAuto.setChecked(share.getSLTZisAuto());
        isAuto = share.getSLTZisAuto();

        //接收
        Intent intent = getIntent();
        if (intent != null) {
            autoCode =   intent.getStringExtra("code");
            fidcontainer = intent.getStringArrayListExtra("fid");
            Lg.e("Intent:"+autoCode);
            Lg.e("Intent:"+fidcontainer.toString());
        }
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
        container.clear();
        pushDownSubDao = daosession.getPushDownSubDao();
        pushDownMainDao = daosession.getPushDownMainDao();
        for (int i = 0; i < fidcontainer.size(); i++) {
            QueryBuilder<PushDownSub> qb = pushDownSubDao.queryBuilder();
            List<PushDownSub> list = qb.where(PushDownSubDao.Properties.FInterID.eq(fidcontainer.get(i))).build().list();
            container.addAll(list);
        }
        if (container.size() > 0) {
            pushDownSubListAdapter = new PushDownSubListAdapter(mContext, container);
            lvPushsub.setAdapter(pushDownSubListAdapter);
            pushDownSubListAdapter.notifyDataSetChanged();
        } else {
            Toast.showText(mContext, "未查询到数据");
        }

        if (autoCode!=null && !"".equals(autoCode)){
//            LoadingUtil.show(mContext,"正在自动添加...");
            LoadingUtil.showDialog(mContext,"正在自动添加...");
            OnReceive(autoCode);
            autoCode=null;
        }
    }

    private void LoadBasicData() {
//        storageSpinner = method.getStorageSpinner(spStorage);
//        payMethodSpAdapter1 = method.getweiwaiSpinner(spWeiwaileixing);
        tvDate.setText(getTime(true));
        spCapturePerson.setAutoSelection(Config.People1+activity,"");
        spSignPerson.setAutoSelection(Config.People2+activity,"");
        spStorage.setAutoSelection(Config.Storage+activity,"");
//        employeeSpAdapter = CommonMethod.getMethod(mContext).getEmployeeAdapter(spCapturePerson);
//        spSignPerson.setAdapter(employeeSpAdapter);
//        employeeSpAdapter.notifyDataSetChanged();

//        spCapturePerson.setSelection(share.getSLTZcapturePerson());
//        spSignPerson.setSelection(share.getSLTZSignPerson());

    }

    @Override
    protected void initListener() {
//        spWeiwaileixing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                PurchaseMethod p = (PurchaseMethod) payMethodSpAdapter1.getItem(i);
//                weiwaiId = p.FItemID;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
        cbIsAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAuto = b;
                share.setSLTZisAuto(b);
            }
        });
        cbIsStorage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isGetDefaultStorage = b;
            }
        });

        spUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Unit unit = (Unit) unitAdapter.getItem(i);
                if (unit != null) {
                    unitId = unit.FMeasureUnitID;
                    unitName = unit.FName;
                    unitrate = Double.parseDouble(unit.FCoefficient);
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
                Hawk.put(Config.Storage+activity,storage.FName);
                waveHouseID = "0";
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
                if (canClick){
                    canClick =false;
                }else{
                    return;
                }
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
    private void getUnitrateSub(PushDownSub pushDownSub){
        UnitDao unitDao = daosession.getUnitDao();
        List<Unit> units = unitDao.queryBuilder().where(
                UnitDao.Properties.FMeasureUnitID.eq(pushDownSub.FUnitID)
        ).build().list();
        if (units.size()>0){
            unitrateSub=Double.parseDouble(units.get(0).FCoefficient);
            Lg.e("获得明细换算率："+unitrateSub);
        }else{
            unitrateSub=1;
            Lg.e("获得明细换算率失败："+unitrateSub);
        }
    }
    private void clickList(final Product product) {
        productName.setText(product.FName);
        productID = pushDownSub.FItemID;
        wavehouseAutoString=product.FSPID;
        if ((product.FBatchManager) != null && product.FBatchManager.equals("1")) {
            edBatchNo.setEnabled(true);
        }else{
            edBatchNo.setEnabled(false);
        }
        if (isGetDefaultStorage) {
            spStorage.setAutoSelection("",product.FDefaultLoc);
//            for (int j = 0; j < storageSpinner.getCount(); j++) {
//                if (((Storage) storageSpinner.getItem(j)).FItemID.equals(product.FDefaultLoc)) {
//                    spStorage.setSelection(j);
//                    break;
//                }
//            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    spWavehouse.setAuto(mContext,storage,wavehouseAutoString);
//
//                    for (int j = 0; j < waveHouseAdapter.getCount(); j++) {
//                        if (((WaveHouse) waveHouseAdapter.getItem(j)).FSPID.equals(product.FSPID)) {
//                            spWavehouse.setSelection(j);
//                            break;
//                        }
//                    }
                }
            }, 50);
        }
        fid = pushDownSub.FInterID;
        fentryid = pushDownSub.FEntryID;
        fprice = pushDownSub.FAuxPrice;
        unitAdapter = CommonMethod.getMethod(mContext).getUnitAdapter(product.FUnitGroupID, spUnit);
        if (fromScan){
            chooseUnit(default_unitID);
        }else{
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
        }else{
            lockScan(0);
        }
    }
    //定位单位
    private void chooseUnit(String str){
        if (str!=null){
            for(int i = 0;i<unitAdapter.getCount();i++){
                if(((Unit)unitAdapter.getItem(i)).FMeasureUnitID.equals(str)){
                    Lg.e("定位单位："+unitAdapter.getItem(i).toString());
                    spUnit.setSelection(i);
                }
            }
        }

    }

    private String barcode = "";
    @Override
    protected void OnReceive(String code) {
//        ScanBarCode(code);
        barcode = code;
        LoadingUtil.showDialog(mContext,"正在查找...");
        //查询条码唯一表
        CodeCheckBean bean = new CodeCheckBean(barcode);
        DataModel.codeCheckForIn(gson.toJson(bean));
        canClick =true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("resume", "resume");
        getList();
    }

    private void getdate() {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            }
        }, year, day, month);

        datePickerDialog.setButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int year = datePickerDialog.getDatePicker().getYear();
                int month = datePickerDialog.getDatePicker().getMonth();
                int day = datePickerDialog.getDatePicker().getDayOfMonth();
                date = year + "-" + ((month < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((day < 10) ? "0" + day : day);
                tvDate.setText(date);
                Toast.showText(mContext, date);
                datePickerDialog.dismiss();

            }
        });
        datePickerDialog.show();
    }


    @OnClick({R.id.btn_add, R.id.btn_backorder, R.id.btn_checkorder, R.id.tv_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                AddorderBefore();
                break;
            case R.id.btn_backorder:
                btnBackorder.setClickable(false);
                LoadingUtil.show(mContext,"正在回单...");
                upload();
                break;
            case R.id.btn_checkorder:
                Bundle b = new Bundle();
                b.putInt("activity", activity);
                startNewActivity(Table3Activity.class, 0, 0, false, b);
                break;
            case R.id.tv_date:
                getdate();
                break;

        }
    }

    private void ScanBarCode(String barcode) {
        product = null;
        ProductDao productDao = daosession.getProductDao();
        BarCodeDao barCodeDao = daosession.getBarCodeDao();
        if (BasicShareUtil.getInstance(mContext).getIsOL()) {
            App.getRService().getProductForId(codeCheckBackDataBean.FItemID, new MySubscribe<CommonResponse>() {
                @Override
                public void onNext(CommonResponse commonResponse) {
                    LoadingUtil.dismiss();
                    final DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                    if (dBean.products.size() > 0) {
                        product = dBean.products.get(0);
                        Lg.e("获得物料："+product.toString());
//                        getProductOL(dBean, 0);
                        default_unitID = dBean.products.get(0).FUnitID;
                        fromScan = true;
                        setProduct(product);
                    } else {
                        lockScan(0);
                        canClick =false;
                        Toast.showText(mContext, "未找到物料信息");
                    }
                }

                @Override
                public void onError(Throwable e) {
                    LoadingUtil.dismiss();
                    lockScan(0);
                    canClick =false;
                    Toast.showText(mContext, "物料信息获取失败" + e.toString());
                }
            });

//            Asynchttp.post(mContext, getBaseUrl() + WebApi.SEARCHPRODUCTS, barcode, new Asynchttp.Response() {
//                @Override
//                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                    final DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
//                    Log.e("product.size", dBean.products.size() + "");
//                    if (dBean.products.size() > 0) {
//                        product = dBean.products.get(0);
//                        Log.e("product.size", product + "");
//                        default_unitID = dBean.products.get(0).FUnitID;
//                        fromScan = true;
//                        setProduct(product);
//                    }else{
//                        lockScan(0);
//                        Toast.showText(mContext,"未找到物料信息");
//                    }
//                }
//
//                @Override
//                public void onFailed(String Msg, AsyncHttpClient client) {
//                    Toast.showText(mContext, "ScanCode"+Msg);
//                    lockScan(0);
//                }
//            });
        } else {
            final List<BarCode> barCodes = barCodeDao.queryBuilder().where(BarCodeDao.Properties.FBarCode.eq(barcode)).build().list();
            if (barCodes.size() > 0) {
                List<Product> products = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(barCodes.get(0).FItemID)).build().list();
                if (products != null && products.size() > 0) {
                    product = products.get(0);
                    default_unitID = barCodes.get(0).FUnitID;
                    fromScan = true;
                    setProduct(product);
                }
            } else {lockScan(0);
                Toast.showText(mContext, "条码不存在");
                MediaPlayer.getInstance(mContext).error();
            }
        }

    }

    private void setProduct(Product product) {
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
                        if (!"".equals(default_unitID)){
                            if (default_unitID.equals(pushDownSub1.FUnitID)){
                                flag = false;
                                hasUnit = true;
                                lvPushsub.setSelection(j);
                                lvPushsub.performItemClick(lvPushsub.getChildAt(j), j, lvPushsub.getItemIdAtPosition(j));
                                break;
                            }
                        }else{
                            flag = false;
                            hasUnit = true;
                            lvPushsub.setSelection(j);
                            lvPushsub.performItemClick(lvPushsub.getChildAt(j), j, lvPushsub.getItemIdAtPosition(j));
                            break;
                        }

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
                canClick =false;
                Toast.showText(mContext, "商品不存在");
                MediaPlayer.getInstance(mContext).error();

            }
        } else {
            lockScan(0);
            canClick =false;
            Toast.showText(mContext, "列表中不存在商品");
        }
    }

    private void AddorderBefore(){
        String num = edNum.getText().toString();



        if(product==null){
            Toast.showText(mContext,"未选中物料");
            MediaPlayer.getInstance(mContext).error();
            lockScan(0);
            return;
        }
        if (MathUtil.toD(edNum.getText().toString())<=0) {
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
        if (Double.parseDouble(pushDownSub.FAuxQty) < ((Double.parseDouble(num) * unitrate)/unitrateSub + Double.parseDouble(pushDownSub.FQtying))) {
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "大兄弟,您的数量超过我的想象");
            lockScan(0);
            return;
        }
        //插入条码唯一临时表
        CodeCheckBean bean = new CodeCheckBean(barcode,ordercode + "",storageID==null?"":storageID,waveHouseID == null ? "0" : waveHouseID,BasicShareUtil.getInstance(mContext).getIMIE());
        DataModel.codeInsertForIn(gson.toJson(bean));

    }

    private void Addorder() {

            batchNo = edBatchNo.getText().toString();
            String discount = "";
            String num = edNum.getText().toString();
            T_DetailDao t_detailDao = daosession.getT_DetailDao();
            T_mainDao t_mainDao = daosession.getT_mainDao();

        LoadingUtil.showDialog(mContext,"请稍等...");


//            if (edNum.getText().toString().equals("0") || edNum.getText().toString().equals("")
//                    || fid == null || Double.parseDouble(pushDownSub.FAuxQty) < ((Double.parseDouble(num) * unitrate) + Double.parseDouble(pushDownSub.FQtying))) {
//                pg.dismiss();
//                if (edNum.getText().toString().equals("0") || edNum.getText().toString().equals("")) {
//                    Toast.showText(mContext, "请输入数量");
//                } else if (fid == null) {
//                    Toast.showText(mContext, "请选择单据");
//                } else if (Double.parseDouble(pushDownSub.FAuxQty) < ((Double.parseDouble(num)) + Double.parseDouble(pushDownSub.FQtying))) {
//                    Toast.showText(mContext, "大兄弟,您的数量超过我的想象");
//                }
//            } else {
                boolean isHebing = true;
                if (isHebing) {
                    List<T_Detail> detailhebing = t_detailDao.queryBuilder().where(T_DetailDao.Properties.Activity.eq(activity), T_DetailDao.Properties.FInterID.eq(fid)
                            , T_DetailDao.Properties.FUnitId.eq(unitId), T_DetailDao.Properties.FProductId.eq(product.FItemID), T_DetailDao.Properties.FStorageId.eq(storageID), T_DetailDao.Properties.FPositionId.eq(waveHouseID == null ? "0" : waveHouseID),
                            T_DetailDao.Properties.FEntryID.eq(fentryid), T_DetailDao.Properties.FBatch.eq(batchNo == null ? "" : batchNo)).build().list();
                    if (detailhebing.size() > 0) {
                        for (int i = 0; i < detailhebing.size(); i++) {
                            num = (Double.parseDouble(num) + Double.parseDouble(detailhebing.get(i).FQuantity)) + "";
                            List<T_main> t_mainList = t_mainDao.queryBuilder().where(T_mainDao.Properties.FIndex.eq(detailhebing.get(i).FIndex)).build().list();
                            if (t_mainList.size() > 0) {
                                t_mainDao.delete(t_mainList.get(0));
                            }
                            t_detailDao.delete(detailhebing.get(i));
                        }
                    }
                }

                String second = getTimesecond();
                T_main t_main = new T_main();
                t_main.FDepartment = departmentName == null ? "" : departmentName;
                t_main.FDepartmentId = departmentId == null ? "" : departmentId;
                t_main.FIndex = second;
        t_main.IMIE = getIMIE();
        t_main.activity = activity;
        t_main.orderId = ordercode;
        t_main.MakerId = share.getsetUserID();
                t_main.FPaymentDate = "";
                t_main.orderDate = tvDate.getText().toString();
                t_main.FPurchaseUnit = "";
                t_main.FSalesMan = employeeName == null ? "" : employeeName;
                t_main.FSalesManId = employeeId == null ? "" : employeeId;
                t_main.FMaker = share.getUserName();
                Log.e("AFMaker", share.getUserName());
                t_main.FMakerId = share.getsetUserID();
                Log.e("AFMakerId", share.getsetUserID());
                t_main.FDirector = ManagerName == null ? "" : ManagerName;
                t_main.FDirectorId = ManagerId == null ? "" : ManagerId;
                t_main.FPaymentType = spSignPerson.getEmployeeName();
                t_main.FPaymentTypeId = spSignPerson.getEmployeeId();
                t_main.saleWayId = spCapturePerson.getEmployeeId();
                t_main.saleWay = spCapturePerson.getEmployeeName();
                t_main.FDeliveryAddress = "";
                t_main.FRemark = PurchaseMethodId == null ? "" : PurchaseMethodId;
                t_main.FCustody = ManagerName == null ? "" : ManagerName;
                t_main.FCustodyId = ManagerId == null ? "" : ManagerId;
                t_main.FAcount = wanglaikemuName == null ? "" : wanglaikemuName;
                t_main.FAcountID = wanglaikemuId == null ? "" : wanglaikemuId;
                t_main.Rem = "";
                t_main.supplier = weiwaiId == null ? "" : weiwaiId;
                t_main.supplierId = fwanglaiUnit == null ? "" : fwanglaiUnit;
                t_main.FSendOutId = "";
                t_main.FDeliveryType = fid == null ? "" : fid;
                t_main.sourceOrderTypeId = "";
                long insert1 = t_mainDao.insert(t_main);

                T_Detail t_detail = new T_Detail();
                t_detail.FBillNo = billNo;
                t_detail.FBatch = batchNo == null ? "" : batchNo;
                t_detail.FOrderId = ordercode;
        t_detail.IMIE = getIMIE();
        t_detail.FBarcode =barcode;
        t_detail.MakerId = share.getsetUserID();
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
                t_detail.FDiscount = discount;
                t_detail.FQuantity = num;
                t_detail.unitrate = unitrate;
                t_detail.FTaxUnitPrice = fprice == null ? "" : fprice;
                t_detail.FEntryID = fentryid == null ? "" : fentryid;
                t_detail.FInterID = fid == null ? "" : fid;
        t_detail.IsAssemble = barcode.contains("ZZ")?barcode:"";
        t_detail.FKFDate = codeCheckBackDataBean==null?"":codeCheckBackDataBean.FKFDate;
        t_detail.FKFPeriod = codeCheckBackDataBean==null?"":codeCheckBackDataBean.FKFPeriod;

        long insert = t_detailDao.insert(t_detail);

                if (insert1 > 0 && insert > 0) {
                    pushDownSub.FQtying = DoubleUtil.sum(Double.parseDouble(pushDownSub.FQtying) ,(Double.parseDouble(edNum.getText().toString()) * unitrate)/unitrateSub) + "";
                    pushDownSubDao.update(pushDownSub);
                    Toast.showText(mContext, "添加成功");
                    MediaPlayer.getInstance(mContext).ok();
                    edNum.setText("");
                    pushDownSubListAdapter.notifyDataSetChanged();
                    LoadingUtil.dismiss();
                    if (!BasicShareUtil.getInstance(mContext).getIsOL()) {
                        InStorageNumDao inStorageNumDao = daosession.getInStorageNumDao();
                        List<InStorageNum> innum = inStorageNumDao.queryBuilder().where(InStorageNumDao.Properties.FBatchNo.eq(edBatchNo.getText().toString()), InStorageNumDao.Properties.FStockID.eq(storageID)
                                , InStorageNumDao.Properties.FStockPlaceID.eq(waveHouseID), InStorageNumDao.Properties.FItemID.eq(product.FItemID)).build().list();
                        if (innum.size() > 0) {
                            innum.get(0).FQty = (Double.parseDouble(innum.get(0).FQty) + (Double.parseDouble(edNum.getText().toString()) * unitrate)) + "";
                            inStorageNumDao.update(innum.get(0));
                        } else {
                            InStorageNum i = new InStorageNum();
                            i.FQty = num;
                            i.FItemID = product.FItemID;
                            i.FBatchNo = edBatchNo.getText().toString();
                            i.FStockID = storageID;
                            i.FStockPlaceID = waveHouseID;
                            inStorageNumDao.insert(i);
                        }
                    }
                    resetAll();
                } else {
                    Toast.showText(mContext, "添加失败，请重试");
                    MediaPlayer.getInstance(mContext).error();
                    LoadingUtil.dismiss();
                }

                lockScan(0);
    }


    private void resetAll() {
        edNum.setText("");
        edBatchNo.setEnabled(false);
        productName.setText("");
        product = null;
        lockScan(0);
    }

    private void upload() {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        t_mainDao = daosession.getT_mainDao();
        t_detailDao = daosession.getT_DetailDao();
        fidc = new ArrayList<>();
        ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data = new ArrayList<>();
        List<T_main> mainsTemp = t_mainDao.queryBuilder().where(T_mainDao.Properties.Activity.eq(activity)).build().list();
        Lg.e(mainsTemp.size()+"");
        TreeSet<String> getFids=new TreeSet<>();
        for (int i = 0; i < mainsTemp.size(); i++) {
            getFids.add(mainsTemp.get(i).FDeliveryType);
        }
        for (String str:getFids) {
            List<T_main> mains = t_mainDao.queryBuilder().where(
                    T_mainDao.Properties.Activity.eq(activity),
                    T_mainDao.Properties.FDeliveryType.eq(str)
            ).build().list();
            for (int i = 0; i < mains.size(); i++) {
                if (i > 0 && mains.get(i).FDeliveryType.equals(mains.get(i - 1).FDeliveryType)) {

                } else {
                    PurchaseInStoreUploadBean.purchaseInStore puBean = pBean.new purchaseInStore();
                    detailContainer = new ArrayList<>();
                    String main;
                    String detail = "";
                    T_main t_main = mains.get(i);
                    fidc.add(t_main.FDeliveryType);
                    main = t_main.FMakerId + "|" +
                            t_main.orderDate + "|" +
                            "1" + "|" +
                            t_main.saleWayId + "|" +
                            t_main.FPaymentTypeId + "|" +
                            "" + "|" +
                            t_main.supplier + "|" +
                            "" + "|"+
                            t_main.orderId + "|"+
                            t_main.IMIE + "|";
                    puBean.main = main;
                    List<T_Detail> details =DataModel.mergeDetail(mContext,t_main.orderId+"",activity);
//                    List<T_Detail> details = t_detailDao.queryBuilder().where(
//                            T_DetailDao.Properties.FInterID.eq(t_main.FDeliveryType),
//                            T_DetailDao.Properties.Activity.eq(activity)
//                    ).build().list();
                    for (int j = 0; j < details.size(); j++) {
                        if (j != 0 && j % 49 == 0) {
                            Log.e("j%49", j % 49 + "");
                            T_Detail t_detail = details.get(j);
                            detail = detail +
                                    t_detail.FProductId + "|" +
                                    t_detail.FUnitId + "|" +
                                    t_detail.FTaxUnitPrice + "|" +
                                    t_detail.FQuantity + "|" +
                                    t_detail.FStorageId + "|" +
                                    t_detail.FPositionId + "|" +
                                    t_detail.FEntryID + "|" +
                                    t_detail.FInterID + "|" +
                                    t_detail.FBatch + "|"+
                                    t_detail.FKFDate + "|"+
                                    t_detail.FKFPeriod + "|"+
                                    t_detail.IsAssemble + "|";
                            detail = detail.subSequence(0, detail.length() - 1).toString();
                            detailContainer.add(detail);
                            detail = "";
                        } else {
                            Log.e("j", j + "");
                            Log.e("details.size()", details.size() + "");
                            T_Detail t_detail = details.get(j);
                            detail = detail +
                                    t_detail.FProductId + "|" +
                                    t_detail.FUnitId + "|" +
                                    t_detail.FTaxUnitPrice + "|" +
                                    t_detail.FQuantity + "|" +
                                    t_detail.FStorageId + "|" +
                                    t_detail.FPositionId + "|" +
                                    t_detail.FEntryID + "|" +
                                    t_detail.FInterID + "|" +
                                    t_detail.FBatch + "|"+
                                    t_detail.FKFDate + "|"+
                                    t_detail.FKFPeriod + "|"+
                                    t_detail.IsAssemble + "|";
                            Log.e("detail1", detail);
                        }

                    }
                    if (detail.length() > 0) {
                        detail = detail.subSequence(0, detail.length() - 1).toString();
                    }

                    Log.e("detail", detail);
                    detailContainer.add(detail);
                    puBean.detail = detailContainer;
                    data.add(puBean);
                }

            }
        }
        postToServer(data);
    }

    private void postToServer(ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data) {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        pBean.list = data;
        Gson gson = new Gson();
        Asynchttp.post(mContext, getBaseUrl() + WebApi.PushDownOCISUpload, gson.toJson(pBean), new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                MediaPlayer.getInstance(mContext).ok();
                Toast.showText(mContext, "上传成功");
                List<T_Detail> list = t_detailDao.queryBuilder().where(T_DetailDao.Properties.Activity.eq(activity)).build().list();
                List<T_main> list1 = t_mainDao.queryBuilder().where(T_mainDao.Properties.Activity.eq(activity)).build().list();
                for (int i = 0; i < list.size(); i++) {
                    t_detailDao.delete(list.get(i));
                }
                for (int i = 0; i < list1.size(); i++) {
                    t_mainDao.delete(list1.get(i));
                }
                for (int i = 0; i < fidc.size(); i++) {
                    List<PushDownSub> pushDownSubs = pushDownSubDao.queryBuilder().where(PushDownSubDao.Properties.FInterID.eq(fidc.get(i))).build().list();
                    pushDownSubDao.deleteInTx(pushDownSubs);
                    List<PushDownMain> pushDownMains = pushDownMainDao.queryBuilder().where(PushDownMainDao.Properties.FInterID.eq(fidc.get(i))).build().list();
                    pushDownMainDao.deleteInTx(pushDownMains);
                }
                btnBackorder.setClickable(true);
                LoadingUtil.dismiss();
                Bundle b = new Bundle();
                b.putInt("123", tag);
                startNewActivity(PushDownPagerActivity.class, 0, 0, true, b);
            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
                Toast.showText(mContext, Msg);
                btnBackorder.setClickable(true);
                MediaPlayer.getInstance(mContext).error();
                LoadingUtil.dismiss();
            }
        });
    }

    public static void start(Context context, String code, ArrayList<String> fid) {
        Intent starter = new Intent(context, OutsourcingOrdersISActivity.class);
        starter.putExtra("code", code);
        starter.putStringArrayListExtra("fid", fid);
        context.startActivity(starter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bundle b = new Bundle();
        b.putInt("123", tag);
        startNewActivity(PushDownPagerActivity.class, 0, 0, true, b);
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
