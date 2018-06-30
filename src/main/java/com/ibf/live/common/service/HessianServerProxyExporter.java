package com.ibf.live.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.caucho.HessianServiceExporter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HessianServerProxyExporter extends HessianServiceExporter {
	private static Logger logger = LoggerFactory.getLogger(HessianServerProxyExporter.class);
	public String hessianAuth;

	public HessianServerProxyExporter() {
		this.hessianAuth = "hessianAuth";
	}

	public void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("++++ hessian request clientIp:" + request.getRemoteAddr() + "++++requestData:"
				+ request.getRequestURL());

		String auth = request.getHeader("hessianAuth");
		if ((auth == null) || (!auth.equalsIgnoreCase(this.hessianAuth))) {
			logger.info("+++++hessianAuth->fail :" + request.getRemoteAddr() + "," + request.getRequestURL());

			return;
		}
		super.handleRequest(request, response);
	}
}