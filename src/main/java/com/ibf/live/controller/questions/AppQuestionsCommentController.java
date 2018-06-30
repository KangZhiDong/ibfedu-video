package com.ibf.live.controller.questions;

import com.ibf.live.common.controller.BaseController;
import com.ibf.live.common.util.SingletonLoginUtils;
import com.ibf.live.entity.questions.Questions;
import com.ibf.live.entity.questions.QuestionsComment;
import com.ibf.live.service.questions.QuestionsCommentService;
import com.ibf.live.service.questions.QuestionsService;
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
@RequestMapping("/api/questionsComment")
public class AppQuestionsCommentController extends BaseController {
	
	//private static Logger logger = Logger.getLogger(AppUserController.class);
	private static Logger logger = LoggerFactory.getLogger(AppQuestionsCommentController.class);
	@Autowired
	private QuestionsService questionsService;
	@Autowired
	private QuestionsCommentService questionsCommentService;
	@Autowired
	private UserService userService;
	
	
	/**
	 * 根据问答回复id  获取子评论 
	 */
	@RequestMapping("/getCommentById")
	@ResponseBody
	public Map<String, Object> getCommentById(HttpServletRequest request) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			String commentId = request.getParameter("commentId");
			if (commentId == null || commentId.trim().equals("")) {
				json = this.setJson(false, "参数不能为空", null);
				return json;
			}
			QuestionsComment questionsComment=new QuestionsComment();
			questionsComment.setOrderFlag("new");
			//查找该评论下的子评论
			questionsComment.setCommentId(Long.parseLong(commentId));
			questionsComment.setLimitSize(9);
			List<QuestionsComment> questionsCommentRepList=questionsCommentService.getQuestionsCommentList(questionsComment);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("questionsCommentRepList", questionsCommentRepList);
			json = this.setJson(true, "成功", map);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("APPQuestionsController.getQuestionsCommentById()---error", e);
		}
		return json;
	}
	
	/**
	 * 添加回答回复
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Object addQuestionsComment(HttpServletRequest request, QuestionsComment questionsComment){
		Map<String,Object> json = new HashMap<String,Object>();
		try{
			int userId = SingletonLoginUtils.getLoginUserId(request);
			if (userId==0) {
				json = this.setJson(false, "请先登录", "");
				return json;
			}
			questionsComment.setCusId(Long.valueOf(userId));
			questionsComment.setAddTime(new Date());
			questionsComment.setIsBest(0);
			questionsComment.setReplyCount(0);
			questionsComment.setPraiseCount(0);
			questionsComment.setCommentId(0L);
			questionsCommentService.addQuestionsComment(questionsComment);
			
			//修改问答的评论数
			Questions questions=questionsService.getQuestionsById(questionsComment.getQuestionId());
			questions.setReplyCount(questions.getReplyCount()+1);
			questionsService.updateQuestions(questions);
			json = this.setJson(true, "", "");
		}catch (Exception e) {
			json = this.setJson(false, "系统错误,请稍后重试", "");
			logger.error("addQuestionsComment()---error",e);
		}
		return json;
	}
	
	
	/**
	 * 添加回答 回复 评论
	 */
	@RequestMapping("/addReply")
	@ResponseBody
	public Object addQuestionsCommentReply(HttpServletRequest request, QuestionsComment questionsComment){
		Map<String,Object> json = new HashMap<String,Object>();
		try{
			int userId = SingletonLoginUtils.getLoginUserId(request);
			if (userId==0) {
				json = this.setJson(false, "请先登录", "");
				return json;
			}
			questionsComment.setCusId(Long.valueOf(userId));
			questionsComment.setAddTime(new Date());
			questionsComment.setIsBest(0);
			questionsComment.setReplyCount(0);
			questionsComment.setPraiseCount(0);
			questionsComment.setQuestionId(0L);
			questionsCommentService.addQuestionsComment(questionsComment);
			
			//修改问答回复的评论数
			questionsComment=questionsCommentService.getQuestionsCommentById(questionsComment.getCommentId());
			questionsComment.setReplyCount(questionsComment.getReplyCount()+1);
			questionsCommentService.updateQuestionsComment(questionsComment);
			json = this.setJson(true, "", "");
		}catch (Exception e) {
			json = this.setJson(false, "系统错误,请稍后重试", "");
			logger.error("addQuestionsCommentReply()---error",e);
		}
		return json;
	}
	
	/**
	 * 采纳问答回复 为最佳答案
	 */
	@RequestMapping("/acceptComment")
	@ResponseBody
	public Object acceptComment(HttpServletRequest request, QuestionsComment questionsComment){
		Map<String,Object> json = new HashMap<String,Object>();
		try{
			int userId = SingletonLoginUtils.getLoginUserId(request);
			if (userId==0) {
				json = this.setJson(false, "请先登录", "");
				return json;
			}
			//查询问答
			Questions questions=questionsService.getQuestionsById(questionsComment.getQuestionId());
			if (questions.getStatus()==1) {
				json = this.setJson(false, "该问答已采纳最佳答案", "");
				return json;
			}
			if(userId!=questions.getCusId().intValue()){
				json = this.setJson(false, "您不是该问答的创建者,无权操作!", "");
				return json;
			}
			//查询问答回复评论
			questionsComment=questionsCommentService.getQuestionsCommentById(questionsComment.getId());
			/*if(questionsComment.getCusId()==questions.getCusId()){
				json = this.setJson(false, "您自己发表的问答回复,不能采纳!", "");
				return json;
			}*/
			//修改问答回复为最佳答案状态
			questionsComment.setIsBest(1);
			questionsCommentService.updateQuestionsComment(questionsComment);
			//修改问答的状态未 已采纳最佳答案
			questions.setStatus(1);
			questionsService.updateQuestions(questions);
			json = this.setJson(true, "", "");
		}catch (Exception e) {
			json = this.setJson(false, "系统错误,请稍后重试", "");
			logger.error("acceptComment()---error",e);
		}
		return json;
	}
}
