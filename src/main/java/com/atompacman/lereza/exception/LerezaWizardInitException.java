package com.atompacman.lereza.exception;

public class LerezaWizardInitException extends Exception {

	private static final long serialVersionUID = 1L;
	
	
	public LerezaWizardInitException() { 
		super(); 
	}

	public LerezaWizardInitException(String message) {
		super(message); 
	}

	public LerezaWizardInitException(String message, Throwable cause) {
		super(message, cause); 
	}

	public LerezaWizardInitException(Throwable cause) { 
		super(cause); 
	}
}
