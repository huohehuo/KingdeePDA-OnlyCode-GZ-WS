package com.fangzuo.assist.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.greendao.gen.BarCodeDao;
import com.fangzuo.greendao.gen.BibieDao;
import com.fangzuo.greendao.gen.ClientDao;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.DepartmentDao;
import com.fangzuo.greendao.gen.EmployeeDao;
import com.fangzuo.greendao.gen.GetGoodsDepartmentDao;
import com.fangzuo.greendao.gen.InStorageNumDao;
import com.fangzuo.greendao.gen.InStoreTypeDao;
import com.fangzuo.greendao.gen.PayTypeDao;
import com.fangzuo.greendao.gen.PriceMethodDao;
import com.fangzuo.greendao.gen.ProductDao;
import com.fangzuo.greendao.gen.PurchaseMethodDao;
import com.fangzuo.greendao.gen.StorageDao;
import com.fangzuo.greendao.gen.SuppliersDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.fangzuo.greendao.gen.UnitDao;
import com.fangzuo.greendao.gen.UserDao;
import com.fangzuo.greendao.gen.WanglaikemuDao;
import com.fangzuo.greendao.gen.WaveHouseDao;
import com.fangzuo.greendao.gen.YuandanTypeDao;
import com.google.gson.Gson;

import org.greenrobot.greendao.async.AsyncSession;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DataService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.fangzuo.assist.Service.action.FOO";
    private static final String ACTION_BAZ = "com.fangzuo.assist.Service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.fangzuo.assist.Service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.fangzuo.assist.Service.extra.PARAM2";

    public DataService() {
        super("DataService");
    }

    private DaoSession session;
    private T_DetailDao t_detailDao;
    private T_mainDao t_mainDao;
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        session = GreenDaoManager.getmInstance(App.getContext()).getDaoSession();
        t_mainDao = session.getT_mainDao();
        t_detailDao = session.getT_DetailDao();
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void deleteAll(Context context) {
        mContext = context;
        Intent intent = new Intent(context, DataService.class);
        intent.setAction(ACTION_FOO);
        context.startService(intent);
    }

    /**
     *
     * @param context
     * @param param1        activity
     * @param param2        下推时的单号
     */
    public static void deleteDetail(Context context, int param1,long param2) {
        Intent intent = new Intent(context, DataService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                handleActionFoo();
            } else if (ACTION_BAZ.equals(action)) {
                final int param1 = intent.getIntExtra(EXTRA_PARAM1,0);
                final long param2 = intent.getLongExtra(EXTRA_PARAM2,0);
                handleActionBaz(param1,param2);
            }
        }
    }

    /**
     * 删除所有本地数据
     */
    private void handleActionFoo() {
        session.getBibieDao().deleteAll();
        session.getBarCodeDao().deleteAll();
        session.getT_DetailDao().deleteAll();
        session.getT_mainDao().deleteAll();
        session.getClientDao().deleteAll();
        session.getDepartmentDao().deleteAll();
        session.getEmployeeDao().deleteAll();
        session.getGetGoodsDepartmentDao().deleteAll();
        session.getInStorageNumDao().deleteAll();
        session.getInStoreTypeDao().deleteAll();
        session.getPayTypeDao().deleteAll();
        session.getPDMainDao().deleteAll();
        session.getPDSubDao().deleteAll();
        session.getPriceMethodDao().deleteAll();
        session.getProductDao().deleteAll();
        session.getPurchaseMethodDao().deleteAll();
        session.getPushDownMainDao().deleteAll();
        session.getPushDownSubDao().deleteAll();
        session.getStorageDao().deleteAll();
        session.getSuppliersDao().deleteAll();
        session.getUnitDao().deleteAll();
        session.getUserDao().deleteAll();
        session.getWanglaikemuDao().deleteAll();
        session.getWaveHouseDao().deleteAll();
        session.getYuandanTypeDao().deleteAll();
        session.getSendOrderListBeanDao().deleteAll();
        session.getGProductDao().deleteAll();
        session.getPrintHistoryDao().deleteAll();
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private PurchaseInStoreUploadBean pBean;
    private PurchaseInStoreUploadBean.purchaseInStore listBean;
    private ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data;
    private ArrayList<T_Detail> t_detailList;
    private ArrayList<T_main> t_mainsList;
    DownloadReturnBean dBean;
    List<T_Detail> list;
    List<T_main> list_main;
    private void handleActionBaz(int activity,long orderid) {
    App.getRService().deleteAllDetailTable(BasicShareUtil.getInstance(mContext).getIMIE(), new MySubscribe<CommonResponse>() {
        @Override
        public void onNext(CommonResponse commonResponse) {
            EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Delete_Ok,"删除成功"));
        }
        @Override
        public void onError(Throwable e) {
            EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Delete_Error,"删除失败："+e.toString()));
        }
    });

