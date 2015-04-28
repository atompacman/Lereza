package com.atompacman.lereza.piece.tool;

public class PBT {

	//======================================= FIELDS =============================================\\

	private final int part;
	private final int bar;
	private final int timeunit;
	
	
	
	//======================================= METHODS ============================================\\

	//---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

	public PBT(int part) {
		this(part, 0, 0);
	}
	
	public PBT(int part, int bar) {
		this(part, bar, 0);
	}
	
	public PBT(int part, int bar, int timeunit) {
		this.part = part;
		this.bar = bar;
		this.timeunit = timeunit;
	}
	

	//--------------------------------------- GETTERS --------------------------------------------\\

	public int part() {
		return part;
	}

	public int bar() {
		return bar;
	}

	public int timeunit() {
		return timeunit;
	}
	
	
	//--------------------------------------- EQUALS ---------------------------------------------\\

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bar;
		result = prime * result + part;
		result = prime * result + timeunit;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PBT other = (PBT) obj;
		if (bar != other.bar)
			return false;
		if (part != other.part)
			return false;
		if (timeunit != other.timeunit)
			return false;
		return true;
	}

	
	//-------------------------------------- TO STRING -------------------------------------------\\

	public String toString() {
		return "(" + part + "|" + bar + "|" + timeunit + ")";
	}
}
