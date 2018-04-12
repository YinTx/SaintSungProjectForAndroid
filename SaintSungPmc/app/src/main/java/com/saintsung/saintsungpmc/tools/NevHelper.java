package com.saintsung.saintsungpmc.tools;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;


/**
 * 解决Fragment调度与重用问题，达到最优的Fragment的切换
 * Created by EvanShu on 2018/4/11.
 */

public class NevHelper<T> {
    //所有的Tab集合
    private final SparseArray<Tab<T>> tabs = new SparseArray<>();
    //用于初始化的必须参数
    private final FragmentManager fragmentManager;
    private final int containerId;
    private final Context context;
    private OnTabChangedListener<T> onTabChangedListener;
    //当前选中的Tab
    private Tab<T> currentTab;

    public NevHelper(Context context, FragmentManager fragmentManager, int containerId, OnTabChangedListener<T> onTabChangedListener) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.context = context;
        this.onTabChangedListener = onTabChangedListener;
    }

    /**
     * 添加Tab
     *
     * @param menuId Tab对应的菜单Id
     * @param tab    Tab
     */
    public NevHelper<T> add(int menuId, Tab<T> tab) {
        tabs.put(menuId, tab);
        return this;
    }

    /**
     * 获取当前显示的Tab
     *
     * @return 当前的Tab
     */
    public Tab<T> getCurrentTab() {
        return currentTab;
    }

    /**
     * 执行点击菜单的操作
     *
     * @param menuId 菜单的Id
     * @return 是否能够处理这个点击
     */
    public boolean performClickMenu(int menuId) {
        //集合中寻找点击菜单对应的Tab
        //如果有则进行处理
        Tab<T> tab = tabs.get(menuId);

        if (tab != null) {
            doSelect(tab);
            return true;
        }
        return false;
    }

    /**
     * 进行真实的Tab选择操作
     *
     * @param tab Tab
     */
    private void doSelect(Tab<T> tab) {
        Tab<T> oldTab = null;
        if (currentTab != null) {
            oldTab = currentTab;
            if (tab == oldTab)
                //多次点击，不做处理 OR 进行刷新处理刷新方法notifyReselect()
                return;

        }
        //赋值并调用切换方法
        currentTab = tab;
        doTabChanged(currentTab, oldTab);
    }

    /**
     * 进行Fragment的真实的调度操作
     * @param newTab
     * @param oldTab
     */
    private void doTabChanged(Tab<T> newTab, Tab<T> oldTab) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (oldTab != null) {
            if (oldTab.fragment != null) {
                //从界面移除，但是还在Fragment的缓存空间中
                ft.detach(oldTab.fragment);
            }
        }
        if (newTab != null) {
            if (newTab.fragment == null) {
                //首次新建
                Fragment fragment = Fragment.instantiate(context, newTab.clx.getName(), null);
                //缓存
                newTab.fragment = fragment;
                //提交到FragmentManger
                ft.add(containerId, fragment, newTab.clx.getName());
            } else
                //从FragmentManger的缓存空间中重新加载到界面
                ft.attach(newTab.fragment);
        }
        ft.commit();
        notifyTabSelect(newTab, oldTab);
    }

    /**
     * 回掉监听器
     *
     * @param newTab 新的Tab
     * @param oldTab 旧的Tab
     */
    private void notifyTabSelect(Tab<T> newTab, Tab<T> oldTab) {
        if (onTabChangedListener != null) {
            onTabChangedListener.onTabChanged(newTab, oldTab);
        }
    }

    private void notifyReselect(Tab<T> tab) {
        //TODO 二次点击或多次点击进行的操作
    }

    /**
     * 我们的所用的Tab基础属性
     *
     * @param <T> 泛型的额外参数
     */
    public static class Tab<T> {
        public Tab(Class<?> clx, T extra) {
            this.clx = clx;
            this.extra = extra;
        }

        //Fragment对应的Class信息
        public Class<?> clx;
        //额外的字段，用户自己设定需要什么东西
        public T extra;
        //内部缓存的对应的Fragment
        // package权限，外部无法使用
        Fragment fragment;
    }

    /**
     * 定义事件处理完成后的回掉接口
     *
     * @param <T>
     */
    public interface OnTabChangedListener<T> {
        void onTabChanged(Tab<T> newTab, Tab<T> oldTab);
    }
}
