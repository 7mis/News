package com.oliver.news;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.oliver.news.activity.GuideActivity;
import com.oliver.news.activity.HomeActivity;
import com.oliver.news.utils.L;
import com.oliver.news.utils.MyConstaints;
import com.oliver.news.utils.SPUtils;

public class SplashActivity extends AppCompatActivity {

    private ImageView iv_bg;
    private AnimationSet as;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initView();

        initAnimation();

        initEvent();
    }

    /**
     * 判断动画结束的事件
     */
    private void initEvent() {
        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                /*动画开始*/
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                /*动画结束，是否有设置
                * 1. 进入设置向导界面
                * 2. 进入主界面
                * */

                /**默认下，没有完成设置向导*/
                boolean isSetUpFinish = SPUtils.getBoolean(getApplicationContext(), MyConstaints.ISSETUPFINISH, false);
                if (isSetUpFinish) {
                    /*进入主界面*/
                    L.d("进入主界面");
                    Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    /*进入设置向导界面*/
                    L.d("进入设置向导界面");

                    Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                    startActivity(intent);
                    finish();
                }


            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                /*动画重复*/

            }
        });

    }

    /**
     * 动画
     */
    private void initAnimation() {
        //false 各自插入各自的动画器
        as = new AnimationSet(false);
        //旋转
        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(2000);//时长
        ra.setFillAfter(true);//停留在动画结束的位置
        //缩放
        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(2000);
        ra.setFillAfter(true);
        //渐变
        AlphaAnimation aa = new AlphaAnimation(0,1);
        aa.setDuration(2000);
        aa.setFillAfter(true);

        /**添加到动画集中*/
        as.addAnimation(ra);
        as.addAnimation(sa);
        as.addAnimation(aa);


        /**播放动画*/
        iv_bg.startAnimation(as);

    }

    /**
     * 视图
     */
    private void initView() {
        setContentView(R.layout.activity_splash);

        iv_bg = (ImageView) findViewById(R.id.splash_bg);
    }
}
