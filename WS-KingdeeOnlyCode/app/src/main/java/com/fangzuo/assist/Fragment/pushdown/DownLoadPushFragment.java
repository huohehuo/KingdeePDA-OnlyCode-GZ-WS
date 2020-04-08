package com.fangzuo.assist.Fragment.pushdown;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.device.ScanDevice;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseFragment;
import com.fangzuo.assist.Activity.CGDDPDSLTZDActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Activity.FHTZDDBActivity;
import com.fangzuo.assist.Activity.HBDPDCPRKActivity;
import com.fangzuo.assist.Activity.OutCheckGoodsActivity;
import com.fangzuo.assist.Activity.OutsourcingOrdersIS2Activity;
import com.fangzuo.assist.Activity.OutsourcingOrdersISActivity;
import com.fangzuo.assist.Activity.OutsourcingOrdersOSActivity;
import com.fangzuo.assist.Activity.PdBackMsg2SaleOutRedActivity;
import com.fangzuo.assist.Activity.PdProductGetCheckActivity;
import com.fangzuo.assist.Activity.PdShouLiao2LLCheckActivity;
import com.fangzuo.assist.Activity.ProducePushInStore2Activity;
import com.fangzuo.assist.Activity.ProducePushInStoreActivity;
import com.fangzuo.assist.Activity.ProductSearchActivity;
import com.fangzuo.assist.Activity.PushDownMTActivity;
import com.fangzuo.assist.Activity.PushDownPOActivity;
import com.fangzuo.assist.Activity.PushDownPagerActivity;
import com.fangzuo.assist.Activity.PushDownSNActivity;
import com.fangzuo.assist.Activity.SCRWDPDSCHBDActivity;
import com.fangzuo.assist.Activity.ShengchanrenwudanxiatuilingliaoActivity;
import com.fangzuo.assist.Activity.ShouLiaoOrder2WwrkActivity;
import com.fangzuo.assist.Activity.ShouLiaoTongZhiActivity;
import com.fangzuo.assist.Activity.WwOrder2SLTZActivity;
import com.fangzuo.assist.Activity.XSDDPDFLTZDActivity;
import com.fangzuo.assist.Adapter.PushDownListAdapter;
import com.fangzuo.assist.Beans.CodeCheckBackDataBean;
import com.fangzuo.assist.Beans.CodeCheckBean;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownLoadSubListBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.PushDownDLBean;
import com.fangzuo.assist.Beans.PushDownListRequestBean;
import com.fangzuo.assist.Beans.PushDownListReturnBean;
import com.fangzuo.assist.Beans.ScanDLReturnBean;
import com.fangzuo.assist.Dao.Client;
import com.fangzuo.assist.Dao.PushDownMain;
import com.fangzuo.assist.Dao.PushDownSub;
import com.fangzuo.assist.Dao.Suppliers;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.DataModel;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.PushDownMainDao;
import com.fangzuo.greendao.gen.PushDownSubDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * //下载单据信息Fragment（所属：PushDownPagerActivity);
 */
public class DownLoadPushFragment extends BaseFragment {


    @BindView(R.id.sp_wlunit)
    Spinner spWlunit;           //往来单位
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.ed_dw)
    EditText edDw;
    @BindView(R.id.start_date)
    TextView startDate;
    @BindView(R.id.end_date)
    TextView endDate;
    @BindView(R.id.tv_dw)
    TextView tvDw;
    @BindView(R.id.btn_download)
    Button btnDownload;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.rl1)
    LinearLayout rl1;
    @BindView(R.id.lv_pushdown_download)
    ListView lvPushdownDownload;
    Unbinder unbinder;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.cb_all)
    CheckBox cbAll;
    private int tag;
    public String billNO;
    public String bringCode;
    private Activity activity;
    private FragmentActivity mContext;
    //    private SupplierSpAdapter supplierAdapter;
