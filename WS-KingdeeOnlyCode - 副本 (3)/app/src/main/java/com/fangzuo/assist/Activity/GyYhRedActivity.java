package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.BackOrderProductListAdapter;
import com.fangzuo.assist.Adapter.CheckFirstInOutAdapter;
import com.fangzuo.assist.Adapter.FidNoAdapter;
import com.fangzuo.assist.Adapter.SendOrderBeanListAdapter;
import com.fangzuo.assist.Adapter.SendOrderProductListAdapter;
import com.fangzuo.assist.Adapter.SendOrderRedBeanListAdapter;
import com.fangzuo.assist.Beans.BackOrderQuery;
import com.fangzuo.assist.Beans.BackOrderResult;
import com.fangzuo.assist.Beans.CheckInOutBean;
import com.fangzuo.assist.Beans.CodeCheckBackDataBean;
import com.fangzuo.assist.Beans.CodeCheckBean;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Beans.SendOrderListBean;
import com.fangzuo.assist.Beans.SendOrderQuery;
import com.fangzuo.assist.Beans.SendOrderResult;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.RxSerivce.ToSubscribe;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.DataModel;
import com.fangzuo.assist.Utils.DoubleUtil;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MD5;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.MediaPlayer;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.greendao.gen.SendOrderListBeanDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GyYhRedActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.lv_pushsub)
    ListView lvPushsub;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.tv_name)
    AppCompatTextView tvName;
    @BindView(R.id.tv_number)
    AppCompatTextView tvNumber;
    @BindView(R.id.tv_model)
    TextView tvModel;
    @BindView(R.id.ed_num)
    EditText edNum;
    @BindView(R.id.btn_backorder)
    Button btnBackorder;
    @BindView(R.id.cb_isAuto)
    CheckBox cbIsAuto;
    @BindView(R.id.mDrawer)         //总布局
            DrawerLayout mDrawer;

    private int activity = Config.GyYhRedActivity;
    BackOrderQuery bean;
