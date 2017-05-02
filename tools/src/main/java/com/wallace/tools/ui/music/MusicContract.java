package com.wallace.tools.ui.music;

import android.app.Activity;

import com.wallace.tools.bean.MusicInfo;
import com.wallace.tools.mvp.BasePresenter;
import com.wallace.tools.mvp.BaseView;

import java.util.ArrayList;

/**
 * Package com.wallace.tools.ui.music
 * Created by Wallace.
 * on 2017/4/1.
 */

interface MusicContract {
    interface View extends BaseView<Presenter> {

        void findList(ArrayList<MusicInfo> l);
        void showError();
        Activity getAppCompatActivity();

    }

    interface Presenter extends BasePresenter {

        void getData();

    }
}
