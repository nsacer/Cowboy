package model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * 股票Model
 */
public class Stock implements Parcelable {

    private static final String ZC = "103";
    private static final String TP = "2";
    private static final String YTS = "902";
    private static final String WSS = "104";
    private static final String WKP = "-2";
    private static final String JHJJ = "-1";

    private String stockCode;
    private String stockName;
    private String tradingStatus;
    private String currentPrice;
    private String pxChg;
    private String pxChgRatio;

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getTradingStatus() {
        return tradingStatus;
    }

    public void setTradingStatus(String tradingStatus) {

        if (TextUtils.isEmpty(tradingStatus))
            return;

        String ss = "未知";
        switch (tradingStatus) {

            case ZC:
                ss = "正常";
                break;
            case TP:
                ss = "停牌";
                break;
            case YTS:
                ss = "已退市";
                break;
            case WSS:
                ss = "未上市";
                break;
            case WKP:
                ss = "未开盘";
                break;
            case JHJJ:
                ss = "集合竞价";
                break;
        }
        this.tradingStatus = ss;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getPxChg() {
        return pxChg;
    }

    public void setPxChg(String pxChg) {
        this.pxChg = pxChg;
    }

    public String getPxChgRatio() {
        return pxChgRatio;
    }

    public void setPxChgRatio(String pxChgRatio) {
        this.pxChgRatio = pxChgRatio;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    public static Parcelable.Creator<Stock> getCreator() {
        return CREATOR;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(stockCode);
        dest.writeString(stockName);
        dest.writeString(tradingStatus);
        dest.writeString(currentPrice);
        dest.writeString(pxChg);
        dest.writeString(pxChgRatio);

    }

    public static final Parcelable.Creator<Stock> CREATOR = new Creator<Stock>() {

        public Stock createFromParcel(Parcel source) {

            Stock model = new Stock();
            model.setStockCode(source.readString());
            model.setStockName(source.readString());
            model.setTradingStatus(source.readString());
            model.setCurrentPrice(source.readString());
            model.setPxChg(source.readString());
            model.setPxChgRatio(source.readString());

            return model;
        }

        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };


}
