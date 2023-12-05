package com.vskub.certificate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

//import com.crashlytics.android.Crashlytics;
import com.vskub.certificate.common.Circle;
import com.vskub.certificate.common.CircleAngleAnimation;

//import io.fabric.sdk.android.Fabric;

/**
 * Created by HP PC on 26-03-2018.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.splash_lay);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Circle circle = (Circle) findViewById(R.id.circle);
        circle.setPaintColor("#10DDE7");
        circle.setAngle(20);

        CircleAngleAnimation animation = new CircleAngleAnimation(circle, 150);
        animation.setDuration(2000);
        circle.startAnimation(animation);

        Circle circle1 = (Circle) findViewById(R.id.round1);
        circle1.setPaintColor("#ff74ad");
        circle1.setAngle(0);

        CircleAngleAnimation animation1 = new CircleAngleAnimation(circle1, 150);
        animation1.setDuration(2000);
        circle1.startAnimation(animation1);
        ImageView logo=(ImageView)findViewById(R.id.logoimage);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(myFadeInAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               startActivity(new Intent(SplashActivity.this, com.vskub.certificate.WelcomeActivity.class));
                overridePendingTransition(R.anim.slide_come_right, R.anim.right_to_left);
            }
        }, 4000);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_come_up, R.anim.slide_out_down);
    }
}
