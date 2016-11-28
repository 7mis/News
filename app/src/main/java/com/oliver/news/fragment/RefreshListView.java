package com.oliver.news.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oliver.news.R;
import com.oliver.news.utils.L;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @desc 只显示界面
 * @auther Oliver
 * @time 2016/11/25 16:58
 */
public class RefreshListView extends ListView {

    private LinearLayout headRoot;
    private LinearLayout mRefreshHeadView;
    private View viewFoot;
    private int mRefreshHeadHeight;
    private int mViewFootHeight;
    private float downY = -1;//初始化值，方便进行判断
    private View m_lunbo;

    private static final int PULLDOWN_STATE = 1;//下拉刷新的状态
    private static final int RELEASE_STATE = 2;//松开刷新的状态
    private static final int REFRESHING_STATE = 3;//正在刷新的状态
    private int reFreshState = PULLDOWN_STATE;//初始状态为下拉刷新的状态
    private ImageView iv_arrow;
    private ProgressBar pb_loading;
    private TextView tv_headStateDesc;
    private TextView tv_refreshTime;
    private RotateAnimation ra_up;
    private RotateAnimation ra_down;

    private boolean isLoadingMore = false;//是否在加载更多数据


    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        /**1. 初始化head*/
        initHead();
        /**2. 初始化foot*/
        initFoot();

        /**3. 初始化动画*/
        initAnimation();

