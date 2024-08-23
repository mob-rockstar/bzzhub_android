package smartdev.bzzhub.ui.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.provider.MediaStore;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

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
import smartdev.bzzhub.repository.model.AllCompanyResponse;
import smartdev.bzzhub.repository.model.CityResponse;
import smartdev.bzzhub.repository.model.CompanyMediaResponse;
import smartdev.bzzhub.repository.model.CountryResponse;
import smartdev.bzzhub.repository.model.InterestResponse;
import smartdev.bzzhub.repository.model.JobResponse;
import smartdev.bzzhub.repository.model.MediaSearchReponse;
import smartdev.bzzhub.repository.model.ProfileMediaResponse;
import smartdev.bzzhub.repository.model.SccidResponse;
import smartdev.bzzhub.repository.model.SelectorResponse;
import smartdev.bzzhub.repository.network.BzHubRepository;
import smartdev.bzzhub.ui.adapter.ImageAdapter;
import smartdev.bzzhub.ui.adapter.JobAdapter;
import smartdev.bzzhub.ui.adapter.VideoAdapter;
import smartdev.bzzhub.ui.base.BaseFragment;
import smartdev.bzzhub.util.Constant;
import smartdev.bzzhub.util.navigation.NavigationManager;

import static smartdev.bzzhub.util.navigation.Arg.ARG_URL_IMAGE;
import static smartdev.bzzhub.util.navigation.Arg.ARG_URL_VIDEO;

public class MediaListFragment extends BaseFragment implements VideoAdapter.SelectedListener, ImageAdapter.SelectedListener {

    private int pageId = 1;
    private int pageCount = 10;

    @BindView(R.id.recyclerView_image)
    RecyclerView recyclerViewImage;
    @BindView(R.id.recyclerView_video)
    RecyclerView recyclerViewVideo;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private  boolean isLoading = false;
    private boolean loadMore = true;
    private List<JobResponse.Result> jobs = new ArrayList<>();

    private JobAdapter jobAdapter;

    @BindView(R.id.tv_find_media)
    TextView tvFindCompany;
    int currentTabPosition;

    private List<InterestResponse.Result> interests;
    private ArrayList<SelectorResponse.Result> selectorList =new ArrayList<>();
    private  ArrayList<CountryResponse.Result> countries =new ArrayList<>();
    private  ArrayList<CityResponse.Result> cities =new ArrayList<>();
    private ArrayList<CityResponse.Result> allCities = new ArrayList<>();
    private Spinner spinnerCity;
    private Spinner spinnerInterest;
    private ArrayAdapter<CityResponse.Result> cityAdapter;
    private ArrayAdapter<InterestResponse.Result> interestAdapter;
    private ArrayList<AllCompanyResponse.Result> allCompanies = new ArrayList<>();

    private String searchKey = "";
    private Integer selectorId = 0;
    private Integer cityId = 0;
    private Integer countryId = 0;
    private Integer companyId = 0;
    private Integer interestID = 0;
    private int isImageOrVideo = 0;

    @BindView(R.id.tv_no_video)
    TextView tvNoVideo;
    @BindView(R.id.tv_no_image)
    TextView tvNoImage;
    List<CompanyMediaResponse.Video> mVideoList = new ArrayList<>();
    List<CompanyMediaResponse.Video> mImageList = new ArrayList<>();

    @BindView(R.id.iv_close)
    ImageView ivClose;

    @BindView(R.id.layout_image)
    LinearLayout layoutImage;
    @BindView(R.id.layout_video)
    LinearLayout layoutVideo;

    @BindView(R.id.tv_image)
    TextView tvImage;
    @BindView(R.id.tv_video)
    TextView tvVideo;

    ImageAdapter imageAdapter ;
    VideoAdapter videoAdapter;

