package com.ibf.live.dao.course;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.entity.course.CourseNote;
import com.ibf.live.entity.course.QueryCourseNote;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * CourseNote管理接口
 * @author www.inxedu.com
 */
@Mapper
public interface CourseNoteDao {

	/**
	 * 添加CourseNote
	 * 
	 * @param courseNote
	 *            要添加的CourseNote
	 * @return id
	 */
	public Long addCourseNote(CourseNote courseNote);

	/**
	 * 根据id删除一个CourseNote
	 * 
	 * @param id
	 *            要删除的id
	 */
	public void deleteCourseNoteById(Long id);

	/**
	 * 修改CourseNote
	 * 
	 * @param courseNote
	 *            要修改的CourseNote
	 */
	public void updateCourseNote(CourseNote courseNote);

	/**
	 * 根据id获取单个CourseNote对象
	 * 
	 * @param id
	 *            要查询的id
	 * @return CourseNote
	 */
	public CourseNote getCourseNoteById(Long id);

	/**
	 * 根据用户id和节点id查询笔记
	 * 
	 * @return CourseNote
	 */
	public CourseNote getCourseNoteByKpointIdAndUserId(Long kpointId, Long userId);

	/**
	 * 根据条件获取CourseNote列表
	 * 
	 * @param courseNote
	 *            查询条件
	 * @return List<CourseNote>
	 */
	public List<CourseNote> getCourseNoteList(CourseNote courseNote);

	/**
	 * 查询笔记 分页
	 * 
	 * @param queryCourseNote
	 * @param page
	 * @return
	 */
	List<QueryCourseNote> getCourseNoteListPage(QueryCourseNote queryCourseNote, PageEntity page);

	/**
	 * 更新显示隐藏
	 * 
	 * @param courseNote
	 */
	public void updateCourseNoteListStatus(CourseNote courseNote);

	/**
	 * 查询 QueryCourseNote
	 * 
	 * @param id
	 * @return
	 */
	public QueryCourseNote getQueryCourseNoteById(Long id);

	/**
	 * 查询用户笔记
	 * 
	 * @param userId
	 * @param page
	 * @return
	 */
	public List<QueryCourseNote> getUserCourseNoteByUserId(Long userId, PageEntity page);
}