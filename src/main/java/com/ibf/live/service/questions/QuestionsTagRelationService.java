package com.ibf.live.service.questions;

import com.ibf.live.entity.questions.QuestionsTagRelation;

import java.util.List;

/**
* 问答和 问答标签的 关联表service层接口
* @author www.inxedu.com
*/
public interface QuestionsTagRelationService {
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
