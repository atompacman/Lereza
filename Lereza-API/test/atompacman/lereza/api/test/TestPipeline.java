package atompacman.lereza.api.test;

import com.atompacman.atomLog.Log;
import com.atompacman.atomLog.Log.Verbose;
import com.atompacman.lereza.common.solfege.Context.Genre;
import com.atompacman.lereza.core.profile.tool.DataChart.Importance;

import atompacman.leraza.midi.container.MidiInstrument;
import atompacman.lereza.api.Wizard;

public class TestPipeline {

	static {
		Log.setVerbose(Verbose.EXTRA);
	}
	
	public static void main(String args[]) {
		for (int i = 0; i < args.length; ++i){
			String filePath = args[i];
			
			Wizard.initialize();
			
			Wizard.midiFileReader.read(filePath);
			Wizard.midiFilePlayer.setInstrumentToAllChannels(MidiInstrument.Celesta);
			//Wizard.midiFilePlayer.playFile(filePath);
			Wizard.pieceBuilder.build(filePath);
			Wizard.library.addComposition(filePath, "My song no." + i, "Composer no." + i, Genre.BAROQUE.HIGH_BAROQUE.FUGUE, "My set");
			Wizard.profileManager.profile("My song no." + i, "Composer no." + i, "My set");
			Wizard.profileManager.printReport("My song no." + i, "Composer no." + i, Importance.VERY_LOW);
		}
	}
}