package smartdev.bzzhub.ui.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;
import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.BusinessCategoryResponse;
import smartdev.bzzhub.repository.model.SimpleResponse;
import smartdev.bzzhub.repository.model.SubCategoryResponse;
import smartdev.bzzhub.ui.activity.CompanyDetailActivity;
import smartdev.bzzhub.ui.activity.ForgotPasswordActivity;
import smartdev.bzzhub.ui.activity.SignUpActivity;
import smartdev.bzzhub.ui.adapter.BusinesscategoryAdapter;
import smartdev.bzzhub.ui.adapter.SubcategoryAdapter;
import smartdev.bzzhub.ui.base.BaseFragment;
import smartdev.bzzhub.util.Constant;
import smartdev.bzzhub.util.KeyboardUtils;
import smartdev.bzzhub.util.Validator;
import com.tbruyelle.rxpermissions2.RxPermissions;

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

public class CompanyRFQFragment extends BaseFragment implements BusinesscategoryAdapter.SelectedListener, SubcategoryAdapter.SelectedListener {

    @BindView(R.id.edtttext_name)
    EditText editTextName;
    @BindView(R.id.edittext_mobile)
    EditText editTextMobile;
    @BindView(R.id.edittext_product_keyword)
    EditText editTextProductKeyword;
    @BindView(R.id.edittext_category)
    EditText editTextCategory;
    @BindView(R.id.edittext_subcategory)
    EditText editTextSubcategory;
    @BindView(R.id.edittext_specification)
    EditText editTextSpecification;
    @BindView(R.id.eittext_unit)
    EditText editTextUnit;
    @BindView(R.id.edittext_pieces)
    EditText editTextPieces;
    @BindView(R.id.edittext_upload)
    EditText editTextUpload;
    @BindView(R.id.edittext_email)
    EditText editTextEmail;
    private Integer companyId;
    private File file = null;
    private String strName,strMobile,strProductKeyword,strCategory,strSubcategory,strSpecification,strUnit,strPieces,strEmail;
    private Dialog dialog;
    private int currentBusinessId = 0,currentSubcategoryId = 0;
    private List<BusinessCategoryResponse.Result> businessCategories = new ArrayList<>();
    private List<SubCategoryResponse.Result> subcategoriesList = new ArrayList<>();
    private ProgressDialog progressDialog;

