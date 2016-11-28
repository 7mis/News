package com.oliver.news.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oliver.news.activity.HomeActivity;

/**
 * fragment 的基类:
 * 1. 界面 必须实现
 * 2. 数据 ---
 * 3. 事件 ---
 *
 * Created by Administrator on 2016/11/18.
 */
public abstract class BaseFragment extends Fragment {

    protected HomeActivity mContext;

    /**
     * 只要 activity 不销毁，该方法不会重复调用
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        initData();
        initEvent();
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 依附在 activity
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //获取上下文
        mContext = (HomeActivity) getActivity();
        super.onCreate(savedInstanceState);
    }

    /** 显示 View
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    /**子类必须要实现一个 View*/
    public abstract View initView();


    /**
     * 子类覆盖此方法完成数据的初始化
     */
    public void initData() {
    }

    /**
     * 子类覆盖此方法完成事件的初始化
     */
    public void initEvent() {

    }

    /**
     * 生命周期图，onCreateView() 下面
     * 调用初始化数据和事件
     */
    @Override
    public void onStart() {
//        initData();
//        initEvent();
        super.onStart();
    }
}
