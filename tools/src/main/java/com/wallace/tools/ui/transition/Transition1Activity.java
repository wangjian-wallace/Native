package com.wallace.tools.ui.transition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.wallace.tools.R;

public class Transition1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition1);

        ImageView view = (ImageView) findViewById(R.id.ivTransition_1);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        Transition1Activity.this, v, getString(R.string.image_transition_name));

// 使用 Intent 跳转界面，并传递共享对象信息
                Intent intent = new Intent(Transition1Activity.this, Transition2Activity.class);
                startActivity(intent, options.toBundle());
            }
        });

    }
}
