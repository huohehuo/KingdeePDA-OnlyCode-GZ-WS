package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class Suppliers4P2 {
    @Id(autoincrement = true)
    public Long id;
    public String FPassWord;
    public String FID;
    public String FName ;
    public String FLevel;//等级：1：一般权限，2:管理员权限，3：超级管理员
    public String FPermit;


    @Generated(hash = 156726741)
    public Suppliers4P2(Long id, String FPassWord, String FID, String FName,
            String FLevel, String FPermit) {
        this.id = id;
        this.FPassWord = FPassWord;
        this.FID = FID;
        this.FName = FName;
        this.FLevel = FLevel;
        this.FPermit = FPermit;
    }
    @Generated(hash = 1947266461)
    public Suppliers4P2() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFPassWord() {
        return this.FPassWord;
    }
    public void setFPassWord(String FPassWord) {
        this.FPassWord = FPassWord;
    }
    public String getFID() {
        return this.FID;
    }
    public void setFID(String FID) {
        this.FID = FID;
    }
    public String getFName() {
        return this.FName;
    }
    public void setFName(String FName) {
        this.FName = FName;
    }
    public String getFLevel() {
        return this.FLevel;
    }
    public void setFLevel(String FLevel) {
        this.FLevel = FLevel;
    }
    public String getFPermit() {
        return this.FPermit;
    }
    public void setFPermit(String FPermit) {
        this.FPermit = FPermit;
    }

}
