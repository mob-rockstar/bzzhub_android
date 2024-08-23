package smartdev.bzzhub.ui.activity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.CompanyDetailResponse;
import smartdev.bzzhub.repository.model.EventBusMessage;
import smartdev.bzzhub.repository.model.SimpleResponse;
import smartdev.bzzhub.ui.adapter.CompanyDetailPagerAdapter;
import smartdev.bzzhub.ui.base.BaseActivity;
import smartdev.bzzhub.ui.fragment.CompanyHomeFragment;
import smartdev.bzzhub.ui.fragment.CompanyMapFragment;
import smartdev.bzzhub.ui.fragment.CompanyMediaFragment;
import smartdev.bzzhub.ui.fragment.CompanyRFQFragment;
import smartdev.bzzhub.ui.fragment.CompanyTenderFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import smartdev.bzzhub.util.Constant;
import smartdev.bzzhub.util.KeyboardUtils;

import static smartdev.bzzhub.util.navigation.Arg.ARG_COMPANY_ID;
import static smartdev.bzzhub.util.navigation.Arg.ARG_CONNECTED_STATUS;

public class CompanyDetailActivity extends BaseActivity {

    public Integer companyId;

    @BindView(R.id.view_pager)
    public ViewPager viewPager;
    @BindView(R.id.tabLayout)
    public TabLayout tabLayout;

    @BindView(R.id.iv_profile)
    CircleImageView ivProfile;
    @BindView(R.id.iv_banner)
    ImageView ivBanner;
    @BindView(R.id.tv_comapny_name)
    TextView tvCompanyName;
    @BindView(R.id.tv_address)
    TextView tAddress;
    @BindView(R.id.rootview)
    LinearLayout layoutRootView;
    @BindView(R.id.tv_connect)
    TextView tvConnect;
    @BindView(R.id.cv_connect)
    CardView cvConnect;

    List<Fragment> fragments = new ArrayList<>();
    List<String> mTitle= new ArrayList<>();
    public int connectedStatus = 0;

    @BindView(R.id.progress_bar)
    View mProgressBar;
    private Dialog dialog;

    boolean isDialogShowing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_detail);
        ButterKnife.bind(this);
        currentActivity = this;
        initCurrentActivity();
        extractArguments();
        initFragmentLists();
        setUI();
    }

    private void setUI(){

        viewPager.setAdapter(new CompanyDetailPagerAdapter(getSupportFragmentManager(),fragments,mTitle));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                KeyboardUtils.hideKeyboard(currentActivity);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (Constant.getInstance().getLoginType() == null){
            LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
            tabStrip.getChildAt(4).setOnTouchListener((v, event) -> {
                if (!isDialogShowing){
                    showRequireLogin();
                }
                return true;
            });
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                KeyboardUtils.hideKeyboard(currentActivity);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initFragmentLists(){
        fragments.add(new CompanyHomeFragment());
        fragments.add(new CompanyTenderFragment());
        fragments.add(new CompanyMediaFragment());
        fragments.add(new CompanyMapFragment());
        fragments.add(new CompanyRFQFragment());

        mTitle.add(currentActivity.getResources().getString(R.string.str_home));
        mTitle.add(currentActivity.getResources().getString(R.string.str_tender));
        mTitle.add(currentActivity.getResources().getString(R.string.str_media));
        mTitle.add(currentActivity.getResources().getString(R.string.str_map));
        mTitle.add(currentActivity.getResources().getString(R.string.str_rfq));
    }

    private void initCurrentActivity(){
        currentActivity = CompanyDetailActivity.this;
        BzzApp.setCurrentActivity(currentActivity);
    }

    private void extractArguments(){
        Bundle bundle = getIntent().getExtras().getBundle(this.getClass().getName());
        if (bundle != null){
            connectedStatus = bundle.getInt(ARG_CONNECTED_STATUS,2);
            companyId = bundle .getInt(ARG_COMPANY_ID);
        }
    }

    public void setCompanyProfile(CompanyDetailResponse.CompanyDetail companyProfile){
        tvCompanyName.setText(companyProfile.getCompanyName());
        tAddress.setText(getResources().getString(R.string.str_address_value,companyProfile.getCountry() == null ? "":companyProfile.getCountry(),
                companyProfile.getCity() == null ? "":companyProfile.getCity()));
        Glide.with(currentActivity).load(companyProfile.getImage()).centerCrop().apply(new RequestOptions().placeholder(R.drawable.ic_bzzhub).error(R.drawable.ic_bzzhub)).into(ivProfile);
        Glide.with(currentActivity).load(companyProfile.getBanner()).centerCrop().apply(new RequestOptions().placeholder(R.drawable.ic_bzzhub).error(R.drawable.ic_bzzhub)).into(ivBanner);

        setConnection();
        if (connectedStatus == 0){
            cvConnect.setOnClickListener(v -> {
                connectedStatus = 1;
                connectCompany();
                setConnection();
            });
        }
    }

    private void setConnection(){
        if (Constant.getInstance().getLoginType() != null) {
            if (Constant.getInstance().getLoginType() == 1) {
                if (connectedStatus == 0) {
                    cvConnect.setVisibility(View.VISIBLE);
                    tvConnect.setText("Connect");
                } else if (connectedStatus == 1) {
                    cvConnect.setVisibility(View.VISIBLE);
                    tvConnect.setText("Connected");
                } else {
                    cvConnect.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCurrentActivity();
    }

    @OnClick(R.id.iv_back) void onBackClicked(){
        finish();
    }

    private void connectCompany(){
        mProgressBar.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().connectToCompany(Constant.getInstance().getCompanyProfile().getCompanyId(),companyId)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose  =d ;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        mProgressBar.setVisibility(View.GONE);
                        EventBus.getDefault().post(new EventBusMessage(EventBusMessage.MessageType.CompanyConnectionChanged,""));
                        if (simpleResponse.getStatus() != null && simpleResponse.getStatus()){
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    private void showRequireLogin() {
        dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_require_login, null);
        dialog.getWindow();

        isDialogShowing = true;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tvOk = dialog.findViewById(R.id.tv_yes);
        tvOk.setOnClickListener(v -> {
            dialog.dismiss();
            if (currentActivity instanceof CompanyDetailActivity){
                ((CompanyDetailActivity)currentActivity).viewPager.setCurrentItem(0,false);
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);

        dialog.setOnDismissListener(dialog -> isDialogShowing = false);
        dialog.setCancelable(true);
        dialog.show();
    }
}
