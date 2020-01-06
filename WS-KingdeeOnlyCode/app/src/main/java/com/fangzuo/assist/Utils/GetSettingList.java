package com.fangzuo.assist.Utils;

import android.text.TextUtils;
import android.util.Log;

import com.fangzuo.assist.Beans.SettingList;
import com.fangzuo.assist.R;

import java.util.ArrayList;

/**
 * Created by NB on 2017/7/28.
 */

public class GetSettingList {
    public static ArrayList<SettingList> getList() {
        ArrayList<SettingList> items = new ArrayList<>();
        SettingList s = new SettingList();
        s.ImageResourse = R.mipmap.download;
        s.tv = "下载配置";
        items.add(s);
        SettingList s1 = new SettingList();
        s1.ImageResourse = R.mipmap.wifi;
        s1.tv = "wifi连接";
        items.add(s1);
        SettingList s2 = new SettingList();
        s2.ImageResourse = R.mipmap.sound;
        s2.tv = "声音设置";
        items.add(s2);
        SettingList s3 = new SettingList();
        s3.ImageResourse = R.mipmap.getnewversion;
        s3.tv = "更新版本";
        items.add(s3);
        SettingList s4 = new SettingList();
        s4.ImageResourse = R.mipmap.tomcat;
        s4.tv = "服务器设置";
        items.add(s4);
        SettingList s5 = new SettingList();
        s5.ImageResourse = R.mipmap.test;
        s5.tv = "网络测试";
        items.add(s5);
        items.add(new SettingList(R.mipmap.test,"通用设置"));
        items.add(new SettingList("1",R.mipmap.chuku,"打印机蓝牙配置"));

        return items;


    }
    public static ArrayList<SettingList> getPurchaseList(String[] ary) {
        ArrayList<SettingList> items = new ArrayList<>();
        //最终返回的选项
        ArrayList<SettingList> backItems = new ArrayList<>();
//        SettingList s = new SettingList();
//        s.ImageResourse = R.mipmap.purchaseorder;
//        s.tv = "采购订单";
//        items.add(s);
//        SettingList s1 = new SettingList();
//        s1.ImageResourse = R.mipmap.purchaseorback;
//        s1.tv = "外购入库";
//        items.add(s1);
//        SettingList s2 = new SettingList();
//        s2.ImageResourse = R.mipmap.purchaseinstorage;
//        s2.tv = "产品入库";
//        items.add(s2);

        items.add(new SettingList("1",R.mipmap.chuku,"组装单"));
        items.add(new SettingList("2",R.mipmap.diaobo,"调拨"));
        items.add(new SettingList("3",R.mipmap.pandian,"电商出库"));
        items.add(new SettingList("4",R.mipmap.pandian,"电商出库红字"));
        items.add(new SettingList("5",R.mipmap.ruku,"其他入库"));
        items.add(new SettingList("6",R.mipmap.chuku,"其他出库"));
        items.add(new SettingList("16",R.mipmap.chuku,"其他出库红字"));
        items.add(new SettingList("7",R.mipmap.pandian,"散装盘点"));
        items.add(new SettingList("8",R.mipmap.pandian,"盘点"));
        items.add(new SettingList("9",R.mipmap.pandian,"库存查询"));
        items.add(new SettingList("17",R.mipmap.pandian,"产品入库红字"));

        for (int i=0; i<items.size();i++){
//            Log.e("test","定位ary:"+ary[i]);
//            Log.e("test","定位items:"+items.get(i).tag);
            //根据ary的值，遍历list符合的item并添加
            for (int j=0;j<ary.length;j++){
                if (TextUtils.equals(items.get(i).tag,ary[j])){
                    Lg.e("加入单据",items.get(i));
                    backItems.add(items.get(i));
                }
            }
        }
//        backItems.add(new SettingList("18",R.mipmap.pandian,"调价单"));
        backItems.add(new SettingList("31",R.mipmap.pandian,"拆箱单"));

        return backItems;


    }

