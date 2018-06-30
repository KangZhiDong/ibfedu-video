package com.ibf.live.common.constants;

import com.ibf.live.common.util.PropertyUtil;

/**
 * 常量
 * @author www.inxedu.com
 */
public class CommonConstants {

	public static String propertyFile = "project";
	public static PropertyUtil propertyUtil = PropertyUtil.getInstance(propertyFile);
	public static String contextPath = propertyUtil.getProperty("contextPath");
	public static String staticServer = propertyUtil.getProperty("contextPath");
	public static String uploadImageServer = propertyUtil.getProperty("contextPath");
	public static String staticImage = propertyUtil.getProperty("contextPath");
	public static String socketPath = propertyUtil.getProperty("socketPath");
//	public static String authStringPrefix = propertyUtil.getProperty("accessKey");
	public static String bceSecretKey = propertyUtil.getProperty("bceSecretKey");
	public static String bceAccessKey = propertyUtil.getProperty("bceAccessKey");
	public static String bceVodSigningKey = propertyUtil.getProperty("bceVodSigningKey");
	public static String bceVodUserId = propertyUtil.getProperty("bceVodUserId");
	public static String aliCdnKey = propertyUtil.getProperty("aliCdnKey");
	public static String aliVodCdnDomian = propertyUtil.getProperty("aliVodCdnDomian");
	public static String projectName = propertyUtil.getProperty("projectName");
	public static final String MYDOMAIN = propertyUtil.getProperty("mydomain");
	public static final String authorizationKey = propertyUtil.getProperty("authorizationKey");
	public static final String publicKey = propertyUtil.getProperty("publicKey");
	

	/** 邮箱正则表达式 */
	public static String emailRegex = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	/** 电话号码正则表达式 */
	public static String telRegex = "^1[0-9]{10}$";
	/** 后台用户登录名正则表达式 */
	public static String loginRegex = "^(?=.*[a-zA-Z])[a-zA-Z0-9_]{6,20}$";
	/** 图片验证码Session的K */
	public static final String RAND_CODE = "COMMON_RAND_CODE";
}
