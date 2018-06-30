package com.ibf.live.service.gift;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.entity.gift.GiftSort;

import java.util.List;

/**
 * @author www.ibeifeng.com
 * 礼物分类service接口
 */
public interface GiftSortService {

	public List<GiftSort> queryGiftSortList(GiftSort giftSort, PageEntity page);

	public void createGiftSort(GiftSort giftSort);

	public GiftSort queryGiftSortById(int id);

	public void updateGiftSort(GiftSort giftSort);

	public void deleteGiftSortById(int id);
	
	public List<GiftSort> list(GiftSort giftSort) ;
		
}
