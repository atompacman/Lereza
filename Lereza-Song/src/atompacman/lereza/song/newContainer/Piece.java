package atompacman.lereza.song.newContainer;

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
	
	public void setRythmicSignature(RythmicSignature rythmicSignature) {
		this.rythmicSignature = rythmicSignature;
	}
	
	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////
	
	public RythmicSignature getRythmicSignature() {
		return rythmicSignature;
	}
}
