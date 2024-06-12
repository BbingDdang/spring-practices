package com.poscodx.mysite.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.poscodx.mysite.security.Auth;
import com.poscodx.mysite.service.BoardService;
import com.poscodx.mysite.service.FileUploadService;
import com.poscodx.mysite.service.GuestbookService;
import com.poscodx.mysite.service.SiteService;
import com.poscodx.mysite.service.UserService;
import com.poscodx.mysite.vo.GuestbookVo;
import com.poscodx.mysite.vo.SiteVo;
import com.poscodx.mysite.vo.UserVo;


@Controller
@Auth(role="ADMIN")
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private GuestbookService guestbookService;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@RequestMapping("")
	public String main(Model model) {
		SiteVo vo = siteService.getSite();
		model.addAttribute("siteVo", vo);
		return "admin/main";
	}
	
	@RequestMapping("/main/update")
	public String update(SiteVo vo, MultipartFile file) {
		
		String profile = fileUploadService.restore(file);
		if(profile != null) {
			vo.setProfile(profile);
		}
		
		siteService.updateSite(vo);
		servletContext.setAttribute("sitevo", vo);
		return "redirect:/admin";
	}
	
	@RequestMapping("/guestbook")
	public String guestbook(Model model) {
		List<GuestbookVo> list = guestbookService.getContentsList();
		model.addAttribute("list", list);
		return "admin/guestbook";
	}
	
	@RequestMapping(value = "/guestbook/delete/{no}", method = RequestMethod.GET)
	public String delete(@PathVariable("no") Long no) {
		siteService.deleteGuestbook(no);
		return "redirect:/admin/guestbook";
	}
	
	@RequestMapping("/board")
	public String index(Model model, @RequestParam(value="p", defaultValue = "1", required=false) Long page, @RequestParam(value="kwd",required=false) String keyword) {
		Map<String, Object> map = null;

		if (keyword == null || keyword == "") {
			map = boardService.getContentsList(page);
		}
		else {
			map = boardService.getContentsList(page, keyword);
		}
		
		model.addAttribute("page", page);
		model.addAttribute("keyword", keyword);
		model.addAllAttributes(map);
		
		return "admin/board";
	}
	
	@RequestMapping("/user")
	public String user(Model model) {
		List<UserVo> list = userService.getUserList();
		model.addAttribute("list", list);
		
		return "admin/user";
	}
	
}
