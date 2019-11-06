package com.fangzuo.assist.Beans;

import java.util.List;

public class SendOrderResult {


    /**
     * success : true
     * errorCode :
     * subErrorCode :
     * errorDesc :
     * subErrorDesc :
     * requestMethod : gy.erp.trade.deliverys.get
     * deliverys : [{"create_date":"2019-06-05 16:40:03","modify_date":"2019-06-05 18:53:02","code":"SDO128884548931","qty":1,"amount":0,"payment":0,"pay_time":null,"cod":false,"refund":0,"invoiceDate":null,"bigchar":null,"cancel":0,"picture_bill":null,"platform_code":"20190605001","unpaid_amount":0,"post_fee":0,"cod_fee":0,"discount_fee":0,"post_cost":0,"plan_delivery_date":null,"buyer_memo":null,"seller_memo":null,"receiver_name":"张三","receiver_phone":"1000","receiver_mobile":"100000","receiver_zip":"100010","receiver_address":"5","create_name":"tangyi","express_no":"G00301939491","vip_name":"张三","shop_name":"小宇专卖店","area_name":"北京-北京市-东城区","warehouse_name":"式颜测试总仓","express_code":"YTO","express_name":"圆通速递","tag_name":"","seller_memo_late":null,"shelf_no":null,"details":[{"qty":1,"price":0,"amount":0,"refund":0,"memo":"","picUrl":null,"oid":null,"trade_code":"SO128884410162","origin_price":0,"item_id":"12883259694","item_sku_id":null,"item_code":"KFJ888","item_name":"咖啡机888","sku_code":null,"sku_name":null,"sku_note":null,"combine_item_code":null,"platform_code":"20190605001","discount_fee":0,"amount_after":0,"post_fee":0,"post_cost":0,"tax_rate":0,"tax_amount":0,"order_type":"Sales","platform_flag":1,"detail_unique":null,"detail_batch":[],"is_gift":0,"businessman_name":"huly","item_add_attribute":0,"other_service_fee":0,"gift_source_view":null,"currency_code":null,"currency_name":null,"total_cost_price":0}],"delivery_statusInfo":{"scan":false,"weight":false,"wms":0,"delivery":0,"cancel":false,"intercept":false,"print_express":true,"express_print_name":"admin","express_print_date":"2019-06-05 18:48:41","print_delivery":true,"delivery_print_name":null,"delivery_print_date":null,"scan_name":null,"scan_date":null,"weight_name":null,"weight_date":null,"wms_order":0,"delivery_name":null,"delivery_date":null,"cancel_name":null,"cancel_date":null,"weight_qty":"0.0","thermal_print":2,"thermal_print_status":0,"picking_user":null,"picking_date":null,"standard_weight":0,"pick_finish":false},"invoices":[],"vip_code":"01","warehouse_code":"SY001","shop_code":"zzy","vip_real_name":"张三","vip_id_card":"","package_center_code":null,"package_center_name":null,"sync_status":0,"sync_memo":null,"drp_tenant_name":null,"drp_tenant_mobile":null}]
     * total : 1
     */

    private boolean success;
    private String errorCode;
    private String subErrorCode;
    private String errorDesc;
    private String subErrorDesc;
    private String requestMethod;
    private int total;
    private List<DeliverysBean> deliverys;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getSubErrorCode() {
        return subErrorCode;
    }

