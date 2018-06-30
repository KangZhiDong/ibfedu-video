package com.ibf.live.controller.questions;

import com.ibf.live.common.controller.BaseController;
import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.common.util.ObjectUtils;
import com.ibf.live.common.util.SingletonLoginUtils;
import com.ibf.live.common.util.StringUtils;
import com.ibf.live.entity.questions.Questions;
import com.ibf.live.entity.questions.QuestionsComment;
import com.ibf.live.entity.questions.QuestionsTag;
import com.ibf.live.entity.questions.QuestionsTagRelation;
import com.ibf.live.service.questions.QuestionsCommentService;
import com.ibf.live.service.questions.QuestionsService;
import com.ibf.live.service.questions.QuestionsTagRelationService;
import com.ibf.live.service.questions.QuestionsTagService;
import com.ibf.live.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 问答Controller
 * @author www.ibeifeng.com
 */
@Controller
@RequestMapping("/api/questions")
public class AppQuestionsController extends BaseController {
	
	//private static Logger logger = Logger.getLogger(AppUserController.class);
	private static Logger logger = LoggerFactory.getLogger(AppQuestionsController.class);
	@Autowired
	private QuestionsService questionsService;
	@Autowired
	private UserService userService;
	@Autowired
	private QuestionsCommentService questionsCommentService;
	@Autowired
	private QuestionsTagRelationService questionsTagRelationService;
	@Autowired
	private QuestionsTagService questionsTagService;
	
	
	/**
	 * 添加问答
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Object addQuestions(HttpServletRequest request, Questions questions) {
		Map<String,Object> json = new HashMap<String,Object>();
		try {
			int userId = SingletonLoginUtils.getLoginUserId(request);
			if (userId == 0) {
				json = this.setJson(false, "请先登录", "");
				return json;
			}
			questions.setCusId(Long.valueOf(userId));
			questions.setAddTime(new Date());
			questionsService.addQuestions(questions);
			// 保存该问答的 问答标签(多个)
			String questionsTag = request.getParameter("questionsTag");
			if (ObjectUtils.isNotNull(questionsTag)) {
				questionsTag = questionsTag.substring(1);
				String questionsTagArr[] = questionsTag.split(",");
				for (int i = 0; i < questionsTagArr.length; i++) {
					QuestionsTagRelation questionsTagRelation = new QuestionsTagRelation();
					questionsTagRelation.setQuestionsId(questions.getId());
					questionsTagRelation.setQuestionsTagId(Long.valueOf(questionsTagArr[i]));
					questionsTagRelationService.addQuestionsTagRelation(questionsTagRelation);
				}
			}
			json = this.setJson(true, "", questions.getId());
		} catch (Exception e) {
			logger.error("QuestionsController.addQuestions()---error", e);
			json = this.setJson(false, "系统错误,请稍后重试", "");
		}
		return json;
	}
	

	/**
	 * 问答列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Map<String, Object> getQuestionsList(HttpServletRequest request) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			String currentPage = request.getParameter("currentPage");// 当前页
			if (currentPage == null || currentPage.trim().equals("")) {
				json = this.setJson(false, "页码不能为空", null);
				return json;
			}
			
			PageEntity page = new PageEntity();
			page.setPageSize(8);// 每页多少条数据
			page.setCurrentPage(1);
			String pageSize = request.getParameter("pageSize");
			if (pageSize != null) {
				page.setPageSize(Integer.parseInt(pageSize));
			}
			if(Integer.parseInt(currentPage)>0){
				page.setCurrentPage(Integer.parseInt(currentPage));// 当前页
			}
			String questionsTagId=request.getParameter("questionsTagId");
			String type=request.getParameter("type");
			String orderFalg =request.getParameter("orderFalg");
			Questions questions=new Questions();
			if(!StringUtils.isEmpty(questionsTagId)){
				questions.setQuestionsTagId(Long.parseLong((questionsTagId)));
			}
			if(!StringUtils.isEmpty(type)){
				questions.setType(Integer.parseInt(type));
			}
			if(!StringUtils.isEmpty(orderFalg)){
				questions.setOrderFalg(orderFalg);
			}
			List<Questions> questionsList = questionsService.getQuestionsList(questions, page);
			// 查询该问答的标签
			QuestionsTagRelation questionsTagRelation = new QuestionsTagRelation();
			if (questionsList != null && questionsList.size() > 0) {
				QuestionsComment questionsComment = new QuestionsComment();
				for (Questions questionsTemp : questionsList) {
					questionsTagRelation.setQuestionsId(questionsTemp.getId());
					questionsTemp.setQuestionsTagRelationList(questionsTagRelationService.queryQuestionsTagRelation(questionsTagRelation));

					if (questionsTemp.getReplyCount() > 0) {// 问答评论不为空
						questionsComment.setQuestionId(questionsTemp.getId());
						questionsComment.setLimitSize(1);// 一条
						if (questionsTemp.getStatus() == 1) {// 已采纳最佳答案
							// 查找最佳回答回复
							questionsComment.setIsBest(1);// 已采纳
							questionsTemp.setQuestionsCommentList(questionsCommentService.getQuestionsCommentList(questionsComment));
						} else {
							// 查找最新回答回复
							questionsComment.setIsBest(-1);
							questionsComment.setOrderFlag("new");// 最新
							questionsTemp.setQuestionsCommentList(questionsCommentService.getQuestionsCommentList(questionsComment));
						}
					}
				}
			}
			//全部问答标签
			List<QuestionsTag> questionsTagList = questionsTagService.getQuestionsTagList(new QuestionsTag());
			//热门问答 推荐
			List<Questions> hotQuestionsList = questionsService.queryQuestionsOrder(8);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("hotQuestionsList", hotQuestionsList);
			map.put("questionsTagList", questionsTagList);
			map.put("questionsList", questionsList);
			map.put("page", page);
			json = this.setJson(true, "成功", map);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("showCourseList()--error", e);
		}
		return json;
	}
	
	
	/**
	 * wo的问答列表
	 */
	@RequestMapping("/myList")
	@ResponseBody
	public Map<String, Object> getMyQuestionsList(HttpServletRequest request) {
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
			String leftType=request.getParameter("leftType");
			if (leftType == null || leftType.trim().equals("")) {
				json = this.setJson(false, "参数有误,请求超时！", null);
				return json;
			}
			Questions questions=new Questions();
			if(leftType.equals("3")){  //查询我的提问
				questions.setCusId(Long.parseLong(userId+""));
		    }else if(leftType.equals("4")){ //查询我的评论
		    	questions.setCommentUserId(Long.parseLong(userId+""));
			}
			PageEntity page = new PageEntity();
			page.setPageSize(5);// 每页多少条数据
			page.setCurrentPage(1);
			String pageSize = request.getParameter("pageSize");
			if (pageSize != null) {
				page.setPageSize(Integer.parseInt(pageSize));
			}
			if(Integer.parseInt(currentPage)>0){
				page.setCurrentPage(Integer.parseInt(currentPage));// 当前页
			}
			List<Questions> questionsList = questionsService.getQuestionsList(questions, page);
			// 查询该问答的标签
			QuestionsTagRelation questionsTagRelation = new QuestionsTagRelation();
			if (questionsList != null && questionsList.size() > 0) {
				QuestionsComment questionsComment = new QuestionsComment();
				for (Questions questionsTemp : questionsList) {
					questionsTagRelation.setQuestionsId(questionsTemp.getId());
					questionsTemp.setQuestionsTagRelationList(questionsTagRelationService.queryQuestionsTagRelation(questionsTagRelation));

					if (questionsTemp.getReplyCount() > 0) {// 问答评论不为空
						questionsComment.setQuestionId(questionsTemp.getId());
						questionsComment.setLimitSize(1);// 一条
						if (questionsTemp.getStatus() == 1) {// 已采纳最佳答案
							// 查找最佳回答回复
							questionsComment.setIsBest(1);// 已采纳
							questionsTemp.setQuestionsCommentList(questionsCommentService.getQuestionsCommentList(questionsComment));
						} else {
							// 查找最新回答回复
							questionsComment.setIsBest(-1);
							questionsComment.setOrderFlag("new");// 最新
							questionsTemp.setQuestionsCommentList(questionsCommentService.getQuestionsCommentList(questionsComment));
						}
					}
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("questionsList", questionsList);
			map.put("page", page);
			json = this.setJson(true, "成功", map);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("showCourseList()--error", e);
		}
		return json;
	}
	
	/**
	 * 到问答详情
	 */
	@RequestMapping("/info")
	@ResponseBody
	public Map<String, Object> getQuestionsInfo(HttpServletRequest request) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			String currentPage = request.getParameter("currentPage");// 当前页
			if (currentPage == null || currentPage.trim().equals("")) {
				json = this.setJson(false, "页码不能为空", null);
				return json;
			}
			
			PageEntity page = new PageEntity();
			page.setPageSize(3);
			page.setCurrentPage(1);
			String pageSize = request.getParameter("pageSize");
			if (pageSize != null) {
				page.setPageSize(Integer.parseInt(pageSize));
			}
			if(Integer.parseInt(currentPage)>0){
				page.setCurrentPage(Integer.parseInt(currentPage));// 当前页
			}
			String questionId=request.getParameter("questionId");
			if (questionId == null || questionId.trim().equals("")) {
				json = this.setJson(false, "请求超时！", null);
				return json;
			}
			Questions questions = questionsService.getQuestionsById(Long.parseLong(questionId));
			// 更新问答的浏览数 +1
			questions.setBrowseCount(questions.getBrowseCount() + 1);
			questionsService.updateQuestions(questions);

			// 查询该问答的标签
			QuestionsTagRelation questionsTagRelation = new QuestionsTagRelation();
			questionsTagRelation.setQuestionsId(questions.getId());
			questions.setQuestionsTagRelationList(questionsTagRelationService.queryQuestionsTagRelation(questionsTagRelation));
			// 热门问答
			List<Questions> hotQuestionsList = questionsService.queryQuestionsOrder(8);

			// 查询所有的问答标签
			List<QuestionsTag> questionsTagList = questionsTagService.getQuestionsTagList(new QuestionsTag());
			
			//查询普通评论
			QuestionsComment questionsComment=new QuestionsComment();
			questionsComment.setQuestionId(Long.parseLong(questionId));
			List<QuestionsComment> questionsCommentList = questionsCommentService.queryQuestionsCommentListByQuestionsId(questionsComment, page);
			//查询最佳答案
			if(questions.getStatus()==1){//已采纳最佳答案
				questionsComment=new QuestionsComment();
				//查找最佳回答回复
				questionsComment.setIsBest(1);//已采纳
				questionsComment.setQuestionId(questions.getId());
				questionsComment.setLimitSize(1);
				List<QuestionsComment> GGOOdCommentList =questionsCommentService.getQuestionsCommentList(questionsComment);
				questions.setQuestionsCommentList(GGOOdCommentList);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("questionsCommentList", questionsCommentList);
			map.put("questionsTagList", questionsTagList);
			map.put("hotQuestionsList", hotQuestionsList);
			map.put("questions", questions);
			map.put("page", page);
			json = this.setJson(true, "成功", map);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("APPQuestionsController.getQuestionsInfo()---error", e);
		}
		return json;
	}
	

	/**
	 * 问答列表
	 */
	@RequestMapping("/tagList")
	@ResponseBody
	public Map<String, Object> getQuestionsTagList(HttpServletRequest request) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			List<QuestionsTag> questionsTagList = questionsTagService.getQuestionsTagList(new QuestionsTag());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("questionsTagList", questionsTagList);
			json = this.setJson(true, "成功", map);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("showCourseList()--error", e);
		}
		return json;
	}
	
	
}