    public static ArrayList<SettingList> getSaleList(String[] ary) {
        ArrayList<SettingList> items = new ArrayList<>();
        //最终返回的选项
        ArrayList<SettingList> backItems = new ArrayList<>();
        items.add(new SettingList("10",R.mipmap.ruku,"生产任务单下推产品入库"));
        items.add(new SettingList("11",R.mipmap.pandian,"委外订单下推委外入库"));
        items.add(new SettingList("12",R.mipmap.pandian,"发货通知下推销售出库"));
        items.add(new SettingList("13",R.mipmap.pandian,"退货通知单下推销售出库红字"));
        items.add(new SettingList("14",R.mipmap.pandian,"汇报单下推产品入库"));
        items.add(new SettingList("15",R.mipmap.pandian,"生产任务单下推生产领料"));
        for (int i=0; i<items.size();i++){
//            Log.e("test","定位ary:"+ary[i]);
//            Log.e("test","定位items:"+items.get(i).tag);
            //根据ary的值，遍历list符合的item并添加
            for (int j=0;j<ary.length;j++){
                if (TextUtils.equals(items.get(i).tag,ary[j])){
                    Lg.e("加入单据",items.get(i));
                    backItems.add(items.get(i));
                }
            }
        }
        backItems.add(new SettingList("19",R.mipmap.pandian,"调拨单验货"));
////        SettingList s = new SettingList();
////        s.ImageResourse = R.mipmap.saleorder;
////        s.tv = "销售订单";
////        items.add(s);
//        SettingList s1 = new SettingList();
//        s1.ImageResourse = R.mipmap.sellinout;
//        s1.tv = "销售出库";
//        items.add(s1);
//        SettingList s2 = new SettingList();
//        s2.ImageResourse = R.mipmap.sellout;
//        s2.tv = "单据下推";
//        items.add(s2);
//        SettingList s3 = new SettingList();
//        s3.ImageResourse = R.mipmap.chuku;
//        s3.tv = "生产领料";
//        items.add(s3);
        return backItems;


    }

    //原材料
    public static ArrayList<SettingList> getStorageList() {
        ArrayList<SettingList> items = new ArrayList<>();
        //最终返回的选项
        ArrayList<SettingList> backItems = new ArrayList<>();
        items.add(new SettingList("20",R.mipmap.ruku,"采购订单下推收料通知单"));
        items.add(new SettingList("21",R.mipmap.pandian,"收料通知下推外购入库"));
        items.add(new SettingList("22",R.mipmap.pandian,"其他入库"));
        items.add(new SettingList("23",R.mipmap.pandian,"其他出库"));
        items.add(new SettingList("24",R.mipmap.pandian,"生产任务单下推产品入库"));
        items.add(new SettingList("25",R.mipmap.pandian,"委外订单下推委外入库"));

        items.add(new SettingList("26",R.mipmap.pandian,"委外订单下推委外出库"));
        items.add(new SettingList("27",R.mipmap.pandian,"生产任务单下推生产领料"));
        items.add(new SettingList("28",R.mipmap.pandian,"收料通知单下推来料检验单"));
        items.add(new SettingList("30",R.mipmap.pandian,"委外订单下推收料通知单"));
        items.add(new SettingList("29",R.mipmap.pandian,"库存查询"));

//        for (int i=0; i<items.size();i++){
////            Log.e("test","定位ary:"+ary[i]);
////            Log.e("test","定位items:"+items.get(i).tag);
//            //根据ary的值，遍历list符合的item并添加
//            for (int j=0;j<ary.length;j++){
//                if (TextUtils.equals(items.get(i).tag,ary[j])){
//                    Lg.e("加入单据",items.get(i));
//                    backItems.add(items.get(i));
//                }
//            }
//        }
        return items;




//        SettingList s = new SettingList();
//        s.ImageResourse = R.mipmap.pandian;
//        s.tv = "盘点";
//        items.add(s);
//        SettingList s1 = new SettingList();
//        s1.ImageResourse = R.mipmap.diaobo;
//        s1.tv = "调拨";
//        items.add(s1);
//        SettingList s2 = new SettingList();
//        s2.ImageResourse = R.mipmap.ruku;
//        s2.tv = "其他入库";
//        items.add(s2);
//        SettingList s3 = new SettingList();
//        s3.ImageResourse = R.mipmap.chuku;
//        s3.tv = "其他出库";
//        items.add(s3);
//        SettingList s4 = new SettingList();
//        s4.ImageResourse = R.mipmap.chuku;
//        s4.tv = "库存查询";
//        items.add(s4);
//        SettingList s5 = new SettingList();
//        s5.ImageResourse = R.mipmap.chuku;
//        s5.tv = "条码状态查询";
//        items.add(s5);
//        return items;


    }

