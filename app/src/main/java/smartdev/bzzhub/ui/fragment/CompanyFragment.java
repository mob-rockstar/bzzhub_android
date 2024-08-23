package smartdev.bzzhub.ui.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.OnClick;
import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.CityResponse;
import smartdev.bzzhub.repository.model.Company;
import smartdev.bzzhub.repository.model.CountryResponse;
import smartdev.bzzhub.repository.model.EventBusMessage;
import smartdev.bzzhub.repository.model.MainPageResponse;
import smartdev.bzzhub.repository.model.SccidResponse;
import smartdev.bzzhub.repository.model.SelectorResponse;
import smartdev.bzzhub.repository.model.SimpleResponse;
import smartdev.bzzhub.ui.adapter.MainPageAdapter;
import smartdev.bzzhub.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import smartdev.bzzhub.util.Constant;


public class CompanyFragment extends BaseFragment implements MainPageAdapter.SelectedListener {

    private int pageId = 1;
    private int pageCount = 10;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private MainPageAdapter mainPageAdapter;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private  boolean isLoading = false;
    private  List<Company> companies = new ArrayList<>();
    private boolean loadMore = true;

    private ArrayList<SelectorResponse.Result> selectorList =new ArrayList<>();
    private  ArrayList<CountryResponse.Result> countries =new ArrayList<>();
    private  ArrayList<CityResponse.Result> cities =new ArrayList<>();
    private ArrayList<CityResponse.Result> allCities = new ArrayList<>();
    private Spinner spinnerCity;
    private ArrayAdapter<CityResponse.Result> cityAdapter;

    private String searchKey ;
    private Integer selectorId ;
    private Integer cityId ;
    private Integer countryId ;
    private Integer companyId;

