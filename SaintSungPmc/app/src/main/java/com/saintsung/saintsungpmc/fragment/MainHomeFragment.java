package com.saintsung.saintsungpmc.fragment;

import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.saintsung.common.app.Fragment;
import com.saintsung.saintsungpmc.R;

import butterknife.BindView;

/**
 * Created by XLzY on 2017/7/28.
 */

public class MainHomeFragment extends Fragment {
    @BindView(R.id.appbar)
    View mLayAppbar;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_home;
    }
    @Override
    protected void initData(){
        super.initData();
        Glide.with(this).load(R.drawable.bg_src_morning).centerCrop().into(new ViewTarget<View, GlideDrawable>(mLayAppbar) {
            @Override
            public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                this.view.setBackground(glideDrawable.getCurrent());
            }
        });
    }
}
