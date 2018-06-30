package com.ibf.live.service.impl.course;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.dao.course.CourseFavoritesDao;
import com.ibf.live.entity.course.CourseFavorites;
import com.ibf.live.entity.course.FavouriteCourseDTO;
import com.ibf.live.service.course.CourseFavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CourseFavorites 课程收藏 管理接口
 * @author www.inxedu.com
 */
@Service("courseFavoritesService")
public class CourseFavoritesServiceImpl implements CourseFavoritesService {

	@Autowired
	private CourseFavoritesDao courseFavoritesDao;
	
	public void createCourseFavorites(CourseFavorites cf) {
		courseFavoritesDao.createCourseFavorites(cf);
	}
	
	public void deleteCourseFavoritesById(String ids) {
		courseFavoritesDao.deleteCourseFavoritesById(ids);
	}

	
	public boolean checkFavorites(int userId, int courseId) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("courseId", courseId);
		int count = courseFavoritesDao.checkFavorites(map);
		if(count>0){
			return true;
		}
		return false;
	}

	
	public List<FavouriteCourseDTO> queryFavoritesPage(int userId, PageEntity page) {
		return courseFavoritesDao.queryFavoritesPage(userId, page);
	}
    

}