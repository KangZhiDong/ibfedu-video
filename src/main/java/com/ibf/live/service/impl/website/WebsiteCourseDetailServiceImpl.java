package com.ibf.live.service.impl.website;


import com.ibf.live.common.constants.CacheConstans;
import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.common.redis.RedisCacheUtil;
import com.ibf.live.dao.website.WebsiteCourseDetailDao;
import com.ibf.live.entity.website.WebsiteCourseDetail;
import com.ibf.live.entity.website.WebsiteCourseDetailDTO;
import com.ibf.live.service.website.WebsiteCourseDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推荐课程关联接口实现
 * @author www.inxedu.com
 */
@Service("websiteCourseDetailService")
public class WebsiteCourseDetailServiceImpl implements WebsiteCourseDetailService {
	@Autowired
	private WebsiteCourseDetailDao websiteCourseDetailDao;
	
	public void createWebsiteCourseDetail(String detail) {
		websiteCourseDetailDao.createWebsiteCourseDetail(detail);
		//EHCacheUtil.remove(CacheConstans.RECOMMEND_COURSE);
		 RedisCacheUtil.del(CacheConstans.RECOMMEND_COURSE);
	}

	
	public List<WebsiteCourseDetailDTO> queryCourseDetailPage(WebsiteCourseDetailDTO dto, PageEntity page) {
		return websiteCourseDetailDao.queryCourseDetailPage(dto, page);
	}

	
	public void deleteDetailById(int id) {
		websiteCourseDetailDao.deleteDetailById(id);
		//EHCacheUtil.remove(CacheConstans.RECOMMEND_COURSE);
		RedisCacheUtil.del(CacheConstans.RECOMMEND_COURSE);
	}

	
	public void updateSort(int id, int sort) {
		Map<String,Integer> map =new HashMap<String, Integer>();
		map.put("id", id);
		map.put("sort", sort);
		websiteCourseDetailDao.updateSort(map);
		//EHCacheUtil.remove(CacheConstans.RECOMMEND_COURSE);
		RedisCacheUtil.del(CacheConstans.RECOMMEND_COURSE);
	}

	
	public List<WebsiteCourseDetail> queryDetailListByrecommendId(int recommendId) {
		return websiteCourseDetailDao.queryDetailListByrecommendId(recommendId);
	}
}