package smartdev.bzzhub.ui.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.CompanyVerifyCodeResponse;
import smartdev.bzzhub.repository.model.SimpleResponse;
import smartdev.bzzhub.repository.model.UserVerifyCodeResponse;
import smartdev.bzzhub.ui.activity.ForgotPasswordActivity;
import smartdev.bzzhub.ui.activity.SignUpActivity;
import smartdev.bzzhub.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static smartdev.bzzhub.util.navigation.Arg.ARG_COMPANY_ID;
import static smartdev.bzzhub.util.navigation.Arg.ARG_USER_ID;

public class EnterVerifyCodeFragment extends BaseFragment {

    private int userType ;
    String strVerifyCode;
    @BindView(R.id.edittext_code)
    EditText editTextCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enter_verify_code, container, false);
        ButterKnife.bind(this, view);

        userType = ((ForgotPasswordActivity)currentActivity).userType;
        return view;
    }

    @OnClick(R.id.layout_next) void onNextClicked(){
        strVerifyCode = editTextCode.getText().toString().trim();
        if (strVerifyCode.isEmpty()){
            ((ForgotPasswordActivity)currentActivity).showSnackBar(currentActivity.getResources().getString(
                    R.string.str_empty_code
            ),false);
        }else if (strVerifyCode.length() < 4){
            ((ForgotPasswordActivity)currentActivity).showSnackBar(currentActivity.getResources().getString(
                    R.string.str_invalid_verify_code
            ),false);
        }else {
            verifyCode();
        }
    }

    private void verifyCode(){
        if (userType == 0)
            verifyCodeUser();
        else
            verifyCodeCompany();
    }

    private void verifyCodeUser(){
        ((ForgotPasswordActivity)currentActivity).showProgressBar();
        BzzApp.getBzHubRepository().verifyCodeUser(strVerifyCode).doOnNext(simpleResponse -> {
            if (simpleResponse.getCode() != 200){
                ((ForgotPasswordActivity)currentActivity).hideProgressBar();
                ((ForgotPasswordActivity)currentActivity).onError(new Exception(simpleResponse.getMessage()));
            }
        })
                .subscribe(new Observer<UserVerifyCodeResponse>() {
                    Disposable mDisposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(UserVerifyCodeResponse simpleResponse) {
                        ((ForgotPasswordActivity)currentActivity).hideProgressBar();
                        if (simpleResponse.getStatus() &&  simpleResponse.getCode() == 200 && simpleResponse.getResult() != null){
                            Bundle bundle = new Bundle();
                            bundle.putString(ARG_USER_ID,String.valueOf(simpleResponse.getResult()));
                            ((ForgotPasswordActivity) currentActivity).addFragment(new ResetPasswordFragment(),false,true,((ForgotPasswordActivity) currentActivity).rootLayout,bundle);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((ForgotPasswordActivity)currentActivity).hideProgressBar();
                        ((ForgotPasswordActivity)currentActivity).onError(new Exception(e));
                    }

                    @Override
                    public void onComplete() {
                        mDisposable.dispose();
                    }
                });
    }

    private void verifyCodeCompany(){
        ((ForgotPasswordActivity)currentActivity).showProgressBar();
        BzzApp.getBzHubRepository().verifyCodeCompany(strVerifyCode).doOnNext(simpleResponse -> {
            if (simpleResponse.getCode()!= 200) {
                ((ForgotPasswordActivity)currentActivity).hideProgressBar();
                ((ForgotPasswordActivity)currentActivity).onError(new Exception(simpleResponse.getMessage()));
            }

        })
                .subscribe(new Observer<CompanyVerifyCodeResponse>() {
                    Disposable mDisposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(CompanyVerifyCodeResponse simpleResponse) {
                        ((ForgotPasswordActivity)currentActivity).hideProgressBar();
                        if (simpleResponse.getStatus() && simpleResponse.getCode() == 200){
                            Bundle bundle = new Bundle();
                            bundle.putString(ARG_COMPANY_ID,String.valueOf(simpleResponse.getResult()));
                            ((ForgotPasswordActivity) currentActivity).addFragment(new ResetPasswordFragment(),false,true,((ForgotPasswordActivity) currentActivity).rootLayout,bundle);

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((ForgotPasswordActivity)currentActivity).hideProgressBar();
                        ((ForgotPasswordActivity)currentActivity).onError(new Exception(e));
                    }

                    @Override
                    public void onComplete() {
                        mDisposable.dispose();
                    }
                });
    }

}
