package com.ibf.live.entity.user;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 查询用户
 * @author www.inxedu.com
 *
 */
@Data
public class QueryUser {
	private int isavalible;
	private String keyWord;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date beginCreateTime;//查询 开始注册时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endCreateTime;//查询 结束注册时间
	private String roleId;
	private String userName;/**用户名*/
	private int userId;/**用户ID*/
	private String mobile;/**手机号*/
	private String email;/**邮箱*/
	private String showName;
	private int sex;/**性别 1男2女*/
	private int type;/**1普通用户4内部员工*/
}
