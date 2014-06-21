package atompacman.leraza.midi;

public class Parameters {

	// MIDIFileReader
	public static final long 	MAX_FILE_SIZE = 1000000;
	public static final boolean NOTE_VISUALISATION = false;
	public static final boolean NOTE_PLAY_AUDIO = true;
	public static final int 	VISUALISATION_SPEED = 40;
	public static final int     NOTE_LENGTH_CORRECTION_THRESHOLD = 5;
	public static final int     MAX_NOTE_MIDI_LENGTH = 3000; 			// O(n/15)
	public static final int		MIN_RAW_REST_LENGTH = 10;
	
	// MIDIFileReaderErrorSummary
	public static final int		MAX_NB_CHORDS_WITHOUT_HOMOPHONIC = 2;
	
	// MIDIFilePlayer
	public static final int		PLAYBACK_SPEED_CORRECTION = 4;
}
