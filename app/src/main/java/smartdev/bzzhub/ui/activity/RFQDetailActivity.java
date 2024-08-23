package smartdev.bzzhub.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.RFQDetailResponse;
import smartdev.bzzhub.ui.base.BaseActivity;

public class RFQDetailActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.tv_subcategory)
    TextView tvSubcategory;
    @BindView(R.id.tv_product)
    TextView tvProduct;
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.tv_quantity)
    TextView tvQuantity;
    @BindView(R.id.iv_attach)
    ImageView ivAttach;

   int rfqId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_f_q_detail);
        ButterKnife.bind(this);

        extractArguments();
        getRFQDetail();
    }

    private void extractArguments(){
        rfqId = getIntent().getIntExtra("RFQ_ID",0);
    }

    private void getRFQDetail(){
        BzzApp.getBzHubRepository().getRFQDetail(rfqId)
                .subscribe(new Observer<RFQDetailResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(RFQDetailResponse rfqDetailResponse) {
                        if (rfqDetailResponse.getStatus() && rfqDetailResponse.getCode() == 200 && rfqDetailResponse.getResult()!= null){
                            initUI(rfqDetailResponse.getResult());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        RFQDetailActivity.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    private void initUI(RFQDetailResponse.Result rfqDetail){
        tvTitle.setText(rfqDetail.getName());
        tvEmail.setText(rfqDetail.getEmail());
        tvDetail.setText(rfqDetail.getDetails());
        tvQuantity.setText(rfqDetail.getQty());
        tvPhone.setText(rfqDetail.getPhone());
        tvSubcategory.setText(rfqDetail.getSectorName());
        tvCategory.setText(rfqDetail.getCategory());
        if (rfqDetail.getKeywords() != null){
            tvProduct.setText(rfqDetail.getKeywords());
        }
        Glide.with(RFQDetailActivity.this).setDefaultRequestOptions(new RequestOptions().error(R.drawable.ic_placeholder_product_image)).load(rfqDetail.getImage()).centerCrop().into(ivAttach);
    }

    @OnClick(R.id.iv_back) void onBackClicked(){
        finish();
    }
}
