package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.AutoTVAdapter;
import com.fangzuo.assist.Adapter.DepartmentSpAdapter;
import com.fangzuo.assist.Adapter.EmployeeSpAdapter;
import com.fangzuo.assist.Adapter.PayMethodSpAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter1;
import com.fangzuo.assist.Adapter.StorageSpAdapter;
import com.fangzuo.assist.Adapter.SupplierSpAdapter;
import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Adapter.WaveHouseSpAdapter;
import com.fangzuo.assist.Beans.CodeCheckBackDataBean;
import com.fangzuo.assist.Beans.CodeCheckBean;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.InStoreNumBean;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.BarCode;
import com.fangzuo.assist.Dao.Department;
import com.fangzuo.assist.Dao.Employee;
import com.fangzuo.assist.Dao.InStorageNum;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.PurchaseMethod;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.Suppliers;
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
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.MediaPlayer;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.assist.widget.MyWaveHouseSpinner;
import com.fangzuo.assist.widget.SpinnerUnit;
import com.fangzuo.assist.zxing.activity.CaptureActivity;
import com.fangzuo.greendao.gen.BarCodeDao;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.InStorageNumDao;
import com.fangzuo.greendao.gen.ProductDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.fangzuo.greendao.gen.WaveHouseDao;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//                  外购入库
public class PurchaseInStorageActivity extends BaseActivity {

    @BindView(R.id.drawer)
    DrawerLayout mDrawer;
    @BindView(R.id.scanbyCamera)
    RelativeLayout scanbyCamera;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.ishebing)
    CheckBox cbHebing;
    @BindView(R.id.ed_supplier)
    EditText edSupplier;
    @BindView(R.id.sp_which_storage)
    Spinner spWhichStorage;
    @BindView(R.id.tv_goodName)
    TextView tvGoodName;
    @BindView(R.id.tv_model)
    TextView tvModel;
    @BindView(R.id.sp_unit)
    SpinnerUnit spUnit;
    @BindView(R.id.ed_pihao)
    EditText edPihao;
    @BindView(R.id.sp_wavehouse)
    MyWaveHouseSpinner spWavehouse;
    @BindView(R.id.tv_numinstorage)
    TextView tvNuminstorage;            //库存
    @BindView(R.id.ed_num)
    EditText edNum;
    @BindView(R.id.ed_zhaiyao)
    EditText edZhaiyao;
    @BindView(R.id.ed_pricesingle)
    EditText edPricesingle;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.btn_finishorder)
    Button btnFinishorder;
    @BindView(R.id.btn_backorder)
    Button btnBackorder;
    @BindView(R.id.btn_checkorder)
    Button btnCheckorder;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_date_pay)
    TextView tvDatePay;
    @BindView(R.id.sp_purchaseMethod)
    Spinner spPurchaseMethod;
    @BindView(R.id.sp_capture_person)
    Spinner spCapturePerson;
    @BindView(R.id.sp_sign_person)
    Spinner spSignPerson;
    @BindView(R.id.sp_department)
    Spinner spDepartment;
    @BindView(R.id.sp_employee)
    Spinner spEmployee;
    @BindView(R.id.sp_manager)
    Spinner spManager;
    @BindView(R.id.sp_wanglaikemu)
    Spinner spWanglaikemu;
    @BindView(R.id.search)
    RelativeLayout search;
    public static final int PurchaseInStorage = 1;
    @BindView(R.id.search_supplier)
    RelativeLayout searchSupplier;
    @BindView(R.id.isAutoAdd)
    CheckBox autoAdd;
    @BindView(R.id.cb_isStorage)
    CheckBox cbIsStorage;
    @BindView(R.id.redorBlue)
    RadioGroup redorBlue;
    private PurchaseInStorageActivity mContext;
    private Unbinder bind;
    private ProductDao productDao;
    private ArrayList<String> container1;
    private ArrayList<String> container;
    private AutoTVAdapter items;
    private List<Product> resultAll;
    private PopupWindow popupWindow;
    private DaoSession daoSession;
    private StorageSpAdapter storageSpAdapter;
    private SupplierSpAdapter supplierSpAdapter;
    private Storage storage;
    private List<WaveHouse> waveHouses;
    private boolean isGetDefaultStorage = true;
    private boolean isSpStorageDefault = true;
    private List<Suppliers> supplierses;
    private WaveHouseDao wavehousedao;
    private boolean fBatchManager;
    private String wavehouseID = "0";
    private List<Product> list;
    private int year;
    private int month;
    private int day;
    private String dateAdd;
    private String datePay;
