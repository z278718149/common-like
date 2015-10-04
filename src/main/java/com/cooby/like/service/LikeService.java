package com.cooby.like.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cooby.like.po.LikeRecord;

public interface LikeService {
	/**
	 * 获得Target被喜欢的次数
	 * @param targetId
	 * @return
	 */
	public long countTargetBelikedNum(String targetId);
	
	/**
	 * 多个Target被喜欢的次数
	 * @param targetIds
	 * @return
	 */
	public long[] countTargetBelikedNum(String[] targetIds);
	
	/**
	 * 是否喜欢过
	 * @param targetIds
	 * @return
	 */
	public Map<String, Boolean> isLike(String[] targetIds);

	/**
	 * 喜欢
	 * @param userId
	 * @param targetId
	 */
	public void like(String userId, String targetId);

	/**
	 * 获得用户喜欢列表
	 * @param userId
	 * @return
	 */
	public List<LikeRecord> getUserLikeRecords(String userId);

	/**
	 * 获得Target被喜欢的UserId列表
	 * 可能会比较大[1万]
	 * @param targetId
	 * @return
	 */
	public Set<String> getUserIdsByTargetId(String targetId);
}
