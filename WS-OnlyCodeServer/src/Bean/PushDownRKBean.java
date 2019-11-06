package Bean;

import java.util.ArrayList;

/**
 * Created by NB on 2017/8/24.
 *          下载bean  产品入库验货
 */
public class PushDownRKBean {
    public ArrayList<Rkbean> list;
    public class Rkbean{
        public String FBillNo;
        public String FName;
        public String FDate;
        public String FSupplyID;
        public String FDeptID;
        public String FEmpID;
        public String FInterID;
        public String FMangerID;

//        public String FNumber;
//        public String FModel;
//        public String FEntryID;
//        public String FItemID;
//        public String FUnitID;
//        public String FAuxQty;
//        public String FQtying;
//        public String FDCStockID;
//        public String FSCStockID;
//        public String FDCSPID;
//        public String FDCStockName;
//        public String FDCSPName;
//        public String FBatchNo;
//        public String FKFPeriod;
//        public String FAuxPrice;
//        public String FKFDate;


        @Override
        public String toString() {
            return "Rkbean{" +
                    "FBillNo='" + FBillNo + '\'' +
                    ", FName='" + FName + '\'' +
                    ", FDate='" + FDate + '\'' +
                    ", FSupplyID='" + FSupplyID + '\'' +
                    ", FDeptID='" + FDeptID + '\'' +
                    ", FEmpID='" + FEmpID + '\'' +
                    ", FInterID='" + FInterID + '\'' +
                    ", FMangerID='" + FMangerID + '\'' +
                    '}';
        }
    }
}
