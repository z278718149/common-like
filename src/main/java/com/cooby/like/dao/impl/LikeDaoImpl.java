package com.cooby.like.dao.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.cooby.like.dao.LikeDao;

/**
 * <pre>
 * Like 用户喜欢的
 * Set
 * key: Like_fromId --> Like_User123
 * value:targetIds --> [targetId1,targetId2……]
 * 
 * Beliked target被喜欢的
 * Set
 * key: Beliked_targetId --> Beliked_product123
 * value:fromIds --> [user1,user2……]
 * 
 * BeLikeNum
 * key:BeLikeNum_targetId --> product123
 * value: num --> 1
 * </pre>
 * 
 * @author zengguangwei
 *
 */
@Service
public class LikeDaoImpl implements LikeDao {

	private static final String PRE_LIKE = "LIKE_";
	private static final String PRE_BELIKE = "BELIKED_";
	private static final String PRE_BELIKENUM = "NUM_BELIKE_";

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Override
	public long countByTarget(String targetId) {
		//		BoundValueOperations<String, String> boundValueOps = redisTemplate.boundValueOps(PRE_BELIKENUM + targetId);
		//		return Integer.parseInt(boundValueOps.getKey());
		BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(PRE_BELIKENUM);
		BoundZSetOperations<String, String> boundZSetOps = redisTemplate.boundZSetOps("1212");
		BoundListOperations<String, String> boundListOps = redisTemplate.boundListOps("");
		return Long.parseLong(boundHashOps.get(targetId).toString());
	}

	@Override
	public boolean isTargetBeLikedByUser(String fromId, String targetId) {
		BoundSetOperations<String, String> likeSetOps = redisTemplate.boundSetOps(PRE_LIKE + fromId);
		return likeSetOps.isMember(targetId);
	}

	@Override
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

	@Override
	public Set<String> getUserLikes(String fromId) {
		BoundSetOperations<String, String> likedSetOps = redisTemplate.boundSetOps(PRE_LIKE + fromId);
		return likedSetOps.members();
	}

	@Override
	public Set<String> getTargetLikes(String targetId) {
		BoundSetOperations<String, String> belikedSetOps = redisTemplate.boundSetOps(PRE_BELIKE + targetId);
		return belikedSetOps.members();
	}

}
