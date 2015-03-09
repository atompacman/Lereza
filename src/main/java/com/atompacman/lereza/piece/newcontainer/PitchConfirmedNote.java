package com.atompacman.lereza.piece.newcontainer;

import com.atompacman.lereza.solfege.Articulation;
import com.atompacman.lereza.solfege.Dynamic;
import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.Value;

public class PitchConfirmedNote extends ArticulatedNote {
	
	//======================================= METHODS ============================================\\
	
	//------------------------------ PUBLIC STATIC CONSTRUCTOR -----------------------------------\\
	
	public static PitchConfirmedNote valueOf(ArticulatedNote note, Pitch confirmedPitch) {
		return new PitchConfirmedNote(confirmedPitch, note.getValue(), 
				note.getArticulation(), note.getDynamic(), note.isTied());
	}
	

	//--------------------------------- PROTECTED CONSTRUCTOR ------------------------------------\\

	private PitchConfirmedNote(Pitch pitch, Value value, Articulation articulation, 
			Dynamic dynamic, boolean isTied) {
		
		super(pitch, value, articulation, dynamic, isTied);
	}
}
