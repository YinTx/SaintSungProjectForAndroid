package com.saintsung.saintsungpmc.loading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.saintsung.saintsungpmc.MainActivity;
import com.saintsung.saintsungpmc.R;
import com.saintsung.saintsungpmc.networkconnections.SocketConnect;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * Created by XLzY on 2017/11/16.
 */

public class WelcomeActivity extends Activity {
    private static final int ANIM_TIME = 1500;
    private static final float SCALE_END = 1.15F;
    //设置启动页，APP的响应页背景的常量
    private static final int Img = R.drawable.welcomimg2;
    @Bind(R.id.iv_entry)
    ImageView mIVEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断APP是否为第一次启动
        boolean isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true);
        // 如果是第一次启动，则先进入功能引导页
        if (isFirstOpen) {
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        startMainActivity();
    }

    /**
     * 该方法为设置启动页背景
     */
    private void startMainActivity() {
//        Random random = new Random(SystemClock.elapsedRealtime());//SystemClock.elapsedRealtime() 从开机到现在的毫秒数（手机睡眠(sleep)的时间也包括在内）
        mIVEntry.setImageResource(Img);

        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {

                    @Override
                    public void call(Long aLong) {
                        startAnim();
                    }
                });
    }

    /**
     * 动画效果页面
     */
    private void startAnim() {

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mIVEntry, "scaleX", 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mIVEntry, "scaleY", 1f, SCALE_END);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIM_TIME).play(animatorX).with(animatorY);
        set.start();

        set.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isUserSharedPreferences()) {
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    WelcomeActivity.this.finish();
                    createObservable();
                } else {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    WelcomeActivity.this.finish();
                }

            }
        });
    }
    private void createObservable() {
        Observable mObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                SocketConnect socketConnect = new SocketConnect();
                subscriber.onNext(socketConnect.sendDate(SharedPreferencesUtil.getSharedPreferences(WelcomeActivity.this, "Port", ""), SharedPreferencesUtil.getSharedPreferences(WelcomeActivity.this, "UserNameAndPassword", "")));
            }
        });
        Subscriber subscriber=new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable error) {

            }

            @Override
            public void onNext(String string) {
                Log.e("TAG","服务器返回："+string);
            }
        };
        mObservable.subscribe(subscriber);
    }

    /**
     * 判断用户名密码是否保存在本地
     *
     * @return
     */
    private Boolean isUserSharedPreferences() {
        String isLogin = SharedPreferencesUtil.getSharedPreferences(this, "UserNameAndPassword", "");
        if (!isLogin.equals("")) {
            return true;
        } else
            return false;
    }

    /**
     * 屏蔽物理返回按钮
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
