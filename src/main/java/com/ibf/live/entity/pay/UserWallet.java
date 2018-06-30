package com.ibf.live.entity.pay;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author www.ibeifeng.com
 *
 */
@Data
public class UserWallet implements Serializable {
	private static final long serialVersionUID = 1L;
	private String uid;
	private double amt;
	private Date createdDate;
	private Date updatedDate;

}
