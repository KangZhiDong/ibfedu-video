package com.ibf.live.common.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibf.live.common.util.DateEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
	public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	protected static final String SIMPLE_VIEW_PATH = "ibfedu";
	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

	@InitBinder({ "page" })
	public void initBinderPage(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("page.");
	}

	public static String getViewPath(String path) {
		if ((path != null) && (!path.trim().equals(""))) {
			return "ibfedu" + path;
		}
		return "";
	}

	public Map<String, Object> setJson(boolean success, String message, Object entity) {
		Map json = new HashMap();
		json.put("success", Boolean.valueOf(success));
		json.put("message", message);
		json.put("entity", entity);
		return json;
	}
	public String setExceptionRequest(HttpServletRequest request, Exception e) {
		logger.error(request.getContextPath(), e);
		StackTraceElement[] messages = e.getStackTrace();
		if ((messages != null) && (messages.length > 0)) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(e.toString()).append("<br/>");
			for(int i = 0; i < messages.length; i++) {
				buffer.append(messages[i].toString()).append("<br/>");
			}
			request.setAttribute("myexception", buffer.toString());
		}
		return "/common/error";
	}

	public Map<String, Object> setAjaxException(Map<String, Object> map) {
		map.put("success", Boolean.valueOf(false));
		map.put("message", "系统繁忙，请稍后再操作！");
		map.put("entity", null);
		return map;
	}

	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, new DateEditor());
	}
}
