package com.ibf.live.service.gift;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.entity.gift.Gift;

import java.util.List;

/**
 * @author www.ibeifeng.com
 * 礼物service接口
 */
public interface GiftService {

 public	List<Gift> queryGiftList(Gift gift, PageEntity page);

 public void createGift(Gift gift);

 public Gift queryGiftById(int id);

 public void updateGift(Gift gift);

 public void deleteGiftById(int id);
	
 public List<Gift> queryGiftBySortId(int id);
}
