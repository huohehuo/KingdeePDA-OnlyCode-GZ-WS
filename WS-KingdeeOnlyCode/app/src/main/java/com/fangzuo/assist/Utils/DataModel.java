package com.fangzuo.assist.Utils;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.BackOrderResult;
import com.fangzuo.assist.Beans.CodeCheckBackDataBean;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Beans.SendOrderListBean;
import com.fangzuo.assist.Beans.SendOrderResult;
import com.fangzuo.assist.Dao.PushDownMain;
import com.fangzuo.assist.Dao.PushDownSub;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;

public class DataModel {
    public static void codeCheck(String json){
        App.getRService().getCodeCheckData(json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (null != dBean && dBean.codeCheckBackDataBeans.size() > 0) {
                    if ("OK".equals(dBean.codeCheckBackDataBeans.get(0).FTip)) {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_OK,dBean.codeCheckBackDataBeans.get(0)));
                    } else {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,dBean.codeCheckBackDataBeans.get(0)));
                    }
                } else {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,new CodeCheckBackDataBean("找不到条码信息")));
                }
            }
            @Override
            public void onError(Throwable e) {
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,new CodeCheckBackDataBean("查找条码信息错误" + e.toString())));
            }
        });
    }
    public static void codeCheckForIn(String json){
        App.getRService().getCodeCheckDataForIn(json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (null != dBean && dBean.codeCheckBackDataBeans.size() > 0) {
                    if ("OK".equals(dBean.codeCheckBackDataBeans.get(0).FTip)) {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_OK,dBean.codeCheckBackDataBeans.get(0)));
                    } else {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,dBean.codeCheckBackDataBeans.get(0)));
                    }
                } else {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,new CodeCheckBackDataBean("找不到条码信息")));
                }
            }
            @Override
            public void onError(Throwable e) {
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,new CodeCheckBackDataBean("查找条码信息错误" + e.toString())));
            }
        });
    }

    public static void CheckBatch(String io,String json){
        App.getRService().doIOAction(io,json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Check_Batch_result,dBean));
            }
            @Override
            public void onError(Throwable e) {
                Toast.showText(App.getContext(),"查找条码信息错误："+e.toString());
            }
        });
    }


    public static void codeCheckForOut(String json){
        App.getRService().getCodeCheckDataForOut(json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (null != dBean && dBean.codeCheckBackDataBeans.size() > 0) {
                    if ("OK".equals(dBean.codeCheckBackDataBeans.get(0).FTip)) {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_OK,dBean.codeCheckBackDataBeans.get(0)));
                    } else {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,dBean.codeCheckBackDataBeans.get(0)));
                    }
                } else {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,new CodeCheckBackDataBean("找不到条码信息")));
                }
            }
            @Override
            public void onError(Throwable e) {
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,new CodeCheckBackDataBean("查找条码信息错误" + e.toString())));
            }
        });
    }
    public static void codeCheckForOut4PdSN(String json){
        App.getRService().doIOAction(WebApi.CodeCheckOut4PdSn,json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (null != dBean && dBean.codeCheckBackDataBeans.size() > 0) {
                    if (null!=dBean.codeCheckBackDataBeans.get(0).FTip && dBean.codeCheckBackDataBeans.get(0).FTip.contains("OK")) {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_OK,dBean.codeCheckBackDataBeans.get(0)));
                    } else {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,dBean.codeCheckBackDataBeans.get(0)));
                    }
                } else {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,new CodeCheckBackDataBean("找不到条码信息")));
                }
            }
            @Override
            public void onError(Throwable e) {
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,new CodeCheckBackDataBean("查找条码信息错误" + e.toString())));
            }
        });
    }

    //条码检测
    public static void codeCheck4DB(String io,String json){
        App.getRService().doIOAction(io,json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (null != dBean && dBean.codeCheckBackDataBeans.size() > 0) {
                    if ("OK".equals(dBean.codeCheckBackDataBeans.get(0).FTip)) {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_OK,dBean.codeCheckBackDataBeans.get(0)));
                    } else if ("OK2".equals(dBean.codeCheckBackDataBeans.get(0).FTip)){
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_OK,dBean.codeCheckBackDataBeans.get(0)));
                    }else{
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,dBean.codeCheckBackDataBeans.get(0)));
                    }
                } else {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,new CodeCheckBackDataBean("找不到条码信息")));
                }
            }
            @Override
            public void onError(Throwable e) {
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,new CodeCheckBackDataBean("查找条码信息错误" + e.toString())));
            }
        });
    }
    //条码检测
    public static void codeCheck(String io,String json){
        App.getRService().doIOAction(io,json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (null != dBean && dBean.codeCheckBackDataBeans.size() > 0) {
                    if ("OK".equals(dBean.codeCheckBackDataBeans.get(0).FTip)) {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_OK,dBean.codeCheckBackDataBeans.get(0)));
                    } else {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,dBean.codeCheckBackDataBeans.get(0)));
                    }
                } else {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,new CodeCheckBackDataBean("找不到条码信息")));
                }
            }
            @Override
            public void onError(Throwable e) {
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,new CodeCheckBackDataBean("查找条码信息错误" + e.toString())));
            }
        });
    }


    public static void codeCheckForOutForRed(String json){
        App.getRService().getCodeCheckDataForOutForRed(json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (null != dBean && dBean.codeCheckBackDataBeans.size() > 0) {
                    if ("OK".equals(dBean.codeCheckBackDataBeans.get(0).FTip)) {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_OK,dBean.codeCheckBackDataBeans.get(0)));
                    } else {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,dBean.codeCheckBackDataBeans.get(0)));
                    }
                } else {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,new CodeCheckBackDataBean("找不到条码信息")));
                }
            }
            @Override
            public void onError(Throwable e) {
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,new CodeCheckBackDataBean("查找条码信息错误" + e.toString())));
            }
        });
    }

    public static void codeCheckForPD(String json){
        App.getRService().getCodeCheckDataForPD(json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (null != dBean && dBean.codeCheckBackDataBeans.size() > 0) {
                    if ("OK".equals(dBean.codeCheckBackDataBeans.get(0).FTip)) {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_OK,dBean.codeCheckBackDataBeans.get(0)));
                    } else {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,dBean.codeCheckBackDataBeans.get(0)));
                    }
                } else {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,new CodeCheckBackDataBean("找不到条码信息")));
                }
            }
            @Override
            public void onError(Throwable e) {
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeCheck_Error,new CodeCheckBackDataBean("查找条码信息错误" + e.toString())));
            }
        });
    }


    public static void codeInsert(String json){
        App.getRService().getCodeCheckInsert(json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (null != dBean && dBean.codeCheckBackDataBeans.size() > 0) {
                    if ("OK".equals(dBean.codeCheckBackDataBeans.get(0).FTip)) {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_OK,dBean.codeCheckBackDataBeans.get(0)));
                    } else {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_Error,dBean.codeCheckBackDataBeans.get(0)));
                    }
                } else {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_Error,new CodeCheckBackDataBean("找不到条码信息")));
                }            }

            @Override
            public void onError(Throwable e) {
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_Error,new CodeCheckBackDataBean("查找条码信息错误" + e.toString())));
            }
        });
    }
    public static void codeInsertForIn(String json){
        App.getRService().getCodeCheckInsertForIn(json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (null != dBean && dBean.codeCheckBackDataBeans.size() > 0) {
                    if ("OK".equals(dBean.codeCheckBackDataBeans.get(0).FTip)) {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_OK,dBean.codeCheckBackDataBeans.get(0)));
                    } else {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_Error,dBean.codeCheckBackDataBeans.get(0)));
                    }
                } else {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_Error,new CodeCheckBackDataBean("找不到条码信息")));
                }            }

            @Override
            public void onError(Throwable e) {
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_Error,new CodeCheckBackDataBean("查找条码信息错误" + e.toString())));
            }
        });
    }
    //出库写入临时表
    public static void codeInsertForOut(String json){
        App.getRService().getCodeCheckInsertForOut(json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (null != dBean && dBean.codeCheckBackDataBeans.size() > 0) {
                    if ("OK".equals(dBean.codeCheckBackDataBeans.get(0).FTip)) {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_OK,dBean.codeCheckBackDataBeans.get(0)));
                    } else {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_Error,dBean.codeCheckBackDataBeans.get(0)));
                    }
                } else {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_Error,new CodeCheckBackDataBean("找不到条码信息")));
                }
            }

            @Override
            public void onError(Throwable e) {
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_Error,new CodeCheckBackDataBean("查找条码信息错误" + e.toString())));
            }
        });
    }
    //出库写入临时表
    public static void codeInsert(String io,String json){
        App.getRService().doIOAction(io,json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (null != dBean && dBean.codeCheckBackDataBeans.size() > 0) {
                    if ("OK".equals(dBean.codeCheckBackDataBeans.get(0).FTip)) {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_OK,dBean.codeCheckBackDataBeans.get(0)));
                    } else {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_Error,dBean.codeCheckBackDataBeans.get(0)));
                    }
                } else {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_Error,new CodeCheckBackDataBean("找不到条码信息")));
                }
            }

            @Override
            public void onError(Throwable e) {
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_Error,new CodeCheckBackDataBean("查找条码信息错误" + e.toString())));
            }
        });
    }


    public static void codeInsertForOutForRed(String json){
        App.getRService().getCodeCheckInsertForOutForRed(json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (null != dBean && dBean.codeCheckBackDataBeans.size() > 0) {
                    if ("OK".equals(dBean.codeCheckBackDataBeans.get(0).FTip)) {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_OK,dBean.codeCheckBackDataBeans.get(0)));
                    } else {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_Error,dBean.codeCheckBackDataBeans.get(0)));
                    }
                } else {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_Error,new CodeCheckBackDataBean("找不到条码信息")));
                }
            }

            @Override
            public void onError(Throwable e) {
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_Error,new CodeCheckBackDataBean("查找条码信息错误" + e.toString())));
            }
        });
    }
    //管易云条码检测
    public static void CheckCode(String io, String json, final String backCode){
        App.getRService().doIOAction(io,json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                EventBusUtil.sendEvent(new ClassEvent(backCode,dBean));
            }
            @Override
            public void onError(Throwable e) {
                Toast.showText(App.getContext(),"查找条码信息错误："+e.toString());
            }
        });
    }
    public static void InsertForInOutY(String io,String json){
        App.getRService().doIOAction(io,json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                if (!commonResponse.state)return;
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Insert_result,dBean));
            }
            @Override
            public void onError(Throwable e) {
                Toast.showText(App.getContext(),"执行错误："+e.toString());
            }
        });
    }


    public static void upload(Context context,String url,String json){
        Asynchttp.post(context, url, json, new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Upload_OK,""));
            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Upload_Error,Msg));
            }
        });
    }
    //统一回单数据请求
    public static void upload(String url,String json){
        App.getRService().doIOAction(url, json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                super.onNext(commonResponse);
                if (commonResponse.state){
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Upload_OK,""));
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Upload_Error,e.toString()));
            }
        });
    }

    public static List<T_Detail> mergeDetail4CSO(Context context,int Activity){
        List<T_Detail> list = new ArrayList<>();
        DaoSession daoSession = GreenDaoManager.getmInstance(context).getDaoSession();
        Cursor cursor = daoSession.getDatabase().rawQuery("SELECT " +
                "FRATE," +
                "FORDER_ID," +
                "FSTORAGE_ID," +
                "FPRODUCT_ID," +
                "FUNIT_ID," +
                "FTAX_UNIT_PRICE," +
                "FBATCH," +
                "FPOSITION_ID," +
                "FKFDATE," +
                "FKFPERIOD," +
                "FPRODUCT_NAME," +
                "FPRODUCT_CODE," +
                "FBARCODE," +
                "SUM(FQUANTITY) AS FQUANTITY " +
                "FROM T__DETAIL " +
                "WHERE " +
                "ACTIVITY = ? " +
                "GROUP BY " +
                "FORDER_ID," +
                "FPRODUCT_NAME," +
                "FPRODUCT_CODE,FKFPERIOD,FRATE,FKFDATE,FPOSITION_ID,FBATCH,FSTORAGE_ID,FPOSITION_ID", new String[]{ Activity+""});
        while (cursor.moveToNext()){
            T_Detail t_detail = new T_Detail();
            t_detail.FProductId = cursor.getString(cursor.getColumnIndex("FPRODUCT_ID"));
            t_detail.FUnitId = cursor.getString(cursor.getColumnIndex("FUNIT_ID"));
            t_detail.FTaxUnitPrice = cursor.getString(cursor.getColumnIndex("FTAX_UNIT_PRICE"));
            t_detail.FQuantity = cursor.getString(cursor.getColumnIndex("FQUANTITY"));
            t_detail.FStorageId = cursor.getString(cursor.getColumnIndex("FSTORAGE_ID"));
            t_detail.FPositionId = cursor.getString(cursor.getColumnIndex("FPOSITION_ID"));
            t_detail.FBatch = cursor.getString(cursor.getColumnIndex("FBATCH"));
            t_detail.FBarcode = cursor.getString(cursor.getColumnIndex("FBARCODE"));
            t_detail.FKFDate = cursor.getString(cursor.getColumnIndex("FKFDATE"));
            t_detail.FKFPeriod = cursor.getString(cursor.getColumnIndex("FKFPERIOD"));
            t_detail.FRate = cursor.getString(cursor.getColumnIndex("FRATE"));

            list.add(t_detail);
        }

        return list;
    }

    //统计
    public static List<T_Detail> mergeDetail(Context context,String orderID,int Activity){
        List<T_Detail> list = new ArrayList<>();
        DaoSession daoSession = GreenDaoManager.getmInstance(context).getDaoSession();
        Cursor cursor = daoSession.getDatabase().rawQuery("SELECT " +
                "FRATE," +
                "FORDER_ID," +
                "FPRODUCT_ID," +
                "FUNIT_ID," +
                "FTAX_UNIT_PRICE," +
                "FDISCOUNT," +
                "FSTORAGE_ID," +
                "FKFDATE," +
                "FKFPERIOD," +
                "IS_ASSEMBLE," +
                "FBATCH," +
                "FPOSITION_ID," +
                "FINTER_ID," +
                "FENTRY_ID," +
                "FBARCODE," +
                "SUM(FQUANTITY) AS FQUANTITY " +
                "FROM T__DETAIL " +
                "WHERE " +
                "FORDER_ID = ? AND ACTIVITY = ? " +
                "GROUP BY " +
                "FRATE," +
                "FORDER_ID," +
                "FPRODUCT_ID," +
                "IS_ASSEMBLE," +
                "FKFDATE," +
                "FKFPERIOD," +
                "FUNIT_ID,FTAX_UNIT_PRICE,FDISCOUNT,FSTORAGE_ID,FBATCH,FPOSITION_ID,FINTER_ID,FENTRY_ID", new String[]{orderID+"", Activity+""});
        while (cursor.moveToNext()){
            T_Detail t_detail = new T_Detail();
            t_detail.FProductId = cursor.getString(cursor.getColumnIndex("FPRODUCT_ID"));
            t_detail.FUnitId = cursor.getString(cursor.getColumnIndex("FUNIT_ID"));
            t_detail.FTaxUnitPrice = cursor.getString(cursor.getColumnIndex("FTAX_UNIT_PRICE"));
            t_detail.FQuantity = cursor.getString(cursor.getColumnIndex("FQUANTITY"));
            t_detail.FStorageId = cursor.getString(cursor.getColumnIndex("FSTORAGE_ID"));
            t_detail.FBatch = cursor.getString(cursor.getColumnIndex("FBATCH"));
            t_detail.FPositionId = cursor.getString(cursor.getColumnIndex("FPOSITION_ID"));
            t_detail.FDiscount = cursor.getString(cursor.getColumnIndex("FDISCOUNT"));
            t_detail.FEntryID = cursor.getString(cursor.getColumnIndex("FENTRY_ID"));
            t_detail.FInterID = cursor.getString(cursor.getColumnIndex("FINTER_ID"));
            t_detail.FBarcode = cursor.getString(cursor.getColumnIndex("FBARCODE"));
            t_detail.FKFDate = cursor.getString(cursor.getColumnIndex("FKFDATE"));
            t_detail.FKFPeriod = cursor.getString(cursor.getColumnIndex("FKFPERIOD"));
            t_detail.IsAssemble = cursor.getString(cursor.getColumnIndex("IS_ASSEMBLE"));
            t_detail.FRate = cursor.getString(cursor.getColumnIndex("FRATE"));

            list.add(t_detail);
        }

        return list;
    }

    //统计订单中的总数量
    public static List<SendOrderListBean>  getSendOrderQty(Context context, String activity){
        String num=null;
        List<SendOrderListBean> list = new ArrayList<>();
        DaoSession daoSession = GreenDaoManager.getmInstance(context).getDaoSession();
        Cursor cursor = daoSession.getDatabase().rawQuery("SELECT " +
                "FACTIVITY," +
                "FWL_NO," +
                "SUM(FQTY) AS FQTY," +
                "SUM(FQTYING) AS FQTYING " +
                "FROM SEND_ORDER_LIST_BEAN " +
                "WHERE " +
                "FACTIVITY = ? " +
                "GROUP BY " +
                "FACTIVITY", new String[]{activity});
        while (cursor.moveToNext()){
            SendOrderListBean t_detail = new SendOrderListBean();
            t_detail.FActivity = cursor.getString(cursor.getColumnIndex("FACTIVITY"));
            t_detail.FWlNo = cursor.getString(cursor.getColumnIndex("FWL_NO"));
            t_detail.FQty = cursor.getString(cursor.getColumnIndex("FQTY"));
            t_detail.FQtying = cursor.getString(cursor.getColumnIndex("FQTYING"));
            list.add(t_detail);
        }
        return list;
    }

    //电商出库的列表已验数量处理
    public static List<T_Detail> getPdUpload(Context context,String activity){
        String num=null;
        List<T_Detail> list = new ArrayList<>();
        DaoSession daoSession = GreenDaoManager.getmInstance(context).getDaoSession();
        Cursor cursor = daoSession.getDatabase().rawQuery("SELECT " +
                "ACTIVITY," +
                "FRATE," +
                "IMIE," +
                "FINTER_ID," +
                "MAKER_ID " +
                "FROM T__DETAIL " +
                "WHERE " +
                "ACTIVITY = ? " +
                "GROUP BY " +
                "FRATE", new String[]{activity});
        while (cursor.moveToNext()){
            T_Detail t_detail = new T_Detail();
            t_detail.activity = cursor.getInt(cursor.getColumnIndex("ACTIVITY"));
            t_detail.FInterID = cursor.getString(cursor.getColumnIndex("FINTER_ID"));
            t_detail.IMIE = cursor.getString(cursor.getColumnIndex("IMIE"));
            t_detail.FRate = cursor.getString(cursor.getColumnIndex("FRATE"));
            t_detail.MakerId = cursor.getString(cursor.getColumnIndex("MAKER_ID"));
            list.add(t_detail);
        }

        return list;
    }


    //电商出库的列表已验数量处理
    public static List<T_Detail> getDetailUpload(Context context,String activity,String wlno){
        String num=null;
        List<T_Detail> list = new ArrayList<>();
        DaoSession daoSession = GreenDaoManager.getmInstance(context).getDaoSession();
        Cursor cursor = daoSession.getDatabase().rawQuery("SELECT " +
                "FAPI_CLIENT," +
                "FAPI_SEND_NO," +
                "FAPI_PTNO," +
                "ACTIVITY," +
                "FRATE," +
                "IMIE," +
                "FORDER_ID," +
                "FDISCOUNT " +
                "FROM T__DETAIL " +
                "WHERE " +
                "ACTIVITY = ? AND " +
                "FRATE = ? " +
                "GROUP BY " +
                "FRATE", new String[]{activity,wlno});
        while (cursor.moveToNext()){
            T_Detail t_detail = new T_Detail();
            t_detail.activity = cursor.getInt(cursor.getColumnIndex("ACTIVITY"));
            t_detail.FRate = cursor.getString(cursor.getColumnIndex("FRATE"));
            t_detail.IMIE = cursor.getString(cursor.getColumnIndex("IMIE"));
            t_detail.FOrderId = cursor.getLong(cursor.getColumnIndex("FORDER_ID"));
            t_detail.FDiscount = cursor.getString(cursor.getColumnIndex("FDISCOUNT"));
            t_detail.FApiClient = cursor.getString(cursor.getColumnIndex("FAPI_CLIENT"));
            t_detail.FApiSendNo = cursor.getString(cursor.getColumnIndex("FAPI_SEND_NO"));
            t_detail.FApiPTNo = cursor.getString(cursor.getColumnIndex("FAPI_PTNO"));
            list.add(t_detail);
        }

        return list;
    }
    //
    public static List<PushDownMain>  getPdMain4DbCG(Context context, String tag){
        List<PushDownMain> list = new ArrayList<>();
        DaoSession daoSession = GreenDaoManager.getmInstance(context).getDaoSession();
        Cursor cursor = daoSession.getDatabase().rawQuery("SELECT " +
                "FBILL_NO," +
                "TAG," +
                "FINTER_ID " +
                "FROM PUSH_DOWN_MAIN " +
                "WHERE " +
                "TAG = ? " +
                "GROUP BY " +
                "TAG,FBILL_NO", new String[]{tag});
        while (cursor.moveToNext()){
            PushDownMain t_detail = new PushDownMain();
            t_detail.FBillNo = cursor.getString(cursor.getColumnIndex("FBILL_NO"));
            t_detail.tag = cursor.getInt(cursor.getColumnIndex("TAG"));
            t_detail.FInterID = cursor.getString(cursor.getColumnIndex("FINTER_ID"));
            list.add(t_detail);
        }
        return list;
    }
    //
    public static List<PushDownSub>  getPdSub4DbCG(Context context, String interid){
        List<PushDownSub> list = new ArrayList<>();
        DaoSession daoSession = GreenDaoManager.getmInstance(context).getDaoSession();
        Cursor cursor = daoSession.getDatabase().rawQuery("SELECT " +
                "FBILL_NO," +
                "SUM(FAUX_QTY) AS FAUX_QTY," +
                "SUM(FQTYING) AS FQTYING, " +
                "FINTER_ID " +
                "FROM PUSH_DOWN_SUB " +
                "WHERE " +
                "FINTER_ID = ? " +
                "GROUP BY " +
                "FBILL_NO,FINTER_ID", new String[]{interid});
        while (cursor.moveToNext()){
            PushDownSub t_detail = new PushDownSub();
            t_detail.FBillNo = cursor.getString(cursor.getColumnIndex("FBILL_NO"));
            t_detail.FAuxQty = cursor.getString(cursor.getColumnIndex("FAUX_QTY"));
            t_detail.FQtying = cursor.getString(cursor.getColumnIndex("FQTYING"));
            t_detail.FInterID = cursor.getString(cursor.getColumnIndex("FINTER_ID"));
            list.add(t_detail);
//            t_detail.FStorageId = cursor.getString(cursor.getColumnIndex("FSTORAGE_ID"));
//            t_detail.FoutStorageid = cursor.getString(cursor.getColumnIndex("FOUT_STORAGEID"));
//            t_detail.Foutwavehouseid = cursor.getString(cursor.getColumnIndex("FOUTWAVEHOUSEID"));
//            t_detail.FBatch = cursor.getString(cursor.getColumnIndex("FBATCH"));
//            t_detail.FPositionId = cursor.getString(cursor.getColumnIndex("FPOSITION_ID"));
//            t_detail.FDiscount = cursor.getString(cursor.getColumnIndex("FDISCOUNT"));
//            t_detail.FEntryID = cursor.getString(cursor.getColumnIndex("FENTRY_ID"));
//            t_detail.FInterID = cursor.getString(cursor.getColumnIndex("FINTER_ID"));
//            t_detail.FBarcode = cursor.getString(cursor.getColumnIndex("FBARCODE"));
//            t_detail.IMIE = cursor.getString(cursor.getColumnIndex("IMIE"));
//            t_detail.activity = cursor.getInt(cursor.getColumnIndex("ACTIVITY"));
//            t_detail.FKFPeriod = cursor.getString(cursor.getColumnIndex("FKFPERIOD"));
//            t_detail.IsAssemble = cursor.getString(cursor.getColumnIndex("IS_ASSEMBLE"));

//            list.add(t_detail);
        }
        return list;
    }
    //
    public static List<T_Detail>  getDetail4DbCG(Context context,String activity,String interid){
        List<T_Detail> list = new ArrayList<>();
        DaoSession daoSession = GreenDaoManager.getmInstance(context).getDaoSession();
        Cursor cursor = daoSession.getDatabase().rawQuery("SELECT " +
                "FDISCOUNT," +
                "FBILL_NO," +
                "FORDER_ID," +
                "ACTIVITY," +
                "FINTER_ID," +
                "IMIE " +
                "FROM T__DETAIL " +
                "WHERE " +
                "ACTIVITY = ? AND " +
                "FINTER_ID = ? " +
                "GROUP BY " +
                "ACTIVITY,FINTER_ID", new String[]{activity,interid});
        while (cursor.moveToNext()){
            T_Detail t_detail = new T_Detail();
            t_detail.FBillNo = cursor.getString(cursor.getColumnIndex("FBILL_NO"));
            t_detail.FOrderId = cursor.getLong(cursor.getColumnIndex("FORDER_ID"));
            t_detail.activity = cursor.getInt(cursor.getColumnIndex("ACTIVITY"));
            t_detail.FInterID = cursor.getString(cursor.getColumnIndex("FINTER_ID"));
            t_detail.IMIE = cursor.getString(cursor.getColumnIndex("IMIE"));
            t_detail.FDiscount = cursor.getString(cursor.getColumnIndex("FDISCOUNT"));
            list.add(t_detail);
//            t_detail.FStorageId = cursor.getString(cursor.getColumnIndex("FSTORAGE_ID"));
//            t_detail.FoutStorageid = cursor.getString(cursor.getColumnIndex("FOUT_STORAGEID"));
//            t_detail.Foutwavehouseid = cursor.getString(cursor.getColumnIndex("FOUTWAVEHOUSEID"));
//            t_detail.FBatch = cursor.getString(cursor.getColumnIndex("FBATCH"));
//            t_detail.FPositionId = cursor.getString(cursor.getColumnIndex("FPOSITION_ID"));
//            t_detail.FDiscount = cursor.getString(cursor.getColumnIndex("FDISCOUNT"));
//            t_detail.FEntryID = cursor.getString(cursor.getColumnIndex("FENTRY_ID"));
//            t_detail.FInterID = cursor.getString(cursor.getColumnIndex("FINTER_ID"));
//            t_detail.FBarcode = cursor.getString(cursor.getColumnIndex("FBARCODE"));
//            t_detail.IMIE = cursor.getString(cursor.getColumnIndex("IMIE"));
//            t_detail.activity = cursor.getInt(cursor.getColumnIndex("ACTIVITY"));
//            t_detail.FKFPeriod = cursor.getString(cursor.getColumnIndex("FKFPERIOD"));
//            t_detail.IsAssemble = cursor.getString(cursor.getColumnIndex("IS_ASSEMBLE"));

//            list.add(t_detail);
        }

        return list;
    }


    //电商出库的列表已验数量处理
    public static String  getDetailNum(Context context,String code,String wlno){
        if (null==wlno || null == code)return "0";
        String num=null;
        List<T_Detail> list = new ArrayList<>();
        DaoSession daoSession = GreenDaoManager.getmInstance(context).getDaoSession();
        Cursor cursor = daoSession.getDatabase().rawQuery("SELECT " +
                "FRATE," +
                "FPRODUCT_CODE," +
                "SUM(FQUANTITY) AS FQUANTITY " +
                "FROM T__DETAIL " +
                "WHERE " +
                "FPRODUCT_CODE = ? AND " +
                "FRATE = ? " +
                "GROUP BY " +
                "FPRODUCT_CODE", new String[]{code,wlno});
        while (cursor.moveToNext()){
//            T_Detail t_detail = new T_Detail();
            num=cursor.getString(cursor.getColumnIndex("FQUANTITY"));
//            t_detail.FProductId = cursor.getString(cursor.getColumnIndex("FPRODUCT_ID"));
//            t_detail.FUnitId = cursor.getString(cursor.getColumnIndex("FUNIT_ID"));
//            t_detail.FTaxUnitPrice = cursor.getString(cursor.getColumnIndex("FTAX_UNIT_PRICE"));
//            t_detail.FQuantity = cursor.getString(cursor.getColumnIndex("FQUANTITY"));
//            t_detail.FStorageId = cursor.getString(cursor.getColumnIndex("FSTORAGE_ID"));
//            t_detail.FoutStorageid = cursor.getString(cursor.getColumnIndex("FOUT_STORAGEID"));
//            t_detail.Foutwavehouseid = cursor.getString(cursor.getColumnIndex("FOUTWAVEHOUSEID"));
//            t_detail.FBatch = cursor.getString(cursor.getColumnIndex("FBATCH"));
//            t_detail.FPositionId = cursor.getString(cursor.getColumnIndex("FPOSITION_ID"));
//            t_detail.FDiscount = cursor.getString(cursor.getColumnIndex("FDISCOUNT"));
//            t_detail.FEntryID = cursor.getString(cursor.getColumnIndex("FENTRY_ID"));
//            t_detail.FInterID = cursor.getString(cursor.getColumnIndex("FINTER_ID"));
//            t_detail.FBarcode = cursor.getString(cursor.getColumnIndex("FBARCODE"));
//            t_detail.IMIE = cursor.getString(cursor.getColumnIndex("IMIE"));
//            t_detail.activity = cursor.getInt(cursor.getColumnIndex("ACTIVITY"));
//            t_detail.FKFPeriod = cursor.getString(cursor.getColumnIndex("FKFPERIOD"));
//            t_detail.IsAssemble = cursor.getString(cursor.getColumnIndex("IS_ASSEMBLE"));

//            list.add(t_detail);
        }

        return num==null?"0":num;
    }

    public static String dealSendOrderDetail(List<SendOrderResult.DeliverysBean.DetailsBean> details,String wlNo,String storageCode,String activity) {
        try{
            PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
            PurchaseInStoreUploadBean.purchaseInStore puBean = pBean.new purchaseInStore();
            ArrayList<String> detailContainer = new ArrayList<>();
            ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data = new ArrayList<>();
            detailContainer = new ArrayList<>();
            puBean = pBean.new purchaseInStore();
            String detail = "";
            puBean.main = activity;
            for (int j = 0; j < details.size(); j++) {
                if (j != 0 && j % 49 == 0) {
                    Log.e("j%49", j % 49 + "");
                    SendOrderResult.DeliverysBean.DetailsBean t_detail = details.get(j);
                    detail = detail +
                            t_detail.getItem_code() + "|" +
                            t_detail.getQty() + "|" +
                            t_detail.getTrade_code() + "|" +
                            wlNo + "|"+
                            storageCode + "|";
                    detail = detail.subSequence(0, detail.length() - 1).toString();
                    detailContainer.add(detail);
                    detail = "";
                } else {
                    Log.e("j", j + "");
                    Log.e("details.size()", details.size() + "");
                    SendOrderResult.DeliverysBean.DetailsBean t_detail = details.get(j);
                    detail = detail +
                            t_detail.getItem_code() + "|" +
                            t_detail.getQty() + "|" +
                            t_detail.getTrade_code() + "|" +
                            wlNo + "|"+
                            storageCode + "|";
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
            pBean.list = data;
            return new Gson().toJson(pBean);
        }catch (Exception e){
            return "";
        }
    }
    //电商出库红字(管易退货单)，用于拼接所需数据到后台，返回显示数据
    public static String dealBackOrderDetail(List<BackOrderResult.TradeReturnsBean.DetailsBean> details, String wlNo,String storagecode, String activity) {
        try{
            PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
            PurchaseInStoreUploadBean.purchaseInStore puBean = pBean.new purchaseInStore();
            ArrayList<String> detailContainer = new ArrayList<>();
            ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data = new ArrayList<>();
            detailContainer = new ArrayList<>();
            puBean = pBean.new purchaseInStore();
            String detail = "";
            puBean.main = activity;
            for (int j = 0; j < details.size(); j++) {
                if (j != 0 && j % 49 == 0) {
                    Log.e("j%49", j % 49 + "");
                    BackOrderResult.TradeReturnsBean.DetailsBean t_detail = details.get(j);
                    detail = detail +
                            t_detail.getItem_code() + "|" +
                            t_detail.getQty() + "|" +
                            "" + "|" +
                            wlNo + "|" +
                            storagecode + "|";
                    detail = detail.subSequence(0, detail.length() - 1).toString();
                    detailContainer.add(detail);
                    detail = "";
                } else {
                    Log.e("j", j + "");
                    Log.e("details.size()", details.size() + "");
                    BackOrderResult.TradeReturnsBean.DetailsBean t_detail = details.get(j);
                    detail = detail +
                            t_detail.getItem_code() + "|" +
                            t_detail.getQty() + "|" +
                            "" + "|" +
                            wlNo + "|" +
                            storagecode + "|";
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
            pBean.list = data;
            return new Gson().toJson(pBean);
        }catch (Exception e){
            return "";
        }
    }

    //下推时，统一回单数据请求
    public static long findOrderCode(final Context context, int activity, ArrayList<String> fidcontainer){
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data = new ArrayList<>();
        PurchaseInStoreUploadBean.purchaseInStore puBean = pBean.new purchaseInStore();
        ArrayList<String> detailContainer = new ArrayList<>();
        String con="";
        for (String str:fidcontainer) {
            con= con+str+",";
        }
        if (con.length() > 0) {
            con = con.subSequence(0, con.length() - 1).toString();
        }
        ArrayList<T_main> mainTips = new ArrayList<>();
        String SQL = "SELECT ORDER_ID,FINDEX,FDELIVERY_TYPE FROM T_MAIN WHERE ACTIVITY=? AND FDELIVERY_TYPE IN ("+con+")";
        Lg.e("SQL:"+SQL);
        Cursor cursor = GreenDaoManager.getmInstance(context).getDaoSession().getDatabase().rawQuery(SQL, new String[]{activity + ""});
        while (cursor.moveToNext()) {
            T_main f = new T_main();
            f.FIndex = cursor.getString(cursor.getColumnIndex("FINDEX"));
            f.orderId = cursor.getLong(cursor.getColumnIndex("ORDER_ID"));
            f.FDeliveryType = cursor.getString(cursor.getColumnIndex("FDELIVERY_TYPE"));
            mainTips.add(f);
        }
        if (mainTips.size()>0){
            if (mainTips.size()==1){
                return mainTips.get(0).orderId;
            }else{
                long ordercode = System.currentTimeMillis();
                String oldOrderCode="";
                for (int i = 0; i < mainTips.size(); i++) {
                    //重新查找并更新，不适用上面的查找数据，不然会被清空，无法更新
                    List<T_main> mains = GreenDaoManager.getmInstance(context).getDaoSession().getT_mainDao().queryBuilder().where(
                            T_mainDao.Properties.Activity.eq(activity),
                            T_mainDao.Properties.OrderId.eq(mainTips.get(i).orderId)
                    ).build().list();
                    List<T_Detail> t_details = GreenDaoManager.getmInstance(context).getDaoSession().getT_DetailDao().queryBuilder().where(
                            T_DetailDao.Properties.FOrderId.eq(mainTips.get(i).orderId)
                    ).build().list();
                    for (T_Detail bean:t_details) {
                        bean.FOrderId = ordercode;
                        GreenDaoManager.getmInstance(context).getDaoSession().getT_DetailDao().update(bean);
                    }
                    for (T_main bean:mains) {
                        bean.orderId = ordercode;
                        GreenDaoManager.getmInstance(context).getDaoSession().getT_mainDao().update(bean);
                        Lg.e("更新main："+bean.toString());
                    }
                    if (i != 0 && i % 49 == 0) {
                        oldOrderCode = oldOrderCode+
                                mainTips.get(i).orderId+"|"+
                                BasicShareUtil.getInstance(context).getIMIE()+"|";
                        oldOrderCode = oldOrderCode.subSequence(0, oldOrderCode.length() - 1).toString();
                        detailContainer.add(oldOrderCode);
                        oldOrderCode="";
                    }else{
                        oldOrderCode = oldOrderCode+
                                mainTips.get(i).orderId+"|"+
                                BasicShareUtil.getInstance(context).getIMIE()+"|";
                    }
                }
                if (oldOrderCode.length() > 0) {
                    oldOrderCode = oldOrderCode.subSequence(0, oldOrderCode.length() - 1).toString();
                }
                detailContainer.add(oldOrderCode);
                puBean.main=ordercode+"|"+BasicShareUtil.getInstance(context).getIMIE();
                puBean.detail = detailContainer;
                data.add(puBean);
                pBean.list=data;
                App.getRService().getOrderCodeUpdate(new Gson().toJson(pBean), new MySubscribe<CommonResponse>() {
                    @Override
                    public void onNext(CommonResponse commonResponse) {
                        Lg.e("更新临时表成功");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.showText(context,"更新临时表失败"+e.toString());
                        Lg.e("更新临时表-----失败"+e.toString());
                    }
                });
                return ordercode;
            }
        }else{
            return System.currentTimeMillis();
        }

    }
    //检测点击回单时是否存在单据
    public static boolean checkHasDetail(Context mContext,int activity){
        return GreenDaoManager.getmInstance(mContext).getDaoSession().getT_DetailDao().queryBuilder().where(
                T_DetailDao.Properties.Activity.eq(activity)).build().list().size()>0;
    }
}
