package com.ibf.live.dao.website;


import com.ibf.live.entity.website.WebsiteProfile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 网站配置
 * @author www.inxedu.com
 */
@Mapper
public interface WebsiteProfileDao {
	/**
	 * 根据type查询网站配置
	 */
	public WebsiteProfile getWebsiteProfileByType(String type);
	/**
	 * 添加查询网站配置
	 */
	public void addWebsiteProfileByType(WebsiteProfile websiteProfile);
	/**
	 * 更新网站配置管理
	 */
	public void updateWebsiteProfile(WebsiteProfile websiteProfile);

	/**
	 * 查询网站配置
	 */
	public List<WebsiteProfile> getWebsiteProfileList();
}