//    BackOrderProductListAdapter adapter;
    SendOrderRedBeanListAdapter adapter;
    private String code4Number="";
    private String default_waveHouseID;
    private String default_storageID;
    private SendOrderListBean pushDownSub;
    private long ordercode;
    private CodeCheckBackDataBean codeCheckBackDataBean;

    private SendOrderListBeanDao sendOrderListBeanDao;
    private ArrayList<String> orderDone;
    private ArrayList<String> orderDoing;

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg) {
//            case EventBusInfoCode.Upload_OK://回单成功
//                t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(
//                        T_DetailDao.Properties.Activity.eq(activity)
//                ).build().list());
//                t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(
//                        T_mainDao.Properties.Activity.eq(activity)
//                ).build().list());
//                for (int i = 0; i < fidc.size(); i++) {
//                    pushDownSubDao.deleteInTx(pushDownSubDao.queryBuilder().where(
//                            PushDownSubDao.Properties.FInterID.eq(fidc.get(i))).build().list());
//                    pushDownMainDao.deleteInTx(pushDownMainDao.queryBuilder().where(
//                            PushDownMainDao.Properties.FInterID.eq(fidc.get(i))).build().list());
//                }
//                btnBackorder.setClickable(true);
//                LoadingUtil.dismiss();
//                Toast.showText(mContext, "上传成功");
//                MediaPlayer.getInstance(mContext).ok();
//                Bundle b = new Bundle();
//                b.putInt("123", tag);
//                startNewActivity(PushDownPagerActivity.class, 0, 0, true, b);
//                break;
//            case EventBusInfoCode.Upload_Error://回单失败
//                String error = (String) event.postEvent;
//                Toast.showText(mContext, error);
//                btnBackorder.setClickable(true);
//                LoadingUtil.dismiss();
//                MediaPlayer.getInstance(mContext).error();
//                break;
            case EventBusInfoCode.CodeCheck_OK://批号检测结果
                codeCheckBackDataBean = (CodeCheckBackDataBean)event.postEvent;
                Lg.e("条码检测返回：",codeCheckBackDataBean);
//                if (dBean.inOutBeans.size() > 0) {
//                    if (dBean.inOutBeans.get(0).FTip.equals("OK")) {
                        code4Number = codeCheckBackDataBean.FNumber;
                        edNum.setText(codeCheckBackDataBean.FQty);
//                        default_storageID = codeCheckBackDataBean.FStockID;
//                        default_waveHouseID = codeCheckBackDataBean.FStockPlaceID;
                        setProduct(code4Number);
//                    } else {
//                        lockScan(0);
//                        Toast.showText(mContext, dBean.inOutBeans.get(0).FTip);
//                    }
//                } else {
//                    lockScan(0);
//                    Toast.showText(mContext, "无法找到条码验证数据");
//                }
                break;
            case EventBusInfoCode.CodeCheck_Error://检测条码失败
                LoadingUtil.dismiss();
                lockScan(0);
                canClick =false;
                codeCheckBackDataBean = (CodeCheckBackDataBean)event.postEvent;
                Toast.showText(mContext, codeCheckBackDataBean.FTip);
                MediaPlayer.getInstance(mContext).error();
                break;
            case EventBusInfoCode.Insert_result://写入临时表结果
                DownloadReturnBean dBean3 = (DownloadReturnBean) event.postEvent;
                if (dBean3.inOutBeans.get(0).FTip.equals("OK")) {
                    Addorder();
                } else {
                    lockScan(0);
                    Toast.showText(mContext, dBean3.inOutBeans.get(0).FTip);
                }
                break;
            case EventBusInfoCode.Check_Search_NO://检测物流单号
                DownloadReturnBean dBean4 = (DownloadReturnBean) event.postEvent;
                LoadingUtil.dismiss();
                if (dBean4.inOutBeans.get(0).FTip.equals("OK")) {
                    if (null== barcodeStr || "".equals(barcodeStr)){
                        barcodeStr = edCode.getText().toString();
                    }
                    searchNo(barcodeStr,"0");
                } else {
                    lockScan(0);
                    LoadingUtil.dismiss();
                    Toast.showText(mContext, dBean4.inOutBeans.get(0).FTip);
                }
                break;
            case EventBusInfoCode.Upload_OK://回单成功
                //删除已经做完的单据信息
                for (int i = 0; i < orderDone.size(); i++) {
                    sendOrderListBeanDao.deleteInTx(sendOrderListBeanDao.queryBuilder().where(
                            SendOrderListBeanDao.Properties.FWlNo.eq(orderDone.get(i))
                    ).build().list());
                    t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(
                            T_DetailDao.Properties.Activity.eq(activity),
                            T_DetailDao.Properties.FRate.eq(orderDone.get(i))
                    ).build().list());
                }
