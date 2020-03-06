package com.fangzuo.assist.Beans;

public class OrderQuery extends BasePush {
    public String page_no;
    public String page_size;
    public String start_date;
    public String end_date;
    public String date_type;
    public String order_state;
    public String warehouse_code;
    public String shop_code;
    public String vip_name;
    public String platform_code;
    public String receiver_mobile;
    public String code;
    public boolean has_cancel_data;




    public OrderQuery() {

    }

    public void setData(){
        this.page_no = "1";
        this.page_size = "50";
        this.start_date = "";
        this.end_date = "";
        this.date_type = "0";
        this.order_state = "0";
        this.warehouse_code = "";
        this.shop_code = "";
        this.vip_name = "";
        this.platform_code = "";
        this.receiver_mobile = "";
        this.code = "";
        this.has_cancel_data = false;
    }
    public OrderQuery(String page_no, String page_size, String start_date, String end_date, String date_type, String order_state, String warehouse_code) {
        this.page_no = page_no;
        this.page_size = page_size;
        this.start_date = start_date;
        this.end_date = end_date;
        this.date_type = date_type;
        this.order_state = order_state;
        this.warehouse_code = warehouse_code;
    }

}
