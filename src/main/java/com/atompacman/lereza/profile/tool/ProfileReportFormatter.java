package com.atompacman.lereza.profile.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.profile.ProfilabilityProblem;
import com.atompacman.lereza.profile.Profile;
import com.atompacman.lereza.profile.tool.DataChart.Importance;

public class ProfileReportFormatter {

	private static final int TITLE_LINE_LENGTH = 100;
	private static final char TITLE_LINE_CHAR = '=';
	private static final int LEVEL_INDENTATION = 5;
		
	public enum Orientation {
		PORTRAIT, LANDSCAPE;
	}
	
	
	private ProfileReportNode rootNode;
	private ProfileReportNode currentProfile;
	private DataChart currentChart;
	
	
	//------------ CONSTRUCTORS ------------\\

	public ProfileReportFormatter(Class<? extends Profile> rootProfile) {
		this.rootNode = new ProfileReportNode(rootProfile, null);
		this.currentProfile = rootNode;
		this.currentChart = null;
	}
	
	
	//------------ CHILD ------------\\

	public void selectParentProfile() {
		if (currentProfile.parentNode == null) {
			throw new RuntimeException("Current profile \"" + currentProfile.profileClass + "\" is root, so it has no parent profile.");
		}
		currentProfile = currentProfile.parentNode;
	}
	
	public void selectChildProfile(Class<? extends Profile> profile) {
		Integer childIndex = currentProfile.childNodesIndexes.get(profile);
		if (childIndex == null) {
			throw new RuntimeException("No child profile \"" + profile.getSimpleName() + "\" for profile \"" + currentProfile.profileClass + "\".");
		}
		currentProfile = currentProfile.childNodes.get(childIndex);
	}
	
	public void addChildProfile(Class<? extends Profile> profile) {
		Integer childIndex = currentProfile.childNodesIndexes.get(profile);
		if (childIndex != null) {
			throw new RuntimeException("There is already a child profile \"" + profile.getSimpleName() + "\" for profile \"" + currentProfile.profileClass + "\".");
		}
		currentProfile.childNodesIndexes.put(profile, currentProfile.childNodesIndexes.size());
		currentProfile.childNodes.add(new ProfileReportNode(profile, currentProfile));
	}
	
	
	//------------ CHART ------------\\

	public void selectChart(String chartName) {
		Integer chartIndex = currentProfile.dataChartsIndexes.get(chartName);
		if (chartIndex == null) {
			throw new RuntimeException("No chart with name \"" + chartName + "\" for profile \"" + currentProfile.profileClass + "\".");
		}
		currentChart = currentProfile.dataCharts.get(chartIndex);
	}
	
	public void addChart(String chartName, Importance importance) {
		Integer childIndex = currentProfile.dataChartsIndexes.get(chartName);
		if (childIndex != null) {
			throw new RuntimeException("A chart with name \"" + chartName + "\" was already added to profile \"" + currentProfile.profileClass + "\".");
		}
		currentProfile.dataChartsIndexes.put(chartName, currentProfile.dataChartsIndexes.size());
		currentProfile.dataCharts.add(new DataChart(chartName, importance));
	}
	
	public void addChartFromMap(String chartName, Importance importance, Map<?, ?> map, String firstColumnHeader, String secondColumnHeader, Orientation orientation) {
		addChart(chartName, importance);
		selectChart(chartName);
		
		if (orientation == Orientation.PORTRAIT) {
			setDataAt(0, 0, firstColumnHeader);
			setDataAt(1, 0, secondColumnHeader);
			
			changeChartProperties().setHorizBorder(1, "-");

			List<Entry<?, ?>> entries = new ArrayList<Entry<?, ?>>(map.entrySet());
			
			for (int i = 0; i < entries.size(); ++i) {
				Entry<?, ?> entry = entries.get(i);
				setDataAt(0, i + 1, entry.getKey());
				setDataAt(1, i + 1, entry.getValue());
			}
		} else {
			setDataAt(0, 0, firstColumnHeader);
			setDataAt(0, 1, secondColumnHeader);
			
			changeChartProperties().setVertBorder(1, ":");

			List<Entry<?, ?>> entries = new ArrayList<Entry<?, ?>>(map.entrySet());
			
			for (int i = 0; i < entries.size(); ++i) {
				Entry<?, ?> entry = entries.get(i);
				setDataAt(i + 1, 0, entry.getKey());
				setDataAt(i + 1, 1, entry.getValue());
			}	
		}
	}
	
	
	//------------ PROBLEM ------------\\

