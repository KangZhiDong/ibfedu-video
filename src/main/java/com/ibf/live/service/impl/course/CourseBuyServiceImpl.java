package com.ibf.live.service.impl.course;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.dao.course.CourseBuyDao;
import com.ibf.live.dao.course.CourseDao;
import com.ibf.live.entity.course.Course;
import com.ibf.live.entity.course.CourseBuy;
import com.ibf.live.service.course.CourseBuyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Course 课程service接口实现
 * @author www.inxedu.com
 */
@Service("courseBuyService")
public class CourseBuyServiceImpl implements CourseBuyService {
	
	@Autowired
	private CourseBuyDao courseBuyDao;
	@Autowired
	private CourseDao courseDao;

	public int insert(CourseBuy courseBuy) {
		return courseBuyDao.insert(courseBuy);
	}

	public List<CourseBuy> queryListPage(CourseBuy courseBuy,PageEntity page) {
		return courseBuyDao.queryListPage(courseBuy, page);
	}

   public Boolean getUserCourseCount(int userId,int courseId){
	   return courseBuyDao.getUserCourseCount(userId, courseId);
   }
   
  public void createUserBuy(int userId,int courseId){
	  Course course = courseDao.queryCourseById(courseId);
	  if(course.getCourseType()==1){//套餐课程
		  //添加购买子课程
		  String idsStr = course.getChildrenIds();
		  if(idsStr!=null&&!idsStr.equals("")){
			 String[] ids =  idsStr.split(",");
			 for(String cid:ids){
				 if(cid!=null&&!cid.equals("")){
					 courseBuyDao.createUserBuy(userId, Integer.parseInt(cid));
				 }
			 }
		  }
	  }
	  courseBuyDao.createUserBuy(userId, courseId);
  }
}