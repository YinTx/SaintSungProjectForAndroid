package com.saintsung.saintsungpmc.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saintsung.saintsungpmc.R;

/**
 * Created by XLzY on 2017/7/28.
 */

public class MainPersonalFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_personal,container,false);
        return view;
    }
}
