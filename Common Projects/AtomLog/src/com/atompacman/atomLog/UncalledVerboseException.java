package com.atompacman.atomLog;

public class UncalledVerboseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	
	public UncalledVerboseException() { 
		super(); 
	}

	public UncalledVerboseException(String message) {
		super(message); 
	}

	public UncalledVerboseException(String message, Throwable cause) {
		super(message, cause); 
	}

	public UncalledVerboseException(Throwable cause) { 
		super(cause); 
	}
	
	public void printStackTrace() {
		super.printStackTrace();
	}
}
