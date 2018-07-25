package fragment;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import adapter.RvAdapterStock;
import asynctask.StocksInfoAsyncTask;
import cn.live9666.cowboy.R;
import cn.live9666.cowboy.SearchActivity;
import listener.SimpleItemTouchHelperCallback;
import model.Stock;
import response.StocksInfoResponse;
import utils.CowboyHandlerKey;
import utils.CowboyResponseDocument;
import utils.SharedPreferencesUtil;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_tab_stock)
public class TabStock extends BaseFragment implements Toolbar.OnMenuItemClickListener {

    private static final String TAG_DOWN = "tag_down";
    private static final String TAG_UP = "tag_up";

    @ViewInject(R.id.toolbar_stock)
    private Toolbar toolbar;

    @ViewInject(R.id.tabLayout_stock_title)
    private TabLayout tabLayout;

    @ViewInject(R.id.tv_stock_price)
    private TextView tvStockPrice;

    @ViewInject(R.id.tv_stock_applies)
    private TextView tvStockApplies;

    @ViewInject(R.id.recycler_tab_stock)
    private RecyclerView recyclerView;

    @ViewInject(R.id.tv_no_stocks)
    private TextView tvNoStocks;

    //股票数据轮询
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private RvAdapterStock adapter;
    private SimpleItemTouchHelperCallback simpleCallback;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            doMessage(msg);
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

    }

    @Override
    public void onResume() {
        super.onResume();
        getConcernedStocks();
    }

    @Override
    public void onPause() {
        super.onPause();
        shutdownExecutorService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        shutdownExecutorService();
    }

    //开启轮询股票数据
    private void startExecutorService(final String stocks) {

        if (TextUtils.isEmpty(stocks)) return;
        if (executorService.isShutdown()) {
            executorService = Executors.newSingleThreadScheduledExecutor();
        }
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                requestStocksInfo(stocks);
            }
        }, 0, 3000, TimeUnit.MILLISECONDS);
    }

    //关闭ExecutorService
    private void shutdownExecutorService() {
        if (!executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }

    private void initView() {

        initToolbar(toolbar);

        initTabLayout();

        initRecyclerView();
    }

    private void initToolbar(Toolbar toolbar) {

        toolbar.setTitle(getResources().getString(R.string.tab_stock));
        toolbar.inflateMenu(R.menu.tab_stock);
        toolbar.setOnMenuItemClickListener(this);
    }

    private void initTabLayout() {

        tvStockPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        tvStockApplies.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {

                    case 1:
                        setDrawableAndTag(tvStockPrice);
                        break;
                    case 2:
                        setDrawableAndTag(tvStockApplies);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {

                    case 1:
                        setDrawableAndTag(tvStockPrice);
                        break;
                    case 2:
                        setDrawableAndTag(tvStockApplies);
                        break;
                }
            }
        });
    }

    private void initRecyclerView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);

        adapter = new RvAdapterStock(activity);
        recyclerView.setAdapter(adapter);

        simpleCallback = new SimpleItemTouchHelperCallback(adapter, false, true);
        ItemTouchHelper helper = new ItemTouchHelper(simpleCallback);
        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_edit:
                showToast("编辑");
                break;
            case R.id.action_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
        }

        return false;
    }

    /**
     * 设置价格/振幅排序呢
     */
    private void setDrawableAndTag(TextView tv) {

        Drawable drawable = tv.getCompoundDrawables()[2];
        if (drawable == null) {

            tv.setTag(TAG_DOWN);
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    ContextCompat.getDrawable(context, R.mipmap.ic_eject_white_down_18dp), null);
        } else {

            if (tv.getTag() == TAG_DOWN) {

                tv.setTag(TAG_UP);
                tv.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        ContextCompat.getDrawable(context, R.mipmap.ic_eject_white_up_18dp), null);
            } else {

                tv.setTag(TAG_DOWN);
                tv.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        ContextCompat.getDrawable(context, R.mipmap.ic_eject_white_down_18dp), null);
            }
        }
    }

    ////////******网络请求******////////

    //请求行情数据
    private void requestStocksInfo(String stocks) {

        new StocksInfoAsyncTask(handler, stocks).execute();
    }


    ////////******domessage******////////
    private void doMessage(Message message) {

        Bundle bundle = message.getData();
        String status = bundle.getString(CowboyResponseDocument.STATUS);
        String desc = bundle.getString(CowboyResponseDocument.STATUS_INFO);

        if (TextUtils.isEmpty(status))
            Toast.makeText(activity, getResources().getString(R.string.net_error), Toast.LENGTH_SHORT).show();
        else if (CowboyResponseDocument.STATUS_CODE_SUCCESS.equals(status)) {

            switch (message.what) {

                case CowboyHandlerKey.STOCKS_INFO:

                    StocksInfoResponse response = bundle.getParcelable(CowboyResponseDocument.STOCKS_INFO);
                    dealWithStocksInfoResponse(response, desc);
                    break;
            }
        } else {

            Toast.makeText(activity, desc, Toast.LENGTH_SHORT).show();
        }
    }

    private void dealWithStocksInfoResponse(StocksInfoResponse response, String desc) {

        if (response == null) {

            Toast.makeText(activity, desc, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!"1".equals(response.getAllowed())) {
            shutdownExecutorService();
            simpleCallback.setLongPressEnable(true);
        } else simpleCallback.setLongPressEnable(false);

        ArrayList<Stock> stocks = response.getInfos();
        if (stocks != null && !stocks.isEmpty())
            adapter.setStocks(stocks);
        else
            Toast.makeText(activity, getResources().getString(R.string.operation_fail),
                    Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取关注的股票
     */
    private void getConcernedStocks() {

        String stocks = SharedPreferencesUtil.getStrings(CowboyResponseDocument.STOCKS_INFO, null);
        if (TextUtils.isEmpty(stocks)) {

            tvNoStocks.setVisibility(View.VISIBLE);
        } else {

            tvNoStocks.setVisibility(View.INVISIBLE);
            startExecutorService(stocks);
        }

    }
}
