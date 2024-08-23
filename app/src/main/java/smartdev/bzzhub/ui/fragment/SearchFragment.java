package smartdev.bzzhub.ui.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import smartdev.bzzhub.R;
import smartdev.bzzhub.ui.base.BaseFragment;
import smartdev.bzzhub.ui.activity.OnboardingActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearchFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.tv_next) void onNextClicked(){
        ((OnboardingActivity) currentActivity).onNextClicked();
    }
}
