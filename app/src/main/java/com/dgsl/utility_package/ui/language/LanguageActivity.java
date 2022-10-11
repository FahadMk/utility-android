package com.dgsl.utility_package.ui.language;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dgsl.utility_package.ui.BaseActivity;
import com.dgsl.utility_package.ui.settings.SettingActivity;

import java.util.Locale;
import java.util.prefs.BackingStoreException;

import utility_package.R;


public class LanguageActivity extends BaseActivity {

    private Intent intent;
    private Context context;
    private String language;
    private ListView languageView;
    private String[] listItem;
    private languageListAdapter adapter;
    private LocaleHelper localeHelper = new LocaleHelper();
    int selectedPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        context = this;
        LocaleHelper localeHelper = new LocaleHelper();
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSelectedLanguage();

        setUpLanguageList();


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /**
     * get the selected the language for the app
     */
    private void getSelectedLanguage(){
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        selectedPosition = sh.getInt("position", 0);
    }

    /**
     * setting up the adapter for the language list
     */
    private void setUpLanguageList(){
        languageView = findViewById(R.id.languageListView);
        String languageSelected = localeHelper.getLanguage(this);
        listItem = getResources().getStringArray(R.array.languageArray);
        adapter = new languageListAdapter(this,listItem);
        setLanguageSelectedItem(languageSelected);
        languageView.setAdapter(adapter);
        adapter.selectedItemPosition = selectedPosition;
        adapter.notifyDataSetChanged();
        languageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value=adapter.getItem(position);

                adapter.selectedItemPosition = ((adapter.selectedItemPosition!=position)?position:position);
                adapter.notifyDataSetChanged();
                Intent refresh = new Intent(context, SettingActivity.class);
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putInt("position", position);
                myEdit.commit();
                myEdit.apply();
                switch(value){
                    case "മലയാളം":
                        localeHelper.setLocaleLanguage(context,"ml");
                        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(refresh);
                        finish();
                        break;
                    case "हिन्दी":
                        localeHelper.setLocaleLanguage(context,"hi");
                        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(refresh);
                        finish();
                        break;
                    case "English":
                        localeHelper.setLocaleLanguage(context,"en");
                        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(refresh);
                        finish();
                        break;
//                    case "Italiano":
//                        localeHelper.setLocaleLanguage(context,"it");
//                        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(refresh);
//                        finish();
//                        break;

                }
//                Toast.makeText(getApplicationContext(),value,Toast.LENGTH_SHORT).show();
            }

        });
    }




    private void setLanguageSelectedItem(String languageSelected){
        if(languageSelected != null){
            switch(languageSelected){
                case "ml":
                    languageView.setItemChecked(0,true);

                    languageView.setSelected(true);

                    break;
                case "en":
                    languageView.setItemChecked(2,true);
                    languageView.setSelected(true);
                    break;
                case "hi":
                    languageView.setItemChecked(1,true);
                    languageView.setSelected(true);
                    break;
            }

        }else{
            languageView.setItemChecked(2,true);
            languageView.setSelected(true);
        }
    }
}