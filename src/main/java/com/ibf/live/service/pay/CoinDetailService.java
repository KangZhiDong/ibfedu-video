package com.ibf.live.service.pay;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.entity.pay.CoinDetail;

import java.util.List;

public interface CoinDetailService {
	
	/**
	 * 获取消费记录
	 */
	public List<CoinDetail> queryCoinListPage(CoinDetail query, PageEntity page);

}
