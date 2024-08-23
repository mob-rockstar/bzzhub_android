package smartdev.bzzhub.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.ConnectionResponse;
import smartdev.bzzhub.ui.activity.CompanyProfileActivity;
import smartdev.bzzhub.ui.adapter.ConnectionAdapter;
import smartdev.bzzhub.ui.base.BaseFragment;
import smartdev.bzzhub.util.Constant;

public class CompanyProfileConnectionsFragment extends BaseFragment implements ConnectionAdapter.SelectedListener{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;

    @BindView(R.id.layout_chat)
    RelativeLayout layoutChat;

    private ConnectionAdapter connectionAdapter;
    private int selection = -1;
    private ConnectionResponse.Result currentConnection = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_profile_connections, container, false);
        ButterKnife.bind(this, view);

        initRecyclerViews();

        return view;
    }

    private void initRecyclerViews(){
        recyclerView.setLayoutManager(new LinearLayoutManager(currentActivity));
    }

    private void getConnections(){
        BzzApp.getBzHubRepository().getCompanyConnections((Constant.getInstance().getCompanyProfile().getCompanyId()))
                .subscribe(new Observer<ConnectionResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(ConnectionResponse connectionResponse) {
                        if (connectionResponse.getResult() != null){
                            setRecyclerView(connectionResponse.getResult());
                            tvNoData.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            layoutChat.setVisibility(View.VISIBLE);
                        }else {
                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            layoutChat.setVisibility(View.GONE);
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

    private void setRecyclerView(List<ConnectionResponse.Result > connections){
        if (connections.isEmpty()){
            tvNoData.setVisibility(View.VISIBLE);
            layoutChat.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }else {
            layoutChat.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            connectionAdapter = new ConnectionAdapter(currentActivity,connections,this);
            recyclerView.setAdapter(connectionAdapter);
        }


    }

    @Override
    public void onConnectionClicked(int position,ConnectionResponse.Result connection) {
        currentConnection = connection;
        selection = position;
    }

    @OnClick(R.id.layout_chat)
    void onChatClicked(){
        if (currentConnection != null && selection != -1){
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
               i.putExtra(Intent.EXTRA_EMAIL  , new String[]{currentConnection.getEmail()});
            i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
            i.putExtra(Intent.EXTRA_TEXT   , "body of email");
            try {
                startActivity(Intent.createChooser(i, ""));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(currentActivity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser){
            getConnections();
        }
    }
}
