package com.oliver.news.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.widget.ImageView;

import com.oliver.news.activity.HomeActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @desc 图片的三级缓存类:
 * 1. 从内存获取数据
 * 2. 本地磁盘获取数据 cache
 * 3. 网络获取数据
 * @auther Oliver
 * @time 2016/11/29 14:34
 */
public class BitmapCacheUtil {
    private HomeActivity mContext;

    private Map<ImageView, String> iv_urls = new HashMap<>();//记录最后显示 ImageView 对应的 url


    /**
     * 获取可用内存的一般作为缓存容器大小
     */
    private int maxSize = (int) (Runtime.getRuntime().freeMemory() / 2);
    //android 提供的缓存机制类
    private LruCache<String, Bitmap> mLruCaches = new LruCache<String, Bitmap>(maxSize) {

        /**动态计算 存放信息大小 不会造成内存浪费*/
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount();//获取字节大小
//            return value.getRowBytes() * value.getHeight();
        }
    };
    private Bitmap bitmap;
    private Bitmap bitmapFromLocal;
    private final ExecutorService mThreadPool;//线程池


    public BitmapCacheUtil(HomeActivity context) {
        this.mContext = context;

        mThreadPool = Executors.newFixedThreadPool(6);
    }

    public void display(ImageView view, String url) {
        //1. 从内存获取数据
        bitmap = mLruCaches.get(url);
        if (bitmap != null) {
            L.d("-My- 内存中获取图片");
            /**缓存中有数据*/
            view.setImageBitmap(bitmap);

            /**往内存中保存一份*/
            writeBitmap2Memory(url, bitmap);

            return ;
        }

        //2. 本地磁盘获取数据
        bitmapFromLocal = getBitmapFromLocal(url);
        if (bitmapFromLocal != null) {
            L.d("-My- 本地中获取图片");

            /**本地有图片*/
            view.setImageBitmap(bitmapFromLocal);
            return;
        }

        //3. 网络获取数据

        /**保存到 hashMap 中*/
        iv_urls.put(view, url);
        getBitmapFromNet(view, url);

    }

    /**
     * 本地获取数据
     *
     * @param mUrl
     */
    private Bitmap getBitmapFromLocal(String mUrl) {
        File file = new File(mContext.getCacheDir(), mUrl.substring(mUrl.lastIndexOf('/')+1));
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        return bitmap;
    }

    /**
     * 网络获取数据
     *
     * @param view
     * @param url
     */
    private void getBitmapFromNet(ImageView view, String url) {
        //通过线程来做

        /**线程优化 线程池*/

//        new Thread(new BitmapFromNet(view, url)).start();
        mThreadPool.submit(new BitmapFromNet(view, url));

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
                    L.d(" -My-  网络获取图片");

                    /**请求成功*/
                    InputStream inputStream = connection.getInputStream();//获取 IO 流

                    /**把 is 转换成 bitmap*/
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);//网络图片的 bitmap

                    /**1. 内存存放*/
                    writeBitmap2Memory(mUrl, bitmap);


                    /**2. 本地 cache 目录存放*/
                    writeBitmap2Local(mUrl, bitmap);

                    /**3. 显示图片 主线程*/
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            /**判断当前 url 获取的图片是否是最新的*/
                            if (iv_urls.get(mView).equals(mUrl)) {
                                /**最新的 url*/
                                L.d(" -My-  网络获取数据");
                                mView.setImageBitmap(bitmap);
                            } else {
                                /**网速慢 造成的图片错位 不显示 图片*/
                            }




                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    /**
     * 本地中保存
     *
     * @param mUrl
     * @param bitmap
     */
    private void writeBitmap2Local(String mUrl, Bitmap bitmap) {
        //cache
        File cacheDir = mContext.getCacheDir();
        File file = new File(cacheDir, mUrl.substring(mUrl.lastIndexOf('/')+1));


        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));//图片类型、质量、流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 内存中保存数据
     *
     * @param mUrl
     * @param bitmap
     */
    private void writeBitmap2Memory(String mUrl, Bitmap bitmap) {

        mLruCaches.put(mUrl, bitmap);//内存中保存一份

    }


}

