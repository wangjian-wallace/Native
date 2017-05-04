package com.wallace.tools.ui.camera;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.view.View;

import com.wallace.tools.mvp.BasePresenter;
import com.wallace.tools.mvp.BaseView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Package com.wallace.tools.ui.camera
 * Created by Wallace.
 * on 2017/5/4.
 */

public interface ImageContract {
    interface ImageView extends BaseView<Presenter> {
        void toNext(ArrayList<HashMap<String,String>> text);
        void toComplete();
        void showError();
        void toChangeView(int viewId,View view);
        void setImageBitmap(Bitmap bitmap);
        Fragment getFragmentView();
    }
    interface Presenter extends BasePresenter {
        void getData();
        void onClick(int position);
    }
}
