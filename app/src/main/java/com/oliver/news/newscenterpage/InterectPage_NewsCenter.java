package com.oliver.news.newscenterpage;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.oliver.news.activity.HomeActivity;

/**
 * @desc 互动页面
 * @auther Oliver
 * @time 2016/11/21 10:57
 */
public class InterectPage_NewsCenter extends BaseNewsCenterPage {

    public InterectPage_NewsCenter(HomeActivity mContext) {
        super(mContext);
    }

    @Override
    public View initView() {
        /*内容*/
        TextView textView = new TextView(mContext);
        textView.setText("互动 --- 的内容");
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }
}
