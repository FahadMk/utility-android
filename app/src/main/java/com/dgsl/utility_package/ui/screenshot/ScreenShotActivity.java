package com.dgsl.utility_package.ui.screenshot;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.dgsl.utility_package.ui.BaseActivity;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

//import utility_package.BuildConfig;
import utility_package.R;

public class ScreenShotActivity extends BaseActivity implements View.OnClickListener {
    MaterialButton enableScreenShotWithInAppBtn,disableScreenShotWithInAppBtn;
    View statusView;
    private static final int PERMISSION_REQUEST_CODE = 200;
    View rootView;
    TextView screenStatusTV;
    SharedPreferences sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_screen_shot);



        screenStatusTV = findViewById(R.id.screenShotStatusTV);
        screenStatusTV.setText("ScreenShot : Enabled");
        enableScreenShotWithInAppBtn = findViewById(R.id.enableScreenshotWithinAppBtn);
        disableScreenShotWithInAppBtn = findViewById(R.id.disableScreenshotWithinAppBtn);

        statusView = findViewById(R.id.notifyView);

        rootView = getWindow().getDecorView().findViewById(android.R.id.content);

        enableScreenShotWithInAppBtn.setOnClickListener(this);
        disableScreenShotWithInAppBtn.setOnClickListener(this);

        sh = getSharedPreferences("ScreenShotSharedPref", MODE_PRIVATE);
        boolean screenShotStatus = sh.getBoolean("status", false);

        if(screenShotStatus){
            statusView.setBackgroundColor(getResources().getColor(R.color.orange));
            screenStatusTV.setText("ScreenShot : Disabled");
        }else{
            screenStatusTV.setText("ScreenShot : Enabled");
            statusView.setBackgroundColor(getResources().getColor(R.color.green));
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences("ScreenShotSharedPref",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        switch(v.getId()){
            case R.id.enableScreenshotWithinAppBtn:
                statusView.setBackgroundColor(getResources().getColor(R.color.green));
                screenStatusTV.setText("ScreenShot : Enabled");
                getWindow( ).clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
                myEdit.putBoolean("status", false);
                myEdit.commit();
                break;
            case R.id.disableScreenshotWithinAppBtn:
                screenStatusTV.setText("ScreenShot : Disabled");
                statusView.setBackgroundColor(getResources().getColor(R.color.orange));
                getWindow( ).setFlags( WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE );

                myEdit.putBoolean("status", true);
                myEdit.commit();
                break;
        }
        myEdit.apply();
    }

    /**
     *   it takes the content view of the screen. Need to pass the
     *            getWindow().getDecorView() of the screen to take it as screen shot and pass it
     */
    private void takeScreenShot(){
        rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED ) {
            // Permission is granted. Continue the action or workflow in your
            // app.
            Date date = new Date();
            CharSequence format = DateFormat.format("MM-dd-yyyy_hh:mm:ss.SSS", date);

            //It will make sure to store file to given below Directory and If the file Directory dosen't exist then it will create it.
            try {
                File mainDir = new File(
                        this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "FileShare");
                if (!mainDir.exists()) {
                    boolean mkdir = mainDir.mkdir();
                }

                //Providing file name along with Bitmap to capture screen view
                String path = mainDir + "/" + "ScreenShot" + "-" + format + ".jpeg";
                rootView.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
                rootView.setDrawingCacheEnabled(false);

                //This logic is used to save file at given location with the given filename and compress the Image Quality.
                File imageFile = new File(path);
                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

                //Create New Method to take ScreenShot with the imageFile.
//                shareImage(imageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            requestPermission();
        }
    }



    /**
     * requestPermission method is used to request user permission to read and write the taken
     * screenshot image need to be called first before access or doing any operation with in app.
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                // if granted
                takeScreenShot();
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, CAMERA)
                            != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        showMessageOKCancel(
                                (dialog, which) -> {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermission();
                                    }
                                });
                    }
                }
            }
        }
    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ScreenShotActivity.this)
                .setMessage("You need to allow access permissions")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    /**
     * @param file need to pass the created file from the takeScreenShot() method to shareImage
     *             which will try to find there is any app which can be shared to. If there is no
     *             app to be shared a toast will be shown saying 'no app Available'
     */
//    private void shareImage(File file){
//        //Using sub-class of Content provider
//
//        Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
//                BuildConfig.APPLICATION_ID + ".provider", file);
//        //Explicit intent
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_SEND);
//        intent.setType("image/*");
//        intent.putExtra(android.content.Intent.EXTRA_TEXT, "This is Sample App to take ScreenShot");
//        intent.putExtra(Intent.EXTRA_STREAM, uri);
//
//        //It will show the application which are available to share Image; else Toast message will throw.
//        try {
//            this.startActivity(Intent.createChooser(intent, "Share With"));
//        } catch (ActivityNotFoundException e) {
//            Toast.makeText(this, "No App Available", Toast.LENGTH_SHORT).show();
//        }
//    }
}