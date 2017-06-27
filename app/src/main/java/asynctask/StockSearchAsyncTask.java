package asynctask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import exception.CowboyException;
import protocol.CowboyStockProtocol;
import response.StockSearchResponse;
import utils.CowboyHandlerKey;
import utils.CowboyResponseDocument;

public class StockSearchAsyncTask extends BasicAsyncTask<Void, Void, Bundle> {

    private CowboyStockProtocol cowboyStockProtocol = CowboyStockProtocol.getInstance();

    private Handler handler;

    private String stock;

    public StockSearchAsyncTask(Handler handler, String stock) {

        if (TextUtils.isEmpty(stock))
            return;
        this.handler = handler;
        this.stock = stock;
    }

    @Override
    protected Bundle doInBackground(Void... params) {

        Bundle bundle = new Bundle();

        try {
            StockSearchResponse response = cowboyStockProtocol.searchStock(stock);
            bundle.putParcelable(CowboyResponseDocument.SEARCH_STOCK_RESULT_LIST, response);

            if (response != null) {
                bundle.putString(CowboyResponseDocument.STATUS_INFO, response.getResponseStatus().getStatusInfo());
                bundle.putString(CowboyResponseDocument.STATUS, response.getResponseStatus().getStatus());
            } else {
                bundle.putString(CowboyResponseDocument.STATUS_INFO, CowboyResponseDocument.NETWORK_ERROR);
            }
        } catch (CowboyException e) {

        }
        return bundle;
    }

    @Override
    protected void onPostExecute(Bundle result) {
        super.onPostExecute(result);
        if (!isCancelled()) {
            Message msg = handler.obtainMessage();
            msg.what = CowboyHandlerKey.SEARCH_STOCK_RESULT_LIST;
            msg.setData(result);
            msg.sendToTarget();
        }
    }

}
