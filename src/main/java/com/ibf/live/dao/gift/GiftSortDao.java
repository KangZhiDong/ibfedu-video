package com.ibf.live.dao.gift;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.entity.gift.GiftSort;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * @author http://www.inxedu.com
 */
@Mapper
public interface GiftSortDao {

	public List<GiftSort> queryGiftSortList(GiftSort giftSort, PageEntity page);

	public void createGiftSort(GiftSort giftSort);

	public GiftSort queryGiftSortById(int id);

	public void updateGiftSort(GiftSort giftSort);

	public void deleteGiftSortById(int id);
	
	public List<GiftSort> list(GiftSort giftSort) ;
    
}
