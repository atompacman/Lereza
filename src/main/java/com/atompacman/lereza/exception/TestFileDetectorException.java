package com.atompacman.lereza.exception;

public class TestFileDetectorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TestFileDetectorException() { 
		super(); 
	}

	public TestFileDetectorException(String message) {
		super(message); 
	}

	public TestFileDetectorException(String message, Throwable cause) {
		super(message, cause); 
	}

	public TestFileDetectorException(Throwable cause) { 
		super(cause); 
	}
}
