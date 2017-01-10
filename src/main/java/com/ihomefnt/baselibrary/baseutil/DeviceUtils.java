package com.ihomefnt.baselibrary.baseutil;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by shulong on 2014/10/18.
 */
public class DeviceUtils {
    /**
     * Unknown network class. {@hide}
     */
    public static final int NETWORK_CLASS_UNKNOWN = 0;
    public static final String NEWWORK_2G = "2g";
    public static final String NEWWORK_3G = "3g";
    public static final String NEWWORK_4G = "4g";
    public static final String NEWWORK_WIFI = "wifi";
    public static final String NEWWORK_UNKNOWN = "known";
    public static final String APP_LABEL = "android";

    private static final String KEY_PARTNER = "partner";
    private static final String KEY_PARTNER_NAME = "partnerName";

    private static String deviceToken = null;
    /**
     * Class of broadly defined "2G" networks. {@hide}
     */
    public static final int NETWORK_CLASS_2_G = 1;
    /**
     * Class of broadly defined "3G" networks. {@hide}
     */
    public static final int NETWORK_CLASS_3_G = 2;
    /**
     * Class of broadly defined "4G" networks. {@hide}
     */
    public static final int NETWORK_CLASS_4_G = 3;


    /**
     * Network type is unknown
     */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    /**
     * Current network is GPRS
     */
    public static final int NETWORK_TYPE_GPRS = 1;
    /**
     * Current network is EDGE
     */
    public static final int NETWORK_TYPE_EDGE = 2;
    /**
     * Current network is UMTS
     */
    public static final int NETWORK_TYPE_UMTS = 3;
    /**
     * Current network is CDMA: Either IS95A or IS95B
     */
    public static final int NETWORK_TYPE_CDMA = 4;
    /**
     * Current network is EVDO revision 0
     */
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    /**
     * Current network is EVDO revision A
     */
    public static final int NETWORK_TYPE_EVDO_A = 6;
    /**
     * Current network is 1xRTT
     */
    public static final int NETWORK_TYPE_1xRTT = 7;
    /**
     * Current network is HSDPA
     */
    public static final int NETWORK_TYPE_HSDPA = 8;
    /**
     * Current network is HSUPA
     */
    public static final int NETWORK_TYPE_HSUPA = 9;
    /**
     * Current network is HSPA
     */
    public static final int NETWORK_TYPE_HSPA = 10;
    /**
     * Current network is iDen
     */
    public static final int NETWORK_TYPE_IDEN = 11;
    /**
     * Current network is EVDO revision B
     */
    public static final int NETWORK_TYPE_EVDO_B = 12;
    /**
     * Current network is LTE
     */
    public static final int NETWORK_TYPE_LTE = 13;
    /**
     * Current network is eHRPD
     */
    public static final int NETWORK_TYPE_EHRPD = 14;
    /**
     * Current network is HSPA+
     */
    public static final int NETWORK_TYPE_HSPAP = 15;

    /**
     * get device display information
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getDisplayInfo(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WM.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    public static int getDisplayWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WM.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int getDisplayHeight(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WM.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }


    public static String getNetWorkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null) {

            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                //mobile network
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                int networkClass = getNetworkClass(tm.getNetworkType());
                switch (networkClass) {
                    case NETWORK_CLASS_2_G:
                        return NEWWORK_2G;
                    case NETWORK_CLASS_3_G:
                        return NEWWORK_3G;
                    case NETWORK_CLASS_4_G:
                        return NEWWORK_4G;
                    default:
                        return NEWWORK_UNKNOWN;
                }
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                //wifi network
                return NEWWORK_WIFI;
            }
        }

        return "";
    }

    public static int getNetworkClass(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;
            case NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }

    public static String getAppVersion(Context context) {
        String version = "unknown";
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (Exception e) {
        }

        return version;
    }

    public static String getDeviceToken(Context context) {
        if (deviceToken == null) {
            deviceToken = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceToken;
    }

    public static String getAppLabel() {
        return APP_LABEL;
    }

    public static String getOSInfo(Context context) {
        return Build.VERSION.RELEASE;

    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return (info != null && (info.getType() == ConnectivityManager.TYPE_WIFI));
    }
}
