package smartdev.bzzhub.ui.activity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ProgressBar;

import butterknife.BindView;
import smartdev.bzzhub.R;
import smartdev.bzzhub.ui.base.BaseActivity;
import smartdev.bzzhub.ui.fragment.EnterEmailFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import smartdev.bzzhub.util.navigation.Arg;

public class ForgotPasswordActivity extends BaseActivity {
    public int rootLayout;

    public int userType = 0;

    @BindView(R.id.progress_bar)
    ProgressBar mprogressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);


        this.progressBar = mprogressBar;
        extractArguments();
        rootLayout = R.id.frameLayout;
        initCurrentActivity();
        addFragment(new EnterEmailFragment(),true,false,rootLayout,null);
    }

    private void extractArguments(){
        Bundle bundle = getIntent().getExtras().getBundle(this.getClass().getName());
        if (bundle != null)
            userType = bundle .getInt(Arg.ARG_LOGIN_TYPE,0);
    }

    private void initCurrentActivity(){
        currentActivity = ForgotPasswordActivity.this;
        setCurrentActivity();
    }

    @OnClick(R.id.iv_back) void onBackClicked(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 1){
            getSupportFragmentManager().popBackStack();
        }else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        onBackClicked();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCurrentActivity();
    }
}
