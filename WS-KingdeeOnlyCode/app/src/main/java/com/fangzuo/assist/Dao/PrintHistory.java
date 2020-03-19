package com.fangzuo.assist.Dao;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class PrintHistory {
    @Id(autoincrement = true)
    public Long id;
    public String FTitle;
    public String FBarCode;
    public String FBJMan;
    public String FName;
    public String FNumber;
    public String FModel;
    public String FNum;
    public String FSupplier;
    public String tag;

    public String FBatch;
    public String FNum2;
    public String FUnit;
    public String FBaseUnit;
    public String FBaseUnitID;
    public String FStoreUnit;
    public String FUnitAux;
    public String FNot;
    public String FPrintMan;
    public String FStorage;
    public String FWaveHouse;
    public String FSaveIn;
    public String FCheck;
    public String FDate;
    public String FMaterialid;
    public String FAuxSign;
    public String FActualModel;

    public String FLevel;//等级
    public String FYmLenght;//原木长度
    public String FYmDiameter;//原木直径
    public String FBLenght;//板长
    public String FBWide;//板宽
    public String FBThick;//板厚
    public String FVolume;//体积
    public String FCeng;//体积
    public String FWidth;//体积
    public String FProject;//区分单据类型 0 是水版 1为原木（立方米版本）,2或者其他为原木英尺版本
    public String F_TypeID;//区分单据类型 0 是水版 1为原木(英尺版本) todo//早期版本0 是水版 1为原木（立方米版本）,2或者其他为原木英尺版本，0711西安已统一为0水板1原木
    public String F_Plies;//层数

    public String FPlanType;

    @Generated(hash = 1310191731)
    public PrintHistory(Long id, String FTitle, String FBarCode, String FBJMan, String FName, String FNumber, String FModel, String FNum,
            String FSupplier, String tag, String FBatch, String FNum2, String FUnit, String FBaseUnit, String FBaseUnitID, String FStoreUnit,
            String FUnitAux, String FNot, String FPrintMan, String FStorage, String FWaveHouse, String FSaveIn, String FCheck, String FDate,
            String FMaterialid, String FAuxSign, String FActualModel, String FLevel, String FYmLenght, String FYmDiameter, String FBLenght,
            String FBWide, String FBThick, String FVolume, String FCeng, String FWidth, String FProject, String F_TypeID, String F_Plies,
            String FPlanType) {
        this.id = id;
        this.FTitle = FTitle;
        this.FBarCode = FBarCode;
        this.FBJMan = FBJMan;
        this.FName = FName;
        this.FNumber = FNumber;
        this.FModel = FModel;
        this.FNum = FNum;
        this.FSupplier = FSupplier;
        this.tag = tag;
        this.FBatch = FBatch;
        this.FNum2 = FNum2;
        this.FUnit = FUnit;
        this.FBaseUnit = FBaseUnit;
        this.FBaseUnitID = FBaseUnitID;
        this.FStoreUnit = FStoreUnit;
        this.FUnitAux = FUnitAux;
        this.FNot = FNot;
        this.FPrintMan = FPrintMan;
        this.FStorage = FStorage;
        this.FWaveHouse = FWaveHouse;
        this.FSaveIn = FSaveIn;
        this.FCheck = FCheck;
        this.FDate = FDate;
        this.FMaterialid = FMaterialid;
        this.FAuxSign = FAuxSign;
        this.FActualModel = FActualModel;
        this.FLevel = FLevel;
        this.FYmLenght = FYmLenght;
        this.FYmDiameter = FYmDiameter;
        this.FBLenght = FBLenght;
        this.FBWide = FBWide;
        this.FBThick = FBThick;
        this.FVolume = FVolume;
        this.FCeng = FCeng;
        this.FWidth = FWidth;
        this.FProject = FProject;
        this.F_TypeID = F_TypeID;
        this.F_Plies = F_Plies;
        this.FPlanType = FPlanType;
    }
    @Generated(hash = 915761306)
    public PrintHistory() {
    }

    public void setProduct(Product product){
        this.FName = product.FName;
        this.FModel = product.FModel;
    }
    public void setProject(String pro){
        this.FProject = pro;
    }

    public String getFTitle() {
        return this.FTitle;
    }
    public void setFTitle(String FTitle) {
        this.FTitle = FTitle;
    }
    public String getFBarCode() {
        return this.FBarCode;
    }
    public void setFBarCode(String FBarCode) {
        this.FBarCode = FBarCode;
    }
    public String getFBatch() {
        return this.FBatch;
    }
    public void setFBatch(String FBatch) {
        this.FBatch = FBatch;
    }
    public String getFName() {
        return this.FName;
    }
    public void setFName(String FName) {
        this.FName = FName;
    }
    public String getFModel() {
        return this.FModel;
    }
    public void setFModel(String FModel) {
        this.FModel = FModel;
    }
    public String getFNum() {
        return this.FNum;
    }
    public void setFNum(String FNum) {
        this.FNum = FNum;
    }
    public String getFNum2() {
        return this.FNum2;
    }
    public void setFNum2(String FNum2) {
        this.FNum2 = FNum2;
    }
    public String getFUnit() {
        return this.FUnit;
    }
    public void setFUnit(String FUnit) {
        this.FUnit = FUnit;
    }
    public String getFBaseUnit() {
        return this.FBaseUnit;
    }
    public void setFBaseUnit(String FBaseUnit) {
        this.FBaseUnit = FBaseUnit;
    }
    public String getFBaseUnitID() {
        return this.FBaseUnitID;
    }
    public void setFBaseUnitID(String FBaseUnitID) {
        this.FBaseUnitID = FBaseUnitID;
    }
    public String getFStoreUnit() {
        return this.FStoreUnit;
    }
    public void setFStoreUnit(String FStoreUnit) {
        this.FStoreUnit = FStoreUnit;
    }
    public String getFUnitAux() {
        return this.FUnitAux;
    }
    public void setFUnitAux(String FUnitAux) {
        this.FUnitAux = FUnitAux;
    }
    public String getFNot() {
        return this.FNot;
    }
    public void setFNot(String FNot) {
        this.FNot = FNot;
    }
    public String getFPrintMan() {
        return this.FPrintMan;
    }
    public void setFPrintMan(String FPrintMan) {
        this.FPrintMan = FPrintMan;
    }
    public String getFNumber() {
        return this.FNumber;
    }
    public void setFNumber(String FNumber) {
        this.FNumber = FNumber;
    }
    public String getFStorage() {
        return this.FStorage;
    }
    public void setFStorage(String FStorage) {
        this.FStorage = FStorage;
    }
    public String getFWaveHouse() {
        return this.FWaveHouse;
    }
    public void setFWaveHouse(String FWaveHouse) {
        this.FWaveHouse = FWaveHouse;
    }
    public String getFSaveIn() {
        return this.FSaveIn;
    }
    public void setFSaveIn(String FSaveIn) {
        this.FSaveIn = FSaveIn;
    }
    public String getFCheck() {
        return this.FCheck;
    }
    public void setFCheck(String FCheck) {
        this.FCheck = FCheck;
    }
    public String getFDate() {
        return this.FDate;
    }
    public void setFDate(String FDate) {
        this.FDate = FDate;
    }
    public String getFMaterialid() {
        return this.FMaterialid;
    }
    public void setFMaterialid(String FMaterialid) {
        this.FMaterialid = FMaterialid;
    }
    public String getFAuxSign() {
        return this.FAuxSign;
    }
    public void setFAuxSign(String FAuxSign) {
        this.FAuxSign = FAuxSign;
    }
    public String getFActualModel() {
        return this.FActualModel;
    }
    public void setFActualModel(String FActualModel) {
        this.FActualModel = FActualModel;
    }
    public String getFLevel() {
        return this.FLevel;
    }
    public void setFLevel(String FLevel) {
        this.FLevel = FLevel;
    }
    public String getFYmLenght() {
        return this.FYmLenght;
    }
    public void setFYmLenght(String FYmLenght) {
        this.FYmLenght = FYmLenght;
    }
    public String getFYmDiameter() {
        return this.FYmDiameter;
    }
    public void setFYmDiameter(String FYmDiameter) {
        this.FYmDiameter = FYmDiameter;
    }
    public String getFBLenght() {
        return this.FBLenght;
    }
    public void setFBLenght(String FBLenght) {
        this.FBLenght = FBLenght;
    }
    public String getFBWide() {
        return this.FBWide;
    }
    public void setFBWide(String FBWide) {
        this.FBWide = FBWide;
    }
    public String getFBThick() {
        return this.FBThick;
    }
    public void setFBThick(String FBThick) {
        this.FBThick = FBThick;
    }
    public String getFVolume() {
        return this.FVolume;
    }
    public void setFVolume(String FVolume) {
        this.FVolume = FVolume;
    }
    public String getFCeng() {
        return this.FCeng;
    }
    public void setFCeng(String FCeng) {
        this.FCeng = FCeng;
    }
    public String getFWidth() {
        return this.FWidth;
    }
    public void setFWidth(String FWidth) {
        this.FWidth = FWidth;
    }
    public String getFProject() {
        return this.FProject;
    }
    public void setFProject(String FProject) {
        this.FProject = FProject;
    }
    public String getF_TypeID() {
        return this.F_TypeID;
    }
    public void setF_TypeID(String F_TypeID) {
        this.F_TypeID = F_TypeID;
    }
    public String getF_Plies() {
        return this.F_Plies;
    }
    public void setF_Plies(String F_Plies) {
        this.F_Plies = F_Plies;
    }
    public String getFBJMan() {
        return this.FBJMan;
    }
    public void setFBJMan(String FBJMan) {
        this.FBJMan = FBJMan;
    }
    public String getFSupplier() {
        return this.FSupplier;
    }
    public void setFSupplier(String FSupplier) {
        this.FSupplier = FSupplier;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTag() {
        return this.tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getFPlanType() {
        return this.FPlanType;
    }
    public void setFPlanType(String FPlanType) {
        this.FPlanType = FPlanType;
    }














}
