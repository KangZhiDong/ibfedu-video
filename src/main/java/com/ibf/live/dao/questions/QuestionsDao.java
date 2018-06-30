package com.ibf.live.dao.questions;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.entity.questions.Questions;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * questions管理接口
 * @author www.inxedu.com
 */
@Mapper
public interface QuestionsDao {

    /**
     * 添加Questions
     *
     * @param questions 要添加的Questions
     * @return id
     */
    public Long addQuestions(Questions questions);

    /**
     * 根据id删除一个Questions
     *
     * @param id 要删除的id
     */
    public Long deleteQuestionsById(Long id);

    /**
     * 修改Questions
     *
     * @param questions 要修改的Questions
     */
    public void updateQuestions(Questions questions);

    /**
     * 根据id获取单个Questions对象
     *
     * @param id 要查询的id
     * @return Questions
     */
    public Questions getQuestionsById(Long id);

    /**
     * 根据条件获取Questions列表
     *
     * @param questions 查询条件
     * @param page       分页参数
     * @return List<Questions>
     */
    public List<Questions> getQuestionsList(Questions questions, PageEntity page);

    /**
     * 最新排行
     *
     * @param size 传入显示的条数
     * @return List<Questions>
     */
    public List<Questions> queryQuestionsOrder(int size);
    
    /**
     * 所有问答数
     * @return
     */
    public int queryAllQuestionsCount();
}