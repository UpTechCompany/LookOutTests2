package com.example.uptechapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.uptechapp.R;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "ActivitySplash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long id = 0L;
            try {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                id = sharedPref.getLong(getString(R.string.id_logging), 0L);

            } catch (Exception e) {
                Log.i(TAG, e.getMessage());
            }

            Intent intent;
//            if (id != 0l){
//                intent = new Intent(SplashActivity.this, MainActivityFragments.class);
//            }
//            else {
//                intent = new Intent(SplashActivity.this, LoginActivity.class);
//            }

            intent = new Intent(SplashActivity.this, MainActivityFragments.class);
            startActivity(intent);
            SplashActivity.this.finish();

        }).start();

    }


}