package com.atompacman.lereza.piece.container;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.midi.MIDISequence;
import com.atompacman.lereza.solfege.RythmicSignature;

public class Piece {

	//======================================= FIELDS =============================================\\

	private List<Part> parts;
	private RythmicSignature rythmicSignature;

	private MIDISequence midiSeq;
	
	
	
	//======================================= METHODS ============================================\\

	//---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\
	
	public Piece(MIDISequence midiSeq) {
		this.parts = new ArrayList<Part>();
		this.rythmicSignature = midiSeq.getRythmicSignature();
		this.midiSeq = midiSeq;
	}
	
	
	//--------------------------------------- SETTERS --------------------------------------------\\

	public void addPart(Part part) {
		parts.add(part);
	}
	
	public void setRythmicSignature(RythmicSignature rythmicSignature) {
		this.rythmicSignature = rythmicSignature;
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
}
