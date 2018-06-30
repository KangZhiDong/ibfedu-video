package com.ibf.live.common.redis;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;

import java.util.List;
import java.util.Map;
import java.util.Set;

public  class RedisCacheUtil {
	private static RedisManage rm = null;
	public static RedisManage initRedisManage() {
		try {
			if (rm == null) {
				 ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("redis.xml");
				 rm= (RedisManage)ctx.getBean("redisManager");
			}
		} catch (Exception var1) {
			var1.printStackTrace();
		}

		return rm;
	}
	
	static {
		initRedisManage();
	}
	
	/**hash
     * 通过key给field设置指定的值,如果key不存在,则先创建 ,存在会覆盖原来的值
     * @param key
     * @param field字段
     * @param value
     * @return 如果不存在，新建的返回1，存在返回0, 异常返回null
     * 
     */
    public static  Long hset(String key, String field, String value) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.hset(key, field, value);
        rm.releaseJedis(jedis);
        return result;
    }
    /**Hash
     * 为哈希表 key 中的域 field 的值加上增量 value
     * @param key
     * @param field
     * @param value
     * @return
     */
    public static  Long hincrBy(String key, String field, long value) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.hincrBy(key, field, value);
        rm.releaseJedis(jedis);
        return result;
    }

    /**

     * 通过key给field设置指定的值,如果key不存在则先创建,如果field已经存在，操作无效
     * @param key
     * @param field
     * @param value
     * @return 不存在新建返回1，存在返回0
     */
    public static  Long hsetnx(String key, String field, String value) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.hsetnx(key, field, value);
        rm.releaseJedis(jedis);
        return result;
    }

    /**
     * 通过key同时设置 hash的多个field
     * @param key
     * @param hash
     * @return 返回OK 异常返回null
     */
    public static String hmset(String key, Map<String, String> hash) {
        Jedis jedis = rm.getJedis();
        String result = jedis.hmset(key, hash);
        rm.releaseJedis(jedis);
        return result;
    }

    /**
     * 通过key 和 field 获取指定的 value
     * @param key
     * @param field
     * @return 没有返回null
     */
    public static String hget(String key, String field) {
        Jedis jedis = rm.getJedis();
        String result = jedis.hget(key, field);
        rm.releaseJedis(jedis);
        return result;
    }

    /**
     * 通过key 和 fields 获取指定的value 如果没有对应的value则返回null
     * @param key
     * @param fields可以使 一个String 也可以是 String数组
     * @return
     */
    public static List<String> hmget(String key, String... fields) {
        Jedis jedis = rm.getJedis();
        List<String> result = jedis.hmget(key, fields);
        rm.releaseJedis(jedis);
        return result;
    }

    /**
     * 通过key获取所有的field和value
     * @param key
     * @return
     */
    public static Map<String, String> hgetAll(String key) {
        Jedis jedis = rm.getJedis();
        Map<String, String> result = jedis.hgetAll(key);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 通过key删除field的value
     * @param key
     * @return
     */
    public static Long hdel(String key, String field) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.hdel(key, field);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 返回key为键中存放的field值的个数
     * @param key
     * @return 
     */
    public static Long hlen(String key) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.hlen(key);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 查看key是否存在指定的field
     * @param key
     * @return 
     */
    public static Boolean hexists(String key, String field) {
        Jedis jedis = rm.getJedis();
        Boolean result = jedis.hexists(key, field);
        rm.releaseJedis(jedis);
        return result;
    }

    /**
     * 返回key存储的map对象中的所有key  
     * @param key
     * @return 
     */
    public static Set<String> hkeys(String key) {
        Jedis jedis = rm.getJedis();
        Set<String> result = jedis.hkeys(key);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 返回key存储的map对象中的所有键的values值  
     * @param key
     * @return 
     */
    public static List<String> hvals(String key) {
        Jedis jedis = rm.getJedis();
        List<String> result = jedis.hvals(key);
        rm.releaseJedis(jedis);
        return result;
    }

    /**
     * 判断key是否存在
     * @param key
     * @return true OR false
     */
    public static boolean exists(String key) {
        Jedis jedis = null;
        boolean result = false;
        try {
            jedis = rm.getJedis();
            result = jedis.exists(key);
        } catch (Exception e) {
            rm.releaseBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            rm.releaseJedis(jedis);
        }

        return result;
    }

    /**
     * 删除指定的key,也可以传入一个包含key的数组
     * @param keys
     * @return 返回删除成功的个数
     */
    public static Long del(String... keys) {
        Jedis jedis = null;
        Long result = 0L;
        try {
            jedis = rm.getJedis();
            result = jedis.del(keys);
        } catch (Exception e) {
            rm.releaseBrokenJedis(jedis);
            e.printStackTrace();
            result = 0L;
        } finally {
            rm.releaseJedis(jedis);
        }
        return result;
    }

    /**
     * 对key的对应value值排序
     * @return 
     */
    public static List<String> sort(String key) {
        Jedis jedis = rm.getJedis();
        List<String> result = jedis.sort(key);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 将当前数据库的 ke移动到给定的数据库 db 当中
     * @return 
     */
    public static Long move(String key, int dbIndex) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.move(key, dbIndex);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 返回某个key元素的数据类型 ( none:不存在,string:字符,list,set,zset,hash)
     * @return 
     */
    public static String type(String key) {
        Jedis jedis = rm.getJedis();
        String result = jedis.type(key);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 返回当前数据库的key的总数
     * @return 
     */
    public static Long dbsize() {
        Jedis jedis = rm.getJedis();
        Long result = jedis.dbSize();
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     *设置某个key的过期时间(秒),(EXPIRE bruce 1000：设置bruce这个key1000秒后系统自动删除)注意：如果在还没有过期的时候，对值进行了改变，那么那个值会被清除。
     * @return 
     */
    public static Long expire(String key, int seconds) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.expire(key, seconds);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置生存时间。
     * 不同在于 EXPIREAT 命令接受的时间参数是 UNIX 时间戳(unix timestamp)。
     * @return 
     */

    public static Long expireAt(String key, Long unixTime) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.expireAt(key, unixTime);
        rm.releaseJedis(jedis);
        return result;
    }
    /**List
     * 通过key在list头部添加值
     * @param key
     * @param value
     * @return 在 push 操作后的 list 长度。
     */
    public static Long lpush(String key, String value) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.lpush(key, value);
        rm.releaseJedis(jedis);
        return result;
    }
    /**List
     * 向存于 key 的列表的尾部插入所有指定的值。如果 key 不存在，那么会创建一个空的列表然后再进行 push 操作。 
     * 当 key 保存的不是一个列表，那么会返回一个错误。
     * @param key
     * @param value
     * @return  在 push 操作后的列表长度
     */
    public static Long rpush(String key, String value) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.rpush(key, value);
        rm.releaseJedis(jedis);
        return result;
    }
    /**List
     * 获取list的长度
     * @param key
     * @return 
     */
    public static Long llen(String key) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.llen(key);
        rm.releaseJedis(jedis);
        return result;
    }
    /**List
     * 返回存储在 key 的列表里指定范围内的元素
     * @param key 
     * @parm start 开始位置
     * @param end 结束位置 -1表示最后一个
     * @return 
     */
    public static List<String> lrange(String key, long start, long end) {
        Jedis jedis = rm.getJedis();
        List<String> result = jedis.lrange(key, start, end);
        rm.releaseJedis(jedis);
        return result;
    }
    /**List
     * 截取(trim)一个已存在的 list，这样 list 就会只包含指定范围的指定元素
     * @param key 
     * @parm start 开始位置
     * @param end 结束位置 -1表示最后一个
     * @return 
     */

    public static String ltrim(String key, long start, long end) {
        Jedis jedis = rm.getJedis();
        String result = jedis.ltrim(key, start, end);
        rm.releaseJedis(jedis);
        return result;
    }
    /**List
     * 通过key在list头部添加值
     * 只有当 key 已经存在并且存着一个 list 的时候，在这个 key 下面的 list 的头部插入 value。 与 LPUSH 相反，当 key 不存在的时候不会进行任何操作。
     * @param key
     * @param value
     * @return 在 push 操作后的 list 长度。
     */

    public static Long lpushx(String key, String value) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.lpushx(key, value);
        rm.releaseJedis(jedis);
        return result;
    }

    public static Long rpushx(String key, String value) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.rpushx(key, value);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 弹出 List 的第一个元素
     * @param key
     * @return
     */
    public static String lpop(String key) {
        Jedis jedis = rm.getJedis();
        String result = jedis.lpop(key);
        rm.releaseJedis(jedis);
        return result;
    }

    public static String rpop(String key) {
        Jedis jedis = rm.getJedis();
        String result = jedis.rpop(key);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 根据参数 COUNT 的值，移除列表中与参数 VALUE 相等的元素。
     * count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。
     * count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。
     * count = 0 : 移除表中所有与 VALUE 相等的值。
     */

    public static Long lrem(String key, long count, String value) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.lrem(key, count, value);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 设置 index 位置的list元素的值为 value
     * @param key
     * @param index
     * @param value
     * @return
     */
    public static String lset(String key, long index, String value) {
        Jedis jedis = rm.getJedis();
        String result = jedis.lset(key, index, value);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 返回列表 key 中，下标为 index 的元素。
     * @param key
     * @param index
     * @return
     */
    public static String lindex(String key, long index) {
        Jedis jedis = rm.getJedis();
        String result = jedis.lindex(key, index);
        rm.releaseJedis(jedis);
        return result;
    }
   /**
    *命令 RPOPLPUSH 在一个原子时间内，执行以下两个动作：
    *将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端。
    *将 source 弹出的元素插入到列表 destination ，作为 destination 列表的的头元素。
    *举个例子，你有两个列表 source 和 destination ， source 列表有元素 a, b, c ， destination 列表有元素 x, y, z ，
    *执行 RPOPLPUSH source destination 之后， source 列表包含元素 a, b ， destination 列表包含元素 c, x, y, z ，
    *并且元素 c 会被返回给客户端。
    * @param srcKey
    * @param dstKey
    * @return
    */
    public static String rpoplpush(String srcKey, String dstKey) {
        Jedis jedis = rm.getJedis();
        String result = jedis.rpoplpush(srcKey, dstKey);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * BRPOPLPUSH 是 RPOPLPUSH 的阻塞版本，当给定列表 source 不为空时， BRPOPLPUSH 的表现和 RPOPLPUSH 一样。
     *当列表 source 为空时， BRPOPLPUSH 命令将阻塞连接，直到等待超时，或有另一个客户端对 source 执行 LPUSH 或 RPUSH 命令为止。
     *超时参数 timeout 接受一个以秒为单位的数字作为值。超时参数设为 0 表示阻塞时间可以无限期延长(block indefinitely) 。
     * @param source
     * @param destination
     * @param timeout
     * @return
     */
    public static String brpoplpush(String source, String destination, int timeout) {
        Jedis jedis = rm.getJedis();
        String result = jedis.brpoplpush(source, destination, timeout);
        rm.releaseJedis(jedis);
        return result;
    }


   /**
    * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略(set不重复)。
    * 假如 key 不存在，则创建一个只包含 member 元素作成员的集合。
    * @param key
    * @param member
    * @return
    */
    public static Long sadd(String key, String member) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.sadd(key, member);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 移除集合 key 中的一个 member 元素，不存在的 member 元素会被忽略。
     * @param key
     * @param member
     * @return
     */
    public static Long srem(String key, String member) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.srem(key, member);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 返回集合 key 中的所有成员。
     * @param key
     * @return
     */
    public static Set<String> smembers(String key) {
        Jedis jedis = rm.getJedis();
        Set<String> result = jedis.smembers(key);
        rm.releaseJedis(jedis);
        return result;
    }
     /**
      * 判断 member 元素是否集合 key 的成员。
      * @param key
      * @param member
      * @return
      */
    public static Boolean sismember(String key, String member) {
        Jedis jedis = rm.getJedis();
        Boolean result = jedis.sismember(key, member);
        rm.releaseJedis(jedis);
        return result;
    }
   /**
    * 返回集合 key集合中元素的数量)。
    * @param key
    * @return
    */
    public static Long scard(String key) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.scard(key);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 将 member 元素从 source 集合移动到 destination 集合。
     *SMOVE 是原子性操作。
     *如果 source 集合不存在或不包含指定的 member 元素，则 SMOVE 命令不执行任何操作，仅返回 0 。否则， member 元素从 source 集合中被移除，并添加到 destination 集合中去。
     *当 destination 集合已经包含 member 元素时， SMOVE 命令只是简单地将 source 集合中的 member 元素删除。
     *当 source 或 destination 不是集合类型时，返回一个错误。
     * @param srckey
     * @param dstkey
     * @param member
     * @return
     */
    public static Long smove(String srckey, String dstkey, String member) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.smove(srckey, dstkey, member);
        rm.releaseJedis(jedis);
        return result;
    }

    /**
     * 移除并返回集合中的一个随机元素。
     * @param key
     * @return
     */
    public static String spop(String key) {
        Jedis jedis = rm.getJedis();
        String result = jedis.spop(key);
        rm.releaseJedis(jedis);
        return result;
    }
     /**
      * 返回集合中的一个随机元素
      * @param key
      * @return
      */
    public static String srandmember(String key) {
        Jedis jedis = rm.getJedis();
        String result = jedis.srandmember(key);
        rm.releaseJedis(jedis);
        return result;
    }

    /**
     * 返回给定集合的交集。
     * @param keys
     * @return
     */
    public static Set<String> sinter(String... keys) {
        Jedis jedis = rm.getJedis();
        Set<String> result = jedis.sinter(keys);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 类似于 SINTER 命令，但它将结果保存到 destination 集合，而不是简单地返回结果集
     * @param dstkey
     * @param keys
     * @return
     */
    public static Long sinterstore(String dstkey, String... keys) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.sinterstore(dstkey, keys);
        rm.releaseJedis(jedis);
        return result;
    }
   /**
    * 返回所有给定集合的并集
    * @param keys
    * @return
    */
    public static Set<String> sunion(String... keys) {
        Jedis jedis = rm.getJedis();
        Set<String> result = jedis.sunion(keys);
        rm.releaseJedis(jedis);
        return result;
    }

    public static Long sunionstore(String dstkey, String... keys) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.sunionstore(dstkey, keys);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 返回所有给定集合之间的差集。
     * @param keys
     * @return
     */
    public static Set<String> sdiff(String... keys) {
        Jedis jedis = rm.getJedis();
        Set<String> result = jedis.sdiff(keys);
        rm.releaseJedis(jedis);
        return result;
    }

    public static Long sdiffstore(String dstkey, String... keys) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.sdiffstore(dstkey, keys);
        rm.releaseJedis(jedis);
        return result;
    }
   /**server
    * 清空整个 Redis 服务器的数据(删除所有数据库的所有 key )。
    * @return
    */
    public static String flushAll() {
        Jedis jedis = rm.getJedis();
        String result = jedis.flushAll();
        rm.releaseJedis(jedis);
        return result;
    }
   /**
    * 清空当前数据库中的所有 key。
    * @return
    */
    public static String flushDB() {
        Jedis jedis = rm.getJedis();
        String result = jedis.flushDB();
        rm.releaseJedis(jedis);
        return result;
    }
    /** 
    *停止所有客户端
    *如果有至少一个保存点在等待，执行 SAVE 命令
    *如果 AOF 选项被打开，更新 AOF 文件
    * 关闭 redis 服务器(server)
    * @return
    */

    public static String shutdown() {
        Jedis jedis = rm.getJedis();
        String result = jedis.shutdown();
        rm.releaseJedis(jedis);
        return result;
    }

    /**sorted set
     * 将一个 member 元素及其 score值加入到有序集 key 当中。
     * 如果某个 member 已经是有序集的成员，那么更新这个 member 的 score 值，
     *并通过重新插入这个 member 元素，来保证该 member 在正确的位置上。
     * @param key
     * @param score
     * @param member
     * @return
     */
    public static Long zadd(String key, double score, String member) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.zadd(key, score, member);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * sorted set
     * 移除有序集 key 中的一成员member，不存在的成员将被忽略。
     * @param key
     * @param member
     * @return
     */
    public static Long zrem(String key, String member) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.zrem(key, member);
        rm.releaseJedis(jedis);
        return result;
    }
    /**sorted set
     * 返回集合 key集合中元素的数量
     * @param key
     * @return
     */
    public static Long zcard(String key) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.zcard(key);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 返回有序集 key 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员的数量。
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Long zcount(String key, double min, double max) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.zcount(key, min, max);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 返回有序集 key 中，成员 member 的 score 值。
     * @param key
     * @param member
     * @return
     */
    public static Double zscore(String key, String member) {
        Jedis jedis = rm.getJedis();
        Double result = jedis.zscore(key, member);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 为有序集 key 的成员 member 的 score 值加上增量"score"
     * @param key
     * @param score
     * @param member
     * @return
     */
    public static Double zincrby(String key, double score, String member) {
        Jedis jedis = rm.getJedis();
        Double result = jedis.zincrby(key, score, member);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 为有序集 key 的值加上增量"score"
     * @param key
     * @param score
     * @return
     */
    public static Double incrByFloat(String key, double score) {
        Jedis jedis = rm.getJedis();
        Double result = jedis.incrByFloat(key, score);
        rm.releaseJedis(jedis);
        return result;
    }
    
   /**
    * 返回有序集 key 中，指定区间内的成员。其中成员的位置按 score 值递增(从小到大)来排序。
    * @param key
    * @param start
    * @param end
    * @return
    */
    public static Set<String> zrange(String key, int start, int end) {
        Jedis jedis = rm.getJedis();
        Set<String> result = jedis.zrange(key, start, end);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 其中成员的位置按 score 值递减(从大到小)来排列。
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Set<String> zrevrange(String key, int start, int end) {
        Jedis jedis = rm.getJedis();
        Set<String> result = jedis.zrevrange(key, start, end);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。
     * 有序集成员按 score 值递减(从大到小)的次序排列。
     * @param key
     * @param max
     * @param min
     * @param offset
     * @param count
     * @return
     */
    public static Set<String> zrevrangeByScore(String key, double max, double min,int offset, int count) {
        Jedis jedis=rm.getJedis();
        try{
            Set<String> result=jedis.zrevrangeByScore(key, max, min, offset, count);
            return result;
        }finally{
            rm.releaseJedis(jedis);
        }
    }

    public static Set<String> zrevrangeByScore(String key, double max, double min) {
        Jedis jedis=rm.getJedis();
        try{
            Set<String> result=jedis.zrevrangeByScore(key, max, min);
            return result;
        }finally{
            rm.releaseJedis(jedis);
        }
    }
    /**
     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。
     * 有序集成员按 score 值递增(从小到大)次序排列。
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Set<String> zrangeByScore(String key, double min, double max) {
        Jedis jedis=rm.getJedis();
        try{
            Set<String> result=jedis.zrangeByScore(key, min, max);
            return result;
        }finally{
            rm.releaseJedis(jedis);
        }
    }
    /**
     * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。
     *  排名以 0 为底，也就是说， score 值最小的成员排名为 0 。
     */

    public static Long zrank(String key, String member) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.zrank(key, member);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递减(从大到小)排序。
     * 排名以 0 为底，也就是说， score 值最大的成员排名为 0 。
     * @param key
     * @param member
     * @return
     */
    public static Long zrevrank(String key, String member) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.zrevrank(key, member);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 移除有序集 key 中，指定排名(rank)区间内的所有成员。
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Long zremrangeByRank(String key, int start, int end) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.zremrangeByRank(key, start, end);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 移除有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。
     * @param key
     * @param start
     * @param end
     * @return
     */

    public static Long zremrangeByScore(String key, double start, double end) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.zremrangeByScore(key, start, end);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 计算给定的一个或多个有序集的交集，并将该交集(结果集)储存到 destination 。
     * 默认情况下，结果集中某个成员的 score 值是所有给定集下该成员 score 值之和.
     * @param dstkey
     * @param sets
     * @return
     */
    public static Long zinterstore(String dstkey, String... sets) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.zinterstore(dstkey, sets);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 计算给定的一个或多个有序集的并集，并将该并集(结果集)储存到 destination 。
     * 默认情况下，结果集中某个成员的 score 值是所有给定集下该成员 score 值之 和 。
     * @param dstkey
     * @param sets
     * @return
     */
    public static Long zunionstore(String dstkey, String... sets) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.zunionstore(dstkey, sets);
        rm.releaseJedis(jedis);
        return result;
    }

    /**String
     * 通过key获取储存在redis中的value
     * 并释放连接
     * @param key
     * @return 成功返回value 失败返回null
     */
    public static String get(String key) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = rm.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            rm.releaseBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            rm.releaseJedis(jedis);
        }
        return result;
    }

    /**string
     * 向redis存入key和value,并释放连接资源
     * 如果key已经存在 则覆盖
     * @param key
     * @param value
     * @return 成功 返回OK 失败返回 0
     */
    public static String set(String key, String value) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = rm.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            rm.releaseBrokenJedis(jedis);
            e.printStackTrace();
            result = "0";
        } finally {
            rm.releaseJedis(jedis);
        }
        return result;
    }

    /**string
     * 向redis存入key和value,并设置超时时间(s)释放连接资源
     * 如果key已经存在 则覆盖
     * @param key
     * @param value
     * @param time
     * @return 成功 返回OK 失败返回 0
     */
    public static String set(String key, String value,int time) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = rm.getJedis();
            result = jedis.set(key, value);
            jedis.expire(key, time);
        } catch (Exception e) {
            rm.releaseBrokenJedis(jedis);
            e.printStackTrace();
            result = "0";
        } finally {
            rm.releaseJedis(jedis);
        }
        return result;
    }
    
    /**
     * <p>
     * 设置key value,如果key已经存在则返回0,nx==> not exist
     * @param key
     * @param value
     * @return 成功返回1 如果存在 和 发生异常 返回 0
     */
    public static Long setnx(String key, String value) {
        Jedis jedis = null;
        Long result = 0L;
        try {
            jedis = rm.getJedis();
            result = jedis.setnx(key, value);
        } catch (Exception e) {
            rm.releaseBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            rm.releaseJedis(jedis);
        }

        return result;
    }
  /**string
   * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
   * @param key
   * @param value
   * @return
   */
    public static String getSet(String key, String value) {
        Jedis jedis = rm.getJedis();
        String result = jedis.getSet(key, value);
        rm.releaseJedis(jedis);
        return result;
    }
