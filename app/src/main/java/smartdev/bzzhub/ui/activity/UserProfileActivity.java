package smartdev.bzzhub.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;

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
import smartdev.bzzhub.repository.model.CityResponse;
import smartdev.bzzhub.repository.model.CountryResponse;
import smartdev.bzzhub.repository.model.SimpleResponse;
import smartdev.bzzhub.repository.model.UserLoginResponse;
import smartdev.bzzhub.repository.model.UserProfileResponse;
import smartdev.bzzhub.ui.adapter.ViewPagerAdapter;
import smartdev.bzzhub.ui.base.BaseActivity;
import smartdev.bzzhub.ui.fragment.UserFeedFragment;
import smartdev.bzzhub.ui.fragment.UserJobListFragment;
import smartdev.bzzhub.ui.fragment.UserMediaFragment;
import smartdev.bzzhub.ui.fragment.UserProfileFragment;
import smartdev.bzzhub.ui.fragment.UserRequestsMsgFragment;
import smartdev.bzzhub.ui.fragment.UserVisitorsFragment;
import smartdev.bzzhub.util.Constant;
import smartdev.bzzhub.util.KeyboardUtils;
import smartdev.bzzhub.util.ProfileUtils;

public class UserProfileActivity extends BaseActivity {

    List<Fragment> fragments = new ArrayList<>();
    List<String> mTitle= new ArrayList<>();
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.iv_profile)
    ImageView ivProfile;
    @BindView(R.id.iv_banner)
    ImageView ivBanner;
    @BindView(R.id.tv_user_name)
    TextView tvUsrName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    File fileLogo = null, fileBanner = null;

    private int TYPE_BANNER = 1001;
    private int TYPE_LOGO = 1002;

    ImageView ivDialogProfile;
    ImageView ivDialogBanner;
    UserProfileResponse.Result mUserProfile = null;

    String strEmail,strMobile,strUniversity,strSchool,strLannguage,strWork;
    EditText edittextLanguage;

    int userId = 0;

    public boolean isOtherUser;
    public int mUserId = 0;

    @BindView(R.id.layout_edit_photo)
    View editPhoto;
    @BindView(R.id.layout_take_photo)
    View takePhoto;
    @BindView(R.id.layout_edit_banner)
    View editBanner;

    private EasyImage easyImage;
    private File file;
    private int imageRequestCode = 1001;
    private Spinner spinnerCity,spinnerCountry;
    private Integer countryId= 0, cityId = 0;
    private  ArrayList<CountryResponse.Result> countries =new ArrayList<>();
    private  ArrayList<CityResponse.Result> cities =new ArrayList<>();
    private ArrayList<CityResponse.Result> allCities = new ArrayList<>();
    private ArrayAdapter<CityResponse.Result> cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        // Define Easy Image
        easyImage =
                new  EasyImage.Builder(this).setCopyImagesToPublicGalleryFolder(false)
                        .allowMultiple(false)
                        .build();

        countries = Constant.getInstance().getCountries();
        allCities = Constant.getInstance().getCities();

        userId = getIntent().getIntExtra("User_id",0);
        if (userId != 0){
            mUserId  = userId;
            editBanner.setVisibility(View.GONE);
            editPhoto.setVisibility(View.GONE);
            takePhoto.setVisibility(View.GONE);
        }else mUserId = ProfileUtils.getUserProfile().getUserId();
        isOtherUser = userId != 0;
        progressBar  = mProgressBar;
        initCurrentActivity();
        initFragments();
        initUI();
    }

    private void initCurrentActivity(){
        currentActivity = UserProfileActivity.this;
        BzzApp.setCurrentActivity(currentActivity);
    }

    private void initFragments(){
        fragments.add(new UserProfileFragment());
        fragments.add(new UserFeedFragment());
        if (userId == 0){
            fragments.add(new UserRequestsMsgFragment());
        }
        fragments.add(new UserMediaFragment());
        if (userId == 0){
            fragments.add(new UserVisitorsFragment());
        }
        fragments.add(new UserJobListFragment());

        mTitle.add(currentActivity.getResources().getString(R.string.str_home));
        mTitle.add(currentActivity.getResources().getString(R.string.str_feeds));
        if (userId == 0){
            mTitle.add(currentActivity.getResources().getString(R.string.str_requests_msg));
        }
        mTitle.add(currentActivity.getResources().getString(R.string.str_media));
        if (userId == 0){
            mTitle.add(currentActivity.getResources().getString(R.string.str_visitor));
        }
        mTitle.add(currentActivity.getResources().getString(R.string.str_my_job));
    }

    private void initUI(){
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),fragments,mTitle));
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


    @OnClick(R.id.layout_take_photo) void onTakePhotoClicked(){
        showEditImageDialog();
    }

    @OnClick(R.id.layout_edit_photo) void onEditPhotoClicked(){
        showEditProfileDialog();
    }


    private void showUploadPhotoDialog() {
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_update_photo, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.findViewById(R.id.layout_root).setOnClickListener(v -> dialog.dismiss());

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

    public void initViews(UserProfileResponse.Result userProfile){
        mUserProfile= userProfile;
        Glide.with(currentActivity).load(userProfile.getImage()).centerCrop().apply(new RequestOptions().placeholder(R.drawable.ic_bzzhub).error(R.drawable.ic_bzzhub)).into(ivProfile);
        Glide.with(currentActivity).load(userProfile.getBanner()).centerCrop().apply(new RequestOptions().placeholder(R.drawable.ic_bzzhub).error(R.drawable.ic_bzzhub)).into(ivBanner);
        tvUsrName.setText(userProfile.getFullName());
        if ( userProfile.getCountry() != null  && !userProfile.getCountry().isEmpty()){
            tvAddress.setText(getResources().getString(R.string.str_address_value,userProfile.getCountry() == null ? "":userProfile.getCountry(),
                    userProfile.getCity() == null ? "":userProfile.getCity()));
        }
    }

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

        Glide.with(currentActivity).load(mUserProfile.getImage()).centerCrop().apply(new RequestOptions().placeholder(R.drawable.ic_bzzhub).error(R.drawable.ic_bzzhub)).into(ivDialogProfile);
        Glide.with(currentActivity).load(mUserProfile.getBanner()).centerCrop().apply(new RequestOptions().placeholder(R.drawable.ic_bzzhub).error(R.drawable.ic_bzzhub)).into(ivDialogBanner);

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
            if (fileLogo != null || fileBanner != null) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


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

    private void uploadProfilePicture(){
        progressBar.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().uploadUserLogo( String.valueOf(mUserId),fileLogo,fileBanner)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        if (simpleResponse.getCode() == 200 && simpleResponse.getStatus()){
                            if (userId != 0){
                                if (fileLogo != null){
                                    Glide.with(currentActivity).load(fileLogo.getAbsolutePath()).centerCrop().into(ivProfile);
                                    mUserProfile.setImage(fileLogo.getAbsolutePath());
                                    UserProfileResponse.Result userProfile = Constant.getInstance().getUserProfile();
                                    userProfile.setImage(fileLogo.getAbsolutePath());
                                    ProfileUtils.saveUserProfile(userProfile);
                                }
                                if (fileBanner != null){
                                    Glide.with(currentActivity).load(fileBanner.getAbsolutePath()).centerCrop().into(ivBanner);
                                    mUserProfile.setBanner(fileBanner.getAbsolutePath());
                                }
                            }

                            fileLogo = null;
                            fileBanner = null;
                            progressBar.setVisibility(View.GONE);

                        }else {
                            UserProfileActivity.this.onError(new Exception(simpleResponse.getMessage()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        UserProfileActivity.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    @OnClick(R.id.layout_edit_banner) void onEditBannerClicked(){
        showEditImageDialog();
    }

    @OnClick(R.id.iv_back) void onBackClicked(){
        finish();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1){
            getSupportFragmentManager().popBackStack();
        }else
            finish();
    }

    public void showEditProfileDialog(){
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_update_user_profile, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText editTextEmail = dialog.findViewById(R.id.edittext_email);
        EditText editTextMobile = dialog.findViewById(R.id.edittext_mobile);
        edittextLanguage = dialog.findViewById(R.id.edittext_language);

        EditText editTextSchool = dialog.findViewById(R.id.edittext_school);
        EditText editTextUniversity = dialog.findViewById(R.id.edittext_university);
        EditText editTextWork = dialog.findViewById(R.id.edittext_work);
        spinnerCountry = dialog.findViewById(R.id.spinner_country);
        spinnerCity = dialog.findViewById(R.id.spinner_city);
        initLocaleSpinners();

        editTextEmail.setText(mUserProfile.getEmail());
        editTextMobile.setText(mUserProfile.getPhone());
        if (!edittextLanguage.getText().toString().trim().isEmpty()){
            edittextLanguage.setText(mUserProfile.getLanguages() != null ?   getLanguages(mUserProfile.getLanguages()): "");
        }
        editTextSchool.setText(mUserProfile.getSchool());
        editTextUniversity.setText(mUserProfile.getUniversity());
        editTextWork.setText(mUserProfile.getWork());
        edittextLanguage.setText(mUserProfile.getUserLanguage());
        TextView tvUpload = dialog.findViewById(R.id.tv_upload);
        tvUpload.setOnClickListener(v -> {
            strEmail = editTextEmail.getText().toString().trim();
            strMobile = editTextMobile.getText().toString().trim();
            strLannguage = edittextLanguage.getText().toString().trim();
            strSchool = editTextSchool.getText().toString().trim();
            strUniversity = editTextUniversity.getText().toString().trim();
            strWork = editTextWork.getText().toString().trim();

            if (!strEmail.isEmpty() &&android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches() && !strMobile.isEmpty()
             && strMobile.length() > 5){
                updateUserProfile();
                dialog.dismiss();
            }else {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.str_make_sure_valid_field),Toast.LENGTH_LONG).show();
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

    private String getLanguages(List<String> languages){
        StringBuilder strLanguage = new StringBuilder();
        if (!languages.isEmpty()){
            for (int i = 0;i<languages.size();i++){
                strLanguage.append(languages.get(i)).append(i<languages.size()-1 ? " ," : "");
            }
        }
        return strLanguage.toString();
    }

    private void updateUserProfile(){
        BzzApp.getBzHubRepository().updateUserProfile(ProfileUtils.getUserProfile().getUserId(),strEmail,strMobile,strLannguage,strSchool,
                strUniversity,strWork,countryId,cityId).subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
            public void onSubscribe(Disposable d) {
                mDispose = d;
            }

            @Override
            public void onNext(SimpleResponse simpleResponse) {
                mUserProfile.setEmail(strEmail);
                mUserProfile.setPhone(strMobile);
                edittextLanguage.setText(strLannguage);
                mUserProfile.setSchool(strSchool);
                mUserProfile.setUniversity(strUniversity);
                mUserProfile.setWork(strWork);

                if (simpleResponse.getCode() == 200){
                    UserProfileActivity.this.showSnackBar(simpleResponse.getMessage(),true);
                    initFragments();
                    initUI();
                }
                else {
                    UserProfileActivity.this.onError( new Exception( simpleResponse.getMessage()));
                }
            }

            @Override
            public void onError(Throwable e) {
                UserProfileActivity.this.onError(e);
            }

            @Override
            public void onComplete() {
                mDispose.dispose();
            }
        });
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

        if (mUserProfile.getCountryId() != null){
            for (int i = 0;i<countries.size();i++){
                if (countries.get(i).getCountryId() == mUserProfile.getCountryId()){
                    spinnerCountry.setSelection(i);
                }
            }
        }else {
            spinnerCountry.setSelection( countries.size()-1);
        }
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
                Log.d("cityIdIs",String.valueOf(cityId));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (mUserProfile.getCityId() != null){
            for (int i = 0; i< cities.size();i++){
                if (cities.get(i).getCityId() == mUserProfile.getCityId()){
                    spinnerCity.setSelection(i);
                }
            }
        }else{
            spinnerCity.setSelection(cities.size()- 1);
        }
    }
}
