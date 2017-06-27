package model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zpf on 2016/5/12.
 */
public class IndexHotPointModel implements Parcelable {

    private String title;			//  <!-- 文章标题 -->
    private String picUrl;				// <!-- 图片地址-->
    private String roomId;            // <!-- 直播室id -->
    private String articleId;            // 文章的id
    private String articleUrl;          // 文章的url
    private String userName;           // <!-- 文章作者 -->
    private String nickName;           // <!-- 对应文章作者的昵称 -->
    private String userPic;           // <!-- 对应文章作者的头像 -->
    private String readNum;           // <!-- 阅读数 -->

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getReadNum() {
        return readNum;
    }

    public void setReadNum(String readNum) {
        this.readNum = readNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flag) {
        Bundle bundle = new Bundle();

        bundle.putString("title", title);
        bundle.putString("picUrl", picUrl);
        bundle.putString("roomId", roomId);
        bundle.putString("articleId", articleId);
        bundle.putString("articleUrl", articleUrl);
        bundle.putString("userName", userName);
        bundle.putString("nickName", nickName);
        bundle.putString("userPic", userPic);
        bundle.putString("readNum", readNum);

        dest.writeBundle(bundle);
    }


    public static final Creator<IndexHotPointModel> CREATOR = new Creator<IndexHotPointModel>() {

        @Override
        public IndexHotPointModel createFromParcel(Parcel resource) {
            IndexHotPointModel item = new IndexHotPointModel();

            Bundle bundle = resource.readBundle();

            if (bundle != null) {

                item.setTitle(bundle.getString("title"));
                item.setPicUrl(bundle.getString("picUrl"));
                item.setRoomId(bundle.getString("roomId"));
                item.setArticleId(bundle.getString("articleId"));

                item.setArticleUrl(bundle.getString("articleUrl"));
                item.setUserName(bundle.getString("userName"));
                item.setNickName(bundle.getString("nickName"));
                item.setUserPic(bundle.getString("userPic"));
                item.setReadNum(bundle.getString("readNum"));
            }

            return item;
        }

        @Override
        public IndexHotPointModel[] newArray(int size) {

            return new IndexHotPointModel[size];
        }


    };
}
