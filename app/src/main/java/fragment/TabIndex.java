package fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import adapter.MainPagerAdapter;
import cn.live9666.cowboy.MainActivity;
import cn.live9666.cowboy.R;
import cn.live9666.cowboy.SearchActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabIndex extends Fragment implements Toolbar.OnMenuItemClickListener,
        TabLayout.OnTabSelectedListener {

    private String[] pageTitles;
    /**
     * 用来设置Toolbar双击后fragment中的页面回到顶部
     */
    private long mLastTime = 0, mCurTime = 0;

    private ViewPager viewPager;
    private MainPagerAdapter pagerAdapter;

    public TabIndex() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_tab_index, container, false);

        initView(root);

        return root;
    }

    /**
     * init view
     */
    private void initView(View root) {

        pageTitles = getResources().getStringArray(R.array.page_titles);

        initToolbar(root);

        initTabLayoutAndViewPager(root);
    }

    /**
     * init Toolbar
     */
    private void initToolbar(View root) {

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.ic_account_circle_white_24dp);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.app_name);
        ((MainActivity) getActivity()).bindDrawer(toolbar);
        toolbar.setOnMenuItemClickListener(this);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLastTime = mCurTime;
                mCurTime = System.currentTimeMillis();
                if (mCurTime - mLastTime < 200) {

                    int currentIndex = viewPager.getCurrentItem();
                    resetRecyclerView(currentIndex);

                }
            }
        });

    }

    /**
     * getInnerFragment
     */
    private void resetRecyclerView(int currentIndex) {

        switch (currentIndex) {

            case 0:

                scrollToTop();
                break;

            case 1:

                break;

            case 2:

                break;
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.tab_index_search, menu);
    }

    /**
     * init TabLayout
     */
    private void initTabLayoutAndViewPager(View root) {

        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(this);

        String[] sTitles = new String[]{
                getResources().getString(R.string.tab_title_1),
                getResources().getString(R.string.tab_title_2),
                getResources().getString(R.string.tab_title_3)};

        TitleOne titleOne = new TitleOne();
        TitleTwo titleTwo = new TitleTwo();
        TitleThree titleThree = new TitleThree();

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(titleOne);
        fragmentArrayList.add(titleTwo);
        fragmentArrayList.add(titleThree);

        viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        pagerAdapter = new MainPagerAdapter(getChildFragmentManager(),
                fragmentArrayList, sTitles);
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_search:

                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /**
     * 查找到Fragment TitleOne 并调用它的scrollToTop();滚动到顶部
     */
    public void scrollToTop() {

        Fragment fragment = pagerAdapter.getFragments().get(viewPager.getCurrentItem());
        if (fragment instanceof TitleOne)
            ((TitleOne) fragment).scrollToTop();
    }
}
