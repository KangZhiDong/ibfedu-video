package com.ibf.live.controller.comment;

import com.github.pagehelper.PageHelper;
import com.ibf.live.common.controller.BaseController;
import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.common.util.SingletonLoginUtils;
import com.ibf.live.entity.comment.Comment;
import com.ibf.live.service.comment.CommentService;
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
 * 前台评论模块
 * @author www.ibeifeng.com
 */
@Controller
@RequestMapping("/api/comment")
public class AppCommentController extends BaseController {
	
	private static Logger logger = LoggerFactory.getLogger(AppCommentController.class);
	@Autowired
	private CommentService commentService;
//	@Autowired
//	HttpServletRequest request1;
//	@Autowired
//	private QuestionsCommentService questionsCommentService;
	/**
	 * 查询评论
	 */
	@RequestMapping("/query")
	@ResponseBody
	public Map<String, Object> queryComment(HttpServletRequest request, Comment comment) {
		Map<String, Object> json=new HashMap<String, Object>();
		try {
			String currentPage=request.getParameter("currentPage");//当前页
			if(currentPage==null||currentPage.trim().equals("")){
				json=this.setJson(false, "页码不能为空", null);
				return json;
			}
			PageEntity page = new PageEntity();
			page.setCurrentPage(Integer.parseInt(currentPage));//当前页
			
			String pageSize = request.getParameter("pageSize");
			if(pageSize!=null ){
				page.setPageSize(Integer.parseInt(pageSize));
			}
			//查询评论一级
			comment.setPCommentId(0);
			if (pageSize != null) {
				page.setPageSize(Integer.parseInt(pageSize));
				PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
			}else {
				PageHelper.startPage(Integer.parseInt(currentPage), 8);
			}
			List<Comment> commentList = commentService.getCommentByPage(comment);// 查询评论
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("commentList", commentList);// 评论list
			map.put("page", page);
			//map.put("comment", comment);
			json=this.setJson(true, "成功", map);
		} catch(Exception e){
			json=this.setJson(false, "异常", null);
			logger.error("queryComment()--error",e);
		}
		return json;
	}
	
	/**
	 * 查询文章评论的回复
	 */
	@RequestMapping("/commentreply")
	@ResponseBody
	public Map<String, Object> queryCommentReply(HttpServletRequest request, Comment comment) {
		Map<String,Object> json = new HashMap<String,Object>();
		try {
			comment.setLimitNumber(9);
			List<Comment> commentList = commentService.queryCommentList(comment);
			json = this.setJson(true, "成功", commentList);
//			User user = SingletonLoginUtils.getLoginUser(request);
//			request.setAttribute("user", user);
		} catch (Exception e) {
			logger.error("queryCommentReply()--error", e);
		}
		return json;
	}
	
	/**
	 * 添加评论
	 */
	@RequestMapping("/addcomment")
	@ResponseBody
	public Map<String, Object> addComment(HttpServletRequest request,Comment comment)  {
		Map<String,Object> json = new HashMap<String,Object>();

		try {
			// 如果用户未登录则不能评论
			int userId = SingletonLoginUtils.getLoginUserId(request);
//			String userKey = WebUtils.getCookie(request, CacheConstans.USER_LOGIN_INFO);
//			User user = gson.fromJson(RedisCacheUtil.get(userKey),User.class);
//			int userId = user.getUserId();
			//int userId=1;
			if (userId== 0) {
				json = this.setJson(false, "isnotlogin", "用户id为空");
				return json;
			}
			// 登陆用户id

			comment.setUserId(userId);
			// 添加评论
			commentService.addComment(comment);
			json = this.setJson(true, "true", "发送成功");

		} catch (Exception e) {
			json = this.setJson(false, "false", "发送异常");
			logger.error("addComment()--error", e);
		}
		return json;
	}
	
	
}
