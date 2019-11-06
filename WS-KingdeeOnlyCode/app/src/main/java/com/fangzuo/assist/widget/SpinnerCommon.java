package com.fangzuo.assist.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.Beans.CommonBean;
import com.fangzuo.assist.Beans.CommonSpAdapter;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.greendao.gen.DaoSession;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
/*
*
<com.fangzuo.assist.widget.SpinnerCommon
                android:id="@+id/sp_db_direction"
                app:manspinner_name="调拨类别:"
                app:manspinner_title="请选择调拨类别"
                app:manspinner_size="12sp"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"/>
*
*
* */

public class SpinnerCommon extends RelativeLayout {
    // 返回按钮控件
    private Spinner mSp;
    // 标题Tv
    private TextView mTitleTv;
    private boolean showEd = false;
    //    private SpinnerAdapter adapter;
    private DaoSession daoSession;
    private ArrayList<String> autoList;
    private BasicShareUtil share;
    private ArrayList<CommonBean> container;
    private CommonSpAdapter adapter;
    private String autoString="";//用于联网时，再次去自动设置值
    private String saveKeyString="";//用于保存数据的key
    private String employeeId="";
    private String employeeName="";
    private String employeeNumber="";
    private String T="常用数据：";     //3


    public SpinnerCommon(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_common_spinner, this);
        daoSession = GreenDaoManager.getmInstance(context).getDaoSession();

        autoList = new ArrayList<>();
        share = BasicShareUtil.getInstance(context);
        container = new ArrayList<>();
        // 获取控件
        mSp = (Spinner) findViewById(R.id.sp);
        mTitleTv = (TextView) findViewById(R.id.tv);
        TypedArray attrArray = context.obtainStyledAttributes(attributeSet, R.styleable.ManSpinner);
        int count = attrArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attrName = attrArray.getIndex(i);
            switch (attrName) {
                case R.styleable.ManSpinner_manspinner_name:
                    mTitleTv.setText(attrArray.getString(R.styleable.ManSpinner_manspinner_name));
                    break;
                case R.styleable.ManSpinner_manspinner_title:
                    mSp.setPrompt(attrArray.getString(R.styleable.ManSpinner_manspinner_title));
                    break;
                case R.styleable.ManSpinner_manspinner_size:
                    mTitleTv.setText(attrArray.getString(R.styleable.ManSpinner_manspinner_name));
                    mTitleTv.setTextSize(attrArray.getDimension(R.styleable.ManSpinner_manspinner_size,15));
                    break;
            }
        }
        attrArray.recycle();
        adapter = new CommonSpAdapter(context, container);
        mSp.setAdapter(adapter);

        mSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CommonBean employee = (CommonBean) adapter.getItem(i);
                employeeName = employee.FName;
                employeeNumber = employee.FNumber;
                Lg.e("选中123"+T,employee);
                Hawk.put(saveKeyString,employee.FName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

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

    public void setEnable(boolean b){
        mSp.setEnabled(b);
    }

    public String getDataId() {
        return employeeId == null ? "" : employeeId;
    }

    public String getDataName() {
        return employeeName == null ? "" : employeeName;
    }
    public String getDataNumber() {
        return employeeNumber == null ? "" : employeeNumber;
    }
    public CommonSpAdapter getAdapter() {
        return adapter;
    }
    public void setData(String what,String saveKeyStr,String string){

//        if (Info.Type_DB_type.equals(what)){
//            container.add(new CommonBean("组织内调拨","InnerOrgTransfer"));
//            container.add(new CommonBean("跨组织调拨","OverOrgTransfer"));
//        }else if (Info.Type_DB_direction.equals(what)){
//            container.add(new CommonBean("普通","GENERAL"));
//            container.add(new CommonBean("退货","RETURN"));
//        }else if (Info.Type_Hz_type.equals(what)){
//            container.add(new CommonBean("业务组织","BD_OwnerOrg"));
//            container.add(new CommonBean("供应商","BD_Supplier"));
//        }
        mSp.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void setData(String what){
//        if (Info.Type_DB_type.equals(what)){
//            container.add(new CommonBean("组织内调拨","InnerOrgTransfer"));
//            container.add(new CommonBean("跨组织调拨","OverOrgTransfer"));
//        }else if (Info.Type_DB_direction.equals(what)){
//            container.add(new CommonBean("普通","GENERAL"));
//            container.add(new CommonBean("退货","RETURN"));
//        }else if (Info.Type_Hz_type.equals(what)){
//            container.add(new CommonBean("业务组织","BD_OwnerOrg"));
//            container.add(new CommonBean("供应商","BD_Supplier"));
//        }else if (Info.Type_Hz_type_All.equals(what)){
//            container.add(new CommonBean("业务组织","BD_OwnerOrg"));
//            container.add(new CommonBean("供应商","BD_Supplier"));
//            container.add(new CommonBean("客户","BD_Customer"));
//        }
        mSp.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    /**
     *
     * @param saveKeyStr        用于保存的key
     * @param string            自动设置的z值
     * */
    public void setAutoSelection(String saveKeyStr,String string) {
        Lg.e("调拨自动："+string);
        saveKeyString =saveKeyStr;
        autoString = string;
        if ("".equals(string)&&!"".equals(saveKeyStr)){
            autoString = Hawk.get(saveKeyString,"");
        }
        for (int j = 0; j < adapter.getCount(); j++) {
            if (((CommonBean) adapter.getItem(j)).FNumber.equals(autoString)
                    || ((CommonBean) adapter.getItem(j)).FName.equals(autoString)) {
                mSp.setSelection(j);
//                autoString = null;
                break;
            }
        }
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
