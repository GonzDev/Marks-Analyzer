package com.gonz.upv.marksanalyzer.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.gonz.upv.marksanalyzer.R;

public class OpeningActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.opening);
        final ImageView imageView = (ImageView) findViewById(R.id.initial);
        imageView.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                imageView.setImageResource(0);
                startActivity(new Intent("android.intent.action.MAINACTIVITY"));

            }
        });

    }
}
