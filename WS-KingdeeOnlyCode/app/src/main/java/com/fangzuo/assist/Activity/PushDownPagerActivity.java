package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.PrintHistoryAdapter;
import com.fangzuo.assist.Adapter.StripAdapter;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Dao.PrintHistory;
import com.fangzuo.assist.Fragment.pushdown.ChooseFragment;
import com.fangzuo.assist.Fragment.pushdown.DownLoadPushFragment;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.PagerSlidingTabStrip;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.greendao.gen.PrintHistoryDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;


//          下载，选择单据页面（包含：下载单据Fragment，选择单据Fragment）
public class PushDownPagerActivity extends BaseActivity {
    @BindView(R.id.tabstrip)
    PagerSlidingTabStrip tabstrip;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv_print)
    RelativeLayout tvPrint;
    @BindColor(R.color.cpb_blue)
    int cpb_blue;
    public int tag;
    public String billNO;
    public String bringCode;

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg) {
            case EventBusInfoCode.Print_Out://打印
                List<PrintHistory> data = (List<PrintHistory>) event.postEvent;
                try {
                    CommonUtil.doPrint(mContext,data,"1");
                } catch (Exception e) {
//                    e.printStackTrace();
                    LoadingUtil.showAlter(mContext,"打印错误","请检查打印机");
                }
                break;
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_push_down_pager);
        ButterKnife.bind(this);
        tag = getIntent().getExtras().getInt("123");
        billNO = getIntent().getExtras().getString("billNO");
        bringCode = getIntent().getExtras().getString("barcode");
        Log.e("获取到--tag--", tag +"");
        Log.e("获取到--billNO--", billNO +"");
        if (tag == 14 || tag == 30){
            tvPrint.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        fragments.add(new DownLoadPushFragment());
        fragments.add(new ChooseFragment());
        titles.add("下载单据");
        titles.add("选择单据");
        StripAdapter stripAdapter = new StripAdapter(getSupportFragmentManager(), fragments, titles);
        Log.e("stripAdapter", stripAdapter + "");
        viewpager.setAdapter(stripAdapter);
        tabstrip.setShouldExpand(true);
        tabstrip.setViewPager(viewpager);
        tabstrip.setDividerColor(cpb_blue);
        tabstrip.setUnderlineHeight(3);
        tabstrip.setIndicatorHeight(6);
        tabstrip.setIndicatorColor(cpb_blue);

    }

    @Override
    protected void initListener() {
        tvPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAndPrintData();
            }
        });

    }
    //获取列表相对应的打印数据
    private void getAndPrintData() {
        final List<PrintHistory> printHistoryList=daoSession.getPrintHistoryDao().loadAll();
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle("请选择要补打的单据");
        View v = LayoutInflater.from(mContext).inflate(R.layout.pd_alert, null);
        ListView lv = v.findViewById(R.id.lv_alert);
        PrintHistoryAdapter printHistoryAdapter = new PrintHistoryAdapter(mContext, printHistoryList);
        lv.setAdapter(printHistoryAdapter);
        ab.setView(v);
        final AlertDialog alertDialog = ab.create();
        alertDialog.show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                doPrintBarCode(printHistoryList.get(i).FBarCode);
                alertDialog.dismiss();
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(mContext)
                        .setTitle("是否删除该单据？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletePrint(printHistoryList.get(position).FBarCode);
                                alertDialog.dismiss();
                            }
                        })
                        .create().show();
                return true;
            }
        });
    }
    //执行打印
    private void doPrintBarCode(String barcode){
        List<PrintHistory> printHistoryList =daoSession.getPrintHistoryDao().queryBuilder().where(
                PrintHistoryDao.Properties.Tag.eq(tag),
                PrintHistoryDao.Properties.FBarCode.eq(barcode)
        ).build().list();
        if (printHistoryList.size()<=0){
            Toast.showText(mContext,"无法找到打印的相关信息");
        }else{
            EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Print_Out, printHistoryList));
        }
    }
    //删除单据
    private void deletePrint(String barcode){
        daoSession.getPrintHistoryDao().deleteInTx(daoSession.getPrintHistoryDao().queryBuilder().where(
                PrintHistoryDao.Properties.Tag.eq(tag),
                PrintHistoryDao.Properties.FBarCode.eq(barcode)
        ).build().list());
    }
    @Override
    protected void OnReceive(String code) {
        Log.e("code:","PushDownPagerActivity-"+code);
    }

    public int getTitles(){
        return tag;
    }

    public String getBillNo(){
        return billNO==null?"":billNO;
    }
    public String getBarcode(){
        return bringCode==null?"":bringCode;
    }
}
