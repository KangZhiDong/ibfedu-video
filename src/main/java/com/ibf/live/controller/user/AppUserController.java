package com.ibf.live.controller.user;

import com.ibf.live.common.constants.CacheConstans;
import com.ibf.live.common.constants.CommonConstants;
import com.ibf.live.common.controller.BaseController;
import com.ibf.live.common.redis.RedisCacheUtil;
import com.ibf.live.common.util.MD5;
import com.ibf.live.common.util.SingletonLoginUtils;
import com.ibf.live.common.util.StringUtils;
import com.ibf.live.common.util.WebUtils;
import com.ibf.live.entity.pay.UserWallet;
import com.ibf.live.entity.user.User;
import com.ibf.live.entity.user.UserLoginLog;
import com.ibf.live.service.pay.UserWalletService;
import com.ibf.live.service.user.UserLoginLogService;
import com.ibf.live.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author www.inxedu.com
 */
@Controller
@RequestMapping("/api/user")
public class AppUserController extends BaseController {
	//private static Logger logger = Logger.getLogger(AppUserController.class);
	private static Logger logger = LoggerFactory.getLogger(AppUserController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private UserLoginLogService userLoginLogService;
	@Autowired
	private UserWalletService userWalletService;
	/**
	 * 登录
	 */
	@RequestMapping("/login")
	@ResponseBody
	public Map<String, Object> userLogin(HttpServletRequest request,
										 HttpServletResponse response) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			String account = request.getParameter("account");
			String password = request.getParameter("password");
			String ipForget = request.getParameter("ipForget");
			if (!StringUtils.isNotEmpty(account)) {
				json = this.setJson(false, "请输入登录帐号", null);
				return json;
			}
			if (!StringUtils.isNotEmpty(password)) {
				json = this.setJson(false, "请输入登录密码", null);
				return json;
			}

			String  loginCount = RedisCacheUtil.get(CacheConstans.USER_LOGIN_COUNT);
			int userCount=0; //当前用户登录数
			if(null!=loginCount&&!"".equals(loginCount)){
				userCount=Integer.parseInt(loginCount);
			}
			boolean isTrue= SingletonLoginUtils.validateUserCount(userCount);
			if(!isTrue){
				json = this.setJson(false, "用户连接数已被限制", null);
				return json;
			}

			User user = userService.getLoginUser(account, MD5.getMD5(password));
			if (user == null) {
				json = this.setJson(false, "帐号或密码错误", null);
				return json;
			}
			String prefix = WebUtils.getCookie(request, CacheConstans.USER_LOGIN_INFO);
			RedisCacheUtil.del(prefix);
			if (user.getIsavalible() == 2) {
				json = this.setJson(false, "帐号已被禁用", null);
				return json;
			}
			/*//判断用户是否有权限登录
			if(user.getRoleId()>0){
				if(sysRoleService.querySysRoleById(user.getRoleId()+"")==null||
						sysRoleService.querySysRoleById(user.getRoleId()+"").getRoleId()<=0){
					json = this.setJson(false, "该用户没有登录权限", null);
					return json;
				}
			}else{
				json = this.setJson(false, "该用户没有登录权限", null);
				return json;
			}*/
			String uuid = CacheConstans.USER_LOGIN_INFO + user.getUserId();// StringUtils.createUUID().replace("-",
			if ("true".equals(ipForget)) {
				// 缓存用户
				RedisCacheUtil.set(CacheConstans.USER_LOGIN_INFO + user.getUserId(), gson.toJson(user), CacheConstans.USER_TIME);
				WebUtils.setCookie(response, CacheConstans.USER_LOGIN_INFO, uuid,(CacheConstans.USER_TIME / 60 / 60 / 24));
				RedisCacheUtil.set(CacheConstans.USER_LOGIN_COUNT,String.valueOf(userCount+1), CacheConstans.USER_TIME);
			} else {
				// 缓存用户
				RedisCacheUtil.set(uuid, gson.toJson(user), 86400);// 1day
				WebUtils.setCookie(response, CacheConstans.USER_LOGIN_INFO, uuid, 1);
				RedisCacheUtil.set(CacheConstans.USER_LOGIN_COUNT,String.valueOf(userCount+1),86400);
			}
			//设置登录类型为local
			HttpSession session = request.getSession();
			session.setAttribute("AUTH_TYPE","local");
			UserLoginLog loginLog = new UserLoginLog();
			loginLog.setIp(WebUtils.getIpAddr(request));
			loginLog.setLoginTime(new Date());
			String userAgent = WebUtils.getUserAgent(request);
			if (StringUtils.isNotEmpty(userAgent)) {
				loginLog.setOsName(userAgent.split(";")[1]);
				loginLog.setUserAgent(userAgent.split(";")[0]);
			}
			loginLog.setUserId(user.getUserId());
			userLoginLogService.createLoginLog(loginLog);
			json = this.setJson(true, "登录成功", user);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("userLogin()--error", e);
		}
		return json;
	}
	/**
	 * 退出登录
	 *
	 * @param request
	 * @return String
	 */
	@RequestMapping("/exit")
	@ResponseBody
	public Map<String, Object> outLogin(HttpServletRequest request) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			String prefix = WebUtils.getCookie(request, CacheConstans.USER_LOGIN_INFO);
			json = this.setJson(true, null, null);
			if (prefix != null) {
				RedisCacheUtil.del(prefix);
				String  loginCount = RedisCacheUtil.get(CacheConstans.USER_LOGIN_COUNT);
				if(null!=loginCount&&!"".equals(loginCount)){
					loginCount=String.valueOf((Integer.parseInt(loginCount)-1));
					RedisCacheUtil.set(CacheConstans.USER_LOGIN_COUNT,loginCount,86400);
				}
				HttpSession session = request.getSession();
				String type = session.getAttribute("AUTH_TYPE").toString();
				if(!type.equals("local")){//第三方登录退出
					String id_token = session.getAttribute("ID_TOKEN").toString();
					String cur_url = session.getAttribute("CURRENT_URL").toString();
					String callbackurl =  CommonConstants.propertyUtil.getProperty("thirdAuth."+type+".LogoutUrl")+"?id_token_hint="+id_token+"&post_logout_redirect_uri="+cur_url;
					json=this.setJson(true, "退出第三方登录", callbackurl);
				}
			}
		} catch (Exception e) {
			this.setAjaxException(json);
			logger.error("outLogin()--error", e);
		}
		return json;
	}
	/**
	 * 获得用户
	 */
	@RequestMapping("/queryUserById")
	@ResponseBody
	public Map<String, Object> queryUserById(HttpServletRequest request) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			String id = request.getParameter("id");
			if (id == null || id.equals("")) {
				json = this.setJson(false, "用户Id不能为空", null);
				return json;
			}
			User user = userService.queryUserById(Integer.parseInt(id));
			json = this.setJson(true, "获取用户成功", user);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("queryUserById()--eror", e);
		}
		return json;
	}

	/**
	 * 修改用户信息
	 */
	@RequestMapping("/updateUser")
	@ResponseBody
	public Map<String, Object> updateUserInfo(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			int userId = SingletonLoginUtils.getLoginUserId(request);
			if (userId == 0) {
				json = this.setJson(false, "请先登录", null);
				return json;
			}
			String userName = request.getParameter("userName");// 姓名
			if (userName == null || userName.trim().equals("")) {
				json = this.setJson(false, "姓名不能为空", null);
				return json;
			}else if(userName!=null && !userName.trim().equals("") && !WebUtils.checkLoginName(userName.trim())){
				json = this.setJson(false, "请输入6到20位字母或者和数字组合的帐号", null);
				return json;
			}
			String showName = request.getParameter("showName");// 昵称
			if (showName == null || showName.trim().equals("")) {
				json = this.setJson(false, "昵称不能为空", null);
				return json;
			}
			String sex = request.getParameter("sex");// 性别 1男 2女
			if (sex == null || sex.trim().equals("")) {
				json = this.setJson(false, "性别不能为空", null);
				return json;
			}
			String age = request.getParameter("age");// 年龄
			if (age == null || age.trim().equals("")) {
				age="0";
			}
			String email = request.getParameter("email");// 邮箱
			if (email == null || email.trim().equals("")) {
				json = this.setJson(false, "邮箱地址不能为空", null);
				return json;
			}else if(email!=null && !email.trim().equals("") && !WebUtils.checkEmail(email, 50)){
				json = this.setJson(false, "请输入正确的邮箱号", null);
				return json;
			}
			String qq=request.getParameter("qq");
			String career = request.getParameter("career");// 简介
			String userKey = WebUtils.getCookie(request, CacheConstans.USER_LOGIN_INFO);
			User user = gson.fromJson(RedisCacheUtil.get(userKey),User.class);
			user.setUserName(userName);// 姓名
			user.setShowName(showName);// 昵称
			user.setSex(Integer.parseInt(sex));// 性别
			user.setAge(Integer.parseInt(age));// 年龄
			user.setEmail(email);
			user.setCareer(career);
			user.setQq(qq);
			userService.updateUser(user);// 修改基本信息
			RedisCacheUtil.set(CacheConstans.USER_LOGIN_INFO + user.getUserId(), gson.toJson(user), CacheConstans.USER_TIME);
			WebUtils.setCookie(response, CacheConstans.USER_LOGIN_INFO, CacheConstans.USER_LOGIN_INFO + user.getUserId(),(CacheConstans.USER_TIME / 60 / 60 / 24));
			json = this.setJson(true, "修改成功", user);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("updateUserInfo()---error", e);
		}
		return json;
	}
	/**
	 * 修改用户密码
	 * @return Map<String,Object>
	 */
	@RequestMapping("/updatepwd")
	@ResponseBody
	public Map<String,Object> updateUserPew(HttpServletRequest request){
		Map<String,Object> json = new HashMap<String,Object>();
		try{
			int userId = SingletonLoginUtils.getLoginUserId(request);
			if (userId == 0) {
				json = this.setJson(false, "请先登录", null);
				return json;
			}
			String password = request.getParameter("password");
			String newPassword = request.getParameter("newPassword");
			String confirmPwd = request.getParameter("confirmPwd");
			User passUser =userService.queryUserPassWordById(userId);
			if(!MD5.getMD5(password).equals(passUser.getPassword())){
				json = this.setJson(false, "原密码输入错误！", null);
				return json;
			}
			if(newPassword==null || newPassword.trim().length()==0 || confirmPwd==null|| confirmPwd.trim().length()==0){
				json = this.setJson(false, "两次密码不一致！", null);
				return json;
			}
			if(password!=null && password.trim().length()>0){
				User user =new User();
				user.setUserId(userId);
				if(!WebUtils.isPasswordAvailable(newPassword)){
					json = this.setJson(false, "输入错误，密码可由“_”，数字，英文大于等6位小于等于16位", null);
					return json;
				}
				user.setPassword(MD5.getMD5(newPassword));
				userService.updateUserPwd(user);
				RedisCacheUtil.del(WebUtils.getCookie(request, CacheConstans.USER_LOGIN_INFO));
			}
			json = this.setJson(true, "密码修改成功", null);
		}catch (Exception e) {
			this.setAjaxException(json);
			logger.error("updateUserPew()---error",e);
		}
		return json;
	}


	/**
	 * 修改用户头像
	 * @return Map<String,Object>
	 */
	@RequestMapping("/updateImg")
	@ResponseBody
	public Map<String,Object> updatePicImg(HttpServletRequest request){
		Map<String,Object> json = new HashMap<String,Object>();
		try{
			int userId = SingletonLoginUtils.getLoginUserId(request);
			if (userId == 0) {
				json = this.setJson(false, "请先登录", null);
				return json;
			}
			String picImg=request.getParameter("picImg");
			if(StringUtils.isEmpty(picImg) ){
				json = this.setJson(false, "上传失败！", null);
				return json;
			}
			User user =new User();
			user.setUserId(userId);
			user.setPicImg(picImg);
			userService.updateImg(user);
			//修改缓存用户
			userService.setLoginInfo(request,user.getUserId(),"false");
			json = this.setJson(true, null, null);
		}catch (Exception e) {
			this.setAjaxException(json);
			logger.error("updatePicImg()",e);
		}
		return json;
	}

	/**
	 * 获取登录用户
	 */
	@RequestMapping("/getloginUser")
	@ResponseBody
	public Map<String, Object> getLoginUser(HttpServletRequest request,
											HttpServletResponse response) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			User user = SingletonLoginUtils.getLoginUser(request);
			if (user == null || user.getUserId() == 0) {
				json = this.setJson(false, null, null);
			} else {
				// 加入在线用户缓存
				String uuid = CacheConstans.USER_LOGIN_INFO + user.getUserId();
				RedisCacheUtil.set(uuid ,gson.toJson(user));
				//EHCacheUtil.set("onlineuser:" + user.getUserId(),gson.toJson(user));
				json = this.setJson(true, null, user);
			}
		} catch (Exception e) {
			this.setAjaxException(json);
			logger.error("getLoginUser()---error", e);
		}
		return json;
	}
	/**
	 * 通过token获取登录用户
	 */
	@RequestMapping("/getloginUserByToken/{token}")
	@ResponseBody
	public Map<String, Object> getloginUserByToken(HttpServletRequest request, HttpServletResponse response, @PathVariable String token) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
