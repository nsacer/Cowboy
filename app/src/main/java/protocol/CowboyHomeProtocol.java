package protocol;

import android.content.Context;

import exception.CowboyException;
import response.GetTabResourceResponse;


/**
 * 首页协议接口
 *
 */
public abstract class CowboyHomeProtocol {
	
	
	public static CowboyHomeProtocol getInstance(){
		return CowboyHomeProtocolImpl.getInstance();
	}

	/**
	 * <!-- 首页数据 请求-->
	 * @param tabId 需要获取的tabid，必须输入
	 * @param action 用户列表操作，empty:无数据的时候传空， up:上拉刷新，down:下拉刷新
	 * @param lastResourceId 上次请求返回的最后一个Resource的ID，第一次的时候传0
	 * @return
	 * @throws CowboyException
	 */
	abstract public GetTabResourceResponse getTabResource(String tabId, String action, String lastResourceId,
														  Context context)throws CowboyException;

	
}
