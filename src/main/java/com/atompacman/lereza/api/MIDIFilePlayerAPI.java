package com.atompacman.lereza.api;

import com.atompacman.lereza.exception.DatabaseException;
import com.atompacman.lereza.midi.container.MIDIInstrument;

public interface MIDIFilePlayerAPI extends Device {

	//---------------------------- START NOTE / STOP NOTE / REST ---------------------------------\\

	void startNote(int note);
	void stopNote (int note);
	
	void startNote(int note, int channel);
	void stopNote (int note, int channel);

	void playNote(int note, int length, double tempo);

	void rest(int length, double tempo);

	
	//-------------------------------------- PLAY TRACK ------------------------------------------\\

	void playTrack(int caseID, int track, double tempo) throws DatabaseException;
	void playTrack(int caseID, int track, double tempo, MIDIInstrument instrument) 
			throws DatabaseException;

	
	//-------------------------------------- PLAY FILE -------------------------------------------\\

	void playFile(int caseID) throws DatabaseException;
	void playFileAndWait(int caseID) throws DatabaseException;

	
	//----------------------------------- SET INSTRUMENT -----------------------------------------\\

	void setInstrument(MIDIInstrument instr, int channel);
	void setInstrumentToAllChannels(MIDIInstrument instr);
}
