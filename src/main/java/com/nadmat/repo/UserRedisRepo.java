package com.nadmat.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.nadmat.model.UserRedisModel;

@Repository
public class UserRedisRepo {
	
	public static final String HASH_KEY = "UserDetails";

	@Autowired
	private RedisTemplate template;

	public UserRedisModel insertRedis(UserRedisModel userModel) {
		template.opsForHash().put(HASH_KEY, userModel.getPanNo(), userModel);
		return userModel;
	}

	public List<UserRedisModel> findAll() {
		return template.opsForHash().values(HASH_KEY);
	}

	public UserRedisModel findByPanNo(String panNo) {
		return (UserRedisModel) template.opsForHash().get(HASH_KEY, panNo);
	}

	public String deleteByPan(String pan) {
		template.opsForHash().delete(HASH_KEY, pan);
		return "user details removed !!";
	}

	public void clearAllRedis() {
		template.delete(HASH_KEY);
	}

}
