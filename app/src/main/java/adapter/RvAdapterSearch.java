package adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.live9666.cowboy.R;
import me.grantland.widget.AutofitTextView;
import model.Stock;
import utils.CowboyResponseDocument;
import utils.SharedPreferencesUtil;

/**
 * Created by zpf on 2017/4/21.
 */

public class RvAdapterSearch extends RecyclerView.Adapter<RvAdapterSearch.SearchHolder> implements View.OnClickListener{

    private static final int VIEW_TYPE_HEAD = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private Context mContext;
    private ArrayList<Stock> stocks = new ArrayList<>();

    private TextView tvHintResult;

    public RvAdapterSearch(Context context) {
        this.mContext = context;
    }

    public void setStocks(ArrayList<Stock> stocks) {

        this.stocks.clear();

        if (stocks != null && !stocks.isEmpty()) {

            this.stocks.addAll(stocks);
            tvHintResult.setText(mContext.getResources().getString(R.string.search_result));
        } else {

            tvHintResult.setText(mContext.getResources().getString(R.string.search_no_result));
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_HEAD : VIEW_TYPE_ITEM;
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_HEAD) {

            View viewHead = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_view, parent, false);
            tvHintResult = (TextView) viewHead.findViewById(R.id.tv);
            tvHintResult.setText(parent.getContext().getResources().getString(R.string.search_no_result));
            return new SearchHolder(viewHead, viewType);
        } else {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_stock, parent, false);
            ImageButton ib = (ImageButton) view.findViewById(R.id.ib_add_search_stock);
            ib.setOnClickListener(this);

            return new SearchHolder(view, viewType);
        }
    }

    @Override
    public void onBindViewHolder(final SearchHolder holder, int position) {

        if (position == 0)
            return;

        position = position - 1;

        Stock stock = stocks.get(position);
        if (stock == null)
            return;

        String codeSource = stock.getStockCode();
        String code = codeSource + "\n";
        String name = stock.getStockName();
        holder.tvStock.setText(code);

        if (!TextUtils.isEmpty(name)) {

            SpannableString ss = new SpannableString(name);
            ss.setSpan(new RelativeSizeSpan(0.6f), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.tab_index_read)),
                    0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvStock.append(ss);
        }

        holder.ib.setTag(R.id.cv_search_stock, stock);

        String stocks = SharedPreferencesUtil.getStrings(CowboyResponseDocument.STOCKS_INFO, null);
        if (!TextUtils.isEmpty(stocks) && !TextUtils.isEmpty(codeSource) &&
                stocks.contains(codeSource)) {

            holder.ib.setTag(R.id.ib_add_search_stock, true);
            doRotationAnimCM45(holder.ib);
        } else {

            holder.ib.setTag(R.id.ib_add_search_stock, false);
        }
    }

    @Override
    public int getItemCount() {
        return stocks.size() + 1;
    }

    @Override
    public void onClick(View v) {

        if (v.getTag(R.id.cv_search_stock) != null &&
                v.getTag(R.id.cv_search_stock) instanceof Stock) {

            Stock stock = (Stock) v.getTag(R.id.cv_search_stock);
            String code = stock.getStockCode();
            concernedStock(v, code);
        }
    }

    class SearchHolder extends RecyclerView.ViewHolder {

        AutofitTextView tvStock;
        ImageButton ib;

        SearchHolder(View itemView, int viewType) {
            super(itemView);

            if (viewType == VIEW_TYPE_HEAD)
                return;

            tvStock = (AutofitTextView) itemView.findViewById(R.id.tv_code_search_stock_result_item);
            ib = (ImageButton) itemView.findViewById(R.id.ib_add_search_stock);

        }
    }

    private void doRotationAnimCM45(View target) {

        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", 0f, 45f);
        animator.setDuration(200);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();

    }

    private void doRotationAnimCM90(View target) {

        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", 0f, 90f);
        animator.setDuration(200);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();

    }

    private void concernedStock(View v, String code) {

        if (TextUtils.isEmpty(code)) {

            Toast.makeText(mContext, mContext.getResources().getString(R.string.operation_fail), Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isConcerned = (boolean) v.getTag(R.id.ib_add_search_stock);
        if (isConcerned) {

            String stocks = SharedPreferencesUtil.getStrings(CowboyResponseDocument.STOCKS_INFO, null);
            if (!TextUtils.isEmpty(stocks)) {

                int index = stocks.indexOf(code);
                boolean bDelete = false;
                if (index > 0) {

                    String stocksNew = stocks.replace("|" + code, "");
                    bDelete = SharedPreferencesUtil.putString(CowboyResponseDocument.STOCKS_INFO, stocksNew);
                } else if (index == 0){

                    String stocksNew = stocks.replace(code + "|", "");
                    bDelete = SharedPreferencesUtil.putString(CowboyResponseDocument.STOCKS_INFO, stocksNew);
                }

                String resultDelete = bDelete ? mContext.getResources().getString(R.string.delete_success) :
                        mContext.getResources().getString(R.string.delete_fail);
                Toast.makeText(mContext, resultDelete, Toast.LENGTH_SHORT).show();

                if (bDelete) {

                    doRotationAnimCM90(v);
                    v.setTag(R.id.ib_add_search_stock, false);
                }

            }

        } else {

            String stocks = SharedPreferencesUtil.getStrings(CowboyResponseDocument.STOCKS_INFO, null);

            boolean concerned;
            if (TextUtils.isEmpty(stocks)) {

                concerned = SharedPreferencesUtil.putString(CowboyResponseDocument.STOCKS_INFO, code);
            } else {

                String stocksNew = stocks + "|" + code;
                concerned = SharedPreferencesUtil.putString(CowboyResponseDocument.STOCKS_INFO, stocksNew);
            }

            String desc = concerned ? mContext.getResources().getString(R.string.concern_stock_success) :
                    mContext.getResources().getString(R.string.concern_stock_fail);
            Toast.makeText(mContext, desc, Toast.LENGTH_SHORT).show();

            if (concerned) {

                doRotationAnimCM45(v);
                v.setTag(R.id.ib_add_search_stock, true);
            }
        }

    }

}