    public static ArrayList<SettingList> GetPushDownList() {
        ArrayList<SettingList> items = new ArrayList<>();
        SettingList s = new SettingList();
        s.ImageResourse = R.mipmap.pandian;
        s.tv = "销售订单下推销售出库";
        items.add(s);
        SettingList s1 = new SettingList();
        s1.ImageResourse = R.mipmap.diaobo;
        s1.tv = "采购订单下推外购入库";
        items.add(s1);
//        SettingList s2 = new SettingList();
//        s2.ImageResourse = R.mipmap.ruku;
//        s2.tv = "发货通知下推销售出库";
//        items.add(s2);
//        SettingList s3 = new SettingList();
//        s3.ImageResourse = R.mipmap.chuku;
//        s3.tv = "收料通知下推外购入库";
//        items.add(s3);
//        SettingList s5 = new SettingList();
//        s5.ImageResourse = R.mipmap.diaobo;
//        s5.tv = "委外订单下推委外出库";
//        items.add(s5);
        SettingList s6 = new SettingList();
        s6.ImageResourse = R.mipmap.ruku;
        s6.tv = "生产任务单下推产品入库";
        items.add(s6);
        SettingList s7= new SettingList();
        s7.ImageResourse = R.mipmap.pandian;
        s7.tv = "生产任务单下推生产领料";
        items.add(s7);
        SettingList s4 = new SettingList();
        s4.ImageResourse = R.mipmap.pandian;
        s4.tv = "委外订单下推委外入库";
        items.add(s4);
//        SettingList s8= new SettingList();
//        s8.ImageResourse = R.mipmap.pandian;
//        s8.tv = "采购订单下推收料通知单";
//        items.add(s8);
//        SettingList s9= new SettingList();
//        s9.ImageResourse = R.mipmap.pandian;
//        s9.tv = "销售订单下推发料通知单";
//        items.add(s9);
//        SettingList s10= new SettingList();
//        s10.ImageResourse = R.mipmap.pandian;
//        s10.tv = "生产任务单下推生产汇报单";
//        items.add(s10);
//        SettingList s12= new SettingList();
//        s12.ImageResourse = R.mipmap.pandian;
//        s12.tv = "汇报单下推产品入库";
//        items.add(s12);
//        SettingList s13= new SettingList();
//        s13.ImageResourse = R.mipmap.pandian;
//        s13.tv = "销售出库单验货";
//        items.add(s13);
//        SettingList s14= new SettingList();
//        s14.ImageResourse = R.mipmap.pandian;
//        s14.tv = "发货通知生成调拨单";
//        items.add(s14);
//        SettingList s15= new SettingList();
//        s15.ImageResourse = R.mipmap.pandian;
//        s15.tv = "产品入库验货";
//        items.add(s15);
        return items;


    }


    //二期单据
    public static ArrayList<SettingList> getPurchaseList4P2(String[] ary) {
        ArrayList<SettingList> items = new ArrayList<>();
        ArrayList<SettingList> backItems = new ArrayList<>();

        items.add(new SettingList("1",R.mipmap.chuku,"扫描查询"));
        items.add(new SettingList("2",R.mipmap.chuku,"销售出库"));
        items.add(new SettingList("3",R.mipmap.chuku,"销售出库红字"));
        items.add(new SettingList("4",R.mipmap.diaobo,"查询条码记录"));
        items.add(new SettingList("5",R.mipmap.pandian,"盘点单"));
        items.add(new SettingList("6",R.mipmap.pandian,"库存查询"));
        items.add(new SettingList("7",R.mipmap.pandian,"销售出库分类汇总"));

        for (int i=0; i<items.size();i++){
//            Log.e("test","定位ary:"+ary[i]);
//            Log.e("test","定位items:"+items.get(i).tag);
            //根据ary的值，遍历list符合的item并添加
            for (int j=0;j<ary.length;j++){
                if (TextUtils.equals(items.get(i).tag,ary[j])){
                    Lg.e("加入单据",items.get(i));
                    backItems.add(items.get(i));
                }
            }
        }
        return backItems;


    }
}
