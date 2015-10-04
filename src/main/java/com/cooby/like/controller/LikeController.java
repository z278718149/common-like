package com.cooby.like.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooby.like.service.LikeService;

@RestController
@RequestMapping(value = "/likes")
public class LikeController {

	@Autowired
	private LikeService likeService;

	@RequestMapping(value = "/{fromId}/{targetId}", method = RequestMethod.PUT)
	public void like(@PathVariable String fromId, @PathVariable String targetId) {
		likeService.like(fromId, targetId);
	}

	@RequestMapping(value = "/target/{targetId}", method = RequestMethod.GET)
	public Set<String> getTagetLikes(@PathVariable String targetId) {
		return likeService.getUserIdsByTargetId(targetId);
	}

	@RequestMapping(value = "/target/num/{targetId}", method = RequestMethod.GET)
	public long countTargetBelikedNum(@PathVariable String targetId) {
		return likeService.countTargetBelikedNum(targetId);
	}

}
