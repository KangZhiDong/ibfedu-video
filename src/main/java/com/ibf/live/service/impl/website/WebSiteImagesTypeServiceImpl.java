package com.ibf.live.service.impl.website;

import com.ibf.live.dao.website.WebSiteImagesTypeDao;
import com.ibf.live.entity.website.WebSiteImagesType;
import com.ibf.live.service.website.WebSiteImagesTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author www.inxedu.com
 *
 */
@Service("webSiteImagesTypeService")
public class WebSiteImagesTypeServiceImpl implements WebSiteImagesTypeService {
	
	@Autowired
	private WebSiteImagesTypeDao webSiteImagesTypeDao;
	
	public int createImageType(WebSiteImagesType type) {
		return webSiteImagesTypeDao.createImageType(type);
	}

	
	public List<WebSiteImagesType> queryAllTypeList() {
		return webSiteImagesTypeDao.queryAllTypeList();
	}

	
	public void deleteTypeById(int typeId) {
		webSiteImagesTypeDao.deleteTypeById(typeId);
	}

	
	public void updateType(WebSiteImagesType type) {
		webSiteImagesTypeDao.updateType(type);
	}

}
