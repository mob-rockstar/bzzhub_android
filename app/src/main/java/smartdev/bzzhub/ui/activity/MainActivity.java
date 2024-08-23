package smartdev.bzzhub.ui.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import smartdev.bzzhub.BuildConfig;
import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.MyPreferenceManager;
import smartdev.bzzhub.repository.model.CityResponse;
import smartdev.bzzhub.repository.model.CountryResponse;
import smartdev.bzzhub.repository.model.InterestResponse;
import smartdev.bzzhub.repository.model.SccidResponse;
import smartdev.bzzhub.repository.model.SelectorResponse;
import smartdev.bzzhub.ui.base.BaseActivity;
import smartdev.bzzhub.ui.fragment.CommunityFragment;
import smartdev.bzzhub.ui.fragment.CompanyFragment;
import smartdev.bzzhub.ui.fragment.JobListFragment;
import smartdev.bzzhub.ui.fragment.MediaListFragment;
import smartdev.bzzhub.util.Constant;
import smartdev.bzzhub.util.ProfileUtils;
import smartdev.bzzhub.util.navigation.NavigationManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Intent.ACTION_SEND;
import static smartdev.bzzhub.repository.PreferenceKey.ARG_ALREADY_MEMBER;
import static smartdev.bzzhub.repository.PreferenceKey.ARG_FIREBASE_TOKEN;
import static smartdev.bzzhub.repository.PreferenceKey.ARG_USER_LANGUAGE;

public class MainActivity extends BaseActivity{

    private static String TAG = "MainActivityClass";

    @BindView(R.id.mainView)
    RelativeLayout mainView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;

    @BindView(R.id.bottom_nav_bar)
    BottomNavigationViewEx bottomNavigationViewEx;
    @BindView(R.id.tv_logout)
    TextView tvLogout;

    @BindView(R.id.iv_profile)
    CircleImageView ivProfile;
    @BindView(R.id.layout_profile)
    LinearLayout layoutProfile;
    @BindView(R.id.layout_sign_up)
    LinearLayout layoutSignUp;
    @BindView(R.id.layout_sign_in)
    LinearLayout layoutSignIn;


    @BindView(R.id.tv_name)
    TextView tvName;
    int currentLanguage;
    private List<InterestResponse.Result> interests;
    private ArrayList<SelectorResponse.Result> selectorList =new ArrayList<>();
    private  ArrayList<CountryResponse.Result> countries =new ArrayList<>();
    private  ArrayList<CityResponse.Result> cities =new ArrayList<>();
    private ArrayList<CityResponse.Result> allCities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MyPreferenceManager.getInstance(this).put(ARG_ALREADY_MEMBER,true);

