package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CodeCheckBackDataBean;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UnBoxActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ed_code)
    EditText edCode;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_un_box);
        ButterKnife.bind(this);
        tvTitle.setText("拆箱单");
    }

    @Override
    protected void OnReceive(String code) {
        edCode.setText(code);
        showDlg();
        lockScan(0);
    }
    private void showDlg(){
        if ("".equals(edCode.getText().toString())){
            Toast.showText(mContext,"请输入条码");
            return;
        }
        new AlertDialog.Builder(mContext)
                .setTitle("是否进行拆箱："+edCode.getText().toString())
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        unBoxUpload();
                    }
                })
                .setNegativeButton("取消",null)
                .create().show();
    }
    private void unBoxUpload(){
        String json = edCode.getText().toString()+"|"+ ShareUtil.getInstance(mContext).getsetUserID()+"|"+"PDA";
        App.getRService().doIOAction("UnBoxUpload",json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (null != dBean && dBean.codeCheckBackDataBeans.size() > 0) {
                    LoadingUtil.showAlter(mContext,dBean.codeCheckBackDataBeans.get(0).FTip);
                } else {
                    LoadingUtil.showAlter(mContext,"拆箱失败,返回数据为空");
                }
            }
            @Override
            public void onError(Throwable e) {
                LoadingUtil.showAlter(mContext,"拆箱错误：" + e.toString());
            }
        });
    }

    @OnClick({R.id.btn_back, R.id.btn_unbox})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_unbox:
                showDlg();
                break;
        }
    }






    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }


}
