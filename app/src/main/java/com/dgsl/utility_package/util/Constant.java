package com.dgsl.utility_package.util;

public class Constant {
    public static String[] paths = { "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
            "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
    public static String test_keys = "test-keys";
    public static String[] rootCheck3 = { "/system/xbin/which", "su"};
    public static String alertDialogMessage = "your device cannot run this app";
}
