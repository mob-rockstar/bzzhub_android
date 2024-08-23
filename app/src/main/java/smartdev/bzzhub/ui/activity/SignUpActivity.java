package smartdev.bzzhub.ui.activity;

import android.os.Bundle;
import android.util.Log;

import smartdev.bzzhub.GPSTracker;
import smartdev.bzzhub.R;
import smartdev.bzzhub.ui.base.BaseActivity;
import smartdev.bzzhub.ui.fragment.CompanyRegisterFragment;
import smartdev.bzzhub.ui.fragment.MemberRegisterFragment;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    public double latitude,longitude;

    int tabPosition = 0;
    String facebokoLoginId = "";
    String email = "";
    String firstName = "";
    String lastName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        extractArguments();
        initCurrentActivity();
        getLocation();
        initTabLayout();

    }

    private void extractArguments(){
        tabPosition = getIntent().getIntExtra("tab_position", -1);
        facebokoLoginId = getIntent().getStringExtra("facebook_id") ;
        firstName = getIntent().getStringExtra("first_name");
        lastName = getIntent().getStringExtra("last_name");
        email = getIntent().getStringExtra("email");

    }

    private void getLocation(){
        GPSTracker gps = new GPSTracker(getApplicationContext());
        // Check if GPS enabled
        if (!gps.canGetLocation()) {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }else {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.d("currentLocation", latitude + "--" + longitude);
        }
    }

    private void initCurrentActivity(){
        currentActivity = SignUpActivity.this;
        setCurrentActivity();
    }

    private void initTabLayout(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    addFragment(new MemberRegisterFragment(facebokoLoginId != null ? facebokoLoginId : "",email != null ? email : "",firstName != null ? firstName : "",lastName != null?  lastName : ""),true,false,R.id.frameLayout,null);
                }else {
                    addFragment(new CompanyRegisterFragment(facebokoLoginId != null ? facebokoLoginId : "",email != null ? email : "",firstName != null ? firstName : "",lastName != null?  lastName : ""),true,false,R.id.frameLayout,null);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if (tabPosition != -1){
            if (tabPosition  == 0){
                addFragment(new MemberRegisterFragment(facebokoLoginId != null ? facebokoLoginId : "",email != null ? email : "",firstName != null ? firstName : "",lastName != null?  lastName : ""),true,true,R.id.frameLayout,null);
            }else {
                TabLayout.Tab tab = tabLayout.getTabAt(tabPosition);
                tab.select();
                addFragment(new CompanyRegisterFragment(facebokoLoginId != null ? facebokoLoginId : "",email != null ? email : "",firstName != null ? firstName : "",lastName != null?  lastName : ""),true,false,R.id.frameLayout,null);
            }
        }else {
            addFragment(new MemberRegisterFragment(facebokoLoginId != null ? facebokoLoginId : "",email != null ? email : "",firstName != null ? firstName : "",lastName != null?  lastName : ""),true,true,R.id.frameLayout,null);
        }
    }

    @OnClick(R.id.layout_parent) void onLayoutParentClicked(){
        hideKeyboard();
    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
    }

    @OnClick(R.id.iv_back) void onBackClicked(){
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCurrentActivity();
    }
}
