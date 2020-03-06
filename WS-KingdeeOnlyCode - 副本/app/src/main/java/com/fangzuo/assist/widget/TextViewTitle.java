package com.fangzuo.assist.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzuo.assist.R;


public class TextViewTitle extends RelativeLayout {
    // 返回按钮控件
    // 标题Tv
    private TextView titleone;
    private TextView titletwo;
    private View line;
    public TextViewTitle(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_textview_title, this);
        // 获取控件
        titleone = (TextView) findViewById(R.id.tv_one);
        titletwo = (TextView) findViewById(R.id.tv_two);
        line = (View) findViewById(R.id.linecenter);
        TypedArray attrArray = context.obtainStyledAttributes(attributeSet, R.styleable.Style_TextView_Title);
        int count = attrArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attrName = attrArray.getIndex(i);
            switch (attrName) {
                case R.styleable.Style_TextView_Title_TextView_Title_one:
                    titleone.setText(attrArray.getString(R.styleable.Style_TextView_Title_TextView_Title_one));
                    break;
                case R.styleable.Style_TextView_Title_TextView_Title_two:
                    titletwo.setText(attrArray.getString(R.styleable.Style_TextView_Title_TextView_Title_two));
                    break;
                case R.styleable.Style_TextView_Title_TextView_Title_size:
                    titleone.setTextSize(attrArray.getDimension(R.styleable.Style_TextView_Title_TextView_Title_size, 10));
                    titletwo.setTextSize(attrArray.getDimension(R.styleable.Style_TextView_Title_TextView_Title_size, 10));
                    break;
                case R.styleable.Style_TextView_Title_TextView_Title_justone:
                    titletwo.setVisibility(attrArray.getBoolean(R.styleable.Style_TextView_Title_TextView_Title_justone,false)?GONE:VISIBLE);
                    line.setVisibility(attrArray.getBoolean(R.styleable.Style_TextView_Title_TextView_Title_justone,false)?GONE:VISIBLE);
                    break;
            }
        }
        attrArray.recycle();
    }

}
