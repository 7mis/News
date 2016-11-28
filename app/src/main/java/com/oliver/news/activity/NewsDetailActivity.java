package com.oliver.news.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.oliver.news.R;
import com.oliver.news.utils.MyConstaints;

/**
 * @desc 新闻显示界面
 * @auther Oliver
 * @time 2016/11/28 13:00
 */
public class NewsDetailActivity extends AppCompatActivity {

    private ImageView iv_back;
    private ImageView iv_textSize;
    private ImageView iv_share;
    private WebView wv_news;
    private ProgressBar pb_loading;

    private int textSizeIndex = 2;
    private Dialog dialog;
    private WebSettings webSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        initData();
        initEvent();
    }

    /**
     * 数据
     */
    private void initData() {
        String newsUrl = getIntent().getStringExtra(MyConstaints.NEWSDETAILURL);

        /*webview*/
        wv_news.loadUrl(newsUrl);//加载 url 对应的页面数据


    }

    /**
     * 事件
     */
    private void initEvent() {
        /**监听数据加载完成的事件*/
        wv_news.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                /**页面加载数据完成的回调*/
                /**隐藏进度条*/
                pb_loading.setVisibility(View.GONE);

                super.onPageFinished(view, url);
            }
        });


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_basepage_arrow:
                        // 返回
                        finish();
                        break;
                    case R.id.iv_basepage_share:
                        share();// 分享
                        break;

                    case R.id.iv_basepage_textsize:
                        showTextSizeDialog();// 修改字体大小
                        break;
                    default:
                        break;
                }
            }
        };

        // 设置三个按钮的事件
        iv_back.setOnClickListener(listener);
        iv_share.setOnClickListener(listener);
        iv_textSize.setOnClickListener(listener);

    }

    private void showTextSizeDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("字体大小");
        ab.setSingleChoiceItems(new String[] { "最小", "较小", "正常", "较大", "最大" },
                textSizeIndex, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textSizeIndex = which;
                        textSize(textSizeIndex + 1);
                        dialog.dismiss();
                    }
                });
        dialog = ab.create();
        dialog.show();
    }

    private static final int LARGEST = 5;// 字体最大
    private static final int LARGER = 4;// 字体最大
    private static final int NORMAL = 3;// 字体最大
    private static final int SMALLER = 2;// 字体最大
    private static final int SMALLEST = 1;// 字体最大

    private void textSize(int index) {
        switch (index) {
            case LARGEST:
                webSettings.setTextSize(WebSettings.TextSize.LARGEST);
                break;
            case LARGER:
                webSettings.setTextSize(WebSettings.TextSize.LARGER);
                break;
            case NORMAL:
                webSettings.setTextSize(WebSettings.TextSize.NORMAL);
                break;
            case SMALLER:
                webSettings.setTextSize(WebSettings.TextSize.SMALLER);
                break;
            case SMALLEST:
                webSettings.setTextSize(WebSettings.TextSize.SMALLEST);
                break;

            default:
                break;
        }
    }

    private void share() {

    }

    /**
     * 界面
     */
    private void initView() {
        setContentView(R.layout.activity_news_detail);

        /*隐藏 menu*/
        findViewById(R.id.iv_basepage_menu).setVisibility(View.GONE);
        /*隐藏 标题*/
        findViewById(R.id.tv_basepage_title).setVisibility(View.GONE);

        /*显示箭头*/
        iv_back = (ImageView) findViewById(R.id.iv_basepage_arrow);
        iv_back.setVisibility(View.VISIBLE);
        /*显示字体大小*/
        iv_textSize = (ImageView) findViewById(R.id.iv_basepage_textsize);
        iv_textSize.setVisibility(View.VISIBLE);
        /*显示分享*/
        iv_share = (ImageView) findViewById(R.id.iv_basepage_share);
        iv_share.setVisibility(View.VISIBLE);


        /*webview*/
        wv_news = (WebView) findViewById(R.id.wv_newsdetail);

        // 设置WebView的参数
        webSettings = wv_news.getSettings();

        // 设置可以运行js
        webSettings.setJavaScriptEnabled(true);

        // 设置缩放按钮
        webSettings.setBuiltInZoomControls(true);

        // 双击放大或缩小
        webSettings.setUseWideViewPort(true);

        /*progressbar*/
        pb_loading = (ProgressBar) findViewById(R.id.pb_newsdetail);

    }
}
