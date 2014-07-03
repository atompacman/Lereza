package atompacman.leraza.midi.api;

import java.util.List;
import atompacman.leraza.midi.container.MidiInstrument;
import atompacman.lereza.common.architecture.DeviceAPI;

public interface MidiFilePlayerAPI extends DeviceAPI {

	void startNote(int note, int channel);
	void startNote(int note);
	void stopNote (int note, int channel);
	void stopNote (int note);

	void playNoteFor(int note, int length, double tempo, int channel);
	void playNoteFor(int note, int length, double tempo);

	void playMIDItrack(String filePath, int track, int finalTimestamp, double tempo, MidiInstrument instrument);
	void playMIDItrack(String filePath, int track, int finalTimestamp, double tempo);

	void playMIDIFile(String filePath, List<MidiInstrument> instruments);
	void playMIDIFile(String filePath);

	void setInstrument(MidiInstrument instr, int channel);
}
