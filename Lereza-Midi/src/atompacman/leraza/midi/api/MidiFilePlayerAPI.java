package atompacman.leraza.midi.api;

import java.util.Stack;

import atompacman.leraza.midi.container.MidiInstrument;
import atompacman.leraza.midi.container.MidiNote;
import atompacman.lereza.common.architecture.DeviceAPI;
import atompacman.lereza.common.solfege.Pitch;
import atompacman.lereza.common.solfege.Value;

public interface MidiFilePlayerAPI extends DeviceAPI {

	void startNote(int note, int channel);
	void stopNote (int note, int channel);
	
	void startNote(int note);
	void stopNote (int note);

	void playNote(int note, int length, double tempo);
	void playNote(Pitch pitch, Value value, double tempo);
	
	void rest(int length, double tempo);
	
	void playNoteStack(Stack<MidiNote> noteStack, int track, double tempo);
	
	void playTrack(String filePath, int track, double tempo, MidiInstrument instrument);
	void playTrack(String filePath, int track, double tempo);

	void playFile(String filePath);
	void playFileAndWait(String filePath);
	
	void setInstrument(MidiInstrument instr, int channel);
	void setInstrumentToAllChannels(MidiInstrument instr);
}
