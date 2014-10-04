package com.atompacman.lereza.exception;

public class DatabaseException extends Exception {

	private static final long serialVersionUID = 1L;
	
	
	public DatabaseException() { 
		super(); 
	}

	public DatabaseException(String message) {
		super(message); 
	}

	public DatabaseException(String message, Throwable cause) {
		super(message, cause); 
	}

	public DatabaseException(Throwable cause) { 
		super(cause); 
	}
}