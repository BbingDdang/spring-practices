package com.poscodx.guestbook.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.poscodx.guestbook.vo.GuestbookVo;

@Repository
public class GuestbookRepositoryWithJdbcTemplate {
	private JdbcTemplate jdbcTemplate;
	
	public GuestbookRepositoryWithJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<GuestbookVo> findAll() {
		return jdbcTemplate.query(
				"select no, name, password, contents, reg_date from guestbook order by reg_date desc",
				new RowMapper<GuestbookVo>() {

					@Override
					public GuestbookVo mapRow(ResultSet rs, int rowNum) throws SQLException {
						GuestbookVo vo = new GuestbookVo();
						vo.setNo(rs.getLong(1));
						vo.setName(rs.getString(2));
						vo.setPassword(rs.getString(3));
						vo.setContents(rs.getString(4));
						vo.setReg_time(rs.getString(5));
						return vo;
					}}
				);
	}
	
	public int insert(GuestbookVo vo) {
		return jdbcTemplate.update(
				"insert into guestbook values(null, ?, ?, ?, date_format(now(), '%Y/%m/%d %H:%i:%s'))",
				new Object[] {vo.getName(), vo.getPassword(), vo.getContents()}
				);
	}
	
	public int deleteByNoAndPassword(Long no, String password) {
		return jdbcTemplate.update(
				"delete from guestbook where no = ? and password = ?",
				new Object[] {no, password}
				);

	}
	
}
