package com.atompacman.lereza.api.test;

import com.atompacman.atomLog.Log;
import com.atompacman.atomLog.Log.Verbose;
import com.atompacman.lereza.api.Wizard;
import com.atompacman.lereza.common.solfege.Context.Form;
import com.atompacman.lereza.core.profile.tool.DataChart.Importance;

public class SystemTester {
	
	public static final String SYSTEM_TESTS_ROOT = ".../Data/Test/System";
	
	
	public static void runWithFile(TestFile testFile) {
		Log.setVerbose(Verbose.EXTRA);
		
		String filePath = testFile.getFilePath();
		String title = testFile.getTitle();
		String artist = testFile.getArtist();
		String compositionSet = testFile.getCompositionSet();
		Form form = testFile.getContext().getForm();
		
		Wizard.initialize();
		Wizard.midiFileReader.read(filePath);
		Wizard.pieceBuilder.build(filePath);
		Wizard.library.addComposition(filePath, title, artist, form, compositionSet);
		Wizard.profileManager.profile(title, artist, compositionSet);
		Wizard.profileManager.printReport(title, artist, Importance.VERY_LOW);
	}
}
