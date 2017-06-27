package response;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import model.ResponseStatus;
import model.Stock;

/**
 * 关注的股票获取信息的response
 *
 * @author nys
 */
public class StocksInfoResponse implements Parcelable {

    private String allowed;

    private ArrayList<Stock> infos;

    private ResponseStatus responseStatus;

    public String getAllowed() {
        return allowed;
    }

    public void setAllowed(String allowed) {
        this.allowed = allowed;
    }

    public ArrayList<Stock> getInfos() {
        return infos;
    }

    public void setInfos(ArrayList<Stock> infos) {
        this.infos = infos;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        Bundle bundle = new Bundle();

        bundle.putString("allowed", allowed);
        bundle.putParcelable("responseStatus", responseStatus);
        bundle.putParcelableArrayList("infos", infos);

        parcel.writeBundle(bundle);
    }

    public static final Creator<StocksInfoResponse> CREATOR = new Creator<StocksInfoResponse>() {
        @Override
        public StocksInfoResponse createFromParcel(Parcel in) {

            Bundle bundle = in.readBundle(getClass().getClassLoader());

            StocksInfoResponse response = new StocksInfoResponse();

            if (bundle != null) {

                response.setAllowed(bundle.getString("allowed"));
                ArrayList<Stock> stocks = bundle.getParcelableArrayList("infos");
                response.setInfos(stocks);
                response.setResponseStatus((ResponseStatus) bundle.getParcelable("responseStatus"));
            }

            return response;
        }

        @Override
        public StocksInfoResponse[] newArray(int size) {
            return new StocksInfoResponse[size];
        }
    };

}
