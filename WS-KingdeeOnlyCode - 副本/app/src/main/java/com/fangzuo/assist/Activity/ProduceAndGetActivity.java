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
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.PurchaseMethod;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.Dao.Unit;
import com.fangzuo.assist.Dao.WaveHouse;
import com.fangzuo.assist.Dao.YuandanType;
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

//                生产领料
public class ProduceAndGetActivity extends BaseActivity {


    @BindView(R.id.ishebing)
    CheckBox cbHebing;
    @BindView(R.id.isAutoAdd)
    CheckBox autoAdd;
    @BindView(R.id.sp_department)
    Spinner spDepartment;
    @BindView(R.id.sp_which_storage)
    Spinner spWhichStorage;
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
    TextView tvNuminstorage;
    @BindView(R.id.ed_pihao)
    EditText edPihao;
    @BindView(R.id.sp_wavehouse)
    MyWaveHouseSpinner spWavehouse;
    @BindView(R.id.ed_num)
    EditText edNum;
    @BindView(R.id.sp_unit)
    SpinnerUnit spUnit;
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
    @BindView(R.id.cb_isStorage)
    CheckBox cbIsStorage;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.sp_getType)
    Spinner spGetType;
    @BindView(R.id.sp_getman)
    Spinner spGetman;
    @BindView(R.id.sp_sendman)
    Spinner spSendman;
    @BindView(R.id.mDrawer)
    DrawerLayout mDrawer;
    boolean isHebing = true;
    @BindView(R.id.blue)
    RadioButton blue;
    @BindView(R.id.red)
    RadioButton red;
    @BindView(R.id.redorBlue)
    RadioGroup redorBlue;
    @BindView(R.id.sp_pihao)
    Spinner spPihao;
    private boolean isAuto;
    private DecimalFormat df;
    private DaoSession daoSession;
    private int year;
    private int month;
    private int day;
    private CommonMethod method;
    private long ordercode;
    private StorageSpAdapter storageAdapter;
    private PayMethodSpAdapter produceTypeSpAdapter;
    private EmployeeSpAdapter getManAdapter;
    private DepartmentSpAdapter departmentAdapter;
    private List<Product> products;
    private boolean fBatchManager;
    private UnitSpAdapter unitAdapter;
    private String default_unitID;
    private ProductselectAdapter productselectAdapter;
    private Product product;
    private ProductselectAdapter1 productselectAdapter1;
    private T_mainDao t_mainDao;
    private T_DetailDao t_detailDao;
    private String wavehouseID;
    private String pihao;
    public static final int ProduceAndGet = 10208;
    private boolean isGetDefaultStorage;
    private String departmentId;
    private String departmentName;
    private String unitId;
    private String unitName;
    private double unitrate;
    private Storage storage;
    private WaveHouseSpAdapter waveHouseAdapter;
    private String storageId;
    private String storageName;
    private String wavehouseName;
    private double storenum;
    private String getmanId;
    private String getmanName;
    private String getTypeId;
    private String getTypeName;
    private String SendmanId;
    private String SendmanName;
    private String date;
    private boolean isRed = false;
    private String redblue = "蓝字";
    private PiciSpAdapter piciSpAdapter;
    List<InStorageNum> piciContainer;
    private boolean checkStorage=false;  // 0不允许负库存false  1允许负库存出库true
    private String wavehouseAutoString="";
    private CodeCheckBackDataBean codeCheckBackDataBean;
    private int activity = Config.ProduceAndGetActivity;
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
    protected void initView() {
        setContentView(R.layout.activity_produce_and_get);
        mContext = this;
        ButterKnife.bind(ProduceAndGetActivity.this);
        share = ShareUtil.getInstance(mContext);
        df = new DecimalFormat("######0.00");
        initDrawer(mDrawer);
        edPihao.setEnabled(false);
        daoSession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        t_detailDao = daoSession.getT_DetailDao();
        t_mainDao=daoSession.getT_mainDao();
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        cbHebing.setChecked(isHebing);
        autoAdd.setChecked(share.getPGisAuto());
        isAuto = share.getPGisAuto();

    }

    @Override
    protected void initData() {
        method = CommonMethod.getMethod(mContext);
        if (share.getPROGOrderCode() == 0) {
            ordercode = Long.parseLong(getTime(false) + "001");
            Log.e("ordercode", ordercode + "");
            share.setPROGOrderCode(ordercode);
        } else {
            ordercode = share.getPROGOrderCode();
            Log.e("ordercode", ordercode + "");
        }
        LoadBasicData();
    }

    private void LoadBasicData() {
        departmentAdapter = method.getDepartMentAdapter(spDepartment);
        storageAdapter = method.getStorageSpinner(spWhichStorage);
        produceTypeSpAdapter = method.getSaleMethodSpinner(spGetType);
        getManAdapter = method.getEmployeeAdapter(spGetman);
        tvDate.setText(share.getPROISdate());
        method.getEmployeeAdapter(spSendman);

//        spDepartment.setSelection(share.getPGDepartment());
//        spGetman.setSelection(share.getPGGetMan());
//        spGetType.setSelection(share.getPGGetType());
//        spSendman.setSelection(share.getPGsendMan());
    }

    @Override
    protected void initListener() {

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
                isGetDefaultStorage = b;
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
                share.setPGisAuto(b);
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
        spPihao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                InStorageNum inStorageNum = (InStorageNum) piciSpAdapter.getItem(i);
                Lg.e("点击库存："+inStorageNum.toString());
                pihao = inStorageNum.FBatchNo;
                getInstorageNum(product);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Department department = (Department) departmentAdapter.getItem(i);
                departmentId = department.FItemID;
                departmentName = department.FName;
//                share.setPGDepartment(i);
                if (isFirst){
                    share.setPGDepartment(i);
                    spDepartment.setSelection(i);
                }
                else{
                    spDepartment.setSelection(share.getPGDepartment());
                    isFirst=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spGetman.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Employee getMan = (Employee) getManAdapter.getItem(i);
                getmanId = getMan.FItemID;
                getmanName = getMan.FName;
//                share.setPGGetMan(i);
                if (isFirst2){
                    share.setPGGetMan(i);
                    spGetman.setSelection(i);
                }
                else{
                    spGetman.setSelection(share.getPGGetMan());
                    isFirst2=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spGetType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PurchaseMethod yuandanType = (PurchaseMethod) produceTypeSpAdapter.getItem(i);
                getTypeId = yuandanType.FItemID;
                getTypeName = yuandanType.FName;
//                share.setPGGetType(i);
                if (isFirst3){
                    share.setPGGetType(i);
                    spGetType.setSelection(i);
                }
                else{
                    spGetType.setSelection(share.getPGGetType());
                    isFirst3=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spSendman.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Employee getMan = (Employee) getManAdapter.getItem(i);
                SendmanId = getMan.FItemID;
                SendmanName = getMan.FName;
//                share.setPGsendMan(i);
                if (isFirst4){
                    share.setPGsendMan(i);
                    spSendman.setSelection(i);
                }
                else{
                    spSendman.setSelection(share.getPGsendMan());
                    isFirst4=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
        spWhichStorage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                storage = (Storage) storageAdapter.getItem(i);
                if ("1".equals(storage.FUnderStock)){
                    checkStorage=true;
                }else{
                    checkStorage=false;
                }
                wavehouseID = "0";
                wavehouseName = "";
//                waveHouseAdapter = CommonMethod.getMethod(mContext).getWaveHouseAdapter(storage, spWavehouse);
                spWavehouse.setAuto(mContext,storage,wavehouseAutoString);

                storageId = storage.FItemID;
                storageName = storage.FName;
                Log.e("storageId", storageId);
                Log.e("storageName", storageName);
//                getpici();
//                getInstorageNum(product);

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
    }


    private String barcode = "";
    @Override
    protected void OnReceive(String code) {
        Lg.e("扫码："+code);
        barcode = code;
        edNum.setEnabled(!barcode.contains("ZZ"));
        LoadingUtil.showDialog(mContext,"正在查找...");
        //查询条码唯一表
        CodeCheckBean bean = new CodeCheckBean(barcode,ordercode + "",BasicShareUtil.getInstance(mContext).getIMIE());
        DataModel.codeCheckForOut(gson.toJson(bean));
//
//        try {
//            String[] split = code.split("\\^");
//            Lg.e("截取条码："+split);
//            if (split.length > 2) {
//                edPihao.setText(split[1]+"");
//                edNum.setText(split[2]);
//                setDATA(split[0],false);
//            } else {
//                if (edPihao.hasFocus()) {
//                    edPihao.setText(code);
//                    if (isAuto) {
//                        Addorder();
//                    } else if (edNum.getText().toString().equals("")) {
//                        setfocus(edNum);
//                    }
//                }else {
//                    edCode.setText(code);
//                    setDATA(code, false);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Lg.e("截取条码出错："+e.toString());
//            Toast.showText(mContext,"条码有误");
//        }

    }

    //获取批次
    private void getpici(){
        piciContainer = new ArrayList<>();
        piciSpAdapter = new PiciSpAdapter(mContext,piciContainer);
        spPihao.setAdapter(piciSpAdapter);
        if (product==null){
            return;
        }
        if (fBatchManager) {
            Log.e(TAG,"开启批次");
            spPihao.setEnabled(true);

            if (!BasicShareUtil.getInstance(mContext).getIsOL()) {
                piciSpAdapter = CommonMethod.getMethod(mContext).getPici(storage,wavehouseID,product, spPihao);
                getInstorageNum(product);
            } else {
//                final List<InStorageNum> container = new ArrayList<>();
//                piciSpAdapter = new PiciSpAdapter(mContext, container);
//                spPihao.setAdapter(piciSpAdapter);
                GetBatchNoBean bean = new GetBatchNoBean();
                bean.ProductID=product.FItemID;
                bean.StorageID=storageId;
                bean.WaveHouseID=wavehouseID;
                String json = new Gson().toJson(bean);
                Log.e(TAG, "getPici批次请求："+json);
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
                                    piciContainer.add(dBean.InstorageNum.get(i));

                                }
                            }
                                    piciSpAdapter.notifyDataSetChanged();
                                    getInstorageNum(product);
                        }
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
            getInstorageNum(product);
        }
    }



    @OnClick({R.id.scanbyCamera, R.id.search, R.id.btn_add, R.id.btn_finishorder, R.id.btn_backorder, R.id.btn_checkorder, R.id.tv_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
        }
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
                share.setPROGOrderCode(ordercode);
            }
        });
        ab.setNegativeButton("取消", null);
        ab.create().show();

    }

    //自动添加
    private void tvorisAuto(final Product product) {
        edCode.setText(product.FNumber);
        tvModel.setText(product.FModel);
//        wavehouseAutoString=product.FSPID;
        wavehouseAutoString=codeCheckBackDataBean.FStockPlaceID;
        edPricesingle.setText(df.format(Double.parseDouble(product.FSalePrice)));
        tvGoodName.setText(product.FName);
        if ((product.FBatchManager) != null && (product.FBatchManager).equals("1")) {
            fBatchManager = true;
            setfocus(edPihao);
            edPihao.setEnabled(false);
        } else {
            edPihao.setEnabled(false);
            fBatchManager = false;
        }
        if (true) {
            for (int j = 0; j < storageAdapter.getCount(); j++) {
                if (((Storage) storageAdapter.getItem(j)).FItemID.equals(codeCheckBackDataBean.FStockID)) {
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
            }, 100);
        }

//        getpici();
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
//        if ((isAuto && !fBatchManager) || (isAuto && fBatchManager && !"".equals(pihao))) {
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


    private void getInstorageNum(Product product) {
        if (product == null){
            return;
        }
//        Log.e(TAG,"getInstorageNum获取product："+product.toString());
        pihao = edPihao.getText().toString();
        if (fBatchManager) {
            if (pihao == null || pihao.equals("")) {
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
                    storenum = Double.parseDouble(cBean.returnJson);
                    tvNuminstorage.setText((storenum / unitrate) + "");
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Toast.showText(mContext, Msg);
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
                Double qty = Double.parseDouble(list1.get(0).FQty);
                Log.e("qty", qty + "");
                if (qty != null) {
                    tvNuminstorage.setText((qty / unitrate) + "");
                }

            } else {
                tvNuminstorage.setText("0");
            }

        }


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
        }
    }

    private void getProductOL(DownloadReturnBean dBean, int j) {
        product = dBean.products.get(j);
        spUnit.setAuto(mContext,product.FUnitGroupID,codeCheckBackDataBean.FUnitID,SpinnerUnit.Id);
        tvorisAuto(product);
    }

    private void AddorderBefore(){
        String num = "";
        pihao = edPihao.getText().toString();
        String discount = "";
        if (isRed) {
            num = "-" + edNum.getText().toString();
        } else {
            num = edNum.getText().toString();
        }
        T_DetailDao t_detailDao = daoSession.getT_DetailDao();
        T_mainDao t_mainDao = daoSession.getT_mainDao();
        if (product==null) {
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "请选择物料");
            lockScan(0);
            return;
        }
        if ("".equals(edCode.getText().toString())){
            Toast.showText(mContext, "请输入物料编号");
            MediaPlayer.getInstance(mContext).error();
            lockScan(0);
            return;
        }
        if ("".equals(edPricesingle.getText().toString())){
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
        InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
        List<InStorageNum> innum = inStorageNumDao.queryBuilder().where(
                InStorageNumDao.Properties.FItemID.eq(product.FItemID),
                InStorageNumDao.Properties.FStockID.eq(storageId),
                InStorageNumDao.Properties.FStockPlaceID.eq(wavehouseID),
                InStorageNumDao.Properties.FBatchNo.eq(pihao == null ? "" : pihao)
        ).build().list();
        //是否开启库存管理 true，开启允许负库存
        if (!checkStorage && !isRed) {
            if (!BasicShareUtil.getInstance(mContext).getIsOL()
                    && innum.size() > 0
                    && (Double.parseDouble(innum.get(0).FQty) - (Double.parseDouble(edNum.getText().toString()) * unitrate)) < 0) {
                MediaPlayer.getInstance(mContext).error();
                Toast.showText(mContext, "大兄弟，库存不够了");
                lockScan(0);
                return;
            } else if (BasicShareUtil.getInstance(mContext).getIsOL() && Double.parseDouble(edNum.getText().toString()) > storenum) {
                MediaPlayer.getInstance(mContext).error();
                Toast.showText(mContext, "大兄弟，库存不够了");
                lockScan(0);
                return;
            } else if (!BasicShareUtil.getInstance(mContext).getIsOL() && innum.size() < 1) {
                MediaPlayer.getInstance(mContext).error();
                Toast.showText(mContext, "未找到库存");
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
        DataModel.codeInsertForOut(gson.toJson(bean));
    }
    private void Addorder() {
        String num = "";
        pihao = edPihao.getText().toString();
        String discount = "";
        if (wavehouseID == null) {
            wavehouseID = "0";
        }

        if (isRed) {
            num = "-" + edNum.getText().toString();
        } else {
            num = edNum.getText().toString();
        }
//        T_DetailDao t_detailDao = daoSession.getT_DetailDao();
//        T_mainDao t_mainDao = daoSession.getT_mainDao();
//
//        if ("".equals(edCode.getText().toString())){
//            Toast.showText(mContext, "请输入物料编号");
//            MediaPlayer.getInstance(mContext).error();
//            return;
//        }
//        if ("".equals(edPricesingle.getText().toString())){
//            Toast.showText(mContext, "请输入单价");
//            MediaPlayer.getInstance(mContext).error();
//            return;
//        }
//        if ("".equals(edNum.getText().toString())){
//            Toast.showText(mContext, "请输入数量");
//            MediaPlayer.getInstance(mContext).error();
//            return;
//        }
//            InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
//            List<InStorageNum> innum = inStorageNumDao.queryBuilder().where(
//                    InStorageNumDao.Properties.FItemID.eq(product.FItemID),
//                    InStorageNumDao.Properties.FStockID.eq(storageId),
//                    InStorageNumDao.Properties.FStockPlaceID.eq(wavehouseID),
//                    InStorageNumDao.Properties.FBatchNo.eq(pihao == null ? "" : pihao)
//            ).build().list();
//        //是否开启库存管理 true，开启允许负库存
//        if (!checkStorage && !isRed) {
//            if (!BasicShareUtil.getInstance(mContext).getIsOL()
//                    && innum.size() > 0
//                    && (Double.parseDouble(innum.get(0).FQty) - (Double.parseDouble(edNum.getText().toString()) * unitrate)) < 0) {
//                MediaPlayer.getInstance(mContext).error();
//                Toast.showText(mContext, "大兄弟，库存不够了");
//                return;
//            } else if (BasicShareUtil.getInstance(mContext).getIsOL() && Double.parseDouble(edNum.getText().toString()) > storenum) {
//                MediaPlayer.getInstance(mContext).error();
//                Toast.showText(mContext, "大兄弟，库存不够了");
//                return;
//            } else if (!BasicShareUtil.getInstance(mContext).getIsOL() && innum.size() < 1) {
//                MediaPlayer.getInstance(mContext).error();
//                Toast.showText(mContext, "未找到库存");
//                return;
//            }
//        }

//                if (isHebing) {
//                    List<T_Detail> detailhebing = t_detailDao.queryBuilder().where(
//                            T_DetailDao.Properties.Activity.eq(activity),
//                            T_DetailDao.Properties.FOrderId.eq(ordercode),
//                            T_DetailDao.Properties.FProductId.eq(product.FItemID),
//                            T_DetailDao.Properties.FBatch.eq(pihao == null ? "" : pihao),
//                            T_DetailDao.Properties.FUnitId.eq(unitId),
//                            T_DetailDao.Properties.FStorageId.eq(storageId),
//                            T_DetailDao.Properties.FDiscount.eq(discount),
//                            T_DetailDao.Properties.FPositionId.eq(wavehouseID),
//                            T_DetailDao.Properties.FDiscount.eq(discount)
//                    ).build().list();
//                    if (detailhebing.size() > 0) {
//                        for (int i = 0; i < detailhebing.size(); i++) {
//                            num = (Double.parseDouble(num) + Double.parseDouble(detailhebing.get(i).FQuantity)) + "";
//                            t_detailDao.delete(detailhebing.get(i));
//                        }
//                    }
//                }
//                List<T_main> dewlete = t_mainDao.queryBuilder().where(T_mainDao.Properties.OrderId.eq(ordercode)).build().list();
//                t_mainDao.deleteInTx(dewlete);
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
                t_main.FPaymentDate = "";
                t_main.orderDate = tvDate.getText().toString();
                t_main.FPurchaseUnit = unitName == null ? "" : unitName;
                t_main.FSalesMan = getmanName == null ? "" : getmanName;
                t_main.FSalesManId = getmanId == null ? "" : getmanId;
                t_main.FMaker = share.getUserName();
                t_main.FMakerId = share.getsetUserID();
                t_main.FDirector = SendmanName == null ? "" : SendmanName;
                t_main.FDirectorId = SendmanId == null ? "" : SendmanId;
                t_main.saleWay = "";
                t_main.FDeliveryAddress = "";
                t_main.FRemark = "";
                t_main.saleWayId = "";
                t_main.FCustody = getTypeName == null ? "" : getTypeName;
                t_main.FCustodyId = getTypeName == null ? "" : getTypeName;
                t_main.FAcount = "";
                t_main.FAcountID = "";
                t_main.FRedBlue = redblue;
                t_main.Rem = "";
                t_main.supplier = "";
                t_main.supplierId = "";
                t_main.FSendOutId = "";
                t_main.sourceOrderTypeId = "";


                T_Detail t_detail = new T_Detail();
        t_detail.FIndex = second;
        t_detail.FBarcode = barcode;
        t_detail.MakerId = share.getsetUserID();
        t_detail.DataInput = tvDate.getText().toString();
        t_detail.DataPush = tvDate.getText().toString();
        t_detail.IMIE = getIMIE();
        t_detail.activity = activity;
        t_detail.FOrderId = ordercode;

                t_detail.FRedBlue = redblue;
                t_detail.FBatch = pihao == null ? "" : pihao;
                t_detail.FProductCode = edCode.getText().toString();
                t_detail.FProductId = product.FItemID;
                t_detail.model = product.FModel;
                t_detail.FProductName = product.FName;
                t_detail.FUnitId = unitId == null ? "" : unitId;
                t_detail.FUnit = unitName == null ? "" : unitName;
                t_detail.FStorage = storageName == null ? "" : storageName;
                t_detail.FStorageId = storageId == null ? "" : storageId;
                t_detail.FPosition = wavehouseName == null ? "" : wavehouseName;
                t_detail.FPositionId = wavehouseID == null ? "" : wavehouseID;
                t_detail.FDiscount = discount;
                t_detail.FQuantity = num;
                t_detail.unitrate = unitrate;
                t_detail.FTaxUnitPrice = edPricesingle.getText().toString();
        t_detail.IsAssemble = barcode.contains("ZZ")?barcode:"";
        t_detail.FKFDate = codeCheckBackDataBean==null?"":codeCheckBackDataBean.FKFDate;
        t_detail.FKFPeriod = codeCheckBackDataBean==null?"":codeCheckBackDataBean.FKFPeriod;


//                if (!BasicShareUtil.getInstance(mContext).getIsOL()) {
//                    long insert = t_detailDao.insert(t_detail);
//                    long insert1 = t_mainDao.insert(t_main);
//                    if (insert > 0 && insert1 > 0) {
//                        Toast.showText(mContext, "添加成功");
//                        MediaPlayer.getInstance(mContext).ok();
//                        Log.e("qty_insert", Double.parseDouble(innum.get(0).FQty) + "");
//                        Log.e("qty_insert", (Double.parseDouble(edNum.getText().toString()) * unitrate) + "");
//                        Log.e("qty_insert", (unitrate) + "");
////                        if (isRed) {
////                            innum.get(0).FQty = String.valueOf(((Double.parseDouble(innum.get(0).FQty) + (Double.parseDouble(edNum.getText().toString()) * unitrate))));
////                        } else {
//                            innum.get(0).FQty = String.valueOf(((Double.parseDouble(innum.get(0).FQty) - (Double.parseDouble(edNum.getText().toString()) * unitrate))));
////                        }
//                        inStorageNumDao.update(innum.get(0));
//                        resetAll();
//                    } else {
//                        Toast.showText(mContext, "添加失败");
//                        MediaPlayer.getInstance(mContext).error();
//                    }
//                } else {
                    long insert = t_detailDao.insert(t_detail);
                    long insert1 = t_mainDao.insert(t_main);
                    resetAll();
                    if (insert > 0 && insert1 > 0) {
                        Toast.showText(mContext, "添加成功");
                        MediaPlayer.getInstance(mContext).ok();
                    }else{
                        Toast.showText(mContext, "添加失败");
                        MediaPlayer.getInstance(mContext).error();
                    }
//                }

    }

    private void setDATA(String fnumber, boolean flag) {
        default_unitID = null;
        products = null;
//        edPihao.setText("");
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
//                final List<BarCode> barCodes = barCodeDao.queryBuilder().where(BarCodeDao.Properties.FBarCode.eq(fnumber)).build().list();
//
//                if (barCodes.size() > 0) {
//                    if (barCodes.size() == 1) {
//                        products = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(barCodes.get(0).FItemID)).build().list();
//                        default_unitID = barCodes.get(0).FUnitID;
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
//                                default_unitID = barCode.FUnitID;
//                                products = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(barCode.FItemID)).build().list();
//                                alertDialog.dismiss();
//                            }
//                        });
//                    }
//                } else {
//                    MediaPlayer.getInstance(mContext).error();
//                    Toast.showText(mContext, "未找到条码");
//                }
//                if (products != null && products.size() > 0) {
//                    product = products.get(0);
//                    tvorisAuto(product);
//                } else {
//                    Toast.showText(mContext, "未找到物料");
//                    edPihao.setEnabled(false);
//                    edCode.setText("");
//                }
//            }
        }


    }

    private void resetAll() {
        product =null;
        red.setClickable(false);
        blue.setClickable(false);
        red.setBackgroundColor(Color.GRAY);
        blue.setBackgroundColor(Color.GRAY);
        edNum.setText("");
        edPricesingle.setText("");
        edCode.setText("");
        tvNuminstorage.setText("");
        tvGoodName.setText("");
        tvModel.setText("");
        List<InStorageNum> container = new ArrayList<>();
        piciSpAdapter = new PiciSpAdapter(mContext,container);
        spPihao.setAdapter(piciSpAdapter);
        setfocus(edCode);
        lockScan(0);
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
                        t_main.FDepartmentId + "|"
                        + t_main.FDirectorId + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.FRedBlue + "|" +
                        t_main.FCustodyId + "|" +
                        "" + "|"+
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
                                t_detail.FPositionId + "|"+
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
                                t_detail.FPositionId + "|"+
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
        DataModel.upload(WebApi.PRODUCEANDGET,gson.toJson(pBean));
//        postToServer(data);
    }

    private void postToServer(ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data) {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        pBean.list = data;
        Gson gson = new Gson();
        Asynchttp.post(mContext, getBaseUrl() + WebApi.PRODUCEANDGET, gson.toJson(pBean), new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                MediaPlayer.getInstance(mContext).ok();
                Toast.showText(mContext, "上传成功");
                List<T_Detail> list = t_detailDao.queryBuilder().where(
                        T_DetailDao.Properties.Activity.eq(activity)
                ).build().list();
                List<T_main> list1 = t_mainDao.queryBuilder().where(
                        T_mainDao.Properties.Activity.eq(activity)
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
                btnBackorder.setClickable(true);
                Toast.showText(mContext, Msg);
                LoadingUtil.dismiss();
            }
        });
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
                share.setPROGOrderCode(ordercode);
                finish();
            }
        });
        ab.setNegativeButton("取消", null);
        ab.create().show();
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
