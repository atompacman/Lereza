package com.atompacman.lereza.api;

import com.atompacman.lereza.exception.DatabaseException;
import com.atompacman.lereza.exception.MIDIFileReaderException;

public interface MIDIFileReaderAPI {

	void read(int fileID) throws MIDIFileReaderException, DatabaseException;
}
