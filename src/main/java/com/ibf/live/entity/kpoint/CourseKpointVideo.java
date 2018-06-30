package com.ibf.live.entity.kpoint;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class CourseKpointVideo  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	  
    private String kpointId;/**视频节点ID*/
    private int courseId;/**课程ID*/
    private int videoMachine;/**视频源*/
    private String sourseId;/**视频源id*/
    private String soursePath;/**视频源地址*/
    private String videoType;/**视频格式*/
    private String videoUrl;/**视频地址*/
    private String imagePath;/**视频图片地址*/
}
