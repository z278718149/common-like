package com.cooby.like.dao;

import java.util.Set;

/**
 * 
 * @author zengguangwei
 *
 */
public interface LikeDao {

	public long countByTarget(String targetId);

	public boolean isTargetBeLikedByUser(String fromId,String targetId);

	public void createLike(String fromId, String targetId);

	public Set<String> getUserLikes(String fromId);
	
	public Set<String> getTargetLikes(String targetId);
}
