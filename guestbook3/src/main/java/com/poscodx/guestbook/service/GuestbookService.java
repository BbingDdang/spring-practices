package com.poscodx.guestbook.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.poscodx.guestbook.repository.GuestbookLogRepository;
import com.poscodx.guestbook.repository.GuestbookRepository;
import com.poscodx.guestbook.vo.GuestbookVo;

@Service
public class GuestbookService {
	
	@Autowired
	private GuestbookRepository guestbookRepository;

	@Autowired
	private GuestbookLogRepository guestbookLogRepository;
	@Autowired
	private DataSource dataSource;

	public List<GuestbookVo> getContentsList() {
		return guestbookRepository.findAll();
	}
	
	public void deleteContents(Long no, String password) {
		
		guestbookLogRepository.update(no);
		guestbookRepository.deleteByNoAndPassword(no, password);
	}
	
	public void addContents(GuestbookVo vo) {
		TransactionSynchronizationManager.initSynchronization();
		Connection conn = DataSourceUtils.getConnection(dataSource);
		
		try {
			///// tx begin//////
			conn.setAutoCommit(false);
			
			int count = guestbookLogRepository.update();
			
			if(count == 0) {
				guestbookLogRepository.insert();
			}
			
			guestbookRepository.insert(vo);
			
			
		} 
		catch (Throwable e) {
			// tx end
			try {
				conn.rollback();
			}
			catch (SQLException ignored) {
			}
		}
	}
}