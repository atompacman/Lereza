package atompacman.lereza.menu.test;

import atompacman.atomLog.Log;
import atompacman.atomLog.Log.Verbose;
import atompacman.lereza.api.Wizard;

public class TestSetup {
	
	static {
		Log.setVerbose(Verbose.INFOS);
		Wizard.initialize();
	}
	
	protected static boolean validArgs(String args[], int nbArgs) {
		if (args.length < nbArgs) {
			Log.error("Not enough arguments provided.");
			return false;
		}
		return true;
	}
}
