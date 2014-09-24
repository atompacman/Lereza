package com.atompacman.lereza.core.profile;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.atompacman.lereza.core.profile.tool.ProfileReportFormatter;

public abstract class Profile {
	
	protected String timeOfCreation;
	protected ProfileReportFormatter formatter;
	
	
	//------------ CONSTRUCTORS ------------\\

	public Profile() {
		recordTimeOfCreation();
		this.formatter = new ProfileReportFormatter(this.getClass());
	}

	private void recordTimeOfCreation() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		timeOfCreation = dateFormat.format(date);
	}

	
	//------------ GETTERS ------------\\

	public String getTimeOfCreation() {
		return timeOfCreation;
	}

	
	//------------ FORMAT ------------\\

	public abstract ProfileReportFormatter getReportFormatter();
}
