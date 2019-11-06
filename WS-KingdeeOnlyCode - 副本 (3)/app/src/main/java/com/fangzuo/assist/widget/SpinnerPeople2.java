package com.fangzuo.assist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.Adapter.EmployeeSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Dao.Employee;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.EmployeeDao;
import com.loopj.android.http.AsyncHttpClient;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

public class SpinnerPeople2 extends RelativeLayout {
    // 返回按钮控件
    private Spinner mSp;
    // 标题Tv
    private TextView mTitleTv;
    private boolean showEd = false;
    //    private SpinnerAdapter adapter;
    private DaoSession daoSession;
    private ArrayList<String> autoList;
    private BasicShareUtil share;
    private ArrayList<Employee> container;
    private EmployeeSpAdapter adapter;
    private String autoString;//用于联网时，再次去自动设置值
    private String saveKeyString="";//用于保存数据的key
    private String employeeId="";
    private String employeeName="";


    public SpinnerPeople2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_my_people_spinner, this);
        daoSession = GreenDaoManager.getmInstance(context).getDaoSession();

        autoList = new ArrayList<>();
        share = BasicShareUtil.getInstance(context);
        container = new ArrayList<>();
        // 获取控件
        mSp = (Spinner) findViewById(R.id.sp);
        adapter = new EmployeeSpAdapter(context, container);
        mSp.setAdapter(adapter);
        if (share.getIsOL()) {
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(3);
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
                    EmployeeDao payTypeDao = daoSession.getEmployeeDao();
                    payTypeDao.deleteAll();
                    payTypeDao.insertOrReplaceInTx(dBean.employee);
                    payTypeDao.detachAll();
                    if (dBean.employee.size() > 0 && container.size()<=0){
                        container.add(new Employee("","","","","",""));
                        container.addAll(dBean.employee);
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
            EmployeeDao employeeDao = daoSession.getEmployeeDao();
            List<Employee> employees = employeeDao.loadAll();
            container.add(new Employee("","","","","",""));
            container.addAll(employees);
            adapter.notifyDataSetChanged();
            setAutoSelection(saveKeyString,autoString);
//            Log.e("CommonMethod", "获取到本地数据：\n" + container.toString());
//            adapter.notifyDataSetChanged();
//        }


        mSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Employee employee = (Employee) adapter.getItem(i);
                employeeId = employee.FItemID;
                employeeName = employee.FName;
                Lg.e("选中业务员：",employee);
                Hawk.put(saveKeyString,employee.FName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    public EmployeeSpAdapter getEmployeeSpAdapter() {
        if (adapter.getCount() < 0) {
            Lg.e("adapter初始化失败，重新更新adapter");
            EmployeeDao employeeDao = daoSession.getEmployeeDao();
            List<Employee> employees = employeeDao.loadAll();
            container.addAll(employees);
            adapter.notifyDataSetChanged();
            return adapter;
        } else {
            return adapter;
        }
    }

    // 为左侧返回按钮添加自定义点击事件
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        mSp.setOnItemSelectedListener(listener);
    }

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
            if (((Employee) adapter.getItem(j)).FName.equals(autoString)
                    || ((Employee) adapter.getItem(j)).FItemID.equals(autoString)) {
                mSp.setSelection(j);
//                autoString = null;
                break;
            }
        }
    }

    public EmployeeSpAdapter getAdapter() {
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
