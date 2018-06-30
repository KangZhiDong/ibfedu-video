package com.ibf.live.dao.user;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.entity.user.UserLoginLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author www.inxedu.com
 *
 */
@Mapper
public interface UserLoginLogDao {
	/**
	 * 添加登录日志
	 * @param loginLog
	 * @return 日志ID
	 */
	public int createLoginLog(UserLoginLog loginLog);
	
	/**
	 * 查询用户登录日志
	 * @param userId 用户ID
	 * @param page 分页条件
	 * @return List<SysUserLoginLog>
	 */
	public List<UserLoginLog> queryUserLogPage(int userId, PageEntity page);

}
