package com.fangzuo.assist.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Dao.CheckResult;
import com.fangzuo.assist.Dao.PushDownMain;
import com.fangzuo.assist.Dao.PushDownSub;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.DoubleUtil;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.MediaPlayer;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.assist.widget.SpinnerCheckType;
import com.fangzuo.assist.widget.SpinnerPeople;
import com.fangzuo.assist.widget.SpinnerPeople2;
import com.fangzuo.greendao.gen.PushDownMainDao;
import com.fangzuo.greendao.gen.PushDownSubDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddCheckActivity extends BaseActivity {

    private int activity = Config.PdShouLiao2LLCheckActivity;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.ed_num_check)
    EditText edNumCheck;
    @BindView(R.id.ed_num_pass)
    EditText edNumPass;
    @BindView(R.id.ed_num_broken)
    EditText edNumBroken;
    @BindView(R.id.ed_result)
    EditText edResult;
    @BindView(R.id.sp_checkType)
    SpinnerCheckType spCheckType;
    @BindView(R.id.sp_send_man)
    SpinnerPeople2 spSendMan;
    @BindView(R.id.sp_check_man)
    SpinnerPeople spCheckMan;
    @BindView(R.id.btn_add)
    Button btnAdd;

    private PushDownSubDao pushDownSubDao;
    private PushDownSub pushDownSub;
    private String FInterID;
    private String FEntryID;
    private CheckResult checkResult;
    private Long ordercode;
    private int tag;
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg){
            case EventBusInfoCode.Search_CheckResult:
                checkResult = (CheckResult)event.postEvent;
                edResult.setText(checkResult.FName);
                break;

//            case EventBusInfoCode.Upload_OK://回单成功
//                t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(
//                        T_DetailDao.Properties.Activity.eq(activity)
//                ).build().list());
//                t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(
//                        T_mainDao.Properties.Activity.eq(activity)
//                ).build().list());
//                for (int i = 0; i < fidc.size(); i++) {
//                    List<PushDownSub> pushDownSubs = pushDownSubDao.queryBuilder().where(
//                            PushDownSubDao.Properties.FInterID.eq(fidc.get(i))
//                    ).build().list();
//                    pushDownSubDao.deleteInTx(pushDownSubs);
//                    List<PushDownMain> pushDownMains = pushDownMainDao.queryBuilder().where(
//                            PushDownMainDao.Properties.FInterID.eq(fidc.get(i))
//                    ).build().list();
//                    pushDownMainDao.deleteInTx(pushDownMains);
//                }
//                MediaPlayer.getInstance(mContext).ok();
//                btnBackorder.setClickable(true);
//                LoadingUtil.dismiss();
//                Bundle b = new Bundle();
//                b.putInt("123",tag);
//                startNewActivity(PushDownPagerActivity.class,0,0,true,b);
//
////                btnBackorder.setClickable(true);
////                LoadingUtil.dismiss();
//                break;
//            case EventBusInfoCode.Upload_Error://回单失败
//                String error = (String)event.postEvent;
//                Toast.showText(mContext, error);
//                btnBackorder.setClickable(true);
//                LoadingUtil.dismiss();
//                MediaPlayer.getInstance(mContext).error();
//                break;
        }
    }
    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_check);
        ButterKnife.bind(this);
        pushDownSubDao = daoSession.getPushDownSubDao();
        //接收
        Intent intent = getIntent();
        if (intent != null) {
            FInterID = intent.getStringExtra("FInterID");
            FEntryID = intent.getStringExtra("FEntryID");
            ordercode = intent.getLongExtra("orderid",0);
            tag = intent.getIntExtra("tag",0);
            List<PushDownSub> list = pushDownSubDao.queryBuilder().where(
                    PushDownSubDao.Properties.Tag.eq(tag),
                    PushDownSubDao.Properties.FInterID.eq(FInterID),
                    PushDownSubDao.Properties.FEntryID.eq(FEntryID)
            ).build().list();
            if (list.size() > 0) pushDownSub = list.get(0);
//            backBus = intent.getStringExtra("backBus");
//            where = intent.getIntExtra("where",0);
//            fidcontainer = intent.getStringArrayListExtra("fid");
//            Lg.e("Intent:"+fidcontainer.toString());
        }

    }

    @Override
    protected void initData() {

        spSendMan.setAutoSelection("", share.getUserName());
        spCheckMan.setAutoSelection("", "荔湾工厂");
        spCheckType.setAutoSelection(Config.People3 + activity, "");
        if (null != pushDownSub) {
            edNumCheck.setText(pushDownSub.FAuxQty);
            edNumBroken.setText("0");
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void OnReceive(String code) {

    }

    @OnClick({R.id.btn_back, R.id.btn_add, R.id.search_result})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                this.overridePendingTransition(0, R.anim.bottom_end);
                break;
            case R.id.btn_add:
                addCheck();
                break;
            case R.id.search_result:
                Bundle b1 = new Bundle();
                b1.putString("search", edResult.getText().toString());
                b1.putInt("where", Info.Search_CheckResult);
                startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.Search_CheckResult, b1);
                break;
        }
    }

    private void addCheck() {
        if (MathUtil.toD(edNumCheck.getText().toString()) <=0 ||
            MathUtil.toD(edNumPass.getText().toString()) <=0
                ) {
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "检验数量、样品破坏数 或 合格数 不能为空");
            lockScan(0);
            return;
        }
        if ("".equals(spSendMan.getEmployeeId())){
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "请选择送检人");
            lockScan(0);
            return;
        }
        if (null==checkResult || "".equals(edResult.getText().toString())){
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "请选择质检结果");
            lockScan(0);
            return;
        }

        if (MathUtil.toD(edNumCheck.getText().toString()) > MathUtil.toD(pushDownSub.FAuxQty)) {
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "检验数量不能大于验收数量");
            lockScan(0);
            return;
        }
        if (MathUtil.sum(edNumBroken.getText().toString(),edNumPass.getText().toString())>MathUtil.toD(edNumCheck.getText().toString())) {
            MediaPlayer.getInstance(mContext).error();
            Toast.showText(mContext, "合格数量+ 样品破坏数的和不能大于检验数量");
            lockScan(0);
            return;
        }


        t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(T_mainDao.Properties.OrderId.eq(ordercode)).build().list());
        String second = getTimesecond();
        T_main t_main = new T_main();
        t_main.FIndex = second;
        t_main.orderId = ordercode;
        t_main.FMaker = share.getUserName();
        t_main.FMakerId = share.getsetUserID();
        t_main.FDeliveryType = pushDownSub.FInterID;
        t_main.activity = activity;
        t_main.IMIE =  getIMIE();
        long insert1 = t_mainDao.insert(t_main);

        T_Detail t_detail = new T_Detail();
        t_detail.FBillNo = pushDownSub.FBillNo;
        t_detail.FOrderId = ordercode;
        t_detail.FProductId = pushDownSub.FItemID;
        t_detail.FProductName = pushDownSub.FName;
        t_detail.model = pushDownSub.FModel;
