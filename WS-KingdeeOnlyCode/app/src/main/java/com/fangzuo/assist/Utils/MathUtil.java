package com.fangzuo.assist.Utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathUtil {

    //防止强转时崩溃操作
    public static double toD(String string) {
        try{
            if (null == string) {
                return 0;
            } else if (string.equals("")) {
                return 0;
            } else {
                return Double.parseDouble(string);
            }
        }catch (Exception e){
            return 0;
        }

    }
    //防止强转时崩溃操作
    public static int toInt(String string) {
        if (null == string) {
            return 0;
        } else if (string.equals("")) {
            return 0;
        } else {
            try{
                return Integer.parseInt(string);
            }catch (Exception e){
                return 0;
            }
        }
    }

    //解决 1.1-1.0=0.10000009的情况
    //double相减
    public static double doubleSub(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.subtract(b2).doubleValue();
    }
    //解决 1.1+1.0=2.09999999的情况
    //double相加
    public static double sum(String d1,String d2){
        BigDecimal bd1 = new BigDecimal(d1);
        BigDecimal bd2 = new BigDecimal(d2);
        return bd1.add(bd2).doubleValue();
    }
    //是否为数字
    public static boolean isNumeric(String str) {
        //Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");//这个有问题，一位的整数不能通过
        Pattern pattern = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");//这个是对的
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
    //是否为纯数字
    public static boolean isNumber(Object o){
        return  (Pattern.compile("[0-9]*")).matcher(String.valueOf(o)).matches();
    }
    //保留两位小数（四舍五入
    public static double D2save2(Double d){
        return Double.parseDouble(String.format("%.2f", d));
    }
    /**
     * double 乘法
     * @param d1
     * @param d2
     * @return
     */
    public static double mul(double d1,double d2){
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.multiply(bd2).doubleValue();
    }

    /**
     * double 除法
     * @param d1
     * @param d2
     * @param scale 四舍五入 小数点位数
     * @return
     */
    public static double div(double d1,double d2,int scale){
        //  当然在此之前，你要判断分母是否为0，
        //  为0你可以根据实际需求做相应的处理

        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.divide
                (bd2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
