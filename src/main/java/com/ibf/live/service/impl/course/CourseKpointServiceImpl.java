package com.ibf.live.service.impl.course;

import com.ibf.live.dao.course.CourseKpointDao;
import com.ibf.live.entity.kpoint.CourseKpoint;
import com.ibf.live.entity.kpoint.CourseKpointDto;
import com.ibf.live.entity.kpoint.CourseKpointVideo;
import com.ibf.live.service.course.CourseKpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CourseKpoint 课程章节 管理接口
 * @author www.inxedu.com
 */
@Service("courseKpointService")
public class CourseKpointServiceImpl implements CourseKpointService {

 	@Autowired
    private CourseKpointDao courseKpointDao;
 	
    public String addCourseKpoint(CourseKpoint courseKpoint){
    	return courseKpointDao.addCourseKpoint(courseKpoint);
    }

	
	public List<CourseKpoint> queryCourseKpointByCourseId(int courseId) {
		return courseKpointDao.queryCourseKpointByCourseId(courseId);
	}

	
	public CourseKpointDto queryCourseKpointById(String kpointId) {
		return courseKpointDao.queryCourseKpointById(kpointId);
	}

	
	public void updateKpoint(CourseKpoint kpoint) {
		courseKpointDao.updateKpoint(kpoint);
	}

	
	public void deleteKpointByIds(String[] kids) {
			courseKpointDao.deleteKpointByIds(kids);
	}

	
	public void updateKpointParentId(int kpointId, int parentId) {
		Map<String,Integer> map = new HashMap<String, Integer>();
		map.put("kpointId", kpointId);
		map.put("parentId", parentId);
		courseKpointDao.updateKpointParentId(map);
		
	}


	@Override
	public int getSecondLevelKpointCount(Long courseId) {
		return courseKpointDao.getSecondLevelKpointCount(courseId);
	}
    
	/**
     * 获取课程节点视频地址
     */
	public CourseKpointVideo getCourseKpointVideoPath(String kpointId){
    	return courseKpointDao.getCourseKpointVideoPath(kpointId);
    }
}