    @BindView(R.id.tv_find_company)
    TextView tvFindCompany;
    @BindView(R.id.tv_no_result)
    TextView tvNoResults;
    @BindView(R.id.iv_close)
    ImageView ivClose;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(currentActivity));
        initScrollListener();
        extractArguments();
        progressBar.setVisibility(View.VISIBLE);
        getSCCIDResponse();
        mainPageAdapter =  new MainPageAdapter(currentActivity,companies,CompanyFragment.this);
        recyclerView.setAdapter(mainPageAdapter);
        return view;
    }

    private void extractArguments(){
        companyId = (Constant.getInstance().getLoginType() != null && Constant.getInstance().getLoginType() == 1 ? Constant.getInstance().getCompanyProfile().getCompanyId():0);
    }

    private void getData(){
        progressBar.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().getCompanyList(pageId,pageCount).doOnNext(mainPageResponse -> {})
                .subscribe(new Observer<MainPageResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(MainPageResponse mainPageResponse) {
                        progressBar.setVisibility(View.GONE);
                            companies = new ArrayList<>();

                        isLoading = mainPageResponse.getResult() == null || mainPageResponse.getResult().size() != 10;
                        if (mainPageResponse.getResult() != null){
                            if (loadMore ){
                                companies = (mainPageResponse.getResult());
                                loadMore = false;
                                pageId += 1;
                            }
                            else {
                                pageId = 1;
                                companies = mainPageResponse.getResult();
                            }
                        }
                        mainPageAdapter =  new MainPageAdapter(currentActivity,companies, CompanyFragment.this);
                        recyclerView.setAdapter(mainPageAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        CompanyFragment.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }


    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == companies.size() - 1) {
                        //bottom of list!
                        searchCompanyList();
                        isLoading = true;
                        loadMore = true;
                    }
                }
            }
        });
    }

    @OnClick(R.id.layout_search_bar) void onLayoutSearchClicked(){
       Dialog  dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_search_company, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        LinearLayout layoutMain = view.findViewById(R.id.layout_main);
        layoutMain.setOnClickListener(v -> dialog.dismiss());
        Spinner spinner= view.findViewById(R.id.spinner_piece);

        EditText editTextKeyword = view.findViewById(R.id.editttext_key_word);
        if (ivClose.getVisibility() == View.VISIBLE){
            editTextKeyword.setText(tvFindCompany.getText().toString().trim());
        }

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

        TextView tvSearchCompany = view.findViewById(R.id.tv_search_company);
        tvSearchCompany.setOnClickListener(v -> {

            searchKey = (editTextKeyword.getText().toString().trim());
            countryId =  countries.get(spinnerCountry.getSelectedItemPosition()) .getCountryId();
            cityId = cities.get(spinnerCity.getSelectedItemPosition()).getCityId();
            selectorId = selectorList.get(spinner.getSelectedItemPosition()).getCategoryId();
            pageId = 1;
            mainPageAdapter.setProducts(new ArrayList<>());
            companies = new ArrayList<>();
            searchCompanyList();
            tvFindCompany.setText(!searchKey.isEmpty() ? searchKey : currentActivity.getResources().getText(R.string.str_find_company));
            if (!searchKey.isEmpty()) {
                ivClose.setVisibility(View.VISIBLE);
            }else {
                ivClose.setVisibility(View.GONE);
            }
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

/*    private void getCountries(){
        BzzApp.getBzHubRepository().getCountries().subscribe(new Observer<CountryResponse>() {
            Disposable mDispose;
            @Override
            public void onSubscribe(Disposable d) {
                mDispose = d;
            }

            @Override
            public void onNext(CountryResponse selectorResponse) {
                if (selectorResponse.getResult() != null){
                    countries = (ArrayList<CountryResponse.Result>) selectorResponse.getResult();
                }
                CountryResponse.Result result = new CountryResponse.Result("","- Select -");
                countries.add(result);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                mDispose.dispose();
            }
        });
    }*/

/*
    private void getCityList(String countryId){
        BzzApp.getBzHubRepository().getCities(Integer.parseInt(countryId))
                .subscribe(new Observer<CityResponse>() {
                    Disposable mDispose;

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose =d;
                    }

                    @Override
                    public void onNext(CityResponse cityResponse) {
                        if (cityResponse.getResult() != null){
                            cities = (ArrayList<CityResponse.Result>) cityResponse.getResult();
                        }

                    setCityAdapter();
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
*/

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

/*    private void getSelectors(){
        BzzApp.getBzHubRepository().getSelectors().subscribe(new Observer<SelectorResponse>() {
            Disposable mDispose;
            @Override
            public void onSubscribe(Disposable d) {
                mDispose = d;
            }

            @Override
            public void onNext(SelectorResponse selectorResponse) {
                if (selectorResponse.getResult() != null){
                    selectorList = (ArrayList<SelectorResponse.Result>) selectorResponse.getResult();
                }
                SelectorResponse.Result result = new SelectorResponse.Result("","","- Select -","","");
                selectorList.add(result);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                mDispose.dispose();
            }
        });
    }*/

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
                if (companyId != null ){
                    searchCompanyList();
                }else {
                    getData();
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

    private void searchCompanyList(){
        progressBar.setVisibility(View.VISIBLE);
        try {
            currentActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }catch (Exception e){

        }
        BzzApp.getBzHubRepository().searchCompany(pageId,10,searchKey,selectorId,cityId,countryId,companyId)
                .subscribe(new Observer<MainPageResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(MainPageResponse mainPageResponse) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            currentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }catch (Exception ignored){

                        }

                        companies = new ArrayList<>();
                        isLoading = mainPageResponse.getResult() == null || mainPageResponse.getResult().size() != 10;

                        if (mainPageResponse.getResult() != null){
                            companies =(mainPageResponse.getResult());
                            if (loadMore ){
                                loadMore = false;
                                pageId += 1;
                            }
                        }

                        mainPageAdapter.addProducts(companies);

                        if (mainPageAdapter.getItemCount() == 0)
                            tvNoResults.setVisibility(View.VISIBLE);
                        else
                            tvNoResults.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            currentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }catch (Exception e1){

                        }

                        CompanyFragment.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    @OnClick(R.id.iv_close) void onCloseClicked(){
        tvFindCompany.setText("");
        ivClose.setVisibility(View.GONE);
        getData();
    }

    @Override
    public void onConnectClicked(Company company) {
        connectCompany(company);
    }

    private void connectCompany(Company company){
        progressBar.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().connectToCompany(Constant.getInstance().getCompanyProfile().getCompanyId(),company.getCompanyId())
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose  =d ;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        progressBar.setVisibility(View.GONE);
                        if (simpleResponse.getStatus() != null && simpleResponse.getStatus()){
                        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage messageEvent) {
        if (messageEvent != null) {
            int messageType = messageEvent.getMessageType();
            if (messageType == EventBusMessage.MessageType.CompanyConnectionChanged){
                pageId = 1;
                searchKey = "";
                if (companyId != null){
                    mainPageAdapter.setProducts(new ArrayList<>());
                    searchCompanyList();
                }else {
                    getData();
                }
            }
        }
    }
}
