package cn.live9666.cowboy;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import adapter.RvAdapterSearch;
import asynctask.StockSearchAsyncTask;
import model.Stock;
import response.StockSearchResponse;
import utils.CowboyHandlerKey;
import utils.CowboyResponseDocument;

@ContentView(R.layout.activity_search)
public class SearchActivity extends BaseActivity {

    @ViewInject(R.id.toolbar_search)
    private Toolbar toolbar;

    @ViewInject(R.id.rv_search)
    private RecyclerView rvSearch;

    private RvAdapterSearch adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        initRecyclerView();
    }

    private void initToolbar() {

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_arrow_back_white_24dp));
        toolbar.inflateMenu(R.menu.search);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        //设置展开后图标的样式,这里只有两种,一种图标在搜索框外,一种在搜索框内
        searchView.setIconifiedByDefault(false);
        // 写上此句后searchView初始是可以点击输入的状态，如果不写，那么就需要点击下放大镜，才能出现输入框,也就是设置为ToolBar的ActionView，默认展开
//        searchView.onActionViewExpanded();
        //添加提交按钮，监听在OnQueryTextListener的onQueryTextSubmit响应
//        searchView.setSubmitButtonEnabled(true);
        //将控件设置成可获取焦点状态,默认是无法获取焦点的,只有设置成true,才能获取控件的点击事件
//        searchView.setFocusable(true);
        //输入框内icon不显示
//        searchView.setIconified(false);
//        searchView.requestFocus();//输入焦点
//        searchView.requestFocusFromTouch();//模拟焦点点击事件

//        searchView.setFocusable(false);//禁止弹出输入法，在某些情况下有需要
//        searchView.clearFocus();//禁止弹出输入法，在某些情况下有需要

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

//                showToast("submit");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (TextUtils.isEmpty(newText)) {

                    adapter.setStocks(null);
                    return false;

                } else
                    searchStockRequest(newText);
                return true;
            }
        });
    }

    private void initRecyclerView() {

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });
        rvSearch.setLayoutManager(manager);
        rvSearch.setHasFixedSize(true);


        adapter = new RvAdapterSearch(this);
        rvSearch.setAdapter(adapter);
    }

    @Override
    protected void doMessage(Message message) {
        super.doMessage(message);

        Bundle bundle = message.getData();
        String status = bundle.getString(CowboyResponseDocument.STATUS);
        String desc = bundle.getString(CowboyResponseDocument.STATUS_INFO);

        if (CowboyResponseDocument.STATUS_CODE_SUCCESS.equals(status) &&
                message.what == CowboyHandlerKey.SEARCH_STOCK_RESULT_LIST) {

            StockSearchResponse response = bundle.getParcelable(CowboyResponseDocument.SEARCH_STOCK_RESULT_LIST);
            dealWithSearchResult(response, desc);

        } else {

            adapter.setStocks(null);
        }
    }

    private void searchStockRequest(String content) {

        if (!TextUtils.isEmpty(content) && content.length() > 7)
            return;

        StockSearchAsyncTask async = new StockSearchAsyncTask(handler, content);
        async.execute();
    }

    private void dealWithSearchResult(StockSearchResponse response, String desc) {

        if (response == null) {

            showToast(desc);
            return;
        }

        ArrayList<Stock> stocks = response.getStockList();
        adapter.setStocks(stocks);
    }

}
