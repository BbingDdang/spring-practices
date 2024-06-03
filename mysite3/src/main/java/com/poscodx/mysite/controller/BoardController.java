package com.poscodx.mysite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.poscodx.mysite.service.BoardService;

@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;
}
