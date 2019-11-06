package com.fangzuo.assist.Beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by NB on 2017/7/28.
 */
@Entity
public class SendOrderListBean {
    @Id(autoincrement = true)
    public Long id;
    public String FTip;
    public String FName;
    public String FNumber;
    public String FModel;
    public String FQty;
    public String FQtying;
    public String FWlNo;
    public String FSaleNo;
    public String FUnit;
    public String FUnitID;
    public String FItemID;
    public String FStorage;
    public String FStorageID;
    public String FStorageCode;
    public String FStorageIn;
    public String FStorageInID;
    public String FStorageInCode;
    public String FWaveHouse;
    public String FWaveHouseID;
    public String FWaveHouseCode;
    public String FWaveHouseIn;
    public String FWaveHouseInID;
    public String FWaveHouseInCode;
    public String FActivity;
    @Generated(hash = 1117056535)
    public SendOrderListBean(Long id, String FTip, String FName, String FNumber,
            String FModel, String FQty, String FQtying, String FWlNo,
            String FSaleNo, String FUnit, String FUnitID, String FItemID,
            String FStorage, String FStorageID, String FStorageCode,
            String FStorageIn, String FStorageInID, String FStorageInCode,
            String FWaveHouse, String FWaveHouseID, String FWaveHouseCode,
            String FWaveHouseIn, String FWaveHouseInID, String FWaveHouseInCode,
            String FActivity) {
        this.id = id;
        this.FTip = FTip;
        this.FName = FName;
        this.FNumber = FNumber;
        this.FModel = FModel;
        this.FQty = FQty;
        this.FQtying = FQtying;
        this.FWlNo = FWlNo;
        this.FSaleNo = FSaleNo;
        this.FUnit = FUnit;
        this.FUnitID = FUnitID;
        this.FItemID = FItemID;
        this.FStorage = FStorage;
        this.FStorageID = FStorageID;
        this.FStorageCode = FStorageCode;
        this.FStorageIn = FStorageIn;
        this.FStorageInID = FStorageInID;
        this.FStorageInCode = FStorageInCode;
        this.FWaveHouse = FWaveHouse;
        this.FWaveHouseID = FWaveHouseID;
        this.FWaveHouseCode = FWaveHouseCode;
        this.FWaveHouseIn = FWaveHouseIn;
        this.FWaveHouseInID = FWaveHouseInID;
        this.FWaveHouseInCode = FWaveHouseInCode;
        this.FActivity = FActivity;
    }
    @Generated(hash = 2139752105)
    public SendOrderListBean() {
    }
    public String getFTip() {
        return this.FTip;
    }
    public void setFTip(String FTip) {
        this.FTip = FTip;
    }
    public String getFName() {
        return this.FName;
    }
    public void setFName(String FName) {
        this.FName = FName;
    }
    public String getFNumber() {
        return this.FNumber;
    }
    public void setFNumber(String FNumber) {
        this.FNumber = FNumber;
    }
    public String getFModel() {
        return this.FModel;
    }
    public void setFModel(String FModel) {
        this.FModel = FModel;
    }
    public String getFQty() {
        return this.FQty;
    }
    public void setFQty(String FQty) {
        this.FQty = FQty;
    }
    public String getFWlNo() {
        return this.FWlNo;
    }
    public void setFWlNo(String FWlNo) {
        this.FWlNo = FWlNo;
    }
    public String getFSaleNo() {
        return this.FSaleNo;
    }
    public void setFSaleNo(String FSaleNo) {
        this.FSaleNo = FSaleNo;
    }
    public String getFUnit() {
        return this.FUnit;
    }
    public void setFUnit(String FUnit) {
        this.FUnit = FUnit;
    }
    public String getFUnitID() {
        return this.FUnitID;
    }
    public void setFUnitID(String FUnitID) {
        this.FUnitID = FUnitID;
    }
    public String getFItemID() {
        return this.FItemID;
    }
    public void setFItemID(String FItemID) {
        this.FItemID = FItemID;
    }
    public String getFActivity() {
        return this.FActivity;
    }
    public void setFActivity(String FActivity) {
        this.FActivity = FActivity;
    }
    public String getFQtying() {
        return this.FQtying;
    }
    public void setFQtying(String FQtying) {
        this.FQtying = FQtying;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFStorage() {
        return this.FStorage;
    }
    public void setFStorage(String FStorage) {
        this.FStorage = FStorage;
    }
    public String getFStorageID() {
        return this.FStorageID;
    }
    public void setFStorageID(String FStorageID) {
        this.FStorageID = FStorageID;
    }
    public String getFStorageCode() {
        return this.FStorageCode;
    }
    public void setFStorageCode(String FStorageCode) {
        this.FStorageCode = FStorageCode;
    }
    public String getFStorageIn() {
        return this.FStorageIn;
    }
    public void setFStorageIn(String FStorageIn) {
        this.FStorageIn = FStorageIn;
    }
    public String getFStorageInID() {
        return this.FStorageInID;
    }
    public void setFStorageInID(String FStorageInID) {
        this.FStorageInID = FStorageInID;
    }
    public String getFStorageInCode() {
        return this.FStorageInCode;
    }
    public void setFStorageInCode(String FStorageInCode) {
        this.FStorageInCode = FStorageInCode;
    }
    public String getFWaveHouse() {
        return this.FWaveHouse;
    }
    public void setFWaveHouse(String FWaveHouse) {
        this.FWaveHouse = FWaveHouse;
    }
    public String getFWaveHouseID() {
        return this.FWaveHouseID;
    }
    public void setFWaveHouseID(String FWaveHouseID) {
        this.FWaveHouseID = FWaveHouseID;
    }
    public String getFWaveHouseCode() {
        return this.FWaveHouseCode;
    }
    public void setFWaveHouseCode(String FWaveHouseCode) {
        this.FWaveHouseCode = FWaveHouseCode;
    }
    public String getFWaveHouseIn() {
        return this.FWaveHouseIn;
    }
    public void setFWaveHouseIn(String FWaveHouseIn) {
        this.FWaveHouseIn = FWaveHouseIn;
    }
    public String getFWaveHouseInID() {
        return this.FWaveHouseInID;
    }
    public void setFWaveHouseInID(String FWaveHouseInID) {
        this.FWaveHouseInID = FWaveHouseInID;
    }
    public String getFWaveHouseInCode() {
        return this.FWaveHouseInCode;
    }
    public void setFWaveHouseInCode(String FWaveHouseInCode) {
        this.FWaveHouseInCode = FWaveHouseInCode;
    }


}
