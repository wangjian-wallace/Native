package com.wallace.tools.ui.example;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wallace.tools.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExampleFragment extends Fragment implements ExampleContract.ExampleView{

    private ExampleContract.Presenter presenter;

    private RecyclerView recyclerView;

    private ExampleAdapter mAdapter;
    private ArrayList<HashMap<String,String>> list;
//    private TextView textView;
//    private RxPermissions rxPermissions;

    private static final String TAG = "ExampleFragment";

    public static ExampleFragment newInstance(){
        return new ExampleFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_example, container, false);

//        rxPermissions = new RxPermissions(getActivity());
        list = new ArrayList<>();
        initViews(view);
        return view;
    }

    @Override
    public void toNext(ArrayList<HashMap<String,String>> text) {
        list.addAll(text);
        mAdapter.setList(list);
        mAdapter.notifyDataSetChanged();
        Log.d(TAG, "toNext: "+text);
//        textView.setText(information.toString());
    }

    @Override
    public void showError() {

    }

    @Override
    public void toComplete() {
//        if (list != null && list.size() != 0){
//
//        }
    }
    @Override
    public void onResume() {
        super.onResume();
        presenter.toStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.toStop();
    }

    @Override
    public void initViews(View view) {
//        textView = (TextView) view.findViewById(R.id.tvExample);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvExample);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarExample);
        final ExampleActivity activity = (ExampleActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        list = new ArrayList<>();
        mAdapter = new ExampleAdapter(getActivity(),list);
        recyclerView.setAdapter(mAdapter);
        presenter.getData();
//        RxView.clicks(textView)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .compose(rxPermissions.ensure(Manifest.permission.CAMERA))
//                .subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(@NonNull Boolean aBoolean) throws Exception {
//                        Log.d(TAG, "accept: " + aBoolean);
//                    }
//                });
    }

    @Override
    public void setPresenter(ExampleContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
