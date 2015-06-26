package com.atompacman.lereza.core.profile.drum;

public enum PercussionElement {

	// CYMBAL
	CLOSED_HI_HAT 	(PercussionType.CYMBAL),
	OPENED_HI_HAT 	(PercussionType.CYMBAL),
	SPLASH_CYMBAL 	(PercussionType.CYMBAL),
	RIDE_CYMBAL 	(PercussionType.CYMBAL),
	CHINA_CYMBAL 	(PercussionType.CYMBAL),
	CRASH_CYMBAL 	(PercussionType.CYMBAL),
	BELL_CYMBAL 	(PercussionType.CYMBAL),

	// TOM
	HIGH_TOM 		(PercussionType.TOM),
	MIDDLE_TOM 		(PercussionType.TOM),
	LOW_TOM 		(PercussionType.TOM),
	FLOOR_TOM 		(PercussionType.TOM),
	
	// SNARE
	SNARE_DRUM 		(PercussionType.SNARE),
	HAND_CLAP 		(PercussionType.SNARE),
	DRUM_STICK		(PercussionType.SNARE),

	// BASS DRUM
	KICK_DRUM 		(PercussionType.BASS_DRUM),
	
	// OTHER
	COWBELL			(PercussionType.OTHER);

	
	
	//======================================= FIELDS =============================================\\

	private PercussionType type;
	
	
	
	//======================================= METHODS ============================================\\

	//---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

	private PercussionElement(PercussionType type) {
		this.type = type;
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public PercussionType getType() {
		return type;
	}
}
