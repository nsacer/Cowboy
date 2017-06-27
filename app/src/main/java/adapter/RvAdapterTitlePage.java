package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.codehaus.jackson.type.TypeReference;

import java.util.ArrayList;
import java.util.List;

import cn.live9666.cowboy.R;
import cn.live9666.cowboy.WebViewActivity;
import model.FirstResourceModel;
import model.IndexHotPointModel;
import top.wefor.circularanim.CircularAnim;
import utils.JsonUtil;

/**
 * Created by zpf on 2016/11/2.
 */
public class RvAdapterTitlePage extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_HEADER = 0, TYPE_NORMAL = 1, TYPE_FOOTER = -1;
    private Context mContext;
    private LayoutInflater inflater;
    private View headerView;
    private ProgressBar pbFooter;
    private boolean isLoadingMore = false;
    private boolean hasMoreData = false;
    private ArrayList<FirstResourceModel> dataList = new ArrayList<>();

    public RvAdapterTitlePage(Context context) {

        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public boolean isLoadingMore() {
        return isLoadingMore;
    }

    public void setLoadingMore(boolean loadingMore) {
        isLoadingMore = loadingMore;
    }

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
    }

    /**
     * 清空重新设置List数据
     */
    public void setDataList(List<FirstResourceModel> list) {

        if (list != null && !list.isEmpty()) {

            dataList.clear();
            for (int i = 0; i < list.size(); i++) {

                //TODO 暂时只显示类型为1的数据
                if (!"1".equals(list.get(i).getType()))
                    list.remove(i);
            }
            dataList.addAll(list);

            hasMoreData = true;

            notifyDataSetChanged();

        } else
            hasMoreData = false;
    }

    /**
     * 追加List数据
     */
    public void appendDataList(List<FirstResourceModel> list) {

        if (list != null && !list.isEmpty()) {

            hasMoreData = true;
            dataList.addAll(list);
            this.notifyDataSetChanged();

        } else {

            hasMoreData = false;
        }

        setFooterView(hasMoreData);
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return TYPE_HEADER;
        else if (position == getItemCount() - 1)
            return TYPE_FOOTER;
        else
            return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View root;
        if (viewType == TYPE_HEADER)
            root = headerView;
        else if (viewType == TYPE_FOOTER) {

            View footerView = inflater.inflate(R.layout.item_footer, parent, false);
            pbFooter = (ProgressBar) footerView.findViewById(R.id.pb_footer);
            pbFooter.getIndeterminateDrawable().setColorFilter(0xffff0000,
                    android.graphics.PorterDuff.Mode.SRC_IN);

            setFooterView(hasMoreData);

            return new HolderOne(footerView, viewType);
        } else
            root = inflater.inflate(R.layout.item_one_title_page, parent, false);
        return new HolderOne(root, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        setDataToTarget(holder, position);
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 2;
    }

    private void setDataToTarget(RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        if (viewType == TYPE_HEADER)
            return;

        if (viewType == TYPE_FOOTER)
            return;

        int positionReal = position - 1;

        FirstResourceModel model = dataList.get(positionReal);

        ((HolderOne) holder).tvTitle.setText(model.getTitle());

        String contentJson = model.getContentJson();
        if (TextUtils.isEmpty(contentJson))
            return;
        ArrayList<IndexHotPointModel> hotPointModels = JsonUtil.Json2Object(contentJson,
                new TypeReference<ArrayList<IndexHotPointModel>>() {
                });

        ((HolderOne) holder).recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        ((HolderOne) holder).recyclerView.setHasFixedSize(true);
        RvAdapterTitlePageInner adapter = new RvAdapterTitlePageInner(mContext);
        adapter.setModelList(hotPointModels);
        ((HolderOne) holder).recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RvAdapterTitlePageInner.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, IndexHotPointModel model) {

                if (model.getArticleUrl() == null)
                    return;

                final String url = model.getArticleUrl();
                CircularAnim.fullActivity((Activity) mContext, view)
                        .colorOrImageRes(R.color.colorAccent)
                        .go(new CircularAnim.OnAnimationEndListener() {
                            @Override
                            public void onAnimationEnd() {

                                Intent intent = new Intent(mContext, WebViewActivity.class);
                                intent.putExtra(WebViewActivity.WEB_URL, url);
                                mContext.startActivity(intent);
                            }
                        });
            }
        });

    }

    private class HolderOne extends RecyclerView.ViewHolder {

        TextView tvTitle;
        RecyclerView recyclerView;

        HolderOne(View itemView, int viewType) {
            super(itemView);

            if (viewType == TYPE_HEADER)
                return;

            if (viewType == TYPE_FOOTER)
                return;

            tvTitle = (TextView) itemView.findViewById(R.id.tv_card_title);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_inner);
        }
    }

    /**
     * 处理加载更多footerview
     */
    private void setFooterView(boolean hasMoreData) {

        if (hasMoreData && pbFooter != null)
            pbFooter.setVisibility(View.VISIBLE);
        else if (pbFooter != null)
            pbFooter.setVisibility(View.INVISIBLE);
    }
}
