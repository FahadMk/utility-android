package com.dgsl.utility_package.ui.ui_theming;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dgsl.utility_package.data.model.ColorListModel;
import com.dgsl.utility_package.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import utility_package.R;

public class UIThemeActivity extends BaseActivity  {

    private static int selectedPosition,selectedGradientPosition = -1;
    private RecyclerView colorRecyclerView,gradientColorRecyclerView;
    private List<ColorListModel> colorList = new ArrayList<>();
    private List<ColorListModel> colorGradientList = new ArrayList<>();
    private colorListAdapter adapter;
    private static boolean gradientToggle,colorToggle = false;
    private colorGradientListAdapter gradientAdapter;
    private Switch darkModeSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ui_theme);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSelectedColorPosition();
        addColorListData();
        addGradientColorListData();
        setColorRecyclerView();
        setGradientRecyclerView();

        darkModeSwitch = findViewById(R.id.switchTheme);
        SharedPreferences sh = getSharedPreferences("ColorPref", MODE_PRIVATE);
        boolean nightToggle = sh.getBoolean("nightModeToggle", false);

        darkModeSwitch.setChecked(nightToggle);

        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences("ColorPref",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    myEdit.putBoolean("nightModeToggle", true);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    myEdit.putBoolean("nightModeToggle", false);
                }
                myEdit.commit();
                myEdit.apply();
            }
        });

        adapter.setOnItemClickListener(new ClickListener<ColorListModel>(){
            @Override
            public void onItemClick(ColorListModel data) {
                SharedPreferences sharedPreferences = getSharedPreferences("ColorPref",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putInt("position", data.getPosition());
                myEdit.putBoolean("tickToggle",true);
                myEdit.commit();
                myEdit.apply();

                adapter.selectedItemPosition = data.getPosition();
                adapter.notifyDataSetChanged();
                setColorThemeSelected(data);
            }
        });

        gradientAdapter.setOnItemClickListener(new GradientClickListener<ColorListModel>() {
            @Override
            public void onItemClick(ColorListModel data) {
                SharedPreferences sharedPreferences = getSharedPreferences("ColorPref",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putInt("GradientPosition", data.getPosition());
                myEdit.putBoolean("tickToggle",false);
                myEdit.commit();
                myEdit.apply();

                gradientAdapter.selectedItemPosition = data.getPosition();
                gradientAdapter.notifyDataSetChanged();
                setColorThemeSelected(data);
            }
        });
    }


    /**
     * set up color recyclerview
     */
    private void setColorRecyclerView(){
        colorRecyclerView = findViewById(R.id.colorListView);
        adapter = new colorListAdapter(colorList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        colorRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        if(gradientToggle && !colorToggle){
            adapter.selectedItemPosition = -1;
        }else{
            adapter.selectedItemPosition = selectedPosition;
        }
        colorRecyclerView.setAdapter(adapter);
        adapter.notifyItemChanged(selectedPosition);
    }

    /**
     * gradient color theme layout setup
     */
    private void setGradientRecyclerView() {
        gradientColorRecyclerView = findViewById(R.id.colorGradientListView);
        gradientAdapter = new colorGradientListAdapter(colorGradientList);
        gradientAdapter.gradientToggle = false;
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        gradientColorRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        if(!gradientToggle && colorToggle){
            gradientAdapter.selectedItemPosition = -1;
        }else{
            gradientAdapter.selectedItemPosition = selectedGradientPosition;
        }
        gradientColorRecyclerView.setAdapter(gradientAdapter);
        gradientAdapter.notifyItemChanged(selectedGradientPosition);
    }


    /**
     * @param list pass the selected color object
     *  depending on the selected color the theme will change for the app.
     */
    private void setColorThemeSelected(ColorListModel list){
        SharedPreferences sharedPreferences = getSharedPreferences("theme",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        switch (list.getColorName()){
            case "teal":
                myEdit.putString("themeSelected", "teal");
                break;

            case "indigo":
                myEdit.putString("themeSelected", "indigo");
                recreate();
                break;

            case "cyan":
                myEdit.putString("themeSelected", "cyan");
                break;

            case "orange":
                myEdit.putString("themeSelected", "orange");
                break;

            case "default":
                myEdit.putString("themeSelected", "default");
                break;

            case "blue":
                myEdit.putString("themeSelected", "blue");
                break;

            case "green":
                myEdit.putString("themeSelected", "green");
                break;

            case "pink":
                myEdit.putString("themeSelected", "pink");
                break;

            case "yellow":
                myEdit.putString("themeSelected", "yellow");
                break;

            case "GradientTeal":
                myEdit.putString("themeSelected", "GradientTeal");
                break;

            case "GradientYellow":
                myEdit.putString("themeSelected", "GradientYellow");
                break;

            case "GradientIndigo":
                myEdit.putString("themeSelected", "GradientIndigo");
                recreate();
                break;

            case "GradientCyan":
                myEdit.putString("themeSelected", "GradientCyan");
                break;

            case "GradientOrange":
                myEdit.putString("themeSelected", "GradientOrange");
                break;

            case "GradientBlue":
                myEdit.putString("themeSelected", "GradientBlue");
                break;

            case "GradientGreen":
                myEdit.putString("themeSelected", "GradientGreen");
                break;

            case "GradientPink":
                myEdit.putString("themeSelected", "GradientPink");
                break;
        }
        recreate();
        myEdit.commit();
        myEdit.apply();
    }

    private void getSelectedColorPosition(){
        SharedPreferences sh = getSharedPreferences("ColorPref", MODE_PRIVATE);
        boolean tickToggle = sh.getBoolean("tickToggle",true);
        if(tickToggle){
            selectedPosition = sh.getInt("position", -1);
            gradientToggle = false;
            colorToggle = true;
        }else{
            selectedGradientPosition = sh.getInt("GradientPosition",-1);
            gradientToggle = true;
            colorToggle = false;
        }
    }


    /**
     * add list of color in array list
     */
    private void addColorListData() {
//        colorGradientList.add(new ColorListModel("#008577",0,"teal"));
        colorList.add(new ColorListModel("#008577",0,"teal",""));
        colorList.add(new ColorListModel("#26c6da",1,"cyan",""));
        colorList.add(new ColorListModel("#FFA500",2,"orange",""));
        colorList.add(new ColorListModel("#043865",3,"blue",""));
        colorList.add(new ColorListModel("#ffca28",4,"yellow",""));
        colorList.add(new ColorListModel("#D81B60",5,"pink",""));
        colorList.add(new ColorListModel("#1B633B",6,"green",""));
        colorList.add(new ColorListModel("#6B00BA",7,"indigo",""));
    }

    private void addGradientColorListData() {
        colorGradientList.add(new ColorListModel("#008577",0,"GradientTeal","#000000"));
        colorGradientList.add(new ColorListModel("#26c6da",1,"GradientCyan","#000000"));
        colorGradientList.add(new ColorListModel("#FFA500",2,"GradientOrange","#000000"));
        colorGradientList.add(new ColorListModel("#043865",3,"GradientBlue","#000000"));
        colorGradientList.add(new ColorListModel("#ffca28",4,"GradientYellow","#000000"));
        colorGradientList.add(new ColorListModel("#D81B60",5,"GradientPink","#000000"));
        colorGradientList.add(new ColorListModel("#1B633B",6,"GradientGreen","#000000"));
        colorGradientList.add(new ColorListModel("#6B00BA",7,"GradientIndigo","#000000"));
    }

}