package com.example.absenti.actifity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.absenti.R;
import com.google.android.material.snackbar.Snackbar;

public class SplashActivity extends AppCompatActivity {
    private long ms = 0, splashTime = 1000;
    private boolean splashActive = true, paused = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setStatusBarColor(R.color.colorPrimary);

        final ConstraintLayout logo = findViewById(R.id.logoSplash);
        Thread thread = new Thread(){
            public void run(){
                try {
                    while (splashActive && ms<splashTime){
                        if(!paused)
                            ms=ms+100;
                        sleep(100);
                    }
                }catch (Exception e){

                }finally {
                    if (!isOnline()){
                        Snackbar snackbar = Snackbar
                                .make(logo,"No Internet Access", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        recreate();
                                    }
                                });
                        snackbar.show();
                    }else {
                        goLogin();
                    }
                }
            }
        };
        thread.start();
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null &&  connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();

    }

    private void setStatusBarColor(@ColorRes int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            int color = ContextCompat.getColor(this, statusBarColor);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    private void goLogin() {
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
