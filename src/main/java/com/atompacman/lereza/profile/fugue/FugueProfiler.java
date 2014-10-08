package com.atompacman.lereza.profile.fugue;

import com.atompacman.lereza.profile.harmonic.ContrapunctalProfiler;
import com.atompacman.lereza.piece.container.Piece;
import com.atompacman.lereza.profile.Profile;
import com.atompacman.lereza.profile.Profiler;

public class FugueProfiler extends Profiler {

	private FugueProfile profile;
	
	
	//------------ VERIFY ------------\\

	public boolean verifyProfilability(Piece piece) {
		return true;
	}
	
	
	//------------ PROFILE ------------\\

	public Profile profile(Piece piece) {
		prepareProfiler(piece);
		profile = new FugueProfile();
		
		profile.addSubProfile(new ContrapunctalProfiler().profile(piece));
		
		return profile;
	}
}
