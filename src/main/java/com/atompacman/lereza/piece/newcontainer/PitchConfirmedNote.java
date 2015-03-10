package com.atompacman.lereza.piece.newcontainer;

import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.Value;

public class PitchConfirmedNote extends BarNote {
	
	//======================================= METHODS ============================================\\
	
	//------------------------------ PUBLIC STATIC CONSTRUCTOR -----------------------------------\\
	
	public static PitchConfirmedNote valueOf(BarNote note, Pitch confirmedPitch) {
		return new PitchConfirmedNote(confirmedPitch, note.getValue(), note.isTied());
	}
	

	//--------------------------------- PROTECTED CONSTRUCTOR ------------------------------------\\

	private PitchConfirmedNote(Pitch pitch, Value value, boolean isTied) {
		super(pitch, value, isTied);
	}
}
