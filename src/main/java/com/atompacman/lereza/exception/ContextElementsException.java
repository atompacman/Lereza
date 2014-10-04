package com.atompacman.lereza.exception;

public class ContextElementsException extends Exception {

	private static final long serialVersionUID = 1L;
	
	
	public ContextElementsException() { 
		super(); 
	}

	public ContextElementsException(String message) {
		super(message); 
	}

	public ContextElementsException(String message, Throwable cause) {
		super(message, cause); 
	}

	public ContextElementsException(Throwable cause) { 
		super(cause); 
	}
}
