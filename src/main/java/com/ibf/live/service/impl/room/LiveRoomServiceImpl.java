package com.ibf.live.service.impl.room;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.dao.room.LiveRoomDao;
import com.ibf.live.entity.room.LiveRoom;
import com.ibf.live.entity.room.LiveRoomCo;
import com.ibf.live.entity.room.QueryRoom;
import com.ibf.live.service.room.LiveRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 
 * @author www.ibeifeng.com
 */

@Service("liveRoomServiceService")
public class LiveRoomServiceImpl implements LiveRoomService {

	@Autowired
	private LiveRoomDao liveRoomDao;
	
	/**
	 * 获取直播间列表
	 * @return 返回房间列表
	 */
	public List<LiveRoomCo> getRoomList(QueryRoom queryRoom){
		return liveRoomDao.getRoomList(queryRoom);
	}
	
	public int getCountBySql(String sql){
		return liveRoomDao.getCountBySql(sql);
	}
	
	/**
	 * 通过sql获取结果
	 * @return List<LinkedHashMap<String, Object>>
	 */
	public List<LinkedHashMap<String, Object>> getResultBySql(String sql){
		return liveRoomDao.getResultBySql(sql);
	}
	
	/**
	 * 通过房间号获取房间
	 * @return 返回房间信息
	 */
	public LiveRoom getRoomById(int roomid){
		return liveRoomDao.getRoomById(roomid);
	}
	public LiveRoom getRoomByName(String roomname){
		return liveRoomDao.getRoomByName(roomname);
	}
	
	public List<LiveRoomCo> queryRoomListPage(QueryRoom queryRoom,PageEntity page){
		return liveRoomDao.queryRoomListPage(queryRoom,page);
	}
	public void deleteRoom(int roomId){
		 liveRoomDao.deleteRoom(roomId);
	}
	public int addLiveRoom(LiveRoom room){
		 return liveRoomDao.addLiveRoom(room);
	}
	public void updateRoom(LiveRoom room){
		liveRoomDao.updateRoom(room);
	}
}
