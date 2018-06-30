package com.ibf.live.entity.course;

import com.ibf.live.entity.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @description 用户购买课程订单
 * @author www.inxedu.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CourseBuy implements Serializable {
    private static final long serialVersionUID = 5434482371608343070L;
    
    private int courseBuyId; //订单ID
    
    private Course course;//课程ID
    
    private User user;//用户ID
    
	private Date addTime; //创建时间
	
	private String keyUser;//查询
}
