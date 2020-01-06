package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.EmployeeSpAdapter;
import com.fangzuo.assist.Adapter.GetGoodsDepartmentSpAdapter;
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
import com.fangzuo.assist.Beans.InStoreNumBean;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.Employee;
import com.fangzuo.assist.Dao.InStorageNum;
import com.fangzuo.assist.Dao.Product;
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
import com.fangzuo.assist.Utils.LocDataUtil;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.MediaPlayer;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.assist.widget.MyWaveHouseSpinner;
import com.fangzuo.assist.widget.SpinnerStorage;
import com.fangzuo.assist.widget.SpinnerStorage4Type;
import com.fangzuo.assist.widget.SpinnerUnit;
import com.fangzuo.assist.zxing.activity.CaptureActivity;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.InStorageNumDao;
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

public class ProductInStorageRedActivity extends BaseActivity {

    @BindView(R.id.mDrawer)
    DrawerLayout mDrawer;
    @BindView(R.id.ishebing)
    CheckBox cbHebing;
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
    @BindView(R.id.ed_pihao)
    EditText edPihao;
    @BindView(R.id.sp_wavehouse)
    MyWaveHouseSpinner spWavehouse;
    @BindView(R.id.tv_numinstorage)
    TextView tvNuminstorage;
    @BindView(R.id.ed_num)
    EditText edNum;
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
    @BindView(R.id.sp_yuandan)
    Spinner spYuandan;
    @BindView(R.id.sp_yanshou)
    Spinner spYanshou;
    @BindView(R.id.sp_capture)
    Spinner spCapture;
    @BindView(R.id.isAutoAdd)
    CheckBox autoAdd;
    @BindView(R.id.sp_which_storage)
    SpinnerStorage4Type spWhichStorage;
    @BindView(R.id.cb_isStorage)
    CheckBox cbIsStorage;
    @BindView(R.id.edghunit)
    EditText edghunit;
    @BindView(R.id.search_ghunit)
    ImageView searchGhunit;
    private ProductInStorageRedActivity mContext;
    private DecimalFormat df;
    private DaoSession daoSession;
    private int year;
    private int month;
    private int day;
    private long ordercode;
    private GetGoodsDepartmentSpAdapter goodsDepartmentSpAdapter;
//    private StorageSpAdapter storageAdapter;
    private YuandanSpAdapter yuandanSpAdapter;
    private CommonMethod method;
    private EmployeeSpAdapter yanshouAdapter;
    private String captureName;
    private String captureId;
    private List<Product> products;
    private boolean isGetDefaultStorage=true;
    private boolean fBatchManager;
    private UnitSpAdapter unitAdapter;
    private String storageName;
    private Storage storage;
    private String storageId;
    private WaveHouseSpAdapter wavehouseAdapter;
    private String wavehouseID = "0";
    private String wavehouseName;
    private String unitId;
    private String unitName;
    private double unitrate;
    private String yanshouName;
    private String yanshouId;
    private String jiaohuoName;
    private String jiaohuoId;
    public static final int PRODUCTINSTORE = 55;
    boolean isHebing = true;
    private T_mainDao t_mainDao;
    private T_DetailDao t_detailDao;
    private String dateAdd;
    private String yuandanId;
    private boolean isAuto;
    private Product product;
    private ProductselectAdapter productselectAdapter;
    private ProductselectAdapter1 productselectAdapter1;
    private String default_unitID;
    private String wavehouseAutoString="";
    private CodeCheckBackDataBean codeCheckBackDataBean;
    private int activity = Config.ProductInStorageRedActivity;
    @Override
    public void initView() {
        setContentView(R.layout.activity_product_in_storage_red);
        mContext = this;
        ButterKnife.bind(this);
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
        autoAdd.setChecked(share.getPISisAuto());
        isAuto = share.getPISisAuto();
        cbIsStorage.setChecked(isGetDefaultStorage);
    }

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
        if (share.getPROISOrderCode() == 0) {
            ordercode = Long.parseLong(getTime(false) + "001");
            Log.e("ordercode", ordercode + "");
            share.setPROISOrderCode(ordercode);
        } else {
            ordercode = share.getPROISOrderCode();
            Log.e("ordercode", ordercode + "");
        }
        LoadBasicData();
    }


    @Override
    public void initListener() {
        searchGhunit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("search", "onclick");
                Bundle b = new Bundle();
                b.putString("search", edghunit.getText().toString());
                b.putInt("where", Info.SEARCHJH);
                startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.SEARCHJH, b);
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
                share.setPISisAuto(b);
            }
        });


        edPihao.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    getInstorageNum();
                    setfocus(edPihao);
                }
                return true;
            }
        });
