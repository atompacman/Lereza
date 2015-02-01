package com.atompacman.lereza.piece.tool;

public class PBT {

	public int part;
	public int bar;
	public int timeunit;
	
	
	//------------ CONSTRUCTORS ------------\\

	public PBT(int part, int bar, int timeunit) {
		this.part = part;
		this.bar = bar;
		this.timeunit = timeunit;
	}
	

	//------------ STRING ------------\\

	public String toString() {
		return "(" + part + "|" + bar + "|" + timeunit + ")";
	}
}
