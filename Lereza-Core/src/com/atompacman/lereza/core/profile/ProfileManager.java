package com.atompacman.lereza.core.profile;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.atompacman.lereza.core.composition.container.Composition;
import com.atompacman.lereza.core.composition.tool.Library;
import com.atompacman.lereza.core.exception.ProfilerException;
import com.atompacman.lereza.core.piece.container.Piece;
import com.atompacman.lereza.core.profile.tool.DataChart.Importance;

public class ProfileManager implements ProfileManagerAPI {

	//------------ PROFILE ------------\\

	public void profile(String compositionName, String composerName) {
		profile(Library.getComposition(compositionName, composerName));
	}
	
	public void profile(String compositionName, String composerName, String compositionSetName) {
		profile(Library.getComposition(compositionName, composerName, compositionSetName));
	}
	
	private void profile(Composition composition) {
		Profiler profiler = getAppropriateProfiler(composition);
		profiler.profile();
		composition.setProfile(profiler.getProfile());
	}
	
	@SuppressWarnings("unchecked")
	private Profiler getAppropriateProfiler(Composition composition) {
		Profiler profiler = null;
		try {
			String formName = composition.getContext().getForm().toString();
			String lowerCaseFormName = formName.toLowerCase();
			String capitalizedFormName = formName.charAt(0) + formName.substring(1).toLowerCase();
			String profileName = capitalizedFormName + "Profile";
			String profilerName = capitalizedFormName + "Profiler";
			String packageRoot = this.getClass().getPackage().getName();
			String formClassRoot = packageRoot + "." + lowerCaseFormName;
			String profileClassName = formClassRoot + "." + profileName;
			String profilerClassName = formClassRoot + "." + profilerName;
			Class<? extends Profile> profileClass = 
					(Class<? extends Profile>) Class.forName(profileClassName);
			Class<? extends Profiler> profilerClass = 
					(Class<? extends Profiler>) Class.forName(profilerClassName);
			int nbParts = composition.getPiece().getNbParts();
			Profile profile = profileClass.getConstructor(int.class).newInstance(nbParts);
			Constructor<? extends Profiler> constructor = 
					profilerClass.getConstructor(Piece.class, Profile.class);
			profiler = constructor.newInstance(composition.getPiece(), profile);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | 
				IllegalArgumentException | InvocationTargetException | 
				NoSuchMethodException | SecurityException e) {
			throw new ProfilerException(e);
		}
		return profiler;
	}
	
	
	//------------ PRINT REPORT ------------\\

	public void printReport(String compositionName, String composerName, Importance importance) {
		Composition composition = Library.getComposition(compositionName, composerName);
		composition.getMainProfile().getReportFormatter().print(importance);
	}
	
	public void printReport(String compositionName, String composerName, 
			String compositionSetName, Importance importance) {
		Composition composition = 
				Library.getComposition(compositionName, composerName, compositionSetName);
		composition.getMainProfile().getReportFormatter().print(importance);
	}
}
