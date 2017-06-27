package response;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import model.ResponseStatus;
import model.Stock;

/**
 * @author nys
 * @case 009_02
 * 我收藏的股票列表
 */
public class PersonalStockResponse implements Parcelable {


    private ResponseStatus responseStatus;

    /**
     * 返回我收藏的股票列表
     */
    private ArrayList<Stock> myStockList;

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public ArrayList<Stock> getMyStockList() {
        return myStockList;
    }

    public void setMyStockList(ArrayList<Stock> myStockList) {
        this.myStockList = myStockList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();

        bundle.putParcelable("responseStatus", responseStatus);
        bundle.putParcelableArrayList("myStockList", myStockList);

        dest.writeBundle(bundle);
    }

    public static final Creator<PersonalStockResponse> CREATOR = new Creator<PersonalStockResponse>() {

        @Override
        public PersonalStockResponse createFromParcel(Parcel resource) {
            PersonalStockResponse res = new PersonalStockResponse();
            Bundle bundle = resource.readBundle(getClass().getClassLoader());

            if (bundle != null) {
                res.setResponseStatus((ResponseStatus) bundle.getParcelable("responseStatus"));
                ArrayList<Stock> sList = bundle.getParcelableArrayList("myStockList");

                res.setMyStockList(sList);
            }
            return res;
        }

        @Override
        public PersonalStockResponse[] newArray(int size) {
            return new PersonalStockResponse[size];
        }

    };

}
