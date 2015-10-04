package com.cooby.like.dao;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TargetLikedRecordDao {

	private static final String KEY = "TargetLikedRecord_";

	@Autowired
	private StringRedisTemplate redisTemplate;

	/**
	 * target增加一个喜欢
	 * 
	 * @param targetId
	 */
	public void like(String userId, String targetId) {
		BoundSetOperations<String, String> boundSetOps = redisTemplate.boundSetOps(KEY + targetId);
		boundSetOps.add(userId);
	}

	/**
	 * target的喜欢列表
	 * 
	 * @param targetId
	 */
	public Set<String> getTargetLikeList(String targetId) {
		BoundSetOperations<String, String> boundSetOps = redisTemplate.boundSetOps(KEY + targetId);
		return boundSetOps.members();
	}

}
