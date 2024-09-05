package net.duchung.quora.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}") String host;
    @Value("${spring.redis.port}") int port;
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory= new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(host);
        jedisConnectionFactory.setPort(port);
        return jedisConnectionFactory;
    }
    @Bean
    public RedisTemplate<String,Object> redisTemplate(){
        RedisTemplate<String,Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();

        template.setHashKeySerializer(new StringRedisSerializer());
        return template;
    }
}
