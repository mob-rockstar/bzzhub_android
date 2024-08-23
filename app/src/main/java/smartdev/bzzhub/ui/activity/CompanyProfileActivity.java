package smartdev.bzzhub.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;
import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.CityResponse;
import smartdev.bzzhub.repository.model.CompanyDetailResponse;
import smartdev.bzzhub.repository.model.CompanyLoginResult;
import smartdev.bzzhub.repository.model.CountryResponse;
import smartdev.bzzhub.repository.model.EventBusMessage;
import smartdev.bzzhub.repository.model.SelectorResponse;
import smartdev.bzzhub.repository.model.SimpleResponse;
import smartdev.bzzhub.repository.model.SubCategoryResponse;
import smartdev.bzzhub.ui.adapter.CompanyDetailPagerAdapter;
import smartdev.bzzhub.ui.base.BaseActivity;
import smartdev.bzzhub.ui.fragment.CompanyFeedFragment;
import smartdev.bzzhub.ui.fragment.CompanyProfileConnectionsFragment;
import smartdev.bzzhub.ui.fragment.CompanyProfileFragment;
import smartdev.bzzhub.ui.fragment.CompanyProfileJobsFragment;
import smartdev.bzzhub.ui.fragment.CompanyProfileMapFragment;
import smartdev.bzzhub.ui.fragment.CompanyProfileMediaFragment;
import smartdev.bzzhub.ui.fragment.CompanyProfileRFQFragment;
import smartdev.bzzhub.ui.fragment.CompanyProfileTenderFragment;
import smartdev.bzzhub.util.Constant;
import smartdev.bzzhub.util.KeyboardUtils;
import smartdev.bzzhub.util.ProfileUtils;
import smartdev.bzzhub.util.RealPathUtils;

