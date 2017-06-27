package model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by twx on 5/9/16.
 */
public class FirstResourceModel implements Parcelable {

    private String type;			//	<!-- type 1：点击进入直播室   2：查看视频课程  3：博客   4:观点  5：问股  6:(四种类型1、直播室  2、视频  3、博客  4、专题)-->
    private String title;			//  <!-- 标题 -->
    private String resourceId;      //  <!--首页资源id， 用来统计-->
    private String contentJson;


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
    public String getResourceId() {
        return resourceId;
    }
    public String getContentJson() {
        return contentJson;
    }

    public void setContentJson(String contentJson) {
        this.contentJson = contentJson;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        Bundle bundle = new Bundle();

        bundle.putString("type", type);
        bundle.putString("title", title);
        bundle.putString("resourceId", resourceId);
        bundle.putString("contentJson", contentJson);

        dest.writeBundle(bundle);
    }



    public Creator<FirstResourceModel> CREATOR = new Creator<FirstResourceModel>() {

        @Override
        public FirstResourceModel createFromParcel(Parcel resource) {
            FirstResourceModel model = new FirstResourceModel();
            Bundle bundle = resource.readBundle();

            if (bundle != null) {

                model.setType(bundle.getString("type"));
                model.setTitle(bundle.getString("title"));
                model.setResourceId(bundle.getString("resourceId"));
                model.setContentJson(bundle.getString("contentJson"));

            }

            return model;
        }

        @Override
        public FirstResourceModel[] newArray(int size) {

            return new FirstResourceModel[size];
        }

    };

}
