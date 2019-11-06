package com.fangzuo.assist.Beans;

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

    public CodeCheckBackDataBean(String FTip){
        this.FTip=FTip;
    }

    @Override
    public String toString() {
        return "CodeCheckBackDataBean{" +
                "FTip='" + FTip + '\'' +
                "FBillNo='" + FBillNo + '\'' +
                ", FItemID='" + FItemID + '\'' +
                ", FUnitID='" + FUnitID + '\'' +
                ", FQty='" + FQty + '\'' +
                ", FStockID='" + FStockID + '\'' +
                ", FStockPlaceID='" + FStockPlaceID + '\'' +
                ", FBatchNo='" + FBatchNo + '\'' +
                ", FKFPeriod='" + FKFPeriod + '\'' +
                ", FKFDate='" + FKFDate + '\'' +
                '}';
    }
}
