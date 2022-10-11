package com.dgsl.utility_package.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.dgsl.utility_package.ui.language.LocaleHelper;

import utility_package.R;

public class BaseActivity extends AppCompatActivity {

    public LocaleHelper localeHelper = new LocaleHelper();
    String themeColor;
    SharedPreferences colorThemeSh;
    @Override
    protected void onStart() {
        super.onStart();

    }
    public String selectedThemeName(){
        return themeColor = colorThemeSh.getString("themeSelected","default");
    }
    public void setTheme(){
        colorThemeSh = getSharedPreferences("theme",MODE_PRIVATE);
        themeColor = colorThemeSh.getString("themeSelected","default");
        switch(themeColor){
            case "default":
                setTheme(R.style.AppTheme_Purple);
                break;
            case "teal":
                setTheme(R.style.AppTheme_Teal);
                break;
            case "cyan":
                setTheme(R.style.AppTheme_Cyan);
                break;
            case "indigo":
                setTheme(R.style.AppTheme_Indigo);
                break;
            case "orange":
                setTheme(R.style.AppTheme_Orange);
                break;
            case "pink":
                setTheme(R.style.AppTheme_Pink);
                break;
            case "green":
                setTheme(R.style.AppTheme_Green);
                break;
            case "yellow":
                setTheme(R.style.AppTheme_Yellow);
                break;
            case "blue":
                setTheme(R.style.AppTheme_Blue);
                break;

                //gradient theme below
            case "GradientTeal":
                setTheme(R.style.AppTheme_GradientTeal);
                break;
            case "GradientCyan":
                setTheme(R.style.AppTheme_GradientCyan);
                break;
            case "GradientIndigo":
                setTheme(R.style.AppTheme_GradientIndigo);
                break;
            case "GradientOrange":
                setTheme(R.style.AppTheme_GradientOrange);
                break;
            case "GradientPink":
                setTheme(R.style.AppTheme_GradientPink);
                break;
            case "GradientGreen":
                setTheme(R.style.AppTheme_GradientGreen);
                break;
            case "GradientYellow":
                setTheme(R.style.AppTheme_GradientYellow);
                break;
            case "GradientBlue":
                setTheme(R.style.AppTheme_GradientBlue);
                break;


        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences sh = getSharedPreferences("ColorPref", MODE_PRIVATE);
        boolean nightToggle = sh.getBoolean("nightModeToggle", false);
        if(nightToggle){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        setTheme();
        super.onCreate(savedInstanceState);
        //this is to when app is killed and loaded to set the language
        String language = localeHelper.getLanguage(this);
//        Toast.makeText(getApplicationContext(),language + " this is done from background",Toast.LENGTH_SHORT).show();
        localeHelper.setLocaleLanguage(this,language);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}