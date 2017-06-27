package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zpf on 2016/8/3.
 * TabIndex 里边 titlePage用的adapter
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();
    private String [] titles;

    public List<Fragment> getFragments() {

        return fragments;
    }

    public MainPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] sTitles) {
        super(fm);

        this.fragments = fragments;
        titles = sTitles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
