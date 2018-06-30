package com.ibf.live.dao.praise;

import com.ibf.live.entity.praise.Praise;
import org.apache.ibatis.annotations.Mapper;

/**
 * 点赞管理接口
 *@author www.inxedu.com
 */
@Mapper
public interface PraiseDao {
	/**
	 * 添加点赞记录
	 */
	public Long addPraise(Praise praise);
	
	/**
	 * 根据条件查询点赞数
	 */
	public int queryPraiseCount(Praise praise);
}
