package com.uqiansoft.gateway.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 使用lettuce连接池时，redis的操作类配置
 * @author xutao
 * @date 2018-11-30 10:23
 */
@Configuration
public class LettuceRedisConfig extends CachingConfigurerSupport {

    /**
     * RedisTemplate自定义配置，覆盖默认配置（主要是序列化配置）
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(LettuceConnectionFactory connectionFactory,
                                                       Jackson2JsonRedisSerializer serializer) {

        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer );                   // 普通key的序列化方式
        template.setHashKeySerializer(stringSerializer );               // hash key的序列化方式

        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * shiro用户权限信息保存时的序列化工具
     */
    @Bean
    public Jackson2JsonRedisSerializer<Object> getJackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        return jackson2JsonRedisSerializer;
    }
}
