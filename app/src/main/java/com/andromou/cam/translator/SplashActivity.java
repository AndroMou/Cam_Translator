package com.andromou.cam.translator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;



public class SplashActivity extends AppCompatActivity {
   // com.google.gms.googleservices.GoogleServicesPlugin.config.disableVersionCheck = true


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   //     compart();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.activity_splash);


        Thread timer = new Thread(){
            public void run() {
                try {
                    sleep(2000);
                }catch(InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);
                }
            }

        };
        timer.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
    public void compart(){
        String s = "skhf.qhj.translate.";
        if(!getPackageName().equals("skhf.qhj.trans"+"late.skhfqhj")){
            String error = null;
            error.getBytes();
        }
    }





}