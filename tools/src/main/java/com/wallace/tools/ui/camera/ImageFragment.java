package com.wallace.tools.ui.camera;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.wallace.tools.R;
import com.wallace.tools.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment implements ImageContract.ImageView{

    private ImageView imageView;
    private FrameLayout flBottom;
    private RecyclerView recyclerView;

    private ImageContract.Presenter presenter;

    public static ImageFragment newInstance(){
        return new ImageFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_image, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void toNext(ArrayList<HashMap<String, String>> text) {
        ImageAdapter mAdapter = new ImageAdapter(getActivity(), text);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                presenter.onClick(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    @Override
    public void toComplete() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void toChangeView(int viewId,View view) {
        if (flBottom.getChildCount() > 0){
            flBottom.removeAllViews();
        }
        flBottom.addView(view == null ? LayoutInflater.from(getActivity()).inflate(viewId,null) : view);

    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public Fragment getFragmentView() {
        return this;
    }

    @Override
    public void initViews(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarI);
        imageView = (ImageView) view.findViewById(R.id.img_xy);
        flBottom = (FrameLayout) view.findViewById(R.id.flImage);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvImage);

        final ImageActivity activity = (ImageActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(
                activity,LinearLayoutManager.HORIZONTAL,false));

        presenter.getData();
    }

    @Override
    public void setPresenter(ImageContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