        /**初始化事件*/
        initEvent();

    }

    /**
     * ListView 添加滑动事件
     */
    private void initEvent() {
        this.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                /**监听是否滑动到最后一条数据*/
                L.d("ListView 状态改变");
                //静止状态
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                    //是否滑动到最后一条数据
                    L.d("最后可视的位置 - " + getLastVisiblePosition() + "<>" + getAdapter().getCount());
                    if (getLastVisiblePosition() == getAdapter().getCount() - 1 && !isLoadingMore) {

                        isLoadingMore = true;

                        /**加载更多*/
                        L.d("上拉加载更多");

                        /**界面显示*/
                        viewFoot.setPadding(0, 0, 0, 0);


                        /**设置加载更多数据界面显示*/
                        setSelection(getAdapter().getCount());

                        if (mOnRefreshDataListener != null) {
                            mOnRefreshDataListener.loadMore();
                        }

                    }

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                /**高灵敏度*/

            }
        });
    }

    /**
     * 覆盖 touch 事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {


        /**拖动事件处理 分析：
         * 1. ListView 显示第一条数据时，拖动出下拉刷新的 View，从上往下拖动
         *
         */
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /*按下*/
                downY = ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:

                /**如果处于正在刷新的状态*/
                if (reFreshState == REFRESHING_STATE) {
                    return true;//屏蔽事件
                }

                if (!isLunboShow()) {
                    /**轮播图没有完全显示*/
                    break;//跳出当前语句块
                }

                /*拖动*/
                if (downY == -1) {
                    downY = ev.getY();
                }
                float moveY = ev.getY();

                float dy = moveY - downY;//间距
                /**ListView 显示第一个数据 + 从上往下 ： 拖动出下拉刷新的 View*/
                /**轮播图完全显示 才响应我们自己的 touch 事件：
                 * -
                 */
                if (getFirstVisiblePosition() == 0 && dy > 0) {
                    /**显示刷洗的 View*/
                    float hiddenHeight = -mRefreshHeadHeight + dy;
                    if (hiddenHeight >= 0 && reFreshState != RELEASE_STATE) {
                        reFreshState = RELEASE_STATE;
                        processState();

                    } else if (hiddenHeight < 0 && reFreshState != PULLDOWN_STATE) {
                        reFreshState = PULLDOWN_STATE;
                        processState();

                    }


                    /**设置位置*/

                    mRefreshHeadView.setPadding(0, (int) hiddenHeight, 0, 0);
                    return true;//走自己的 touch 事件
                }


                break;
            case MotionEvent.ACTION_UP:
                /*松开*/
                /**判断状态*/
                if (reFreshState == PULLDOWN_STATE) {
                    /**继续隐藏*/
                    mRefreshHeadView.setPadding(0, -mRefreshHeadHeight, 0, 0);
                } else if (reFreshState == RELEASE_STATE) {
                    /**松开刷新*/
                    reFreshState = REFRESHING_STATE;
                    processState();

                    /**刷新数据业务的调用*/
                    if (mOnRefreshDataListener != null) {
                        mOnRefreshDataListener.freshData();
                    }
                    mRefreshHeadView.setPadding(0, 0, 0, 0);
                }
                break;

            default:
                break;

        }

        return super.onTouchEvent(ev);//原生事件
    }

    /**
     * 加载更多 更新状态
     */
    public void updataState() {
        if (isLoadingMore) {
            /**加载更多*/
            viewFoot.setPadding(0, -mViewFootHeight, 0, 0);
            isLoadingMore = false;//防止继续做下拉刷新
        } else {
            /**下拉刷新*/
            updateRefreshState();
        }


    }


    /**
     * 更新刷新数据的状态
     */
    public void updateRefreshState() {
        /**改变状态 下拉刷新*/
        reFreshState = PULLDOWN_STATE;
        /**显示箭头*/
        iv_arrow.setVisibility(View.VISIBLE);
        /**隐藏进度条*/
        pb_loading.setVisibility(View.GONE);
        /**改变文字*/
        tv_headStateDesc.setText("下拉刷新");
        /**设置刷新时间*/
        tv_refreshTime.setText(getCurrentTime());
        /**隐藏刷新的 View*/
        mRefreshHeadView.setPadding(0, -mRefreshHeadHeight, 0, 0);
    }

    private String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    private void initAnimation() {
        ra_up = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra_up.setDuration(500);
        ra_up.setFillAfter(true);//动画结束的位置

        ra_down = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra_down.setDuration(500);
        ra_down.setFillAfter(true);//动画结束的位置
    }


    /**
     * 刷新数据状态的回调
     */
    private OnRefreshDataListener mOnRefreshDataListener;

    public void setOnRefreshDataListener(OnRefreshDataListener listener) {
        mOnRefreshDataListener = listener;
    }

    public interface OnRefreshDataListener {
        /**
         * 刷新数据
         */
        void freshData();

        /**
         * 加载更多
         */

        void loadMore();


    }


    /**
     * 状态处理
     */
    private void processState() {
        switch (reFreshState) {
            case PULLDOWN_STATE:
                //下拉刷新状态
                L.d("下拉刷新状态");
                /**箭头向下动画*/
                iv_arrow.startAnimation(ra_down);

                /**文字的切换*/
                tv_headStateDesc.setText("下拉刷新");

                break;
            case RELEASE_STATE:
                //松开刷新状态
                L.d("松开刷新状态");
                /**箭头向上动画*/
                iv_arrow.startAnimation(ra_up);
                /**文字切换*/
                tv_headStateDesc.setText("松开刷新");

                break;
            case REFRESHING_STATE:
                //正在刷新状态
                L.d("正在刷新状态");

                /**清除动画*/
                iv_arrow.clearAnimation();
                /**隐藏箭头*/
                iv_arrow.setVisibility(View.GONE);
                /**显示进度*/
                pb_loading.setVisibility(View.VISIBLE);
                /**改变文字*/
                tv_headStateDesc.setText("正在刷新");


                break;
            default:
                break;

        }
    }


    /**
     * 加载轮播图
     *
     * @param v_lunbo
     */
    public void addLunbo(View v_lunbo) {
        m_lunbo = v_lunbo;
        headRoot.addView(v_lunbo);
    }


    /**
     * 轮播图是否完全显示
     *
     * @return
     */
    public boolean isLunboShow() {
        int[] location = new int[2];
        /**获取 ListView 在屏幕中的坐标*/
        this.getLocationInWindow(location);

        /**ListView 在屏幕中的 Y 坐标*/
        int lv_Y = location[1];

        /**获取轮播图在屏幕中的位置*/

        m_lunbo.getLocationInWindow(location);

        /**获取轮播图在屏幕中的 Y 坐标*/
        int lunbo_Y = location[1];

        if (lunbo_Y >= lv_Y) {
            return true;
        } else {
            return false;
        }


    }

    /**
     * 初始化 head
     */
    private void initHead() {
        headRoot = (LinearLayout) View.inflate(getContext(), R.layout.listview_head, null);

        mRefreshHeadView = (LinearLayout) headRoot.findViewById(R.id.ll_listview_head_refreshview);

        /**获取子控件*/
        iv_arrow = (ImageView) headRoot.findViewById(R.id.iv_listview_head_arrow);
        pb_loading = (ProgressBar) headRoot.findViewById(R.id.pb_listview_head_loading);
        tv_headStateDesc = (TextView) headRoot.findViewById(R.id.tv_listview_head_statedesc);
        tv_refreshTime = (TextView) headRoot.findViewById(R.id.tv_listview_head_time);


        /**隐藏*/
        mRefreshHeadView.measure(0, 0);
        mRefreshHeadHeight = mRefreshHeadView.getMeasuredHeight();
        /**隐藏下拉刷新头*/
        mRefreshHeadView.setPadding(0, -mRefreshHeadHeight, 0, 0);
        addHeaderView(headRoot);
    }


    /**
     * 初始化 foot
     */
    private void initFoot() {
        viewFoot = View.inflate(getContext(), R.layout.listview_foot, null);

        /**隐藏加载更多 footer*/
        viewFoot.measure(0, 0);
        mViewFootHeight = viewFoot.getMeasuredHeight();
        viewFoot.setPadding(0, -mViewFootHeight, 0, 0);
        addFooterView(viewFoot);


    }


    public RefreshListView(Context context) {
        this(context, null);
    }
}
