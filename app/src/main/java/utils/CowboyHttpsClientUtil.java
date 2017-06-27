package utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import exception.CowboyException;


public class CowboyHttpsClientUtil {

	private volatile static CowboyHttpsClientUtil httpClientSingleton;
	// 请求超时时间
	private static final int CONNECTION_TIME_OUT = 20000;
	private static HttpClient httpClient = null;
	private String methodName;

	private CowboyHttpsClientUtil() {
	}

	public static CowboyHttpsClientUtil getInstance() {
		if (httpClientSingleton == null) {
			httpClientSingleton = new CowboyHttpsClientUtil();
		}
		return httpClientSingleton;
	}
	
	
	public static HttpClient getHttpsClient(){

		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				CONNECTION_TIME_OUT);
		HttpConnectionParams.setSoTimeout(httpParameters,
				CONNECTION_TIME_OUT);
		httpClient = initHttpClient(httpParameters);
		return httpClient;
	}
	
	public String getURL(String url) throws CowboyException {
		
		HttpGet httpRequest = null;
		ByteArrayOutputStream contentOutputStream = null;
		try {
			httpRequest = new HttpGet(url);
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					CONNECTION_TIME_OUT);
			HttpConnectionParams.setSoTimeout(httpParameters,
					CONNECTION_TIME_OUT);

			httpClient = initHttpClient(httpParameters);
			httpRequest.setHeader("websiteid", CowboySetting.UUID);
			httpRequest.setHeader("deviceid", CowboySetting.DEVICE_ID); 		// 手机的UUID，每个设备都唯一.
			httpRequest.setHeader("username", CowboySetting.USER_NAME); 		// 用户名称
			httpRequest.setHeader("usercert", CowboySetting.VALID_ID);
			httpRequest.setHeader("channel", CowboySetting.CHANNEL);
			httpRequest.setHeader("version", CowboySetting.CLIENT_VERSION);
			httpRequest.setHeader("sdk", CowboySetting.SDK);
			httpRequest.setHeader("brand", CowboySetting.PHONE_BRAND);
			httpRequest.setHeader("methodName", methodName);
			httpRequest.setHeader("platform", "android");
			
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			StatusLine status = httpResponse.getStatusLine();
			
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				throw new CowboyException(
						CowboyException.INVALID_RESPONSE_CODE, "无效的响应："
								+ status.toString());
			}

			HttpEntity httpEntity = httpResponse.getEntity();
			String result = EntityUtils.toString(httpEntity, "UTF-8");
			
			return result;

		} catch (UnsupportedEncodingException e) {
			throw new CowboyException(CowboyException.NETWORK_ERROR,
					"不支持的编码格式", e);
		} catch (ClientProtocolException e) {
			throw new CowboyException(CowboyException.NETWORK_ERROR, "客户端协议异常",
					e);
		} catch (ConnectTimeoutException e) {
			throw new CowboyException(CowboyException.CONNECT_TIMEOUT, "连接超时",
					e);
		} catch (IOException e) {
			throw new CowboyException(CowboyException.NETWORK_ERROR,
					"联网错误,请检查网络状态", e);

		}
		// 最后要关闭流,关闭连接
		finally {
			try {
				// 先判断如果对象不为null
				if (contentOutputStream != null)
					contentOutputStream.close();
			} catch (IOException e) {
				throw new CowboyException(CowboyException.NETWORK_ERROR,
						"联网错误,请检查网络状态", e);
			}
			
			if (httpRequest != null) {
				httpRequest.abort();
			}
			
			if(httpClient!=null){
				httpClient.getConnectionManager().shutdown();
				httpClient = null;
			}
		}
	}
	
	
	public String postURL(String url, String json) throws CowboyException {
		HttpPost httpRequest = null;
		ByteArrayOutputStream contentOutputStream = null;
		try {
			httpRequest = new HttpPost(url);
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					CONNECTION_TIME_OUT);
			HttpConnectionParams.setSoTimeout(httpParameters,
					CONNECTION_TIME_OUT);

			httpClient = initHttpClient(httpParameters);
			httpRequest.setHeader("websiteid", CowboySetting.UUID);
			httpRequest.setHeader("deviceid", CowboySetting.DEVICE_ID); 		// 手机的UUID，每个设备都唯一.
			httpRequest.setHeader("username", CowboySetting.USER_NAME); 		// 用户名称
			httpRequest.setHeader("usercert", CowboySetting.VALID_ID);
			httpRequest.setHeader("channel", CowboySetting.CHANNEL);
			httpRequest.setHeader("version", CowboySetting.CLIENT_VERSION);
			httpRequest.setHeader("brand", CowboySetting.PHONE_BRAND);
			httpRequest.setHeader("sdk", CowboySetting.SDK);
			httpRequest.setHeader("methodName", methodName);
			httpRequest.setHeader("platform", "android");

			httpRequest.setEntity(new StringEntity(json));
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			StatusLine status = httpResponse.getStatusLine();
			
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				throw new CowboyException(
						CowboyException.INVALID_RESPONSE_CODE, "无效的响应："
								+ status.toString());
			}

			HttpEntity httpEntity = httpResponse.getEntity();
			String result = EntityUtils.toString(httpEntity, "UTF-8");
			
			return result;

		} catch (UnsupportedEncodingException e) {
			throw new CowboyException(CowboyException.NETWORK_ERROR,
					"不支持的编码格式", e);
		} catch (ClientProtocolException e) {
			throw new CowboyException(CowboyException.NETWORK_ERROR, "客户端协议异常",
					e);
		} catch (ConnectTimeoutException e) {
			throw new CowboyException(CowboyException.CONNECT_TIMEOUT, "连接超时",
					e);
		} catch (IOException e) {
			throw new CowboyException(CowboyException.NETWORK_ERROR,
					"联网错误,请检查网络状态", e);

		}
		// 最后要关闭流,关闭连接
		finally {
			try {
				// 先判断如果对象不为null
				if (contentOutputStream != null)
					contentOutputStream.close();
			} catch (IOException e) {
				throw new CowboyException(CowboyException.NETWORK_ERROR,
						"联网错误,请检查网络状态", e);
			}
			
			if (httpRequest != null) {
				httpRequest.abort();
			}
			
			if(httpClient!=null){
				httpClient.getConnectionManager().shutdown();
				httpClient = null;
			}
		}
	}

	public String postURLForFirst(String url, String json, String lng_lat) throws CowboyException {
		HttpPost httpRequest = null;
		ByteArrayOutputStream contentOutputStream = null;
		try {
			httpRequest = new HttpPost(url);
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					CONNECTION_TIME_OUT);
			HttpConnectionParams.setSoTimeout(httpParameters,
					CONNECTION_TIME_OUT);

			httpClient = initHttpClient(httpParameters);
			httpRequest.setHeader("websiteid", CowboySetting.UUID);
			httpRequest.setHeader("deviceid", CowboySetting.DEVICE_ID); 		// 手机的UUID，每个设备都唯一.
			httpRequest.setHeader("devicename", CowboySetting.MODEL);
			httpRequest.setHeader("username", CowboySetting.USER_NAME); 		// 用户名称
			httpRequest.setHeader("usercert", CowboySetting.VALID_ID);
			httpRequest.setHeader("lng_lat", lng_lat);
			httpRequest.setHeader("channel", CowboySetting.CHANNEL);
			httpRequest.setHeader("version", CowboySetting.CLIENT_VERSION);
			httpRequest.setHeader("sdk", CowboySetting.SDK);
			httpRequest.setHeader("brand", CowboySetting.PHONE_BRAND);
			httpRequest.setHeader("methodName", methodName);
			httpRequest.setHeader("platform", "android");
			
			httpRequest.setEntity(new StringEntity(json));
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			StatusLine status = httpResponse.getStatusLine();
			
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				throw new CowboyException(
						CowboyException.INVALID_RESPONSE_CODE, "无效的响应："
								+ status.toString());
			}

			HttpEntity httpEntity = httpResponse.getEntity();
			String result = EntityUtils.toString(httpEntity, "UTF-8");
			
			return result;

		} catch (UnsupportedEncodingException e) {
			throw new CowboyException(CowboyException.NETWORK_ERROR,
					"不支持的编码格式", e);
		} catch (ClientProtocolException e) {
			throw new CowboyException(CowboyException.NETWORK_ERROR, "客户端协议异常",
					e);
		} catch (ConnectTimeoutException e) {
			throw new CowboyException(CowboyException.CONNECT_TIMEOUT, "连接超时",
					e);
		} catch (IOException e) {
			throw new CowboyException(CowboyException.NETWORK_ERROR,
					"联网错误,请检查网络状态", e);

		}
		// 最后要关闭流,关闭连接
		finally {
			try {
				// 先判断如果对象不为null
				if (contentOutputStream != null)
					contentOutputStream.close();
			} catch (IOException e) {
				throw new CowboyException(CowboyException.NETWORK_ERROR,
						"联网错误,请检查网络状态", e);
			}
			
			if (httpRequest != null) {
				httpRequest.abort();
			}
			
			if(httpClient!=null){
				httpClient.getConnectionManager().shutdown();
				httpClient = null;
			}
		}
	}

	
	// 基本请求的信息
	public static Map<String, String> getCowboyBasicRequestParams() {
		
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("version",CowboySetting.CLIENT_VERSION);		// 客户端版本号
		params.put("imei", CowboySetting.IMEI); 				// 客户端手机设备编码
		params.put("model", CowboySetting.MODEL); 				// 客户端手机型号
		params.put("sdk", CowboySetting.SDK); 					// 客户端手机系统版本
		params.put("channel", CowboySetting.CHANNEL); 			// 安装包来自的渠道
		params.put("validId", CowboySetting.VALID_ID); 			// 用户身份标识,未登陆，没有值.
		params.put("uuid", CowboySetting.UUID); 			    // 手机的UUID，每个设备都唯一.
		params.put("brand", CowboySetting.PHONE_BRAND); 		// 手机品牌.同一品牌一致
		params.put("platform", "android");
		return params;
	}
	
	
	

	    /**
	     * 初始化HttpClient对象
	     * @param params
	     * @return
	     */
	    public static synchronized HttpClient initHttpClient(HttpParams params) {
	        if(httpClient == null){
	            try {
	                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	                trustStore.load(null, null);
	                 
	                SSLSocketFactory sf = new SSLSocketFactoryImp(trustStore);
	                //允许所有主机的验证
	                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	                 
	                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
	                // 设置http和https支持
	                SchemeRegistry registry = new SchemeRegistry();
	                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	                registry.register(new Scheme("https", sf, 443));
	                 
	                ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
	                 
	                return new DefaultHttpClient(ccm, params);
	            } catch (Exception e) {
	                e.printStackTrace();
	                return new DefaultHttpClient(params);
	            }
	        }
	        return httpClient;
	    }
	 
		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}

	public static class SSLSocketFactoryImp extends SSLSocketFactory {
	        final SSLContext sslContext = SSLContext.getInstance("TLS");
	 
	        public SSLSocketFactoryImp(KeyStore truststore)
	                throws NoSuchAlgorithmException, KeyManagementException,
	                KeyStoreException, UnrecoverableKeyException {
	            super(truststore);
	 
	            TrustManager tm = new X509TrustManager() {
	                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                    return null;
	                }
	 
	                @Override
	                public void checkClientTrusted(
	                        java.security.cert.X509Certificate[] chain,
	                        String authType)
	                        throws java.security.cert.CertificateException {
	                }
	 
	                @Override
	                public void checkServerTrusted(
	                        java.security.cert.X509Certificate[] chain,
	                        String authType)
	                        throws java.security.cert.CertificateException {
	                }
	            };
	            sslContext.init(null, new TrustManager[] { tm }, null);
	        }
	 
	        @Override
	        public Socket createSocket(Socket socket, String host, int port,
	                boolean autoClose) throws IOException, UnknownHostException {
	            return sslContext.getSocketFactory().createSocket(socket, host,
	                    port, autoClose);
	        }
	 
	        @Override
	        public Socket createSocket() throws IOException {
	            return sslContext.getSocketFactory().createSocket();
	        }
	    }
	   
	   
	   
	
}
