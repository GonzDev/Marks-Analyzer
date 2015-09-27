package com.gonz.upv.marksanalyzer.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.gonz.upv.marksanalyzer.R;

import java.util.Locale;

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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(0);
                startActivity(new Intent("android.intent.action.MAINACTIVITY"));
            }
        });

        setLanguaje();
    }

    private void setLanguaje() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.contains(MainActivity.KEY_LANGUAJE)) {
            String localeCode = preferences.getString(MainActivity.KEY_LANGUAJE, "es");
            Locale locale = new Locale(localeCode);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        }
    }

}
