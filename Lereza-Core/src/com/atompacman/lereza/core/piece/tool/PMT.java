package com.atompacman.lereza.core.piece.tool;

public class PMT {

	public int part;
	public int measure;
	public int timeunit;
	
	
	public PMT(int part, int measure, int timeunit) {
		this.part = part;
		this.measure = measure;
		this.timeunit = timeunit;
	}
	
	public String toString() {
		return "(" + part + "|" + measure + "|" + timeunit + ")";
	}
}
