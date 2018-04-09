package com.saintsung.saintsungpmc.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;
import com.saintsung.saintsungpmc.R;
import com.saintsung.saintsungpmc.configure.Constant;
import com.saintsung.saintsungpmc.text.RetrofitService;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by XLzY on 2017/7/28.
 */

public class MainHomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final PullRefreshLayout layout = (PullRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 刷新3秒完成
                        layout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

//        view.findViewById(R.id.sendData).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                createObservable();
//                RetrofitService retrofitService = new RetrofitService();
//                retrofitService.sendRequest(Constant.LoginService, Constant.LoginService, action1);
//            }
//        });
        return view;
    }

    private void createObservable() {
        Observable mObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

            }
        }).subscribeOn(Schedulers.io());
        Subscriber subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable error) {

            }

            @Override
            public void onNext(String string) {

                Log.e("TAG", "服务器返回：" + string);
            }
        };
        mObservable.subscribe(subscriber);
    }
}
