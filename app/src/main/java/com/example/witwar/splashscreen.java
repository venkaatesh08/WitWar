package com.example.witwar;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class splashscreen extends AppCompatActivity {

    ImageView logo;
    private static int SPLASH_TIME_OUT=3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        logo=(ImageView)findViewById(R.id.logobtn);

        Animation myanima= AnimationUtils.loadAnimation(this,R.anim.myanim);
        logo.startAnimation(myanima);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loin=new Intent(splashscreen.this,MainActivity.class);
                splashscreen.this.startActivity(loin);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
