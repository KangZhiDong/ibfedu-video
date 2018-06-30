package com.ibf.live.service.pay;


import com.ibf.live.entity.pay.UserWallet;

public interface UserWalletService {

	/**
	 * 添加用户钱包
	 */
	public Long addUserWallet(UserWallet uw);
	
	/**
	 * 获取用户钱包
	 */
	public UserWallet getUserWallet(String uid);
}
