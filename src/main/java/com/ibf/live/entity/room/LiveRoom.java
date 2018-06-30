package com.ibf.live.entity.room;

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
public class LiveRoom implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String roomName; 
	private String streamUrl; 
	private String liveUrl; //高清播放地址
	private String speedLiveUrl;//标清播放地址
	private String title;
	private String comment;
	private Date arrangeTime;
	private String notice;
	private int vip;
	private int isIndex;
	private int broadcasting;
	private int courseId;
	private int userId;
}