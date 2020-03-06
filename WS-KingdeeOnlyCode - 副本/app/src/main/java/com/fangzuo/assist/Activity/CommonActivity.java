package com.fangzuo.assist.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.Info;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommonActivity extends BaseActivity {


    @BindView(R.id.btn_back)
    RelativeLayout btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.cb_first_inout)
    CheckBox cbFirstInout;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_common);
        ButterKnife.bind(this);
        mContext = this;
    }

    @Override
    protected void initData() {
        if (Hawk.get(Info.FirstInOut,"0").equals("0")){//关闭
            cbFirstInout.setChecked(false);
        }else{
            cbFirstInout.setChecked(true);//开启
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void OnReceive(String code) {

    }


    public static void start(Context context) {
        Intent starter = new Intent(context, CommonActivity.class);
//        starter.putExtra("code", code);
//        starter.putStringArrayListExtra("fid", fid);
        context.startActivity(starter);
    }

    @OnClick({R.id.btn_back, R.id.ll_first_inout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.ll_first_inout:
                if (cbFirstInout.isChecked()) {
                    cbFirstInout.setChecked(false);
                }else{
                    cbFirstInout.setChecked(true);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (cbFirstInout.isChecked()){
            Hawk.put(Info.FirstInOut,"1");//开启
        }else{
            Hawk.put(Info.FirstInOut,"0");//关闭
        }

        super.onDestroy();
    }
}
