package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.PrintHistoryAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter1;
import com.fangzuo.assist.Adapter.TableAdapter4;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.PrintHistory;
import com.fangzuo.assist.Dao.PushDownSub;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.Dao.Unit;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.PrintHistoryDao;
import com.fangzuo.greendao.gen.PushDownSubDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.fangzuo.greendao.gen.UnitDao;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


//回單信息
public class Table3Activity extends BaseActivity implements TableAdapter4.InnerClickListener {


    @BindView(R.id.btn_back)
    RelativeLayout btnBack;
    @BindView(R.id.btn_delete)
    RelativeLayout btnDelete;
    @BindView(R.id.lv_result)
    ListView lvResult;
    @BindView(R.id.productcategory)
    TextView productcategory;
    @BindView(R.id.productnum)
    TextView productnum;
    private DaoSession daoSession;
    private T_mainDao t_mainDao;
    private T_DetailDao t_detailDao;
    private int activity;
    private ArrayList<Boolean> isCheck;
    private List<T_Detail> list;
    private List<T_main> list1;
    private TableAdapter4 tableAdapter;

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg) {

            case EventBusInfoCode.Print_Out://打印
                List<PrintHistory> data = (List<PrintHistory>) event.postEvent;
                try {
                    CommonUtil.doPrint(mContext,data,"1");
                } catch (Exception e) {
//                    e.printStackTrace();
                    LoadingUtil.showAlter(mContext,"打印错误","请检查打印机");

                }
                break;
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_table3);
        ButterKnife.bind(this);
        mContext = this;
    }

    @Override
    protected void initData() {
        daoSession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        t_mainDao = daoSession.getT_mainDao();
        t_detailDao = daoSession.getT_DetailDao();
        Intent in = getIntent();
        Bundle extras = in.getExtras();
        //获得跳转信息
        activity = extras.getInt("activity");
        Log.e(TAG, "获得跳转信息activity" + activity);

        initList();

    }

    private void initList() {
        isCheck = new ArrayList<>();
        list = t_detailDao.queryBuilder().where(
                T_DetailDao.Properties.Activity.eq(activity)
        ).build().list();
        Lg.e( "获得T_Detail：" , list);
        list1 = t_mainDao.queryBuilder().where(
                T_mainDao.Properties.Activity.eq(activity)
        ).build().list();
        Lg.e("获得T_main：" ,list1);
//        Lg.e("list.size", list.size() + "");
//        Lg.e("list1.size", list1.size() + "");
        // TODO: 2018/4/10 两个size都要验证大于0？
        if (list.size() > 0 && list1.size() >= 0) {
            for (int i = 0; i < list.size(); i++) {
                isCheck.add(false);
            }

        }
        double num = 0;
        tableAdapter = new TableAdapter4(mContext, list, isCheck);
        lvResult.setAdapter(tableAdapter);
        tableAdapter.setInnerListener(this);
        tableAdapter.notifyDataSetChanged();

        List<String> products = new ArrayList<>();
        products.clear();
        if (list.size() > 0) {
            if (products.size() == 0) {
                products.add(list.get(0).FProductId);
            }
            for (int i = 0; i < list.size(); i++) {
                if (!products.contains(list.get(i).FProductId)) {
                    products.add(list.get(i).FProductId);
                }
            }

            for (int i = 0; i < list.size(); i++) {
                num += Double.parseDouble(list.get(i).FQuantity);
            }
            productcategory.setText("物料类别数:" + products.size() + "个");
            productnum.setText("物料总数为:" + num + "");


        } else {
            productcategory.setText("物料类别数:" + 0 + "个");
            productnum.setText("物料总数为:" + 0 + "");
        }
        if(list1.size()>0){
            for(int i = 0;i<list1.size();i++){
                List<T_Detail> details = t_detailDao.queryBuilder().where(T_DetailDao.Properties.FOrderId.eq(list1.get(i).orderId)).build().list();
                if(details==null||details.size()==0){
                    t_mainDao.deleteInTx(list1.get(i));
                }
            }
        }

    }

    @Override
    protected void initListener() {
        lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isCheck.get(i)) {
                    isCheck.set(i, false);
                } else {
                    isCheck.set(i, true);
                }

                Log.e("ischeck", isCheck.get(i) + "");
                tableAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void OnReceive(String code) {

    }

    private PurchaseInStoreUploadBean pBean;
    private PurchaseInStoreUploadBean.purchaseInStore listBean;
    private ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data;
    private ArrayList<T_Detail> t_detailList;
    private ArrayList<T_main> t_mainsList;
    @OnClick({R.id.btn_back, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_delete:
                AlertDialog.Builder ab1 = new AlertDialog.Builder(mContext);
                ab1.setTitle("确认");
                ab1.setMessage("确认删除么?");
                ab1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LoadingUtil.showDialog(mContext,"正在删除...");
                        t_detailList = new ArrayList<>();
                        t_mainsList = new ArrayList<>();
                        pBean = new PurchaseInStoreUploadBean();
                        listBean=pBean.new purchaseInStore();
                        data = new ArrayList<>();
                        for (int j = 0; j < isCheck.size(); j++) {
                            if (isCheck.get(j)) {
                                Log.e(i + "", isCheck.get(j) + "");
                                final T_Detail t_detail = t_detailDao.queryBuilder().where(
                                        T_DetailDao.Properties.FIndex.eq(list.get(j).FIndex)
                                ).build().unique();
                                t_detailList.add(t_detail);
//                                final T_main t_main = t_mainDao.queryBuilder().where(
//                                        T_mainDao.Properties.FIndex.eq(list.get(j).FIndex)
//                                ).build().unique();
//                                t_mainsList.add(t_main);
                                Lg.e("获取到T_Detail:",t_detail);
                            }
                        }
                        String detail ="";
                        listBean.main="";
                        ArrayList<String> detailContainer = new ArrayList<>();
                        for (int j = 0; j < t_detailList.size(); j++) {
                            if (j != 0 && j % 49 == 0) {
                                Log.e("j%49", j % 49 + "");
                                T_Detail t_detail = t_detailList.get(j);
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
                                T_Detail t_detail = t_detailList.get(j);
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
                        Lg.e("detail", detail);
                        detailContainer.add(detail);
                        listBean.detail = detailContainer;
                        data.add(listBean);
                        pBean.list=data;
                        Gson gson = new Gson();
                        App.getRService().getCodeCheckDelete(gson.toJson(pBean), new MySubscribe<CommonResponse>() {
                            @Override
                            public void onNext(CommonResponse commonResponse) {
                                for (T_Detail t_detail:t_detailList) {
                                    PushDownSubDao pushDownSubDao = daoSession.getPushDownSubDao();
                                    List<PushDownSub> pushDownSubs = pushDownSubDao.queryBuilder().where(
                                            PushDownSubDao.Properties.Tag.eq(t_detail.tag),
                                            PushDownSubDao.Properties.FInterID.eq(t_detail.FInterID),
                                            PushDownSubDao.Properties.FItemID.eq(t_detail.FProductId),
                                            PushDownSubDao.Properties.FEntryID.eq(t_detail.FEntryID)
                                    ).build().list();
//                                    Lg.e(pushDownSubs.size()+"多少个");
                                    if (pushDownSubs.size() > 0) {
                                        //删除后，更新数据里面的已验收数
//                                        Lg.e( "获取到pushDownSubs:" + pushDownSubs);
                                        double result=(Double.valueOf(t_detail.FQuantity) * t_detail.unitrate) / getUnitrateSub(pushDownSubs.get(0));
//                                        Lg.e("result:"+result);
                                        pushDownSubs.get(0).FQtying = doubleSub(Double.valueOf(pushDownSubs.get(0).FQtying),result) +"";
//                                        Lg.e("unitrate:"+t_detail.unitrate+"  getUnitrateSub："+getUnitrateSub(pushDownSubs.get(0)));
                                        pushDownSubDao.update(pushDownSubs.get(0));
//                                        Lg.e("QTY:"+pushDownSubs.get(0).FQtying);
                                    }
                                }
                                t_detailDao.deleteInTx(t_detailList);
//                                t_mainDao.deleteInTx(t_mainsList);
                                Toast.showText(mContext, "删除成功");
                                initList();
                                tableAdapter.notifyDataSetChanged();
                                LoadingUtil.dismiss();
                            }

                            @Override
                            public void onError(Throwable e) {
                                LoadingUtil.dismiss();
                                Toast.showText(mContext, "删除失败："+e.toString());
                            }
                        });
//
//                        for (int j = 0; j < isCheck.size(); j++) {
//                            if (isCheck.get(j)) {
//                                Log.e(i + "", isCheck.get(j) + "");
//                                T_Detail t_detail = t_detailDao.queryBuilder().where(
//                                        T_DetailDao.Properties.FIndex.eq(list.get(j).FIndex)
//                                ).build().unique();
//                                Log.e(TAG, "获取到T_Detail:" + t_detail.toString());
////                                T_main t_main = t_mainDao.queryBuilder().where(
////                                        T_mainDao.Properties.FIndex.eq(list1.get(j).FIndex)
////                                ).build().unique();
//                                PushDownSubDao pushDownSubDao = daoSession.getPushDownSubDao();
//                                List<PushDownSub> pushDownSubs = pushDownSubDao.queryBuilder().where(
//                                        PushDownSubDao.Properties.FInterID.eq(t_detail.FInterID),
//                                        PushDownSubDao.Properties.FItemID.eq(t_detail.FProductId),
//                                        PushDownSubDao.Properties.FEntryID.eq(t_detail.FEntryID)
//                                ).build().list();
//                                Lg.e(pushDownSubs.size()+"多少个");
//                                if (pushDownSubs.size() > 0) {
//                                    //删除后，更新数据里面的已验收数
//                                    Log.e(TAG, "获取到pushDownSubs:" + pushDownSubs.toString());
//                                    double result=(Double.valueOf(t_detail.FQuantity) * t_detail.unitrate) / getUnitrateSub(pushDownSubs.get(0));
//                                    Lg.e("result:"+result);
//                                    pushDownSubs.get(0).FQtying = doubleSub(Double.valueOf(pushDownSubs.get(0).FQtying),result) +"";
//                                    Lg.e("unitrate:"+t_detail.unitrate+"  getUnitrateSub："+getUnitrateSub(pushDownSubs.get(0)));
//                                    pushDownSubDao.update(pushDownSubs.get(0));
//                                    Lg.e("QTY:"+pushDownSubs.get(0).FQtying);
//                                }
//                                t_detailDao.delete(t_detail);
////                                t_mainDao.delete(t_main);
//                                Toast.showText(mContext, "删除成功");
//                            }
//                        }
//                        initList();
//                        tableAdapter.notifyDataSetChanged();
                    }
                });
                ab1.setNegativeButton("取消", null);
                ab1.create().show();
                break;
        }
    }

    //解决 1.1-1.0=0.10000009的情况
    //double相减
    private Double doubleSub(Double v1,Double v2){
        BigDecimal b1=new BigDecimal(v1.toString());
        BigDecimal b2=new BigDecimal(v2.toString());
        return b1.subtract(b2).doubleValue();
    }

    private double getUnitrateSub(PushDownSub pushDownSub) {
        UnitDao unitDao = daoSession.getUnitDao();
        List<Unit> units = unitDao.queryBuilder().where(
                UnitDao.Properties.FMeasureUnitID.eq(pushDownSub.FUnitID)
        ).build().list();
        if (units.size() > 0) {
            return Double.valueOf(units.get(0).FCoefficient);
//            Lg.e("获得明细换算率：" + unitrateSub);
        } else {
           return  1;
//            Lg.e("获得明细换算率失败：" + unitrateSub);
        }
    }

    //ListView的点击事件
    @Override
    public void InOnClick(View v) {
        final int position = (int) v.getTag();
        switch (v.getId()) {
            //ListView中的删除按钮（已弃用隐藏）
            case R.id.delete:
                AlertDialog.Builder ab1 = new AlertDialog.Builder(mContext);
                ab1.setTitle("确认");
                ab1.setMessage("确认删除么?");
                ab1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        T_Detail t_detail = t_detailDao.queryBuilder().where(
                                T_DetailDao.Properties.FIndex.eq(list.get(position).FIndex)
                        ).build().unique();
                        T_main t_main = t_mainDao.queryBuilder().where(
                                T_mainDao.Properties.FIndex.eq(list1.get(position).FIndex)
                        ).build().unique();
                        PushDownSubDao pushDownSubDao = daoSession.getPushDownSubDao();
                        Log.e("entryid", t_detail + "");
                        Log.e("entryid", t_detail.FEntryID + "");
                        Log.e("FInterID", t_detail.FInterID + "");
                        List<PushDownSub> pushDownSubs = pushDownSubDao.queryBuilder().where(
                                PushDownSubDao.Properties.FInterID.eq(t_detail.FInterID),
                                PushDownSubDao.Properties.FEntryID.eq(t_detail.FEntryID)
                        ).build().list();

                        if (pushDownSubs.size() > 0) {
                            pushDownSubs.get(0).FQtying =
                                    (Double.parseDouble(pushDownSubs.get(0).FQtying)
                                            - (Double.parseDouble(t_detail.FQuantity) * t_detail.unitrate)/getUnitrateSub(pushDownSubs.get(0))) + "";
                            pushDownSubDao.update(pushDownSubs.get(0));
                        }
                        t_detailDao.delete(t_detail);
                        t_mainDao.delete(t_main);
                        Toast.showText(mContext, "删除成功");
                        initList();
                        tableAdapter.notifyDataSetChanged();
                    }
                });
                ab1.setNegativeButton("取消", null);
                ab1.create().show();
                break;
            //修改按钮
            case R.id.fix:
                AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                ab.setTitle("修改数据");
                View view = LayoutInflater.from(mContext).inflate(R.layout.fixalertitems, null);
                final EditText mEtNum = view.findViewById(R.id.ed_num);
