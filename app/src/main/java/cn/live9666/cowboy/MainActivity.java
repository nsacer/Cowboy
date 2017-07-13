package cn.live9666.cowboy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import java.util.ArrayList;

import customview.CircleImageView;
import fragment.TabFind;
import fragment.TabIndex;
import fragment.TabLive;
import fragment.TabStock;
import fragment.TabTreasure;
import utils.CowboyResponseDocument;
import utils.CowboySetting;
import utils.MobileUtils;
import utils.PermissionUtil;
import utils.SharedPreferencesUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String FRAGMENT_INDEX = "index", FRAGMENT_STOCK = "stock",
            FRAGMENT_LIVE = "live", FRAGMENT_TREASURE = "treasure", FRAGMENT_FIND = "find";

    private static final String[] FRAGMENT_TAG = {FRAGMENT_INDEX, FRAGMENT_STOCK,
            FRAGMENT_LIVE, FRAGMENT_TREASURE, FRAGMENT_FIND};

    private FragmentManager fragmentManager;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ActionBarDrawerToggle toggle;
    private CardView cvTabBar;

    public CardView getBottomTab() {

        return cvTabBar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        initView();
    }

    private void initView() {


        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        initDrawHeader(navView);

        cvTabBar = (CardView) findViewById(R.id.cv_tab_bar);

        initRadioGroup();

        showFragmentByTag(fragments.get(0), FRAGMENT_INDEX);

        PermissionUtil.requestPermission(this, Manifest.permission.READ_PHONE_STATE,
                CowboyResponseDocument.PERMISSION_READ_PHONE_STATE);

        setInfo();

    }

    private void initDrawHeader(NavigationView nv) {

        View viewRoot = nv.getHeaderView(0);
        CircleImageView civHeader = (CircleImageView) viewRoot.findViewById(R.id.civ_header);
        civHeader.setOnClickListener(this);
    }

    /**
     * 添加、显示Fragment
     */
    private void showFragmentByTag(Fragment fragmentNew, String tag) {

        if (TextUtils.isEmpty(tag))
            return;

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (fragmentManager.findFragmentByTag(tag) != null) {

            hideFragments(fragmentManager, transaction);
            transaction.show(fragmentManager.findFragmentByTag(tag)).commit();

        } else if (fragmentNew != null) {

            hideFragments(fragmentManager, transaction);
            transaction.add(R.id.layout_content, fragmentNew, tag).commit();
        }

    }

    /**
     * hide fragment
     * */
    private void hideFragments(FragmentManager fragmentManager, FragmentTransaction transaction) {

        if(fragmentManager.findFragmentByTag(FRAGMENT_FIND) != null)
            transaction.hide(fragmentManager.findFragmentByTag(FRAGMENT_FIND));
        if(fragmentManager.findFragmentByTag(FRAGMENT_INDEX) != null)
            transaction.hide(fragmentManager.findFragmentByTag(FRAGMENT_INDEX));
        if(fragmentManager.findFragmentByTag(FRAGMENT_LIVE) != null)
            transaction.hide(fragmentManager.findFragmentByTag(FRAGMENT_LIVE));
        if(fragmentManager.findFragmentByTag(FRAGMENT_STOCK) != null)
            transaction.hide(fragmentManager.findFragmentByTag(FRAGMENT_STOCK));
        if(fragmentManager.findFragmentByTag(FRAGMENT_TREASURE) != null)
            transaction.hide(fragmentManager.findFragmentByTag(FRAGMENT_TREASURE));
    }


    /**
     * init RadioGroup
     */
    private void initRadioGroup() {

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg_tab);

        TabIndex fragIndex = new TabIndex();
        TabStock fragStock = new TabStock();
        TabLive fragLive = new TabLive();
        TabTreasure fragTreasure = new TabTreasure();
        TabFind fragFind = new TabFind();

        fragments.add(fragIndex);
        fragments.add(fragStock);
        fragments.add(fragLive);
        fragments.add(fragTreasure);
        fragments.add(fragFind);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                int index = 0;
                switch (checkedId) {

                    case R.id.trb_index:
                        index = 0;
                        break;

                    case R.id.trb_stock:
                        index = 1;
                        break;

                    case R.id.trb_live:
                        index = 2;
                        break;

                    case R.id.trb_treasure:
                        index = 3;
                        break;

                    case R.id.trb_find:
                        index = 4;
                        break;
                }
                showFragmentByTag(fragments.get(index), FRAGMENT_TAG[index]);
            }
        });

        radioGroup.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (fragmentManager.findFragmentByTag(FRAGMENT_INDEX).isVisible()) {

                    TabIndex one = (TabIndex) fragmentManager.findFragmentByTag(FRAGMENT_INDEX);
                    one.scrollToTop();
                }
            }
        });
    }

    /**
     * 点击事件
     * 1、底部五个按钮
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.civ_header:

                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.trb_index:

                if (fragmentManager.findFragmentByTag(FRAGMENT_INDEX).isVisible()) {

                    TabIndex one = (TabIndex) fragmentManager.findFragmentByTag(FRAGMENT_INDEX);
                    one.scrollToTop();
                }
                break;

            default:
                break;
        }
    }

    /**
     * NavigationViewItemClickListener
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_setting:

                break;

            case R.id.nav_box:

                break;

            case R.id.nav_live:

                break;

            case R.id.nav_ask:

                break;

            case R.id.nav_notification:

                break;

            case R.id.nav_help:

                break;

            case R.id.nav_feedback:

                break;

            case R.id.nav_darkMode:

                boolean b = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.SP_DARK_MODE, false);
                SharedPreferencesUtil.putBoolean(SharedPreferencesUtil.SP_DARK_MODE,
                        !b);
                this.recreate();
                break;

            default:
                break;
        }

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 供Fragment的toolbar绑定DrawerLayout
     * Fragment：TabIndex、TabStock、TabLive、TabTreasure、TabFind
     */
    public void bindDrawer(Toolbar toolbar) {

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
//                R.string.drawer_open, R.string.drawer_close);
        toggle = new ActionBarDrawerToggle(this, drawer, 0, 0);
        if (getActionBar() != null) getActionBar().setDisplayHomeAsUpEnabled(true);
        drawer.addDrawerListener(toggle);
//        toggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        toggle.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CowboyResponseDocument.PERMISSION_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    setInfo();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    /***/
    private void setInfo() {


        CowboySetting.IMEI = MobileUtils.getIMEI(getApplicationContext());
        CowboySetting.IMSI = MobileUtils.getIMSI(getApplicationContext());
        CowboySetting.MODEL = Build.MODEL;
        CowboySetting.PHONE_BRAND = Build.BRAND;
        CowboySetting.SDK = String.valueOf(android.os.Build.VERSION.RELEASE);
        CowboySetting.UUID = MobileUtils.getUUID(getApplicationContext());
        CowboySetting.DEVICE_ID = MobileUtils.getDeviceId(getApplicationContext());
//        CowboySetting.CHANNEL = MobileUtils.getChannelName(this);
//        CowboySetting.CLIENT_VERSION = MobileUtils.getVersion(this);
        CowboySetting.CHANNEL = "cowboy";
        CowboySetting.CLIENT_VERSION = "3.2.0";
    }

}
