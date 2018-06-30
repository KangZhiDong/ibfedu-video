package com.ibf.live.service.room;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.entity.room.LiveRoom;
import com.ibf.live.entity.room.LiveRoomCo;
import com.ibf.live.entity.room.QueryRoom;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author www.ibeifeng.com
 * 房间service接口
 */
public interface LiveRoomService {
	/**
	 * 获取直播间列表
	 * @return 返回房间列表
	 */
	public List<LiveRoomCo> getRoomList(QueryRoom queryRoom);
	
	/**
	 * 通过房间号获取房间
	 * @return 返回房间信息
	 */
	public LiveRoom getRoomById(int roomid);
	public LiveRoom getRoomByName(String roomname);
	/**
	 * 通过sql获取count
	 * @return count
	 */
	public int getCountBySql(String sql);
	
	/**
	 * 通过sql获取结果
	 * @return  List<LinkedHashMap<String, Object>>
	 */
	public List<LinkedHashMap<String, Object>> getResultBySql(String sql);
	
	/**
	 * 通过条件获取直播间列表
	 * @return 返回房间列表
	 */
	public List<LiveRoomCo> queryRoomListPage(QueryRoom queryRoom, PageEntity page);
	
	public void deleteRoom(int roomId);
	
	 public int addLiveRoom(LiveRoom room);
	 
	public void updateRoom(LiveRoom room);
	
}
