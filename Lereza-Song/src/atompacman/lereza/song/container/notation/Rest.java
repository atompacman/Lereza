package atompacman.lereza.song.container.notation;

import atompacman.lereza.common.solfege.Value;

public class Rest implements Notation {
	private Value value;
	private boolean isBeginningOfRest;
	
	
	public Rest(Value value) {
		this.value = value;
		this.isBeginningOfRest = false;
	}
	
	public Value getValue() {
		return value;
	}
	
	public boolean isRealNote() {
		return isBeginningOfRest;
	}
	
	public void setToBeginningOfRest() {
		isBeginningOfRest = true;
	}
	
	public String toString() {
		if (isBeginningOfRest) {
			return "[R]";
		}
		return "---";
	}
}
