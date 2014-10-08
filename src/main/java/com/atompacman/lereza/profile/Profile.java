package com.atompacman.lereza.profile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atompacman.lereza.profile.tool.ProfileReportFormatter;

public abstract class Profile {

	protected Map<Class<? extends Profile>, List<Profile>> subProfilesLists;

	protected String timeOfCreation;
	protected ProfileReportFormatter formatter;


	//------------ CONSTRUCTORS ------------\\

	public Profile() {
		recordTimeOfCreation();
		formatter = new ProfileReportFormatter(this.getClass());
	}
	
	public Profile(Class<? extends Profile> subProfileClass) {
		recordTimeOfCreation();
		subProfilesLists = new HashMap<Class<? extends Profile>, List<Profile>>();
		subProfilesLists.put(subProfileClass, new ArrayList<Profile>());
		formatter = new ProfileReportFormatter(this.getClass());
	}
	
	public Profile(List<Class<? extends Profile>> subProfileClasses) {
		recordTimeOfCreation();
		subProfilesLists = new HashMap<Class<? extends Profile>, List<Profile>>();

		for (Class<? extends Profile> subProfileClass : subProfileClasses) {
			subProfilesLists.put(subProfileClass, new ArrayList<Profile>());
		}

		formatter = new ProfileReportFormatter(this.getClass());
	}

	private void recordTimeOfCreation() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		timeOfCreation = dateFormat.format(date);
	}


	//------------ ADD SUB-PROFILE ------------\\

	public void addSubProfile(Profile subProfile) {
		Class<? extends Profile> profileClass = subProfile.getClass();

		if (!subProfilesLists.containsKey(profileClass)) {
			throw new IllegalArgumentException(profileClass.getSimpleName() + " is not a valid "
					+ "sub-profile of " + getClass().getSimpleName() + ".");
		}

		subProfilesLists.get(profileClass).add(subProfile);
	}


	//------------ GET SUB-PROFILE ------------\\

	public Profile getSubProfile(Class<? extends Profile> profileClass) {
		List<Profile> subProfiles = getSubProfiles(profileClass);

		if (subProfiles.size() != 1) {
			throw new IllegalArgumentException(getClass().getSimpleName() + " has more than one "
					+ "sub-profile of type \"" + profileClass.getSimpleName() + "\".");
		}

		return subProfiles.get(0);
	}

	public Profile getSubProfile(Class<? extends Profile> profileClass, int index) {
		List<Profile> subProfiles = getSubProfiles(profileClass);

		if (index >= subProfiles.size()) {
			throw new IllegalArgumentException(getClass().getSimpleName() 
					+ " has less than " + (index - 1) + "sub-profile of type \"" 
					+ profileClass.getSimpleName() + "\" (it has " + subProfiles.size() + ").");
		}

		return subProfiles.get(index);
	}

	public List<Profile> getSubProfiles(Class<? extends Profile> profileClass) {
		if (!subProfilesLists.containsKey(profileClass)) {
			throw new IllegalArgumentException(profileClass.getSimpleName() + " is not a valid "
					+ "sub-profile type for " + getClass().getSimpleName() + ".");
		}

		List<Profile> subProfiles = subProfilesLists.get(profileClass);

		if (subProfiles.isEmpty()) {
			throw new IllegalArgumentException("No " + profileClass.getSimpleName() + " sub-profile"
					+ " was set to " + getClass().getSimpleName() + ".");
		}

		return subProfiles;
	}


	//------------ GETTERS ------------\\

	public String getTimeOfCreation() {
		return timeOfCreation;
	}


	//------------ FORMAT ------------\\

	public abstract ProfileReportFormatter getReportFormatter();

	protected void importChildReportFormatters() {
		for (List<Profile> childProfiles : subProfilesLists.values()) {
			for (Profile childProfile : childProfiles) {
				formatter.importChildProfile(childProfile.getReportFormatter());
			}
		}
	}
}
