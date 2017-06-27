package model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 广告图片
 *
 * @author yongsheng
 */
public class AdModel implements Parcelable {

    /**
     * model id
     */
    private String id;

    /**
     * 類型
     * 1 : 直播室
     * 2 ：WebView
     */
    private String type;

    /**
     * 圖片地址
     */
    private String img;

    /**
     * 直播室id
     */
    private String roomId;

    /**
     * 顯示在第幾個位置
     */
    private String position;

    /**
     * WebView的url
     */
    private String url;

    /**
     * WebView分享標題
     */
    private String title;

    /**
     * 0 : 不分享
     * 1 : 可以分享
     */
    private String isShare;

    /**
     * 分享摘要
     */
    private String shareDescrip;

    public String getType() {
        return type;
    }

    public String getShareDescrip() {
        return shareDescrip;
    }

    public void setShareDescrip(String shareDescrip) {
        this.shareDescrip = shareDescrip;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {

        return 0;
    }


    public static Creator<AdModel> getCreator() {
        return CREATOR;
    }


    public String getIsShare() {
        return isShare;
    }

    public void setIsShare(String isShare) {
        this.isShare = isShare;
    }

    public Boolean shareAble() {

        if ("0".equals(isShare)) {
            return false;
        }
        return true;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(type);
        dest.writeString(id);
        dest.writeString(img);
        dest.writeString(roomId);
        dest.writeString(position);
        dest.writeString(url);
        dest.writeString(title);
        dest.writeString(isShare);
        dest.writeString(shareDescrip);
    }

    public static final Creator<AdModel> CREATOR = new Creator<AdModel>() {

        public AdModel createFromParcel(Parcel source) {
            AdModel model = new AdModel();

            model.setType(source.readString());
            model.setId(source.readString());
            model.setImg(source.readString());

            model.setRoomId(source.readString());
            model.setPosition(source.readString());

            model.setUrl(source.readString());
            model.setTitle(source.readString());
            model.setIsShare(source.readString());
            model.setShareDescrip(source.readString());
            return model;
        }

        public AdModel[] newArray(int size) {
            return new AdModel[size];
        }
    };

}
