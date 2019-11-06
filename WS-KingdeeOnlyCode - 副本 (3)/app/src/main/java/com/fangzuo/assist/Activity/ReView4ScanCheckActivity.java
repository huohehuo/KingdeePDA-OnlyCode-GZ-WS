package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.ReView1Adapter;
import com.fangzuo.assist.Adapter.TableAdapter;
import com.fangzuo.assist.Adapter.TableAdapter1;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Beans.SendOrderListBean;
import com.fangzuo.assist.Dao.InStorageNum;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.InStorageNumDao;
import com.fangzuo.greendao.gen.PDSubDao;
import com.fangzuo.greendao.gen.SendOrderListBeanDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReView4ScanCheckActivity extends BaseActivity {
    @BindView(R.id.btn_back)
    RelativeLayout btnBack;
    @BindView(R.id.lv_result)
    ListView lvResult;
    @BindView(R.id.btn_delete)
    RelativeLayout btnDelete;
    @BindView(R.id.productcategory)
    TextView productcategory;
    @BindView(R.id.productnum)
    TextView productnum;
    @BindView(R.id.delete_all)
    TextView deleteAll;
    private List<T_Detail> list;
    private List<T_main> list1;
    private ReView1Adapter tableAdapter;
    private List<Boolean> isCheck;
    private int activity;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_re_view4_scan_check);
        mContext = this;
        ButterKnife.bind(this);

    }

    @Override
    protected void initData() {
        Intent in = getIntent();
        Bundle extras = in.getExtras();
        activity = extras.getInt("activity");
        initList();
    }

    private void initList() {
        double num = 0;
        isCheck = new ArrayList<>();
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        list1.clear();
        list.clear();
        list = t_detailDao.queryBuilder().where(T_DetailDao.Properties.Activity.eq(activity)).build().list();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                isCheck.add(false);
            }
        }
        Lg.e("列表数据：", list);
        tableAdapter = new ReView1Adapter(mContext, list, isCheck);
        lvResult.setAdapter(tableAdapter);
        tableAdapter.notifyDataSetChanged();
        List<String> products = new ArrayList<>();
        products.clear();
        if (list.size() > 0) {
            if (products.size() == 0) {
                products.add(list.get(0).FProductId);
            }
            for (int i = 0; i < list.size(); i++) {
                if (!products.contains(list.get(i).FProductId)) {
                    products.add(list.get(i).FProductId);
                }
            }


            for (int i = 0; i < list.size(); i++) {
                num += MathUtil.toD(list.get(i).FQuantity);
            }
            productcategory.setText("物料类别数:" + products.size() + "个");
//            productnum.setText("物料总数为:" + num + "");


        } else {
            productcategory.setText("物料类别数:" + 0 + "个");
//            productnum.setText("物料总数为:" + 0 + "");
        }

    }

    private PurchaseInStoreUploadBean pBean;
    private PurchaseInStoreUploadBean.purchaseInStore listBean;
    private ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data;
    private ArrayList<T_Detail> t_detailList;
    private ArrayList<T_main> t_mainsList;

    @Override
    protected void initListener() {
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                ab.setTitle("确认删除");
                ab.setMessage("确认删除所有么？");
                ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LoadingUtil.showDialog(mContext, "正在删除...");
                        t_detailDao.deleteInTx(list);
                        Toast.showText(mContext, "删除成功");
                        initList();

                    }
                });
                ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                ab.create().show();

            }
        });
        lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isCheck.get(i)) {
                    isCheck.set(i, false);
                } else {
                    isCheck.set(i, true);
                }

                Log.e("ischeck", isCheck.get(i) + "");
                tableAdapter.notifyDataSetChanged();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.size() > 0) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                    ab.setTitle("确认删除");
                    ab.setMessage("选中数据会被删除，确定？");
                    ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            LoadingUtil.showDialog(mContext, "正在删除...");
                            t_detailList = new ArrayList<>();
                            pBean = new PurchaseInStoreUploadBean();
                            listBean = pBean.new purchaseInStore();
                            data = new ArrayList<>();
                            for (int j = 0; j < isCheck.size(); j++) {
                                if (isCheck.get(j)) {
                                    Log.e(i + "", isCheck.get(j) + "");
                                    final T_Detail t_detail = t_detailDao.queryBuilder().where(
                                            T_DetailDao.Properties.FIndex.eq(list.get(j).FIndex)
                                    ).build().unique();
                                    t_detailList.add(t_detail);

                                }
                            }
                            String detail = "";
                            listBean.main = "1";
                            ArrayList<String> detailContainer = new ArrayList<>();
                            for (int j = 0; j < t_detailList.size(); j++) {
                                if (j != 0 && j % 49 == 0) {
                                    Log.e("j%49", j % 49 + "");
                                    T_Detail t_detail = t_detailList.get(j);
                                    if (null == t_detail.FBarcode || "".equals(t_detail.FBarcode)) {
                                        continue;
                                    }
                                    detail = detail +
                                            t_detail.FBarcode + "|" +
                                            t_detail.FQuantity + "|" +
                                            t_detail.IMIE + "|" +
                                            t_detail.FOrderId + "|";
                                    detail = detail.subSequence(0, detail.length() - 1).toString();
                                    detailContainer.add(detail);
                                    detail = "";
                                } else {
                                    Log.e("j", j + "");
                                    T_Detail t_detail = t_detailList.get(j);
                                    if (null == t_detail.FBarcode || "".equals(t_detail.FBarcode)) {
                                        continue;
                                    }
                                    detail = detail +
                                            t_detail.FBarcode + "|" +
                                            t_detail.FQuantity + "|" +
                                            t_detail.IMIE + "|" +
                                            t_detail.FOrderId + "|";
                                    Log.e("detail1", detail);
                                }
                            }
                            if (detail.length() > 0) {
                                detail = detail.subSequence(0, detail.length() - 1).toString();
                            }
                            Log.e("detail", detail);
                            detailContainer.add(detail);
                            listBean.detail = detailContainer;
                            data.add(listBean);
                            pBean.list = data;
                            App.getRService().doIOAction(WebApi.DeleteForOnlyCode, gson.toJson(pBean), new MySubscribe<CommonResponse>() {
                                @Override
                                public void onNext(CommonResponse commonResponse) {
                                    super.onNext(commonResponse);
                                    if (!commonResponse.state) return;
                                    t_detailDao.deleteInTx(t_detailList);
//                                    t_mainDao.deleteInTx(t_mainsList);
                                    Toast.showText(mContext, "删除成功");
                                    initList();
                                    tableAdapter.notifyDataSetChanged();
                                    LoadingUtil.dismiss();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    LoadingUtil.dismiss();
                                    Toast.showText(mContext, "删除失败：" + e.toString());
                                }
                            });
                        }
                    });
                    ab.setNegativeButton("取消", null);
                    ab.create().show();
                } else {
                    Toast.showText(mContext, "没有数据");
                }

            }
        });
    }

    @Override
    protected void OnReceive(String code) {

    }


    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        onBackPressed();
    }

}