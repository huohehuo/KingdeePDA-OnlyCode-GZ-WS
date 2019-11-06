package com.fangzuo.assist.Fragment;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.fangzuo.assist.ABase.BaseFragment;
import com.fangzuo.assist.Activity.BulkPdActivity;
import com.fangzuo.assist.Activity.ChangePriceActivity;
import com.fangzuo.assist.Activity.DBActivity;
import com.fangzuo.assist.Activity.GyYhActivity;
import com.fangzuo.assist.Activity.GyYhRedActivity;
import com.fangzuo.assist.Activity.OtherInStoreActivity;
import com.fangzuo.assist.Activity.OtherOutStore4RedActivity;
import com.fangzuo.assist.Activity.OtherOutStoreActivity;
import com.fangzuo.assist.Activity.PDActivity;
import com.fangzuo.assist.Activity.PackageActivity;
import com.fangzuo.assist.Activity.ProductInStorageRedActivity;
import com.fangzuo.assist.Activity.PushDownPagerActivity;
import com.fangzuo.assist.Activity.StorageCheckActivity;
import com.fangzuo.assist.Adapter.GridViewAdapter;
import com.fangzuo.assist.Beans.SettingList;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.GetSettingList;
import com.fangzuo.assist.Activity.ProductInStorageActivity;
import com.fangzuo.assist.Activity.PurchaseInStorageActivity;
import com.fangzuo.assist.Activity.PurchaseOrderActivity;
import com.fangzuo.assist.R;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class PurchaseFragment extends BaseFragment {
    @BindView(R.id.gv)
    GridView gv;
    Unbinder unbinder;
    private FragmentActivity mContext;

    public PurchaseFragment() {

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
        String getPermit= Hawk.get(Config.User_Permit,"");
        String[] aa = getPermit.split("\\-"); // 这样才能得到正确的结果
        ada = new GridViewAdapter(mContext, GetSettingList.getPurchaseList(aa));
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
                    case "1"://组装单
                        startNewActivity(PackageActivity.class, null);
                        break;
                    case "2"://调拨
                        startNewActivity(DBActivity.class,null);
                        break;
                    case "3"://电商出库
                        startNewActivity(GyYhActivity.class,null);
                        break;
                    case "4"://电商出库红字
                        startNewActivity(GyYhRedActivity.class,null);
                        break;
                    case "5"://其他入库
                        startNewActivity(OtherInStoreActivity.class,null);
                        break;
                    case "6"://其他出库
                        startNewActivity(OtherOutStoreActivity.class,null);
                        break;
                    case "7"://散装盘点
                        startNewActivity(BulkPdActivity.class, null);
                        break;
                    case "8"://盘点
                        startNewActivity(PDActivity.class, null);
                        break;
                    case "9"://库存查询
                        startNewActivity(StorageCheckActivity.class,null);
                        break;
                    case "16"://其他出库红字
                        startNewActivity(OtherOutStore4RedActivity.class,null);
                        break;
                    case "17"://产品入库红字
                        startNewActivity(ProductInStorageRedActivity.class, null);
                        break;
                    case "18"://调价单
                        startNewActivity(ChangePriceActivity.class, null);
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
