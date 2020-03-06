package com.fangzuo.assist.Beans;

import java.util.List;

public class OrderQueryResult{

    /**
     * success : true
     * errorCode :
     * subErrorCode :
     * errorDesc :
     * subErrorDesc :
     * requestMethod : gy.erp.trade.get
     * orders : [{"code":"SO21991541294","qty":1,"amount":200,"payment":180,"approve":true,"cod":false,"cancle":false,"vipIdCard":null,"vipEmail":null,"vipRealName":null,"accountStatus":"未到账","accountAmount":0,"assignState":2,"platform_code":"SH201608031033146760","createtime":"2016-08-03 10:33:22","modifytime":"2016-08-26 16:15:28","dealtime":"2016-08-03 10:33:14","paytime":"2016-08-03 10:33:22","shop_name":"康宝旗舰店","shop_code":"02","warehouse_name":"正品仓","warehouse_code":"100","express_name":"圆通速递","express_code":"YTO","vip_name":"张兴禹","vip_code":"6962","receiver_name":"效应于","receiver_phone":"15578320721","receiver_mobile":"15578320721","receiver_zip":"000000","receiver_address":"吉林省白山市江源县测试地址","receiver_area":null,"buyer_memo":"","seller_memo":null,"seller_memo_late":null,"post_fee":0,"cod_fee":0,"discount_fee":20,"post_cost":0,"weight_origin":0,"payment_amount":200,"delivery_state":0,"order_type_name":"销售订单","business_man":null,"hold_info":null,"platform_flag":0,"extend_memo":null,"tax_amount":0,"approveDate":"2016-08-03 10:48:22","accountDate":"","trade_tag_code":"","trade_tag_name":"","plan_delivery_date":"","details":[{"oid":null,"qty":1,"price":200,"amount":200,"refund":0,"note":"","item_code":"XERW1","item_name":"测试商品-满减","item_simple_name":"","sku_name":"Default","sku_code":"XERW1","post_fee":0,"discount_fee":20,"amount_after":180,"platform_item_name":"XERW1","platform_sku_name":"XERW1","sku_note":"默认属性"}],"payments":[{"payment":180,"payCode":"","pay_type_name":"预存款","paytime":"2016-08-03 10:33:22"}],"invoices":[{"address":null,"phone":null,"invoice_type_name":"增值专用发票","invoice_title":"","invoice_content":"","invoice_amount":180,"tex_payer_number":null,"bank_name":null,"bank_account":null,"invoice_type":"2","invoice_title_type":"1"}],"deliverys":[{"delivery":false,"code":"SDO23799674502","printExpress":true,"printDeliveryList":false,"scan":true,"weight":false,"warehouse_name":"正品仓","warehouse_code":"100","express_name":"圆通速递","express_code":"YTO","mail_no":"4356464564645"}],"platform_trading_state":"","substitut_order":0}]
     * total : 39394
     */

    private boolean success;
    private String errorCode;
    private String subErrorCode;
    private String errorDesc;
    private String subErrorDesc;
    private String requestMethod;
    private int total;
    private List<OrdersBean> orders;

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

