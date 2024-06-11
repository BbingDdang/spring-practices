package com.poscodx.mysite.service;

import java.util.ArrayList;
import java.util.HashMap;
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

	public Map<String, Object> getContentsList(Long p) {
		List<BoardVo> list = boardRepository.findAllByPageNum(p);
		Long totalPage = boardRepository.getTotalPageCount();
		Long base = (p - 1)/5;
		List<PageInfo> entry = new ArrayList<>();
		for (int i = 1; i <= 5;i++) {
			PageInfo pageInfo = new PageInfo();
			pageInfo.setPageNo(i + base*5);
			pageInfo.setActive(i + base*5 <= totalPage);	
			entry.add(pageInfo);
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("totalPage", totalPage);
		map.put("base", base);
		map.put("entry", entry);
		
		
		return map;
	}

	public void addContents(BoardVo vo) {
		System.out.println(vo +" : addContents1");
		boardRepository.insert(vo);
		System.out.println(vo +" : addContents2");
	}
	
	public void addReplies(BoardVo vo, Long parentNo) {
		System.out.println(parentNo);
		BoardVo parentVo = boardRepository.findAllByNo(parentNo);
		System.out.println(parentVo);
		Long gno = parentVo.getGroupNo();
		Long ono = parentVo.getOrderNo();
		Long depth = parentVo.getDepth();
		
		vo.setGroupNo(gno);
		vo.setOrderNo(ono+1);
		vo.setDepth(depth+1);
		System.out.println(vo);
		boardRepository.updateOrderNo(vo);
		boardRepository.insertReply(vo, parentNo);
	}
	
	public BoardVo getTitleAndContents(Long no) {
		return boardRepository.findTitleAndContentsByNo(no);
	}
	
	public BoardVo getAll(Long no) {
//		BoardVo vo = boardRepository.findAllByNo(no);
		boardRepository.increaseHit(no);
		
		return boardRepository.findAllByNo(no);
	}
//	public BoardVo getTitles(Long no) {
//		return boardRepository.findTitleByNo(no);
//	}
//	
	public BoardVo getContents(Long boardNo, Long userNo) {
		return null;
	}
	
	public void updateContents(BoardVo vo) {
		boardRepository.update(vo);
		
	}
	
	public void deleteContents(Long boardNo, Long userNo) {
		boardRepository.deleteByNo(boardNo);
	}
	
	public Map<String, Object> getContentsList(Long currentPage, String keyword) {
		
		List<BoardVo> list = boardRepository.findAllByPageNumAndKeyword(currentPage, keyword);
		Long totalPage = boardRepository.getTotalPageCountByKeyword(keyword);
		Long base = (currentPage - 1)/5;
		List<PageInfo> entry = new ArrayList<>();
		for (int i = 1; i <= 5;i++) {
			PageInfo pageInfo = new PageInfo();
			pageInfo.setPageNo(i + base*5);
			pageInfo.setActive(i + base*5 <= totalPage);
			entry.add(pageInfo);
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("totalPage", totalPage);
		map.put("base", base);
		map.put("entry", entry);
		return map;
	}
	
	public static class PageInfo {
		private boolean active;
		private Long pageNo;
		public boolean isActive() {
			return active;
		}
		public void setActive(boolean active) {
			this.active = active;
		}
		public Long getPageNo() {
			return pageNo;
		}
		public void setPageNo(Long pageNo) {
			this.pageNo = pageNo;
		}
	}
}
