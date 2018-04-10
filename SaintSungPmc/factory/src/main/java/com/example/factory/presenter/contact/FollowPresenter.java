//package com.example.factory.presenter.contact;
//
//import com.example.factory.data.helper.UserHelper;
//import com.example.factory.model.card.UserCard;
//
//import net.qiujuer.genius.kit.handler.Run;
//import net.qiujuer.genius.kit.handler.runable.Action;
//
//import factory.data.DataSource;
//import factory.presenter.BasePresenter;
//
//
///**
// * 关注的逻辑实现
// *
// * @author qiujuer Email:qiujuer@live.cn
// * @version 1.0.0
// */
//public class FollowPresenter extends BasePresenter<FollowContract.View>
//        implements FollowContract.Presenter, DataSource.Callback<UserCard> {
//
//    public FollowPresenter(FollowContract.View view) {
//        super(view);
//    }
//
//    @Override
//    public void follow(String id) {
//        start();
//        UserHelper.follow(id, this);
//    }
//
//    @Override
//    public void onDataLoaded(final UserCard userCard) {
//        // 成功
//        final FollowContract.View view = getView();
//        if (view != null) {
//            Run.onUiAsync(new Action() {
//                @Override
//                public void call() {
//                    view.onFollowSucceed(userCard);
//                }
//            });
//        }
//    }
//
//    @Override
//    public void onDataNotAvailable(final int strRes) {
//        final FollowContract.View view = getView();
//        if (view != null) {
//            Run.onUiAsync(new Action() {
//                @Override
//                public void call() {
//                    view.showError(strRes);
//                }
//            });
//        }
//    }
//}
