package atompacman.lereza.core.solfege;

public class RythmicSignature {
	
	private Meter meter;
	private Grouping noteGrouping;
	private int midiBeatNoteValue;
	private int valueOfShortestNote;
	

	public RythmicSignature(Meter meter, Grouping noteGrouping, int midiBeatNoteValue, int valueOfShortestNote) {
		this.meter = meter;
		this.noteGrouping = noteGrouping;
		this.midiBeatNoteValue = midiBeatNoteValue;
		this.valueOfShortestNote = valueOfShortestNote;
	}
	
	public Meter getMeter() {
		return meter;
	}

	public Grouping getNoteGrouping() {
		return noteGrouping;
	}
	
	public int getMidiBeatNoteValue() {
		return midiBeatNoteValue;
	}
	
	public int getValueOfShortestNote() {
		return valueOfShortestNote;
	}
}
