package com.saintsung.saintsungpmc.loading;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.saintsung.saintsungpmc.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by XLzY on 2017/11/16.
 */
public class WelcomeGuideActivity extends Activity implements View.OnClickListener {

    Button starBtn;
    private ViewPager vp;
    private GuideViewPagerAdapter adapter;
    private List<View> views;
    // 引导页图片资源
    private static final int[] pics = {R.layout.welcome_img1,
            R.layout.welcome_img2, R.layout.welcome_img3};

    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wlecom);

        views = new ArrayList<View>();
        for (int i = 0; i < pics.length; i++) {
            View view = LayoutInflater.from(this).inflate(pics[i], null);
            if (i == pics.length - 1) {
                starBtn = (Button) view.findViewById(R.id.btn_enter);
                starBtn.setTag("enter");
                starBtn.setOnClickListener(this);
            }
            views.add(view);
        }
        vp = (ViewPager) findViewById(R.id.vp_guide);
        adapter = new GuideViewPagerAdapter(views);
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new PageChangeListener());

        initDots();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 如果切换到后台，就设置下次不进入功能引导页
        SharedPreferencesUtil.putBoolean(WelcomeGuideActivity.this, SharedPreferencesUtil.FIRST_OPEN, false);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        dots = new ImageView[pics.length];

        // 循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            // 得到一个LinearLayout下面的每一个子元素
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(false);// 都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(true); // 设置为白色，即选中状态

    }

    /**
     * 设置当前view
     *
     * @param position
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        vp.setCurrentItem(position);
    }

    /**
     * 设置当前指示点
     *
     * @param position
     */
    private void setCurDot(int position) {
        if (position < 0 || position > pics.length || currentIndex == position) {
            return;
        }
        dots[position].setEnabled(true);
        dots[currentIndex].setEnabled(false);
        currentIndex = position;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("enter")) {
            enterMainActivity();
            return;
        }

        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }


    private void enterMainActivity() {
        Intent intent = new Intent(WelcomeGuideActivity.this,
                WelcomeActivity.class);
        startActivity(intent);
        SharedPreferencesUtil.putBoolean(WelcomeGuideActivity.this, SharedPreferencesUtil.FIRST_OPEN, false);
        finish();
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int position) {

        }

        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            // 设置底部小点选中状态
            setCurDot(position);
        }

    }
}
