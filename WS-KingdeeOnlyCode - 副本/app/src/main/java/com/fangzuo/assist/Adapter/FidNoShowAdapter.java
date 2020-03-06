package com.fangzuo.assist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fangzuo.assist.Beans.FidNoShowBean;
import com.fangzuo.assist.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 王璐阳 on 2017/11/8.
 */

public class FidNoShowAdapter extends BaseAdapter {
    Context context;
    ArrayList<FidNoShowBean> items;
    private ViewHolder viewHolder;

    public FidNoShowAdapter(Context context, ArrayList<FidNoShowBean> items) {
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_fidshow, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvName.setText(items.get(i).FOrder);
        viewHolder.tvNum.setText(items.get(i).FNum);
        viewHolder.tvState.setText(items.get(i).FState);
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_state)
        TextView tvState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
