package com.saintsung.saintsungpmc.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.saintsung.common.app.Activity;
import com.saintsung.saintsungpmc.R;

import butterknife.BindView;

public class ElectricDetailsActivity extends Activity {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_electric_details;
    }

    @Override
    protected void initData() {
        super.initData();
        txtTitle.setText(R.string.electricdetails_title);
        imgBack.setVisibility(View.VISIBLE);
    }
}
