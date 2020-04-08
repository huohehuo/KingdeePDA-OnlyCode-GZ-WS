package com.fangzuo.assist.Fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.fangzuo.assist.ABase.BaseFragment;
import com.fangzuo.assist.Activity.CodeStateCheck;
import com.fangzuo.assist.Activity.OtherInStore2Activity;
import com.fangzuo.assist.Activity.OtherOutStore2Activity;
import com.fangzuo.assist.Activity.PushDownPagerActivity;
import com.fangzuo.assist.Activity.StorageCheck4YCLActivity;
import com.fangzuo.assist.Activity.StorageCheckActivity;
import com.fangzuo.assist.Adapter.CarNoScAdapter;
import com.fangzuo.assist.Adapter.GridViewAdapter;
import com.fangzuo.assist.Adapter.StorageFragmentAdapter;
import com.fangzuo.assist.Beans.SettingList;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.GetSettingList;
import com.fangzuo.assist.Activity.DBActivity;
import com.fangzuo.assist.Activity.OtherInStoreActivity;
import com.fangzuo.assist.Activity.OtherOutStoreActivity;
import com.fangzuo.assist.Activity.PDActivity;
import com.fangzuo.assist.R;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StorageFragment extends BaseFragment {
//    @BindView(R.id.gv)
//    GridView gv;
    @BindView(R.id.ry_list)
    EasyRecyclerView ryList;
    Unbinder unbinder;
    private FragmentActivity mContext;

    private StorageFragmentAdapter adapter;
    public StorageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_storage, container, false);
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

//    GridViewAdapter ada;
    @Override
    protected void initData() {
//        String getPermit= Hawk.get(Config.User_Permit,"");
//        String[] aa = getPermit.split("\\-"); // 这样才能得到正确的结果
        ryList.setAdapter(adapter = new StorageFragmentAdapter(mContext));
        ryList.setLayoutManager(new GridLayoutManager(mContext,3));
//        ryList.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
//        ada= new GridViewAdapter(mContext, GetSettingList.getStorageList());
//        gv.setAdapter(ada);
        adapter.addAll(GetSettingList.getStorageList());
        adapter.notifyDataSetChanged();
    }



    Bundle b;
    @Override
    protected void initListener() {
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SettingList tv= (SettingList) adapter.getAllData().get(position);
                b = new Bundle();
                switch (tv.tag){
                    case "22"://其他入库
                        startNewActivity(OtherInStore2Activity.class,null);
                        break;
                    case "23"://其他出库
                        startNewActivity(OtherOutStore2Activity.class,null);
                        break;
                    case "20"://
                        b.putInt("123", 14);//采购订单下推收料通知单
                        startNewActivity(PushDownPagerActivity.class, b);
                        break;
                    case "30"://
                        b.putInt("123", 30);//委外订单下推收料通知单
                        startNewActivity(PushDownPagerActivity.class, b);
                        break;
                    case "21"://
                        b.putInt("123", 4);//收料通知下推外购入库
                        startNewActivity(PushDownPagerActivity.class, b);
                        break;
                    case "24"://生产任务单下推产品入库（原材料）
                        b.putInt("123", 25);
                        startNewActivity(PushDownPagerActivity.class, b);
                        break;
                    case "25"://委外订单下推委外入库（原材料）
                        b.putInt("123", 26);
                        startNewActivity(PushDownPagerActivity.class, b);
                        break;
                    case "26"://委外订单下推委外出库（先进先出）（原材料）
                        b.putInt("123", 12);
                        startNewActivity(PushDownPagerActivity.class, b);
                        break;
                    case "27"://生产任务单下推生产领料（先进先出）（原材料）
                        b.putInt("123", 27);
                        startNewActivity(PushDownPagerActivity.class, b);
                        break;
                    case "28"://收料通知单下推来料检验单
                        b.putInt("123", 28);
                        startNewActivity(PushDownPagerActivity.class, b);
                        break;
                    case "32"://生产领料单验货
                        b.putInt("123", 29);
                        startNewActivity(PushDownPagerActivity.class, b);
                        break;
                    case "29"://库存查询
                        startNewActivity(StorageCheck4YCLActivity.class,null);
                        break;
                    case "33"://收料通知单下推委外入库单
                        b.putInt("123", 31);
                        startNewActivity(PushDownPagerActivity.class, b);
                        break;
//                    case 0://盘点
//                        startNewActivity(PDActivity.class,null);
//                        break;
//                    case 1://调拨
//                        startNewActivity(DBActivity.class,null);
//                        break;
//                    case 2://其他入库
//                        startNewActivity(OtherInStoreActivity.class,null);
//                        break;
//                    case 3://其他出库
//                        startNewActivity(OtherOutStoreActivity.class,null);
//                        break;
//                    case 4://库存查询
//                        startNewActivity(StorageCheckActivity.class,null);
//                        break;
//                    case 5://条码状态查询
//                        startNewActivity(CodeStateCheck.class,null);
//                        break;


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
