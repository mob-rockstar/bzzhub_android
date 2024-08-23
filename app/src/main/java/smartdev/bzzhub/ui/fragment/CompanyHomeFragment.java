package smartdev.bzzhub.ui.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Objects;

import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.CompanyDetailResponse;
import smartdev.bzzhub.repository.model.ProductDetail;
import smartdev.bzzhub.ui.activity.CompanyDetailActivity;
import smartdev.bzzhub.ui.adapter.CategoryAdapterHome;
import smartdev.bzzhub.ui.adapter.ProductAdapterHome;
import smartdev.bzzhub.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CompanyHomeFragment extends BaseFragment implements ProductAdapterHome.SelectedListener{

    private Integer companyId;
    @BindView(R.id.tv_overview)
    TextView tOverView;
    @BindView(R.id.tv_year_established)
    TextView tvYearEstablished;
    @BindView(R.id.tv_website)
    TextView tvWebsite;
    @BindView(R.id.recyclerView_category)
    RecyclerView recyclerViewCaetgory;

    @BindView(R.id.progress_bar)
    ProgressBar profgressBar;

    @BindView(R.id.recyclerView_products)
    RecyclerView recyclerViewProducts;
    ImageView icProductOne, icProductTwo, icProductThree;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_home, container, false);
        ButterKnife.bind(this, view);
        extractArguments();

        if (isVisibleToUser){
            getCompanyDetails();
        }

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (getView() != null && isVisibleToUser){
            getCompanyDetails();
        }
    }

    private void extractArguments(){
        companyId =  ((CompanyDetailActivity)currentActivity).companyId;
    }

    private void getCompanyDetails(){

        BzzApp.getBzHubRepository().getCompanyDetail((companyId))
                .doOnNext(companyDetailResponse -> {
                    if (companyDetailResponse.getCode() != 200)
                        CompanyHomeFragment.this.onError(new Exception(companyDetailResponse.getMessage()));
                })
                .subscribe(new Observer<CompanyDetailResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(CompanyDetailResponse companyDetailResponse) {
                        if (companyDetailResponse.getStatus() && companyDetailResponse.getCode() == 200){
                            ((CompanyDetailActivity)currentActivity).setCompanyProfile(companyDetailResponse.getResult());
                            setUI(companyDetailResponse.getResult());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        CompanyHomeFragment.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    private void setUI(CompanyDetailResponse.CompanyDetail companyDetail){
        tOverView.setText(companyDetail.getDescription());
        tvWebsite.setText((companyDetail.getWebsite() == null || companyDetail.getWebsite().isEmpty()) ? "No Website" : companyDetail.getWebsite());
        tvYearEstablished.setText(companyDetail.getEstablishedYear() == null || companyDetail.getEstablishedYear()  == null  ?"Not Found" :  String.valueOf(companyDetail.getEstablishedYear()));

        if (companyDetail.getIntersts() != null && !companyDetail.getIntersts().isEmpty()){
            recyclerViewCaetgory.setVisibility(View.VISIBLE);
            recyclerViewCaetgory.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL,false));
            recyclerViewCaetgory.setAdapter(new CategoryAdapterHome(currentActivity,companyDetail.getIntersts()));
        }else {
            recyclerViewCaetgory.setVisibility(View.GONE);
        }

        if (companyDetail.getProducts() != null && !companyDetail.getProducts().isEmpty()){
            recyclerViewProducts.setVisibility(View.VISIBLE);
            recyclerViewProducts.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL,false));
            recyclerViewProducts.setAdapter(new ProductAdapterHome(currentActivity,companyDetail.getProducts(),CompanyHomeFragment.this));

        }else {
            recyclerViewProducts.setVisibility(View.GONE);
        }
    }

    private void getProductDettail(Integer productId){

        currentActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        BzzApp.getBzHubRepository().getProductDetail(productId).subscribe(
                new Observer<ProductDetail>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(ProductDetail productDetail) {

                        currentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if (productDetail.getStatus() &&  productDetail.getCode() == 200){
                            showViewProductDetailDialog(productDetail.getResult());
                        }else {
                            CompanyHomeFragment.this.onError(new Exception(productDetail.getMessage()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        currentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        CompanyHomeFragment.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                }
        );
    }

    private void showViewProductDetailDialog(ProductDetail.Result productDetail){
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_product_detail, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        icProductOne = dialog.findViewById(R.id.iv_product_one);
        icProductTwo = dialog.findViewById(R.id.iv_product_two);
        icProductThree = dialog.findViewById(R.id.iv_product_three);

        icProductOne.setVisibility(View.GONE);
        icProductTwo.setVisibility(View.GONE);
        icProductThree.setVisibility(View.GONE);
        LinearLayout layoutEdit = dialog.findViewById(R.id.layout_edit);
        layoutEdit.setVisibility(View.GONE);
        ImageView ivProduct = dialog.findViewById(R.id.iv_product);
        TextView tvProductName = dialog.findViewById(R.id.tv_product_name);
        tvProductName.setText(productDetail.getTitle());
        TextView tvPrice = dialog.findViewById(R.id.tv_product_price);
        tvPrice.setText(productDetail.getPrice());
        TextView tvDescription = dialog.findViewById(R.id.tv_product_description);
        tvDescription.setText(productDetail.getDescription());

        if (productDetail.getImages().size() > 0){
            Glide.with(currentActivity).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_placeholder_product_image).error(R.drawable.ic_placeholder_product_image)).load(productDetail.getImages().get(0).getImage()).centerCrop().into(ivProduct);
            Glide.with(currentActivity).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_placeholder_product_image).error(R.drawable.ic_placeholder_product_image)).load(productDetail.getImages().get(0).getImage()).centerCrop().into(icProductOne);
            icProductOne.setVisibility(View.VISIBLE);
            if (productDetail.getImages().size() > 1){
                Glide.with(currentActivity).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_placeholder_product_image).error(R.drawable.ic_placeholder_product_image)).load(productDetail.getImages().get(1).getImage()).centerCrop().into(icProductTwo); }
                icProductTwo.setVisibility(View.VISIBLE);
            if (productDetail.getImages().size() > 2){
                Glide.with(currentActivity).load(productDetail.getImages().get(2).getImage()).apply(new RequestOptions().placeholder(R.drawable.ic_placeholder_product_image).error(R.drawable.ic_placeholder_product_image)).centerCrop().into(icProductThree);
                icProductThree.setVisibility(View.VISIBLE);
              }
        }


        TextView tvSize = dialog.findViewById(R.id.tv_size);
        TextView tvColor = dialog.findViewById(R.id.tv_color);
        tvColor.setText(getSelectedColor(productDetail.getAttributes().getColors()));
        tvSize.setText(getSelectedSize(productDetail.getAttributes().getSizes()));

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

    @Override
    public void onItemClicked(Integer productId) {
        getProductDettail(productId);
    }


    public String getSelectedSize(List<ProductDetail.Size> sizeList){
        StringBuilder strLanguage = new StringBuilder();
        if (!sizeList.isEmpty()){
            for (int i = 0;i<sizeList.size();i++){
                strLanguage.append(sizeList.get(i).getName()).append(i<sizeList.size()-1 ? "," : "");
            }
        }

        return strLanguage.toString();
    }

    public String getSelectedColor(List<ProductDetail.Color> sizeList){
        StringBuilder strLanguage = new StringBuilder();
        if (!sizeList.isEmpty()){
            for (int i = 0;i<sizeList.size();i++){
                strLanguage.append(sizeList.get(i).getName()).append(i<sizeList.size()-1 ? "," : "");
            }
        }

        return strLanguage.toString();
    }
}