    public void setSubErrorCode(String subErrorCode) {
        this.subErrorCode = subErrorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getSubErrorDesc() {
        return subErrorDesc;
    }

    public void setSubErrorDesc(String subErrorDesc) {
        this.subErrorDesc = subErrorDesc;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DeliverysBean> getDeliverys() {
        return deliverys;
    }

    public void setDeliverys(List<DeliverysBean> deliverys) {
        this.deliverys = deliverys;
    }

    public static class DeliverysBean {
        /**
         * create_date : 2019-06-05 16:40:03
         * modify_date : 2019-06-05 18:53:02
         * code : SDO128884548931
         * qty : 1.0
         * amount : 0.0
         * payment : 0.0
         * pay_time : null
         * cod : false
         * refund : 0
         * invoiceDate : null
         * bigchar : null
         * cancel : 0
         * picture_bill : null
         * platform_code : 20190605001
         * unpaid_amount : 0.0
         * post_fee : 0.0
         * cod_fee : 0.0
         * discount_fee : 0.0
         * post_cost : 0.0
         * plan_delivery_date : null
         * buyer_memo : null
         * seller_memo : null
         * receiver_name : 张三
         * receiver_phone : 1000
         * receiver_mobile : 100000
         * receiver_zip : 100010
         * receiver_address : 5
         * create_name : tangyi
         * express_no : G00301939491
         * vip_name : 张三
         * shop_name : 小宇专卖店
         * area_name : 北京-北京市-东城区
         * warehouse_name : 式颜测试总仓
         * express_code : YTO
         * express_name : 圆通速递
         * tag_name :
         * seller_memo_late : null
         * shelf_no : null
         * details : [{"qty":1,"price":0,"amount":0,"refund":0,"memo":"","picUrl":null,"oid":null,"trade_code":"SO128884410162","origin_price":0,"item_id":"12883259694","item_sku_id":null,"item_code":"KFJ888","item_name":"咖啡机888","sku_code":null,"sku_name":null,"sku_note":null,"combine_item_code":null,"platform_code":"20190605001","discount_fee":0,"amount_after":0,"post_fee":0,"post_cost":0,"tax_rate":0,"tax_amount":0,"order_type":"Sales","platform_flag":1,"detail_unique":null,"detail_batch":[],"is_gift":0,"businessman_name":"huly","item_add_attribute":0,"other_service_fee":0,"gift_source_view":null,"currency_code":null,"currency_name":null,"total_cost_price":0}]
         * delivery_statusInfo : {"scan":false,"weight":false,"wms":0,"delivery":0,"cancel":false,"intercept":false,"print_express":true,"express_print_name":"admin","express_print_date":"2019-06-05 18:48:41","print_delivery":true,"delivery_print_name":null,"delivery_print_date":null,"scan_name":null,"scan_date":null,"weight_name":null,"weight_date":null,"wms_order":0,"delivery_name":null,"delivery_date":null,"cancel_name":null,"cancel_date":null,"weight_qty":"0.0","thermal_print":2,"thermal_print_status":0,"picking_user":null,"picking_date":null,"standard_weight":0,"pick_finish":false}
         * invoices : []
         * vip_code : 01
         * warehouse_code : SY001
         * shop_code : zzy
         * vip_real_name : 张三
         * vip_id_card :
         * package_center_code : null
         * package_center_name : null
         * sync_status : 0
         * sync_memo : null
         * drp_tenant_name : null
         * drp_tenant_mobile : null
         */

        private String create_date;
        private String modify_date;
        private String code;
        private double qty;
        private double amount;
        private double payment;
        private Object pay_time;
        private boolean cod;
        private int refund;
        private String invoiceDate;
        private String bigchar;
        private int cancel;
        private boolean picture_bill;
        private String platform_code;
        private double unpaid_amount;
        private double post_fee;
        private double cod_fee;
        private double discount_fee;
        private double post_cost;
        private String plan_delivery_date;
        private String buyer_memo;
        private String seller_memo;
        private String receiver_name;
        private String receiver_phone;
        private String receiver_mobile;
        private String receiver_zip;
        private String receiver_address;
        private String create_name;
        private String express_no;
        private String vip_name;
        private String shop_name;
        private String area_name;
        private String warehouse_name;
        private String express_code;
        private String express_name;
        private String tag_name;
        private String seller_memo_late;
        private String shelf_no;
        private DeliveryStatusInfoBean delivery_statusInfo;
        private String vip_code;
        private String warehouse_code;
        private String shop_code;
        private String vip_real_name;
        private String vip_id_card;
        private String package_center_code;
        private String package_center_name;
        private int sync_status;
        private String sync_memo;
        private Object drp_tenant_name;
        private Object drp_tenant_mobile;
        private List<DetailsBean> details;
        private List<?> invoices;

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public String getModify_date() {
            return modify_date;
        }

        public void setModify_date(String modify_date) {
            this.modify_date = modify_date;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public double getQty() {
            return qty;
        }

        public void setQty(double qty) {
            this.qty = qty;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getPayment() {
            return payment;
        }

        public void setPayment(double payment) {
            this.payment = payment;
        }

        public Object getPay_time() {
            return pay_time;
        }

        public void setPay_time(Object pay_time) {
            this.pay_time = pay_time;
        }

        public boolean isCod() {
            return cod;
        }

        public void setCod(boolean cod) {
            this.cod = cod;
        }

        public int getRefund() {
            return refund;
        }

        public void setRefund(int refund) {
            this.refund = refund;
        }

        public Object getInvoiceDate() {
            return invoiceDate;
        }

        public void setInvoiceDate(String invoiceDate) {
            this.invoiceDate = invoiceDate;
        }

        public Object getBigchar() {
            return bigchar;
        }

        public void setBigchar(String bigchar) {
            this.bigchar = bigchar;
        }

        public int getCancel() {
            return cancel;
        }

        public void setCancel(int cancel) {
            this.cancel = cancel;
        }

        public Object getPicture_bill() {
            return picture_bill;
        }

        public void setPicture_bill(boolean picture_bill) {
            this.picture_bill = picture_bill;
        }

        public String getPlatform_code() {
            return platform_code;
        }

        public void setPlatform_code(String platform_code) {
            this.platform_code = platform_code;
        }

        public double getUnpaid_amount() {
            return unpaid_amount;
        }

        public void setUnpaid_amount(double unpaid_amount) {
            this.unpaid_amount = unpaid_amount;
        }

        public double getPost_fee() {
            return post_fee;
        }

        public void setPost_fee(double post_fee) {
            this.post_fee = post_fee;
        }

        public double getCod_fee() {
            return cod_fee;
        }

        public void setCod_fee(double cod_fee) {
            this.cod_fee = cod_fee;
        }

        public double getDiscount_fee() {
            return discount_fee;
        }

        public void setDiscount_fee(double discount_fee) {
            this.discount_fee = discount_fee;
        }

        public double getPost_cost() {
            return post_cost;
        }

        public void setPost_cost(double post_cost) {
            this.post_cost = post_cost;
        }

        public Object getPlan_delivery_date() {
            return plan_delivery_date;
        }

        public void setPlan_delivery_date(String plan_delivery_date) {
            this.plan_delivery_date = plan_delivery_date;
        }

        public Object getBuyer_memo() {
            return buyer_memo;
        }

        public void setBuyer_memo(String buyer_memo) {
            this.buyer_memo = buyer_memo;
        }

        public Object getSeller_memo() {
            return seller_memo;
        }

        public void setSeller_memo(String seller_memo) {
            this.seller_memo = seller_memo;
        }

        public String getReceiver_name() {
            return receiver_name;
        }

        public void setReceiver_name(String receiver_name) {
            this.receiver_name = receiver_name;
        }

        public String getReceiver_phone() {
            return receiver_phone;
        }

        public void setReceiver_phone(String receiver_phone) {
            this.receiver_phone = receiver_phone;
        }

        public String getReceiver_mobile() {
            return receiver_mobile;
        }

        public void setReceiver_mobile(String receiver_mobile) {
            this.receiver_mobile = receiver_mobile;
        }

        public String getReceiver_zip() {
            return receiver_zip;
        }

        public void setReceiver_zip(String receiver_zip) {
            this.receiver_zip = receiver_zip;
        }

        public String getReceiver_address() {
            return receiver_address;
        }

        public void setReceiver_address(String receiver_address) {
            this.receiver_address = receiver_address;
        }

        public String getCreate_name() {
            return create_name;
        }

        public void setCreate_name(String create_name) {
            this.create_name = create_name;
        }

        public String getExpress_no() {
            return express_no;
        }

        public void setExpress_no(String express_no) {
            this.express_no = express_no;
        }

        public String getVip_name() {
            return vip_name;
        }

        public void setVip_name(String vip_name) {
            this.vip_name = vip_name;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public String getWarehouse_name() {
            return warehouse_name;
        }

        public void setWarehouse_name(String warehouse_name) {
            this.warehouse_name = warehouse_name;
        }

        public String getExpress_code() {
            return express_code;
        }

        public void setExpress_code(String express_code) {
            this.express_code = express_code;
        }

        public String getExpress_name() {
            return express_name;
        }

        public void setExpress_name(String express_name) {
            this.express_name = express_name;
        }

        public String getTag_name() {
            return tag_name;
        }

        public void setTag_name(String tag_name) {
            this.tag_name = tag_name;
        }

        public Object getSeller_memo_late() {
            return seller_memo_late;
        }

        public void setSeller_memo_late(String seller_memo_late) {
            this.seller_memo_late = seller_memo_late;
        }

        public Object getShelf_no() {
            return shelf_no;
        }

        public void setShelf_no(String shelf_no) {
            this.shelf_no = shelf_no;
        }

        public DeliveryStatusInfoBean getDelivery_statusInfo() {
            return delivery_statusInfo;
        }

        public void setDelivery_statusInfo(DeliveryStatusInfoBean delivery_statusInfo) {
            this.delivery_statusInfo = delivery_statusInfo;
        }

        public String getVip_code() {
            return vip_code;
        }

        public void setVip_code(String vip_code) {
            this.vip_code = vip_code;
        }

        public String getWarehouse_code() {
            return warehouse_code;
        }

        public void setWarehouse_code(String warehouse_code) {
            this.warehouse_code = warehouse_code;
        }

        public String getShop_code() {
            return shop_code;
        }

        public void setShop_code(String shop_code) {
            this.shop_code = shop_code;
        }

        public String getVip_real_name() {
            return vip_real_name;
        }

        public void setVip_real_name(String vip_real_name) {
            this.vip_real_name = vip_real_name;
        }

        public String getVip_id_card() {
            return vip_id_card;
        }

        public void setVip_id_card(String vip_id_card) {
            this.vip_id_card = vip_id_card;
        }

        public Object getPackage_center_code() {
            return package_center_code;
        }

        public void setPackage_center_code(String package_center_code) {
            this.package_center_code = package_center_code;
        }

        public Object getPackage_center_name() {
            return package_center_name;
        }

        public void setPackage_center_name(String package_center_name) {
            this.package_center_name = package_center_name;
        }

        public int getSync_status() {
            return sync_status;
        }

        public void setSync_status(int sync_status) {
            this.sync_status = sync_status;
        }

        public Object getSync_memo() {
            return sync_memo;
        }

        public void setSync_memo(String sync_memo) {
            this.sync_memo = sync_memo;
        }

        public Object getDrp_tenant_name() {
            return drp_tenant_name;
        }

        public void setDrp_tenant_name(Object drp_tenant_name) {
            this.drp_tenant_name = drp_tenant_name;
        }

        public Object getDrp_tenant_mobile() {
            return drp_tenant_mobile;
        }

        public void setDrp_tenant_mobile(Object drp_tenant_mobile) {
            this.drp_tenant_mobile = drp_tenant_mobile;
        }

        public List<DetailsBean> getDetails() {
            return details;
        }

        public void setDetails(List<DetailsBean> details) {
            this.details = details;
        }

        public List<?> getInvoices() {
            return invoices;
        }

        public void setInvoices(List<?> invoices) {
            this.invoices = invoices;
        }

        public static class DeliveryStatusInfoBean {
            /**
             * scan : false
             * weight : false
             * wms : 0
             * delivery : 0
             * cancel : false
             * intercept : false
             * print_express : true
             * express_print_name : admin
             * express_print_date : 2019-06-05 18:48:41
             * print_delivery : true
             * delivery_print_name : null
             * delivery_print_date : null
             * scan_name : null
             * scan_date : null
             * weight_name : null
             * weight_date : null
             * wms_order : 0
             * delivery_name : null
             * delivery_date : null
             * cancel_name : null
             * cancel_date : null
             * weight_qty : 0.0
             * thermal_print : 2
             * thermal_print_status : 0
             * picking_user : null
             * picking_date : null
             * standard_weight : 0.0
             * pick_finish : false
             */

            private boolean scan;
            private boolean weight;
            private int wms;
            private int delivery;
            private boolean cancel;
            private boolean intercept;
            private boolean print_express;
            private String express_print_name;
            private String express_print_date;
            private boolean print_delivery;
            private Object delivery_print_name;
            private Object delivery_print_date;
            private Object scan_name;
            private Object scan_date;
            private Object weight_name;
            private Object weight_date;
            private int wms_order;
            private Object delivery_name;
            private Object delivery_date;
            private Object cancel_name;
            private Object cancel_date;
            private String weight_qty;
            private int thermal_print;
            private int thermal_print_status;
            private Object picking_user;
            private Object picking_date;
            private double standard_weight;
            private boolean pick_finish;

            public boolean isScan() {
                return scan;
            }

            public void setScan(boolean scan) {
                this.scan = scan;
            }

            public boolean isWeight() {
                return weight;
            }

            public void setWeight(boolean weight) {
                this.weight = weight;
            }

            public int getWms() {
                return wms;
            }

            public void setWms(int wms) {
                this.wms = wms;
            }

            public int getDelivery() {
                return delivery;
            }

            public void setDelivery(int delivery) {
                this.delivery = delivery;
            }

            public boolean isCancel() {
                return cancel;
            }

            public void setCancel(boolean cancel) {
                this.cancel = cancel;
            }

            public boolean isIntercept() {
                return intercept;
            }

            public void setIntercept(boolean intercept) {
                this.intercept = intercept;
            }

            public boolean isPrint_express() {
                return print_express;
            }

            public void setPrint_express(boolean print_express) {
                this.print_express = print_express;
            }

            public String getExpress_print_name() {
                return express_print_name;
            }

            public void setExpress_print_name(String express_print_name) {
                this.express_print_name = express_print_name;
            }

            public String getExpress_print_date() {
                return express_print_date;
            }

            public void setExpress_print_date(String express_print_date) {
                this.express_print_date = express_print_date;
            }

            public boolean isPrint_delivery() {
                return print_delivery;
            }

            public void setPrint_delivery(boolean print_delivery) {
                this.print_delivery = print_delivery;
            }

            public Object getDelivery_print_name() {
                return delivery_print_name;
            }

            public void setDelivery_print_name(Object delivery_print_name) {
                this.delivery_print_name = delivery_print_name;
            }

            public Object getDelivery_print_date() {
                return delivery_print_date;
            }

            public void setDelivery_print_date(Object delivery_print_date) {
                this.delivery_print_date = delivery_print_date;
            }

            public Object getScan_name() {
                return scan_name;
            }

            public void setScan_name(Object scan_name) {
                this.scan_name = scan_name;
            }

            public Object getScan_date() {
                return scan_date;
            }

            public void setScan_date(Object scan_date) {
                this.scan_date = scan_date;
            }

            public Object getWeight_name() {
                return weight_name;
            }

            public void setWeight_name(Object weight_name) {
                this.weight_name = weight_name;
            }

            public Object getWeight_date() {
                return weight_date;
            }

            public void setWeight_date(Object weight_date) {
                this.weight_date = weight_date;
            }

            public int getWms_order() {
                return wms_order;
            }

            public void setWms_order(int wms_order) {
                this.wms_order = wms_order;
            }

            public Object getDelivery_name() {
                return delivery_name;
            }

            public void setDelivery_name(Object delivery_name) {
                this.delivery_name = delivery_name;
            }

            public Object getDelivery_date() {
                return delivery_date;
            }

            public void setDelivery_date(Object delivery_date) {
                this.delivery_date = delivery_date;
            }

            public Object getCancel_name() {
                return cancel_name;
            }

            public void setCancel_name(Object cancel_name) {
                this.cancel_name = cancel_name;
            }

            public Object getCancel_date() {
                return cancel_date;
            }

            public void setCancel_date(Object cancel_date) {
                this.cancel_date = cancel_date;
            }

            public String getWeight_qty() {
                return weight_qty;
            }

            public void setWeight_qty(String weight_qty) {
                this.weight_qty = weight_qty;
            }

            public int getThermal_print() {
                return thermal_print;
            }

            public void setThermal_print(int thermal_print) {
                this.thermal_print = thermal_print;
            }

            public int getThermal_print_status() {
                return thermal_print_status;
            }

            public void setThermal_print_status(int thermal_print_status) {
                this.thermal_print_status = thermal_print_status;
            }

            public Object getPicking_user() {
                return picking_user;
            }

            public void setPicking_user(Object picking_user) {
                this.picking_user = picking_user;
            }

            public Object getPicking_date() {
                return picking_date;
            }

            public void setPicking_date(Object picking_date) {
                this.picking_date = picking_date;
            }

            public double getStandard_weight() {
                return standard_weight;
            }

            public void setStandard_weight(double standard_weight) {
                this.standard_weight = standard_weight;
            }

            public boolean isPick_finish() {
                return pick_finish;
            }

            public void setPick_finish(boolean pick_finish) {
                this.pick_finish = pick_finish;
            }
        }

        public static class DetailsBean {
            /**
             * qty : 1.0
             * price : 0.0
             * amount : 0.0
             * refund : 0
             * memo :
             * picUrl : null
             * oid : null
             * trade_code : SO128884410162
             * origin_price : 0.0
             * item_id : 12883259694
             * item_sku_id : null
             * item_code : KFJ888
             * item_name : 咖啡机888
             * sku_code : null
             * sku_name : null
             * sku_note : null
             * combine_item_code : null
             * platform_code : 20190605001
             * discount_fee : 0.0
             * amount_after : 0.0
             * post_fee : 0.0
             * post_cost : 0.0
             * tax_rate : 0.0
             * tax_amount : 0.0
             * order_type : Sales
             * platform_flag : 1
             * detail_unique : null
             * detail_batch : []
             * is_gift : 0
             * businessman_name : huly
             * item_add_attribute : 0
             * other_service_fee : 0.0
             * gift_source_view : null
             * currency_code : null
             * currency_name : null
             * total_cost_price : 0.0
             */

            private double qty;
            private double price;
            private double amount;
            private int refund;
            private String memo;
            private String picUrl;
            private Object oid;
            private String trade_code;
            private double origin_price;
            private String item_id;
            private String item_sku_id;
            private String item_code;
            private String item_name;
            private String sku_code;
            private String sku_name;
            private String sku_note;
            private Object combine_item_code;
            private String platform_code;
            private double discount_fee;
            private double amount_after;
            private double post_fee;
            private double post_cost;
            private double tax_rate;
            private double tax_amount;
            private String order_type;
            private int platform_flag;
            private Object detail_unique;
            private int is_gift;
            private String businessman_name;
            private int item_add_attribute;
            private double other_service_fee;
            private String gift_source_view;
            private String currency_code;
            private String currency_name;
            private double total_cost_price;
            private List<?> detail_batch;

            public double getQty() {
                return qty;
            }

            public void setQty(double qty) {
                this.qty = qty;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public int getRefund() {
                return refund;
            }

            public void setRefund(int refund) {
                this.refund = refund;
            }

            public String getMemo() {
                return memo;
            }

            public void setMemo(String memo) {
                this.memo = memo;
            }

            public Object getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public Object getOid() {
                return oid;
            }

            public void setOid(Object oid) {
                this.oid = oid;
            }

            public String getTrade_code() {
                return trade_code;
            }

            public void setTrade_code(String trade_code) {
                this.trade_code = trade_code;
            }

            public double getOrigin_price() {
                return origin_price;
            }

            public void setOrigin_price(double origin_price) {
                this.origin_price = origin_price;
            }

            public String getItem_id() {
                return item_id;
            }

            public void setItem_id(String item_id) {
                this.item_id = item_id;
            }

            public Object getItem_sku_id() {
                return item_sku_id;
            }

            public void setItem_sku_id(String item_sku_id) {
                this.item_sku_id = item_sku_id;
            }

            public String getItem_code() {
                return item_code;
            }

            public void setItem_code(String item_code) {
                this.item_code = item_code;
            }

            public String getItem_name() {
                return item_name;
            }

            public void setItem_name(String item_name) {
                this.item_name = item_name;
            }

            public String getSku_code() {
                return sku_code;
            }

            public void setSku_code(String sku_code) {
                this.sku_code = sku_code;
            }

            public String getSku_name() {
                return sku_name;
            }

            public void setSku_name(String sku_name) {
                this.sku_name = sku_name;
            }

            public String getSku_note() {
                return sku_note;
            }

            public void setSku_note(String sku_note) {
                this.sku_note = sku_note;
            }

            public Object getCombine_item_code() {
                return combine_item_code;
            }

            public void setCombine_item_code(Object combine_item_code) {
                this.combine_item_code = combine_item_code;
            }

            public String getPlatform_code() {
                return platform_code;
            }

            public void setPlatform_code(String platform_code) {
                this.platform_code = platform_code;
            }

            public double getDiscount_fee() {
                return discount_fee;
            }

            public void setDiscount_fee(double discount_fee) {
                this.discount_fee = discount_fee;
            }

            public double getAmount_after() {
                return amount_after;
            }

            public void setAmount_after(double amount_after) {
                this.amount_after = amount_after;
            }

            public double getPost_fee() {
                return post_fee;
            }

            public void setPost_fee(double post_fee) {
                this.post_fee = post_fee;
            }

            public double getPost_cost() {
                return post_cost;
            }

            public void setPost_cost(double post_cost) {
                this.post_cost = post_cost;
            }

            public double getTax_rate() {
                return tax_rate;
            }

            public void setTax_rate(double tax_rate) {
                this.tax_rate = tax_rate;
            }

            public double getTax_amount() {
                return tax_amount;
            }

            public void setTax_amount(double tax_amount) {
                this.tax_amount = tax_amount;
            }

            public String getOrder_type() {
                return order_type;
            }

            public void setOrder_type(String order_type) {
                this.order_type = order_type;
            }

            public int getPlatform_flag() {
                return platform_flag;
            }

            public void setPlatform_flag(int platform_flag) {
                this.platform_flag = platform_flag;
            }

            public Object getDetail_unique() {
                return detail_unique;
            }

            public void setDetail_unique(Object detail_unique) {
                this.detail_unique = detail_unique;
            }

            public int getIs_gift() {
                return is_gift;
            }

            public void setIs_gift(int is_gift) {
                this.is_gift = is_gift;
            }

            public String getBusinessman_name() {
                return businessman_name;
            }

            public void setBusinessman_name(String businessman_name) {
                this.businessman_name = businessman_name;
            }

            public int getItem_add_attribute() {
                return item_add_attribute;
            }

            public void setItem_add_attribute(int item_add_attribute) {
                this.item_add_attribute = item_add_attribute;
            }

            public double getOther_service_fee() {
                return other_service_fee;
            }

            public void setOther_service_fee(double other_service_fee) {
                this.other_service_fee = other_service_fee;
            }

            public Object getGift_source_view() {
                return gift_source_view;
            }

            public void setGift_source_view(String gift_source_view) {
                this.gift_source_view = gift_source_view;
            }

            public Object getCurrency_code() {
                return currency_code;
            }

            public void setCurrency_code(String currency_code) {
                this.currency_code = currency_code;
            }

            public Object getCurrency_name() {
                return currency_name;
            }

            public void setCurrency_name(String currency_name) {
                this.currency_name = currency_name;
            }

            public double getTotal_cost_price() {
                return total_cost_price;
            }

            public void setTotal_cost_price(double total_cost_price) {
                this.total_cost_price = total_cost_price;
            }

            public List<?> getDetail_batch() {
                return detail_batch;
            }

            public void setDetail_batch(List<?> detail_batch) {
                this.detail_batch = detail_batch;
            }
        }
    }
}
