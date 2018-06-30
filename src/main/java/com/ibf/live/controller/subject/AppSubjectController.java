package com.ibf.live.controller.subject;

import com.google.gson.reflect.TypeToken;
import com.ibf.live.common.constants.CacheConstans;
import com.ibf.live.common.controller.BaseController;
import com.ibf.live.common.redis.RedisCacheUtil;
import com.ibf.live.entity.subject.QuerySubject;
import com.ibf.live.entity.subject.Subject;
import com.ibf.live.service.subject.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author www.inxedu.com
 */
@Controller
@RequestMapping("/api/subject")
public class AppSubjectController extends BaseController {
	//private static Logger logger = Logger.getLogger(AppSubjectController.class);
	private static Logger logger = LoggerFactory.getLogger(AppSubjectController.class);
	@Autowired
	private SubjectService subjectService;

	/**
	 * 返回专业
	 */
//	@RequestMapping("/openapi/subj/toSubjectList")
//	@ResponseBody
//	public Map<String, Object> toSubjectList(HttpServletRequest request) {
//		Map<String, Object> json = new HashMap<String, Object>();
//		try {
//			QuerySubject querySubject = new QuerySubject();
//			List<Subject> subjectList = subjectService
//					.getSubjectList(querySubject);
//			json = this.setJson(true, "成功", subjectList);
//		} catch (Exception e) {
//			json = this.setJson(false, "异常", null);
//			logger.error("toSubjectList()--error", e);
//		}
//		return json;
//	}

	/**
	 * 所有专业列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Map<String, Object> showSubjectList(HttpServletRequest request) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			List<Subject> subjectList = gson.fromJson(
					RedisCacheUtil.get(CacheConstans.SUBJECT_COURSE),
					new TypeToken<List<Subject>>() {
					}.getType());
			if (subjectList == null) {
				QuerySubject querySubject = new QuerySubject();
				subjectList = subjectService.getSubjectList(querySubject);
				RedisCacheUtil.set(CacheConstans.SUBJECT_COURSE,gson.toJson(subjectList), CacheConstans.SUBJECT_COURSE_TIME);
				// EHCacheUtil.set(CacheConstans.SUBJECT_COURSE,
				// subjectList,CacheConstans.SUBJECT_COURSE_TIME);
			}
			json = this.setJson(true, "成功", subjectList);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("showCourseList()--error", e);
		}
		return json;
	}
}
