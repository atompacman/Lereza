package atompacman.lereza.common.solfege;

public class Chord {

	private Tone tone;
	private ChordType chord;
	
	
	public Chord(Tone tone, ChordType chord) {
		this.tone = tone;
		this.chord = chord;
	}
	
	public Tone getTone() {
		return tone;
	}
	
	public ChordType getChord() {
		return chord;
	}
}
