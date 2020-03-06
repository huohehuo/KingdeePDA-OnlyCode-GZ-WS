package com.fangzuo.assist.Beans;

import java.util.List;

public class BackOrderResult {


    /**
     * success : true
     * errorCode :
     * subErrorCode :
     * errorDesc :
     * subErrorDesc :
     * requestMethod : gy.erp.trade.return.get
     * tradeReturns : [{"code":"RGO140550279065","reason":"","note":"测试使用 稍后删除","create_date":"2019-07-30 12:13:59","approve_date":null,"approve":0,"receive_date":null,"cancel":false,"shop_name":"BOOX旗舰店","shop_code":"C00297","platform_code":"547005061186613396","vip_code":"welovethd@hotmail.com","warehousein_code":"922","warehouseout_code":"919","express_code":null,"express_num":"","receiver_phone":"","details":[{"id":140550279068,"qty":5,"price":0,"amount":0,"note":"","oid":null,"sku_code":"","item_sku_id":"","sku_note":"","item_name":"Felt笔尖(黑色) 黑色","amount_after":0,"item_code":"OPL.OPL1035R","item_id":"110943102885","real_in":0,"discount_fee":0,"post_fee":0,"is_gift":0,"detail_batch":null,"detail_unique":null,"combine_item_code":"OPL.OPL1035R","other_service_fee":0,"total_cost_price":20},{"id":140550278199,"qty":1,"price":79,"amount":79,"note":"","oid":null,"sku_code":"","item_sku_id":"","sku_note":"","item_name":"BOOX笔尖盒","amount_after":79,"item_code":"OCS.OCS0507R","item_id":"110943099278","real_in":0,"discount_fee":0,"post_fee":0,"is_gift":0,"detail_batch":null,"detail_unique":null,"combine_item_code":"OCS.OCS0507R","other_service_fee":0,"total_cost_price":5},{"id":140550279069,"qty":1,"price":0,"amount":0,"note":"","oid":null,"sku_code":"","item_sku_id":"","sku_note":"","item_name":"笔尖更换夹具","amount_after":0,"item_code":"OMD.OMD0070R","item_id":"110943102879","real_in":0,"discount_fee":0,"post_fee":0,"is_gift":0,"detail_batch":null,"detail_unique":null,"combine_item_code":"OMD.OMD0070R","other_service_fee":0,"total_cost_price":4}],"payments":[],"return_type":null,"receive":"0","agree_refuse":null,"order_code":"SO140501457474","modify_date":"2019-07-30 12:14:21","business_man":"管理员","receiver_name":"陆均暐","receiver_mobile":"18918082778","receiver_zip":"200120","receiver_address":"上海 上海市 浦东新区 洋泾街道苗圃路218弄1号801室","area_name":"上海-上海市-浦东新区","platform_refund_id":"","sanwu_package":false,"refund_type":1,"refund_phase":null,"express_name":"","drp_tenant_name":null,"drp_tenant_mobile":null,"cancel_date":null,"warehousein_name":"塘朗电商退货仓","vip_name":"jefferylu","platform_status":null,"refund_codes":[]}]
     * total : 1
     */

    private boolean success;
    private String errorCode;
    private String subErrorCode;
    private String errorDesc;
    private String subErrorDesc;
    private String requestMethod;
    private int total;
    private List<TradeReturnsBean> tradeReturns;

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

    public List<TradeReturnsBean> getTradeReturns() {
        return tradeReturns;
    }

    public void setTradeReturns(List<TradeReturnsBean> tradeReturns) {
        this.tradeReturns = tradeReturns;
    }

