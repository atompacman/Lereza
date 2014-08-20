package com.atompacman.lereza.core.profile.fugue;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.core.piece.container.Piece;
import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.Profiler;
import com.atompacman.lereza.core.profile.harmonic.polyphonic.contrapuntal.ContrapunctalProfiler;

public class FugueProfiler extends Profiler {

	private FugueProfile profile;
	
	
	public FugueProfiler(Piece piece, Profile profile) {
		super(piece, profile);
		this.profile = (FugueProfile) originalProfile;
	}

	public void verifyProfilability() {
		
	}
	
	public void profile() {
		if(Log.infos() && Log.title(this.getClass().getSimpleName(), 0));
		
		new ContrapunctalProfiler(piece, profile.getContrapuntalProfile()).profile();
	}
}
