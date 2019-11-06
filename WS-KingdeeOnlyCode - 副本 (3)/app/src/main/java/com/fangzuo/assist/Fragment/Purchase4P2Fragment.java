package com.fangzuo.assist.Fragment;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.fangzuo.assist.ABase.BaseFragment;
import com.fangzuo.assist.Activity.P2.CheckStoreActivity;
import com.fangzuo.assist.Activity.P2.CheckLogActivity;
import com.fangzuo.assist.Activity.P2.CreateSaleOutActivity;
import com.fangzuo.assist.Activity.P2.CreateSaleOutRedActivity;
import com.fangzuo.assist.Activity.P2.ScanCheckActivity;
import com.fangzuo.assist.Activity.PDActivity;
import com.fangzuo.assist.Adapter.GridViewAdapter;
import com.fangzuo.assist.Beans.SettingList;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.GetSettingList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class Purchase4P2Fragment extends BaseFragment {
    @BindView(R.id.gv)
    GridView gv;
    Unbinder unbinder;
    private FragmentActivity mContext;

    public Purchase4P2Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_purchase, container, false);
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
//        String getPermit= Hawk.get(Config.User_Permit,"");
//        String[] aa = getPermit.split("\\-"); // 这样才能得到正确的结果
        ada = new GridViewAdapter(mContext, GetSettingList.getPurchaseList4P2());
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

                switch (tv.tag) {
//                    case 0://采购订单
//                        startNewActivity(PurchaseOrderActivity.class, null);
//                        break;
//                    case 0://外购入库
//                        startNewActivity(PurchaseInStorageActivity.class, null);
//                        break;
//                    case 1://产品入库
//                        startNewActivity(ProductInStorageActivity.class, null);
//                        break;
                    case "1"://扫码查询
                        startNewActivity(ScanCheckActivity.class, null);
                        break;
                    case "2"://销售出库
                        startNewActivity(CreateSaleOutActivity.class,null);
                        break;
                    case "5"://销售出库红字
                        startNewActivity(CreateSaleOutRedActivity.class,null);
                        break;
                    case "3"://查询条码记录
                        startNewActivity(CheckLogActivity.class,null);
                        break;
                    case "4"://盘点单
                        startNewActivity(PDActivity.class,null);
                        break;
                    case "6"://库存查询
                        startNewActivity(CheckStoreActivity.class,null);
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
