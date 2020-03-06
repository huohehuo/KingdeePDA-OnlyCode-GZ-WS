package com.fangzuo.assist.Activity.P2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.LogSearchRyAdapter;
import com.fangzuo.assist.Adapter.StoreSearch2RyAdapter;
import com.fangzuo.assist.Beans.CheckLogBackBean;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckStoreShowActivity extends BaseActivity implements StoreSearch2RyAdapter.OnItemClickListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_all_num)
    TextView tvAllNum;
    @BindView(R.id.ry_search)
    RecyclerView rySearch;
    @BindView(R.id.btn_back)
    RelativeLayout btnBack;

    private ArrayList<CheckLogBackBean> containerForProduct;
    private StoreSearch2RyAdapter adapter;
    private String searchJson;
    private double allNum=0d;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_check_store_show);
        ButterKnife.bind(this);
        tvTitle.setText("查询库存记录");
        //接收
        Intent intent = getIntent();
        if (intent != null) {
            searchJson = intent.getStringExtra("json");
//            fidcontainer = intent.getStringArrayListExtra("fid");
            Lg.e("Intent:" + searchJson);
//            Lg.e("Intent:"+fidcontainer.toString());
        }
    }

    @Override
    protected void initData() {
        containerForProduct = new ArrayList<>();
        adapter = new StoreSearch2RyAdapter(mContext, containerForProduct);
        rySearch.setAdapter(adapter);
        rySearch.setItemAnimator(new DefaultItemAnimator());
        rySearch.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        adapter.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();
        if (null != searchJson) {
            getLogData();
        }
    }

    @Override
    protected void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void OnReceive(String code) {

    }
    private void getLogData() {
        LoadingUtil.showDialog(mContext,"正在查找...");
        App.getRService().doIOAction(WebApi.CheckStoreSearch2, searchJson, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                LoadingUtil.dismiss();
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (null != dBean && dBean.checkLogBackBeans.size() > 0) {
                    adapter.addAll(dBean.checkLogBackBeans);
                    adapter.notifyDataSetChanged();
                    tvTitle.setText("查询库存记录："+dBean.checkLogBackBeans.size());
                    dealNum(dBean.checkLogBackBeans);
                } else {
                    adapter.clear();
                    Toast.showText(mContext, "查无数据");
                    tvTitle.setText("查询库存记录：0");
//                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_Error,new CodeCheckBackDataBean("找不到条码信息")));
                }
            }

            @Override
            public void onError(Throwable e) {
                LoadingUtil.dismiss();
                Toast.showText(mContext,"查询条码历史数据失败，"+e.getMessage());
//                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_Error, new CodeCheckBackDataBean("查找条码信息错误" + e.toString())));
            }
        });
    }
    private void dealNum(ArrayList<CheckLogBackBean> checkLogBackBeans){
        for (CheckLogBackBean bean:checkLogBackBeans) {
            allNum+= MathUtil.toD(bean.FNum);
        }
        tvAllNum.setText("汇总："+allNum);
    }

    @Override
    public void onItemClick(View view, int position) {

//        CheckLogShow2Activity.start(mContext,adapter.getAllData().get(position).FInterID);
//        product = adapter.getAllData().get(position);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }


    public static void start(Context context, String json) {
        Intent starter = new Intent(context, CheckStoreShowActivity.class);
        starter.putExtra("json", json);
//        starter.putStringArrayListExtra("fid", fid);
        context.startActivity(starter);
    }

}
