package asynctask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import exception.CowboyException;
import protocol.CowboyStockProtocol;
import response.StocksInfoResponse;
import utils.CowboyHandlerKey;
import utils.CowboyResponseDocument;

public class StocksInfoAsyncTask extends BasicAsyncTask<Void, Void, Bundle> {

    private CowboyStockProtocol cowboyStockProtocol = CowboyStockProtocol.getInstance();

    private Handler handler;

    private String stocks;

    public StocksInfoAsyncTask(Handler handler, String stocks) {

        if (TextUtils.isEmpty(stocks))
            return;
        this.handler = handler;
        this.stocks = stocks;
    }

    @Override
    protected Bundle doInBackground(Void... params) {

        Bundle bundle = new Bundle();

        try {
            StocksInfoResponse response = cowboyStockProtocol.getStockQuotationBasicInfo(stocks);
            bundle.putParcelable(CowboyResponseDocument.STOCKS_INFO, response);

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
            msg.what = CowboyHandlerKey.STOCKS_INFO;
            msg.setData(result);
            msg.sendToTarget();
        }
    }

}
