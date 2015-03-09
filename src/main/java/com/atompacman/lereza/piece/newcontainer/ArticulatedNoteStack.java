package com.atompacman.lereza.piece.newcontainer;

import com.atompacman.lereza.solfege.Articulation;
import com.atompacman.lereza.solfege.Dynamic;
import com.atompacman.toolkat.test.Assert;

public final class ArticulatedNoteStack<T extends BarNote> extends NoteStack<T> {

	//======================================= FIELDS =============================================\\

	private final Articulation articulation;
	
	
	
	//======================================= METHODS ============================================\\

	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

	ArticulatedNoteStack(Dynamic dynamic, Articulation articulation) {
		super(dynamic);
		this.articulation = Assert.paramIsNotNull(articulation, "articulation");
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public Articulation getArticulation() {
		return articulation;
	}
}
