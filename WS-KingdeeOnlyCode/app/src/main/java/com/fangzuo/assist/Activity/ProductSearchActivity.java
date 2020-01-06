package com.fangzuo.assist.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.SearchAdapter;
import com.fangzuo.assist.Adapter.SearchCheckResultAdapter;
import com.fangzuo.assist.Adapter.SearchClientAdapter;
import com.fangzuo.assist.Adapter.SearchDbTypeAdapter;
import com.fangzuo.assist.Adapter.SearchDepartmentAdapter;
import com.fangzuo.assist.Adapter.SearchStorageAdapter;
import com.fangzuo.assist.Adapter.SearchSupplierAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Dao.CheckResult;
import com.fangzuo.assist.Dao.Client;
import com.fangzuo.assist.Dao.DbType;
import com.fangzuo.assist.Dao.GetGoodsDepartment;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.Suppliers;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.greendao.gen.ClientDao;
import com.fangzuo.greendao.gen.GetGoodsDepartmentDao;
import com.fangzuo.greendao.gen.ProductDao;
import com.fangzuo.greendao.gen.StorageDao;
import com.fangzuo.greendao.gen.SuppliersDao;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductSearchActivity extends BaseActivity {


    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.lv_result)
    ListView lvResult;
    @BindView(R.id.cancle)
    View cancle;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.model)
    TextView model;
    @BindView(R.id.name)
    TextView name;
    private String searchString;
    private ProductSearchActivity mContext;
    private SearchAdapter ada;
    private List<Product> items1;
    private List<Product> items;
    private List<Product> itemAll;
    private int where;
    private List<Suppliers> itemAllSupplier;
    private List<Suppliers> suppliersList;
    private List<DbType> dbTypeList;
    private List<Storage> storageList;
    private List<CheckResult> resultList;
    private List<Client> itemAllClient;
    private List<Client> itemClient;
    private List<GetGoodsDepartment> goodsDepartmentList;
    private List<GetGoodsDepartment> goodsDepartmentAllList;

    @Override
    public void initView() {
        setContentView(R.layout.activity_product_search);
        ButterKnife.bind(this);
        mContext = this;
        Intent in = getIntent();
        Bundle b = in.getExtras();
        searchString = b.getString("search", "");
        where = b.getInt("where");
        Log.e("searchString", searchString);
        if (where == Info.Search_DbType) title.setText("查询结果(调拨类别)");
        if (where == Info.SEARCHPRODUCT) title.setText("查询结果(物料)");
        if (where == Info.SEARCHSUPPLIER) title.setText("查询结果(供应商)");
        if (where == Info.SEARCHCLIENT) title.setText("查询结果(客户)");
        if (where == Info.SEARCHJH) title.setText("查询结果(交货单位)");
        if (where == Info.Search_Storage) title.setText("查询结果(仓库)");
        if (where == Info.Search_CheckResult) title.setText("查询结果(质检结果)");
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        super.receiveEvent(event);
    }

    @Override
    public void initData() {
        //物料
        if (where == Info.SEARCHPRODUCT) {
            model.setText("编码");
            name.setText("名称");
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                Asynchttp.post(mContext, getBaseUrl() + WebApi.PRODUCTSEARCHLIKE, searchString, new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                        items = dBean.products;
                        Log.e("getProduct:",items.toString());
                        itemAll = new ArrayList<>();
                        itemAll.addAll(items);
                        if (itemAll.size() > 0) {
                            ada = new SearchAdapter(mContext, itemAll);
                            lvResult.setAdapter(ada);
                            ada.notifyDataSetChanged();
                        } else {
                            Toast.showText(mContext, "无数据");
                            setResult(-9998, null);
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        Toast.showText(mContext, Msg);
                    }
                });
            } else {
                model.setText("编码");
                name.setText("名称");
                ProductDao productDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getProductDao();
                items = productDao.queryBuilder().whereOr(
                        ProductDao.Properties.FNumber.like("%" + searchString + "%"),
                        ProductDao.Properties.FBarcode.like("%" + searchString + "%"),
                        ProductDao.Properties.FName.like("%" + searchString + "%")).
                        orderAsc(ProductDao.Properties.FNumber).limit(50).orderAsc(ProductDao.Properties.FNumber).build().list();
                itemAll = new ArrayList<>();
                itemAll.addAll(items);
                if (itemAll.size() > 0) {
                    ada = new SearchAdapter(mContext, itemAll);
                    lvResult.setAdapter(ada);
                    ada.notifyDataSetChanged();
                } else {
                    Toast.showText(mContext, "无数据");
                    setResult(-9998, null);
                    onBackPressed();
                }
            }

            //供应商
        } else if (where == Info.SEARCHSUPPLIER) {
            model.setText("编号");
            name.setText("名称");
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                Asynchttp.post(mContext, getBaseUrl() + WebApi.SUPPLIERSEARCHLIKE, searchString, new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                        suppliersList = dBean.suppliers;
                        itemAllSupplier = new ArrayList<>();
                        itemAllSupplier.addAll(suppliersList);
                        if (itemAllSupplier.size() > 0) {
                            SearchSupplierAdapter ada1 = new SearchSupplierAdapter(mContext, itemAllSupplier);
                            lvResult.setAdapter(ada1);
                            ada1.notifyDataSetChanged();
                        } else {
                            Toast.showText(mContext, "无数据");
                            setResult(-9998, null);
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        Toast.showText(mContext, Msg);
                    }
                });
            }else{
                SuppliersDao suppliersDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getSuppliersDao();
                List<Suppliers> list = suppliersDao.queryBuilder().whereOr(
                        SuppliersDao.Properties.FName.like("%" + searchString + "%"),
                        SuppliersDao.Properties.FItemID.like("%" + searchString + "%")
                ).orderAsc(SuppliersDao.Properties.FItemID).limit(50).build().list();
                itemAllSupplier = new ArrayList<>();
                itemAllSupplier.addAll(list);
                if (itemAllSupplier.size() > 0) {
                    SearchSupplierAdapter ada1 = new SearchSupplierAdapter(mContext, itemAllSupplier);
                    lvResult.setAdapter(ada1);
                    ada1.notifyDataSetChanged();
                } else {
                    Toast.showText(mContext, "未查询到数据");
                    setResult(-9998, null);
                    onBackPressed();
                }

            }
                //客户
        } else if (where == Info.SEARCHCLIENT) {
            model.setText("编号");
            name.setText("名称");
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                Asynchttp.post(mContext, getBaseUrl() + WebApi.CLIENTSEARCHLIKE, searchString, new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                        itemClient = dBean.clients;
                        itemAllClient = new ArrayList<>();
                        itemAllClient.addAll(itemClient);
                        if (itemAllClient.size() > 0) {
                            SearchClientAdapter ada2 = new SearchClientAdapter(mContext, itemAllClient);
                            lvResult.setAdapter(ada2);
                            ada2.notifyDataSetChanged();
                        } else {
                            Toast.showText(mContext, "无数据");
                            setResult(-9998, null);
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        Toast.showText(mContext, Msg);
                    }
                });
            }else{
                ClientDao clientDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getClientDao();
                List<Client> clients = clientDao.queryBuilder().whereOr(ClientDao.Properties.FName.like("%" + searchString + "%"), ClientDao.Properties.FItemID.like("%" + searchString + "%")).orderAsc(ClientDao.Properties.FItemID).build().list();
                itemAllClient = new ArrayList<>();
                itemAllClient.addAll(clients);
                if (itemAllClient.size() > 0) {
                    SearchClientAdapter ada2 = new SearchClientAdapter(mContext, itemAllClient);
                    lvResult.setAdapter(ada2);
                    ada2.notifyDataSetChanged();
                } else {
                    Toast.showText(mContext, "未查询到数据");
                    setResult(-9998, null);
                    onBackPressed();
                }
            }
        //交货单位
        } else if (where == Info.SEARCHJH) {
            model.setText("编号");
            name.setText("名称");
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                Asynchttp.post(mContext, getBaseUrl() + WebApi.SEARCHJHSEARCHLIKE, searchString, new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                        goodsDepartmentList = dBean.getGoodsDepartments;
                        goodsDepartmentAllList = new ArrayList<>();
                        goodsDepartmentAllList.addAll(goodsDepartmentList);
                        if (goodsDepartmentAllList.size() > 0) {
                            SearchDepartmentAdapter ada1 = new SearchDepartmentAdapter(mContext, goodsDepartmentAllList);
                            lvResult.setAdapter(ada1);
                            ada1.notifyDataSetChanged();
                        } else {
                            Toast.showText(mContext, "无数据");
                            setResult(-9998, null);
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        Toast.showText(mContext, Msg);
                    }
                });
            }else{
                GetGoodsDepartmentDao getGoodsDepartmentDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getGetGoodsDepartmentDao();
                List<GetGoodsDepartment> list = getGoodsDepartmentDao.queryBuilder().whereOr(
                        GetGoodsDepartmentDao.Properties.FNumber.like("%" + searchString + "%"),
                        GetGoodsDepartmentDao.Properties.FName.like("%" + searchString + "%")
                ).build().list();
                goodsDepartmentList = new ArrayList<>();
                goodsDepartmentList.addAll(list);
                if (goodsDepartmentList.size() > 0) {
                    SearchDepartmentAdapter ada3 = new SearchDepartmentAdapter(mContext, goodsDepartmentList);
                    lvResult.setAdapter(ada3);
                    ada3.notifyDataSetChanged();
                } else {
                    Toast.showText(mContext, "未查询到数据");
                    setResult(-9998, null);
                    onBackPressed();
                }
            }

        }else if (where == Info.Search_DbType) {
            model.setText("编号");
            name.setText("名称");
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                Asynchttp.post(mContext, getBaseUrl() + WebApi.SearchDbType, searchString, new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                        dbTypeList = dBean.dbTypes;
//                        itemAllSupplier = new ArrayList<>();
//                        itemAllSupplier.addAll(suppliersList);
                        if (dbTypeList.size() > 0) {
                            SearchDbTypeAdapter ada1 = new SearchDbTypeAdapter(mContext, dbTypeList);
                            lvResult.setAdapter(ada1);
                            ada1.notifyDataSetChanged();
                        } else {
                            Toast.showText(mContext, "无数据");
                            setResult(-9998, null);
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        Toast.showText(mContext, Msg);
                    }
                });
            }else{
                SuppliersDao suppliersDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getSuppliersDao();
                List<Suppliers> list = suppliersDao.queryBuilder().whereOr(
                        SuppliersDao.Properties.FName.like("%" + searchString + "%"),
                        SuppliersDao.Properties.FItemID.like("%" + searchString + "%")
                ).orderAsc(SuppliersDao.Properties.FItemID).limit(50).build().list();
                itemAllSupplier = new ArrayList<>();
                itemAllSupplier.addAll(list);
                if (itemAllSupplier.size() > 0) {
                    SearchSupplierAdapter ada1 = new SearchSupplierAdapter(mContext, itemAllSupplier);
                    lvResult.setAdapter(ada1);
                    ada1.notifyDataSetChanged();
                } else {
                    Toast.showText(mContext, "未查询到数据");
                    setResult(-9998, null);
                    onBackPressed();
                }

            }
            //客户
        }else if (where == Info.Search_Storage) {
            model.setText("编号");
            name.setText("名称");
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                Asynchttp.post(mContext, getBaseUrl() + WebApi.SearchStorage, searchString, new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                        storageList = dBean.storage;
                        if (storageList.size() > 0) {
                            SearchStorageAdapter ada1 = new SearchStorageAdapter(mContext, storageList);
                            lvResult.setAdapter(ada1);
                            ada1.notifyDataSetChanged();
                        } else {
                            Toast.showText(mContext, "无数据");
                            setResult(-9998, null);
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        Toast.showText(mContext, Msg);
                    }
                });
            }else{
                StorageDao suppliersDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getStorageDao();
                List<Storage> list = suppliersDao.queryBuilder().whereOr(
                        StorageDao.Properties.FName.like("%" + searchString + "%"),
                        StorageDao.Properties.FItemID.like("%" + searchString + "%")
                ).orderAsc(StorageDao.Properties.FItemID).limit(50).build().list();
                storageList = new ArrayList<>();
                storageList.addAll(list);
                if (storageList.size() > 0) {
                    SearchStorageAdapter ada1 = new SearchStorageAdapter(mContext, storageList);
                    lvResult.setAdapter(ada1);
                    ada1.notifyDataSetChanged();
                } else {
                    Toast.showText(mContext, "未查询到数据");
                    setResult(-9998, null);
                    onBackPressed();
                }

            }
        }else if (where == Info.Search_CheckResult) {
            model.setText("编号");
            name.setText("名称");
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                Asynchttp.post(mContext, getBaseUrl() + WebApi.ResultSearchLike, searchString, new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                        resultList = dBean.checkResults;
                        if (resultList.size() > 0) {
                            SearchCheckResultAdapter ada1 = new SearchCheckResultAdapter(mContext, resultList);
                            lvResult.setAdapter(ada1);
                            ada1.notifyDataSetChanged();
                        } else {
                            Toast.showText(mContext, "无数据");
                            setResult(-9998, null);
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        Toast.showText(mContext, Msg);
                    }
                });
            }else{
//                StorageDao suppliersDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getStorageDao();
//                List<Storage> list = suppliersDao.queryBuilder().whereOr(
//                        StorageDao.Properties.FName.like("%" + searchString + "%"),
//                        StorageDao.Properties.FItemID.like("%" + searchString + "%")
//                ).orderAsc(StorageDao.Properties.FItemID).limit(50).build().list();
//                storageList = new ArrayList<>();
//                storageList.addAll(list);
//                if (storageList.size() > 0) {
//                    SearchStorageAdapter ada1 = new SearchStorageAdapter(mContext, storageList);
//                    lvResult.setAdapter(ada1);
//                    ada1.notifyDataSetChanged();
//                } else {
                    Toast.showText(mContext, "未查询到数据");
                    setResult(-9998, null);
                    onBackPressed();
//                }

            }
        }


    }

    @Override
    public void initListener() {
        lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent mIntent = new Intent();
                Bundle b = new Bundle();
                if (where == Info.SEARCHPRODUCT) {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.PRODUCTRETURN,itemAll.get(i)));
                    setResult(Info.SEARCHFORRESULT, mIntent);
                    onBackPressed();
                } else if (where == Info.SEARCHSUPPLIER) {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Search_Supplier,itemAllSupplier.get(i)));
                    b.putString("001", itemAllSupplier.get(i).FItemID);
                    b.putString("002", itemAllSupplier.get(i).FName);
                    mIntent.putExtras(b);
                    setResult(Info.SEARCHFORRESULTPRODUCT, mIntent);
                    onBackPressed();
                } else if (where == Info.SEARCHCLIENT) {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Search_client,itemAllClient.get(i)));
                    b.putString("001", itemAllClient.get(i).FItemID);
                    b.putString("002", itemAllClient.get(i).FName);
                    mIntent.putExtras(b);
                    setResult(Info.SEARCHFORRESULTCLIRNT, mIntent);
                    onBackPressed();
                } else if (where == Info.SEARCHJH) {
                    b.putString("001", goodsDepartmentList.get(i).FItemID);
                    b.putString("002", goodsDepartmentList.get(i).FName);
                    mIntent.putExtras(b);
                    setResult(Info.SEARCHFORRESULTJH, mIntent);
                    onBackPressed();
                } else if (where == Info.Search_DbType) {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Search_DbType,dbTypeList.get(i)));
                    onBackPressed();
                } else if (where == Info.Search_Storage) {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Search_Storage,storageList.get(i)));
                    onBackPressed();
                } else if (where == Info.Search_CheckResult) {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Search_CheckResult,resultList.get(i)));
                    onBackPressed();
                }

            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void OnReceive(String code) {

    }


    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
        this.overridePendingTransition(R.anim.bottom_end, 0);
    }

    @Override
    public void onBackPressed() {
        finish();
        this.overridePendingTransition(0, R.anim.bottom_end);
    }



}
