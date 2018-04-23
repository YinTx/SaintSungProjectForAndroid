package com.saintsung.saintsungpmc.fragment;

import android.widget.TextView;

import com.saintsung.common.app.Fragment;
import com.saintsung.saintsungpmc.R;

import butterknife.BindView;

/**
 * Created by XLzY on 2017/7/28.
 */

public class MainPersonalFragment extends Fragment {
    @BindView(R.id.txt_title)
    TextView txtTitle;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void initData() {
        super.initData();
        txtTitle.setText(R.string.electric_title);
    }
}
