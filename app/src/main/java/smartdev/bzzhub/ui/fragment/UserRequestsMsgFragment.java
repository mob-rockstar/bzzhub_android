package smartdev.bzzhub.ui.fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.RequestMsgResponse;
import smartdev.bzzhub.repository.model.SimpleResponse;
import smartdev.bzzhub.repository.model.UserProfileResponse;
import smartdev.bzzhub.ui.activity.UserProfileActivity;
import smartdev.bzzhub.ui.adapter.ContactSuggestionAdapter;
import smartdev.bzzhub.ui.adapter.PendingRequestAdapter;
import smartdev.bzzhub.ui.adapter.SentRequestsAdapter;
import smartdev.bzzhub.ui.base.BaseFragment;
import smartdev.bzzhub.util.Constant;

public class UserRequestsMsgFragment extends BaseFragment implements PendingRequestAdapter.SelectedListener,
ContactSuggestionAdapter.SelectedListener{

    private List<UserProfileResponse.Friend> pendingRequests = new ArrayList<>();
    private  List<UserProfileResponse.Friend> contactSuggestion = new ArrayList<>();
    private  List<UserProfileResponse.Friend> sentRequests = new ArrayList<>();

    private PendingRequestAdapter pendingRequestAdapter;
    private ContactSuggestionAdapter contactSuggestionAdapter;
    private  SentRequestsAdapter sentRequestsAdapter;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.recyclerView_pending_requests)
    RecyclerView recyclerViewPendingRequests;
    @BindView(R.id.recyclerView_contact_suggestion)
    RecyclerView recyclerViewContactSuggestion;
    @BindView(R.id.recyclerView_sent_request)
    RecyclerView recyclerViewSentRequest;

    @BindView(R.id.layout_send_requests)
    RelativeLayout layoutSendRequests;
    @BindView(R.id.layout_suggestion)
    RelativeLayout layoutSuggestion;
    @BindView(R.id.layout_pendingRequests)
    RelativeLayout layotuPendingRequests;
    @BindView(R.id.layout_data)
    LinearLayout layoutData;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_requests_msg, container, false);
        ButterKnife.bind(this, view);
        initRecyclerViews();
        if ( isVisibleToUser){
            getFriends();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (getView() != null && isVisibleToUser){
            getFriends();
        }
    }

    private void getFriends(){
        progressBar.setVisibility(View.VISIBLE);
        BzzApp.getBzHubRepository().getFriends((Constant.getInstance().getUserProfile().getUserId()))
                .subscribe(new Observer<RequestMsgResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(RequestMsgResponse requestMsgResponse) {
                        progressBar.setVisibility(View.GONE);
                        if (requestMsgResponse.getStatus() && requestMsgResponse.getCode() == 200){
                            pendingRequests = requestMsgResponse.getResult().getPending();
                            contactSuggestion = requestMsgResponse.getResult().getSuggestion();
                            sentRequests = requestMsgResponse.getResult().getSent();
                            initFriendsList();
                            layoutData.setVisibility(View.VISIBLE);
                            tvNoData.setVisibility(View.GONE);
                        }else {
                            layoutData.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);
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

    private void initRecyclerViews(){
        recyclerViewContactSuggestion.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPendingRequests.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSentRequest.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.HORIZONTAL, false));
    }

    private void initFriendsList(){
        initPendingRequests();
        initContactSuggestion();
        initSentRequests();
    }

    private void initPendingRequests(){
        if (pendingRequests != null && !pendingRequests.isEmpty()){
            pendingRequestAdapter = new PendingRequestAdapter(currentActivity,pendingRequests,this::onAcceptClicked);
            recyclerViewPendingRequests.setAdapter(pendingRequestAdapter);
            layotuPendingRequests.setVisibility(View.VISIBLE);
        }else {
            recyclerViewPendingRequests.setVisibility(View.GONE);
        }
    }

    private void initContactSuggestion(){
        if (contactSuggestion != null && !contactSuggestion.isEmpty()){
            contactSuggestionAdapter = new ContactSuggestionAdapter(currentActivity,contactSuggestion,this::onSendClicked);
            recyclerViewContactSuggestion.setAdapter(contactSuggestionAdapter);
            layoutSuggestion.setVisibility(View.VISIBLE);
        }else {
            recyclerViewContactSuggestion.setVisibility(View.GONE);
        }
    }

    private void initSentRequests(){
        if (sentRequests != null && !sentRequests.isEmpty()){
            sentRequestsAdapter = new SentRequestsAdapter(currentActivity,sentRequests);
            recyclerViewSentRequest.setAdapter(sentRequestsAdapter);
            layoutSendRequests.setVisibility(View.VISIBLE);
        }else{
            recyclerViewSentRequest.setVisibility(View.GONE);

        }
    }

    @Override
    public void onAcceptClicked(Integer contactId) {
        AcceptRequest(contactId);
    }

     private void AcceptRequest(Integer contactId){
        BzzApp.getBzHubRepository().acceptFriends(contactId)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        if (simpleResponse.getStatus() && simpleResponse.getCode() == 200){
                            ((UserProfileActivity) currentActivity).showSnackBar(simpleResponse.getMessage(),true);
                            getFriends();
                        }else
                            ((UserProfileActivity) currentActivity).showSnackBar(simpleResponse.getMessage(),false);
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

    @Override
    public void onSendClicked(Integer toUser) {
        sendRequest(toUser);
    }

    private void sendRequest(Integer toUser){
        BzzApp.getBzHubRepository().sendRequests(toUser)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        if (simpleResponse.getStatus() && simpleResponse.getCode() == 200){
                            ((UserProfileActivity) currentActivity).showSnackBar(simpleResponse.getMessage(),true);
                            getFriends();
                        }else
                            ((UserProfileActivity) currentActivity).showSnackBar(simpleResponse.getMessage(),false);
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
}
