package Bean;

import java.util.ArrayList;

/**
 * Created by NB on 2017/8/24.
 *
 *              下载bean  销售出库单验货
 *
 */
public class PushDownDLBean {
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
        public String FDCStockID;//入
        public String FDCSPID;
        public String FSCStockID;//出
        public String FSCSPID;
        public String FStorageIn;
        public String FStorageOut;
        public String FDCStockName;
        public String FDCSPName;
        public String FBatchNo;
        public String FKFPeriod;
        public String FAuxPrice;
        public String FKFDate;
        public String FEntrySelfS0168;
        public String FProductType;
        public int tag;

        @Override
        public String toString() {
            return "DLbean{" +
                    "FName='" + FName + '\'' +
                    ", FNumber='" + FNumber + '\'' +
                    ", FModel='" + FModel + '\'' +
                    ", FBillNo='" + FBillNo + '\'' +
                    ", FInterID='" + FInterID + '\'' +
                    ", FEntryID='" + FEntryID + '\'' +
                    ", FItemID='" + FItemID + '\'' +
                    ", FUnitID='" + FUnitID + '\'' +
                    ", FAuxQty='" + FAuxQty + '\'' +
                    ", FQtying='" + FQtying + '\'' +
                    ", FDCStockID='" + FDCStockID + '\'' +
                    ", FSCStockID='" + FSCStockID + '\'' +
                    ", FDCSPID='" + FDCSPID + '\'' +
                    ", FDCStockName='" + FDCStockName + '\'' +
                    ", FDCSPName='" + FDCSPName + '\'' +
                    ", FBatchNo='" + FBatchNo + '\'' +
                    ", FKFPeriod='" + FKFPeriod + '\'' +
                    ", FAuxPrice='" + FAuxPrice + '\'' +
                    ", FKFDate='" + FKFDate + '\'' +
                    ", FEntrySelfS0168='" + FEntrySelfS0168 + '\'' +
                    '}';
        }
    }
}
