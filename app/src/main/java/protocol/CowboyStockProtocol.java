package protocol;

import exception.CowboyException;
import response.PersonalStockResponse;
import response.StockSearchResponse;
import response.StocksInfoResponse;

/**
 * 首页协议接口
 */
public abstract class CowboyStockProtocol {


    public static CowboyStockProtocol getInstance() {
        return CowboyStockProtocolImpl.getInstance();
    }

    /**
     * 左上角"自选股"-列表
     */
    abstract public PersonalStockResponse getMyStocks() throws CowboyException;

    /**
     * 关联搜索 股票
     */
    abstract public StockSearchResponse searchStock(String content) throws CowboyException;

    /**
     * 获取关注的股票的行情
     */
    abstract public StocksInfoResponse getStockQuotationBasicInfo(String stocks) throws CowboyException;

}