//			User user = SingletonLoginUtils.getLoginUser(request);
			if (token == null || token.equals("")) {
				json.put("authentication", false);
				json.put("success", false);
				json.put("message", "认证失败");
			} else {
				// 加入在线用户缓存
				String rname_uuid = RedisCacheUtil.get(token);
				String rname = rname_uuid.split(":")[0];
				String uuid = rname_uuid.split(":")[1];
				if(uuid==null|| uuid.equals("")){
					json.put("authentication", false);
					json.put("success", false);
					json.put("message", "登录超时");
				}else{
					WebUtils.setCookie(response, CacheConstans.USER_LOGIN_INFO, uuid,1);
//					User user = gson.fromJson(RedisCacheUtil.get(uuid),User.class);
//					json.put("authentication", true);
//					json.put("success", true);
//					json.put("message", "成功");
					response.sendRedirect(CommonConstants.contextPath+"/#/teacherChat/"+rname);
				}
			}
		} catch (Exception e) {
			this.setAjaxException(json);
			logger.error("getLoginUser()---error", e);
		}
		return json;
	}

	/**
	 * 登录检测用户
	 */
	@RequestMapping("/checkUserExist")
	@ResponseBody
	public Map<String, Object> checkUserExist(HttpServletRequest request,
											  HttpServletResponse response) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			String account = request.getParameter("account");
			User user = userService.queryUserByEmailOrMobile(account);
			if (user != null) {
				json = this.setJson(true, "用户已存在", "true");
			} else {
				json = this.setJson(false, "用户不存在", null);
			}
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("queryUserById()--eror", e);
		}
		return json;
	}
	/**
	 * 获取在线用户信息
	 */
	@RequestMapping("/getUserById/{uid}")
	@ResponseBody
	public Map<String, Object> getUserById(HttpServletRequest request, @PathVariable("uid") String uid) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			// 从用户缓存中获取用户信息
			// User user = (User)
			// gson.fromJson(EHCacheUtil.get("onlineuser:"+uid).toString(),User.class);
			String uuid = CacheConstans.USER_LOGIN_INFO + uid;
			User user = gson.fromJson(RedisCacheUtil.get(uuid), User.class);
			if(user!=null){
				if(user.getMobile()!=null&&!user.getMobile().equals("")){
					String mob = user.getMobile();
					mob = mob.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");//隐藏手机号中间4位
					user.setMobile(mob);
				}
				if (user.getShowName() == null
						|| user.getShowName().trim().equals("")) {
					user.setShowName(user.getUserName());
				}
				if (user.getShowName() == null
						|| user.getShowName().trim().equals("")) {
					user.setShowName(user.getMobile());
				}
				if (user.getShowName() == null
						|| user.getShowName().trim().equals("")) {
					user.setShowName(user.getEmail());
				}
			}
			json = this.setJson(true, "成功", user);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("couinfo()--error", e);
		}
		return json;
	}
	/**
	 * 注册前端用户
	 *
	 * @param user
	 *            学员对象
	 * @return Map<String,Object>
	 */
	@RequestMapping("/createuser")
	@ResponseBody
	public Map<String, Object> createUser(HttpServletRequest request,
										  HttpServletResponse response) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			String registerCode = request.getParameter("registerCode") == null ? ""
					: request.getParameter("registerCode");
			Object randCode = request.getSession().getAttribute(
					CommonConstants.RAND_CODE);
			if (randCode == null || !registerCode.equals(randCode.toString())) {
				json = this.setJson(false, "请输入正确的验证码", null);
				return json;
			}
			String confirmPwd = request.getParameter("confirmPwd");
			String password = request.getParameter("password");
			String mobile = request.getParameter("mobile");
			String showname = request.getParameter("showname");
			User user = new User();
			// if(user.getEmail()==null || user.getEmail().trim().length()==0 ||
			// !WebUtils.checkEmail(user.getEmail(), 50)){
			// json = this.setJson(false, "请输入正确的邮箱号", null);
			// return json;
			// }
			// if(userService.checkEmail(user.getEmail().trim())){
			// json = this.setJson(false, "该邮箱号已被使用", null);
			// return json;
			// }
			if (mobile == null || mobile.trim().length() == 0
					|| !WebUtils.checkMobile(mobile)) {
				json = this.setJson(false, "请输入正确的手机号", null);
				return json;
			}
			user.setMobile(mobile);
			if (userService.checkMobile(mobile)) {
				json = this.setJson(false, "该手机号已被使用", null);
				return json;
			}
			if (password == null || password.trim().length() == 0
					|| !WebUtils.isPasswordAvailable(password)) {
				json = this.setJson(false, "密码有字母和数字组合且≥6位≤16位", null);
				return json;
			}
			if (!password.equals(confirmPwd)) {
				json = this.setJson(false, "两次密码不一致", null);
				return json;
			}
			user.setCreateTime(new Date());
			user.setIsavalible(1);
			user.setPassword(MD5.getMD5(password));
			user.setMsgNum(0);
			user.setSysMsgNum(0);
			user.setLastSystemTime(new Date());
			user.setShowName(showname);
			user.setType(1);
