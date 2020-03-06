package com.fangzuo.assist.Beans;

import com.fangzuo.assist.Utils.Info;

public class SendOrderQuery extends BasePush{
    public String page_no;
    public String page_size;
    public String start_create;
    public String end_create;
    public String start_delivery_date;
    public String end_delivery_date;
    public String start_modify_date;
    public String end_modify_date;
    public String del;
    public String code;
    public String warehouse_code;
    public String shop_code;
    public String outer_code;
    public String print;
    public String scan;
    public String delivery;
    public String cod;
    public String vip_name;
    public String wms;
    public String mail_no;
    public String sync_status;




    public SendOrderQuery() {

    }
    public void setBase(String code,String url){
        appkey= Info.AppKey;
        sessionkey = Info.SessionKey;
        method =url;

        this.page_no = "1";
        this.page_size = "50";
        this.delivery="0";
        this.mail_no = code;
    }
    public void setBase2(String code,String url){
        appkey= Info.AppKey;
        sessionkey = Info.SessionKey;
        method =url;

        this.page_no = "1";
        this.page_size = "50";
        this.delivery="0";
        this.code = code;
    }
    public void setSign(String sn){
        sign = sn;
    }

}
