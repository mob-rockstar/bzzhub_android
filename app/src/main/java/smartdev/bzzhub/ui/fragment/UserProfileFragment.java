package smartdev.bzzhub.ui.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import smartdev.bzzhub.repository.model.CompanyDetailResponse;
import smartdev.bzzhub.repository.model.InterestResponse;
import smartdev.bzzhub.repository.model.SimpleResponse;
import smartdev.bzzhub.repository.model.UserProfileResponse;
import smartdev.bzzhub.ui.activity.CompanyProfileActivity;
import smartdev.bzzhub.ui.activity.UserProfileActivity;
import smartdev.bzzhub.ui.adapter.ChooseSectorAdapter;
import smartdev.bzzhub.ui.adapter.ChooseSectorAdapterUser;
import smartdev.bzzhub.ui.adapter.GroupsAdapter;
import smartdev.bzzhub.ui.adapter.UserProfileFriendAdapter;
import smartdev.bzzhub.ui.adapter.UserProfileInterestAdapter;
import smartdev.bzzhub.ui.base.BaseFragment;
import smartdev.bzzhub.util.Constant;

public class UserProfileFragment extends BaseFragment implements UserProfileInterestAdapter.SelectedListener{

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.toggle_email)
    ToggleButton toggleEmail;
    @BindView(R.id.toggle_phone)
    ToggleButton togglePhone;
    @BindView(R.id.toggle_school)
    ToggleButton toggleSchool;
    @BindView(R.id.toggle_university)
    ToggleButton toggleUniversity;
    @BindView(R.id.toggle_work)
    ToggleButton toggleWork;

    @BindView(R.id.edittext_email)
    TextView editTextEmail;
    @BindView(R.id.edittext_phone)
    TextView editTextPhone;
    @BindView(R.id.edittext_membership_status)
    TextView editTextMembershipStatus;
    @BindView(R.id.edittext_language)
    TextView editTextLanguage;
    @BindView(R.id.edittext_school)
    TextView editTextSchool;
    @BindView(R.id.edittext_university)
    TextView editTextUniversity;
    @BindView(R.id.edittext_work)
    TextView editTextWork;

    @BindView(R.id.recyclerView_friends)
    RecyclerView recyclerViewFriends;
    @BindView(R.id.recyclerView_interests)
    RecyclerView recyclerViewInterests;
    @BindView(R.id.recyclerView_groups)
    RecyclerView recyclerViewGroups;
    @BindView(R.id.tv_no_group)
    TextView tvNoGroup;

    @BindView(R.id.tv_edit_language)
    View editLanguage;
    @BindView(R.id.tv_edit_phone)
    View editPhone;
    @BindView(R.id.tv_edit_email)
    View editEmail;
    @BindView(R.id.tv_add_interest)
    View addInterest;

    @BindView(R.id.tv_edit_school)
    TextView tvEditSchool;
    @BindView(R.id.tv_edit_university)
    TextView tvEditUniversity;
    @BindView(R.id.tv_edit_work)
    TextView tvEditWork;

    private List<InterestResponse.Result> interests = new ArrayList<>();
    List<UserProfileResponse.Interst> selectedInterest = new ArrayList<>();
    private  ChooseSectorAdapterUser chooseSectorAdapter;
    List<UserProfileResponse.Interst> userInterestList =new ArrayList<>();

    UserProfileInterestAdapter userProfileInterestAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, view);
        initRecyclerViews();

        if(isVisibleToUser){
            getAllInterest();
            getUserDetail();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (getView() != null && isVisibleToUser){
            getAllInterest();
            getUserDetail();
        }
    }

    private void initRecyclerViews(){
        recyclerViewInterests.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewGroups.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL,false));
    }

    private void getUserDetail(){
        progressBar.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().getUserProfile(((UserProfileActivity)currentActivity).mUserId)
                .subscribe(new Observer<UserProfileResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(UserProfileResponse userProfileResponse) {
                        progressBar.setVisibility(View.GONE);
                        if (userProfileResponse.getStatus() && userProfileResponse.getCode() == 200){
                            ((UserProfileActivity)currentActivity).initViews(userProfileResponse.getResult());
                            initView(userProfileResponse.getResult());
                            initToggles(userProfileResponse.getResult());
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

    private void initView(UserProfileResponse.Result profile){
        editTextEmail.setText(profile.getEmailIsVisible() != null && profile.getEmailIsVisible().equals(1) ? profile.getEmail() : profile.getEmail());
        editTextPhone.setText(profile.getPhoneIsVisible() != null && profile.getPhoneIsVisible().equals(1) ? profile.getPhone() : profile.getPhone());
        editTextUniversity.setText(profile.getUniverstyIsVisible() != null && profile.getUniverstyIsVisible().equals(1) ? profile.getUniversity(): profile.getUniversity());
        editTextSchool.setText(profile.getSchoolIsVisible() != null && profile.getSchoolIsVisible().equals(1) ? profile.getSchool() : profile.getSchool());
        editTextWork.setText(profile.getWorkIsVisible() != null && profile.getWorkIsVisible().equals(1) ? profile.getWork() : profile.getWork());
        if (profile.getLanguages() != null && !profile.getLanguages().isEmpty())
            editTextLanguage.setText(getLanguages(profile.getLanguages()));
        initInterests(profile.getInterst());
        if (profile.getInterst() != null) userInterestList = profile.getInterst();
        initGroup(profile.getGroups());
        initFriends(profile.getFriends());
    }

    private void initInterests(List<UserProfileResponse.Interst> interests){

        if (interests != null){
            userProfileInterestAdapter  = new UserProfileInterestAdapter(currentActivity,interests,false,this);
            recyclerViewInterests.setAdapter(userProfileInterestAdapter);
        }
    }

    private String getLanguages(List<String> languages){
        StringBuilder strLanguage = new StringBuilder();
        for (int i = 0;i<languages.size();i++){
            strLanguage.append(languages.get(i)).append(i<languages.size()-1 ? " ," : "");
        }

        return strLanguage.toString();
    }

    private void initGroup(List<UserProfileResponse.Group> groups){
        if (groups != null){
            recyclerViewGroups.setAdapter(new GroupsAdapter(currentActivity,groups));
            tvNoGroup.setVisibility(View.GONE);
        }else {
            tvNoGroup.setVisibility(View.VISIBLE);
            recyclerViewGroups.setVisibility(View.GONE);
        }

    }

    private void initFriends(List<UserProfileResponse.Friend> friends){
        if (friends != null)
            recyclerViewFriends.setAdapter(new UserProfileFriendAdapter(currentActivity,friends));
    }

    @Override
    public void onRemoveClicked(int position, Integer id) {
        showDeleteFeedDialog(position,id);
    }

    private void removeInterest(int position, Integer id){
        progressBar.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().removeInterest(id)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        progressBar.setVisibility(View.GONE);
                        userProfileInterestAdapter.removeItem(position);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        ((UserProfileActivity) currentActivity).onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    private void initToggles(UserProfileResponse.Result result){
        togglePhone.setChecked(result.getPhoneIsVisible() != null && result.getPhoneIsVisible().equals(0));
        toggleEmail.setChecked(result.getEmailIsVisible() != null && result.getEmailIsVisible().equals(0));
        toggleWork.setChecked(result.getWorkIsVisible() != null && result.getWorkIsVisible().equals(0));
        toggleUniversity.setChecked(result.getUniverstyIsVisible() != null &&result.getUniverstyIsVisible().equals(0));
        toggleSchool.setChecked(result.getSchoolIsVisible() != null && result.getSchoolIsVisible().equals(0));

        toggleEmail.setOnCheckedChangeListener((buttonView, isChecked) -> {
            hideEmail(isChecked ? 0 : 1);
        });

        togglePhone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            hidePhone(isChecked ? 0 : 1);
        });

        toggleSchool.setOnCheckedChangeListener((buttonView, isChecked) -> {
            hideSchool(isChecked ? 0 : 1);
        });

        toggleUniversity.setOnCheckedChangeListener((buttonView, isChecked) -> {
            hideUniversity(isChecked ? 0 : 1);
        });

        toggleWork.setOnCheckedChangeListener((buttonView, isChecked) -> {
            hideWork(isChecked ? 0 : 1);
        });

        if( ((UserProfileActivity)currentActivity).isOtherUser){
            toggleWork.setVisibility(View.GONE);
            toggleUniversity.setVisibility(View.GONE);
            toggleSchool.setVisibility(View.GONE);
            togglePhone.setVisibility(View.GONE);
            toggleEmail.setVisibility(View.GONE);

            editEmail.setVisibility(View.GONE);
            editLanguage.setVisibility(View.GONE);
            editPhone.setVisibility(View.GONE);
            addInterest.setVisibility(View.GONE);

            tvEditSchool.setVisibility(View.GONE);
            tvEditUniversity.setVisibility(View.GONE);
            tvEditWork.setVisibility(View.GONE);
        }
    }

    private void hideWork(int status){
        BzzApp.getBzHubRepository().hideWork(Constant.getInstance().getUserProfile().getUserId(),status)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        if (simpleResponse.getStatus() && simpleResponse.getCode() == 200){
                            getUserDetail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((UserProfileActivity) currentActivity).onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    private void hideUniversity(int status){
        BzzApp.getBzHubRepository().hideUniversity(Constant.getInstance().getUserProfile().getUserId(),status)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        getUserDetail();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((UserProfileActivity) currentActivity).onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    private void hideSchool(int status){
        BzzApp.getBzHubRepository().hideSchool(Constant.getInstance().getUserProfile().getUserId(),status)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        getUserDetail();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((UserProfileActivity) currentActivity).onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    private void hidePhone(int status){
        BzzApp.getBzHubRepository().hidePhone(Constant.getInstance().getUserProfile().getUserId(),status)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        getUserDetail();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((UserProfileActivity) currentActivity).onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    private void hideEmail(int status){
        BzzApp.getBzHubRepository().hideEmail(Constant.getInstance().getUserProfile().getUserId(),status)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        getUserDetail();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((UserProfileActivity) currentActivity).onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }


    @OnClick(R.id.tv_add_interest) void onAddInterest(){
        showUpdateInterests();
    }

    private void showUpdateInterests(){
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_interest, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText editText = dialog.findViewById(R.id.tv_find_company);
        RecyclerView recyclerInterest = dialog.findViewById(R.id.recycler_interest_select);
        recyclerInterest.setNestedScrollingEnabled(false);
        recyclerInterest.setHasFixedSize(true);
        recyclerInterest.setLayoutManager(new GridLayoutManager(currentActivity,3));
        chooseSectorAdapter = new ChooseSectorAdapterUser(currentActivity,cleanInterests());
        recyclerInterest.setAdapter(chooseSectorAdapter);
        TextView tvSave = dialog.findViewById(R.id.tv_save);
        tvSave.setOnClickListener(v -> {
            if (chooseSectorAdapter.getIdList().isEmpty()){
                ((UserProfileActivity) currentActivity).onError(new Exception(currentActivity.getResources().getString(R.string.str_select_one_id)));
            }else {
                updateInterests(chooseSectorAdapter.getIdList());
                dialog.dismiss();
                chooseSectorAdapter = null;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                chooseSectorAdapter.filterInterest(s.toString());
            }
        });
        LinearLayout layoutRoot = dialog.findViewById(R.id.layout_root);
        layoutRoot.setOnClickListener(v -> {
            dialog.dismiss();
            chooseSectorAdapter = null;
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

    private List<Integer> idList(){
        List<Integer> idList = new ArrayList<>();
        if (!userInterestList.isEmpty()){
            for (int i = 0;i<userInterestList.size();i++){
                idList.add(userInterestList.get(i).getInterestID());
            }
        }
        return idList;
    }

    private List<InterestResponse.Result> cleanInterests(){

        List<Integer> userInterestList = idList();
        List<InterestResponse.Result> cleanedList = new ArrayList<>();
        for (int i = 0;i<interests.size();i++){
            if (!userInterestList.contains(interests.get(i).getInterestId())){
                cleanedList.add(interests.get(i));
            }
        }

        for (InterestResponse.Result interest : cleanedList)
            interest.setSelected(false);

        return cleanedList;
    }

    private void updateInterests(String idList){
        BzzApp.getBzHubRepository().updateUserInterest(
                idList).subscribe(new Observer<SimpleResponse>() {
            Disposable mDispose;
            @Override
            public void onSubscribe(Disposable d) {
                mDispose = d;
            }

            @Override
            public void onNext(SimpleResponse simpleResponse) {
                getUserDetail();
            }

            @Override
            public void onError(Throwable e) {
                ((UserProfileActivity)currentActivity).onError(e);
            }

            @Override
            public void onComplete() {
                mDispose.dispose();;
            }
        });
    }


    private void getAllInterest(){
        BzzApp.getBzHubRepository().getInterestList().subscribe(
                new Observer<InterestResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(InterestResponse interestResponse) {
                        interests = interestResponse.getResult();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ((UserProfileActivity)currentActivity).onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                }
        );
    }

    private void showDeleteFeedDialog(int position, Integer id){
        Dialog dialog = new Dialog(currentActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(currentActivity);

        final View view = layoutInflater.inflate(R.layout.dialog_delete_interest, null);
        dialog.getWindow();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvYes = dialog.findViewById(R.id.tv_yes);
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        tvYes.setOnClickListener(v -> {
            removeInterest(position,id);
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


    @OnClick({R.id.tv_edit_language,R.id.tv_edit_phone,R.id.tv_edit_email,R.id.tv_edit_school,R.id.tv_edit_university,R.id.tv_edit_work})
    void onEditProfileClicked(){
        if (currentActivity instanceof UserProfileActivity){
            ((UserProfileActivity)currentActivity).showEditProfileDialog();
        }
    }

}
