package com.ibf.live.controller.course;

import com.ibf.live.common.controller.BaseController;
import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.common.util.SingletonLoginUtils;
import com.ibf.live.entity.course.CourseFavorites;
import com.ibf.live.entity.course.FavouriteCourseDTO;
import com.ibf.live.service.course.CourseFavoritesService;
import com.ibf.live.service.course.CourseService;
import com.ibf.live.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收藏课程
 * @author www.inxedu.com
 */
@Controller
@RequestMapping("/api/courseFavorties")
public class AppCourseFavortiesController extends BaseController {
	//private static Logger logger = Logger.getLogger(AppCourseController.class);
	private static Logger logger = LoggerFactory.getLogger(AppCourseFavortiesController.class);
	@Autowired
	private CourseService courseService;
	@Autowired
	private UserService userService;
	@Autowired
	private CourseFavoritesService courseFavoritesService;
	
	/**
	 * 收藏课程
	 */
	@RequestMapping("/create/{courseId}")
	@ResponseBody
	public Map<String, Object> createFavorites(HttpServletRequest request, @PathVariable("courseId") int courseId) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			// 用户Id
			int userId = SingletonLoginUtils.getLoginUserId(request);
			if (userId==0) {
				json = this.setJson(false, "请先登录", "");
				return json;
			}
			// 课程Id
//			String courseId = request.getParameter("courseId");
//			if (courseId == null || courseId.trim().equals("")) {
//				json = this.setJson(false, "课程Id不能为空", null);
//				return json;
//			}
			// 判断是否收藏过
			boolean is = courseFavoritesService.checkFavorites(userId,courseId);
			if (is) {
				json = this.setJson(false, "该课程你已经收藏过了！", null);
				return json;
			}
			CourseFavorites courseFavorites = new CourseFavorites();
			courseFavorites.setCourseId(courseId);
			courseFavorites.setUserId(userId);
			courseFavorites.setAddTime(new Date());
			courseFavoritesService.createCourseFavorites(courseFavorites);
			json = this.setJson(true, "收藏成功", null);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("createFavorites()--error", e);
		}
		return json;
	}

	/**
	 * 删除收藏
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Map<String, Object> deleteFavorite(HttpServletRequest request) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			// 用户Id
			int userId = SingletonLoginUtils.getLoginUserId(request);
			if (userId==0) {
				json = this.setJson(false, "请先登录", "");
				return json;
			}
			String ids = request.getParameter("ids");
			if (ids == null || ids.trim().equals("")) {
				json = setJson(false, "id不能为空", null);
				return json;
			}
			String idsString[] =ids.split(",");
			for (int i = 0; i < idsString.length; i++) {
				courseFavoritesService.deleteCourseFavoritesById(idsString[i]);
			}
			json = setJson(true, "取消收藏成功", null);
		} catch (Exception e) {
			json = setJson(false, "异常", null);
			logger.error("deleteFavorite()---error", e);
		}
		return json;
	}

	/**
	 * 我的收藏课程列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Map<String, Object> myFavorites(HttpServletRequest request, @ModelAttribute("page") PageEntity page) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			// 用户Id
			int userId = SingletonLoginUtils.getLoginUserId(request);
			if (userId==0) {
				json = this.setJson(false, "请先登录", "");
				return json;
			}
			String currentPage = request.getParameter("currentPage");// 当前页
			if (currentPage == null || currentPage.trim().equals("")) {
				json = this.setJson(false, "页码不能为空", null);
				return json;
			}
			page.setCurrentPage(Integer.parseInt(currentPage));
			page.setPageSize(5);// 每页显示多少条数据
			String pageSize = request.getParameter("pageSize");
			if (pageSize != null) {
				page.setPageSize(Integer.parseInt(pageSize));
			}
			List<FavouriteCourseDTO> favoriteList = courseFavoritesService.queryFavoritesPage(userId, page);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("favoriteList", favoriteList);
			map.put("page", page);
			json = this.setJson(true, "成功", map);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("myFavorites()---error", e);
		}
		return json;
	}
}
