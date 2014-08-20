package com.atompacman.lereza.core.profile.harmonic.polyphonic;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.core.piece.container.Piece;
import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.Profiler;
import com.atompacman.lereza.core.profile.harmonic.HarmonicProfiler;

public class PolyphonicProfiler extends Profiler {
	
	private PolyphonicProfile profile;
	
	
	public PolyphonicProfiler(Piece piece, Profile profile) {
		super(piece, profile);
		this.profile = (PolyphonicProfile) originalProfile;
	}

	public void verifyProfilability() {
		
	}
	
	public void profile() {
		if(Log.infos() && Log.title(this.getClass().getSimpleName(), 0));
		
		new HarmonicProfiler(piece, profile).profile();
	}

}
