package com.saintsung.saintsungpmc.fragment;

import android.view.View;

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
    }
}
