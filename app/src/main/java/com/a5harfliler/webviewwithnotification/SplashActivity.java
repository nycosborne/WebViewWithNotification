package com.a5harfliler.webviewwithnotification;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

/**
 * Created by danielosborne on 3/30/17.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        finish();


        final Handler handel = new Handler();
        handel.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO kill SplashPage or mainActivity shoudle begine to loead webview then call back.

                Intent loadSplash = new Intent(SplashActivity.this, MainActivity.class);

                startActivity(loadSplash);

                finish();
            }
        }, 1000);
    }
}