//    private ShareUtil share;
    private long ordercode;
    private String storageId;
    private String storageName;
    public String wavehouseName;
    private List<PurchaseMethod> purchaseMethods;
    private List<Employee> employees;
    private List<Department> departments;
    private List<Wanglaikemu> wanglaikemus;
    private String PurchaseMethodId;
    private String purchaseMethodName;
    private String capturePersonId;
    private String capyurePersonName;
    private String signPersonId;
    private String signPersonName;
    private String departmentId;
    private String departmentName;
    private String PersonId;
    private String PersonName;
    private String managerId;
    private String managerName;
    private String wanglaikemuId = "";
    private String wanglaikemuName;
    private List<Unit> units;
    private String unitId;
    private String unitName;
    private String supplierid = "";
    private String supplierName = "";
    private boolean isHebing = true;
    private Double unitrate;
    private PayMethodSpAdapter payMethodSpAdapter;
    private EmployeeSpAdapter employeeSpAdapter;
    private DepartmentSpAdapter departMentAdapter;
    private WaveHouseSpAdapter waveHouseAdapter;
    private List<Wanglaikemu> wanglaikemuList;
    private UnitSpAdapter unitAdapter;
    private DecimalFormat df;
    private T_DetailDao t_detailDao;
    private T_mainDao t_mainDao;
    private String date;
    private Double qty;
    private boolean isAuto = false;
    private Product product;
    private ProductselectAdapter productselectAdapter;
    private ProductselectAdapter1 productselectAdapter1;


    private String default_unitID;
    private boolean isRed;
    private String redblue = "蓝字";
    private String BatchNo;
    private String wavehouseAutoString="";
    private CodeCheckBackDataBean codeCheckBackDataBean;
    private int activity = Config.PurchaseInStorage;

    @Override
    public void initView() {
        setContentView(R.layout.activity_purchase_in_storage);
        mContext = this;
        df = new DecimalFormat("######0.00");
        bind = ButterKnife.bind(mContext);
//        share = ShareUtil.getInstance(mContext);
        edPihao.setEnabled(false);
        daoSession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        t_detailDao = daoSession.getT_DetailDao();
        t_mainDao=daoSession.getT_mainDao();
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        initDrawer(mDrawer);
//        cbHebing.setChecked(true);
        autoAdd.setChecked(share.getPUISisAuto());
        isAuto = share.getPUISisAuto();
        cbIsStorage.setChecked(isGetDefaultStorage);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg) {
//            case EventBusInfoCode.PRODUCTRETURN:
//                product = (Product) event.postEvent;
//                setDATA("", true);
//                break;
            case EventBusInfoCode.CodeCheck_OK://检测条码成功
                codeCheckBackDataBean = (CodeCheckBackDataBean)event.postEvent;
                Lg.e("条码检查："+codeCheckBackDataBean.toString());
                edPihao.setText(codeCheckBackDataBean.FBatchNo);
                edNum.setText(codeCheckBackDataBean.FQty);
                LoadingUtil.dismiss();
                setDATA(codeCheckBackDataBean.FItemID,false);
                break;
            case EventBusInfoCode.CodeCheck_Error://检测条码失败
                LoadingUtil.dismiss();
                codeCheckBackDataBean = (CodeCheckBackDataBean)event.postEvent;
                Toast.showText(mContext, codeCheckBackDataBean.FTip);
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
                btnBackorder.setClickable(true);
                LoadingUtil.dismiss();
                MediaPlayer.getInstance(mContext).ok();
                break;
            case EventBusInfoCode.Upload_Error://回答失败
                String error = (String)event.postEvent;
                Toast.showText(mContext, error);
                btnBackorder.setClickable(true);
                LoadingUtil.dismiss();
                MediaPlayer.getInstance(mContext).error();
                break;
        }
    }

    @Override
    public void initData() {
        if (share.getPISOrderCode() == 0) {
            ordercode = Long.parseLong(getTime(false) + "001");
            Log.e("ordercode", ordercode + "");
            share.setPISOrderCode(ordercode);
        } else {
            ordercode = share.getPISOrderCode();
            Log.e("ordercode", ordercode + "");
        }
        LoadBasicData();
    }


    @Override
    public void initListener() {
        redorBlue.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = (RadioButton) findViewById(i);
                isRed = radioButton.getText().toString().equals("红字");
                redblue = isRed?"红字":"蓝字";
                Log.e("isred",isRed+"");

            }
        });
        cbIsStorage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isGetDefaultStorage = b;
            }
        });
//        cbHebing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                isHebing = b;
//            }
//        });

        //批号输入监听
        edPihao.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.e(TAG,"批号监听。。。");
                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    getInstorageNum(product);
                    setfocus(edPihao);
                }
                return true;
            }
        });
//        edCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                    setDATA(edCode.getText().toString(), false);
//                    setfocus(edCode);
//
//                }
//                return true;
//            }
//        });
//
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e("search", "onclick");
//                Bundle b = new Bundle();
//                b.putString("search", edCode.getText().toString());
//                b.putInt("where", Info.SEARCHPRODUCT);
//                startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.SEARCHFORRESULT, b);
//            }
//        });

        spWhichStorage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                isSpStorageDefault = false;
                storage = (Storage) storageSpAdapter.getItem(i);
                wavehouseID = "0";
