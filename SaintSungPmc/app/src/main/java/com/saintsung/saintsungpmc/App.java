package com.saintsung.saintsungpmc;



import com.example.factory.Factory;
import com.saintsung.common.app.Application;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 调用Factory进行初始化
        Factory.setup();
        // 推送进行初始化
//        PushManager.getInstance().initialize(this);
    }
}
