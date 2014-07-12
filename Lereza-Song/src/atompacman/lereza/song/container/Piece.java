package atompacman.lereza.song.container;

import java.util.ArrayList;
import java.util.List;

import atompacman.lereza.common.solfege.RythmicSignature;

public class Piece {

	private List<Part> parts;
	private RythmicSignature rythmicSignature;

	
	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////
	
	public Piece() {
		this.parts = new ArrayList<Part>();
	}
	
	
	//////////////////////////////
	//         SETTERS          //
	//////////////////////////////
	
	public void addPart(Part part) {
		this.parts.add(part);
	}
	
	public void setRythmicSignature(RythmicSignature rythmicSignature) {
		this.rythmicSignature = rythmicSignature;
	}
	
	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////
	
	public Part getPart(int index) {
		return parts.get(index);
	}
	
	public RythmicSignature getRythmicSignature() {
		return rythmicSignature;
	}
}
