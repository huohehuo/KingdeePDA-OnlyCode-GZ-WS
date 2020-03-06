package com.fangzuo.assist.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CodeCheckBackDataBean;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.MediaPlayer;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePriceActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.etStorage)
    EditText etStorage;
    @BindView(R.id.et_price)
    EditText etPrice;
    private Storage storage;
    private Product product;
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg) {
            case EventBusInfoCode.PRODUCTRETURN:
                product = (Product) event.postEvent;
                Lg.e("物料",product);
                etCode.setText(product.FName);
                break;
            case EventBusInfoCode.Search_Storage:
                storage = (Storage) event.postEvent;
                Lg.e("仓库",storage);
                etStorage.setText(storage.FName);
                break;
        }
    }
    @Override
    protected void initView() {
        setContentView(R.layout.activity_change_price);
        ButterKnife.bind(this);
        tvTitle.setText("调价单");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void OnReceive(String code) {

    }

    @OnClick({R.id.btn_back, R.id.btn_clear, R.id.check, R.id.btn_clear2, R.id.check_storage, R.id.btn_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_clear:
                etCode.setText("");
                break;
            case R.id.check:
                Bundle b1 = new Bundle();
                b1.putString("search", etCode.getText().toString());
                b1.putInt("where", Info.SEARCHPRODUCT);
                startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.SEARCHFORRESULT, b1);
                break;
            case R.id.btn_clear2:
                etStorage.setText("");
                break;
            case R.id.check_storage:
                Bundle bss = new Bundle();
                bss.putString("search", etStorage.getText().toString());
                bss.putInt("where", Info.Search_Storage);
                startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.Search_Storage, bss);
                break;
            case R.id.btn_change:
                upLoad();
                break;
        }
    }
    private void upLoad(){
        if ("".equals(etStorage.getText().toString()) ||null ==storage){
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "请选择仓库");
            return;
        }
        if ("".equals(etCode.getText().toString()) || null == product){
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "请选择物料");
            return;
        }
        if (MathUtil.toD(etPrice.getText().toString())<=0){
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "请输入价格");
            return;
        }
        LoadingUtil.showDialog(mContext,"正在执行...");
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(ShareUtil.getInstance(mContext).getsetUserID()).append("|")
                .append(product.FItemID).append("|")
                .append(storage.FItemID).append("|")
                .append(etPrice.getText().toString());

        App.getRService().doIOAction(WebApi.ChangePriceUpload, stringBuffer.toString(), new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                super.onNext(commonResponse);
                if (!commonResponse.state)return;
                LoadingUtil.dismiss();
                etPrice.setText("");
                LoadingUtil.showAlter(mContext,"执行成功");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LoadingUtil.dismiss();
                Toast.showText(mContext,"执行失败"+e.getMessage());
            }
        });

    }
}
