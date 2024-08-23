package smartdev.bzzhub.util.navigation;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import smartdev.bzzhub.GPSTracker;
import smartdev.bzzhub.ui.activity.AboutUsActivity;
import smartdev.bzzhub.ui.activity.CompanyDetailActivity;
import smartdev.bzzhub.ui.activity.CompanyProfileActivity;
import smartdev.bzzhub.ui.activity.ForgotPasswordActivity;
import smartdev.bzzhub.ui.activity.ImageActivity;
import smartdev.bzzhub.ui.activity.LoginActivity;
import smartdev.bzzhub.ui.activity.MainActivity;
import smartdev.bzzhub.ui.activity.OnboardingActivity;
import smartdev.bzzhub.ui.activity.PDFActivity;
import smartdev.bzzhub.ui.activity.UserProfileActivity;
import smartdev.bzzhub.ui.activity.SettingsActivity;
import smartdev.bzzhub.ui.activity.SignUpActivity;
import smartdev.bzzhub.ui.activity.VideoActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.Disposable;

public class NavigationManager {
    private static void goTo(Activity activity, Class clazz, Bundle bundle, String action, int flag) {
        Intent intent = new Intent(activity, clazz);
        if (action != null) {
            intent.setAction(action);
        }
        if (bundle != null) {
            intent.putExtra(clazz.getName(), bundle);
        }
        intent.setFlags(flag);
        activity.startActivity(intent);
    }

    private static void goTo(Context context, Class clazz, Bundle bundle, String action, int flag) {
        Intent intent = new Intent(context, clazz);
        if (action != null) {
            intent.setAction(action);
        }
        if (bundle != null) {
            intent.putExtra(clazz.getName(), bundle);
        }
        intent.setFlags(flag);
        context.startActivity(intent);
    }

    private static void goTo(Activity activity, Class clazz, Bundle bundle) {
        goTo(activity, clazz, bundle, null, 0);
    }

    private static void goTo(Activity activity, Class clazz) {
        goTo(activity, clazz, null, null, 0);
    }

    public static void gotoOnboardingActivity(Activity currentActivity){
        goTo(currentActivity, OnboardingActivity.class);
    }

    public static void gotoLoginActivity(Activity currentActivity){
        goTo(currentActivity, LoginActivity.class);
    }

    public static void gotoMainActivity(Activity currentActivity){
        goTo(currentActivity, MainActivity.class);
    }

    public static void gotoSignUpActivity(Activity currentActivity){

        RxPermissions permissions = new RxPermissions((FragmentActivity) currentActivity);
        permissions.request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        GPSTracker gps = new GPSTracker(currentActivity.getApplicationContext());
                        // Check if GPS enabled
                        if (!gps.canGetLocation()) {
                            // Can't get location.
                            // GPS or network is not enabled.
                            // Ask user to enable GPS/network in settings.
                            gps.showSettingsAlert();
                        }else {
                            goTo(currentActivity, SignUpActivity.class);
                         //   updatePickupLocation(gps.getLatitude(),gps.getLongitude());
                        }
                    }
                });

    }

    public static void gotoForgotPasswordActivity(Activity currentActivity,Bundle bundle){
        goTo(currentActivity, ForgotPasswordActivity.class,bundle);
    }

    public static void gotoCompanyDetailsActivity(Activity currentActivity, Bundle bundle){
        goTo(currentActivity, CompanyDetailActivity.class,bundle);
    }

    public static void gotoPDFActivity(Activity currentActivity, Bundle bundle){
        goTo(currentActivity, PDFActivity.class,bundle);
    }

    public static void gotoImageActivity(Activity currentActivity, Bundle bundle){
        goTo(currentActivity, ImageActivity.class,bundle);
    }

    public static void gotoVideoActivity(Activity currentActivity, Bundle bundle){
        goTo(currentActivity, VideoActivity.class,bundle);
    }

    public static void gotoAboutUsActivity(Activity currentActivity){
        goTo(currentActivity, AboutUsActivity.class);
    }

    public static void gotoSettingsActivity(Activity currentActivity){
        goTo(currentActivity, SettingsActivity.class);
    }

    public static void gotoUserProfileActivity(Activity currentActivity){
        goTo(currentActivity, UserProfileActivity.class);
    }

    public static void gotoCompanyProfileActivity(Activity currentActivity){
        goTo(currentActivity, CompanyProfileActivity.class);
    }
}
