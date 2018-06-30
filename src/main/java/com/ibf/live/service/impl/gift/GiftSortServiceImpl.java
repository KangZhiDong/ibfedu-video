package com.ibf.live.service.impl.gift;


import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.dao.gift.GiftSortDao;
import com.ibf.live.entity.gift.GiftSort;
import com.ibf.live.service.gift.GiftSortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author http://www.inxedu.com
 */
@Service("giftSortService")
public class GiftSortServiceImpl implements GiftSortService {
	@Autowired
    private GiftSortDao giftSortDao;

	@Override
	public List<GiftSort> queryGiftSortList(GiftSort giftSort, PageEntity page) {
		
		return giftSortDao.queryGiftSortList(giftSort,page);
	}

	@Override
	public void createGiftSort(GiftSort giftSort) {
		giftSortDao.createGiftSort(giftSort);
		
	}

	@Override
	public GiftSort queryGiftSortById(int id) {
		return giftSortDao.queryGiftSortById(id);
	}

	@Override
	public void updateGiftSort(GiftSort giftSort) {
		giftSortDao.updateGiftSort(giftSort);
	}

	@Override
	public void deleteGiftSortById(int id) {
		giftSortDao.deleteGiftSortById(id);
	}

	public List<GiftSort> list(GiftSort giftSort) {
		return	giftSortDao.list(giftSort);
	}
}
