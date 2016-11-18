package com.oliver.news.fragment;

import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/11/18.
 */
public class LeftFragment extends BaseFragment {
    @Override
    public View initView() {
        TextView tv = new TextView(mContext);
        tv.setText("LeftFragment");
        tv.setTextSize(30);

        return tv;
    }
}
