package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.DepartmentSpAdapter;
import com.fangzuo.assist.Adapter.EmployeeSpAdapter;
import com.fangzuo.assist.Adapter.PayMethodSpAdapter;
import com.fangzuo.assist.Adapter.PayTypeSpAdapter;
import com.fangzuo.assist.Adapter.PiciSpAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter1;
import com.fangzuo.assist.Adapter.StorageSpAdapter;
import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Adapter.WaveHouseSpAdapter;
import com.fangzuo.assist.Adapter.YuandanSpAdapter;
import com.fangzuo.assist.Beans.CodeCheckBackDataBean;
import com.fangzuo.assist.Beans.CodeCheckBean;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.GetBatchNoBean;
import com.fangzuo.assist.Beans.InStoreNumBean;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.BarCode;
import com.fangzuo.assist.Dao.Department;
import com.fangzuo.assist.Dao.Employee;
import com.fangzuo.assist.Dao.InStorageNum;
import com.fangzuo.assist.Dao.PayType;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.PurchaseMethod;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.Dao.Unit;
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
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


//                  销售出库
public class SoldOutActivity extends BaseActivity {


    @BindView(R.id.drawer)
    DrawerLayout mDrawer;
    @BindView(R.id.ishebing)
    CheckBox cbHebing;
    @BindView(R.id.sp_send_storage)
    Spinner spSendStorage;              //发货仓库
    @BindView(R.id.ed_client)
    EditText edClient;                  //客户输入框
    @BindView(R.id.search_supplier)
    RelativeLayout searchSupplier;
    @BindView(R.id.scanbyCamera)
    RelativeLayout scanbyCamera;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.search)
    RelativeLayout search;
    @BindView(R.id.tv_goodName)
    TextView tvGoodName;
    @BindView(R.id.tv_model)
    TextView tvModel;
    @BindView(R.id.tv_numinstorage)
    TextView tvNuminstorage;            //库存
    @BindView(R.id.sp_wavehouse)
    MyWaveHouseSpinner spWavehouse;
    @BindView(R.id.ed_onsale)
    EditText edOnsale;                  //折扣率
    @BindView(R.id.ed_num)
    EditText edNum;
    @BindView(R.id.sp_unit)
    SpinnerUnit spUnit;
    @BindView(R.id.ed_pricesingle)
    EditText edPricesingle;
    @BindView(R.id.ed_zhaiyao)
    EditText edZhaiyao;
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
    @BindView(R.id.sp_sale_scope)
    Spinner spSaleScope;             //销售范围
    @BindView(R.id.sp_saleMethod)
    Spinner spSaleMethod;           //销售方式
    @BindView(R.id.sp_yuandan)
    Spinner spYuandan;              //保管
    @BindView(R.id.sp_sendMethod)
    Spinner spSendMethod;           //交货方式
    @BindView(R.id.sp_payMethod)
    Spinner spPayMethod;            //结算方式
    @BindView(R.id.sp_sendplace)
    Spinner spSendplace;            //交货地点
    @BindView(R.id.sp_department)
    Spinner spDepartment;           //部门
    @BindView(R.id.sp_employee)
    Spinner spEmployee;             //业务员
    @BindView(R.id.sp_manager)
    Spinner spManager;              //主管
    @BindView(R.id.isAutoAdd)
    CheckBox autoAdd;
    @BindView(R.id.cb_isStorage)
    CheckBox cbIsStorage;           //是否带出默认仓库
    @BindView(R.id.sp_pihao)
    Spinner spPihao;
    @BindView(R.id.blue)
    RadioButton blue;
    @BindView(R.id.red)
    RadioButton red;
    @BindView(R.id.redorBlue)
    RadioGroup redorBlue;
    @BindView(R.id.ed_pihao)
    EditText edPihao;
    private SoldOutActivity mContext;
    private DecimalFormat df;
    private DaoSession daoSession;
    private int year;
    private int month;
    private int day;
    private long ordercode;         //获取当前时间段
    private StorageSpAdapter storageSpAdapter;
    private CommonMethod method;
    private PayMethodSpAdapter slaesRange;
    private PayMethodSpAdapter saleMethodSpAdapter;
    private EmployeeSpAdapter employeeAdapter;
    private YuandanSpAdapter yuandanSpAdapter;
    private PayTypeSpAdapter payTypeSpAdapter;
    private DepartmentSpAdapter departMentAdapter;
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
    private String sendMethodId;
    private String sendMethodName;
    private List<Product> products;
    private UnitSpAdapter unitAdapter;
    private String unitId;
    private String unitName;
    private double unitrate;
    private boolean fBatchManager;
    private String pihao;
    private Storage storage;
    private WaveHouseSpAdapter waveHouseAdapter;
    private String storageId;
    private String storageName;
    private String wavehouseID = "0";
    private String wavehouseName;
    private String clientId;
    private String clientName;
    private String date;
    private String datePay;
    private boolean isHebing = true;
    public static final int SOLDOUT = 6452;
    private Double qty;
    private T_mainDao t_mainDao;
    private T_DetailDao t_detailDao;
    private boolean isAuto;
    private boolean isGetStorage;
    private Product product;
    private ProductselectAdapter productselectAdapter;
    private ProductselectAdapter1 productselectAdapter1;
    private PiciSpAdapter pici;
    private boolean isRed = false;
    private String redblue = "蓝字";
    private String default_unitID;
    private Double storenum;
    private boolean checkStorage=false;  // 0不允许负库存false  1允许负库存出库true
    private String wavehouseAutoString="";
    private CodeCheckBackDataBean codeCheckBackDataBean;
    private int activity = Config.SoldOutActivity;
    @Override
    public void initView() {
        setContentView(R.layout.activity_sold_out);
        mContext = this;
        ButterKnife.bind(mContext);
        share = ShareUtil.getInstance(mContext);

        initDrawer(mDrawer);
        df = new DecimalFormat("######0.00");
        daoSession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        t_detailDao = daoSession.getT_DetailDao();
        t_mainDao=daoSession.getT_mainDao();

        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        initDrawer(mDrawer);
        cbHebing.setChecked(true);
        edOnsale.setText("0");

        autoAdd.setChecked(share.getSOUTisAuto());
        isAuto = share.getSOUTisAuto();
    }
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg){
//            case EventBusInfoCode.PRODUCTRETURN:
//                product = (Product)event.postEvent;
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
                lockScan(0);
                codeCheckBackDataBean = (CodeCheckBackDataBean)event.postEvent;
                Toast.showText(mContext, codeCheckBackDataBean.FTip);
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
    public void initData() {
        method = CommonMethod.getMethod(mContext);
        //获取当前时间值
        if (share.getSOUTOrderCode() == 0) {
            ordercode = Long.parseLong(getTime(false) + "001");
            Log.e("ordercode", ordercode + "");
            share.setSOUTOrderCode(ordercode);
        } else {
            ordercode = share.getSOUTOrderCode();
            Log.e("ordercode", ordercode + "");
        }
        //初始化各种spinner
        LoadBasicData();
    }

    private void LoadBasicData() {

        storageSpAdapter = method.getStorageSpinner(spSendStorage);
        slaesRange = method.getPurchaseRange(spSaleScope);
        saleMethodSpAdapter = method.getSaleMethodSpinner(spSaleMethod);
        payTypeSpAdapter = method.getpayType(spPayMethod);
        departMentAdapter = method.getDepartMentAdapter(spDepartment);
        employeeAdapter = method.getEmployeeAdapter(spEmployee);
        method.getEmployeeAdapter(spManager);
        getGoodsType = method.getGoodsTypes(spSendMethod);


        spManager.setAdapter(employeeAdapter);
        spEmployee.setAdapter(employeeAdapter);
        spYuandan.setAdapter(employeeAdapter);

//        spDepartment.setSelection(share.getSOUTDepartment());
//        spSaleMethod.setSelection(share.getSOUTsaleMethod());
//        spSaleScope.setSelection(share.getSoutSaleRange());
//        spYuandan.setSelection(share.getSOUTYuandan());
//        spPayMethod.setSelection(share.getSOUTPayMethod());
//        spEmployee.setSelection(share.getSOUTEmployee());
//        spManager.setSelection(share.getSOUTManager());
//        spSendMethod.setSelection(share.getSOUTsendmethod());


        tvDate.setText(share.getSOUTdate());
        tvDatePay.setText(share.getSOUTdatepay());
    }

    @Override
    public void initListener() {
        redorBlue.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                isRed = radioButton.getText().toString().equals("红字");
                redblue = isRed ? "红字" : "蓝字";

            }
        });
        cbIsStorage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isGetStorage = b;
            }
        });
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
                share.setSOUTisAuto(b);
            }
        });

