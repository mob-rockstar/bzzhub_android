package smartdev.bzzhub.ui.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;
import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.ColorResponse;
import smartdev.bzzhub.repository.model.CompanyDetailResponse;
import smartdev.bzzhub.repository.model.InterestResponse;
import smartdev.bzzhub.repository.model.ProductDetail;
import smartdev.bzzhub.repository.model.SelectorResponse;
import smartdev.bzzhub.repository.model.SimpleResponse;
import smartdev.bzzhub.repository.model.Size;
import smartdev.bzzhub.repository.model.SizeResponse;
import smartdev.bzzhub.ui.activity.CompanyProfileActivity;
import smartdev.bzzhub.ui.activity.UserProfileActivity;
import smartdev.bzzhub.ui.adapter.CategoryAdapterCompanyProfile;
import smartdev.bzzhub.ui.adapter.ChooseSectorAdapter;
import smartdev.bzzhub.ui.adapter.ColorAdapter;
import smartdev.bzzhub.ui.adapter.CompanyProfileProductAdapter;
import smartdev.bzzhub.ui.adapter.ProductImageListAdapter;
import smartdev.bzzhub.ui.adapter.SelectedColorAdapter;
import smartdev.bzzhub.ui.adapter.SelectedSizeAdapter;
import smartdev.bzzhub.ui.adapter.SizeAdapter;
import smartdev.bzzhub.ui.base.BaseFragment;
import smartdev.bzzhub.util.Constant;

import static android.app.Activity.RESULT_OK;

