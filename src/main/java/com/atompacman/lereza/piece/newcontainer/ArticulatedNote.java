package com.atompacman.lereza.piece.newcontainer;

import com.atompacman.lereza.solfege.Articulation;
import com.atompacman.lereza.solfege.Dynamic;
import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.Value;

public class ArticulatedNote extends BarNote {
	
	//======================================= FIELDS =============================================\\

	private Articulation articulation;
	
	
	
	//======================================= METHODS ============================================\\
	
	//------------------------------ PUBLIC STATIC CONSTRUCTOR -----------------------------------\\
	
	public static ArticulatedNote valueOf(BarNote note, Articulation articulation) {
		return new ArticulatedNote(note.getPitch(), note.getValue(), 
				articulation, note.getDynamic(), note.isTied());
	}
	

	//--------------------------------- PROTECTED CONSTRUCTOR ------------------------------------\\

	protected ArticulatedNote(Pitch pitch, Value value, Articulation articulation, 
			Dynamic dynamic, boolean isTied) {
		
		super(pitch, value, dynamic, isTied);
		this.articulation = articulation;
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public Articulation getArticulation() {
		return articulation;
	}
}
