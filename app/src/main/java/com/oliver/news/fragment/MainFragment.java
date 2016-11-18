package com.oliver.news.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/11/18.
 */
public class MainFragment extends BaseFragment {
    @Override
    public View initView() {
        TextView tv = new TextView(mContext);
        tv.setText("MainFragment");
        tv.setTextSize(30);
        tv.setBackgroundColor(Color.LTGRAY);

        return tv;
    }
}
