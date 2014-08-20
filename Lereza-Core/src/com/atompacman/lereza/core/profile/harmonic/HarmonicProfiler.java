package com.atompacman.lereza.core.profile.harmonic;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.core.piece.container.Part;
import com.atompacman.lereza.core.piece.container.Piece;
import com.atompacman.lereza.core.piece.tool.PieceNavigator;
import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.Profiler;
import com.atompacman.lereza.core.profile.harmonic.polyphonic.PolyphonicProfile;
import com.atompacman.lereza.core.profile.key.KeyProfiler;

public class HarmonicProfiler extends Profiler {

	private HarmonicProfile profile;
	private PieceNavigator navig;
	

	//////////////////////////////
	//       CONSTRUCTOR        //
	//////////////////////////////
	
	public HarmonicProfiler(Piece piece, Profile profile) {
		super(piece, profile);
		this.profile = (HarmonicProfile) originalProfile;
		this.navig = new PieceNavigator(piece);
	}
	
	
	//////////////////////////////
	//   VERIFY PROFILABILITY   //
	//////////////////////////////
	
	public void verifyProfilability() {
		if(Log.infos() && Log.title(this.getClass().getSimpleName() + " - Verifying profitability", 0));
		
		if (profile instanceof PolyphonicProfile) {
			verifyPolyphony();
		}
	}
	
	private void verifyPolyphony() {
		if(Log.infos() && Log.title("Verifying polyphony", 1));
		
		for (int i = 0; i < piece.getNbParts(); ++i) {
			navig.goToPart(i);
			navig.goToFirstUnemptyMeasure();
			navig.goToFirstNoteOfMeasure();
			while (!navig.endOfPart()) {
				if (navig.getCurrentNoteStack().size() > 1) {
					profile.getBasicStatisticsProfile().problems.add(new UnmonophonicTimeunitProblem(navig.getPMT(), navig.getCurrentPart()));
				}
				navig.goToNextNote();
			}
		}
	}
	
	
	//////////////////////////////
	//  	   PROFILE		    //
	//////////////////////////////
	
	public void profile() {
		if(Log.infos() && Log.title(this.getClass().getSimpleName() + " - Profiling", 0));
		
		profileBasicStatistics();
		
		new KeyProfiler(piece, profile.getKeyProfile()).profile();
	}
	
	
	// ----- Basic statistics ----- \\
	
	private void profileBasicStatistics() {
		if(Log.infos() && Log.title("General statistics", 1));

		BasicStatisticsProfile basicStatisticsProfile = profile.getBasicStatisticsProfile();
		
		basicStatisticsProfile.measureCount = piece.getPartNo(0).getNbMeasures();
		basicStatisticsProfile.lastTimestamp = piece.getPartNo(0).getNbMeasures() * piece.getRythmicSignature().getMeasureTimeunitLength();
		
		for (int i = 0; i < piece.getNbParts(); ++i) {
			countNbUnemptyMeasuresInPart(i);
			findPartLastTimestamp(i);
			navig.goToPart(i);
			navig.goToFirstUnemptyMeasure();
			navig.goToFirstNoteOfMeasure();
			while (!navig.endOfPart()) {
				int stackSize = navig.getCurrentNoteStack().size();
				basicStatisticsProfile.incrementNoteCountOf(i, navig.getMeasureInPart(), stackSize);
				basicStatisticsProfile.incrementStackSizeCount(i, stackSize);
				navig.goToNextNote();
			}
		}
	}

	private void countNbUnemptyMeasuresInPart(int partNo) {
		PartBasicStatisticsProfile partProfile = profile.getBasicStatisticsProfile().getPartProfile(partNo);
		Part part = piece.getPartNo(partNo);
		int nbMeasures = part.getNbMeasures();
		int unemptyMeasures = 0;

		for (int i = 0; i < nbMeasures; ++i) {
			if (!part.getMeasureNo(i).isEmpty()) {
				if (partProfile.firstUnemptyMeasure == -1) {
					partProfile.firstUnemptyMeasure = i;
				}
				partProfile.lastUnemptyMeasure = i;
				++unemptyMeasures;
			}
		}
		partProfile.unEmptyMeasuresInPartCount = unemptyMeasures;
	}
	
	private void findPartLastTimestamp(int partNo) {
		PartBasicStatisticsProfile partProfile = profile.getBasicStatisticsProfile().getPartProfile(partNo);
		int nbTimeunitsInMeasure = piece.getRythmicSignature().getMeasureTimeunitLength();

		for (int i = nbTimeunitsInMeasure * piece.getPartNo(partNo).getNbMeasures() - 1; i >= 0; --i) {
			if (!piece.getPartNo(partNo).getMeasureNo(i / nbTimeunitsInMeasure).getNotes().get(i % nbTimeunitsInMeasure).isEmpty()) {
				partProfile.finalTimestamp = i;
				return;
			}
		}
	}
}
