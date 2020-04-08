package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.fangzuo.assist.Adapter.CheckFirstInOutAdapter;
import com.fangzuo.assist.Adapter.DepartmentSpAdapter;
import com.fangzuo.assist.Adapter.EmployeeSpAdapter;
import com.fangzuo.assist.Adapter.PayMethodSpAdapter;
import com.fangzuo.assist.Adapter.PayTypeSpAdapter;
import com.fangzuo.assist.Adapter.PushDownSubListAdapter;
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
import com.fangzuo.assist.Dao.Employee;
import com.fangzuo.assist.Dao.InStorageNum;
import com.fangzuo.assist.Dao.PayType;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.PurchaseMethod;
import com.fangzuo.assist.Dao.PushDownMain;
import com.fangzuo.assist.Dao.PushDownSub;
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
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.DataModel;
import com.fangzuo.assist.Utils.DoubleUtil;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
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
import com.fangzuo.assist.widget.SpinnerPeople;
import com.fangzuo.assist.widget.SpinnerStorage;
import com.fangzuo.assist.widget.SpinnerStorage4Type;
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

public class PdBackMsg2SaleOutRedActivity extends BaseActivity {

    private int tag =23;
    private int activity = Config.PdBackMsg2SaleOutRedActivity;


    @BindView(R.id.lv_pushsub)
    ListView lvPushsub;
    @BindView(R.id.sp_storage)
    SpinnerStorage4Type spStorage;
    @BindView(R.id.sp_wavehouse)
    MyWaveHouseSpinner spWavehouse;
    @BindView(R.id.productName)
    TextView productName;
    @BindView(R.id.sp_unit)
    SpinnerUnit spUnit;
    @BindView(R.id.ed_pihao)
    EditText edPihao;
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
    @BindView(R.id.tv_date_pay)
    TextView tvDatePay;
    @BindView(R.id.sp_sale_scope)
    Spinner spSaleScope;
    @BindView(R.id.sp_saleMethod)
    Spinner spSaleMethod;
    @BindView(R.id.sp_yuandan)
    Spinner spYuandan;
    @BindView(R.id.sp_sendMethod)
    Spinner spSendMethod;
    @BindView(R.id.sp_payMethod)
    Spinner spPayMethod;
    @BindView(R.id.sp_sendplace)
    Spinner spSendplace;
    @BindView(R.id.sp_manager)
    SpinnerPeople spManager;
    @BindView(R.id.ed_zhaiyao)
    EditText edZhaiyao;
    @BindView(R.id.sp_capture)
    SpinnerPeople spCapture;
    @BindView(R.id.sp_sendman)
    SpinnerPeople spSendman;
    @BindView(R.id.tv_kucun)
    TextView tvKucun;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private DaoSession daosession;
    private int year;
    private int month;
    private int day;
    private ArrayList<String> fidcontainer;
    private List<PushDownSub> container;
    private PushDownSubDao pushDownSubDao;
    private PushDownMainDao pushDownMainDao;
    private String fwanglaiUnit;
    private PushDownSubListAdapter pushDownSubListAdapter;
//    private StorageSpAdapter storageSpinner;
    private String storageID;
    private String storageName;
    private String waveHouseID;
    private String waveHouseName;
    private String productID;
    private BatchNoSpAdapter batchNoSpAdapter;
    private String batchNo;
    private CommonMethod method;
    private PayMethodSpAdapter slaesRange;
    private PayMethodSpAdapter payMethodSpinner;
    private YuandanSpAdapter yuandanSpAdapter;
    private PayTypeSpAdapter payTypeSpAdapter;
    private DepartmentSpAdapter departMentAdapter;
//    private EmployeeSpAdapter employeeAdapter;
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
    private boolean isHebing = true;
    private List<PushDownSub> list;
    private String fid;
    private String fentryid;
    private String fprice;
    private WaveHouseSpAdapter waveHouseAdapter;
    private PushDownSub pushDownSub;
    private UnitSpAdapter unitAdapter;
    private T_mainDao t_mainDao;
    private String datePay;
    private String date;
    private T_DetailDao t_detailDao;
    private boolean isAuto = false;
    @BindView(R.id.cb_isAuto)
    CheckBox cbIsAuto;
    @BindView(R.id.cb_isStorage)
    CheckBox cbIsStorage;
//    private boolean isGetDefaultStorage=false;
    private List<Storage> storages;
    private Product product;
    private ShareUtil share;
    private String captureID;
    private String captureName;
    private double qty;
    private String billNo;
    ArrayList<String> detailContainer;
    private ArrayList<String> fidc;
    private String sendmanID;
    private boolean fBatchManager;
    private String default_unitID;
    private boolean fromScan = false;
    private boolean checkStorage=false;  // 0不允许负库存false  1允许负库存出库true
    private String wavehouseAutoString="";
    private Storage storage;