//        if (activity == 0){
//            list = t_detailDao.loadAll();
//            list_main = t_mainDao.loadAll();
//            if (list.size()>0){
//                pBean = new PurchaseInStoreUploadBean();
//                listBean=pBean.new purchaseInStore();
//                data = new ArrayList<>();
//                String detail ="";
//                listBean.main="";
//                ArrayList<String> detailContainer = new ArrayList<>();
//                for (int j = 0; j < list.size(); j++) {
//                    if (j != 0 && j % 49 == 0) {
//                        Log.e("j%49", j % 49 + "");
//                        T_Detail t_detail = list.get(j);
//                        detail = detail +
//                                t_detail.FBarcode + "|" +
//                                t_detail.FQuantity + "|" +
//                                t_detail.IMIE + "|" +
//                                t_detail.FOrderId + "|";
//                        detail = detail.subSequence(0, detail.length() - 1).toString();
//                        detailContainer.add(detail);
//                        detail = "";
//                    } else {
//                        Log.e("j", j + "");
//                        T_Detail t_detail = list.get(j);
//                        detail = detail +
//                                t_detail.FBarcode + "|" +
//                                t_detail.FQuantity + "|" +
//                                t_detail.IMIE + "|" +
//                                t_detail.FOrderId + "|";
//                        Log.e("detail1", detail);
//                    }
//                }
//                if (detail.length() > 0) {
//                    detail = detail.subSequence(0, detail.length() - 1).toString();
//                }
//                Log.e("detail", detail);
//                detailContainer.add(detail);
//                listBean.detail = detailContainer;
//                data.add(listBean);
//                pBean.list=data;
//                Gson gson = new Gson();
//                App.getRService().getCodeCheckDelete(gson.toJson(pBean), new MySubscribe<CommonResponse>() {
//                    @Override
//                    public void onNext(CommonResponse commonResponse) {
//                        t_detailDao.deleteInTx(list);
//                        t_mainDao.deleteInTx(list_main);
//                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Delete_Ok,"删除成功"));
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Delete_Error,"删除失败："+e.toString()));
//                    }
//                });
//            }else{
//                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Delete_Ok,"删除成功"));
//            }
//        }else{
//            list = t_detailDao.queryBuilder().where(
//                    T_DetailDao.Properties.Activity.eq(activity),
//                    T_DetailDao.Properties.FOrderId.eq(orderid)
//            ).build().list();
//            list_main = t_mainDao.queryBuilder().where(
//                    T_mainDao.Properties.Activity.eq(activity),
//                    T_mainDao.Properties.OrderId.eq(orderid)
//            ).build().list();
//            if (list.size()>0){
//                pBean = new PurchaseInStoreUploadBean();
//                listBean=pBean.new purchaseInStore();
//                data = new ArrayList<>();
//                String detail ="";
//                listBean.main="";
//                ArrayList<String> detailContainer = new ArrayList<>();
//                for (int j = 0; j < list.size(); j++) {
//                    if (j != 0 && j % 49 == 0) {
//                        Log.e("j%49", j % 49 + "");
//                        T_Detail t_detail = list.get(j);
//                        detail = detail +
//                                t_detail.FBarcode + "|" +
//                                t_detail.FQuantity + "|" +
//                                t_detail.IMIE + "|" +
//                                t_detail.FOrderId + "|";
//                        detail = detail.subSequence(0, detail.length() - 1).toString();
//                        detailContainer.add(detail);
//                        detail = "";
//                    } else {
//                        Log.e("j", j + "");
//                        T_Detail t_detail = list.get(j);
//                        detail = detail +
//                                t_detail.FBarcode + "|" +
//                                t_detail.FQuantity + "|" +
//                                t_detail.IMIE + "|" +
//                                t_detail.FOrderId + "|";
//                        Log.e("detail1", detail);
//                    }
//                }
//                if (detail.length() > 0) {
//                    detail = detail.subSequence(0, detail.length() - 1).toString();
//                }
//                Log.e("detail", detail);
//                detailContainer.add(detail);
//                listBean.detail = detailContainer;
//                data.add(listBean);
//                pBean.list=data;
//                Gson gson = new Gson();
//                App.getRService().getCodeCheckDelete(gson.toJson(pBean), new MySubscribe<CommonResponse>() {
//                    @Override
//                    public void onNext(CommonResponse commonResponse) {
//                        t_detailDao.deleteInTx(list);
//                        t_mainDao.deleteInTx(list_main);
//                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Delete_Ok,"删除成功"));
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Delete_Error,"删除失败："+e.toString()));
//                    }
//                });
//            }else{
//                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Delete_Ok,"删除成功"));
//            }
//        }

    }

}
