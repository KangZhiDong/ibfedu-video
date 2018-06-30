package com.ibf.live;

import com.ibf.live.common.redis.RedisCacheUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IbfeduVideoApplicationTests {

	@Test
	public void queryUserByIdTest() {
//		Jedis jedis = new Jedis("localhost", 56379, 100000);
//		int i = 0;
//
//		try {
//			long start = System.currentTimeMillis();// 开始毫秒数
//			while (true) {
//				long end = System.currentTimeMillis();
//				if (end - start >= 1000) {// 当大于等于1000毫秒（相当于1秒）时，结束操作
//					break;
//				}
//
//				//new Class<?>[]{RediTest.class};
//				i++;
//				jedis.set("test" + i, i + "","XX","EX",100);
//			}
//		} finally {// 关闭连接
//			jedis.close();
//		}
		// 打印1秒内对Redis的操作次数
	//	System.out.println("redis每秒操作：" + i + "次");
		String userKey = "kzd666";
		RedisCacheUtil.set(userKey,"牛逼");
		RedisCacheUtil.get(userKey);
		

	}

}
