package com.oliver.news.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.oliver.news.R;

/**
 * @desc
 * @auther Oliver
 * @time 2016/11/25 16:58
 */
public class RefreshListView extends ListView {

    private LinearLayout headRoot;
    private View refreshhead;
    private View viewFoot;

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        /**1. 初始化head*/
        initHead();
        /**2. 初始化foot*/
        initFoot();


    }

    /**
     * 初始化 foot
     */
    private void initFoot() {
        viewFoot = View.inflate(getContext(), R.layout.listview_foot, null);
        addFooterView(viewFoot);


    }


    /**加载轮播图
     * @param v_lunbo
     */
    public void addLunbo(View v_lunbo) {
        headRoot.addView(v_lunbo);
    }

    /**
     * 初始化 head
     */
    private void initHead() {
        headRoot = (LinearLayout) View.inflate(getContext(), R.layout.listview_head, null);

        refreshhead = headRoot.findViewById(R.id.ll_listview_head_refreshview);

        addHeaderView(headRoot);
    }
}
