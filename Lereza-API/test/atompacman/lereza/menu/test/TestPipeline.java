package atompacman.lereza.menu.test;

import atompacman.lereza.api.Wizard;

public class TestPipeline extends TestSetup {

	private static final int NB_ARGS = 2;

	
	public static void main(String args[]) {
		if (!validArgs(args, NB_ARGS)) {
			return;
		}
		for (String path : args) {
			Wizard.midiFileManager.reader.read(path);
			Wizard.songManager.builder.build(path);
		}
	}
}