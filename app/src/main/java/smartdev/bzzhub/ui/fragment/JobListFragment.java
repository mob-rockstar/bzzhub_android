package smartdev.bzzhub.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
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
import smartdev.bzzhub.repository.model.CityResponse;
import smartdev.bzzhub.repository.model.CountryResponse;
import smartdev.bzzhub.repository.model.JobResponse;
import smartdev.bzzhub.repository.model.SccidResponse;
import smartdev.bzzhub.repository.model.SelectorResponse;
import smartdev.bzzhub.ui.activity.ApplicantsJobActivity;
import smartdev.bzzhub.ui.activity.JobDetailActivity;
import smartdev.bzzhub.ui.adapter.JobAdapter;
import smartdev.bzzhub.ui.adapter.MainPageAdapter;
import smartdev.bzzhub.ui.base.BaseFragment;
import smartdev.bzzhub.util.Constant;
import smartdev.bzzhub.util.navigation.Arg;

public class JobListFragment extends BaseFragment implements JobAdapter.SelectedListener{

    private int pageId = 1;
    private int pageCount = 10;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private  boolean isLoading = true;
    private boolean loadMore = true;
    private List<JobResponse.Result> jobs = new ArrayList<>();

    private JobAdapter jobAdapter;

    @BindView(R.id.tv_find_job)
    TextView tvFindCompany;
    @BindView(R.id.tv_no_result)
    TextView tvNoResults;
    int currentTabPosition;
    private ArrayList<SelectorResponse.Result> selectorList =new ArrayList<>();
    private  ArrayList<CountryResponse.Result> countries =new ArrayList<>();
    private  ArrayList<CityResponse.Result> cities =new ArrayList<>();
    private ArrayList<CityResponse.Result> allCities = new ArrayList<>();
    private Spinner spinnerCity;
    private ArrayAdapter<CityResponse.Result> cityAdapter;

    private String searchKey = "";
    private Integer selectorId = 0;
    private Integer cityId = 0;
    private Integer countryId = 0;
    private Integer companyId;

