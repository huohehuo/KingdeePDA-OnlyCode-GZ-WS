package com.fangzuo.assist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.T_mainDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NB on 2017/8/3.
 */

public class ReView1Adapter extends BaseAdapter implements View.OnClickListener {
    Context context;
    List<T_Detail> detail;
    private ViewHolder viewHolder;
    InnerClickListener mListener;
    List<Boolean> isCheck;

    public ReView1Adapter(Context context, List<T_Detail> detail, List<Boolean> isCheck) {
        this.context = context;
        this.detail = detail;
        this.isCheck = isCheck;
    }

    @Override
    public int getCount() {
        return detail.size();
    }

    @Override
    public Object getItem(int i) {
        return detail.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_review1_layout, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
//        if (detail.get(i).activity == Config.CreateSaleOutActivity) {
//            viewHolder.tvCode.setVisibility(View.VISIBLE);
            viewHolder.tvCode.setText("条码:" + detail.get(i).FBarcode);
//        }
        viewHolder.tvNumber.setText("商品编码:" + detail.get(i).FProductCode);
        viewHolder.tvName.setText("商品名称:" + detail.get(i).FProductName);


//
//            viewHolder.delete.setOnClickListener(this);
//            viewHolder.delete.setTag(i);
//            viewHolder.fix.setOnClickListener(this);
//            viewHolder.fix.setTag(i);
        viewHolder.cbIscheck.setChecked(isCheck.get(i));

        return view;
    }

    @Override
    public void onClick(View view) {
        mListener.InOnClick(view);
    }

    public void setInnerListener(InnerClickListener mListener) {
        this.mListener = mListener;
    }

    public interface InnerClickListener {
        void InOnClick(View v);
    }


    static class ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_code)
        TextView tvCode;
        @BindView(R.id.cb_ischeck)
        CheckBox cbIscheck;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
