package com.ibf.live.dao.questions;


import com.ibf.live.entity.questions.QuestionsTagRelation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 问答和 问答标签的 关联表dao层接口
 * @author www.inxedu.com
 */
@Mapper
public interface QuestionsTagRelationDao {
	/**
	 * 添加
	 * @param questionsTagRelation
	 * @return
	 */
	public Long addQuestionsTagRelation(QuestionsTagRelation questionsTagRelation);
	
	/**
	 * 查询
	 */
	public List<QuestionsTagRelation> queryQuestionsTagRelation(QuestionsTagRelation questionsTagRelation);
}
