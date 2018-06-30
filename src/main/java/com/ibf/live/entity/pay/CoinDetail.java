package com.ibf.live.entity.pay;

import lombok.Data;

import java.io.Serializable;

/**
 * @author www.ibeifeng.com
 *
 */
@Data
public class CoinDetail implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;/*主键ID*/
	private String type;/*类型：expand*/
	private String action;/*动作：sendgift*/
	private String uid;/*发送者id*/
	private String touid;/*接收者id*/
	private int giftid;/*礼物id*/
	private String giftName;/*礼物名称*/
	private int giftcount;/*礼物数量*/
	private String content;/*内容*/
	private String objectIcon;/*物体的图片*/
	private int coin;/*总金额*/
	private int showId;/*显示id*/
	private int addtime;/*添加时间*/
	
	private String startDate;/*开始时间*/
	private String endDate;/*结束时间*/
	private String sort;/**排序列*/
	private int isDesc;/**升降序0升序 1降序*/
	
}
