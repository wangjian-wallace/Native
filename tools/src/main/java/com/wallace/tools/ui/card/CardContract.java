package com.wallace.tools.ui.card;

import com.wallace.tools.mvp.BasePresenter;
import com.wallace.tools.mvp.BaseView;
import com.wallace.tools.ui.example.ExampleContract;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by akmob on 2017/8/15.
 */

public class CardContract {
    interface Card2View extends BaseView<CardContract.Presenter> {
        void toNext(ArrayList<HashMap<String,String>> text);
        void toComplete();
        void showError();
    }
    interface Presenter extends BasePresenter {
        void getData();
    }
}
