package com.fangzuo.assist.Beans;

import com.fangzuo.assist.Utils.Info;

public class FidNoShowBean extends BasePush{
    public String FOrder;
    public String FOrderDone;
    public String FOrderDoing;
    public String FNum;
    public String FState;
    public String FState2;
    public String FState3;
    public String FState4;

    public FidNoShowBean(String FOrder, String FNum, String FState) {
        this.FOrder = FOrder;
        this.FNum = FNum;
        this.FState = FState;
    }

}
