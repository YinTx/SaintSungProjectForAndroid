package com.saintsung.saintsungpmc.activity;

import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.saintsung.common.app.Activity;
import com.saintsung.saintsungpmc.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by EvanShu on 2018/4/20.
 */

public class UnauthorizedActivity extends Activity {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_unauthorized;
    }

    @Override
    protected void initData() {
        super.initData();
        imgBack.setVisibility(View.VISIBLE);
        txtTitle.setText(R.string.unauthorized_title);
    }
    @OnClick(R.id.img_back)
    void black(){
        finish();
    }
}
