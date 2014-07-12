package atompacman.lereza.common.solfege;

import java.util.Arrays;
import java.util.List;

public enum ScaleType {

	MINOR (Arrays.asList(
			new Degree(ScaleDegree.I, 	ChordType.MINOR),
			new Degree(ScaleDegree.II, 	ChordType.DIMINISHED),
			new Degree(ScaleDegree.III, ChordType.MAJOR),
			new Degree(ScaleDegree.IV, 	ChordType.MAJOR),
			new Degree(ScaleDegree.V, 	ChordType.MINOR),
			new Degree(ScaleDegree.VI, 	ChordType.MAJOR),
			new Degree(ScaleDegree.VII, ChordType.MAJOR))),
	
	MAJOR (Arrays.asList(
			new Degree(ScaleDegree.I, 	ChordType.MAJOR),
			new Degree(ScaleDegree.II, 	ChordType.MAJOR),
			new Degree(ScaleDegree.III, ChordType.MINOR),
			new Degree(ScaleDegree.IV, 	ChordType.MAJOR),
			new Degree(ScaleDegree.V, 	ChordType.MAJOR),
			new Degree(ScaleDegree.VI, 	ChordType.MINOR),
			new Degree(ScaleDegree.VII, ChordType.DIMINISHED)));
	
	private List<Degree> degrees;
	
	
	private ScaleType(List<Degree> degrees) {
		this.degrees = degrees;
	}
	
	public List<Degree> getDegrees() {
		return degrees;
	}
}
