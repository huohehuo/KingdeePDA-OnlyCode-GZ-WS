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
public class LogSearch2RyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    public ArrayList<CheckLogBackBean> items;
    private RvInnerClickListener mListener;
    //1、定义一个集合，用来记录选中
    Context context;
    private OnItemClickListener onItemClickListener;

    public LogSearch2RyAdapter(Context context, ArrayList<CheckLogBackBean> items) {
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
            View v = mInflater.inflate(R.layout.item_log_2_search,parent,false);
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
            ((MarkHolder) holder).billno.setText(   "单据编号:"+items.get(position).getFBillNo());
            ((MarkHolder) holder).sn.setText(   "SN号:"+items.get(position).getFBarCode());
            ((MarkHolder) holder).clienName.setText(   "客户名称:"+items.get(position).getFCLientName());
            ((MarkHolder) holder).num.setText(   "数量:"+items.get(position).getFNum());
            ((MarkHolder) holder).price.setText(    "单价:"+items.get(position).getFPrice());
//            ((MarkHolder) holder).num.setText(    items.get(position).getFQty());
//            ((MarkHolder) holder).helpcode.setText(    items.get(position).getFHelpCode());
//            ((MarkHolder) holder).pici.setText(items.get(position).getFBatchNo());

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

    public void setOnItemClickListener(LogSearch2RyAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    class MarkHolder  extends RecyclerView.ViewHolder {

        private TextView billno;
        private TextView sn;
        private TextView clienName;
        private TextView price;
//        private TextView name;
        private TextView num;
        private TextView helpcode;
//        private TextView pici;

        public MarkHolder(View itemView) {
            super(itemView);
            sn        = (TextView) itemView.findViewById(R.id.tv_no0);
            billno        = (TextView) itemView.findViewById(R.id.tv_no1);
            clienName        = (TextView) itemView.findViewById(R.id.tv_no2);
//            name        = (TextView) itemView.findViewById(R.id.tv_no3);
            num        = (TextView) itemView.findViewById(R.id.tv_no4);
            price        = (TextView) itemView.findViewById(R.id.tv_no5);

        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}
