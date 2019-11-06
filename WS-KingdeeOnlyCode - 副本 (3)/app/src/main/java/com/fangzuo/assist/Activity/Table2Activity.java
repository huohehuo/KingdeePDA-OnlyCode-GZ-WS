package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.fangzuo.assist.Adapter.TableAdapter1;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.PDSub;
import com.fangzuo.assist.Dao.PushDownSub;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.PDSubDao;
import com.fangzuo.greendao.gen.PushDownSubDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Table2Activity extends BaseActivity implements TableAdapter1.InnerClickListener {

    @BindView(R.id.btn_back)
    RelativeLayout btnBack;
    @BindView(R.id.btn_delete)
    RelativeLayout btnDelete;
    @BindView(R.id.rl1)
    RelativeLayout rl1;
    @BindView(R.id.lv_result)
    ListView lvResult;
    @BindView(R.id.productcategory)
    TextView productcategory;
    @BindView(R.id.productnum)
    TextView productnum;
    private DaoSession daoSession;
    private T_DetailDao t_detailDao;
    private ArrayList<Boolean> isCheck;
    private TableAdapter1 tableAdapter1;
    private PDSubDao pdSubDao;
    private List<T_Detail> list;
    private double num;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_table2);
        mContext = this;
        ButterKnife.bind(this);
        daoSession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        t_detailDao = daoSession.getT_DetailDao();
        pdSubDao = daoSession.getPDSubDao();
    }

    @Override
    protected void initData() {
        num = 0;
        isCheck = new ArrayList<>();
        list = t_detailDao.queryBuilder().where(T_DetailDao.Properties.Activity.eq(Config.PDActivity)).build().list();
        Lg.e("数据集：",list);
        for (int i = 0; i < list.size(); i++) {
            isCheck.add(false);
        }
        tableAdapter1 = new TableAdapter1(mContext, list, isCheck);
        lvResult.setAdapter(tableAdapter1);
        tableAdapter1.setInnerListener(this);
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
                num += Double.parseDouble(list.get(i).FQuantity);
            }
            productcategory.setText("物料类别数:" + products.size() + "个");
            productnum.setText("物料总数为:" + num + "");


        } else {
            productcategory.setText("物料类别数:" + 0 + "个");
            productnum.setText("物料总数为:" + 0 + "");
        }
    }



    @Override
    protected void initListener() {
        lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isCheck.get(i)) {
                    isCheck.set(i, false);
                } else {
                    isCheck.set(i, true);
                }
                Log.e("ischeck", isCheck.get(i) + "");
                tableAdapter1.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void OnReceive(String code) {

    }

    private PurchaseInStoreUploadBean pBean;
    private PurchaseInStoreUploadBean.purchaseInStore listBean;
    private ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data;
    private ArrayList<T_Detail> t_detailList;
    private ArrayList<T_main> t_mainsList;
    @OnClick({R.id.btn_back, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_delete:
                AlertDialog.Builder ab1 = new AlertDialog.Builder(mContext);
                ab1.setTitle("确认");
                ab1.setMessage("确认删除么?");
                ab1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LoadingUtil.showDialog(mContext,"正在删除...");
                        t_detailList = new ArrayList<>();
                        t_mainsList = new ArrayList<>();
                        pBean = new PurchaseInStoreUploadBean();
                        listBean=pBean.new purchaseInStore();
                        data = new ArrayList<>();
                        for (int j = 0; j < isCheck.size(); j++) {
                            if (isCheck.get(j)) {
                                Log.e(i + "", isCheck.get(j) + "");
                                final T_Detail t_detail = t_detailDao.queryBuilder().where(
                                        T_DetailDao.Properties.FIndex.eq(list.get(j).FIndex)
                                ).build().unique();
                                t_detailList.add(t_detail);
//                                final T_main t_main = t_mainDao.queryBuilder().where(
//                                        T_mainDao.Properties.FIndex.eq(list.get(j).FIndex)
//                                ).build().unique();
//                                t_mainsList.add(t_main);
                                Log.e(TAG, "获取到T_Detail:" + t_detail.toString());
                            }
                        }
                        String detail ="";
                        listBean.main="";
                        ArrayList<String> detailContainer = new ArrayList<>();
                        for (int j = 0; j < t_detailList.size(); j++) {
                            if (j != 0 && j % 49 == 0) {
                                Log.e("j%49", j % 49 + "");
                                T_Detail t_detail = t_detailList.get(j);
                                detail = detail +
                                        t_detail.FBarcode + "|" +
                                        t_detail.FQuantity + "|" +
                                        t_detail.IMIE + "|" +
                                        t_detail.FRate + "|";
                                detail = detail.subSequence(0, detail.length() - 1).toString();
                                detailContainer.add(detail);
                                detail = "";
                            } else {
                                Log.e("j", j + "");
                                T_Detail t_detail = t_detailList.get(j);
                                detail = detail +
                                        t_detail.FBarcode + "|" +
                                        t_detail.FQuantity + "|" +
                                        t_detail.IMIE + "|" +
                                        t_detail.FRate + "|";
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
                        pBean.list=data;
                        Gson gson = new Gson();
                        App.getRService().getCodeCheckDelete(gson.toJson(pBean), new MySubscribe<CommonResponse>() {
                            @Override
                            public void onNext(CommonResponse commonResponse) {
                                for (T_Detail t_detail:t_detailList) {
                                    List<PDSub> list2 = pdSubDao.queryBuilder().where(
                                            PDSubDao.Properties.FID.eq(t_detail.FInterID),
                                            PDSubDao.Properties.FBarCode.eq(t_detail.FBarcode)
                                    ).build().list();
                                    Lg.e("相关明细sub",list2);
                                    if (list2.size() > 0) {
                                        Lg.e("1111",list2.get(0).FCheckQty);
                                        list2.get(0).FCheckQty = MathUtil.doubleSub(MathUtil.toD(list2.get(0).FCheckQty),MathUtil.toD(t_detail.FQuantity)) + "";
                                        Lg.e("2222",list2.get(0).FCheckQty);
                                        pdSubDao.update(list2.get(0));
                                    }
                                }
                                t_detailDao.deleteInTx(t_detailList);
                                Toast.showText(mContext, "删除成功");
                                initData();
                                LoadingUtil.dismiss();
                            }

                            @Override
                            public void onError(Throwable e) {
                                LoadingUtil.dismiss();
                                Toast.showText(mContext, "删除失败："+e.toString());
                            }
                        });


//                        for (int j = 0; j < isCheck.size(); j++) {
//                            if (isCheck.get(j)) {
//                                List<PDSub> list2 = pdSubDao.queryBuilder().where(PDSubDao.Properties.FID.eq(list.get(j).FInterID), PDSubDao.Properties.FBatchNo.eq(list.get(j).FBatch), PDSubDao.Properties.FStockID.eq(list.get(j).FStorageId), PDSubDao.Properties.FStockPlaceID.eq(list.get(j).FPositionId==null?"":list.get(j).FPositionId)).build().list();
//                                if (list2.size() > 0) {
//                                    list2.get(0).FCheckQty = (Double.parseDouble(list2.get(0).FCheckQty) - Double.parseDouble(list.get(j).FQuantity)) + "";
//                                    pdSubDao.update(list2.get(0));
//                                }
//                                t_detailDao.delete(list.get(j));
//
//                            }
//                        }
//                        initData();
                    }
                });
                ab1.setNegativeButton("取消", null);
                ab1.create().show();
                break;

        }
    }

    @Override
    public void InOnClick(View v) {
        final int position = (int) v.getTag();
        switch (v.getId()) {
            case R.id.delete:
                AlertDialog.Builder ab1 = new AlertDialog.Builder(mContext);
                ab1.setTitle("确认");
                ab1.setMessage("确认删除么?");
                ab1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e("list2",list.get(position).FInterID+"  "+list.get(position).FBatch+"  "+list.get(position).FStorageId+"  "+list.get(position).FPositionId);
                        List<PDSub> list2 = pdSubDao.queryBuilder().where(PDSubDao.Properties.FID.eq(list.get(position).FInterID),
                                PDSubDao.Properties.FBatchNo.eq(list.get(position).FBatch==null?"":list.get(position).FBatch),
                                PDSubDao.Properties.FStockID.eq(list.get(position).FStorageId), PDSubDao.Properties.FStockPlaceID.
                                        eq(list.get(position).FPositionId==null?"":list.get(position).FPositionId)).build().list();
                        if (list2.size() > 0) {
                            list2.get(0).FCheckQty = (Double.parseDouble(list2.get(0).FCheckQty) - Double.parseDouble(list.get(position).FQuantity)) + "";
                            list2.get(0).FAdjQty = (Double.parseDouble(list2.get(0).FAdjQty) - Double.parseDouble(list.get(position).FIdentity)) + "";
                            pdSubDao.update(list2.get(0));
                            t_detailDao.delete(list.get(position));
                            initData();
                        }
                    }
                });
                ab1.setNegativeButton("取消", null).create().show();
                break;
            case R.id.fix:
                AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                ab.setTitle("修改数据");
                View view = LayoutInflater.from(mContext).inflate(R.layout.fixalertitems, null);
                final EditText mEtNum = view.findViewById(R.id.ed_num);
                final EditText mEtPrice = view.findViewById(R.id.ed_price);
                TextView mTv = view.findViewById(R.id.price);
                mTv.setText("调整数量:");
                mEtNum.setText(list.get(position).FQuantity);
                mEtPrice.setText(list.get(position).FIdentity);
                ab.setView(view);
                ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        T_Detail t_detail = t_detailDao.queryBuilder().where(T_DetailDao.Properties.FIndex.eq(list.get(position).FIndex)).build().unique();
                        String old = t_detail.FQuantity;
                        String newnum = mEtNum.getText().toString();
                        double getnum = Double.parseDouble(old) - Double.parseDouble(newnum);
                        List<PDSub> list2 = pdSubDao.queryBuilder().where(PDSubDao.Properties.FID.eq(list.get(position).FIndex), PDSubDao.Properties.FBatchNo.eq(list.get(position).FBatch), PDSubDao.Properties.FStockID.eq(list.get(position).FStorageId), PDSubDao.Properties.FStockPlaceID.eq(list.get(position).FPositionId)).build().list();
                        if (list2.size() > 0 && !mEtNum.getText().toString().equals("")) {
                            list2.get(0).FCheckQty = (Double.parseDouble(list2.get(0).FCheckQty) + getnum) + "";
                            pdSubDao.update(list2.get(0));
                            t_detail.FQuantity = (Double.parseDouble(t_detail.FQuantity) + getnum) + "";
                            t_detailDao.update(t_detail);
                        }

                    }
                });
                ab.setNegativeButton("取消", null).create().show();
                break;
        }
    }
}
