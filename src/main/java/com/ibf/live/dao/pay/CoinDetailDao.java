package com.ibf.live.dao.pay;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.entity.pay.CoinDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface CoinDetailDao {
	public List<CoinDetail> queryCoinListPage(CoinDetail query, PageEntity page);
}
