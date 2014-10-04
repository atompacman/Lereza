package com.atompacman.lereza.exception;

public class ProfilerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProfilerException() { 
		super(); 
	}

	public ProfilerException(String message) {
		super(message); 
	}

	public ProfilerException(String message, Throwable cause) {
		super(message, cause); 
	}

	public ProfilerException(Throwable cause) { 
		super(cause); 
	}
}