package waller;

import android.os.Parcel;
import android.os.Parcelable;

import response.StockSearchResponse;
import response.StocksInfoResponse;

/**
 * Created by zpf on 2017/4/21.
 */

public class StocksInfoResponseWaller implements Parcelable {

    private StocksInfoResponse response;

    public StocksInfoResponse getResponse() {
        return response;
    }

    public void setResponse(StocksInfoResponse response) {
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

    public static final Creator<StocksInfoResponseWaller> CREATOR = new Creator<StocksInfoResponseWaller>() {

        @Override
        public StocksInfoResponseWaller createFromParcel(Parcel resource) {
            StocksInfoResponseWaller wap = new StocksInfoResponseWaller();
            wap.setResponse((StocksInfoResponse) resource.readParcelable(getClass().getClassLoader()));
            return wap;
        }

        @Override
        public StocksInfoResponseWaller[] newArray(int size) {
            return new StocksInfoResponseWaller[size];
        }
    };
}
