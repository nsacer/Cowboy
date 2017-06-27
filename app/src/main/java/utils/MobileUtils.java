package utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;

import java.util.UUID;


public class MobileUtils {

    /**
     * 手机uuid
     *
     * @param context
     * @return uuid
     */
    public static String getUUID(Context context) {

        String uuid = "";
        final TelephonyManager telephonyManager = (TelephonyManager) (context
                .getSystemService(Context.TELEPHONY_SERVICE));
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + telephonyManager.getDeviceId();
        tmSerial = "" + telephonyManager.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        uuid = deviceUuid.toString();
        return uuid;
    }


    /**
     * 手机imei
     *
     * @param context
     * @return imei
     */
    @SuppressLint("HardwareIds")
    public static String getIMEI(Context context) {


        String imei = "";
        TelephonyManager telephonyManager = (TelephonyManager) (context
                .getSystemService(Context.TELEPHONY_SERVICE));
        if (telephonyManager.getDeviceId() != null) {
            imei = telephonyManager.getDeviceId();
        }
        return imei;
    }

    /**
     * sim卡imsi
     *
     * @param context
     * @return imsi
     */
    public static String getIMSI(Context context) {
        String imsi = "";
        TelephonyManager telephonyManager = (TelephonyManager) (context
                .getSystemService(Context.TELEPHONY_SERVICE));
        if (telephonyManager.getSubscriberId() != null) {
            imsi = telephonyManager.getSubscriberId();
        }
        return imsi;
    }

    /**
     * android 手机的 device id
     *
     * @param context
     * @return deviceId
     */
    public static String getDeviceId(Context context) {
        String deviceId = "";
        final TelephonyManager telephonyManager = (TelephonyManager) (context
                .getSystemService(Context.TELEPHONY_SERVICE));
        deviceId = telephonyManager.getDeviceId();
        return deviceId;
    }

    /**
     * 判断wifi是否可用
     *
     * @param context
     * @return false 不可用;true 可用
     */
    public static boolean isWiFiActive(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                int size = info.length;
                for (int i = 0; i < size; i++) {
                    if (info[i].getTypeName().equalsIgnoreCase("WIFI")
                            && info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String getChannelName(Context ctx) {
        if (ctx == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = applicationInfo.metaData.getString("channel");
                        if (channelName == null) {
                            channelName = String.valueOf(applicationInfo.metaData.getInt("channel"));
                        }
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }

    public static String getVersion(Context context)//获取版本号
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    public static float distance(PointF p1, PointF p2) {
        float x = p1.x - p2.x;
        float y = p1.y - p2.y;
        return (float) Math.sqrt(x * x + y * y);
    }

    public static float angle(PointF p1, PointF p2) {
        return angle(p1.x, p1.y, p2.x, p2.y);
    }

    public static float angle(float x1, float y1, float x2, float y2) {
        return (float) Math.atan2(y2 - y1, x2 - x1);
    }

    public static float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    public static void midpoint(MotionEvent event, PointF point) {
        float x1 = event.getX(0);
        float y1 = event.getY(0);
        float x2 = event.getX(1);
        float y2 = event.getY(1);
        midpoint(x1, y1, x2, y2, point);
    }

    public static void midpoint(float x1, float y1, float x2, float y2, PointF point) {
        point.x = (x1 + x2) / 2.0f;
        point.y = (y1 + y2) / 2.0f;
    }

}
