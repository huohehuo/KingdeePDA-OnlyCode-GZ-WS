package com.fangzuo.assist.Beans;

public class CodeCheckBean {
    public String FOrderID;
    public String FPDAID;
    public String FUserID;
    public String FBarCode;
    public String FQty;
    public String FBatch;
    public String FUnitID;
    public String FItemID;
    public String FScanType;
    public String FStockID;
    public String FStockPlaceID;
    public String FInStore;
    public String FOutStore;
    public String FIfCheck;

    public CodeCheckBean(String FBarCode,String FOrderID, String FPDAID) {
        this.FOrderID = FOrderID;
        this.FPDAID = FPDAID;
        this.FBarCode = FBarCode;
    }
    public CodeCheckBean(String FBarCode) {
        this.FBarCode = FBarCode;
    }

    public CodeCheckBean() {
    }

    public CodeCheckBean(String FBarCode,String FOrderID,String FQty, String FPDAID) {
        this.FOrderID = FOrderID;
        this.FPDAID = FPDAID;
        this.FBarCode = FBarCode;
        this.FQty = FQty;
    }

    public CodeCheckBean(String FBarCode,String FOrderID, String FStockID,String FStockPlaceID,String FPDAID) {
        this.FOrderID = FOrderID;
        this.FPDAID = FPDAID;
        this.FBarCode = FBarCode;
        this.FStockID = FStockID;
        this.FStockPlaceID = FStockPlaceID;
    }

}
