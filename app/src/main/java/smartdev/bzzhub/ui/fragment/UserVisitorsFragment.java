package smartdev.bzzhub.ui.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.nio.file.Path;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.UserProfileVisitorResponse;
import smartdev.bzzhub.ui.activity.UserProfileActivity;
import smartdev.bzzhub.ui.adapter.VisitorAdapter;
import smartdev.bzzhub.ui.base.BaseFragment;
import smartdev.bzzhub.util.Constant;

public class UserVisitorsFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_visitors, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();
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

    private void getData(){
        BzzApp.getBzHubRepository().getVisitors((Constant.getInstance().getUserProfile().getUserId()))
                .subscribe(new Observer<UserProfileVisitorResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(UserProfileVisitorResponse userProfileVisitorResponse) {
                        if (userProfileVisitorResponse.getResult() != null && !userProfileVisitorResponse.getResult().isEmpty()){
                            tvTitle.setVisibility(View.VISIBLE);
                            tvNoData.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            setRecyclerView(userProfileVisitorResponse.getResult());
                            tvTitle.setText(currentActivity.getResources().getString(R.string.str_visitor_number,userProfileVisitorResponse.getResult().size()));
                        }else {
                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            tvTitle.setVisibility(View.GONE);
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

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(currentActivity));
    }

    private void setRecyclerView(List<UserProfileVisitorResponse.Result> list){
        recyclerView.setAdapter(new VisitorAdapter(currentActivity,list));
    }
}
