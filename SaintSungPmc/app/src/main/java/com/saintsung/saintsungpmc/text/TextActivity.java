package com.saintsung.saintsungpmc.text;


import com.saintsung.common.app.Activity;
import com.saintsung.common.widget.GalleyView;
import com.saintsung.saintsungpmc.R;

import butterknife.BindView;

/**
 * Created by EvanShu on 2018/5/15.
 */

public class TextActivity extends Activity {
//    @BindView(R.id.galleyView)
//    GalleyView mGalley;
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initData() {
        super.initData();
//        mGalley.setup(getLoaderManager(), new GalleyView.SelectedChangeListener() {
//            @Override
//            public void onSelectedCount(int count) {
//
//            }
//        });
    }
}
