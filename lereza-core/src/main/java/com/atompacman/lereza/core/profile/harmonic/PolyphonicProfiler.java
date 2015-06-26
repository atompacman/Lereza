package com.atompacman.lereza.core.profile.harmonic;

import com.atompacman.atomlog.Log;
import com.atompacman.lereza.core.piece.container.Part;
import com.atompacman.lereza.core.piece.container.Piece;
import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.Profiler;

public class PolyphonicProfiler extends Profiler {
	
	private PolyphonicProfile profile;
	
	
	//------------ VERIFY ------------\\

	public boolean verifyProfilability(Piece piece) {
		if(Log.infos() && Log.title(getClass().getSimpleName() + " - Verifying profitability", 0));

		for (int i = 0; i < piece.numParts(); ++i) {
			navig.goToPart(i);
			navig.goToFirstUnemptyBar();
			navig.goToFirstNoteOfBar();
			while (!navig.endOfPart()) {
				if (navig.getCurrentNoteStack().size() > 1) {
					Part currPart = navig.getCurrentPart();
					problems.add(new UnmonophonicTimeunitProblem(navig.getPBT(), currPart));
				}
				navig.goToNextNote();
			}
		}
		return problems.isEmpty();
	}
	
	
	//------------ PROFILE ------------\\

	public Profile profile(Piece piece) {
		if(Log.infos() && Log.title(this.getClass().getSimpleName(), 0));
		profile = new PolyphonicProfile();
		
		new HarmonicProfiler().profile(piece);
		
		return profile;
	}
}
