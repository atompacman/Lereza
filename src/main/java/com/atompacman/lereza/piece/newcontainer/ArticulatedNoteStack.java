package com.atompacman.lereza.piece.newcontainer;

import java.util.Map;

import com.atompacman.lereza.solfege.Articulation;
import com.atompacman.lereza.solfege.Dynamic;
import com.atompacman.lereza.solfege.Pitch;

public final class ArticulatedNoteStack<T extends BarNote> extends NoteStack<T> {

	//======================================= FIELDS =============================================\\

	private final Articulation articulation;
	
	
	
	//======================================= METHODS ============================================\\

	//------------------------------ PACKAGE STATIC CONSTRUCTOR ----------------------------------\\
	
	static <T extends BarNote> ArticulatedNoteStack<T> valueOf(NoteStack<T> noteStack, 
															   Articulation articulation) {
		
		return new ArticulatedNoteStack<T>(noteStack.getStartingNoteMap(), 
				noteStack.getStartedNoteMap(), noteStack.getDynamic(), articulation);
	}
	
	
	//---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

	private ArticulatedNoteStack(Map<Pitch, T> startingNotes, 
								 Map<Pitch, T> startedNotes, 
								 Dynamic dynamic, 
								 Articulation articulation) {
		
		super(startingNotes, startedNotes, dynamic);
		this.articulation = articulation;
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public Articulation getArticulation() {
		return articulation;
	}
}
