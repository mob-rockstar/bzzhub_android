package smartdev.bzzhub.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.MyPreferenceManager;
import smartdev.bzzhub.repository.model.SimpleResponse;
import smartdev.bzzhub.repository.network.BzHubRepository;
import smartdev.bzzhub.ui.base.BaseActivity;

import butterknife.ButterKnife;
import smartdev.bzzhub.util.Constant;

import static smartdev.bzzhub.repository.PreferenceKey.ARG_USER_LANGUAGE;

public class SettingsActivity extends BaseActivity {

    int currentLanguage;
    @BindView(R.id.layout_language)
    LinearLayout layoutLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initCurrentActivity();

        // If 1 : English, If 2 : Arabic,  Default English
        currentLanguage = MyPreferenceManager.getInstance(getApplicationContext()).getInt(ARG_USER_LANGUAGE,2);
    }

    private void initCurrentActivity(){
        currentActivity = SettingsActivity.this;
        setCurrentActivity();
    }

    @OnClick(R.id.iv_back) void onBackClicked(){
        finish();
    }

    private void showlanguageDialog() {
        Dialog  dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_change_language, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvEnglish= view.findViewById(R.id.tv_english);
        tvEnglish.setOnClickListener(v -> {
            if (currentLanguage != 1){
                if (Constant.getInstance().getLoginID() == 0){
                    changeLocalLanguage(1);
                }else{
                    changeLanguage(1);
                }
            }
            dialog.dismiss();
        });

        TextView tvArabic = view.findViewById(R.id.tv_arabic);
        tvArabic.setOnClickListener(v -> {
            if (currentLanguage != 2){
                if (Constant.getInstance().getLoginID() == 0){
                    changeLocalLanguage(2);
                }else{
                    changeLanguage(2);
                }
            }
            dialog.dismiss();
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);

        dialog.setCancelable(true);
        dialog.show();
    }

    private void changeLanguage(int language){
        int flag = 0;
        if (Constant.getInstance().getLoginFlag() == 1){
            flag = 1;
        }else {
            flag = 0;
        }
        String langCode = "ar";
        if (language == 1) langCode = "en";
        else  langCode = "ar";
        BzzApp.getBzHubRepository().changeLanguage(Constant.getInstance().getLoginID(),flag,langCode)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        if (simpleResponse.getCode() == 200 && simpleResponse.getStatus()){
                            changeLocalLanguage(language);
                        }else {
                            SettingsActivity.this.onError(new Exception(simpleResponse.getMessage()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        SettingsActivity.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    private void changeLocalLanguage(int language){
        MyPreferenceManager.getInstance(getApplicationContext()).put(ARG_USER_LANGUAGE,language);
        restartApp();
    }

    @OnClick(R.id.layout_language) void onLanguageClicked(){
       // if (Constant.getInstance().getLoginType() != null){
            showlanguageDialog();
    //    }
    }

    @OnClick(R.id.layout_term_policy) void onTermAndPolicyClicked(){
        startActivity(new Intent(SettingsActivity.this,TermsConditionActivity.class));

    }

    @Override
    protected void onResume() {
        super.onResume();
        setCurrentActivity();
    }
}
