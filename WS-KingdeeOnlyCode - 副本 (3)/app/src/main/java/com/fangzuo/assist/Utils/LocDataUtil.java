package com.fangzuo.assist.Utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;

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

}
