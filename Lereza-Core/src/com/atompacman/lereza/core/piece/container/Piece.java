package com.atompacman.lereza.core.piece.container;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.common.solfege.RythmicSignature;

public class Piece {

	private List<Part> parts;
	private RythmicSignature rythmicSignature;

	
	//------------ CONSTRUCTORS ------------\\

	public Piece() {
		this.parts = new ArrayList<Part>();
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
	
	
	//------------ STATUS ------------\\

	public int getNbParts() {
		return parts.size();
	}
}
