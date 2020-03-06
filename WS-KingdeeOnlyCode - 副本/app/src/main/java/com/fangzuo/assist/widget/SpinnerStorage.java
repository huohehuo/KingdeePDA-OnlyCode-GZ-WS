package com.fangzuo.assist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.StorageSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.StorageDao;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

public class SpinnerStorage extends RelativeLayout {
    // 返回按钮控件
    private Spinner mSp;
    // 标题Tv
    private TextView mTitleTv;
    private boolean showEd = false;
    //    private SpinnerAdapter adapter;
    private DaoSession daoSession;
    private ArrayList<String> autoList;
    private BasicShareUtil share;
    private ArrayList<Storage> container;
    private ArrayList<Storage> tempList;
    private StorageSpAdapter adapter;
    private String autoString="";//用于联网时，再次去自动设置值
    private String saveKeyString="";//用于保存数据的key
    private String Id="";
    private String Name="";
    private String T="仓库：";     //19


    public SpinnerStorage(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_my_people_spinner, this);
        daoSession = GreenDaoManager.getmInstance(context).getDaoSession();

        autoList = new ArrayList<>();
        share = BasicShareUtil.getInstance(context);
        container = new ArrayList<>();
        tempList = new ArrayList<>();
        // 获取控件
        mSp = (Spinner) findViewById(R.id.sp);
        adapter = new StorageSpAdapter(context, container);
        mSp.setAdapter(adapter);

        if (share.getIsOL()) {
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(6);
            String json = JsonCreater.DownLoadData(
                    share.getDatabaseIp(),
                    share.getDatabasePort(),
                    share.getDataBaseUser(),
                    share.getDataBasePass(),
                    share.getDataBase(),
                    share.getVersion(),
                    choose
            );
            App.getRService().doIOAction(WebApi.DOWNLOADDATA, json, new MySubscribe<CommonResponse>() {
                @Override
                public void onNext(CommonResponse cBean) {
                    super.onNext(cBean);
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    StorageDao yuandanTypeDao = daoSession.getStorageDao();
                    yuandanTypeDao.deleteAll();
                    yuandanTypeDao.insertOrReplaceInTx(dBean.storage);
                    yuandanTypeDao.detachAll();
                    if (dBean.storage.size()>0 && container.size()<=0){
                        tempList.addAll(dBean.storage);
                        container.addAll(dBean.storage);
                        adapter.notifyDataSetChanged();
                        setAutoSelection(saveKeyString,autoString);
                    }
                }

                @Override
                public void onError(Throwable e) {
//                    super.onError(e);
                }
            });
        }
//        else {
            StorageDao storageDao = daoSession.getStorageDao();
            List<Storage> storages = storageDao.loadAll();
            tempList.addAll(storages);
            container.addAll(storages);
            adapter.notifyDataSetChanged();
            if (autoString != null) {
                setAutoSelection(saveKeyString,autoString);
            }
//            Log.e("CommonMethod", "获取到本地数据：\n" + container.toString());
//        }


//        mSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                InStoreType employee = (InStoreType) adapter.getItem(i);
//                Id = employee.FID;
//                Name = employee.FName;
//                Lg.e("选中"+T+employee.toString());
//                Hawk.put(saveKeyString,employee.FName);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

    }

    //自动设置保存的值

//
//    public EmployeeSpAdapter getEmployeeSpAdapter() {
//        if (adapter.getCount() < 0) {
//            Lg.e("adapter初始化失败，重新更新adapter");
//            EmployeeDao employeeDao = daoSession.getEmployeeDao();
//            List<Employee> employees = employeeDao.loadAll();
//            container.addAll(employees);
//            adapter.notifyDataSetChanged();
//            return adapter;
//        } else {
//            return adapter;
//        }
//    }

    // 为左侧返回按钮添加自定义点击事件
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        mSp.setOnItemSelectedListener(listener);
    }
//    public void setAdapter(SpinnerAdapter adapter){
//        this.adapter = adapter;
//        mSp.setAdapter(adapter);
//    }
    public void setSelection(int i){
        mSp.setSelection(i);
    }
    public void setSelectionById(String interid){
        for (int j = 0; j < adapter.getCount(); j++) {
            if (((Storage) adapter.getItem(j)).FItemID.equals(interid)) {
                mSp.setSelection(j);
                break;
            }
        }
    }


//    public String getDataId() {
//        return Id == null ? "" : Id;
//    }
//
//    public String getDataName() {
//        return Name == null ? "" : Name;
//    }

    /**
     *
     * @param saveKeyStr        用于保存的key
     * @param string            自动设置的z值
     * */
    public void setAutoSelection(String saveKeyStr,String string) {
        saveKeyString =saveKeyStr;
        autoString = string;
        Lg.e("自动"+T+autoString);
        if ("".equals(string)){
            autoString = Hawk.get(saveKeyString,"");
        }
        for (int j = 0; j < adapter.getCount(); j++) {
            if (((Storage) adapter.getItem(j)).FName.equals(autoString)
                    || ((Storage) adapter.getItem(j)).FItemID.equals(autoString)) {
                mSp.setSelection(j);
//                autoString = null;
                break;
            }
        }
    }

    public void setAutoSelection(String saveKeyStr,String string,boolean zhuangxiang) {
        saveKeyString =saveKeyStr;
        autoString = string;
        Lg.e("自动"+T+autoString);
        if ("".equals(string)){
            autoString = Hawk.get(saveKeyString,"");
        }
        Lg.e("过滤前"+container.size());
        for (int j = 0; j < tempList.size(); j++) {
            if (!"LW01".equals(tempList.get(j).FNumber)&&
                    !"919".equals(tempList.get(j).FNumber)&&
                    !"303".equals(tempList.get(j).FNumber) &&
                    !"920".equals(tempList.get(j).FNumber) &&
                    !"923".equals(tempList.get(j).FNumber)
                    ){
                container.remove(tempList.get(j));
            }
        }
        Lg.e("过滤后"+container.size(),container);
        adapter.notifyDataSetChanged();
        Lg.e("adpter",adapter.getCount());
        for (int j = 0; j < adapter.getCount(); j++) {
            if (((Storage) adapter.getItem(j)).FName.equals(autoString)
                    || ((Storage) adapter.getItem(j)).FItemID.equals(autoString)) {
                mSp.setSelection(j);
//                autoString = null;
                break;
            }
        }

    }


    public StorageSpAdapter getAdapter() {
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

    public void setEnable(boolean b){
        mSp.setEnabled(b);
    }
}
