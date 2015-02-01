package com.atompacman.lereza.piece.container;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.midi.container.MIDIFile;
import com.atompacman.lereza.resources.database.Storable;
import com.atompacman.lereza.solfege.RythmicSignature;

public class Piece implements Storable {

	private List<Part> parts;
	private RythmicSignature rythmicSignature;

	private MIDIFile midiFile;
	
	
	//------------ CONSTRUCTORS ------------\\

	public Piece(MIDIFile midiFile) {
		this(midiFile, null);
	}
	
	public Piece(MIDIFile midiFile, RythmicSignature rythmicSignature) {
		this.parts = new ArrayList<Part>();
		this.rythmicSignature = rythmicSignature;
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
	
	
	//------------ OBSERVERS ------------\\

	public int nbParts() {
		return parts.size();
	}
}
