package com.requestshorter.frontapi.configs

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.support.NoOpCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

import java.time.Duration

@Configuration
@Slf4j
class RedisConfig {

    @Value('${spring.cache.type}')
    def type

    /**
     * Custom key rule
     * @return
     */
//    @Bean
//    KeyGenerator keyGenerator() {
//        return new KeyGenerator() {
//            @Override
//            Object generate(Object target, Method method, Object... params) {
//                StringBuilder sb = new StringBuilder();
//                sb.append(target.getClass().getName());
//                sb.append(method.getName());
//                for (Object obj : params) {
//                    sb.append(obj.toString());
//                }
//                return sb.toString();
//            }
//        };
//    }

    /**
     * Set RedisTemplate rules
     * @param redisConnectionFactory
     * @return
     */
    /**
     * Define your own redisTemplate object. If there is a redisTemplate object in the container,
     * springboot The default redisTemplate object will not be injected, and the name attribute may not be specified. The method name must be redisTemplate
     * (The default name of the bean should be the method name. This mechanism can also exclude the spring boot injection (default redisTemplate)
     */
    @Bean
    RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        try {
            RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(redisConnectionFactory);
            Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

            //(take out the data serialization configuration)
            // Configure the problem that the json string cannot be converted into a java Bean when the template object is used to read the json string from the cache. If it is not configured, an exception will be thrown
            // (java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to
            // com.springboot.enty.Stu)
            //Solve the problem of abnormal query cache conversion
            ObjectMapper om = new ObjectMapper();
            // Specify the fields to be serialized, field,get and set, and modifier range. ANY includes private and public
            om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            // Specify the type of serialized input. The class must be non final modified. Final modified classes, such as string and integer, will run exceptions
           // om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
            jackson2JsonRedisSerializer.setObjectMapper(om);

            //Serial number key value
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
            redisTemplate.setHashKeySerializer(new StringRedisSerializer());
            redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

            redisTemplate.afterPropertiesSet();
            return redisTemplate;
        } catch (e) {
            return null
        }
    }

    /**
     * Set CacheManager cache rules
     * @param factory
     * @return
     */
    /**
     * Customize the cache manager and inject RedisCacheManager (not provided by springboot by default),
     * The following cache manager configures the cache expiration time (if there are other requirements, you need to redefine the cache manager and specify the corresponding cache manager when using cache annotation)
     * The expiration time is only valid for those annotations of the Cache, such as (@ Cacheable, @ CachePut), which has nothing to do with the Cache added by the redisTemplate object
     * And the serialization setting of cache annotation access data
     */
    @Bean
    CacheManager cacheManager(RedisConnectionFactory factory) {
        log.info( "Using CacheManager = ${type}")

        if (type != 'redis') {
            return new NoOpCacheManager()
        }
        try {
            RedisSerializer<String> redisSerializer = new StringRedisSerializer();
            Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

            //(take out the data serialization configuration)
            // The configuration uses @ Cacheable(value="'stus'",key="'stu:1 '") annotation when reading from the cache for the second time
            // The json string cannot be converted into a java Bean when the data is not configured. If it is not configured, an exception will be thrown
            // (java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to
            // com.springboot.enty.Stu)
            //Solve the problem of abnormal query cache conversion
            ObjectMapper om = new ObjectMapper();
            om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
            jackson2JsonRedisSerializer.setObjectMapper(om);

            // Configure serialization (solve the problem of garbled code), and the expiration time is 600 seconds
            RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofSeconds(60*30))
                    .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer)).serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                    .disableCachingNullValues();

            RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                    .cacheDefaults(config)
                    .build();
            return cacheManager;
        } catch (e) {
            return null
        }
    }

}
