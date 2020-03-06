package com.fangzuo.assist.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommonUtil {
    /*条码规则：
    原材料条码规则:条码^批次^采购订单号^数量
    */
    public static List<String> ScanBack(String code) {
        List<String> list = new ArrayList<>();
        if (code.contains("^")){
            String[] split = code.split("\\^", 4);
            Log.e("code:", code);
            Log.e("code:", split.length + "");
            if (split.length == 4) {//条形码^批次^订单编号^数量
                try {
                    list.add(split[0]);
                    list.add(split[1]);
                    list.add(split[2]);
                    list.add(split[3]);
                    return list;
                } catch (Exception e) {
                    Toast.showText(App.getContext(), "条码有误");
                    return new ArrayList<>();
                }

            } else {
                Toast.showText(App.getContext(), "条码有误,截取数量有误");
                return new ArrayList<>();
            }
        }else{
//            try {
//                list.add(code);
//                return list;
//            } catch (Exception e) {
                Toast.showText(App.getContext(), "条码有误,不存在指定符号");
                return new ArrayList<>();
//            }
        }

//
//        if (code.length()>22){
//            String barcode = code.substring(0, 12);
//            list.add(barcode);
//            return list;
//        }else if (code.length()>16 && code.length()<20){
//            String barcode = code.substring(0, 12);
//            list.add(barcode);
//            return list;
//        }else{
//            return new ArrayList<>();
//        }

        // 大于12位的条码  前面12是条形码 后面为数量
        //角标以 0 未开始获取
//        if (code.length()>12){
//            try {
//                String barcode = code.substring(0, 12);
//                list.add(barcode);
//                String num = code.substring(12, code.length());
//                list.add(num);
//                return list;
//            } catch (Exception e) {
//                Toast.showText(App.getContext(), "条码有误");
//                return new ArrayList<>();
//            }
//        }else{
//            Toast.showText(App.getContext(), "条码有误");
//            return new ArrayList<>();
//        }
    }
    //先进先出时添加临时表的拼接
    public static String getJsonForCheckBatch(String main,String FItemID,String unit,String num,
                                              String storageid,String wave,String batch,String imie,
                                              String ordercode,String activity){
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        PurchaseInStoreUploadBean.purchaseInStore puBean = pBean.new purchaseInStore();
        ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data = new ArrayList<>();
        puBean.main = main;
        ArrayList<String> detailContainer = new ArrayList<>();
        detailContainer.add(FItemID+"|"+
                unit+"|"+
                num+"|"+
                storageid+"|"+
                wave+"|"+
                batch+"|"+
                imie+"|"+
                ordercode+"|"+
                activity
        );
        puBean.detail=detailContainer;
        data.add(puBean);
        pBean.list=data;
        return new Gson().toJson(pBean);
    }

    //生成单据编号
    public static long createOrderCode(Activity activity){
        Long ordercode=0l;
        ShareUtil share =ShareUtil.getInstance(activity.getApplicationContext());
        if (share.getOrderCode(activity) == 0) {
            ordercode = Long.parseLong(getTime(false) + "001");
            share.setOrderCode(activity,ordercode);
        } else {
            //当不是当天时，生成新的单据，重新计算
            if (String.valueOf(share.getOrderCode(activity)).contains(getTime(false))){
                ordercode =share.getOrderCode(activity);
            }else{
                ordercode = Long.parseLong(getTime(false) + "001");
                share.setOrderCode(activity,ordercode);
            }
        }
        Log.e("生成新的单据:", ordercode + "");
        return ordercode;
    }
    //生成单据编号
    public static long createOrderCode(Activity activity,String no) {
        Long ordercode = 0l;
        ShareUtil share = ShareUtil.getInstance(activity.getApplicationContext());
        if (share.getOrderCode(activity,no) == 0) {
            ordercode = Long.parseLong(getTimeLong(false) + "001");
            share.setOrderCode(activity,no, ordercode);
        } else {
            //当不是当天时，生成新的单据，重新计算
//            if (String.valueOf(share.getOrderCode(activity,no)).contains(getTime(false))) {
            ordercode = share.getOrderCode(activity,no);
//            } else {
//                ordercode = Long.parseLong(getTimeLong(false) + "001");
//                share.setOrderCode(activity,no, ordercode);
//            }
        }
        Log.e("生成新的单据:", ordercode + "");
        return ordercode;
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

    //更新软件
    public static void installApk(Context context, String apkPath) {
        if (context == null || TextUtils.isEmpty(apkPath)) {
            return;
        }
        Lg.e("获得文件路径："+apkPath);

        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            Lg.e(">=24时");
//            Log.v(TAG,"7.0以上，正在安装apk...");
            //provider authorities
            Uri apkUri = FileProvider.getUriForFile(context,
                    context.getPackageName()+".provider",
//                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
//            Uri apkUri = FileProvider.getUriForFile(context, "com.fangzuo.assist.fileprovider", file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            Lg.e("<24时");
//            Log.v(TAG,"7.0以下，正在安装apk...");
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }



    //读取本地下载好的txt数据包，解析
    public static String getString(String txtName) {
        String lineTxt = null;
        StringBuilder builder = new StringBuilder();
        try {

            File file = new File(txtName);
            if (file.isFile() && file.exists()) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "GBK");
                BufferedReader br = new BufferedReader(isr);
                while ((lineTxt = br.readLine()) != null) {
                    lineTxt+=br.readLine();
                    Lg.e("读取txt:"+lineTxt);
                    if (!"".equals(lineTxt)){
                        builder.append(lineTxt);
//                        Lg.e("读取txt2:"+builder.toString());
                    }
                }
                br.close();
                return builder.toString().substring(0,builder.toString().length()-4);
            } else {
                System.out.println("文件不存在!");
            }

//            File f = new File(txtName);
//            //以防有中文名路径，中文路径里面的空格会被"%20"代替
//            txtName = java.net.URLDecoder.decode(txtName, "utf-8");
//
//            FileInputStream redis = new FileInputStream(f);
////            br = new BufferedReader(new InputStreamReader(redis));
//
//            InputStream inputStream = App.getContext().getResources().getAssets().open(txtName);
//            byte[] arrayOfByte = new byte[inputStream.available()];
//            inputStream.read(arrayOfByte);
//            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineTxt;
    }
}



/*
String str = "abc,12,3yy98,0";
String[]  strs=str.split(",");//以，截取   或者\\^
for(int i=0,len=strs.length;i<len;i++){
    System.out.println(strs[i].toString());



 sb.substring(2);//索引号 2 到结尾

String barcode = code.substring(0, 12);//第一位到第十一位

3.通过StringUtils提供的方法
StringUtils.substringBefore(“dskeabcee”, “e”);
/结果是：dsk/
这里是以第一个”e”，为标准。

StringUtils.substringBeforeLast(“dskeabcee”, “e”)
结果为：dskeabce
这里以最后一个“e”为准。
* */