package com.fangzuo.assist.RxSerivce;


import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.BackOrderQuery;
import com.fangzuo.assist.Beans.BackOrderResult;
import com.fangzuo.assist.Beans.OrderQuery;
import com.fangzuo.assist.Beans.OrderQueryResult;
import com.fangzuo.assist.Beans.SendOrderQuery;
import com.fangzuo.assist.Beans.SendOrderResult;
import com.fangzuo.assist.Utils.Info;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by aid on 11/10/16.
 */

public class CloudService {

    private ServiceRequest request;
    private Retrofit retrofit;

    public CloudService() {
        //当ip地址发生变化时，替换掉原有对象
//        if (!App.isChangeIp) {
//            request = App.getRetrofit().create(ServiceRequest.class);
//        } else {
            retrofit = new Retrofit.Builder()
                    .client(App.getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                    .baseUrl(Hawk.get(Config.Cloud_Url,"http://api.guanyierp.com/rest/erp_open/"))
                    .baseUrl(Hawk.get(Info.AppKey,"http://v2.api.guanyierp.com/rest/erp_open/"))
                    .build();
            request = retrofit.create(ServiceRequest.class);
//            App.setRetrofit(retrofit);
//        }
    }


    //执行接口
    public void getOrderQuery(OrderQuery data, ToSubscribe<OrderQueryResult> mySubscribe) {
        toSubscribe(request.getOrderQuery("",data), mySubscribe);
    }
    public void getSendOrderQuery(SendOrderQuery data, ToSubscribe<SendOrderResult> mySubscribe) {
        toSubscribe(request.getOrderQuery("",data), mySubscribe);
    }

    public void getBackOrderQuery(BackOrderQuery data, ToSubscribe<BackOrderResult> mySubscribe) {
        toSubscribe(request.getBackOrderQuery("",data), mySubscribe);
    }
    /**
     * retrofit 线程管理
     */
    private static <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    private OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request request = chain.request();
//                        Request.Builder requestBuilder = request.newBuilder();
//                        request = requestBuilder
//                                .addHeader("Content-Type", "application/json;charset=UTF-8")
//                                .post(RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
//                                        bodyToString(request.body())))//关键部分，设置requestBody的编码格式为json
//                                .build();
//                        return chain.proceed(request);
//                    }
//                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        //这里获取请求返回的cookie
                        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                            final StringBuffer cookieBuffer = new StringBuffer();
                            //最近在学习RxJava,这里用了RxJava的相关API大家可以忽略,用自己逻辑实现即可.大家可以用别的方法保存cookie数据
                            Observable.from(originalResponse.headers("Set-Cookie"))
                                    .map(new Func1<String, String>() {
                                        @Override
                                        public String call(String s) {
                                            String[] cookieArray = s.split(";");
                                            return cookieArray[0];
                                        }
                                    })
                                    .subscribe(new Action1<String>() {
                                        @Override
                                        public void call(String cookie) {
                                            if (cookie.startsWith("kdservice-sessionid")){
                                                Hawk.put("cookie",cookie);
                                            }
//                                            cookieBuffer.append(cookie).append(";");
                                        }
                                    });
//                            Lg.e("cooking:"+cookieBuffer.toString());
                        }

                        return originalResponse;
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        final Request.Builder builder = chain.request().newBuilder();
                        builder.addHeader("Cookie", Hawk.get("cookie",""));
                        Hawk.delete("cookie");
                        return chain.proceed(builder.build());
                    }
                })
                .addInterceptor(App.getInterceptor())
                .connectTimeout(5000, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS)
                .build();
    }
//
//    private static class OkHttpClientHolder {
//        private static final OkHttpClient client;
//
//        static {
//            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            if (BuildConfig.DEBUG) {
//                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//                builder.addInterceptor(interceptor);
//            }
//            client = builder.addNetworkInterceptor(new HttpLoggingInterceptor())
//                    .connectTimeout(5, TimeUnit.SECONDS)
//                    .build();
//        }
//
//    }



//    private static Map<String, Object> getMap(StoreQuery bean) {
//        Map<String, Object> jObj = new HashMap<>();
////        UUID uuid = UUID.randomUUID();
////        int hashCode = uuid.toString().hashCode();
//        jObj.put("code",bean.code);
//        jObj.put("sessionkey",bean.sessionkey);
//        jObj.put("method",bean.method);
//        jObj.put("sign",bean.sign);
//        jObj.put("page_no",bean.page_no);
//        jObj.put("page_size",bean.page_size);
//        jObj.put("modify_start_date",bean.modify_start_date);
//        jObj.put("modify_end_date",bean.modify_end_date);
//        jObj.put("appkey",bean.appkey);
////        jObj.put("format", Info.format);
////        jObj.put("useragent", Info.useragent);
////        jObj.put("rid", hashCode);
////        jObj.put("parameters", chinaToUnicode(json));
////        jObj.put("timestamp", new Date().toString());
////        jObj.put("v", "1.0");
//        Log.e("请求：", "网络数据：" + jObj.toString());
//        Log.e("请求：", "网络数据：" + new Gson().toJson(jObj));
//        return jObj;
//    }
//
//    private static Map<String, String> getParams(String json) {
//        BasicShareUtil share = BasicShareUtil.getInstance(App.getContext());
//        Map<String, String> params = new HashMap<>();
//        params.put("json", json);
//        params.put("sqlip", share.getDatabaseIp());
//        params.put("sqlport", share.getDatabasePort());
//        params.put("sqluser", share.getDataBaseUser());
//        params.put("sqlpass", share.getDataBasePass());
//        params.put("sqlname", share.getDataBase());
//        params.put("version", share.getVersion());
//        Log.e("请求：", "网络数据：" + params.toString());
//        return params;
//    }

    /**
     * 把中文转成Unicode码
     *
     * @param str
     * @return
     */
    public static String chinaToUnicode(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            int chr1 = (char) str.charAt(i);
            if (chr1 >= 19968 && chr1 <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
                result += "\\u" + Integer.toHexString(chr1);
            } else {
                result += str.charAt(i);
            }
        }
        return result;
    }
}
