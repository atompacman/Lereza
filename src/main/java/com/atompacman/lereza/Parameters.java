package com.atompacman.lereza;

import java.io.File;

import com.atompacman.configuana.param.LaxParam;
import com.atompacman.configuana.param.StrictParam;
import com.atompacman.lereza.solfege.Value;

public enum Parameters implements StrictParam {;
		
	public static class Paths {
		public static final String LEREZA_PACKAGE 			= "com.atompacman.lereza";
		public static final String APP_CONFIG_FILE_PATH		= "../../Lereza/app.json";

		public static class Dirs {
			public static final String RESOURCES_ROOT_DIR = "resources" + File.separator;
			public static final String TEST_ROOT_DIR 	  = "test" + File.separator;
			public static final String ASSETS_ROOT_DIR 	  = RESOURCES_ROOT_DIR + "assets" + File.separator;
			public static final String MIDI_ROOT_DIR 	  = RESOURCES_ROOT_DIR + "midi" + File.separator;
			public static final String SQL_ROOT_DIR 	  = RESOURCES_ROOT_DIR + "sql" + File.separator;
		}
		
		public static class Assets {
			public static final String LOGIN_ICON = Dirs.ASSETS_ROOT_DIR + "title_connections.png";
		}
		
		public static class SQL {
			public static final String MIDI_FILE_INDEX_QUERIES_DIR = "MIDIFileIndex" + File.separator;
		
			public static class MIDIFileIndex {
				public static final String COMPLETE_INDEX				= Dirs.SQL_ROOT_DIR + MIDI_FILE_INDEX_QUERIES_DIR + "completeIndex.sql";
				public static final String GLOBAL_SELECTION				= Dirs.SQL_ROOT_DIR + MIDI_FILE_INDEX_QUERIES_DIR + "globalSelection.sql";
				public static final String CONTEXT_ELEM_ID				= Dirs.SQL_ROOT_DIR + MIDI_FILE_INDEX_QUERIES_DIR + "contextElementID.sql";
				public static final String NO_PARENT_CONTEXT_ELEM		= Dirs.SQL_ROOT_DIR + MIDI_FILE_INDEX_QUERIES_DIR + "noParentContextElement.sql";
				public static final String SINGLE_PARENT_CONTEXT_ELEM	= Dirs.SQL_ROOT_DIR + MIDI_FILE_INDEX_QUERIES_DIR + "singleParentContextElement.sql";
				public static final String MULT_PARENT_CONTEXT_ELEM		= Dirs.SQL_ROOT_DIR + MIDI_FILE_INDEX_QUERIES_DIR + "multipleParentContextElement.sql";
			}
		}
	}
	
	public enum Piece implements StrictParam {;
	
		public enum BuildingAnimation implements LaxParam {
			
			/** If true, activates an console animation that shows that notes that are being added 
			 * during song creation. */
			NOTE_ADDING_VISUALISATION	(false),
			
			/** Play notes when adding/visualising notes */
			NOTE_ADDING_AUDIO			(false),
			
			/** A tempo correction factor that changes the building speed of a song in order to play 
			 * notes in an appreciable way.*/
			NOTE_ADDING_AUDIO_TEMPO		(14);

			private Object defaultValue;
			private BuildingAnimation(Object defaultValue) {this.defaultValue = defaultValue;}
			public Object defaultValue() {return defaultValue;}
		}
		
		public enum TimestampCorrection implements LaxParam {
			
			/** Offbeat timeunit radius. Determines the sensibility of the TimestampsChecker to 
			 * timestamp variations. */
			OFFBEAT_TU_RADIUS				(4),
			
			/** Play notes when adding/visualizing notes */
			LONGEST_FUSIONNABLE_REST_LENGTH	(Value.THIRTYSECONTH.ordinal());

			private Object defaultValue;
			private TimestampCorrection(Object defaultValue) {this.defaultValue = defaultValue;}
			public Object defaultValue() {return defaultValue;}
		}
	}
	
	public enum MIDI implements StrictParam {;
	
		public enum Devices implements LaxParam {
			
			KEYBOARD_CONTROLLER_ID ("SAMSON Carbon61  (No details available)"),
			SYNTHESIZER_ID		   ("LerezaToSynthFontMIDIPort (External MIDI Port)"),
			SEQUENCER_ID		   ("Real Time Sequencer (Software sequencer)");
			
			private Object defaultValue;
			private Devices(Object defaultValue) {this.defaultValue = defaultValue;}
			public Object defaultValue() {return defaultValue;}
		}
	
		public enum FileReader implements LaxParam {
			
			/** Maximum MIDI file size (in bytes) */
			MAX_FILE_SIZE 						(1000000),
			
			/** Display an animation when loading MIDI files. */
			NOTE_VISUALISATION 					(false),
			
			/** If MIDI note visualization is on, play the notes that are being added. */
			NOTE_PLAY_AUDIO 					(true),
			
			/** A tempo correction factor to adjust MIDI note reading process in order to smoothly 
			 * play the song that is being read.*/
			VISUALISATION_SPEED_CORRECTION 		(2.5);
			
	
			private Object defaultValue;
			private FileReader(Object defaultValue) {this.defaultValue = defaultValue;}
			public Object defaultValue() {return defaultValue;}
		}
		
		public enum FilePlayer implements LaxParam {
			
			/** A tempo correction factor that adjusts MIDI track playback speed. */
			TRACK_PLAYBACK_SPEED_CORRECTION (50000),
			
			/** A tempo correction factor that adjusts MIDI note playback speed. */
			NOTE_PLAYBACK_SPEED_CORRECTION 	(4),
			
			/** A delay (ms) added between notes during the playback of a MIDI song. */
			CONSECUTIVE_NOTE_CORRECTION 	(0.3);
	
			private Object defaultValue;
			private FilePlayer(Object defaultValue) {this.defaultValue = defaultValue;}
			public Object defaultValue() {return defaultValue;}
		}
	}
	
	public enum Database implements LaxParam {
		
		FULL_SERVER_ADRESS	("jdbc:mysql://localhost/lereza"),
		DB_CONNECTOR_CLASS	("com.mysql.jdbc.Driver");
		
		private Object defaultValue;
		private Database(Object defaultValue) {this.defaultValue = defaultValue;}
		public Object defaultValue() {return defaultValue;}
	}
	
	public enum Misc implements LaxParam {
		
		RANDOM_SEED		(421521511L);
		
		private Object defaultValue;
		private Misc(Object defaultValue) {this.defaultValue = defaultValue;}
		public Object defaultValue() {return defaultValue;}
	}
}
