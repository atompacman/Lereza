package com.atompacman.lereza.profile.harmonic;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.piece.container.Part;
import com.atompacman.lereza.piece.container.Piece;
import com.atompacman.lereza.profile.Profile;
import com.atompacman.lereza.profile.Profiler;

public class HarmonicProfiler extends Profiler {

	private HarmonicProfile profile;
	private BasicStatisticsProfile basicStatsProfile;
	private PartBasicStatisticsProfile currPartProfile;
	
	
	//------------ VERIFY PROFILABILITY ------------\\

	public boolean verifyProfilability(Piece piece) {
		return true;
	}
	
	
	//------------ PROFILE ------------\\

	public Profile profile(Piece piece) {
		prepareProfiler(piece);
		profile = new HarmonicProfile();
		
		profileBasicStatistics();
		
		profile.addSubProfile(basicStatsProfile);
		
		return profile;
	}
	
	private void profileBasicStatistics() {
		if(Log.infos() && Log.title("General statistics", 1));

		basicStatsProfile = new BasicStatisticsProfile(piece.getNbParts());

		basicStatsProfile.barCount = piece.getPartNo(0).getNbBars();
		basicStatsProfile.lastTimestamp = piece.getPartNo(0).getNbBars() 
				* piece.getRythmicSignature().getBarTimeunitLength();
		
		for (int i = 0; i < piece.getNbParts(); ++i) {
			currPartProfile = new PartBasicStatisticsProfile();
			basicStatsProfile.addSubProfile(currPartProfile);

			countNbUnemptyBarsInPart(i);
			findPartLastTimestamp(i);
			navig.goToPart(i);
			navig.goToFirstUnemptyBar();
			navig.goToFirstNoteOfBar();
			while (!navig.endOfPart()) {
				int stackSize = navig.getCurrentNoteStack().size();
				basicStatsProfile.incrementNoteCountOf(i, navig.getBarInPart(), stackSize);
				basicStatsProfile.incrementStackSizeCount(i, stackSize);
				navig.goToNextNote();
			}
		}
	}

	private void countNbUnemptyBarsInPart(int partNo) {
		Part part = piece.getPartNo(partNo);
		int nbBars = part.getNbBars();
		int unemptyBars = 0;

		for (int i = 0; i < nbBars; ++i) {
			if (!part.getBarNo(i).isEmpty()) {
				if (currPartProfile.firstUnemptyBar == -1) {
					currPartProfile.firstUnemptyBar = i;
				}
				currPartProfile.lastUnemptyBar = i;
				++unemptyBars;
			}
		}
		currPartProfile.unEmptyBarsInPartCount = unemptyBars;
	}
	
	private void findPartLastTimestamp(int partNo) {
		int nbTimeunitsInBar = piece.getRythmicSignature().getBarTimeunitLength();

		for (int i = nbTimeunitsInBar * piece.getPartNo(partNo).getNbBars() - 1; i >= 0; --i) {
			if (!piece.getPartNo(partNo).getBarNo(i / nbTimeunitsInBar).getNotes().get(i % nbTimeunitsInBar).isEmpty()) {
				currPartProfile.finalTimestamp = i;
				return;
			}
		}
	}
}
