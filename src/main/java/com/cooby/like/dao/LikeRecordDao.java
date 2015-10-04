package com.cooby.like.dao;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 
 * @author zengguangwei
 *
 */
public class LikeRecordDao {

	private static final String PRE_LIKE = "LIKE_";
	private static final String PRE_BELIKE = "BELIKED_";
	private static final String PRE_BELIKENUM = "NUM_BELIKE_";

	@Autowired
	private StringRedisTemplate redisTemplate;

	public long countByTarget(String targetId) {
		// BoundValueOperations<String, String> boundValueOps =
		// redisTemplate.boundValueOps(PRE_BELIKENUM + targetId);
		// return Integer.parseInt(boundValueOps.getKey());
		BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(PRE_BELIKENUM);
		BoundZSetOperations<String, String> boundZSetOps = redisTemplate.boundZSetOps("1212");
		BoundListOperations<String, String> boundListOps = redisTemplate.boundListOps("");
		return Long.parseLong(boundHashOps.get(targetId).toString());
	}

	public boolean isTargetBeLikedByUser(String fromId, String targetId) {
		BoundSetOperations<String, String> likeSetOps = redisTemplate.boundSetOps(PRE_LIKE + fromId);
		return likeSetOps.isMember(targetId);
	}

	public void createLike(String fromId, String targetId) {
		// 创建某物被某人喜欢
		BoundSetOperations<String, String> belikedSetOps = redisTemplate.boundSetOps(PRE_BELIKE + targetId);
		belikedSetOps.add(fromId);
		// 创建某物被喜欢次数+1
		BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(PRE_BELIKENUM);
		boundHashOps.increment(targetId, 1);
		// 创建某人喜欢某物
		BoundSetOperations<String, String> likedSetOps = redisTemplate.boundSetOps(PRE_LIKE + fromId);
		likedSetOps.add(targetId);
	}

	public Set<String> getUserLikes(String fromId) {
		BoundSetOperations<String, String> likedSetOps = redisTemplate.boundSetOps(PRE_LIKE + fromId);
		return likedSetOps.members();
	}

	public Set<String> getTargetLikes(String targetId) {
		BoundSetOperations<String, String> belikedSetOps = redisTemplate.boundSetOps(PRE_BELIKE + targetId);
		return belikedSetOps.members();
	}
}
