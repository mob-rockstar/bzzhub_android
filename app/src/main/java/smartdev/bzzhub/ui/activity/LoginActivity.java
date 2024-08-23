package smartdev.bzzhub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.facebook.FacebookIO;
import smartdev.bzzhub.repository.facebook.FacebookUser;
import smartdev.bzzhub.repository.model.CompanyLoginResult;
import smartdev.bzzhub.repository.model.EventBusMessage;
import smartdev.bzzhub.repository.model.UserLoginResponse;
import smartdev.bzzhub.ui.base.BaseActivity;
import smartdev.bzzhub.util.Constant;
import smartdev.bzzhub.util.KeyboardUtils;
import smartdev.bzzhub.util.ProfileUtils;
import smartdev.bzzhub.util.Validator;
import smartdev.bzzhub.util.navigation.NavigationManager;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import smartdev.bzzhub.util.navigation.Arg;

import static smartdev.bzzhub.util.navigation.Arg.ARG_COMPANY_ID;
import static smartdev.bzzhub.util.navigation.Arg.ARG_COMPANY_IMAGE;
import static smartdev.bzzhub.util.navigation.Arg.ARG_COMPANY_NAME;
import static smartdev.bzzhub.util.navigation.Arg.ARG_USER_ID;
import static smartdev.bzzhub.util.navigation.Arg.ARG_USER_IMAGE;
import static smartdev.bzzhub.util.navigation.Arg.ARG_USER_NAME;

public class LoginActivity extends BaseActivity {

    private static AccessTokenTracker accessTokenTracker;

    @BindView(R.id.edittext_email)
    EditText editTextEmail;
    @BindView(R.id.edittext_password)
    EditText editTextPassword;
    @BindView(R.id.checkbox)
    AppCompatCheckBox checkBox;
    @BindView(R.id.btn_facebook)
    Button btnFacebook;

    String strEmail = "", strPassword = "";

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private CallbackManager mCallbackManager;
    AccessToken accessToken;

