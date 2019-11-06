package com.fangzuo.assist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fangzuo.assist.Beans.InOutBean;
import com.fangzuo.assist.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NB on 2017/9/6.
 */

public class CheckFirstInOutAdapter extends BaseAdapter {
    Context context;
    List<InOutBean> data;

    private ViewHolder viewHolder;

    public CheckFirstInOutAdapter(Context context, List<InOutBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_check_first_inout, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvName.setVisibility(View.VISIBLE);
        viewHolder.tvName.setText("物料名："+data.get(i).FName);
        viewHolder.tvBatch.setText("条码："+data.get(i).FBarCode);
        viewHolder.tvQty.setText("库存："+data.get(i).FQty);
        viewHolder.tvStorage.setText("仓库："+data.get(i).FStockName);
        viewHolder.tvWavehouse.setText("仓位："+data.get(i).FStockPlaceName);
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_batch)
        TextView tvBatch;
        @BindView(R.id.tv_qty)
        TextView tvQty;
        @BindView(R.id.tv_storage)
        TextView tvStorage;
        @BindView(R.id.tv_wavehouse)
        TextView tvWavehouse;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
