package com.poscodx.mysite.exception;

public class UesrRepositoryException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public UesrRepositoryException() {
		super("UserRepositoryException Occurs");
	}
	
	public UesrRepositoryException(String message) {
		super(message);
	}
	
}
