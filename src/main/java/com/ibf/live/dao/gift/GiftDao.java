package com.ibf.live.dao.gift;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.entity.gift.Gift;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * @author http://www.inxedu.com
 */
@Mapper
public interface GiftDao {

	public List<Gift> queryGiftList(Gift gift, PageEntity page);

	public void createGift(Gift gift);

	public Gift queryGiftById(int id);

	public void updateGift(Gift gift);

	public void deleteGiftById(int id);

	public List<Gift> queryGiftBySortId(int id) ;
}