package com.ibf.live.service.impl.pay;

import com.ibf.live.dao.pay.UserWalletDao;
import com.ibf.live.entity.pay.UserWallet;
import com.ibf.live.service.pay.UserWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userWalletService")
public class UserWalletServiceImpl implements UserWalletService {
	
	@Autowired
	private UserWalletDao userWalletDao;
	
	@Override
	public Long addUserWallet(UserWallet uw){
		UserWallet userWallet = userWalletDao.getUserWallet(uw.getUid());
		if(userWallet==null){
			return userWalletDao.addUserWallet(uw);
		}
		else{
			return (long) 0;
		}
	}
	
	public UserWallet getUserWallet(String uid){
		return userWalletDao.getUserWallet(uid);
	}
}
