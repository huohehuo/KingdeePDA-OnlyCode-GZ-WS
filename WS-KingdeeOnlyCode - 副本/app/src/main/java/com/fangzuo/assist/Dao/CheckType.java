package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class CheckType {
    @Id(autoincrement = true)
    public Long id;
    public String FInterID;
    public String FBillNo;
    @Generated(hash = 1848093613)
    public CheckType(Long id, String FInterID, String FBillNo) {
        this.id = id;
        this.FInterID = FInterID;
        this.FBillNo = FBillNo;
    }
    @Generated(hash = 281386950)
    public CheckType() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFInterID() {
        return this.FInterID;
    }
    public void setFInterID(String FInterID) {
        this.FInterID = FInterID;
    }
    public String getFBillNo() {
        return this.FBillNo;
    }
    public void setFBillNo(String FBillNo) {
        this.FBillNo = FBillNo;
    }


}
