package com.atompacman.lereza.common.solfege;

public class RythmicSignature {
	
	private Meter meter;
	private Grouping noteGrouping;


	//------------ CONSTRUCTORS ------------\\

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
		return meter.getNumerator() * Value.QUARTER.toTimeunit();
	}
	
	
}
