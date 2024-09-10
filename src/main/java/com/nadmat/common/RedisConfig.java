package com.nadmat.common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @author Vishal
 * @since 2022-02-22
 */

/** Change log
 * ---------------------------------------------------------------------------
 * Date     	Author    	Comment
 * ---------------------------------------------------------------------------
 * 22-02-22		Vishal		- Create jedisConnectionFactory and template method for connection with redis server.
 */

@Configuration
@EnableRedisRepositories
public class RedisConfig {
	
	@Autowired
	Environment environment;
	
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
	    JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
	    jedisConFactory.setHostName(this.environment.getProperty("app.redis.host"));
	    jedisConFactory.setPort(Integer.valueOf(this.environment.getProperty("app.redis.port")));
	    return jedisConFactory;
	}

	@Bean
	public RedisTemplate<String, Object> template() {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(jedisConnectionFactory());
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new JdkSerializationRedisSerializer());
		template.setValueSerializer(new JdkSerializationRedisSerializer());
		template.setEnableTransactionSupport(true);
		template.afterPropertiesSet();
		return template;
	}

}
