package com.ibf.live.service.website;


import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.entity.website.WebsiteCourseDetail;
import com.ibf.live.entity.website.WebsiteCourseDetailDTO;

import java.util.List;

/**
 * 课程推荐关联接口
 * @author www.inxedu.com
 */
public interface WebsiteCourseDetailService {
	/**
	 * 创建推荐课程 
	 * @param detail
	 */
	public void createWebsiteCourseDetail(String detail);
	
	/**
	 * 分页查询推荐课程
	 * @param dto 查询条件
	 * @param page 分页条件
	 * @return List<WebsiteCourseDetailDTO>
	 */
	public List<WebsiteCourseDetailDTO> queryCourseDetailPage(WebsiteCourseDetailDTO dto, PageEntity page);

	/**
	 * 删除推荐课程
	 * @param id 推荐课程ID
	 */
	public void deleteDetailById(int id);

	/**
	 * 修改推荐课程排序值
	 * @param id 推荐课程ID
	 * @param sort 新的排序值
	 */
	public void updateSort(int id, int sort);
	
	/**
	 * 通过类型ID，查询该类型的推荐课程
	 * @param recommendId 类型ID
	 * @return List<WebsiteCourseDetail>
	 */
	public List<WebsiteCourseDetail> queryDetailListByrecommendId(int recommendId);
}