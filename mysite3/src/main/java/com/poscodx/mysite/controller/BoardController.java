package com.poscodx.mysite.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poscodx.mysite.service.BoardService;
import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@GetMapping
	public String index(Model model, @RequestParam(value="p", defaultValue = "1", required=false) Long page, @RequestParam(value="kwd",required=false) String keyword) {
		List<BoardVo> list = null;
		if (keyword == null || keyword == "") {
			list = boardService.getContentsList(page);
		}
		else {
			list = boardService.getContentsList(page, keyword);
		}
		System.out.println(list);
		model.addAttribute("page", page);
		model.addAttribute("keyword", keyword);
		model.addAttribute("list", list);
		return "board/list";
	}
	
	@PostMapping("/add")
	public String add(BoardVo vo) {
		boardService.addContents(vo);
		return "";
	}
	
	@DeleteMapping("/delete/{no}")
	public String delete(HttpSession session, @PathVariable("no") Long no) { 
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) {
			return "redirect:/";
		}
		
		return "";
	}
	
	public String modify() {
		return "";
	}
	
	public String reply() {
		return "";
	}
	
	@GetMapping("/view/{no}")
	public String view(@PathVariable("no") Long no, @RequestParam(value="p") Long page, @RequestParam(value="kwd", required=false) String keyword,Model model) {
		BoardVo vo = boardService.getContents(no);
		model.addAttribute("page", page);
		model.addAttribute("keyword", keyword);
		model.addAttribute("vo", vo);
		return "board/view";
	}
	
	
}
