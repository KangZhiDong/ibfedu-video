package com.ibf.live.service.impl.questions;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.dao.questions.QuestionsCommentDao;
import com.ibf.live.entity.questions.QuestionsComment;
import com.ibf.live.service.questions.QuestionsCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author www.inxedu.com
 *
 */
@Service("questionsCommentService")
public class QuestionsCommentServiceImpl implements QuestionsCommentService {

	@Autowired
	private QuestionsCommentDao questionsCommentDao;
	
	@Override
	public Long addQuestionsComment(QuestionsComment questionsComment) {
		return questionsCommentDao.addQuestionsComment(questionsComment);
		
	}

	@Override
	public Long deleteQuestionsCommentById(Long id) {
		return questionsCommentDao.deleteQuestionsCommentById(id);
	}
	
	@Override
	public QuestionsComment getQuestionsCommentById(Long id) {
		return questionsCommentDao.getQuestionsCommentById(id);
	}

	@Override
	public void updateQuestionsComment(QuestionsComment questionsComment) {
		questionsCommentDao.updateQuestionsComment(questionsComment);
	}

	@Override
	public List<QuestionsComment> getQuestionsCommentList(QuestionsComment questionsComment) {
		return questionsCommentDao.getQuestionsCommentList(questionsComment);
	}

	@Override
	public List<QuestionsComment> queryQuestionsCommentListByQuestionsId(QuestionsComment questionsComment,
			PageEntity page) {
		return questionsCommentDao.queryQuestionsCommentListByQuestionsId(questionsComment,page);
	}

	@Override
	public Long delQuestionsCommentByQuestionId(Long id) {
		return questionsCommentDao.delQuestionsCommentByQuestionId(id);
	}

	@Override
	public List<QuestionsComment> queryQuestionsCommentList(QuestionsComment questionsComment, PageEntity page) {
		return questionsCommentDao.queryQuestionsCommentList(questionsComment,page);
	}

	@Override
	public Long delQuestionsCommentByCommentId(Long commentId) {
		return questionsCommentDao.delQuestionsCommentByCommentId(commentId);
	}

}
