package smartdev.bzzhub.ui.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.CompanyMediaResponse;
import smartdev.bzzhub.repository.model.ProfileMediaResponse;
import smartdev.bzzhub.ui.activity.CompanyDetailActivity;
import smartdev.bzzhub.ui.adapter.ImageAdapter;
import smartdev.bzzhub.ui.adapter.VideoAdapter;
import smartdev.bzzhub.ui.base.BaseFragment;
import smartdev.bzzhub.util.navigation.NavigationManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static smartdev.bzzhub.util.navigation.Arg.ARG_URL_IMAGE;
import static smartdev.bzzhub.util.navigation.Arg.ARG_URL_VIDEO;

public class CompanyMediaFragment extends BaseFragment implements VideoAdapter.SelectedListener, ImageAdapter.SelectedListener{

    private int companyId;

    @BindView(R.id.recyclerView_video)
    RecyclerView recyclerViewVideo;
    @BindView(R.id.recyclerView_image)
    RecyclerView recyclerViewImage;

    ImageAdapter imageAdapter;
    VideoAdapter videoAdapter;
    @BindView(R.id.tv_video)
    TextView tvVideo;
    @BindView(R.id.tv_image)
    TextView tvImage;
    @BindView(R.id.tv_no_video)
    TextView tvNoVideo;
    @BindView(R.id.tv_no_image)
    TextView tvNoImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_media, container, false);
        ButterKnife.bind(this, view);

        extractVariables();
        initRecyclerViews();

        if ( isVisibleToUser){
            getData();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (getView() != null && isVisibleToUser){
            getData();
        }
    }

    private void extractVariables(){
        companyId = (((CompanyDetailActivity)currentActivity).companyId);
    }

    private void getData(){
        BzzApp.getBzHubRepository().getCompanyMedias(companyId).doOnNext(companyMediaResponse -> {
        }).subscribe(new Observer<ProfileMediaResponse>() {
            Disposable mDisposable;
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(ProfileMediaResponse companyMediaResponse) {
                if (companyMediaResponse.getResult() != null){
                    setData(companyMediaResponse.getResult());
                }else {
                    tvNoVideo.setVisibility(View.VISIBLE);
                    tvNoImage.setVisibility(View.VISIBLE);
                    recyclerViewVideo.setVisibility(View.GONE);
                    recyclerViewImage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                mDisposable.dispose();
            }
        });
    }

    private void initRecyclerViews(){
        recyclerViewImage.setLayoutManager(new GridLayoutManager(currentActivity,2));
        recyclerViewVideo.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL,false));
    }

    private void setData(ProfileMediaResponse.Result result){
        if (result.getImages() != null && !result.getImages().isEmpty()){
            recyclerViewImage.setVisibility(View.VISIBLE);
            tvNoImage.setVisibility(View.GONE);
            setImageRecyclerView(result.getImages());
        }else {
            tvNoImage.setVisibility(View.VISIBLE);
            recyclerViewImage.setVisibility(View.GONE);
        }
        if (result.getVideos() != null && !result.getVideos().isEmpty()){
           // tvVideo.setVisibility(View.VISIBLE);
            recyclerViewVideo.setVisibility(View.VISIBLE);
            setVideoRecyclerView(result.getVideos());
            tvNoVideo.setVisibility(View.GONE);
        }else {
       //     tvNoVideo.setVisibility(View.VISIBLE);
            recyclerViewVideo.setVisibility(View.GONE);
            tvNoVideo.setVisibility(View.VISIBLE);
        }

    }

    private void setImageRecyclerView(List<CompanyMediaResponse.Video> images){
        recyclerViewImage.setAdapter(new ImageAdapter(currentActivity,images, this));
    }

    private void setVideoRecyclerView(List<CompanyMediaResponse.Video> videos){
        recyclerViewVideo.setAdapter(new VideoAdapter(currentActivity,videos, this));
    }

    @Override
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

}
