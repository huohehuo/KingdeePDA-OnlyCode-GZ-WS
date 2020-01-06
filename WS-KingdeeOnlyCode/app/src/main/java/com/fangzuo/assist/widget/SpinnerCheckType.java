package com.fangzuo.assist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.Adapter.CheckTypeSpAdapter;
import com.fangzuo.assist.Adapter.GProductSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Dao.CheckType;
import com.fangzuo.assist.Dao.GProduct;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.greendao.gen.CheckTypeDao;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.GProductDao;
import com.loopj.android.http.AsyncHttpClient;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

public class SpinnerCheckType extends RelativeLayout {
    // 返回按钮控件
    private Spinner mSp;
    // 标题Tv
    private TextView mTitleTv;
    private boolean showEd = false;
    //    private SpinnerAdapter adapter;
    private DaoSession daoSession;
    private ArrayList<String> autoList;
    private BasicShareUtil share;
    private ArrayList<CheckType> container;
    private CheckTypeSpAdapter adapter;
    private String autoString;//用于联网时，再次去自动设置值
    private String saveKeyString="";//用于保存数据的key
    private String employeeId="";
    private String employeeName="";


    public SpinnerCheckType(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_my_people_spinner, this);
        daoSession = GreenDaoManager.getmInstance(context).getDaoSession();

        autoList = new ArrayList<>();
        share = BasicShareUtil.getInstance(context);
        container = new ArrayList<>();
        // 获取控件
        mSp = (Spinner) findViewById(R.id.sp);
        adapter = new CheckTypeSpAdapter(context, container);
        mSp.setAdapter(adapter);
        if (share.getIsOL()) {
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(22);
            String json = JsonCreater.DownLoadData(
                    share.getDatabaseIp(),
                    share.getDatabasePort(),
                    share.getDataBaseUser(),
                    share.getDataBasePass(),
                    share.getDataBase(),
                    share.getVersion(),
                    choose
            );
            Asynchttp.post(context, share.getBaseURL() + WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
//                        GProductDao payTypeDao = daoSession.getGProductDao();
//                        payTypeDao.deleteAll();
//                        payTypeDao.insertOrReplaceInTx(dBean.gProducts);
//                        payTypeDao.detachAll();
                    if (null!=dBean.checkTypes && dBean.checkTypes.size() > 0 && container.size()<=0){
                        container.addAll(dBean.checkTypes);
                        adapter.notifyDataSetChanged();
                        setAutoSelection(saveKeyString,autoString);
                    }
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
//                    Toast.showText(context, Msg);
                }
            });
        }
//        else {
        CheckTypeDao employeeDao = daoSession.getCheckTypeDao();
        List<CheckType> employees = employeeDao.loadAll();
        container.addAll(employees);
        adapter.notifyDataSetChanged();
        setAutoSelection(saveKeyString,autoString);
//            Log.e("CommonMethod", "获取到本地数据：\n" + container.toString());
//            adapter.notifyDataSetChanged();
//        }


        mSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CheckType employee = (CheckType) adapter.getItem(i);
                employeeId = employee.FInterID;
                employeeName = employee.FBillNo;
                Lg.e("选中业务员：",employee);
                Hawk.put(saveKeyString,employee.FBillNo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

//    //仓库Spinner
//    public StorageSpAdapter getStorageSpinner(AutoSpinner sp){
//        final ArrayList<Storage> container = new ArrayList<>();
//        final StorageSpAdapter adapter = new StorageSpAdapter(context, container);
//        sp.setAdapter(adapter);
//        if(share.getIsOL()){
//            Log.e("CommonMethod:","getStorageSpinner联网");
//            ArrayList<Integer> choose = new ArrayList<>();
//            choose.add(6);
//            String json = JsonCreater.DownLoadData(
//                    share.getDatabaseIp(),
//                    share.getDatabasePort(),
//                    share.getDataBaseUser(),
//                    share.getDataBasePass(),
//                    share.getDataBase(),
//                    share.getVersion(),
//                    choose
//            );
//            Asynchttp.post(context,share.getBaseURL()+ WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
//                @Override
//                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                    Log.e("CommonMethod:","getStorageSpinner获得联网数据：\n"+cBean.returnJson);
//                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
//                    container.addAll(dBean.storage);
//                    adapter.notifyDataSetChanged();
//                }
//
//                @Override
//                public void onFailed(String Msg, AsyncHttpClient client) {
//                    Toast.showText(context, Msg);
//                }
//            });
////            RetrofitUtil.getInstance(context).createReq(WebAPI.class).
////                    downloadData(RetrofitUtil.getParams(context,json)).enqueue(new CallBack() {
////                @Override
////                public void onSucceed(CommonResponse cBean) {
////                    Log.e("CommonMethod:","getStorageSpinner获得数据：\n"+cBean.returnJson);
////                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
////                    container.addAll(dBean.storage);
////                    adapter.notifyDataSetChanged();
////                }
////
////                @Override
////                public void OnFail(String Msg) {
////                    Toast.showText(context, Msg);
////                }
////            });
//        }else{
//            Log.e("CommonMethod:","getStorageSpinner不-联网");
//            StorageDao storageDao = daoSession.getStorageDao();
//            List<Storage> storages = storageDao.loadAll();
//            container.addAll(storages);
//            Log.e("CommonMethod","获取到本地数据：\n"+container.toString());
//            adapter.notifyDataSetChanged();
//        }
//        return adapter;
//    }
    //自动设置保存的值

    // 为左侧返回按钮添加自定义点击事件
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        mSp.setOnItemSelectedListener(listener);
    }
//    public void setAdapter(SpinnerAdapter adapter){
//        this.adapter = adapter;
//        mSp.setAdapter(adapter);
//    }
//    public void setSelection(int i){
//        mSp.setSelection(i);
//    }


    public String getEmployeeId() {
        return employeeId == null ? "" : employeeId;
    }

    public String getEmployeeName() {
        return employeeName == null ? "" : employeeName;
    }

    /**
     *
     * @param saveKeyStr        用于保存的key
     * @param string            自动设置的z值
     * */
    public void setAutoSelection(String saveKeyStr,String string) {
        saveKeyString =saveKeyStr;
        autoString = string;
        if ("".equals(string)){
            autoString = Hawk.get(saveKeyString,"");
        }
        for (int j = 0; j < adapter.getCount(); j++) {
            if (((CheckType) adapter.getItem(j)).FBillNo.equals(autoString)
                    || ((CheckType) adapter.getItem(j)).FInterID.equals(autoString)) {
                mSp.setSelection(j);
//                autoString = null;
                break;
            }
        }
    }

    public CheckTypeSpAdapter getAdapter() {
        return adapter;
    }

    //清空
    private void clear() {
        container.clear();
    }
//     设置标题的方法
//    public void setTitleText(String title) {
//        mTitleTv.setText(title);
//    }

}
