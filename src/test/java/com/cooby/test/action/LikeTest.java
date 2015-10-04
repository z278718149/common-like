package com.cooby.test.action;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONArray;
import com.cooby.like.Application;
import com.zeng.util.HttpUtil;
import com.zeng.util.Util;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class LikeTest {

	private static final String ROOT = "likes";

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}

	@Test
	public void testLike() throws Exception {
		// 创建
		String fromId = "User" + String.valueOf(System.currentTimeMillis());
		String targetId = "Traget" + String.valueOf(System.currentTimeMillis());
		String uri = HttpUtil.getRestPath(ROOT, fromId, targetId);
		System.out.println(uri);
		// 创建一个Like
		Assert.assertEquals("true", like(fromId, targetId));
	}

	@Test
	public void testCountTargetLikedNum() throws Exception {
		// 创建
		String fromId1 = "User" + String.valueOf(System.currentTimeMillis());
		String fromId2 = "User" + String.valueOf(System.currentTimeMillis() + 100);
		String targetId = "Traget" + String.valueOf(System.currentTimeMillis());
		like(fromId1, targetId);
		like(fromId2, targetId);
		//
		Assert.assertEquals(2, getTargetLikedNum(targetId));
	}

	@Test
	public void testUserLikes() throws Exception {
		String fromId = "User" + String.valueOf(System.currentTimeMillis());
		long currentTimeMillis = System.currentTimeMillis();
		int num = 10;
		for (int i = 0; i < num; i++) {
			String targetId = "Traget" + String.valueOf(currentTimeMillis + i * 10);
			like(fromId, targetId);
		}
		List<String> liset = getUserLikes(fromId);
		List<String> src2 = new ArrayList<String>();
		if (Util.isSameArray(liset, src2)) {
			Assert.fail();
		}
	}

	@Test
	public void testTargetLikedBySameUser() throws Exception {
		// 创建
		String fromId = "User" + String.valueOf(System.currentTimeMillis());
		String targetId = "Traget" + String.valueOf(System.currentTimeMillis());
		// 创建一个Like
		Assert.assertEquals("true", like(fromId, targetId));
		// 再次Like 会报false
		Assert.assertEquals("false", like(fromId, targetId));
		// 还是被1人喜欢
		Assert.assertEquals(1, getTargetLikedNum(targetId));
		// 用户喜欢了一个target
		List<String> userLikes = getUserLikes(fromId);
		Assert.assertEquals(1, userLikes.size());
		Assert.assertEquals(targetId, userLikes.get(0));
		// target beliked by one user
		List<String> targetLikes = getTargetLikes(targetId);
		Assert.assertEquals(1, targetLikes.size());
		Assert.assertEquals(fromId, targetLikes.get(0));
		Assert.assertEquals(1, getTargetLikedNum(targetId));
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTargetLikedByUsers() throws Exception {
		// 创建
		String targetId = "Traget" + String.valueOf(System.currentTimeMillis());
		long time = System.currentTimeMillis();
		String fromId1 = "User" + String.valueOf(time);
		String fromId2 = "User" + String.valueOf(time + 100);
		// 创建一个Like
		Assert.assertEquals("true", like(fromId1, targetId));
		// 再次Like 会报false
		Assert.assertEquals("false", like(fromId1, targetId));
		// 另一个用户也喜欢了
		like(fromId2, targetId);
		// target beliked by 2 users
		List<String> targetLikes = getTargetLikes(targetId);
		Assert.assertEquals(2, targetLikes.size());
		Util.isSameArray(targetLikes, Arrays.asList(fromId1, fromId2));
		Assert.assertEquals(2, getTargetLikedNum(targetId));
		// 用户1喜欢了一个target
		List<String> userLikes = getUserLikes(fromId1);
		Assert.assertEquals(1, userLikes.size());
		Assert.assertEquals(targetId, userLikes.get(0));
		// 用户2喜欢了一个target
		userLikes = getUserLikes(fromId2);
		Assert.assertEquals(1, userLikes.size());
		Assert.assertEquals(targetId, userLikes.get(0));
	}

	private String like(String fromId, String targetId) throws Exception {
		String uri = HttpUtil.getRestPath(ROOT, fromId, targetId);
		MockHttpServletRequestBuilder method = MockMvcRequestBuilders.put(uri);
		ResultActions resultActions = this.mvc.perform(method);
		resultActions.andExpect(status().isOk());
		resultActions.andDo(MockMvcResultHandlers.print());
		resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
		MvcResult result = resultActions.andReturn();
		String content = result.getResponse().getContentAsString();
		return content;
	}

	private List<String> getUserLikes(String fromId) throws Exception {
		String uri = HttpUtil.getRestPath(ROOT, "from", fromId);
		MockHttpServletRequestBuilder method = MockMvcRequestBuilders.get(uri);
		ResultActions resultActions = this.mvc.perform(method);
		resultActions.andExpect(status().isOk());
		resultActions.andDo(MockMvcResultHandlers.print());
		MvcResult result = resultActions.andReturn();
		String content = result.getResponse().getContentAsString();
		List<String> liset = JSONArray.parseArray(content, String.class);
		return liset;
	}

	private List<String> getTargetLikes(String targetId) throws Exception {
		String uri = HttpUtil.getRestPath(ROOT, "target", targetId);
		MockHttpServletRequestBuilder method = MockMvcRequestBuilders.get(uri);
		ResultActions resultActions = this.mvc.perform(method);
		resultActions.andExpect(status().isOk());
		resultActions.andDo(MockMvcResultHandlers.print());
		MvcResult result = resultActions.andReturn();
		String content = result.getResponse().getContentAsString();
		List<String> liset = JSONArray.parseArray(content, String.class);
		return liset;
	}

	private long getTargetLikedNum(String targetId) throws Exception {
		String uri = HttpUtil.getRestPath(ROOT, "target", "num", targetId);
		MockHttpServletRequestBuilder method = MockMvcRequestBuilders.get(uri);
		ResultActions resultActions = this.mvc.perform(method);
		resultActions.andExpect(status().isOk());
		resultActions.andDo(MockMvcResultHandlers.print());
		MvcResult result = resultActions.andReturn();
		String content = result.getResponse().getContentAsString();
		return Long.parseLong(content);
	}
}
