package com.fangzuo.assist.Activity.P2;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.StoreSearchRyAdapter;
import com.fangzuo.assist.Beans.CheckLogBackBean;
import com.fangzuo.assist.Beans.CheckLogSearchBean;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckStoreActivity extends BaseActivity implements StoreSearchRyAdapter.OnItemClickListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.ed_storage)
    EditText edStorage;
    @BindView(R.id.ry_search)
    RecyclerView rySearch;
    @BindView(R.id.btn_check)
    Button btnCheck;
    private ArrayList<CheckLogBackBean> containerForProduct;
    private StoreSearchRyAdapter adapter;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_check_store);
        ButterKnife.bind(this);
        tvTitle.setText("库存查询");
    }

    @Override
    protected void initData() {
        containerForProduct = new ArrayList<>();
        adapter = new StoreSearchRyAdapter(mContext, containerForProduct);
        rySearch.setAdapter(adapter);
        rySearch.setItemAnimator(new DefaultItemAnimator());
        rySearch.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        adapter.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void OnReceive(String code) {

    }

    @OnClick({R.id.btn_back, R.id.btn_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_check:
                getData();
                break;
        }
    }

    private void getData() {
        CheckLogSearchBean bean = new CheckLogSearchBean();
        bean.FNumber = edCode.getText().toString();
        bean.FName = edName.getText().toString();
        bean.FStorage = edStorage.getText().toString();
        bean.FUserID = ShareUtil.getInstance(mContext).getsetUserID();
        LoadingUtil.showDialog(mContext, "正在查找...");
        App.getRService().doIOAction(WebApi.CheckStoreSearch1, gson.toJson(bean), new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                LoadingUtil.dismiss();
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (null != dBean && dBean.checkLogBackBeans.size() > 0) {
                    adapter.addAll(dBean.checkLogBackBeans);
                    adapter.notifyDataSetChanged();
                    btnCheck.setText("查询:"+dBean.checkLogBackBeans.size());
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
                Toast.showText(mContext, "查询条码历史数据失败，" + e.getMessage());
//                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.CodeInsert_Error, new CodeCheckBackDataBean("查找条码信息错误" + e.toString())));
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        CheckLogBackBean backBean = adapter.getAllData().get(position);
        CheckLogSearchBean bean = new CheckLogSearchBean();
        bean.FStockID = backBean.FStockID;
        bean.FItemID = backBean.FItemID;
        CheckStoreShowActivity.start(mContext, gson.toJson(bean));

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

}