//        edCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                    setDATA(edCode.getText().toString(),false);
//                    setfocus(edCode);
//                }
//                return true;
//            }
//        });


        spYanshou.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Employee employee = (Employee) yanshouAdapter.getItem(i);
                yanshouName = employee.FName;
                yanshouId = employee.FItemID;
//                share.setPROISyanshou(i);
                if (isFirst){
                    share.setPROISyanshou(i);
                    spYanshou.setSelection(i);
                }
                else{
                    spYanshou.setSelection(share.getPROISyanshou());
                    isFirst=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spCapture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Employee employee = (Employee) yanshouAdapter.getItem(i);
                captureName = employee.FName;
                captureId = employee.FItemID;
//                share.setPROISCapture(i);
                if (isFirst2){
                    share.setPROISCapture(i);
                    spCapture.setSelection(i);
                }
                else{
                    spCapture.setSelection(share.getPROISCapture());
                    isFirst2=true;
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
                yuandanId = yuandanType.FID;
//                share.setProISYuanDan(i);
                if (isFirst3){
                    share.setProISYuanDan(i);
                    spYuandan.setSelection(i);
                }
                else{
                    spYuandan.setSelection(share.getProISYuanDan());
                    isFirst3=true;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spWhichStorage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                storage = (Storage) spWhichStorage.getAdapter().getItem(i);
                Hawk.put(Config.Storage+activity,storage.FName);
                wavehouseID = "0";
                storageName = storage.FName;
                storageId = storage.FItemID;
//                wavehouseAdapter = method.getWaveHouseAdapter(storage, spWavehouse);
                spWavehouse.setAuto(mContext,storage,wavehouseAutoString);
                getInstorageNum();
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

                getInstorageNum();
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

                getInstorageNum();
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
                        share.setPROISdate(dateAdd);
                    }
                }, year, day, month);
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
    }

    private String barcode = "";
    @Override
    protected void OnReceive(String code) {
//        if(edPihao.hasFocus()){
//            edPihao.setText(code);
//            if(isAuto){
//                Addorder();
//            }else if(edNum.getText().toString().equals("")){
//                setfocus(edNum);
//            }
//        }else{
//            edCode.setText(code);
//            setDATA(code, false);
//        }
        barcode = code;
        LoadingUtil.showDialog(mContext,"正在查找...");
        //查询条码唯一表
        CodeCheckBean bean = new CodeCheckBean(barcode);
        DataModel.codeCheckForIn(gson.toJson(bean));
    }

    private void LoadBasicData() {
//        storageAdapter = method.getStorageSpinner(spWhichStorage);
        yuandanSpAdapter = method.getyuandanSp(spYuandan);
        yanshouAdapter = method.getEmployeeAdapter(spYanshou);
        tvDate.setText(share.getPROISdate());
        method.getEmployeeAdapter(spCapture);
        spYuandan.setSelection(ShareUtil.getInstance(mContext).getPROISyanshou());
        spYanshou.setSelection(ShareUtil.getInstance(mContext).getPROISyanshou());
        spCapture.setSelection(ShareUtil.getInstance(mContext).getPROISCapture());
        spWhichStorage.setAutoSelection(Config.Storage+activity, "");

    }

    private void getProductOL(DownloadReturnBean dBean, int j){
        product = dBean.products.get(j);
        spUnit.setAuto(mContext,product.FUnitGroupID,codeCheckBackDataBean.FUnitID,SpinnerUnit.Id);
        tvorisAuto(product);
    }

    private void tvorisAuto(final Product product){
        Lg.e("product:"+product.toString());
        edCode.setText(product.FNumber);
        tvModel.setText(product.FModel);
        wavehouseAutoString=product.FSPID;
        edPricesingle.setText(df.format(Double.parseDouble(product.FSalePrice)));
        tvGoodName.setText(product.FName);
        if ((product.FBatchManager) != null && (product.FBatchManager).equals("1")) {
            fBatchManager = true;
            setfocus(edPihao);
            edPihao.setEnabled(true);
        } else {
            edPihao.setEnabled(false);
            fBatchManager = false;
        }
        if (isGetDefaultStorage) {
            spWhichStorage.setAutoSelection("",product.FDefaultLoc);
//            for (int j = 0; j < storageAdapter.getCount(); j++) {
//                if (((Storage)storageAdapter.getItem(j)).FItemID.equals(product.FDefaultLoc)) {
//                    spWhichStorage.setSelection(j);
//                    break;
//                }
//            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    spWavehouse.setAuto(mContext,storage,wavehouseAutoString);
//                    for (int j = 0; j < wavehouseAdapter.getCount(); j++) {
//                        if (((WaveHouse)wavehouseAdapter.getItem(j)).FSPID.equals(product.FSPID)) {
//                            spWavehouse.setSelection(j);
//                            break;
//                        }
//                    }
                }
            },50);
        }