        // If 1 : English, If 2 : Arabic,  Default English
        currentLanguage = MyPreferenceManager.getInstance(getApplicationContext()).getInt(ARG_USER_LANGUAGE,2);
   //     getSCCIDResponse();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        Log.d(TAG, token);
                    }
                });
        initCurrentActivity();
        initSideMenu();
        initUI();
    }

    private void initCurrentActivity(){
        currentActivity = MainActivity.this;
        setCurrentActivity();
    }

    private void initUI() {
        addFragment(new CompanyFragment(),true,false,R.id.frameLayout,null);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mainView.setTranslationX( currentLanguage == 1 ? slideOffset * drawerView.getWidth() : -slideOffset * drawerView.getWidth() );
                drawer.bringChildToFront(drawerView);
                drawer.requestLayout();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);

            }
        };
        drawer.addDrawerListener(toggle);
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //  drawer.setScrimColor(getResources().getColor(android.R.color.transparent));
    }

    @OnClick(R.id.iv_home) void onHomeClicked(){
        openDrawer();
    }

    public void openDrawer() {
        if (!drawer.isDrawerOpen(GravityCompat.START))
            drawer.openDrawer(GravityCompat.START, true);
    }

    public void closeDrawer(){
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START, true);
    }

    private BottomNavigationViewEx.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.company:
                addFragment(new CompanyFragment(),true,false,R.id.frameLayout,null);
                return true;
            case R.id.community:
                addFragment(new CommunityFragment(),true,false,R.id.frameLayout,null);
                return true;
            case R.id.job:
                addFragment(new JobListFragment(),true,false,R.id.frameLayout,null);
                return true;
            case R.id.media:
                addFragment(new MediaListFragment(),true,false,R.id.frameLayout,null);
                return true;
        }
        return false;
    };


    @OnClick(R.id.layout_sign_in) void onSignInClicked(){
        NavigationManager.gotoLoginActivity(currentActivity);
    }

    @OnClick(R.id.layout_sign_up) void onSignUpClicked(){
        NavigationManager.gotoSignUpActivity(currentActivity);
    }

    private void initSideMenu(){
        if (Constant.getInstance().getLoginType() == null){
                layoutProfile.setVisibility(View.GONE);
                tvLogout.setVisibility(View.GONE);
                layoutSignIn.setVisibility(View.VISIBLE);
                layoutSignUp.setVisibility(View.VISIBLE);
        }else {
            layoutProfile.setVisibility(View.VISIBLE);
            tvLogout.setVisibility(View.VISIBLE);
            layoutSignIn.setVisibility(View.GONE);
            layoutSignUp.setVisibility(View.GONE);
        }

        if (Constant.getInstance().getLoginType() != null){
            if (Constant.getInstance().getLoginType() == 1){
                tvName.setText(Constant.getInstance().getCompanyProfile().getCompanyName());
                Glide.with(MainActivity.this).load(Constant.getInstance().getCompanyProfile()
                .getImage()).apply(new RequestOptions().placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_placeholder)).into(ivProfile);

            }else {
                tvName.setText(Constant.getInstance().getUserProfile().getFullName());
                Glide.with(MainActivity.this).load(Constant.getInstance().getUserProfile()
                        .getImage()).apply(new RequestOptions().placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_placeholder)).into(ivProfile);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            closeDrawer();
        }else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1){
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    @OnClick(R.id.layout_about_us) void onAboutUsClicked(){
        NavigationManager.gotoAboutUsActivity(currentActivity);
    }

    @OnClick(R.id.layout_setting) void onSettingsClicked(){
        NavigationManager.gotoSettingsActivity(currentActivity);
    }

    @OnClick(R.id.layout_contact_us) void onContactUsClicked(){
        startActivity(new Intent(MainActivity.this,  ContactActivity.class));
    }

    @OnClick(R.id.layout_share) void onShareClicked(){
      //  share();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
        String shareMessage= "\nLet me recommend you this application\n\n";
        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(shareIntent, "choose one"));
    }

    private void share(){
        try {
/*            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));*/
            Intent shareIntent = new Intent(Intent.ACTION_SENDTO);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));

        } catch(Exception e) {
            //e.toString();
        }
    }

    @OnClick(R.id.layout_home) void onHomePageClicked(){
        bottomNavigationViewEx.setCurrentItem(0);
        closeDrawer();
        addFragment(new CompanyFragment(),true,false,R.id.frameLayout,null);
    }

    @OnClick(R.id.tv_logout) void onLogoutClicked(){
        try {
            LoginManager.getInstance().logOut();
        }catch (Exception e){

        }

        ProfileUtils.saveUserProfile(null);
        ProfileUtils.saveCompanyProfile(null);
        Constant.getInstance().setCompanyProfile(null);
        Constant.getInstance().setUserProfile(null);
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCurrentActivity();

        if (Constant.getInstance().getLoginType() != null){
            if (Constant.getInstance().getLoginType() == 1){
                tvName.setText(Constant.getInstance().getCompanyProfile().getCompanyName());
                Glide.with(MainActivity.this).load(Constant.getInstance().getCompanyProfile()
                        .getImage()).apply(new RequestOptions().placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_placeholder)).into(ivProfile);

            }else {
                tvName.setText(Constant.getInstance().getUserProfile().getFullName());
                Glide.with(MainActivity.this).load(Constant.getInstance().getUserProfile()
                        .getImage()).apply(new RequestOptions().placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_placeholder)).into(ivProfile);
            }
        }
    }

    @OnClick(R.id.layout_profile) public void onProfileClicked(){
        if (Constant.getInstance().getLoginType() != null && Constant.getInstance().getLoginType() == 1){
            NavigationManager.gotoCompanyProfileActivity(currentActivity);
        }else {
            NavigationManager.gotoUserProfileActivity(currentActivity);
        }
    }

    private void getSCCIDResponse(){
        BzzApp.getBzHubRepository().getSCCIDResponse().subscribe(new Observer<SccidResponse>() {
            Disposable mDispose;
            @Override
            public void onSubscribe(Disposable d) {
                mDispose = d;
            }

            @Override
            public void onNext(SccidResponse sccidResponse) {
                if (sccidResponse != null && sccidResponse.getResult() != null){
                    selectorList = (ArrayList<SelectorResponse.Result>) sccidResponse.getResult().getSectors();
                    selectorList.add(new SelectorResponse.Result(0,0,"- Select -","",0));
                    Constant.getInstance().setSelectorList(selectorList);
                    countries = (ArrayList<CountryResponse.Result>) sccidResponse.getResult().getCountries();
                    allCities = (ArrayList<CityResponse.Result>) sccidResponse.getResult().getCities();
                    CountryResponse.Result result = new CountryResponse.Result(0,"- Select -");
                    countries.add(result);
                    Constant.getInstance().setCountries(countries);
                    Constant.getInstance().setCities(allCities);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                mDispose.dispose();
            }
        });
    }

}
