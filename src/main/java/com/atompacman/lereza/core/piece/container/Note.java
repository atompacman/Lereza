package com.atompacman.lereza.core.piece.container;

import com.atompacman.lereza.common.solfege.Articulation;
import com.atompacman.lereza.common.solfege.Octave;
import com.atompacman.lereza.common.solfege.Pitch;
import com.atompacman.lereza.common.solfege.Tone;
import com.atompacman.lereza.common.solfege.Value;

public class Note {
	
	private Pitch pitch;
	private Value value;
	private Articulation articulation;
	private NoteStatus status;
	
	public enum NoteStatus { 
		NOTE_START, TIED_NOTE_START, NOTE_CONTINUATION, TIED_NOTE_CONTINUATION 
	};
	
	
	//------------ CONSTRUCTORS ------------\\
	
	public Note(Pitch pitch, Value value, Articulation articulation, NoteStatus status) {
		this.pitch = pitch;
		this.value = value;
		this.articulation = articulation;
		this.status = status;
	}
	
	public Note(Pitch pitch, Value value, NoteStatus status) {
		this(pitch, value, Articulation.NORMAL, status);
	}
	
	public Note(int hexValue, Value value, NoteStatus status) {
		this(new Pitch(Tone.thatIsMoreCommonForSemitoneValue(hexValue), 
				Octave.fromHex(hexValue)), value, status);
	}

	
	//------------ GETTERS ------------\\

	public Pitch getPitch() {
		return pitch;
	}
	
	public Value getValue() {
		return value;
	}
	
	public Articulation getArticulation() {
		return articulation;
	}
	
	public NoteStatus getStatus() {
		return status;
	}

	public boolean startSomething() {
		return status == NoteStatus.NOTE_START || status == NoteStatus.TIED_NOTE_START;
	}
	
	
	//------------ SETTERS ------------\\

	public void setArticulation(Articulation articulation) {
		this.articulation = articulation;
	}
	
	
	//------------ STRING ------------\\

	public String toString() {
		String output = pitch.toString();
		if (articulation == Articulation.STACCATO) {
			output += "·";
		}
		switch(status) {
		case NOTE_START: return output;
		case TIED_NOTE_START: return "(" + output + ")";
		case NOTE_CONTINUATION: return "";
		case TIED_NOTE_CONTINUATION: return "";
		default: return null;
		}
	}
}