//    private ClientSpAdapter clientSpAdapter;
//    private String clientID;
//    private String supplierID;
    private boolean defaultsp = false;
    private ArrayList<Boolean> isCheck;
    private PushDownListAdapter pushDownListAdapter;
    private ArrayList<PushDownMain> downloadIDs;            //用于listview选择时，添加临时对象
    private PushDownListReturnBean puBean;
    private DaoSession daosession;
    private String enddate;
    private String startdate;
    private ArrayList<PushDownMain> container;
    private ScanDevice sm;
    private Intent intent;
    private String TAG = "DownLoadPushFragment";
    private CodeCheckBackDataBean codeCheckBackDataBean;
    private T_DetailDao t_detailDao;
    private int searchType;
    private String searchWldw = "";


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ClassEvent event) {
        if (event != null) {
            switch (event.Msg) {
                case EventBusInfoCode.CodeCheck_OK://检测条码成功
                    codeCheckBackDataBean = (CodeCheckBackDataBean) event.postEvent;
                    Lg.d("条码检查：" + codeCheckBackDataBean.toString());
                    Lg.e("什么鬼");
                    downFromScan(codeCheckBackDataBean.FBillNo);
//                    edPihao.setText(codeCheckBackDataBean.FBatchNo);
//                    edNum.setText(codeCheckBackDataBean.FQty);
                    LoadingUtil.dismiss();
//                    setDATA(codeCheckBackDataBean.FItemID,false);
                    break;
                case EventBusInfoCode.CodeCheck_Error://检测条码失败
                    LoadingUtil.dismiss();//ps：可能添加过了，但是还是跳转过去，并且自动添加，再去提示
                    codeCheckBackDataBean = (CodeCheckBackDataBean) event.postEvent;
//                    Toast.showText(mContext, codeCheckBackDataBean.FTip);
                    LoadingUtil.dismiss();
                    if (codeCheckBackDataBean.FBillNo==null){
                        downFromScan(billNO);
                    }else{
                        downFromScan(codeCheckBackDataBean.FBillNo);
                    }
                    break;
                case EventBusInfoCode.Search_client:
                    Client client = (Client) event.postEvent;
                    edDw.setText(client.FName);
                    searchWldw = client.FItemID;
                    break;
                case EventBusInfoCode.Search_Supplier:
                    Suppliers suppliers = (Suppliers) event.postEvent;
                    edDw.setText(suppliers.FName);
                    searchWldw = suppliers.FItemID;
                    break;
//                case EventBusInfoCode.CodeInsert_OK://写入条码唯一表成功
//                    Addorder();
//                    break;
//                case EventBusInfoCode.CodeInsert_Error://写入条码唯一表失败
//                    codeCheckBackDataBean = (CodeCheckBackDataBean)event.postEvent;
//                    Toast.showText(mContext, codeCheckBackDataBean.FTip);
//                    break;
//                case EventBusInfoCode.Upload_OK://回单成功
//                    t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(
//                            T_DetailDao.Properties.Activity.eq(activity)
//                    ).build().list());
//                    t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(
//                            T_mainDao.Properties.Activity.eq(activity)
//                    ).build().list());
//                    btnBackorder.setClickable(true);
//                    LoadingUtil.dismiss();
//                    MediaPlayer.getInstance(mContext).ok();
//                    break;
//                case EventBusInfoCode.Upload_Error://回单失败
//                    String error = (String)event.postEvent;
//                    Toast.showText(mContext, error);
//                    btnBackorder.setClickable(true);
//                    LoadingUtil.dismiss();
//                    MediaPlayer.getInstance(mContext).error();
//                    break;

            }
        }
    }

    @Override
    protected void initView() {
        EventBusUtil.register(this);
        isCheck = new ArrayList<>();
        downloadIDs = new ArrayList<>();
        daosession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        pushDownMainDao = daosession.getPushDownMainDao();
        t_detailDao = daosession.getT_DetailDao();

        if (tag == 1 || tag == 3) {
            searchType = Info.SEARCHCLIENT;
            //客户信息绑定
//            clientSpAdapter = CommonMethod.getMethod(mContext).getCilent(spWlunit);
        } else {
            searchType = Info.SEARCHSUPPLIER;
            //供应商信息绑定
//            supplierAdapter = CommonMethod.getMethod(mContext).getSupplier(spWlunit);
        }

        if (!"".equals(bringCode)){
            Lg.e("返回的单号："+bringCode);
            OnReceive(bringCode);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Lg.e("Fragment初始化完毕");
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        try{
//            EventBusUtil.unregister(this);
//        }catch (Exception e){
//
//        }
//    }

    private String barcode = "";

    @Override
    protected void OnReceive(String barCode) {
        Log.e("Fragment-code:", barCode);
        if (tag == 23 || tag == 3 || tag == 28 || tag == 29 || tag == 31  || tag == 4 ){
//            if (checkHasBarCode().size() > 0) {
//                ArrayList<String> list = new ArrayList<>();
//                list.add(checkHasBarCode().get(0).FInterID);
//                OutsourcingOrdersISActivity.start(mContext, barcode, list);
//            }else{
                downFromScan(barCode);
//            }
        }else if (tag==14 || tag == 30){
            List<String> list = CommonUtil.ScanBack(barCode);
            if (list.size()>0){
                downFromScan(list.get(2));
            }
        }else{
            LoadingUtil.showDialog(mContext, "正在查找...");
            //查询条码唯一表
            CodeCheckBean bean = new CodeCheckBean(barcode);
            DataModel.codeCheckForIn(new Gson().toJson(bean));
        }

    }

    //本地扫码唯一判断
    private List<T_Detail> checkHasBarCode() {
        List<T_Detail> list = t_detailDao.queryBuilder().where(
//                T_DetailDao.Properties.Activity.eq(Config.PDActivity),
                T_DetailDao.Properties.FBarcode.eq(barcode)
        ).build().list();
        if (list.size() > 0) {
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    private void downFromScan(final String billNo) {
        if (null == billNo || "".equals(billNo)) {
            Lg.e("aaaaaaaa");
            if (tag == 9) {
                if (checkHasBarCode().size() > 0) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(checkHasBarCode().get(0).FInterID);
                    ProducePushInStoreActivity.start(mContext, barcode, list);
                }
                try{
                    getActivity().finish();
                }catch (Exception e){}
            }else if (tag == 25){
                if (checkHasBarCode().size() > 0) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(checkHasBarCode().get(0).FInterID);
                    ProducePushInStore2Activity.start(mContext, barcode, list);
                }
                try{
                    getActivity().finish();
                }catch (Exception e){}
            } else if (tag == 11){
                if (checkHasBarCode().size() > 0) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(checkHasBarCode().get(0).FInterID);
                    OutsourcingOrdersISActivity.start(mContext, barcode, list);
                }
                try{
                    getActivity().finish();
                }catch (Exception e){}
            }
            else if (tag == 26){
                if (checkHasBarCode().size() > 0) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(checkHasBarCode().get(0).FInterID);
                    OutsourcingOrdersIS2Activity.start(mContext, barcode, list);
                }
                try{
                    getActivity().finish();
                }catch (Exception e){}
            }
        } else {
            Lg.e("ssssssss");
            final ProgressDialog pg = new ProgressDialog(mContext);
            pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pg.setMessage("正在下载..");
            pg.show();
            Toast.showText(mContext, billNo + "下载中...");
            PushDownListRequestBean pBean = new PushDownListRequestBean();
            pBean.id = tag;
            pBean.code = billNo;
            Asynchttp.post(
                    mContext,
                    BasicShareUtil.getInstance(mContext).getBaseURL() + WebApi.SCANTODLPDLIST,
                    new Gson().toJson(pBean),
                    new Asynchttp.Response() {
                        @Override
                        public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                            pg.dismiss();
                            Log.e(TAG, "OnReceive-获取数据:" + cBean.returnJson);
                            final ScanDLReturnBean sBean = new Gson().fromJson(cBean.returnJson, ScanDLReturnBean.class);
                            PushDownSubDao pushDownSubDao = daosession.getPushDownSubDao();
                            List<PushDownMain> list = pushDownMainDao.queryBuilder().where(
                                    PushDownMainDao.Properties.Tag.eq(tag),
                                    PushDownMainDao.Properties.FInterID.eq(sBean.list1.get(0).FInterID)
                            ).build().list();
                            if (list.size() > 0) {
//                            pushDownMainDao.deleteInTx(list);
//                            List<PushDownSub> pushDownSubs = pushDownSubDao.queryBuilder().where(PushDownSubDao.Properties.FInterID.eq(sBean.list1.get(0).FInterID)).build().list();
//                            if(pushDownSubs.size()>0){ pushDownSubDao.deleteInTx(pushDownSubs);}
//                            T_mainDao t_mainDao = daosession.getT_mainDao();
//                            T_DetailDao t_detailDao = daosession.getT_DetailDao();
//                            t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(T_mainDao.Properties.FDeliveryType.eq(sBean.list1.get(0).FInterID)).build().list());
//                            t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(T_DetailDao.Properties.FInterID.eq(sBean.list1.get(0).FInterID)).build().list());
                            } else {
                                pushDownMainDao.insert(sBean.list1.get(0));
                                pushDownSubDao.insertInTx(sBean.list);
                            }

                            final ArrayList<String> container = new ArrayList<>();
                            container.add(sBean.list1.get(0).FInterID);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Bundle b = new Bundle();
                                    b.putStringArrayList("fid", container);
                                    if (tag == 9) {
                                        ProducePushInStoreActivity.start(mContext, barcode, container);
                                        try{
                                            getActivity().finish();
                                        }catch (Exception e){}
                                    }else if (tag == 25 ){
                                        ProducePushInStore2Activity.start(mContext, barcode, container);
                                        try{
                                            getActivity().finish();
                                        }catch (Exception e){}
                                    }else if (tag == 11 ){
                                        OutsourcingOrdersISActivity.start(mContext, barcode, container);
                                        try{
                                            getActivity().finish();
                                        }catch (Exception e){}
                                    }else if (tag == 26 ){
                                        OutsourcingOrdersIS2Activity.start(mContext, barcode, container);
                                        try{
                                            getActivity().finish();
                                        }catch (Exception e){}
                                    }else {
                                        switch (tag) {
                                            case 1://销售订单下推销售出库
                                                intent = new Intent(mContext, PushDownMTActivity.class);
                                                break;
                                            case 2://采购订单下推外购入库
                                                intent = new Intent(mContext, PushDownPOActivity.class);
                                                break;
                                            case 3://发货通知下推销售出库
                                                intent = new Intent(mContext, PushDownSNActivity.class);
                                                break;
                                            case 4://收料通知下推外购入库
                                                intent = new Intent(mContext, ShouLiaoTongZhiActivity.class);
                                                break;
//                                            case 11://委外订单下推委外入库
//                                                intent = new Intent(mContext, OutsourcingOrdersISActivity.class);
//                                                break;
                                            case 12://委外订单下推委外出库
                                                intent = new Intent(mContext, OutsourcingOrdersOSActivity.class);
                                                break;
//                                            case 9://生产任务单下推产品入库
//                                                intent = new Intent(mContext, ProducePushInStoreActivity.class);
//                                                break;
                                            case 13://生产任务单下推生产领料
                                                intent = new Intent(mContext, ShengchanrenwudanxiatuilingliaoActivity.class);
                                                break;
                                            case 14://采购订单下推收料通知单
                                                intent = new Intent(mContext, CGDDPDSLTZDActivity.class);
                                                break;
                                            case 30://采购订单下推收料通知单
                                                intent = new Intent(mContext, WwOrder2SLTZActivity.class);
                                                break;
                                            case 15://销售订单下推发料通知单
                                                intent = new Intent(mContext, XSDDPDFLTZDActivity.class);
                                                break;
                                            case 16://生产任务单下推生生产汇报单
                                                intent = new Intent(mContext, SCRWDPDSCHBDActivity.class);
                                                break;
                                            case 18://汇报单下推产品入库
                                                intent = new Intent(mContext, HBDPDCPRKActivity.class);
                                                break;
                                            case 7://销售出库单验货
                                                intent = new Intent(mContext, OutCheckGoodsActivity.class);
                                                break;
                                            case 20://发货通知生成调拨单
                                                intent = new Intent(mContext, FHTZDDBActivity.class);
                                                break;
                                            case 23://退货通知单下推销售出库红字
                                                intent = new Intent(mContext, PdBackMsg2SaleOutRedActivity.class);
                                                break;
                                            case 28://退货通知单下推销售出库红字
                                                intent = new Intent(mContext, PdShouLiao2LLCheckActivity.class);
                                                break;
                                            case 29://生产领料单验货
                                                intent = new Intent(mContext, PdProductGetCheckActivity.class);
                                                break;
                                            case 31://收料通知单下推委外入库（原材料）
                                                intent = new Intent(mContext, ShouLiaoOrder2WwrkActivity.class);
                                                break;
//                            case 8:
//                                intent = new Intent(mContext, GetGoodsCheckActivity.class);
//                                break;
//                            case 10:
//                                intent = new Intent(mContext, PDProduceReportPROISActivity.class);
//                                break;
                                        }


                                        intent.putExtras(b);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }

                                }
                            }, 200);

                        }

                        @Override
                        public void onFailed(String Msg, AsyncHttpClient client) {
                            pg.dismiss();
                            Toast.showText(mContext, Msg);
                        }
                    });
        }

    }

    @Override
    protected void initData() {
        startDate.setText(getTimeBegin());
        endDate.setText(getTime(true));

    }

    @Override
    protected void initListener() {
        //下拉刷新
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchList();
                refresh.setRefreshing(false);
            }
        });

        cbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheck.clear();
                if (null!=pushDownListAdapter){
                    if (isChecked){
                        for (int i = 0; i < pushDownListAdapter.getCount(); i++) {
                            isCheck.add(true);
                            downloadIDs.add((PushDownMain) pushDownListAdapter.getItem(i));
                        }
                    }else{
                        for (int i = 0; i < pushDownListAdapter.getCount(); i++) {
                            if (downloadIDs.contains((PushDownMain) pushDownListAdapter.getItem(i))) {
                                downloadIDs.remove((PushDownMain) pushDownListAdapter.getItem(i));
                            }
                            isCheck.add(false);
                        }
                    }
                    pushDownListAdapter.notifyDataSetChanged();
                }else{
                    Toast.showText(mContext,"请先查找单据");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cbAll.setChecked(false);
                        }
                    }, 50);
                }

            }
        });