    String socialLoginType;
    String socialId;
    String strMobile;
    String facebookEmail = "";
    String firstName = "";
    String lastName = "";
    int type = 0;
    private Observable<FacebookUser> dataFromFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        initCurrentActivity();
        initFBObjects();
        progressBar = mProgressBar;
    }

    private void initCurrentActivity(){
        currentActivity = LoginActivity.this;
        setCurrentActivity();
    }

    @OnClick(R.id.layout_login) void onLoginClicked(){
        String validationText = Validator.validateLoginInfo(editTextEmail,editTextPassword);
        strEmail = editTextEmail.getText().toString().trim();
        strPassword = editTextPassword.getText().toString().trim();
        if (validationText.isEmpty()){
            if (tabLayout.getSelectedTabPosition() == 0)
                onLoginUser();
            else
                loginCompany();
        }else
            showSnackBar(validationText,false);
    }

    @OnClick(R.id.tv_join_us) void onJoinUsClicked(){
        NavigationManager.gotoSignUpActivity(currentActivity);
    }

    @OnClick(R.id.layout_parent) void onParentClicked(){
        hideKeyboard();
    }

    @OnClick(R.id.tv_forgot_password) void onForgotPassword(){
        Bundle bundle = new Bundle();
        bundle.putInt(Arg.ARG_LOGIN_TYPE,tabLayout.getSelectedTabPosition());
        NavigationManager.gotoForgotPasswordActivity(currentActivity,bundle);
    }

    @OnClick(R.id.tv_remember_me) void onRememberMeClicked(){
        checkBox.performClick();
    }

    private void onLoginUser(){
        KeyboardUtils.hideKeyboard(LoginActivity.this);
        showProgressBar();
        BzzApp.getBzHubRepository().loginUser(strEmail,strPassword,Constant.getLanguage(getApplicationContext())).doOnNext(
                userLoginResponse -> {
                    hideProgressBar();
                    if (userLoginResponse.getCode() != 200){
                        LoginActivity.this.onError(new Exception(userLoginResponse.getMessage()));
                    }
                })
                .subscribe(new Observer<UserLoginResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(UserLoginResponse userLoginResponse) {
                        hideProgressBar();
                        if (userLoginResponse.getStatus() &&  userLoginResponse.getCode() == 200){
                           // if (checkBox.isChecked()){
                                ProfileUtils.saveCompanyProfile(null);
                                ProfileUtils.saveUserProfile(userLoginResponse.getResult());
                       //     }else {
                      /*          ProfileUtils.saveCompanyProfile(null);
                                Constant.getInstance().setCompanyProfile(null);
                                Constant.getInstance().setUserProfile(userLoginResponse.getResult());
                            }*/

                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra(ARG_USER_NAME,userLoginResponse.getResult().getFullName());
                            intent.putExtra(ARG_USER_IMAGE,userLoginResponse.getResult().getImage());
                            intent.putExtra(ARG_USER_ID,userLoginResponse.getResult().getUserId());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressBar();
                        LoginActivity.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    private void facebookLoginUser(){
        KeyboardUtils.hideKeyboard(LoginActivity.this);
        showProgressBar();
        BzzApp.getBzHubRepository().loginFacebookUser(socialId,Constant.getLanguage(getApplicationContext())).doOnNext(
                userLoginResponse -> {
                    hideProgressBar();
                    if (userLoginResponse.getCode() != 200){
                        LoginActivity.this.onError(new Exception(userLoginResponse.getMessage()));
                    }
                })
                .subscribe(new Observer<UserLoginResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(UserLoginResponse userLoginResponse) {
                        hideProgressBar();
                        if (userLoginResponse.getStatus() &&  userLoginResponse.getCode() == 200){
                            // if (checkBox.isChecked()){
                            ProfileUtils.saveCompanyProfile(null);
                            ProfileUtils.saveUserProfile(userLoginResponse.getResult());
                            //     }else {
                      /*          ProfileUtils.saveCompanyProfile(null);
                                Constant.getInstance().setCompanyProfile(null);
                                Constant.getInstance().setUserProfile(userLoginResponse.getResult());
                            }*/

                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra(ARG_USER_NAME,userLoginResponse.getResult().getFullName());
                            intent.putExtra(ARG_USER_IMAGE,userLoginResponse.getResult().getImage());
                            intent.putExtra(ARG_USER_ID,userLoginResponse.getResult().getUserId());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else{
                            if (userLoginResponse.getCode() == 500){
                                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                                intent.putExtra("tab_position",0);
                                intent.putExtra("facebook_id",socialId);
                                intent.putExtra("first_name",firstName);
                                intent.putExtra("last_name",lastName);
                                intent.putExtra("email",facebookEmail);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressBar();
                        LoginActivity.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    @OnClick(R.id.iv_back) void onBackClicked(){
        finish();
    }

    private void loginCompany(){
        KeyboardUtils.hideKeyboard(LoginActivity.this);
        showProgressBar();
        BzzApp.getBzHubRepository().loginCompany(strEmail,strPassword,Constant.getLanguage(getApplicationContext())).doOnNext(companyLoginResult -> { hideProgressBar();
            if (companyLoginResult.getCode() != 200){
                LoginActivity.this.onError(new Exception(companyLoginResult.getMessage()));
            }})
                .subscribe(new Observer<CompanyLoginResult>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(CompanyLoginResult companyLoginResult) {
                        hideProgressBar();
                        if (companyLoginResult.getStatus() && companyLoginResult.getCode() == 200){
                       //     if (checkBox.isChecked()){
                                ProfileUtils.saveUserProfile(null);
                                ProfileUtils.saveCompanyProfile(companyLoginResult.getResult());
                       /*     }else {
                                ProfileUtils.saveUserProfile(null);
                                Constant.getInstance().setUserProfile(null);
                                Constant.getInstance().setCompanyProfile(companyLoginResult.getResult());
                            }
*/
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra(ARG_COMPANY_NAME,companyLoginResult.getResult().getCompanyName());
                            intent.putExtra(ARG_COMPANY_IMAGE,companyLoginResult.getResult().getImage());
                            intent.putExtra(ARG_COMPANY_ID,companyLoginResult.getResult().getCompanyId());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressBar();
                        LoginActivity.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage messageEvent) {
        if (messageEvent != null) {
            int messageType = messageEvent.getMessageType();
            if (messageType == EventBusMessage.MessageType.ResetPasswordCompleted){
               String message =messageEvent.getJson();
               showSnackBar(getResources().getString(R.string.str_password_change_success),true);
            }
        }
    }

    private void initFBObjects() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logOut();
        LoginButton loginButton = findViewById(R.id.login_button);
        btnFacebook.setOnClickListener(view -> {
            loginButton.setReadPermissions("email", "public_profile");
            loginButton.registerCallback(mCallbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            // BasketApp code
                            accessToken = loginResult.getAccessToken();
                            getUserData();
                        }

                        @Override
                        public void onCancel() {
                            // BasketApp code
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // BasketApp code
                        }
                    });
            if (AccessToken.getCurrentAccessToken() != null) {
                accessToken = AccessToken.getCurrentAccessToken();
                getUserData();
            } else {
                loginButton.performClick();
            }
        });
    }

    private void facebookLoginCompany(){
        KeyboardUtils.hideKeyboard(LoginActivity.this);
        showProgressBar();
        BzzApp.getBzHubRepository().loginFacebookCompany(socialId,Constant.getLanguage(getApplicationContext())).doOnNext(companyLoginResult -> { hideProgressBar();
            if (companyLoginResult.getCode() != 200){
                LoginActivity.this.onError(new Exception(companyLoginResult.getMessage()));
            }})
                .subscribe(new Observer<CompanyLoginResult>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(CompanyLoginResult companyLoginResult) {
                        hideProgressBar();
                        if (companyLoginResult.getStatus() && companyLoginResult.getCode() == 200){
                            //     if (checkBox.isChecked()){
                            ProfileUtils.saveUserProfile(null);
                            ProfileUtils.saveCompanyProfile(companyLoginResult.getResult());
                       /*     }else {
                                ProfileUtils.saveUserProfile(null);
                                Constant.getInstance().setUserProfile(null);
                                Constant.getInstance().setCompanyProfile(companyLoginResult.getResult());
                            }
*/
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra(ARG_COMPANY_NAME,companyLoginResult.getResult().getCompanyName());
                            intent.putExtra(ARG_COMPANY_IMAGE,companyLoginResult.getResult().getImage());
                            intent.putExtra(ARG_COMPANY_ID,companyLoginResult.getResult().getCompanyId());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else {
                            if (companyLoginResult.getCode() == 500){
                                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                                intent.putExtra("tab_position",1);
                                intent.putExtra("facebook_id",socialId);
                                intent.putExtra("first_name",firstName);
                                intent.putExtra("last_name",lastName);
                                intent.putExtra("email",facebookEmail);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressBar();
                        LoginActivity.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    private void getUserData() {
        if (accessTokenTracker != null)
            accessTokenTracker.stopTracking();
        FacebookIO.getUser()
                .subscribe(new SingleObserver<FacebookUser>() {
                    Disposable mDisposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onSuccess(FacebookUser user) {
                        getFBObservable(user);
                        socialId = user.getId();
                        type = 1;
                        socialLoginType = "facebook";
                        facebookEmail = user.getEmail();
                        firstName = user.getFirstName();
                        lastName = user.getLastName();

                        if (tabLayout.getSelectedTabPosition() == 0)
                            facebookLoginUser();
                        else
                            facebookLoginCompany();

                        mDisposable.dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoginActivity.this.onError(e);
                    }
                });
    }



    private void getFBObservable(FacebookUser user) {
        type = 1;
        dataFromFB = Observable.create(e -> {
            e.onNext(user);
            e.onComplete();
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

