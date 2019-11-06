package Bean;

import java.util.ArrayList;

public class ScanPDReturnBean {
    public ArrayList<PushDownListBean> list1;
    public class PushDownListBean{
        public String FBillNo;
        public String FName;
        public String FDate;
        public String FSupplyID;
        public String FDeptID;
        public String FManagerID;
        public String FEmpID;
        public String FInterID;
        public int tag;
    }
    public ArrayList<DLbean> list;
    public class DLbean{
        public String FName;
        public String FNumber;
        public String FModel;
        public String FBillNo;
        public String FInterID;
        public String FEntryID;
        public String FItemID;
        public String FUnitID;
        public String FAuxQty;
        public String FQtying;
        public String FDCStockID;
        public String FDCSPID;
        public String FBatchNo;
        public String FKFPeriod;
        public String FAuxPrice;
        public String FKFDate;
        public String FProductType;
        public String FDCStockName;
        public String FDCSPName;
    }

}
