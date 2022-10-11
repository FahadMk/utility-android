package com.dgsl.utility_package.ui.root_access_check;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dgsl.utility_package.ui.BaseActivity;
import com.scottyab.rootbeer.RootBeer;
import com.scottyab.rootbeer.util.Utils;


import java.util.ArrayList;
import utility_package.R;

public class RootAccessActivity extends BaseActivity {

    ArrayList<RootItemResult> rootItemResultList;
    RootBeer rootBeer;
    ListView listView;
    RootItemAdapter adapter;
    LinearLayout layoutView;
    Button checkForRootBtn;
    View rootCardView,tryAgainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_access);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rootBeer = new RootBeer(this);
        checkForRootBtn = findViewById(R.id.rootAccessCheckBtn);
        rootCardView = findViewById(R.id.rootCardView);
        tryAgainView = findViewById(R.id.tryAgainTV);
        listView = findViewById(R.id.rootAccessListView);
        layoutView = findViewById(R.id.rootListLayoutView);
        rootItemResultList  = new ArrayList<>();
        addRootCheckDataList();

        checkForRootBtn.setOnClickListener(v -> {
            setUpListView();
        });

        TextView rootStatus = findViewById(R.id.rootStatusTV);
        if(rootBeer.isRooted()){
            rootStatus.setText("Status : Rooted");
        }else{
            rootStatus.setText("Status : Not Rooted");
        }

        tryAgainView.setOnClickListener(v -> {
            adapter.clear();
            adapter.notifyDataSetChanged();
            layoutView.setVisibility(View.GONE);
            rootCardView.setVisibility(View.VISIBLE);
            tryAgainView.setVisibility(View.GONE);
            addRootCheckDataList();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setUpListView(){
        Animation bottomUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bottom_up);
        LayoutAnimationController anim = AnimationUtils.loadLayoutAnimation(this, R.anim.my_layout_animation);
        rootCardView.setVisibility(View.GONE);
        layoutView.startAnimation(bottomUp);
        layoutView.setVisibility(View.VISIBLE);
        listView.setLayoutAnimation(anim);
        adapter = new RootItemAdapter(this, rootItemResultList);
        listView.setAdapter(adapter);
        tryAgainView.setVisibility(View.VISIBLE);

    }
    private void addRootCheckDataList(){
        rootItemResultList.add(new RootItemResult("Root Management Apps", rootBeer.detectRootManagementApps()));
//        rootItemResultList.add(new RootItemResult("Potentially Dangerous Apps", rootBeer.detectPotentiallyDangerousApps()));
        rootItemResultList.add(new RootItemResult("Root Cloaking Apps", rootBeer.detectRootCloakingApps()));
//        rootItemResultList.add(new RootItemResult("TestKeys", rootBeer.detectTestKeys()));
        rootItemResultList.add(new RootItemResult("BusyBoxBinary", rootBeer.checkForBusyBoxBinary()));
//        rootItemResultList.add(new RootItemResult("SU Binary", rootBeer.checkForSuBinary()));
//        rootItemResultList.add(new RootItemResult("2nd SU Binary check", rootBeer.checkSuExists()));
//        rootItemResultList.add(new RootItemResult("For RW Paths", rootBeer.checkForRWPaths()));
//        rootItemResultList.add(new RootItemResult("Dangerous Props", rootBeer.checkForDangerousProps()));
        rootItemResultList.add(new RootItemResult("Root via native check", rootBeer.checkForRootNative()));
//        rootItemResultList.add(new RootItemResult("SE linux Flag Is Enabled", !Utils.isSelinuxFlagInEnabled()));
        rootItemResultList.add(new RootItemResult("Magisk specific checks", rootBeer.checkForMagiskBinary()));
    }

}