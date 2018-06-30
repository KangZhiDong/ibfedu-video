package com.ibf.live.controller.room;

import com.ibf.live.common.constants.CacheConstans;
import com.ibf.live.common.constants.CommonConstants;
import com.ibf.live.common.controller.BaseController;
import com.ibf.live.common.redis.RedisCacheUtil;
import com.ibf.live.common.util.SingletonLoginUtils;
import com.ibf.live.entity.pay.UserWallet;
import com.ibf.live.entity.room.LiveRoom;
import com.ibf.live.entity.room.LiveRoomCo;
import com.ibf.live.entity.room.QueryRoom;
import com.ibf.live.entity.user.User;
import com.ibf.live.service.pay.UserWalletService;
import com.ibf.live.service.room.LiveRoomService;
import com.ibf.live.service.user.UserLoginLogService;
import com.ibf.live.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * @author www.ibeifeng.com
 */
@Controller
@RequestMapping("/api/liveRoom")
public class LiveRoomController extends BaseController {
	//private static Logger logger = Logger.getLogger(LiveRoomController.class);
	private static Logger logger = LoggerFactory.getLogger(LiveRoomController.class);
	@Autowired
	private LiveRoomService roomService;
	@Autowired
	private UserWalletService userWalletService;
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserLoginLogService userLoginLogService;


