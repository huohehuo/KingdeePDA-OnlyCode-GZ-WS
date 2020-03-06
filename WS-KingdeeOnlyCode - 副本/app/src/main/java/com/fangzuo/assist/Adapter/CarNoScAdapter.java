package com.fangzuo.assist.Adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fangzuo.assist.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

public class CarNoScAdapter extends RecyclerArrayAdapter<String> {
    Context context;

    public CarNoScAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MarkHolder(parent);
    }
    class MarkHolder extends BaseViewHolder<String> {

        private TextView name;
        public MarkHolder(ViewGroup parent) {
            super(parent, R.layout.item_billno_search);
            name= $(R.id.tv_name);
//            checkBox = $(R.id.view_cb);
        }

        @Override
        public void setData(String data) {
            super.setData(data);
            name.setText(data);
        }
    }


//    //纯文字布局
//    class MainHolderForTxt extends BaseViewHolder<PlanBean> {
//
//        private TextView time;
//        private TextView eesay;
//        private ImageView favour;
//        private TextView num;
//        public MainHolderForTxt(ViewGroup parent) {
//            super(parent, R.layout.item_plan_for_txt);
//            time = $(R.id.tv_time);
//            eesay = $(R.id.tv_main_essay);
//            num = $(R.id.tv_favour);
//            favour = $(R.id.iv_favour);
//        }
//
//        @Override
//        public void setData(PlanBean data) {
//            super.setData(data);
//            eesay.setText(data.getEssay());
//            time.setText(data.getCreatedAt());
////            num.setText(data.getFavour().get__op());
//
//            favour.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(App.getContext(), "喜欢+1", Toast.LENGTH_SHORT).show();
//                }
//            });
//
////            Glide.with(getContext())
////                    .load(data.getPic())
////                    .placeholder(R.mipmap.ic_launcher)
////                    .centerCrop()
////                    .into(imageView);
//
//        }
//    }
}
