package smartdev.bzzhub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.ApplicantResponse;
import smartdev.bzzhub.repository.model.JobResponse;
import smartdev.bzzhub.ui.adapter.ApplicantsAdapter;
import smartdev.bzzhub.ui.base.BaseActivity;
import smartdev.bzzhub.util.navigation.Arg;

public class ApplicantsJobActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
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

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicants_job);
        ButterKnife.bind(this);
        extractArguments();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getApplicants();
        initViews();
    }

    private void extractArguments(){
        job = (JobResponse.Result)getIntent().getSerializableExtra(Arg.ARG_JOB_DETAIL);
    }

    void getApplicants(){
        BzzApp.getBzHubRepository().getApplicantDetail(job.getUserId()).subscribe(
                new Observer<ApplicantResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(ApplicantResponse applicantResponse) {
                        if (applicantResponse.getStatus() && applicantResponse.getCode() == 200){
                            if (applicantResponse.getResult() != null && !applicantResponse.getResult().isEmpty()){
                                email = applicantResponse.getResult().get(0).getEmail();
                            }
                            recyclerView.setAdapter(new ApplicantsAdapter(ApplicantsJobActivity.this,applicantResponse.getResult()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ApplicantsJobActivity.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                }
        );
    }

    private void initViews(){
        Glide.with(ApplicantsJobActivity.this).load(job.getBanner()).apply(new RequestOptions()
                .error(R.drawable.ic_bzzhub).placeholder(R.drawable.ic_bzzhub)).centerCrop().into(ivBanner);
        tvOverView.setText(job.getDescription());
        tvJob.setText(job.getTitle());
        if (job.getCountry() != null && job.getCity() != null){
            tvAddress.setText(ApplicantsJobActivity.this.getResources().getString(R.string.str_address_value,job.getCountry(),
                    job.getCity()));
        }
    }

    @OnClick(R.id.tv_apply)
    void onApplyClicked(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
        i.putExtra(Intent.EXTRA_SUBJECT, "");
        i.putExtra(Intent.EXTRA_TEXT   , "");
        try {
            startActivity(Intent.createChooser(i, ""));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(currentActivity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.iv_back)
    public void onBackCLicked(){
        finish();
    }
}
