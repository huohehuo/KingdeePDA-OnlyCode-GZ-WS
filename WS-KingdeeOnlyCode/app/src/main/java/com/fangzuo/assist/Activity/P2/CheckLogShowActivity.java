package com.fangzuo.assist.Activity.P2;

import android.content.Context;
import android.content.Intent;
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
import com.fangzuo.assist.Beans.CheckLogBackBean;
import com.fangzuo.assist.Beans.CodeCheckBackDataBean;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckLogShowActivity extends BaseActivity implements LogSearchRyAdapter.OnItemClickListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ry_search)
    RecyclerView rySearch;
    @BindView(R.id.btn_back)
    RelativeLayout btnBack;
    private ArrayList<CheckLogBackBean> containerForProduct;
    private LogSearchRyAdapter adapter;
    private String searchJson;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_check_log_show);
        ButterKnife.bind(this);
        tvTitle.setText("查询条码记录");
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
        adapter = new LogSearchRyAdapter(mContext, containerForProduct);
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
        App.getRService().doIOAction(WebApi.CheckLogSearch1, searchJson, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                LoadingUtil.dismiss();
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (null != dBean && dBean.checkLogBackBeans.size() > 0) {
                    adapter.addAll(dBean.checkLogBackBeans);
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.clear();
                    Toast.showText(mContext, "查无数据");
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

    @Override
    public void onItemClick(View view, int position) {

        CheckLogShow2Activity.start(mContext,adapter.getAllData().get(position).FInterID);
//        product = adapter.getAllData().get(position);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }


    public static void start(Context context, String json) {
        Intent starter = new Intent(context, CheckLogShowActivity.class);
        starter.putExtra("json", json);
//        starter.putStringArrayListExtra("fid", fid);
        context.startActivity(starter);
    }

}
