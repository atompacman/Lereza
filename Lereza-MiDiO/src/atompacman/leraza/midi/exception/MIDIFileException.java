package atompacman.leraza.midi.exception;

public class MIDIFileException extends Exception {

	private static final long serialVersionUID = 1L;
	
	
	public MIDIFileException() { 
		super(); 
	}

	public MIDIFileException(String message) {
		super(message); 
	}

	public MIDIFileException(String message, Throwable cause) {
		super(message, cause); 
	}

	public MIDIFileException(Throwable cause) { 
		super(cause); 
	}
}
