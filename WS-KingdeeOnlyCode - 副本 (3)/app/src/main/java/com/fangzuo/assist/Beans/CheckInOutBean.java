package com.fangzuo.assist.Beans;

public class CheckInOutBean {
    public String FItemID;
    public String FBatchNo;
    public String FOrderID;
    public String FPDAID;
    public String FBarCode;
    public String FSN;
    public String FStockID;
    public String FStockPlaceID;
    public String FUnitID;
    public CheckInOutBean(String FItemID, String FBatchNo) {
        this.FItemID = FItemID;
        this.FBatchNo = FBatchNo;
    }
    public CheckInOutBean(String FBarCode, String FSN, boolean forOnlyCode) {//boolean其实没啥用
        this.FBarCode = FBarCode;
        this.FSN = FSN;
    }
    public CheckInOutBean(String FBarCode) {//boolean其实没啥用
        this.FBarCode = FBarCode;
    }


    public CheckInOutBean(String FBatchNo, String FOrderID, String FBarCode, String FSN, String FStockID, String FStockPlaceID, String FUnitID, String FPDAID) {
        this.FBatchNo = FBatchNo;
        this.FOrderID = FOrderID;
        this.FPDAID = FPDAID;
        this.FBarCode = FBarCode;
        this.FSN = FSN;
        this.FStockID = FStockID;
        this.FStockPlaceID = FStockPlaceID;
        this.FUnitID = FUnitID;
    }
    public CheckInOutBean(String FBatchNo, String FOrderID, String FBarCode, String FSN, String FStockID, String FStockPlaceID, String FPDAID) {
        this.FBatchNo = FBatchNo;
        this.FOrderID = FOrderID;
        this.FPDAID = FPDAID;
        this.FBarCode = FBarCode;
        this.FSN = FSN;
        this.FStockID = FStockID;
        this.FStockPlaceID = FStockPlaceID;
    }
}
