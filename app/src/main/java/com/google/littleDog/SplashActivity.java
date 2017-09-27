package com.google.littleDog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.kupai_ads.MainActivity;
import com.umeng.analytics.MobclickAgent;
import com.yulong.sdk.ad.ssp_sdk.HjAdManager;
import com.yulong.sdk.ad.ssp_sdk.listener.HjAdListener;

/**
 * Created by appchina on 2017/3/7.
 */

public class SplashActivity extends Activity {

    static final String ADPID = "1705100002";
    static final String APP_ID = "6";   // 广告id

    private static final boolean ASK_BANNER_AD = true;
    static final String UMENG_KEY = "58de2bdae88bad2029001684";
    private static boolean isAdClick = false;   // 广告是不是被点击了
    private static boolean isAdSkip = true; // 是否点广告跳过
    private static final String TAG = "SplashActivity";
    private static final boolean ISDEBUG = false;


    static Handler handler;
    private boolean isIntented = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        showSplash(this);

    }

    private void init() {



        HjAdManager.init(this,APP_ID);

        /**
         * 友盟 初始化
         * cGold : 是渠道号
         * 584912f375ca3528ff00056d : 是友盟 key
         */
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this, UMENG_KEY, getPackageName()));

        if (ASK_BANNER_AD){
//            initBanner(this);
        }

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what){
                    case 0:
                        gotoNextActivity("onSplashAdFailed");
                        break;
                    case 1:
                        gotoNextActivity("onSplashAdDismiss");
                        break;
                    case 2:
                        gotoNextActivity("isAdSkip ads");
                        break;
                    case 3:
                        gotoNextActivity("onResume");
                        break;
                    default:
                        gotoNextActivity("default");
                        break;
                }

            }
        };

    }



    /**
     *  banner 广告
     * @param context
     */
//    public static void initBanner(final Activity context){
//        /************** 将以下内容添加到onCreate方法中 **********************/
////        LinearLayout view = new LinearLayout(this);
//        AdView adView = new AdView(context, ADPID);
//        adView.setAdListener(new AdBannerListener() {
//            @Override
//            public void onAdShow(Object o) {
//                MobclickAgent.onEvent(context,"ban_show");
//            }
//
//            @Override
//            public void onAdClick() {
//                MobclickAgent.onEvent(context,"ban_click");
//            }
//
//            @Override
//            public void onAdError(String s) {
//
//            }
//        });
//        // 初始化广告
//        RelativeLayout.LayoutParams layoutParams=
//                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
////        layoutParams.setLayoutDirection(RelativeLayout.ALIGN_TOP);
//        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT);
//        RelativeLayout rl = new RelativeLayout(context);
//        rl.addView(adView,layoutParams);
//        context.addContentView(rl, lp);
////        view.addView(adView);
//
//    }
    static final String SPLASH_ID = "914";//"1992";   // 广告id

    /**
     * 开屏广告
     * @param context
     */
    public  void showSplash(final Context context){


        // 第二个参数传入目标activity，或者传入null，改为setIntent传入跳转的intent
//        HjSplashAd hjSplashAd = new HjSplashAd(SplashActivity.this, null, SPLASH_ID);
        HjAdManager.showSplashAd(this, null, SPLASH_ID, new HjAdListener() {
            @Override
            public void onDisplayAd() {
                MobclickAgent.onEvent(context,"splash_show");

            }

            @Override
            public void onCloseAd(int closeType) {
                isAdSkip = false;
                if (!isAdClick){
                    handler.sendEmptyMessage(1);
                }
                Log.d("wanka", "splash---splash关闭");
            }

            @Override
            public void onClickAd(int click_type) {
                isAdClick = true;
                isAdSkip = false;
                MobclickAgent.onEvent(context,"splash_click");
                Log.d("wanka", "splash---splash被点击");
            }

            @Override
            public void onAdError(String msg, int code) {
                isAdSkip = false;
                handler.sendEmptyMessage(0);
                Log.e(ADPID,msg);
                Log.i("wanka", "splash---onAdLoadFailed:code"+code+"---->"+msg);
            }

        });// 有回调
////        Intent intent = new Intent(context, DreActivity.class);
////        hjSplashAd.setIntent(intent);
//        hjSplashAd.setIsJumpTargetWhenFail(true);
//        hjSplashAd.setAutoCloseTime(6 * 1000); // 自定义自动跳转时间
//        hjSplashAd.showCountDown(true); // 是否启动倒计时
//        hjSplashAd.setShowAdTime(4 * 1000); // 自定义展示时间
//        HjSplashAd.HjSplashAdView splashAdView = hjSplashAd.getAdView();
//        final FrameLayout splashLayout = new FrameLayout(this);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
//        splashLayout.addView(splashAdView, params);
//      ---------将视图嵌入布局----------
/*
 * 直接请求广告，无回调
 */
//        HjAdManager.showSplashAd(this, hjSplashAd);
//        hjSplashAd.setHjAdListener(new HjAdListener() {



        // 如果开屏广告 点跳过 则 执行这个方法
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAdSkip){
                    handler.sendEmptyMessage(2);
                }
            }
        },8000);


        }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

        if (isAdClick){
            handler.sendEmptyMessageDelayed(3,1000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private  void gotoNextActivity(String msg) {
        showLog(msg);
        if (!isIntented){
            isIntented = true;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }



    public static void showLog(String msg){
        if (ISDEBUG){
            Log.e(TAG,msg);
        }
    }



}
