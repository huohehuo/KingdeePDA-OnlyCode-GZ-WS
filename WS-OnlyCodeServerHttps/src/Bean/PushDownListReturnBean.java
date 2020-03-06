package Bean;


import java.util.ArrayList;

/**
 * Created by NB on 2017/8/23.
 */
public class PushDownListReturnBean {
    public ArrayList<PushDownListBean> list;
    public class PushDownListBean{
        public String FBillNo;
        public String FName;
        public String FDate;
        public String FSupplyID;
        public String FDeptID;
        public String FManagerID;
        public String FEmpID;
        public String FInterID;
        public String FPhoneAddress;
        public String FIfCheck;
        public int tag;


        @Override
        public String toString() {
            return "PushDownListBean{" +
                    "FBillNo='" + FBillNo + '\'' +
                    ", FName='" + FName + '\'' +
                    ", FDate='" + FDate + '\'' +
                    ", FSupplyID='" + FSupplyID + '\'' +
                    ", FDeptID='" + FDeptID + '\'' +
                    ", FManagerID='" + FManagerID + '\'' +
                    ", FEmpID='" + FEmpID + '\'' +
                    ", FInterID='" + FInterID + '\'' +
                    ", tag=" + tag +
                    '}';
        }
    }
}
