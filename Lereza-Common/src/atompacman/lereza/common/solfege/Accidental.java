package atompacman.lereza.common.solfege;

public enum Accidental {
	
	NONE, SHARP, FLAT;
	
	
	public static Accidental parseChar(char accidental) {
		switch(accidental) {
		case '#': return SHARP;
		case 'b': return FLAT;
		default: return NONE;
		}
	}
	
	public String toString() {
		switch(this) {
		case NONE:  return "";
		case SHARP: return "#";
		case FLAT:  return "b";
		default:	return "?";
		}
	}
}
