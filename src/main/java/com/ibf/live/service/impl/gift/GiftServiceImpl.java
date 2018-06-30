package com.ibf.live.service.impl.gift;


import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.dao.gift.GiftDao;
import com.ibf.live.entity.gift.Gift;
import com.ibf.live.service.gift.GiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author http://www.inxedu.com
 */
@Service("giftService")
public class GiftServiceImpl implements GiftService {
	@Autowired
    private GiftDao giftDao;

	@Override
	public List<Gift> queryGiftList(Gift gift, PageEntity page) {
		return giftDao.queryGiftList(gift,page);
	}

	@Override
	public void createGift(Gift gift) {
		giftDao.createGift(gift);
		
	}

	@Override
	public Gift queryGiftById(int id) {
	   return giftDao.queryGiftById(id);
	}

	@Override
	public void updateGift(Gift gift) {
		giftDao.updateGift(gift);		
	}

	@Override
	public void deleteGiftById(int id) {
		giftDao.deleteGiftById(id);
		
	}

	@Override
	public List<Gift> queryGiftBySortId(int id) {
		return giftDao.queryGiftBySortId(id);
	}
}
