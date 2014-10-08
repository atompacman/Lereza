package com.atompacman.lereza.profile.harmonic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atompacman.lereza.profile.tool.DataChart.Alignement;
import com.atompacman.lereza.profile.tool.DataChart.Importance;
import com.atompacman.lereza.profile.tool.ProfileReportFormatter;
import com.atompacman.lereza.profile.tool.ProfileReportFormatter.Orientation;
import com.atompacman.lereza.profile.Profile;

public class BasicStatisticsProfile extends Profile {
	
	protected int partCount;
	protected int barCount;
	protected int notesInPieceCount;
	protected int lastTimestamp;
	protected Map<Integer, Integer> stackSizeCount;
	protected boolean isPolyphonic;
	
	
	//------------ CONSTRUCTORS ------------\\

	public BasicStatisticsProfile(int nbParts) {
		super(PartBasicStatisticsProfile.class);
		this.partCount = nbParts;
		this.notesInPieceCount = 0;
		this.stackSizeCount = new HashMap<Integer, Integer>();
	}

	
	//------------ SETTERS ------------\\

	public void incrementNoteCountOf(int partNo, int barNo, int count) {
		notesInPieceCount += count;
		getPartProfile(partNo).notesInPartCount += count;
		
		Integer notesInBarCount = getPartProfile(partNo).notesInBarCount.get(barNo);
		if (notesInBarCount == null) {
			getPartProfile(partNo).notesInBarCount.put(barNo, count);
		} else {
			notesInBarCount += count;
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
	
	
	//------------ GETTERS ------------\\

	public boolean pieceIsPolyphonic() {
		List<Profile> partBasicStatsProfiles = getSubProfiles(PartBasicStatisticsProfile.class);
		
		for (int i = 0; i < partBasicStatsProfiles.size(); ++i) {
			if (!((PartBasicStatisticsProfile) partBasicStatsProfiles.get(i)).isMonophonic()) {
				return false;
			}
		}
		return true;
	}
	
	private PartBasicStatisticsProfile getPartProfile(int partNo) {
		return (PartBasicStatisticsProfile) getSubProfile(PartBasicStatisticsProfile.class, partNo);
	}
	
	private List<PartBasicStatisticsProfile> getPartProfiles() {
		List<PartBasicStatisticsProfile> profiles = new ArrayList<PartBasicStatisticsProfile>();
		
		for (Profile subProfile : getSubProfiles(PartBasicStatisticsProfile.class)) {
			profiles.add((PartBasicStatisticsProfile) subProfile);
		}
		return profiles;
	}
	
	
	//------------ REPORT ------------\\

	public ProfileReportFormatter getReportFormatter() {
		if (formatter.isEmpty()) {
			addBasicPieceStatisticsChart();
			addGlobalStackSizeChart();
			addPartsBasicStatistics();
		}
		
		return formatter;
	}
	
	private void addBasicPieceStatisticsChart() {
		formatter.addChart("Basic piece statistics", Importance.VERY_LOW);
		formatter.selectChart("Basic piece statistics");
		formatter.setDataAt(0, 0, "Part count");
		formatter.setDataAt(0, 1, partCount);
		formatter.setDataAt(1, 0, "Bar count");
		formatter.setDataAt(1, 1, barCount);
		formatter.setDataAt(2, 0, "Note count");
		formatter.setDataAt(2, 1, notesInPieceCount);
		formatter.setDataAt(3, 0, "Last timestamp");
		formatter.setDataAt(3, 1, lastTimestamp);
	}
	
	private void addGlobalStackSizeChart() {
		formatter.addChartFromMap("Stack count statistics", Importance.VERY_LOW, stackSizeCount, 
				"Stack size", "Count", Orientation.LANDSCAPE);
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
		formatter.setDataAt(0, 2, "Unempty bar count");
		formatter.setDataAt(0, 3, "First unempty bar");
		formatter.setDataAt(0, 4, "Last unempty bar");
		formatter.setDataAt(0, 5, "Final timestamp");
		
		List<PartBasicStatisticsProfile> partProfiles = getPartProfiles();
		
		for (int i = 0; i < partProfiles.size(); ++i) {
			PartBasicStatisticsProfile profile = partProfiles.get(i);
			formatter.setDataAt(i + 1, 0, "Part " + (i + 1));
			formatter.setDataAt(i + 1, 1, profile.notesInPartCount);
			formatter.setDataAt(i + 1, 2, profile.unEmptyBarsInPartCount);
			formatter.setDataAt(i + 1, 3, profile.firstUnemptyBar);
			formatter.setDataAt(i + 1, 4, profile.lastUnemptyBar);
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
		
		List<PartBasicStatisticsProfile> partProfiles = getPartProfiles();
		
		for (PartBasicStatisticsProfile profile : partProfiles) {
			int partBiggestStackSize = profile.stackSizeCount.keySet().size();
			if (biggestStackSize < partBiggestStackSize) {
				biggestStackSize = partBiggestStackSize;
			}
		}
		formatter.setDataAt(0, 0, "Stack size");
		
		for (int y = 0; y < biggestStackSize; ++y) {
			formatter.setDataAt(0, y + 1, y + 1);

			for (int x = 0; x < partProfiles.size(); ++x) {
				PartBasicStatisticsProfile profile = partProfiles.get(x);
				int stackSizeCount = profile.stackSizeCount.get(y + 1);
				formatter.setDataAt(x + 1, 0, "Part " + (x + 1));
				formatter.setDataAt(x + 1, y + 1, stackSizeCount);
			}
		}
	}
}
