package com.ibf.live.dao.letter;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.entity.letter.MsgSystem;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;


/**
 * @description 站内信发件箱的Dao
 * @author www.inxedu.com
 */
@Mapper
public interface MsgSystemDao {
    /**
     * 添加系统消息
     *
     * @param msgSender
     * @return
     * @throws Exception
     */
    public Long addMsgSystem(MsgSystem msgSystem) throws Exception;

    /**
     * 查询系统消息
     *
     * @param msgSystem
     * @return
     * @throws Exception
     */
    public List<MsgSystem> queryMsgSystemList(MsgSystem msgSystem, PageEntity page) throws Exception;

    /**
     * 通过id删除系统消息
     */
    public Long delMsgSystemById(String ids) throws Exception;

    /**
     * 查询大于传入的时间的系统系统消息
     */
    public List<MsgSystem> queryMSListByLT(Date lastTime) throws Exception;

    /**
     * 更新过期的系统消息的字段为过期
     */
    public void updateMsgSystemPastTime(Date lastTime) throws Exception;
}
