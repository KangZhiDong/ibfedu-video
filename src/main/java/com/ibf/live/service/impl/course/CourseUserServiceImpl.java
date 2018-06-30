package com.ibf.live.service.impl.course;


import com.ibf.live.dao.course.CourseUserDao;
import com.ibf.live.service.course.CourseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CourseTeacher管理接口
 * @author www.inxedu.com
 */
@Service("CourseUserService")
public class CourseUserServiceImpl implements CourseUserService {

 	@Autowired
    private CourseUserDao courseUserDao;

	@Override
	public void addCourseUser(String value) {
		courseUserDao.addCourseUser(value);
		
	}

	@Override
	public void deleteCourseUser(int courseId) {
		courseUserDao.deleteCourseUser(courseId);
		
	}
}