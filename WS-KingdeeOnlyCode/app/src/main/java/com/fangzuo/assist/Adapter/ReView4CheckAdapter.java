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

public class ReView4CheckAdapter extends BaseAdapter implements View.OnClickListener {
    Context context;
    List<T_main> main;
    List<T_Detail> detail;
    private ViewHolder viewHolder;
    InnerClickListener mListener;
    List<Boolean> isCheck;

    public ReView4CheckAdapter(Context context, List<T_Detail> detail, List<Boolean> isCheck) {
        this.context = context;
        this.main = main;
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
            view = LayoutInflater.from(context).inflate(R.layout.list_layout_check, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvItme1.setText("物料名称："+detail.get(i).FProductName);
        viewHolder.tvItme2.setText("物料规格："+detail.get(i).model);
        if (detail.get(i).activity == Config.PdProductGetCheckActivity){
            viewHolder.tvItme3.setText("批次："+detail.get(i).FBatch);
            viewHolder.tvItme4.setText("已验数量："+detail.get(i).FCheckNum);
            viewHolder.tvItme5.setVisibility(View.GONE);
            viewHolder.tvItme6.setVisibility(View.GONE);
            viewHolder.tvItme7.setVisibility(View.GONE);
        }else{
            viewHolder.tvItme3.setText("检验数量："+detail.get(i).FCheckNum);
            viewHolder.tvItme4.setText("合格数量："+detail.get(i).FCheckPassNum);
            viewHolder.tvItme5.setText("样品破坏数："+detail.get(i).FCheckBrokenNum);
            viewHolder.tvItme6.setText("质检方案："+detail.get(i).FCheckTypeName);
            viewHolder.tvItme7.setText("质检结果："+detail.get(i).FCheckResultName);
        }



            viewHolder.delete.setOnClickListener(this);
            viewHolder.delete.setTag(i);
            viewHolder.fix.setOnClickListener(this);
            viewHolder.fix.setTag(i);
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
        @BindView(R.id.tv_item1)
        TextView tvItme1;
        @BindView(R.id.tv_item2)
        TextView tvItme2;
        @BindView(R.id.tv_item3)
        TextView tvItme3;
        @BindView(R.id.tv_item4)
        TextView tvItme4;
        @BindView(R.id.tv_item5)
        TextView tvItme5;
        @BindView(R.id.tv_item6)
        TextView tvItme6;
        @BindView(R.id.tv_item7)
        TextView tvItme7;

        @BindView(R.id.delete)
        Button delete;
        @BindView(R.id.fix)
        Button fix;
        @BindView(R.id.cb_ischeck)
        CheckBox cbIscheck;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }



}
