package com.ibf.live.dao.room;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.entity.room.LiveRoom;
import com.ibf.live.entity.room.LiveRoomCo;
import com.ibf.live.entity.room.QueryRoom;
import org.apache.ibatis.annotations.Mapper;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * LiveRoom管理接口
 * @author www.ibeifeng.com
 */
@Mapper
public interface LiveRoomDao {
	
	/**
     * 查询LiveRoom列表
     * @return List<LiveRoom>
     */
	public List<LiveRoomCo> getRoomList(QueryRoom queryRoom);
	
	public int getCountBySql(String sql);

	/**
	 * 通过sql获取结果
	 * @return List<LinkedHashMap<String, Object>> 
	 */
	public List<LinkedHashMap<String, Object>> getResultBySql(String sql);
	
	/**
	 * 通过房间号获取房间
	 * @return 返回房间信息
	 */
	public LiveRoom getRoomById(int roomid);
	public LiveRoom getRoomByName(String roomname);
	
	public List<LiveRoomCo> queryRoomListPage(QueryRoom queryRoom, PageEntity page);
	
	public void deleteRoom(int roomId);
	
	public int addLiveRoom(LiveRoom room);
	public void updateRoom(LiveRoom room);
}
