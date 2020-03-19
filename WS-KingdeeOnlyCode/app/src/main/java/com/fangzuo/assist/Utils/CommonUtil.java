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
import com.fangzuo.assist.Beans.BlueToothBean;
import com.fangzuo.assist.Beans.PrintTimeBean;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.PrintHistory;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;

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

import zpSDK.zpSDK.zpBluetoothPrinter;

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




    //刨光打印
    public static void doPrint(Context context, List<PrintHistory> beans, String times) throws Exception{
        Lg.e("打印数据:"+times,beans);
        zpBluetoothPrinter zpSDK=new zpBluetoothPrinter(context);
        if(!zpSDK.connect(Hawk.get(Config.OBJ_BLUETOOTH, new BlueToothBean("", "")).address))
        {
            Toast.showText(context,"打印机连接失败------");
            return;
        }
        int size=4;
        int size3=3;
        int size2=2;
        int lineSize=3;
//        int printNum = Integer.parseInt(null==times?"1":times);
        int printNum = Hawk.get(Config.PrintNum,1);
        for (int i = 0; i < printNum; i++) {

            List<PrintTimeBean> printTimeBeanList = new ArrayList<>();
            if (beans.size()>3){
                if (beans.size()>6){
                    printTimeBeanList.add(new PrintTimeBean(0,3));
                    printTimeBeanList.add(new PrintTimeBean(3,6));
                    printTimeBeanList.add(new PrintTimeBean(6,beans.size()));
                }else{
                    printTimeBeanList.add(new PrintTimeBean(0,3));
                    printTimeBeanList.add(new PrintTimeBean(3,beans.size()));
                }
            }else{
                printTimeBeanList.add(new PrintTimeBean(0,beans.size()));
            }

            for (int k = 0; k < printTimeBeanList.size(); k++) {
//            for (int k = 0; k < 2; k++) {
                PrintHistory beanTop = beans.get(0);
                zpSDK.pageSetup(850, 600);
//            zpSDK.drawBox(1,1,1,668,888);
                zpSDK.drawText(0, 1, "__________________________________________________________________", 2, 0, 0, false, false);
                zpSDK.drawText(290, 34, "收料通知请检单", size2, 0, 1, false, false);
                zpSDK.drawText(560, 34, "类别:"+beanTop.FPlanType, size2, 0, 1, false, false);
                zpSDK.drawText(0, 50, "__________________________________________________________________", 2, 0, 0, false, false);
                zpSDK.drawBarCode(100, 78,  beanTop.FBarCode, 128, false, 4, 66);
                zpSDK.drawText(20, 142,"               "+beanTop.FBarCode, size3, 0, 0, false, false);
                zpSDK.drawText(20, 165, "供应商:", size, 0, 0, false, false);
                zpSDK.drawText(200, 173, beanTop.FSupplier,size3, 0, 0, false, false);
                zpSDK.drawText(20, 215, "物料名称", size, 0, 0, false, false);//区间 20--300
                zpSDK.drawText(300, 215, "物料代码", size, 0, 0, false, false);//区间 300--560
                zpSDK.drawText(560, 215, "报检数量", size, 0, 0, false, false);//区间 560--300
//                zpSDK.drawText(0, 202, "_______________________________________________________________", 2, 0, 0, false, false);

                int print_y = 265;//初始位置
                int print_y2 = 265;//初始位置(规格和数量)
                int print_y_line = 265;//横线的位置
                for (int j = printTimeBeanList.get(k).FStart; j < printTimeBeanList.get(k).FEnd; j++) {
//                for (int j = 0; j < 3; j++) {
                    PrintHistory bean = beans.get(j);
                    if (bean.FName.length()>13){
                        if (bean.FName.length()>26){
                            zpSDK.drawText(20, print_y, bean.FName.substring(0,13),size2, 0, 0, false, false);
                            zpSDK.drawText(20, print_y+28, bean.FName.substring(13,26),size2, 0, 0, false, false);
//                            zpSDK.drawText(20, print_y+50, bean.FName.substring(26,bean.FName.length()),size2, 0, 0, false, false);
                        }else{
                            zpSDK.drawText(20, print_y, bean.FName.substring(0,13),size2, 0, 0, false, false);
                            zpSDK.drawText(20, print_y+28, bean.FName.substring(13,bean.FName.length()),size2, 0, 0, false, false);
                        }
                        print_y_line+=51;
                        print_y+=84;
                    }else{
                        zpSDK.drawText(20, print_y, bean.FName,size2, 0, 0, false, false);
                        print_y_line+=51;
                        print_y+=84;
                    }
                    zpSDK.drawText(300, print_y2, bean.FNumber,size3, 0, 0, false, false);
                    zpSDK.drawText(590, print_y2, bean.FNum,size3, 0, 0, false, false);
                    zpSDK.drawText(0, print_y_line, "__________________________________________________________________", 2, 0, 0, false, false);
                    //更新其他定位到最新的y轴
                    print_y2=print_y;
                    print_y_line=print_y;
                }
                    zpSDK.drawText(20, 570, "报检人:", size3, 0, 0, false, false);
                    zpSDK.drawText(200, 573, beans.get(0).FBJMan,size3, 0, 0, false, false);
                    zpSDK.drawText(524, 573, getTime(true),size3, 0, 0, false, false);
                    zpSDK.drawText(704, 573, "("+printTimeBeanList.size()+"/"+(k+1)+")",size3, 0, 0, false, false);
                zpSDK.print(0, 1);



            }
//                for (int j = 0; j < beans.size(); j++) {
//                    PrintHistory bean = beans.get(j);
//                    zpSDK.pageSetup(850, 600);
////            zpSDK.drawBox(1,1,1,668,888);
//                    zpSDK.drawText(0, 1, "_______________________________________________________________", 2, 0, 0, false, false);
//                    zpSDK.drawText(270, 32, "收料通知请检单", size2, 0, 1, false, false);
//                    zpSDK.drawText(0, 50, "_______________________________________________________________", 2, 0, 0, false, false);
//                    zpSDK.drawBarCode(100, 78,  bean.FBarCode, 128, false, 4, 66);
//                    zpSDK.drawText(20, 150, "供应商:", size, 0, 0, false, false);
//                    zpSDK.drawText(200, 158, bean.FSupplier,size3, 0, 0, false, false);
//                    zpSDK.drawText(20, 200, "物料名称", size, 0, 0, false, false);//区间 20--300
//                    zpSDK.drawText(300, 200, "规格型号", size, 0, 0, false, false);//区间 300--560
//                    zpSDK.drawText(560, 200, "报检数量", size, 0, 0, false, false);//区间 560--300
//                    zpSDK.drawText(0, 202, "_______________________________________________________________", 2, 0, 0, false, false);
//
//                    zpSDK.drawText(20, 255, "物料名称",size2, 0, 0, false, false);
//                    zpSDK.drawText(300, 255, "规格型号",size2, 0, 0, false, false);
//                    zpSDK.drawText(590, 255, "报检数量",size2, 0, 0, false, false);
//
//                    int print_y = 250;
//                    if (bean.FName.length()>27){
//                        zpSDK.drawText(20, print_y, bean.FName.substring(0,28),size2, 0, 0, false, false);
//                        zpSDK.drawText(20, print_y, bean.FName.substring(28,bean.FName.length()),size2, 0, 0, false, false);
//                    }else{
//                        zpSDK.drawText(20, print_y, bean.FName,size2, 0, 0, false, false);
//                    }
//
//                    zpSDK.drawText(300, print_y, bean.FModel,size2, 0, 0, false, false);
//                    zpSDK.drawText(590, print_y, bean.FNum,size2, 0, 0, false, false);
//
//
//                    zpSDK.drawText(20, 558, "报检人:", size, 0, 0, false, false);
//                    zpSDK.drawText(200, 564, bean.FBJMan,size3, 0, 0, false, false);
//                    zpSDK.drawText(604, 564, getTime(true),size3, 0, 0, false, false);
//                    zpSDK.print(0, 1);
//                }






//            zpSDK.drawQrCode(10, 800, bean.FBarCode, 1, 12, 0);
//            /*右边数值*/
//            zpSDK.drawText(10, 124, bean.get(0).FBatch,size, 0, 0, false, false);
//            zpSDK.drawText(156, 174, bean.get(0).FName,size, 0, 0, false, false);
//            String[] split = bean.get(0).FModel.split("x", 3);
//            Lg.e("截取长度", split.length);
//            Lg.e("截取长度", split);
//            if (split.length == 3) {
//                zpSDK.drawText(156, 224, split[0],size, 0, 0, false, false);
//                zpSDK.drawText(156, 284, split[1],size, 0, 0, false, false);
//                zpSDK.drawText(156, 344, split[2],size, 0, 0, false, false);
//            }
//            zpSDK.drawText(156, 444, MathUtil.Cut0(bean.get(0).FQtySum),size, 0, 0, false, false);
//            zpSDK.drawText(156, 504, bean.get(0).FM2Sum,size, 0, 0, false, false);

//            zpSDK.drawText(500, 284, "MM",size, 0, 0, false, false);
//            String wide = bean.FBWide==null?"":bean.FBWide;
//            if (wide.length()>19){
//                if (wide.length()<38){
//                    zpSDK.drawText(180, 344, wide.substring(0,19),size2, 0, 0, false, false);
//                    zpSDK.drawText(180, 368, wide.substring(19,wide.length()),size2, 0, 0, false, false);
//                }else{
//                    zpSDK.drawText(180, 344, wide.substring(0,19),size2, 0, 0, false, false);
//                    zpSDK.drawText(180, 368, wide.substring(19,38),size2, 0, 0, false, false);
//                    zpSDK.drawText(180, 392, wide.substring(38,wide.length()),size2, 0, 0, false, false);
//                }
//            }else{
//                zpSDK.drawText(180, 344, wide,size2, 0, 0, false, false);
//            }
////            zpSDK.drawText(500, 344, "MM",size2, 0, 0, false, false);
//            zpSDK.drawText(160, 404, bean.FCeng==null?"":bean.FCeng,size, 0, 0, false, false);
////            zpSDK.drawText(450, 404, bean.FUnitAux==null?"":bean.FUnitAux,size2, 0, 0, false, false);
//            zpSDK.drawText(160, 464, bean.FVolume==null?"":bean.FVolume,size, 0, 0, false, false);
//            zpSDK.drawText(500, 464, "M3",size, 0, 0, false, false);
//            zpSDK.drawText(10, 500, "______________________________________________", 2, 0, 0, false, false);
//            zpSDK.drawQrCode(10, 560, bean.get(0).FBoxCode, 0, 11, 0);
//            zpSDK.drawText(300, 560, "仓位:",size2, 0, 0, false, false);
//            zpSDK.drawText(380, 560, "",size2, 0, 0, false, false);
//            zpSDK.drawText(300, 640, "录入:",size2, 0, 0, false, false);
////            zpSDK.drawText(380, 640, bean.FSaveIn==null?"":bean.FSaveIn,size2, 0, 0, false, false);
//            zpSDK.drawText(380, 640, bean.get(0).FUser,size2, 0, 0, false, false);
//            zpSDK.drawText(300, 720, "审核:",size2, 0, 0, false, false);
////            zpSDK.drawText(380, 720, bean.FCheck==null?"":bean.FCheck,size2, 0, 0, false, false);
//            zpSDK.drawText(300, 790, "日期:",size2, 0, 0, false, false);
//            zpSDK.drawText(380, 790, getTime(true),size2, 0, 0, false, false);
//            zpSDK.drawText(10, 850, "______________________________________________", 2, 0, 0, false, false);
//            zpSDK.print(0, 1);
//            zpSDK.pageSetup(668, 900);
//            zpSDK.drawText(0, 55, "明细:",size2, 0, 0, false, false);
//            zpSDK.drawText(0, 65, "______________________________________________", 2, 0, 0, false, false);
//
//            zpSDK.drawText(90, 90, "L", size, 0, 0, false, false);
//            zpSDK.drawText(260, 90, "PCS", size, 0, 0, false, false);
//            zpSDK.drawText(420, 90, "M2", size, 0, 0, false, false);
//            zpSDK.drawText(0, 123, "______________________________________________", 2, 0, 0, false, false);
//
//            int print_x=150;
//            for (int j = 0; j < bean.size(); j++) {
//                zpSDK.drawText(90, print_x, bean.get(j).FLenght, 2, 0, 0, false, false);
//                zpSDK.drawText(260, print_x, bean.get(j).FQty, 2, 0, 0, false, false);
//                zpSDK.drawText(420, print_x, bean.get(j).FM2, 2, 0, 0, false, false);
//                print_x+=35;
//            }
//            zpSDK.drawText(0, print_x+10, "______________________________________________", 2, 0, 0, false, false);
//            zpSDK.drawText(90, print_x+8, "汇总:", 2, 0, 0, false, false);
//            zpSDK.drawText(260, print_x+8, MathUtil.Cut0(bean.get(0).FQtySum), 2, 0, 0, false, false);
//            zpSDK.drawText(420, print_x+8, bean.get(0).FM2Sum, 2, 0, 0, false, false);
//            zpSDK.print(0, 1);
        }
        zpSDK.disconnect();
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