package com.fangzuo.assist.Fragment.pushdown;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseFragment;
import com.fangzuo.assist.Activity.CGDDPDSLTZDActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Activity.DbCheckGoodsActivity;
import com.fangzuo.assist.Activity.FHTZDDBActivity;
import com.fangzuo.assist.Activity.HBDPDCPRKActivity;
import com.fangzuo.assist.Activity.InCheckGoodsActivity;
import com.fangzuo.assist.Activity.OutCheckGoodsActivity;
import com.fangzuo.assist.Activity.OutsourcingOrdersISActivity;
import com.fangzuo.assist.Activity.OutsourcingOrdersOSActivity;
import com.fangzuo.assist.Activity.PdBackMsg2SaleOutRedActivity;
import com.fangzuo.assist.Activity.ProducePushInStoreActivity;
import com.fangzuo.assist.Activity.ProductInCheckGoodsActivity;
import com.fangzuo.assist.Activity.ProductSearchActivity;
import com.fangzuo.assist.Activity.PushDownMTActivity;
import com.fangzuo.assist.Activity.PushDownPOActivity;
import com.fangzuo.assist.Activity.PushDownPagerActivity;
import com.fangzuo.assist.Activity.PushDownSNActivity;
import com.fangzuo.assist.Activity.SCRWDPDSCHBDActivity;
import com.fangzuo.assist.Activity.ShengchanrenwudanxiatuilingliaoActivity;
import com.fangzuo.assist.Activity.ShouLiaoTongZhiActivity;
import com.fangzuo.assist.Activity.XSDDPDFLTZDActivity;
import com.fangzuo.assist.Adapter.ClientSpAdapter;
import com.fangzuo.assist.Adapter.PushDownListAdapter;
import com.fangzuo.assist.Adapter.SupplierSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.Client;
import com.fangzuo.assist.Dao.PushDownMain;
import com.fangzuo.assist.Dao.PushDownSub;
import com.fangzuo.assist.Dao.Suppliers;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.CommonMethod;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.PushDownMainDao;
import com.fangzuo.greendao.gen.PushDownSubDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


//选择单据信息Fragment（所属：PushDownPagerActivity);
public class ChooseFragment extends BaseFragment {
    @BindView(R.id.sp_wlunit)
    Spinner spWlunit;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.start_date)
    TextView startDate;
    @BindView(R.id.end_date)
    TextView endDate;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.lv_pushdown_download)
    ListView lvPush;
    Unbinder unbinder;
    @BindView(R.id.btn_getpush)
    Button btnGetpush;
    @BindView(R.id.rl1)
    LinearLayout rl1;
    @BindView(R.id.v1)
    View v1;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.ed_dw)
    EditText edDw;
    @BindView(R.id.tv_dw)
    TextView tvDw;
    private FragmentActivity mContext;
    private DaoSession daosession;
    private ArrayList<Boolean> isCheck;
    private int year;
    private int month;
    private int day;
    private String enddate;
    private String startdate;
    private PushDownMainDao pushDownMainDao;
    private int tag;
