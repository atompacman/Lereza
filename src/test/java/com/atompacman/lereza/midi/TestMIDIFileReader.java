package com.atompacman.lereza.midi;

import java.io.IOException;

import com.atompacman.configuana.AppLauncher;
import com.atompacman.lereza.Parameters.Paths;
import com.atompacman.lereza.api.Wizard;
import com.atompacman.lereza.exception.MIDIFileReaderException;
import com.atompacman.toolkat.io.IO;

public class TestMIDIFileReader {

	public static void main(String[] args) throws MIDIFileReaderException, IOException {
		AppLauncher.createApp(Paths.APP_CONFIG_FILE_PATH);
		MIDISequence seq = Wizard.getModule(MIDIFileReader.class).read(
				IO.getFile("../../Lereza/resources/midi/Bach/Keyboard Works/The Well-Tempered Clavier/Book I/Prelude & Fugue No.1 In C, BWV 846 - Fugue.mid"));
		seq.getClass();
	}
}