    @BindView(R.id.iv_close)
    ImageView ivClose;
    boolean search = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_list, container, false);
        ButterKnife.bind(this, view);
        getSCCIDResponse();
        initGridView();
        progressBar.setVisibility(View.VISIBLE);
        getJobLists();

        return view;
    }


    private void initGridView(){
        recyclerView.setLayoutManager(new GridLayoutManager(currentActivity,2));
        jobAdapter =  new JobAdapter(currentActivity,jobs,this::onSelected);
        recyclerView.setAdapter(jobAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (search &&  !isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == jobAdapter.getItemCount() - 1) {
                        //bottom of list!
                        if (currentTabPosition == 0){
                            searchApplicantJobs();
                        }else if (currentTabPosition == 1){
                            searchCompanyJobs();
                        }
                        isLoading = true;
                        loadMore = true;
                    }
                }
            }
        });
    }

    private void getJobLists(){
        search = false;
        BzzApp.getBzHubRepository().getAllJobList(pageId).
                subscribe(new Observer<JobResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(JobResponse jobResponse) {
                        progressBar.setVisibility(View.GONE);
                        jobs = new ArrayList<>();
                        isLoading = jobResponse.getResult() == null || jobResponse.getResult().size() != 10;

                        if (jobResponse.getResult() != null){
                            jobs =(jobResponse.getResult());
                            if (loadMore ){
                                loadMore = false;
                                pageId += 1;
                            }

                        }
                        jobAdapter.setJobs(jobs);
                        recyclerView.setAdapter(jobAdapter);
                        if (jobAdapter.getItemCount() == 0)
                            tvNoResults.setVisibility(View.VISIBLE);
                        else
                            tvNoResults.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    @Override
    public void onSelected(JobResponse.Result job) {
        if (job.getUserId() != null){
            Intent intent = new Intent(currentActivity, ApplicantsJobActivity.class);
            intent.putExtra(Arg.ARG_JOB_DETAIL,(Serializable) job);
            startActivity(intent);
        }else {
            Intent intent = new Intent(currentActivity, JobDetailActivity.class);
            intent.putExtra(Arg.ARG_JOB_DETAIL,(Serializable) job);
            startActivity(intent);
        }

    }

    @OnClick(R.id.layout_search_bar) void onLayoutSearchClicked(){
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_search_jobs, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

       LinearLayout layoutMain = view.findViewById(R.id.layout_main);
        layoutMain.setOnClickListener(v -> dialog.dismiss());
         Spinner spinner= view.findViewById(R.id.spinner_piece);
        currentTabPosition = 0;
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                currentTabPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        EditText editTextKeyword = view.findViewById(R.id.editttext_key_word);
        ArrayAdapter<SelectorResponse.Result> adapter =
                new ArrayAdapter<SelectorResponse.Result>(currentActivity , android.R.layout.simple_spinner_dropdown_item, selectorList);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(selectorList.size()-1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectorId = selectorList.get(position).getCategoryId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinnerCountry = view.findViewById(R.id.spinner_country);
        ArrayAdapter<CountryResponse.Result> countrySpinnerAdapter =
                new ArrayAdapter<CountryResponse.Result>(currentActivity , android.R.layout.simple_spinner_dropdown_item, countries);
        countrySpinnerAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countrySpinnerAdapter);
        spinnerCountry.setSelection(countries.size()-1);

        spinnerCity = view.findViewById(R.id.spinner_city);
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

        if (ivClose.getVisibility() == View.VISIBLE){
            editTextKeyword.setText(tvFindCompany.getText().toString());
        }

        TextView tvSearchCompany = view.findViewById(R.id.tv_search_company);
        tvSearchCompany.setOnClickListener(v -> {

            searchKey = editTextKeyword.getText().toString().trim();
            countryId =  countries.get(spinnerCountry.getSelectedItemPosition()) .getCountryId();
            cityId = cities.get(spinnerCity.getSelectedItemPosition()).getCityId();
            selectorId = selectorList.get(spinner.getSelectedItemPosition()).getCategoryId();
            pageId = 1;
            jobAdapter.setJobs(new ArrayList<>());
            jobs = new ArrayList<>();
            if (currentTabPosition == 0){

                searchApplicantJobs();
            }else if (currentTabPosition == 1){
                searchCompanyJobs();
            }
        //    searchCompanyList();
            tvFindCompany.setText(!searchKey.isEmpty() ? searchKey : currentActivity.getResources().getText(R.string.str_find_company));
            if (searchKey.isEmpty()){
                ivClose.setVisibility(View.GONE);
            }else ivClose.setVisibility(View.VISIBLE);
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

    private void filterCities(Integer countryId){
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
    }

    private void getSCCIDResponse(){
        BzzApp.getBzHubRepository().getSCCIDResponse().subscribe(new Observer<SccidResponse>() {
            Disposable mDispose;
            @Override
            public void onSubscribe(Disposable d) {
                mDispose = d;
            }

            @Override
            public void onNext(SccidResponse sccidResponse) {
                if (sccidResponse != null && sccidResponse.getResult() != null){
                    selectorList = (ArrayList<SelectorResponse.Result>) sccidResponse.getResult().getSectors();
                    selectorList.add(new SelectorResponse.Result(0,0,"- Select -","",0));
                    Constant.getInstance().setSelectorList(selectorList);
                    countries = (ArrayList<CountryResponse.Result>) sccidResponse.getResult().getCountries();
                    allCities = (ArrayList<CityResponse.Result>) sccidResponse.getResult().getCities();
                    CountryResponse.Result result = new CountryResponse.Result(0,"- Select -");
                    countries.add(result);
                    Constant.getInstance().setCountries(countries);
                    Constant.getInstance().setCities(allCities);
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

    private void searchCompanyJobs(){
        search = true;
        BzzApp.getBzHubRepository().searchCompanyJobs(searchKey,selectorId,cityId ,countryId ,pageId)
                .subscribe(new Observer<JobResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(JobResponse jobResponse) {
                        jobs = new ArrayList<>();
                        isLoading = jobResponse.getResult() == null || jobResponse.getResult().size() != 10;

                        if (jobResponse.getResult() != null){
                            jobs =(jobResponse.getResult());
                            if (loadMore ){
                                loadMore = false;
                                pageId += 1;
                            }

                        }
                        if (pageId == 1) jobAdapter = new JobAdapter(currentActivity,new ArrayList<>(),JobListFragment.this::onSelected);
                        jobAdapter.addJobs(jobs);
                        recyclerView.setAdapter(jobAdapter);
                        if (jobAdapter.getItemCount() == 0)
                            tvNoResults.setVisibility(View.VISIBLE);
                        else
                            tvNoResults.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        JobListFragment.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }



    private void searchApplicantJobs(){
        search = true;
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().searchApplicantJobs(searchKey,selectorId,cityId,countryId,pageId)
                .subscribe(new Observer<JobResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(JobResponse jobResponse) {
                        progressBar.setVisibility(View.GONE);
                        jobs = new ArrayList<>();
                        isLoading = jobResponse.getResult() == null || jobResponse.getResult().size() != 10;

                        if (jobResponse.getResult() != null){
                            jobs =(jobResponse.getResult());
                            if (loadMore ){
                                loadMore = false;
                                pageId += 1;
                            }
                        }

                        if (pageId == 1){
                            jobAdapter.setJobs(new ArrayList<>());
                        }
                        jobAdapter.addJobs(jobs);
                        recyclerView.setAdapter(jobAdapter);

                        if (jobAdapter.getItemCount() == 0)
                            tvNoResults.setVisibility(View.VISIBLE);
                        else
                            tvNoResults.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        JobListFragment.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    @OnClick(R.id.iv_close)
    void onCancelClicked(){
        tvFindCompany.setText(currentActivity.getResources().getString(R.string.str_find_job));
        ivClose.setVisibility(View.GONE);
        getJobLists();
    }
}
