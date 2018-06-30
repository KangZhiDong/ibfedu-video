package com.ibf.live.service.impl.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibf.live.common.constants.CacheConstans;
import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.common.exception.BaseException;
import com.ibf.live.common.redis.RedisCacheUtil;
import com.ibf.live.common.util.MD5;
import com.ibf.live.common.util.ObjectUtils;
import com.ibf.live.common.util.StringUtils;
import com.ibf.live.common.util.WebUtils;
import com.ibf.live.dao.user.UserDao;
import com.ibf.live.entity.user.QueryUser;
import com.ibf.live.entity.user.User;
import com.ibf.live.service.user.UserService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 用户
 * @author www.inxedu.com
 *
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	
	@Autowired
	private UserDao userDao;


	public int createUser(User user) {
		return userDao.createUser(user);
	}

	
	public User queryUserById(int userId) {
		return userDao.queryUserById(userId);
	}

	public boolean checkUserName(String userName) {
		int count = userDao.checkUserName(userName);
		if(count>0){
			return true;
		}
		return false;
	}
	
	public boolean checkMobile(String mobile) {
		int count = userDao.checkMobile(mobile);
		if(count>0){
			return true;
		}
		return false;
	}

	
	public boolean checkEmail(String email) {
		int count = userDao.checkEmail(email);
		if(count>0){
			return true;
		}
		return false;
	}
	public boolean checkUuid(String uuid){
		int count = userDao.checkUuid(uuid);
		if(count>0){
			return true;
		}
		return false;
	}
	
	public User getLoginUser(String account,String password) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("account", account);
		map.put("password", password);
		return userDao.getLoginUser(map);
	}

	
	public void updateUserPwd(User user) {
		userDao.updateUserPwd(user);
		
	}

	
	public List<User> queryUserListPage(QueryUser query, PageEntity page) {
		return userDao.queryUserListPage(query, page);
	}

	
	public void updateUserStates(User user) {
		userDao.updateUserStates(user);
		
	}

	
	public void updateUser(User user) {
		userDao.updateUser(user);
	}
	public void updateUserByUUID(User user){
		userDao.updateUserByUUID(user);
	}
	
	public void updateImg(User user) {
		userDao.updateImg(user);
	}

	
	public int queryAllUserCount() {
		return userDao.queryAllUserCount();
	}

	
	public User queryUserByEmailOrMobile(String emailOrMobile) {
		return userDao.queryUserByEmailOrMobile(emailOrMobile);
	}


	public Map<String, User> queryCustomerInCusIds(List<Long> cusIds) throws Exception {
		if(ObjectUtils.isNotNull(cusIds)){
            Map<String, User> map = new HashMap<String, User>();
            // 通过传入的cusIds 查询customer
            List<User> queryCustomerList = userDao.queryUsersByIds(cusIds);
            // 整理数据放回map
            if (ObjectUtils.isNotNull(queryCustomerList)) {
                for (User queryCustomer : queryCustomerList) {
                    map.put(String.valueOf(queryCustomer.getUserId()), queryCustomer);
                }
            }
            return map;
        }else{
            return null;
        }
	}


	public Map<String, User> getUserExpandByUids(String uids) throws Exception {
		String [] arrays=uids.split(",");
        List<Long> list = new ArrayList<Long>();
        for(String lo:arrays){
            if(StringUtils.isNotEmpty(lo)&&!"null".equals(lo)){
                list.add(Long.valueOf(lo));
            }
        }
        return queryCustomerInCusIds(list);
	}


	public void updateUnReadMsgNumAddOne(String falg, Long cusId) {
		userDao.updateUnReadMsgNumAddOne(falg, cusId);
	}

	public void updateUnReadMsgNumReset(String falg, Long cusId) {
		userDao.updateUnReadMsgNumReset(falg, cusId);
	}


	public void updateCusForLST(Long cusId, Date date) {
		userDao.updateCusForLST(cusId, date);
	}


	public String updateImportExcel(HttpServletRequest request, MultipartFile myFile, Integer mark) throws Exception {
		String msg="";
 		HSSFWorkbook wookbook = new HSSFWorkbook(myFile.getInputStream());
		HSSFSheet sheet = wookbook.getSheet("Sheet1");
		
		int rows = sheet.getLastRowNum();// 指的行数，一共有多少行+
		if(rows==0){
			throw new BaseException("请填写数据");
		}
		for (int i = 1; i <= rows+1; i++) {
			// 读取左上端单元格
			HSSFRow row = sheet.getRow(i);
			// 行不为空
			if (row != null) {
				// **读取cell**
				String email = getCellValue(row.getCell((short) 0));//邮箱
				String mobile = getCellValue(row.getCell((short) 1));//手机
				String pwd=getCellValue(row.getCell((short) 2));//获得密码
				//String roleId=getCellValue(row.getCell((short) 3));//获得角色ID
				String type=getCellValue(row.getCell((short) 3));//获得角色ID
				//邮箱
				if(ObjectUtils.isNull(email) || email.equals("")){
					if(mark==1){
						msg+="第" + i + "行邮箱错误<br/>";
						continue;
					}else{
						throw new BaseException("第" + i + "行邮箱错误<br/>");
					}
				}
				if(ObjectUtils.isNotNull(email) && StringUtils.isNotEmpty(email)){
					if (WebUtils.checkEmail(email, 50)==false) {
						if(mark==1){
							msg+="第"+i+"行邮箱格式错误<br/>";
							continue;
						}else{
							throw new BaseException("第"+i+"行邮箱格式错误<br/>");
						}
					}
				}
				boolean b = checkEmail(email.toLowerCase());
				if (b==true) {
					if(mark==1){
						msg+="第"+i+"行邮箱已存在<br/>";
						continue;
					}else{
						throw new BaseException("第"+i+"行邮箱已存在<br/>");
					}
				}
				//手机
				if(ObjectUtils.isNull(mobile) || mobile.equals("")){
					if(mark==1){
						msg+="第"+i+"行手机错误<br/>";
						continue;
					}else{
						throw new BaseException("第"+i+"行手机错误<br/>");
					}
				}
				if(ObjectUtils.isNotNull(mobile) && StringUtils.isNotEmpty(mobile)){
					if(!WebUtils.checkMobile(mobile)){
						if(mark==1){
							msg+="第"+i+"行手机格式错误<br/>";
							continue;
						}else{
							throw new BaseException("第"+i+"行手机格式错误<br/>");
						}
					}
				}
				boolean m = checkMobile(mobile.toLowerCase());
				if (m==true) {
					if(mark==1){
						msg+="第"+i+"行手机号已存在<br/>";
						continue;
					}else{
						throw new BaseException("第"+i+"行手机号已存在<br/>");
					}
				}
				
				//密码
				if(pwd!=null && !pwd.trim().equals("")){
					if(pwd.length()<6 || pwd.length()>20||!WebUtils.isPasswordAvailable(pwd)){
						if(mark==1){
							msg+="第"+i+"行 密码错误，密码可由“_”，数字，英文大于等6位小于等于16位<br/>";
							continue;
						}else{
							throw new BaseException("第"+i+"行 密码错误，密码可由“_”，数字，英文大于等6位小于等于16位<br/>");
						}
					}
				}else{
					pwd="111111";
				}
				//角色
				/*if(ObjectUtils.isNotNull(roleId) && !roleId.equals("")){
					if(sysRoleDao.querySysRoleById(roleId)==null||sysRoleDao.querySysRoleById(roleId).getRoleId()<=0){
						if(mark==1){
							msg+="第"+i+"行密码错误<br/>";
							continue;
						}else{
							throw new BaseException("第"+i+"行密码错误<br/>");
						}
					}
				}else{
					roleId="5";//默认为普通用户
				}*/
				if(ObjectUtils.isNull(type) || type.equals("")){
					type="1";
				}
				if(!"1".equals(type)&&!"4".equals(type)){
					if(mark==1){
						msg+="第"+i+"行类型错误<br/>";
						continue;
					}else{
						throw new BaseException("第"+i+"行类型错误<br/>");
					}
				}	
				User user = new User();
				user.setEmail(email);//用户学员邮箱
				user.setMobile(mobile);//用户学员手机
				user.setPassword(MD5.getMD5(pwd));//用户学员密码
				user.setCreateTime(new Date());//注册时间
				user.setLastSystemTime(new Date());//上传统计系统消息时间
				user.setCreateTime(new Date());
				user.setIsavalible(1);
				user.setType(Integer.parseInt(type));
				//user.setRoleId(Integer.parseInt(roleId));
				user.setSex(1);
				userDao.createUser(user);//添加学员
			}
		}
		return msg;
	}
	
	/**
	 * 获得Hsscell内容
	 * 
	 * @param cell
	 * @return
	 */
	public String getCellValue(HSSFCell cell) {
		String value = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_FORMULA:
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				DecimalFormat df = new DecimalFormat("0");    
				value = df.format(cell.getNumericCellValue());
				break;
			case HSSFCell.CELL_TYPE_STRING:
				value = cell.getStringCellValue().trim();
				break;
			default:
				value = "";
				break;
			}
		}
		return value.trim();
	}

	/**
	 * 缓存用户信息
	 */
	public void setLoginInfo(HttpServletRequest request, int userId, String autoThirty){
		User user =this.queryUserById(userId);
		//用户密码不能让别人看到
		user.setPassword("");
		//用户cookie key
		String uuid = WebUtils.getCookie(request, CacheConstans.USER_LOGIN_INFO);
		//缓存用户的登录时间
		//user.setLoginTimeStamp(Long.parseLong( EHCacheUtil.get(CacheConstans.USER_CURRENT_LOGINTIME+user.getUserId()).toString()));
		//user.setLoginTimeStamp(Long.parseLong( RedisCacheUtil.get(CacheConstans.USER_CURRENT_LOGINTIME+user.getUserId()).toString()));
		if(autoThirty!=null&&autoThirty.equals("true")){//自动登录
			//EHCacheUtil.set(uuid, user, CacheConstans.USER_TIME);
			RedisCacheUtil.set(uuid, gson.toJson(user), CacheConstans.USER_TIME);
		}else{
		  //	EHCacheUtil.set(uuid, user, 86400);
			RedisCacheUtil.set(uuid, gson.toJson(user), 86400);
		}
	}
	
     /***
      * 修改用户登录最后登录时间和IP
      */
	public void updateUserLoginLog(int userId, Date time, String ip) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("time", time);
		map.put("ip", ip);
		userDao.updateUserLoginLog(map);
	}

     /**
      * 后端用户登录 (登录名或手机或邮箱均可登录)
      * @param user
	  * @return
      */
	public User queryLoginUser(User user) {
		return userDao.queryLoginUser(user);
	}

	/**
	 * 查询用户密码
	 * @param Id
	 * @return
	 */
	public User queryUserPassWordById(int Id) {
		return userDao.queryUserPassWordById(Id);
	}


	/***
	 * 用户列表
	 */
	public List<User> queryUserList(QueryUser query) {
		return userDao.queryUserList(query);
	}

	  /**
     * 查询课程讲师列表
     * @param courseId
     * @return
     */
	public List<Map<String, Object>> queryCourseTeacerList(int courseId) {
		return userDao.queryCourseTeacerList(courseId);
	}
	
	public User getLoginUserByUuid(String uuid){
		return userDao.getLoginUserByUuid(uuid);
	}
}
