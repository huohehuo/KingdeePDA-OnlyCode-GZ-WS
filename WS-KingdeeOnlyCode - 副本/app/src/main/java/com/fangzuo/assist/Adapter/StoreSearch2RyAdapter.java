package com.fangzuo.assist.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fangzuo.assist.Beans.CheckLogBackBean;
import com.fangzuo.assist.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王璐阳 on 2018/3/27.
 */
//代码，名称，规格，总库存，单位
public class StoreSearch2RyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    public ArrayList<CheckLogBackBean> items;
    private RvInnerClickListener mListener;
    //1、定义一个集合，用来记录选中
    Context context;
    private OnItemClickListener onItemClickListener;

    public StoreSearch2RyAdapter(Context context, ArrayList<CheckLogBackBean> items) {
        this.items = items;
        this.context = context;

    }

    public void addAll(List<CheckLogBackBean> itemss) {
        items.clear();
        items.addAll(itemss);
        notifyDataSetChanged();
    }
    public void clear(){
        items.clear();
        notifyDataSetChanged();
    }
    public ArrayList<CheckLogBackBean> getAllData(){
        return items;
    }

//    @Override
//    public int getItemViewType(int position) {
//        return Integer.valueOf(items.get(position).getBackDateType());
//
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder holder = null;
//        if(1 == viewType){
            View v = mInflater.inflate(R.layout.item_store2_search,parent,false);
            holder = new MarkHolder(v);
//        }else if (2 == viewType){
//            View v = mInflater.inflate(R.layout.item_account_check_b,parent,false);
//            holder = new MainHolderTwo(v);
//        }else{
//            View v = mInflater.inflate(R.layout.item_account_check_c,parent,false);
//            holder = new MainHolderThree(v);
//        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
//        int pos;
            ((MarkHolder) holder).no1.setText(   "条码:"+items.get(position).getFBarCode());
            ((MarkHolder) holder).no2.setText(   "商品代码:"+items.get(position).getFNumber());
            ((MarkHolder) holder).no3.setText(   "商品名称:"+items.get(position).getFName());
            ((MarkHolder) holder).no4.setText(   "数量:"+items.get(position).getFNum());
            ((MarkHolder) holder).no5.setText(    "单位:"+items.get(position).getFUnit());
            ((MarkHolder) holder).no6.setText(    "规格:"+items.get(position).getFModel());

//            if (Double.parseDouble(items.get(position).getFLastMoney())>300000.00){
//                ((MainHolderThree) holder).FLastMoney.setTextColor(Color.RED);
//            }else{
//                ((MainHolderThree) holder).FLastMoney.setTextColor(Color.BLACK);
//            }
            ((MarkHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = ((MarkHolder) holder).getLayoutPosition();
//                    if (pos==position){
//                        ((MarkHolder) holder).name.setTextColor(Color.BLUE);
//                    }else{
//                        ((MarkHolder) holder).name.setTextColor(Color.BLACK);
//                    }
                    onItemClickListener.onItemClick(((MarkHolder) holder).itemView, pos);
                }
            });




    }

    public CheckLogBackBean getItems(int position) {
        return items.get(position);
    }



    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public void onClick(View view) {
        mListener.InnerItemOnClick(view);
    }

    public interface RvInnerClickListener {
        void InnerItemOnClick(View v);
    }

    public void setInnerClickListener(RvInnerClickListener mListener) {
        this.mListener = mListener;
    }

    public void addNewItem(CheckLogBackBean temp) {
        if (items != null) {
            items.add(temp);
            notifyItemInserted(items.size() + 1);
        }
    }

    public void deleteItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void setOnItemClickListener(StoreSearch2RyAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    class MarkHolder  extends RecyclerView.ViewHolder {

        private TextView no1;
        private TextView no2;
        private TextView no5;
        private TextView no3;
        private TextView no4;
        private TextView no6;

        public MarkHolder(View itemView) {
            super(itemView);
            no1        = (TextView) itemView.findViewById(R.id.tv_no1);
            no2        = (TextView) itemView.findViewById(R.id.tv_no2);
            no3        = (TextView) itemView.findViewById(R.id.tv_no3);
            no4        = (TextView) itemView.findViewById(R.id.tv_no4);
            no5        = (TextView) itemView.findViewById(R.id.tv_no5);
            no6        = (TextView) itemView.findViewById(R.id.tv_no6);

        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}