//                t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(
//                        T_mainDao.Properties.Activity.eq(activity)
//                ).build().list());
//                sendOrderListBeanDao.deleteInTx(sendOrderListBeanDao.queryBuilder().where(
//                        SendOrderListBeanDao.Properties.FActivity.eq(activity)
//                ).build().list());
                adapter.clear();
                btnBackorder.setClickable(true);
                LoadingUtil.dismiss();
                MediaPlayer.getInstance(mContext).ok();

                break;
            case EventBusInfoCode.Upload_Error://回单失败
                String error = (String) event.postEvent;
                Toast.showText(mContext, error);
                btnBackorder.setClickable(true);
                LoadingUtil.dismiss();
                MediaPlayer.getInstance(mContext).error();
                break;
        }
    }
    @Override
    protected void initView() {
        setContentView(R.layout.activity_gy_yh_red);
        ButterKnife.bind(this);
        initDrawer(mDrawer);
        tvTitle.setText("电商出库红字");
        tvRight.setText("菜单");
    }

    @Override
    protected void initData() {
        setfocus(tvModel);
        cbIsAuto.setChecked(Hawk.get(Config.autoAdd+activity,true));
        sendOrderListBeanDao = daoSession.getSendOrderListBeanDao();
    }

    private void checkBeforeSearchNo(String no){
//        searchNo(no);
        LoadingUtil.showDialog(mContext,"正在检测物流单号...");
        DataModel.CheckCode(WebApi.CheckSearchNo, gson.toJson(new CheckInOutBean(no, "", true)), EventBusInfoCode.Check_Search_NO);
    }
    private String wlString="";
    private String apiSendNo="";//发货单号
    private String apiPTNo="";//平台单号
    private String apiClient="";//店铺(客户)
    private String apiStorageCode="";//退入仓库代码
    //根据物流单号查找商品数据
    private void searchNo(String no, final String type) {
        ordercode = CommonUtil.createOrderCode(this,no);
        if ("".equals(no)){
            Toast.showText(mContext,"请输入物流单号");
            return;
        }
        wlString=no;
        edCode.setText(wlString);
        bean = new BackOrderQuery();
//        bean.setBase(no,"gy.erp.trade.return.get");
        if ("1".equals(type)){//查询发货单号
            bean.setBase(no,"gy.erp.trade.return.get");
        }else{
            bean.setBase2(no,"gy.erp.trade.return.get");
        }
        String getSignString = Info.Secret + gson.toJson(bean) + Info.Secret;
        bean.setSign(MD5.getMD5to32Big(getSignString));
//        String json = new Gson().toJson(bean);
//        String json = JsonDealUtils.getStoreQuery(bean);
        Lg.e("请求数据："+gson.toJson(bean));
        LoadingUtil.showDialog(mContext, "正在获取数据...");
        App.CloudService().getBackOrderQuery(bean, new ToSubscribe<BackOrderResult>() {
            @Override
            public void onNext(BackOrderResult s) {
                super.onNext(s);
                if (s.isSuccess()) {
                    LoadingUtil.dismiss();
//                    Lg.e("得到数据：" + s.getOrders().size());
                    List<BackOrderResult.TradeReturnsBean.DetailsBean> items = new ArrayList<>();
                    for (BackOrderResult.TradeReturnsBean bean:s.getTradeReturns()) {
                        apiClient = bean.getShop_name();
                        apiPTNo = bean.getPlatform_code();
                        apiSendNo = bean.getCode();
                        apiStorageCode = bean.getWarehousein_code();
                        for (BackOrderResult.TradeReturnsBean.DetailsBean detailsBean:bean.getDetails()) {
                            items.add(detailsBean);
                        }
                    }
                    if (items.size()>0){
                        String json = DataModel.dealBackOrderDetail(items,wlString,apiStorageCode,activity+"");
                        App.getRService().doIOAction(WebApi.getSendOrderRedList, json, new MySubscribe<CommonResponse>() {
                            @Override
                            public void onNext(CommonResponse commonResponse) {
                                super.onNext(commonResponse);if (!commonResponse.state)return;
                                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                                if (null!=dBean && dBean.sendOrderListBeans.size()>0){
                                    if (dBean.sendOrderListBeans.get(0).FTip.equals("OK")){
                                        List<SendOrderListBean> listBeans = sendOrderListBeanDao.queryBuilder().where(
                                                SendOrderListBeanDao.Properties.FWlNo.eq(dBean.sendOrderListBeans.get(0).FWlNo)).build().list();
                                        if (listBeans.size()<=0){
                                            sendOrderListBeanDao.saveInTx(dBean.sendOrderListBeans);
                                            adapter = new SendOrderRedBeanListAdapter(mContext, dBean.sendOrderListBeans);
                                        }else{
                                            adapter = new SendOrderRedBeanListAdapter(mContext, listBeans);
                                        }
                                        lvPushsub.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                        setfocus(edNum);
                                    }else{
                                        Toast.showText(mContext,dBean.sendOrderListBeans.get(0).FTip);
                                    }
                                }else{
                                    Toast.showText(mContext,"无法找到该单据相应信息");
                                    setfocus(edCode);
                                }
                                barcodeStr="";
                                lockScan(0);
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                LoadingUtil.dismiss();
                                lockScan(0);
                            }
                        });

//                        adapter = new BackOrderProductListAdapter(mContext, items);
//                        lvPushsub.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
//                        setfocus(edNum);
                    }else{
                        if ("1".equals(type)){
                            Toast.showText(mContext,"无法找到该单据信息");
                            lockScan(0);
                            setfocus(edCode);
                            barcodeStr="";
                        }else{
                            searchNo(wlString,"1");
                        }
                    }
                    lockScan(0);
                    LoadingUtil.dismiss();

                } else {
                    lockScan(0);
                    LoadingUtil.dismiss();
                    LoadingUtil.showAlter(mContext, "查询错误:" + s.getErrorDesc() + s.getSubErrorDesc());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LoadingUtil.dismiss();
                lockScan(0);
                LoadingUtil.showAlter(mContext, "请求失败:" + e.getMessage() + e.toString());
            }
        });
//        Asynchttp
//        App.getRService().doIOAction("gy.erp.trade.get", new Gson().toJson(bean),new ToSubscribe<String>(){
//
//        });
    }

    @Override
    protected void initListener() {
        cbIsAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Hawk.put(Config.autoAdd+activity,b);
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(Gravity.RIGHT);
            }
        });
        btnBackorder.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if (DataModel.checkHasDetail(mContext, activity)) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                    ab.setTitle("是否回单");
                    ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            btnBackorder.setClickable(false);
                            LoadingUtil.show(mContext, "正在回单...");
                            upload();
                        }
                    });
                    ab.setNegativeButton("取消",null);
                    ab.create().show();
                } else {
                    Toast.showText(mContext, "无单据信息");
                }
            }
        });

        lvPushsub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (canClick){
                    canClick = false;
                }else{
                    return;
                }
                pushDownSub = (SendOrderListBean) adapter.getItem(i);
                tvName.setText(pushDownSub.getFName());
                tvNumber.setText(pushDownSub.getFNumber());
                tvModel.setText(pushDownSub.getFModel());
                default_storageID=pushDownSub.getFStorageID();
                default_waveHouseID = pushDownSub.getFWaveHouseID();
                if (cbIsAuto.isChecked()){
                    AddorderBefore();
                }else{
                    lockScan(0);
                }
            }
        });
    }
    //扫码后设置product数据
    private void setProduct(String number) {
        if (number != null) {
            boolean flag = true;
//            boolean hasUnit = false;
            String qtying=DataModel.getDetailNum(mContext,number,wlString);
            for (int j = 0; j < adapter.getCount(); j++) {
                SendOrderListBean pushDownSub1 = (SendOrderListBean) adapter.getItem(j);
                if (number.equals(pushDownSub1.getFNumber())) {
                    if (MathUtil.toD(pushDownSub1.getFQty()+"") == MathUtil.toD(qtying)) {
                        flag = true;
                        continue;
                    } else {
//                        for (int i = 0; i < pushDownSubListAdapter.getCount(); i++) {
//                            PushDownSub pushDownSub = (PushDownSub) pushDownSubListAdapter.getItem(i);
//                        if (!"".equals(default_unitID)) {
//                            if (default_unitID.equals(pushDownSub1.FUnitID)) {
//                                hasUnit = true;
                        flag = false;
                        lvPushsub.setSelection(j);
                        lvPushsub.performItemClick(lvPushsub.getChildAt(j), j, lvPushsub.getItemIdAtPosition(j));
                        break;
//                            }
//                        } else {
//                            hasUnit = true;
//                            flag = false;
//                            lvPushsub.setSelection(j);
//                            lvPushsub.performItemClick(lvPushsub.getChildAt(j), j, lvPushsub.getItemIdAtPosition(j));
//                            break;
//                        }

                    }
                }
            }

            if (flag) {
                canClick =false;
                Toast.showText(mContext, "商品不存在");
                MediaPlayer.getInstance(mContext).error();
                lockScan(0);
                setfocus(edNum);
                tvName.setText("");
                tvNumber.setText("");
                tvModel.setText("");
            }
        } else {
            lockScan(0);
            canClick =false;
            Toast.showText(mContext, "返回的条码信息为空");
        }
    }

    @Override
    protected void OnReceive(String code) {
        if (null!=adapter && adapter.getCount()>0){
            if (edCode.hasFocus()){
                checkBeforeSearchNo(code);
            }else{
                //查询条码唯一表
                CodeCheckBean bean = new CodeCheckBean(code);
                DataModel.codeCheck(WebApi.CodeCheckForOutForRed,gson.toJson(bean));
                canClick =true;
            }
        }else{
            checkBeforeSearchNo(code);
        }
    }
    private void AddorderBefore() {
        if (edNum.getText().toString().equals("")) {
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "请输入数量");
            lockScan(0);
            return;
        }

