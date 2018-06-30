package com.ibf.live.dao.message;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.entity.message.MessageLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface MessageLogDao {

	
	/**
     * 分页查询聊天记录
     * @return
     */
	public List<MessageLog> queryLogListPage(MessageLog query, PageEntity page);

	public List<MessageLog> queryListPage(MessageLog query, PageEntity page);

	public List<MessageLog> queryInfoListPage(MessageLog query, PageEntity page);

	public List<MessageLog> queryChatPeopleForExport(MessageLog query, PageEntity page);

	public List<MessageLog> queryChatSomedayForExport(MessageLog query, PageEntity page);
}
