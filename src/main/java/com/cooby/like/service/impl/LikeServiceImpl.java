package com.cooby.like.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cooby.like.dao.LikeRecordDao;
import com.cooby.like.po.LikeRecord;
import com.cooby.like.service.LikeService;

@Service
public class LikeServiceImpl implements LikeService {

	@Autowired
	private LikeRecordDao likeDao;

	@Override
	public long countTargetBelikedNum(String targetId) {
		return likeDao.countByTarget(targetId);
	}


	@Override
	public List<LikeRecord> getUserLikeRecords(String fromId) {
		return null;
	}

	@Override
	public long[] countTargetBelikedNum(String[] targetIds) {
		return null;
	}

	@Override
	public Map<String, Boolean> isLike(String[] targetIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void like(String userId, String targetId) {
		if (likeDao.isTargetBeLikedByUser(userId, targetId)) {
		}
		//可以发起一个异步操作
		likeDao.createLike(userId, targetId);
	}

	@Override
	public Set<String> getUserIdsByTargetId(String targetId) {
		return null;
	}

}
