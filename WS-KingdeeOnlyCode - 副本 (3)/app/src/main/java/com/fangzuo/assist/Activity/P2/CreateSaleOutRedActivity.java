package com.fangzuo.assist.Activity.P2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Activity.ReView4ScanCheckActivity;
import com.fangzuo.assist.Beans.CodeCheckBackDataBean;
import com.fangzuo.assist.Beans.CodeCheckBean;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.DataModel;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MediaPlayer;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.assist.zxing.CustomCaptureActivity;
import com.fangzuo.assist.zxing.activity.CaptureActivity;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateSaleOutRedActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.btn_backorder)
    Button btnBackorder;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.isAutoAdd)
    CheckBox isAutoAdd;
    private CodeCheckBackDataBean codeCheckBackDataBean;
    private int activity = Config.CreateSaleOutRedActivity;

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg) {
            case EventBusInfoCode.ScanResult://
                BarcodeResult res = (BarcodeResult) event.postEvent;
                edCode.setText(barcode);
                OnReceive(res.getResult().getText());
                break;
            case EventBusInfoCode.CodeCheck_OK://检测条码成功
                codeCheckBackDataBean = (CodeCheckBackDataBean) event.postEvent;
                Lg.e("条码检查：", codeCheckBackDataBean);
                tvName.setText(codeCheckBackDataBean.FName);
                tvNumber.setText(codeCheckBackDataBean.FNumber);
//                edPihao.setText(codeCheckBackDataBean.FBatchNo);
//                edNum.setText(codeCheckBackDataBean.FQty);
                LoadingUtil.dismiss();
//                setDATA(codeCheckBackDataBean.FItemID,false);

                if (isAutoAdd.isChecked()) {
                    AddorderBefore();
                }else{
                    lockScan(0);
                }
                break;
            case EventBusInfoCode.CodeCheck_Error://检测条码失败
                LoadingUtil.dismiss();
                lockScan(0);
                codeCheckBackDataBean = (CodeCheckBackDataBean) event.postEvent;
                Toast.showText(mContext, codeCheckBackDataBean.FTip);
                break;
            case EventBusInfoCode.CodeInsert_OK://写入条码唯一表成功
                Addorder();
                break;
            case EventBusInfoCode.CodeInsert_Error://写入条码唯一表失败
                codeCheckBackDataBean = (CodeCheckBackDataBean) event.postEvent;
                Toast.showText(mContext, codeCheckBackDataBean.FTip);
                lockScan(0);
                break;
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_create_sale_out);
        ButterKnife.bind(this);
        tvTitle.setText("销售出库红字");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        btnBackorder.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                upLoad();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String barcode = "";

    @Override
    protected void OnReceive(String code) {
        barcode = code;
        LoadingUtil.showDialog(mContext, "正在查找...");
        //查询条码唯一表
        CodeCheckBean bean = new CodeCheckBean(barcode);
        bean.FUserID = ShareUtil.getInstance(mContext).getsetUserID();
        DataModel.codeCheck(WebApi.CodeCheck4SaleoutRed, gson.toJson(bean));
    }

    @OnClick({R.id.btn_back, R.id.scanbyCamera, R.id.btn_add, R.id.btn_checkorder, R.id.search_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.scanbyCamera:
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                // 设置自定义扫描Activity
                intentIntegrator.setCaptureActivity(CustomCaptureActivity.class);
                intentIntegrator.initiateScan();
//                Intent in = new Intent(mContext, CaptureActivity.class);
//                startActivityForResult(in, 0);
                break;
            case R.id.btn_add:
                AddorderBefore();
                break;
            case R.id.btn_checkorder:
                Bundle b2 = new Bundle();
                b2.putInt("activity", activity);
                startNewActivity(ReView4ScanCheckActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, false, b2);
                break;
            case R.id.search_code:
                if ("".equals(edCode.getText().toString())){
                    Toast.showText(mContext,"请输入需要查询的条码");
                    return;
                }
                OnReceive(edCode.getText().toString());
                break;
        }
    }

    private void AddorderBefore() {
        if ("".equals(tvName.getText().toString())) {
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "请先查询出物料信息");
            lockScan(0);
            return;
        }
        if (null == codeCheckBackDataBean) {
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "请进行条码验证后添加");
            lockScan(0);
            return;
        }

        //插入条码唯一临时表
        CodeCheckBean bean = new CodeCheckBean(barcode, "0", BasicShareUtil.getInstance(mContext).getIMIE());
        bean.FUserID = ShareUtil.getInstance(mContext).getsetUserID();
        bean.FQty = codeCheckBackDataBean.FQty == null ? "-1" : "-"+codeCheckBackDataBean.FQty;
        DataModel.codeInsert(WebApi.CodeInsert4SaleoutRed, gson.toJson(bean));
    }

    private void Addorder() {
        String second = getTimesecond();
        T_Detail t_detail = new T_Detail();
        t_detail.FIndex = second;
        t_detail.FBarcode = barcode;
        t_detail.MakerId = share.getsetUserID();
        t_detail.IMIE = getIMIE();
        t_detail.activity = activity;
        t_detail.FQuantity = codeCheckBackDataBean.FQty == null ? "1" : codeCheckBackDataBean.FQty;
        t_detail.FProductId = codeCheckBackDataBean.FItemID;
        t_detail.FUnitId = codeCheckBackDataBean.FUnitID;
        t_detail.FStorageId = codeCheckBackDataBean.FStockID;
        t_detail.FPositionId = codeCheckBackDataBean.FStockPlaceID;
        t_detail.FBatch = codeCheckBackDataBean.FBatchNo;
        t_detail.FKFDate = codeCheckBackDataBean.FKFDate;
        t_detail.FKFPeriod = codeCheckBackDataBean.FKFPeriod;
        t_detail.FTaxUnitPrice = codeCheckBackDataBean.FPrice;
        t_detail.FProductName = tvName.getText().toString();
        t_detail.FProductCode = tvNumber.getText().toString();
        t_detail.FOrderId = 0;
        t_detail.FRate = codeCheckBackDataBean.FOLOrderBillNo;
        long insert = t_detailDao.insert(t_detail);

        if (insert > 0) {
            MediaPlayer.getInstance(mContext).ok();
            Toast.showText(mContext, "添加成功");
            resetAll();
        } else {
            lockScan(0);
            Toast.showText(mContext, "添加失败，请重试");
            MediaPlayer.getInstance(mContext).error();
        }
    }

    private void upLoad() {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        PurchaseInStoreUploadBean.purchaseInStore puBean = pBean.new purchaseInStore();
        t_mainDao = daoSession.getT_mainDao();
        t_detailDao = daoSession.getT_DetailDao();
        ArrayList<String> detailContainer = new ArrayList<>();
        ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data = new ArrayList<>();
        detailContainer = new ArrayList<>();
        puBean = pBean.new purchaseInStore();
        String main;
        String detail = "";
        puBean.main = ShareUtil.getInstance(mContext).getsetUserID() + "|0|" + BasicShareUtil.getInstance(mContext).getIMIE();
        List<T_Detail> details = DataModel.mergeDetail4CSO(mContext, activity);

//                List<T_Detail> details = t_detailDao.queryBuilder().where(
//                        T_DetailDao.Properties.FOrderId.eq(t_main.orderId),
//                        T_DetailDao.Properties.Activity.eq(activity)).build().list();
        for (int j = 0; j < details.size(); j++) {
            if (j != 0 && j % 49 == 0) {
                Log.e("j%49", j % 49 + "");
                T_Detail t_detail = details.get(j);
                detail = detail +
                        t_detail.FProductId + "|" +
                        t_detail.FUnitId + "|" +
                        t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" +
                        t_detail.FStorageId + "|" +
                        t_detail.FPositionId + "|" +
                        t_detail.FBatch + "|" +
                        t_detail.FKFDate + "|" +
                        t_detail.FKFPeriod + "|" +
                        t_detail.FRate + "|";
                detail = detail.subSequence(0, detail.length() - 1).toString();
                detailContainer.add(detail);
                detail = "";
            } else {
                Log.e("j", j + "");
                Log.e("details.size()", details.size() + "");
                T_Detail t_detail = details.get(j);
                detail = detail +
                        t_detail.FProductId + "|" +
                        t_detail.FUnitId + "|" +
                        t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" +
                        t_detail.FStorageId + "|" +
                        t_detail.FPositionId + "|" +
                        t_detail.FBatch + "|" +
                        t_detail.FKFDate + "|" +
                        t_detail.FKFPeriod + "|" +
                        t_detail.FRate + "|";
                Log.e("detail1", detail);
            }

        }
        if (detail.length() > 0) {
            detail = detail.subSequence(0, detail.length() - 1).toString();
        }

        Log.e("detail", detail);
        detailContainer.add(detail);
        puBean.detail = detailContainer;
        data.add(puBean);
        pBean.list = data;

        LoadingUtil.showDialog(mContext, "正在回单...");
        App.getRService().doIOAction(WebApi.CreateSORedUpload, gson.toJson(pBean), new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                super.onNext(commonResponse);
                if (!commonResponse.state) return;
                t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(
                        T_DetailDao.Properties.Activity.eq(activity)
                ).build().list());
                LoadingUtil.showAlter(mContext, "回单成功");
                LoadingUtil.dismiss();
                MediaPlayer.getInstance(mContext).ok();

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Toast.showText(mContext, e.getMessage());
                LoadingUtil.dismiss();
                MediaPlayer.getInstance(mContext).error();
            }
        });
    }


    private void resetAll() {
        tvName.setText("");
        tvNumber.setText("");
        setfocus(edCode);
        lockScan(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("code", requestCode + "" + "    " + resultCode);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                String message = b.getString("result");
                edCode.setText(message);
                Toast.showText(mContext, message);
                OnReceive(message);
//                edCode.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
            }
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

}