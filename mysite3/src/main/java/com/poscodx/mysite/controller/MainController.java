package com.poscodx.mysite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poscodx.mysite.service.SiteService;
import com.poscodx.mysite.vo.UserVo;

@Controller
public class MainController {
	
	private SiteService siteService;
	
	public MainController(SiteService siteService) {
		this.siteService = siteService;
	}
	
	@RequestMapping({"/", "/main"})
	public String index() {
		return "main/index";
	}
	
	@ResponseBody
	@RequestMapping("/mgs01")
	public String message01() {
		return "Hello World!";
	}
	
	@ResponseBody
	@RequestMapping("/mgs02")
	public String message02(String name) {
		return "하이~!" + name;
	}
	
	@ResponseBody
	@RequestMapping("/mgs03")
	public Object message03() {
		UserVo vo = new UserVo();
		vo.setNo(1L);
		vo.setName("bbingddang");
		vo.setEmail("1@1");
		vo.setGender("male");
		return vo;
	}
	
}
