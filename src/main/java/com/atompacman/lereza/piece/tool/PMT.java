package com.atompacman.lereza.piece.tool;

public class PMT {

	public int part;
	public int bar;
	public int timeunit;
	
	
	//------------ CONSTRUCTORS ------------\\

	public PMT(int part, int bar, int timeunit) {
		this.part = part;
		this.bar = bar;
		this.timeunit = timeunit;
	}
	
	
	//------------ STIRNG ------------\\

	public String toString() {
		return "(" + part + "|" + bar + "|" + timeunit + ")";
	}
}