/**
 * 返回所有(一个或多个)给定 key 的值。
 * 如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值 nil 。因此，该命令永不失败。
 * @param keys
 * @return
 */
    public static List<String> mget(String[] keys) {
        Jedis jedis = rm.getJedis();
        List<String> result = jedis.mget(keys);
        rm.releaseJedis(jedis);
        return result;
    }
   /**
    * 同时设置一个或多个 key-value 对。
    * 有会覆盖
    * @param keysvalues
    */
    public static void mset(String... keysvalues) {
        Jedis jedis = rm.getJedis();
        jedis.mset(keysvalues);
        rm.releaseJedis(jedis);
    }
    /**
     * key不存在时才插入
     * @param keysvalues
     */
    public static void msetnx(String... keysvalues) {
        Jedis jedis = rm.getJedis();
        jedis.msetnx(keysvalues);
        rm.releaseJedis(jedis);
    }
     /**
      * 将 key 所储存的值加上增量 increment 。
      * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令。
      * @param key
      * @param integer
      * @return
      */
    public static Long incrBy(String key, Integer integer) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.incrBy(key, integer);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 返回 key 所储存的字符串值的长度。
     * @param key
     * @return
     */
    public static Long strlen(String key) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.strlen(key);
        rm.releaseJedis(jedis);
        return result;
    }

    /**
     * 通过key 对value进行加值+1操作,当value不是int类型时会返回错误,当key不存在是则value为1
     * @param key
     * @return 加值后的结果
     */
    public static Long incr(String key) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.incr(key);
        rm.releaseJedis(jedis);
        return result;
    }



    /**
     * 对key的值做减减操作,如果key不存在,则设置key为-1

     * @param key
     * @return
     */
    public static Long decr(String key) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.decr(key);
        rm.releaseJedis(jedis);
        return result;
    }

    /**
     * 减去指定的值
     * @param key
     * @param integer
     * @return
     */
    public static Long decrBy(String key, Integer integer) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.decrBy(key, integer);
        rm.releaseJedis(jedis);
        return result;
    }

    /**
     * 通过key向指定的value值追加值
     * @param key
     * @param str
     * @return 成功返回 添加后value的长度 失败 返回 添加的 value 的长度 异常返回0L
     */
    public static Long append(String key, String str) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = rm.getJedis();
            res = jedis.append(key, str);
        } catch (Exception e) {
            rm.releaseBrokenJedis(jedis);
            e.printStackTrace();
            res = 0L;
        } finally {
            rm.releaseJedis(jedis);
        }
        return res;
    }

    public static String subStr(String key, int Start, int end) {
        Jedis jedis = rm.getJedis();
        String result = jedis.substr(key, Start, end);
        rm.releaseJedis(jedis);
        return result;
    }

    /**
     * 设置key value并制定这个键值的有效期
     * @param key
     * @param value
     * @param seconds
     *            单位:秒
     * @return 成功返回OK 失败和异常返回null
     */
    public static String setex(String key, String value, int seconds) {
        Jedis jedis = null;
        String res = null;
        try {
            jedis = rm.getJedis();
            res = jedis.setex(key, seconds, value);
        } catch (Exception e) {
            rm.releaseBrokenJedis(jedis);
            e.printStackTrace();
        } finally {
            rm.releaseJedis(jedis);
        }
        return res;
    }
   /**
    * 用 value 参数覆写(overwrite)给定 key 所储存的字符串值，从偏移量 offset 开始。
    * @param key
    * @param offset
    * @param value
    * @return
    */
    public static Long setRange(String key, long offset, String value) {
        Jedis jedis = rm.getJedis();
        Long result = jedis.setrange(key, offset, value);
        rm.releaseJedis(jedis);
        return result;
    }

    public static String getRange(String key, long StartOffset, long endOffset) {
        Jedis jedis = rm.getJedis();
        String result = jedis.getrange(key, StartOffset, endOffset);
        rm.releaseJedis(jedis);
        return result;
    }
    /**
     * 查找所有符合给定模式 pattern 的 key 。
     * @param key
     * @return
     */
    public static Set<String> keys(String key){
        Jedis jedis = rm.getJedis();
        Set<String> keys = jedis.keys(key);
        rm.releaseJedis(jedis);
        return keys;
    }


    public static List<String> sort(String key,SortingParams params){
        Jedis jedis = rm.getJedis();
        List<String> sortedResult =  jedis.sort(key,params);
        rm.releaseJedis(jedis);
        return sortedResult;
    }

    /**
     * 检测给定key的剩余生存时间，单位秒
     * @param key
     * @return returns -2 if the key does not exist.returns -1 if the key exists but has no associated expire.
     */
    public static long ttl(String key){
        Long result = -1L;
        Jedis jedis = null;
        try{
            jedis = rm.getJedis();
            result = jedis.ttl(key);
        }catch (Exception e){
            if(jedis != null){
            	rm.releaseBrokenJedis(jedis);
            }
        }finally {
            rm.releaseJedis(jedis);
        }

        return result;
    }

    /**
     * 检测给定key的剩余生存时间，单位毫秒
     * @param key
     * @return returns -2 if the key does not exist.returns -1 if the key exists but has no associated expire.
     */
    public static long pttl(String key){
        long result = -2L;
        Jedis jedis = null;
        try{
            jedis = rm.getJedis();
            result = jedis.ttl(key);
        }catch (Exception e){
            if(jedis != null){
            	rm.releaseBrokenJedis(jedis);
            }
        }finally {
        	rm.releaseJedis(jedis);
        }
        return result;
    }
}
