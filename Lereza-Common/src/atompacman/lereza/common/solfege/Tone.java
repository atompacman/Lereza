package atompacman.lereza.common.solfege;

public enum Tone {

	C_FLAT (NoteLetter.C, Accidental.FLAT), C (NoteLetter.C, Accidental.NONE), C_SHARP (NoteLetter.C, Accidental.SHARP),
	D_FLAT (NoteLetter.D, Accidental.FLAT), D (NoteLetter.D, Accidental.NONE), D_SHARP (NoteLetter.D, Accidental.SHARP),
	E_FLAT (NoteLetter.E, Accidental.FLAT), E (NoteLetter.E, Accidental.NONE), E_SHARP (NoteLetter.E, Accidental.SHARP),
	F_FLAT (NoteLetter.F, Accidental.FLAT), F (NoteLetter.F, Accidental.NONE), F_SHARP (NoteLetter.F, Accidental.SHARP),
	G_FLAT (NoteLetter.G, Accidental.FLAT), G (NoteLetter.G, Accidental.NONE), G_SHARP (NoteLetter.G, Accidental.SHARP),
	A_FLAT (NoteLetter.A, Accidental.FLAT), A (NoteLetter.A, Accidental.NONE), A_SHARP (NoteLetter.A, Accidental.SHARP),
	B_FLAT (NoteLetter.B, Accidental.FLAT), B (NoteLetter.B, Accidental.NONE), B_SHARP (NoteLetter.B, Accidental.SHARP);
	
	
	private NoteLetter note;
	private Accidental alteration;
	
	
	private Tone(NoteLetter note, Accidental alteration) {
		this.note = note;
		this.alteration = alteration;
	}
	
	public NoteLetter getNote() {
		return note;
	}
	
	public Accidental getAlteration() {
		return alteration;
	}
	
	public String toString() {
		return note.name() + alteration.toString();
	}
	
	public int toHex() {
		switch(this) {
		case C:	case B_SHARP:		return 0;
		case C_SHARP: case D_FLAT:	return 1;
		case D:						return 2;
		case D_SHARP: case E_FLAT:	return 3;
		case E: case F_FLAT:		return 4;
		case F: case E_SHARP:		return 5;
		case F_SHARP: case G_FLAT:	return 6;
		case G:						return 7;
		case G_SHARP: case A_FLAT:	return 8;
		case A:						return 9;
		case A_SHARP: case B_FLAT:	return 10;
		case B:	case C_FLAT:		return 11;
		default: return -1;	
		}
	}
	
	public static Tone getToneFromHex(int hexPitch) {
		int hexTone = hexPitch % 12;
		switch(hexTone) {
		case 0 : return C;
		case 1 : return C_SHARP;
		case 2 : return D;
		case 3 : return E_FLAT;
		case 4 : return E;
		case 5 : return F;
		case 6 : return F_SHARP;
		case 7 : return G;
		case 8 : return A_FLAT;
		case 9 : return A;
		case 10: return B_FLAT;
		case 11: return B;
		default: return null;
		}
	}
}
