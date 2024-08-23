package smartdev.bzzhub.ui.fragment;

import android.Manifest;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.BookResponse;
import smartdev.bzzhub.repository.model.Company;
import smartdev.bzzhub.repository.model.EventBusMessage;
import smartdev.bzzhub.repository.model.SimpleResponse;
import smartdev.bzzhub.ui.activity.CompanyProfileActivity;
import smartdev.bzzhub.ui.adapter.CompanyProfileTenderAdapter;
import smartdev.bzzhub.ui.base.BaseFragment;
import smartdev.bzzhub.util.Constant;
import smartdev.bzzhub.util.navigation.NavigationManager;

import static android.app.Activity.RESULT_OK;
import static smartdev.bzzhub.util.navigation.Arg.ARG_URL_PDF;

public class CompanyProfileTenderFragment extends BaseFragment implements CompanyProfileTenderAdapter.SelectedListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    List<BookResponse.PDFResult> pdfResults = new ArrayList<>();
    String strKeyword;
    String strUrl;
    File file;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private TextView tvSelectFile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_profile_tender, container, false);
        ButterKnife.bind(this, view);
        initRecyclerView();


        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser){
            getCompanyDetails();
        }
    }

    private void initRecyclerView(){
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(currentActivity,2));
    }

    private void getCompanyDetails(){
        progressBar.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().getBookList((Constant.getInstance().getCompanyProfile().getCompanyId()))
                .doOnNext(bookResponse -> {
                    if (bookResponse.getCode() != 200)
                        CompanyProfileTenderFragment.this.onError(new Exception(bookResponse.getMessage()));
                })
                .subscribe(new Observer<BookResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(BookResponse companyDetailResponse) {
                        progressBar.setVisibility(View.GONE);
                        if (companyDetailResponse.getStatus() && companyDetailResponse.getCode() == 200 && companyDetailResponse.getResult() != null ){
                            pdfResults = companyDetailResponse.getResult();
                            recyclerView.setAdapter(new CompanyProfileTenderAdapter(currentActivity,companyDetailResponse.getResult(),CompanyProfileTenderFragment.this));
                        }else {
                            recyclerView.setAdapter(new CompanyProfileTenderAdapter(currentActivity,new ArrayList<>(),CompanyProfileTenderFragment.this));

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        CompanyProfileTenderFragment.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    @Override
    public void onRemoved(Integer tenderId) {
            showDeleteFeedDialog(tenderId);
    }

    private void removePDF(Integer tenderId){
        BzzApp.getBzHubRepository().removePDF(tenderId)
        .subscribe(new Observer<SimpleResponse>() {
            Disposable mDispose;
            @Override
            public void onSubscribe(Disposable d) {
                mDispose = d;
            }

            @Override
            public void onNext(SimpleResponse simpleResponse) {
                if (simpleResponse.getCode() == 200 && simpleResponse.getStatus()){
                    getCompanyDetails();
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

    @Override
    public void onAddClicked() {
        addPDF();
    }

    @Override
    public void onSelected(String path) {

        RxPermissions permissions = new RxPermissions(this);
        Disposable subscribe = permissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        Bundle bundle = new Bundle();

                        bundle.putString(ARG_URL_PDF,path);
                        NavigationManager.gotoPDFActivity(currentActivity,bundle);
                    }
                });
    }


    private void addPDF(){
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_add_tender, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        strKeyword = "";
        EditText editText = dialog.findViewById(R.id.edittext_description);
        RelativeLayout layoutSelectFile = dialog.findViewById(R.id.layout_select_file);
        layoutSelectFile.setOnClickListener(v -> {
            RxPermissions permissions = new RxPermissions(this);
            final Disposable subscribe = permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            ((CompanyProfileActivity) Objects.requireNonNull(getActivity())).onPDFClicked();
                        }
                    });
        });
        tvSelectFile = dialog.findViewById(R.id.tv_select_file);
        TextView tvUpload = dialog.findViewById(R.id.tv_upload);
        tvUpload.setOnClickListener(v -> {
            strKeyword = editText.getText().toString().trim();
            if (strKeyword.isEmpty()){
                ((CompanyProfileActivity)currentActivity).onError(new Exception(currentActivity.getResources().getString(R.string.str_empty_keyword)));
            }else if (file == null){
                ((CompanyProfileActivity)currentActivity).onError(new Exception(currentActivity.getResources().getString(R.string.str_empty_tender)));
            }else{
                dialog.dismiss();
                addImage(strKeyword,1,0,file);
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

    private void addImage(String strKeyword,int flag, int isPrivate, File file){
        BzzApp.getBzHubRepository().addPDF(String.valueOf(Constant.getInstance().getCompanyProfile().getCompanyId()),strKeyword,
                file).subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
            @Override
            public void onSubscribe(Disposable d) {
                mDispose = d;
            }

            @Override
            public void onNext(SimpleResponse simpleResponse) {
                ((CompanyProfileActivity)currentActivity).showSnackBar(simpleResponse.getMessage(), simpleResponse.getCode() == 200);
                if (simpleResponse.getStatus() && simpleResponse.getCode() == 200){
                    getCompanyDetails();
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


    private void showDeleteFeedDialog(Integer tenderId){
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_delete_tender, null);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvYes = dialog.findViewById(R.id.tv_yes);
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        tvYes.setOnClickListener(v -> {
            dialog.dismiss();
            removePDF((tenderId));
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


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage messageEvent) {
        if (messageEvent != null) {
            int messageType = messageEvent.getMessageType();
            if (messageType == EventBusMessage.MessageType.PDF_SELECTED){
               file =  ((CompanyProfileActivity)getActivity()).pdfFile;
                tvSelectFile.setText(file.getName());
            }
        }
    }

/*    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK){
            ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            if (files != null){
                file = new File(files.get(0).getPath());
                if (file != null && tvSelectFile != null)
                    tvSelectFile.setText(file.getName());
            }

        }
    }*/

}
