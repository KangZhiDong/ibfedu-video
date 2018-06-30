package com.ibf.live.controller.letter;

import com.ibf.live.common.controller.BaseController;
import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.common.util.SingletonLoginUtils;
import com.ibf.live.common.util.StringUtils;
import com.ibf.live.entity.letter.MsgReceive;
import com.ibf.live.entity.letter.QueryMsgReceive;
import com.ibf.live.service.letter.MsgReceiveService;
import com.ibf.live.service.user.UserService;
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
 * 我的消息
 * @author http://www.inxedu.com
 */
@Controller
@RequestMapping("/api/letter")
public class AppLetterController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(AppLetterController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private MsgReceiveService msgReceiveService;

	/**
	 * 查询站内信收件箱（无社区）
	 *
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Map<String, Object> queryUserLetter(HttpServletRequest request) {
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
			page.setPageSize(5);// 每页多少条数据
			page.setCurrentPage(1);
			String pageSize = request.getParameter("pageSize");
			if (pageSize != null) {
				page.setPageSize(Integer.parseInt(pageSize));
			}
			if(Integer.parseInt(currentPage)>0){
				page.setCurrentPage(Integer.parseInt(currentPage));// 当前页
			}
			MsgReceive msgReceive = new MsgReceive();
			msgReceive.setReceivingCusId(Long.valueOf(userId));// set用户id
			List<QueryMsgReceive> queryLetterList = msgReceiveService.queryMsgReceiveByInbox(msgReceive, page);// 查询站内信收件箱
			//修改用户消息数后  重新加入缓存
			userService.setLoginInfo(request,msgReceive.getReceivingCusId().intValue(),"false");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("queryLetterList", queryLetterList);
			map.put("page", page);
			json = this.setJson(true, "成功", map);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("myFavorites()---error", e);
		}
		return json;
	}

	/**
     * 删除某条信息
     *
     * @param msgReceive
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Map<String, Object> delLetterById(HttpServletRequest request) {
    	  Map<String, Object> json = new HashMap<String, Object>();
        try {
        	int userId = SingletonLoginUtils.getLoginUserId(request);
			if (userId==0) {
				json = this.setJson(false, "请先登录", "");
				return json;
			}
			String idString=request.getParameter("letterId");
			if(StringUtils.isEmpty(idString)){
				json = this.setJson(false, "参数有误，请求超时！", "");
				return json;
			}
			MsgReceive msgReceive=new MsgReceive();
			msgReceive.setId(Long.parseLong(idString));
			msgReceive.setReceivingCusId(Long.valueOf(userId));
            Long num = msgReceiveService.delMsgReceiveInbox(msgReceive);// 删除收件箱
           
            if (num.intValue() == 1) {
            	json = this.setJson(true, "成功", null);;// 成功
            } else {
            	json = this.setJson(true, "失败", null);
            }
        } catch (Exception e) {
          	json = this.setJson(true, "失败", null);
            logger.error("UserController.delLetterInbox()", e);
            setExceptionRequest(request, e);
        }
        return json;
    }
}
