package com.atompacman.lereza.midi;

import java.io.IOException;

import com.atompacman.configuana.AppLauncher;
import com.atompacman.lereza.Parameters.Paths;
import com.atompacman.lereza.api.Wizard;
import com.atompacman.lereza.exception.MIDIFileReaderException;
import com.atompacman.lereza.report.ReportManager;

public class TestMIDIFileReader {

	public static void main(String[] args) throws MIDIFileReaderException, IOException {
		AppLauncher.createApp(Paths.APP_CONFIG_FILE_PATH);
		Wizard.initDevice(ReportManager.class);
		MIDIFileReader.getInstance().read("resources/midi/Bach/Concerto Arrangements "
				+ "For Harpsichord/Concerto In D, BWV 972 - I - Allegro.mid");
	}
}
