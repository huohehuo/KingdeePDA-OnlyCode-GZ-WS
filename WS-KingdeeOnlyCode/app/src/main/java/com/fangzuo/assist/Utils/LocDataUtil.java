package com.fangzuo.assist.Utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Spinner;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.Dao.Unit;
import com.fangzuo.assist.Dao.WaveHouse;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.StorageDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.fangzuo.greendao.gen.UnitDao;
import com.fangzuo.greendao.gen.WaveHouseDao;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;

public class LocDataUtil {
    public static boolean checkHasMain(Context mContext,long orderid) {
        List<T_main> list1 = GreenDaoManager.getmInstance(mContext).getDaoSession().getT_mainDao().queryBuilder().where(T_mainDao.Properties.OrderId.eq(orderid)).build().list();
        if (list1.size()>0){
            return true;
        }else{
            return false;
        }
    }
    public static String getDetailNum(Context mContext,long orderid) {
        List<T_Detail> list1 = GreenDaoManager.getmInstance(mContext).getDaoSession().getT_DetailDao().queryBuilder().where(
                T_DetailDao.Properties.FOrderId.eq(orderid)).build().list();
        return list1.size()+"";
    }

    public static Storage getStorage(String str, int type){
        if (null==str)return null;
        DaoSession daoSession = GreenDaoManager.getmInstance(App.getContext()).getDaoSession();
        StorageDao storageDao = daoSession.getStorageDao();
        List<Storage> storages;
        if (type==0){
            storages  = storageDao.queryBuilder().where(StorageDao.Properties.FItemID.eq(str)).build().list();
        }else{
            storages  = storageDao.queryBuilder().whereOr(StorageDao.Properties.FItemID.like(str),StorageDao.Properties.FName.like(str)).build().list();
        }
        if (storages.size()>0){
            return storages.get(0);
        }else{
            return null;
        }
    }
    public static WaveHouse getWaveHouse(String str, int type){
        if (null==str)return null;
        DaoSession daoSession = GreenDaoManager.getmInstance(App.getContext()).getDaoSession();
        WaveHouseDao waveHouseDao = daoSession.getWaveHouseDao();
        List<WaveHouse> waveHouseList = waveHouseDao.queryBuilder().where(WaveHouseDao.Properties.FSPID.eq(str)).build().list();
        if (waveHouseList.size()>0){
            return waveHouseList.get(0);
        }else{
            return null;
        }
    }

    //获取单位的基础数据
    public static void getBaseData4Unit(final Context context){
        BasicShareUtil share = BasicShareUtil.getInstance(context);
        try {
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(7);
            String json = JsonCreater.DownLoadData(
                    share.getDatabaseIp(),
                    share.getDatabasePort(),
                    share.getDataBaseUser(),
                    share.getDataBasePass(),
                    share.getDataBase(),
                    share.getVersion(),
                    choose
            );
            Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                    Log.e("CommonMethod:","联网获取单位："+cBean.returnJson);
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    if(dBean!=null&&dBean.units!=null&&dBean.units.size()>0){
                        UnitDao unitDao = GreenDaoManager.getmInstance(context).getDaoSession().getUnitDao();
                        unitDao.deleteAll();
                        unitDao.insertOrReplaceInTx(dBean.units);
                        unitDao.detachAll();
                    }
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
//                    Toast.showText(context, Msg);
                }
            });

        }catch (Exception e){}

    }
    //获取单位的基础数据
    public static void getBaseData4Storage(final Context context){
        BasicShareUtil share = BasicShareUtil.getInstance(context);
        try {
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(6);
            String json = JsonCreater.DownLoadData(
                    share.getDatabaseIp(),
                    share.getDatabasePort(),
                    share.getDataBaseUser(),
                    share.getDataBasePass(),
                    share.getDataBase(),
                    share.getVersion(),
                    choose
            );
            Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                    Log.e("CommonMethod:","联网获取单位："+cBean.returnJson);
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    if(dBean.storage!=null&&dBean.storage.size()>0){
                        StorageDao unitDao = GreenDaoManager.getmInstance(context).getDaoSession().getStorageDao();
                        unitDao.deleteAll();
                        unitDao.insertOrReplaceInTx(dBean.storage);
                        unitDao.detachAll();
                    }
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
//                    Toast.showText(context, Msg);
                }
            });

        }catch (Exception e){}

    }
}
