package com.fangzuo.assist.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.fangzuo.assist.Adapter.BatchNoSpAdapter;
import com.fangzuo.assist.Adapter.EmployeeSpAdapter;
import com.fangzuo.assist.Adapter.PayMethodSpAdapter;
import com.fangzuo.assist.Adapter.PayTypeSpAdapter;
import com.fangzuo.assist.Adapter.PushDownSubListAdapter;
import com.fangzuo.assist.Adapter.StorageSpAdapter;
import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Adapter.WaveHouseSpAdapter;
import com.fangzuo.assist.Adapter.YuandanSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.GetBatchNoBean;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.BarCode;
import com.fangzuo.assist.Dao.InStorageNum;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.PushDownMain;
import com.fangzuo.assist.Dao.PushDownSub;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.Dao.Unit;
import com.fangzuo.assist.Dao.WaveHouse;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.CommonMethod;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.DoubleUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MediaPlayer;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Synchttp;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.assist.widget.MyWaveHouseSpinner;
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

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SCRWDPDSCHBDActivity extends BaseActivity {
    private int tag =16;
    private int activity = Config.SCRWDPDSCHBDActivity;

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
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private DaoSession daosession;
    private CommonMethod method;
    private int year;
    private int month;
    private int day;
    private List<PushDownSub> container;
    private ArrayList<String> fidcontainer;
    private List<PushDownSub> list;
    private PushDownSubDao pushDownSubDao;
    private PushDownMainDao pushDownMainDao;
    private String fwanglaiUnit;
    private String employeeId;
    private String departmentId;
    private PushDownSubListAdapter pushDownSubListAdapter;
    private EmployeeSpAdapter employeeAdapter;
    private StorageSpAdapter storageSpinner;
    private PayMethodSpAdapter slaesRange;
    private PayMethodSpAdapter payMethodSpinner;
    private YuandanSpAdapter yuandanSpAdapter;
    private PayTypeSpAdapter payTypeSpAdapter;
    private WaveHouseSpAdapter waveHouseAdapter;
    private String SaleMethodId;
    private String SaleMethodName;
    private String saleRangeId;
    private String saleRangeName;
    private String yuandanID;
    private String yuandanName;
    private String payTypeId;
    private String payTypeName;
    private String ManagerId;
    ArrayList<String> detailContainer;
    private String ManagerName;
    private String storageID;
    private String storageName;
    private String waveHouseID;
    private String waveHouseName;
    private PushDownSub pushDownSub;
    private List<Product> products;
    private String productID;
    private String fid;
    private String fentryid;
    private UnitSpAdapter unitAdapter;
    private String unitId;
    private String unitName;
    private double unitrate;
    private double unitrateSub;
    private String fprice;
    private T_mainDao t_mainDao;
    private String datePay;
    private boolean isHebing = true;
    private BatchNoSpAdapter batchNoSpAdapter;
    private String batchNo;
    private T_DetailDao t_detailDao;
    @BindView(R.id.cb_isStorage)
    CheckBox cbIsStorage;
    private boolean isGetDefaultStorage;
    private List<Storage> storages;
    private Product product;
    @BindView(R.id.cb_isAuto)
    CheckBox cbIsAuto;
    private boolean isAuto = false;
    private ShareUtil share;
    private String captureID;
    private String captureName;
    private String billNo;
    private ArrayList<String> fidc;
    private String wordkshop;
    private boolean fBatchManager;
    private String default_unitID;
    private boolean fromScan=false;
    private String wavehouseAutoString="";
    private Storage storage;

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
                    }
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Toast.showText(mContext, Msg);
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
                }
            } else {
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

    @Override
    protected void initView() {
        setContentView(R.layout.activity_scrwdpdschbd);
        mContext = this;
        ButterKnife.bind(this);
        share = ShareUtil.getInstance(mContext);
        daosession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        method = CommonMethod.getMethod(mContext);
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        cbIsAuto.setChecked(share.getPDPOisAuto());
        isAuto = share.getPDPOisAuto();
    }

    @Override
    protected void initData() {
        LoadBasicData();
//        LoadBasicData();
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
    }

    private void getList() {
        container.clear();
        pushDownSubDao = daosession.getPushDownSubDao();
        pushDownMainDao = daosession.getPushDownMainDao();
        for (int i = 0; i < fidcontainer.size(); i++) {
            QueryBuilder<PushDownSub> qb = pushDownSubDao.queryBuilder();
            list = qb.where(PushDownSubDao.Properties.FInterID.eq(fidcontainer.get(i))).build().list();
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
        storageSpinner = method.getStorageSpinner(spStorage);
        tvDate.setText(getTime(true));
    }

    @Override
    protected void initListener() {

        cbIsAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAuto = b;
                share.setPDPOisAuto(b);
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
                storage = (Storage) storageSpinner.getItem(i);
                storageID = storage.FItemID;
                storageName = storage.FName;
                waveHouseID = "0";
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
                            }
                        }

                        @Override
                        public void onFailed(String Msg, AsyncHttpClient client) {
                            Toast.showText(mContext, Msg);
                        }
                    });
                } else {
                    products = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(pushDownSub.FItemID)).build().list();
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


        spBatchNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                InStorageNum inStorageNum = (InStorageNum) batchNoSpAdapter.getItem(i);
                batchNo = inStorageNum.FBatchNo;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

        if ((product.FBatchManager) != null && (product.FBatchManager).equals("1")) {
            fBatchManager = true;
            spBatchNo.setEnabled(true);
        } else {
            spBatchNo.setEnabled(false);
            fBatchManager = false;
        }
        if (isGetDefaultStorage) {
            for (int j = 0; j < storageSpinner.getCount(); j++) {
                if (((Storage) storageSpinner.getItem(j)).FItemID.equals(product.FDefaultLoc)) {
                    spStorage.setSelection(j);
                    break;
                }
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    spWavehouse.setAuto(mContext,storage,wavehouseAutoString);

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
        wordkshop = pushDownSub.FAuxPrice;
        fentryid = pushDownSub.FEntryID;
        fprice = pushDownSub.FAuxPrice;
        unitAdapter = CommonMethod.getMethod(mContext).getUnitAdapter(product.FUnitGroupID, spUnit);
        if (fromScan){
            chooseUnit(default_unitID);
        }else{
            chooseUnit(pushDownSub.FUnitID);
        }
        fromScan = false;
        getBatchNo();
        if (isAuto) {
            edNum.setText("1.0");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Addorder();
                }
            }, 100);
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
    @Override
    protected void OnReceive(String code) {
        ScanBarCode(code);

    }


    @OnClick({R.id.btn_add, R.id.btn_backorder, R.id.btn_checkorder, R.id.tv_date})
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
                startNewActivity(Table3Activity.class, 0, 0, false, b);
                break;
            case R.id.tv_date:
                getdate();
                break;

        }
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
                String date = year + "-" + ((month < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((day < 10) ? "0" + day : day);
                tvDate.setText(date);
                Toast.showText(mContext, date);
                datePickerDialog.dismiss();

            }
        });
        datePickerDialog.show();
    }


    private void Addorder() {
        if (product != null) {
            String discount = "";
            String num = edNum.getText().toString();
            T_DetailDao t_detailDao = daosession.getT_DetailDao();
            T_mainDao t_mainDao = daosession.getT_mainDao();

            if (edNum.getText().toString().equals("")||edNum.getText().toString().equals("0")) {
                MediaPlayer.getInstance(mContext).error();
                Toast.showText(mContext, "请输入数量");
                return;
            }
            if (fid == null) {
                MediaPlayer.getInstance(mContext).error();
                Toast.showText(mContext, "请选择单据");
                return;
            }
            if (Double.parseDouble(pushDownSub.FAuxQty) < ((Double.parseDouble(num) * unitrate)/unitrateSub + Double.parseDouble(pushDownSub.FQtying))) {
                MediaPlayer.getInstance(mContext).error();
                Toast.showText(mContext, "大兄弟,您的数量超过我的想象");
                return;
            }
            ProgressDialog pg = new ProgressDialog(mContext);
            pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pg.setMessage("请稍后...");
            pg.setCancelable(false);
            pg.show();

                if (isHebing) {
                    List<T_Detail> detailhebing = t_detailDao.queryBuilder().where(T_DetailDao.Properties.Activity.eq(activity), T_DetailDao.Properties.FInterID.eq(fid)
                            , T_DetailDao.Properties.FProductId.eq(product.FItemID == null ? "" : product.FItemID), T_DetailDao.Properties.FStorageId.eq(storageID), T_DetailDao.Properties.FPositionId.eq(waveHouseID)
                            , T_DetailDao.Properties.FUnitId.eq(unitId), T_DetailDao.Properties.FEntryID.eq(fentryid), T_DetailDao.Properties.FBatch.eq(batchNo == null ? "0" : batchNo)).build().list();
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
                t_main.FDepartment = "";
                t_main.FDepartmentId = departmentId == null ? "0" : departmentId;
                t_main.FIndex = second;
                t_main.FPaymentDate = "";
                t_main.orderId = 0;
                t_main.orderDate = tvDate.getText().toString();
                t_main.FPurchaseUnit = "";
                t_main.FSalesMan = "";
                t_main.FSalesManId = employeeId == null ? "0" : employeeId;
                t_main.FMaker = share.getUserName();
                Log.e("AFMaker", share.getUserName());
                t_main.FMakerId = share.getsetUserID();
                Log.e("AFMakerId", share.getsetUserID());
                t_main.FDirector = ManagerName == null ? "0" : ManagerName;
                t_main.FDirectorId = ManagerId == null ? "0" : ManagerId;
                t_main.FPaymentType = payTypeName == null ? "0" : payTypeName;
                t_main.FPaymentTypeId = payTypeId == null ? "0" : payTypeId;
                t_main.saleWayId = SaleMethodId == null ? "0" : SaleMethodId;
                t_main.saleWay = saleRangeName == null ? "0" : saleRangeName;
                t_main.FDeliveryAddress = "";
                t_main.FRemark = "";
                t_main.FCustody = "";
                t_main.FCustodyId = saleRangeId == null ? "0" : saleRangeId;
                t_main.FAcount = "";
                t_main.FAcountID = "";
                t_main.Rem = wordkshop==null?"":wordkshop;
                t_main.supplier = captureID == null ? "0" : captureID;
                t_main.supplierId = fwanglaiUnit == null ? "0" : fwanglaiUnit;
                t_main.FSendOutId = "";
                t_main.FDeliveryType = fid == null ? "0" : fid;
                t_main.activity = activity;
                t_main.sourceOrderTypeId = yuandanID == null ? "0" : yuandanID;
                long insert1 = t_mainDao.insert(t_main);

                T_Detail t_detail = new T_Detail();
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
                t_detail.FPositionId = waveHouseID == null ? "" : waveHouseID;
                t_detail.activity = activity;
                t_detail.FDiscount = discount;
                t_detail.FQuantity = num;
                t_detail.unitrate = unitrate;
                t_detail.FTaxUnitPrice = fprice == null ? "" : fprice;
                t_detail.FEntryID = fentryid == null ? "" : fentryid;
                t_detail.FInterID = fid == null ? "" : fid;
                long insert = t_detailDao.insert(t_detail);

                if (insert1 > 0 && insert > 0) {
                    pushDownSub.FQtying = DoubleUtil.sum(Double.parseDouble(pushDownSub.FQtying) , (Double.parseDouble(edNum.getText().toString()) * unitrate)/unitrateSub) + "";
                    pushDownSubDao.update(pushDownSub);
                    Toast.showText(mContext, "添加成功");
                    MediaPlayer.getInstance(mContext).ok();
                    pushDownSubListAdapter.notifyDataSetChanged();
                    resetAll();
                    pg.dismiss();
                } else {
                    Toast.showText(mContext, "添加失败，请重试");
                    MediaPlayer.getInstance(mContext).error();
                    pg.dismiss();
                }
        } else {
            Toast.showText(mContext, "未选中物料");
        }

    }

    private void resetAll() {
        edNum.setText("");
        productName.setText("");
        product = null;
    }

    private void getBatchNo() {
        if (waveHouseID == null) {
            waveHouseID = "0";
        }
        if (fBatchManager) {
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                GetBatchNoBean gBean = new GetBatchNoBean();
                gBean.ProductID = productID;
                gBean.StorageID = storageID;
                gBean.WaveHouseID = waveHouseID;
                Synchttp.post(mContext, WebApi.GETPICI, new Gson().toJson(gBean), new Synchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        DownloadReturnBean downloadReturnBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                        batchNoSpAdapter = new BatchNoSpAdapter(mContext, downloadReturnBean.InstorageNum);
                        spBatchNo.setAdapter(batchNoSpAdapter);
                        batchNoSpAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
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
                    main = t_main.FMakerId + "|" + t_main.orderDate + "|" + t_main.Rem + "|";
                    puBean.main = main;
                    List<T_Detail> details = t_detailDao.queryBuilder().where(T_DetailDao.Properties.FInterID.eq(t_main.FDeliveryType), T_DetailDao.Properties.Activity.eq(activity)).build().list();
                    for (int j = 0; j < details.size(); j++) {
                        if (j != 0 && j % 49 == 0) {
                            Log.e("j%49", j % 49 + "");
                            T_Detail t_detail = details.get(j);
                            detail = detail + t_detail.FProductId + "|" + t_detail.FUnitId + "|" + t_detail.FTaxUnitPrice + "|" +
                                    t_detail.FQuantity + "|" + t_detail.FStorageId + "|" + t_detail.FPositionId + "|" + t_detail.FEntryID +
                                    "|" + t_detail.FInterID + "|" + t_detail.FBatch + "|";
                            detail = detail.subSequence(0, detail.length() - 1).toString();
                            detailContainer.add(detail);
                            detail = "";
                        } else {
                            Log.e("j", j + "");
                            Log.e("details.size()", details.size() + "");
                            T_Detail t_detail = details.get(j);
                            detail = detail + t_detail.FProductId + "|" + t_detail.FUnitId + "|" + t_detail.FTaxUnitPrice + "|" +
                                    t_detail.FQuantity + "|" + t_detail.FStorageId + "|" + t_detail.FPositionId + "|" + t_detail.FEntryID +
                                    "|" + t_detail.FInterID + "|" + t_detail.FBatch + "|";
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
        Asynchttp.post(mContext, getBaseUrl() + WebApi.SCRWDPDSCHBDUPLOAD, gson.toJson(pBean), new Asynchttp.Response() {
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
                MediaPlayer.getInstance(mContext).error();
                btnBackorder.setClickable(true);
                Toast.showText(mContext, Msg);
                LoadingUtil.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bundle b = new Bundle();
        b.putInt("123", tag);
        startNewActivity(PushDownPagerActivity.class, 0, 0, true, b);
    }


}
