package com.example.buybooksonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    View v;
    Animation anim;
    ImageView logo_image;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        logo_image=findViewById(R.id.splash_logo);
        fAuth=FirebaseAuth.getInstance();
        anim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        logo_image.setAnimation(anim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(fAuth.getCurrentUser()!=null){

                    startActivity(new Intent(getApplicationContext(),MainActivity2.class));
                    finish();
                }
                else{
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();}
            }
        },5000);

    }
}