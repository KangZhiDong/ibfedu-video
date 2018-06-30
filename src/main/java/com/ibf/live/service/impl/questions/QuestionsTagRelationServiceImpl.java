package com.ibf.live.service.impl.questions;

import com.ibf.live.dao.questions.QuestionsTagRelationDao;
import com.ibf.live.entity.questions.QuestionsTagRelation;
import com.ibf.live.service.questions.QuestionsTagRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* 问答和 问答标签的 关联表service层接口实现
* @author www.inxedu.com
*/
@Service("questionsTagRelationService")
public class QuestionsTagRelationServiceImpl implements QuestionsTagRelationService {

	@Autowired
	private QuestionsTagRelationDao questionsTagRelationDao;
	@Override
	public Long addQuestionsTagRelation(QuestionsTagRelation questionsTagRelation) {
		return questionsTagRelationDao.addQuestionsTagRelation(questionsTagRelation);
	}

	@Override
	public List<QuestionsTagRelation> queryQuestionsTagRelation(QuestionsTagRelation questionsTagRelation) {
		return questionsTagRelationDao.queryQuestionsTagRelation(questionsTagRelation);
	}

}