    private CodeCheckBackDataBean codeCheckBackDataBean;
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
            case EventBusInfoCode.CodeCheck_OK://检测条码成功
                codeCheckBackDataBean = (CodeCheckBackDataBean)event.postEvent;
                Lg.e("条码检查："+codeCheckBackDataBean.toString());
                edPihao.setText(codeCheckBackDataBean.FBatchNo);
                edNum.setText(codeCheckBackDataBean.FQty);
                LoadingUtil.dismiss();
                ScanBarCode(codeCheckBackDataBean.FItemID);
//                setDATA(codeCheckBackDataBean.FItemID,false);
                break;
            case EventBusInfoCode.CodeCheck_Error://检测条码失败
                LoadingUtil.dismiss();
                lockScan(0);
                canClick =false;
                codeCheckBackDataBean = (CodeCheckBackDataBean)event.postEvent;
                Toast.showText(mContext, codeCheckBackDataBean.FTip);
                break;
//            case EventBusInfoCode.Check_First_InOut://先进先出检测
//                DownloadReturnBean dBeanb = (DownloadReturnBean) event.postEvent;
//                if (dBeanb.inOutBeans.size() > 0) {
////                    batchOk = false;
//                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
//                    ab.setTitle("存在更早的商品");
//                    View v = LayoutInflater.from(mContext).inflate(R.layout.pd_alert, null);
//                    ListView lv = v.findViewById(R.id.lv_alert);
//                    CheckFirstInOutAdapter checkBatchAdapter = new CheckFirstInOutAdapter(mContext, dBeanb.inOutBeans);
//                    lv.setAdapter(checkBatchAdapter);
//                    ab.setView(v);
//                    final AlertDialog alertDialog = ab.create();
//                    alertDialog.show();
//                } else {
//                    //插入条码唯一临时表
//                    CodeCheckBean bean = new CodeCheckBean(barcode,ordercode + "",edNum.getText().toString(), BasicShareUtil.getInstance(mContext).getIMIE());
//                    DataModel.codeInsertForOut(gson.toJson(bean));
//                }
//                break;

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


