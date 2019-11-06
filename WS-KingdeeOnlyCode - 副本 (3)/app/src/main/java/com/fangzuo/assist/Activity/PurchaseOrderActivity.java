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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.DepartmentSpAdapter;
import com.fangzuo.assist.Adapter.EmployeeSpAdapter;
import com.fangzuo.assist.Adapter.PayMethodSpAdapter;
import com.fangzuo.assist.Adapter.PayTypeSpAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter1;
import com.fangzuo.assist.Adapter.PurchaseMethodSpAdapter;
import com.fangzuo.assist.Adapter.PurchaseScopeSpAdapter;
import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Adapter.YuandanSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.BarCode;
import com.fangzuo.assist.Dao.Department;
import com.fangzuo.assist.Dao.Employee;
import com.fangzuo.assist.Dao.PayType;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.PurchaseMethod;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.Dao.Unit;
import com.fangzuo.assist.Dao.YuandanType;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.CommonMethod;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MediaPlayer;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.assist.widget.SpinnerPeople;
import com.fangzuo.assist.widget.SpinnerUnit;
import com.fangzuo.assist.widget.TextViewCard;
import com.fangzuo.assist.zxing.activity.CaptureActivity;
import com.fangzuo.greendao.gen.BarCodeDao;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.ProductDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.orhanobut.hawk.Hawk;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


//              采购订单
public class PurchaseOrderActivity extends BaseActivity {


    @BindView(R.id.drawer)
    DrawerLayout mDrawer;
    @BindView(R.id.ishebing)
    CheckBox cbHebing;
    @BindView(R.id.tv_date_arrive)
    TextView tvDateArrive;
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
    @BindView(R.id.sp_unit)
    SpinnerUnit spUnit;
    @BindView(R.id.ed_onsale)
    EditText edOnsale;
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
    @BindView(R.id.sp_purchase_scope)
    Spinner spPurchaseScope;
    @BindView(R.id.sp_purchaseMethod)
    Spinner spPurchaseMethod;
    @BindView(R.id.sp_yuandan)
    Spinner spYuandan;
    @BindView(R.id.sp_payMethod)
    Spinner spPayMethod;
    @BindView(R.id.sp_department)
    Spinner spDepartment;
    @BindView(R.id.sp_employee)
    SpinnerPeople spEmployee;
    @BindView(R.id.sp_manager)
    SpinnerPeople spManager;
    @BindView(R.id.ed_supplier)
    EditText edSupplier;
    @BindView(R.id.search_supplier)
    RelativeLayout searchSupplier;
    @BindView(R.id.isAutoAdd)
    CheckBox autoAdd;
    private PurchaseOrderActivity mContext;
    private Unbinder bind;
    private ShareUtil share;
    private PurchaseScopeSpAdapter purchaseScopeAdapter;
    private YuandanSpAdapter yuandanSpAdapter;
    private PurchaseMethodSpAdapter purchaseMethodSpAdapter;
    private PayTypeSpAdapter payTypeSpAdapter;
    private CommonMethod method;
    private DepartmentSpAdapter departMentAdapter;
    private EmployeeSpAdapter employeeAdapter;
    private EmployeeSpAdapter managerAdapter;
    private Department department;
    private String departmentId;
    private String departmentName;
    private String PurchaseMethodId;
    private String purchaseMethodName;
    private String purchaseRangeId;
    private String purchaseRangeName;
    private String yuandanID;
    private String yuandanName;
    private String payTypeId;
    private String payTypeName;
//    private String employeeId;
//    private String employeeName;
//    private String ManagerId;
//    private String ManagerName;
    private long ordercode;
    public static final int PURCHASEORDER = 6667;
    private DaoSession daoSession;
    private int year;
    private int month;
    private int day;
    private String date;
    private String supplierid;
    private String supplierName;
    private List<Product> products;
    private boolean isGetDefaultStorage = true;
    private DecimalFormat df;
    private UnitSpAdapter unitAdapter;
    private String dateArrive;
    private String datePay;
    private boolean isHebing = true;
    private String unitId;
    private String unitName;
    private String default_unitID;
    private double unitrate;
    private T_mainDao t_mainDao;
    private T_DetailDao t_detailDao;
    private boolean isAuto;
    private Product product;
    private ProductselectAdapter productselectAdapter;
    private ProductselectAdapter1 productselectAdapter1;