	/**
	 * 获取首页推荐的直播间
	 */
	@RequestMapping("/getIndexRooms")
	@ResponseBody
	public Map<String, Object> getIndexRooms(HttpServletRequest request) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			// 获取所有直播间列表
			QueryRoom queryRoom = new QueryRoom();
			queryRoom.setBroadcasting(1);// 获取正在直播的
			queryRoom.setIsIndex(1);// 获取首页推荐
			queryRoom.setCount(20);// 获取最多20个
			List<LiveRoomCo> rooms = roomService.getRoomList(queryRoom);
			// 获取观看人数
			// for(LiveRoomCo room:rooms){
			// long ucount =
			// RedisCacheUtil.smembers("room:userlist:"+room.getRoomName()).size();
			// room.setUcount(ucount);
			// }
			json = this.setJson(true, "成功", rooms);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("couinfo()--error", e);
		}
		return json;
	}

	/**
	 * 获取热门直播间
	 */
	@RequestMapping("/getHotRooms")
	@ResponseBody
	public Map<String, Object> getHotRooms(HttpServletRequest request) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			// 获取所有直播间列表
			QueryRoom queryRoom = new QueryRoom();
			// queryRoom.setBroadcasting(1);//获取正在直播的
			// queryRoom.setIsIndex(1);//获取首页推荐
			queryRoom.setCount(50);// 获取50个热门直播
			List<LiveRoomCo> rooms = roomService.getRoomList(queryRoom);
			// 获取观看人数
			for (LiveRoomCo room : rooms) {
				long ucount = RedisCacheUtil.smembers(
						"room:userlist:" + room.getRoomName()).size();
				room.setUcount(ucount);
			}
			rooms.sort(new Comparator<LiveRoomCo>() {
				@Override
				public int compare(LiveRoomCo x, LiveRoomCo y) {
					return (int) (y.getUcount() - x.getUcount());
				}
			});
			List<LiveRoomCo> res = new ArrayList<LiveRoomCo>();
			for (int i = 0; i < 8 && i < rooms.size(); i++) {// 取前八个
				res.add(rooms.get(i));
			}
			json = this.setJson(true, "成功", res);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("couinfo()--error", e);
		}
		return json;
	}

	/**
	 * 获取直播间列表
	 */
	@RequestMapping("/getRoomList")
	@ResponseBody
	public Map<String, Object> getRoomList(HttpServletRequest request) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			// 获取所有直播间列表
			QueryRoom queryRoom = new QueryRoom();
			queryRoom.setBroadcasting(1);// 正在直播的
			queryRoom.setCount(20);
			List<LiveRoomCo> rooms = roomService.getRoomList(queryRoom);
			// 获取观看人数
			json = this.setJson(true, "成功", rooms);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("couinfo()--error", e);
		}
		return json;
	}

	/**
	 * 获取直播间
	 */
	@RequestMapping("/getRoomInfo/{roomName}")
	@ResponseBody
	public Map<String, Object> getRoomInfo(HttpServletRequest request, @PathVariable String roomName){
		Map<String, Object> json=new HashMap<String, Object>();
		try{
				LiveRoom room = roomService.getRoomByName(roomName);
				json.put("room",  room);
				
				//获取直播间礼物列表
				List<String> gifts = RedisCacheUtil.lrange("gift:list", Long.valueOf(0), Long.valueOf(-1));
				json.put("gifts",gifts);  
				//获取直播间用户列表，禁言列表
				Set<String> u_ids = RedisCacheUtil.smembers("room:userlist:"+room.getRoomName());
				List<User> userlist = new ArrayList<User>();
				//从缓存中获取用户信息(用户缓存暂时放在jvm中)
				for(String uid:u_ids){
					String uuid = CacheConstans.USER_LOGIN_INFO+uid;
					User user =  gson.fromJson(RedisCacheUtil.get(uuid),User.class);
					if(user==null){//缓存中没有表示已经离线
						RedisCacheUtil.srem("room:userlist:"+room.getRoomName(),uid);
					}else{
						if(user.getShowName()==null||"".equals(user.getShowName().trim())){
							user.setShowName(user.getUserName());
						}
						if(user.getShowName()==null||"".equals(user.getShowName().trim())){
							user.setShowName(user.getMobile());
						}
						if(user.getShowName()==null||"".equals(user.getShowName().trim())){
							user.setShowName(user.getEmail());
						}
						userlist.add(user);
					}
				}
				Set<String> mutelist = RedisCacheUtil.smembers("room:muteuserlist:"
						+ room.getRoomName());
				//获取观看人数
				long lineCount = RedisCacheUtil.smembers("room:userlist:"+room.getRoomName()).size();
				json.put("lineCount",  lineCount);
				
				json.put("userlist", userlist);
				json.put("mutelist", mutelist);
				json.put("success", true);
				json.put("message", "成功");
			} catch (Exception e) {
				json = this.setJson(false, "异常", null);
				logger.error("couinfo()--error", e);
			}
			return json;
		}

	/**
	 * 获取直播间登录信息
	 */
	@RequestMapping("/getLoginInfo/{roomName}")
	@ResponseBody
	public Map<String, Object> getLoginInfo(HttpServletRequest request,
			@PathVariable String roomName) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			int userId = SingletonLoginUtils.getLoginUserId(request);
    		if(userId<=0){
    			json = this.setJson(false, "请登录！", null);
    			return json;
    		}
			// 获取socket
			String token = UUID.randomUUID().toString();
			// 设置redis token过去时间
			RedisCacheUtil.set("token:" + userId, token, 60);
			Map map = new HashMap();
			map.put("uid", userId);
			map.put("rid", roomName);
			map.put("token", token);
			String q = Base64.getEncoder().encodeToString(
					gson.toJson(map).getBytes());
			json.put("socket_url", CommonConstants.socketPath
					+ "/websocket/?q=" + q);

			// 获取用户北风币
			UserWallet uw = userWalletService.getUserWallet(userId + "");
			if(uw!=null){
				json.put("coinsNum", uw.getAmt());
			}
			json.put("success", true);
			json.put("message", "成功");
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("couinfo()--error", e);
		}
		return json;
	}

	
	/**
	 * 更新直播间
	 */
	@RequestMapping("/updateRoom")
	@ResponseBody
	public Map<String, Object> updateRoom(HttpServletRequest request) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			int userId = SingletonLoginUtils.getLoginUserId(request);
    		if(userId<=0){
    			json = this.setJson(false, "请登录！", null);
    			return json;
    		}
			// 更新直播间
			String roomid = request.getParameter("roomid");
			if (roomid == null || roomid.trim().equals("")) {
				json = this.setJson(false, "房间号不能为空", null);
				return json;
			}
			LiveRoom room = roomService.getRoomById(Integer.parseInt(roomid));
			String notice = request.getParameter("notice");
			if (notice != null &&!notice.trim().equals("")) {
				room.setNotice(notice);
			}
			roomService.updateRoom(room);
			// 获取观看人数
			json = this.setJson(true, "成功", null);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("couinfo()--error", e);
		}
		return json;
	}
}
