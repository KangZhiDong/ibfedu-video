package com.ibf.live.dao.websiteehcache;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.entity.websiteehcache.WebsiteEhcache;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 缓存管理
 * @author www.inxedu.com
 *
 */
@Mapper
public interface WebsiteEhcacheDao {
	/**
	 * 添加
	 */
	public void addWebsiteEhcache(WebsiteEhcache websiteEhcache);
	
	/**
	 * 列表
	 */
	public List<WebsiteEhcache> queryWebsiteEhcacheList(WebsiteEhcache websiteEhcache, PageEntity page);
	
	/**
	 * 列表
	 */
	public List<WebsiteEhcache> queryWebsiteEhcacheList(WebsiteEhcache websiteEhcache);
	
	/**
	 * 删除
	 */
	public Long delWebsiteEhcache(Long id);
	
	/**
	 * 根据Id查询
	 */
	public WebsiteEhcache getWebsiteEhcacheById(Long id);
	
	/**
	 * 查询key是否存在
	 */
	public int queryWebsiteEhcacheIsExsit(String key);
	
	/**
	 * 修改
	 */
	public Long updateWebsiteEhcache(WebsiteEhcache websiteEhcache);
}
