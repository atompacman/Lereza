package atompacman.leraza.midi;

public class Parameters {

	// MIDIFileReader
	public static final long 	MAX_FILE_SIZE = 1000000;
	public static final boolean NOTE_VISUALISATION = false;
	public static final boolean NOTE_PLAY_AUDIO = true;
	public static final double 	VISUALISATION_SPEED_CORRECTION = 2.5;
	public static final int     NOTE_LENGTH_CORRECTION_THRESHOLD = 5;
	public static final int		MIN_RAW_REST_LENGTH = 10;
	public static final int		TOTAL_ROUND_OFFSET_LIMIT = 10;
	
	// MIDIFileReaderErrorSummary
	public static final int		MAX_NB_CHORDS_WITHOUT_HOMOPHONIC = 2;
	
	// MIDIFilePlayer
	public static final double  TRACK_PLAYBACK_SPEED_CORRECTION = 50000;
	public static final double  NOTE_PLAYBACK_SPEED_CORRECTION = 4;
	public static final double	CONSECUTIVE_NOTE_CORRECTION = 0.3; // in milliseconds
}
