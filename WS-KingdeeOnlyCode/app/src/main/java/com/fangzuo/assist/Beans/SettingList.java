package com.fangzuo.assist.Beans;

/**
 * Created by NB on 2017/7/28.
 */

public class SettingList {
    public int ImageResourse;
    public String tv;
    public String tag;
    public SettingList(String tag,int imageResourse,String tv) {
        ImageResourse = imageResourse;
        this.tv = tv;
        this.tag = tag;
    }
    public SettingList(int imageResourse, String tv) {
        ImageResourse = imageResourse;
        this.tv = tv;
    }

    public SettingList() {
    }
}
