package atompacman.lereza.core.container.notation;

import atompacman.lereza.core.solfege.Articulation;
import atompacman.lereza.core.solfege.NoteName;
import atompacman.lereza.core.solfege.Octave;
import atompacman.lereza.core.solfege.Pitch;
import atompacman.lereza.core.solfege.Relation;
import atompacman.lereza.core.solfege.Value;

public class Note implements Notation {
	private Pitch pitch;
	private Value value;
	private Articulation articulation;
	private Relation relationWithPreviousNote;
	private boolean isFirstNoteOfVoice;
	
	
	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////
	
	public Note(int hexValue, Value value, Pitch lastNotePitch) {
		NoteName noteName = NoteName.values()[hexValue % 12];
		Octave octave = Octave.values()[(int) hexValue / 12 - 1];
		this.pitch = new Pitch(noteName, octave);
		this.value = value;
		this.articulation = Articulation.NORMAL;
		if (lastNotePitch  == null) {
			this.relationWithPreviousNote = null;
		} else {
			this.relationWithPreviousNote = new Relation(lastNotePitch, this.pitch);
		}
		this.isFirstNoteOfVoice = false;
	}
	
	public Note(int hexValue, Value value, Pitch lastNotePitch, boolean isFirstNoteOfVoice) {
		this(hexValue, value, lastNotePitch);
		this.isFirstNoteOfVoice = isFirstNoteOfVoice;
	}

	public Note(int hexValue, Value value, Pitch lastNotePitch, Articulation articulation) {
		this(hexValue, value, lastNotePitch);
		this.articulation = articulation;
	}
	
	
	//////////////////////////////
	//      STATUS CHECKER      //
	//////////////////////////////

	public boolean isRealNote() {
		return relationWithPreviousNote != null || isFirstNoteOfVoice;
	}

	public boolean isFirstNoteOfVoice() {
		return isFirstNoteOfVoice;
	}
	
	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////

	public Pitch getPitch() {
		return pitch;
	}
	
	public Value getValue() {
		return value;
	}
	
	public Articulation getArticulation() {
		return articulation;
	}
	
	public Relation getRelationWithPreviousNote() {
		return relationWithPreviousNote;
	}

	
	//////////////////////////////
	//         SETTERS          //
	//////////////////////////////

	public void setArticulation(Articulation articulation) {
		this.articulation = articulation;
	}

	public void setRelationWithPreviousNote(Relation relationWithPreviousNote) {
		this.relationWithPreviousNote = relationWithPreviousNote;
	}
	
	public void setToFirstNoteOfVoice() {
		isFirstNoteOfVoice = true;
	}

	
	//////////////////////////////
	//        TO STRING         //
	//////////////////////////////
	
	public String toString() {
		String output = "";
		if (isRealNote()) {
			output += pitch.toString();
		} else {
			output += "(" + pitch.toString() + ")";
		}
		if (articulation == Articulation.STACCATO) {
			output += "·";
		}
		return output;
	}
}
