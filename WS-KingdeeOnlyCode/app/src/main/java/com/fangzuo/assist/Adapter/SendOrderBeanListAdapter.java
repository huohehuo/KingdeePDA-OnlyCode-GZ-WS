package com.fangzuo.assist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fangzuo.assist.Beans.SendOrderListBean;
import com.fangzuo.assist.Beans.SendOrderResult;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.DataModel;
import com.fangzuo.assist.Utils.MathUtil;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NB on 2017/8/25.
 */

public class SendOrderBeanListAdapter extends BaseAdapter {
    Context context;
    List<SendOrderListBean> items;

    public SendOrderBeanListAdapter(Context context, List<SendOrderListBean> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void clear(){
        items.clear();
        notifyDataSetChanged();
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.send_order_product_list, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }



//        viewHolder.billNo.setText("订单编号:"+items.get(i).getCode());
        viewHolder.tvName.setText("商品名称:"+items.get(i).FName);
        viewHolder.tvNumber.setText("商品代码:"+items.get(i).FNumber);
        viewHolder.tvModel.setText("规格型号:"+items.get(i).FModel);
        viewHolder.tvNum.setText("数量:"+items.get(i).FQty);
        viewHolder.tvIsGif.setText("仓库:"+items.get(i).FStorage);
//        String num = DataModel.getDetailNum(context,items.get(i).FNumber,items.get(i).FWlNo);
        viewHolder.tvNumIng.setText("已验数量:"+items.get(i).FQtying);
        if((int) (MathUtil.toD(items.get(i).FQtying)/MathUtil.toD(items.get(i).FQty)*100)==100){
            view.setBackgroundColor(viewHolder.blue);
        }else{
            view.setBackgroundColor(viewHolder.white);
        }
//        if (items.get(i).getIs_gift()==0){
//            viewHolder.tvIsGif.setVisibility(View.GONE);
//        }else{
//            viewHolder.tvIsGif.setVisibility(View.VISIBLE);
//            if (items.get(i).getIs_gift()==1)viewHolder.tvIsGif.setText("赠品");
//        }

//        viewHolder.productId.setText("物料名称:"+ items.get(i).FName);
//        viewHolder.tvModel.setText("规格型号:"+ items.get(i).FModel);
//        viewHolder.numyanshouing.setText("已验数量:"+items.get(i).FQtying);
//        viewHolder.tvLastnum.setText("未验数量:"+ DoubleUtil.Cut4((MathUtil.toD(items.get(i).FQty)-MathUtil.toD(items.get(i).FQtying))+""));
//        viewHolder.unit.setText("单位:"+unit.FName);
        viewHolder.pg.setProgress((int) (MathUtil.toD(items.get(i).FQtying)/MathUtil.toD(items.get(i).FQty)*100));
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.billNo)
        TextView billNo;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_model)
        TextView tvModel;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_num_ing)
        TextView tvNumIng;
        @BindView(R.id.tv_is_gif)
        TextView tvIsGif;

        @BindView(R.id.progress)
        ProgressBar pg;
        @BindColor(R.color.cpb_blue)int blue;
        @BindColor(R.color.white)int white;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