    public static class TradeReturnsBean {
        /**
         * code : RGO140550279065
         * reason :
         * note : 测试使用 稍后删除
         * create_date : 2019-07-30 12:13:59
         * approve_date : null
         * approve : 0
         * receive_date : null
         * cancel : false
         * shop_name : BOOX旗舰店
         * shop_code : C00297
         * platform_code : 547005061186613396
         * vip_code : welovethd@hotmail.com
         * warehousein_code : 922
         * warehouseout_code : 919
         * express_code : null
         * express_num :
         * receiver_phone :
         * details : [{"id":140550279068,"qty":5,"price":0,"amount":0,"note":"","oid":null,"sku_code":"","item_sku_id":"","sku_note":"","item_name":"Felt笔尖(黑色) 黑色","amount_after":0,"item_code":"OPL.OPL1035R","item_id":"110943102885","real_in":0,"discount_fee":0,"post_fee":0,"is_gift":0,"detail_batch":null,"detail_unique":null,"combine_item_code":"OPL.OPL1035R","other_service_fee":0,"total_cost_price":20},{"id":140550278199,"qty":1,"price":79,"amount":79,"note":"","oid":null,"sku_code":"","item_sku_id":"","sku_note":"","item_name":"BOOX笔尖盒","amount_after":79,"item_code":"OCS.OCS0507R","item_id":"110943099278","real_in":0,"discount_fee":0,"post_fee":0,"is_gift":0,"detail_batch":null,"detail_unique":null,"combine_item_code":"OCS.OCS0507R","other_service_fee":0,"total_cost_price":5},{"id":140550279069,"qty":1,"price":0,"amount":0,"note":"","oid":null,"sku_code":"","item_sku_id":"","sku_note":"","item_name":"笔尖更换夹具","amount_after":0,"item_code":"OMD.OMD0070R","item_id":"110943102879","real_in":0,"discount_fee":0,"post_fee":0,"is_gift":0,"detail_batch":null,"detail_unique":null,"combine_item_code":"OMD.OMD0070R","other_service_fee":0,"total_cost_price":4}]
         * payments : []
         * return_type : null
         * receive : 0
         * agree_refuse : null
         * order_code : SO140501457474
         * modify_date : 2019-07-30 12:14:21
         * business_man : 管理员
         * receiver_name : 陆均暐
         * receiver_mobile : 18918082778
         * receiver_zip : 200120
         * receiver_address : 上海 上海市 浦东新区 洋泾街道苗圃路218弄1号801室
         * area_name : 上海-上海市-浦东新区
         * platform_refund_id :
         * sanwu_package : false
         * refund_type : 1
         * refund_phase : null
         * express_name :
         * drp_tenant_name : null
         * drp_tenant_mobile : null
         * cancel_date : null
         * warehousein_name : 塘朗电商退货仓
         * vip_name : jefferylu
         * platform_status : null
         * refund_codes : []
         */

        private String code;
        private String reason;
        private String note;
        private String create_date;
        private Object approve_date;
        private int approve;
        private Object receive_date;
        private boolean cancel;
        private String shop_name;
        private String shop_code;
        private String platform_code;
        private String vip_code;
        private String warehousein_code;
        private String warehouseout_code;
        private Object express_code;
        private String express_num;
        private String receiver_phone;
        private Object return_type;
        private String receive;
        private Object agree_refuse;
        private String order_code;
        private String modify_date;
        private String business_man;
        private String receiver_name;
        private String receiver_mobile;
        private String receiver_zip;
        private String receiver_address;
        private String area_name;
        private String platform_refund_id;
        private boolean sanwu_package;
        private int refund_type;
        private Object refund_phase;
        private String express_name;
        private Object drp_tenant_name;
        private Object drp_tenant_mobile;
        private Object cancel_date;
        private String warehousein_name;
        private String vip_name;
        private Object platform_status;
        private List<DetailsBean> details;
        private List<?> payments;
        private List<?> refund_codes;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public Object getApprove_date() {
            return approve_date;
        }

        public void setApprove_date(Object approve_date) {
            this.approve_date = approve_date;
        }

        public int getApprove() {
            return approve;
        }

        public void setApprove(int approve) {
            this.approve = approve;
        }

        public Object getReceive_date() {
            return receive_date;
        }

        public void setReceive_date(Object receive_date) {
            this.receive_date = receive_date;
        }

        public boolean isCancel() {
            return cancel;
        }

