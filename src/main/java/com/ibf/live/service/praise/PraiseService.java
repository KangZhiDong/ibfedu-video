package com.ibf.live.service.praise;


import com.ibf.live.entity.praise.Praise;

/**
 * 点赞服务接口
 *@author www.inxedu.com
 */
public interface PraiseService {
	/**
	 * 添加点赞记录
	 */
	public Long addPraise(Praise praise);
	
	/**
	 * 根据条件查询点赞数
	 */
	public int queryPraiseCount(Praise praise);
}
