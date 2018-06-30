package com.ibf.live.service.impl.websiteehcache;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.dao.websiteehcache.WebsiteEhcacheDao;
import com.ibf.live.entity.websiteehcache.WebsiteEhcache;
import com.ibf.live.service.websiteehcache.WebsiteEhcacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 缓存管理
 * @author www.inxedu.com
 *
 */
@Service("websiteEhcacheService")
public class WebsiteEhcacheServiceImpl implements WebsiteEhcacheService {
	
	@Autowired
	private WebsiteEhcacheDao websiteEhcacheDao;

	@Override
	public void addWebsiteEhcache(WebsiteEhcache websiteEhcache) {
		websiteEhcacheDao.addWebsiteEhcache(websiteEhcache);
	}
	
	@Override
	public List<WebsiteEhcache> queryWebsiteEhcacheList(WebsiteEhcache websiteEhcache, PageEntity page) {
		return websiteEhcacheDao.queryWebsiteEhcacheList(websiteEhcache, page);
	}
	
	@Override
	public List<WebsiteEhcache> queryWebsiteEhcacheList(WebsiteEhcache websiteEhcache) {
		return websiteEhcacheDao.queryWebsiteEhcacheList(websiteEhcache);
	}

	@Override
	public Long delWebsiteEhcache(Long id) {
		return websiteEhcacheDao.delWebsiteEhcache(id);
	}

	@Override
	public WebsiteEhcache getWebsiteEhcacheById(Long id) {
		return websiteEhcacheDao.getWebsiteEhcacheById(id);
	}

	@Override
	public boolean queryWebsiteEhcacheIsExsit(String key) {
		if(websiteEhcacheDao.queryWebsiteEhcacheIsExsit(key)>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Long updateWebsiteEhcache(WebsiteEhcache websiteEhcache) {
		return websiteEhcacheDao.updateWebsiteEhcache(websiteEhcache);
	}

	

}
