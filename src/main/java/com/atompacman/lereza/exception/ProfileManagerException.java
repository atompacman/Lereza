package com.atompacman.lereza.exception;

public class ProfileManagerException extends Exception {

	private static final long serialVersionUID = 1L;

	public ProfileManagerException() { 
		super(); 
	}

	public ProfileManagerException(String message) {
		super(message); 
	}

	public ProfileManagerException(String message, Throwable cause) {
		super(message, cause); 
	}

	public ProfileManagerException(Throwable cause) { 
		super(cause); 
	}
}