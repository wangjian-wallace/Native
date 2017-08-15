package com.wallace.tools.ui.card;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wallace.tools.R;

public class CardActivity extends AppCompatActivity {

    private CardFragment fragment;
    private String TAG = "CardFragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        if (savedInstanceState != null) {
            fragment = (CardFragment) getSupportFragmentManager().getFragment(savedInstanceState, "CardFragment");
        } else {
            fragment = CardFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        new CardPresenter(fragment);

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "CardFragment", fragment);
    }
}
