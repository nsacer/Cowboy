package adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import cn.live9666.cowboy.R;
import listener.OnItemTouchListener;
import model.Stock;
import utils.CowboyResponseDocument;
import utils.SharedPreferencesUtil;

/**
 * Created by Administrator on 2017/4/25.
 */

public class RvAdapterStock extends RecyclerView.Adapter<RvAdapterStock.StockViewHolder> implements OnItemTouchListener {

    private Context mContext;
    private ArrayList<Stock> stocks = new ArrayList<>();
    private AlertDialog alertDialog;
    private int positionDelete;

    public RvAdapterStock(Context context) {

        this.mContext = context;
    }

    public ArrayList<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(ArrayList<Stock> stocks) {

        if (stocks != null && !stocks.isEmpty()) {

            this.stocks.clear();
            this.stocks = stocks;
            notifyDataSetChanged();
        }
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_personal_stock, parent, false);

        return new StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, int position) {

        Stock stock = stocks.get(position);
        if (stock == null)
            return;

        String codeSource = stock.getStockCode();
        String code = codeSource + "\n";
        String name = stock.getStockName();
        String status = stock.getTradingStatus();

        holder.tvStock.setText(code);

        if (!TextUtils.isEmpty(name)) {

            SpannableString ss = new SpannableString(name);
            ss.setSpan(new RelativeSizeSpan(0.7f), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.tab_index_read)),
                    0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvStock.append(ss);
        }

        int color = ContextCompat.getColor(mContext, R.color.stock_gray);
        String pxRadio = stock.getPxChgRatio();
        if (!TextUtils.isEmpty(pxRadio)) {

            BigDecimal bd = new BigDecimal(pxRadio);
            String s = bd.setScale(2, BigDecimal.ROUND_UP).toString();
            double b = bd.setScale(2, BigDecimal.ROUND_UP).doubleValue();
            if (b > 0) {

                s = "+" + s;
                color = ContextCompat.getColor(mContext, R.color.stock_red);
            } else if (b == 0) {

                color = ContextCompat.getColor(mContext, R.color.stock_gray);
            } else {

                color = ContextCompat.getColor(mContext, R.color.stock_green);
            }

            s = s + "%";
            holder.tvPxRadio.setText(s);

        } else {

            holder.tvPxRadio.setText(status);
        }
        holder.tvPxRadio.setBackgroundColor(color);

        holder.tvPrice.setText(stock.getCurrentPrice());
        holder.tvPrice.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        Collections.swap(stocks, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);

        return changeStockPosition(fromPosition, toPosition);
    }

    @Override
    public void onSwipeDismiss(int position) {

        positionDelete = position;
        popAlertDialog();
    }

    class StockViewHolder extends RecyclerView.ViewHolder {

        private TextView tvStock;
        private TextView tvPrice;
        private TextView tvPxRadio;

        StockViewHolder(View itemView) {
            super(itemView);

            tvStock = (TextView) itemView.findViewById(R.id.tv_stock_personal);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price_personal);
            tvPxRadio = (TextView) itemView.findViewById(R.id.tv_radio_personal);
        }
    }

    /**
     * 弹出AlertDialog
     */
    private void popAlertDialog() {

        if (alertDialog == null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            alertDialog = builder
                    .setMessage(R.string.delete_confirm)
                    .setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            deleteConcernStock(positionDelete);
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    })
                    .create();

            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {

                    notifyDataSetChanged();
                }
            });

        }

        alertDialog.show();

//        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
//                .setTextColor(ContextCompat.getColor(mContext, R.color.gray));
//        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
//                .setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));

    }

    private boolean changeStockPosition(int fromPosition, int toPosition) {

        String stockList = SharedPreferencesUtil.getStrings(CowboyResponseDocument.STOCKS_INFO, null);

        boolean operationResult = Boolean.TRUE;
        if (TextUtils.isEmpty(stockList)) {

            operationResult = false;
        } else {

            String codeFrom = stocks.get(fromPosition).getStockCode();
            String codeTo  = stocks.get(toPosition).getStockCode();
            if (!TextUtils.isEmpty(codeFrom) && !TextUtils.isEmpty(codeTo)) {

                String ss = "******";
                stockList = stockList.replace(codeFrom, ss).replace(codeTo, codeFrom).replace(ss, codeTo);
                boolean result = SharedPreferencesUtil.putString(CowboyResponseDocument.STOCKS_INFO, stockList);
                if (!result)
                    operationResult = false;
            } else
                operationResult = false;

        }

        return operationResult;
    }

    private void deleteConcernStock(int position) {

        String code = stocks.get(position).getStockCode();
        if (TextUtils.isEmpty(code)) {

            Toast.makeText(mContext, mContext.getString(R.string.operation_fail), Toast.LENGTH_SHORT).show();
            return;
        }

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

                this.stocks.remove(position);
                notifyItemRemoved(position);
            }

        }
    }
}
