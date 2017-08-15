package com.wallace.tools.ui.card;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wallace.tools.R;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class CardFragment extends Fragment implements CardContract.Card2View
        ,ViewPager.OnPageChangeListener{

    private CardContract.Presenter presenter;
    private TextView tvNum,tvDate,tvCVV,tvHolder;
    private ProgressBar progressBar;

    public static CardFragment newInstance() {
        return new CardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void toNext(ArrayList<HashMap<String, String>> text) {

    }

    @Override
    public void toComplete() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void initViews(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarCard);
        tvNum = (TextView) view.findViewById(R.id.tvCard_number);
        tvDate = (TextView) view.findViewById(R.id.tvCard_date);
        tvCVV = (TextView) view.findViewById(R.id.tvCard_cvv);
        tvHolder = (TextView) view.findViewById(R.id.tvCard_holder);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.vpCard);
        progressBar = (ProgressBar) view.findViewById(R.id.pbCard);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        presenter.getData();

        ArrayList<NCardFragment> fragments = new ArrayList<>();
        fragments.add(NCardFragment.newInstance(0));
        fragments.add(NCardFragment.newInstance(1));
        fragments.add(NCardFragment.newInstance(2));
        fragments.add(NCardFragment.newInstance(3));
        CardAdapter mAdapter = new CardAdapter(getFragmentManager(), fragments);
        mAdapter.setListener(0, new CardInputListener() {
            @Override
            public void inputText(CharSequence text) {
                tvNum.setText(text);
            }
        });
        mAdapter.setListener(1, new CardInputListener() {
            @Override
            public void inputText(CharSequence text) {
                tvDate.setText(text);
            }
        });
        mAdapter.setListener(2, new CardInputListener() {
            @Override
            public void inputText(CharSequence text) {
                tvCVV.setText(text);
            }
        });
        mAdapter.setListener(3, new CardInputListener() {
            @Override
            public void inputText(CharSequence text) {
                tvHolder.setText(text);
            }
        });
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void setPresenter(CardContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(25);

                break;
            case 1:
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(50);

                break;
            case 2:
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(75);

                break;
            case 3:
                progressBar.setVisibility(View.GONE);

                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
