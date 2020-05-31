package com.pkinc.begusaraisabji;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Products extends AppCompatActivity {

    Button btnVegetable;
    Button btnFruittable;
    Button btnGroery;
    Button btnHomeservice;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);

        //----------initializing adMob sdk starts------------------------------------------

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8106090973347908/8600146496");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        //----------initializing adMob sdk ends------------------------------------------

        btnVegetable = (Button)findViewById(R.id.btnVegetable);
        btnVegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                Intent vegTable = new Intent(Products.this, VegPriceList.class);
                startActivity(vegTable);
            }
        });

        btnFruittable = (Button)findViewById(R.id.btnFruit);
        btnFruittable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                Intent fruitTable = new Intent(Products.this, FruitPriceList.class);
                startActivity(fruitTable);
            }
        });


        btnGroery = (Button)findViewById(R.id.btnGrocery);
        btnGroery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                Toast.makeText(Products.this,"Currently Not Available. Coming Soon.....",Toast.LENGTH_LONG).show();
            }
        });

        btnHomeservice = (Button)findViewById(R.id.btnHomeServices);
        btnHomeservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                Toast.makeText(Products.this,"Currently Not Available. Coming Soon.....",Toast.LENGTH_LONG).show();
            }
        });


    }
}
