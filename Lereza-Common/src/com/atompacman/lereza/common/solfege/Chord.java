package com.atompacman.lereza.common.solfege;

public class Chord {

	private Tone tone;
	private Intervals intervals;
	private ChordType chord;
	
	
	//------------ CONSTRUCTORS ------------\\

	public Chord(Tone tone, ChordType chord) {
		this.tone = tone;
		this.intervals = new Intervals(chord.getIntervals());
		this.chord = chord;
	}
	
	
	//------------ STATIC CONSTRUCTORS ------------\\

	public static Chord valueOf(String repres) {
		if (repres == null || repres.isEmpty()) {
			throw new IllegalArgumentException("String representation can't be null.");
		}
		Tone tone = null;
		ChordType chord = null;
		if (repres.length() > 1 && (repres.charAt(1) == 'b' || repres.charAt(1) == '#')) {
			tone = Tone.valueOf(repres.substring(0, 2));
			chord = ChordType.valueOf(repres.substring(2));
		} else {
			tone = Tone.valueOf(repres.substring(0, 1));
			chord = ChordType.valueOf(repres.substring(1));
		}
		return new Chord(tone, chord);
	}
	
	
	//------------ GETTERS ------------\\

	public Tone getTone() {
		return tone;
	}
	
	public Intervals getIntervals() {
		return intervals;
	}
	
	public ChordType getChord() {
		return chord;
	}

	
	//------------ STRING ------------\\

	public String toString() {
		return tone.toString() + chord.toString();
	}
}
