package com.ibf.live.controller.course;

import com.ibf.live.common.controller.BaseController;
import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.common.util.SingletonLoginUtils;
import com.ibf.live.entity.course.CourseBuy;
import com.ibf.live.entity.course.CourseStudyhistory;
import com.ibf.live.entity.user.User;
import com.ibf.live.service.course.CourseBuyService;
import com.ibf.live.service.course.CourseKpointService;
import com.ibf.live.service.course.CourseService;
import com.ibf.live.service.course.CourseStudyhistoryService;
import com.ibf.live.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author www.inxedu.com
 */
@Controller
@RequestMapping("/api/courseBuy")
public class AppCourseBuyController extends BaseController {
	//private static Logger logger = Logger.getLogger(AppCourseController.class);
	private static Logger logger = LoggerFactory.getLogger(AppCourseBuyController.class);
	@Autowired
	private CourseService courseService;
	@Autowired
	private UserService userService;
	@Autowired
	private CourseBuyService courseBuyService;
	@Autowired
	private CourseStudyhistoryService courseStudyhistoryService;
	@Autowired
	private CourseKpointService courseKpointService;

	/**
	 * 已买课程列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Map<String, Object> getCourseBuyList(HttpServletRequest request) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
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
			
			PageEntity page = new PageEntity();
			page.setPageSize(12);// 每页多少条数据
			page.setCurrentPage(1);
			String pageSize = request.getParameter("pageSize");
			if (pageSize != null) {
				page.setPageSize(Integer.parseInt(pageSize));
			}
			if(Integer.parseInt(currentPage)>0){
				page.setCurrentPage(Integer.parseInt(currentPage));// 当前页
			}
			
			User user=new User();


			user.setUserId(userId);
			CourseBuy coursebuy=new CourseBuy();
			coursebuy.setUser(user);
			List<CourseBuy> CourseBuyList = courseBuyService.queryListPage(coursebuy, page);
			if(CourseBuyList!=null&&CourseBuyList.size()>0){
				//获取登录用户ID
				for(CourseBuy courseBuy:CourseBuyList){
					CourseStudyhistory courseStudyhistory=new CourseStudyhistory();
					courseStudyhistory.setUserId(Long.valueOf(userId));
					courseStudyhistory.setCourseId(Long.valueOf(String.valueOf(courseBuy.getCourse().getCourseId())));
					//我的课程学习记录
					List<CourseStudyhistory>  couStudyhistorysLearned=courseStudyhistoryService.getCourseStudyhistoryList(courseStudyhistory);
					int courseHistorySize=0;
					if (couStudyhistorysLearned!=null&&couStudyhistorysLearned.size()>0) {
						courseHistorySize=couStudyhistorysLearned.size();
					}
					//二级视频节点的 总数
					int sonKpointCount=courseKpointService.getSecondLevelKpointCount(Long.valueOf(courseBuy.getCourse().getCourseId()));
					NumberFormat numberFormat = NumberFormat.getInstance();
					//我的学习进度百分比
					// 设置精确到小数点后0位
					numberFormat.setMaximumFractionDigits(0);
					String studyPercent = numberFormat.format((float) courseHistorySize / (float) sonKpointCount * 100);
					if(sonKpointCount==0){
						studyPercent="0";
					}
					courseBuy.getCourse().setStudyPercent(studyPercent);
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("CourseBuyList", CourseBuyList);
			map.put("page", page);
			json = this.setJson(true, "成功", map);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("getCourseBuyList()--error", e);
		}
		    return json;
	}
}
