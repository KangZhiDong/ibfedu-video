package com.ibf.live.dao.course;


import org.apache.ibatis.annotations.Mapper;

/**
 * CourseTeacher管理接口
 * @author www.inxedu.com
 */
@Mapper
public interface CourseUserDao {

    /**
     * 添加课程与讲师的关联数
     */
    public void addCourseUser(String value);
    
    /**
     * 删除课程与讲师的关联数据
     * @param courseId 课程ID
     */
    public void deleteCourseUser(int courseId);
    
    
}