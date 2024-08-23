package smartdev.bzzhub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.JobResponse;
import smartdev.bzzhub.ui.base.BaseActivity;
import smartdev.bzzhub.util.navigation.Arg;

public class JobDetailActivity extends BaseActivity {

    JobResponse.Result job;
    @BindView(R.id.iv_banner)
    RoundedImageView ivBanner;
    @BindView(R.id.tv_job)
    TextView tvJob;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_apply)
    TextView tvApply;
    @BindView(R.id.tv_overview)
    TextView tvOverView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        ButterKnife.bind(this);

        extractArguments();
        initViews();
    }

    private void extractArguments(){
        job = (JobResponse.Result)getIntent().getSerializableExtra(Arg.ARG_JOB_DETAIL);
    }

    private void initViews(){
        Glide.with(JobDetailActivity.this).load(job.getBanner()).apply(new RequestOptions()
        .error(R.drawable.ic_bzzhub).placeholder(R.drawable.ic_bzzhub)).centerCrop().into(ivBanner);
        tvOverView.setText(job.getDescription());
        tvJob.setText(job.getTitle());
        if (job.getCountry() == null || job.getCity() == null){
            tvAddress.setVisibility(View.GONE);
        }else {
            tvAddress.setVisibility(View.VISIBLE);
            tvAddress.setText(JobDetailActivity.this.getResources().getString(R.string.str_address_value,job.getCountry(),
                    job.getCity()));
        }

    }

    @OnClick(R.id.iv_back)
    void onBackClicked(){
        finish();
    }

    @OnClick(R.id.tv_apply)
    void onApplyClicked(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{job.getEmail()});
        i.putExtra(Intent.EXTRA_SUBJECT, "");
        i.putExtra(Intent.EXTRA_TEXT   , "");
        try {
            startActivity(Intent.createChooser(i, ""));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(currentActivity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