//                waveHouseAdapter = CommonMethod.getMethod(mContext).getWaveHouseAdapter(storage, spWavehouse);
                spWavehouse.setAuto(mContext,storage,wavehouseAutoString);
                storageId = storage.FItemID;
                storageName = storage.FName;
                Log.e("storageId", storageId);
                Log.e("storageName", storageName);
                getInstorageNum(product);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spWavehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WaveHouse waveHouse = (WaveHouse) spWavehouse.getAdapter().getItem(i);
                wavehouseID = waveHouse.FSPID;
                wavehouseName = waveHouse.FName;
                Log.e("wavehouseName", wavehouseName);
                getInstorageNum(product);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tvDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dateAdd = i + "-" + ((i1 < 10) ? "0" + (i1 + 1) : (i1 + 1)) + "-" + ((i2 < 10) ? "0" + i2 : i2);
                        tvDate.setText(dateAdd);
                        share.setPISdate(dateAdd);
                    }
                }, year, month, day);
                datePickerDialog.show();
                datePickerDialog.setButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        datePickerDialog.dismiss();
                        Toast.showText(mContext, dateAdd);
                    }
                });
            }
        });

        tvDatePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        datePay = i + "-" + ((i1 < 10) ? "0" + (i1 + 1) : (i1 + 1)) + "-" + ((i2 < 10) ? "0" + i2 : i2);
                        tvDatePay.setText(datePay);
                        share.setPISdatepay(datePay);
                    }
                }, year, month, day);
                datePickerDialog.show();
                datePickerDialog.setButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        datePickerDialog.dismiss();
                        Toast.showText(mContext, dateAdd);
                    }
                });
            }
        });

        spPurchaseMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PurchaseMethod item = payMethodSpAdapter.getChild(i);
                PurchaseMethodId = item.FItemID;
                purchaseMethodName = item.FName;
