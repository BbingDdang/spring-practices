package com.poscodx.mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.poscodx.mysite.vo.BoardVo;

@Repository
public class BoardRepository {

	private Connection getConnection() throws SQLException {
		Connection conn = null;

		try {
			Class.forName("org.mariadb.jdbc.Driver");
			
			String url = "jdbc:mariadb://192.168.64.10:3306/webdb?charset=utf8";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} 
		
		return conn;
	}
	
	// insert
	public int insert(BoardVo vo) {
		int result = 0;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt1 = conn.prepareStatement("insert into board (no, title, contents, hit, reg_date, g_no, o_no, depth, user_no)"
						+ "select null, ?, ?, 0, date_format(now(), '%Y/%m/%d %H:%i:%s'), coalesce(max(g_no), 0) + 1, 1, 0, ? from board");
				PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
				) {
				
				pstmt1.setString(1, vo.getTitle());
				pstmt1.setString(2, vo.getContents());
				pstmt1.setLong(3, vo.getUserNo());
				result = pstmt1.executeUpdate();
				
				ResultSet rs = pstmt2.executeQuery();
				vo.setNo(rs.next() ? rs.getLong(1) : null);
				rs.close();
	
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;
	}
	
	// reply insert
	public int replyInsert(BoardVo vo) {
		int result = 0;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt1 = conn.prepareStatement("update board set o_no = o_no+1 where g_no = ? and o_no >=?");
				PreparedStatement pstmt2 = conn.prepareStatement("insert into board values(null, ?, ?, 0, now(), ?, ?, ?, ?)");
				
				) {
				
				
				pstmt1.setLong(1, vo.getGroupNo());
				pstmt1.setLong(2, vo.getOrderNo());
				result = pstmt1.executeUpdate();
				
				
				pstmt2.setString(1, vo.getTitle());
				pstmt2.setString(2, vo.getContents());
				pstmt2.setLong(3, vo.getGroupNo()); // gno
				pstmt2.setLong(4, vo.getOrderNo()); // ono
				pstmt2.setLong(5, vo.getDepth()); // depth
				pstmt2.setLong(6, vo.getUserNo()); // userno
				
				result = pstmt2.executeUpdate();
				
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;
	}
	
	// update
	public int update(BoardVo vo) {
		int result = 0;
		
		try (
				Connection conn = getConnection();
				
				PreparedStatement pstmt1 = conn.prepareStatement("update board set contents = ? where no = ?");
				PreparedStatement pstmt2 = conn.prepareStatement("update board set title = ? where no = ?");
				PreparedStatement pstmt3 = conn.prepareStatement("update board set title = ?, contents = ? where no = ?");
				) {
				if ("".equals(vo.getTitle())) {
					pstmt1.setString(1, vo.getContents());
					pstmt1.setLong(2, vo.getNo());
					result = pstmt1.executeUpdate();
				}
				else if ("".equals(vo.getContents())) {
					pstmt2.setString(1, vo.getTitle());
					pstmt2.setLong(2, vo.getNo());
					result = pstmt2.executeUpdate();
				}
				else {
					pstmt3.setString(1, vo.getTitle());
					pstmt3.setString(2, vo.getContents());
					pstmt3.setLong(3, vo.getNo());
					result = pstmt3.executeUpdate();
				}
				
		}
		catch (SQLException e) {
			System.out.println();
		}
		return result;
	}
	
	// findAll
	public List<BoardVo> findAllByPageNum(Long pageNum) {
		List<BoardVo> result = new ArrayList<>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			//1. JDBC Driver 로딩
			Class.forName("org.mariadb.jdbc.Driver");
			
			//2. 연결하기
			String url = "jdbc:mariadb://192.168.64.10:3306/webdb?charset=utf8";
			conn = DriverManager.getConnection(url, "webdb", "webdb");

			//3. Statement 준비
			String sql =
				"   select b.*, u.name" + 
			    "     from board b, user u" + 
				"		where u.no = b.user_no" +
				" order by b.g_no desc, b.o_no asc" + 
				" limit ?,5";
			pstmt = conn.prepareStatement(sql);
			
			//4. binding
			pstmt.setLong(1, (pageNum-1)*5);
			//5. SQL 실행
			rs = pstmt.executeQuery();
			
			//6. 결과 처리
			while(rs.next()) {
				Long no = rs.getLong(1);
				String title = rs.getString(2);
				String contents = rs.getString(3);
				Long hits = rs.getLong(4);
				String regDate = rs.getString(5);
				Long groupNo = rs.getLong(6);
				Long orderNo = rs.getLong(7);
				Long depth = rs.getLong(8);
				Long userNo = rs.getLong(9);
				String userName = rs.getString(10);
				
				
				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setHit(hits);
				vo.setReg_date(regDate);
				vo.setGroupNo(groupNo);
				vo.setOrderNo(orderNo);
				vo.setDepth(depth);
				vo.setUserNo(userNo);
				vo.setUserName(userName);
				
				result.add(vo);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				
				if(pstmt != null) {
					pstmt.close();
				}
				
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	
	}
	
	
	
	// find gno, ono, depth
	public List<BoardVo> findGnoOnoDepthByNo(Long no) {
		List<BoardVo> result = new ArrayList<>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			//1. JDBC Driver 로딩
			Class.forName("org.mariadb.jdbc.Driver");
			
			//2. 연결하기
			String url = "jdbc:mariadb://192.168.64.10:3306/webdb?charset=utf8";
			conn = DriverManager.getConnection(url, "webdb", "webdb");

			//3. Statement 준비
			String sql =
				"   select g_no, o_no, depth" + 
			    "     from board" + 
				"		where no = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, no);
			
			//4. binding
			
			//5. SQL 실행
			rs = pstmt.executeQuery();
			
			//6. 결과 처리
			while(rs.next()) {
				Long gNo = rs.getLong(1);
				Long oNo = rs.getLong(2);
				Long depth = rs.getLong(3);
				
				
				BoardVo vo = new BoardVo();
				vo.setGroupNo(gNo);
				vo.setOrderNo(oNo);
				vo.setDepth(depth);
				
				result.add(vo);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				
				if(pstmt != null) {
					pstmt.close();
				}
				
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	
	}
	
	
	public String findTitleByNo(Long no) {
		String result = null;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select title from board where no = ?");
				) {
			
				pstmt.setLong(1, no);
				
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					
					result = rs.getString(1);
					
				}
				rs.close();
	
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;
	}
	
	public Long findUserNoByNo(Long no) {
		Long result = null;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select user_no from board where no = ?");
				) {
			
				pstmt.setLong(1, no);
				
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					
					result = rs.getLong(1);
					
				}
				rs.close();
	
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;
	}
	
