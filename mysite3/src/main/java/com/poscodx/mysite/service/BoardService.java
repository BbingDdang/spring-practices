package com.poscodx.mysite.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poscodx.mysite.repository.BoardRepository;
import com.poscodx.mysite.vo.BoardVo;

@Service
public class BoardService {
	@Autowired
	private BoardRepository boardRepository;

	public List<BoardVo> getContentsList(Long p) {
		return boardRepository.findAllByPageNum(p);
	}

	public void addContents(BoardVo vo) {
		boardRepository.insert(vo);
	}
	
	public BoardVo getContents(Long no) {
		return boardRepository.findContentsByNo(no);
	}
	
//	public BoardVo getTitles()
	
	public BoardVo getContents(Long boardNo, Long userNo) {
		return null;
	}
	
	public void updateContents(BoardVo vo) {
		
	}
	
	public void deleteContents(Long boardNo, Long userNo) {
		
	}
	
	public List<BoardVo> getContentsList(Long currentPage, String keyword) {
		
//		List<BoardVo> list = null;
//		Map<String, Object> map = null;
//		map.put("list", list);
//		map.put("keyword", "");
//		map.put("totalCount", 0);
//		map.put("listSize", 0);
//		map.put("currentPage", 0);
//		map.put("beginPage", 0);
//		map.put("endPage", 0);
//		map.put("prevPage", 0);
//		map.put("nextPage", 0);
		return boardRepository.findAllByPageNumAndKeyword(currentPage, keyword);
	}
	
}
