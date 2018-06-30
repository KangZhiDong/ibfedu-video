package com.ibf.live.entity.course;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author www.inxedu.com
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CourseUser implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
    private Integer courseId;
    private Integer userId;
}
