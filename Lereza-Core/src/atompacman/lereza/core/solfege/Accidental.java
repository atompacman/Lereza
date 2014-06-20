package atompacman.lereza.core.solfege;

public enum Accidental {
	NONE, SHARP, FLAT;
	
	public String toString() {
		if (this == NONE) {
			return "";
		} else if (this == SHARP) {
			return "#";
		} else {
			return "b";
		}
	}
}