//        unitAdapter = CommonMethod.getMethod(mContext).getUnitAdapter(product.FUnitGroupID, spUnit);

//        chooseUnit(default_unitID);

//        if(default_unitID!=null){
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    for(int i = 0;i<unitAdapter.getCount();i++){
//                        if(default_unitID.equals(((Unit)unitAdapter.getItem(i)).FMeasureUnitID)){
//                            spUnit.setSelection(i);
//                        }
//                    }
//                }
//            },100);
//        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getInstorageNum();
            }
        }, 100);
//        if(isAuto){
//            edNum.setText("1.0");
//        }

        if ((isAuto&&!fBatchManager)||(isAuto&&fBatchManager&&!edPihao.getText().toString().equals(""))) {
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

    private void setDATA(String fnumber,boolean flag) {
        default_unitID=null;
        products = null;
//        edPihao.setText("");
        if(flag){
            tvorisAuto(product);
        }else{
            if(BasicShareUtil.getInstance(mContext).getIsOL()){
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
//                        final DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson,DownloadReturnBean.class);
//                        if(dBean.products.size()==1){
//                            getProductOL(dBean,0);
//                            default_unitID = dBean.products.get(0).FUnitID;
//                            chooseUnit(default_unitID);
//                        }else if(dBean.products.size()>1){
//                            AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
//                            ab.setTitle("请选择物料");
//                            View v  = LayoutInflater.from(mContext).inflate(R.layout.pd_alert,null);
//                            ListView lv = v.findViewById(R.id.lv_alert);
//                            productselectAdapter1 = new ProductselectAdapter1(mContext,dBean.products);
//                            lv.setAdapter(productselectAdapter);
//                            ab.setView(v);
//                            final AlertDialog alertDialog = ab.create();
//                            alertDialog.show();
//                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                    getProductOL(dBean,i);
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
//                        Toast.showText(mContext,Msg);
//                    }
//                });
            }
//            else{
//                final ProductDao productDao = daoSession.getProductDao();
//                BarCodeDao barCodeDao = daoSession.getBarCodeDao();
//                final List<BarCode> barCodes = barCodeDao.queryBuilder().where(BarCodeDao.Properties.FBarCode.eq(fnumber)).build().list();
//
//                if (barCodes.size() > 0) {
//                    if(barCodes.size()==1){
//                        products = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(barCodes.get(0).FItemID)).build().list();
//                        default_unitID = barCodes.get(0).FUnitID;
//                        getProductOFL(products);
//                    }else{
//                        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
//                        ab.setTitle("请选择物料");
//                        View v  = LayoutInflater.from(mContext).inflate(R.layout.pd_alert,null);
//                        ListView lv = v.findViewById(R.id.lv_alert);
//                        productselectAdapter = new ProductselectAdapter(mContext,barCodes);
//                        lv.setAdapter(productselectAdapter);
//                        ab.setView(v);
//                        final AlertDialog alertDialog = ab.create();
//                        alertDialog.show();
//                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                BarCode barCode = (BarCode) productselectAdapter.getItem(i);
//                                default_unitID =barCode.FUnitID;
//                                products = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(barCode.FItemID)).build().list();
//                                getProductOFL(products);
//                                alertDialog.dismiss();
//                            }
//                        });
//                    }
//                }else{
//                    MediaPlayer.getInstance(mContext).error();
//                    Toast.showText(mContext,"未找到条码" );
//                }
//            }
        }


    }
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
    private void getInstorageNum() {
        if (product ==null){
            return;
        }
        String pihao;
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
        if(BasicShareUtil.getInstance(mContext).getIsOL()){
            InStoreNumBean iBean = new InStoreNumBean();
            iBean.FStockPlaceID = wavehouseID;
            iBean.FBatchNo = pihao;
            iBean.FStockID = storageId;
            iBean.FItemID = product.FItemID;
            String json = new Gson().toJson(iBean);
            Log.e("inStorenum",json);
            Asynchttp.post(mContext, getBaseUrl() + WebApi.GETINSTORENUM, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    double num = Double.parseDouble(cBean.returnJson);
                    tvNuminstorage.setText((num / unitrate) + "");
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    tvNuminstorage.setText("0");
                }
            });
        }else{
            InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
            List<InStorageNum> list1 = inStorageNumDao.queryBuilder().
                    where(InStorageNumDao.Properties.FItemID.eq(product.FItemID), InStorageNumDao.Properties.FStockID.eq(storageId),
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
        if (jiaohuoId==null) {
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "请选择交货单位");
            lockScan(0);
            return;
        }
        String discount = "0";
        String num = edNum.getText().toString();
        T_DetailDao t_detailDao = daoSession.getT_DetailDao();
        T_mainDao t_mainDao = daoSession.getT_mainDao();
        if ((edghunit.getText().toString().equals(""))||edCode.getText().toString().equals("") || edPricesingle.getText().toString().equals("") || MathUtil.toD(edNum.getText().toString())<=0) {
            MediaPlayer.getInstance(mContext).error();
            lockScan(0);
            if (edCode.getText().toString().equals("")) {
                Toast.showText(mContext, "请输入物料编号");
                lockScan(0);
            } else if (edPricesingle.getText().toString().equals("")) {
                Toast.showText(mContext, "请输入单价");
                lockScan(0);
            } else if (MathUtil.toD(edNum.getText().toString())<=0){
                Toast.showText(mContext, "请输入数量");
                lockScan(0);
            }else if(edghunit.getText().toString().equals("")){
                Toast.showText(mContext, "请输入交货单位");
                lockScan(0);
            }
        } else if (fBatchManager && edPihao.getText().toString().equals("")) {
            Toast.showText(mContext, "请输入批次号");
            lockScan(0);
        } else {

            if (LocDataUtil.checkHasBarcode(mContext,barcode)){
                Toast.showText(mContext,"本地已存在该条码信息，请重新扫码添加");
                lockScan(0);
                return;
            }
            //插入条码唯一临时表
            CodeCheckBean bean = new CodeCheckBean(barcode,ordercode + "",storageId==null?"":storageId,wavehouseID == null ? "0" : wavehouseID,BasicShareUtil.getInstance(mContext).getIMIE());
            DataModel.codeInsertForIn(gson.toJson(bean));
        }
    }
    private void Addorder() {
        String discount = "0";
        String num = edNum.getText().toString();
//        T_DetailDao t_detailDao = daoSession.getT_DetailDao();
//        T_mainDao t_mainDao = daoSession.getT_mainDao();
//        if ((edghunit.getText().toString().equals(""))||edCode.getText().toString().equals("") || edPricesingle.getText().toString().equals("") || edNum.getText().toString().equals("")) {
//            MediaPlayer.getInstance(mContext).error();
//            if (edCode.getText().toString().equals("")) {
//                Toast.showText(mContext, "请输入物料编号");
//            } else if (edPricesingle.getText().toString().equals("")) {
//                Toast.showText(mContext, "请输入单价");
//            } else if (edNum.getText().toString().equals("")) {
//                Toast.showText(mContext, "请输入数量");
//            }else if(edghunit.getText().toString().equals("")){
//                Toast.showText(mContext, "请输入交货单位");
//            }
//        } else if (fBatchManager && edPihao.getText().toString().equals("")) {
//            Toast.showText(mContext, "请输入批次号");
//        } else {
//            if (isHebing) {
//                List<T_Detail> detailhebing = t_detailDao.queryBuilder().where(
//                        T_DetailDao.Properties.Activity.eq(activity),
//                        T_DetailDao.Properties.FOrderId.eq(ordercode),
//                        T_DetailDao.Properties.FProductId.eq(product.FItemID),
//                        T_DetailDao.Properties.FBatch.eq(edPihao.getText().toString()),
//                        T_DetailDao.Properties.FUnitId.eq(unitId),
//                        T_DetailDao.Properties.FStorageId.eq(storageId),
//                        T_DetailDao.Properties.FPositionId.eq(wavehouseID),
//                        T_DetailDao.Properties.FDiscount.eq(discount)
//                ).build().list();
//                if (detailhebing.size() > 0) {
//                    for (int i = 0; i < detailhebing.size(); i++) {
//                        num = (Double.parseDouble(num) + Double.parseDouble(detailhebing.get(i).FQuantity)) + "";
//                        t_detailDao.delete(detailhebing.get(i));
//                    }
//                }
//            }
//                List<T_main> dewlete = t_mainDao.queryBuilder().where(
//                        T_mainDao.Properties.OrderId.eq(ordercode)
//                ).build().list();
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

        t_main.FDepartment = jiaohuoName==null?"":jiaohuoName;
        t_main.FDepartmentId = jiaohuoId==null?"":jiaohuoId;
        t_main.FPaymentDate = "";
        t_main.orderDate = share.getPROISdate();
        t_main.FPurchaseUnit = unitName==null?"":unitName;
        t_main.FSalesMan = "";
        t_main.FSalesManId = "";
        t_main.FMaker = share.getUserName();
        t_main.FMakerId = share.getsetUserID();
        t_main.FDirector = yanshouName==null?"":yanshouName;
        t_main.FDirectorId = yanshouId==null?"":yanshouId;
        t_main.saleWay = "";
        t_main.FDeliveryAddress = "";
        t_main.FRemark = "";
        t_main.saleWayId = "";
        t_main.FCustody = captureName==null?"":captureName;
        t_main.FCustodyId = captureId==null?"":captureId;
        t_main.FAcount = "";
        t_main.FAcountID = "";
        t_main.Rem = "";
        t_main.FRedBlue = "红字";
        t_main.supplier = "";
        t_main.supplierId = "";
        t_main.FSendOutId = "";
        t_main.sourceOrderTypeId = yuandanId==null?"":yuandanId;
        Log.e("yuandan", yuandanId);
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
        t_detail.FUnitId = unitId==null?"":unitId;
        t_detail.FUnit = unitName==null?"":unitName;
        t_detail.FStorage = storageName==null?"":storageName;
        t_detail.FStorageId = storageId==null?"":storageId;
        t_detail.FPosition = wavehouseName==null?"":wavehouseName;
        t_detail.FPositionId = wavehouseID==null?"":wavehouseID;
        t_detail.FDiscount = discount;
        t_detail.FQuantity = num;
        t_detail.unitrate = unitrate;
        t_detail.FTaxUnitPrice = edPricesingle.getText().toString();
        t_detail.IsAssemble = barcode.contains("ZZ")?barcode:"";
        t_detail.FKFDate = codeCheckBackDataBean==null?"":codeCheckBackDataBean.FKFDate;
        t_detail.FKFPeriod = codeCheckBackDataBean==null?"":codeCheckBackDataBean.FKFPeriod;
        long insert = t_detailDao.insert(t_detail);


        if (insert1 > 0 && insert > 0) {
            MediaPlayer.getInstance(mContext).ok();
            Toast.showText(mContext, "添加成功");
            lockScan(0);
//                if(!BasicShareUtil.getInstance(mContext).getIsOL()){
//                    InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
//                    List<InStorageNum> innum = inStorageNumDao.queryBuilder().where(
//                            InStorageNumDao.Properties.FBatchNo.eq(edPihao.getText().toString()),
//                            InStorageNumDao.Properties.FStockID.eq(storageId)
//                            , InStorageNumDao.Properties.FStockPlaceID.eq(wavehouseID),
//                            InStorageNumDao.Properties.FItemID.eq(product.FItemID)
//                    ).build().list();
//                    if (innum.size() > 0) {
//                        innum.get(0).FQty = (Double.parseDouble(innum.get(0).FQty) + (Double.parseDouble(edNum.getText().toString()) * unitrate)) + "";
//                        Log.e("QTY",innum.get(0).FQty);
//                        Log.e("QTY",unitrate+"");
//                        Log.e("QTY",num+"");
//                        inStorageNumDao.update(innum.get(0));
//                    } else {
//                        InStorageNum i = new InStorageNum();
//                        i.FQty = num;
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


    @OnClick({R.id.scanbyCamera, R.id.search, R.id.btn_add, R.id.btn_finishorder, R.id.btn_backorder, R.id.btn_checkorder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.scanbyCamera:
                Intent in = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(in, 0);
                break;
            case R.id.search:
                searchproduct();
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
                Bundle b = new Bundle();
                b.putInt("activity", activity);
                startNewActivity(TableActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, false, b);
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
                        t_main.FDepartmentId + "|" +
                        t_main.FDirectorId + "|" +
                        t_main.FCustodyId + "|" +
                        t_main.FRedBlue + "|" +
                        t_main.sourceOrderTypeId + "|";
//                        t_main.orderId + "|"+
//                        t_main.IMIE + "|";
                puBean.main = main;
                List<T_Detail> details =DataModel.mergeDetail(mContext,t_main.orderId+"",activity);
//                List<T_Detail> details = t_detailDao.queryBuilder().where(
//                        T_DetailDao.Properties.FOrderId.eq(t_main.orderId),
//                        T_DetailDao.Properties.Activity.eq(activity)
//                ).build().list();
                for (int j = 0; j < details.size(); j++) {
                    if (j != 0 && (j+1) % 50 == 0) {
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
        DataModel.upload(mContext,getBaseUrl()+ WebApi.UPLOADPROIS,gson.toJson(pBean));
//        postToServer(data);
    }

    private void postToServer(ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data) {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        pBean.list = data;
        Gson gson = new Gson();
        Asynchttp.post(mContext, getBaseUrl() + WebApi.UPLOADPROIS, gson.toJson(pBean), new Asynchttp.Response() {
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

    public void finishOrder() {
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle("确认使用完单");
        ab.setMessage("确认？");
        ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ordercode++;
                Log.e("ordercode", ordercode + "");
                share.setPROISOrderCode(ordercode);
            }
        });
        ab.setNegativeButton("取消", null);
        ab.create().show();

    }

    private void searchproduct() {
        Log.e("search", "onclick");
        Bundle b = new Bundle();
        b.putString("search", edCode.getText().toString());
        b.putInt("where", Info.SEARCHPRODUCT);
        startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.SEARCHFORRESULT, b);
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
        } else if(requestCode==Info.SEARCHJH){
            if (resultCode == Info.SEARCHFORRESULTJH) {
                Bundle b = data.getExtras();
                jiaohuoId = b.getString("001");
                jiaohuoName = b.getString("002");
                edghunit.setText(jiaohuoName);
            }
        }
    }
    private void resetAll() {
        product = null;
        edNum.setText("");
        edPricesingle.setText("");
        edPihao.setText("");
        edCode.setText("");
        tvNuminstorage.setText("");
        tvGoodName.setText("");
        tvModel.setText("");
        setfocus(edCode);
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

    //用于adpater首次更新时，不存入默认值，而是选中之前的选项
    private boolean isFirst=false;
    private boolean isFirst2=false;
    private boolean isFirst3=false;
    private boolean isFirst4=false;
    private boolean isFirst5=false;
    private boolean isFirst6=false;
    private boolean isFirst7=false;
}
