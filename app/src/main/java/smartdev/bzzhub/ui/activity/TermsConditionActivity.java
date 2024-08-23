package smartdev.bzzhub.ui.activity;

import android.os.Bundle;

import butterknife.OnClick;
import smartdev.bzzhub.R;
import smartdev.bzzhub.ui.base.BaseActivity;

import butterknife.ButterKnife;

public class TermsConditionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_back) void onBackClicked(){
        finish();
    }

    @OnClick(R.id.tv_agree) void onAgreeClicked(){
        finish();
    }

}
