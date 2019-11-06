package com.fangzuo.assist.Activity;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.SettingListAdapter;
import com.fangzuo.assist.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fangzuo.assist.Utils.GetSettingList.GetPushDownList;


public class PushDownActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    RelativeLayout btnBack;
    @BindView(R.id.lv_pushdown_menu)
    ListView lvPushdownMenu;
    private Bundle b;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_push_down);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        SettingListAdapter ada = new SettingListAdapter(mContext, GetPushDownList());
        lvPushdownMenu.setAdapter(ada);
        ada.notifyDataSetChanged();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void initListener() {
        b = new Bundle();
        lvPushdownMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        b.clear();
                        b.putInt("123", 1);//销售订单下推销售出库
                        break;
                    case 1:
                        b.clear();
                        b.putInt("123", 2);//采购订单下推外购入库
                        break;
//                    case 2:
//                        b.clear();
//                        b.putInt("123", 3);//发货通知下推销售出库
//                        break;
//                    case 3:
//                        b.clear();
//                        b.putInt("123", 4);//收料通知下推外购入库
//                        break;
//                    case 5:
//                        b.clear();
//                        b.putInt("123", 12);//委外订单下推委外出库
//                        break;
                    case 2:
                        b.clear();
                        b.putInt("123", 9);//生产任务单下推产品入库
                        break;
                    case 3:
                        b.clear();
                        b.putInt("123", 13);//生产任务单下推生产领料
                        break;
                    case 4:
                        b.clear();
                        b.putInt("123", 11);//委外订单下推委外入库
                        break;
//                    case 8:
//                        b.clear();
//                        b.putInt("123", 14);//采购订单下推收料通知单
//                        break;
//                    case 9:
//                        b.clear();
//                        b.putInt("123", 15);//销售订单下推发料通知单
//                        break;
//                    case 10:
//                        b.clear();
//                        b.putInt("123", 16);//生产任务单下推生产汇报单
//                        break;
//                    case 11:
//                        b.clear();
//                        b.putInt("123", 18);//汇报单下推产品入库
//                        break;
//                    case 12:
//                        b.clear();
//                        b.putInt("123", 7);//销售出库单验货
//                        break;
//                    case 13:
//                        b.clear();
//                        b.putInt("123", 20);//发货通知生成调拨单
//                        break;
//                    case 14:
//                        b.clear();
//                        b.putInt("123", 22);//产品入库验货
//                        break;

                }
                startNewActivity(PushDownPagerActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, false, b);

            }
        });
    }

    @Override
    protected void OnReceive(String code) {

    }


}
