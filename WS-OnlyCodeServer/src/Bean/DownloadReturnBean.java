package Bean;

import java.util.ArrayList;

public class DownloadReturnBean {
    public ArrayList<bibiezhong> bibiezhongs;//�ұ��
    public ArrayList<department> department;//���ű�
    public ArrayList<employee> employee;//ְԱ��
    public ArrayList<wavehouse> wavehouse;//��λ��
    public ArrayList<InstorageNum> InstorageNum;//����
    public ArrayList<storage> storage;//�ֿ��
    public ArrayList<Unit> units;//��λ��
    public ArrayList<BarCode> BarCode;//�����
    public ArrayList<suppliers> suppliers;//��Ӧ�̱�
    public ArrayList<payType> payTypes;//���㷽ʽ��
    public ArrayList<product> products;//��Ʒ���ϱ�
    public ArrayList<User> User;//�û���Ϣ��
    public ArrayList<Client> clients;//�ͻ���Ϣ��
    public ArrayList<GetGoodsDepartment> getGoodsDepartments;//������λ
    public ArrayList<purchaseMethod> purchaseMethod;//����/�ɹ���ʽ��
    public ArrayList<yuandanType> yuandanTypes;//Դ������
    public ArrayList<wanglaikemu> wanglaikemu;//������Ŀ
    public ArrayList<PriceMethod> priceMethods;//�۸�����
    public ArrayList<InStorageType> inStorageTypes;
    public ArrayList<CodeCheckBackDataBean> codeCheckBackDataBeans;
    public ArrayList<InOutBean> inOutBeans;
    public ArrayList<SendOrderListBean> sendOrderListBeans;
    public ArrayList<GProduct> gProducts;
    public ArrayList<DbType> dbTypes;
    public ArrayList<Suppliers4P2> suppliers4P2s;
    public ArrayList<CheckLogBackBean> checkLogBackBeans;
    public ArrayList<CheckType> checkTypes;
    public ArrayList<CheckResult> checkResults;
    public ArrayList<PrintHistory> printHistories;

    public int size;

    public class PrintHistory {
        public String FTitle;
        public String FBarCode;
        public String FBJMan;
        public String FName;
        public String FNumber;
        public String FModel;
        public String FNum;
        public String FSupplier;
        public String tag;
        public String FPlanType;

    }
    public class CheckResult {
        public String FInterID;
        public String FName;



    }


    public class CheckType {
        public String FInterID;
        public String FBillNo;
    }
    public class DbType {
        public String FInterID;
        public String FID;
        public String FName ;
    }
    public class Suppliers4P2 {
        public String FPassWord;
        public String FUserID;
        public String FID;
        public String FName ;
        public String FLevel;//等级：1：一般权限，2:管理员权限，3：超级管理员
        public String FPermit;
    }

    public class GProduct {
        public String FInterID;
        public String FID;
        public String FName ;
    }
    public class CodeCheckBackDataBean {
        public String FTip;
        public String FBillNo;
        public String FItemID;
        public String FUnitID;
        public String FQty;
        public String FStockID;
        public String FStockPlaceID;
        public String FBatchNo;
        public String FKFPeriod;
        public String FKFDate;
        public String FNumber;
        public String FName;
        public String FPrice;
        public String FOLOrderBillNo;
    }

    public class InStorageType {
        public String FID;
        public String FName;
    }

    public class bibiezhong {
        public String FCurrencyID;
        public String FNumber;
        public String FName;
        public String FExChangeRate;
        public String fClassTypeId;
    }

    public class department {
        public String FItemID;
        public String FNumber;
        public String FName;
        public String FparentID;
    }

    public class employee {
        public String FItemID;
        public String FNumber;
        public String FName;
        public String FDpartmentID;
        public String FEmpGroup;
        public String FEmpGroupID;
    }

    public class wavehouse {
        public String FSPID;
        public String FSPGroupID;
        public String FNumber;
        public String FName;
        public String FFullName;
        public String FLevel;
        public String FDetail;
        public String FParentID;
        public String FClassTypeID;
        public String FDefaultSPID;

    }

    public class InstorageNum {
        public String FItemID;
        public String FStockID;
        public String FQty;
        public String FBal;
        public String FStockPlaceID;
        public String FKFPeriod;
        public String FKFDate;
        public String FBatchNo;
        public String FUnitID;