    public List<OrdersBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersBean> orders) {
        this.orders = orders;
    }

    public static class OrdersBean {
        /**
         * code : SO21991541294
         * qty : 1.0
         * amount : 200.0
         * payment : 180.0
         * approve : true
         * cod : false
         * cancle : false
         * vipIdCard : null
         * vipEmail : null
         * vipRealName : null
         * accountStatus : 未到账
         * accountAmount : 0.0
         * assignState : 2
         * platform_code : SH201608031033146760
         * createtime : 2016-08-03 10:33:22
         * modifytime : 2016-08-26 16:15:28
         * dealtime : 2016-08-03 10:33:14
         * paytime : 2016-08-03 10:33:22
         * shop_name : 康宝旗舰店
         * shop_code : 02
         * warehouse_name : 正品仓
         * warehouse_code : 100
         * express_name : 圆通速递
         * express_code : YTO
         * vip_name : 张兴禹
         * vip_code : 6962
         * receiver_name : 效应于
         * receiver_phone : 15578320721
         * receiver_mobile : 15578320721
         * receiver_zip : 000000
         * receiver_address : 吉林省白山市江源县测试地址
         * receiver_area : null
         * buyer_memo :
         * seller_memo : null
         * seller_memo_late : null
         * post_fee : 0.0
         * cod_fee : 0.0
         * discount_fee : 20.0
         * post_cost : 0.0
         * weight_origin : 0.0
         * payment_amount : 200.0
         * delivery_state : 0
         * order_type_name : 销售订单
         * business_man : null
         * hold_info : null
         * platform_flag : 0
         * extend_memo : null
         * tax_amount : 0.0
         * approveDate : 2016-08-03 10:48:22
         * accountDate :
         * trade_tag_code :
         * trade_tag_name :
         * plan_delivery_date :
         * details : [{"oid":null,"qty":1,"price":200,"amount":200,"refund":0,"note":"","item_code":"XERW1","item_name":"测试商品-满减","item_simple_name":"","sku_name":"Default","sku_code":"XERW1","post_fee":0,"discount_fee":20,"amount_after":180,"platform_item_name":"XERW1","platform_sku_name":"XERW1","sku_note":"默认属性"}]
         * payments : [{"payment":180,"payCode":"","pay_type_name":"预存款","paytime":"2016-08-03 10:33:22"}]
         * invoices : [{"address":null,"phone":null,"invoice_type_name":"增值专用发票","invoice_title":"","invoice_content":"","invoice_amount":180,"tex_payer_number":null,"bank_name":null,"bank_account":null,"invoice_type":"2","invoice_title_type":"1"}]
         * deliverys : [{"delivery":false,"code":"SDO23799674502","printExpress":true,"printDeliveryList":false,"scan":true,"weight":false,"warehouse_name":"正品仓","warehouse_code":"100","express_name":"圆通速递","express_code":"YTO","mail_no":"4356464564645"}]
         * platform_trading_state :
         * substitut_order : 0
         */

        private String code;
        private double qty;
        private double amount;
        private double payment;
        private boolean approve;
        private boolean cod;
        private boolean cancle;
        private Object vipIdCard;
        private Object vipEmail;
        private Object vipRealName;
        private String accountStatus;
        private double accountAmount;
        private int assignState;
        private String platform_code;
        private String createtime;
        private String modifytime;
        private String dealtime;
        private String paytime;
        private String shop_name;
        private String shop_code;
        private String warehouse_name;
        private String warehouse_code;
        private String express_name;
        private String express_code;
        private String vip_name;
        private String vip_code;
        private String receiver_name;
        private String receiver_phone;
        private String receiver_mobile;
        private String receiver_zip;
        private String receiver_address;
        private Object receiver_area;
        private String buyer_memo;
        private Object seller_memo;
        private Object seller_memo_late;
        private double post_fee;
        private double cod_fee;
        private double discount_fee;
        private double post_cost;
        private double weight_origin;
        private double payment_amount;
        private int delivery_state;
        private String order_type_name;
        private Object business_man;
        private Object hold_info;
        private int platform_flag;
        private Object extend_memo;
        private double tax_amount;
        private String approveDate;
        private String accountDate;
        private String trade_tag_code;
        private String trade_tag_name;
        private String plan_delivery_date;
        private String platform_trading_state;
        private int substitut_order;
        private List<DetailsBean> details;
        private List<PaymentsBean> payments;
        private List<InvoicesBean> invoices;
        private List<DeliverysBean> deliverys;

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

        public boolean isApprove() {
            return approve;
        }

        public void setApprove(boolean approve) {
            this.approve = approve;
        }

        public boolean isCod() {
            return cod;
        }

        public void setCod(boolean cod) {
            this.cod = cod;
        }

        public boolean isCancle() {
            return cancle;
        }

        public void setCancle(boolean cancle) {
            this.cancle = cancle;
        }

        public Object getVipIdCard() {
            return vipIdCard;
        }

        public void setVipIdCard(Object vipIdCard) {
            this.vipIdCard = vipIdCard;
        }

        public Object getVipEmail() {
            return vipEmail;
        }

        public void setVipEmail(Object vipEmail) {
            this.vipEmail = vipEmail;
        }

        public Object getVipRealName() {
            return vipRealName;
        }

        public void setVipRealName(Object vipRealName) {
            this.vipRealName = vipRealName;
        }

        public String getAccountStatus() {
            return accountStatus;
        }

        public void setAccountStatus(String accountStatus) {
            this.accountStatus = accountStatus;
        }

        public double getAccountAmount() {
            return accountAmount;
        }

        public void setAccountAmount(double accountAmount) {
            this.accountAmount = accountAmount;
        }

        public int getAssignState() {
            return assignState;
        }

        public void setAssignState(int assignState) {
            this.assignState = assignState;
        }

        public String getPlatform_code() {
            return platform_code;
        }

        public void setPlatform_code(String platform_code) {
            this.platform_code = platform_code;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getModifytime() {
            return modifytime;
        }

        public void setModifytime(String modifytime) {
            this.modifytime = modifytime;
        }

        public String getDealtime() {
            return dealtime;
        }

        public void setDealtime(String dealtime) {
            this.dealtime = dealtime;
        }

        public String getPaytime() {
            return paytime;
        }

        public void setPaytime(String paytime) {
            this.paytime = paytime;
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

        public String getWarehouse_name() {
            return warehouse_name;
        }

        public void setWarehouse_name(String warehouse_name) {
            this.warehouse_name = warehouse_name;
        }

        public String getWarehouse_code() {
            return warehouse_code;
        }

        public void setWarehouse_code(String warehouse_code) {
            this.warehouse_code = warehouse_code;
        }

        public String getExpress_name() {
            return express_name;
        }

        public void setExpress_name(String express_name) {
            this.express_name = express_name;
        }

        public String getExpress_code() {
            return express_code;
        }

        public void setExpress_code(String express_code) {
            this.express_code = express_code;
        }

        public String getVip_name() {
            return vip_name;
        }

        public void setVip_name(String vip_name) {
            this.vip_name = vip_name;
        }

        public String getVip_code() {
            return vip_code;
        }

        public void setVip_code(String vip_code) {
            this.vip_code = vip_code;
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

        public Object getReceiver_area() {
            return receiver_area;
        }

        public void setReceiver_area(Object receiver_area) {
            this.receiver_area = receiver_area;
        }

        public String getBuyer_memo() {
            return buyer_memo;
        }

        public void setBuyer_memo(String buyer_memo) {
            this.buyer_memo = buyer_memo;
        }

        public Object getSeller_memo() {
            return seller_memo;
        }

        public void setSeller_memo(Object seller_memo) {
            this.seller_memo = seller_memo;
        }

        public Object getSeller_memo_late() {
            return seller_memo_late;
        }

        public void setSeller_memo_late(Object seller_memo_late) {
            this.seller_memo_late = seller_memo_late;
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

        public double getWeight_origin() {
            return weight_origin;
        }

        public void setWeight_origin(double weight_origin) {
            this.weight_origin = weight_origin;
        }

        public double getPayment_amount() {
            return payment_amount;
        }

        public void setPayment_amount(double payment_amount) {
            this.payment_amount = payment_amount;
        }

        public int getDelivery_state() {
            return delivery_state;
        }

        public void setDelivery_state(int delivery_state) {
            this.delivery_state = delivery_state;
        }

        public String getOrder_type_name() {
            return order_type_name;
        }

        public void setOrder_type_name(String order_type_name) {
            this.order_type_name = order_type_name;
        }

        public Object getBusiness_man() {
            return business_man;
        }

        public void setBusiness_man(Object business_man) {
            this.business_man = business_man;
        }

        public Object getHold_info() {
            return hold_info;
        }

        public void setHold_info(Object hold_info) {
            this.hold_info = hold_info;
        }

        public int getPlatform_flag() {
            return platform_flag;
        }

        public void setPlatform_flag(int platform_flag) {
            this.platform_flag = platform_flag;
        }

        public Object getExtend_memo() {
            return extend_memo;
        }

        public void setExtend_memo(Object extend_memo) {
            this.extend_memo = extend_memo;
        }

        public double getTax_amount() {
            return tax_amount;
        }

        public void setTax_amount(double tax_amount) {
            this.tax_amount = tax_amount;
        }

        public String getApproveDate() {
            return approveDate;
        }

        public void setApproveDate(String approveDate) {
            this.approveDate = approveDate;
        }

        public String getAccountDate() {
            return accountDate;
        }

        public void setAccountDate(String accountDate) {
            this.accountDate = accountDate;
        }

        public String getTrade_tag_code() {
            return trade_tag_code;
        }

        public void setTrade_tag_code(String trade_tag_code) {
            this.trade_tag_code = trade_tag_code;
        }

        public String getTrade_tag_name() {
            return trade_tag_name;
        }

        public void setTrade_tag_name(String trade_tag_name) {
            this.trade_tag_name = trade_tag_name;
        }

        public String getPlan_delivery_date() {
            return plan_delivery_date;
        }

        public void setPlan_delivery_date(String plan_delivery_date) {
            this.plan_delivery_date = plan_delivery_date;
        }

        public String getPlatform_trading_state() {
            return platform_trading_state;
        }

        public void setPlatform_trading_state(String platform_trading_state) {
            this.platform_trading_state = platform_trading_state;
        }

        public int getSubstitut_order() {
            return substitut_order;
        }

        public void setSubstitut_order(int substitut_order) {
            this.substitut_order = substitut_order;
        }

        public List<DetailsBean> getDetails() {
            return details;
        }

        public void setDetails(List<DetailsBean> details) {
            this.details = details;
        }

        public List<PaymentsBean> getPayments() {
            return payments;
        }

        public void setPayments(List<PaymentsBean> payments) {
            this.payments = payments;
        }

        public List<InvoicesBean> getInvoices() {
            return invoices;
        }

        public void setInvoices(List<InvoicesBean> invoices) {
            this.invoices = invoices;
        }

        public List<DeliverysBean> getDeliverys() {
            return deliverys;
        }

        public void setDeliverys(List<DeliverysBean> deliverys) {
            this.deliverys = deliverys;
        }

        public static class DetailsBean {
            /**
             * oid : null
             * qty : 1.0
             * price : 200.0
             * amount : 200.0
             * refund : 0
             * note :
             * item_code : XERW1
             * item_name : 测试商品-满减
             * item_simple_name :
             * sku_name : Default
             * sku_code : XERW1
             * post_fee : 0.0
             * discount_fee : 20.0
             * amount_after : 180.0
             * platform_item_name : XERW1
             * platform_sku_name : XERW1
             * sku_note : 默认属性
             */

            private Object oid;
            private double qty;
            private double price;
            private double amount;
            private int refund;
            private String note;
            private String item_code;
            private String item_name;
            private String item_simple_name;
            private String sku_name;
            private String sku_code;
            private double post_fee;
            private double discount_fee;
            private double amount_after;
            private String platform_item_name;
            private String platform_sku_name;
            private String sku_note;

            public Object getOid() {
                return oid;
            }

            public void setOid(Object oid) {
                this.oid = oid;
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

            public int getRefund() {
                return refund;
            }

            public void setRefund(int refund) {
                this.refund = refund;
            }

            public String getNote() {
                return note;
            }

            public void setNote(String note) {
                this.note = note;
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

            public String getItem_simple_name() {
                return item_simple_name;
            }

            public void setItem_simple_name(String item_simple_name) {
                this.item_simple_name = item_simple_name;
            }

            public String getSku_name() {
                return sku_name;
            }

            public void setSku_name(String sku_name) {
                this.sku_name = sku_name;
            }

            public String getSku_code() {
                return sku_code;
            }

            public void setSku_code(String sku_code) {
                this.sku_code = sku_code;
            }

            public double getPost_fee() {
                return post_fee;
            }

            public void setPost_fee(double post_fee) {
                this.post_fee = post_fee;
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

            public String getPlatform_item_name() {
                return platform_item_name;
            }

            public void setPlatform_item_name(String platform_item_name) {
                this.platform_item_name = platform_item_name;
            }

            public String getPlatform_sku_name() {
                return platform_sku_name;
            }

            public void setPlatform_sku_name(String platform_sku_name) {
                this.platform_sku_name = platform_sku_name;
            }

            public String getSku_note() {
                return sku_note;
            }

            public void setSku_note(String sku_note) {
                this.sku_note = sku_note;
            }
        }

        public static class PaymentsBean {
            /**
             * payment : 180.0
             * payCode :
             * pay_type_name : 预存款
             * paytime : 2016-08-03 10:33:22
             */

            private double payment;
            private String payCode;
            private String pay_type_name;
            private String paytime;

            public double getPayment() {
                return payment;
            }

            public void setPayment(double payment) {
                this.payment = payment;
            }

            public String getPayCode() {
                return payCode;
            }

            public void setPayCode(String payCode) {
                this.payCode = payCode;
            }

            public String getPay_type_name() {
                return pay_type_name;
            }

            public void setPay_type_name(String pay_type_name) {
                this.pay_type_name = pay_type_name;
            }

            public String getPaytime() {
                return paytime;
            }

            public void setPaytime(String paytime) {
                this.paytime = paytime;
            }
        }

        public static class InvoicesBean {
            /**
             * address : null
             * phone : null
             * invoice_type_name : 增值专用发票
             * invoice_title :
             * invoice_content :
             * invoice_amount : 180.0
             * tex_payer_number : null
             * bank_name : null
             * bank_account : null
             * invoice_type : 2
             * invoice_title_type : 1
             */

            private Object address;
            private Object phone;
            private String invoice_type_name;
            private String invoice_title;
            private String invoice_content;
            private double invoice_amount;
            private Object tex_payer_number;
            private Object bank_name;
            private Object bank_account;
            private String invoice_type;
            private String invoice_title_type;

            public Object getAddress() {
                return address;
            }

            public void setAddress(Object address) {
                this.address = address;
            }

            public Object getPhone() {
                return phone;
            }

            public void setPhone(Object phone) {
                this.phone = phone;
            }

            public String getInvoice_type_name() {
                return invoice_type_name;
            }

            public void setInvoice_type_name(String invoice_type_name) {
                this.invoice_type_name = invoice_type_name;
            }

            public String getInvoice_title() {
                return invoice_title;
            }

            public void setInvoice_title(String invoice_title) {
                this.invoice_title = invoice_title;
            }

            public String getInvoice_content() {
                return invoice_content;
            }

            public void setInvoice_content(String invoice_content) {
                this.invoice_content = invoice_content;
            }

            public double getInvoice_amount() {
                return invoice_amount;
            }

            public void setInvoice_amount(double invoice_amount) {
                this.invoice_amount = invoice_amount;
            }

            public Object getTex_payer_number() {
                return tex_payer_number;
            }

            public void setTex_payer_number(Object tex_payer_number) {
                this.tex_payer_number = tex_payer_number;
            }

            public Object getBank_name() {
                return bank_name;
            }

            public void setBank_name(Object bank_name) {
                this.bank_name = bank_name;
            }

            public Object getBank_account() {
                return bank_account;
            }

            public void setBank_account(Object bank_account) {
                this.bank_account = bank_account;
            }

            public String getInvoice_type() {
                return invoice_type;
            }

            public void setInvoice_type(String invoice_type) {
                this.invoice_type = invoice_type;
            }

            public String getInvoice_title_type() {
                return invoice_title_type;
            }

            public void setInvoice_title_type(String invoice_title_type) {
                this.invoice_title_type = invoice_title_type;
            }
        }

        public static class DeliverysBean {
            /**
             * delivery : false
             * code : SDO23799674502
             * printExpress : true
             * printDeliveryList : false
             * scan : true
             * weight : false
             * warehouse_name : 正品仓
             * warehouse_code : 100
             * express_name : 圆通速递
             * express_code : YTO
             * mail_no : 4356464564645
             */

            private boolean delivery;
            private String code;
            private boolean printExpress;
            private boolean printDeliveryList;
            private boolean scan;
            private boolean weight;
            private String warehouse_name;
            private String warehouse_code;
            private String express_name;
            private String express_code;
            private String mail_no;

            public boolean isDelivery() {
                return delivery;
            }

            public void setDelivery(boolean delivery) {
                this.delivery = delivery;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public boolean isPrintExpress() {
                return printExpress;
            }

            public void setPrintExpress(boolean printExpress) {
                this.printExpress = printExpress;
            }

            public boolean isPrintDeliveryList() {
                return printDeliveryList;
            }

            public void setPrintDeliveryList(boolean printDeliveryList) {
                this.printDeliveryList = printDeliveryList;
            }

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

            public String getWarehouse_name() {
                return warehouse_name;
            }

            public void setWarehouse_name(String warehouse_name) {
                this.warehouse_name = warehouse_name;
            }

            public String getWarehouse_code() {
                return warehouse_code;
            }

            public void setWarehouse_code(String warehouse_code) {
                this.warehouse_code = warehouse_code;
            }

            public String getExpress_name() {
                return express_name;
            }

            public void setExpress_name(String express_name) {
                this.express_name = express_name;
            }

            public String getExpress_code() {
                return express_code;
            }

            public void setExpress_code(String express_code) {
                this.express_code = express_code;
            }

            public String getMail_no() {
                return mail_no;
            }

            public void setMail_no(String mail_no) {
                this.mail_no = mail_no;
            }
        }
    }
}
