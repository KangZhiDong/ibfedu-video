package com.ibf.live.dao.pay;


import com.ibf.live.entity.pay.UserWallet;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserWalletDao {
	/**
	 * 添加用户钱包
	 */
	public Long addUserWallet(UserWallet uw);
	
	/**
	 * 获取用户钱包
	 */
	public UserWallet getUserWallet(String uid);
}
