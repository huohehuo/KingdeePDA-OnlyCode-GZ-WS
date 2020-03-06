package com.fangzuo.assist.Utils;

import com.orhanobut.hawk.Hawk;

/**
 * Created by NB on 2017/7/24.
 */

public class Info {

    public static String getAppNo(){
        if ("1".equals(Hawk.get(Config.PDA_Project_Type,"1"))){
            return "1.900";
        }else{
            return "1.4";
        }
    }

    public static final String AppKey = "132883";//数据库名称
    public static final String Secret = "14a0dc819df34f7590e3b8b16ac94232";//数据库名称
    public static final String SessionKey = "117ef281a0d24deaa742b680755eafb6";//数据库名称


    public static final String DATABASESETTING = "master";
    public static final int SEARCHFORRESULT = 9998;
    public static final int SEARCHFORRESULTPRODUCT = 9997;
    public static final int SEARCHFORRESULTCLIRNT = 9999;
    public static final int SEARCHFORRESULTJH = 9996;
    public static final int SEARCHPRODUCT = 7777;
    public static final int Search_Storage = 7780;
    public static final int SEARCHSUPPLIER = 7778;
    public static final int SEARCHCLIENT = 7779;
    public static final int SEARCHJH = 7770;
    public static final int Search_DbType = 7769;
    public static final int Search_Man = 7771;
    public static final int Search_CheckResult = 7772;

    public static final String AutoLoginName="AutoLoginName";
    public static final String AutoLoginPw="AutoLoginPw";
    public static final String AutoLoginUserID="AutoLoginUserID";
    public static final String AutoLogin="AutoLogin";
    public static final String IsRemanber="IsRemanber";
    public static final String FirstInOut="FirstInOut"; //1 开启先进先出，0关闭


}
