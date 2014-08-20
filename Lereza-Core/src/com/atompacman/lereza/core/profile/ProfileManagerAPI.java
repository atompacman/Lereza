package com.atompacman.lereza.core.profile;

import com.atompacman.lereza.core.profile.tool.DataChart.Importance;

public interface ProfileManagerAPI {

	void profile(String compositionName, String composerName);
	void profile(String compositionName, String composerName, String compositionSetName);
	
	void printReport(String compositionName, String composerName, Importance importance);
	void printReport(String compositionName, String composerName, String compositionSetName, Importance importance);
}
