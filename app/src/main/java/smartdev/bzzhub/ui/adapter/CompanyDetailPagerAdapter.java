package smartdev.bzzhub.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class CompanyDetailPagerAdapter  extends FragmentPagerAdapter {

    List<Fragment> mFragment;
    List<String> mTitleList;
    public CompanyDetailPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        mFragment = fragmentList;
    }

    public CompanyDetailPagerAdapter(FragmentManager fm, List<Fragment> fragmentList,List<String> titles) {
        super(fm);
        mFragment = fragmentList;
        this.mTitleList = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }


    @Override
    public int getCount() {
        return mTitleList.size(); //three fragments
    }


}