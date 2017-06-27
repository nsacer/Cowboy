package waller;

import android.os.Parcel;
import android.os.Parcelable;

import response.PersonalStockResponse;

public class PersonalStockResponseWaller implements Parcelable {


    private PersonalStockResponse response;

    public PersonalStockResponse getResponse() {
        return response;
    }

    public void setResponse(PersonalStockResponse response) {
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

    public static final Creator<PersonalStockResponseWaller> CREATOR = new Creator<PersonalStockResponseWaller>() {

        @Override
        public PersonalStockResponseWaller createFromParcel(Parcel resource) {
            PersonalStockResponseWaller wap = new PersonalStockResponseWaller();
            wap.setResponse((PersonalStockResponse) resource.readParcelable(getClass().getClassLoader()));
            return wap;
        }

        @Override
        public PersonalStockResponseWaller[] newArray(int size) {
            return new PersonalStockResponseWaller[size];
        }

    };

}
