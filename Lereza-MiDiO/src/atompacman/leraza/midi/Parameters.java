package atompacman.leraza.midi;

public class Parameters {

	// MIDIFileReader
	public static final long 	MAX_FILE_SIZE = 1000000;
	public static final boolean NOTE_VISUALISATION = false;
	public static final boolean NOTE_PLAY_AUDIO = false;
	public static final int 	VISUALISATION_DELAY = 80;
	public static final int     NOTE_LENGTH_CORRECTION_THRESHOLD = 5;
	
	// MIDIFileReaderErrorSummary
	public static final int		MAX_NB_CHORDS_WITHOUT_HOMOPHONIC = 2;
}
