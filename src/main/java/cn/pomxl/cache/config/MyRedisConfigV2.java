package cn.pomxl.cache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.net.UnknownHostException;

/**
 * 1、使用Jackson2JsonRedisSerializer需要指明序列化的类Class，可以使用Obejct.class
 *
 * 2、使用GenericJacksonRedisSerializer比Jackson2JsonRedisSerializer效率低，占用内存高。
 *
 * 3、GenericJacksonRedisSerializer反序列化带泛型的数组类会报转换异常，解决办法存储以JSON字符串存储。
 *
 * 4、GenericJacksonRedisSerializer和Jackson2JsonRedisSerializer都是以JSON格式去存储数据，都可以作为Redis的序列化方式。
 * ---------------------
 */

@Configuration
public class MyRedisConfigV2 {

    @Bean
    public RedisTemplate<Object, Object> commonRedisTemplate(
            RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer ser = new Jackson2JsonRedisSerializer(Object.class);
        template.setDefaultSerializer(ser);
        return template;
    }

    //CacheManagerCustomizers可以来定制缓存的一些规则
    @Primary  //将某个缓存管理器作为默认的
    @Bean
    public RedisCacheManager commonCacheManager(RedisTemplate<Object, Object> commonRedisTemplate){
        RedisCacheManager cacheManager = new RedisCacheManager(commonRedisTemplate);
        //key多了一个前缀

        //使用前缀，默认会将CacheName作为key的前缀
        cacheManager.setUsePrefix(true);

        return cacheManager;
    }


}
