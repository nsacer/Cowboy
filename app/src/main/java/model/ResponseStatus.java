package model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author yongsheng
 *
 */
public class ResponseStatus implements Parcelable{
	
	
	private String status; 		//	<!-- 反馈信息返回状态 0表明成功  1表示服务器异常  2服务器已经不维护此版本，强制升级(见本协议底部)-->
	private String statusInfo;  //  <!-- 服务器最新APP下载地址 -->
	
	private String downloadUrl; //	<!-- 服务器最新APP下载地址 -->
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusInfo() {
		return statusInfo;
	}
	public void setStatusInfo(String statusInfo) {
		this.statusInfo = statusInfo;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	

	public static Creator<ResponseStatus> getCreator() {
		return CREATOR;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(status);
		dest.writeString(statusInfo);
		dest.writeString(downloadUrl);

	}

	public static final Creator<ResponseStatus> CREATOR = new Creator<ResponseStatus>() {

		public ResponseStatus createFromParcel(Parcel source) {
			ResponseStatus model = new ResponseStatus();
			model.setStatus(source.readString());
			model.setStatusInfo(source.readString());
			model.setDownloadUrl(source.readString());
			return model;
		}

		public ResponseStatus[] newArray(int size) {
			return new ResponseStatus[size];
		}
	};
	
	
	
}
