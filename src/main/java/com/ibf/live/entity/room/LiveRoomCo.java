package com.ibf.live.entity.room;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class LiveRoomCo extends LiveRoom implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String courseName;
	private String userName;
	private String logo;
	private long ucount;//观看人数
}