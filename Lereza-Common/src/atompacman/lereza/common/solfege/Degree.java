package atompacman.lereza.common.solfege;

public class Degree {

	private ScaleDegree degreeOnScale;
	private ChordType chord;
	
	
	public Degree(ScaleDegree degreeOnScale, ChordType chord) {
		this.degreeOnScale = degreeOnScale;
		this.chord = chord;
	}
	
	public ScaleDegree getDegreeOnScale() {
		return degreeOnScale;
	}
	
	public ChordType getChord() {
		return chord;
	}
	
	public String toString() {
		String output = degreeOnScale.name();
		if (chord.equals(ChordType.MINOR)) {
			output = output.toLowerCase();
		}
		if (chord.equals(ChordType.DIMINISHED)) {
			output += "o";
		}
		return output;
	}
}
