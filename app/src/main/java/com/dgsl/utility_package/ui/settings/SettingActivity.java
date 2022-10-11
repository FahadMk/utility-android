package com.dgsl.utility_package.ui.settings;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ListView;

import com.dgsl.utility_package.data.model.SettingListModel;
import com.dgsl.utility_package.ui.BaseActivity;
import com.dgsl.utility_package.ui.currency_conversion.TabNavActivity;
import com.dgsl.utility_package.ui.language.LanguageActivity;
import com.dgsl.utility_package.ui.language.LocaleHelper;
import com.dgsl.utility_package.ui.root_access_check.RootAccessActivity;
import com.dgsl.utility_package.ui.screenshot.ScreenShotActivity;
import com.dgsl.utility_package.ui.ui_theming.UIThemeActivity;

import java.util.ArrayList;
import java.util.Locale;

import utility_package.R;

public class SettingActivity extends BaseActivity {
    ArrayList<SettingListModel> settingsList;
    SettingsAdapter adapter;
    ListView listView;
    TypedArray iconArray;
    static String color;
    @Override
    protected void onResume() {
        super.onResume();
        if(color != selectedThemeName()){
            setTheme();
            recreate();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        color = selectedThemeName();
        settingsList = new ArrayList<>();

        Resources resources = getResources();
        TypedArray resArrayObj = resources.obtainTypedArray(R.array.settingsArray);
        String[] your_array = getResources().getStringArray(R.array.settingsArray);
        iconArray = getResources().obtainTypedArray(R.array.iconsArray);
        setLocale(LocaleHelper.getLanguage(this));
        getSettingsListConverted(iconArray,your_array);
        resArrayObj.recycle();

        listView.setOnItemClickListener((parent, view, position, id) -> {
//                SettingListModel selectedItem = settingsList.get(position);
            Intent intent;
            switch (position){
                case 0:
                    intent = new Intent(getApplicationContext(), ScreenShotActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(getApplicationContext(), LanguageActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(getApplicationContext(), RootAccessActivity.class);
                    startActivity(intent);
                    break;
//                case 3:
//                    Toast.makeText(getApplicationContext(), "coming soon", Toast.LENGTH_SHORT).show();
//                    break;
                case 3:
                    intent = new Intent(getApplicationContext(), TabNavActivity.class);
                    startActivity(intent);
                    break;
                case 4:
                    intent = new Intent(getApplicationContext(), UIThemeActivity.class);
                    startActivity(intent);
                    break;

            }

        });
    }

    @Override
    public void recreate() {
        super.recreate();
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        startActivity(getIntent());
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        onConfigurationChanged(conf);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    private void getSettingsListConverted(TypedArray obj,String[] arrayObj){

       for(int i=0;i<arrayObj.length;i++){
           settingsList.add(new SettingListModel(arrayObj[i],obj.getResourceId(i, -1)));
       }
//       iconArray.recycle();
        setUpListView();
    }
    private void setUpListView(){
        listView = findViewById(R.id.setting_list);
        adapter = new SettingsAdapter(this, settingsList);
        listView.setAdapter(adapter);
    }
}