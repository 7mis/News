package com.oliver.news.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.oliver.news.activity.HomeActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @desc 图片的三级缓存类:
 * 1. 从内存获取数据
 * 2. 本地磁盘获取数据 cache
 * 3. 网络获取数据
 * @auther Oliver
 * @time 2016/11/29 14:34
 */
public class BitmapCacheUtils {
    private HomeActivity mContext;

    public BitmapCacheUtils(HomeActivity context) {
        this.mContext = context;
    }

    public void display(ImageView view, String url) {
        //1. 从内存获取数据

        //2. 本地磁盘获取数据

        //3. 网络获取数据
        getBitmapFromNet(view, url);

    }

    /**
     * 网络获取数据
     *
     * @param view
     * @param url
     */
    private void getBitmapFromNet(ImageView view, String url) {
        //通过线程来做
        new Thread(new BitmapFromNet(view, url)).start();

    }

    /**
     * 网络获取数据
     */
    private class BitmapFromNet implements Runnable {
        private ImageView mView;
        private String mUrl;

        public BitmapFromNet(ImageView view, String url) {
            this.mView = view;
            this.mUrl = url;
        }

        @Override
        public void run() {
            /**1. 请求网络*/
            try {
                URL url = new URL(mUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(7000);//7s 的超时设置
                connection.setRequestMethod("GET");// 请求方式
                int responseCode = connection.getResponseCode();//请求码
                if (responseCode == 200) {
                    L.d(" -My-  请求网络成功");

                    /**请求成功*/
                    InputStream inputStream = connection.getInputStream();//获取 IO 流

                    /**把 is 转换成 bitmap*/
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);//网络图片的 bitmap

                    /**1. 内存存放*/


                    /**2. 本地 cache 目录存放*/

                    /**3. 显示图片 主线程*/
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            L.d(" -My-  网络获取数据");
                            mView.setImageBitmap(bitmap);

                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

}
