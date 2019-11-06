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
import com.fangzuo.assist.Adapter.DepartmentSpAdapter;
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
import com.fangzuo.assist.Beans.InStoreNumBean;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.BarCode;
import com.fangzuo.assist.Dao.Department;
import com.fangzuo.assist.Dao.Employee;
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

public class FHTZDDBActivity extends BaseActivity {
    private int tag =20;
    private int activity = Config.FHTZDDBActivity;

    @BindView(R.id.lv_pushsub)
    ListView lvPushsub;
    @BindView(R.id.sp_storageout)
    Spinner spStorageout;
    @BindView(R.id.sp_wavehouseout)
    MyWaveHouseSpinner spWavehouseout;
    @BindView(R.id.sp_storagein)
    Spinner spStoragein;
    @BindView(R.id.sp_wavehousein)
    Spinner spWavehousein;
    @BindView(R.id.productName)
    TextView productName;
    @BindView(R.id.sp_unit)
    Spinner spUnit;
    @BindView(R.id.ed_num)
    EditText edNum;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.btn_backorder)
    Button btnBackorder;
    @BindView(R.id.btn_checkorder)
    Button btnCheckorder;
    @BindView(R.id.cb_isAuto)
    CheckBox cbIsAuto;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.sp_department)
    Spinner spDepartment;
    @BindView(R.id.sp_employee)
    Spinner spEmployee;
    @BindView(R.id.sp_sign_person)
    Spinner spSignPerson;
    @BindView(R.id.sp_capture_person)
    Spinner spCapturePerson;
    @BindView(R.id.ed_batchNo)
    EditText edBatchNo;
    @BindView(R.id.et_beizhu)
    EditText etBeizhu;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.tv_kucun)
    TextView tvKucun;
    @BindView(R.id.sp_batchNo)
    Spinner spBatchNo;
    private DaoSession daosession;
    private PushDownSubListAdapter pushDownSubListAdapter;
    private UnitSpAdapter unitAdapter;
    private StorageSpAdapter storageOutSpAdapter;
    private StorageSpAdapter storageInSpAdapter;
    private WaveHouseSpAdapter waveHouseAdapter;
    private String storageName;
    private String waveHouseName;
    private String productID;
    private BatchNoSpAdapter batchNoSpAdapter;
    private String pihao;
    private CommonMethod method;
    private PayMethodSpAdapter slaesRange;
    private PayMethodSpAdapter payMethodSpinner;
    private YuandanSpAdapter yuandanSpAdapter;
    private PayTypeSpAdapter payTypeSpAdapter;
    private DepartmentSpAdapter departMentAdapter;
    private EmployeeSpAdapter employeeAdapter;
    private PayMethodSpAdapter getGoodsType;
    private String departmentId;
    private String departmentName;
    private String SaleMethodId;
    private String SaleMethodName;
    private String saleRangeId;
    private String saleRangeName;
    private String yuandanID;
    private String yuandanName;
    private String payTypeId;
    private String payTypeName;
    private String employeeId;
    private String employeeName;
    private String ManagerId;
    private String ManagerName;
    private String unitId;
    private String unitName;
    private double unitrate;
    private double unitrateSub;
    private String sendMethodId;
    private String sendMethodName;
    private List<Product> products;
    private String fid;
    private String fentryid;
    private String fprice;
    public static final String TAG = "activity";
    private PushDownSub pushDownSub;
    private PushDownSubDao pushDownSubDao;
    private int year;
    private int month;
    private int day;
    private String datePay;
    private String date;
    private T_mainDao t_mainDao;
    private T_DetailDao t_detailDao;
    private PushDownMainDao pushDownMainDao;
    private String fwanglaiUnit;
    private ArrayList<String> fidcontainer;
    private List<PushDownSub> container;
    @BindView(R.id.cb_isStorage)
    CheckBox cbIsStorage;
    private boolean isGetDefaultStorage;
    private List<Storage> storages;
    private Product product;
    private boolean isAuto = false;
    private ShareUtil share;
    private String captureID;
    private String captureName;
    private double qty;
    private String billNo;
    private boolean scanFlag;
    private ArrayList<String> detailContainer;
    private ArrayList<String> fidc;
    private String sendmanID;
    private String default_unitID;
    private String signID;
    private WaveHouseSpAdapter inwaveHouseAdapter;
    private WaveHouseSpAdapter outwaveHouseAdapter;
    private String employeeID;
    private String departmentID;
    private String inwaveHouseID;
    private String outstorageID;
    private String instorageID;
    private String outwaveHouseID;
    private String instorageName;
    private String inwaveHouseName;
    private String outstorageName;
    private String outwaveHouseName;
    private boolean fBatchManager;
    private boolean fromScan = false;
    private boolean checkStorage=false;  // 0不允许负库存false  1允许负库存出库true
    private String wavehouseAutoString="";
    private Storage storage;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_fhtzddb);
        mContext = this;
        ButterKnife.bind(this);
        share = ShareUtil.getInstance(mContext);
        daosession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        method = CommonMethod.getMethod(mContext);
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        cbIsAuto.setChecked(share.getPDMTisAuto());
        isAuto = share.getPDMTisAuto();
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
            Log.e("employeeId", employeeId == null ? "" : employeeId);
            Log.e("departmentId", departmentId == null ? "" : departmentId);
            billNo = list1.get(0).FBillNo;
        }
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
    }

    private void LoadBasicData() {
        storageOutSpAdapter = method.getStorageSpinner(spStorageout);
        storageInSpAdapter = method.getStorageSpinner(spStoragein);
        tvDate.setText(getTime(true));
        employeeAdapter = method.getEmployeeAdapter(spCapturePerson);
        method.getEmployeeAdapter(spEmployee);
        method.getEmployeeAdapter(spSignPerson);
        departMentAdapter = method.getDepartMentAdapter(spDepartment);

//        spCapturePerson.setSelection(share.getFHDBCapture());
//        spEmployee.setSelection(share.getFHDBEmployee());
//        spSignPerson.setSelection(share.getFHDBsign());
//        spDepartment.setSelection(share.getFHDBDepartment());

    }

    private void ScanBarCode(String barcode) {
        scanFlag = false;
        product = null;
        ProductDao productDao = daosession.getProductDao();
        BarCodeDao barCodeDao = daosession.getBarCodeDao();
        if (BasicShareUtil.getInstance(mContext).getIsOL()) {
            Asynchttp.post(mContext, getBaseUrl() + WebApi.SEARCHPRODUCTS, barcode, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    final DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                    if (dBean.products.size() > 0) {
                        product = dBean.products.get(0);
                        default_unitID = dBean.products.get(0).FUnitID;
                        fromScan= true;
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
                Toast.showText(mContext, "未找到条形码");
                MediaPlayer.getInstance(mContext).error();
            }
        }

    }
    //扫码的时候设置product
    private void setProduct(Product product) {
        Lg.e("setProduct","获取到："+product.toString());
        if (product != null) {
            boolean errorFlag = true;
            boolean hasUnit = true;
            for (int j = 0; j < pushDownSubListAdapter.getCount(); j++) {
                PushDownSub pushDownSub1 = (PushDownSub) pushDownSubListAdapter.getItem(j);
                if (product.FItemID.equals(pushDownSub1.FItemID)) {
                    if (Double.parseDouble(pushDownSub1.FAuxQty) == Double.parseDouble(pushDownSub1.FQtying)) {
                        continue;
                    } else {
//                        for (int i = 0; i < pushDownSubListAdapter.getCount(); i++) {
//                            PushDownSub pushDownSub = (PushDownSub) pushDownSubListAdapter.getItem(i);
                        if (!"".equals(default_unitID)){
                            if (default_unitID.equals(pushDownSub1.FUnitID)){
                                errorFlag = false;
                                hasUnit = true;
                                lvPushsub.setSelection(j);
                                lvPushsub.performItemClick(lvPushsub.getChildAt(j), j, lvPushsub.getItemIdAtPosition(j));
                                break;
                            }
                        }else{
                            errorFlag = false;
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
            if (errorFlag) {
                Toast.showText(mContext, "列表中不存在商品");
                MediaPlayer.getInstance(mContext).error();
            }
        } else {
            Toast.showText(mContext, "列表中不存在商品");
            MediaPlayer.getInstance(mContext).error();
        }
    }

    private void getInstorageNum() {
//        pihao = edBatchNo.getText().toString();
        if (pihao == null || pihao.equals("")) {
            pihao = "";
        }
        if (outwaveHouseID == null) {
            outwaveHouseID = "0";
        }
        if (product==null){
            return;
        }
        if (BasicShareUtil.getInstance(mContext).getIsOL()) {
            InStoreNumBean iBean = new InStoreNumBean();
            iBean.FStockPlaceID = outwaveHouseID;
            iBean.FBatchNo = pihao;
            iBean.FStockID = outstorageID;
            iBean.FItemID = (product.FItemID);
            String json = new Gson().toJson(iBean);
            Log.e("请求库存", json);
            Asynchttp.post(mContext, getBaseUrl() + WebApi.GETINSTORENUM, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    Lg.e("获得库存："+cBean.returnJson);
                    qty = Double.parseDouble(cBean.returnJson);
                    tvKucun.setText(qty+"");
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Lg.e("获得库存error："+Msg);
                    qty = 0.0;
                    tvKucun.setText(qty+"");
                }
            });
        } else {
            InStorageNumDao inStorageNumDao = daosession.getInStorageNumDao();
            List<InStorageNum> list1 = inStorageNumDao.queryBuilder().
                    where(InStorageNumDao.Properties.FItemID.eq(product.FItemID), InStorageNumDao.Properties.FStockID.eq(outstorageID),
                            InStorageNumDao.Properties.FStockPlaceID.eq(outwaveHouseID == null ? "0" : outwaveHouseID), InStorageNumDao.Properties.FBatchNo.eq(pihao == null ? "" : pihao)).build().list();
            if (list1.size() > 0) {
                Log.e("FQty", list1.get(0).FQty);
                qty = Double.parseDouble(list1.get(0).FQty);
                Log.e("qty", qty + "");
                tvKucun.setText(qty+"");
            } else {
                qty = 0.0;
                tvKucun.setText(qty+"");

            }

        }


    }

    @Override
    protected void initListener() {
        spDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Department department = (Department) departMentAdapter.getItem(i);
                departmentID = department.FItemID;
//                share.setFHDBDepartment(i);
                if (isFirst){
                    share.setFHDBDepartment(i);
                    spDepartment.setSelection(i);
                }
                else{
                    spDepartment.setSelection(share.getFHDBDepartment());
                    isFirst=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spEmployee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Employee employee = (Employee) employeeAdapter.getItem(i);
                employeeID = employee.FItemID;
//                share.setFHDBEmployee(i);
                if (isFirst2){
                    share.setFHDBEmployee(i);
                    spEmployee.setSelection(i);
                }
                else{
                    spEmployee.setSelection(share.getFHDBEmployee());
                    isFirst2=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spSignPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Employee sendman = (Employee) employeeAdapter.getItem(i);
                signID = sendman.FItemID;
//                share.setFHDBsign(i);
                if (isFirst3){
                    share.setFHDBsign(i);
                    spSignPerson.setSelection(i);
                }
                else{
                    spSignPerson.setSelection(share.getFHDBsign());
                    isFirst3=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spCapturePerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Employee capture = (Employee) employeeAdapter.getItem(i);
                captureID = capture.FItemID;
                captureName = capture.FName;
//                share.setFHDBCapture(i);
                if (isFirst4){
                    share.setFHDBCapture(i);
                    spCapturePerson.setSelection(i);
                }
                else{
                    spCapturePerson.setSelection(share.getFHDBCapture());
                    isFirst4=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        cbIsAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAuto = b;
                share.setPDMTisAuto(b);
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

        spStoragein.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Storage storage = (Storage) storageInSpAdapter.getItem(i);
                inwaveHouseID = "0";
                instorageID = storage.FItemID;
                instorageName = storage.FName;
                inwaveHouseAdapter = CommonMethod.getMethod(mContext).getWaveHouseAdapter(storage, spWavehousein);
                if (product != null) {
                    getInstorageNum();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spWavehousein.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WaveHouse waveHouse = (WaveHouse) inwaveHouseAdapter.getItem(i);
                inwaveHouseID = waveHouse.FSPID;
                inwaveHouseName = waveHouse.FName;
                if (product != null) {
                    getInstorageNum();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spStorageout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                storage = (Storage) storageOutSpAdapter.getItem(i);
                if ("1".equals(storage.FUnderStock)) {
                    checkStorage = true;
                } else {
                    checkStorage = false;
                }
                outwaveHouseID = "0";
                outstorageID = storage.FItemID;
                outstorageName = storage.FName;
//                outwaveHouseAdapter = CommonMethod.getMethod(mContext).getWaveHouseAdapter(storage, spWavehouseout);
                spWavehouseout.setAuto(mContext,storage,wavehouseAutoString);

                if (product != null) {
                    getInstorageNum();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spWavehouseout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WaveHouse waveHouse = (WaveHouse) spWavehouseout.getAdapter().getItem(i);
                outwaveHouseID = waveHouse.FSPID;
                outwaveHouseName = waveHouse.FName;
                if (product != null) {
                    getInstorageNum();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spBatchNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                InStorageNum inStorageNum = (InStorageNum) batchNoSpAdapter.getItem(i);
                pihao = inStorageNum.FBatchNo;

                getInstorageNum();

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

    }

    private void getBatchNo() {
        if (outwaveHouseID == null) {
            outwaveHouseID = "0";
        }

        if (fBatchManager) {

            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                GetBatchNoBean gBean = new GetBatchNoBean();
                gBean.ProductID = productID;
                gBean.StorageID = outstorageID;
                gBean.WaveHouseID = outwaveHouseID;
                String json = new Gson().toJson(gBean);
                Lg.e("请求批次；", json);
                Asynchttp.post(mContext, getBaseUrl() + WebApi.GETPICI, json, new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        Lg.e("获取批次："+cBean.returnJson);
                        DownloadReturnBean downloadReturnBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                        batchNoSpAdapter = new BatchNoSpAdapter(mContext, downloadReturnBean.InstorageNum);
                        spBatchNo.setAdapter(batchNoSpAdapter);
                        batchNoSpAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        Lg.e("获取批次error："+Msg);
                        Toast.showText(mContext, Msg);
                    }
                });

//
//            Synchttp.post(mContext, WebApi.GETPICI, new Gson().toJson(gBean), new Synchttp.Response() {
//                @Override
//                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//
//                }
//
//                @Override
//                public void onFailed(String Msg, AsyncHttpClient client) {
//                    Toast.showText(mContext, Msg);
//                }
//            });
            } else {
                InStorageNumDao inStorageNumDao = daosession.getInStorageNumDao();
                List<InStorageNum> inStorageNa = inStorageNumDao.queryBuilder().where(
                        InStorageNumDao.Properties.FStockID.eq(outstorageID),
                        InStorageNumDao.Properties.FStockPlaceID.eq(outwaveHouseID),
                        InStorageNumDao.Properties.FItemID.eq(productID)).build().list();
                if (inStorageNa.size() > 0) {
                    batchNoSpAdapter = new BatchNoSpAdapter(mContext, inStorageNa);
                    spBatchNo.setAdapter(batchNoSpAdapter);
                    batchNoSpAdapter.notifyDataSetChanged();
                }
            }

        } else {
            spBatchNo.setEnabled(false);
        }
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
        Lg.e("clickList",product.toString());
        productName.setText(product.FName);
        productID = pushDownSub.FItemID;
        wavehouseAutoString=product.FSPID;
        if ((product.FBatchManager) != null && (product.FBatchManager).equals("1")) {
            fBatchManager = true;
            spBatchNo.setEnabled(true);
            edBatchNo.setEnabled(true);
        } else {
            spBatchNo.setEnabled(false);
            edBatchNo.setEnabled(false);
            fBatchManager = false;
        }
        if (isGetDefaultStorage) {
            for (int j = 0; j < storageOutSpAdapter.getCount(); j++) {
                if (((Storage) storageOutSpAdapter.getItem(j)).FItemID.equals(product.FDefaultLoc)) {
                    Lg.e("定位调出仓库");
                    spStorageout.setSelection(j);
                    break;
                }
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    spWavehouseout.setAuto(mContext,storage,wavehouseAutoString);

//                    for (int j = 0; j < outwaveHouseAdapter.getCount(); j++) {
//                        if (((WaveHouse) outwaveHouseAdapter.getItem(j)).FSPID.equals(product.FSPID)) {
//                            Lg.e("定位调出仓位");
//                            spWavehouseout.setSelection(j);
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
        getBatchNo();
        getInstorageNum();
        if (isAuto) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Addorder();

                }
            }, 150);
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
        try {
            String[] split = code.split("\\^");
            if (split.length >= 3) {
//                edBatchNo.setText(split[1]);
                edNum.setText(split[2]);
                Log.e(TAG, "OnReceive:" + "批号:" + split[1] + "|||条码:" + split[0]
                        + "|||数量:" + split[2]);
                ScanBarCode(split[0]);
            } else {
                MediaPlayer.getInstance(mContext).error();
                Toast.showText(mContext, "条码数据有误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "条码数据有误");
        }
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
                date = year + "-" + ((month < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((day < 10) ? "0" + day : day);
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

            if (fBatchManager && "".equals(pihao)){
                Toast.showText(mContext,"批次有误");
                return;
            }
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
            if (outstorageID.equals(instorageID)) {
                MediaPlayer.getInstance(mContext).error();
                Toast.showText(mContext, "调出调入仓库不可相同");
                return;
            }

            //是否开启库存管理 true，开启允许负库存
            if (!checkStorage) {
                if ((qty / unitrate) < Double.parseDouble(num)) {
                    MediaPlayer.getInstance(mContext).error();
                    Toast.showText(mContext, "库存不够");
                    return;
                }
            }
                    boolean isHebing = true;
                    if (isHebing) {
                        List<T_Detail> detailhebing = t_detailDao.queryBuilder().where(
                                T_DetailDao.Properties.Activity.eq(activity),
                                T_DetailDao.Properties.FInterID.eq(fid),
                                T_DetailDao.Properties.FUnitId.eq(unitId),
                                T_DetailDao.Properties.FProductId.eq(product.FItemID),
                                T_DetailDao.Properties.FStorageId.eq(outstorageID),
//                                T_DetailDao.Properties.FRemark.eq(etBeizhu.getText().toString()),
                                T_DetailDao.Properties.FPositionId.eq(outwaveHouseID == null ? "0" : outwaveHouseID),
                                T_DetailDao.Properties.FEntryID.eq(fentryid),
//                                T_DetailDao.Properties.FBatch.eq(edBatchNo.getText().toString())
                                T_DetailDao.Properties.FBatch.eq(pihao ==null ?"":pihao )
                        ).build().list();
                        if (detailhebing.size() > 0) {
                            for (int i = 0; i < detailhebing.size(); i++) {
                                num = (Double.parseDouble(num) + Double.parseDouble(detailhebing.get(i).FQuantity)) + "";
                                List<T_main> t_mainList = t_mainDao.queryBuilder().where(
                                        T_mainDao.Properties.FIndex.eq(detailhebing.get(i).FIndex)
                                ).build().list();
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
                    t_main.FDepartmentId = departmentID == null ? "" : departmentID;
                    t_main.FIndex = second;
                    t_main.FPaymentDate = "";
                    t_main.orderId = 0;
                    t_main.orderDate = tvDate.getText().toString();
                    t_main.FPurchaseUnit = "";
                    t_main.FSalesMan = employeeName == null ? "" : employeeName;
                    t_main.FSalesManId = employeeID == null ? "" : employeeID;
                    t_main.FMaker = share.getUserName();
                    t_main.FMakerId = share.getsetUserID();
                    t_main.FDirector = ManagerName == null ? "" : ManagerName;
                    t_main.FDirectorId = ManagerId == null ? "" : ManagerId;
                    t_main.FPaymentType = payTypeName == null ? "" : payTypeName;
                    t_main.FPaymentTypeId = payTypeId == null ? "" : payTypeId;
                    t_main.saleWayId = signID == null ? "" : signID;
                    t_main.saleWay = captureID == null ? "" : captureID;
                    t_main.FDeliveryAddress = "";
                    t_main.FRemark = "";
                    t_main.FCustody = sendmanID == null ? "" : sendmanID;
                    t_main.FCustodyId = saleRangeId == null ? "" : saleRangeId;
                    t_main.FAcount = sendMethodId == null ? "" : sendMethodId;
                    t_main.FAcountID = "";
                    t_main.Rem = "";
                    t_main.supplier = captureID == null ? "" : captureID;
                    t_main.supplierId = fwanglaiUnit == null ? "" : fwanglaiUnit;
                    t_main.FSendOutId = "";
                    t_main.FDeliveryType = fid;
                    t_main.activity = activity;
                    t_main.sourceOrderTypeId = yuandanID == null ? "" : yuandanID;
                    long insert1 = t_mainDao.insert(t_main);

                    T_Detail t_detail = new T_Detail();
                    t_detail.FBillNo = billNo;
//                    t_detail.FBatch = edBatchNo.getText().toString();
                    t_detail.FBatch = pihao == null ? "" : pihao;
                    t_detail.FOrderId = 0;
                    t_detail.FProductId = product.FItemID;
                    t_detail.FProductName = product.FName;
                    t_detail.FProductCode = product.FNumber;
                    t_detail.FIndex = second;
                    t_detail.FUnitId = unitId == null ? "" : unitId;
                    t_detail.FUnit = unitName == null ? "" : unitName;
                    t_detail.FStorage = outstorageName == null ? "" : outstorageName;
                    t_detail.FStorageId = outstorageID == null ? "" : outstorageID;
                    t_detail.FPosition = outwaveHouseName == null ? "" : waveHouseName;
                    t_detail.FPositionId = outwaveHouseID == null ? "0" : outwaveHouseID;
                    t_detail.FoutStorageid = instorageID == null ? "" : instorageID;
                    t_detail.Foutwavehouseid = inwaveHouseID == null ? "" : inwaveHouseID;
                    t_detail.activity = activity;
                    t_detail.FDiscount = discount;
                    t_detail.FQuantity = num;
                    t_detail.unitrate = unitrate;
                    t_detail.FTaxUnitPrice = fprice == null ? "" : fprice;
                    t_detail.FEntryID = fentryid == null ? "" : fentryid;
                    t_detail.FInterID = fid == null ? "" : fid;
//                    t_detail.FRemark =etBeizhu.getText().toString();
                    long insert = t_detailDao.insert(t_detail);

                    if (insert1 > 0 && insert > 0) {
                        pushDownSub.FQtying = DoubleUtil.sum(Double.parseDouble(pushDownSub.FQtying) , (Double.parseDouble(edNum.getText().toString()) * unitrate)/unitrateSub) + "";
                        pushDownSubDao.update(pushDownSub);
                        Toast.showText(mContext, "添加成功");
                        MediaPlayer.getInstance(mContext).ok();
                        edNum.setText("");
                        pushDownSubListAdapter.notifyDataSetChanged();
                        product = null;
                        qty = 0.0;
                        resetAll();
                    } else {
                        Toast.showText(mContext, "添加失败，请重试");
                        MediaPlayer.getInstance(mContext).error();
                        qty = 0.0;
                    }


        } else {
            Toast.showText(mContext, "未选中物料");
            MediaPlayer.getInstance(mContext).error();
        }

    }

    private void resetAll() {
        edNum.setText("");
        productName.setText("");
        etBeizhu.setText("");
        product = null;
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
                            t_main.FPaymentDate + "|" +
                            t_main.supplierId + "|" +
                            "0" + "|" +
                            t_main.FDepartmentId + "|" +
                            t_main.FSalesManId + "|" +
                            "" + "|" +
                            t_main.saleWayId + "|" +
                            t_main.saleWay + "|";
                    puBean.main = main;
                    List<T_Detail> details = t_detailDao.queryBuilder().where(
                            T_DetailDao.Properties.FInterID.eq(t_main.FDeliveryType),
                            T_DetailDao.Properties.Activity.eq(activity)
                    ).build().list();
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
                                    t_detail.FBatch + "|" +
                                    t_detail.FoutStorageid + "|" +
                                    t_detail.Foutwavehouseid + "|";
//                                t_detail.FRemark + "|";
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
                                    t_detail.FBatch + "|" +
                                    t_detail.FoutStorageid + "|" +
                                    t_detail.Foutwavehouseid + "|";
//                                t_detail.FRemark + "|";
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
        Asynchttp.post(mContext, getBaseUrl() + "PushDownFHDBUpload", gson.toJson(pBean), new Asynchttp.Response() {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bundle b = new Bundle();
        b.putInt("123", tag);
        startNewActivity(PushDownPagerActivity.class, 0, 0, true, b);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
