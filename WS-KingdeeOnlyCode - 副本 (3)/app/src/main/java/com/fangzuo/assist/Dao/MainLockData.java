package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class MainLockData {
    public long FOrderID;
    public String FBoxCode;
    public String FBatchNO;
    @Generated(hash = 1219157927)
    public MainLockData(long FOrderID, String FBoxCode, String FBatchNO) {
        this.FOrderID = FOrderID;
        this.FBoxCode = FBoxCode;
        this.FBatchNO = FBatchNO;
    }
    @Generated(hash = 2133367071)
    public MainLockData() {
    }
    public String getFBoxCode() {
        return this.FBoxCode;
    }
    public void setFBoxCode(String FBoxCode) {
        this.FBoxCode = FBoxCode;
    }
    public String getFBatchNO() {
        return this.FBatchNO;
    }
    public void setFBatchNO(String FBatchNO) {
        this.FBatchNO = FBatchNO;
    }
    public long getFOrderID() {
        return this.FOrderID;
    }
    public void setFOrderID(long FOrderID) {
        this.FOrderID = FOrderID;
    }
   
}
