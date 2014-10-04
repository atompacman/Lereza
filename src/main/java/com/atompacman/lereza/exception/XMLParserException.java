package com.atompacman.lereza.exception;

public class XMLParserException extends Exception {

	private static final long serialVersionUID = 1L;

	public XMLParserException() { 
		super(); 
	}

	public XMLParserException(String message) {
		super(message); 
	}

	public XMLParserException(String message, Throwable cause) {
		super(message, cause); 
	}

	public XMLParserException(Throwable cause) { 
		super(cause); 
	}
}