package com.pkinc.begusaraisabji;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    ImageView img_splash;
    ProgressBar progressBar;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        getSupportActionBar().hide();


        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        img_splash = (ImageView)findViewById(R.id.img_splash);
        Animation my_animation  = AnimationUtils.loadAnimation(this,R.anim.my_animation);
        title = (TextView)findViewById(R.id.begu);
        title.startAnimation(my_animation);

        Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    //progressBar.setVisibility(View.GONE);
                    Intent i =new Intent(Splash.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        myThread.start();
    }

    }
