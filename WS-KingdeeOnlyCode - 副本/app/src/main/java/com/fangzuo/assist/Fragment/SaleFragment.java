package com.fangzuo.assist.Fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.fangzuo.assist.ABase.BaseFragment;
import com.fangzuo.assist.Activity.ProduceAndGetActivity;
import com.fangzuo.assist.Activity.PushDownPagerActivity;
import com.fangzuo.assist.Adapter.GridViewAdapter;
import com.fangzuo.assist.Beans.SettingList;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.GetSettingList;
import com.fangzuo.assist.Activity.PushDownActivity;
import com.fangzuo.assist.Activity.SaleOrderActivity;
import com.fangzuo.assist.Activity.SoldOutActivity;
import com.fangzuo.assist.R;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

//销售fragment
public class SaleFragment extends BaseFragment {
    @BindView(R.id.gv)
    GridView gv;
    Unbinder unbinder;
    private FragmentActivity mContext;

    public SaleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sale, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }


    @Override
    public void initView() {
        mContext = getActivity();
    }

    @Override
    protected void OnReceive(String barCode) {

    }
    GridViewAdapter ada;
    @Override
    protected void initData() {
        String getPermit= Hawk.get(Config.User_Permit,"");
        String[] aa = getPermit.split("\\-"); // 这样才能得到正确的结果
        ada = new GridViewAdapter(mContext, GetSettingList.getSaleList(aa));
        gv.setAdapter(ada);
        ada.notifyDataSetChanged();
    }
    Bundle b;
    @Override
    protected void initListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SettingList tv= (SettingList) ada.getItem(i);
                b = new Bundle();
                switch (tv.tag){
//                    case 0://销售订单
//                        startNewActivity(SaleOrderActivity.class,null);
//                        break;
//                    case 0://销售出库
//                        startNewActivity(SoldOutActivity.class,null);
//                        break;
//                    case 1://单据下推
//                        startNewActivity(PushDownActivity.class,null);
//                        break;
//                    case 2://生产领料
//                        startNewActivity(ProduceAndGetActivity.class,null);
                    case "10"://
                        b.putInt("123", 9);//生产任务单下推产品入库
                        startNewActivity(PushDownPagerActivity.class, b);
                        break;
                    case "11"://
                        b.putInt("123", 11);//委外订单下推委外入库
                        startNewActivity(PushDownPagerActivity.class, b);
                        break;
                    case "12"://
                        b.putInt("123",3);//发货通知下推销售出库
                        startNewActivity(PushDownPagerActivity.class, b);
                        break;
                    case "13"://
                        b.putInt("123",23);//退货通知单下推销售出库红字
                        startNewActivity(PushDownPagerActivity.class, b);
                        break;
                    case "14"://
                        b.putInt("123",18);//汇报单下推产品入库
                        startNewActivity(PushDownPagerActivity.class, b);
                        break;
                    case "15"://
                        b.putInt("123",13);//生产任务单下推生产领料
                        startNewActivity(PushDownPagerActivity.class, b);
                        break;
                    case "19"://
                        b.putInt("123",24);//调拨单验货
                        startNewActivity(PushDownPagerActivity.class, b);
                        break;

                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
