package com.ibf.live.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibf.live.common.constants.CacheConstans;
import com.ibf.live.common.constants.CommonConstants;
import com.ibf.live.common.redis.RedisCacheUtil;
import com.ibf.live.entity.user.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author www.inxedu.com
 *
 */
public class SingletonLoginUtils {

	public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	/**
	 *
	 * 获取后台登录用户ID
	 * @param request
	 * @return 返因用户ID
	 */
	public static int getLoginSysUserId(HttpServletRequest request) {
		User useObject = getLoginSysUser(request);
		if (ObjectUtils.isNotNull(useObject)) {
			return useObject.getUserId();
		} else {
			return 0;
		}
	}

	/**
	 * 获取后台登录用户
	 * @return SysUser
	 * @throws Exception
	 */
	public static User getLoginSysUser(HttpServletRequest request) {
		String userKey = WebUtils.getCookie(request, CacheConstans.USER_LOGIN_INFO);
		if (StringUtils.isNotEmpty(userKey)) {
			User sysUser = gson.fromJson(RedisCacheUtil.get(userKey),User.class);
			if (ObjectUtils.isNotNull(sysUser)) {
				return sysUser;
			}
		}
		return null;
	}

	/**
	 * 获取前台登录用户ID
	 * @param request
	 * @return 返回用户ID
	 */
	public static int getLoginUserId(HttpServletRequest request){
		User user = getLoginUser(request);
		if(user!=null){
			return user.getUserId();
		}
		return 0;
	}

	/**
	 * 获取前台登录用户
	 * @param request
	 * @return User
	 */
	public static User getLoginUser(HttpServletRequest request){
		String userKey = WebUtils.getCookie(request, CacheConstans.USER_LOGIN_INFO);
		if(StringUtils.isNotEmpty(userKey)){
			//User user = (User) EHCacheUtil.get(userKey);

			User user = gson.fromJson(RedisCacheUtil.get(userKey),User.class);
			//User user = (User) request.getSession().getAttribute(userKey);
			if(ObjectUtils.isNotNull(user)){
				return user;
			}
		}
		return null;
	}

	/***
	 * 验证用户数量
	 * @param userCount
	 * @return
	 */
	public static boolean validateUserCount(long userCount){
		boolean isTrueBoolean=false;
		try {
			byte[] encodedData= RsaCoderUtil.decryptBASE64(CommonConstants.authorizationKey); //解密
			byte[] decodedData = RsaCoderUtil.decryptByPublicKey(encodedData,CommonConstants.publicKey);
			String outputStr = new String(decodedData);
			String  aryStrings=outputStr.substring(outputStr.lastIndexOf("-")+1,outputStr.length());
			long total=Integer.parseInt(aryStrings);
			if(userCount<=total){
				isTrueBoolean=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return isTrueBoolean;
		}
	}

	//判断是否为手机浏览器
	public static boolean JudgeIsMoblie(HttpServletRequest request) {
		boolean isMoblie = false;
		String[] mobileAgents = { "iphone", "android","ipad", "phone", "mobile", "wap", "netfront", "java", "opera mobi",
				"opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry", "dopod",
				"nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma",
				"docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos",
				"techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem",
				"wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
				"pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320",
				"240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",
				"blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
				"kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
				"mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",
				"prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
				"smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",
				"voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
				"Googlebot-Mobile" };
		if (request.getHeader("User-Agent") != null) {
			String agent=request.getHeader("User-Agent");
			for (String mobileAgent : mobileAgents) {
				if (agent.toLowerCase().indexOf(mobileAgent) >= 0&&agent.toLowerCase().indexOf("windows nt")<=0 &&agent.toLowerCase().indexOf("macintosh")<=0) {
					isMoblie = true;
					break;
				}
			}
		}
		return isMoblie;
	}
}
