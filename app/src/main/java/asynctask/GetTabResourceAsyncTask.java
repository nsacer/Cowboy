package asynctask;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import exception.CowboyException;
import protocol.CowboyHomeProtocol;
import response.GetTabResourceResponse;
import utils.CowboyHandlerKey;
import utils.CowboyResponseDocument;


/**
 * Created by twx on 5/9/16.
 */
public class GetTabResourceAsyncTask extends BasicAsyncTask<Void, Void, Bundle> {

    public static final String TAG = "GetTabResourceAsyncTask";

    private Handler handler;

    private String tabId;

    private String action;

    private String lastResourceId;

    private Context context;


    @Override
    protected Bundle doInBackground(Void... params) {

        Bundle bundle = new Bundle();
        GetTabResourceResponse getTabResourceResponse = null;
        try {
            getTabResourceResponse = CowboyHomeProtocol.getInstance()
                    .getTabResource(tabId, action, lastResourceId, context);
        } catch (CowboyException e) {
            bundle = doException(e, "GetTabResourceAsyncTask");
        }
        bundle.putParcelable(
                CowboyResponseDocument.RESPONSE_INDEX_TITLE_ONE, getTabResourceResponse);

        if (getTabResourceResponse != null) {
            bundle.putString(CowboyResponseDocument.STATUS_INFO, getTabResourceResponse.getResponseStatus().getStatusInfo());
            bundle.putString(CowboyResponseDocument.STATUS, getTabResourceResponse.getResponseStatus().getStatus());
        } else {
            bundle.putString(CowboyResponseDocument.STATUS_INFO, CowboyResponseDocument.NETWORK_ERROR);
        }

        return bundle;
    }

    @Override
    protected void onPostExecute(Bundle result) {
        super.onPostExecute(result);
        if (!isCancelled()) {
            Message msg = handler.obtainMessage();
            if (action.equals(CowboyResponseDocument.LOAD_MORE)) {
                msg.what = CowboyHandlerKey.LOAD_MORE_INDEX_TITLE_ONE;
            } else {
                msg.what = CowboyHandlerKey.DOWNLOAD_INDEX_TITLE_ONE;
            }
            msg.setData(result);
            msg.sendToTarget();
        }

    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setTabId(String tabId) {
        this.tabId = tabId;
    }

    public void setAction(String action) {

        if (action.equals(CowboyResponseDocument.DOWNLOAD) ||
                action.equals(CowboyResponseDocument.LOAD_MORE))
            this.action = action;
        else
            Log.e("CowboyException", "the action is one of CowboyResponseDocument.DOWNLOAD & " +
                    "CowboyResponseDocument.LOAD_MORE");
    }

    public void setLastResourceId(String lastResourceId) {
        this.lastResourceId = lastResourceId;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
