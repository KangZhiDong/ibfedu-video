package com.ibf.live.entity.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 * @author www.inxedu.com
 */
@Data
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int userId;/**用户ID*/
	
	private int roleId;/**角色ID*/
	
	private String roleName;/**角色名称*/
	
	private String userName;/**用户名*/
	
	private String mobile;/**手机号*/
	
	private String email;/**邮箱*/
	
	private String password;/**密码*/
	
	private String showName;/**显示名（昵称）*/
	
	private int sex;/**性别 1男2女*/
	
	private int age;/**年龄*/
	
	private String picImg;/**用户头像*/
	
	private Date birthday;/**出生日期*/
	
	private String qq; /**qq号*/
	
	private String qqOpenid; /**qq openid*/
	
	private String weibo; /**微博*/
	
	private String weiboOpenid; /**weibo openid*/
	
	private String wechat; /**微信*/
	
	private String wechatOpenid; /**wechat openid*/
	
	private String bannerUrl;/**个人中心个性图片URL*/
	
    private int msgNum;/** 站内信未读消息数*/
	
	private int sysMsgNum;/**系统自动消息未读消息数*/
	
	private Date createTime;/**注册时间*/
	
	private int isavalible;/**状态 0删除1正常2冻结*/
	
	private Date lastSystemTime;/**上传统计系统消息时间*/
   
	private long loginTimeStamp;/**登录时的当前时间戳*/
	
	private int vip; /**用户vip等级*/
	
	private Date lastLoginTime;/**最后登录时间*/
	
	private String lastLoginIp;/**最后登录IP号*/
	
	private String isImport; /**0:不是导入的1:是到导入的*/
	
	private String birthdayTime;
	
    private String education; /** 资历*/
    
    private String career; /** 简介*/ 
    
    private int type; /**用户类型 1普通用户4内部员工*/
    
    private String uuid; /**tpcs平台uuid*/
}