//        if (Hawk.get(Info.FirstInOut,"1").equals("1")){
//            CheckInOutBean bean = new CheckInOutBean(barcodeStr);
//            App.getRService().doIOAction(WebApi.CheckFirstInOut,gson.toJson(bean), new MySubscribe<CommonResponse>() {
//                @Override
//                public void onNext(CommonResponse commonResponse) {
//                    DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
//                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Check_First_InOut,dBean));
//                }
//                @Override
//                public void onError(Throwable e) {
//                    lockScan(0);
//                    Toast.showText(mContext,"查找先进先出失败."+e.getMessage());
//                }
//            });
//        }else{
            //添加进临时表
            DataModel.InsertForInOutY(WebApi.InsertForOnlyCodeOutRed, gson.toJson(new CheckInOutBean("",
                    ordercode + "", barcodeStr, edNum.getText().toString(), default_storageID, default_waveHouseID,
                    BasicShareUtil.getInstance(mContext).getIMIE())));
//        }

    }
    //添加
    private void Addorder() {
        try {
//            batchNo = edBatchNo.getText().toString();
//            String discount = "";
            //数量
            String num = edNum.getText().toString();
//                boolean isHebing = true;
//                if (isHebing) {
//                    List<T_Detail> detailhebing = t_detailDao.queryBuilder().where(
//                            T_DetailDao.Properties.Activity.eq(activity),
//                            T_DetailDao.Properties.FInterID.eq(fid),
//                            T_DetailDao.Properties.FUnitId.eq(unitId),
//                            T_DetailDao.Properties.FProductId.eq(product.FItemID),
//                            T_DetailDao.Properties.FStorageId.eq(storageID == null ? "0" : storageID),
//                            T_DetailDao.Properties.FPositionId.eq(spWavehouse.getWaveHouseId()),
//                            T_DetailDao.Properties.FEntryID.eq(fentryid),
//                            T_DetailDao.Properties.FBatch.eq(batchNo == null ? "" : batchNo)
//                    ).build().list();
//                    if (detailhebing.size() > 0) {
//                        for (int i = 0; i < detailhebing.size(); i++) {
//                            num = (MathUtil.toD(num) + MathUtil.toD(detailhebing.get(i).FQuantity)) + "";
//                            t_detailDao.delete(detailhebing.get(i));
//                        }
//                    }
//                }

            String second = getTimesecond();
            T_Detail t_detail = new T_Detail();
//            t_detail.FBatch = batchNo == null ? "" : batchNo;
//            if (!onlyClick){
//                t_detail.FBarcode = barcodeStr.length()==24?barcodeStr:productCode+edSn.getText().toString();
//            }else if (barcodeStr.length()==24){
            t_detail.FBarcode = barcodeStr;
            t_detail.FRate = wlString;
            t_detail.FApiClient = apiClient==null?"":apiClient;
            t_detail.FApiSendNo = apiSendNo==null?"":apiSendNo;
            t_detail.FApiPTNo = apiPTNo==null?"":apiPTNo;
//            }
            t_detail.FOrderId = ordercode;
//            t_detail.FProductId = product.FItemID;
            t_detail.FProductName = pushDownSub.getFName();
            t_detail.FProductCode = pushDownSub.getFNumber();
            t_detail.model = pushDownSub.getFModel();
            t_detail.FIndex = second;
            t_detail.activity = activity;
            t_detail.FDiscount = ShareUtil.getInstance(mContext).getsetUserID();
            t_detail.FQuantity = num;
            t_detail.IMIE = BasicShareUtil.getInstance(mContext).getIMIE();
            long insert = t_detailDao.insert(t_detail);
            Log.e(TAG, "添加条数：" + insert);
            Log.e(TAG, "添加了数据：" + t_detail.toString());

            if (insert > 0) {
                //更新订单详情的已验收数量
                pushDownSub.FQtying = DoubleUtil.sum(MathUtil.toD(pushDownSub.FQtying),
                        MathUtil.toD(edNum.getText().toString())) + "";
                sendOrderListBeanDao.update(pushDownSub);
                Toast.showText(mContext, "添加成功");
                MediaPlayer.getInstance(mContext).ok();
//                if (barcodeStr.length()==24){
//                    product = null;
//                    productName.setText("");
//                    edBatchNo.setText("");
//                    barcodeStr="";
//                }else{
//
//                }
                clearUi();
            } else {
                Toast.showText(mContext, "添加失败，请重试");
//                    MediaPlayer.getInstance(mContext).error();
            }
        } catch (Exception e) {
//            DataService.pushError(mContext, this.getClass().getSimpleName(), e);
        }
    }

    private void clearUi(){
        lockScan(0);
        tvName.setText("");
        tvNumber.setText("");
        tvModel.setText("");
        edNum.setText("");
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null!=adapter && adapter.getCount()>0)adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.btn_back, R.id.tv_right, R.id.btn_search, R.id.btn_add, R.id.btn_backorder, R.id.btn_checkorder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_right:
                break;
            case R.id.btn_search:
                checkBeforeSearchNo(edCode.getText().toString());
                break;
            case R.id.btn_add:
                AddorderBefore();
                break;
            case R.id.btn_backorder:
                break;
            case R.id.btn_checkorder:
                Bundle b = new Bundle();
                b.putInt("activity", activity);
                startNewActivity(ReViewForOnlyCodeActivity.class, 0, 0, false, b);
                break;
        }
    }

    PurchaseInStoreUploadBean pBean;
    private void upload() {
        orderDone = new ArrayList<>();
        orderDoing = new ArrayList<>();
        pBean = new PurchaseInStoreUploadBean();
        PurchaseInStoreUploadBean.purchaseInStore puBean = pBean.new purchaseInStore();
        t_mainDao = daoSession.getT_mainDao();
        t_detailDao = daoSession.getT_DetailDao();
        ArrayList<String> detailContainer = new ArrayList<>();
        ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data = new ArrayList<>();
        List<SendOrderListBean> listBeans = DataModel.getSendOrderQty(mContext,activity+"");
        Lg.e("过滤Send",listBeans);
        for (int i = 0; i < listBeans.size(); i++) {
            //过滤出为完成的单据
            if (MathUtil.toD(listBeans.get(i).FQty)==MathUtil.toD(listBeans.get(i).FQtying)){
                orderDone.add(listBeans.get(i).FWlNo);
            }else{
                orderDoing.add(listBeans.get(i).FWlNo);
            }
        }
        Lg.e("未完成",orderDoing);
        Lg.e("已完成",orderDone);
        for (int i = 0; i < orderDone.size(); i++) {
            detailContainer = new ArrayList<>();
            puBean = pBean.new purchaseInStore();
            String detail = "";
            puBean.main = "";
            List<T_Detail> details = DataModel.getDetailUpload(mContext,activity+"",orderDone.get(i));
            for (int j = 0; j < details.size(); j++) {
                if (j != 0 && j % 49 == 0) {
                    Log.e("j%49", j % 49 + "");
                    T_Detail t_detail = details.get(j);
                    detail = detail +
                            t_detail.FDiscount + "|" +
                            t_detail.FRate + "|" +
                            t_detail.FOrderId + "|" +
                            t_detail.IMIE + "|" +
                            t_detail.FApiSendNo + "|" +
                            t_detail.FApiPTNo + "|" +
                            t_detail.FApiClient + "|";
                    detail = detail.subSequence(0, detail.length() - 1).toString();
                    detailContainer.add(detail);
                    detail = "";
                } else {
                    Log.e("j", j + "");
                    Log.e("details.size()", details.size() + "");
                    T_Detail t_detail = details.get(j);
                    detail = detail +
                            t_detail.FDiscount + "|" +
                            t_detail.FRate + "|" +
                            t_detail.FOrderId + "|" +
                            t_detail.IMIE + "|" +
                            t_detail.FApiSendNo + "|" +
                            t_detail.FApiPTNo + "|" +
                            t_detail.FApiClient + "|";
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
        pBean.list = data;
        if (orderDoing.size()>0){
            btnBackorder.setClickable(true);
            LoadingUtil.dismiss();
            AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
            ab.setTitle("以下单据未完成是否继续回单");
            View v = LayoutInflater.from(mContext).inflate(R.layout.pd_alert, null);
            ListView lv = v.findViewById(R.id.lv_alert);
            FidNoAdapter fidNoAdapter = new FidNoAdapter(mContext, orderDoing);
            lv.setAdapter(fidNoAdapter);
            ab.setView(v);
            ab.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (pBean.list.size()<=0){
                        btnBackorder.setClickable(true);
                        Toast.showText(mContext,"本地无已完成的单据");
                    }else{
                        LoadingUtil.show(mContext, "正在回单...");
                        DataModel.upload(WebApi.GyRedUpload, gson.toJson(pBean));
                    }
                }
            });
            ab.setNegativeButton("否", null);
            ab.create().show();
        }else{
            DataModel.upload(WebApi.GyRedUpload, gson.toJson(pBean));
        }
    }
}