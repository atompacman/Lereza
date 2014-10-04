package com.atompacman.lereza.exception;

public class PieceBuilderException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PieceBuilderException() { 
		super(); 
	}

	public PieceBuilderException(String message) {
		super(message); 
	}

	public PieceBuilderException(String message, Throwable cause) {
		super(message, cause); 
	}

	public PieceBuilderException(Throwable cause) { 
		super(cause); 
	}
}
