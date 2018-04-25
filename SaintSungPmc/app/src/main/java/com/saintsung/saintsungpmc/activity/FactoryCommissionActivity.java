package com.saintsung.saintsungpmc.activity;



import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.saintsung.common.app.Activity;
import com.saintsung.saintsungpmc.R;

import butterknife.BindView;

public class FactoryCommissionActivity extends Activity {
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.img_back)
    ImageView imgBack;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_factory_commission;
    }

    @Override
    protected void initData() {
        super.initData();
        txtTitle.setText(R.string.personal_map_title);
        imgBack.setVisibility(View.VISIBLE);
    }



}
