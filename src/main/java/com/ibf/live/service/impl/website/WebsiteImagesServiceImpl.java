package com.ibf.live.service.impl.website;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ibf.live.common.cache.EHCacheUtil;
import com.ibf.live.common.constants.CacheConstans;
import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.common.redis.RedisCacheUtil;
import com.ibf.live.dao.website.WebsiteImagesDao;
import com.ibf.live.entity.website.WebsiteImages;
import com.ibf.live.service.website.WebsiteImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 广告图service 实现
 * @author www.inxedu.com
 *
 */
@Service("websiteImagesService")
public class WebsiteImagesServiceImpl implements WebsiteImagesService {
	@Autowired
	private WebsiteImagesDao websiteImagesDao;
	public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	
	public int creasteImage(WebsiteImages image) {
		//EHCacheUtil.remove(CacheConstans.BANNER_IMAGES);
		RedisCacheUtil.del(CacheConstans.BANNER_IMAGES);
		return websiteImagesDao.creasteImage(image);
	}

	
	public List<Map<String,Object>> queryImagePage(WebsiteImages image,
			PageEntity page) {
		return websiteImagesDao.queryImagePage(image, page);
	}

	
	public WebsiteImages queryImageById(int imageId) {
		return websiteImagesDao.queryImageById(imageId);
	}

	
	public void deleteImages(String imageIds) {
		websiteImagesDao.deleteImages(imageIds);
		//EHCacheUtil.remove(CacheConstans.BANNER_IMAGES);
		RedisCacheUtil.del(CacheConstans.BANNER_IMAGES);
	}

	
	public void updateImage(WebsiteImages image) {
		websiteImagesDao.updateImage(image);
		//EHCacheUtil.remove(CacheConstans.BANNER_IMAGES);
		RedisCacheUtil.del(CacheConstans.BANNER_IMAGES);
	}

	
	public Map<String,List<WebsiteImages>> queryImagesByType() {
		//从缓存中查询图片数据
		@SuppressWarnings("unchecked")
		//Map<String,List<WebsiteImages>> imageMapList = (Map<String,List<WebsiteImages>>) EHCacheUtil.get(CacheConstans.BANNER_IMAGES);
		Map<String,List<WebsiteImages>> imageMapList = gson.fromJson(RedisCacheUtil.get(CacheConstans.BANNER_IMAGES),new TypeToken<Map<String, List<WebsiteImages>>>() {}.getType());
		//如果缓存中不存在则重新查询
		if(imageMapList==null){
			List<WebsiteImages> imageList = websiteImagesDao.queryImagesByType();
			if(imageList!=null && imageList.size()>0){
				imageMapList = new HashMap<String, List<WebsiteImages>>();
				
				List<WebsiteImages> _list = new ArrayList<WebsiteImages>();
				int typeId =imageList.get(0).getTypeId();
				for(WebsiteImages _wi : imageList){
					if(typeId==_wi.getTypeId()){
						_list.add(_wi);
					}else{
						imageMapList.put("type_"+typeId, _list);
						 _list = new ArrayList<WebsiteImages>();
						 _list.add(_wi);
					}
					typeId = _wi.getTypeId();
				}
				//添加最后一条记录
				imageMapList.put("type_"+typeId, _list);
				//EHCacheUtil.set(CacheConstans.BANNER_IMAGES, imageMapList,CacheConstans.BANNER_IMAGES_TIME);
				EHCacheUtil.set(CacheConstans.BANNER_IMAGES, gson.toJson(imageMapList), CacheConstans.BANNER_IMAGES_TIME);
			}
		}
		return imageMapList;
	}
	
	

}