public class CompanyProfileActivity extends BaseActivity {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.iv_profile)
    CircleImageView ivProfile;
    @BindView(R.id.iv_banner)
    ImageView ivBanner;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.tv_address)
    TextView tAddress;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    List<Fragment> fragments;
    List<String> mTitle= new ArrayList<>();

    String[] zipTypes = {"pdf"};
    CompanyDetailResponse.CompanyDetail mCompanyDetail;
    File fileLogo = null, fileBanner = null;

    private int TYPE_BANNER = 1001;
    private int TYPE_LOGO = 1002;

    private Spinner spinnerCity,spinnerCountry;
    private Spinner spinnerBusinessCategory,spinnerSubcategory;
    private  ArrayList<CountryResponse.Result> countries =new ArrayList<>();
    private  ArrayList<CityResponse.Result> cities =new ArrayList<>();
    private ArrayList<CityResponse.Result> allCities = new ArrayList<>();
    private ArrayList<SelectorResponse.Result> sectorList = new ArrayList<>();
    private ArrayAdapter<CityResponse.Result> cityAdapter;
    ArrayList<SubCategoryResponse.Result> subcategories = new ArrayList<>();
    private Integer countryId= 0, cityId = 0;
    private int selectorId ,currentBusinessId ,strYear;
    private String strWebsite = "",strDescription = "",strPostcode="",strLandLine = "";

    private EasyImage easyImage;
    private File file;
    private int imageRequestCode = 1001;
    ArrayList<com.jaiselrahman.filepicker.model.MediaFile> files;

    public File pdfFile;

    public int manufacturer = 0;
    public int trader = 0;
    public int serviceCompany =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);
        ButterKnife.bind(this);
        viewPager.setOffscreenPageLimit(0);

        // Define Easy Image
        easyImage =
               new  EasyImage.Builder(this).setCopyImagesToPublicGalleryFolder(false)
                .allowMultiple(false)
                .build();

        progressBar = mProgressBar;
        currentActivity = this;
        sectorList = Constant.getInstance().getSelectorList();
        countries = Constant.getInstance().getCountries();
        allCities = Constant.getInstance().getCities();
        initFragments();

        setUI();
    }

    private void initFragments(){
        fragments = new ArrayList<>();
        fragments.add(new CompanyProfileFragment());
        fragments.add(new CompanyProfileTenderFragment());
        fragments.add(new CompanyProfileMapFragment());
        fragments.add(new CompanyProfileMediaFragment());
        fragments.add(new CompanyFeedFragment());
        fragments.add(new CompanyProfileJobsFragment());
        fragments.add(new CompanyProfileConnectionsFragment());
        fragments.add(new CompanyProfileRFQFragment());

        mTitle.add(currentActivity.getResources().getString(R.string.str_home));
        mTitle.add(currentActivity.getResources().getString(R.string.str_tender));
        mTitle.add(currentActivity.getResources().getString(R.string.str_map));
        mTitle.add(currentActivity.getResources().getString(R.string.str_media));
        mTitle.add(currentActivity.getResources().getString(R.string.str_feeds));
        mTitle.add(currentActivity.getResources().getString(R.string.str_jobs));
        mTitle.add(currentActivity.getResources().getString(R.string.str_connections));
        mTitle.add(currentActivity.getResources().getString(R.string.str_rfq));
    }

    private void setUI(){

        viewPager.setAdapter(new CompanyDetailPagerAdapter(getSupportFragmentManager(),fragments,mTitle));
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                KeyboardUtils.hideKeyboard(currentActivity);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                KeyboardUtils.hideKeyboard(currentActivity);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initCurrentActivity(){
        currentActivity = CompanyProfileActivity.this;
        BzzApp.setCurrentActivity(currentActivity);
    }

    public void setCompanyProfile(CompanyDetailResponse.CompanyDetail companyProfile){
        mCompanyDetail = companyProfile;
        strYear = companyProfile.getEstablishedYear() != null ? companyProfile.getEstablishedYear() : 0;
        strWebsite = companyProfile.getWebsite() != null ? companyProfile.getWebsite() : "";
        strDescription = companyProfile.getDescription() != null ? companyProfile.getDescription() : "";
        strPostcode = companyProfile.getPostCode() != null ? companyProfile.getPostCode() : "";
        strLandLine = companyProfile.getLandline() != null ? companyProfile.getLandline() : "";
        countryId = companyProfile.getCountryId() != null ? companyProfile.getCountryId() : 0;
        cityId = companyProfile.getCityId() != null ? companyProfile.getCityId() : 0;
        selectorId = companyProfile.getSectorId() != null ? companyProfile.getSectorId() : 0;
        currentBusinessId = companyProfile.getCategoryId() != null ? companyProfile.getCategoryId() : 0;

        tvCompanyName.setText(companyProfile.getCompanyName());
        tAddress.setText(getResources().getString(R.string.str_address_value,companyProfile.getCountry() == null ? "":companyProfile.getCountry(),
                companyProfile.getCity() == null ? "":companyProfile.getCity()));
        Glide.with(currentActivity).load(companyProfile.getImage()).centerCrop().apply(new RequestOptions().placeholder(R.drawable.ic_bzzhub).error(R.drawable.ic_bzzhub)).into(ivProfile);
        Glide.with(currentActivity).load(companyProfile.getBanner()).centerCrop().apply(new RequestOptions().placeholder(R.drawable.ic_bzzhub).error(R.drawable.ic_bzzhub)).into(ivBanner);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCurrentActivity();


    }

    @OnClick(R.id.iv_back) void onBackClicked(){
        finish();
    }

    @OnClick(R.id.layout_edit_photo) void onEditProfileClicked(){
        showEditProfileDialog();
    }

    @OnClick(R.id.layout_edit_banner) void onEditBannerClicked(){
        openImageDialog();
    }

    @OnClick(R.id.layout_take_photo) void onTakePhotoClicked(){
        openImageDialog();
    }

    private void openImageDialog(){
        RxPermissions permissions = new RxPermissions(currentActivity);
        Disposable subscribe = permissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        showEditImageDialog();
                    }
                });
    }

    public void showEditProfileDialog(){
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_update_company_profile, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        spinnerCountry = dialog.findViewById(R.id.spinner_country);
        spinnerCity = dialog.findViewById(R.id.spinner_city);

        initLocaleSpinners();

        spinnerBusinessCategory = dialog.findViewById(R.id.spinner_type_business);
        spinnerSubcategory = dialog.findViewById(R.id.spinner_business_category);
        initCategorySpinners();

        EditText editTextYear = dialog.findViewById(R.id.edittext_established_year);
        EditText editTextWebsite = dialog.findViewById(R.id.edittext_website);
        EditText editTextPostalCode = dialog.findViewById(R.id.edittext_postal);
        EditText editTextLandLine = dialog.findViewById(R.id.edittext_landline);
        EditText editTextDescription = dialog.findViewById(R.id.edittext_description);
        if (strYear != 0){
            editTextYear.setText(String.valueOf(strYear));
        }
        editTextWebsite.setText(strWebsite);
        editTextDescription.setText(strDescription);
        editTextLandLine.setText(strLandLine);
        editTextPostalCode.setText(strPostcode);

        TextView tvUpload = dialog.findViewById(R.id.tv_upload);
        tvUpload.setOnClickListener(v -> {
            if (editTextYear.getText().toString().trim().length() != 0){
                strYear = Integer.parseInt(editTextYear.getText().toString().trim());
            }else {
                strYear = 0;
            }

            strWebsite = editTextWebsite.getText().toString().trim();
            strPostcode = editTextPostalCode.getText().toString().trim();
            strLandLine = editTextLandLine.getText().toString().trim();
            strDescription = editTextDescription.getText().toString().trim();
            if (strLandLine.isEmpty() || strPostcode.isEmpty() || strWebsite.isEmpty() ||
                    cityId == 0|| selectorId == 0 || strDescription.isEmpty()){
                CompanyProfileActivity.this.onError(new Exception(getResources().getString(R.string.str_make_sure_valid_field)));
            }else{
                updateCompanyProfile();
                dialog.dismiss();
            }
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

    private void initCategorySpinners(){
        sectorList.remove(sectorList.size()-1);
        sectorList.add(new SelectorResponse.Result(0,0, getResources().getString(R.string.str_business_category),"",0));

        ArrayAdapter<SelectorResponse.Result> adapter =
                new ArrayAdapter<SelectorResponse.Result>(currentActivity , android.R.layout.simple_spinner_dropdown_item, sectorList);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerBusinessCategory.setAdapter(adapter);
        if (mCompanyDetail.getCategoryId() != null){
            for (int i = 0;i<sectorList.size();i++){
                if (mCompanyDetail.getCategoryId() == sectorList.get(i).getCategoryId()){
                    spinnerBusinessCategory.setSelection(i);
                }
            }
        }else {
            spinnerBusinessCategory.setSelection(sectorList.size()-1);
        }


        spinnerBusinessCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectorId = sectorList.get(position).getCategoryId();
                if (selectorId != 0){
                    try {
                        getSubcategoriesList((selectorId));
                    }catch (Exception e){

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getSubcategoriesList(int id){
        BzzApp.getBzHubRepository().getSubcategories(id).subscribe(
                new Observer<SubCategoryResponse>() {
                    Disposable mDisposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(SubCategoryResponse subCategoryResponse) {
                        if (subCategoryResponse.getStatus() && subCategoryResponse.getCode() == 200 && subCategoryResponse.getResult() != null){
                            subcategories = (ArrayList<SubCategoryResponse.Result>) subCategoryResponse.getResult();
                            subcategories.add(new SubCategoryResponse.Result(0,0,getResources().getString(R.string.str_subcategory),"",0));
                            ArrayAdapter<SubCategoryResponse.Result> adapter =
                                    new ArrayAdapter<SubCategoryResponse.Result>(currentActivity , android.R.layout.simple_spinner_dropdown_item, subcategories);
                            adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                            spinnerSubcategory.setAdapter(adapter);

                            spinnerSubcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    selectorId = subcategories.get(position).getSubId();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            if (mCompanyDetail.getCategoryId() != null){
                                for (int i = 0;i<subcategories.size();i++){
                                    if (subcategories.get(i).getSubId() == mCompanyDetail.getSectorId()){
                                        spinnerSubcategory.setSelection(i);
                                    }
                                }
                            }else {
                                spinnerSubcategory.setSelection(subcategories.size()-1);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mDisposable.dispose();
                    }
                }
        );
    }

    ImageView ivDialogProfile;
    ImageView ivDialogBanner;

    private void showEditImageDialog(){
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_update_photo, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ivDialogProfile = dialog.findViewById(R.id.iv_dialog_profile);
        ivDialogBanner = dialog.findViewById(R.id.iv_dialog_banner);

        Glide.with(currentActivity).load(mCompanyDetail.getImage()).apply(new RequestOptions().placeholder(R.drawable.ic_bzzhub).error(R.drawable.ic_bzzhub)).centerCrop().into(ivDialogProfile);
        Glide.with(currentActivity).load(mCompanyDetail.getBanner()).apply(new RequestOptions().placeholder(R.drawable.ic_bzzhub).error(R.drawable.ic_bzzhub)).centerCrop().into(ivDialogBanner);

        RelativeLayout layoutEditLogo = dialog.findViewById(R.id.layout_edit_logo);
        RelativeLayout layoutEditBanner = dialog.findViewById(R.id.layout_edit_banner);
        layoutEditLogo.setOnClickListener(v -> {
            openImagePicker(TYPE_LOGO);
        });

        layoutEditBanner.setOnClickListener(v -> {
            openImagePicker(TYPE_BANNER);
        });

        TextView tvDone = dialog.findViewById(R.id.tv_done);
        tvDone.setOnClickListener(v -> {
              if (fileLogo != null|| fileBanner != null) {
                  uploadProfilePicture();
                  dialog.dismiss();
                } else {
                 showSnackBar("Please select at least one image",false);
              }
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

    private void openImagePicker(int type){
        imageRequestCode = type;
        easyImage.openChooser(this);
    }


    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void onPDFClicked(){



/*        Intent intent = new Intent(CompanyProfileActivity.this, FilePickerActivity.class);

        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true).setSuffixes("pdf").setShowFiles(true).setShowImages(false)
                .setShowVideos(false)
                .enableImageCapture(false)
                .setMaxSelection(1)
                .setSkipZeroSizeFiles(true)
                .build());

        startActivityForResult(intent, 1000);*/

        Intent intent = new Intent(this, FilePickerActivity.class);

        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowImages(false)
                .setShowVideos(false)
                .setShowAudios(false)
                .enableImageCapture(false)
                .setMaxSelection(1)
                .setSuffixes("pdf")
                .setShowFiles(true)
                .setSkipZeroSizeFiles(true)
                .build());

        startActivityForResult(intent, 1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000){
            files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            if (files != null && !files.isEmpty()){
                if (files.get(0).getUri() != null){
           //        pdfFile = new File(getRealPathFromUri(CompanyProfileActivity.this, files.get(0).getUri()));
                    pdfFile = new File(RealPathUtils.getRealPath(CompanyProfileActivity.this, files.get(0).getUri()));
                    EventBus.getDefault().post(new EventBusMessage(EventBusMessage.MessageType.PDF_SELECTED,""));
                }
            }
        }else{
            easyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                @Override
                public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                    if (imageRequestCode == TYPE_LOGO){
                        try {
                            fileLogo = imageFiles[0].getFile();

                            Glide.with(currentActivity).load(fileLogo.getAbsolutePath()).centerCrop().into(ivDialogProfile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (imageRequestCode == TYPE_BANNER){
                        try {
                            fileBanner = imageFiles[0].getFile();

                            Glide.with(currentActivity).load(fileBanner.getAbsolutePath()).centerCrop().into(ivDialogBanner);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    }

    private void uploadProfilePicture(){
        progressBar.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().uploadCompanyLogo(String.valueOf(Constant.getInstance().getCompanyProfile().getCompanyId()),fileLogo,fileBanner)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        if (simpleResponse.getCode() == 200 && simpleResponse.getStatus()){
                            if (fileLogo != null){
                                Glide.with(currentActivity).load(fileLogo.getAbsolutePath()).centerCrop().into(ivProfile);
                                mCompanyDetail.setImage(fileLogo.getAbsolutePath());
                                CompanyDetailResponse.CompanyDetail companyProfile = Constant.getInstance().getCompanyProfile();
                                companyProfile.setImage(fileLogo.getAbsolutePath());
                                Constant.getInstance().setCompanyProfile(companyProfile);
                                ProfileUtils.saveCompanyProfile(companyProfile);
                            }
                            if (fileBanner != null){
                                Glide.with(currentActivity).load(fileBanner.getAbsolutePath()).centerCrop().into(ivBanner);
                                mCompanyDetail.setBanner(fileBanner.getAbsolutePath());
                            }

                            fileLogo = null;
                            fileBanner = null;
                            progressBar.setVisibility(View.GONE);
                        }else {
                           CompanyProfileActivity.this.onError(new Exception(simpleResponse.getMessage()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        CompanyProfileActivity.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1){
            getSupportFragmentManager().popBackStack();
        }else
            finish();
    }

    private void initLocaleSpinners(){
        if (countries != null && !countries.isEmpty()){
            countries.remove(countries.size()- 1);
        }
        CountryResponse.Result result = new CountryResponse.Result(0,getResources().getString(R.string.str_country));
        countries.add(result);
        ArrayAdapter<CountryResponse.Result> countrySpinnerAdapter =
                new ArrayAdapter<CountryResponse.Result>(currentActivity , android.R.layout.simple_spinner_dropdown_item, countries);
        countrySpinnerAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countrySpinnerAdapter);

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryId = countries.get(position).getCountryId();
                if (position != countries.size()-1){
                    filterCities(countries.get(position).getCountryId());
                }else {
                    cities = new ArrayList<>();
                }
                setCityAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (mCompanyDetail.getCountryId() != null){
            for (int i = 0;i<countries.size();i++){
                if (countries.get(i).getCountryId() == mCompanyDetail.getCountryId()){
                    spinnerCountry.setSelection(i);
                }
            }
        }else {
            spinnerCountry.setSelection( countries.size()-1);
        }


/*        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryId = countries.get(position).getCountryId();
                if (position != countries.size()-1){
                    filterCities(countries.get(position).getCountryId());
                    setCityAdapter();
                }else {
                    cities = new ArrayList<>();
                    setCityAdapter();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    private void filterCities(Integer countryId){
        cities = new ArrayList<>();
        if (allCities!= null && !allCities.isEmpty()){
            for (CityResponse.Result city : allCities){
                if (city.getCountryId().equals(countryId)){
                    cities.add(city);
                }
            }
        }
    }

    private void setCityAdapter(){
        CityResponse.Result result = new CityResponse.Result(0,0,getResources().getString(R.string.str_city));
        cities.add(result);
        cityAdapter =
                new ArrayAdapter<CityResponse.Result>(currentActivity , android.R.layout.simple_spinner_dropdown_item, cities);
        cityAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(cityAdapter);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityId = cities.get(position).getCityId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (mCompanyDetail.getCityId() != null){
            for (int i = 0; i< cities.size();i++){
                if (cities.get(i).getCityId() == mCompanyDetail.getCityId()){
                    spinnerCity.setSelection(i);
                }
            }
        }else{
            spinnerCity.setSelection(cities.size()- 1);
        }
    }

    public void updateCompanyProfile(){ {
            BzzApp.getBzHubRepository().updateCompanyProfile(Constant.getInstance().getCompanyProfile().getCompanyId(),strYear,strWebsite,
                    strDescription,selectorId,cityId,strPostcode,strLandLine, manufacturer, trader, serviceCompany)
                    .subscribe(new Observer<SimpleResponse>() {
                        Disposable mDispose;
                        @Override
                        public void onSubscribe(Disposable d) {
                            mDispose = d;
                        }

                        @Override
                        public void onNext(SimpleResponse simpleResponse) {
                            mCompanyDetail.setSectorId(selectorId);
                            mCompanyDetail.setCityId(cityId);
                            mCompanyDetail.setPostCode(strPostcode);
                            mCompanyDetail.setLandline(strLandLine);
                            mCompanyDetail.setDescription(strDescription);
                            mCompanyDetail.setEstablishedYear(strYear);
                            mCompanyDetail.setWebsite(strWebsite);

                            if (simpleResponse.getCode() == 200){
                                CompanyProfileActivity.this.showSnackBar(simpleResponse.getMessage(),true);
                                initFragments();
                                setUI();
                            }
                            else {
                                CompanyProfileActivity.this.onError( new Exception( simpleResponse.getMessage()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            CompanyProfileActivity.this.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            mDispose.dispose();;
                        }
                    });
        }
    }


}
