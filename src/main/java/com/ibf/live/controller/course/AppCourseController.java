package com.ibf.live.controller.course;

import com.google.gson.reflect.TypeToken;
import com.ibf.live.common.constants.CommonConstants;
import com.ibf.live.common.controller.BaseController;
import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.common.redis.RedisCacheUtil;
import com.ibf.live.common.util.MD5;
import com.ibf.live.common.util.SignatureUtil;
import com.ibf.live.common.util.SingletonLoginUtils;
import com.ibf.live.entity.course.*;
import com.ibf.live.entity.kpoint.CourseKpoint;
import com.ibf.live.entity.kpoint.CourseKpointVideo;
import com.ibf.live.service.course.*;
import com.ibf.live.service.subject.SubjectService;
import com.ibf.live.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author www.inxedu.com
 */
@Controller
@RequestMapping("/api/course")
public class AppCourseController extends BaseController {
	//private static Logger logger = Logger.getLogger(AppCourseController.class);
	private static Logger logger = LoggerFactory.getLogger(AppCourseController.class);

	@Autowired
	private CourseService courseService;

	@Autowired
	private UserService userService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private CourseFavoritesService courseFavoritesService;
	@Autowired
	private CourseKpointService courseKpointService;
	@Autowired
	private CourseStudyhistoryService courseStudyhistoryService;
	@Autowired
	private CourseBuyService courseBuyService;
	/**
	 * 课程列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Map<String, Object> getCourseList(HttpServletRequest request) {
		Map<String, Object> json = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			String currentPage = request.getParameter("currentPage");// 当前页
			if (currentPage == null || currentPage.trim().equals("")) {
				json = this.setJson(false, "页码不能为空", null);
				return json;
			}
			PageEntity page = new PageEntity();
			page.setCurrentPage(1);
			page.setPageSize(10);// 每页多少条数据
			if(Integer.parseInt(currentPage)>0){
				page.setCurrentPage(Integer.parseInt(currentPage));// 当前页
			}
			String pageSize = request.getParameter("pageSize");
			if (pageSize != null) {
				page.setPageSize(Integer.parseInt(pageSize));
			}

			String teacher = request.getParameter("teacherId");// 讲师Id
			int teacherId = 0;
			if (teacher != null && !teacher.trim().equals("")) {
				teacherId = Integer.parseInt(teacher);// 讲师Id
			}

			String courseName = request.getParameter("courseName");// 课程名称
			String subject = request.getParameter("subjectId");// 专业Id
			int subjectId = 0;
			if (subject != null && !subject.trim().equals("")) {
				subjectId = Integer.parseInt(subject);// 专业Id
			}
			String beginTime = request.getParameter("beginCreateTime");// 开始添加时间
			String endTime = request.getParameter("endCreateTime");// 结束添加时间
			Date beginCreateTime = null;
			Date endCreateTime = null;
			if (beginTime != null && !beginTime.trim().equals("")) {
				beginCreateTime = sdf.parse(beginTime);// 开始添加时间
			}
			if (endTime != null && !endTime.trim().equals("")) {
				endCreateTime = sdf.parse(endTime);// 结束添加时间
			}

			/* ================查询条件================= */
			QueryCourse queryCourse = new QueryCourse();
			queryCourse.setIsavaliable(1);// 1.正常课程2.删除课程
			queryCourse.setTeacherId(teacherId);// 讲师Id
			queryCourse.setCourseName(courseName);// 课程名称
			queryCourse.setSubjectId(subjectId);// 专业Id
			queryCourse.setBeginCreateTime(beginCreateTime);// 开始添加事件
			queryCourse.setEndCreateTime(endCreateTime);// 结束添加时间
			String order =  request.getParameter("order");
			if (order != null && !order.trim().equals("")) {
				queryCourse.setOrder(order);
			}
			String courseType =  request.getParameter("courseType");
			if (courseType != null && !courseType.trim().equals("")) {
				queryCourse.setCourseType(Integer.parseInt(courseType));
			}
			String isFree =  request.getParameter("isFree");
			if (isFree != null && !isFree.trim().equals("")) {
				queryCourse.setIsFree(isFree);
			}
			List<CourseDto> courseList = courseService.queryCourseListPage(queryCourse, page);
			if(courseList!=null&&courseList.size()>0){
				for(CourseDto cdo:courseList){
					String learnCount = RedisCacheUtil.get("COURSE_LEARNED_USER_COUNT_" + cdo.getCourseId());
					Long couStudyhistorysLearnedCount ;
					if (learnCount != null && !learnCount.trim().equals("")) {
						couStudyhistorysLearnedCount = Long.valueOf(learnCount);
					} else {
						couStudyhistorysLearnedCount = Long.valueOf(courseStudyhistoryService.getCourseStudyhistoryCountByCouId(Long.valueOf(cdo.getCourseId())));
						//couStudyhistorysLearnedCount = Long.valueOf(0);
						RedisCacheUtil.set("COURSE_LEARNED_USER_COUNT_" + cdo.getCourseId(),couStudyhistorysLearnedCount.toString(), 3600);// 缓存一小时
					}
					cdo.setLearningCount(couStudyhistorysLearnedCount);
				  }
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("courseList", courseList);
			map.put("courseName", courseName);
			map.put("page", page);
			json = this.setJson(true, "成功", map);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("showCourseList()--error", e);
		}
		return json;
	}

