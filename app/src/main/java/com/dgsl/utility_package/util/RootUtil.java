package com.dgsl.utility_package.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class RootUtil {


    /**
     * @return isDeviceRooted should be called to check if the device is rooted or not
     * it contains three type of checking within device for rooted access depending on that it
     * will return true or false.
     */
    public static boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
    }


    /**
     * @return checkRootedMethod1 checks for the test keys if it exist with the device os build
     * depending on if it contains the key the method will return true or false.
     */
    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains(Constant.test_keys);
    }


    /**
     * @return checkRootMethod2 search for array of file path if it exist with in the app. If
     * any of the mentioned path exist means the device is rooted and method return true depending
     * on it.
     */
    private static boolean checkRootMethod2() {
        for (String path : Constant.paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    /**
     * @return checkRootedMethod3 checks for the array of string content if it exist in the path.
     * It runs command line code to check with in the app and return true or false depending on the
     * existing of files path or content for rooted device.
     */
    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(Constant.rootCheck3);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

    /**
     * @param message it shows the display text to user for the alert dialog
     * @param activity pass the current context or this in the activity to show
     */
    public static void showAlertDialogAndExitApp(String message, Activity activity) {

        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                });

        alertDialog.show();
    }
}
