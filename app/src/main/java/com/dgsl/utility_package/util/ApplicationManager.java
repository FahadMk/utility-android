package com.dgsl.utility_package.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import com.dgsl.utility_package.ui.language.LocaleHelper;

import utility_package.R;

public class ApplicationManager extends Application {
    private Context context;
    boolean screenShotStatus;
    SharedPreferences sh;
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        setupActivityListener();

        sh = getSharedPreferences("ScreenShotSharedPref", MODE_PRIVATE);

    }


    private void setupActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                screenShotStatus = sh.getBoolean("status", false);
                if(screenShotStatus){
                    activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
                }else{
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
                }

            }
            @Override
            public void onActivityStarted(Activity activity) {
                screenShotStatus = sh.getBoolean("status", false);
                if(screenShotStatus){
                    activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
                }else{
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
                }


            }
            @Override
            public void onActivityResumed(Activity activity) {
                screenShotStatus = sh.getBoolean("status", false);
                if(screenShotStatus){
                    activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
                }else{
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
                }

            }
            @Override
            public void onActivityPaused(Activity activity) {
                screenShotStatus = sh.getBoolean("status", false);
                if(screenShotStatus){
                    activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
                }else{
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
                }

            }
            @Override
            public void onActivityStopped(Activity activity) {
                screenShotStatus = sh.getBoolean("status", false);
                if(screenShotStatus){
                    activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
                }else{
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
                }
            }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
            @Override
            public void onActivityDestroyed(Activity activity) {
                screenShotStatus = sh.getBoolean("status", false);
                if(screenShotStatus){
                    activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
                }else{
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
                }
            }
        });
    }
}
