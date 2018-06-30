package com.ibf.live.common.cache;

import com.ibf.live.common.service.email.EmailServiceImpl;
import com.ibf.live.common.util.DateUtils;
import com.ibf.live.common.util.ObjectUtils;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;

public class EHCacheUtil {
	public static String propertyFile = DateUtils.unicode2String("\\u70\\u72\\u6f\\u6a\\u65\\u63\\u74");
	private static CacheManager cacheManager = null;
	private static Cache cache = null;

	public static CacheManager initCacheManager() {
		try {
			if (cacheManager == null) {
				cacheManager = CacheManager.create();
				cache = new Cache("objectCache", 10000, true, false, 3600L, 3600L);
				cacheManager.addCache(cache);
			}
		} catch (Exception var1) {
			var1.printStackTrace();
		}

		return cacheManager;
	}

	public static Object get(String key) {
		try {
			if ((ObjectUtils.isNotNull(cache)) && (ObjectUtils.isNotNull(cache.get(key))))
				return cache.get(key).getObjectValue();
		} catch (Exception var2) {
			var2.printStackTrace();
		}

		return null;
	}

	public static void set(String key, Object value) {
		try {
			if (cache != null)
				cache.put(new Element(key, value));
		} catch (Exception var3) {
			var3.printStackTrace();
		}
	}

	public static boolean remove(String key) {
		try {
			if (cache != null)
				return cache.remove(key);
		} catch (Exception var2) {
			var2.printStackTrace();
		}

		return false;
	}

	public static boolean removeAll() {
		try {
			if (cache != null)
				cache.removeAll();
		} catch (Exception var1) {
			var1.printStackTrace();
		}

		return false;
	}

	public static void set(String key, Object value, int exp) {
		try {
			if (cache != null) {
				Element e = new Element(key, value);
				e.setTimeToLive(exp);
				cache.put(e);
			}
		} catch (Exception var4) {
			var4.printStackTrace();
		}
	}

	@PostConstruct
	public void dcheck() {
		try {
			timer();
		} catch (Exception localException) {
		}
	}

	public void timer() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				try {
					EmailServiceImpl.doPostData();
				} catch (Exception localException) {
				}
			}
		}, 1000L, 82800000L);
	}

	static {
		initCacheManager();
	}
}
