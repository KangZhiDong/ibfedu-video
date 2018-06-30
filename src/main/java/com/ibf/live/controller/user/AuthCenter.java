package com.ibf.live.controller.user;

import com.alibaba.fastjson.JSON;
import com.ibf.live.common.constants.CacheConstans;
import com.ibf.live.common.constants.CommonConstants;
import com.ibf.live.common.controller.BaseController;
import com.ibf.live.common.redis.RedisCacheUtil;
import com.ibf.live.common.util.HttpUtil;
import com.ibf.live.common.util.MD5;
import com.ibf.live.common.util.StringUtils;
import com.ibf.live.common.util.WebUtils;
import com.ibf.live.entity.pay.UserWallet;
import com.ibf.live.entity.room.LiveRoomCo;
import com.ibf.live.entity.room.QueryRoom;
import com.ibf.live.entity.user.User;
import com.ibf.live.entity.user.UserLoginLog;
import com.ibf.live.service.pay.UserWalletService;
import com.ibf.live.service.room.LiveRoomService;
import com.ibf.live.service.user.UserLoginLogService;
import com.ibf.live.service.user.UserService;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/webapp")
public class AuthCenter extends BaseController {
	
	//private static Logger logger = Logger.getLogger(AuthCenter.class);
	private static Logger logger = LoggerFactory.getLogger(AuthCenter.class);

	@Autowired
	private UserService userService;
	@Autowired
	private UserLoginLogService userLoginLogService;
	@Autowired
	private UserWalletService userWalletService;
	@Autowired
	private LiveRoomService roomService;

