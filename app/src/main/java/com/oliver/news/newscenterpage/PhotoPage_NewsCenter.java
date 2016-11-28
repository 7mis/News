package com.oliver.news.newscenterpage;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.oliver.news.R;
import com.oliver.news.activity.HomeActivity;
import com.oliver.news.domain.PhotoNewsData;
import com.oliver.news.utils.L;
import com.oliver.news.utils.SPUtils;
import com.oliver.news.utils.T;

import java.util.List;

/**
 * @desc 组图页面
 * @auther Oliver
 * @time 2016/11/21 10:57
 */
public class PhotoPage_NewsCenter extends BaseNewsCenterPage {
    @ViewInject(R.id.lv_photos)
    private ListView lv_showData;
    @ViewInject(R.id.gv_photos)
    private GridView gv_showData;
    private Object dataFromNet;
    private MyAdapter mAdapter;
    private List<PhotoNewsData.DataBean.NewsBean> mNews;
    private final BitmapUtils mBitmapUtils;

    public PhotoPage_NewsCenter(HomeActivity mContext) {
        super(mContext);


        mBitmapUtils = new BitmapUtils(mContext);
    }

    @Override
    public View initView() {
        /**显示 ListView or GirdView*/
        View rootView = View.inflate(mContext, R.layout.photos_view, null);

        ViewUtils.inject(this, rootView);
        return rootView;
    }

    @Override
    public void initData() {
        /**本地数据*/


        /**网络数据*/
        String photoUrl = mContext.getString(R.string.photourl);
        getDataFromNet(photoUrl);
        super.initData();
    }

    /**
     * 网络获取数据
     *
     * @param photoUrl
     * @return
     */
    public void getDataFromNet(final String photoUrl) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, photoUrl, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                L.d("网络请求成功 - photos");

                /**1. 获取 json 数据*/
                String jsonData = responseInfo.result;

                /**2. 保存  json 数据到本地*/
                SPUtils.putString(mContext, photoUrl, jsonData);

                /**3. 解析 json 数据*/
                PhotoNewsData photoNewsData = parseJsonData(jsonData);


                /**4. 处理 json 数据*/
                processData(photoNewsData);

            }


            @Override
            public void onFailure(HttpException error, String msg) {
                L.d("网络请求失败 - photos");
                T.showShort(mContext, "网络请求失败");
            }
        });

    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (mNews == null)
                return 0;
            return mNews.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_photos_lv_gv, null);
                viewHolder = new ViewHolder();
                convertView.setTag(viewHolder);

                viewHolder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_photos_item_pic);
                viewHolder.tv_desc = (TextView) convertView.findViewById(R.id.tv_photos_item_desc);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            /**数据*/
            PhotoNewsData.DataBean.NewsBean news = mNews.get(position);
            /*desc*/
            viewHolder.tv_desc.setText(news.getTitle());

            /*图片*/
            mBitmapUtils.display(viewHolder.iv_pic, news.getListimage());


            return convertView;
        }
    }

    /**
     * 组件 缓存复用
     */
    private class ViewHolder {
        ImageView iv_pic;
        TextView tv_desc;
    }

    /**
     * 处理 json 数据
     *
     * @param photoNewsData
     */
    private void processData(PhotoNewsData photoNewsData) {
        //listView gridView

        /**获取最新的数据*/
        mNews = photoNewsData.getData().getNews();


        if (mAdapter == null) {
            mAdapter = new MyAdapter();
            //设置给 ListView 和 GridView
            lv_showData.setAdapter(mAdapter);
            gv_showData.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }


    }


    /**
     * 解析 json 数据 组图
     *
     * @param jsonData
     */
    private PhotoNewsData parseJsonData(String jsonData) {
        Gson gson = new Gson();
        PhotoNewsData photoNewsData = gson.fromJson(jsonData, PhotoNewsData.class);

        return photoNewsData;

    }
}
