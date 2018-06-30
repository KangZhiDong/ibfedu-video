package com.ibf.live.dao.course;


import com.ibf.live.entity.kpoint.CourseKpoint;
import com.ibf.live.entity.kpoint.CourseKpointDto;
import com.ibf.live.entity.kpoint.CourseKpointVideo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * CourseKpoint管理接口
 * @author www.inxedu.com
 */
@Mapper
public interface CourseKpointDao {
    /**
     * 添加视频节点
     */
    public String addCourseKpoint(CourseKpoint courseKpoint);
    
    /**
     * 通过课程ID，查询课程所属视频
     * @param courseId 课程ID
     * @return List<CourseKpoint>
     */
    public List<CourseKpoint> queryCourseKpointByCourseId(int courseId);
    
    /**
     * 通过ID，查询视频详情
     * @param kpointId 视频ID
     * @return CourseKpointDto
     */
    public CourseKpointDto queryCourseKpointById(String kpointId);
    
    /**
     * 修改视频节点
     * @param kpoint
     */
    public void updateKpoint(CourseKpoint kpoint);
    
    /**
     * 删除视频节点
     * @param ids ID串
     */
    public void deleteKpointByIds(String[] kids);
    
    /**
     * 修改视频节点父ID
     * @param map
     */
    public void updateKpointParentId(Map<String, Integer> map);
    
    /**
     * 获取课程的 二级视频节点总数(只支持二级)
     */
    public int getSecondLevelKpointCount(Long courseId);

    /**
     * 获取课程节点视频地址
     */
    public CourseKpointVideo getCourseKpointVideoPath(String kpointId);
}