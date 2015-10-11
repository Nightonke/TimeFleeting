package com.timefleeting.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        setContentView(R.layout.splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        SharedPreferences preferences;
        preferences = getSharedPreferences("Values", MODE_PRIVATE);
        GlobalSettings.ITEM_BACKGROUND_COLOR = preferences.getInt("ITEM_BACKGROUND_COLOR", GlobalSettings.DEFAULT_ITEM_BACKGROUND_COLOR);
        final WaveView waveView = (WaveView)findViewById(R.id.wave_view_splash);
        waveView.setBackgroundColor(GlobalSettings.ITEM_BACKGROUND_COLOR);
        
        LinearLayout splashLinearLayout = (LinearLayout)findViewById(R.id.splash_layout);
        splashLinearLayout.setBackgroundColor(GlobalSettings.ITEM_BACKGROUND_COLOR);
        
        AnimationSet animationSet = new AnimationSet(true);
		TranslateAnimation translateAnimation = 
				new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, 0f,
						Animation.RELATIVE_TO_SELF, 0f,
						Animation.RELATIVE_TO_SELF, 1f,
						Animation.RELATIVE_TO_SELF, 0f);
		translateAnimation.setDuration(5000);
		animationSet.addAnimation(translateAnimation);
		animationSet.setFillAfter(true);
		waveView.startAnimation(animationSet);
        
        new Handler().postDelayed(new Runnable() {   
            public void run() {   
                Splash.this.finish();
            }   
        }, 5000);

    }

    

}
