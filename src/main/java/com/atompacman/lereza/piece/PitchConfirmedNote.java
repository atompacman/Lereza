package com.atompacman.lereza.piece;

import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.Value;

public class PitchConfirmedNote extends Note {
	
	//======================================= METHODS ============================================\\
	
	//------------------------------ PUBLIC STATIC CONSTRUCTOR -----------------------------------\\
	
	public static PitchConfirmedNote valueOf(Note note, Pitch confirmedPitch) {
		return new PitchConfirmedNote(confirmedPitch, note.getValue(), note.isTied());
	}
	

	//--------------------------------- PROTECTED CONSTRUCTOR ------------------------------------\\

	private PitchConfirmedNote(Pitch pitch, Value value, boolean isTied) {
		super(pitch, value, isTied);
	}
}
