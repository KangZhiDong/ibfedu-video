package com.ibf.live.dao.website;


import com.ibf.live.entity.website.WebSiteImagesType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author www.inxedu.com
 *
 */
@Mapper
public interface WebSiteImagesTypeDao {
	/**
	 * 创建图片类型
	 * @param type 类型
	 * @return 返回图片类型ID
	 */
	public int createImageType(WebSiteImagesType type);
	
	/**
	 * 查询所有的图片类型
	 * @return List<WebSiteImagesType>
	 */
	public List<WebSiteImagesType> queryAllTypeList();
	
	/**
	 * 删除图片类型
	 * @param typeId 类型ID
	 */
	public void deleteTypeById(int typeId);
	
	/**
	 * 修改图片类型
	 * @param type 类型
	 */
	public void updateType(WebSiteImagesType type);
}
