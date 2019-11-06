package com.fangzuo.assist.Utils;

import android.util.Log;

import com.fangzuo.assist.Activity.Crash.App;
import com.google.gson.Gson;

public class Lg {

    public static void e(String string){
        if (App.isDebug){
            if (string!=null){
                Log.e("TEST","\n"+string);
            }
        }
    }
    public static void d(String string){
        if (App.isDebug){
            if (string!=null){
                Log.d("TEST","\n"+string);
            }
        }
    }
    public static void e(String tag,String string){
        try{
            if (App.isDebug){
                if (string!=null){
                    Log.e(tag,"\n"+string);
                }
            }
        }catch (Exception e){
            Log.e(tag,"\nLog日志内部错误");
        }

    }
    public static void e(String tag,Object string){
        try{
            if (App.isDebug){
                if (string!=null){
                    Log.e(tag,"\n"+new Gson().toJson(string));
                }else{
                    Log.e(tag,"对象为空");
                }
            }
        }catch (Exception e){
            Log.e(tag,"\nLog日志内部错误");
        }

    }
}
