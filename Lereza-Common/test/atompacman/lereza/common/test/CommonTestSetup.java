package atompacman.lereza.common.test;

import atompacman.atomLog.Log;
import atompacman.atomLog.Log.Verbose;

public class CommonTestSetup {

	static {
		Log.setVerbose(Verbose.INFOS);
	}
	
	protected static boolean validArgs(String args[], int nbArgs) {
		if (args.length < nbArgs) {
			Log.error("Not enough arguments provided.");
			return false;
		}
		return true;
	}
}
