package com.atompacman.lereza.core.profile.harmonic;

import com.atompacman.lereza.core.piece.container.Piece;
import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.Profiler;

public class ContrapunctalProfiler extends Profiler {

	private ContrapunctalProfile profile;
	
		
	//------------ VERIFY PROFILABILITY ------------\\

	public boolean verifyProfilability(Piece piece) {
		return true;
	}
	
	
	//------------ PROFILE ------------\\

	public Profile profile(Piece piece) {
		prepareProfiler(piece);
		profile = new ContrapunctalProfile();
		
		new PolyphonicProfiler().profile(piece);
		
		return profile;
	}
}