//    private SupplierSpAdapter supplierAdapter;
//    private ClientSpAdapter clientSpAdapter;
//    private String supplierID;
private int searchType;
    private String searchWldw="";
    private boolean defaultsp = false;
    private List<PushDownMain> container;               //单据信息，用于存储查找到的单据数据
    private ArrayList<PushDownMain> downloadIDs;        //单据信息，用于存储选中的单据数据
    private PushDownListAdapter pushDownListAdapter;
    private Intent intent;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(ClassEvent event) {
        if (event != null) {
            switch (event.Msg) {
//            case EventBusInfoCode.DownData_OK:
//                String result = (String) event.postEvent;
//                LoadingUtil.dismiss();
//                if ("0".equals(result)) {
//                    long nowTime = Long.parseLong(event.Msg2);
//                    int size = Integer.parseInt(event.Msg3);
//                    long endTime = System.currentTimeMillis();
//                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
//                    ab.setTitle("下载完成");
//                    ab.setMessage("耗时:" + (endTime - nowTime) + "ms" + ",共插入" + size + "条数据");
//                    ab.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            startNewActivity(LoginActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, true, null);
//                        }
//                    });
//                    ab.create().show();
//                } else {
//                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
//                    ab.setTitle("下载错误");
//                    ab.setPositiveButton("确认", null);
//                    ab.create().show();
//                }
//                break;
                case EventBusInfoCode.Delete_Ok:
                    PushDownSubDao pushDownSubDao = daosession.getPushDownSubDao();
                    for (int i = 0; i < downloadIDs.size(); i++) {
                        List<PushDownSub> pushDownSubs = pushDownSubDao.queryBuilder().where(PushDownSubDao.Properties.FInterID.eq(downloadIDs.get(i).FInterID)).build().list();
                        for (int j = 0; j < pushDownSubs.size(); j++) {
                            pushDownSubDao.delete(pushDownSubs.get(j));
                        }
                        pushDownMainDao.delete(downloadIDs.get(i));
                        Toast.showText(mContext, "删除成功");
                    }

                    initList();
                    Search();
                    break;
                case EventBusInfoCode.Delete_Error:
                    Toast.showText(mContext,"删除数据有误，配置失败");
                    break;
                case EventBusInfoCode.Search_client:
                    Client client = (Client) event.postEvent;
                    edDw.setText(client.FName);
                    searchWldw=client.FItemID;
                    break;
                case EventBusInfoCode.Search_Supplier:
                    Suppliers suppliers = (Suppliers) event.postEvent;
                    edDw.setText(suppliers.FName);
                    searchWldw=suppliers.FItemID;
                    break;

            }


        }
    }

    public ChooseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        tag = ((PushDownPagerActivity) activity).getTitles();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        return view;
    }

    @Override
    protected void initView() {
        EventBusUtil.register(this);
        isCheck = new ArrayList<>();
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        downloadIDs = new ArrayList<PushDownMain>();
        if (tag == 1 || tag == 3) {
            searchType= Info.SEARCHCLIENT;
            //初始化客户数据
//            clientSpAdapter = CommonMethod.getMethod(mContext).getCilent(spWlunit);
        } else {
            searchType=Info.SEARCHSUPPLIER;
            //初始化往来单位数据
//            supplierAdapter = CommonMethod.getMethod(mContext).getSupplier(spWlunit);
        }
        container = new ArrayList<>();
        daosession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        t_mainDao = daosession.getT_mainDao();
        t_detailDao = daosession.getT_DetailDao();
        pushDownSubDao = daosession.getPushDownSubDao();
        pushDownMainDao = daosession.getPushDownMainDao();
        initList();
    }

    @Override
    protected void OnReceive(String barCode) {

    }

    @Override
    protected void initData() {

    }

    private void push() {
        boolean flag = true;
        ArrayList<String> container = new ArrayList<>();
        if (downloadIDs.size() == 0) {
            Toast.showText(mContext, "请选择单据");
            return;
        }
        if (tag == 24){
            if (downloadIDs.size()>=2){
                Toast.showText(mContext,"只允许单个订单下推");
                return;
            }
        }
        for (int i = 0; i < downloadIDs.size(); i++) {
            container.add(downloadIDs.get(i).FInterID);
            if (i > 0 && !downloadIDs.get(i).FSupplyID.equals(downloadIDs.get(i - 1).FSupplyID)) {
                flag = false;
                break;
            }
        }

        if (flag && downloadIDs.size() > 0) {
            Bundle b = new Bundle();
            b.putStringArrayList("fid", container);
            Log.e("ChooseFragment", "跳转数据：" + container.toString());
            switch (tag) {
//                case 1://销售订单下推销售出库
//                    intent = new Intent(mContext, PushDownMTActivity.class);
//                    break;
                case 9://生产任务单下推产品入库
                    intent = new Intent(mContext, ProducePushInStoreActivity.class);
                    break;
                case 13://生产任务单下推生产领料
                    intent = new Intent(mContext, ShengchanrenwudanxiatuilingliaoActivity.class);
                    break;
//                case 2://采购订单下推外购入库
//                    intent = new Intent(mContext, PushDownPOActivity.class);
//                    break;
                case 3://发货通知下推销售出库
                    intent = new Intent(mContext, PushDownSNActivity.class);
                    break;
//                case 4://收料通知下推外购入库
//                    intent = new Intent(mContext, ShouLiaoTongZhiActivity.class);
//                    break;
                case 11://委外订单下推委外入库
                    intent = new Intent(mContext, OutsourcingOrdersISActivity.class);
                    break;
                case 23://退货通知单下推销售出库红字
                    intent = new Intent(mContext, PdBackMsg2SaleOutRedActivity.class);
                    break;
                case 24://调拨单验货
                    intent = new Intent(mContext, DbCheckGoodsActivity.class);
                    break;
//                case 25://产品入库单验货
//                    intent = new Intent(mContext, ProductInCheckGoodsActivity.class);
//                    break;
//                case 12://委外订单下推委外出库
//                    intent = new Intent(mContext, OutsourcingOrdersOSActivity.class);
//                    break;
//                case 14://采购订单下推收料通知单
//                    intent = new Intent(mContext, CGDDPDSLTZDActivity.class);
//                    break;
//                case 15://销售订单下推发料通知单
//                    intent = new Intent(mContext, XSDDPDFLTZDActivity.class);
//                    break;
//                case 16://生产任务单下推生生产汇报单
//                    intent = new Intent(mContext, SCRWDPDSCHBDActivity.class);
//                    break;
                case 18://汇报单下推产品入库
                    intent = new Intent(mContext, HBDPDCPRKActivity.class);
                    break;
//                case 7://销售出库单验货
//                    intent = new Intent(mContext, OutCheckGoodsActivity.class);
//                    break;
//                case 20://发货通知生成调拨单
//                    intent = new Intent(mContext, FHTZDDBActivity.class);
//                    break;
//                        case 22://产品入库验货
//                            intent = new Intent(mContext, InCheckGoodsActivity.class);
//                            break;
            }


            intent.putExtras(b);
            startActivity(intent);
            getActivity().finish();
        } else {
            if (!flag) Toast.showText(mContext, "供应商不一致");
            else if (downloadIDs.size() < 0) Toast.showText(mContext, "未选择下推单据");
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            initList();
        } else {
            //相当于Fragment的onPause
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        initList();
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

    private void initList() {
        isCheck.clear();
        container.clear();
        downloadIDs.clear();

        //根据 tag 查找相应的单据
        List<PushDownMain> pushDownMains = pushDownMainDao.queryBuilder().where(
                PushDownMainDao.Properties.Tag.eq(tag)
        ).build().list();
        container.addAll(pushDownMains);
        for (int i = 0; i < pushDownMains.size(); i++) {
            isCheck.add(false);
        }
        Lg.e("choose列表数据："+new Gson().toJson(container));
        pushDownListAdapter = new PushDownListAdapter(mContext, container, isCheck);
        lvPush.setAdapter(pushDownListAdapter);
        pushDownListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {
        //刷新
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initList();
                refresh.setRefreshing(false);
            }
        });

        //列表选中监听
        lvPush.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PushDownMain pushDownListReturnBean = (PushDownMain) pushDownListAdapter.getItem(i);
                if (isCheck.get(i)) {
                    Log.e("choose", "不--选中");
                    isCheck.set(i, false);
                    for (int j = 0; j < downloadIDs.size(); j++) {
                        if (downloadIDs.get(j).FInterID.equals(pushDownListReturnBean.FInterID)) {
                            downloadIDs.remove(j);
                        }
                    }
                } else {
                    Log.e("choose", "选中");
                    isCheck.set(i, true);
                    downloadIDs.add(pushDownListReturnBean);
                }
                pushDownListAdapter.notifyDataSetChanged();
            }
        });
