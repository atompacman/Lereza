package com.atompacman.lereza.piece.container;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.midi.MIDISequence;
import com.atompacman.lereza.solfege.RythmicSignature;

public final class Piece {

	//======================================= FIELDS =============================================\\

	private final List<Part> 		parts;
	private final RythmicSignature 	rythmicSignature;
	private final MIDISequence 		midiSeq;
	
	
	
	//======================================= METHODS ============================================\\

	//------------------------------- PUBLIC STATIC CONSTRUCTORS ---------------------------------\\
	
	public static Piece valueOf(MIDISequence midiSeq) {
		return new Piece(new ArrayList<Part>(), midiSeq.getRythmicSignature(), midiSeq);
	}
	
	public static Piece valueOf(RythmicSignature rythmicSign) {
		return new Piece(new ArrayList<Part>(), rythmicSign, null);
	}
	
	public static Piece valueOf(List<Part> parts) {
		if (parts.isEmpty()) {
			throw new IllegalArgumentException("Cannot create a piece from an empty part list.");
		}
		return new Piece(parts, parts.get(0).getRythmicSignature(), null);
	}
	
	
	//---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

	private Piece(List<Part> parts, RythmicSignature rythmicSign, MIDISequence midiSeq) {
		this.parts = parts;
		this.rythmicSignature = rythmicSign;
		this.midiSeq = midiSeq;
	}
	
	
	//----------------------------------------- ADD ----------------------------------------------\\

	public void addPart(Part part) {
		parts.add(part);
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public Part getPartNo(int index) {
		return parts.get(index);
	}
	
	public RythmicSignature getRythmicSignature() {
		return rythmicSignature;
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
