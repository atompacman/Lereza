package atompacman.leraza.midi.api;

import atompacman.lereza.common.architecture.DeviceAPI;

public interface MidiFileReaderAPI extends DeviceAPI {

	void read(String filePath);
}
