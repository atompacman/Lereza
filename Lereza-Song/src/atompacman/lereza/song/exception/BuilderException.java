package atompacman.lereza.song.exception;

public class BuilderException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BuilderException() { 
		super(); 
	}

	public BuilderException(String message) {
		super(message); 
	}

	public BuilderException(String message, Throwable cause) {
		super(message, cause); 
	}

	public BuilderException(Throwable cause) { 
		super(cause); 
	}
}