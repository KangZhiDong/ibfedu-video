package com.ibf.live.dao.website;


import com.ibf.live.entity.website.WebsiteNavigate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * WebsiteNavigateTbl管理接口
 * @author www.inxedu.com
 */
@Mapper
public interface WebsiteNavigateDao {
	
	/**
	 * 导航列表
	 */
	public List<WebsiteNavigate> getWebsiteNavigate(WebsiteNavigate websiteNavigate);
	/**
	 * 添加导航
	 */
	public void addWebsiteNavigate(WebsiteNavigate websiteNavigate);
	/**
	 * 冻结、解冻导航
	 */
	public void freezeWebsiteNavigate(WebsiteNavigate websiteNavigate);
	/**
	 * 删除导航
	 */
	public void delWebsiteNavigate(int id);
	/**
	 * 更新导航
	 */
	public void updateWebsiteNavigate(WebsiteNavigate websiteNavigate);
	/**
	 * id查询导航
	 */
	public WebsiteNavigate getWebsiteNavigateById(int id);
	/**
	 * 查询未冻结的导航列表
	 */
	public List<WebsiteNavigate> getWebNavigate();
	
}