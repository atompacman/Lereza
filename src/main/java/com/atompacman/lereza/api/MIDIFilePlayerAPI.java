package com.atompacman.lereza.api;

import com.atompacman.lereza.exception.DatabaseException;
import com.atompacman.lereza.midi.container.MIDIInstrument;

public interface MIDIFilePlayerAPI {

	void startNote(int note, int channel);
	void stopNote (int note, int channel);

	void startNote(int note);
	void stopNote (int note);

	void playNote(int note, int length, double tempo);

	void rest(int length, double tempo);

	void playTrack(int caseID, int track, double tempo) throws DatabaseException;
	void playTrack(int caseID, int track, double tempo, MIDIInstrument instrument) 
			throws DatabaseException;

	void playFile(int caseID) throws DatabaseException;
	void playFileAndWait(int caseID) throws DatabaseException;

	void setInstrument(MIDIInstrument instr, int channel);
	void setInstrumentToAllChannels(MIDIInstrument instr);
}
