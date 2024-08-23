package smartdev.bzzhub.ui.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import smartdev.bzzhub.R;
import smartdev.bzzhub.ui.base.BaseActivity;
import smartdev.bzzhub.ui.adapter.ViewPagerAdapter;
import smartdev.bzzhub.ui.fragment.ConnnectFragment;
import smartdev.bzzhub.ui.fragment.GlobalBusinessFragment;
import smartdev.bzzhub.ui.fragment.SearchFragment;
import smartdev.bzzhub.util.navigation.NavigationManager;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnboardingActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.dots_indicator)
    SpringDotsIndicator dotsIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);

        initCurrentActivity();
        setViewPager();
    }

    private void initCurrentActivity(){
        currentActivity = OnboardingActivity.this;
        setCurrentActivity();
    }

    private void setViewPager(){
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ConnnectFragment());
        fragments.add(new GlobalBusinessFragment());
        fragments.add(new SearchFragment());
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),fragments));
        dotsIndicator.setViewPager(viewPager);

    }

    public void onNextClicked(){
        if (viewPager.getCurrentItem() < 2)
            viewPager.setCurrentItem(viewPager.getCurrentItem()+ 1,false);
        else{
            openLoginScreen();
        }
    }

    private void openLoginScreen(){
        NavigationManager.gotoMainActivity(currentActivity);
        finish();
    }

    @OnClick(R.id.tv_skip) void onSkipClicked(){
        openLoginScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCurrentActivity();
    }
}
