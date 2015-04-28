package com.atompacman.lereza.profile.harmonic;

import org.junit.After;
import org.junit.Test;

import com.atompacman.lereza.exception.DatabaseException;
import com.atompacman.lereza.exception.MIDIFileReaderException;
import com.atompacman.lereza.piece.container.Piece;
import com.atompacman.lereza.profile.Profile;
import com.atompacman.lereza.profile.ProfilerTestHelper;
import com.atompacman.lereza.profile.tool.ProfileReportFormatter;
import com.atompacman.lereza.profile.tool.DataChart.Importance;

public class TestHarmonicProfiler {

	private HarmonicProfiler profiler = new HarmonicProfiler();
	
	
	@Test
	public void simpleShortMonophonicPiece() throws DatabaseException, MIDIFileReaderException {
		Piece piece = ProfilerTestHelper.loadPiece(5);
		Profile profile = profiler.profile(piece);
		ProfileReportFormatter formatter = profile.getReportFormatter();
		formatter.addProblems(profiler.getProfilabilityProblems());
		formatter.print(Importance.VERY_LOW);
	}
	
	@After
	public void afterTest() {
		
	}
}
