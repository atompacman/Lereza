package com.atompacman.lereza.exception;

public class MIDIFileReaderException extends Exception {

	private static final long serialVersionUID = 1L;
	
	
	public MIDIFileReaderException() { 
		super(); 
	}

	public MIDIFileReaderException(String message) {
		super(message); 
	}

	public MIDIFileReaderException(String message, Throwable cause) {
		super(message, cause); 
	}

	public MIDIFileReaderException(Throwable cause) { 
		super(cause); 
	}
}
