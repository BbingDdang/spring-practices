package com.poscodx.mysite.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poscodx.mysite.security.Auth;
import com.poscodx.mysite.service.BoardService;
import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@RequestMapping
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
		
		return "board/list";
	}
	
	@Auth
	@GetMapping("/add")
	public String add() {
		return "board/write";
	}
	
	@Auth // access control을 이제 @auth가 함. 하지만 UserVo authUser = (UserVo)session.getAttribute("authUser")를 계속 써야하는 경우도 있음.
	@PostMapping("/add")
	public String add(HttpSession session, BoardVo vo, @RequestParam(value = "no", required=false) Long parentNo) {
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		vo.setUserNo(authUser.getNo());
		if (parentNo == null) {
			boardService.addContents(vo);
			System.out.println(vo + "after add");
		}
		else {
			boardService.addReplies(vo, parentNo);
			System.out.println("reply");
		}
		return "redirect:/board";
	}
	
	@Auth
	@GetMapping("/delete")
	public String delete(HttpSession session, @RequestParam(value = "no") Long no) { 
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) {
			return "redirect:/board";
		}
		boardService.deleteContents(no, authUser.getNo());
		
		return "redirect:/board";
	}
	
	@Auth
	@GetMapping("/modify/{no}")
	public String modify(@PathVariable("no") Long no) {
		return "board/modify";
	}
	
	@Auth
	@PostMapping("/modify/{no}")
	public String modify(HttpSession session, @PathVariable("no") Long no, BoardVo vo) {
		// (@AuthUser UserVo authUser) 를 사용하여 아래 세줄의 코드를 지울 수 있음. (argument resolver)
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) {
			return "redirect:/board";
		}
		boardService.updateContents(vo);
		return "redirect:/board";
		
	}
	
	@Auth
	@GetMapping("/reply")
	public String reply(@RequestParam(value = "no") Long parentNo, Model model) {
		model.addAttribute("no", parentNo);
		return "board/reply";
	}
	
	@GetMapping("/view/{no}")
	public String view(@PathVariable("no") Long no, @RequestParam(value="p") Long page, @RequestParam(value="kwd", required=false) String keyword,Model model) {
		BoardVo vo = boardService.getAll(no);
		model.addAttribute("no", no);
		model.addAttribute("page", page);
		model.addAttribute("keyword", keyword);
		model.addAttribute("vo", vo);
		return "board/view";
	}
	
	
}
