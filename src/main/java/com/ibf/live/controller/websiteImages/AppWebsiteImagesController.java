package com.ibf.live.controller.websiteImages;

import com.ibf.live.common.controller.BaseController;
import com.ibf.live.entity.website.WebsiteImages;
import com.ibf.live.service.website.WebsiteImagesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author www.inxedu.com
 */
@Controller
@RequestMapping("/api/websiteImages")
public class AppWebsiteImagesController extends BaseController {
	//private static Logger logger=Logger.getLogger(AppWebsiteImagesController.class);
	private static Logger logger = LoggerFactory.getLogger(AppWebsiteImagesController.class);
	
	@Autowired
	private WebsiteImagesService websiteImagesService;
	
	/**
	 * banner
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Map<String, Object> websiteImages(HttpServletRequest request){
		Map<String, Object> json=new HashMap<String, Object>();
		try{
			// 获得banner图
			Map<String, List<WebsiteImages>> websiteImages = websiteImagesService.queryImagesByType();
			json=this.setJson(true, "成功", websiteImages);
		}catch(Exception e){
			json=this.setJson(false, "异常", null);
			logger.error("toSubjectList()--error",e);
		}
		return json;
	}
	
}
