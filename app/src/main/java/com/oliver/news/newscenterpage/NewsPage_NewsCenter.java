package com.oliver.news.newscenterpage;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.oliver.news.activity.HomeActivity;

/**
 * @desc 新闻 页面
 * @auther Oliver
 * @time 2016/11/21 10:57
 */
public class NewsPage_NewsCenter extends BaseNewsCenterPage {


    public NewsPage_NewsCenter(HomeActivity context) {
        super(context);
    }

    @Override
    public View initView() {

        /*内容*/
        TextView textView = new TextView(mContext);
        textView.setText("新闻 --- 的内容");
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }
}
