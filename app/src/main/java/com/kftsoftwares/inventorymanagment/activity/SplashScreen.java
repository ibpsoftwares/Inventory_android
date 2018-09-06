package com.kftsoftwares.inventorymanagment.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.kftsoftwares.inventorymanagment.R;

import io.fabric.sdk.android.Fabric;

import static com.kftsoftwares.inventorymanagment.util.Constants.ID;
import static com.kftsoftwares.inventorymanagment.util.Constants.MyPREFERENCES;
import static com.kftsoftwares.inventorymanagment.util.Constants.TYPE;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);
        final SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);

        String str = sharedPreferences.getString(TYPE,"");

        //START THREAD FOR DELAY THE SPLASH SCREEN
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (sharedPreferences.getString(ID,"").equalsIgnoreCase("") ) {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    finish();
                }
                else if (!sharedPreferences.getString(TYPE,"").equalsIgnoreCase("admin")){
                    startActivity(new Intent(SplashScreen.this, SearchByDate.class));
                    finish();
                }
                else
                {
                    startActivity(new Intent(SplashScreen.this, ViewProducts.class));
                    finish();
                }
            }
        }, 2500);
    }
}