        public void setCancel(boolean cancel) {
            this.cancel = cancel;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getShop_code() {
            return shop_code;
        }

        public void setShop_code(String shop_code) {
            this.shop_code = shop_code;
        }

        public String getPlatform_code() {
            return platform_code;
        }

        public void setPlatform_code(String platform_code) {
            this.platform_code = platform_code;
        }

        public String getVip_code() {
            return vip_code;
        }

        public void setVip_code(String vip_code) {
            this.vip_code = vip_code;
        }

        public String getWarehousein_code() {
            return warehousein_code;
        }

        public void setWarehousein_code(String warehousein_code) {
            this.warehousein_code = warehousein_code;
        }

        public String getWarehouseout_code() {
            return warehouseout_code;
        }

        public void setWarehouseout_code(String warehouseout_code) {
            this.warehouseout_code = warehouseout_code;
        }

        public Object getExpress_code() {
            return express_code;
        }

        public void setExpress_code(Object express_code) {
            this.express_code = express_code;
        }

        public String getExpress_num() {
            return express_num;
        }

        public void setExpress_num(String express_num) {
            this.express_num = express_num;
        }

        public String getReceiver_phone() {
            return receiver_phone;
        }

        public void setReceiver_phone(String receiver_phone) {
            this.receiver_phone = receiver_phone;
        }

        public Object getReturn_type() {
            return return_type;
        }

        public void setReturn_type(Object return_type) {
            this.return_type = return_type;
        }

        public String getReceive() {
            return receive;
        }

        public void setReceive(String receive) {
            this.receive = receive;
        }

        public Object getAgree_refuse() {
            return agree_refuse;
        }

        public void setAgree_refuse(Object agree_refuse) {
            this.agree_refuse = agree_refuse;
        }

        public String getOrder_code() {
            return order_code;
        }

        public void setOrder_code(String order_code) {
            this.order_code = order_code;
        }

        public String getModify_date() {
            return modify_date;
        }

        public void setModify_date(String modify_date) {
            this.modify_date = modify_date;
        }

        public String getBusiness_man() {
            return business_man;
        }

        public void setBusiness_man(String business_man) {
            this.business_man = business_man;
        }

        public String getReceiver_name() {
            return receiver_name;
        }

        public void setReceiver_name(String receiver_name) {
            this.receiver_name = receiver_name;
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

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public String getPlatform_refund_id() {
            return platform_refund_id;
        }

        public void setPlatform_refund_id(String platform_refund_id) {
            this.platform_refund_id = platform_refund_id;
        }

        public boolean isSanwu_package() {
            return sanwu_package;
        }

        public void setSanwu_package(boolean sanwu_package) {
            this.sanwu_package = sanwu_package;
        }

        public int getRefund_type() {
            return refund_type;
        }

        public void setRefund_type(int refund_type) {
            this.refund_type = refund_type;
        }

        public Object getRefund_phase() {
            return refund_phase;
        }

        public void setRefund_phase(Object refund_phase) {
            this.refund_phase = refund_phase;
        }

        public String getExpress_name() {
            return express_name;
        }

        public void setExpress_name(String express_name) {
            this.express_name = express_name;
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

        public Object getCancel_date() {
            return cancel_date;
        }

        public void setCancel_date(Object cancel_date) {
            this.cancel_date = cancel_date;
        }

        public String getWarehousein_name() {
            return warehousein_name;
        }

        public void setWarehousein_name(String warehousein_name) {
            this.warehousein_name = warehousein_name;
        }

        public String getVip_name() {
            return vip_name;
        }

        public void setVip_name(String vip_name) {
            this.vip_name = vip_name;
        }

        public Object getPlatform_status() {
            return platform_status;
        }

        public void setPlatform_status(Object platform_status) {
            this.platform_status = platform_status;
        }

        public List<DetailsBean> getDetails() {
            return details;
        }

        public void setDetails(List<DetailsBean> details) {
            this.details = details;
        }

        public List<?> getPayments() {
            return payments;
        }

        public void setPayments(List<?> payments) {
            this.payments = payments;
        }

        public List<?> getRefund_codes() {
            return refund_codes;
        }

        public void setRefund_codes(List<?> refund_codes) {
            this.refund_codes = refund_codes;
        }

        public static class DetailsBean {
            /**
             * id : 140550279068
             * qty : 5.0
             * price : 0.0
             * amount : 0.0
             * note :
             * oid : null
             * sku_code :
             * item_sku_id :
             * sku_note :
             * item_name : Felt笔尖(黑色) 黑色
             * amount_after : 0.0
             * item_code : OPL.OPL1035R
             * item_id : 110943102885
             * real_in : 0.0
             * discount_fee : 0.0
             * post_fee : 0.0
             * is_gift : 0
             * detail_batch : null
             * detail_unique : null
             * combine_item_code : OPL.OPL1035R
             * other_service_fee : 0.0
             * total_cost_price : 20.0
             */

            private long id;
            private double qty;
            private double price;
            private double amount;
            private String note;
            private Object oid;
            private String sku_code;
            private String item_sku_id;
            private String sku_note;
            private String item_name;
            private double amount_after;
            private String item_code;
            private String item_id;
            private double real_in;
            private double discount_fee;
            private double post_fee;
            private int is_gift;
            private Object detail_batch;
            private Object detail_unique;
            private String combine_item_code;
            private double other_service_fee;
            private double total_cost_price;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

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

            public String getNote() {
                return note;
            }

            public void setNote(String note) {
                this.note = note;
            }

            public Object getOid() {
                return oid;
            }

            public void setOid(Object oid) {
                this.oid = oid;
            }

            public String getSku_code() {
                return sku_code;
            }

            public void setSku_code(String sku_code) {
                this.sku_code = sku_code;
            }

            public String getItem_sku_id() {
                return item_sku_id;
            }

            public void setItem_sku_id(String item_sku_id) {
                this.item_sku_id = item_sku_id;
            }

            public String getSku_note() {
                return sku_note;
            }

            public void setSku_note(String sku_note) {
                this.sku_note = sku_note;
            }

            public String getItem_name() {
                return item_name;
            }

            public void setItem_name(String item_name) {
                this.item_name = item_name;
            }

            public double getAmount_after() {
                return amount_after;
            }

            public void setAmount_after(double amount_after) {
                this.amount_after = amount_after;
            }

            public String getItem_code() {
                return item_code;
            }

            public void setItem_code(String item_code) {
                this.item_code = item_code;
            }

            public String getItem_id() {
                return item_id;
            }

            public void setItem_id(String item_id) {
                this.item_id = item_id;
            }

            public double getReal_in() {
                return real_in;
            }

            public void setReal_in(double real_in) {
                this.real_in = real_in;
            }

            public double getDiscount_fee() {
                return discount_fee;
            }

            public void setDiscount_fee(double discount_fee) {
                this.discount_fee = discount_fee;
            }

            public double getPost_fee() {
                return post_fee;
            }

            public void setPost_fee(double post_fee) {
                this.post_fee = post_fee;
            }

            public int getIs_gift() {
                return is_gift;
            }

            public void setIs_gift(int is_gift) {
                this.is_gift = is_gift;
            }

            public Object getDetail_batch() {
                return detail_batch;
            }

            public void setDetail_batch(Object detail_batch) {
                this.detail_batch = detail_batch;
            }

            public Object getDetail_unique() {
                return detail_unique;
            }

            public void setDetail_unique(Object detail_unique) {
                this.detail_unique = detail_unique;
            }

            public String getCombine_item_code() {
                return combine_item_code;
            }

            public void setCombine_item_code(String combine_item_code) {
                this.combine_item_code = combine_item_code;
            }

            public double getOther_service_fee() {
                return other_service_fee;
            }

            public void setOther_service_fee(double other_service_fee) {
                this.other_service_fee = other_service_fee;
            }

            public double getTotal_cost_price() {
                return total_cost_price;
            }

            public void setTotal_cost_price(double total_cost_price) {
                this.total_cost_price = total_cost_price;
            }
        }
    }
}
