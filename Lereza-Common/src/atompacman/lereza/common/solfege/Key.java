package atompacman.lereza.common.solfege;

public class Key {
	private NoteName noteName;
	private Mode mode;
	
	public Key() {
		this.noteName = null;
		this.mode = null;
	}
	
	public NoteName getNoteName() {
		return noteName;
	}
	public Mode getMode() {
		return mode;
	}
}