//			user.setRoleId(5);
			userService.createUser(user);
			request.getSession().removeAttribute(CommonConstants.RAND_CODE);
			//设置登录类型为local
			request.getSession().setAttribute("AUTH_TYPE","local");
			json = this.setJson(true, "注册成功", null);
			// 注册时发送系统消息
			// Map<String, Object> websitemap =
			// websiteProfileService.getWebsiteProfileByType(WebSiteProfileType.web.toString());
			// Map<String, Object> web = (Map<String, Object>)
			// websitemap.get("web");
			// String company = web.get("company").toString();
			// String conent = "欢迎来到" + company + ",希望您能够快乐的学习";
			// msgReceiveService.addSystemMessageByCusId(conent,
			// Long.valueOf(user.getUserId()));
			String uuid = CacheConstans.USER_LOGIN_INFO + user.getUserId();
			// 缓存用户key
			WebUtils.setCookie(response, CacheConstans.USER_LOGIN_INFO,	uuid, 1);
			// 当前时间戳
			Long currentTimestamp = System.currentTimeMillis();
			// 缓存用户的登录时间
//			RedisCacheUtil.set(
//					CacheConstans.USER_CURRENT_LOGINTIME + user.getUserId(),
//					currentTimestamp.toString(), CacheConstans.USER_TIME);
			// 缓存用户
			// userService.setLoginInfo(request,user.getUserId(),"false");
			// User user =this.queryUserById(userId);
			// 用户密码不能让别人看到
			user.setPassword("");
			// 用户cookie key
			// String uuid = WebUtils.getCookie(request,
			// CacheConstans.WEB_USER_LOGIN_PREFIX);
			// 缓存用户的登录时间
			user.setLoginTimeStamp(currentTimestamp);
			RedisCacheUtil.set(uuid, gson.toJson(user), 86400);// 默认缓存1天
			// 设置登录日志
			UserLoginLog loginLog = new UserLoginLog();
			loginLog.setIp(WebUtils.getIpAddr(request));
			loginLog.setLoginTime(new Date());
			String userAgent = WebUtils.getUserAgent(request);
			if (StringUtils.isNotEmpty(userAgent)) {
				loginLog.setOsName(userAgent.split(";")[1]);
				loginLog.setUserAgent(userAgent.split(";")[0]);
			}
			loginLog.setUserId(user.getUserId());
			userLoginLogService.createLoginLog(loginLog);
			// 注册送100金币
			UserWallet uw = new UserWallet();
			uw.setUid(user.getUserId() + "");
			uw.setAmt(100);
			uw.setCreatedDate(new Date());
			userWalletService.addUserWallet(uw);
		} catch (Exception e) {
			logger.error("createUser()--eror", e);
		}
		return json;
	}
	/**
	 * 第三方登录
	 *
	 * @param request
	 * @return String
	 */
	@RequestMapping("/thirdLogin")
	@ResponseBody
	public Map<String, Object> thirdLogin(HttpServletRequest request) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			String curUrl = request.getParameter("cur_url");
			String type = request.getParameter("type");
			String state = StringUtils.createUUID();
			String nonce = StringUtils.createUUID();
			HttpSession session = request.getSession();
			session.setAttribute("STATE", state);
			session.setAttribute("NONCE", nonce);
			session.setAttribute("CURRENT_URL", curUrl);
			session.setAttribute("AUTH_TYPE", type);
			String authUrl = CommonConstants.propertyUtil.getProperty("thirdAuth."+type+".AuthUrl");
			String appKey = CommonConstants.propertyUtil.getProperty("thirdAuth."+type+".AppKey");
			String callbackUrl = CommonConstants.contextPath+"/webapp/auth/userlogin";
			String url = authUrl+"?client_id="+appKey+"&response_type=code&scope=openid offline_access&redirect_uri="+callbackUrl+"&state="+state+"&nonce="+nonce;
			json = this.setJson(true, "成功", url);
		} catch (Exception e) {
			this.setAjaxException(json);
			logger.error("thirdLogin()--error", e);
		}
		return json;
	}
}