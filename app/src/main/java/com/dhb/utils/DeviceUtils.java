package com.dhb.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Surface;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class DeviceUtils {
    @SuppressLint("InlinedApi")
    public static int getDeviceOrientation(Activity activity) {

        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180)
                && height > width
                || (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270)
                && width > height){
            switch (rotation){
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    Logger.debug("Unknown screen orientation. Defaulting to "
                            + "portrait.");
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        }
        // if the device's natural orientation is landscape or if the device
        // is square:
        else {
            switch (rotation){
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    Logger.debug("Unknown screen orientation. Defaulting to "
                            + "landscape.");
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }

        return orientation;
    }

    public static String getDeviceId(Context activity) {

        TelephonyManager tm = (TelephonyManager) activity
                .getSystemService(Activity.TELEPHONY_SERVICE);

        Logger.debug("Device id- :" + tm.getDeviceId());
        return tm.getDeviceId();
    }

    public static boolean isTablet(Context context) {

        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;

    }

    public static String getRandomUUID() {
        String uuid = UUID.randomUUID().toString();
        Logger.debug("UUID :" + uuid);
        uuid = uuid.replaceAll("-", "");
        return uuid;
    }

    public static String randomString(int length) {

        char[] characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        Random random = new SecureRandom();
        char[] result = new char[length];
        for (int i = 0; i < result.length; i++){
            // picks a random index out of character set > random character
            int randomCharIndex = random.nextInt(characterSet.length);
            result[i] = characterSet[randomCharIndex];
        }
        return new String(result);
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH){
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses){
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                    for (String activeProcess : processInfo.pkgList){
                        if (activeProcess.equals(context.getPackageName())){
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())){
                isInBackground = false;
            }
        }

        return isInBackground;
    }

//	public static boolean isRunningInForeground(Context context) {
//		ActivityManager activityManager =
//				(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		List<ActivityManager.AppTask> tasks = activityManager.getAppTasks();
//		if (tasks == null || (tasks != null && tasks.isEmpty())) {
//			return false;
//		}
//		ActivityManager.RecentTaskInfo topActivityName = tasks.get(0).getTaskInfo();
//		if(topActivityName != null && topActivityName. != null
//				&& topActivityName.origActivity.getPackageName() != null){
//
//			if(topActivityName.origActivity.getPackageName().equals(context.getPackageName())){
//				return true;
//			}
//		}
////		return topActivityName.equalsIgnoreCase(context.getPackageName());
//		return false;
//	}

//	public static boolean isBackgroundRunning(Context context) {
//		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
//		for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
//			if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//				for (String activeProcess : processInfo.pkgList) {
//					if (activeProcess.equals(context.getPackageName())) {
//						return true;
//					}
//				}
//			}
//		}
//
//
//		return false;
//	}
}
