package smartdev.bzzhub.ui.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.common.io.LittleEndianDataInputStream;

import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.BusinessCategoryResponse;
import smartdev.bzzhub.repository.model.CompanyRegisterResponse;
import smartdev.bzzhub.repository.model.SelectorResponse;
import smartdev.bzzhub.repository.model.SubCategoryResponse;
import smartdev.bzzhub.ui.activity.MainActivity;
import smartdev.bzzhub.ui.activity.SignUpActivity;
import smartdev.bzzhub.ui.adapter.BusinesscategoryAdapter;
import smartdev.bzzhub.ui.base.BaseFragment;
import smartdev.bzzhub.util.Constant;
import smartdev.bzzhub.util.KeyboardUtils;
import smartdev.bzzhub.util.ProfileUtils;
import smartdev.bzzhub.util.Validator;
import smartdev.bzzhub.util.navigation.NavigationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CompanyRegisterFragment extends BaseFragment implements BusinesscategoryAdapter.SelectedListener {

    @BindView(R.id.checkbox)
    AppCompatCheckBox checkBox;
    @BindView(R.id.edittext_name)
    EditText editTextName;
    @BindView(R.id.edittext_email)
    EditText editTextEmail;
    @BindView(R.id.edittext_mobile)
    EditText editTextMobile;
    @BindView(R.id.edittext_password)
    EditText editTextPassword;
    @BindView(R.id.edittext_confirm_password)
    EditText editTextConfirmPassword;
    @BindView(R.id.spinner_category)
    Spinner categorySpinner;
    @BindView(R.id.spinner_subcategory)
    Spinner spinnerSubcategory;
    String mSocialId = "";

    @BindView(R.id.layout_busines_type)
    RelativeLayout layoutBusniessType;

    int manufacturer = 0;
    int trader = 0;
    int serviceCompany =0;

    public CompanyRegisterFragment(String mSocialId,String email, String firstName, String lastName) {
        this.mSocialId = mSocialId;
        this.strName = firstName + " " + lastName;
        this.strEmail = email;
    }

    ProgressDialog progressDialog ;

    private String strName = "",strEmail = "",strMobile = "",strPassword = "";
    private double latitude, longitude;
    private Dialog dialog;
    private Integer currentBusinessId ;
    ArrayList<BusinessCategoryResponse.Result> businessCategories = new ArrayList<>();
    ArrayList<SubCategoryResponse.Result> subcategories = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_register, container, false);
        ButterKnife.bind(this, view);
        progressDialog = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Loading...");
        extractArguments();
        getBusinessCategories();
        initViews();
        return view;
    }

    private void extractArguments(){
        latitude = ((SignUpActivity)currentActivity).latitude;
        longitude =  ((SignUpActivity)currentActivity).longitude;
    }

    private void initViews(){
        if (!mSocialId.isEmpty()){
            editTextEmail.setText(strEmail);
            editTextName.setText(strName);
        }
    }

    @OnClick(R.id.tv_accept_term) void onTvAcceptTerm(){
        checkBox.performClick();
    }

    @OnClick(R.id.layout_sign_up) void onSignUpClicked(){
        KeyboardUtils.hideKeyboard(currentActivity);
        String validationString = Validator.validateCompanyRegisterInfo(editTextName,editTextEmail,editTextMobile,editTextPassword,editTextConfirmPassword,
                currentBusinessId);
        if (validationString.isEmpty()){
            signUp();
        }else {
            ((SignUpActivity)currentActivity).showSnackBar(validationString,false);
        }
    }

    private void showProgressBar(){
        progressDialog.show();
    }

    private void hideProgressBar(){
        progressDialog.dismiss();
    }

    private void signUp(){
        strName = editTextName.getText().toString().trim();
        strMobile = editTextMobile.getText().toString().trim();
        strEmail = editTextEmail.getText().toString().trim();
        strPassword = editTextPassword.getText().toString().trim();
        showProgressBar();

        BzzApp.getBzHubRepository().signUpWithCompany(strName,strMobile,strEmail,strPassword,
                currentBusinessId,latitude,longitude,Constant.getLanguage(getActivity().getApplicationContext()),mSocialId,
                manufacturer, trader, serviceCompany)
                .doOnNext(userSignUpResponse -> {
                    if (userSignUpResponse.getCode() != 200){
                        hideProgressBar();
                        ((SignUpActivity)currentActivity).onError(new Exception(userSignUpResponse.getMessage()));
                    }
                })
                .subscribe(new Observer<CompanyRegisterResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(CompanyRegisterResponse userSignUpResponse) {
                        hideProgressBar();
                        if (userSignUpResponse.getStatus() && userSignUpResponse.getCode() == 200){
                            if (checkBox.isChecked()){
                                ProfileUtils.saveUserProfile(null);
                                ProfileUtils.saveCompanyProfile(userSignUpResponse.getResult());
                            }else {
                                ProfileUtils.saveUserProfile(null);
                                Constant.getInstance().setUserProfile(null);
                                Constant.getInstance().setCompanyProfile(userSignUpResponse.getResult());
                            }

                            Intent intent = new Intent(currentActivity, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressBar();
                        ((SignUpActivity)currentActivity).onError(new Exception(e));
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    public void getBusinessCategories(){
        BzzApp.getBzHubRepository().getBusinessCategoryResponse().doOnNext(businessCategoryResponse -> {
            if (businessCategoryResponse.getCode() != 200){
                ((SignUpActivity)currentActivity).onError(new Exception(businessCategoryResponse.getMessage()));
            }
        }).subscribe(new Observer<BusinessCategoryResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(BusinessCategoryResponse businessCategoryResponse) {

                        if ( businessCategoryResponse.getStatus() && businessCategoryResponse.getCode() == 200){
                            businessCategories = (ArrayList<BusinessCategoryResponse.Result>) businessCategoryResponse.getResult();
                        }
                        businessCategories.add(new BusinessCategoryResponse.Result(0,"-Select-",0));

                        ArrayAdapter<BusinessCategoryResponse.Result> adapter =
                                new ArrayAdapter<BusinessCategoryResponse.Result>(currentActivity , android.R.layout.simple_spinner_dropdown_item, businessCategories);
                        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                        categorySpinner.setAdapter(adapter);
                        categorySpinner.setSelection(businessCategories.size()-1);

                        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                try {
                                    getSubcategoriesList((businessCategories.get(position).getCategoryId()));
                                }catch (Exception e){

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((SignUpActivity)currentActivity).onError(new Exception(e));
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    private void getSubcategoriesList(int id){
        BzzApp.getBzHubRepository().getSubcategories(id).subscribe(
                new Observer<SubCategoryResponse>() {
                    Disposable mDisposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(SubCategoryResponse subCategoryResponse) {
                        if (subCategoryResponse.getStatus() && subCategoryResponse.getCode() == 200 & subCategoryResponse.getResult() != null){
                            subcategories = (ArrayList<SubCategoryResponse.Result>) subCategoryResponse.getResult();
                            subcategories.add(new SubCategoryResponse.Result(0,0,"-Select-","",0));
                            ArrayAdapter<SubCategoryResponse.Result> adapter =
                                    new ArrayAdapter<SubCategoryResponse.Result>(currentActivity , android.R.layout.simple_spinner_dropdown_item, subcategories);
                            adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                            spinnerSubcategory.setAdapter(adapter);
                            spinnerSubcategory.setSelection(subcategories.size()-1);
                            spinnerSubcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    currentBusinessId = subcategories.get(position).getSubId();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mDisposable.dispose();
                    }
                }
        );
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

    private void showCompanyBusinessTypeDialog() {
        dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_business_type, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView btnSave = dialog.findViewById(R.id.tv_save);
        CheckBox checkboxManufacturer = dialog.findViewById(R.id.checkbox_manufacturers);
        CheckBox checkboxTrader = dialog.findViewById(R.id.checkbox_trader);
        CheckBox checkboxServiceCompany = dialog.findViewById(R.id.checkbox_service_company);

        btnSave.setOnClickListener(v -> {
            if (checkboxManufacturer.isChecked()) manufacturer = 1; else manufacturer = 0;
            if (checkboxTrader.isChecked()) trader = 1; else trader =0;
            if (checkboxServiceCompany.isChecked()) serviceCompany = 1; else serviceCompany = 0;
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

    @Override
    public void onSelected(int position) {
     //   editTextCategory.setText(businessCategories.get(position).getName());
        currentBusinessId = businessCategories.get(position).getCategoryId();
        if (dialog != null) dialog.dismiss();
    }

    @OnClick(R.id.layout_busines_type) void onBusinesTypeClicked(){
        showCompanyBusinessTypeDialog();
    }

    @OnClick(R.id.edittext_business_type) void onBusinessTextClicked(){
        showCompanyBusinessTypeDialog();
    }

    @OnClick(R.id.tv_sign_in) void onSignInClicked(){
        NavigationManager.gotoLoginActivity(currentActivity);
        currentActivity.finish();
    }
}
