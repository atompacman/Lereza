package com.atompacman.lereza.exception;

public class MIDIFileIndexParserException extends Exception {

	private static final long serialVersionUID = 1L;
	
	
	public MIDIFileIndexParserException() { 
		super(); 
	}

	public MIDIFileIndexParserException(String message) {
		super(message); 
	}

	public MIDIFileIndexParserException(String message, Throwable cause) {
		super(message, cause); 
	}

	public MIDIFileIndexParserException(Throwable cause) { 
		super(cause); 
	}
}