package atompacman.lereza.common.solfege;

public class RythmicSignature {
	
	private Meter meter;
	private Grouping noteGrouping;
	private int quarterNoteTimeunitLength;
	

	public RythmicSignature(Meter meter, Grouping noteGrouping, int quarterNoteTimeunitLength) {
		this.meter = meter;
		this.noteGrouping = noteGrouping;
		this.quarterNoteTimeunitLength = quarterNoteTimeunitLength;
	}
	
	public Meter getMeter() {
		return meter;
	}

	public Grouping getNoteGrouping() {
		return noteGrouping;
	}
	
	public int getQuarterNoteTimeunitLength() {
		return quarterNoteTimeunitLength;
	}
	
	public int getMeasureTimeunitLength() {
		return quarterNoteTimeunitLength * meter.getNumerator();
	}
}
