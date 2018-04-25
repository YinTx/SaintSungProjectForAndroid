package com.saintsung.saintsungpmc.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saintsung.common.app.Fragment;
import com.saintsung.saintsungpmc.R;
import com.saintsung.saintsungpmc.activity.AccountSecurityActivity;
import com.saintsung.saintsungpmc.activity.ApplyManageActivity;
import com.saintsung.saintsungpmc.activity.PersonalActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by XLzY on 2017/7/28.
 */

public class MainMapFragment extends Fragment{
    @BindView(R.id.txt_title)
    TextView txtTitle;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_mapl;
    }

    @Override
    protected void initData() {
        super.initData();
        txtTitle.setText(R.string.lockseal_title);
    }
    @OnClick(R.id.fragment_apply_manage)
    void applyManage(){
        startActivity(new Intent(getActivity(), ApplyManageActivity.class));
    }



}
