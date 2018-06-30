package com.ibf.live.entity.room;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class QueryRoom implements Serializable{
		private static final long serialVersionUID = 1L;
		private int userId;
        private int courseId;
        private int broadcasting;
        private int isIndex;
        private int count;
}
