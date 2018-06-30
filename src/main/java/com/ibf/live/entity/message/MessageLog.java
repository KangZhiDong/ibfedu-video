package com.ibf.live.entity.message;

import java.util.Date;

public class MessageLog {

    private int id;
	
	private String uid; //用户ID
	
	private String userName;//用户名
	
	private String showName; //姓名
	
    private String mobile; //手机号
	
	private String email;//邮箱
	
	private String qq;//qq
	
	private int sex;/**性别 1男2女*/
	
	private String msg; //信息
	
	private int roomid;  //房间号
	
	private String roomTitle; //房间标题
	
	private Date createTime; //日期/消息入库时间
	
	private int type; //消息类型:0:文本1:加入房间2:离开房间
	
	private int totalNum;//参课人数
	
	private String keyUser;//查询
	
	private Date leftTime;  //离开房间时间
	
	private Date joinTime; //进入房间时间
	
	private String queryRoomid;
	
	
	public String getQueryRoomid() {
		return queryRoomid;
	}
	public void setQueryRoomid(String queryRoomid) {
		this.queryRoomid = queryRoomid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getRoomid() {
		return roomid;
	}
	public void setRoomid(int roomid) {
		this.roomid = roomid;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRoomTitle() {
		return roomTitle;
	}
	public void setRoomTitle(String roomTitle) {
		this.roomTitle = roomTitle;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public String getKeyUser() {
		return keyUser;
	}
	public void setKeyUser(String keyUser) {
		this.keyUser = keyUser;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public Date getLeftTime() {
		return leftTime;
	}
	public void setLeftTime(Date leftTime) {
		this.leftTime = leftTime;
	}
	public Date getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}
	
}