        @Override
        public String toString() {
            return "InstorageNum{" +
                    "FItemID='" + FItemID + '\'' +
                    ", FStockID='" + FStockID + '\'' +
                    ", FQty='" + FQty + '\'' +
                    ", FBal='" + FBal + '\'' +
                    ", FStockPlaceID='" + FStockPlaceID + '\'' +
                    ", FKFPeriod='" + FKFPeriod + '\'' +
                    ", FKFDate='" + FKFDate + '\'' +
                    ", FBatchNo='" + FBatchNo + '\'' +
                    ", FUnitID='" + FUnitID + '\'' +
                    '}';
        }
    }

    public class storage {
        public String FItemID;
        public String FEmpID;
        public String FName;
        public String FNumber;
        public String FTypeID;
        public String FSPGroupID;
        public String FGroupID;
        public String FStockGroupID;
        public String FIsStockMgr;
        public String FUnderStock;
    }


    public class Unit {
        public String FMeasureUnitID;
        public String FUnitGroupID;
        public String FNumber;
        public String FName;
        public String FCoefficient;
    }

    public class BarCode {
        public String FName;
        public String FTypeID;
        public String FItemID;
        public String FBarCode;
        public String FNumber;
        public String FUnitID;

        @Override
        public String toString() {
            return "BarCode{" +
                    "FName='" + FName + '\'' +
                    ", FTypeID='" + FTypeID + '\'' +
                    ", FItemID='" + FItemID + '\'' +
                    ", FBarCode='" + FBarCode + '\'' +
                    ", FNumber='" + FNumber + '\'' +
                    ", FUnitID='" + FUnitID + '\'' +
                    '}';
        }
    }

    public class suppliers {
        public String FItemID;
        public String FItemClassID;
        public String FNumber;
        public String FParentID;
        public String FLevel;
        public String FDetail;
        public String FName;
        public String FAddress;
        public String FPhone;
        public String FEmail;

        @Override
        public String toString() {
            return "suppliers{" +
                    "FItemID='" + FItemID + '\'' +
                    ", FItemClassID='" + FItemClassID + '\'' +
                    ", FNumber='" + FNumber + '\'' +
                    ", FParentID='" + FParentID + '\'' +
                    ", FLevel='" + FLevel + '\'' +
                    ", FDetail='" + FDetail + '\'' +
                    ", FName='" + FName + '\'' +
                    ", FAddress='" + FAddress + '\'' +
                    ", FPhone='" + FPhone + '\'' +
                    ", FEmail='" + FEmail + '\'' +
                    '}';
        }
    }

    public class payType {
        public String FItemID;
        public String FName;
        public String FNumber;
    }


    public class product {
        public String FItemID;
        public String FIsSnManage;
        public String FSecUnitID;
        public String FSecCoefficient;
        public String FISKFPeriod;
        public String FKFPeriod;
        public String FNumber;
        public String FModel;
        public String FName;
        public String FFullName;
        public String FUnitID;
        public String FUnitGroupID;
        public String FDefaultLoc;
        public String FProfitRate;
        public String FTaxRate;
        public String FOrderPrice;
        public String FSalePrice;
        public String FPlanPrice;
        public String FBarcode;
        public String FSPID;
        public String FBatchManager;
    }

    public class User {
        public String FUserID;
        public String FName;
        public String FPassWord;
        public String FEmpID;
        public String FGroupName;
        public String FPermit;
    }

    public class Client {
        public String FItemID;
        public String FItemClassID;
        public String FNumber;
        public String FParentID;
        public String FLevel;
        public String FDetail;
        public String FName;
        public String FAddress;
        public String FPhone;
        public String FEmail;
        public String FTypeID;
    }

    public class GetGoodsDepartment {
        public String FItemID;
        public String FDeleted;
        public String FNumber;
        public String FName;
        public String FDetail;
    }

    public class purchaseMethod {
        public String FTypeID;
        public String FItemID;
        public String FNumber;
        public String FName;
    }

    public class yuandanType {
        public String FID;
        public String FName_CHS;
    }


    public class wanglaikemu {
        public String FAccountID;
        public String FNumber;
        public String FFullName;
    }

    public class PriceMethod {
        public String FInterID;
        public String FEntryID;
        public String FPri;
        public String FPrice;
        public String FName;
        public String FItemID;
        public String FUnitID;
        public String FRelatedID;
        public String FBegQty;
        public String FEndQty;
        public String FBegDate;
        public String FEndDate;
    }


}
