package com.atompacman.lereza.piece;

import java.util.List;

import com.atompacman.lereza.solfege.RythmicSignature;

public final class Bar<T extends Stack<? extends Note>> implements PieceComponent {

	//======================================= FIELDS =============================================\\

	private final List<Stack<? extends Note>> noteStacks;
	private final RythmicSignature rythmicSign;


	
	//======================================= METHODS ============================================\\

	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

	Bar(List<Stack<? extends Note>> noteStacks, RythmicSignature rythmicSign) {
		this.noteStacks = noteStacks;
		this.rythmicSign = rythmicSign;
	}


	//--------------------------------------- GETTERS --------------------------------------------\\
	
	public Stack<? extends Note> getNoteStack(int timeunit) {
		Piece.checkBoundedTU(timeunit,rythmicSign.timeunitsInABar(), "note stack", "length of bar");
		return noteStacks.get(timeunit);
	}
	
	public RythmicSignature getRythmicSignature() {
		return rythmicSign;
	}
	

	//---------------------------------------- STATE ---------------------------------------------\\

	public boolean isEmpty() {
		for (Stack<? extends Note> stack : noteStacks) {
			if (stack.hasPlayingNotes()) {
				return false;
			}
		}
		return true;
	}

	public int getNumStartingNotes() {
		int sum = 0;
		for (Stack<? extends Note> stack : noteStacks) {
			sum += stack.getNumStartingNotes();
		}
		return sum;
	}
}
