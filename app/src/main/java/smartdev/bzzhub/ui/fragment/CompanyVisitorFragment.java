package smartdev.bzzhub.ui.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import smartdev.bzzhub.R;
import smartdev.bzzhub.ui.base.BaseFragment;

public class CompanyVisitorFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_visitor, container, false);
    }
}