//        //批号
//        spPihao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                InStorageNum inStorageNum = (InStorageNum) pici.getItem(i);
//                pihao = inStorageNum.FBatchNo;
//                getInstorageNum(product);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
        //扫码输入框
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

        //部门
        spDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Department department = (Department) departMentAdapter.getItem(i);
                departmentId = department.FItemID;
                departmentName = department.FName;
//                share.setSOUTDepartment(i);
                if (isFirst){
                    share.setSOUTDepartment(i);
                    spDepartment.setSelection(i);
                }
                else{
                    spDepartment.setSelection(share.getSOUTDepartment());
                    isFirst=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //销售方式
        spSaleMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PurchaseMethod item = (PurchaseMethod) saleMethodSpAdapter.getItem(i);
                SaleMethodId = item.FItemID;
                SaleMethodName = item.FName;
//                share.setSOUTsaleMethod(i);
                if (isFirst2){
                    share.setSOUTsaleMethod(i);
                    spSaleMethod.setSelection(i);
                }
                else{
                    spSaleMethod.setSelection(share.getSOUTsaleMethod());
                    isFirst2=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //销售范围
        spSaleScope.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PurchaseMethod purchaseMethod = (PurchaseMethod) slaesRange.getItem(i);
                saleRangeId = purchaseMethod.FItemID;
                saleRangeName = purchaseMethod.FName;
//                share.setSoutSaleRange(i);
                if (isFirst3){
                    share.setSoutSaleRange(i);
                    spSaleScope.setSelection(i);
                }
                else{
                    spSaleScope.setSelection(share.getSoutSaleRange());
                    isFirst3=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //保管
        spYuandan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Employee employee = (Employee) employeeAdapter.getItem(i);
                yuandanID = employee.FItemID;
                yuandanName = employee.FName;
//                share.setSOUTYuandan(i);
                if (isFirst4){
                    share.setSOUTYuandan(i);
                    spYuandan.setSelection(i);
                }
                else{
                    spYuandan.setSelection(share.getSOUTYuandan());
                    isFirst4=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //结算方式
        spPayMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PayType payType = (PayType) payTypeSpAdapter.getItem(i);
                payTypeId = payType.FItemID;
                payTypeName = payType.FName;
//                share.setSOUTPayMethod(i);
                if (isFirst5){
                    share.setSOUTPayMethod(i);
                    spPayMethod.setSelection(i);
                }
                else{
                    spPayMethod.setSelection(share.getSOUTPayMethod());
                    isFirst5=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //业务员
        spEmployee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Employee employee = (Employee) employeeAdapter.getItem(i);
                employeeId = employee.FItemID;
                employeeName = employee.FName;
//                share.setSOUTEmployee(i);
                if (isFirst6){
                    share.setSOUTEmployee(i);
                    spEmployee.setSelection(i);
                }
                else{
                    spEmployee.setSelection(share.getSOUTEmployee());
                    isFirst6=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //主管
        spManager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Employee employee = (Employee) employeeAdapter.getItem(i);
                ManagerId = employee.FItemID;
                ManagerName = employee.FName;
//                share.setSOUTManager(i);
                if (isFirst7){
                    share.setSOUTManager(i);
                    spManager.setSelection(i);
                }
                else{
                    spManager.setSelection(share.getSOUTManager());
                    isFirst7=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //交货方式
        spSendMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PurchaseMethod purchaseMethod = (PurchaseMethod) getGoodsType.getItem(i);
                sendMethodId = purchaseMethod.FItemID;
                sendMethodName = purchaseMethod.FName;
//                share.setSOUTsendmethod(i);
                if (isFirst8){
                    share.setSOUTsendmethod(i);
                    spSendMethod.setSelection(i);
                }
                else{
                    spSendMethod.setSelection(share.getSOUTsendmethod());
                    isFirst8=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //发货仓库
        spSendStorage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                storage = (Storage) storageSpAdapter.getItem(i);
                Lg.e("仓库："+storage.toString());
                if ("1".equals(storage.FUnderStock)){
                    checkStorage=true;
                }else{
                    checkStorage=false;
                }
//                waveHouseAdapter = CommonMethod.getMethod(mContext).getWaveHouseAdapter(storage, spWavehouse);
                wavehouseID = "0";
                spWavehouse.setAuto(mContext,storage,wavehouseAutoString);
                storageId = storage.FItemID;
                storageName = storage.FName;
//                getPici();
                Log.e("点击仓库storageId", storageId);
                Log.e("点击仓库storageName", storageName);

                //获取仓库对应的仓位
                getInstorageNum(product);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //单位
        spUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Unit unit = (Unit) spUnit.getAdapter().getItem(i);
                if (unit != null) {
                    unitId = unit.FMeasureUnitID;
                    unitName = unit.FName;
                    unitrate = Double.parseDouble(unit.FCoefficient);
                    Log.e("1111", unitrate + "");
                }

                getInstorageNum(product);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //仓位
        spWavehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WaveHouse waveHouse = (WaveHouse) spWavehouse.getAdapter().getItem(i);
                wavehouseID = waveHouse.FSPID;
                wavehouseName = waveHouse.FName;
                Log.e("点击仓位wavehouse：", wavehouseName);
//                getPici();

                getInstorageNum(product);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private String barcode = "";
    @Override
    protected void OnReceive(String code) {
//        setDATA(code, false);
        barcode = code;
        edNum.setEnabled(!barcode.contains("ZZ"));
        LoadingUtil.showDialog(mContext,"正在查找...");
        //查询条码唯一表
        CodeCheckBean bean = new CodeCheckBean(barcode);
        if (isRed){
            DataModel.codeCheckForOutForRed(gson.toJson(bean));
        }else{
            DataModel.codeCheckForOut(gson.toJson(bean));
        }
    }


    //设置或查找product
    private void setDATA(String fnumber, boolean flag) {
        default_unitID = null;
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
//                            Log.e(TAG,"setDATA联网获取product数据:"+dBean.products.get(0));
//                            getProductOL(dBean, 0);
//                            default_unitID = dBean.products.get(0).FUnitID;
//                            chooseUnit(default_unitID);
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
//                                    default_unitID = dBean.products.get(i).FUnitID;
//                                    chooseUnit(default_unitID);
//                                    alertDialog.dismiss();
//                                }
//                            });
//                        }
//                    }
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
//
//                if (barCodes.size() > 0) {
//                    if (barCodes.size() == 1) {
//                        products = productDao.queryBuilder().where(
//                                ProductDao.Properties.FItemID.eq(barCodes.get(0).FItemID)
//                        ).build().list();
//                        default_unitID = barCodes.get(0).FUnitID;
//                        Log.e(TAG,"setDATA联网获取product数据:"+products.get(0));
//                        getProductOFL(products);
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
//                                products = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(barCode.FItemID)).build().list();
//                                default_unitID = barCode.FUnitID;
//                                getProductOFL(products);
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
    private void getProductOL(DownloadReturnBean dBean, int j) {
        product = dBean.products.get(j);
        spUnit.setAuto(mContext,product.FUnitGroupID,codeCheckBackDataBean.FUnitID,SpinnerUnit.Id);
        tvorisAuto(product);
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
//
//
//    private void getProductOFL(List<Product> list)
//     {
//        if (list != null && list.size() > 0) {
//            product = list.get(0);
//            tvorisAuto(product);
//        } else {
//            Toast.showText(mContext, "未找到物料");
//            edCode.setText("");
//            setfocus(edCode);
//        }
//    }

    //获取批次
    private void getPici() {
        List<InStorageNum> container1 = new ArrayList<>();
        pici = new PiciSpAdapter(mContext, container1);
        spPihao.setAdapter(pici);
        if (fBatchManager) {
            spPihao.setEnabled(true);
            if (!BasicShareUtil.getInstance(mContext).getIsOL()) {
                pici = CommonMethod.getMethod(mContext).getPici(storage, wavehouseID, product, spPihao);
            } else {
                final List<InStorageNum> container = new ArrayList<>();
//                pici = new PiciSpAdapter(mContext, container);
//                spPihao.setAdapter(pici);

                GetBatchNoBean bean = new GetBatchNoBean();
                bean.ProductID=product.FItemID;
                bean.StorageID=storageId;
                bean.WaveHouseID=wavehouseID;
                String json = new Gson().toJson(bean);
                Log.e(TAG, "getPici批次提交："+json);
                Asynchttp.post(mContext, getBaseUrl() + WebApi.GETPICI, json, new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        Log.e(TAG,"getPici获取数据："+cBean.returnJson);
                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                        if(dBean.InstorageNum!=null){
                            for (int i = 0; i < dBean.InstorageNum.size(); i++) {
                                if (dBean.InstorageNum.get(i).FQty != null
                                        && Double.parseDouble(dBean.InstorageNum.get(i).FQty) > 0) {
                                    Log.e(TAG,"有库存的批次："+dBean.InstorageNum.get(i).toString());
                                    container.add(dBean.InstorageNum.get(i));
                                }
                            }
                            pici = new PiciSpAdapter(mContext, container);
                            spPihao.setAdapter(pici);
                        }
                        pici.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        Log.e(TAG,"getPici获取数据错误："+Msg);
                        Toast.showText(mContext, Msg);
                    }
                });
            }
        } else {
            spPihao.setEnabled(false);
        }
    }

    private void tvorisAuto(final Product product) {
        edCode.setText(product.FNumber);
        tvModel.setText(product.FModel);
//        wavehouseAutoString=product.FSPID;
        wavehouseAutoString=codeCheckBackDataBean.FStockPlaceID;
        edPricesingle.setText(df.format(Double.parseDouble(product.FSalePrice)));
        tvGoodName.setText(product.FName);
        fBatchManager = (product.FBatchManager) != null && (product.FBatchManager).equals("1");
        if (wavehouseID == null) {
            wavehouseID = "0";
        }
        if (true) {
            for (int j = 0; j < storageSpAdapter.getCount(); j++) {
                if (((Storage)storageSpAdapter.getItem(j)).FItemID.equals(codeCheckBackDataBean.FStockID)) {
                    spSendStorage.setSelection(j);
                    break;
                }
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    spWavehouse.setAuto(mContext,storage,wavehouseAutoString);
//                    for (int j = 0; j < waveHouseAdapter.getCount(); j++) {
//                        if (((WaveHouse)waveHouseAdapter.getItem(j)).FSPID.equals(product.FSPID)) {
//                            spWavehouse.setSelection(j);
//                            break;
//                        }
//                    }
                }
            },100);
        }
//        getPici();
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
        getInstorageNum(product);

        if (isAuto) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    edNum.setText("1.0");
                    edOnsale.setText("0");
                    AddorderBefore();
                }
            }, 150);

        }else{
            lockScan(0);
        }
    }


    //获取库存
    private void getInstorageNum(Product product) {
        if (product == null){
            return;
        }
        Log.e(TAG,"getInstorageNum");
        if (fBatchManager) {
            pihao = edPihao.getText().toString();
            if (pihao == null || pihao.equals("")) {
                pihao = "0";
            }
        } else {
            pihao = "";
        }
        if (wavehouseID == null) {
            wavehouseID = "0";
        }
        if (BasicShareUtil.getInstance(mContext).getIsOL()) {
            InStoreNumBean iBean = new InStoreNumBean();
            iBean.FStockPlaceID = wavehouseID;      //仓位ID
            iBean.FBatchNo = pihao;         //批次
            iBean.FStockID = storage.FItemID;       //
            iBean.FItemID = product.FItemID;
            String json = new Gson().toJson(iBean);
            Log.e(TAG, "getInstorageNum库存提交："+json);
            Asynchttp.post(mContext, getBaseUrl() + WebApi.GETINSTORENUM, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    Log.e(TAG,"库存返回："+cBean.returnJson);
                    storenum = Double.parseDouble(cBean.returnJson);
                    tvNuminstorage.setText((storenum / unitrate) + "");
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Log.e(TAG,"库存错误返回："+Msg);
                    Toast.showText(mContext, Msg);
                    tvNuminstorage.setText("0");
                    storenum = 0.0;
                }
            });
        } else {
            InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
            List<InStorageNum> list1 = inStorageNumDao.queryBuilder().
                    where(InStorageNumDao.Properties.FItemID.eq(product.FItemID),
                            InStorageNumDao.Properties.FStockID.eq(storage.FItemID),
                            InStorageNumDao.Properties.FStockPlaceID.eq(wavehouseID),
                            InStorageNumDao.Properties.FBatchNo.eq(pihao)).build().list();
            Log.e("SoldOutActivity", "list1.size():" + list1.size());
            Log.e("SoldOutActivity", product.FItemID);
            Log.e("SoldOutActivity", wavehouseID);
            Log.e("SoldOutActivity", "pici:" + pihao);
            Log.e("SoldOutActivity", storage.FItemID);
            if (list1.size() > 0) {
                Log.e("FQty", list1.get(0).FQty);
                storenum = Double.parseDouble(list1.get(0).FQty);
                Log.e("qty", storenum + "");
                if (storenum != null) {
                    tvNuminstorage.setText((storenum / unitrate) + "");
                }

            } else {
                storenum = 0.0;
                tvNuminstorage.setText("0");
            }

        }


    }


    @OnClick({R.id.search_supplier, R.id.scanbyCamera, R.id.search, R.id.btn_add, R.id.btn_finishorder, R.id.btn_backorder, R.id.btn_checkorder, R.id.tv_date, R.id.tv_date_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //选择客户
            case R.id.search_supplier:
                Bundle b = new Bundle();
                b.putString("search", edClient.getText().toString());
                b.putInt("where", Info.SEARCHCLIENT);
                startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.SEARCHFORRESULTCLIRNT, b);
                break;
            case R.id.scanbyCamera:
                Intent in = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(in, 0);
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
                Bundle b2 = new Bundle();
                b2.putInt("activity", activity);
                startNewActivity(TableActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, false, b2);
                break;
            case R.id.tv_date:
                getdate();
                break;
            case R.id.tv_date_pay:
                getPaydate();
                break;
        }
    }

    public void finishOrder() {
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle("确认使用完单");
        ab.setMessage("确认？");
        ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                red.setClickable(true);
                blue.setClickable(true);
                red.setBackgroundColor(Color.WHITE);
                blue.setBackgroundColor(Color.WHITE);
                ordercode++;
                Log.e("ordercode", ordercode + "");
                share.setSOUTOrderCode(ordercode);
            }
        });
        ab.setNegativeButton("取消", null);
        ab.create().show();

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
        } else if (requestCode == Info.SEARCHFORRESULTCLIRNT) {
            if (resultCode == Info.SEARCHFORRESULTCLIRNT) {
                Bundle b = data.getExtras();
                clientId = b.getString("001");
                clientName = b.getString("002");
                edClient.setText(clientName);
                setfocus(edCode);
            }
        }
    }

    private void AddorderBefore(){
        if (wavehouseID == null) {
            wavehouseID = "0";
        }
        String discount = edOnsale.getText().toString();
        String num = edNum.getText().toString();

        if (isRed) {
            num = "-" + edNum.getText().toString();
        } else {
            num = edNum.getText().toString();
        }
        if (edCode.getText().toString().equals("")) {
            Toast.showText(mContext, "请输入物料编号");
            MediaPlayer.getInstance(mContext).error();
            lockScan(0);
            return;
        }
        if (clientId==null) {
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "请选择客户");
            lockScan(0);
            return;
        }
        if (edPricesingle.getText().toString().equals("")) {
            Toast.showText(mContext, "请输入单价");
            MediaPlayer.getInstance(mContext).error();
            lockScan(0);
            return;
        }
        if (MathUtil.toD(edNum.getText().toString())<=0) {
            Toast.showText(mContext, "请输入数量");
            MediaPlayer.getInstance(mContext).error();
            lockScan(0);
            return;
        }
        if (edOnsale.getText().toString().equals("")) {
            Toast.showText(mContext, "请输入折扣");
            MediaPlayer.getInstance(mContext).error();
            lockScan(0);
            return;
        }
        if (edClient.getText().toString().equals("")) {
            Toast.showText(mContext, "请输入客户");
            MediaPlayer.getInstance(mContext).error();
            lockScan(0);
            return;
        }

        //是否开启库存管理 true，开启允许负库存
        if (!checkStorage && !isRed) {
            if ((storenum / unitrate) < Double.parseDouble(num)) {
                MediaPlayer.getInstance(mContext).error();
                Toast.showText(mContext, "大兄弟，库存不够了");
                lockScan(0);
                return;
            }
        }
        if (null == codeCheckBackDataBean){
            lockScan(0);
            return;
        }
        if (Double.parseDouble(codeCheckBackDataBean.FQty)<Double.parseDouble(edNum.getText().toString())){
            Toast.showText(mContext, "修改的数量不能大于扫码数量");
            MediaPlayer.getInstance(mContext).error();
            lockScan(0);
            return;
        }

        //插入条码唯一临时表
        CodeCheckBean bean = new CodeCheckBean(barcode,ordercode + "",num,BasicShareUtil.getInstance(mContext).getIMIE());
        DataModel.codeInsertForOutForRed(gson.toJson(bean));
    }
    private void Addorder() {
        if (wavehouseID == null) {
            wavehouseID = "0";
        }
        String discount = edOnsale.getText().toString();
        String num = edNum.getText().toString();
        if (isRed) {
            num = "-" + edNum.getText().toString();
        } else {
            num = edNum.getText().toString();
        }
        pihao = edPihao.getText().toString();
//        T_DetailDao t_detailDao = daoSession.getT_DetailDao();
//        T_mainDao t_mainDao = daoSession.getT_mainDao();
//
//        if (isRed) {
//            num = "-" + edNum.getText().toString();
//        } else {
//            num = edNum.getText().toString();
//        }
//        if (edCode.getText().toString().equals("")) {
//            Toast.showText(mContext, "请输入物料编号");
//            MediaPlayer.getInstance(mContext).error();
//            return;
//        }
//
//        if (edPricesingle.getText().toString().equals("")) {
//            Toast.showText(mContext, "请输入单价");
//            MediaPlayer.getInstance(mContext).error();
//            return;
//        }
//        if (edNum.getText().toString().equals("")) {
//            Toast.showText(mContext, "请输入数量");
//            MediaPlayer.getInstance(mContext).error();
//            return;
//        }
//        if (edOnsale.getText().toString().equals("")) {
//            Toast.showText(mContext, "请输入折扣");
//            MediaPlayer.getInstance(mContext).error();
//            return;
//        }
//        if (edClient.getText().toString().equals("")) {
//            Toast.showText(mContext, "请输入客户");
//            MediaPlayer.getInstance(mContext).error();
//            return;
//        }
//
//        //是否开启库存管理 true，开启允许负库存
//        if (!checkStorage && !isRed) {
//            if ((storenum / unitrate) < Double.parseDouble(num)) {
//                MediaPlayer.getInstance(mContext).error();
//                Toast.showText(mContext, "大兄弟，库存不够了");
//                return;
//            }
//        }
//
//        if (isHebing) {
//            List<T_Detail> detailhebing = t_detailDao.queryBuilder().where(
//                    T_DetailDao.Properties.Activity.eq(activity),
//                    T_DetailDao.Properties.FOrderId.eq(ordercode),
//                    T_DetailDao.Properties.FProductId.eq(product.FItemID),
//                    T_DetailDao.Properties.FBatch.eq(pihao == null ? "" : pihao),
//                    T_DetailDao.Properties.FUnitId.eq(unitId),
//                    T_DetailDao.Properties.FStorageId.eq(storageId),
//                    T_DetailDao.Properties.FDiscount.eq(discount),
//                    T_DetailDao.Properties.FPositionId.eq(wavehouseID),
//                    T_DetailDao.Properties.FDiscount.eq(discount)
//            ).build().list();
//            if (detailhebing.size() > 0) {
//                for (int i = 0; i < detailhebing.size(); i++) {
//                    num = (Double.parseDouble(num) + Double.parseDouble(detailhebing.get(i).FQuantity)) + "";
//                    t_detailDao.delete(detailhebing.get(i));
//                }
//            }
//        }
//        List<T_main> dewlete = t_mainDao.queryBuilder().where(T_mainDao.Properties.OrderId.eq(ordercode)).build().list();
//        t_mainDao.deleteInTx(dewlete);
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
        t_main.orderDate = tvDate.getText().toString();
        t_main.FPurchaseUnit = unitName == null ? "" : unitName;
        t_main.FSalesMan = employeeName == null ? "" : employeeName;
        t_main.FSalesManId = employeeId == null ? "" : employeeId;
        t_main.FMaker = share.getUserName();
        t_main.FMakerId = share.getsetUserID();
        t_main.FDirector = ManagerName == null ? "" : ManagerName;
        t_main.FDirectorId = ManagerId == null ? "" : ManagerId;
        t_main.saleWay = SaleMethodName == null ? "" : SaleMethodName;
        t_main.FDeliveryAddress = "";
        t_main.FRedBlue = redblue;
        t_main.FRemark = edZhaiyao.getText().toString();
        t_main.saleWayId = SaleMethodId == null ? "" : SaleMethodId;
        t_main.FCustody = saleRangeName == null ? "" : saleRangeName;
        t_main.FCustodyId = saleRangeId == null ? "" : saleRangeId;
        t_main.FAcount = sendMethodName == null ? "" : sendMethodName;
        t_main.FAcountID = sendMethodId == null ? "" : sendMethodId;
        t_main.Rem = edZhaiyao.getText().toString();
        t_main.supplier = clientName == null ? "" : clientName;
        t_main.supplierId = clientId == null ? "" : clientId;
        t_main.FSendOutId = payTypeId == null ? "" : payTypeId;
        t_main.sourceOrderTypeId = yuandanID == null ? "" : yuandanID;


        T_Detail t_detail = new T_Detail();
        t_detail.FIndex = second;
        t_detail.FBarcode = barcode;
        t_detail.MakerId = share.getsetUserID();
        t_detail.DataInput = tvDate.getText().toString();
        t_detail.DataPush = tvDate.getText().toString();
        t_detail.IMIE = getIMIE();
        t_detail.activity = activity;
        t_detail.FOrderId = ordercode;

        t_detail.FBatch = pihao == null ? "" : pihao;
        t_detail.FRedBlue = redblue;
        t_detail.FProductCode = edCode.getText().toString();
        t_detail.FProductId = product.FItemID;
        t_detail.model = product.FModel;
        t_detail.FProductName = product.FName;
        t_detail.FUnitId = unitId == null ? "" : unitId;
        t_detail.FUnit = unitName == null ? "" : unitName;
        t_detail.FStorage = storageName == null ? "" : storageName;
        t_detail.FStorageId = storageId == null ? "0" : storageId;
        t_detail.FPosition = wavehouseName == null ? "" : wavehouseName;
        t_detail.FPositionId = wavehouseID == null ? "0" : wavehouseID;
        t_detail.FDiscount = discount;
        t_detail.FQuantity = num;
        t_detail.unitrate = unitrate;
        t_detail.FTaxUnitPrice = edPricesingle.getText().toString();

        t_detail.IsAssemble = barcode.contains("ZZ")?barcode:"";
        t_detail.FKFDate = codeCheckBackDataBean==null?"":codeCheckBackDataBean.FKFDate;
        t_detail.FKFPeriod = codeCheckBackDataBean==null?"":codeCheckBackDataBean.FKFPeriod;

//        if (!BasicShareUtil.getInstance(mContext).getIsOL()) {
//            long insert = t_detailDao.insert(t_detail);
//            long insert1 = t_mainDao.insert(t_main);
//            if (insert > 0 && insert1 > 0) {
//                InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
//                List<InStorageNum> innum = inStorageNumDao.queryBuilder().where(
//                        InStorageNumDao.Properties.FItemID.eq(product.FItemID),
//                        InStorageNumDao.Properties.FStockID.eq(storageId),
//                        InStorageNumDao.Properties.FStockPlaceID.eq(wavehouseID == null ? "0" : wavehouseID),
//                        InStorageNumDao.Properties.FBatchNo.eq(pihao == null ? "" : pihao)
//                ).build().list();
//                Toast.showText(mContext, "添加成功");
//                MediaPlayer.getInstance(mContext).ok();
////                Log.e("qty_insert", Double.parseDouble(innum.get(0).FQty) + "");
////                Log.e("qty_insert", (Double.parseDouble(edNum.getText().toString()) * unitrate) + "");
////                Log.e("qty_insert", (unitrate) + "");
//                if (isRed) {
//                    if (innum.size() == 0) {
//                        InStorageNum inStorageNum = new InStorageNum();
//                        inStorageNum.FItemID = product.FItemID;
//                        inStorageNum.FBatchNo = pihao == null ? "" : pihao;
//                        inStorageNum.FStockPlaceID = wavehouseID;
//                        inStorageNum.FStockID = storageId;
//                        inStorageNum.FQty = (Double.parseDouble(edNum.getText().toString()) * unitrate) + "";
//                        inStorageNumDao.insert(inStorageNum);
//                    } else {
//                        innum.get(0).FQty = String.valueOf(((Double.parseDouble(innum.get(0).FQty) + (Double.parseDouble(edNum.getText().toString()) * unitrate))));
//                    }
//
//                } else {
//                    innum.get(0).FQty = String.valueOf(((Double.parseDouble(innum.get(0).FQty) - (Double.parseDouble(edNum.getText().toString()) * unitrate))));
//                }
//                if (innum.size()!=0){
//                    inStorageNumDao.update(innum.get(0));
//                }
//                resetAll();
//            } else {
//                Toast.showText(mContext, "添加失败");
//                MediaPlayer.getInstance(mContext).error();
//            }
//        } else {
            long insert = t_detailDao.insert(t_detail);
            long insert1 = t_mainDao.insert(t_main);
            if (insert > 0 && insert1 > 0) {
                lockScan(0);
                Toast.showText(mContext, "添加成功");
                MediaPlayer.getInstance(mContext).ok();
                resetAll();
            }else{
                lockScan(0);
            }
//        }

    }

    private void resetAll() {
        codeCheckBackDataBean =null;
        product=null;
        red.setClickable(false);
        blue.setClickable(false);
        red.setBackgroundColor(Color.GRAY);
        blue.setBackgroundColor(Color.GRAY);
        edNum.setText("");
        edPricesingle.setText("");
//        edZhaiyao.setText("");
        edCode.setText("");
        tvNuminstorage.setText("");
        tvGoodName.setText("");
        tvModel.setText("");
        edOnsale.setText("0");
        List<InStorageNum> container = new ArrayList<>();
        pici = new PiciSpAdapter(mContext, container);
        spPihao.setAdapter(pici);
        setfocus(edCode);
    }

    private void upload() {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        PurchaseInStoreUploadBean.purchaseInStore puBean = pBean.new purchaseInStore();
        t_mainDao = daoSession.getT_mainDao();
        t_detailDao = daoSession.getT_DetailDao();
        ArrayList<String> detailContainer = new ArrayList<>();
        ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data = new ArrayList<>();
        List<T_main> mains = t_mainDao.queryBuilder().where(T_mainDao.Properties.Activity.eq(activity)
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
                        t_main.FSalesManId + "|" +
                        t_main.sourceOrderTypeId + "|" +
                        t_main.FRedBlue + "|" +
                        t_main.FCustodyId + "|" +
                        t_main.sourceOrderTypeId + "|"+
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
        DataModel.upload(mContext,getBaseUrl()+ WebApi.UPLOADSOUT,gson.toJson(pBean));
//        postToServer(data);
    }

    private void postToServer(ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data) {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        pBean.list = data;
        Gson gson = new Gson();
        Asynchttp.post(mContext, getBaseUrl() + WebApi.UPLOADSOUT, gson.toJson(pBean), new Asynchttp.Response() {
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

    private void getPaydate() {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

            }
        }, year, month, day);

        datePickerDialog.setButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int year = datePickerDialog.getDatePicker().getYear();
                int month = datePickerDialog.getDatePicker().getMonth();
                int day = datePickerDialog.getDatePicker().getDayOfMonth();
                datePay = year + "-" + ((month < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((day < 10) ? "0" + day : day);
                tvDatePay.setText(datePay);
                Toast.showText(mContext, datePay);
                datePickerDialog.dismiss();

            }
        });
        datePickerDialog.show();
    }

    private void getdate() {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            }
        }, year, month, day);

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
    private boolean isFirst8=false;
}
