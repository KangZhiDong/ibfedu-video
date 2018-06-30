package com.ibf.live.controller.website;

import com.ibf.live.common.constants.CommonConstants;
import com.ibf.live.common.controller.BaseController;
import com.ibf.live.common.enums.WebSiteProfileType;
import com.ibf.live.entity.website.WebsiteImages;
import com.ibf.live.service.website.WebsiteImagesService;
import com.ibf.live.service.website.WebsiteNavigateService;
import com.ibf.live.service.website.WebsiteProfileService;
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

@Controller
@RequestMapping("/api/website")
public class AppWebsiteController extends BaseController {
	//private static Logger logger=Logger.getLogger(AppWebsiteController.class);
	private static Logger logger = LoggerFactory.getLogger(AppWebsiteController.class);
	@Autowired
    private WebsiteProfileService websiteProfileService;
	 @Autowired
	 private WebsiteNavigateService websiteNavigateService;
	 @Autowired
	 private WebsiteImagesService websiteImagesService;
	
	/**
	 * 获取网站配置信息
	 */
	@RequestMapping("/info")
	@ResponseBody
	public Map<String, Object> getWebsiteInfo(HttpServletRequest request){
		Map<String, Object> json=new HashMap<String, Object>();
		try{
			Map<String, Object> map=new HashMap<String, Object>();
			// 获得banner图
 			Map<String, List<WebsiteImages>> websiteImages = websiteImagesService.queryImagesByType();
 			map.put("websiteImages", websiteImages);
        	//获得网站配置
          	Map<String,Object> websitemap=websiteProfileService.getWebsiteProfileByType(WebSiteProfileType.web.toString());
          	map.put("websitemap",websitemap);
            //获得LOGO配置
          	Map<String,Object> logomap=websiteProfileService.getWebsiteProfileByType(WebSiteProfileType.logo.toString());
          	map.put("logomap",logomap);
          	//网站统计代码
//            Map<String,Object> tongjiemap=websiteProfileService.getWebsiteProfileByType(WebSiteProfileType.censusCode.toString());
//            map.put("tongjiemap",tongjiemap);
            //咨询链接
            Map<String,Object> onlinemap = websiteProfileService.getWebsiteProfileByType(WebSiteProfileType.online.toString());
            map.put("onlinemap", onlinemap);
            
            //网站导航配置
          	Map<String,Object> navigatemap=websiteNavigateService.getWebNavigate();
          	map.put("navigatemap",navigatemap);
      		
          	map.put("thirdLogin",Boolean.parseBoolean(CommonConstants.propertyUtil.getProperty("thirdLogin")));
          	map.put("thirdLoginDirectSkip",Boolean.parseBoolean(CommonConstants.propertyUtil.getProperty("thirdLoginDirectSkip")));
          	map.put("systemOwner", CommonConstants.propertyUtil.getProperty("systemOwner"));
			json=this.setJson(true, "成功", map);
		}catch(Exception e){
			json=this.setJson(false, "异常", null);
			logger.error("toSubjectList()--error",e);
		}
		return json;
	}

}