    boolean search = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_list, container, false);
        ButterKnife.bind(this, view);

        getSCCIDResponse();
        getAllInterest();
        getAllCompanies();
        initGridView();


        progressBar.setVisibility(View.VISIBLE);
        return view;
    }

    private void initGridView(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(currentActivity,2);
        recyclerViewImage.setLayoutManager(gridLayoutManager);
        recyclerViewVideo.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL,false));

        recyclerViewVideo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (search &&  !isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == videoAdapter.getItemCount() - 1) {
                        //bottom of list!
                        searchMedias();
                        isLoading = true;
                        loadMore = true;
                    }
                }
            }
        });

        recyclerViewImage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (search && !isLoading) {
                    if (gridLayoutManager != null && gridLayoutManager.findLastCompletelyVisibleItemPosition() == imageAdapter.getItemCount() - 1) {
                        //bottom of list!
                        searchMedias();
                        isLoading = true;
                        loadMore = true;
                    }
                }
            }
        });
    }

    private void getAllInterest(){
        interests = new ArrayList<>();
        interests.add(new InterestResponse.Result(0,"- Select -"));
        BzzApp.getBzHubRepository().getInterestList().subscribe(
                new Observer<InterestResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(InterestResponse interestResponse) {
                        if (interestResponse.getStatus() && interestResponse.getCode() == 200){
                            interests.addAll(interestResponse.getResult()) ;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        MediaListFragment.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                }
        );
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
                if (sccidResponse != null && sccidResponse.getStatus() && sccidResponse.getCode() == 200 && sccidResponse.getResult() != null){
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

                getAllMedias();
            }

            @Override
            public void onError(Throwable e) {
                Log.d("ErrorBody",e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {
                mDispose.dispose();
            }
        });
    }

    @OnClick(R.id.layout_search_bar) void onLayoutSearchClicked(){
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_search_media, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        LinearLayout layoutMain = view.findViewById(R.id.layout_main);
        layoutMain.setOnClickListener(v -> dialog.dismiss());
        Spinner spinner= view.findViewById(R.id.spinner_piece);
        Spinner spinnerCompanyy = view.findViewById(R.id.spinner_company);
        currentTabPosition = 0;
        isImageOrVideo = 0;
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                isImageOrVideo = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        EditText editTextKeyword = view.findViewById(R.id.editttext_key_word);

        ArrayAdapter<AllCompanyResponse.Result> companyAdapter =
                new ArrayAdapter<AllCompanyResponse.Result>(currentActivity , android.R.layout.simple_spinner_dropdown_item, allCompanies);
        companyAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerCompanyy.setAdapter(companyAdapter);
        spinnerCompanyy.setSelection(allCompanies.size()-1);

        spinnerCompanyy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                companyId = allCompanies.get(position).getCompanyId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

        spinnerInterest = view.findViewById(R.id.spinner_interest);
        interestAdapter =
                new ArrayAdapter<InterestResponse.Result>(currentActivity , android.R.layout.simple_spinner_dropdown_item, interests);
        interestAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerInterest.setAdapter(interestAdapter);
        spinnerInterest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                interestID = interests.get(position).getInterestId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

            if (currentTabPosition == 0){
          //      searchApplicantJobs();
            }else if (currentTabPosition == 1){
       //         searchCompanyJobs();
            }

            searchMedias();
            //    searchCompanyList();
            tvFindCompany.setText(!searchKey.isEmpty() ? searchKey : currentActivity.getResources().getText(R.string.str_find_company));
            if (searchKey.isEmpty()){
                ivClose.setVisibility(View.GONE);
            }else {
                ivClose.setVisibility(View.VISIBLE);
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

    private void getAllCompanies(){
        BzzApp.getBzHubRepository().getAllCompanyList().subscribe(
                new Observer<AllCompanyResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(AllCompanyResponse allCompanyResponse) {
                        allCompanies = (ArrayList<AllCompanyResponse.Result>) allCompanyResponse.getResult();
                        allCompanies.add(new AllCompanyResponse.Result(0,"- Select -"));
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                }
        );
    }

    private void getAllMedias(){
        search = false;
        pageId = 1;
        BzzApp.getBzHubRepository().getAllMedias().subscribe(
                new Observer<ProfileMediaResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(ProfileMediaResponse companyMediaResponse) {
                        progressBar.setVisibility(View.GONE);
                        if (companyMediaResponse.getResult() != null){
                            setData(companyMediaResponse.getResult());
                        }else {
                            tvNoVideo.setVisibility(View.VISIBLE);
                            tvNoImage.setVisibility(View.VISIBLE);
                            recyclerViewVideo.setVisibility(View.GONE);
                            recyclerViewImage.setVisibility(View.GONE);
                        }

                        layoutVideo.setVisibility(View.VISIBLE);
                        layoutImage.setVisibility(View.VISIBLE);
                        tvImage.setText(currentActivity.getResources().getString(R.string.str_images));
                        tvVideo.setText(currentActivity.getResources().getString(R.string.str_video));
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        Log.d("MediaError",e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                }
        );
    }

    private void setData(ProfileMediaResponse.Result result){
        List<CompanyMediaResponse.Video> images = new ArrayList<>();
        List<CompanyMediaResponse.Video> videos = new ArrayList<>();
        images = result.getImages();
        videos = result.getVideos();
        if (!images.isEmpty()){
            recyclerViewImage.setVisibility(View.VISIBLE);
            tvNoImage.setVisibility(View.GONE);
            setImageRecyclerView(images);
        }else {
            tvNoImage.setVisibility(View.VISIBLE);
            recyclerViewImage.setVisibility(View.GONE);
        }
        if (!videos.isEmpty()){
            // tvVideo.setVisibility(View.VISIBLE);
            recyclerViewVideo.setVisibility(View.VISIBLE);
            setVideoRecyclerView(videos);
            tvNoVideo.setVisibility(View.GONE);
        }else {
            //     tvNoVideo.setVisibility(View.VISIBLE);
            recyclerViewVideo.setVisibility(View.GONE);
            tvNoVideo.setVisibility(View.VISIBLE);
        }
    }

    private void setImageRecyclerView(List<CompanyMediaResponse.Video> images){
        if (imageAdapter == null || pageId == 1){
            imageAdapter = new ImageAdapter(currentActivity,images, this);
        }else {
            imageAdapter.addItems(images);
        }

        if (imageAdapter.getItemCount() != 0){
            recyclerViewImage.setVisibility(View.VISIBLE);
            tvNoImage.setVisibility(View.GONE);
            recyclerViewImage.setAdapter(imageAdapter);
        }else {
            tvNoImage.setVisibility(View.VISIBLE);
            recyclerViewImage.setVisibility(View.GONE);
        }
    }

    private void setVideoRecyclerView(List<CompanyMediaResponse.Video> videos){
        if (videoAdapter == null || pageId == 1){
            videoAdapter = new VideoAdapter(currentActivity,videos, this);
        }else {
            videoAdapter.addItems(videos);
        }
        if (videoAdapter.getItemCount() != 0){
            // tvVideo.setVisibility(View.VISIBLE);
            recyclerViewVideo.setVisibility(View.VISIBLE);
            recyclerViewVideo.setAdapter(videoAdapter);
            tvNoVideo.setVisibility(View.GONE);
        }else {
            //     tvNoVideo.setVisibility(View.VISIBLE);
            recyclerViewVideo.setVisibility(View.GONE);
            tvNoVideo.setVisibility(View.VISIBLE);
        }

    }

    public void onSelected(String URL) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_URL_VIDEO,URL);
        NavigationManager.gotoVideoActivity(currentActivity,bundle);
    }

    @Override
    public void onImageSelected(String imageURL) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_URL_IMAGE,imageURL);
        NavigationManager.gotoImageActivity(currentActivity,bundle);
    }

    private void searchMedias(){
        search = true;
        progressBar.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().searchMedias(isImageOrVideo,pageId,pageCount,searchKey,selectorId,interestID,
                cityId,countryId,companyId,pageId).subscribe(
                new Observer<MediaSearchReponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(MediaSearchReponse companyMediaResponse) {
                        progressBar.setVisibility(View.GONE);
                        if (companyMediaResponse.getResult() != null){
                            isLoading = companyMediaResponse.getResult() == null || companyMediaResponse.getResult().size() != pageCount;
                            if (isImageOrVideo == 0){
                                setImageRecyclerView(companyMediaResponse.getResult());
                                setVideoRecyclerView(mVideoList);
                                layoutVideo.setVisibility(View.GONE);
                                layoutImage.setVisibility(View.VISIBLE);
                                tvImage.setText(currentActivity.getResources().getString(R.string.str_search_results));
                                tvVideo.setText(currentActivity.getResources().getString(R.string.str_video));
                            }else if (isImageOrVideo == 1){
                                setVideoRecyclerView(companyMediaResponse.getResult());
                                setImageRecyclerView(mImageList);
                                layoutVideo.setVisibility(View.VISIBLE);
                                layoutImage.setVisibility(View.GONE);
                                tvImage.setText(currentActivity.getResources().getString(R.string.str_images));
                                tvVideo.setText(currentActivity.getResources().getString(R.string.str_search_results));
                            }
                            if (loadMore ){
                                loadMore = false;
                                pageId += 1;
                            }
                        }else {
                            if (isImageOrVideo == 0){
                                setImageRecyclerView(new ArrayList<>());
                                setVideoRecyclerView(mVideoList);
                                layoutVideo.setVisibility(View.GONE);
                                layoutImage.setVisibility(View.VISIBLE);
                                tvImage.setText(currentActivity.getResources().getString(R.string.str_search_results));
                                tvVideo.setText(currentActivity.getResources().getString(R.string.str_video));
                            }else if (isImageOrVideo == 1){
                                setVideoRecyclerView(new ArrayList<>());
                                setImageRecyclerView(mImageList);
                                layoutVideo.setVisibility(View.VISIBLE);
                                layoutImage.setVisibility(View.GONE);
                                tvImage.setText(currentActivity.getResources().getString(R.string.str_images));
                                tvVideo.setText(currentActivity.getResources().getString(R.string.str_search_results));
                            }
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
                }
        );
    }

    @OnClick(R.id.iv_close)
    void onCloseClicked(){
        tvFindCompany.setText(currentActivity.getResources().getString(R.string.str_find_media));
        ivClose.setVisibility(View.GONE);
        getAllMedias();
    }
}
