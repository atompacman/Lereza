package atompacman.lereza.menu.test;

import atompacman.atomLog.Log;
import atompacman.atomLog.Log.Verbose;
import atompacman.leraza.midi.container.MidiInstrument;
import atompacman.lereza.api.Wizard;

public class TestPipeline extends PipelineTestSetup {

	private static final int NB_ARGS = 2;

	
	public static void main(String args[]) {
		if (!validArgs(args, NB_ARGS)) {
			return;
		}
		for (String path : args) {
			Log.setVerbose(Verbose.INFOS);
			Wizard.midiFileManager.reader.read(path);
			Wizard.midiFileManager.player.setInstrument(MidiInstrument.Acoustic_Guitar_nylon, 0);
			Wizard.songManager.builder.build(path);
		}
	}
}