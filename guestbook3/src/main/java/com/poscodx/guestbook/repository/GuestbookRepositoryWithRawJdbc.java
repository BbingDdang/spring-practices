package com.poscodx.guestbook.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.poscodx.guestbook.vo.GuestbookVo;

@Repository
public class GuestbookRepositoryWithRawJdbc {
	private DataSource dataSource;
	
	public GuestbookRepositoryWithRawJdbc(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
	public int insert(GuestbookVo vo) {
		int result = 0;
		
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("insert into guestbook values(null, ?, ?, ?, date_format(now(), '%Y/%m/%d %H:%i:%s'))");
			){
		
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getContents());
			//pstmt.setString(4, vo.getReg_time());
			//6. 결과 처리
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} 
		
		return result;
	}

	public int deleteByNoAndPassword(Long no, String password) {
		int result = 0;
		
		try {
			Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("delete from guestbook where no=? and password=?");
			//4. binding
			pstmt.setLong(1, no);
			pstmt.setString(2, password);
			//5. 결과 처리
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;
	}

	public List<GuestbookVo> findAll() {
		List<GuestbookVo> result = new ArrayList<>();

		ResultSet rs = null;
		
		try {
			Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("   select no, name, password, contents, reg_date" + 
				    "     from guestbook" + 
					" order by reg_date desc");

			rs = pstmt.executeQuery();
			
			//6. 결과 처리
			while(rs.next()) {
				Long no = rs.getLong(1);
				String name = rs.getString(2);
				String password = rs.getString(3);
				String contents = rs.getString(4);
				String regTime = rs.getString(5);
				
				GuestbookVo vo = new GuestbookVo();
				vo.setNo(no);
				vo.setName(name);
				vo.setPassword(password);
				vo.setContents(contents);
				vo.setReg_time(regTime);
				
				result.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} 
		
		return result;
	
	}

}