//        spWlunit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (!defaultsp) {
//                    supplierID = "";
//                } else {
//                    if (tag == 1 || tag == 3) {
//                        if (clientSpAdapter != null) {
//                            Client client = (Client) clientSpAdapter.getItem(i);
//                            supplierID = client.FItemID;
//                        }
//                    } else {
//                        if (supplierAdapter != null) {
//                            Suppliers supplier = (Suppliers) supplierAdapter.getItem(i);
//                            supplierID = supplier.FItemID;
//                        }
//                    }
//                }
//                defaultsp = true;
//                Log.e("supplier", supplierID);
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
    }

    public String getTime(boolean b) {
        SimpleDateFormat format = new SimpleDateFormat(b ? "yyyy-MM-dd" : "yyyyMMdd");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try{
            EventBusUtil.unregister(this);
        }catch (Exception e){

        }
        unbinder.unbind();
    }

    @OnClick({R.id.btn_delete, R.id.btn_search, R.id.btn_getpush, R.id.start_date, R.id.end_date, R.id.search_dw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_delete:
                delete();
                break;
            case R.id.btn_search:
                Search();
                break;
            case R.id.btn_getpush:
                push();
                break;
            case R.id.start_date:
                datePicker(startDate);
                break;
            case R.id.end_date:
                datePicker(endDate);
                break;
            case R.id.search_dw:
                Bundle b = new Bundle();
                b.putString("search",edDw.getText().toString());
                b.putInt("where", searchType);
                startNewActivity(ProductSearchActivity.class, b);
                break;

        }
    }

    private T_DetailDao t_detailDao;
    private PushDownSubDao pushDownSubDao;
    private T_mainDao t_mainDao;
    private PurchaseInStoreUploadBean pBean;
    private PurchaseInStoreUploadBean.purchaseInStore listBean;
    private ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data;
    private ArrayList<T_Detail> t_detailList;
    private ArrayList<T_main> t_mainsList;
    DownloadReturnBean dBean;
    List<T_Detail> list;
    List<T_Detail> detailsAll;
    List<T_main> list_main;
    List<T_main> mainsAll;
    List<PushDownSub> pushDownSubs;
    List<PushDownSub> pushDownSubsAll;
    //删除本地数据
    private void delete() {
        detailsAll = new ArrayList<>();
        mainsAll = new ArrayList<>();
        pushDownSubsAll = new ArrayList<>();
        for (int i = 0; i < downloadIDs.size(); i++) {
            list = t_detailDao.queryBuilder().where(
                    T_DetailDao.Properties.FInterID.eq(downloadIDs.get(i).FInterID)
            ).build().list();
            if (list.size()>0){
                detailsAll.addAll(list);
            }
            list_main = t_mainDao.queryBuilder().where(
                    T_mainDao.Properties.FDeliveryType.eq(downloadIDs.get(i).FInterID)
            ).build().list();
            if (list_main.size()>0){
                mainsAll.addAll(list_main);
            }

            pushDownSubs = pushDownSubDao.queryBuilder().where(
                    PushDownSubDao.Properties.FInterID.eq(downloadIDs.get(i).FInterID)
            ).build().list();
            if (pushDownSubs.size()>0){
                pushDownSubsAll.addAll(pushDownSubs);
            }

//            pushDownMainDao.delete(downloadIDs.get(i));
//            Toast.showText(mContext, "删除成功");
        }
        if (detailsAll.size()>0){
            pBean = new PurchaseInStoreUploadBean();
            listBean=pBean.new purchaseInStore();
            data = new ArrayList<>();
            String detail ="";
            listBean.main="";
            ArrayList<String> detailContainer = new ArrayList<>();
            for (int j = 0; j < detailsAll.size(); j++) {
                if (j != 0 && j % 49 == 0) {
                    Log.e("j%49", j % 49 + "");
                    T_Detail t_detail = detailsAll.get(j);
                    detail = detail +
                            t_detail.FBarcode + "|" +
                            t_detail.FQuantity + "|" +
                            t_detail.IMIE + "|" +
                            t_detail.FOrderId + "|";
                    detail = detail.subSequence(0, detail.length() - 1).toString();
                    detailContainer.add(detail);
                    detail = "";
                } else {
                    Log.e("j", j + "");
                    T_Detail t_detail = detailsAll.get(j);
                    detail = detail +
                            t_detail.FBarcode + "|" +
                            t_detail.FQuantity + "|" +
                            t_detail.IMIE + "|" +
                            t_detail.FOrderId + "|";
                    Log.e("detail1", detail);
                }
            }
            if (detail.length() > 0) {
                detail = detail.subSequence(0, detail.length() - 1).toString();
            }
            Log.e("detail", detail);
            detailContainer.add(detail);
            listBean.detail = detailContainer;
            data.add(listBean);
            pBean.list=data;
            Gson gson = new Gson();
            App.getRService().getCodeCheckDelete(gson.toJson(pBean), new MySubscribe<CommonResponse>() {
                @Override
                public void onNext(CommonResponse commonResponse) {
                    t_detailDao.deleteInTx(detailsAll);
                    t_mainDao.deleteInTx(mainsAll);
                    pushDownSubDao.deleteInTx(pushDownSubsAll);
                    pushDownMainDao.deleteInTx(downloadIDs);
                    Toast.showText(mContext, "删除成功");
                    initList();
                    Search();
                }

                @Override
                public void onError(Throwable e) {
                    Toast.showText(mContext, "删除失败");
//                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Delete_Error,"删除失败："+e.toString()));
                }
            });
        }else{
            try {
                pushDownSubDao.deleteInTx(pushDownSubsAll);
                pushDownMainDao.deleteInTx(downloadIDs);
                initList();
                Search();
            }catch (Exception e){

            }

        }


    }

    //查找本地数据
    private void Search() {
        if ("".equals(edDw.getText().toString().trim())){
            searchWldw="";
        }
        Lg.e("查询条件："+searchWldw);
        String code = edCode.getText().toString();
        String endtime = endDate.getText().toString();
        String startTime = startDate.getText().toString();
//        List<PushDownMain> list = pushDownMainDao.queryBuilder().where(
//                PushDownMainDao.Properties.FBillNo.like("%" + code + "%"),
//                PushDownMainDao.Properties.FSupplyID.eq(searchWldw),
//                PushDownMainDao.Properties.FDate.between(startTime, endtime)
//        ).build().list();
//        if (list.size() > 0) {
//            container.clear();
//            container.addAll(list);
//        } else {
//            Toast.showText(mContext, "未查询到数据");
//        }
        container.clear();
        isCheck.clear();
        String con="";
//        pushDownListAdapter.notifyDataSetChanged();
        if (!"".equals(endDate.getText().toString())&& !"".equals(startDate.getText().toString())) {
            con += " and  FDATE between " + "\'" + startTime+" 00:00:00.0" + "\'" + "and" + "\'" + endtime+" 00:00:00.0" + "\'";
        }
        if (!"".equals(searchWldw)){
            con+=" and FSUPPLY_ID='"+searchWldw+"'";
        }
        if (!"".equals(edCode.getText().toString())){
            con+=" and FBILL_NO='"+edCode.getText().toString()+"'";
        }
        String SQL = "SELECT * FROM PUSH_DOWN_MAIN WHERE 1=1 "+con+" and TAG="+tag;
        Lg.e("SQL:"+SQL);
        Cursor cursor = GreenDaoManager.getmInstance(mContext).getDaoSession().getDatabase().rawQuery(SQL, null);
        while (cursor.moveToNext()) {
            PushDownMain f = new PushDownMain();
            f.FBillNo = cursor.getString(cursor.getColumnIndex("FBILL_NO"));
            f.FName = cursor.getString(cursor.getColumnIndex("FNAME"));
            f.FDate = cursor.getString(cursor.getColumnIndex("FDATE"));
            f.FSupplyID = cursor.getString(cursor.getColumnIndex("FSUPPLY_ID"));
            f.FInterID = cursor.getString(cursor.getColumnIndex("FINTER_ID"));
            f.FManagerID = cursor.getString(cursor.getColumnIndex("FMANAGER_ID"));
            f.FEmpID = cursor.getString(cursor.getColumnIndex("FEMP_ID"));
            f.FDeptID = cursor.getString(cursor.getColumnIndex("FDEPT_ID"));
            f.tag = cursor.getInt(cursor.getColumnIndex("TAG"));
            container.add(f);
        }
        if (container.size()==0){
            Toast.showText(mContext,"未查询到数据");
        }else{
            for (int i = 0; i < container.size(); i++) {
                isCheck.add(false);
            }
        }
        pushDownListAdapter.notifyDataSetChanged();


    }


}