    private void ScanBarCode(String barcode) {
        product = null;
        ProductDao productDao = daosession.getProductDao();
        BarCodeDao barCodeDao = daosession.getBarCodeDao();
        if (BasicShareUtil.getInstance(mContext).getIsOL()) {
//            App.getRService().getProductForId(codeCheckBackDataBean.FItemID, new MySubscribe<CommonResponse>() {
            App.getRService().doIOAction(code4Plan?WebApi.SEARCHPRODUCTS:WebApi.PRPDUCTSEARCHWHERE,code4Plan?barcode:codeCheckBackDataBean.FItemID, new MySubscribe<CommonResponse>() {
                @Override
                public void onNext(CommonResponse commonResponse) {
                    LoadingUtil.dismiss();
                    final DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                    if (dBean.products.size() > 0) {
                        product = dBean.products.get(0);
                        Lg.e("获得物料：",product);
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
//
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
//
//                        setProduct(product);
//                    }
//                }
//
//                @Override
//                public void onFailed(String Msg, AsyncHttpClient client) {
//                    Toast.showText(mContext, Msg);
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
                        if (null != default_unitID && !"".equals(default_unitID)){
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


    @Override
    protected void initView() {
        setContentView(R.layout.activity_pd_back_msg2_sale_out_red);
        mContext = this;
        ButterKnife.bind(this);
        share = ShareUtil.getInstance(mContext);
        daosession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        t_detailDao = daosession.getT_DetailDao();
        t_mainDao   = daosession.getT_mainDao();
        method = CommonMethod.getMethod(mContext);
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        cbIsAuto.setChecked(share.getPDSNisAuto());
        isAuto = share.getPDSNisAuto();
        cbIsStorage.setChecked(Hawk.get(Config.autoGetStorage,false));
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
//            ordercode = Long.parseLong(list1.get(0).FInterID);
        }
        ordercode = DataModel.findOrderCode(mContext,activity,fidcontainer);
        Lg.e("得到ordercode:"+ordercode);
    }

    private void getList() {
        container = new ArrayList<>();
        pushDownSubDao = daosession.getPushDownSubDao();
        pushDownMainDao = daosession.getPushDownMainDao();
        for (int i = 0; i < fidcontainer.size(); i++) {
            QueryBuilder<PushDownSub> qb = pushDownSubDao.queryBuilder();
            list = qb.where(
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

    @Override
    protected void initListener() {
        cbIsAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAuto = b;
                share.setPDSNisAuto(b);
            }
        });
        cbIsStorage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                isGetDefaultStorage = b;
                Hawk.put(Config.autoGetStorage,b);
            }
        });
        spSaleMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PurchaseMethod item = (PurchaseMethod) payMethodSpinner.getItem(i);
                SaleMethodId = item.FItemID;
                SaleMethodName = item.FName;
//                share.setPDSNSaleMethod(i);
                if (isFirst3){
                    share.setPDSNSaleMethod(i);
                    spSaleMethod.setSelection(i);
                }
                else{
                    spSaleMethod.setSelection(share.getPDSNSaleMethod());
                    isFirst3=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spSaleScope.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PurchaseMethod purchaseMethod = (PurchaseMethod) slaesRange.getItem(i);
                saleRangeId = purchaseMethod.FItemID;
                saleRangeName = purchaseMethod.FName;
//                share.setPDSNSaleScop(i);
                if (isFirst4){
                    share.setPDSNSaleScop(i);
                    spSaleScope.setSelection(i);
                }
                else{
                    spSaleScope.setSelection(share.getPDSNSaleScop());
                    isFirst4=true;
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
//                share.setPDSNYuandan(i);
                if (isFirst5){
                    share.setPDSNYuandan(i);
                    spYuandan.setSelection(i);
                }
                else{
                    spYuandan.setSelection(share.getPDSNYuandan());
                    isFirst5=true;
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
//                share.setPDSNPayMethod(i);
                if (isFirst6){
                    share.setPDSNPayMethod(i);
                    spPayMethod.setSelection(i);
                }
                else{
                    spPayMethod.setSelection(share.getPDSNPayMethod());
                    isFirst6=true;
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spSendMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PurchaseMethod purchaseMethod = (PurchaseMethod) getGoodsType.getItem(i);
                sendMethodId = purchaseMethod.FItemID;
                sendMethodName = purchaseMethod.FName;
//                share.setPDSNSendMethod(i);
                if (isFirst8){
                    share.setPDSNSendMethod(i);
                    spSendMethod.setSelection(i);
                }
                else{
                    spSendMethod.setSelection(share.getPDSNSendMethod());
                    isFirst8=true;
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
                if ("1".equals(storage.FUnderStock)) {
                    checkStorage = true;
                } else {
                    checkStorage = false;
                }
                waveHouseID="0";
                storageID = storage.FItemID;
                storageName = storage.FName;
//                waveHouseAdapter = CommonMethod.getMethod(mContext).getWaveHouseAdapter(storage, spWavehouse);
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
                waveHouseID = waveHouse.FSPID;
                waveHouseName = waveHouse.FName;

                getInstorageNum();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        lvPushsub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (canClick){
//                    canClick =false;
//                }else{
//                    return;
//                }
                PushDownSub pushDownSubTemp = (PushDownSub) pushDownSubListAdapter.getItem(i);
                if (!canClick && "成品".equals(pushDownSubTemp.FProductType)){
                    Toast.showText(mContext,"成品不可点击，请扫码添加");
                    canClick = false;
                    return;
                }
                canClick = false;
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
                            lockScan(0);
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

//        spBatchNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                InStorageNum inStorageNum = (InStorageNum) batchNoSpAdapter.getItem(i);
//                batchNo = inStorageNum.FBatchNo;
//
//                    getInstorageNum();
//
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
            unitrateSub=Double.parseDouble(units.get(0).FCoefficient);
            Lg.e("获得明细换算率："+unitrateSub);
        }else{
            unitrateSub=1;
            Lg.e("获得明细换算率失败："+unitrateSub);
        }
    }
    private void clickList(final Product product) {
        String  unitTemp="";
        if ("成品".equals(pushDownSub.FProductType)){
            if (code4Plan && null == codeCheckBackDataBean)return;
            wavehouseAutoString=code4Plan?product.FDefaultLoc:(codeCheckBackDataBean==null?"":codeCheckBackDataBean.FStockPlaceID);
            if (cbIsStorage.isChecked()) {
                spStorage.setAutoSelection("",code4Plan?product.FDefaultLoc:codeCheckBackDataBean.FStockID);
//                for (int j = 0; j < storageSpinner.getCount(); j++) {
//                    if (((Storage)storageSpinner.getItem(j)).FItemID.equals(codeCheckBackDataBean.FStockID)) {
//                        spStorage.setSelection(j);
//                        break;
//                    }
//                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        spWavehouse.setAuto(mContext,storage,wavehouseAutoString);
                    }
                },50);
            }
            unitTemp = code4Plan?product.FUnitID:codeCheckBackDataBean.FUnitID;
        }else{
            wavehouseAutoString =pushDownSub.FDCStockID;
            if (cbIsStorage.isChecked()) {
                spStorage.setAutoSelection("",pushDownSub.FDCStockID);
//                for (int j = 0; j < storageSpinner.getCount(); j++) {
//                    if (((Storage)storageSpinner.getItem(j)).FItemID.equals(pushDownSub.FDCStockID)) {
//                        spStorage.setSelection(j);
//                        break;
//                    }
//                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        spWavehouse.setAuto(mContext,storage,wavehouseAutoString);
                    }
                },50);
            }
            unitTemp = pushDownSub.FUnitID;
        }

        productName.setText(product.FName);
        productID = pushDownSub.FItemID;
//        wavehouseAutoString=product.FSPID;
        if ((product.FBatchManager) != null && (product.FBatchManager).equals("1")) {
            fBatchManager = true;
            spBatchNo.setEnabled(true);
        } else {
            spBatchNo.setEnabled(false);
            fBatchManager = false;
        }

        fid = pushDownSub.FInterID;
        fentryid = pushDownSub.FEntryID;
        fprice = pushDownSub.FAuxPrice;

        spUnit.setAuto(mContext,product.FUnitGroupID,unitTemp,SpinnerUnit.Id);
//        unitAdapter = CommonMethod.getMethod(mContext).getUnitAdapter(product.FUnitGroupID, spUnit);

//        if (fromScan){
//            chooseUnit(default_unitID);
//        }else{
//            chooseUnit(pushDownSub.FUnitID);
//        }
        fromScan = false;

//        getBatchNo();

        getInstorageNum();

        if (isAuto) {
//            edNum.setText("1.0");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AddorderBefore();

                }
            }, 300);
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
    private boolean code4Plan=false;
    @Override
    protected void OnReceive(String code) {
//        ScanBarCode(code);
        if (code.contains("^")){
                List<String> list = CommonUtil.ScanBack(code);
                if (list.size()>0){
                    code4Plan=true;
                    canClick =true;
                    edNum.setEnabled(true);
                    codeCheckBackDataBean=null;
//                edNum.setText(list.get(3));
                    edPihao.setText(list.get(1));
                    barcode = list.get(0);
                    ScanBarCode(barcode);
                }else{
                    lockScan(0);
                }
        }else {
            code4Plan = false;
            barcode = code;
            edNum.setEnabled(!barcode.contains("ZZ"));
            LoadingUtil.showDialog(mContext,"正在查找...");
            //查询条码唯一表
            CodeCheckBean bean = new CodeCheckBean(barcode);
            DataModel.codeCheck(WebApi.CodeCheckForOutForRed,gson.toJson(bean));
            canClick =true;
        }

    }

    private void LoadBasicData() {
//        storageSpinner = method.getStorageSpinner(spStorage);
        tvDate.setText(getTime(true));
        tvDatePay.setText(getTime(true));
        slaesRange = method.getPurchaseRange(spSaleScope);
        payMethodSpinner = method.getSaleMethodSpinner(spSaleMethod);
        yuandanSpAdapter = method.getyuandanSp(spYuandan);
        payTypeSpAdapter = method.getpayType(spPayMethod);
        spManager.setAutoSelection(Config.People1+activity,"");
        spSendman.setAutoSelection(Config.People2+activity,"");
        spCapture.setAutoSelection(Config.People3+activity,"");
        getGoodsType = method.getGoodsTypes(spSendMethod);
        spStorage.setAutoSelection(Config.Storage+activity,"");
//        employeeAdapter = method.getEmployeeAdapter(spManager);
//        spCapture.setAdapter(employeeAdapter);
//        spSendman.setAdapter(employeeAdapter);

//        spSaleMethod.setSelection(share.getPDSNSaleMethod());
//        spSaleScope.setSelection(share.getPDSNSaleScop());
//        spYuandan.setSelection(share.getPDSNYuandan());
//        spPayMethod.setSelection(share.getPDSNPayMethod());
//        spManager.setSelection(share.getPDSNManager());
//        spSendMethod.setSelection(share.getPDSNSendMethod());
    }

    @OnClick({R.id.btn_add, R.id.btn_backorder, R.id.btn_checkorder, R.id.tv_date, R.id.tv_date_pay})
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
            case R.id.tv_date_pay:
                getPaydate();
                break;
        }
    }

    private void getInstorageNum() {
        if (1==1)return;
        if (product==null){
            return;
        }
        batchNo = edPihao.getText().toString();
        if (batchNo == null || batchNo.equals("")) {
            batchNo = "";
        }
        if (waveHouseID == null) {
            waveHouseID = "0";
        }
        if (BasicShareUtil.getInstance(mContext).getIsOL()) {
            InStoreNumBean iBean = new InStoreNumBean();
            iBean.FStockPlaceID = waveHouseID;
            iBean.FBatchNo = batchNo;
            iBean.FStockID = storageID;
            iBean.FItemID = (product.FItemID);
            String json = new Gson().toJson(iBean);
            Log.e("inStorenum", json);
            Asynchttp.post(mContext, getBaseUrl() + WebApi.GETINSTORENUM, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    qty = Double.parseDouble(cBean.returnJson);
                    tvKucun.setText(qty+"");
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    qty = 0.0;
                }
            });
        } else {
            InStorageNumDao inStorageNumDao = daosession.getInStorageNumDao();
            List<InStorageNum> list1 = inStorageNumDao.queryBuilder().
                    where(InStorageNumDao.Properties.FItemID.eq(product.FItemID), InStorageNumDao.Properties.FStockID.eq(storageID),
                            InStorageNumDao.Properties.FStockPlaceID.eq(waveHouseID == null ? "0" : waveHouseID), InStorageNumDao.Properties.FBatchNo.eq(batchNo == null ? "" : batchNo)).build().list();
            if (list1.size() > 0) {
                Log.e("FQty", list1.get(0).FQty);
                qty = Double.parseDouble(list1.get(0).FQty);
                tvKucun.setText(qty+"");
                Log.e("qty", qty + "");


            } else {
                qty = 0.0;
                tvKucun.setText(qty+"");
            }

        }


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


    private void AddorderBefore() {
        if (product == null) {
            Toast.showText(mContext,"未选中物料");
            MediaPlayer.getInstance(mContext).error();
            lockScan(0);
            return;
        }
        String discount = "";
        String num = edNum.getText().toString();
        T_DetailDao t_detailDao = daosession.getT_DetailDao();
        T_mainDao t_mainDao = daosession.getT_mainDao();

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

//        //是否开启库存管理 true，开启允许负库存
//        if (!checkStorage) {
//            if ((qty / unitrate) < Double.parseDouble(num)) {
//                MediaPlayer.getInstance(mContext).error();
//                Toast.showText(mContext, "大兄弟，库存不够了");
//                lockScan(0);
//                return;
//            }
//        }
//        if (Hawk.get(Info.FirstInOut,"1").equals("1")){
//            App.getRService().doIOAction(WebApi.CheckFirstInOut,barcode, new MySubscribe<CommonResponse>() {
//                @Override
//                public void onNext(CommonResponse commonResponse) {
//                    DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
//                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Check_First_InOut,dBean));
//                }
//                @Override
//                public void onError(Throwable e) {
//                    Toast.showText(mContext,"查找先进先出失败."+e.getMessage());
//                }
//            });
//        }else{
            //插入条码唯一临时表

        if (code4Plan){
            Addorder();
        }else{
            if ("成品".equals(pushDownSub.FProductType)){
                //插入条码唯一临时表
                CodeCheckBean bean = new CodeCheckBean(barcode,ordercode + "",num,BasicShareUtil.getInstance(mContext).getIMIE());
                bean.FStockID=storageID == null ? "" : storageID;
                bean.FStockPlaceID=waveHouseID == null ? "0" : waveHouseID;
                DataModel.codeInsert(WebApi.CodeInsert4OutRed,gson.toJson(bean));
            }else{
                Addorder();
            }

        }

//        }

    }

    private void Addorder() {
//        if (product != null) {
//            Toast.showText(mContext,"未选中物料");
//            MediaPlayer.getInstance(mContext).error();
//            return;
//        }
        String discount = "";
        String num = edNum.getText().toString();
        batchNo = edPihao.getText().toString();
//            T_DetailDao t_detailDao = daosession.getT_DetailDao();
//            T_mainDao t_mainDao = daosession.getT_mainDao();
//
//            if (edNum.getText().toString().equals("")||edNum.getText().toString().equals("0")) {
//                MediaPlayer.getInstance(mContext).error();
//                Toast.showText(mContext, "请输入数量");
//                return;
//            }
//            if (fid == null) {
//                MediaPlayer.getInstance(mContext).error();
//                Toast.showText(mContext, "请选择单据");
//                return;
//            }
//            if (Double.parseDouble(pushDownSub.FAuxQty) < ((Double.parseDouble(num) * unitrate)/unitrateSub + Double.parseDouble(pushDownSub.FQtying))) {
//                MediaPlayer.getInstance(mContext).error();
//                Toast.showText(mContext, "大兄弟,您的数量超过我的想象");
//                return;
//            }
//
//            //是否开启库存管理 true，开启允许负库存
//            if (!checkStorage) {
//                if ((qty / unitrate) < Double.parseDouble(num)) {
//                    MediaPlayer.getInstance(mContext).error();
//                    Toast.showText(mContext, "大兄弟，库存不够了");
//                    return;
//                }
//            }
//
//            ProgressDialog pg = new ProgressDialog(mContext);
//            pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            pg.setMessage("请稍后...");
//            pg.setCancelable(false);
//            pg.show();
        LoadingUtil.showDialog(mContext,"请稍等...");

                    if (code4Plan) {
                        List<T_Detail> detailhebing = t_detailDao.queryBuilder().where(
                                T_DetailDao.Properties.Activity.eq(activity),
                                T_DetailDao.Properties.FInterID.eq(fid),
                                T_DetailDao.Properties.FUnitId.eq(unitId),
                                T_DetailDao.Properties.FProductId.eq(product.FItemID),
                                T_DetailDao.Properties.FStorageId.eq(storageID),
                                T_DetailDao.Properties.FPositionId.eq(waveHouseID == null ? "0" : waveHouseID),
                                T_DetailDao.Properties.FEntryID.eq(fentryid),
                                T_DetailDao.Properties.FBatch.eq(batchNo == null ? "" : batchNo)
                        ).build().list();
                        if (detailhebing.size() > 0) {
                            for (int i = 0; i < detailhebing.size(); i++) {
                                num = (Double.parseDouble(num) + Double.parseDouble(detailhebing.get(i).FQuantity)) + "";
//                                List<T_main> t_mainList = t_mainDao.queryBuilder().where(T_mainDao.Properties.FIndex.eq(detailhebing.get(i).FIndex)).build().list();
//                                if (t_mainList.size() > 0) {
//                                    t_mainDao.delete(t_mainList.get(0));
//                                }
                                t_detailDao.delete(detailhebing.get(i));
                            }
                        }
                    }
        String second = getTimesecond();
        T_main t_main = new T_main();
        t_main.tag = tag;
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
        t_main.FPurchaseUnit = "";
        t_main.FSalesMan = employeeName == null ? "" : employeeName;
        t_main.FSalesManId = employeeId == null ? "" : employeeId;
        t_main.FMaker = share.getUserName();
        t_main.FMakerId = share.getsetUserID();
        t_main.FDirector = spManager.getEmployeeName();
        t_main.FDirectorId = spManager.getEmployeeId();
        t_main.FPaymentType = payTypeName == null ? "" : payTypeName;
        t_main.FPaymentTypeId = payTypeId == null ? "" : payTypeId;
        t_main.saleWayId = SaleMethodId == null ? "" : SaleMethodId;
        t_main.saleWay = saleRangeName == null ? "" : saleRangeName;
        t_main.FDeliveryAddress = "";
        t_main.FRemark = "";
        t_main.FCustody = spSendman.getEmployeeId();
        t_main.FCustodyId = saleRangeId == null ? "" : saleRangeId;
        t_main.FAcount = sendMethodId == null ? "" : sendMethodId;
        t_main.FAcountID = spCapture.getEmployeeId();
        t_main.Rem = "";
        t_main.supplier = spCapture.getEmployeeId();
//        Log.e("captureID", captureID == null ? "" : captureID);
        t_main.supplierId = fwanglaiUnit == null ? "" : fwanglaiUnit;
        t_main.FSendOutId = "";
        t_main.FDeliveryType = fid == null ? "" : fid;
        t_main.sourceOrderTypeId = yuandanID == null ? "" : yuandanID;
        long insert1 = t_mainDao.insert(t_main);

        T_Detail t_detail = new T_Detail();
        t_detail.tag = tag;
        t_detail.FIndex = second;
        t_detail.FBarcode = barcode;
        t_detail.MakerId = share.getsetUserID();
        t_detail.DataInput = tvDate.getText().toString();
        t_detail.DataPush = tvDate.getText().toString();
        t_detail.IMIE = getIMIE();
        t_detail.activity = activity;
        t_detail.FOrderId = ordercode;
        t_detail.FBillNo = billNo;
        t_detail.FBatch = batchNo == null ? "" : batchNo;
        t_detail.FProductId = product.FItemID;
        t_detail.FProductName = product.FName;
        t_detail.FProductCode = product.FNumber;
        t_detail.FUnitId = unitId == null ? "" : unitId;
        t_detail.FUnit = unitName == null ? "" : unitName;
        t_detail.FStorage = storageName == null ? "" : storageName;
        t_detail.FStorageId = storageID == null ? "" : storageID;
        t_detail.FPosition = waveHouseName == null ? "" : waveHouseName;
        t_detail.FPositionId = waveHouseID == null ? "0" : waveHouseID;
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
            pushDownSub.FQtying = DoubleUtil.sum(Double.parseDouble(pushDownSub.FQtying) , (Double.parseDouble(edNum.getText().toString()) * unitrate)/unitrateSub) + "";
            pushDownSubDao.update(pushDownSub);
            Toast.showText(mContext, "添加成功");
            MediaPlayer.getInstance(mContext).ok();
            edNum.setText("");
            qty = 0.0;
            pushDownSubListAdapter.notifyDataSetChanged();
            resetAll();
            LoadingUtil.dismiss();
        } else {
            Toast.showText(mContext, "添加失败，请重试");
            MediaPlayer.getInstance(mContext).error();
            qty = 0.0;
            LoadingUtil.dismiss();
        }

        lockScan(0);
