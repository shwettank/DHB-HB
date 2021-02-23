package com.dhb.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;

import java.util.Stack;

public class UiUtils {
    private static UiUtils instance = null;

    protected UiUtils() {
        // Exists only to defeat instantiation.
    }

    public static UiUtils getInstance() {
        if (instance == null){
            instance = new UiUtils();
        }
        return instance;
    }

    public void switchToActivity(Activity current, Class<? extends Activity> otherActivityClass, Bundle extras) {
        Intent intent = new Intent(current, otherActivityClass);
        if (extras != null){
            intent.putExtras(extras);
        }
        current.startActivity(intent);
        current.finish();
    }

    public void goToActivity(Activity current, Class<? extends Activity> otherActivityClass, Bundle extras) {
        Intent intent = new Intent(current, otherActivityClass);
        if (extras != null){
            intent.putExtras(extras);
        }
        current.startActivity(intent);
    }

    public Typeface createTypeFace(Context context, String fontName) {
        Typeface tf = Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/" + fontName);
        return tf;
    }

    public void clearActivityStack(Stack<Activity> activityStack) {
        for (Activity activity : activityStack){
            activity.finish();
        }
        activityStack.clear();
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getApplicationContext()
                .getResources().getDisplayMetrics();
        int px = Math.round(dp
                * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

}
