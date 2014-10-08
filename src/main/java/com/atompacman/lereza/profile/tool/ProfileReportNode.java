package com.atompacman.lereza.profile.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atompacman.lereza.profile.tool.DataChart.Importance;
import com.atompacman.lereza.profile.Profile;

public class ProfileReportNode {
	
	protected Class<? extends Profile> profileClass;
	
	protected ProfileReportNode parentNode;
	protected List<ProfileReportNode> childNodes;
	protected List<DataChart> dataCharts;
	
	protected Map<Class<? extends Profile>, Integer> childNodesIndexes;
	protected Map<String, Integer> dataChartsIndexes;
	
	
	//------------ CONSTRUCTORS ------------\\

	public ProfileReportNode(Class<? extends Profile> profileClass, ProfileReportNode parentNode) {
		this.profileClass = profileClass;
		this.parentNode = parentNode;
		this.childNodes = new ArrayList<ProfileReportNode>();
		this.dataCharts = new ArrayList<DataChart>();
		this.childNodesIndexes = new HashMap<Class<? extends Profile>, Integer>();
		this.dataChartsIndexes = new HashMap<String, Integer>();
	}
	
	
	//------------ FORMAT ------------\\

	public List<String> format(List<String> lines, List<Integer> level, Importance importance) {
		char chartLetter = 'A';
		int levelNb = 1;
		
		for (DataChart chart : dataCharts) {
			if (chart.getImportance().ordinal() < importance.ordinal()) {
				continue;
			}
			lines.add("");
			lines.add(ProfileReportFormatter.levelIndentation(level) + ProfileReportFormatter.formatLevel(level, chartLetter++) + chart.getName());
			lines.addAll(chart.generateChart(level));
		}
		for (ProfileReportNode childNode : childNodes) {
			lines.add("");
			ProfileReportFormatter.updateLevel(level, levelNb++);
			lines.add(ProfileReportFormatter.levelIndentation(level) + 
					ProfileReportFormatter.formatLevel(level) + 
					ProfileReportFormatter.getTitleLine(childNode.profileClass.getSimpleName()));
			childNode.format(lines, level, importance);
		}
		
		return lines;
	}
}
