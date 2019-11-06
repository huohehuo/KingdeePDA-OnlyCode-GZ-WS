package WebSide;

import java.util.List;

public class BackDataList {
    /**
     * size : 1
     * res : [{"customer":"吴开随","tel":"","description":"屏有线","treatment_program":"更换新屏幕&刷机","responsibility":"客责","repair_code":10452,"tabulation":"魏宏旺","storage_time":"09/02/2019"}]
     */

    private int size;
    private List<ResBean> res;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<ResBean> getRes() {
        return res;
    }

    public void setRes(List<ResBean> res) {
        this.res = res;
    }

    public static class ResBean {
        /**
         * customer : 吴开随
         * tel :
         * description : 屏有线
         * treatment_program : 更换新屏幕&刷机
         * responsibility : 客责
         * repair_code : 10452
         * tabulation : 魏宏旺
         * storage_time : 09/02/2019
         */

        private String customer;
        private String tel;
        private String description;
        private String treatment_program;
        private String responsibility;
        private int repair_code;
        private String tabulation;
        private String storage_time;

        public String getCustomer() {
            return customer;
        }

        public void setCustomer(String customer) {
            this.customer = customer;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTreatment_program() {
            return treatment_program;
        }

        public void setTreatment_program(String treatment_program) {
            this.treatment_program = treatment_program;
        }

        public String getResponsibility() {
            return responsibility;
        }

        public void setResponsibility(String responsibility) {
            this.responsibility = responsibility;
        }

        public int getRepair_code() {
            return repair_code;
        }

        public void setRepair_code(int repair_code) {
            this.repair_code = repair_code;
        }

        public String getTabulation() {
            return tabulation;
        }

        public void setTabulation(String tabulation) {
            this.tabulation = tabulation;
        }

        public String getStorage_time() {
            return storage_time;
        }

        public void setStorage_time(String storage_time) {
            this.storage_time = storage_time;
        }
    }

}
