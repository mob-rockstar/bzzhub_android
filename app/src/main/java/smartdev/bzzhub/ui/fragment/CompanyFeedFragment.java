package smartdev.bzzhub.ui.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;
import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.SimpleResponse;
import smartdev.bzzhub.repository.model.UserProfileFeedResponse;
import smartdev.bzzhub.ui.activity.CompanyProfileActivity;
import smartdev.bzzhub.ui.activity.UserProfileActivity;
import smartdev.bzzhub.ui.adapter.UserFeedAdapter;
import smartdev.bzzhub.ui.base.BaseFragment;
import smartdev.bzzhub.util.Constant;

public class CompanyFeedFragment  extends BaseFragment implements UserFeedAdapter.SelectedListener{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private UserFeedAdapter userFeedAdapter;
    private File file;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    int pageId  = 0;
    TextView tvFile;

    private EasyImage easyImage;
    private int imageRequestCode = 1001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_feed, container, false);
        ButterKnife.bind(this, view);

        // Define Easy Image
        easyImage =
                new  EasyImage.Builder(getActivity()).setCopyImagesToPublicGalleryFolder(false)
                        .allowMultiple(false)
                        .build();

        initRecyclerView();

        return view;
    }

    private void initRecyclerView(){
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(currentActivity,2));
    }

    private void getUserFeed(){
        BzzApp.getBzHubRepository().getUserFeed((Constant.getInstance().getCompanyProfile().getCompanyId()),1
                ,1,10).subscribe(new Observer<UserProfileFeedResponse>() {
            Disposable mDispose;
            @Override
            public void onSubscribe(Disposable d) {
                mDispose = d;
            }

            @Override
            public void onNext(UserProfileFeedResponse userProfileFeedResponse) {
                if (userProfileFeedResponse.getStatus() && userProfileFeedResponse.getCode() == 200) {
                    setDataToView(userProfileFeedResponse.getResult());
                }else {
                    setDataToView(new ArrayList<UserProfileFeedResponse.Result>());
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

    private void setDataToView(List<UserProfileFeedResponse.Result> results){
        userFeedAdapter = new UserFeedAdapter(currentActivity,results,this);
        recyclerView.setAdapter(userFeedAdapter);
    }

    @Override
    public void onAddSelected() {
        showCreateFeed();
    }

    private void showCreateFeed() {
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_add_user_feed, null);
        dialog.getWindow();
        file = null;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText editTextDescription = dialog.findViewById(R.id.edittext_description);
        RelativeLayout layoutSelectFile = dialog.findViewById(R.id.layout_select_file);
        tvFile = dialog.findViewById(R.id.tv_select_file);
        layoutSelectFile.setOnClickListener(v -> {
            RxPermissions permissions = new RxPermissions(this);
            Disposable subscribe = permissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            imageRequestCode = 100;
                            easyImage.openChooser(this);
                        }
                    });
        });
        TextView tvPost = dialog.findViewById(R.id.tv_post);
        tvPost.setOnClickListener(v -> {
            if (file == null)
                ((CompanyProfileActivity)currentActivity).onError(new Exception(currentActivity.getResources().getString(R.string.str_select_file)));
            else{
                dialog.dismiss();
                addUserFeed(editTextDescription.getText().toString().trim(),file);
            }
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser){
            getUserFeed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        easyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                try {
                    file = imageFiles[0].getFile();
                    if (tvFile != null && file != null && file.getName() != null){
                        tvFile.setText(file.getName());
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

    private void addUserFeed(String description,File file){
        progressBar.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().createUserFeed(String.valueOf(Constant.getInstance().getCompanyProfile().getCompanyId()),description,1,file)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        progressBar.setVisibility(View.GONE);
                        pageId = 0;

                        if (simpleResponse.getStatus() && simpleResponse.getCode() == 200){
                            ((CompanyProfileActivity)currentActivity).showSnackBar(simpleResponse.getMessage(),true);
                            getUserFeed();
                        }else {
                            ((CompanyProfileActivity)currentActivity).showSnackBar(simpleResponse.getMessage(),false);
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

    @Override
    public void onRemoveSelected(Integer feedId) {
        showDeleteFeedDialog(feedId);
    }

    private void showDeleteFeedDialog(Integer feedId){
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_delete_feed, null);
        dialog.getWindow();
        file = null;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvYes = dialog.findViewById(R.id.tv_yes);
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        tvYes.setOnClickListener(v -> {
            onRemoveFeed((feedId));
            dialog.dismiss();
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

    private void onRemoveFeed(int feedId){
        progressBar.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().removeFeed(feedId).subscribe(new Observer<SimpleResponse>() {
            Disposable mDispose;
            @Override
            public void onSubscribe(Disposable d) {
                mDispose = d;
            }

            @Override
            public void onNext(SimpleResponse simpleResponse) {
                progressBar.setVisibility(View.GONE);
                pageId = 0;
                if (simpleResponse.getCode() == 200){
                    ((CompanyProfileActivity)currentActivity).showSnackBar(simpleResponse.getMessage(),true);
                    getUserFeed();
                }else {
                    ((CompanyProfileActivity)currentActivity).showSnackBar(simpleResponse.getMessage(),false);
                }
            }

            @Override
            public void onError(Throwable e) {
                progressBar.setVisibility(View.GONE);
                ((CompanyProfileActivity)currentActivity).onError(e);
            }

            @Override
            public void onComplete() {
                mDispose.dispose();
            }
        });
    }
}
