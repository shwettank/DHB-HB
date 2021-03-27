package com.dhb.uiutils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.dhb.utils.AppPreferenceManager;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class AbstractFragment extends Fragment implements
        ActivityHelper {

    private Context context;
    protected int sdk;
    protected AppPreferenceManager appPreferenceManager;
    protected Typeface fontOpenRobotoRegular, fontOpenRobotoMedium, fontOpenRobotoLight;

    private ActivityHelper ah;

    private Activity activity;

    public static ArrayList<String> selectedCategory = new ArrayList<String>();

    TextView txtPAgeNo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ah = new ActivityHelperImpl(
                getActivity());


        sdk = android.os.Build.VERSION.SDK_INT;
        activity = getActivity();
        appPreferenceManager = new AppPreferenceManager(activity);
        setFont();
        // TODO Auto-generated method stub
        return super.onCreateView(inflater, container,
                savedInstanceState);
    }

    private void setFont() {
        fontOpenRobotoRegular = Typeface.createFromAsset(activity.getAssets(), "fonts/roboto-regular.ttf");
        fontOpenRobotoMedium = Typeface.createFromAsset(activity.getAssets(), "fonts/roboto-medium.ttf");
        fontOpenRobotoLight = Typeface.createFromAsset(activity.getAssets(), "fonts/roboto-light.ttf");

    }

    @Override
    public void hideKeyboard(View view) {

        ah.hideKeyboard(view);
    }

    @Override
    public Typeface createTypeFace(String fontName) {

        return ah.createTypeFace(fontName);
    }

    @Override
    public Drawable createRepeatableDrawable(int imageId) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isNetworkAvailable(Activity activity) {

        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void switchToActivity(Activity			current,
                                 Class<? extends Activity>	otherActivityClass,
                                 Bundle			extras) {

        ah.switchToActivity(current, otherActivityClass, extras);

    }

    @Override
    public void goToActivity(Activity			current,
                             Class<? extends Activity>	otherActivityClass,
                             Bundle				extras) {

        ah.goToActivity(current, otherActivityClass, extras);

    }

    @Override
    public void initUI() {

        // TODO Auto-generated method stub

    }

    @Override
    public void onAttach(Activity activity) {
        context = (FragmentActivity) activity;
        super.onAttach(activity);
    }

	/* public void pushFragments(Fragment fragment, boolean shouldAnimate,
	                       boolean shouldAdd, String tag) {
	     FragmentManager manager = ((HomeScreenActivity) getActivity()).getSupportFragmentManager();
	     FragmentTransaction ft = manager.beginTransaction();
	     if (shouldAnimate) {
	         // ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
	         // R.animator.fragment_slide_left_exit,
	         // R.animator.fragment_slide_right_enter,
	         // R.animator.fragment_slide_right_exit);
	     }

	     ft.replace(R.id.fr_tab_container, fragment, tag);

	     if (shouldAdd) {
	 *//*
     * here you can create named backstack for realize another logic.
     * ft.addToBackStack("name of your backstack");
     *//*
	         ft.addToBackStack(tag);
	     } else {
	 *//*
     * and remove named backstack:
     * manager.popBackStack("name of your backstack",
     * FragmentManager.POP_BACK_STACK_INCLUSIVE); or remove whole:
     * manager.popBackStack(null,
     * FragmentManager.POP_BACK_STACK_INCLUSIVE);
     *//*
	         manager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	     }

	     ft.commit();
	   }*/

// validate First Name

    public static boolean validateFName(String firstName) {

        return firstName.matches("[a-zA-Z-']*");
    }

    // validate last name
    public static boolean validateLName(String lastName) {

        return lastName.matches("[a-zA-Z'-]*");
    }

    public static boolean validatePhoneNumber(CharSequence target) {
        Pattern digitPattern = Pattern.compile("[0-9+-]*");

        return !TextUtils.isEmpty(target)
                && digitPattern.matcher(target)
                .matches();


    }

    public static boolean validateDigit(CharSequence target) {
        Pattern digitPattern = Pattern.compile("[0-9]*");

        return !TextUtils.isEmpty(target)
                && digitPattern.matcher(target)
                .matches();
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target)
                && android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                .matches();
    }

}