    private boolean isFirst=false;
    private boolean isFirst2=false;
    private boolean isFirst3=false;
    private boolean isFirst4=false;
    private boolean isFirst5=false;
    private boolean isFirst6=false;
    private boolean isFirst7=false;
    @Override
    public void initView() {
        setContentView(R.layout.activity_purchase_order);
        mContext = this;
        bind = ButterKnife.bind(mContext);
        share = ShareUtil.getInstance(mContext);
        initDrawer(mDrawer);
        df = new DecimalFormat("######0.00");
        daoSession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        cbHebing.setChecked(isHebing);
        edOnsale.setText("0");
        autoAdd.setChecked(share.getPOisAuto());
        isAuto = share.getPOisAuto();
    }
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg){
            case EventBusInfoCode.PRODUCTRETURN:
                product = (Product)event.postEvent;
                setDATA("", true);
                break;
        }
    }

    @Override
    public void initData() {
        method = CommonMethod.getMethod(mContext);
        if (share.getPOOrderCode() == 0) {
            ordercode = Long.parseLong(getTime(false) + "001");
            Log.e("ordercode", ordercode + "");
            share.setPOOrderCode(ordercode);
        } else {
            ordercode = share.getPOOrderCode();
            Log.e("ordercode", ordercode + "");
        }
        loadBasicData();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                loadBasicData();
//            }
//        }, 1000);
//        spDepartment.setSelection(share.getPODepartment());
//        spPurchaseMethod.setSelection(share.getPOPurchaseMethod());
//        spPurchaseScope.setSelection(share.getPOPurchaseRange());
//        spYuandan.setSelection(share.getPOYuandan());
//        spPayMethod.setSelection(share.getPOPayMethod());
//        spEmployee.setSelection(share.getPOEmployee());
//        spManager.setSelection(share.getPOManager());
    }

    private void loadBasicData() {
        tvDateArrive.setText(getTime(true));
        tvDate.setText(share.getPOdate());
        tvDatePay.setText(share.getPOpaydate());

        purchaseScopeAdapter = method.getPurchaseScope(spPurchaseScope);
        purchaseMethodSpAdapter = method.getPurchaseMethodSpinner(spPurchaseMethod);
        yuandanSpAdapter = method.getyuandanSp(spYuandan);
        payTypeSpAdapter = method.getpayType(spPayMethod);
        departMentAdapter = method.getDepartMentAdapter(spDepartment);
//        employeeAdapter = method.getEmployeeAdapter(spEmployee);
//        managerAdapter=method.getEmployeeAdapter(spManager);
        method.updateSupplier();

//        spDepartment.setSelection(share.getPODepartment());
//        spEmployee.setSelection(share.getPOEmployee());
//        spManager.setSelection(share.getPOManager());
//        spPayMethod.setSelection(share.getPOPayMethod());
//        spPurchaseMethod.setSelection(share.getPOPurchaseMethod());
//        spPurchaseScope.setSelection(share.getPOPurchaseRange());
//        spYuandan.setSelection(share.getPOYuandan());

        spEmployee.setAutoSelection(getString(R.string.spEmployee_po),"");
        spManager.setAutoSelection(getString(R.string.spManager_po),"");
    }

    @Override
    public void initListener() {

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
                share.setPOisAuto(b);
            }
        });

        edCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    setDATA(edCode.getText().toString(), false);
                    setfocus(edCode);
                }
                return true;
            }
        });
        spDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("department111","走。。。。获取对象");
                department = (Department) departMentAdapter.getItem(i);
                departmentId = department.FItemID;
                departmentName = department.FName;
                Log.e("department111","走。。。。获取对象"+department.toString());
                if (isFirst){
                    share.setPODepartment(i);
                    spDepartment.setSelection(i);
                }
                else{
                    spDepartment.setSelection(share.getPODepartment());
                    isFirst=true;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spPurchaseMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PurchaseMethod item = (PurchaseMethod) purchaseMethodSpAdapter.getItem(i);
                PurchaseMethodId = item.FItemID;
                purchaseMethodName = item.FName;
////                share.setPOPurchaseMethod(i);
                if (isFirst2){
                    share.setPOPurchaseMethod(i);
                    spPurchaseMethod.setSelection(i);
                }
                else{
                    isFirst2=true;
                    spPurchaseMethod.setSelection(share.getPOPurchaseMethod());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spPurchaseScope.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PurchaseMethod purchaseMethod = (PurchaseMethod) purchaseScopeAdapter.getItem(i);
                purchaseRangeId = purchaseMethod.FItemID;
                purchaseRangeName = purchaseMethod.FName;
//                share.setPOPurchaseRange(i);
                if (isFirst3){
                    share.setPOPurchaseRange(i);
                    spPurchaseScope.setSelection(i);
                }
                else{
                    isFirst3=true;
                    spPurchaseScope.setSelection(share.getPOPurchaseRange());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spYuandan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                YuandanType yuandanType = (YuandanType) yuandanSpAdapter.getItem(i);
                yuandanID = yuandanType.FID;
                yuandanName = yuandanType.FName_CHS;
//                share.setPOYuandan(i);
                if (isFirst4){
                    share.setPOYuandan(i);
                    spYuandan.setSelection(i);
                }
                else{
                    spYuandan.setSelection(share.getPOYuandan());
                    isFirst4=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spPayMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PayType payType = (PayType) payTypeSpAdapter.getItem(i);
                payTypeId = payType.FItemID;
                payTypeName = payType.FName;
//                share.setPOPayMethod(i);
                if (isFirst5){
                    share.setPOPayMethod(i);
                    spPayMethod.setSelection(i);
                }
                else{
                    spPayMethod.setSelection(share.getPOPayMethod());
                    isFirst5=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//        spEmployee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Employee employee = (Employee) spEmployee.getAdapter().getItem(i);
//                employeeId = employee.FItemID;
//                employeeName = employee.FName;
//                Lg.e("选中业务员："+employee.toString());
//                Hawk.put(getString(R.string.spEmployee_po),employee.FName);
////                share.setPOEmployee(i);
////                if (isFirst6){
////                    share.setPOEmployee(i);
////                    spEmployee.setSelection(i);
////                }
////                else{
////                    spEmployee.setSelection(share.getPOEmployee());
////                    isFirst6=true;
////                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        spManager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Employee employee = (Employee) spManager.getAdapter().getItem(i);
//                ManagerId = employee.FItemID;
//                ManagerName = employee.FName;
////                share.setPOManager(i);
//                if (isFirst7){
//                    share.setPOManager(i);
//                    spManager.setSelection(i);
//                } else{
//                    spManager.setSelection(share.getPOManager());
//                    isFirst7=true;
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
                    unitrate = Double.parseDouble(unit.FCoefficient);
                    Log.e("选取单位：", unit.toString() + "");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void OnReceive(String code) {
        edCode.setText(code);
        setDATA(code, false);

    }


    @OnClick({R.id.search_supplier, R.id.tv_date_arrive, R.id.scanbyCamera, R.id.search, R.id.btn_add, R.id.btn_finishorder, R.id.btn_backorder, R.id.btn_checkorder, R.id.tv_date, R.id.tv_date_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_supplier:
                SearchSupplier();
                break;
            case R.id.tv_date_arrive:
                getArrivedate();
                break;
            case R.id.scanbyCamera:
                Intent in = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(in, 0);
                break;
            case R.id.search:
                Log.e("search", "onclick");
                Bundle b = new Bundle();
                b.putString("search", edCode.getText().toString());
                b.putInt("where", Info.SEARCHPRODUCT);
                startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.SEARCHFORRESULT, b);
                break;
            case R.id.btn_add:
                Addorder();
                break;
            case R.id.btn_finishorder:
                finishOrder();
                break;
            case R.id.btn_backorder:
                btnBackorder.setClickable(false);
                LoadingUtil.show(mContext,"正在回单...");
                upload();
                break;
            case R.id.btn_checkorder:
                Bundle b1 = new Bundle();
                b1.putInt("activity", PURCHASEORDER);
                startNewActivity(TableActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, false, b1);
                break;
            case R.id.tv_date:
                getdate();
                break;
            case R.id.tv_date_pay:
                getPaydate();
                break;
        }
    }


    private void SearchSupplier() {
        Bundle b = new Bundle();
        b.putString("search", edSupplier.getText().toString());
        b.putInt("where", Info.SEARCHSUPPLIER);
        startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.SEARCHFORRESULTPRODUCT, b);

    }



    public void finishOrder() {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("code", requestCode + "" + "    " + resultCode);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                String message = b.getString("result");
                OnReceive(message);
//                edCode.setText(message);
//                Toast.showText(mContext, message);
//                edCode.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
            }
        }  else if (requestCode == Info.SEARCHFORRESULTPRODUCT) {
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
        if (flag) {
            tvorisAuto(product);
        } else {
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                Asynchttp.post(mContext, getBaseUrl() + WebApi.SEARCHPRODUCTS, fnumber, new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        final DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                        Log.e("DownloadReturnBean", dBean.toString());
                        if (dBean.products.size() == 1) {
                            getProductOL(dBean, 0);
                        } else if (dBean.products.size() > 1) {
                            AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                            ab.setTitle("请选择物料");
                            View v = LayoutInflater.from(mContext).inflate(R.layout.pd_alert, null);
                            ListView lv = v.findViewById(R.id.lv_alert);
                            productselectAdapter1 = new ProductselectAdapter1(mContext, dBean.products);
                            lv.setAdapter(productselectAdapter);
                            ab.setView(v);
                            final AlertDialog alertDialog = ab.create();
                            alertDialog.show();
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    getProductOL(dBean, i);
                                    alertDialog.dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        Toast.showText(mContext, Msg);
                    }
                });
            } else {
                final ProductDao productDao = daoSession.getProductDao();
                BarCodeDao barCodeDao = daoSession.getBarCodeDao();
                final List<BarCode> barCodes = barCodeDao.queryBuilder().where(BarCodeDao.Properties.FBarCode.eq(fnumber)).build().list();

                if (barCodes.size() > 0) {
                    if (barCodes.size() == 1) {
                        default_unitID = barCodes.get(0).FUnitID;
//                        chooseUnit(default_unitID);
                        products = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(barCodes.get(0).FItemID)).build().list();
                        getProductOFL(products);
                    } else {
                        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                        ab.setTitle("请选择物料");
                        View v = LayoutInflater.from(mContext).inflate(R.layout.pd_alert, null);
                        ListView lv = v.findViewById(R.id.lv_alert);
                        productselectAdapter = new ProductselectAdapter(mContext, barCodes);
                        lv.setAdapter(productselectAdapter);
                        ab.setView(v);
                        final AlertDialog alertDialog = ab.create();
                        alertDialog.show();
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                BarCode barCode = (BarCode) productselectAdapter.getItem(i);
                                default_unitID = barCode.FUnitID;
//                                chooseUnit(default_unitID);
                                products = productDao.queryBuilder().where(
                                        ProductDao.Properties.FItemID.eq(barCode.FItemID)
                                ).build().list();
                                getProductOFL(products);
                                alertDialog.dismiss();
                            }
                        });
                    }
                }else{
                    MediaPlayer.getInstance(mContext).error();
                    Toast.showText(mContext,"未找到条码" );
                }
            }
        }
    }
    private void getProductOFL(List<Product> list){
        if (list != null && list.size() > 0) {
            product = list.get(0);
            spUnit.setAuto(mContext,product.FUnitGroupID,default_unitID,SpinnerUnit.Id);
            tvorisAuto(product);
        } else {
            Toast.showText(mContext, "未找到物料");
            edCode.setText("");
            setfocus(edCode);
        }
    }
    private void tvorisAuto(Product product) {
        edCode.setText(product.FNumber);
        tvModel.setText(product.FModel);
        edPricesingle.setText(df.format(Double.parseDouble(product.FSalePrice)));
        tvGoodName.setText(product.FName);
        spUnit.setAuto(mContext,product.FUnitGroupID,default_unitID,SpinnerUnit.Id);
//        unitAdapter = CommonMethod.getMethod(mContext).getUnitAdapter(product.FUnitGroupID, spUnit);

        if (isAuto) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    edNum.setText("1.0");
                    edOnsale.setText("0");
                    Addorder();
                }
            }, 100);

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

    private void getProductOL(DownloadReturnBean dBean, int j) {
        product = dBean.products.get(j);
        default_unitID = dBean.products.get(0).FUnitID;
//        chooseUnit(default_unitID);
        spUnit.setAuto(mContext,product.FUnitGroupID,default_unitID,SpinnerUnit.Id);
        tvorisAuto(product);
    }

    private void Addorder() {
        String discount = edOnsale.getText().toString();
        String num = edNum.getText().toString();
        T_DetailDao t_detailDao = daoSession.getT_DetailDao();
        T_mainDao t_mainDao = daoSession.getT_mainDao();
        if (edSupplier.getText().toString().equals("")||edOnsale.getText().toString().equals("") || edCode.getText().toString().equals("") || edPricesingle.getText().toString().equals("") || edNum.getText().toString().equals("")) {
            MediaPlayer.getInstance(mContext).error();
            if (edCode.getText().toString().equals("")) {
                Toast.showText(mContext, "请输入物料编号");
            } else if (edPricesingle.getText().toString().equals("")) {
                Toast.showText(mContext, "请输入单价");
            } else if (edNum.getText().toString().equals("")) {
                Toast.showText(mContext, "请输入数量");
            } else if (edOnsale.getText().toString().equals("")) {
                Toast.showText(mContext, "请输入折扣率");
            }else if(edSupplier.getText().toString().equals("")){
                Toast.showText(mContext, "请输入供应商");
            }
        } else {
            if (isHebing) {
                List<T_Detail> detailhebing = t_detailDao.queryBuilder().where(
                        T_DetailDao.Properties.Activity.eq(PURCHASEORDER),
                        T_DetailDao.Properties.FUnitId.eq(unitId),
                        T_DetailDao.Properties.FOrderId.eq(ordercode),
                        T_DetailDao.Properties.FProductId.eq(product.FItemID),
                        T_DetailDao.Properties.FTaxUnitPrice.eq(edPricesingle.getText().toString()),
                        T_DetailDao.Properties.FDiscount.eq(discount)

                ).build().list();
                if (detailhebing.size() > 0) {
                    for (int i = 0; i < detailhebing.size(); i++) {
                        num = (Double.parseDouble(num) + Double.parseDouble(detailhebing.get(i).FQuantity)) + "";
                        t_detailDao.delete(detailhebing.get(i));
                    }
                }
            }
            List<T_main> dewlete = t_mainDao.queryBuilder().where(
                    T_mainDao.Properties.OrderId.eq(ordercode)
            ).build().list();
            t_mainDao.deleteInTx(dewlete);
            String second = getTimesecond();
            T_main t_main = new T_main();
            t_main.FDepartment = departmentName == null ? "" : departmentName;
            t_main.FDepartmentId = departmentId == null ? "" : departmentId;
            t_main.FIndex = second;
            t_main.FPaymentDate = tvDatePay.getText().toString();
            t_main.orderId = ordercode;
            t_main.orderDate = tvDate.getText().toString();
            t_main.FPurchaseUnit = unitName == null ? "" : unitName;
            t_main.FSalesMan = spEmployee.getEmployeeName();
            t_main.FSalesManId = spEmployee.getEmployeeId();
            t_main.FMaker = share.getUserName();
            t_main.FMakerId = share.getsetUserID();
            t_main.FDirector = spManager.getEmployeeName();
            t_main.FDirectorId = spManager.getEmployeeId();
            t_main.FPaymentType = payTypeName == null ? "" : payTypeName;
            t_main.FPaymentTypeId = payTypeId == null ? "" : payTypeId;
            t_main.saleWayId = PurchaseMethodId == null ? "" : PurchaseMethodId;
            t_main.saleWay = purchaseMethodName == null ? "" : purchaseMethodName;
            t_main.FDeliveryAddress = "";
            t_main.FRemark = edZhaiyao.getText().toString();
            t_main.FCustody = purchaseRangeName == null ? "" : purchaseRangeName;
            t_main.FCustodyId = purchaseRangeId == null ? "" : purchaseRangeId;
            t_main.FAcount = "";
            t_main.FAcountID = "";
            t_main.Rem = edZhaiyao.getText().toString();
            t_main.supplier = supplierName == null ? "" : supplierName;
            t_main.supplierId = supplierid == null ? "" : supplierid;
            t_main.FSendOutId = "";
            t_main.FDeliveryType = "";
            t_main.activity = PURCHASEORDER;
            t_main.sourceOrderTypeId = yuandanID == null ? "" : yuandanID;
            long insert1 = t_mainDao.insert(t_main);

            T_Detail t_detail = new T_Detail();
            t_detail.FBatch = "";
            t_detail.FOrderId = ordercode;
            t_detail.FProductCode = edCode.getText().toString();
            t_detail.FProductId = product.FItemID;
            t_detail.model = product.FModel;
            t_detail.FProductName = product.FName;
            t_detail.FIndex = second;
            t_detail.FUnitId = unitId == null ? "" : unitId;
            t_detail.FUnit = unitName == null ? "" : unitName;
            t_detail.FDateDelivery = tvDateArrive.getText().toString();
            t_detail.FStorage = "";
            t_detail.FStorageId = "";
            t_detail.FPosition = "";
            t_detail.FPositionId = "";
            t_detail.activity = PURCHASEORDER;
            t_detail.FDiscount = discount;
            t_detail.FQuantity = num;
            t_detail.unitrate = unitrate;
            t_detail.FTaxUnitPrice = edPricesingle.getText().toString();
            long insert = t_detailDao.insert(t_detail);

            if (insert1 > 0 && insert > 0) {
                Toast.showText(mContext, "添加成功");
                MediaPlayer.getInstance(mContext).ok();
                resetAll();
            } else {
                Toast.showText(mContext, "添加失败，请重试");
                MediaPlayer.getInstance(mContext).error();
            }
        }


    }

    private void upload() {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        PurchaseInStoreUploadBean.purchaseInStore puBean = pBean.new purchaseInStore();
        t_mainDao = daoSession.getT_mainDao();
        t_detailDao = daoSession.getT_DetailDao();
        ArrayList<String> detailContainer = new ArrayList<>();
        ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data = new ArrayList<>();
        List<T_main> mains = t_mainDao.queryBuilder().where(T_mainDao.Properties.Activity.eq(PURCHASEORDER)).build().list();
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
                        t_main.FAcount + "|" +
                        t_main.FDepartmentId + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.FDirectorId + "|" +
                        t_main.supplierId + "|" +
                        t_main.Rem + "|" +
                        t_main.FCustodyId + "|" +
                        t_main.FPaymentTypeId + "|" +
                        t_main.sourceOrderTypeId + "|";
                puBean.main = main;
                List<T_Detail> details = t_detailDao.queryBuilder().where(
                        T_DetailDao.Properties.FOrderId.eq(t_main.orderId),
                        T_DetailDao.Properties.Activity.eq(PURCHASEORDER)
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
                                t_detail.FDiscount + "|" +
                                t_detail.FDateDelivery + "|";
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
                                t_detail.FDateDelivery + "|";
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
        postToServer(data);
    }

    private void postToServer(ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data) {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        pBean.list = data;
        Gson gson = new Gson();
        Asynchttp.post(mContext, getBaseUrl() + WebApi.UPLOADPO, gson.toJson(pBean), new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                MediaPlayer.getInstance(mContext).ok();
                Toast.showText(mContext, "上传成功");
                List<T_Detail> list = t_detailDao.queryBuilder().where(
                        T_DetailDao.Properties.Activity.eq(PURCHASEORDER)
                ).build().list();
                List<T_main> list1 = t_mainDao.queryBuilder().where(
                        T_mainDao.Properties.Activity.eq(PURCHASEORDER)
                ).build().list();
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
                Toast.showText(mContext, Msg);
                btnBackorder.setClickable(true);
                LoadingUtil.dismiss();
            }
        });
    }

    private void resetAll() {
        edNum.setText("");
        edPricesingle.setText("");
        edOnsale.setText("0");
        edZhaiyao.setText("");
        edCode.setText("");
        tvGoodName.setText("");
        tvModel.setText("");
        setfocus(edCode);

    }

    private void getArrivedate() {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            }
        }, year, month, day);
        datePickerDialog.show();
        datePickerDialog.setButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int year = datePickerDialog.getDatePicker().getYear();
                int month = datePickerDialog.getDatePicker().getMonth();
                int day = datePickerDialog.getDatePicker().getDayOfMonth();
                dateArrive = year + "-" + ((month < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((day < 10) ? "0" + day : day);
                tvDateArrive.setText(dateArrive);
                Toast.showText(mContext, dateArrive);
                datePickerDialog.dismiss();
            }
        });
    }

    private void getPaydate() {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            }
        }, year, month, day);
        datePickerDialog.show();
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
    }

    private void getdate() {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            }
        }, year, month, day);
        datePickerDialog.show();
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
    }
    public String datePicker() {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                date = i + "-" + ((i1 < 10) ? "0" + (i1 + 1) : (i1 + 1)) + "-" + ((i2 < 10) ? "0" + i2 : i2);
            }
        }, year, month, day);
        datePickerDialog.show();
        datePickerDialog.setButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                datePickerDialog.dismiss();
                Toast.showText(mContext, date);
            }
        });
        return date;
    }
}
