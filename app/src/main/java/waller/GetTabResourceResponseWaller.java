package waller;

import android.os.Parcel;
import android.os.Parcelable;

import response.GetTabResourceResponse;


/**
 * Created by twx on 5/9/16.
 */
public class GetTabResourceResponseWaller implements Parcelable {

    private GetTabResourceResponse response ;

    public GetTabResourceResponse getResponse() {
        return response;
    }

    public void setResponse(GetTabResourceResponse response) {
        this.response = response;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeParcelable(response, flag);
    }

    public static final Creator<GetTabResourceResponseWaller> CREATOR = new Creator<GetTabResourceResponseWaller>() {

        @Override
        public GetTabResourceResponseWaller createFromParcel(Parcel resource) {
            GetTabResourceResponseWaller waller = new GetTabResourceResponseWaller();

            waller.setResponse((GetTabResourceResponse) resource.readParcelable(getClass().getClassLoader()));

            return waller;
        }

        @Override
        public GetTabResourceResponseWaller[] newArray(int size) {
            return new GetTabResourceResponseWaller[size];
        }

    };
}
