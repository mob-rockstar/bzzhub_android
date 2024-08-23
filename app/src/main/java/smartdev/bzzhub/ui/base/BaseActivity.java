package smartdev.bzzhub.ui.base;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import smartdev.bzzhub.BzzApp;

import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.MyPreferenceManager;
import smartdev.bzzhub.ui.activity.SplashActivity;
import smartdev.bzzhub.util.KeyboardUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

import static smartdev.bzzhub.repository.PreferenceKey.ARG_USER_LANGUAGE;

public class BaseActivity extends AppCompatActivity {

    public AppCompatActivity currentActivity;

    public ProgressBar progressBar;

    protected void setCurrentActivity(){
        BzzApp.setCurrentActivity(currentActivity);
    }

    public void hideKeyboard(){
        KeyboardUtils.hideKeyboard(currentActivity);
    }

    public void openKeyboard(EditText editText){
        KeyboardUtils.openKeyboard(currentActivity,editText);
    }

    public void showSnackBar(String text,boolean isSuccess){
       Snackbar snackbar =  Snackbar.make(currentActivity.findViewById(android.R.id.content),
                text,
                Snackbar.LENGTH_SHORT);
       snackbar.getView().setBackgroundColor(getResources().getColor(isSuccess?R.color.color_green:R.color.color_background_snackbar));
        TextView tv = (TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        snackbar.show();
    }

    //Add fragment to backstack
    public void addFragment(Fragment newFragment,boolean isRootFragment, boolean isAnimateRTL,int view,Bundle bundle) {
        FragmentManager fragmentManager = getCurrentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isRootFragment) {
            clearBackStack();
        }

      //  if (isAnimateRTL)
        //    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
     //   else
      //      fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);


        Fragment availableFragment = fragmentManager.findFragmentByTag(newFragment.getClass().getName());
        if (availableFragment != null) {
            int stackPosition = isFragmentInBackStack(fragmentManager, newFragment.getClass().getName());

            //if current fragment exists already in back stack, pop it from stack
            if (stackPosition != -1 && fragmentManager.getBackStackEntryCount() > stackPosition) {
                FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(stackPosition);
                fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }

        if (bundle != null)newFragment.setArguments(bundle);
        //add new fragment to frame layout
        fragmentTransaction.add(view, newFragment, newFragment.getClass().getName());
        fragmentTransaction.addToBackStack(newFragment.getClass().getName());
        fragmentTransaction.commitAllowingStateLoss();
    }
    private boolean isRootFragment(Fragment fragment) {
        return true;
      //  return (fragment.getClass().equals(AllProductFragment.class) );
    }


    public void clearBackStack() {
        getCurrentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public int isFragmentInBackStack(FragmentManager fragmentManager, String fragmentTag) {
        for (int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++) {
            if (fragmentTag.equals(fragmentManager.getBackStackEntryAt(entry).getName())) {
                return entry;
            }
        }
        return -1;
    }

    protected FragmentManager getCurrentFragmentManager() {
        return getSupportFragmentManager();
    }

    public void onError(Throwable throwable) {
        try {
            if (currentActivity != null && !currentActivity.isFinishing()) {
                showSnackBar(throwable.getMessage(),false);
            }
        }catch (Exception e){

            e.printStackTrace();
        }
    }

    public void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    public void hideProgressBar(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        checkLocale(this);
        super.onCreate(savedInstanceState);
    }

    public void restartApp(){
        Intent intent = new Intent(getApplicationContext(),SplashActivity.class);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    public void changeLocale(String location,Context mContext) {

        Locale locale = new Locale(location);
        if (android.os.Build.VERSION.SDK_INT > 25){
            // Do something for lollipop and above versions
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            Configuration config = new Configuration();
            config.setLocale(locale);
            config.setLocales(localeList);
            mContext.getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            mContext.createConfigurationContext(config);
            BzzApp.setAppContext(BzzApp.getAppContext().createConfigurationContext(config));
        } else{
            // do something for phones running an SDK before lollipop
            Locale.setDefault(locale);
            Configuration config = mContext.getResources().getConfiguration();
            config.setLocale(locale);
            mContext.createConfigurationContext(config);
            BzzApp.setAppContext(BzzApp.getAppContext().createConfigurationContext(config));
            mContext.getResources().updateConfiguration(config, mContext.getResources().getDisplayMetrics());
        }
    }

    public void checkLocale(Context mContext) {
        if (MyPreferenceManager.getInstance(getApplicationContext()).getInt(ARG_USER_LANGUAGE,2) == 2) {
            changeLocale("ar",mContext);
        } else {
            changeLocale("en",mContext);
        }
    }

    public String getLocale(){
        if (MyPreferenceManager.getInstance(getApplicationContext()).getInt(ARG_USER_LANGUAGE,2) == 2) {
            return "ar";
        } else {
            return "en";
        }
    }
}
