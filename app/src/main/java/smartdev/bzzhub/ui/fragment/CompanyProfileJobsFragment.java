package smartdev.bzzhub.ui.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
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
import smartdev.bzzhub.repository.model.CompanyJobResponse;
import smartdev.bzzhub.repository.model.CountryResponse;
import smartdev.bzzhub.repository.model.SimpleResponse;
import smartdev.bzzhub.repository.model.SubCategoryResponse;
import smartdev.bzzhub.ui.activity.CompanyProfileActivity;
import smartdev.bzzhub.ui.adapter.CompanyJobAdapter;
import smartdev.bzzhub.ui.base.BaseFragment;
import smartdev.bzzhub.util.Constant;
import smartdev.bzzhub.util.KeyboardUtils;

public class CompanyProfileJobsFragment extends BaseFragment  implements CompanyJobAdapter.SelectedListener{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private CompanyJobAdapter userJobAdapter;
    private List<CompanyJobResponse.Result> jobList = new ArrayList<>();
    private ArrayList<BusinessCategoryResponse.Result> jobTypeList = new ArrayList<>();
    private ArrayList<SubCategoryResponse.Result> jobCategoryList ;
    private Spinner spinnerJobTCategory;
    private  Spinner spinnerJobType;
    private  int mSectorId;
    private  Integer mJobId;
    private Spinner spinnerCity,spinnerCountry;
    private  ArrayList<CountryResponse.Result> countries =new ArrayList<>();
    private  ArrayList<CityResponse.Result> cities =new ArrayList<>();
    private ArrayList<CityResponse.Result> allCities = new ArrayList<>();
    private ArrayAdapter<CityResponse.Result> cityAdapter;
    private Integer countryId;
    private final Calendar mCalendar = Calendar.getInstance();
    TextView tvStartDate, tvEndDate;
    CompanyJobResponse.Result mCurrentJob;
    private String strJobTitle,strSalary,jobCategory,strJobDescription,strAge, strVacancies,strStartDate,strEndDate;
    private Integer cityId;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_profile_jobs, container, false);
        ButterKnife.bind(this, view);

        countries = Constant.getInstance().getCountries();
        allCities = Constant.getInstance().getCities();
        initRecyclerViews();

        if (isVisibleToUser){
            getJobs();
            getCategories();
        }
        return view;
    }

    private void initRecyclerViews(){
        recyclerView.setLayoutManager(new LinearLayoutManager(currentActivity));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
    }

    private void getJobs(){
        progressBar.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().getCompanyJobs((Constant.getInstance().getCompanyProfile().getCompanyId()))
                .subscribe(new Observer<CompanyJobResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(CompanyJobResponse userJobResponse) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (userJobResponse.getStatus() && userJobResponse.getCode() == 200 && userJobResponse.getResult() != null){
                            jobList = userJobResponse.getResult();
                            tvNoData.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            setDataToView();
                        }else {
                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        ((CompanyProfileActivity)currentActivity).onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    private void setDataToView(){
        if (jobList.isEmpty()){
            tvNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            userJobAdapter = new CompanyJobAdapter(currentActivity,jobList,this);
            recyclerView.setAdapter(userJobAdapter);
        }else {
            tvNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            userJobAdapter = new CompanyJobAdapter(currentActivity,jobList,this);
            recyclerView.setAdapter(userJobAdapter);
        }

    }

    @OnClick(R.id.layout_add) void onAddJobClicked(){
        mCurrentJob = null;
        showAddDialog(0);
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

    private void showAddDialog(int type){ // type 0: Add, 1: edit
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_add_job_company, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvTitle = dialog.findViewById(R.id.tv_title);
        tvTitle.setText(currentActivity.getResources().getString(type == 0 ? R.string.str_add_job : R.string.str_edit_job));

        TextView tvDone = dialog.findViewById(R.id.tv_done);
        tvDone.setText(currentActivity.getResources().getString(type == 0 ? R.string.str_add : R.string.str_update));

        EditText editTextJobTitle = dialog.findViewById(R.id.edittext_job_title);
        EditText editTextJobDescription = dialog.findViewById(R.id.edittext_description);
        EditText editTextSalary =  dialog.findViewById(R.id.edittext_salary);
        spinnerJobType = dialog.findViewById(R.id.spinner_job_type);
        spinnerJobTCategory = dialog.findViewById(R.id.spinner_job_category);
        ArrayAdapter<BusinessCategoryResponse.Result> adapter =
                new ArrayAdapter<BusinessCategoryResponse.Result>(currentActivity , R.layout.item_spinner_job, jobTypeList);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerJobType.setAdapter(adapter);
        spinnerJobType.setSelection(jobTypeList.size()-1);
        EditText editTextVacancies = dialog.findViewById(R.id.edittext_number_of_vacancy);
        EditText editTextAge = dialog.findViewById(R.id.edittext_age);

        if (type == 1 && mCurrentJob != null){
            if (mCurrentJob.getSectorId() != null ){
                for (int i = 0;i<jobTypeList.size();i++){
                    if (jobTypeList.get(i).getCategoryId().equals(mCurrentJob.getCategoryID())){
                        spinnerJobType.setSelection(i);
                    }
                }
            }
        }

        LinearLayout layoutRoot = dialog.findViewById(R.id.layout_root);
      //  layoutRoot.setOnClickListener(v -> dialog.dismiss());
        spinnerJobType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSubcategories((jobTypeList.get(position).getCategoryId()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerCountry = dialog.findViewById(R.id.spinner_country);
        spinnerCity = dialog.findViewById(R.id.spinner_city);
        initLocaleSpinners();
        if (type == 1 && mCurrentJob != null && mCurrentJob.getCountryID() != null ){
            if (!countries.isEmpty()){
                for (int i = 0;i<countries.size();i++){
                    if (countries.get(i).getCountryId().equals(mCurrentJob.getCountryID())){
                        spinnerCountry.setSelection(i);
                    }
                }
            }
        }
        tvStartDate = dialog.findViewById(R.id.tv_start_date);
        tvEndDate = dialog.findViewById(R.id.tv_end_date);

        tvStartDate.setOnClickListener(v -> {
            KeyboardUtils.hideKeyboard(currentActivity);
            // TODO Auto-generated method stub
            DatePickerDialog datePickerDialog= new DatePickerDialog(currentActivity,R.style.AppCompatDatePickerDialogStyle,dateStartDate, mCalendar
                    .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                    mCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000 );
            datePickerDialog.show();
        });

        tvEndDate.setOnClickListener(v -> {
            KeyboardUtils.hideKeyboard(currentActivity);
            // TODO Auto-generated method stub
            DatePickerDialog datePickerDialogEndDate=  new DatePickerDialog(currentActivity,R.style.AppCompatDatePickerDialogStyle,dateEndDate, mCalendar
                    .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                    mCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialogEndDate.getDatePicker().setMinDate(System.currentTimeMillis()- 1000);
            datePickerDialogEndDate.show();
        });

        if (type == 1 && mCurrentJob != null){
            tvEndDate.setText(mCurrentJob.getEndDate());
            tvStartDate.setText(mCurrentJob.getStartDate());
            editTextJobTitle.setText(mCurrentJob.getTitle());
            editTextJobDescription.setText(mCurrentJob.getDescription());
            editTextAge.setText(String.valueOf(mCurrentJob.getAge()));
            editTextSalary.setText(mCurrentJob.getSalary());
            editTextVacancies.setText(String.valueOf(mCurrentJob.getNoVacancies()));
            cityId = mCurrentJob.getCityId();
            try {
                mSectorId = (mCurrentJob.getSectorId());
            }catch (Exception e){

            }
        }

        tvDone.setOnClickListener(v -> {
             strJobTitle = editTextJobTitle.getText().toString().trim();
             strJobDescription = editTextJobDescription.getText().toString().trim();
             strEndDate = tvEndDate.getText().toString().trim();
             strStartDate = tvEndDate.getText().toString().trim();
             strVacancies = editTextVacancies.getText().toString().trim();
             strAge = editTextAge.getText().toString().trim();
             strSalary =  editTextSalary.getText().toString().trim();
            String strJobDescription = editTextJobDescription.getText().toString().trim();
            if (type == 0){
                if (!strJobTitle.isEmpty() && !strJobDescription.isEmpty() && !strAge.isEmpty()
                        && !strStartDate.isEmpty() && !strEndDate.isEmpty() && cityId != null && cityId != 0
                        && mSectorId != 0) {
                    dialog.dismiss();
                    addJob(mSectorId,cityId);
                }else {
                    ((CompanyProfileActivity)currentActivity).onError(new Exception(currentActivity.getResources().getString(R.string.str_make_sure_valid_field)));
                }

            }else {
                if (mSectorId == 0)
                try {
                    mSectorId =  (mCurrentJob.getSectorId());
                }catch (Exception e){

                }
                if (cityId == null || cityId== 0)
                    cityId = mCurrentJob.getCityId();
                if (!strJobTitle.isEmpty() && !strJobDescription.isEmpty() && !strAge.isEmpty()
                        && !strStartDate.isEmpty() && !strEndDate.isEmpty() && cityId != null && cityId != 0
                        && mSectorId != 0) {
                    dialog.dismiss();
                    editJob((mJobId), strJobTitle, mSectorId, strJobDescription, mCurrentJob);
                }
                else {
                    ((CompanyProfileActivity)currentActivity).onError(new Exception(currentActivity.getResources().getString(R.string.str_make_sure_valid_field)));
                }
            }

        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setCancelable(true);
        dialog.show();
    }

    DatePickerDialog.OnDateSetListener dateStartDate = (view, year, monthOfYear, dayOfMonth) -> {
        // TODO Auto-generated method stub
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateStartDate();
    };

    DatePickerDialog.OnDateSetListener dateEndDate = (view, year, monthOfYear, dayOfMonth) -> {
        // TODO Auto-generated method stub
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateEndDate();
    };


    private void updateStartDate() {
        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvStartDate.setText(sdf.format(mCalendar.getTime()));
    }

    private void updateEndDate(){
        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvEndDate.setText(sdf.format(mCalendar.getTime()));
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

    private void filterCities(int countryId){
        cities = new ArrayList<>();
        if (allCities!= null && !allCities.isEmpty()){
            for (CityResponse.Result city : allCities){
                if (city.getCountryId() == (countryId)){
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

        try {
            if (mCurrentJob != null && mCurrentJob.getCityId() != null ){
                if (!cities.isEmpty()){
                    for (int i = 0;i<cities.size();i++){
                        if (cities.get(i).getCityId().equals(mCurrentJob.getCityId())){
                            spinnerCity.setSelection(i);
                        }
                    }
                }
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onRemoveClicked(Integer jobId) {
        showDeleteFeedDialog(jobId);
    }

    @Override
    public void onEditClicked(CompanyJobResponse.Result currentJob) {
        mCurrentJob = currentJob;
        mJobId = mCurrentJob.getJobId();
        showAddDialog(1);
    }

    private void addJob(int sectorId,int cityId){

            BzzApp.getBzHubRepository().createCompanyJob(Constant.getInstance().getCompanyProfile().getCompanyId(),
                    cityId,(sectorId),strJobTitle,strSalary,strJobDescription,strAge,strVacancies,strStartDate,strEndDate)
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
                            }else {
                                ((CompanyProfileActivity)currentActivity).showSnackBar(simpleResponse.getMessage(),false);
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
                    });


    }

    private void editJob(Integer jobId,String jobTitle,int sectorId,String description,CompanyJobResponse.Result currentJob) {
        if (sectorId == 0)
            try {
                sectorId = (currentJob.getSectorId());
            }catch (Exception e){

            }
        if (cityId == null ||  cityId == 0)
            cityId = currentJob.getCityId();
        if (!strJobTitle.isEmpty() && !strJobDescription.isEmpty() && !strAge.isEmpty()
                && !strStartDate.isEmpty() && !strEndDate.isEmpty() && cityId != null&& cityId != 0
                && sectorId != 0) {
            BzzApp.getBzHubRepository().updateCompanyJob(jobId,cityId,(sectorId),jobTitle,strSalary,description,strAge,strVacancies,
                    strStartDate,strEndDate)
                    .subscribe(new Observer<SimpleResponse>() {
                        Disposable mDispose;

                        @Override
                        public void onSubscribe(Disposable d) {
                            mDispose = d;
                        }

                        @Override
                        public void onNext(SimpleResponse simpleResponse) {
                            if (simpleResponse.getStatus() && simpleResponse.getCode() == 200){
                                ((CompanyProfileActivity)currentActivity).showSnackBar(simpleResponse.getMessage(),true);
                                getJobs();
                            }else {
                                ((CompanyProfileActivity)currentActivity).showSnackBar(simpleResponse.getMessage(),false);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            ((CompanyProfileActivity) currentActivity).onError(e);
                        }

                        @Override
                        public void onComplete() {
                            mDispose.dispose();
                        }
                    });
        }else {
            ((CompanyProfileActivity)currentActivity).onError(new Exception(currentActivity.getResources().getString(R.string.str_make_sure_valid_field)));
        }
    }

    private void removeJob(int jobId){
        BzzApp.getBzHubRepository().deleteCompanyJob((jobId))
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        if (simpleResponse.getStatus() && simpleResponse.getCode() == 200){
                            ((CompanyProfileActivity)currentActivity).showSnackBar(simpleResponse.getMessage(),true);
                            getJobs();
                        }else {
                            ((CompanyProfileActivity)currentActivity).showSnackBar(simpleResponse.getMessage(),false);
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
                        jobTypeList = (ArrayList<BusinessCategoryResponse.Result>) businessCategoryResponse.getResult();
                        jobTypeList.add(new BusinessCategoryResponse.Result(0,"- Select -",0));
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
                        jobCategoryList = (ArrayList<SubCategoryResponse.Result>) subCategoryResponse.getResult();
                        if (jobCategoryList != null){
                            jobCategoryList.add(new SubCategoryResponse.Result(0,0,"- Select -"
                                    ,"",0));

                            ArrayAdapter<SubCategoryResponse.Result> adapter =
                                    new ArrayAdapter<SubCategoryResponse.Result>(currentActivity , android.R.layout.simple_spinner_dropdown_item, jobCategoryList);
                            adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                            spinnerJobTCategory.setAdapter(adapter);
                            spinnerJobTCategory.setSelection(jobCategoryList.size()-1);


                            if  (mCurrentJob != null &&  mCurrentJob.getJobId() != null ){
                                for (int i = 0;i<jobCategoryList.size();i++){
                                    if (jobCategoryList.get(i).getSubId().equals(mCurrentJob.getSectorId())){
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

                    @Override
                    public void onError(Throwable e) {
                        ( (CompanyProfileActivity)currentActivity).onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                }
        );
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (getView() != null &&  isVisibleToUser){
            getJobs();
            getCategories();
        }
    }
}
