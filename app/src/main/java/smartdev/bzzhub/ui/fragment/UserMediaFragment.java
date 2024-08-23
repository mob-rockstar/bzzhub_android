package smartdev.bzzhub.ui.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
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
import smartdev.bzzhub.repository.model.CompanyMediaResponse;
import smartdev.bzzhub.repository.model.ProfileMediaResponse;
import smartdev.bzzhub.repository.model.SimpleResponse;
import smartdev.bzzhub.ui.activity.UserProfileActivity;
import smartdev.bzzhub.ui.adapter.UserProfileImageAdapter;
import smartdev.bzzhub.ui.adapter.UserProfileVideoAdapter;
import smartdev.bzzhub.ui.base.BaseFragment;
import smartdev.bzzhub.util.Constant;
import smartdev.bzzhub.util.navigation.NavigationManager;

import static smartdev.bzzhub.util.navigation.Arg.ARG_URL_IMAGE;
import static smartdev.bzzhub.util.navigation.Arg.ARG_URL_VIDEO;

public class UserMediaFragment extends BaseFragment implements UserProfileImageAdapter.SelectedListener, UserProfileVideoAdapter.SelectedListener {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.recyclerView_video)
    RecyclerView recyclerViewVideo;
    @BindView(R.id.recyclerView_image)
    RecyclerView recyclerViewImage;

    @BindView(R.id.layout_add_image)
    RelativeLayout layoutAddImage;
    @BindView(R.id.layout_add_video)
    RelativeLayout layoutAddVideo;

    private String strKeyword,strUrl;
    private File file;
    private Integer isPrivate = 0;
    private TextView tvSelectFile;
    @BindView(R.id.tv_no_video)
    TextView tvNoVideo;
    @BindView(R.id.tv_no_image)
    TextView tvNoImage;

    private EasyImage easyImage;
    private int imageRequestCode = 1001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_media, container, false);
        ButterKnife.bind(this, view);
        initRecyclerViews();

        // Define Easy Image
        easyImage =
                new  EasyImage.Builder(getActivity()).setCopyImagesToPublicGalleryFolder(false)
                        .allowMultiple(false)
                        .build();

        if (isVisibleToUser){
            getData();
        }

        if( ((UserProfileActivity)currentActivity).isOtherUser){
            layoutAddImage.setVisibility(View.GONE);
            layoutAddVideo.setVisibility(View.GONE);
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

    private void initRecyclerViews(){
        recyclerViewImage.setLayoutManager(new GridLayoutManager(currentActivity,2));
        recyclerViewVideo.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL,false));
    }

    private void getData(){
        progressBar.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().getUserMediaList((
                (UserProfileActivity)currentActivity).mUserId)
                .subscribe(new Observer<ProfileMediaResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(ProfileMediaResponse companyMediaResponse) {
                        progressBar.setVisibility(View.GONE);
                        if (companyMediaResponse .getResult() != null){
                            if (companyMediaResponse.getResult().getImages() != null){
                                recyclerViewImage.setVisibility(View.VISIBLE);
                                tvNoImage.setVisibility(View.GONE);
                                setRecyclerViewImage(companyMediaResponse.getResult().getImages());
                            }else {
                                recyclerViewImage.setVisibility(View.GONE);
                                tvNoImage.setVisibility(View.VISIBLE);

                            }
                            if (companyMediaResponse.getResult().getVideos() != null){
                                recyclerViewVideo.setVisibility(View.VISIBLE);
                                tvNoVideo.setVisibility(View.GONE);
                                setVideoRecyclerView(companyMediaResponse.getResult().getVideos());
                            }else {
                                recyclerViewVideo.setVisibility(View.GONE);
                                tvNoVideo.setVisibility(View.VISIBLE);
                            }
                        }else {
                            recyclerViewImage.setVisibility(View.GONE);
                            recyclerViewVideo.setVisibility(View.GONE);
                            tvNoImage.setVisibility(View.VISIBLE);
                            tvNoVideo.setVisibility(View.VISIBLE);
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

    private void setVideoRecyclerView(List<CompanyMediaResponse.Video> videos){
        recyclerViewVideo.setAdapter(new UserProfileVideoAdapter(currentActivity,videos,this));
    }

    private void setRecyclerViewImage(List<CompanyMediaResponse.Video> images){
        try {
            recyclerViewImage.setAdapter(new UserProfileImageAdapter(currentActivity,images,this));
        }catch (Exception ignored){

        }
    }

    @OnClick(R.id.layout_add_image) void onAddImageClicked(){
        showAddImageDialog();
    }

    @OnClick(R.id.layout_add_video) void onAddVideoClicked(){
        showAddVideoDialog();
    }

    private void showAddImageDialog(){
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_upload_media_photo, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        strKeyword = "";
        EditText editText = dialog.findViewById(R.id.edittext_description);
        RelativeLayout layoutSelectFile = dialog.findViewById(R.id.layout_select_file);
        layoutSelectFile.setOnClickListener(v -> {
            RxPermissions permissions = new RxPermissions(this);
            Disposable subscribe = permissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
            if (granted) {
                easyImage.openChooser(this);
            }
        });
    });
        tvSelectFile = dialog.findViewById(R.id.tv_select_file);

        TextView tvUpload = dialog.findViewById(R.id.tv_upload);
            tvUpload.setOnClickListener(v -> {
            strKeyword = editText.getText().toString().trim();
            if (strKeyword.isEmpty()){
                ((UserProfileActivity)currentActivity).onError(new Exception(currentActivity.getResources().getString(R.string.str_empty_keyword)));
            }else if (file == null){
                ((UserProfileActivity)currentActivity).onError(new Exception(currentActivity.getResources().getString(R.string.str_empty_file)));
            }else{
                addImage(strKeyword,0,0,file);
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

    private void showAddVideoDialog(){
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_upload_video, null);
        dialog.getWindow();

        strKeyword = "";
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText editTextKeyword = dialog.findViewById(R.id.edittext_keyword);

        AppCompatCheckBox checkboxPublic = dialog.findViewById(R.id.checkbox_public);
        AppCompatCheckBox checkBoxYoutube = dialog.findViewById(R.id.checkbox_youtube);

        LinearLayout layoutPublicVideo = dialog.findViewById(R.id.layout_public_video);
        layoutPublicVideo.setOnClickListener(v -> {
            checkboxPublic.performClick();
        });
        EditText url =dialog.findViewById(R.id.edittext_url);
        TextView tvAdd = dialog.findViewById(R.id.tv_add);
        checkboxPublic.setOnCheckedChangeListener((buttonView, isChecked) -> {
             isPrivate = checkboxPublic.isSelected()?0:1;
        });

        tvAdd.setOnClickListener(v ->  {
            strKeyword = editTextKeyword.getText().toString().trim();
            int flag = 0;
            strUrl = url.getText().toString().trim();
            if (strUrl.isEmpty()){
                ((UserProfileActivity)currentActivity).onError(new Exception(currentActivity.getResources().getString(R.string.str_empty_URL)));
            }else {
                addVideo(isPrivate);
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

    @Override
    public void onRemoveClicked(Integer mediaId) {
        showDeleteFeedDialog(mediaId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        easyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                try {
                    file = imageFiles[0].getFile();
                    if (tvSelectFile != null && file != null && file.getName() != null) {
                        tvSelectFile.setText(file.getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

    private void removeMedia(Integer mediaId){
        progressBar.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().removeMedia(mediaId)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        progressBar.setVisibility(View.GONE);
                        getData();
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

    private void addImage(String strKeyword, Integer flag, Integer isPrivate, File file){
        progressBar.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().uploadImage(strKeyword,flag,isPrivate,file)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        progressBar.setVisibility(View.GONE);
                        if (simpleResponse.getCode() == 200){
                            getData();
                        }else {
                            ((UserProfileActivity)currentActivity).onError(new Exception(simpleResponse.getMessage()));
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

    private void addVideo(int isPrivate){
        BzzApp.getBzHubRepository().uploadVideo(strKeyword,0,strUrl,isPrivate)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        if (simpleResponse.getCode() == 200){
                            getData();
                        }else {
                            ((UserProfileActivity)currentActivity).onError(new Exception(simpleResponse.getMessage()));
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

    @Override
    public void onImageSelected(String imageURL) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_URL_IMAGE,imageURL);
        NavigationManager.gotoImageActivity(currentActivity,bundle);
    }

    @Override
    public void onVideoSelected(String URL) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_URL_VIDEO,URL);
        NavigationManager.gotoVideoActivity(currentActivity,bundle);
    }

    private void showDeleteFeedDialog(Integer mediaId){
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_delete_media, null);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvYes = dialog.findViewById(R.id.tv_yes);
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        tvYes.setOnClickListener(v -> {
            dialog.dismiss();
            removeMedia((mediaId));
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

}
