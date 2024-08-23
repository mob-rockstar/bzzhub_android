package smartdev.bzzhub.ui.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import org.greenrobot.eventbus.EventBus;

import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.EventBusMessage;
import smartdev.bzzhub.repository.model.SimpleResponse;
import smartdev.bzzhub.ui.activity.ForgotPasswordActivity;
import smartdev.bzzhub.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static smartdev.bzzhub.util.navigation.Arg.ARG_COMPANY_ID;
import static smartdev.bzzhub.util.navigation.Arg.ARG_USER_ID;

public class ResetPasswordFragment extends BaseFragment {

    @BindView(R.id.edittext_password)
    EditText editTextPassword;
    @BindView(R.id.edittext_confirm_password)
    EditText editTextConfirmPassword;

    private String strPassword = "",strConfirmPassword;
    private int userType = 0;
    private int companyId;

    private int userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        ButterKnife.bind(this, view);
        userType = ((ForgotPasswordActivity)currentActivity).userType;
        extractArguments();
        return view;
    }

    private void extractArguments(){
        if (userType== 0){
            userId = Integer.parseInt(getArguments().getString(ARG_USER_ID,"0"));
        }else {
            companyId = Integer.parseInt(getArguments().getString(ARG_COMPANY_ID,"0"));
        }
    }

    private String validateInfo(){
        strPassword = editTextPassword.getText().toString().trim();
        strConfirmPassword = editTextConfirmPassword.getText().toString().trim();
       if (strPassword.isEmpty())
            return currentActivity.getResources().getString(R.string.str_empty_password);
        else if (strPassword.length() < 6)
            return currentActivity.getResources().getString(R.string.str_invalid_password);
        else if (!strPassword.equals(strConfirmPassword))
            return currentActivity.getResources().getString(R.string.str_not_match_password);
        else return "";
    }

    @OnClick(R.id.layout_login) void onLoginAgain(){
        if (validateInfo().isEmpty()){
            resetPassword();
        }else {
            ((ForgotPasswordActivity)currentActivity).showSnackBar(validateInfo(),false);
        }
    }

    private void resetPassword(){
        if (userType == 0) resetPasswordUser();
        else resetPasswordCompany();
    }

    private void resetPasswordUser(){
        BzzApp.getBzHubRepository().resetPasswordUser(userId,strPassword).doOnNext(simpleResponse -> {
            if (simpleResponse.getCode() != 200){
                ((ForgotPasswordActivity)currentActivity).hideProgressBar();
                ((ForgotPasswordActivity)currentActivity).onError(new Exception(simpleResponse.getMessage()));
            }
        })
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDisposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        if (simpleResponse.getStatus() && simpleResponse.getCode() == 200){
                            EventBus.getDefault().post(new EventBusMessage(EventBusMessage.MessageType.ResetPasswordCompleted,simpleResponse.getMessage()));
                            currentActivity.finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((ForgotPasswordActivity)currentActivity).hideProgressBar();
                        ((ForgotPasswordActivity)currentActivity).onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDisposable.dispose();
                    }
                });
    }

    private void resetPasswordCompany(){
        BzzApp.getBzHubRepository().resetPasswordCompany(companyId,strPassword).doOnNext(simpleResponse -> {
            if (simpleResponse.getCode() != 200){
                ((ForgotPasswordActivity)currentActivity).hideProgressBar();
                ((ForgotPasswordActivity)currentActivity).onError(new Exception(simpleResponse.getMessage()));
            }
        })
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDisposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        if (simpleResponse.getStatus() &&  simpleResponse.getCode() == 200 ){
                            currentActivity.finish();
                         }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((ForgotPasswordActivity)currentActivity).hideProgressBar();
                        ((ForgotPasswordActivity)currentActivity).onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDisposable.dispose();
                    }
                });
    }

}
