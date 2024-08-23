package smartdev.bzzhub.ui.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.SimpleResponse;
import smartdev.bzzhub.ui.activity.ForgotPasswordActivity;
import smartdev.bzzhub.ui.activity.SignUpActivity;
import smartdev.bzzhub.ui.adapter.BusinesscategoryAdapter;
import smartdev.bzzhub.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class EnterEmailFragment extends BaseFragment {

    private int userType = 0;
    @BindView(R.id.edittext_email)
    EditText editTextEmail;

    private String strEmail = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enter_email, container, false);
        ButterKnife.bind(this, view);

        userType = ((ForgotPasswordActivity)currentActivity).userType;
        return view;
    }

    @OnClick(R.id.layout_next) void onNextClicked(){
        strEmail = editTextEmail.getText().toString().trim();
        if (strEmail.isEmpty()){
            ((ForgotPasswordActivity)currentActivity).showSnackBar(currentActivity.getResources().getString(R.string.str_empty_email),false);
        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()){
            ((ForgotPasswordActivity)currentActivity).showSnackBar(currentActivity.getResources().getString(R.string.str_invalid_email),false);
        }else {
            forgotPassword();
        }
    }

    private void forgotPassword(){
        if (userType == 0){
            forgotPasswordUser();
        }else {
            forgotPasswordCompany();
        }

    }

    private void forgotPasswordUser(){
        ((ForgotPasswordActivity)currentActivity).showProgressBar();
        BzzApp.getBzHubRepository().forgotPasswordUser(strEmail).doOnNext(simpleResponse -> {
            if (simpleResponse.getCode() != 200){
                ((ForgotPasswordActivity)currentActivity).onError(new Exception(simpleResponse.getMessage()
                ));
            }
        })
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        ((ForgotPasswordActivity)currentActivity).hideProgressBar();
                        if (simpleResponse.getStatus() &&  simpleResponse.getCode() == 200){
                            showEmailSentDialog();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        ((ForgotPasswordActivity)currentActivity).hideProgressBar();
                        ((ForgotPasswordActivity)currentActivity).onError(new Exception(e));
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    private void forgotPasswordCompany(){
        ((ForgotPasswordActivity)currentActivity).showProgressBar();
        BzzApp.getBzHubRepository().forgotPasswordCompany(strEmail).doOnNext(simpleResponse -> {
            if (simpleResponse.getCode() != 200){
                ((ForgotPasswordActivity)currentActivity).onError(new Exception(simpleResponse.getMessage()
                ));
            }
        })
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        ((ForgotPasswordActivity)currentActivity).hideProgressBar();
                        if (simpleResponse.getStatus() && simpleResponse.getCode() == 200){
                            showEmailSentDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((ForgotPasswordActivity)currentActivity).hideProgressBar();
                        ((ForgotPasswordActivity
                                )currentActivity).onError(new Exception(e));
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    private void showEmailSentDialog() {
        Dialog dialog = new Dialog(getActivity());
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        final View view = layoutInflater.inflate(R.layout.dialog_email_link_sent, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(v -> {
            ((ForgotPasswordActivity)getActivity()).addFragment(new EnterVerifyCodeFragment(),false,
                    false,R.id.frameLayout,null);
            dialog.dismiss();
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setCancelable(false);
        dialog.show();
    }
}
