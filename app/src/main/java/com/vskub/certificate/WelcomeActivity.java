package com.vskub.certificate;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vskub.certificate.common.Appconstants;
import com.vskub.certificate.common.Connection;
import com.vatsal.imagezoomer.ZoomAnimation;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by HP PC on 23-03-2018.
 */

public class WelcomeActivity extends Activity{

    Animation zoomin;
    Animation zoomout;
    ImageView cerimage;
    ImageButton searchimage;
    ImageButton searchimage1;
    FrameLayout layout1;
    FrameLayout layout2;
    FloatingActionButton nextbut;
    FloatingActionButton donebut;
    AnimatorSet mSetRightOut;
    TextView open;
    AnimatorSet mSetLeftIn;
    Animation animation1,animation2,animation3,animation4;
    ImageView phoneimage;
    ImageView phonecerimage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        cerimage=(ImageView)findViewById(R.id.certificate);
        searchimage=(ImageButton)findViewById(R.id.search);
        searchimage1=(ImageButton)findViewById(R.id.search1);
        phoneimage=(ImageView)findViewById(R.id.phone);
        phonecerimage=(ImageView)findViewById(R.id.mobcertificate);
        layout1=(FrameLayout) findViewById(R.id.layout1);
        open=(TextView) findViewById(R.id.open);
        layout2=(FrameLayout) findViewById(R.id.layout2);
        nextbut=(FloatingActionButton)findViewById(R.id.nextbut);
        donebut=(FloatingActionButton)findViewById(R.id.donebut);
        zoomin = AnimationUtils.loadAnimation(this, R.anim.zoomin);
        zoomout = AnimationUtils.loadAnimation(this, R.anim.zoomout);
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);
        PerformVersionTask task=new PerformVersionTask();
        task.execute();
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        layout1.setCameraDistance(scale);
        layout2.setCameraDistance(scale);
        animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_in_right);
        animation2 =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_in_left);
        animation3 =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_down);

        animation4 =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_in_up);

        cerimage.startAnimation(animation2);
        searchimage.startAnimation(animation1);
        nextbut.startAnimation(animation3);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                searchimage.setVisibility(View.GONE);
                searchimage1.setVisibility(View.VISIBLE);
                open.setVisibility(View.VISIBLE);
            }
        }, 1900);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                searchimage.setVisibility(View.GONE);
               // searchimage1.setVisibility(View.VISIBLE);
                ZoomAnimation zoomAnimation = new ZoomAnimation(WelcomeActivity.this);
                zoomAnimation.zoomReverse(searchimage1, 600);

            }
        }, 2000);

        nextbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSetRightOut.setTarget(layout1);
                mSetLeftIn.setTarget(layout2);
                mSetRightOut.start();
                mSetLeftIn.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        phonecerimage.setVisibility(View.VISIBLE);
                        phoneimage.setVisibility(View.VISIBLE);
                        donebut.setVisibility(View.VISIBLE);
                  phoneimage.startAnimation(animation2);
                  phonecerimage.startAnimation(animation4);
                  donebut.startAnimation(animation3);

                    }
                }, 2000);
            }
        });



        donebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WelcomeActivity.this, com.vskub.certificate.MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_up);
            }
        });

    }

    class PerformVersionTask extends AsyncTask<Void, String ,String> {

        String resp="false";
        // private String name;
        private AlertDialog dia;
        int Number;
        private int versionNumber;
        String versionname="";

        protected void onPreExecute() {
            Log.i("PerformVersionTask", "started");
            //logger.info("PerformVersionTask started");
        }

        @Override
        protected String doInBackground(final Void... arg0) {
            Log.i("PerformVersionTask", "center");
            try{
                Connection con = new Connection();
                resp = con.connStringResponse(Appconstants.domain+"version.php");
                JSONArray array=new JSONArray(resp);
                JSONObject obj = array.getJSONObject(0);
                JSONArray arr=obj.getJSONArray("Response");
                JSONObject jobj=arr.getJSONObject(0);
                Number =Integer.parseInt(jobj.getString("version"));
                //name = obj.getString("data");
                PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                versionNumber = com.vskub.certificate.BuildConfig.VERSION_CODE;//pinfo.versionCode;
                versionname=pinfo.versionName;
                Log.d("versionNumber : ",String.valueOf(versionNumber));
                Log.d("version : ",resp);
                if (versionNumber == Number) {
                    resp = "false";
                } else {
                    resp = "true";
                }
            }catch(Exception e1){
                e1.printStackTrace();
                //logger.info("PerformVersionTask resp"+e1.getMessage());
                resp="false";
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String resp) {
            //logger.info("PerformVersionTask final "+resp);
            if(resp.equalsIgnoreCase("true")){
                try {
                    final Dialog update = new Dialog(WelcomeActivity.this);
                    update.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    update.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    View v=getLayoutInflater().inflate(R.layout.app_update_dialog,null);
                    FloatingActionButton updatebut=(FloatingActionButton) v.findViewById(R.id.donebut);
                    FloatingActionButton laterbut=(FloatingActionButton) v.findViewById(R.id.cancelbut);
                    TextView titlename=(TextView) v.findViewById(R.id.titletext);
                    TextView content=(TextView) v.findViewById(R.id.content);
                    ImageView titleimg=(ImageView) v.findViewById(R.id.titleimg);
                    titleimg.setImageResource(R.mipmap.update);

                    update.setContentView(v);
                    update.setCancelable(false);
                    update.show();
                    titlename.setText("Update Available");
                    content.setText("You are using an older version of "+getString(R.string.short_name)+" Result Verify( version "+versionname+" ). Update now to get the latest features.");
                    updatebut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            update.dismiss();
                            Intent intt = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName()+"&hl=en"));
                            startActivity(intt);
                        }
                    });
                    laterbut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            update.dismiss();
                        }
                    });
                }catch(Exception e){
                    //logger.info("PerformVersionTask error" + e.getMessage());
                }
            }

        }
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
