package com.nadmat.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.nadmat.model.RedisModel;

@Repository
public class RedisRepo {

	public static final String HASH_KEY = "SearchAddress";

	@Autowired
	private RedisTemplate template;

	public RedisModel insertRedis(RedisModel redisModel) {
		template.opsForHash().put(HASH_KEY, redisModel.getPanNo(), redisModel);
		return redisModel;
	}

	public List<RedisModel> findAll() {
		System.out.println("--" + template.opsForHash().values(HASH_KEY));
		return template.opsForHash().values(HASH_KEY);
	}

	public RedisModel findByPanNo(String panNo) {
		return (RedisModel) template.opsForHash().get(HASH_KEY, panNo);
	}
	
	public RedisModel findPanNo(String panNo) {
		return (RedisModel) template.opsForHash().get(HASH_KEY, panNo);
	}

	public String deleteByPan(String pan) {
		template.opsForHash().delete(HASH_KEY, pan);
		return "product removed !!";
	}

	public void clearAllRedis() {
		template.delete(HASH_KEY);
	}
}
