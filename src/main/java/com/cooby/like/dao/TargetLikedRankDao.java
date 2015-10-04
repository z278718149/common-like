package com.cooby.like.dao;

import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

@Service
public class TargetLikedRankDao {

	private static final String KEY = "TargetLikedRank_" ;
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	/**
	 * target增加一个喜欢
	 * @param targetId
	 */
	public void like(String targetId){
		BoundZSetOperations<String, String> boundZSetOps = redisTemplate.boundZSetOps(KEY);
		boundZSetOps.incrementScore(targetId, 1);
	}
	
	public void rank(){
		BoundZSetOperations<String, String> boundZSetOps = redisTemplate.boundZSetOps(KEY);
		Set<TypedTuple<String>> rangeWithScores = boundZSetOps.rangeWithScores(0, 100);
		Iterator<TypedTuple<String>> iterator = rangeWithScores.iterator();
		while (iterator.hasNext()) {
			TypedTuple<String> next = iterator.next();
			String value = next.getValue();
			Double score = next.getScore();
		}
	}
}
