package com.wallace.tools.ui.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wallace.tools.R;

public class ExampleActivity extends AppCompatActivity {
    private ExampleFragment fragment;
    private String TAG = "ExampleActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        if (savedInstanceState != null) {
            fragment = (ExampleFragment) getSupportFragmentManager().getFragment(savedInstanceState, "ExampleFragment");
        } else {
            fragment = ExampleFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        new ExamplePresenter(fragment);

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "ExampleFragment", fragment);
    }

}