//            t_detail.FProductCode = product.FNumber;
        t_detail.FIndex = second;
        t_detail.FMan1 = spCheckMan.getEmployeeId();
        t_detail.FMan2 = spSendMan.getEmployeeId();
        t_detail.FCheckBrokenNum =MathUtil.toD(edNumBroken.getText().toString())<=0?"0":edNumBroken.getText().toString();
        t_detail.FCheckNum =edNumCheck.getText().toString();
        t_detail.FCheckPassNum =edNumPass.getText().toString();
        t_detail.FCheckResultID = checkResult.FInterID;
        t_detail.FCheckResultName = checkResult.FName;
        t_detail.FCheckTypeID = spCheckType.getEmployeeId();
        t_detail.FCheckTypeName = spCheckType.getEmployeeName();
        t_detail.activity = activity;
        t_detail.FEntryID = pushDownSub.FEntryID;
        t_detail.FInterID = pushDownSub.FInterID;
        long insert = t_detailDao.insert(t_detail);

        lockScan(0);
        if (insert1 > 0 && insert > 0) {
            pushDownSub.FQtying = DoubleUtil.sum(MathUtil.toD(pushDownSub.FQtying) , MathUtil.toD(edNumCheck.getText().toString()) ) + "";
            pushDownSubDao.update(pushDownSub);
            Toast.showText(mContext, "添加成功");
            MediaPlayer.getInstance(mContext).ok();
            finish();
        } else {
            Toast.showText(mContext, "添加失败，请重试");
            MediaPlayer.getInstance(mContext).error();
        }


    }



    public static void start(Context context, long orderid,String FInterID,String FEntryID,int tag) {
        Intent starter = new Intent(context, AddCheckActivity.class);
        starter.putExtra("tag", tag);
        starter.putExtra("orderid", orderid);
        starter.putExtra("FInterID", FInterID);
        starter.putExtra("FEntryID", FEntryID);
//        starter.putStringArrayListExtra("fid", fid);
        context.startActivity(starter);
    }
}
