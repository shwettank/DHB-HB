package com.dhb.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class InputUtils {
    public static void hideKeyboard(Context activity, View view) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (imm != null) {

            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static Drawable createRepeatableDrawable(Activity activity, int imageId) {

        // TODO: yet to write code
        return null;
    }


    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dpi = (metrics.densityDpi / 160f);

        if (dpi == 1.5 && metrics.widthPixels == 480) {
            dpi = 1.30f;
        }

        float px = dp * dpi;
        return px;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();

        float dpi = (metrics.densityDpi / 160f);

        float dp = px / dpi;
        return dp;
    }
    public static boolean isNull(String val){
        if(val==null||val.equals(null)||val.trim().equals("")||val.trim().equals("null")|| val.trim()==""||val.trim()=="null")
            return true;
        return false;
    }

    public boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }
}