	/**
	 * 第三方登录回调验证
	 * 
	 * @param request
	 * @return String
	 */
	@RequestMapping("/auth/userlogin")
	@ResponseBody
	public Map<String, Object> userlogin(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			String code = request.getParameter("code");
			String state = request.getParameter("state");
			HttpSession session = request.getSession();
			if(code==null||code.equals("")||!state.equals(session.getAttribute("STATE").toString())){
				 json = this.setJson(false, "state验证出错", null);
			}else{
				//验证用户
				Map<String,String> headers = new HashMap<String, String>();
				String type = session.getAttribute("AUTH_TYPE").toString();
				String appKey = CommonConstants.propertyUtil.getProperty("thirdAuth."+type+".AppKey");
				String appPwd = CommonConstants.propertyUtil.getProperty("thirdAuth."+type+".AppPassword");
				String tokenUrl = CommonConstants.propertyUtil.getProperty("thirdAuth."+type+".TokenUrl");
				String getUserInfoUrl = CommonConstants.propertyUtil.getProperty("thirdAuth."+type+".UserUrl");
				headers.put("Authorization", "Basic "+Base64.getEncoder().encodeToString((appKey+":"+appPwd).getBytes()));
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				Map<String,String> params = new HashMap<String, String>();
				params.put("code",code);
				params.put("grant_type","authorization_code");
				params.put("redirect_uri", CommonConstants.contextPath+"/webapp/auth/userlogin");
				String tokenStr = HttpUtil.doPostHeader(tokenUrl,headers, params);
				if(tokenStr==null||tokenStr.equals("")){
					 json = this.setJson(false, "获取用户token失败", null);
				}else{
					Map token =   (Map)JSON.parse(tokenStr);
					String payload  = token.get("id_token").toString().split("\\.")[1];
					//对特殊字符做处理
					payload = payload.replace("-", "+");
					payload = payload.replace("_", "/");
					switch(payload.length()%4){
						case 2:
							payload+="==";
							break;
						case 3:
							payload+="=";
							break;
					}
					//String playload = new String(Base64.getDecoder().decode(token.get("id_token").toString().split("\\.")[1]),"utf-8");
					payload =  new String(Base64.getDecoder().decode(payload),"utf-8");
					Map jwt = (Map)JSON.parse(payload);
					String nonce = jwt.get("nonce").toString();
					String userName =  jwt.get("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name").toString(); 
					if(nonce!=null && nonce.equals(session.getAttribute("NONCE").toString())){
						session.setAttribute("ID_TOKEN", token.get("id_token").toString());
						//通过access_token获取用户数据
						Map<String,String> data = new HashMap<String, String>();
						data.put("access_token", token.get("access_token").toString());
						String userData = HttpUtil.doPost(getUserInfoUrl, data);
						 Map maps = (Map)JSON.parse(userData);  
						 User user = new User();
						 String bfUuid =  maps.get("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier").toString();
						 if(!userService.checkUuid(bfUuid)){
							 user.setType(1);//设置为普通用户
//							 user.setRoleId(roleId);默认为空
							 user.setUserName(userName);
							 user.setUuid(bfUuid);
							 user.setShowName(maps.get("displayname").toString());
							 if(user.getShowName()==null||"".equals(user.getShowName())){
								 user.setShowName(maps.get("nicktemp").toString());
							 }
							 user.setQq(maps.get("qq").toString());
							 user.setMobile(maps.get("phonenumber").toString());
							 user.setEmail(maps.get("emailtemp").toString());
							 user.setPicImg(CommonConstants.propertyUtil.getProperty("thirdAuth."+type+".FileUrl")+maps.get("photopath").toString());
							 userService.createUser(user);
							 
							// 注册送100金币
							UserWallet uw = new UserWallet();
							uw.setUid(user.getUserId() + "");
							uw.setAmt(100);
							uw.setCreatedDate(new Date());
							uw.setUpdatedDate(new Date());
							userWalletService.addUserWallet(uw);
						 }else{//如果存在则更新user
							 user = userService.getLoginUserByUuid(bfUuid);
							 //user.setType(1);//设置为普通用户
//							 user.setRoleId(roleId);默认为空
							 user.setUserName(userName);
							 user.setShowName(maps.get("displayname").toString());
							 if(user.getShowName()==null||"".equals(user.getShowName())){
								 user.setShowName(maps.get("nicktemp").toString());
							 }
							 user.setQq(maps.get("qq").toString());
							 user.setMobile(maps.get("phonenumber").toString());
							 user.setEmail(maps.get("emailtemp").toString());
							 user.setPicImg(CommonConstants.propertyUtil.getProperty("thirdAuth."+type+".FileUrl")+maps.get("photopath").toString());
							 userService.updateUser(user);
						 }
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
						WebUtils.setCookie(response, CacheConstans.USER_LOGIN_INFO,uuid, 1);

						// 当前时间戳
						Long currentTimestamp = System.currentTimeMillis();
						// 缓存用户的登录时间
//						RedisCacheUtil.set(
//								CacheConstans.USER_CURRENT_LOGINTIME + user.getUserId(),
//								currentTimestamp.toString(), CacheConstans.USER_TIME);
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
						 response.sendRedirect(session.getAttribute("CURRENT_URL").toString());
					}else{
						 json = this.setJson(false, "nonce验证出错", null);
					}
				}
			}
		} catch (Exception e) {
			this.setAjaxException(json);
			logger.error("userlogin()--error", e);
		}
		return json;
	}
	
	/**
	 * obs直播间登录
	 */
	@RequestMapping("/room/login/{username}/{pwd}")
	@ResponseBody
	public Map roomLogin(HttpServletRequest request,
                         @PathVariable String username, @PathVariable String pwd) {
		Map json = new HashMap();
		try {
			if (username == null || username.trim().equals("")) {
				json.put("Code", 0);
				json.put("Msg", "用户名不能为空");
				return json;
			}
			if (pwd == null || pwd.trim().equals("")) {
				json.put("Code", 0);
				json.put("Msg", "密码不能为空");
				return json;
			}
			User su = new User();
			su.setUserName(username);
			su.setPassword(MD5.getMD5(pwd));
			User user = userService.queryLoginUser(su);
			if (user == null) {
				json.put("Code", 0);
				json.put("Msg", "用户名或密码错误！");
				return json;
			}
			// 判断用户是否是可用状态
			if (user.getIsavalible() != 1) {
				json.put("Code", 0);
				json.put("Msg", "该用户已经冻结！");
				return json;
			}
			QueryRoom qr = new QueryRoom();
			qr.setUserId(user.getUserId());
			List<LiveRoomCo> rlist = roomService.getRoomList(qr);
			if (rlist == null || rlist.size() == 0) {
				json.put("Code", 0);
				json.put("Msg", "该用户没有开直播间");
				return json;
			}
			LiveRoomCo room = rlist.get(0);
			
			//登录成功写入token
			String uuid = CacheConstans.USER_LOGIN_INFO	+ user.getUserId();
			RedisCacheUtil.set(uuid, gson.toJson(user),
					86400);
			String token = StringUtils.createUUID().replace("-", "");
			RedisCacheUtil.set(token, room.getRoomName()+":"+uuid,2*60);//缓存两分钟
			Map<String, String> result = new HashMap<String, String>();
			result.put("live_ur", CommonConstants.contextPath+"/api/user/getloginUserByToken/"+token);
			
			String stream = room.getStreamUrl();
			if(CommonConstants.propertyUtil.getProperty("lssVersion").equals("3.0")){//lss3.0版本加
				Calendar calendar=Calendar.getInstance();   
				calendar.add(Calendar.HOUR, -6);//加2小时减8小时
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				String time = sdf.format(calendar.getTime());
				String stringToSign = stream+";"+time;
				Mac mac = Mac.getInstance("HmacSHA256");
				String signingKey =  CommonConstants.propertyUtil.getProperty("bceLssSigningKey");
		        mac.init(new SecretKeySpec(signingKey.getBytes(Charset.forName("UTF-8")), "HmacSHA256"));
		        String lsstoken = new String(Hex.encodeHex(mac.doFinal(stringToSign.getBytes(Charset.forName("UTF-8")))));
				stream+="?token="+lsstoken+"&expire="+time;
			}
			int pos = stream.lastIndexOf("/");
			result.put("stream_url_last",
					stream.substring(pos + 1, stream.length()));// rtmp://push.bcelive.com/live/spqdhvatnvygoqoexo
			result.put("stream_url_first", stream.substring(0, pos + 1));
			json.put("Code", 1);
			json.put("Result", result);
			return json;
		} catch (Exception e) {
			logger.error("couinfo()--error", e);
			json.put("Code", 0);
			json.put("Msg", "登录失败");
			return json;
		}
	}
}
