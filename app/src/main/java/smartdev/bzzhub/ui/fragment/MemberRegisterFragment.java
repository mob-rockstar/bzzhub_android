package smartdev.bzzhub.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatCheckBox;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.UserSignUpResponse;
import smartdev.bzzhub.ui.activity.MainActivity;
import smartdev.bzzhub.ui.activity.SignUpActivity;
import smartdev.bzzhub.ui.base.BaseFragment;
import smartdev.bzzhub.util.Constant;
import smartdev.bzzhub.util.KeyboardUtils;
import smartdev.bzzhub.util.ProfileUtils;
import smartdev.bzzhub.util.Validator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import smartdev.bzzhub.util.navigation.NavigationManager;

public class MemberRegisterFragment extends BaseFragment {
    @BindView(R.id.checkbox)
    AppCompatCheckBox checkBox;
    @BindView(R.id.edittext_name)
    EditText editTextName;
    @BindView(R.id.edittext_email)
    EditText editTextEmail;
    @BindView(R.id.edittext_mobile)
    EditText editTextMobile;
    @BindView(R.id.edittext_password)
    EditText editTextPassword;
    @BindView(R.id.edittext_confirm_password)
    EditText editTextConfirmPassword;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    String socialId = "";

    String strName = "",strEmail = "",strMobile = "",strPassword = "";
    double latitude, longitude;

    public MemberRegisterFragment(String mSocialId,String email, String firstName, String lastName) {
        this.socialId = mSocialId;
        this.strName = firstName + " " + lastName;
        this.strEmail = email;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_member_register, container, false);
        ButterKnife.bind(this, view);
        ((SignUpActivity)currentActivity).progressBar = mProgressBar;
        extractArguments();
        initViews();
        return view;
    }

    private void extractArguments(){
        latitude = ((SignUpActivity)currentActivity).latitude;
        longitude =  ((SignUpActivity)currentActivity).longitude;
    }

    private void initViews(){
        if (!socialId.isEmpty()){
            editTextEmail.setText(strEmail);
            editTextName.setText(strName);

        }
    }

    @OnClick(R.id.tv_accept_term) void onTvAcceptTerm(){
        checkBox.performClick();
    }

    @OnClick(R.id.layout_sign_up) void onSignUpClicked(){
        KeyboardUtils.hideKeyboard(currentActivity);
        String validationString = Validator.validateUserRegisterInfo(editTextName,editTextEmail,editTextMobile,editTextPassword,editTextConfirmPassword);
        if (validationString.isEmpty()){
            signUp();
        }else {
            ((SignUpActivity)currentActivity).showSnackBar(validationString,false);
        }
    }

    private void signUp(){
        strName = editTextName.getText().toString().trim();
        strMobile = editTextMobile.getText().toString().trim();
        strEmail = editTextEmail.getText().toString().trim();
        strPassword = editTextPassword.getText().toString().trim();
        ((SignUpActivity)currentActivity).showProgressBar();

        BzzApp.getBzHubRepository().signUpWithUser(strName,strMobile,strEmail,strPassword,latitude,longitude,Constant.getLanguage(getActivity().getApplicationContext()),socialId)
                .doOnNext(userSignUpResponse -> {
                    if (userSignUpResponse.getCode() != 200){
                        ((SignUpActivity)currentActivity).hideProgressBar();
                        ((SignUpActivity)currentActivity).onError(new Exception(userSignUpResponse.getMessage()));
                    }
                })
                .subscribe(new Observer<UserSignUpResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(UserSignUpResponse userSignUpResponse) {
                        ((SignUpActivity)currentActivity).hideProgressBar();
                        if (userSignUpResponse.getStatus() && userSignUpResponse.getCode() == 200){
                            if (checkBox.isChecked()){
                                ProfileUtils.saveCompanyProfile(null);
                                ProfileUtils.saveUserProfile(userSignUpResponse.getResult());
                            }else {
                                    ProfileUtils.saveCompanyProfile(null);
                                    Constant.getInstance().setUserProfile(null);
                                    Constant.getInstance().setUserProfile(userSignUpResponse.getResult());
                            }

                            Intent intent = new Intent(currentActivity, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((SignUpActivity)currentActivity).hideProgressBar();
                        ((SignUpActivity)currentActivity).onError(new Exception(e));
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    @OnClick(R.id.tv_sign_in) void onSignin(){
        NavigationManager.gotoLoginActivity(currentActivity);
        currentActivity.finish();
    }
}