public class CompanyProfileFragment extends BaseFragment implements CompanyProfileProductAdapter.SelectedListener,
CategoryAdapterCompanyProfile.SelectedListener,ProductImageListAdapter.SelectedListener,ColorAdapter.SelectedListener
,SizeAdapter.SelectedListener{

    @BindView(R.id.tv_overview)
    TextView tvOverView;
    @BindView(R.id.tv_year_established)
    TextView tvYearEstablished;
    @BindView(R.id.tv_website)
    TextView tvWebsite;
    @BindView(R.id.recyclerView_products)
    RecyclerView recyclerViewProducts;
    @BindView(R.id.recyclerView_category)
    RecyclerView recyclerViewCategory;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.tv_manufacturer)
    TextView tvManufacturer;
    @BindView(R.id.tv_trader)
    TextView tvTrader;
    @BindView(R.id.tv_service_company)
    TextView tvServiceCompany;

    private Dialog dialog;

    private List<SelectorResponse.Result> interests = new ArrayList<>();
    private  CompanyDetailResponse.CompanyDetail companyDetail;
    private  ChooseSectorAdapter chooseSectorAdapter;
    private  List<CompanyDetailResponse.Interst> selectedInterest = new ArrayList<>();
    private CompanyProfileProductAdapter companyProfileProductAdapter;

    private ArrayList<String> imageArray = new ArrayList<>();
    private List<ColorResponse.Result> colors = new ArrayList<>();
    private List<ColorResponse.Result> selectedColors;
    private List<Size> sizes;
    private SelectedColorAdapter selectedColorAdapter;
    private SizeAdapter sizeAdapter;

    private Dialog productDialog;
    private RecyclerView recyclerViewProductColors,recyclerViewProductSize,recyclerViewImages,recyclerViewAllColors, recyclerViewSelectedColors,
            recyclerViewSizeList;
    ProductImageListAdapter productImageListAdapter;

    SelectedSizeAdapter selectedSizeAdapter;
    SelectedColorAdapter productColorAdapter;
    String strJobTitle = "";
    String strPrice = "";
    String strDescrption = "";
    String selectedColorString = "",selectedSizeString = "";
    ColorAdapter allColorAdapter;
    // class variables
    private static final int REQUEST_CODE = 123;
    private static final int REQUEST_CODE_ADD_SINGLE_IMAGE = 125;
    private static final int REQUEST_CODE_EDIT_SINGLE_IMAGE = 127;
    List<String> results = new ArrayList<>();
    Integer productId = 0;

    ArrayList<String> singleImageResult = new ArrayList<>();
    int currentImagePositino = 0;
    Integer currentImageId = 0;
    ImageView ivProductOne,ivProductTwo,ivProductThree;
    ProgressBar progressBarDialog;

    int position = 0;

    private EasyImage easyImage;
    private int imageRequestCode = 1001;

    int manufacturer = 0;
    int trader = 0;
    int serviceCompany =0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_profile, container, false);
        ButterKnife.bind(this, view);
        initRecyclerViews();

        // Define Easy Image
        easyImage =
                new  EasyImage.Builder(getActivity()).setCopyImagesToPublicGalleryFolder(false)
                        .allowMultiple(false)
                        .build();

        if (isVisibleToUser){
            getColors();
            getSizes();
            getSelectors();
            getCompanyProfile();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (getView() != null && isVisibleToUser){
            getColors();
            getSizes();
            getSelectors();
            getCompanyProfile();
        }
    }

    private void initSize(){
        sizes = new ArrayList<>();
        sizes.add(new Size(currentActivity.getResources().getString(R.string.str_size_small)));
        sizes.add(new Size(currentActivity.getResources().getString(R.string.str_size_medium)));
        sizes.add(new Size(currentActivity.getResources().getString(R.string.str_size_large)));
        sizes.add(new Size(currentActivity.getResources().getString(R.string.str_size_xlarge)));

    }

    private void initRecyclerViews(){
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(currentActivity,RecyclerView.HORIZONTAL,false));
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(currentActivity,RecyclerView.HORIZONTAL,false));
    }

    private void getCompanyProfile(){
        progressBar.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().getCompanyDetail((Constant.getInstance().getCompanyProfile().getCompanyId()))
                .doOnNext(companyDetailResponse -> {
                    if (companyDetailResponse.getCode() != 200)
                        CompanyProfileFragment.this.onError(new Exception(companyDetailResponse.getMessage()));
                }).subscribe(new Observer<CompanyDetailResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(CompanyDetailResponse companyDetailResponse) {
                        progressBar.setVisibility(View.GONE);
                        if (companyDetailResponse.getStatus() && companyDetailResponse.getCode() == 200){
                            companyDetail = companyDetailResponse.getResult();
                            if (companyDetail != null){
                                selectedInterest = companyDetail.getIntersts();
                                ((CompanyProfileActivity)currentActivity).setCompanyProfile(companyDetailResponse.getResult());
                                setUI(companyDetailResponse.getResult());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        CompanyProfileFragment.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    private void setUI(CompanyDetailResponse.CompanyDetail companyDetail){
        tvOverView.setText(companyDetail.getDescription() != null && !companyDetail.getDescription().isEmpty() ? companyDetail.getDescription()
                :currentActivity.getResources().getString(R.string.str_no_data));
        tvWebsite.setText((companyDetail.getWebsite() == null || companyDetail.getWebsite().isEmpty()) ? "No Website" : companyDetail.getWebsite());
        tvYearEstablished.setText(companyDetail.getEstablishedYear() == null || companyDetail.getEstablishedYear() == null  ?"Not Found" : String.valueOf(companyDetail.getEstablishedYear()));

        if (companyDetail.getIntersts() != null){
            recyclerViewCategory.setAdapter(new CategoryAdapterCompanyProfile(currentActivity,companyDetail.getIntersts(),this::onRemoveClicked));
        }

        companyProfileProductAdapter = new CompanyProfileProductAdapter(currentActivity,companyDetail.getProducts() != null ? companyDetail.getProducts() : new ArrayList<>(),CompanyProfileFragment.this,false);
        recyclerViewProducts.setAdapter(companyProfileProductAdapter);

        if (companyDetail.getManufacturer() == 1){
            tvManufacturer.setVisibility(View.VISIBLE);
        }else{
            tvManufacturer.setVisibility(View.GONE);
        }

        if (companyDetail.getTrader() == 1){
            tvTrader.setVisibility(View.VISIBLE);
        }else{
            tvTrader.setVisibility(View.GONE);
        }

        if (companyDetail.getServiceCompany() == 1){
            tvServiceCompany.setVisibility(View.VISIBLE);
        }else{
            tvServiceCompany.setVisibility(View.GONE);
        }

    }

    @Override
    public void onAddClicked() {
        showAddProducts();
    }
    View progressView;
    private void showAddProducts(){
        productDialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_add_product_company, null);
        productDialog.getWindow();

        productDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        productDialog.setContentView(view);
        Objects.requireNonNull(productDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        progressView = view.findViewById(R.id.layout_progress);

        recyclerViewProductColors = productDialog.findViewById(R.id.recyclerview_product_colors);
        recyclerViewProductSize = productDialog.findViewById(R.id.recyclerView_product_sizes);
        recyclerViewImages = productDialog.findViewById(R.id.recyclerView_images);
        recyclerViewAllColors = productDialog.findViewById(R.id.recyclerView_colors);
        recyclerViewSelectedColors = productDialog.findViewById(R.id.recyclerView_selected_colors);
        recyclerViewSizeList = productDialog.findViewById(R.id.recyclerView_size_list);
        initProductImages();
        recyclerViewAllColors.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL,false));
        allColorAdapter = new ColorAdapter(currentActivity,colors,this);
        recyclerViewAllColors.setAdapter(allColorAdapter);
        recyclerViewImages.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL,false));
        productImageListAdapter = new ProductImageListAdapter(currentActivity,imageArray,this);
        recyclerViewImages.setAdapter(productImageListAdapter);

        selectedColorAdapter = new SelectedColorAdapter(currentActivity,new ArrayList<>());
        recyclerViewSelectedColors.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewSelectedColors.setAdapter(selectedColorAdapter);

        recyclerViewProductColors.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewProductSize.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewSizeList.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewSizeList.setAdapter(sizeAdapter);
        recyclerViewSizeList = productDialog.findViewById(R.id.recyclerView_size_list);

        TextView tvPost = productDialog.findViewById(R.id.tv_post);

        recyclerViewProductSize.setAdapter(selectedSizeAdapter);
        recyclerViewProductColors.setAdapter(productColorAdapter);

        EditText editTextJobTitle = productDialog.findViewById(R.id.edittext_job_title);
        EditText editTextPrice = productDialog.findViewById(R.id.edittext_price);
        EditText edittextDescription  = productDialog.findViewById(R.id.edittext_description);

        TextView tvAddAttribute = productDialog.findViewById(R.id.tv_add_attribute);
        TextView tvAddSizeAttribute = productDialog.findViewById(R.id.add_size_attribute);

        tvPost.setOnClickListener(v -> {
            strJobTitle = editTextJobTitle.getText().toString().trim();
            strPrice = editTextPrice.getText().toString().trim();
            strDescrption = edittextDescription.getText().toString().trim();
            selectedColorString = productColorAdapter.getSelectedSize();
            selectedSizeString = selectedSizeAdapter.getSelectedSize();
            progressView.setVisibility(View.VISIBLE);
            onAddProduct();
        });


        tvAddAttribute.setOnClickListener(v -> {
            showAddAttributs(productDialog);
        });

        tvAddSizeAttribute.setOnClickListener(v -> {
            showAddAttributs(productDialog);
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = productDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        productDialog.setCanceledOnTouchOutside(true);

        productDialog.setCancelable(true);
        productDialog.show();
    }

    private void showImageArrayPicker(int position){
        this.position = position;
        RxPermissions permissions = new RxPermissions(this);
        Disposable subscribe = permissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    openImagePicker(REQUEST_CODE);
                  /*  Intent intent = new Intent(currentActivity, ImagesSelectorActivity.class);
                    intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 3);
                    intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
                    intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
                    intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
                    startActivityForResult(intent, REQUEST_CODE);*/
                });
    }

    private void showAddAttributs(Dialog dialog){
        dialog.findViewById(R.id.layout_attributes).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.layout_product).setVisibility(View.GONE);

        dialog.findViewById(R.id.tv_save_attribute).setOnClickListener(v -> {
            dialog.findViewById(R.id.layout_attributes).setVisibility(View.GONE);
            dialog.findViewById(R.id.layout_product).setVisibility(View.VISIBLE);
            selectedSizeAdapter.setData(sizeAdapter.getSelectedSize());
            productColorAdapter.setColors(allColorAdapter.getSelectedSize());
        });
    }

    @OnClick(R.id.tv_add_interest) void onAddInterest(){
        showUpdateInterests();
    }

    private void showUpdateInterests(){
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_interest, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvTitle = dialog.findViewById(R.id.tv_title);
        tvTitle.setText(currentActivity.getResources().getString(R.string.str_select_your_sector));

        EditText editText = dialog.findViewById(R.id.tv_find_company);
        editText.setHint(currentActivity.getResources().getString(R.string.str_find_your_sector));
        RecyclerView recyclerInterest = dialog.findViewById(R.id.recycler_interest_select);
        recyclerInterest.setNestedScrollingEnabled(false);
        recyclerInterest.setHasFixedSize(true);
        recyclerInterest.setLayoutManager(new GridLayoutManager(currentActivity,3));
        chooseSectorAdapter = new ChooseSectorAdapter(currentActivity,cleanInterests());
        recyclerInterest.setAdapter(chooseSectorAdapter);
        TextView tvSave = dialog.findViewById(R.id.tv_save);
        tvSave.setOnClickListener(v -> {
            if (chooseSectorAdapter.getIdList().isEmpty()){
                ((CompanyProfileActivity) currentActivity).onError(new Exception(currentActivity.getResources().getString(R.string.str_select_one_id)));
            }else {
                updateInterests(chooseSectorAdapter.getIdList());
                dialog.dismiss();
                chooseSectorAdapter = null;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                chooseSectorAdapter.filterInterest(s.toString());
            }
        });

        LinearLayout layoutRoot = dialog.findViewById(R.id.layout_root);
        layoutRoot.setOnClickListener(v -> {
            dialog.dismiss();
            chooseSectorAdapter = null;
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

    private List<Integer> idList(){
        List<Integer> idList = new ArrayList<>();
        if (!selectedInterest.isEmpty()){
            for (int i = 0;i<selectedInterest.size();i++){
                idList.add(selectedInterest.get(i).getSubID());
            }
        }
        return idList;
     }

    private List<SelectorResponse.Result> cleanInterests(){
        List<Integer> selectedListInterest =idList();
        List<SelectorResponse.Result> cleanedList = new ArrayList<>();
        for (int i = 0;i<interests.size();i++){
            if (!selectedListInterest.contains(interests.get(i).getSubId())){
                cleanedList.add(interests.get(i));
            }
        }
        for (SelectorResponse.Result interest : cleanedList)
            interest.setSelected(false);
        return cleanedList;
    }

    private void updateInterests(String idList){
        BzzApp.getBzHubRepository().updateCompanyInterests(Constant.getInstance().getCompanyProfile().getCompanyId(),
                idList).subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
            @Override
            public void onSubscribe(Disposable d) {
                    mDispose = d;
            }

            @Override
            public void onNext(SimpleResponse simpleResponse) {
                getCompanyProfile();
            }

            @Override
            public void onError(Throwable e) {
                ((CompanyProfileActivity)currentActivity).onError(e);
            }

            @Override
            public void onComplete() {
                mDispose.dispose();;
            }
        });
    }

/*    private void getAllInterest(){
        BzzApp.getBzHubRepository().getInterestList().subscribe(
                new Observer<InterestResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(InterestResponse interestResponse) {
                        if (interestResponse.getStatus() && interestResponse.getCode() == 200 && interestResponse.getResult() != null){
                            interests = interestResponse.getResult();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((CompanyProfileActivity)currentActivity).onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                }
        );
    }*/

    @Override
    public void onRemoveClicked(Integer id) {
        showRemoveCategoryDialog(id);

    }

    private void removeInterest(Integer id){
        progressBar.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().removeCompanyInterest(id)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        progressBar.setVisibility(View.GONE);
                        getCompanyProfile();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        ((CompanyProfileActivity) currentActivity).onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    @Override
    public void onRemoveProductClicked(Integer id) {
            showRemoveProductDialog(id);
    }

    private void removeProduct(Integer id){
        BzzApp.getBzHubRepository().removeProduct(id)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        getCompanyProfile();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((UserProfileActivity)currentActivity).onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        easyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                if (imageRequestCode == REQUEST_CODE){
                    productImageListAdapter.setImage(imageFiles[0].getFile().getAbsolutePath(),position);
                    initProductImages();
                }    else if (imageRequestCode == REQUEST_CODE_ADD_SINGLE_IMAGE){
                    switch (currentImagePositino){
                        case 0:
                            Glide.with(currentActivity).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_placeholder_product_image).error(R.drawable.ic_placeholder_product_image)).load(imageFiles[0].getFile().getAbsolutePath()).centerCrop().into(ivProductOne);
                            break;
                        case  1:
                            Glide.with(currentActivity).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_placeholder_product_image).error(R.drawable.ic_placeholder_product_image)).load(imageFiles[0].getFile().getAbsolutePath()).centerCrop().into(ivProductTwo);
                            break;
                        case  2:
                            Glide.with(currentActivity).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_placeholder_product_image).error(R.drawable.ic_placeholder_product_image)).load(imageFiles[0].getFile().getAbsolutePath()).centerCrop().into(ivProductThree);
                            break;
                    }
                    addProductImage(new File(imageFiles[0].getFile().getAbsolutePath()));
                }else if (imageRequestCode == REQUEST_CODE_EDIT_SINGLE_IMAGE ){
                    switch (currentImagePositino){
                        case 0:
                            Glide.with(currentActivity).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_placeholder_product_image).error(R.drawable.ic_placeholder_product_image)).load(imageFiles[0].getFile().getAbsolutePath()).centerCrop().into(ivProductOne);
                            break;
                        case  1:
                            Glide.with(currentActivity).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_placeholder_product_image).error(R.drawable.ic_placeholder_product_image)).load(imageFiles[0].getFile().getAbsolutePath()).centerCrop().into(ivProductTwo);
                            break;
                        case  2:
                            Glide.with(currentActivity).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_placeholder_product_image).error(R.drawable.ic_placeholder_product_image)).load(imageFiles[0].getFile().getAbsolutePath()).centerCrop().into(ivProductThree);
                            break;
                    }
                    updateProductImage(new File(imageFiles[0].getFile().getAbsolutePath()));

                }
            }


            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                //Some error handling
                error.printStackTrace();
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {
                //Not necessary to remove any files manually anymore
            }
        });


    }

    @Override
    public void onSelected(int position) {
        showImageArrayPicker(position);
    }

    private void initProductImages(){
        imageArray = new ArrayList<>(3);

        if (productImageListAdapter != null){
            results = productImageListAdapter.getImageList();
        }

        if (!results.isEmpty()){
            imageArray.addAll(results);
        }

        if (imageArray.size() != 3){
            for (int i = 0; i< (5-imageArray.size()); i++){
                imageArray.add("");
            }
        }
    }

    private void getColors(){
        BzzApp.getBzHubRepository().getColors().subscribe(
                new Observer<ColorResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(ColorResponse colorResponse) {
                        if (colorResponse.getResult() != null){
                            colors = colorResponse.getResult();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((CompanyProfileActivity)currentActivity).onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                }
        );
    }

    @Override
    public void onColorSelected(ColorResponse.Result color) {
        if (selectedColorAdapter.getColors() != null){
           if (selectedColorAdapter.getColors().contains(color)){
                selectedColorAdapter.removeColor(color);
            }else {
               selectedColorAdapter.addColor(color);
           }
        }
    }

    public void cleanColors(){
        if ( colors!= null && !colors.isEmpty()){
            for (int i = 0;i<colors.size();i++){
                colors.get(i).setSelected(false);
            }
        }
    }

    public void cleanSizes(){
        if (sizes!= null && !sizes.isEmpty()){
            for (int i = 0;i<sizes.size();i++){
                sizes.get(i).setSelected(false);
            }
        }
    }

    public void setColorLists(List<ProductDetail.Color> productColors){
        if (productColors != null && !productColors.isEmpty()){
            for (int i = 0; i<colors.size(); i++){
                for (int j = 0 ; j<productColors.size(); j++){
                    if (colors.get(i).getLangid().equals(productColors.get(j).getChartId())){
                        colors.get(i).setSelected(true);
                    }
                }
            }
        }
    }

    public void setSizeLists(List<ProductDetail.Size> productSizeLists){
        if(productSizeLists != null && !productSizeLists.isEmpty()){
            for (int i = 0; i<sizes.size(); i++){
                for (int j = 0 ; j<productSizeLists.size(); j++){
                    if (sizes.get(i).getId().equals(productSizeLists.get(j).getChartId())){
                        sizes.get(i).setSelected(true);
                    }
                }
            }
        }
    }

    @Override
    public void onSizeSelected(Size size) {

    }

    private void onAddProduct(){
        if (results != null && !results.isEmpty()){
            for (int i = 0;i<results.size();i++){
                Log.d("ImageURLPage", "url:" + results.get(i));
            }
        }

        if (strJobTitle.isEmpty() || strPrice.isEmpty() || strDescrption.isEmpty() || selectedSizeString == null ||
        selectedSizeString.isEmpty() || selectedColorString == null || selectedColorString.isEmpty()
        || results.isEmpty()){
            progressView.setVisibility(View.GONE);
            Toast.makeText(currentActivity,currentActivity.getResources().getString(R.string.str_make_sure_valid_field),Toast.LENGTH_LONG).show();
        }else {
            progressBar.setVisibility(View.VISIBLE);
            BzzApp.getBzHubRepository().addProductImage(String.valueOf(Constant.getInstance().getCompanyProfile().getCompanyId()),strJobTitle,strPrice,strDescrption,
                    selectedColorString,selectedSizeString,results).subscribe(
                    new Observer<SimpleResponse>() {
                        Disposable mDispose;
                        @Override
                        public void onSubscribe(Disposable d) {
                            mDispose = d;
                        }

                        @Override
                        public void onNext(SimpleResponse simpleResponse) {
                            progressView.setVisibility(View.GONE);
                            if (simpleResponse.getCode() == 200){
                                results = new ArrayList<>();
                                if (productDialog != null){
                                    productDialog.dismiss();
                                }
                                getCompanyProfile();
                            }
                            selectedColorString = "";
                            selectedSizeString = "";

                            ((CompanyProfileActivity)currentActivity).showSnackBar(simpleResponse.getMessage(),simpleResponse.getCode() == 200);

                        }

                        @Override
                        public void onError(Throwable e) {
                            progressView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onComplete() {
                            mDispose.dispose();
                        }
                    }
            );
        }
    }

    private void getSizes(){
        BzzApp.getBzHubRepository().getSizeList().subscribe(
                new Observer<SizeResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose =d;
                    }

                    @Override
                    public void onNext(SizeResponse sizeResponse) {
                        if (sizeResponse.getStatus() && sizeResponse.getCode() == 200){
                            sizes = sizeResponse.getResult();
                            sizeAdapter = new SizeAdapter(currentActivity,sizes,CompanyProfileFragment.this);
                            selectedSizeAdapter = new SelectedSizeAdapter(currentActivity,new ArrayList<>());
                            productColorAdapter = new SelectedColorAdapter(currentActivity,new ArrayList<>());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                }
        );
    }

    private void showEditProductDialog(ProductDetail.Result productDetail){
        productDialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_edit_product_company, null);
        productDialog.getWindow();

        productDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        productDialog.setContentView(view);
        Objects.requireNonNull(productDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        progressBarDialog = productDialog.findViewById(R.id.progress_bar_dialog);
        recyclerViewProductColors = productDialog.findViewById(R.id.recyclerview_product_colors);
        recyclerViewProductSize = productDialog.findViewById(R.id.recyclerView_product_sizes);
        recyclerViewAllColors = productDialog.findViewById(R.id.recyclerView_colors);
        recyclerViewSelectedColors = productDialog.findViewById(R.id.recyclerView_selected_colors);
        recyclerViewSizeList = productDialog.findViewById(R.id.recyclerView_size_list);
        initProductImages();

        cleanColors(); cleanSizes();
        setColorLists(productDetail.getAttributes().getColors());
        setSizeLists(productDetail.getAttributes().getSizes());
        allColorAdapter = new ColorAdapter(currentActivity,colors,CompanyProfileFragment.this);
        sizeAdapter = new SizeAdapter(currentActivity,sizes,CompanyProfileFragment.this);
        recyclerViewAllColors.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL,false));
        allColorAdapter = new ColorAdapter(currentActivity,colors,this);
        recyclerViewAllColors.setAdapter(allColorAdapter);

        selectedColorAdapter = new SelectedColorAdapter(currentActivity,allColorAdapter.getSelectedSize());
        recyclerViewSelectedColors.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewSelectedColors.setAdapter(selectedColorAdapter);
        productColorAdapter = new SelectedColorAdapter(currentActivity,allColorAdapter.getSelectedSize());

        recyclerViewProductColors.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewProductSize.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewSizeList.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewSizeList.setAdapter(sizeAdapter);
        recyclerViewSizeList = productDialog.findViewById(R.id.recyclerView_size_list);

        TextView tvPost = productDialog.findViewById(R.id.tv_post);

        selectedSizeAdapter = new SelectedSizeAdapter(currentActivity,sizeAdapter.getSelectedSize());

        recyclerViewProductSize.setAdapter(selectedSizeAdapter);
        recyclerViewProductColors.setAdapter(productColorAdapter);

        EditText editTextJobTitle = productDialog.findViewById(R.id.edittext_job_title);
        EditText editTextPrice = productDialog.findViewById(R.id.edittext_price);
        EditText edittextDescription  = productDialog.findViewById(R.id.edittext_description);

        editTextJobTitle.setText(productDetail.getTitle());
        edittextDescription.setText(productDetail.getDescription());
        editTextPrice.setText(productDetail.getPrice());

        TextView tvAddAttribute = productDialog.findViewById(R.id.tv_add_attribute);
        TextView tvAddSizeAttribute = productDialog.findViewById(R.id.add_size_attribute);

        productId = productDetail.getProductId();

        RelativeLayout layoutEditProductOne  = productDialog.findViewById(R.id.layout_edit_product_one);
        RelativeLayout layoutEditProductTwo = productDialog.findViewById(R.id.layout_edit_photo_two);
        RelativeLayout layoutEditProductThree = productDialog.findViewById(R.id.layout_edit_photo_three);

         ivProductOne = productDialog.findViewById(R.id.iv_product_one);
         ivProductTwo = productDialog.findViewById(R.id.iv_product_two);
         ivProductThree = productDialog.findViewById(R.id.iv_product_three);

        if (productDetail.getImages() != null){
            if (productDetail.getImages().size() == 0){
                ivProductOne.setOnClickListener(v -> {
                    currentImagePositino = 0;
                    openImagePicker(REQUEST_CODE_ADD_SINGLE_IMAGE);
                //    openSigleImage(REQUEST_CODE_ADD_SINGLE_IMAGE);
                });
            }
            if (productDetail.getImages().size() > 0){
                if (productDetail.getImages().size() == 1){
                    ivProductTwo.setOnClickListener(v -> {
                        currentImagePositino = 1;
                        openImagePicker(REQUEST_CODE_ADD_SINGLE_IMAGE);
                  //      openSigleImage(REQUEST_CODE_ADD_SINGLE_IMAGE);
                    });

                    ivProductThree.setOnClickListener(v -> {
                        currentImagePositino = 2;
                        openImagePicker(REQUEST_CODE_ADD_SINGLE_IMAGE);
             //           openSigleImage(REQUEST_CODE_ADD_SINGLE_IMAGE);
                    });
                }
                layoutEditProductOne.setOnClickListener(v -> {
                    currentImagePositino = 0;
                    currentImageId = productDetail.getImages().get(0).getImageId();
                    openImagePicker(REQUEST_CODE_EDIT_SINGLE_IMAGE);
                });
                layoutEditProductOne.setVisibility(View.VISIBLE);
                Glide.with(currentActivity).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_placeholder_product_image).error(R.drawable.ic_placeholder_product_image)).load(productDetail.getImages().get(0).getImage()).centerCrop().into(ivProductOne);
            }if (productDetail.getImages().size() > 1){
                if (productDetail.getImages().size() == 2){
                    ivProductThree.setOnClickListener(v -> {
                        currentImagePositino = 2;
                        openImagePicker(REQUEST_CODE_ADD_SINGLE_IMAGE);
                    });
                }
                layoutEditProductTwo.setOnClickListener(v -> {
                    currentImagePositino =1;
                    currentImageId = productDetail.getImages().get(1).getImageId();
                    openImagePicker(REQUEST_CODE_EDIT_SINGLE_IMAGE);
                });
                layoutEditProductTwo.setVisibility(View.VISIBLE);
                Glide.with(currentActivity).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_placeholder_product_image).error(R.drawable.ic_placeholder_product_image)).load(productDetail.getImages().get(1).getImage()).centerCrop().into(ivProductTwo);
            }
            if (productDetail.getImages().size() > 2){
                layoutEditProductThree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentImagePositino = 2;
                        currentImageId = productDetail.getImages().get(2).getImageId();
                        openImagePicker(REQUEST_CODE_EDIT_SINGLE_IMAGE);
                    }
                });

                layoutEditProductThree.setVisibility(View.VISIBLE);
                Glide.with(currentActivity).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_placeholder_product_image).error(R.drawable.ic_placeholder_product_image)).load(productDetail.getImages().get(2).getImage()).centerCrop().into(ivProductThree);
            }
        }

        tvPost.setOnClickListener(v -> {
            strJobTitle = editTextJobTitle.getText().toString().trim();
            strPrice = editTextPrice.getText().toString().trim();
            strDescrption = edittextDescription.getText().toString().trim();
            selectedColorString = productColorAdapter.getSelectedSize();
            selectedSizeString = selectedSizeAdapter.getSelectedSize();
            updateProduct(productDetail.getProductId());
        });

        tvAddAttribute.setOnClickListener(v -> {
            showEditAttributes(productDialog);
        });

        tvAddSizeAttribute.setOnClickListener(v -> {
            showEditAttributes(productDialog);
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = productDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        productDialog.setCanceledOnTouchOutside(true);

        productDialog.setCancelable(true);
        productDialog.show();
    }

    ImageView icProductOne, icProductTwo, icProductThree;

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

        LinearLayout layoutDetail = dialog.findViewById(R.id.layout_detail);
        LinearLayout layoutEdit = dialog.findViewById(R.id.layout_edit);
        layoutEdit.setOnClickListener(v -> {
            showEditProductDialog(productDetail);
            dialog.dismiss();
        });
        ImageView ivProduct = dialog.findViewById(R.id.iv_product);
        TextView tvProductName = dialog.findViewById(R.id.tv_product_name);
        tvProductName.setText(productDetail.getTitle());
        TextView tvPrice = dialog.findViewById(R.id.tv_product_price);
        tvPrice.setText(productDetail.getPrice());
        TextView tvDescription = dialog.findViewById(R.id.tv_product_description);
        tvDescription.setText(productDetail.getDescription());

        TextView tvSize = dialog.findViewById(R.id.tv_size);
        TextView tvColor = dialog.findViewById(R.id.tv_color);
        tvColor.setText(getSelectedColor(productDetail.getAttributes().getColors()));
        tvSize.setText(getSelectedSize(productDetail.getAttributes().getSizes()));

        if (productDetail.getImages().size() > 0){
            Glide.with(currentActivity).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_placeholder_product_image).error(R.drawable.ic_placeholder_product_image)).load(productDetail.getImages().get(0).getImage()).centerCrop().into(ivProduct);

            Glide.with(currentActivity).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_placeholder_product_image).error(R.drawable.ic_placeholder_product_image)).load(productDetail.getImages().get(0).getImage()).centerCrop().into(icProductOne);
            if (productDetail.getImages().size() > 1){
            Glide.with(currentActivity).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_placeholder_product_image).error(R.drawable.ic_placeholder_product_image)).load(productDetail.getImages().get(1).getImage()).centerCrop().into(icProductTwo); }
        if (productDetail.getImages().size() > 2){
            Glide.with(currentActivity).load(productDetail.getImages().get(2).getImage()).apply(new RequestOptions().placeholder(R.drawable.ic_placeholder_product_image).error(R.drawable.ic_placeholder_product_image)).centerCrop().into(icProductThree);
        }
    }

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

    private void addProductImage(File file){
        progressBarDialog.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().addCompanyProductImage(String.valueOf(productId),file)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        progressBarDialog.setVisibility(View.GONE);
                        getCompanyProfile();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBarDialog.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    private void updateProductImage(File file){
        progressBarDialog.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().editCompanyProductImage(String.valueOf(currentImageId),file)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        progressBarDialog.setVisibility(View.GONE);
                        getCompanyProfile();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBarDialog.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    private void showEditAttributes(Dialog dialog){
        dialog.findViewById(R.id.layout_attributes).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.layout_product).setVisibility(View.GONE);

        dialog.findViewById(R.id.tv_save_attribute).setOnClickListener(v -> {
            dialog.findViewById(R.id.layout_attributes).setVisibility(View.GONE);
            dialog.findViewById(R.id.layout_product).setVisibility(View.VISIBLE);
            selectedSizeAdapter.setData(sizeAdapter.getSelectedSize());
            productColorAdapter.setColors(allColorAdapter.getSelectedSize());
        });
    }

    @Override
    public void onItemClicked(Integer productId) {
        getProductDettail(productId);
    }

    private void getProductDettail(Integer productId){
        progressBar.setVisibility(View.VISIBLE);
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
                        progressBar.setVisibility(View.GONE);
                        currentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if (productDetail.getStatus() &&  productDetail.getCode() == 200){
                            showViewProductDetailDialog(productDetail.getResult());
                        }else {
                            CompanyProfileFragment.this.onError(new Exception(productDetail.getMessage()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        currentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        CompanyProfileFragment.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                }
        );
    }

    private void updateProduct(Integer productId){

        if (strJobTitle.isEmpty() || strPrice.isEmpty() || strDescrption.isEmpty() || selectedSizeString == null ||
                selectedSizeString.isEmpty() || selectedColorString == null || selectedColorString.isEmpty()){
            Toast.makeText(currentActivity,currentActivity.getResources().getString(R.string.str_make_sure_valid_field),Toast.LENGTH_LONG).show();
        }else {
            productDialog.dismiss();
            progressBar.setVisibility(View.VISIBLE);
            BzzApp.getBzHubRepository().updateProduct(productId,strJobTitle,strPrice,strDescrption,selectedColorString,
                    selectedSizeString).subscribe(
                    new Observer<SimpleResponse>() {
                        Disposable mDispose;
                        @Override
                        public void onSubscribe(Disposable d) {
                            mDispose =d;
                        }

                        @Override
                        public void onNext(SimpleResponse simpleResponse) {
                            selectedColorString  = "";
                            selectedSizeString ="";
                            getCompanyProfile();
                        }

                        @Override
                        public void onError(Throwable e) {
                            progressBar.setVisibility(View.GONE);
                            CompanyProfileFragment.this.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            mDispose.dispose();
                        }
                    }
            );
        }
    }

    private void openSigleImage(int requestCode){
        Intent intent = new Intent(currentActivity, ImagesSelectorActivity.class);
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 1);
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, singleImageResult);
        startActivityForResult(intent, requestCode);
    }

    @OnClick({R.id.tv_edit_overview,R.id.tv_edit_website,R.id.tv_edit_year})
    void onClickEdit(){
        if (currentActivity instanceof CompanyProfileActivity){
            ((CompanyProfileActivity)currentActivity).showEditProfileDialog();
        }
    }

    private void openImagePicker(int type){
        imageRequestCode = type;
        easyImage.openChooser(this);
    }

    private void getSelectors() {
        BzzApp.getBzHubRepository().getSelectors().subscribe(new Observer<SelectorResponse>() {
            Disposable mDispose;

            @Override
            public void onSubscribe(Disposable d) {
                mDispose = d;
            }

            @Override
            public void onNext(SelectorResponse selectorResponse) {
                if (selectorResponse.getResult() != null) {
                    interests = selectorResponse.getResult();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                mDispose.dispose();
            }
        });
    }

    private void showRemoveProductDialog(Integer productId){
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_delete_interest, null);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvTitle = dialog.findViewById(R.id.tv_title);
        tvTitle.setVisibility(View.INVISIBLE);
        TextView tvYes = dialog.findViewById(R.id.tv_yes);
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        tvYes.setOnClickListener(v -> {
            dialog.dismiss();
            removeProduct(productId);
        });

        tvCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        LinearLayout layoutRoot = dialog.findViewById(R.id.layout_root);
        layoutRoot.setOnClickListener(v -> dialog.dismiss());
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

    private void showRemoveCategoryDialog(Integer categoryID){
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_delete_interest, null);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvTitle = dialog.findViewById(R.id.tv_title);
        tvTitle.setVisibility(View.INVISIBLE);
        TextView tvYes = dialog.findViewById(R.id.tv_yes);
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        tvYes.setOnClickListener(v -> {
            dialog.dismiss();
            removeInterest(categoryID);
        });

        tvCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        LinearLayout layoutRoot = dialog.findViewById(R.id.layout_root);
        layoutRoot.setOnClickListener(v -> dialog.dismiss());
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

    public String getSelectedSize(List<ProductDetail.Size> sizeList){
        StringBuilder strLanguage = new StringBuilder();
        if (!sizeList.isEmpty()){
            for (int i = 0;i<sizeList.size();i++){
                switch (Constant.getLanguage(getActivity())) {
                    case "en":
                        strLanguage.append(sizeList.get(i).getName()).append(i < sizeList.size() - 1 ? "," : "");
                        break;
                    case "ar":
                        strLanguage.append(sizeList.get(i).getArabicName()).append(i < sizeList.size() - 1 ? "," : "");
                        break;
                }
            }
        }

        return strLanguage.toString();
    }

    public String getSelectedColor(List<ProductDetail.Color> sizeList){
        StringBuilder strLanguage = new StringBuilder();
        if (!sizeList.isEmpty()){
            for (int i = 0;i<sizeList.size();i++){
                switch (Constant.getLanguage(getActivity())) {
                    case "en":
                        strLanguage.append(sizeList.get(i).getName()).append(i < sizeList.size() - 1 ? "," : "");
                        break;
                    case "ar":
                        strLanguage.append(sizeList.get(i).getArabicName()).append(i < sizeList.size() - 1 ? "," : "");
                        break;
                }
            }
        }

        return strLanguage.toString();
    }


    private void showCompanyBusinessTypeDialog() {
        dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_business_type, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView btnSave = dialog.findViewById(R.id.tv_save);
        CheckBox checkboxManufacturer = dialog.findViewById(R.id.checkbox_manufacturers);
        CheckBox checkboxTrader = dialog.findViewById(R.id.checkbox_trader);
        CheckBox checkboxServiceCompany = dialog.findViewById(R.id.checkbox_service_company);

        checkboxTrader.setChecked((companyDetail.getTrader() == 1));
        checkboxManufacturer.setChecked(companyDetail.getManufacturer() == 1);
        checkboxServiceCompany.setChecked(companyDetail.getServiceCompany() == 1);

        btnSave.setOnClickListener(v -> {
            if (checkboxManufacturer.isChecked()) manufacturer = 1; else manufacturer = 0;
            if (checkboxTrader.isChecked()) trader = 1; else trader =0;
            if (checkboxServiceCompany.isChecked()) serviceCompany = 1; else serviceCompany = 0;

            ((CompanyProfileActivity)currentActivity).trader = trader;
            ((CompanyProfileActivity)currentActivity).manufacturer = manufacturer;
            ((CompanyProfileActivity)currentActivity).serviceCompany = serviceCompany;
            ((CompanyProfileActivity)currentActivity).updateCompanyProfile();
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

    @OnClick(R.id.tv_edit_business_type) void onEditBusinessType(){
        showCompanyBusinessTypeDialog();
    }

    @OnClick(R.id.layout_busines_type_selection) void onEditBusinessTypeLayout(){
        showCompanyBusinessTypeDialog();
    }
}
