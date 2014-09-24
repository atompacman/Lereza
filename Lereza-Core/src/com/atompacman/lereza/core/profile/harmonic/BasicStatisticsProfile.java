package com.atompacman.lereza.core.profile.harmonic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atompacman.lereza.core.profile.Problem;
import com.atompacman.lereza.core.profile.Profile;
import com.atompacman.lereza.core.profile.tool.DataChart.Alignement;
import com.atompacman.lereza.core.profile.tool.DataChart.Importance;
import com.atompacman.lereza.core.profile.tool.ProfileReportFormatter;
import com.atompacman.lereza.core.profile.tool.ProfileReportFormatter.Orientation;

public class BasicStatisticsProfile extends Profile {

	private List<PartBasicStatisticsProfile> partBasicStatisticsProfiles;

	protected List<Problem> problems;
	
	protected int partCount;
	protected int measureCount;
	protected int notesInPieceCount;
	protected int lastTimestamp;
	protected Map<Integer, Integer> stackSizeCount;
	protected boolean isPolyphonic;
	
	
	//------------ CONSTRUCTORS ------------\\

	public BasicStatisticsProfile(int nbParts) {
		this.partBasicStatisticsProfiles = new ArrayList<PartBasicStatisticsProfile>();
		for (int i = 0; i < nbParts; ++i) {
			this.partBasicStatisticsProfiles.add(new PartBasicStatisticsProfile());
		}
		this.problems = new ArrayList<Problem>();
		this.partCount = nbParts;
		this.notesInPieceCount = 0;
		this.stackSizeCount = new HashMap<Integer, Integer>();
	}
	
	
	//------------ SETTERS ------------\\

	public void incrementNoteCountOf(int partNo, int measureNo, int count) {
		notesInPieceCount += count;
		getPartProfile(partNo).notesInPartCount += count;
		
		Integer notesInMeasureCount = getPartProfile(partNo).notesInMeasureCount.get(measureNo);
		if (notesInMeasureCount == null) {
			getPartProfile(partNo).notesInMeasureCount.put(measureNo, count);
		} else {
			notesInMeasureCount += count;
		}
	}
	
	public void incrementStackSizeCount(int partNo, int stackSize) {
		Integer previousCount = getPartProfile(partNo).stackSizeCount.get(stackSize);
		
		if (previousCount == null) {
			previousCount = 0;
		}
		getPartProfile(partNo).stackSizeCount.put(stackSize, previousCount + 1);
		
		Integer previousGlobalCount = stackSizeCount.get(stackSize);
		
		if(previousGlobalCount == null) {
			previousGlobalCount = 0;
		}
		stackSizeCount.put(stackSize, previousGlobalCount + 1);
	}
	
	
	//------------ SUBPROFILES ------------\\

	public PartBasicStatisticsProfile getPartProfile(int partNo) {
		return partBasicStatisticsProfiles.get(partNo);
	}
	
	
	//------------ GETTERS ------------\\

	public boolean pieceIsPolyphonic() {
		for (int i = 0; i < partBasicStatisticsProfiles.size(); ++i) {
			if (!partBasicStatisticsProfiles.get(i).isMonophonic()) {
				return false;
			}
		}
		return true;
	}
	
	
	//------------ REPORT ------------\\

	public ProfileReportFormatter getReportFormatter() {
		if (formatter.isEmpty()) {
			addBasicPieceStatisticsChart();
			addGlobalStackSizeChart();
			addPartsBasicStatistics();
		}
		formatter.addProblems(problems);
		
		return formatter;
	}
	
	private void addBasicPieceStatisticsChart() {
		formatter.addChart("Basic piece statistics", Importance.VERY_LOW);
		formatter.selectChart("Basic piece statistics");
		formatter.setDataAt(0, 0, "Part count");
		formatter.setDataAt(0, 1, partCount);
		formatter.setDataAt(1, 0, "Measure count");
		formatter.setDataAt(1, 1, measureCount);
		formatter.setDataAt(2, 0, "Note count");
		formatter.setDataAt(2, 1, notesInPieceCount);
		formatter.setDataAt(3, 0, "Last timestamp");
		formatter.setDataAt(3, 1, lastTimestamp);
	}
	
	private void addGlobalStackSizeChart() {
		formatter.addChartFromMap("Stack count statistics", Importance.VERY_LOW, stackSizeCount, "Stack size", "Count", Orientation.LANDSCAPE);
	}
	
	private void addPartsBasicStatistics() {
		formatter.addChildProfile(PartBasicStatisticsProfile.class);
		formatter.selectChildProfile(PartBasicStatisticsProfile.class);
		addBasicPartStatisticsChart();
		addPartStackSizeCountChart();
	}
	
	private void addBasicPartStatisticsChart() {
		formatter.addChart("Basic part statistics", Importance.VERY_LOW);
		formatter.selectChart("Basic part statistics");
		
		formatter.changeChartProperties().setHorizBorder(1, "-");
		formatter.changeChartProperties().setVertBorder(1, ":");

		formatter.setDataAt(0, 1, "Note count");		
		formatter.setDataAt(0, 2, "Unempty measure count");
		formatter.setDataAt(0, 3, "First unempty measure");
		formatter.setDataAt(0, 4, "Last unempty measure");
		formatter.setDataAt(0, 5, "Final timestamp");
		
		for (int i = 0; i < partBasicStatisticsProfiles.size(); ++i) {
			PartBasicStatisticsProfile profile = partBasicStatisticsProfiles.get(i);
			formatter.setDataAt(i + 1, 0, "Part " + (i + 1));
			formatter.setDataAt(i + 1, 1, profile.notesInPartCount);
			formatter.setDataAt(i + 1, 2, profile.unEmptyMeasuresInPartCount);
			formatter.setDataAt(i + 1, 3, profile.firstUnemptyMeasure);
			formatter.setDataAt(i + 1, 4, profile.lastUnemptyMeasure);
			formatter.setDataAt(i + 1, 5, profile.finalTimestamp);
		}
	}

	private void addPartStackSizeCountChart() {
		formatter.addChart("Part stack size count", Importance.VERY_LOW);
		formatter.selectChart("Part stack size count");
		
		formatter.changeChartProperties().setHorizBorder(1, "-");
		formatter.changeChartProperties().setVertBorder(1, ":");
		formatter.changeChartProperties().setColumnAlignement(0, Alignement.CENTER);
		
		int biggestStackSize = 0;
		
		for (PartBasicStatisticsProfile profile : partBasicStatisticsProfiles) {
			int partBiggestStackSize = profile.stackSizeCount.keySet().size();
			if (biggestStackSize < partBiggestStackSize) {
				biggestStackSize = partBiggestStackSize;
			}
		}
		formatter.setDataAt(0, 0, "Stack size");
		
		for (int y = 0; y < biggestStackSize; ++y) {
			formatter.setDataAt(0, y + 1, y + 1);

			for (int x = 0; x < partBasicStatisticsProfiles.size(); ++x) {
				PartBasicStatisticsProfile profile = partBasicStatisticsProfiles.get(x);
				int stackSizeCount = profile.stackSizeCount.get(y + 1);
				formatter.setDataAt(x + 1, 0, "Part " + (x + 1));
				formatter.setDataAt(x + 1, y + 1, stackSizeCount);
			}
		}
	}
}
