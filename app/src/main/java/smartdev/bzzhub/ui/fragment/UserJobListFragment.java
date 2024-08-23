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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.BusinessCategoryResponse;
import smartdev.bzzhub.repository.model.CityResponse;
import smartdev.bzzhub.repository.model.CountryResponse;
import smartdev.bzzhub.repository.model.SimpleResponse;
import smartdev.bzzhub.repository.model.SubCategoryResponse;
import smartdev.bzzhub.repository.model.UserJobResponse;
import smartdev.bzzhub.ui.activity.UserProfileActivity;
import smartdev.bzzhub.ui.adapter.UserJobAdapter;
import smartdev.bzzhub.ui.base.BaseFragment;
import smartdev.bzzhub.util.Constant;

public class UserJobListFragment extends BaseFragment implements UserJobAdapter.SelectedListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private UserJobAdapter userJobAdapter;
    private List<UserJobResponse.Result> jobList = new ArrayList<>();
    private ArrayList<BusinessCategoryResponse.Result> jobTypeList = new ArrayList<>();
    private ArrayList<SubCategoryResponse.Result> jobCategoryList ;
    private Spinner spinnerJobTCategory;
    private  Spinner spinnerJobType;
    private  int mSectorId;
    private  int mJobId;
    private Spinner spinnerCity,spinnerCountry;
    private  ArrayList<CountryResponse.Result> countries =new ArrayList<>();
    private  ArrayList<CityResponse.Result> cities =new ArrayList<>();
    private ArrayList<CityResponse.Result> allCities = new ArrayList<>();
    private ArrayAdapter<CityResponse.Result> cityAdapter;
    private Integer countryId;

    @BindView(R.id.layout_add)
    View layoutAdd;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    UserJobResponse.Result mJob;
    Integer cityId ;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_job_list, container, false);
        ButterKnife.bind(this, view);

        countries = Constant.getInstance().getCountries();
        allCities = Constant.getInstance().getCities();
        initRecyclerViews();

        if (isVisibleToUser){
            getJobs();
            getCategories();
        }

        if( ((UserProfileActivity)currentActivity).isOtherUser){
           layoutAdd.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (getView() != null && isVisibleToUser){
            getJobs();
            getCategories();
        }
    }

    private void initRecyclerViews(){
        recyclerView.setLayoutManager(new LinearLayoutManager(currentActivity));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
    }

    private void getJobs(){
        progressBar.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().getAllJobs(((UserProfileActivity)currentActivity).mUserId)
                .subscribe(new Observer<UserJobResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(UserJobResponse userJobResponse) {
                        progressBar.setVisibility(View.GONE);
                        if (userJobResponse.getResult() != null){
                            jobList = userJobResponse.getResult();
                            tvNoData.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            setDataToView();
                            Log.d("test","test");
                        }else {
                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        ((UserProfileActivity)currentActivity).onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    private void setDataToView(){
        if (jobTypeList.isEmpty()){
            tvNoData.setVisibility(View.VISIBLE);
            Log.d("test","test");
            recyclerView.setVisibility(View.GONE);
        }else {
            tvNoData.setVisibility(View.GONE);
            Log.d("test","test");
            recyclerView.setVisibility(View.VISIBLE);
            userJobAdapter = new UserJobAdapter(currentActivity,jobList,this);
            recyclerView.setAdapter(userJobAdapter);
        }

    }

    @OnClick(R.id.layout_add) void onAddJobClicked(){
        mJob = null;
        showAddDialog(0);
    }

    private void showAddDialog(int type){ // type 0: Add, 1: edit
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_add_job, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvTitle = dialog.findViewById(R.id.tv_title);
        tvTitle.setText(currentActivity.getResources().getString(type == 0 ? R.string.str_add_job : R.string.str_edit_job));

        TextView tvDone = dialog.findViewById(R.id.tv_confirm);
        tvDone.setText(currentActivity.getResources().getString(type == 0 ? R.string.str_add : R.string.str_update));
        tvTitle.setText(currentActivity.getResources().getString(type == 0 ? R.string.str_add_job : R.string.str_edit_job));
        tvDone.setText(currentActivity.getResources().getString(type == 0 ? R.string.str_add : R.string.str_update));
        EditText editTextJobTitle = dialog.findViewById(R.id.edittext_job_title);
        EditText editTextJobDescription = dialog.findViewById(R.id.edittext_description);
        spinnerJobType = dialog.findViewById(R.id.spinner_job_type);
        spinnerJobTCategory = dialog.findViewById(R.id.spinner_job_category);
        ArrayAdapter<BusinessCategoryResponse.Result> adapter =
                new ArrayAdapter<BusinessCategoryResponse.Result>(currentActivity , R.layout.item_spinner_job, jobTypeList);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerJobType.setAdapter(adapter);
        spinnerJobType.setSelection(jobTypeList.size()-1);
        if (mJob != null && type == 1){
            editTextJobDescription.setText(mJob.getDescription());
            editTextJobTitle.setText(mJob.getTitle());
        }

        spinnerJobType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSubcategories((jobTypeList.get(position).getCategoryId()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (mJob != null && type == 1 && mJob.getCategoryID() != null ){
            for (int i = 0;i<jobTypeList.size();i++){
                if (jobTypeList.get(i).getCategoryId().equals(mJob.getCategoryID())){
                    spinnerJobType.setSelection(i);
                }
            }
        }

        spinnerCountry = dialog.findViewById(R.id.spinner_country);
        spinnerCity = dialog.findViewById(R.id.spinner_city);
        initLocaleSpinners();

        if (mJob != null && mJob.getCountryID() != null ){
            if (!countries.isEmpty()){
                for (int i = 0;i<countries.size();i++){
                    if (countries.get(i).getCountryId().equals(mJob.getCountryID())){
                        spinnerCountry.setSelection(i);
                    }
                }
            }
        }
        tvDone.setOnClickListener(v -> {
            if (type == 1 && mJob != null){
                if (cityId == null|| cityId == 0){
                    cityId = mJob.getCityId();
                }
                if (mSectorId == 0)
                    mSectorId = (mJob.getSectorId()) ;
            }

            String strJobTitle = editTextJobTitle.getText().toString().trim();
            String strJobDescription = editTextJobDescription.getText().toString().trim();
            if (strJobTitle.isEmpty() || strJobDescription.isEmpty() ||  cityId == 0 || mSectorId == 0){
                ((UserProfileActivity)currentActivity).onError(new Exception(currentActivity.getResources().getString(
                        R.string.str_make_sure_valid_field)));
            }else {
                if (type == 0){
                    addJob(strJobTitle,mSectorId,strJobDescription);
                    dialog.dismiss();
                }else {
                    editJob((mJobId),strJobTitle,mSectorId,strJobDescription);
                    dialog.dismiss();
                }
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

    private void initLocaleSpinners(){
        ArrayAdapter<CountryResponse.Result> countrySpinnerAdapter =
                new ArrayAdapter<CountryResponse.Result>(currentActivity , android.R.layout.simple_spinner_dropdown_item, countries);
        countrySpinnerAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countrySpinnerAdapter);
        spinnerCountry.setSelection( countries.size()-1);
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });
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
        CityResponse.Result result = new CityResponse.Result(0,0,"- Select -");
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

        if ( mJob != null && mJob.getCityId() != null){
            if (!cities.isEmpty()){
                for (int i = 0;i<cities.size();i++){
                    if (cities.get(i).getCityId().equals(mJob.getCityId())){
                        spinnerCity.setSelection(i);
                    }
                }
            }
        }
    }

    private void showDeleteFeedDialog(Integer jobId){
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_delete_job, null);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvYes = dialog.findViewById(R.id.tv_yes);
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        tvYes.setOnClickListener(v -> {
            dialog.dismiss();
            removeJob((jobId));
        });

        tvCancel.setOnClickListener(v -> dialog.dismiss());

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

    @Override
    public void onRemoveClicked(Integer jobId) {
        showDeleteFeedDialog(jobId);
    }

    @Override
    public void onEditClicked(UserJobResponse.Result job) {
        mJob = job;
        mJobId = job.getJobId();
        showAddDialog(1);
    }

    private void addJob(String jobTitle,int sectorId,String description){
        BzzApp.getBzHubRepository().createJob(jobTitle,sectorId,description,cityId)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        if (simpleResponse.getStatus() && simpleResponse.getCode() == 200){
                            getJobs();
                        }
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

    private void editJob(int jobId,String jobTitle,int sectorId,String description) {
        if (cityId == 0){
            cityId = mJob.getCityId();
        }

        if (sectorId == 0)
            sectorId = (mJob.getSectorId());

        BzzApp.getBzHubRepository().updateJob(jobId,jobTitle, sectorId, description, cityId)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        if (simpleResponse.getStatus() && simpleResponse.getCode() == 200){
                            getJobs();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((UserProfileActivity) currentActivity).onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    private void removeJob(Integer jobId){
        BzzApp.getBzHubRepository().deleteJob((jobId))
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        if (simpleResponse.getStatus() && simpleResponse.getCode() == 200){
                            ((UserProfileActivity)currentActivity).showSnackBar(simpleResponse.getMessage(),true);
                            getJobs();
                        }else {
                            ((UserProfileActivity)currentActivity).showSnackBar(simpleResponse.getMessage(),false);
                        }
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

    private void getCategories(){
        BzzApp.getBzHubRepository().getBusinessCategoryResponse().subscribe(
                new Observer<BusinessCategoryResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(BusinessCategoryResponse businessCategoryResponse) {
                        if (businessCategoryResponse.getStatus() && businessCategoryResponse.getCode() == 200){
                            jobTypeList = (ArrayList<BusinessCategoryResponse.Result>) businessCategoryResponse.getResult();
                        }
                        jobTypeList.add(new BusinessCategoryResponse.Result(0,"- Select -",0));
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((UserProfileActivity)currentActivity).onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                }
        );
    }

    private void getSubcategories(int categoryId){
        BzzApp.getBzHubRepository().getSubcategories(categoryId).subscribe(
                new Observer<SubCategoryResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SubCategoryResponse subCategoryResponse) {
                        if (subCategoryResponse.getStatus() && subCategoryResponse.getCode() == 200){
                            jobCategoryList = (ArrayList<SubCategoryResponse.Result>) subCategoryResponse.getResult();
                            if (jobCategoryList != null){
                                jobCategoryList.add(new SubCategoryResponse.Result(0,0,"- Select -"
                                        ,"",0));

                                ArrayAdapter<SubCategoryResponse.Result> adapter =
                                        new ArrayAdapter<SubCategoryResponse.Result>(currentActivity , android.R.layout.simple_spinner_dropdown_item, jobCategoryList);
                                adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                                spinnerJobTCategory.setAdapter(adapter);
                                spinnerJobTCategory.setSelection(jobCategoryList.size()-1);

                                if ( mJob != null && mJob.getSectorId() != null){
                                    for (int i = 0;i<jobCategoryList.size();i++){
                                        if (jobCategoryList.get(i).getSubId().equals(mJob.getSectorId())){
                                            spinnerJobTCategory.setSelection(i);
                                        }
                                    }
                                }

                                spinnerJobTCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        mSectorId = (jobCategoryList.get(position).getSubId());
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((UserProfileActivity)currentActivity).onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                }
        );
    }

}
