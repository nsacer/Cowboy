package fragment;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import adapter.AdViewPagerAdapter;
import adapter.RvAdapterTitlePage;
import asynctask.GetTabResourceAsyncTask;
import cn.live9666.cowboy.MainActivity;
import cn.live9666.cowboy.R;
import cn.live9666.cowboy.WebViewActivity;
import model.AdModel;
import response.GetTabResourceResponse;
import transformer.ZoomOutPageTransformer;
import utils.CowboyHandlerKey;
import utils.CowboyResponseDocument;

/**
 * A simple {@link Fragment} subclass.
 */
public class TitleOne extends Fragment {

    private final String SHOW = "1";
    private final String TYPE_AD_ROOM = "1", TYPE_AD_WEB = "2";
    private Context mContext;
    private View root;
    private static final String[] IMAGES = new String[]{
            "http://isujin.com/wp-content/uploads/2017/02/wallhaven-314162-300x188.jpg",
            "http://isujin.com/wp-content/uploads/2017/02/wallhaven-164067-300x169.jpg",
            "http://isujin.com/wp-content/uploads/2017/02/wallhaven-314162-300x188.jpg",
            "http://isujin.com/wp-content/uploads/2017/02/wallhaven-164067-300x169.jpg",
            "http://isujin.com/wp-content/uploads/2017/02/wallhaven-314162-300x188.jpg"};

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    /**
     * 判断RecyclerView是否到达顶部
     */
    private boolean isRvTop = false;

    /**
     * HeaderView
     */
    private View viewHeader;
    private AdViewPagerAdapter adapterAdHeader;

    private String tabId = "1", lastResourceId = "0";

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            doMessage(msg);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();

        requestTitlePageDate(tabId, CowboyResponseDocument.DOWNLOAD, lastResourceId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_fragment_title_one, container, false);

        initView();

