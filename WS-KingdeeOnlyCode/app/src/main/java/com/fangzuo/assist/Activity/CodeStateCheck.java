package com.fangzuo.assist.Activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CodeStateCheckBean;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.AnimUtil;
import com.fangzuo.assist.databinding.ActivityCodeStateCheckBinding;
import com.fangzuo.assist.widget.LoadingUtil;
import com.google.gson.Gson;

public class CodeStateCheck extends BaseActivity {

    private ActivityCodeStateCheckBinding binding;

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_code_state_check);
        binding.tb.tvTitle.setText("条码状态查询");

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        binding.tb.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void OnReceive(String code) {
        LoadingUtil.showDialog(mContext,"正在查询...");
        App.getRService().getCodeStateCheck(code, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                CodeStateCheckBean dBean = new Gson().fromJson(commonResponse.returnJson, CodeStateCheckBean.class);
                LoadingUtil.dismiss();
                if (binding.layoutShowdata.getVisibility()==View.GONE){
                    AnimUtil.FlipAnimatorXViewShow(binding.layoutNodata,binding.layoutShowdata,200);
                }
                binding.setCheckData(dBean);//设置数据到页面
            }

            @Override
            public void onError(Throwable e) {
                LoadingUtil.dismiss();
                if (binding.layoutNodata.getVisibility()==View.GONE){
                    AnimUtil.FlipAnimatorXViewShow(binding.layoutShowdata,binding.layoutNodata,200);
                }
//                    binding.tvTip.setText("查询失败:"+e.toString());
                    binding.tvTip.setText("查询失败");
            }
        });
    }
}
