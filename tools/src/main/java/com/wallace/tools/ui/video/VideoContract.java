package com.wallace.tools.ui.video;

import com.wallace.tools.mvp.BasePresenter;
import com.wallace.tools.mvp.BaseView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Package com.wallace.tools.ui.video
 * Created by Wallace.
 * on 2017/6/6.
 */

public class VideoContract {
    interface VideoView extends BaseView<Presenter> {
        void toNext(ArrayList<HashMap<String,String>> text);
        void toComplete();
        void showError();
    }
    interface Presenter extends BasePresenter {
        void getData();
    }
}
