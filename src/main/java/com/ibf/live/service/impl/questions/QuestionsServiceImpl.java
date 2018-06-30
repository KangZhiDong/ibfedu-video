package com.ibf.live.service.impl.questions;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.dao.questions.QuestionsDao;
import com.ibf.live.entity.questions.Questions;
import com.ibf.live.service.questions.QuestionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * questions服务接口 实现
 *@author www.inxedu.com
 */
@Service("questionsService")
public class QuestionsServiceImpl implements QuestionsService {

	@Autowired
	private QuestionsDao questionsDao;
	
	@Override
	public Long addQuestions(Questions questions) {
		return questionsDao.addQuestions(questions);
	}

	@Override
	public Long deleteQuestionsById(Long id) {
		return questionsDao.deleteQuestionsById(id);
	}

	@Override
	public void updateQuestions(Questions questions) {
		questionsDao.updateQuestions(questions);
	}

	@Override
	public Questions getQuestionsById(Long id) {
		return questionsDao.getQuestionsById(id);
	}

	@Override
	public List<Questions> getQuestionsList(Questions questions, PageEntity page) {
		return questionsDao.getQuestionsList(questions, page);
	}

	@Override
	public List<Questions> queryQuestionsOrder(int size) {
		return questionsDao.queryQuestionsOrder(size);
	}
	
	@Override
	public int queryAllQuestionsCount() {
		return questionsDao.queryAllQuestionsCount();
	}

}