//	/**
//	 * 课程列表
//	 */
//	@RequestMapping("/openapi/cou/list")
//	@ResponseBody
//	public Map<String, Object> showCourseList(HttpServletRequest request,@ModelAttribute("page") PageEntity page) {
//		Map<String, Object> json = new HashMap<String, Object>();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		try {
//			String currentPage = request.getParameter("currentPage");// 当前页
//			if (currentPage == null || currentPage.trim().equals("")) {
//				json = this.setJson(false, "页码不能为空", null);
//				return json;
//			}
//			page.setCurrentPage(Integer.parseInt(currentPage));// 当前页
//
//			page.setPageSize(10);// 每页多少条数据
//			String pageSize = request.getParameter("pageSize");
//			if (pageSize != null) {
//				page.setPageSize(Integer.parseInt(pageSize));
//			}
//
//			String teacher = request.getParameter("teacherId");// 讲师Id
//			int teacherId = 0;
//			if (teacher != null && !teacher.trim().equals("")) {
//				teacherId = Integer.parseInt(teacher);// 讲师Id
//			}
//
//			String courseName = request.getParameter("courseName");// 课程名称
//			String subject = request.getParameter("subjectId");// 专业Id
//			int subjectId = 0;
//			if (subject != null && !subject.trim().equals("")) {
//				subjectId = Integer.parseInt(subject);// 专业Id
//			}
//
//			String beginTime = request.getParameter("beginCreateTime");// 开始添加时间
//			String endTime = request.getParameter("endCreateTime");// 结束添加时间
//			Date beginCreateTime = null;
//			Date endCreateTime = null;
//			if (beginTime != null && !beginTime.trim().equals("")) {
//				beginCreateTime = sdf.parse(beginTime);// 开始添加时间
//			}
//			if (endTime != null && !endTime.trim().equals("")) {
//				endCreateTime = sdf.parse(endTime);// 结束添加时间
//			}
//
//			/* ================查询条件================= */
//			QueryCourse queryCourse = new QueryCourse();
//			queryCourse.setIsavaliable(1);// 1.正常课程2.删除课程
//			queryCourse.setTeacherId(teacherId);// 讲师Id
//			queryCourse.setCourseName(courseName);// 课程名称
//			queryCourse.setSubjectId(subjectId);// 专业Id
//			queryCourse.setBeginCreateTime(beginCreateTime);// 开始添加事件
//			queryCourse.setEndCreateTime(endCreateTime);// 结束添加时间
//			List<CourseDto> courseList = courseService.queryCourseListPage(queryCourse, page);
//			for(CourseDto cdo:courseList){
//				String learnCount = RedisCacheUtil.get("COURSE_LEARNED_USER_COUNT_" + cdo.getCourseId());
//				Long couStudyhistorysLearnedCount ;
//				if (learnCount != null && !learnCount.trim().equals("")) {
//					couStudyhistorysLearnedCount = Long.valueOf(learnCount);
//				} else {
//					couStudyhistorysLearnedCount = Long.valueOf(courseStudyhistoryService.getCourseStudyhistoryCountByCouId(Long.valueOf(cdo.getCourseId())));
//					//couStudyhistorysLearnedCount = Long.valueOf(0);
//					RedisCacheUtil.set("COURSE_LEARNED_USER_COUNT_" + cdo.getCourseId(),couStudyhistorysLearnedCount.toString(), 3600);// 缓存一小时
//				}
//				cdo.setLearningCount(couStudyhistorysLearnedCount);
//			}
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("courseList", courseList);
//			map.put("page", page);
//			json = this.setJson(true, "成功", map);
//		} catch (Exception e) {
//			json = this.setJson(false, "异常", null);
//			logger.error("showCourseList()--error", e);
//		}
//		return json;
//	}
//
//	/**
//	 * 课程详情
//	 */
//	@RequestMapping("/openapi/front/couinfo")
//	@ResponseBody
//	public Map<String, Object> couinfo(HttpServletRequest request) {
//		Map<String, Object> json = new HashMap<String, Object>();
//		try {
//			String courseId = request.getParameter("courseId");
//			if (courseId == null || courseId.trim().equals("")) {
//				json = this.setJson(true, "课程Id不能为空", null);
//				return json;
//			}
//			// 查询课程详情
//			Course course = courseService.queryCourseById(Integer.parseInt(courseId));
//			json = this.setJson(true, "成功", course);
//		} catch (Exception e) {
//			json = this.setJson(false, "异常", null);
//			logger.error("couinfo()--error", e);
//		}
//		return json;
//	}

	/**
	 * 课程详情及相关数据
	 */
	@RequestMapping("/couinfo/{courseId}")
	@ResponseBody
	public Map<String, Object> getCourseInfo(HttpServletRequest request, @PathVariable("courseId") int courseId) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			// 查询课程详情
			Course course = courseService.queryCourseById(courseId);
			Map<String, Object> map = new HashMap<String, Object>();
			if (course != null) {
				if(course.getCourseType()==1&&course.getChildrenIds()!=null){//如果是套餐课程 查出子类
					List<CourseDto> childList = courseService.queryCourseListByChildren(course.getChildrenIds());
					course.setCourseList(childList);
				}
				map.put("course", course);
				// 更新课程的浏览量
				courseService.updateCourseCount("pageViewcount", courseId);

				// 查询课程老师
				List<Map<String, Object>> teacherList = userService.queryCourseTeacerList(courseId);
				map.put("teacherList", teacherList);
				// 相关课程
				List<CourseDto> courseList = courseService.queryInterfixCourseLis(course.getSubjectId(), 5,course.getCourseId());
				for (CourseDto tempCoursedto : courseList) {
					teacherList = userService.queryCourseTeacerList(tempCoursedto.getCourseId());
					tempCoursedto.setTeacherList(teacherList);//查询相关教师
				}
				map.put("courseList", courseList);
				int userId = SingletonLoginUtils.getLoginUserId(request);
				if (userId > 0) {
					// 查询登用户是否已经收藏
					boolean isFavorites = courseFavoritesService.checkFavorites(userId, courseId);
					map.put("isFavorites", isFavorites);
				}
				// 查询课程章节目录
				List<CourseKpoint> parentKpointList = new ArrayList<CourseKpoint>();// 一级
																					// 课程章节
				List<CourseKpoint> kpointList = courseKpointService.queryCourseKpointByCourseId(courseId);
				if (kpointList != null && kpointList.size() > 0) {
					for (CourseKpoint temp : kpointList) {
						temp.setVideoUrl("");//隐藏VideoUrl;
						if (temp.getParentId() == null || "".equals(temp.getParentId().trim())||"0".equals(temp.getParentId())) {
							parentKpointList.add(temp);
						}
					}
					// String freeVideoId="";
					for (CourseKpoint tempParent : parentKpointList) {
						for (CourseKpoint temp : kpointList) {
							if (temp.getParentId() != null && temp.getParentId().equals(tempParent.getKpointId())) {
								tempParent.getKpointList().add(temp);
							}
							// 获取一个可以试听的视频id
							// if
							// ("".equals(freeVideoId.trim())&&temp.getFree()==1&&temp.getKpointType()==1)
							// {
							// freeVideoId=temp.getKpointId();
							// map.put("freeVideoId",freeVideoId);
							// }
						}
					}
					map.put("parentKpointList", parentKpointList);
				}
				// 从缓存中获取
				// 学习此课程的总人数
				String learnCount = RedisCacheUtil.get("COURSE_LEARNED_USER_COUNT_" + courseId);
				Long couStudyhistorysLearnedCount;
				if (learnCount != null && !learnCount.trim().equals("")) {
					couStudyhistorysLearnedCount = Long.valueOf(learnCount);
				} else {
					couStudyhistorysLearnedCount = Long.valueOf(0);
				}
				// 学习此课程的人 (最新8个)
				String jsonStr = RedisCacheUtil.get("COURSE_LEARNED_USER_"+ courseId);
				List<CourseStudyhistory> couStudyhistorysLearned = gson.fromJson(jsonStr,new TypeToken<List<CourseStudyhistory>>() {}.getType());
				if (couStudyhistorysLearned == null	|| couStudyhistorysLearned.size() == 0) {
					couStudyhistorysLearned = courseStudyhistoryService.getCourseStudyhistoryListByCouId(Long.valueOf(String.valueOf(courseId)));
					if(!couStudyhistorysLearned.isEmpty()&& couStudyhistorysLearned!=null&& couStudyhistorysLearned.size()>0){
						RedisCacheUtil.set("COURSE_LEARNED_USER_" + courseId,gson.toJson(couStudyhistorysLearned), 3600);// 缓存一小时
						couStudyhistorysLearnedCount = Long.valueOf(courseStudyhistoryService.getCourseStudyhistoryCountByCouId(Long.valueOf(courseId)));
						RedisCacheUtil.set("COURSE_LEARNED_USER_COUNT_" + courseId,couStudyhistorysLearnedCount.toString(), 3600);// 缓存一小时
					}
					
				}
				map.put("couStudyhistorsLearnedCount",couStudyhistorysLearnedCount);
				map.put("courseLearnedUserList", couStudyhistorysLearned);
			}
			map.put("isok", true);
			json = this.setJson(true, "成功", map);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("couinfo()--error", e);
		}
		return json;
	}

	/**
	 * // 首页精品课程、最新课程、全部课程
	 */
	@RequestMapping("/getIndexCourseList/{type}")
	@ResponseBody
	public Map<String, Object> getIndexCourseList(HttpServletRequest request, @PathVariable("type") String type) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			QueryCourse queryCourse = new QueryCourse();
			// 只查询上架的
			queryCourse.setIsavaliable(1);
			// 只查询首页推荐的
			queryCourse.setIndexShow(1);
			queryCourse.setCount(8);// 最多20 个
			if (type != null&& !type.trim().equals("")) {
				queryCourse.setOrder(type);
			}else{
				queryCourse.setOrder("SUBJECT");// 按最新课程排序
			}
			// 获得精品课程、最新课程、全部课程
			List<CourseDto> courseDtoBNAList = courseService.queryCourse(queryCourse);
			for(CourseDto cdo:courseDtoBNAList){
				String learnCount = RedisCacheUtil.get("COURSE_LEARNED_USER_COUNT_" + cdo.getCourseId());
				Long couStudyhistorysLearnedCount ;
				if (learnCount != null && !learnCount.trim().equals("")) {
					couStudyhistorysLearnedCount = Long.valueOf(learnCount);
				} else {
					couStudyhistorysLearnedCount = Long.valueOf(courseStudyhistoryService.getCourseStudyhistoryCountByCouId(Long.valueOf(cdo.getCourseId())));
					//couStudyhistorysLearnedCount = Long.valueOf(0);
					RedisCacheUtil.set("COURSE_LEARNED_USER_COUNT_" + cdo.getCourseId(),couStudyhistorysLearnedCount.toString(), 3600);// 缓存一小时
				}
				cdo.setLearningCount(couStudyhistorysLearnedCount);
			}
			json = this.setJson(true, "成功", courseDtoBNAList);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("couinfo()--error", e);
		}
		return json;
	}
	
	  /**
     * 添加课程收藏
     */
    @RequestMapping("/createfavorites/{courseId}")
    @ResponseBody
    public Map<String,Object> createFavorites(HttpServletRequest request, @PathVariable("courseId") int courseId){
    	Map<String,Object> json = new HashMap<String,Object>();
    	try{
    		int userId = SingletonLoginUtils.getLoginUserId(request);
    		if(userId<=0){
    			json = this.setJson(false, "请登录！", null);
    			return json;
    		}
    		if(courseId<=0){
    			json = this.setJson(false, "请选择要收藏的课程！", null);
    			return json;
    		}
    		boolean is = courseFavoritesService.checkFavorites(userId, courseId);
    		if(is){
    			json = this.setJson(false, "该课程你已经收藏过了！", null);
    			return json;
    		}
    		CourseFavorites courseFavorites = new CourseFavorites();
    		courseFavorites.setUserId(userId);
    		courseFavorites.setCourseId(courseId);
    		courseFavorites.setAddTime(new Date());
    		courseFavoritesService.createCourseFavorites(courseFavorites);
    		json = this.setJson(true, "收藏成功", null);
    	}catch (Exception e) {
    		this.setAjaxException(json);
			logger.error("createFavorites()--error",e);
		}
    	return json;
    }
    
    /**
     * 获取用户与课程关联信息
     */
    @RequestMapping("/loginUserCourse/{courseId}")
    @ResponseBody
    public Map<String,Object> getLoginUserCourse(HttpServletRequest request, @PathVariable("courseId") int courseId){
    	Map<String,Object> json = new HashMap<String,Object>();
    	try{
    		int userId = SingletonLoginUtils.getLoginUserId(request);
    		if(userId<=0){
    			json = this.setJson(false, "请登录！", null);
    			return json;
    		}
    		if(courseId<=0){
    			json = this.setJson(false, "请选择课程！", null);
    			return json;
    		}
    		Map<String, Object> map = new HashMap<String, Object>();
    		boolean hasFavorite = courseFavoritesService.checkFavorites(userId, courseId);
			map.put("hasFavorite", hasFavorite);
			boolean isBuy = courseBuyService.getUserCourseCount(userId,courseId);//false;//courseFavoritesService.checkFavorites(userId, courseId);获取用户购买信息，暂时设置为true
			map.put("isBuy", isBuy);
    		json = this.setJson(true, "成功", map);
    	}catch (Exception e) {
    		json = this.setJson(false, "异常", null);
			logger.error("createFavorites()--error",e);
		}
    	return json;
    }
    
    /**
     * 获取课程播放地址
     */
    @RequestMapping("/getVideoPath/{kpointId}")
    @ResponseBody
    public Map<String,Object> getVideoPath(HttpServletRequest request, @PathVariable("kpointId") String kpointId){
    	Map<String,Object> json = new HashMap<String,Object>();
    	try{
    		int userId = SingletonLoginUtils.getLoginUserId(request);
    		if(userId<=0){
    			json = this.setJson(false, "请登录！", null);
    			return json;
    		}
//    		boolean isBuy = false;//courseFavoritesService.checkFavorites(userId, courseId);获取用户购买信息，暂时设置为true
//    		if(!isBuy){
//    			json = this.setJson(false, "请购买课程！", null);
//    			return json;
//    		}
    		CourseKpointVideo video = courseKpointService.getCourseKpointVideoPath(kpointId);
    		Map<String, Object> map = new HashMap<String, Object>();
    		String imageUrl = video.getImagePath();
    		long expirationTime = (new Date().getTime()+2*60*1000)/1000;//增加2分钟
    		if(video.getVideoMachine()==1){//阿里云地址
    			String videoUrl =  video.getVideoUrl();
    			String url = videoUrl.split(CommonConstants.aliVodCdnDomian+"/")[1];
    			String signature = "/"+url+"-"+expirationTime+"-0-0-"+ CommonConstants.aliCdnKey;
                String hashValue = MD5.getMD5(signature);
                String endUrl  = videoUrl+"?auth_key="+expirationTime+"-0-0-"+hashValue;
    			map.put("t", "");
    			map.put("ak", CommonConstants.bceAccessKey);
    			map.put("videoUrl",  endUrl);
    			map.put("imageUrl", imageUrl);
    		}else if(video.getVideoMachine()==2){//百度地址
    			String mediaId = video.getVideoUrl().split("/")[3]; 
    			String message ="/"+mediaId+"/"+expirationTime;
    			String signature = SignatureUtil.bceSign(CommonConstants.bceVodSigningKey, message);
    			String token = SignatureUtil.bceGetToken(signature, CommonConstants.bceVodUserId, expirationTime);
    			map.put("t", token);
    			map.put("ak", CommonConstants.bceAccessKey);
    			map.put("videoUrl",  video.getVideoUrl());
    			map.put("imageUrl", imageUrl);
    		}else if(video.getVideoMachine()==3){//本地获取其他服务器地址
    			map.put("t", "");
    			map.put("ak", CommonConstants.bceAccessKey);
    			map.put("videoUrl",  video.getVideoUrl());
    			map.put("imageUrl", imageUrl);
    		}
    		
    		//添加播放次数 ,播放记录
    		CourseStudyhistory courseStudyhistory = new CourseStudyhistory();
			courseStudyhistory.setCourseId((long)video.getCourseId());
			courseStudyhistory.setKpointId(kpointId);
			courseStudyhistory.setUserId((long)userId);
			courseStudyhistoryService.playertimes(courseStudyhistory);
			
    		json = this.setJson(true, "成功", map);
    	}catch (Exception e) {
    		json = this.setJson(false, "异常", null);
			logger.error("createFavorites()--error",e);
		}
    	return json;
    }
    
    /**
     * 购买课程
     */
    @RequestMapping("/buyCourse/{courseId}")
    @ResponseBody
    public Map<String,Object> buyCourse(HttpServletRequest request, @PathVariable("courseId") int courseId){
    	Map<String,Object> json = new HashMap<String,Object>();
    	try{
    		int userId = SingletonLoginUtils.getLoginUserId(request);
    		if(userId<=0){
    			json = this.setJson(false, "请登录！", null);
    			return json;
    		}
    		Map<String, Object> map = new HashMap<String, Object>();
    		//购买课程逻辑
    		courseBuyService.createUserBuy(userId, courseId);
    		json = this.setJson(true, "成功", map);
    	}catch (Exception e) {
    		json = this.setJson(false, "异常", null);
			logger.error("createFavorites()--error",e);
		}
    	return json;
    }
}
