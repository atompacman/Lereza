package atompacman.lereza.song;

import atompacman.lereza.common.solfege.Value;

public class Parameters {
		
	//////////////////////////////
	//           PART           //
	//////////////////////////////
	// Note adding animation
	public static final boolean NOTE_ADDING_VISUALISATION = true;
	public static final boolean NOTE_ADDING_AUDIO = false;
	public static final int 	NOTE_ADDING_AUDIO_TEMPO = 14;
	
	
	//////////////////////////////
	//       PIECEBUILDER       //
	//////////////////////////////
	// Note value QA
	public static final int     OFFBEAT_TIMEUNITS_QA_RADIUS = 4;
	public static final Value 	LONGEST_FUSIONNABLE_REST_LENGTH = Value.THIRTYSECONTH;
	
	
	//////////////////////////////
	//         MEASURE          //
	//////////////////////////////	
	// Measure print formatting
	public static final int 	TOP_SECTION_HEIGHT = 6;
	public static final int 	MIDDLE_SECTION_HEIGHT = 3;
	public static final int 	BOTTOM_SECTION_HEIGHT = 5;
	public static final int 	LENGTH_PER_TIMEUNIT = 1;
	public static final int 	BORDER_LENGTH = 4;
	public static final int 	NOTE_HEIGHT_CORRECTION = 44;
	public static final boolean SHOW_NOTE_LENGTH = true;
	public static final boolean SHOW_RESTS = true;
	public static final char 	REST_CHAR = '+'; 
}
