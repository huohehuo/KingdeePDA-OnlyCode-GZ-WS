package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class GProduct {
    @Id(autoincrement = true)
    public Long id;
    public String FInterID;
    public String FID;
    public String FName ;
    @Generated(hash = 1430090884)
    public GProduct(Long id, String FInterID, String FID, String FName) {
        this.id = id;
        this.FInterID = FInterID;
        this.FID = FID;
        this.FName = FName;
    }
    @Generated(hash = 1392775320)
    public GProduct() {
    }
    public String getFInterID() {
        return this.FInterID;
    }
    public void setFInterID(String FInterID) {
        this.FInterID = FInterID;
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
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

}
