package com.wallace.tools.ui.example;

import com.wallace.tools.mvp.BasePresenter;
import com.wallace.tools.mvp.BaseView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Package com.wallace.tools.ui.example
 * Created by Wallace.
 * on 2017/4/14.
 */

public interface ExampleContract {
    interface ExampleView extends BaseView<Presenter>{
        void toNext(ArrayList<HashMap<String,String>> text);
        void toComplete();
        void showError();
    }
    interface Presenter extends BasePresenter{
        void getData();
    }
}