    private EasyImage easyImage;
    private int imageRequestCode = 1001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_r_f_q, container, false);
        ButterKnife.bind(this, view);

        // Define Easy Image
        easyImage =
                new  EasyImage.Builder(getActivity()).setCopyImagesToPublicGalleryFolder(false)
                        .allowMultiple(false)
                        .build();

        extractArgument();
        progressDialog = new ProgressDialog(currentActivity,R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Loading...");
        if (isVisibleToUser){
            getBusinessCategories();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (getView() != null && isVisibleToUser){
            getBusinessCategories();
        }
    }

    private void extractArgument(){
        companyId =  ((CompanyDetailActivity)currentActivity).companyId;
    }

    @OnClick(R.id.tv_submit) void onSubmitClicked(){
        initStrings();
        String validationString = Validator.validateCompanyRFQ(strName,strMobile,strProductKeyword,strCategory,strSubcategory,strUnit,strPieces);
        if (validationString.isEmpty()){
            callCompanyRFQ();
        }else {
            ((CompanyDetailActivity)currentActivity).showSnackBar(validationString,false);
        }
    }
/*

    private void initStrings(){
        strName = "test";
        strMobile = "12345678";
        strProductKeyword = "1234";
        strCategory = "12";
        strSubcategory = "1";
        strUnit = "Dozen";
        strPieces = "12";
        strSpecification = "test doc";
        strEmail = "test@mail.com";
    }
*/

    private void initStrings(){
        strName = editTextName.getText().toString().trim();
        strMobile = editTextMobile.getText().toString().trim();
        strProductKeyword = editTextProductKeyword.getText().toString().trim();
        strCategory = editTextCategory.getText().toString().trim();
        strSubcategory = editTextSubcategory.getText().toString().trim();
        strUnit = editTextUnit.getText().toString().trim();
        strPieces = editTextPieces.getText().toString().trim();
        strSpecification = editTextSpecification.getText().toString().trim();
        strEmail = editTextEmail.getText().toString().trim();
    }

    @OnClick(R.id.edittext_upload) void onEdittextUploadClicked(){
        KeyboardUtils.hideKeyboard(currentActivity);
        RxPermissions permissions = new RxPermissions(this);
        Disposable subscribe = permissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        easyImage.openChooser(this);
                    }
                });
    }

    private void callCompanyRFQ(){
        progressDialog.show();
        int flag = 0;
        if (Constant.getInstance().getLoginFlag() == 1){
            flag = 1;
        }else {
            flag = 0;
        }

        BzzApp.getBzHubRepository().createCompanyRFQ((companyId),strName,strMobile,strEmail,strUnit,strPieces,
                strSpecification,strProductKeyword,file,String.valueOf(currentBusinessId),flag,
                Constant.getInstance().getLoginID()).doOnNext(simpleResponse -> {
                    progressDialog.hide();
                    if (simpleResponse.getCode() != 200){
                        ((CompanyDetailActivity)currentActivity).onError(new Exception(simpleResponse.getMessage()));
                    }
        })
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        if (simpleResponse.getCode() != 200){
                            ((CompanyDetailActivity)currentActivity).onError(new Exception(simpleResponse.getMessage()));
                        }else {
                            ((CompanyDetailActivity)currentActivity).showSnackBar((simpleResponse.getMessage()),true);
                        }
                        editTextEmail.setText("");
                        editTextName.setText("");
                        editTextMobile.setText("");
                        editTextProductKeyword.setText("");
                        editTextSpecification.setText("");
                        editTextUnit.setText("");
                        editTextPieces.setText("");
                        file = null;
                        editTextUpload.setText("");
                        editTextCategory.setText("");
                        editTextSubcategory.setText("");
                        progressDialog.hide();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.hide();
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        easyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                try {
                    file = imageFiles[0].getFile();
                    if (file != null && file.getName() != null) {
                        editTextUpload.setText(file.getName());
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

    private void getBusinessCategories(){
        BzzApp.getBzHubRepository().getBusinessCategoryResponse().doOnNext(businessCategoryResponse -> {
            if (businessCategoryResponse.getCode() != 200){
                ((CompanyDetailActivity)currentActivity).onError(new Exception(businessCategoryResponse.getMessage()));
            }
        }).subscribe(new Observer<BusinessCategoryResponse>() {
            Disposable mDispose;
            @Override
            public void onSubscribe(Disposable d) {
                mDispose = d;
            }

            @Override
            public void onNext(BusinessCategoryResponse businessCategoryResponse) {
                if (businessCategoryResponse.getCode() == 200 && businessCategoryResponse.getStatus() && businessCategoryResponse.getResult() != null){
                    businessCategories = businessCategoryResponse.getResult();
                }
            }

            @Override
            public void onError(Throwable e) {
                ((CompanyDetailActivity)currentActivity).onError(new Exception(e));
            }

            @Override
            public void onComplete() {
                mDispose.dispose();
            }
        });
    }

    public void getSubcategoriesList(){
        BzzApp.getBzHubRepository().getSubcategories((currentBusinessId)).doOnNext(businessCategoryResponse -> {
            if (businessCategoryResponse.getCode() != 200){
                ((CompanyDetailActivity)currentActivity).onError(new Exception(businessCategoryResponse.getMessage()));
            }
        }).subscribe(new Observer<SubCategoryResponse>() {
            Disposable mDispose;
            @Override
            public void onSubscribe(Disposable d) {
                mDispose = d;
            }

            @Override
            public void onNext(SubCategoryResponse businessCategoryResponse) {
                if (businessCategoryResponse.getCode() == 200 && businessCategoryResponse.getStatus()
                 && businessCategoryResponse.getResult() != null){
                    subcategoriesList = businessCategoryResponse.getResult();
                }
            }

            @Override
            public void onError(Throwable e) {
                ((CompanyDetailActivity)currentActivity).onError(new Exception(e));
            }

            @Override
            public void onComplete() {
                mDispose.dispose();
            }
        });
    }

    private void showBusinessCategorySelectionDialog() {
        dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_list, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RecyclerView recyclerView = view.findViewById(R.id.dialogRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(currentActivity));
        recyclerView.setAdapter(new BusinesscategoryAdapter(currentActivity,businessCategories, this));
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
    public void onSelected(int position) {
        editTextCategory.setText(businessCategories.get(position).getName());
        currentBusinessId = businessCategories.get(position).getCategoryId();
        if (dialog != null) dialog.dismiss();
        getSubcategoriesList();
    }

    @OnClick(R.id.edittext_category) void onBusinessCategoryClicked(){
        if (businessCategories != null && !businessCategories.isEmpty())
            showBusinessCategorySelectionDialog();
    }

    @OnClick(R.id.edittext_subcategory) void onSubcategorySelected(){
        if (subcategoriesList != null && !subcategoriesList.isEmpty()){
            showSubcategoriesList();
        }
    }

    @Override
    public void onSubcategorySelected(int position) {
        editTextSubcategory.setText(subcategoriesList.get(position).getName());
        currentBusinessId = subcategoriesList.get(position).getCategoryId();
        if (dialog != null) dialog.dismiss();
    }

    private void showSubcategoriesList() {
        dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_list, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RecyclerView recyclerView = view.findViewById(R.id.dialogRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(currentActivity));
        recyclerView.setAdapter(new SubcategoryAdapter(currentActivity,subcategoriesList, this));
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

    private void showRequireLogin() {
        dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_require_login, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tvOk = dialog.findViewById(R.id.tv_yes);
        tvOk.setOnClickListener(v -> {
            dialog.dismiss();
            if (currentActivity instanceof CompanyDetailActivity){
                ((CompanyDetailActivity)currentActivity).viewPager.setCurrentItem(0,false);
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


}
