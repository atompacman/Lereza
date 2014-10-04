package com.atompacman.lereza.common.solfege;

import com.atompacman.lereza.common.solfege.quality.Quality;

public class Degree {

	private ScaleDegree degreeOnScale;
	private ChordType chord;
	
	
	//------------ CONSTRUCTORS ------------\\

	public Degree(ScaleDegree degreeOnScale, ChordType chord) {
		this.degreeOnScale = degreeOnScale;
		this.chord = chord;
	}
	
	
	//------------ STATIC CONSTRUCTORS ------------\\

	public static Degree valueOf(String repres) {
		if (repres == null || repres.isEmpty()) {
			throw new IllegalArgumentException("Degree representation must not be null or empty.");
		}
		Quality quality = null;
		String firstChar = repres.substring(0, 1);
		int separation;
		
		if (firstChar.toUpperCase().equals(firstChar)) {
			quality = Quality.MAJOR;
			int indexOfI = repres.lastIndexOf('I');
			int indexOfV = repres.lastIndexOf('V');
			separation = Math.max(indexOfI, indexOfV) + 1;
		} else {
			quality = Quality.MINOR;
			int indexOfi = repres.lastIndexOf('i');
			int indexOfv = repres.lastIndexOf('v');
			separation = Math.max(indexOfi, indexOfv) + 1;
		}	
		ScaleDegree degree = ScaleDegree.valueOf(repres.substring(0, separation).toUpperCase());
		
		String chordRepres = repres.substring(separation);
		if (quality == Quality.MINOR) {
			chordRepres = "m" + chordRepres;
		}
		ChordType chord = ChordType.valueOf(chordRepres);
		
		return new Degree(degree, chord);
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
		if (chord == null) {
			if (other.chord != null)
				return false;
		} else if (!chord.equals(other.chord))
			return false;
		if (degreeOnScale != other.degreeOnScale)
			return false;
		return true;
	}
}