//        //选择往来单位（供应商或者客户）
//        spWlunit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (!defaultsp) {
//                    clientID = "";
//                    supplierID = "";
//                    defaultsp = true;
//                    Log.e("defaultsp",defaultsp+"");
//                } else {
//                    if (tag == 1 || tag == 3) {
//                        if (clientSpAdapter != null) {
//                            Client client = (Client) clientSpAdapter.getItem(i);
//                            clientID = client.FItemID;
//                        }
//                    } else {
//                        if (supplierAdapter != null) {
//                            Suppliers supplier = (Suppliers) supplierAdapter.getItem(i);
//                            supplierID = supplier.FItemID;
//                        }
//                    }
//                }
//
//                Log.e("client", clientID);
//                Log.e("supplier", supplierID);
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        //列表下载的选择处理

        lvPushdownDownload.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PushDownMain pushDownListReturnBean = (PushDownMain) pushDownListAdapter.getItem(i);
                if (isCheck.get(i)) {
                    isCheck.set(i, false);
                    for (int j = 0; j < downloadIDs.size(); j++) {
                        if (downloadIDs.get(j).FInterID.equals(pushDownListReturnBean.FInterID)) {
                            downloadIDs.remove(j);
                        }
                    }
                } else {
                    isCheck.set(i, true);
                    downloadIDs.add(pushDownListReturnBean);
                }
                pushDownListAdapter.notifyDataSetChanged();
            }
        });
    }


    public DownLoadPushFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        tag = ((PushDownPagerActivity) activity).getTitles();
        billNO = ((PushDownPagerActivity) activity).getBillNo();
        bringCode = ((PushDownPagerActivity) activity).getBarcode();
        Log.e("获取到--tag--dpf", tag + "");
        Log.e("获取到--billNO--dpf", billNO + "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_down_load_push, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            EventBusUtil.unregister(this);
        } catch (Exception e) {

        }
        unbinder.unbind();
    }


    //点击事件
    @OnClick({R.id.btn_download, R.id.btn_search, R.id.start_date, R.id.end_date, R.id.search_dw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_download:
                if (downloadIDs.size() > 0) {
                    LoadingUtil.showDialog(mContext, "下载中...");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            download(downloadIDs);
                        }
                    }, 100);
                } else {
                    Toast.showText(mContext, "未选择单号");
                }
                break;
            case R.id.btn_search:
                searchList();
                break;
            case R.id.start_date:
                datePicker(startDate);
                break;
            case R.id.end_date:
                datePicker(endDate);
                break;
            case R.id.search_dw:
                Bundle b = new Bundle();
                b.putString("search", edDw.getText().toString());
                b.putInt("where", searchType);
                startNewActivity(ProductSearchActivity.class, b);
                break;
        }
    }
    int num;
    PushDownMainDao pushDownMainDao;
    List<PushDownMain> pushDownMains;
    //下载数据
    private void download(final ArrayList<PushDownMain> downloadIDs) {
        num=downloadIDs.size();
        pushDownMains = pushDownMainDao.queryBuilder().where(
                PushDownMainDao.Properties.Tag.eq(tag)
        ).build().list();
        boolean hasData = false;
        for (int i = 0; i < downloadIDs.size(); i++) {
            for (int j = 0; j < pushDownMains.size(); j++) {
                if (pushDownMains.get(j).FInterID.equals(downloadIDs.get(i).FInterID)){
                    hasData =true;
                }
            }
        }
        if (hasData){
            LoadingUtil.dismiss();
            LoadingUtil.showAlter(mContext,"注意","本地已存在选择下载的单据，请确认本地未存在数据或手动删除已存在单据");
            return;
        }

            for (int i = 0; i < downloadIDs.size(); i++) {
            final PushDownMain pushDownMain = downloadIDs.get(i);
            final int finalI = i;
            Log.e("finterid", i + "");
            Log.e("finterid2", finalI + "");
            DownLoadSubListBean dBean = new DownLoadSubListBean();
            dBean.interID = downloadIDs.get(i).FInterID;
            dBean.tag = downloadIDs.get(i).tag;
            //获取下推订单信息
            App.getRService().doIOAction(WebApi.PUSHDOWNDLLIST, new Gson().toJson(dBean), new MySubscribe<CommonResponse>() {
                @Override
                public void onNext(CommonResponse cBean) {
                    super.onNext(cBean);
                    //查找本地的单据信息，若和选择的单据id相同，则删除本地相对应的单据信息，
                    Log.e(TAG, "download-获取数据:" + cBean.returnJson);
                    PushDownDLBean pBean = new Gson().fromJson(cBean.returnJson, PushDownDLBean.class);
                    PushDownSubDao pushDownSubDao = daosession.getPushDownSubDao();
                    for (int j = 0; j < pushDownMains.size(); j++) {
                        if (pushDownMains.get(j).FInterID.equals(downloadIDs.get(finalI).FInterID)) {
                            pushDownMainDao.delete(pushDownMains.get(j));
                            //查找本地的下推订单信息，若和选择的下推订单id相同，则删除本地相对应的下推订单信息，
                            pushDownSubDao.deleteInTx(pushDownSubDao.queryBuilder().where(
                                    PushDownSubDao.Properties.Tag.eq(tag),
                                    PushDownSubDao.Properties.FInterID.eq(pushDownMains.get(j).FInterID)
                            ).build().list());
//                            List<PushDownSub> pushDownSubs = pushDownSubDao.loadAll();
//                            for (int k = 0; k < pushDownSubs.size(); k++) {
//                                if (pushDownMains.get(j).FInterID.equals(pushDownSubs.get(k).FInterID)) {
//                                    pushDownSubDao.delete(pushDownSubs.get(k));
//                                }
//                            }
                        }
                    }
                    //异步添加下推订单信息
                    pushDownSubDao.insertInTx(pBean.list);
//                    for (int j = 0; j < pBean.list.size(); j++) {
//                        pushDownSubDao.insertOrReplaceInTx(pBean.list.get(j));
//                    }

                    T_mainDao t_mainDao = daosession.getT_mainDao();
                    T_DetailDao t_detailDao = daosession.getT_DetailDao();
                    //删除本地指定数据
                    t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(
                            T_mainDao.Properties.Tag.eq(tag),
                            T_mainDao.Properties.FDeliveryType.eq(
                                    downloadIDs.get(finalI).FInterID)
                    ).build().list());
                    //删除本地指定数据
                    t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(
                            T_DetailDao.Properties.Tag.eq(tag),
                            T_DetailDao.Properties.FInterID.eq(
                                    downloadIDs.get(finalI).FInterID)
                    ).build().list());
                    //异步添加单据信息
                    pushDownMainDao.insertInTx(pushDownMain);
//                    pushDownMainDao.insertOrReplaceInTx(pushDownMain);
                    if (num>1){
                        num=num-1;
                    }
                    if (num<2){
                    Toast.showText(mContext, "下载成功");
                        LoadingUtil.dismiss();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    if (num>1){
                        num=num-1;
                    }
                    Lg.e("错误："+pushDownMain.FBillNo+"--"+e.toString());
                    Toast.showText(mContext, e.toString());
                    if (num<2){
                        LoadingUtil.dismiss();
                    }
                }
            });
//            Asynchttp.post(mContext,
//                    BasicShareUtil.getInstance(mContext).getBaseURL() + WebApi.PUSHDOWNDLLIST,
//                    new Gson().toJson(dBean), new Asynchttp.Response() {
//                        @Override
//                        public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                            //查找本地的单据信息，若和选择的单据id相同，则删除本地相对应的单据信息，
//                            PushDownMainDao pushDownMainDao = daosession.getPushDownMainDao();
//                            List<PushDownMain> pushDownMains = pushDownMainDao.loadAll();
//                            Log.e(TAG, "download-获取数据:" + cBean.returnJson);
//                            PushDownDLBean pBean = new Gson().fromJson(cBean.returnJson, PushDownDLBean.class);
//                            PushDownSubDao pushDownSubDao = daosession.getPushDownSubDao();
//
//                            for (int j = 0; j < pushDownMains.size(); j++) {
//                                if (pushDownMains.get(j).FInterID.equals(downloadIDs.get(finalI).FInterID)) {
//                                    pushDownMainDao.delete(pushDownMains.get(j));
//                                    //查找本地的下推订单信息，若和选择的下推订单id相同，则删除本地相对应的下推订单信息，
//                                    List<PushDownSub> pushDownSubs = pushDownSubDao.loadAll();
//                                    for (int k = 0; k < pushDownSubs.size(); k++) {
//                                        if (pushDownMains.get(j).FInterID.equals(pushDownSubs.get(k).FInterID)) {
//                                            pushDownSubDao.delete(pushDownSubs.get(k));
//                                        }
//                                    }
//                                }
//                            }
//                            //异步添加下推订单信息
//                            for (int j = 0; j < pBean.list.size(); j++) {
//                                pushDownSubDao.insertOrReplaceInTx(pBean.list.get(j));
//                            }
//
//                            T_mainDao t_mainDao = daosession.getT_mainDao();
//                            T_DetailDao t_detailDao = daosession.getT_DetailDao();
//                            //删除本地指定数据
//                            t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(
//                                    T_mainDao.Properties.FDeliveryType.eq(
//                                            downloadIDs.get(finalI).FInterID)
//                            ).build().list());
//                            //删除本地指定数据
//                            t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(
//                                    T_DetailDao.Properties.FInterID.eq(
//                                            downloadIDs.get(finalI).FInterID)
//                            ).build().list());
//                            //异步添加单据信息
//                            pushDownMainDao.insertOrReplaceInTx(pushDownMain);
//                            Toast.showText(mContext, "下载成功");
//                            LoadingUtil.dismiss();
//                        }
//
//                        @Override
//                        public void onFailed(String Msg, AsyncHttpClient client) {
//                            Toast.showText(mContext, Msg);
//                            LoadingUtil.dismiss();
//                        }
//                    });
        }

    }

    //获取数据
    private void searchList() {
        cbAll.setChecked(false);
        LoadingUtil.showDialog(mContext, "正在加载...");
        container = new ArrayList<>();
        isCheck = new ArrayList<>();
        String code = edCode.getText().toString();
        String endtime = endDate.getText().toString();
        String startTime = startDate.getText().toString();

        PushDownListRequestBean pBean = new PushDownListRequestBean();
        pBean.id = tag;
        pBean.code = code;
        pBean.StartTime = startTime;
        pBean.endTime = endtime;
        if ("".equals(edDw.getText().toString().trim())) {
            pBean.FWLUnitID = "";
        } else {
            pBean.FWLUnitID = searchWldw;
        }
//        if (tag == 1 || tag == 3) {
//            pBean.FWLUnitID = clientID;
//        } else {
//            pBean.FWLUnitID = supplierID;
//        }
        String Json = new Gson().toJson(pBean);
        //获取单据信息
        App.getRService().doIOAction(WebApi.PUSHDOWNLIST, Json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                super.onNext(commonResponse);
                if (!commonResponse.state)return;
                LoadingUtil.dismiss();
                puBean = new Gson().fromJson(commonResponse.returnJson, PushDownListReturnBean.class);
                Log.e("获取单据信息数：", puBean.list.size() + "");
                isCheck.clear();
                for (int i = 0; i < puBean.list.size(); i++) {
                    isCheck.add(false);
                }
                container.addAll(puBean.list);
                if (null!=puBean.list)btnSearch.setText("查找 "+puBean.list.size()+"条");
                pushDownListAdapter = new PushDownListAdapter(mContext, container, isCheck);
                if (lvPushdownDownload != null) {//可防止还没刷新出数据就退出页面后的崩溃问题
                    lvPushdownDownload.setAdapter(pushDownListAdapter);
                    pushDownListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LoadingUtil.dismiss();
//                Toast.showText(mContext, e.toString());
                btnSearch.setText("查找");
                pushDownListAdapter = new PushDownListAdapter(mContext, container, isCheck);
                if (lvPushdownDownload != null) {
                    lvPushdownDownload.setAdapter(pushDownListAdapter);
                }
                pushDownListAdapter.notifyDataSetChanged();
            }
        });