//                share.setPISpayMethod(i);
                if (isFirst){
                    share.setPISpayMethod(i);
                    spPurchaseMethod.setSelection(i);
                }
                else{
                    spPurchaseMethod.setSelection(share.getPISpayMethod());
                    isFirst=true;
                }

                Log.e("purchaseMethodName", purchaseMethodName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spCapturePerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Employee employee = (Employee) employeeSpAdapter.getItem(i);
                capturePersonId = employee.FItemID;
                capyurePersonName = employee.FName;
//                share.setPISkeepperson(i);
                if (isFirst2){
                    share.setPISkeepperson(i);
                    spCapturePerson.setSelection(i);
                }
                else{
                    spCapturePerson.setSelection(share.getPISkeepperson());
                    isFirst2=true;
                }
                Log.e("capyurePersonName", capyurePersonName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spSignPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Employee employee = (Employee) employeeSpAdapter.getItem(i);
                signPersonId = employee.FItemID;
                signPersonName = employee.FName;
//                share.setPISyanshou(i);
                if (isFirst3){
                    share.setPISyanshou(i);
                    spSignPerson.setSelection(i);
                }
                else{
                    spSignPerson.setSelection(share.getPISyanshou());
                    isFirst3=true;
                }
                Log.e("signPersonName", signPersonName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Department department = (Department) departMentAdapter.getItem(i);
                departmentId = department.FItemID;
                departmentName = department.FName;
//                share.setPISdepartment(i);
                if (isFirst4){
                    share.setPISdepartment(i);
                    spDepartment.setSelection(i);
                }
                else{
                    spDepartment.setSelection(share.getPISdepartment());
                    isFirst4=true;
                }
                Log.e("departmentName", departmentName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spEmployee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Employee employee = (Employee) employeeSpAdapter.getItem(i);
                PersonId = employee.FItemID;
                PersonName = employee.FName;
//                share.setPISemployee(i);
                if (isFirst5){
                    share.setPISemployee(i);
                    spEmployee.setSelection(i);
                }
                else{
                    spEmployee.setSelection(share.getPISemployee());
                    isFirst5=true;
                }
                Log.e("1111", PersonName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spManager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Employee employee = (Employee) employeeSpAdapter.getItem(i);
                managerId = employee.FItemID;
                managerName = employee.FName;
//                share.setPISManager(i);
                if (isFirst6){
                    share.setPISManager(i);
                    spManager.setSelection(i);
                }
                else{
                    spManager.setSelection(share.getPISManager());
                    isFirst6=true;
                }
                Log.e("1111", managerName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spWanglaikemu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (wanglaikemuList.size() > 0) {
                    wanglaikemuId = wanglaikemuList.get(i).FAccountID;
                    wanglaikemuName = wanglaikemuList.get(i).FFullName;
                } else {
                    wanglaikemuId = "";
                    wanglaikemuName = "";
                }
//                share.setPISwanglaikemu(i);
                if (isFirst7){
                    share.setPISwanglaikemu(i);
                    spWanglaikemu.setSelection(i);
                }
                else{
                    spWanglaikemu.setSelection(share.getPISwanglaikemu());
                    isFirst7=true;
                }

                Log.e("1111", unitName + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//        cbHebing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                isHebing = b;
//                Log.e("ishebing", isHebing + "");
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
                    Log.e("unitId", unitId + "");
                }

                getInstorageNum(product);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        edSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edSupplier.getText().toString().equals("")) {
                    edSupplier.selectAll();
                }
            }
        });

        autoAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAuto = b;
                share.setPUISisAuto(b);
            }
        });
    }

    private String barcode = "";
    @Override
    protected void OnReceive(String code) {
//            if (edPihao.hasFocus()) {
//                edPihao.setText(code);
//                getInstorageNum(product);
//                if (isAuto) {
//                    Addorder();
//                } else if (edNum.getText().toString().equals("")) {
//                    setfocus(edNum);
//                }
//            } else {
//                edCode.setText(code);
//                setDATA(code, false);
//            }
        barcode = code;
        LoadingUtil.showDialog(mContext,"正在查找...");
        //查询条码唯一表
        CodeCheckBean bean = new CodeCheckBean(barcode);
        DataModel.codeCheckForIn(gson.toJson(bean));
    }

    private void LoadBasicData() {

        storageSpAdapter = CommonMethod.getMethod(mContext).getStorageSpinner(spWhichStorage);
        payMethodSpAdapter = CommonMethod.getMethod(mContext).getPayMethodSpinner(spPurchaseMethod);
        employeeSpAdapter = CommonMethod.getMethod(mContext).getEmployeeAdapter(spCapturePerson);
        departMentAdapter = CommonMethod.getMethod(mContext).getDepartMentAdapter(spDepartment);
        wanglaikemuList = CommonMethod.getMethod(mContext).getwlkmAdapter(spWanglaikemu);

        spSignPerson.setAdapter(employeeSpAdapter);
        spManager.setAdapter(employeeSpAdapter);
        spEmployee.setAdapter(employeeSpAdapter);
        employeeSpAdapter.notifyDataSetChanged();

//        spPurchaseMethod.setSelection(share.getPISpayMethod());
//        spCapturePerson.setSelection(share.getPISkeepperson());
//        spSignPerson.setSelection(share.getPISyanshou());
//        spManager.setSelection(share.getPISManager());
//        spEmployee.setSelection(share.getPISemployee());
//        spDepartment.setSelection(share.getPISdepartment());
//        spWanglaikemu.setSelection(share.getPISwanglaikemu());

        tvDate.setText(share.getPISdate());
        tvDatePay.setText(share.getPISdatepay());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }


    @OnClick(R.id.scanbyCamera)
    public void onViewClicked() {
        Intent in = new Intent(mContext, CaptureActivity.class);
        startActivityForResult(in, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("code", requestCode + "" + "    " + resultCode);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                String message = b.getString("result");
                edCode.setText(message);
                Toast.showText(mContext, message);
                edCode.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
            }
        } else if (requestCode == Info.SEARCHFORRESULTPRODUCT) {
            if (resultCode == Info.SEARCHFORRESULTPRODUCT) {
                Bundle b = data.getExtras();
                supplierid = b.getString("001");
                supplierName = b.getString("002");
                edSupplier.setText(supplierName);
                setfocus(edCode);
            }
        }
    }

    private void setDATA(String fnumber, boolean flag) {
        default_unitID = null;
        list = null;
        if (flag) {
            tvorisAuto(product);
        } else {
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                App.getRService().getProductForId(codeCheckBackDataBean.FItemID, new MySubscribe<CommonResponse>() {
                    @Override
                    public void onNext(CommonResponse commonResponse) {
                        LoadingUtil.dismiss();
                        final DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                        if (dBean.products.size() > 0) {
                            product = dBean.products.get(0);
                            Lg.e("获得物料："+product.toString());
                            getProductOL(dBean, 0);
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


//                Asynchttp.post(mContext, getBaseUrl() + WebApi.SEARCHPRODUCTS, fnumber, new Asynchttp.Response() {
//                    @Override
//                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                        final DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
//                        if (dBean.products.size() == 1) {
//                            Lg.e("products:"+dBean.products.toString());
//                            getProductOL(dBean, 0);
////                            default_unitID = dBean.products.get(0).FUnitID;
////                            chooseUnit(default_unitID);
//                        } else if (dBean.products.size() > 1) {
//                            AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
//                            ab.setTitle("请选择物料");
//                            View v = LayoutInflater.from(mContext).inflate(R.layout.pd_alert, null);
//                            ListView lv = v.findViewById(R.id.lv_alert);
//                            productselectAdapter1 = new ProductselectAdapter1(mContext, dBean.products);
//                            lv.setAdapter(productselectAdapter);
//                            ab.setView(v);
//                            final AlertDialog alertDialog = ab.create();
//                            alertDialog.show();
//                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                    getProductOL(dBean, i);
////                                    default_unitID = dBean.products.get(i).FUnitID;
////                                    chooseUnit(default_unitID);
//                                    alertDialog.dismiss();
//                                }
//                            });
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(String Msg, AsyncHttpClient client) {
//                        Toast.showText(mContext, Msg);
//                    }
//                });
            }
//            else {
//                final ProductDao productDao = daoSession.getProductDao();
//                BarCodeDao barCodeDao = daoSession.getBarCodeDao();
//                final List<BarCode> barCodes = barCodeDao.queryBuilder().where(
//                        BarCodeDao.Properties.FBarCode.eq(fnumber)
//                ).build().list();
//                if (barCodes.size() > 0) {
//                    if (barCodes.size() == 1) {
//                        list = productDao.queryBuilder().where(
//                                ProductDao.Properties.FItemID.eq(barCodes.get(0).FItemID)
//                        ).build().list();
////                        default_unitID = barCodes.get(0).FUnitID;
////                        chooseUnit(default_unitID);
//                        getProductOFL(list);
//                    } else {
//                        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
//                        ab.setTitle("请选择物料");
//                        View v = LayoutInflater.from(mContext).inflate(R.layout.pd_alert, null);
//                        ListView lv = v.findViewById(R.id.lv_alert);
//                        productselectAdapter = new ProductselectAdapter(mContext, barCodes);
//                        lv.setAdapter(productselectAdapter);
//                        ab.setView(v);
//                        final AlertDialog alertDialog = ab.create();
//                        alertDialog.show();
//                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                BarCode barCode = (BarCode) productselectAdapter.getItem(i);
////                                default_unitID = barCode.FUnitID;
////                                chooseUnit(default_unitID);
//                                list = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(barCode.FItemID)).build().list();
//                                getProductOFL(list);
//                                alertDialog.dismiss();
//                            }
//                        });
//                    }
//                }else{
//                    MediaPlayer.getInstance(mContext).error();
//                    Toast.showText(mContext,"未找到条码" );
//                }
//
//            }
        }


    }

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
//    private void getProductOFL(List<Product> list){
//        if (list != null && list.size() > 0) {
//            product = list.get(0);
//            tvorisAuto(product);
//        } else {
//            Toast.showText(mContext, "未找到物料");
//            edCode.setText("");
//            setfocus(edCode);
//            edPihao.setEnabled(false);
//        }
//    }
    private void getProductOL(DownloadReturnBean dBean, int j) {
        product = dBean.products.get(j);
        spUnit.setAuto(mContext,product.FUnitGroupID,codeCheckBackDataBean.FUnitID,SpinnerUnit.Id);
        tvorisAuto(product);
    }

    //库存
    private void getInstorageNum(Product product) {
        if (product == null){
            return;
        }
        String pihao="";
        if (fBatchManager) {
            pihao = edPihao.getText().toString();
            if (pihao.equals("")) {
                pihao = "";
            }
        } else {
            pihao = "";
        }
        if (wavehouseID == null) {
            wavehouseID = "0";
        }
        if (BasicShareUtil.getInstance(mContext).getIsOL()) {
            InStoreNumBean iBean = new InStoreNumBean();
            iBean.FStockPlaceID = wavehouseID;
            iBean.FBatchNo = pihao;
            iBean.FStockID = storage.FItemID;
            iBean.FItemID = product.FItemID;
            String json = new Gson().toJson(iBean);
            Log.e("inStorenum", json);
            Asynchttp.post(mContext, getBaseUrl() + WebApi.GETINSTORENUM, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    double num = MathUtil.toD(cBean.returnJson);
                    tvNuminstorage.setText((num / unitrate) + "");
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    tvNuminstorage.setText("0");
                }
            });
        } else {
            InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
            List<InStorageNum> list1 = inStorageNumDao.queryBuilder().
                    where(InStorageNumDao.Properties.FItemID.eq(product.FItemID), InStorageNumDao.Properties.FStockID.eq(storage.FItemID),
                            InStorageNumDao.Properties.FStockPlaceID.eq(wavehouseID), InStorageNumDao.Properties.FBatchNo.eq(pihao)).build().list();
            if (list1.size() > 0) {
                Log.e("FQty", list1.get(0).FQty);
                qty =MathUtil.toD(list1.get(0).FQty);
                Log.e("qty", qty + "");
                if (qty != null) {
                    tvNuminstorage.setText((qty / unitrate) + "");
                }
            } else {
                tvNuminstorage.setText("0");
            }

        }


    }

    private void tvorisAuto(final Product product) {
        edCode.setText(product.FNumber);
        tvModel.setText(product.FModel);
        wavehouseAutoString=product.FSPID;
        edPricesingle.setText(df.format(MathUtil.toD(product.FSalePrice)));
        tvGoodName.setText(product.FName);
        if ((product.FBatchManager) != null && (product.FBatchManager).equals("1")) {
            setfocus(edPihao);
            fBatchManager = true;
            edPihao.setEnabled(false);
        } else {
            edPihao.setEnabled(false);
            fBatchManager = false;
        }
        if (isGetDefaultStorage) {
            for (int j = 0; j < storageSpAdapter.getCount(); j++) {
                if (((Storage) storageSpAdapter.getItem(j)).FItemID.equals(product.FDefaultLoc)) {
                    spWhichStorage.setSelection(j);
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
//        unitAdapter = CommonMethod.getMethod(mContext).getUnitAdapter(product.FUnitGroupID, spUnit);
//        chooseUnit(default_unitID);

        //        if (default_unitID != null) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    for (int i = 0; i < unitAdapter.getCount(); i++) {
//                        if (default_unitID.equals(((Unit) unitAdapter.getItem(i)).FMeasureUnitID)) {
//                            spUnit.setSelection(i);
//                        }
//                    }
//                }
//            }, 100);
//        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getInstorageNum(product);
            }
        }, 100);
//        if (isAuto) {
//            edNum.setText("1.0");
//        }

        if ((isAuto && !fBatchManager) || (isAuto && fBatchManager && !edPihao.getText().toString().equals(""))) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AddorderBefore();
                }
            }, 150);
        }else{
            lockScan(0);
        }
    }

    @OnClick({R.id.btn_add, R.id.btn_finishorder, R.id.btn_backorder, R.id.btn_checkorder, R.id.search_supplier})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                AddorderBefore();
                break;
            case R.id.btn_finishorder:
                finishOrder();
                break;
            case R.id.btn_backorder:
                if (DataModel.checkHasDetail(mContext,activity)){
                    btnBackorder.setClickable(false);
                    LoadingUtil.show(mContext,"正在回单...");
                    upload();
                }else{
                    Toast.showText(mContext,"无单据信息");
                }
                break;
            case R.id.btn_checkorder:
                Bundle b = new Bundle();
                b.putInt("activity", activity);
                startNewActivity(TableActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, false, b);
                break;
            case R.id.search_supplier:
                SearchSupplier();
                break;
        }
    }

    private void upload() {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        PurchaseInStoreUploadBean.purchaseInStore puBean = pBean.new purchaseInStore();
        t_mainDao = daoSession.getT_mainDao();
        t_detailDao = daoSession.getT_DetailDao();
        ArrayList<String> detailContainer = new ArrayList<>();
        ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data = new ArrayList<>();
        List<T_main> mains = t_mainDao.queryBuilder().where(
                T_mainDao.Properties.Activity.eq(activity)
        ).orderAsc(T_mainDao.Properties.OrderId).build().list();
        for (int i = 0; i < mains.size(); i++) {
            if (i > 0 && mains.get(i).orderId == mains.get(i - 1).orderId) {

            } else {
                detailContainer = new ArrayList<>();
                puBean = pBean.new purchaseInStore();
                String main;
                String detail = "";
                T_main t_main = mains.get(i);
                main = t_main.FMakerId + "|" +
                        t_main.orderDate + "|" +
                        t_main.FPaymentDate + "|" +
                        t_main.saleWayId + "|" +
                        t_main.FDeliveryAddress + "|" +
                        t_main.FDepartmentId + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.FDirectorId + "|" +
                        t_main.supplierId + "|" +
                        t_main.Rem + "|" +
                        t_main.FSendOutId + "|" +
                        t_main.FCustodyId + "|" +
                        t_main.FRedBlue + "|" +
                        "" + "|" +
                        t_main.sourceOrderTypeId + "|" +
                        t_main.FAcountID + "|" +
                        t_main.orderId + "|"+
                        t_main.IMIE + "|";
                puBean.main = main;
                List<T_Detail> details =DataModel.mergeDetail(mContext,t_main.orderId+"",activity);
//                List<T_Detail> details = t_detailDao.queryBuilder().where(
//                        T_DetailDao.Properties.FOrderId.eq(t_main.orderId),
//                        T_DetailDao.Properties.Activity.eq(activity)
//                ).build().list();
                for (int j = 0; j < details.size(); j++) {
                    if (j != 0 && j % 49 == 0) {
                        Log.e("j%49", j % 49 + "");
                        T_Detail t_detail = details.get(j);
                        detail = detail +
                                t_detail.FProductId + "|" +
                                t_detail.FUnitId + "|" +
                                t_detail.FTaxUnitPrice + "|" +
                                t_detail.FQuantity + "|" +
                                t_detail.FDiscount + "|" +
                                t_detail.FStorageId + "|" +
                                t_detail.FBatch + "|" +
                                t_detail.FPositionId + "|" +
                                t_detail.FKFDate + "|" +
                                t_detail.FKFPeriod + "|" +
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
                                t_detail.FDiscount + "|" +
                                t_detail.FStorageId + "|" +
                                t_detail.FBatch + "|" +
                                t_detail.FPositionId + "|" +
                                t_detail.FKFDate + "|" +
                                t_detail.FKFPeriod + "|" +
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
        pBean.list = data;
        DataModel.upload(mContext,getBaseUrl()+ WebApi.UPLOADPIS,gson.toJson(pBean));
//        postToServer(data);
    }

    private void postToServer(ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data) {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        pBean.list = data;
        Gson gson = new Gson();
        Asynchttp.post(mContext, getBaseUrl() + WebApi.UPLOADPIS, gson.toJson(pBean), new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                Toast.showText(mContext, "上传成功");
                t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(
                        T_DetailDao.Properties.Activity.eq(activity)
                ).build().list());
                t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(
                        T_mainDao.Properties.Activity.eq(activity)
                ).build().list());
                btnBackorder.setClickable(true);
                LoadingUtil.dismiss();
                MediaPlayer.getInstance(mContext).ok();
            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
                Toast.showText(mContext, Msg);
                btnBackorder.setClickable(true);
                LoadingUtil.dismiss();
                MediaPlayer.getInstance(mContext).error();
            }
        });
    }

    private void SearchSupplier() {
        Bundle b = new Bundle();
        b.putString("search", edSupplier.getText().toString());
        b.putInt("where", Info.SEARCHSUPPLIER);
        startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.SEARCHFORRESULTPRODUCT, b);

    }

    private void AddorderBefore(){
        if ("".equals(barcode)){
            Toast.showText(mContext,"条码不能为空");
            MediaPlayer.getInstance(mContext).error();
            lockScan(0);
            return;
        }
        if (product == null){
            Toast.showText(mContext,"未扫描物料");
            MediaPlayer.getInstance(mContext).error();
            lockScan(0);
            return;
        }

        String discount = "0";
        String num = edNum.getText().toString();
        if(isRed){
            num = "-"+edNum.getText().toString();
        }else{
            num = edNum.getText().toString();
        }
        if (supplierid==null) {
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "请选择供应商");
            lockScan(0);
            return;
        }

        if (edSupplier.getText().toString().equals("") || edCode.getText().toString().equals("") || edPricesingle.getText().toString().equals("") || edNum.getText().toString().equals("")) {
            MediaPlayer.getInstance(mContext).error();
            lockScan(0);
            if (edCode.getText().toString().equals("")) {
                setfocus(edCode);
                Toast.showText(mContext, "请输入物料编号");
                lockScan(0);
            } else if (edPricesingle.getText().toString().equals("")) {
                setfocus(edPricesingle);
                Toast.showText(mContext, "请输入单价");
                lockScan(0);
            } else if (edNum.getText().toString().equals("")) {
                setfocus(edNum);
                Toast.showText(mContext, "请输入数量");
                lockScan(0);
            } else if (edSupplier.getText().toString().equals("") && supplierid.equals("")) {
                setfocus(edSupplier);
                Toast.showText(mContext, "请输入供应商");
                lockScan(0);
            }
        } else if (fBatchManager && edPihao.getText().toString().equals("")){
            setfocus(edPihao);
            Toast.showText(mContext, "请输入批次号");
            lockScan(0);
        } else if(isRed&&(Math.abs(MathUtil.toD(num))>(qty/unitrate))){
            Toast.showText(mContext,"库存不足");
            lockScan(0);
        } else {
            //插入条码唯一临时表
            CodeCheckBean bean = new CodeCheckBean(barcode,ordercode + "",storageId==null?"":storageId,wavehouseID == null ? "0" : wavehouseID,BasicShareUtil.getInstance(mContext).getIMIE());
            DataModel.codeInsertForIn(gson.toJson(bean));
        }
    }
    private void Addorder() {
        String discount = "0";
        String num = edNum.getText().toString();
        if(isRed){
            num = "-"+edNum.getText().toString();
        }else{
            num = edNum.getText().toString();
        }
////        T_DetailDao t_detailDao = daoSession.getT_DetailDao();
////        T_mainDao t_mainDao = daoSession.getT_mainDao();
////        if (edSupplier.getText().toString().equals("") || edCode.getText().toString().equals("") || edPricesingle.getText().toString().equals("") || edNum.getText().toString().equals("")) {
////            MediaPlayer.getInstance(mContext).error();
////            if (edCode.getText().toString().equals("")) {
////                setfocus(edCode);
////                Toast.showText(mContext, "请输入物料编号");
////            } else if (edPricesingle.getText().toString().equals("")) {
////                setfocus(edPricesingle);
////                Toast.showText(mContext, "请输入单价");
////            } else if (edNum.getText().toString().equals("")) {
////                setfocus(edNum);
////                Toast.showText(mContext, "请输入数量");
////            } else if (edSupplier.getText().toString().equals("") && supplierid.equals("")) {
////                setfocus(edSupplier);
////                Toast.showText(mContext, "请输入供应商");
////            }
////        } else if (fBatchManager && edPihao.getText().toString().equals("")){
////            setfocus(edPihao);
////            Toast.showText(mContext, "请输入批次号");
////        } else if(isRed&&(Math.abs(MathUtil.toD(num))>(qty/unitrate))){
////                Toast.showText(mContext,"库存不足");
////        } else {

//            if (isHebing) {
//                Log.e("wavehouseID", wavehouseID);
//                Log.e("unitId", unitId);
//                List<T_Detail> detailhebing = t_detailDao.queryBuilder().where(
//                        T_DetailDao.Properties.Activity.eq(activity),
//                        T_DetailDao.Properties.FOrderId.eq(ordercode),
//                        T_DetailDao.Properties.FProductId.eq(product.FItemID),
//                        T_DetailDao.Properties.FBatch.eq(edPihao.getText().toString()),
//                        T_DetailDao.Properties.FUnitId.eq(unitId),
//                        T_DetailDao.Properties.FStorageId.eq(storageId),
//                        T_DetailDao.Properties.FPositionId.eq(wavehouseID==null?"0":wavehouseID)).build().list();
//                if (detailhebing.size() > 0) {
//                    for (int i = 0; i < detailhebing.size(); i++) {
//                        num = (MathUtil.toD(num) + MathUtil.toD(detailhebing.get(i).FQuantity)) + "";
//                        List<T_main> t_mainList = t_mainDao.queryBuilder().where(
//                                T_mainDao.Properties.FIndex.eq(detailhebing.get(i).FIndex)
//                        ).build().list();
//                        t_detailDao.delete(detailhebing.get(i));
//                    }
//                }
//            }
//            List<T_main> delete = t_mainDao.queryBuilder().where(
//                    T_mainDao.Properties.OrderId.eq(ordercode)
//            ).build().list();
//            t_mainDao.deleteInTx(delete);
            String second = getTimesecond();
            T_main t_main = new T_main();
            t_main.FIndex = second;
            t_main.MakerId = share.getsetUserID();
            t_main.DataInput = tvDate.getText().toString();
            t_main.DataPush = tvDate.getText().toString();
            t_main.IMIE = getIMIE();
            t_main.activity = activity;
            t_main.orderId = ordercode;

            t_main.FDepartment = departmentName == null ? "" : departmentName;
            t_main.FDepartmentId = departmentId == null ? "" : departmentId;
            t_main.FPaymentDate = tvDatePay.getText().toString();
            t_main.orderDate = share.getPISdate();
            t_main.FPurchaseUnit = unitName == null ? "" : unitName;
            t_main.FSalesMan = PersonName == null ? "" : PersonName;
            t_main.FSalesManId = PersonId == null ? "" : PersonId;
            t_main.FMaker = share.getUserName();
            t_main.FMakerId = share.getsetUserID();
            t_main.FDirector = managerName == null ? "" : managerName;
            t_main.FDirectorId = managerId == null ? "" : managerId;
            t_main.saleWay = purchaseMethodName == null ? "" : purchaseMethodName;
            t_main.FDeliveryAddress = "";
            t_main.FRemark = edZhaiyao.getText().toString();
            t_main.saleWayId = PurchaseMethodId == null ? "" : PurchaseMethodId;
            t_main.FCustody = capyurePersonName == null ? "" : capyurePersonName;
            t_main.FCustodyId = capturePersonId == null ? "" : capturePersonId;
            t_main.FAcount = wanglaikemuName == null ? "" : wanglaikemuName;
            t_main.FAcountID = wanglaikemuId == null ? "" : wanglaikemuId;
            t_main.Rem = edZhaiyao.getText().toString();
            t_main.supplier = supplierName == null ? "" : supplierName;
            t_main.supplierId = supplierid == null ? "" : supplierid;
            t_main.FSendOutId = signPersonId == null ? "" : signPersonId;
            t_main.FRedBlue = redblue;
            t_main.sourceOrderTypeId = "";
            long insert1 = t_mainDao.insert(t_main);

            T_Detail t_detail = new T_Detail();
            t_detail.FIndex = second;
            t_detail.FBarcode = barcode;
            t_detail.MakerId = share.getsetUserID();
            t_detail.DataInput = tvDate.getText().toString();
            t_detail.DataPush = tvDate.getText().toString();
            t_detail.IMIE = getIMIE();
            t_detail.activity = activity;
            t_detail.FOrderId = ordercode;

            t_detail.FBatch = edPihao.getText().toString();
            t_detail.FProductCode = edCode.getText().toString();
            t_detail.FProductId = product.FItemID;
            t_detail.model = product.FModel;
            t_detail.FProductName = product.FName;
            t_detail.FUnitId = unitId == null ? "" : unitId;
            t_detail.FUnit = unitName == null ? "" : unitName;
            t_detail.FStorage = storageName == null ? "" : storageName;
            t_detail.FStorageId = storageId == null ? "" : storageId;
            t_detail.FPosition = wavehouseName == null ? "" : wavehouseName;
            t_detail.FPositionId = wavehouseID == null ? "0" : wavehouseID;
            t_detail.FDiscount = discount;
            t_detail.FQuantity = num;
            t_detail.unitrate = unitrate;
            t_detail.IsAssemble = barcode.contains("ZZ")?barcode:"";
            t_detail.FKFDate = codeCheckBackDataBean==null?"":codeCheckBackDataBean.FKFDate;
            t_detail.FKFPeriod = codeCheckBackDataBean==null?"":codeCheckBackDataBean.FKFPeriod;
            t_detail.FTaxUnitPrice = edPricesingle.getText().toString();

            long insert = t_detailDao.insert(t_detail);

            if (insert1 > 0 && insert > 0) {
                Toast.showText(mContext, "添加成功");
                MediaPlayer.getInstance(mContext).ok();
                lockScan(0);
//                if (!BasicShareUtil.getInstance(mContext).getIsOL()) {
//                    InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
//                    List<InStorageNum> innum = inStorageNumDao.queryBuilder().where(
//                            InStorageNumDao.Properties.FBatchNo.eq(edPihao.getText().toString()),
//                            InStorageNumDao.Properties.FStockID.eq(storageId)
//                            , InStorageNumDao.Properties.FStockPlaceID.eq(wavehouseID),
//                            InStorageNumDao.Properties.FItemID.eq(product.FItemID)
//                    ).build().list();
//                    if (innum.size() > 0) {
//                        innum.get(0).FQty = (MathUtil.toD(innum.get(0).FQty) + (MathUtil.toD(num) * unitrate)) + "";
//                        inStorageNumDao.update(innum.get(0));
//                    } else {
//                        InStorageNum i = new InStorageNum();
//                        i.FQty = (MathUtil.toD(num) * unitrate) + "";
//                        i.FItemID = product.FItemID;
//                        i.FBatchNo = edPihao.getText().toString();
//                        i.FStockID = storageId;
//                        i.FStockPlaceID = wavehouseID;
//                        inStorageNumDao.insert(i);
//                    }
//                }

                resetAll();
            } else {
                lockScan(0);
                Toast.showText(mContext, "添加失败，请重试");
                MediaPlayer.getInstance(mContext).error();
            }
//        }


    }

    private void resetAll() {
        product=null;
        redorBlue.setVisibility(View.GONE);
        edNum.setText("");
        edPricesingle.setText("");
        edPihao.setText("");
//        edZhaiyao.setText("");
        edCode.setText("");
        tvNuminstorage.setText("");
        tvGoodName.setText("");
        tvModel.setText("");
        setfocus(edCode);
        lockScan(0);
    }

    public void finishOrder() {
        redorBlue.setVisibility(View.VISIBLE);
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle("确认使用完单");
        ab.setMessage("确认？");
        ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ordercode++;
                Log.e("ordercode", ordercode + "");
                share.setPISOrderCode(ordercode);
            }
        });
        ab.setNegativeButton("取消", null);
        ab.create().show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle("确认退出");
        ab.setMessage("退出会自动执行完单,是否退出?");
        ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ordercode++;
                Log.e("ordercode", ordercode + "");
                share.setPISOrderCode(ordercode);
                finish();
            }
        });
        ab.setNegativeButton("取消", null);
        ab.create().show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("按键", keyCode + "");
        return super.onKeyDown(keyCode, event);
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

    //用于adpater首次更新时，不存入默认值，而是选中之前的选项
    private boolean isFirst=false;
    private boolean isFirst2=false;
    private boolean isFirst3=false;
    private boolean isFirst4=false;
    private boolean isFirst5=false;
    private boolean isFirst6=false;
    private boolean isFirst7=false;
}