	public String findContentsByNo(Long no) {
		String result = null;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select contents from board where no = ?");
				) {
			
				pstmt.setLong(1, no);
				
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					
					result = rs.getString(1);
					
				}
				rs.close();
	
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;
	}
	
	// delete
	public boolean deleteByNo(Long no) {
		boolean result = false;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			//1. JDBC Driver 로딩
			Class.forName("org.mariadb.jdbc.Driver");
			
			//2. 연결하기
			String url = "jdbc:mariadb://192.168.64.10:3306/webdb?charset=utf8";
			conn = DriverManager.getConnection(url, "webdb", "webdb");

			//3. Statement 준비
			String sql = "delete from board where no=?";
			pstmt = conn.prepareStatement(sql);

			//4. binding
			pstmt.setLong(1, no);
			
			//4. SQL 실행
			int count = pstmt.executeUpdate();
			
			//5. 결과 처리
			result = count == 1;
			
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	public Long getTotalPageCount() {
		Long cnt = null;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select count(*) from board");
				) {
			
				ResultSet rs = pstmt.executeQuery();
				
				if (rs.next()) {
					
					cnt = rs.getLong(1);
					
				}
				rs.close();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		
		return (cnt/5) + 1;
	}

	public void increaseHit(Long num) {
		try (
				Connection conn = getConnection();
				
				PreparedStatement pstmt = conn.prepareStatement("update board set hit = hit+1 where no = ?");
				) {
				pstmt.setLong(1, num);
				
				pstmt.executeUpdate();
				
		}
		catch (SQLException e) {
			System.out.println();
		}
		
	}

	public Long getTotalPageCountByKeyword(String keyword) {
		Long cnt = null;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select count(*) from board where title like ?");
				) {
			
				pstmt.setString(1, "%" + keyword + "%");
				ResultSet rs = pstmt.executeQuery();
				
				if (rs.next()) {
					
					cnt = rs.getLong(1);
					
				}
				rs.close();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		
		return (cnt/5) + 1;
	}

	public List<BoardVo> findAllByPageNumAndKeyword(Long pageNum, String keyword) {
		List<BoardVo> result = new ArrayList<>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			//1. JDBC Driver 로딩
			Class.forName("org.mariadb.jdbc.Driver");
			
			//2. 연결하기
			String url = "jdbc:mariadb://192.168.64.10:3306/webdb?charset=utf8";
			conn = DriverManager.getConnection(url, "webdb", "webdb");

			//3. Statement 준비
			String sql =
				"   select b.*, u.name" + 
			    "     from board b, user u" + 
				"		where u.no = b.user_no" +
			    "		and title like ?" +
				" order by b.g_no desc, b.o_no asc" + 
				" limit ?,5";
			pstmt = conn.prepareStatement(sql);
			
			//4. binding
			pstmt.setString(1, "%" + keyword + "%");
			pstmt.setLong(2, (pageNum-1)*5);
			//5. SQL 실행
			rs = pstmt.executeQuery();
			
			//6. 결과 처리
			while(rs.next()) {
				Long no = rs.getLong(1);
				String title = rs.getString(2);
				String contents = rs.getString(3);
				Long hits = rs.getLong(4);
				String regDate = rs.getString(5);
				Long groupNo = rs.getLong(6);
				Long orderNo = rs.getLong(7);
				Long depth = rs.getLong(8);
				Long userNo = rs.getLong(9);
				String userName = rs.getString(10);
				
				
				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setHit(hits);
				vo.setReg_date(regDate);
				vo.setGroupNo(groupNo);
				vo.setOrderNo(orderNo);
				vo.setDepth(depth);
				vo.setUserNo(userNo);
				vo.setUserName(userName);
				
				result.add(vo);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				
				if(pstmt != null) {
					pstmt.close();
				}
				
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
}
