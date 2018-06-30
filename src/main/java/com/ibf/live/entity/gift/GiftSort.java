package com.ibf.live.entity.gift;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;


/**
 * @author www.ibeifeng.com
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GiftSort implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;  //ID
    private String sortName; //分类名称
    private int  orderNo;    //排序
    private Date addTime;   //创建时间
    private int delFlag=0;//删除标志。0正常 1删除
}
