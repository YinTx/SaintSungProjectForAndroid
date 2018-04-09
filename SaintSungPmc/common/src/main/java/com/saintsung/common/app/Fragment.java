package com.saintsung.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saintsung.common.widget.convention.PlaceHolderView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by EvanShu on 2018/1/29.
 */

public abstract class Fragment extends android.support.v4.app.Fragment {
    protected View mRoot;
    protected Unbinder mRootUnbinder;
    protected PlaceHolderView mPlaceHolderView;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            int layid = getContentLayoutId();
            //初始化当前的根布局，但是不在创建时就添加到container里面
            View root = inflater.inflate(layid, container, false);
            initWidget(root);
            mRoot = root;
        }else {
            if(mRoot.getParent()!=null){
                //把当前Root从其父控件中移除
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //当View创建完成后初始化数据
        initData();
    }

    protected void initData() {
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    protected abstract int getContentLayoutId();

    protected void initWidget(View root) {
        mRootUnbinder= ButterKnife.bind(this,root);
    }
    /**
     * 初始化相关参数
     * @param bundle 参数Bundle
     * @return 如果参数正确返回true 如果错误返回False
     */
    protected void initArgs(Bundle bundle) {
    }

    /**
     *
     * @return
     */
    public boolean onBackPressed(){
        return false;
    }

    /**
     * 设置占位布局
     * @param placeHolderView
     */
    public void setPlaceHolderView(PlaceHolderView placeHolderView){
        this.mPlaceHolderView=placeHolderView;
    }
}
