package com.ibf.live.common.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil {
	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	public static String doGet(String url, String queryString) {
		String response = null;
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		method.getParams().setParameter("http.protocol.content-charset", "UTF-8");
		try {
			if (StringUtils.isNotBlank(queryString)) {
				method.setQueryString(URIUtil.encodeQuery(queryString));
			}

			client.executeMethod(method);
			if (method.getStatusCode() == 200)
				response = method.getResponseBodyAsString();
		} catch (URIException localURIException) {
		} catch (IOException localIOException) {
		} finally {
			method.releaseConnection();
		}
		return response;
	}

	public static String doPost(String url, Map<String, String> params) {
		StringBuffer result = new StringBuffer();
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		method.getParams().setParameter("http.protocol.content-charset", "UTF-8");
		if (!ObjectUtils.isNull(params)) {
			NameValuePair[] e = new NameValuePair[params.size()];
			int str = 0;
			Entry entry;
			for (Iterator i$ = params.entrySet().iterator(); i$
					.hasNext(); e[(str++)] = new NameValuePair((String) entry.getKey(), (String) entry.getValue())) {
				entry = (Entry) i$.next();
			}

			method.setRequestBody(e);
		}
		try {
			client.executeMethod(method);
			if (method.getStatusCode() == 200) {
				BufferedReader var14 = new BufferedReader(
						new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
				String var15 = null;

				while ((var15 = var14.readLine()) != null)
					result.append(var15);
			}
		} catch (IOException localIOException) {
		} finally {
			method.releaseConnection();
		}

		return result.toString();
	}

	public static String doPostHeader(String url,Map<String, String> headers, Map<String, String> params) {
		StringBuffer result = new StringBuffer();
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		method.getParams().setParameter("http.protocol.content-charset", "UTF-8");
		if (!ObjectUtils.isNull(params)) {
			NameValuePair[] e = new NameValuePair[params.size()];
			int str = 0;
			Entry entry;
			for (Iterator i$ = params.entrySet().iterator(); i$
					.hasNext(); e[(str++)] = new NameValuePair((String) entry.getKey(), (String) entry.getValue())) {
				entry = (Entry) i$.next();
			}
			method.setRequestBody(e);
		}
		if (!ObjectUtils.isNull(headers)) {
			for (Entry<String, String> entry : headers.entrySet()) {
			    method.addRequestHeader(entry.getKey(),entry.getValue());
			}  
		}
		try {
			client.executeMethod(method);
			if (method.getStatusCode() == 200) {
				BufferedReader var14 = new BufferedReader(
						new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
				String var15 = null;

				while ((var15 = var14.readLine()) != null)
					result.append(var15);
			}
		} catch (IOException localIOException) {
		} finally {
			method.releaseConnection();
		}

		return result.toString();
	}
	
	public static String doPostXml(String urlStr, String xmlStr) {
		String result = null;
		try {
			URL urlClient = new URL(urlStr);
			HttpURLConnection connection = (HttpURLConnection) urlClient.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.connect();

			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.write(xmlStr.getBytes());
			out.flush();
			out.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			StringBuffer sb = new StringBuffer("");
			String lines;
			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), "utf-8");
				sb.append(lines);
			}
			reader.close();

			connection.disconnect();
			result = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