//        Asynchttp.post(
//                mContext,
//                BasicShareUtil.getInstance(mContext).getBaseURL() + WebApi.PUSHDOWNLIST,
//                Json,
//                new Asynchttp.Response() {
//                    @Override
//                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                        LoadingUtil.dismiss();
//                        puBean = new Gson().fromJson(cBean.returnJson, PushDownListReturnBean.class);
//                        Log.e("获取单据信息数：", puBean.list.size() + "");
//                        isCheck.clear();
//                        for (int i = 0; i < puBean.list.size(); i++) {
//                            isCheck.add(false);
//                        }
//                        container.addAll(puBean.list);
//                        btnSearch.setText("查找 "+puBean.list.size());
//                        pushDownListAdapter = new PushDownListAdapter(mContext, container, isCheck);
//                        if (lvPushdownDownload != null) {//可防止还没刷新出数据就退出页面后的崩溃问题
//                            lvPushdownDownload.setAdapter(pushDownListAdapter);
//                            pushDownListAdapter.notifyDataSetChanged();
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(String Msg, AsyncHttpClient client) {
//                        LoadingUtil.dismiss();
//                        Toast.showText(mContext, Msg);
//                        btnSearch.setText("查找");
//                        pushDownListAdapter = new PushDownListAdapter(mContext, container, isCheck);
//                        if (lvPushdownDownload != null) {
//                            lvPushdownDownload.setAdapter(pushDownListAdapter);
//                        }
//                        pushDownListAdapter.notifyDataSetChanged();
//
//                    }
//                });
    }

    public String getTime(boolean b) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(b ? "yyyy-MM-dd" : "yyyyMMdd");
        Date curDate = new Date();
        Log.e("date", curDate.toString());
        String str = format.format(curDate);
        return str;
    }

    public String getTimeBegin() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Date curDate = new Date();
        Log.e("date", curDate.toString());
        String str = format.format(curDate);
        return str + "-01-01";
    }
}
