package smartdev.bzzhub.ui.fragment;

import android.Manifest;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import smartdev.bzzhub.BzzApp;

import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.BookResponse;
import smartdev.bzzhub.ui.activity.CompanyDetailActivity;
import smartdev.bzzhub.ui.adapter.PDFAdapter;
import smartdev.bzzhub.ui.base.BaseFragment;
import smartdev.bzzhub.util.navigation.NavigationManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static smartdev.bzzhub.util.navigation.Arg.ARG_URL_PDF;


public class CompanyTenderFragment extends BaseFragment implements PDFAdapter.SelectedListener{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Integer companyId;
    @BindView(R.id.tv_document)
    TextView tvDocument;
    @BindView(R.id.tv_no_data)
            TextView tvNoData;

    List<BookResponse.PDFResult> pdfResults = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_tender, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();
        extractArgument();
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

    private void initRecyclerView(){
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(currentActivity,3));
    }

    private void extractArgument(){
        companyId =  ((CompanyDetailActivity)currentActivity).companyId;
    }

    private void getData(){
        BzzApp.getBzHubRepository().getBookList((companyId))
                .doOnNext(bookResponse -> {
                    if (bookResponse.getCode() != 200)
                        CompanyTenderFragment.this.onError(new Exception(bookResponse.getMessage()));
                })
                .subscribe(new Observer<BookResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(BookResponse companyDetailResponse) {
                        if (companyDetailResponse.getResult() != null ){
                            tvDocument.setVisibility(View.VISIBLE);
                            pdfResults = companyDetailResponse.getResult();
                            if (pdfResults.isEmpty()){
                                tvDocument.setVisibility(View.GONE);
                                tvNoData.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }else {
                                tvNoData.setVisibility(View.GONE);
                                recyclerView.setAdapter(new PDFAdapter(currentActivity,companyDetailResponse.getResult(),CompanyTenderFragment.this::onSelected));
                            }
                        }else {
                            recyclerView.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);
                            tvDocument.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        CompanyTenderFragment.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });
    }

    @Override
    public void onSelected(int position) {

        RxPermissions permissions = new RxPermissions(this);
        Disposable subscribe = permissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                    Bundle bundle = new Bundle();

                    bundle.putString(ARG_URL_PDF,pdfResults.get(position).getFilePath());
                    NavigationManager.gotoPDFActivity(currentActivity,bundle);
                    }
                });

    }
}
