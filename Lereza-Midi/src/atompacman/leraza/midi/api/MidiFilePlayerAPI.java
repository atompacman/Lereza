package atompacman.leraza.midi.api;

import java.util.List;

import atompacman.leraza.midi.container.MidiInstrument;
import atompacman.lereza.common.architecture.DeviceAPI;

public interface MidiFilePlayerAPI extends DeviceAPI {

	void startNote(int note, int channel);
	void startNote(int note);
	void stopNote (int note, int channel);
	void stopNote (int note);

	void playNote(int note, int length, double tempo, int channel);
	void playNote(int note, int length, double tempo);

	void playTrack(String filePath, int track, double tempo, MidiInstrument instrument);
	void playTrack(String filePath, int track, double tempo);

	void playFile(String filePath);
	void playFileAndWait(String filePath);
	void playFile(String filePath, List<MidiInstrument> instruments);
	
	void setInstrument(MidiInstrument instr, int channel);
}
