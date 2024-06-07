package com.poscodx.mysite.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.poscodx.mysite.vo.BoardVo;

@Repository
public class BoardRepository {
	private SqlSession sqlSession;
	
	public BoardRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public int insert(BoardVo vo) {
		return sqlSession.insert("board.insert", vo);
	}
	// reply
	public int updateOrderNo(BoardVo vo) {
		return sqlSession.update("board.updateOrderNo", vo);
//				PreparedStatement pstmt1 = conn.prepareStatement("update board set o_no = o_no+1 where g_no = ? and o_no >=?");
//				PreparedStatement pstmt2 = conn.prepareStatement("insert into board values(null, ?, ?, 0, now(), ?, ?, ?, ?)");
	}
	public int insertReply(BoardVo vo) {
		return sqlSession.insert("board.insertReply", vo);
	}
	////////////////////////////////////////////////////////////////////////
	
	public int update(BoardVo vo) {
		return sqlSession.update("board.update", vo);
	}

	public List<BoardVo> findAllByPageNum(Long pageNum) {
		return sqlSession.selectList("board.findAllByPageNum", (pageNum-1)*5);
	
	}

	public List<BoardVo> findGnoOnoDepthByNo(Long no) {
		return sqlSession.selectOne("board.findGnoOnoDepthByNo");
	}
	
	public String findTitleByNo(Long no) {
		return sqlSession.selectOne("board.findTitleByNo", no);
	}
	
	public Long findUserNoByNo(Long no) {
		return sqlSession.selectOne("board.findUserNoByNo", no);
	}
	
	public BoardVo findContentsByNo(Long no) {
		return sqlSession.selectOne("board.findContentsByNo", no);
	}
	
	public int deleteByNo(Long no) {
		return sqlSession.delete("board.deleteByNo", no);
	}

	public Long getTotalPageCount() {
		long cnt = sqlSession.selectOne("board.getTotalPageCount");
		return (cnt/5) + 1;
	}

	public void increaseHit(Long num) {
		sqlSession.update("board.increaseHit", num);
	}

	public Long getTotalPageCountByKeyword(String keyword) {
		Long cnt = sqlSession.selectOne("board.getTotalPageCountByKeyword", keyword);
		return (cnt/5) + 1;
	}

	public List<BoardVo> findAllByPageNumAndKeyword(Long pageNum, String keyword) {
		return sqlSession.selectList("board.findAllByPageNumAndKeyword", Map.of("pageNum", (pageNum-1)*5, "keyword", keyword));
	}
}
