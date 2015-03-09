package com.atompacman.lereza.piece.newcontainer;

import java.util.List;

import com.atompacman.lereza.midi.MIDISequence;
import com.atompacman.lereza.solfege.RythmicSignature;

public final class Piece<T extends BarNote> {

	//======================================= FIELDS =============================================\\

	private final List<Part<T>>		parts;
	private final RythmicSignature 	rythmicSign;
	private final MIDISequence 		midiSeq;



	//======================================= METHODS ============================================\\

	//------------------------------ PACKAGE STATIC CONSTRUCTOR ----------------------------------\\

	static <T extends BarNote> Piece<T> valueOf(List<Part<T>> parts, 
											 	RythmicSignature rythmicSign, 
											 	MIDISequence midiSeq) {
		
		return new Piece<T>(parts, rythmicSign, midiSeq);
	}
	
	
	//---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

	private Piece(List<Part<T>> parts, RythmicSignature rythmicSign, MIDISequence midiSeq) {
		this.parts = parts;
		this.rythmicSign = rythmicSign;
		this.midiSeq = midiSeq;
	}


	//--------------------------------------- GETTERS --------------------------------------------\\

	public Part<T> getPart(int index) {
		if (index >= parts.size()) {
			throw new IllegalArgumentException("Cannot get part num. " + 
					index + ": Piece only has " + parts.size() + " parts.");
		}
		return parts.get(index);
	}

	public RythmicSignature getRythmicSignature() {
		return rythmicSign;
	}

	public MIDISequence getMIDISequence() {
		return midiSeq;
	}


	//---------------------------------------- STATE ---------------------------------------------\\

	public int numParts() {
		return parts.size();
	}

	public boolean hasAssociatedMIDISequence() {
		return midiSeq != null;
	}
}
