package protocol;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import exception.CowboyException;
import response.PersonalStockResponse;
import response.StockSearchResponse;
import response.StocksInfoResponse;
import utils.CowboyHttpsClientUtil;
import utils.CowboySetting;
import utils.JsonUtil;
import waller.PersonalStockResponseWaller;
import waller.SearchStockResponseWaller;
import waller.StocksInfoResponseWaller;

/**
 * 首页请求页面 协议解析
 *
 * @author yongsheng
 */
public class CowboyStockProtocolImpl extends CowboyStockProtocol {

    private static CowboyStockProtocolImpl cowboyStockProtocol;

    public static CowboyStockProtocolImpl getInstance() {

        if (cowboyStockProtocol == null) {
            cowboyStockProtocol = new CowboyStockProtocolImpl();
        }
        return cowboyStockProtocol;
    }

    /**
     * 获取自选股列表
     */
    @Override
    public PersonalStockResponse getMyStocks() throws CowboyException {

        CowboyHttpsClientUtil cowboyHttpHandler = CowboyHttpsClientUtil.getInstance();
        Map<String, Object> requestParamsMap = new HashMap<>();

        Map<String, String> contentMap = CowboyHttpsClientUtil
                .getCowboyBasicRequestParams();
        contentMap.put("method", "getMyStocks");
        requestParamsMap.put("request", contentMap);

        String responseContent = cowboyHttpHandler.postURL(CowboySetting.SERVER_URL,
                JsonUtil.Object2Json(requestParamsMap));

        PersonalStockResponseWaller waller = JsonUtil.Json2Object(
                responseContent, PersonalStockResponseWaller.class);

        return waller == null ? null : waller.getResponse();

    }

    /**
     * 搜索股票结果
     */
    @Override
    public StockSearchResponse searchStock(String content) throws CowboyException {

        CowboyHttpsClientUtil cowboyHttpHandler = CowboyHttpsClientUtil.getInstance();
        String serverUrl = "http://searchstock.9666.cn/?version=" + CowboySetting.CLIENT_VERSION + "&content=";

        String json = "";
        try {
            json = cowboyHttpHandler.getURL(serverUrl + URLEncoder.encode(content.trim(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        SearchStockResponseWaller waller = JsonUtil.Json2Object(json, SearchStockResponseWaller.class);

        return waller == null ? null : waller.getResponse();
    }

    /**
     * 获取关注的股票的行情信息
     */
    @Override
    public StocksInfoResponse getStockQuotationBasicInfo(String stocks) throws CowboyException {

        CowboyHttpsClientUtil cowboyHttpHandler = CowboyHttpsClientUtil.getInstance();
        String json = "";
        try {
            String url = "http://hq.9666.cn/stock/simple?platform=android&version=" +
                    CowboySetting.CLIENT_VERSION + "&securityIDs=" + URLEncoder.encode(stocks, "UTF-8");
            json = cowboyHttpHandler.getURL(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StocksInfoResponseWaller waller = JsonUtil.Json2Object(json, StocksInfoResponseWaller.class);
        return waller == null ? null : waller.getResponse();
    }


}
