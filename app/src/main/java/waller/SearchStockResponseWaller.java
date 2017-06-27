package waller;

import android.os.Parcel;
import android.os.Parcelable;

import response.StockSearchResponse;

/**
 * Created by zpf on 2017/4/21.
 */

public class SearchStockResponseWaller implements Parcelable{

    private StockSearchResponse response;

    public StockSearchResponse getResponse() {
        return response;
    }

    public void setResponse(StockSearchResponse response) {
        this.response = response;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(response, flags);
    }

    public static final Parcelable.Creator<SearchStockResponseWaller> CREATOR = new Parcelable.Creator<SearchStockResponseWaller>() {

        @Override
        public SearchStockResponseWaller createFromParcel(Parcel resource) {
            SearchStockResponseWaller wap = new SearchStockResponseWaller();
            wap.setResponse((StockSearchResponse) resource.readParcelable(getClass().getClassLoader()));
            return wap;
        }

        @Override
        public SearchStockResponseWaller[] newArray(int size) {
            return new SearchStockResponseWaller[size];
        }
    };
}
