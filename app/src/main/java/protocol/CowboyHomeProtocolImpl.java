package protocol;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import exception.CowboyException;
import response.GetTabResourceResponse;
import utils.CowboyHttpsClientUtil;
import utils.CowboySetting;
import utils.JsonUtil;
import waller.GetTabResourceResponseWaller;


/**
 * 首页请求页面 协议解析
 * 
 * @author yongsheng
 * 
 */
public class CowboyHomeProtocolImpl extends CowboyHomeProtocol {

	private static CowboyHomeProtocolImpl cowboyHomeProtocol;

	public static CowboyHomeProtocolImpl getInstance() {

		if (cowboyHomeProtocol == null) {
			cowboyHomeProtocol = new CowboyHomeProtocolImpl();
		}
		return cowboyHomeProtocol;
	}

	@Override
	public GetTabResourceResponse getTabResource(String tabId, String action, String lastResourceId,
												 Context context) throws CowboyException {
		CowboyHttpsClientUtil cowboyHttpHandler = CowboyHttpsClientUtil
				.getInstance();

		Map<String, Object> requestParamsMap = new HashMap<>();

		Map<String, String> contentMap = CowboyHttpsClientUtil
				.getCowboyBasicRequestParams();
		contentMap.put("method", "getTabResource");

		contentMap.put("tabId", tabId);
		contentMap.put("action", action);
		contentMap.put("lastResourceId", lastResourceId);
		requestParamsMap.put("request", contentMap);

		cowboyHttpHandler.setMethodName("getTabResource");

		String responseContent = cowboyHttpHandler.postURL(
				CowboySetting.SERVER_URL,
				JsonUtil.Object2Json(requestParamsMap));

		GetTabResourceResponseWaller waller = JsonUtil.Json2Object(
				responseContent, GetTabResourceResponseWaller.class);

		return waller == null ? null : waller.getResponse();

	}

}