//                final EditText mEtPrice = view.findViewById(R.id.ed_price);
//                mEtPrice.setVisibility(View.GONE);
                mEtNum.setText(list.get(position).FQuantity);
//                mEtPrice.setText(list.get(position).FTaxUnitPrice);
                ab.setView(view);
                ab.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        T_Detail t_detail = t_detailDao.queryBuilder().where(
                                T_DetailDao.Properties.FIndex.eq(list.get(position).FIndex)
                        ).build().unique();
                        String old = t_detail.FQuantity;
                        String newnum = mEtNum.getText().toString();
                        double getnum = Double.parseDouble(newnum) - Double.parseDouble(old);
                        Log.e("相减后得到：", getnum + "");
                        PushDownSubDao pushDownSubDao = daoSession.getPushDownSubDao();
                        List<PushDownSub> pushDownSubs = pushDownSubDao.queryBuilder().where(
                                PushDownSubDao.Properties.FInterID.eq(t_detail.FInterID),
                                PushDownSubDao.Properties.FEntryID.eq(t_detail.FEntryID)
                        ).build().list();
                        if (pushDownSubs.size() > 0) {
                            Double sum = 0.0;
                            sum = (Double.parseDouble(pushDownSubs.get(0).FQtying)
//                                            + Double.parseDouble(t_detail.FQuantity)
                                    + getnum
                            );
                            //检测修改的数量是否超过订单数量
                            if (sum > Double.parseDouble(pushDownSubs.get(0).FAuxQty)) {
                                Toast.showText(mContext, "修改数量超过订单数量，请重新输入");
                                return;
                            } else {
                                pushDownSubs.get(0).FQtying = sum + "";
                                Log.e(TAG, "?修改后的已盘点数：FQtying" + pushDownSubs.get(0).FQtying);
                                pushDownSubDao.update(pushDownSubs.get(0));
                                t_detail.FQuantity = newnum;
                                t_detailDao.update(t_detail);
                                initList();
                            }

                        }
//                        t_detail.FQuantity = newnum;
//                        t_detailDao.update(t_detail);
//                        initList();

                    }
                });
                ab.setNegativeButton("取消", null);
                ab.create().show();
                break;
        }
    }


}
