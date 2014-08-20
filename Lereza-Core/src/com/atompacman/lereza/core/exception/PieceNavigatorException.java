package com.atompacman.lereza.core.exception;

public class PieceNavigatorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PieceNavigatorException() { 
		super(); 
	}

	public PieceNavigatorException(String message) {
		super(message); 
	}

	public PieceNavigatorException(String message, Throwable cause) {
		super(message, cause); 
	}

	public PieceNavigatorException(Throwable cause) { 
		super(cause); 
	}
}