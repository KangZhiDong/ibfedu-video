package com.ibf.live.service.impl.pay;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.dao.pay.CoinDetailDao;
import com.ibf.live.entity.pay.CoinDetail;
import com.ibf.live.service.pay.CoinDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("coinDetailService")
public class CoinDetailServiceImpl implements CoinDetailService {
	
	@Autowired
	private CoinDetailDao coinDetailDao;
	
	public List<CoinDetail> queryCoinListPage(CoinDetail query, PageEntity page){
		return coinDetailDao.queryCoinListPage(query, page);
	}

}