//        } else {
//            Toast.showText(mContext, "未选中物料");
//        }

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
//                                    && Double.parseDouble(dBean.InstorageNum.get(i).FQty) > 0) {
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
            }else{
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

    private void resetAll() {
        edNum.setText("");
        productName.setText("");
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
                    String main = "";
                    String detail = "";
                    T_main t_main = mains.get(i);
                    fidc.add(t_main.FDeliveryType);
                    main = t_main.FMakerId + "|" +
                            t_main.orderDate + "|" +
                            t_main.FPaymentDate + "|" +
                            t_main.supplierId + "|" +
                            t_main.saleWayId + "|" +
                            t_main.FDepartmentId + "|" +
                            t_main.FSalesManId + "|" +
                            t_main.FDirectorId + "|" +
                            t_main.FCustody + "|" +
                            t_main.supplier + "|"+
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
                                    t_detail.FStorageId + "|" +
                                    t_detail.FPositionId + "|" +
                                    t_detail.FEntryID + "|" +
                                    t_detail.FInterID + "|" +
                                    t_detail.FBatch + "|"+
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
        }
        pBean.list = data;
        DataModel.upload(WebApi.PdBackMsg2SaleOutUpload,gson.toJson(pBean));
//        postToServer(data);
    }

    private void postToServer(ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data) {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        pBean.list = data;
        Gson gson = new Gson();
        Asynchttp.post(mContext, getBaseUrl() + WebApi.PUSHDOWNSNUPLOAD, gson.toJson(pBean), new Asynchttp.Response() {
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
