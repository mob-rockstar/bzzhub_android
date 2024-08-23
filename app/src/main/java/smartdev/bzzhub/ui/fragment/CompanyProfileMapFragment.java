package smartdev.bzzhub.ui.fragment;

import android.os.Bundle;
    
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.CompanyLocationResponse;
import smartdev.bzzhub.ui.base.BaseFragment;
import smartdev.bzzhub.util.Constant;

public class CompanyProfileMapFragment extends BaseFragment implements OnMapReadyCallback {
    GoogleMap map;
    private FragmentManager fm;
    private double latitude,longitude;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_map, container, false);
        ButterKnife.bind(this, view);
    //    getLocation();
        fm = getChildFragmentManager();

        initMap();
        return view;
    }

    private void initMap() {
        if (null != fm) {
            SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.fMap);
            if (mapFragment != null)
                mapFragment.getMapAsync(this);
        }
    }

    private void getLocation(){
        BzzApp.getBzHubRepository().getCompanyLocation((Constant.getInstance().getCompanyProfile().getCompanyId()))
                .subscribe(new Observer<CompanyLocationResponse>() {
                    Disposable mDisposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(CompanyLocationResponse companyLocationResponse) {
                        if (companyLocationResponse.getResult() != null){
                            try {
                                latitude = Double.parseDouble(companyLocationResponse.getResult().getLatitude());
                                longitude = Double.parseDouble(companyLocationResponse.getResult().getLongitude());
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(new LatLng(latitude,longitude));
                                map.addMarker(markerOptions);
                                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude)));
                            }catch (Exception ignored){

                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mDisposable.dispose();
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser){
            getLocation();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        if (map != null) {
            map.clear();
        }
        super.onDestroyView();
    }
}
