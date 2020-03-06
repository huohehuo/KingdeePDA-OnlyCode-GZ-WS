package com.fangzuo.assist.Beans;

import com.fangzuo.assist.Utils.Info;

public class BackOrderQuery extends BasePush{
    public String page_no;
    public String page_size;
    public String code;
    public String start_create;
    public String end_create;
    public String in_begin_time;
    public String in_end_time;
    public String shop_code;
    public String platform_code;
    public String return_type;
    public String express_no;
    public String vip_name;
    public String agree;
    public String receive;
    public String cancel;
    public String no_parcel;
    public String receiver_name;
    public String receiver_phone;
    public String warehousein_code;
    public String warehouseout_code;
    public String modify_start_date;
    public String modify_end_date;

    public BackOrderQuery() {

    }
    public void setBase(String code,String url){
        appkey= Info.AppKey;
        sessionkey = Info.SessionKey;
        method =url;

        this.page_no = "1";
        this.page_size = "50";
        this.code = code;
    }
    public void setBase2(String code,String url){
        appkey= Info.AppKey;
        sessionkey = Info.SessionKey;
        method =url;

        this.page_no = "1";
        this.page_size = "50";
        this.express_no = code;
    }


    public void setSign(String sn){
        sign = sn;
    }

}
