package atompacman.leraza.midi.exception;

public class MidiFileException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	
	public MidiFileException() { 
		super(); 
	}

	public MidiFileException(String message) {
		super(message); 
	}

	public MidiFileException(String message, Throwable cause) {
		super(message, cause); 
	}

	public MidiFileException(Throwable cause) { 
		super(cause); 
	}
	
	public void printStackTrace() {
		super.printStackTrace();
	}
}
