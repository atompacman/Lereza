package atompacman.lereza.midi.test;

import atompacman.atomLog.Log;
import atompacman.atomLog.Log.Verbose;

public class TestSetup {
	
	static {
		Log.setVerbose(Verbose.EXTRA);
	}
	
	protected static boolean validArgs(String args[], int nbArgs) {
		if (args.length < nbArgs) {
			Log.error("Not enough arguments provided.");
			return false;
		}
		return true;
	}
}