	public void addProblems(List<ProfilabilityProblem> problems) {
		String chartName = currentProfile.profileClass.getSimpleName() + " problems";
		addChart(chartName, Importance.VERY_HIGH);
		selectChart(chartName);
		
		setDataAt(0, 0, "Type");
		setDataAt(1, 0, "Description");
		setDataAt(2, 0, "Diagnostic");
		setDataAt(3, 0, "Recoverability");
		
		changeChartProperties().setChartBorders("= ", "|");
		
		for (int y = 0; y < problems.size(); ++y) {
			setDataAt(0, y + 1, problems.get(y).getName());
			setDataAt(1, y + 1, problems.get(y).formatProblem());
			setDataAt(2, y + 1, problems.get(y).getDiagnostic());
			setDataAt(3, y + 1, problems.get(y).getRecoverability());
		}
	}
	
	
	//------------ SET DATA ------------\\

	public void setDataAt(int x, int y, Object... newData) {
		List<String> data = new ArrayList<String>();
		for (Object newDataLine : newData) {
			data.add(newDataLine.toString());
		}
		currentChart.setDataAt(x, y, data);
	}
	
	public void setDataAt(int x, int y, List<String> newdata) {
		currentChart.setDataAt(x, y, newdata);
	}
	

	//------------ CHANGE PROPERTIES ------------\\

	public DataChart changeChartProperties() {
		return currentChart;
	}


	//------------ CHART ------------\\

	public void importChildProfile(ProfileReportFormatter formatter) {
		Class<? extends Profile> newClass = formatter.rootNode.profileClass;
		addChildProfile(newClass);
		currentProfile.childNodes.remove(currentProfile.childNodes.size() - 1);
		currentProfile.childNodes.add(formatter.rootNode);
	}
	
	public void importProfileCharts(ProfileReportFormatter formatter) {
		for (DataChart chart : formatter.rootNode.dataCharts) {
			addChart(chart.getName(), chart.getImportance());
			currentProfile.dataCharts.remove(currentProfile.dataCharts.size() - 1);
			currentProfile.dataCharts.add(chart);
		}
	}
	
	
	//------------ STATUS ------------\\

	public boolean isEmpty() {
		return rootNode.childNodes.isEmpty();
	}
	
	
	//------------ FORMAT ------------\\

	public void print(Importance importance) {
		for (String line : format(importance)) {
			if(Log.vital() && Log.print(line));
		}
	}
	
	public List<String> format(Importance importance) {
		List<String> lines = new ArrayList<String>();
		List<Integer> level = new ArrayList<Integer>();
		
		updateLevel(level, 1);
		
		lines.add(formatLevel(level) + getTitleLine(rootNode.profileClass.getSimpleName()));
		
		return rootNode.format(lines, level, importance);
	}
	
	protected static String getTitleLine(String profileName) {
		int spaces = TITLE_LINE_LENGTH - profileName.length();
		int spacesLeft = (int) Math.rint((((double) spaces - 1.0) / 2));
		int spacesRight = (int) Math.rint((((double) spaces + 1.0) / 2));
		
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < spacesLeft; ++i) {
			builder.append(TITLE_LINE_CHAR);
		}
		builder.append(" " + profileName + " ");
		
		for (int i = 0; i < spacesRight; ++i) {
			builder.append(TITLE_LINE_CHAR);
		}
		return builder.toString();
	}
	
	protected static void updateLevel(List<Integer> level, int number) {
		if (number == 1) {
			level.add(number);
		} else {
			level.set(level.size() - 1, number);	
		}
	}
	
	protected static String formatLevel(List<Integer> level, char letter) {
		StringBuilder builder = new StringBuilder();
		
		builder.append('[');
		for (Integer aLevel : level) {
			builder.append(aLevel.toString() + "-");
		}
		builder.append(letter);
		builder.append("] - ");
		
		return builder.toString();
	}
	
	protected static String formatLevel(List<Integer> level) {
		StringBuilder builder = new StringBuilder();

		builder.append('[');
		
		for (int i = 0; i < level.size() - 1; ++i) {
			builder.append(level.get(i).toString() + "-");
		}
		builder.append(level.get(level.size() - 1));
		builder.append("] - ");
		
		return builder.toString();
	}
	
	protected static String levelIndentation(List<Integer> level) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0 ; i < (level.size() - 1) * LEVEL_INDENTATION; ++i) {
			builder.append(' ');
		}
		return builder.toString();
	}
}
