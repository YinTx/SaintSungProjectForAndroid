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
//    @BindView(R.id.galleyView)
//    GalleyView mGalley;
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void initData() {
        super.initData();
        txtTitle.setText(R.string.electric_title);
//        mGalley.setup(getLoaderManager(), new GalleyView.SelectedChangeListener() {
//            @Override
//            public void onSelectedCount(int count) {
//
//            }
//        });
    }
}
