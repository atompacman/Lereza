package com.atompacman.lereza.solfege;

public class RythmicSignature {
	
	private Meter meter;
	private Grouping noteGrouping;


	//------------ CONSTRUCTOR ------------\\

	public RythmicSignature(Meter meter, Grouping noteGrouping) {
		this.meter = meter;
		this.noteGrouping = noteGrouping;
	}
	
	
	//------------ GETTERS ------------\\

	public Meter getMeter() {
		return meter;
	}

	public Grouping getNoteGrouping() {
		return noteGrouping;
	}
	
	public int timeunitsInABar() {
		return meter.numerator() * Value.QUARTER.toTimeunit();
	}
}
