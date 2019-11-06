package com.fangzuo.assist.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzuo.assist.Adapter.CarNoScAdapter;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Dao.Employee;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.EmployeeDao;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;


public class CarNoSearch extends RelativeLayout {
    // 返回按钮控件
    private EditText mSearch;
    // 标题Tv
    private Button button;
    private TextView textView;
    private EasyRecyclerView recyclerView;
    private CarNoScAdapter adapter;
    private final DaoSession daoSession;
    private List<Employee> employees;
    private Employee employee;
    private EmployeeDao employeeDao;
    private static BasicShareUtil share;
    private boolean isFristInput = true;
    private String autoString;//用于联网时，再次去自动设置值
    List<String> carBillNoList;
    private String thisNo;

    public CarNoSearch(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_car_no_search, this);
        daoSession = GreenDaoManager.getmInstance(context).getDaoSession();
        employees = new ArrayList<>();
        share = BasicShareUtil.getInstance(context);
        carBillNoList = new ArrayList<>();
        // 获取控件
        mSearch = (EditText) findViewById(R.id.et_search);
        button = (Button) findViewById(R.id.btn_search);
        textView = (TextView) findViewById(R.id.tv_name);
        recyclerView = findViewById(R.id.ry_search);
        recyclerView.setAdapter(adapter = new CarNoScAdapter(context));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        employeeDao = daoSession.getEmployeeDao();

        TypedArray attrArray = context.obtainStyledAttributes(attributeSet, R.styleable.CarNoSearch);
        int count = attrArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attrName = attrArray.getIndex(i);
            switch (attrName) {
                case R.styleable.CarNoSearch_car_etHint://输入框提示文
                    mSearch.setHint(attrArray.getString(R.styleable.CarNoSearch_car_etHint));
                    break;
                case R.styleable.CarNoSearch_car_tvName://左侧文字
                    textView.setText(attrArray.getString(R.styleable.CarNoSearch_car_tvName));
                    break;
                case R.styleable.CarNoSearch_car_firstinput://是否首次自动输入
                    isFristInput = attrArray.getBoolean(R.styleable.CarNoSearch_car_firstinput, true);
                    break;
            }
        }
        attrArray.recycle();

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearch.setText("");
                thisNo = null;
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Updata_CarNo,getString()));
                recyclerView.setVisibility(GONE);

            }
        });

        mSearch.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                clear();
                if (carBillNoList.size()>0){
                    if (b){
                        recyclerView.setVisibility(VISIBLE);
                        adapter.addAll(carBillNoList);
                    }else{
                        if ("".equals(getString())){
                            thisNo = carBillNoList.get(0);
//                        Lg.e("焦点消失，"+employee.toString());
                            mSearch.setText(thisNo);
                            recyclerView.setVisibility(GONE);
                            EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Updata_CarNo,getString()));
                        }

                    }
                }else{
                    mSearch.setText("");
                    recyclerView.setVisibility(GONE);
                }
            }
        });

        //输入框文字变化监听
        mSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
//                Log.e("输入过程中执行该方法", "文字变化");
                clear();
                for (String string:carBillNoList) {
                    if (string.contains(s)){
                        adapter.add(string);
                    }
                }
                if (adapter.getAllData().size()<=0){
                    thisNo=null;
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Updata_CarNo,getString()));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // 输入前的监听
//                Log.e("输入前确认执行该方法", "开始输入");
                recyclerView.setVisibility(VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
//                Log.e("输入结束执行该方法", "输入结束");
            }
        });

        //列表点击事件
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                thisNo = adapter.getAllData().get(position);
                mSearch.setText(thisNo);
                recyclerView.setVisibility(GONE);
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Updata_CarNo,thisNo));

            }
        });

    }

    //返回对象
    public String getString() {
        return thisNo==null?"":thisNo;
    }

    public void setListData(ArrayList<String> carBillNoList){
//        this.carBillNoList.clear();
        this.carBillNoList = carBillNoList;
    }

    //清空
    private void clear() {
        adapter.clear();
    }

    public String getText(){
        return mSearch.getText().toString();
    }
    public void setText(String str){
       mSearch.setText(str);
    }

    public boolean hasFocus(){
        if (mSearch.hasFocus()){
            return true;
        }else{
            return false;
        }
    }

}
