package com.fangzuo.assist.Utils.UpgradeUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.widget.LoadingUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.AsyncHttpClient;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppStatisticalUtil {
    public static String BaseUrl = "http://148.70.108.65:8080/LogAssist/UploadStatistical";
//    public static String BaseUrl = "http://192.168.0.136:8084/Assist/UploadStatistical";
    //数据统计操纵
    public static void upDataStatis(final Context context,String activityName) {
        StatisticalBean bean = new StatisticalBean();
        bean.imie = Build.MODEL+"--"+BasicShareUtil.getInstance(context).getIMIE();//手机IMIE码
        bean.AppID=context.getPackageName();//appid
        bean.AppVersion=Info.getAppNo();//版本信息
        bean.onActivity=activityName;//所在页面
        bean.realTime=getTime(true);//当前时间
        bean.phone="10086";//手机号
        Lg.e("数据统计",bean);
        Asynchttp.post(App.getContext(), BaseUrl, new Gson().toJson(bean), new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                Lg.e("统计成功,无返回东西");
//                Lg.e("得到版本信息",cBean.returnJson);
//                showDlg(context,cBean.returnJson);
            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
//                Lg.e("统计失败");
//                Lg.e("查询版本信息错误");
            }
        });
    }
    public static String getTime(boolean b){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(b?"yyyy-MM-dd":"yyyyMMdd");
        Date curDate = new Date();
        Log.e("date",curDate.toString());
        String str = format.format(curDate);
        return str;
    }
    public static String getTimeLong(boolean b) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(b ? "yyyy-MM-dd-HH-mm-ss" : "yyyyMMddHHmmss");
        Date curDate = new Date();
        Log.e("date", curDate.toString());
        String str = format.format(curDate);
        return str;
    }


}
