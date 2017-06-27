package response;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import model.ResponseStatus;
import model.Stock;

/**
 * 搜索股票结果列表 最多联想5条数据
 * @author nys
 *
 */
public class StockSearchResponse implements Parcelable {

	
	private ArrayList<Stock> stockList;
	
	private ResponseStatus responseStatus;

	public ArrayList<Stock> getStockList() {
		return stockList;
	}

	public void setStockList(ArrayList<Stock> stockList) {
		this.stockList = stockList;
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
	public void writeToParcel(Parcel dest, int flags) {

		Bundle bundle = new Bundle();
		
		bundle.putParcelableArrayList("stockList", stockList);
		
		bundle.putParcelable("responseStatus", responseStatus);
		
		dest.writeBundle(bundle);
	}
	

	public static final Creator<StockSearchResponse> CREATOR = new Creator<StockSearchResponse>() {

		@Override
		public StockSearchResponse createFromParcel(Parcel resource) {
			StockSearchResponse res = new StockSearchResponse();
			Bundle bundle = resource.readBundle(getClass().getClassLoader());
			
			if (bundle != null) {
				ArrayList<Stock> stocks = bundle.getParcelableArrayList("stockList");
				res.setStockList(stocks);
				
				res.setResponseStatus((ResponseStatus) bundle.getParcelable("responseStatus"));
			}
			return res;
		}

		@Override
		public StockSearchResponse[] newArray(int size) {
			return new StockSearchResponse[size];
		}
		
	};
}
