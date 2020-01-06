package com.fangzuo.assist.Activity.P2;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.ProductSearchActivity;
import com.fangzuo.assist.Beans.CheckLogSearchBean;
import com.fangzuo.assist.Beans.CodeCheckBackDataBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Dao.Client;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.widget.LoadingUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckLogActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.ed_client)
    EditText edClient;
    @BindView(R.id.tv_date_start)
    TextView tvDateStart;
    @BindView(R.id.tv_date_end)
    TextView tvDateEnd;
    private Client client;
    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg) {
            case EventBusInfoCode.Search_client:
                client = (Client) event.postEvent;
                edClient.setText(client.FName);
                break;
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_check_log);
        ButterKnife.bind(this);
        tvTitle.setText("查询条码记录");
        tvDateStart.setText(getTime(true));
        tvDateEnd.setText(getTime(true));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void OnReceive(String code) {
        edCode.setText(code);
        lockScan(0);
    }

    @OnClick({R.id.btn_back, R.id.search_client, R.id.tv_date_start, R.id.tv_date_end, R.id.btn_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.search_client:
                Bundle b = new Bundle();
                b.putString("search", edClient.getText().toString());
                b.putInt("where", Info.SEARCHCLIENT);
                startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.SEARCHFORRESULTCLIRNT, b);
                break;
            case R.id.tv_date_start:
                datePicker(tvDateStart);
                break;
            case R.id.tv_date_end:
                datePicker(tvDateEnd);
                break;
            case R.id.btn_check:
                goSearch();
                break;
        }
    }

    //构造查询json
    private void goSearch(){
        CheckLogSearchBean bean = new CheckLogSearchBean();
        if (!"".equals(edCode.getText().toString())){
            bean.FBarCode = edCode.getText().toString();
        }
//        if (null == client){
//            Toast.showText(mContext,"请选择客户名称");
//            return;
//        }
//        if (!"".equals(edCode.getText().toString())){
//            bean.FClient = edCode.getText().toString();
//        }
        bean.FClientID = ShareUtil.getInstance(mContext).getsetUserID();
        bean.FStartTime = tvDateStart.getText().toString();
        bean.FEndTime = tvDateEnd.getText().toString();
        CheckLogShowActivity.start(this,gson.toJson(bean));
    }


    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

}
