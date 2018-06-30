package com.ibf.live.entity.gift;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author www.ibeifeng.com 礼物 Entity
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Gift implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id; // ID
	private GiftSort giftSort; // 外键 sid 礼物分类
	private String giftName; // 礼物名称
	private int needCoin; // 价格
	private int giftStyle;
	private int giftType;  
	private String giftIcon_25; // 上传小图
	private String giftIcon;  //上传大图
	private String giftSwf; // 上传flash
	private Date addTime; //创建时间
	private String giftComment;  //物品描述
	private int delFlag=0;//删除标志。0正常 1删除
    private List<GiftSort> giftSorts=new ArrayList<GiftSort>(); //查询使用。	

}