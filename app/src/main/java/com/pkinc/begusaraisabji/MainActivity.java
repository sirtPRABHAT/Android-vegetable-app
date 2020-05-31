package com.pkinc.begusaraisabji;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import hotchemi.android.rate.AppRate;


public class MainActivity extends AppCompatActivity {

    Button btnLocation;
    LocationManager locationManager;
    LocationListener locationListener;
    final Looper looper = null;
    final Criteria criteria = new Criteria();
    String userLocation = "";
    Button orderOnCall;
    Button review, aboutus, help;
    Button products;
    ProgressDialog  working_dialog;
    int Timer = 5000;
    InterstitialAd mInterstitialAd;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Log.i("hhhh","prabhat");

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    locationManager.requestSingleUpdate(criteria, locationListener, looper);
                }else{
                    showGPSDisabledAlertToUser();
                }
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //---------------for rating automatically starts------------------------
        AppRate.with(MainActivity.this)
                .setInstallDays(3)
                .setLaunchTimes(3)
                .setRemindInterval(2)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(MainActivity.this);

        //---------------for rating automatically ends------------------------

        //----------initializing adMob sdk starts------------------------------------------

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8106090973347908/8600146496");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        //addunitId ca-app-pub-9844782404701303/4562341902

        //----------initializing adMob sdk ends------------------------------------------

         btnLocation = (Button) findViewById(R.id.btnLoc);

         //--------order on call -----------------------------------------------//
         orderOnCall = (Button)findViewById(R.id.orderOnCall);
         orderOnCall.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(Intent.ACTION_DIAL);
                 intent.setData(Uri.parse("tel:9931636244"));
                 startActivity(intent);
             }
         });

        //--------order on call ends-----------------------------------------------//


        //---------Review starts-----------------------------------------
        review = (Button)findViewById(R.id.review);
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.pkinc.begusaraisabji")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.pkinc.begusaraisabji")));
                }
            }
        });

        //---------Review starts ends-----------------------------------------

        help = (Button)findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                Toast.makeText(MainActivity.this,"Not Available",Toast.LENGTH_SHORT).show();
            }
        });


        aboutus = (Button)findViewById(R.id.aboutus);
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                Intent us = new Intent(MainActivity.this,Us.class);
                startActivity(us);
            }
        });

        //------------products listener start---------------------------------------
        products = (Button)findViewById(R.id.products);
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                Intent products = new Intent(MainActivity.this, Products.class);
                startActivity(products);
            }
        });


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                 userLocation = String.valueOf(location.getLatitude())+", "+String.valueOf(location.getLongitude());

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                //some code to run
            }

            @Override
            public void onProviderEnabled(String s) {
                //some code to run
            }

            @Override
            public void onProviderDisabled(String s) {
                //some code to run
            }
        };

        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

        // Now create a location manager
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //gps enabled no permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        }else{
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestSingleUpdate(criteria, locationListener, looper);
            }else{
                showGPSDisabledAlertToUser();
            }
        }





        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!userLocation.isEmpty()){
                    Timer = 500;
                }
                showWorkingDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        removeWorkingDialog();
                        String locationText = userLocation;
                        Intent intent = new Intent(MainActivity.this, MakeOrder.class);
                        intent.putExtra("message", locationText);
                        startActivity(intent);
                    }
                },Timer);

            }
        });


    }



    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestSingleUpdate(criteria, locationListener, looper);
        }

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

//------------function for showing progress bar------------------------------------------------------
    private void showWorkingDialog() {
          working_dialog = ProgressDialog.show(MainActivity.this, "","Fetching Your location please wait...", true);
    }
//----------------------------function for removing progress bar---------------------------------------
    public void removeWorkingDialog() {
        if (working_dialog != null) {
            working_dialog.dismiss();
            working_dialog = null;
        }
    }



}

