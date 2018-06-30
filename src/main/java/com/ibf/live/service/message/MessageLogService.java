package com.ibf.live.service.message;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.entity.message.MessageLog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface MessageLogService {

	/**
	 * 获取聊天记录
	 */
	public List<MessageLog> queryLogListPage(MessageLog query, PageEntity page);

	/***
	 * 获取直播间直播数据
	 */
	public List<MessageLog> queryListPage(MessageLog query, PageEntity page);

	/***
	 * 后台聊天数据获取
	 * @param query
	 * @param page
	 * @return
	 */
	public List<MessageLog> queryInfoListPage(MessageLog query, PageEntity page);

	/***
	 * 获取某天所有具体聊天的人员
	 * 导出
	 */
	public List<MessageLog> queryChatPeopleForExport(MessageLog query, PageEntity page);

	/***
	 * 获取某人所有进出房间聊天记录
	 * 导出
	 */
	public List<MessageLog> queryChatSomedayForExport(MessageLog query, PageEntity page);

	/***
	 * 全部导出
	 * @param request
	 * @param response
	 * @param query
	 * @throws Exception
	 */
	public void ExportAll(HttpServletRequest request, HttpServletResponse response, MessageLog query) throws Exception;
	
	
	
}
