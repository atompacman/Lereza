package com.atompacman.lereza;

import java.io.File;

import com.atompacman.lereza.common.solfege.Value;

/**
 * Contains the most important constants of Lereza.
 */

public class Parameters {
	
	//**********************************************************************************************
	//
	//                                        FILE PATHS
	//
	//**********************************************************************************************
	
	public static final String DATA_DIRECTORY 			= "data" + File.separator;
	public static final String TEST_DIRECTORY 			= "test" + File.separator;
	public static final String MIDI_INDEX_XML_PATH 	  	= "test\\midi\\TestMIDIFileIndex\\completeTest.xml";
	//public static final String MIDI_INDEX_XML_FILEPATH 	  = DATA_DIRECTORY + "MIDIFileIndex.xml";
	public static final String TEST_ROUTINES_XML_PATH 	= DATA_DIRECTORY + "TestRoutines.xml";
	//public static final String CONTEXT_ELEM_XML_FILEPATH  = DATA_DIRECTORY + "ContextElements.xml";
	public static final String CONTEXT_ELEM_XML_FILE  	= "test\\common\\context\\TestContextElements\\completeTest.xml";
	
	
	//**********************************************************************************************
	//
	//                                     CLASS PARAMETERS
	//
	//**********************************************************************************************

	/////////////////////////////////// PIECE BUILDING ANIMATION ///////////////////////////////////

	/** If true, activates an console animation that shows that notes that are being added during
	 * song creation. */
	public static final boolean NOTE_ADDING_VISUALISATION = false;

	/** Play notes when adding/visualizing notes */
	public static final boolean NOTE_ADDING_AUDIO 		  = false;
	
	/** A tempo correction factor that changes the building speed of a song in order to play notes
	 *  in a appreciable way.*/
	public static final int 	NOTE_ADDING_AUDIO_TEMPO   = 14;
	
	
	
	/////////////////////////////////////// MEASURE PRINTING ///////////////////////////////////////

	public static final int 	MIDDLE_SECTION_HEIGHT	= 3;
	public static final int 	BOTTOM_SECTION_HEIGHT 	= 5;
	public static final int 	TOP_SECTION_HEIGHT 		= 6;
	public static final int 	LENGTH_PER_TIMEUNIT 	= 1;
	public static final int 	BORDER_LENGTH 			= 4;
	public static final int 	NOTE_HEIGHT_CORRECTION  = 44;
	public static final boolean SHOW_RESTS 				= true;
	public static final char 	REST_CHAR 				= '+'; 
	
	

	///////////////////////////////////// TIMEUNIT CORRECTION //////////////////////////////////////

	/** Offbeat timeunit radius. Determines the sensibility of the TimestampsChecker to timestamp 
	 * variations. */
	public static final int     OFFBEAT_TU_RADIUS = 4;

	/** The longest length of rests that can be merged to a note. */
	public static final Value 	LONGEST_FUSIONNABLE_REST_LENGTH = Value.THIRTYSECONTH;
	
	
	
	/////////////////////////////// MIDIFILEREADER /////////////////////////////

	/** Maximum MIDI file size (in bytes) */
	public static final long 	MAX_FILE_SIZE = 1000000;
	
	/** Display an animation when loading songs from MIDI files. */
	public static final boolean NOTE_VISUALISATION = false;
	
	/** If MIDI note visualization is on, play the notes that are being added. */
	public static final boolean NOTE_PLAY_AUDIO = true;
	
	/** A tempo correction factor to adjust MIDI note reading process in order to smoothly play the
	 *  song that is being read.*/
	public static final double 	VISUALISATION_SPEED_CORRECTION = 2.5;
	
	/** MIDI notes timestamp will not be rounded beyond this value. */
	public static final int     NOTE_LENGTH_CORRECTION_THRESHOLD = 5;
	
	/** The threshold over which the cumulative timestamp error is considered as problematic. */
	public static final int		TOTAL_ROUND_OFFSET_LIMIT = 10;

	
	
	/////////////////////////////// MIDIFILEPLAYER /////////////////////////////

	/** A tempo correction factor that adjusts MIDI track playback speed. */
	public static final double  TRACK_PLAYBACK_SPEED_CORRECTION = 50000;
	
	/** A tempo correction factor that adjusts MIDI note playback speed. */
	public static final double  NOTE_PLAYBACK_SPEED_CORRECTION = 4;
	
	/** A delay (ms) added between notes during the playback of a MIDI song. */
	public static final double	CONSECUTIVE_NOTE_CORRECTION = 0.3; // in milliseconds
}
