package com.atompacman.lereza.core.profile.harmonic.polyphonic.contrapuntal;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.core.piece.container.Piece;
import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.Profiler;
import com.atompacman.lereza.core.profile.harmonic.polyphonic.PolyphonicProfiler;

public class ContrapunctalProfiler extends Profiler {

	private ContrapunctalProfile profile;
	
	
	public ContrapunctalProfiler(Piece piece, Profile profile) {
		super(piece, profile);
		this.profile = (ContrapunctalProfile) originalProfile;
	}

	public void verifyProfilability() {
		
	}
	
	public void profile() {
		if(Log.infos() && Log.title(this.getClass().getSimpleName(), 0));
		profile = (ContrapunctalProfile) originalProfile;
		
		new PolyphonicProfiler(piece, profile).profile();
	}
}
