package atompacman.lereza.core.solfege;

public class Pitch {
	private NoteName noteName;
	private Octave octave;
	private Accidental accidental;
	private boolean hasVerifiedAccidental;
	
	
	public Pitch(NoteName note, Octave octave) {
		this.noteName = note;
		this.octave = octave;
		this.accidental = defaultAccidental();
		this.hasVerifiedAccidental = false;
	}
	
	public Accidental defaultAccidental() {
		Accidental accidental;
		switch(noteName) {
		case Eb: case Ab: case Bb: 
			accidental = Accidental.FLAT; break;
		case Cx: case Fx:
			accidental = Accidental.SHARP; break;
		default:
			accidental = Accidental.NONE; break;
		}
		return accidental;
	}
	
	public NoteName getNoteName(){
		return noteName;
	}
	
	public Octave getOctave(){
		return octave;
	}

	public Accidental getAccidental() {
		return accidental;
	}

	public boolean hasVerifiedAccidental() {
		return hasVerifiedAccidental;
	}

	public int heightOnPartition() {
		int height = 0;
		
		switch(octave) {
		case ONE:
			switch(noteName) {
			case C: case Cx: height = 0; break;
			case D: case Eb: height = 1; break;
			case E:          height = 2; break;
			case F: case Fx: height = 3; break;
			case G: case Ab: height = 4; break;
			case A: case Bb: height = 5; break;
			case B:          height = 6; break;
			}
			break;
		case TWO:
			switch(noteName) {
			case C: case Cx: height = 7; break;
			case D: case Eb: height = 8; break;
			case E:          height = 9; break;
			case F: case Fx: height = 10; break;
			case G: case Ab: height = 11; break;
			case A: case Bb: height = 12; break;
			case B:          height = 13; break;
			}
			break;
		case THREE:
			switch(noteName) {
			case C: case Cx: height = 7; break;
			case D: case Eb: height = 8; break;
			case E:          height = 9; break;
			case F: case Fx: height = 10; break;
			case G: case Ab: height = 11; break;
			case A: case Bb: height = 12; break;
			case B:          height = 13; break;
			}
			break;
		case FOUR:
			switch(noteName) {
			case C: case Cx: height = 14; break;
			case D: case Eb: height = 15; break;
			case E:          height = 16; break;
			case F: case Fx: height = 17; break;
			case G: case Ab: height = 18; break;
			case A: case Bb: height = 19; break;
			case B:          height = 20; break;
			}	
			break;
		case FIVE:
			switch(noteName) {
			case C: case Cx: height = 21; break;
			case D: case Eb: height = 22; break;
			case E:          height = 23; break;
			case F: case Fx: height = 24; break;
			case G: case Ab: height = 25; break;
			case A: case Bb: height = 26; break;
			case B:          height = 27; break;
			}
			break;
		case SIX:
			switch(noteName) {
			case C: case Cx: height = 28; break;
			case D: case Eb: height = 29; break;
			case E:          height = 30; break;
			default :        height = -1; break;
			}
			break;
		default: 
			height = -1; break;
		}
		
		if (accidental == Accidental.FLAT && height != -1) {
			++height;
		}
		return height;
	}

	public String toString() {
		return noteName.name().substring(0, 1) + accidental.toString() + octave.ordinal();
	}
}
