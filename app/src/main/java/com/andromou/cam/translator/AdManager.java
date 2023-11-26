package com.andromou.cam.translator;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.interstitial.InterstitialAd;


public class AdManager{

    // Your Admob Interstitial ID here !!
    private static final String ADMOB_INTERSTITIAL_ID = "ca-app-pub-5963817566033623/7085817091";
    private Context mContext;
    private InterstitialAd interstitialAd;


    public AdManager(Context context){
        mContext = context;
    }



    public void LoadAdsBannerWithInterstitial(final AdView adView){
        if(adView != null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
        // AdView will be hidden from the activity until it loads.
   //     adView.setVisibility(View.INVISIBLE);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.d("==> Banner Ad:", " Closed by user!");
            }


            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.d("==> Banner Ad:", " Clicked by user!");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("==> Banner Ad:", " Loaded successfully!");
                adView.setVisibility(View.VISIBLE);
            }

        });
        // Just load INTERSTITIAL Ads

    }



    public void LoadInterstitial(){
        interstitialAd = new InterstitialAd() {
            @NonNull
            @Override
            public String getAdUnitId() {
                return null;
            }

            @Override
            public void show(@NonNull Activity activity) {

            }

            @Override
            public void setFullScreenContentCallback(@Nullable FullScreenContentCallback fullScreenContentCallback) {

            }

            @Nullable
            @Override
            public FullScreenContentCallback getFullScreenContentCallback() {
                return null;
            }

            @Override
            public void setImmersiveMode(boolean b) {

            }

            @NonNull
            @Override
            public ResponseInfo getResponseInfo() {
                return null;
            }

            @Override
            public void setOnPaidEventListener(@Nullable  OnPaidEventListener onPaidEventListener) {

            }

            @Nullable
            @Override
            public OnPaidEventListener getOnPaidEventListener() {
                return null;
            }
        };





 }
}
