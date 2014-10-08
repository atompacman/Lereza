package com.atompacman.lereza.piece.container;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.common.database.Storable;
import com.atompacman.lereza.common.solfege.RythmicSignature;
import com.atompacman.lereza.midi.container.MIDIFile;

public class Piece implements Storable {

	private List<Part> parts;
	private RythmicSignature rythmicSignature;

	private MIDIFile midiFile;
	
	
	//------------ CONSTRUCTORS ------------\\

	public Piece(MIDIFile midiFile) {
		this.parts = new ArrayList<Part>();
		this.midiFile = midiFile;
	}
	
	
	//------------ SETTERS ------------\\

	public void addPart(Part part) {
		this.parts.add(part);
	}
	
	public void setRythmicSignature(RythmicSignature rythmicSignature) {
		this.rythmicSignature = rythmicSignature;
	}
	
	
	//------------ GETTERS ------------\\

	public Part getPartNo(int index) {
		return parts.get(index);
	}
	
	public RythmicSignature getRythmicSignature() {
		return rythmicSignature;
	}
	
	public MIDIFile getMIDIFile() {
		return midiFile;
	}
	
	
	//------------ STATUS ------------\\

	public int getNbParts() {
		return parts.size();
	}
}
