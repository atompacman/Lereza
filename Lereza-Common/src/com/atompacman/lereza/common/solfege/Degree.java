package com.atompacman.lereza.common.solfege;

public class Degree {

	private ScaleDegree degreeOnScale;
	private ChordType chord;
	
	
	//------------ CONSTRUCTORS ------------\\

	public Degree(ScaleDegree degreeOnScale, ChordType chord) {
		this.degreeOnScale = degreeOnScale;
		this.chord = chord;
	}
	
	
	//------------ GETTERS ------------\\

	public ScaleDegree getDegreeOnScale() {
		return degreeOnScale;
	}
	
	public ChordType getChord() {
		return chord;
	}

	
	//------------ EQUALITITES ------------\\

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chord == null) ? 0 : chord.hashCode());
		result = prime * result
				+ ((degreeOnScale == null) ? 0 : degreeOnScale.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Degree other = (Degree) obj;
		if (chord != other.chord)
			return false;
		if (degreeOnScale != other.degreeOnScale)
			return false;
		return true;
	}
}
