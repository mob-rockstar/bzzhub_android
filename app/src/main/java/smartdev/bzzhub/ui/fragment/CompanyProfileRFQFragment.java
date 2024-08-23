package smartdev.bzzhub.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.RFQResponse;
import smartdev.bzzhub.ui.activity.CompanyProfileActivity;
import smartdev.bzzhub.ui.activity.RFQDetailActivity;
import smartdev.bzzhub.ui.activity.UserProfileActivity;
import smartdev.bzzhub.ui.adapter.CompanyRFQAdapter;
import smartdev.bzzhub.ui.base.BaseFragment;
import smartdev.bzzhub.util.Constant;

public class CompanyProfileRFQFragment extends BaseFragment implements CompanyRFQAdapter.SelectedListener{
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    CompanyRFQAdapter rfqAdapter;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_profile_r_f_q, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();

        return view;
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(currentActivity));
    }

    private void getRFQS(){
        BzzApp.getBzHubRepository().getCompanyRFQ((Constant.getInstance().getCompanyProfile().getCompanyId()))
                .subscribe(new Observer<RFQResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(RFQResponse rfqResponse) {
                        if (rfqResponse.getStatus() && rfqResponse.getCode() == 200){
                            setRecyclerView(rfqResponse.getResult());
                        }else {
                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
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

    private void setRecyclerView(List<RFQResponse.Result> rfqList){
        if (rfqList.isEmpty()){
            tvNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            tvNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            rfqAdapter = new CompanyRFQAdapter(currentActivity,rfqList,this);
            recyclerView.setAdapter(rfqAdapter);
        }

    }

    @Override
    public void onItemClicked(Integer id) {
        Intent intent = new Intent(currentActivity, RFQDetailActivity.class);
        intent.putExtra("RFQ_ID",id);
        startActivity(intent);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser){
            getRFQS();
        }
    }
}
