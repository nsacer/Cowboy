package response;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import model.AdModel;
import model.FirstResourceModel;
import model.ResponseStatus;
import model.Stock;

/**
 * Created by twx on 5/9/16.
 */
public class GetTabResourceResponse implements Parcelable {

    private ResponseStatus responseStatus;  // <!-- 网络访问信息-->

    private String serverTime;      //  <!-- 系统时间 -->

    private String lastResourceId;        // <!--  本次请求获得的最后一个图文专题ID，下次上拉刷新请求的时候上传  -->

    private String isAdShow;        //  <!-- 广告图是否显示 1:代表展现，0代表不展现  -->

    private ArrayList<AdModel> adList;            //<!-- 广告图片列表-->

    private String isStockListShow;           //  <!--指数股票是否显示-->

    private ArrayList<Stock> stockList;            //<!-- 首页指数股票列表-->

    private List<FirstResourceModel> resourceList;   //<!-- 首页数据-->

    public List<FirstResourceModel> getResourceList() {
        return resourceList;
    }

    public String getLastResourceId() {
        return lastResourceId;
    }

    public void setLastResourceId(String lastResourceId) {
        this.lastResourceId = lastResourceId;
    }

    public String getIsAdShow() {
        return isAdShow;
    }

    public void setIsAdShow(String isAdShow) {
        this.isAdShow = isAdShow;
    }

    public void setResourceList(ArrayList<FirstResourceModel> resourceList) {
        this.resourceList = resourceList;
    }


    public String getIsStockListShow() {
        return isStockListShow;
    }

    public void setIsStockListShow(String isStockListShow) {
        this.isStockListShow = isStockListShow;
    }

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


    public ArrayList<AdModel> getAdList() {
        return adList;
    }

    public void setAdList(ArrayList<AdModel> adList) {
        this.adList = adList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        Bundle bundle = new Bundle();

        bundle.putParcelable("responseStatus", responseStatus);
        bundle.putString("serverTime", serverTime);
        bundle.putString("lastResourceId", lastResourceId);
        bundle.putString("isAdShow", isAdShow);
        bundle.putString("isStockListShow", isStockListShow);
        bundle.putParcelableArrayList("adList", adList);
        bundle.putParcelableArrayList("stockList", (ArrayList<? extends Parcelable>) stockList);
        bundle.putParcelableArrayList("resourceList", (ArrayList<? extends Parcelable>) resourceList);

        dest.writeBundle(bundle);
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public static final Creator<GetTabResourceResponse> CREATOR = new Creator<GetTabResourceResponse>() {

        @Override
        public GetTabResourceResponse createFromParcel(Parcel resource) {

            GetTabResourceResponse res = new GetTabResourceResponse();
            Bundle bundle = resource.readBundle(getClass().getClassLoader());
            res.setResponseStatus((ResponseStatus) bundle.getParcelable("responseStatus"));

            res.setServerTime(bundle.getString("serverTime"));
            res.setLastResourceId(bundle.getString("lastResourceId"));
            res.setIsAdShow(bundle.getString("isAdShow"));
            res.setIsStockListShow(bundle.getString("isStockListShow"));

            ArrayList<AdModel> ads = bundle.getParcelableArrayList("adList");
            res.setAdList(ads);

            ArrayList<Stock> stockItems = bundle.getParcelableArrayList("stockList");
            res.setStockList(stockItems);

            ArrayList<FirstResourceModel> indexItems = bundle.getParcelableArrayList("resourceList");
            res.setResourceList(indexItems);

            return res;
        }

        @Override
        public GetTabResourceResponse[] newArray(int size) {
            return new GetTabResourceResponse[size];
        }

    };
}
