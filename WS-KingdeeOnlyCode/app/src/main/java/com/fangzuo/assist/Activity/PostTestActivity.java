package com.fangzuo.assist.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.PostBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.widget.LoadingUtil;
import com.loopj.android.http.AsyncHttpClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostTestActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    RelativeLayout btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ed_post_io)
    EditText edPostIo;
    @BindView(R.id.ed_body)
    EditText edBody;
    @BindView(R.id.ed_get_io)
    EditText edGetIo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_test);
        ButterKnife.bind(this);
        btnBack.setVisibility(View.GONE);
        tvTitle.setText("接口测试");
    }

    @OnClick({R.id.btn_post, R.id.btn_get})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_post:
                PostBean postBean = new PostBean();
                postBean.json = edBody.getText().toString();
                App.getRService().doIOAction4Post(edPostIo.getText().toString(),postBean, new MySubscribe<CommonResponse>() {
                    @Override
                    public void onNext(CommonResponse commonResponse) {
                        Lg.e("POST返回",commonResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Lg.e("POST_Error返回",e.getMessage());

                    }
                });
                break;
            case R.id.btn_get:
                Asynchttp.post(PostTestActivity.this, BasicShareUtil.getInstance(PostTestActivity.this).getBaseURL()+ edGetIo.getText().toString(), "", new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        Lg.e("GET返回",cBean);
                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        Lg.e("GET_error返回",Msg);

                    }
                });
                break;
        }
    }
}