        return root;
    }

    private void initView() {

        initSwipeRefreshLayout();

        initRecyclerView();
    }

    private void initSwipeRefreshLayout() {

        refreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                requestTitlePageDate(tabId, CowboyResponseDocument.DOWNLOAD, lastResourceId);
            }
        });
        refreshLayout.setRefreshing(Boolean.TRUE);
    }

    private void initRecyclerView() {

        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        RvAdapterTitlePage adapter = new RvAdapterTitlePage(mContext);
        initHeaderView(recyclerView);
        adapter.setHeaderView(viewHeader);

        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                confirmIsTop(recyclerView);

                loadingMore(recyclerView);

                setRadioGroupTabBar(dy);
            }
        });

    }


    /**
     * doMessage
     */
    private void doMessage(Message message) {

        Bundle bundle = message.getData();
        String status = bundle.getString(CowboyResponseDocument.STATUS);
        String desc = bundle.getString(CowboyResponseDocument.STATUS_INFO);

        if (TextUtils.isEmpty(status))
            Toast.makeText(mContext, getResources().getString(R.string.net_error), Toast.LENGTH_SHORT).show();
        else if (CowboyResponseDocument.STATUS_CODE_SUCCESS.equals(status)) {

            switch (message.what) {

                case CowboyHandlerKey.DOWNLOAD_INDEX_TITLE_ONE:

                    refreshLayout.setRefreshing(Boolean.FALSE);
                    GetTabResourceResponse response = (GetTabResourceResponse) bundle
                            .get(CowboyResponseDocument.RESPONSE_INDEX_TITLE_ONE);
                    dealWithResponse(response, desc);
                    break;

                case CowboyHandlerKey.LOAD_MORE_INDEX_TITLE_ONE:

                    GetTabResourceResponse responseMore = (GetTabResourceResponse) bundle
                            .get(CowboyResponseDocument.RESPONSE_INDEX_TITLE_ONE);
                    dealWithResponseMore(responseMore, desc);
                    break;

                default:
                    break;
            }
        } else {

            Toast.makeText(mContext, desc, Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * 请求TabIndex title one的数据
     */
    private void requestTitlePageDate(String tabId, String loadAction, String lastResourceId) {

        if (TextUtils.isEmpty(loadAction) || TextUtils.isEmpty(tabId) ||
                TextUtils.isEmpty(lastResourceId))
            return;

        GetTabResourceAsyncTask asyncTask = new GetTabResourceAsyncTask();
        asyncTask.setContext(getContext());
        asyncTask.setHandler(handler);
        asyncTask.setTabId(tabId);
        asyncTask.setAction(loadAction);
        asyncTask.setLastResourceId(lastResourceId);
        asyncTask.execute();
    }

    /***
     * 处理Response
     */
    private void dealWithResponse(GetTabResourceResponse response, String desc) {

        if (response == null) {

            Toast.makeText(mContext, desc, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(response.getLastResourceId()))
            lastResourceId = response.getLastResourceId();

        //TODO 是否显示HeaderView
        boolean showHeaderView = SHOW.equals(response.getIsAdShow());

        if (showHeaderView) {

            adapterAdHeader.setArrayList(response.getAdList());
        }

        ((RvAdapterTitlePage) recyclerView.getAdapter())
                .setDataList(response.getResourceList());
    }

    /**
     * 处理加载更多的Response
     * */
    private void dealWithResponseMore(GetTabResourceResponse response, String desc) {

        if (response == null) {

            Toast.makeText(mContext, desc, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(response.getLastResourceId()))
            lastResourceId = response.getLastResourceId();

        RvAdapterTitlePage adapter = (RvAdapterTitlePage) recyclerView.getAdapter();
        adapter.setLoadingMore(false);
        adapter.appendDataList(response.getResourceList());
    }

    /**
     * init header view
     */
    private void initHeaderView(RecyclerView parent) {

        viewHeader = LayoutInflater.from(mContext).inflate(R.layout.viewpager, parent, false);
        viewHeader.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) mContext.getResources().getDimension(R.dimen.view_pager_header_height)));
        ViewPager vpAd = (ViewPager) viewHeader.findViewById(R.id.viewPager);
        vpAd.setPageMargin(this.getResources().getDimensionPixelOffset(R.dimen.view_pager_header_page_margin));
        vpAd.setOffscreenPageLimit(6);
        vpAd.setPageTransformer(true, new ZoomOutPageTransformer());
        adapterAdHeader = new AdViewPagerAdapter(mContext);
        vpAd.setAdapter(adapterAdHeader);
        adapterAdHeader.setOnItemClickListener(new AdViewPagerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, AdModel model) {

                dealWithAdHeaderItemClick(model);
            }
        });

    }

    /**
     * 廣告Header的item的點擊事件處理
     */
    private void dealWithAdHeaderItemClick(AdModel model) {

        if (model == null)
            return;

        if (!TextUtils.isEmpty(model.getType())) {

            switch (model.getType()) {

                case TYPE_AD_ROOM:

                    if (!TextUtils.isEmpty(model.getRoomId()))
                        Toast.makeText(mContext, model.getRoomId(),
                                Toast.LENGTH_SHORT).show();
                    break;

                case TYPE_AD_WEB:

                    if (!TextUtils.isEmpty(model.getUrl())) {

                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra(WebViewActivity.WEB_URL, model.getUrl());
                        intent.putExtra(WebViewActivity.SHARE_ABLE, model.getIsShare());
                        mContext.startActivity(intent);
                    }

                    break;

                default:
                    break;
            }
        }
    }

    /**
     * 确定RecyclerView是否到达顶部
     */
    private void confirmIsTop(RecyclerView recyclerView) {

        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstItem = manager.findFirstVisibleItemPosition();
        View viewFirst = manager.findViewByPosition(firstItem);
        isRvTop = (recyclerView.getTop() == viewFirst.getTop() && firstItem == 0);

    }

    /**
     * 上拉加载更多数据
     * */
    private void loadingMore(RecyclerView recyclerView) {

        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int lastIndex = manager.findLastVisibleItemPosition();
        View viewLast = manager.findViewByPosition(lastIndex);
        RvAdapterTitlePage adapter = (RvAdapterTitlePage) recyclerView.getAdapter();
        if (viewLast.getBottom() == recyclerView.getBottom() && !adapter.isLoadingMore()) {

            requestTitlePageDate(tabId, CowboyResponseDocument.LOAD_MORE, lastResourceId);
            adapter.setLoadingMore(true);
        }

    }

    /**
     * 对于底部的tab bar显示隐藏
     * */
    private void setRadioGroupTabBar(int dy) {

        CardView rv = ((MainActivity) getActivity()).getBottomTab();
        doAnimRadioGroup(rv, dy);
    }

    /**
     * 使RecyclerView滚动到顶部
     */
    public void scrollToTop() {

        if (isRvTop || recyclerView == null)
            return;
//        recyclerView.scrollToPosition(0);
        recyclerView.smoothScrollToPosition(0);
    }

    /**
     * 添加RadioGroup的出现、消失动画
     * */
    private void doAnimRadioGroup(final View target, int dy) {

        String tagHide = "hide";
        String tagShow = "show";

        if (dy > 24 && target.getTag() != tagHide) {

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(
                    ObjectAnimator.ofFloat(target, "alpha", 1, 0),
                    ObjectAnimator.ofFloat(target, "translationY", 0, target.getMeasuredHeight()));
            animatorSet.setDuration(200);
            animatorSet.setInterpolator(new DecelerateInterpolator());
            animatorSet.start();

            target.setTag(tagHide);

            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {

                    target.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

        } else if (dy <= -16 && target.getTag() == tagHide){

            target.setVisibility(View.VISIBLE);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(
                    ObjectAnimator.ofFloat(target, "alpha", 0, 1),
                    ObjectAnimator.ofFloat(target, "translationY", target.getMeasuredHeight(), 0));
            animatorSet.setDuration(200);
            animatorSet.setInterpolator(new DecelerateInterpolator());
            animatorSet.start();

            target.setTag(tagShow);

        }
    }

}
