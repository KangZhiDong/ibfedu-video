package com.ibf.live.service.course;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.entity.course.CourseBuy;

import java.util.List;

/**
 * Course 课程订单 管理接口
 * @author www.inxedu.com
 */
public interface CourseBuyService {
	
	/**
     * 添加Course
     */
    public int insert(CourseBuy courseBuy);
    
    /**
     * 分页查询课程订单列表
     * @param query 条件条件
     * @param page 分页条件
     * @return List<CourseOrder>
     */
    public List<CourseBuy> queryListPage(CourseBuy courseBuy, PageEntity page);

    /**
     * 查询用户购买课程记录
     * @param userId 用户id，courseId课程id
     * @return Boolean
     */
   public Boolean getUserCourseCount(int userId, int courseId);

   /**
    * 创建用户购买课程记录
    * @param userId 用户id，courseId课程id
    * @return Boolean
    */
  public void createUserBuy(int userId, int courseId);
}