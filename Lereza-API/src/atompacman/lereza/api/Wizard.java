package atompacman.lereza.api;

import java.text.SimpleDateFormat;
import java.util.Date;

import atompacman.atomLog.Log;
import atompacman.lereza.api.manager.MidiFileManagerAPI;
import atompacman.lereza.api.manager.SongManagerAPI;
import atompacman.lereza.common.formatting.Formatting;

public class Wizard {

	public static MidiFileManagerAPI midiFileManager;
	public static SongManagerAPI	 songManager;
	
	private static Date wizardInitializationTime;


	//////////////////////////////
	//     INITIALIZATION       //
	//////////////////////////////

	public static void initialize() {
		Log.infos(Formatting.lineSeparation("Lereza Wizard", 0));
		getInitializationTime();
		midiFileManager = new MidiFileManagerAPI();
		songManager = new SongManagerAPI();
	}

	private static void getInitializationTime() {
		wizardInitializationTime = new Date();
		SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat timestamp  = new SimpleDateFormat("HH:mm:ss");
		Log.infos("Lereza Wizard initialized on " + date.format(wizardInitializationTime) + " at " + timestamp.format(wizardInitializationTime) + ".");
	}
}