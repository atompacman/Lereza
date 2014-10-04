package com.atompacman.lereza.core.profile;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.atompacman.lereza.api.ProfileManagerAPI;
import com.atompacman.lereza.common.context.Context;
import com.atompacman.lereza.common.context.ContextElementType;
import com.atompacman.lereza.common.database.Database;
import com.atompacman.lereza.core.piece.container.Piece;
import com.atompacman.lereza.exception.DatabaseException;
import com.atompacman.lereza.exception.ProfilerException;

public class ProfileManager implements ProfileManagerAPI {

	//------------ PROFILE ------------\\
	
	public void profile(int caseID) throws DatabaseException {
		ProfiledPiece profiledPiece = profile(Database.getPiece(caseID));
		Database.setProfiledPiece(caseID, profiledPiece);
	}
	
	protected ProfiledPiece profile(Piece piece) {
		piece.getMIDIFile().getFileInfos().getContext();
		Profiler profiler = getAppropriateProfiler(piece);
		profiler.profile();
		return new ProfiledPiece(piece, profiler.getProfile());
	}
	
	@SuppressWarnings("unchecked")
	private Profiler getAppropriateProfiler(Piece piece) {
		Profiler profiler = null;
		try {
			Context context = piece.getMIDIFile().getFileInfos().getContext();
			String formName = context.valueOf(ContextElementType.FORM);
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
			int nbParts = piece.getNbParts();
			Profile profile = profileClass.getConstructor(int.class).newInstance(nbParts);
			Constructor<? extends Profiler> constructor = 
					profilerClass.getConstructor(Piece.class, Profile.class);
			profiler = constructor.newInstance(piece, profile);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | 
				IllegalArgumentException | InvocationTargetException | 
				NoSuchMethodException | SecurityException e) {
			throw new ProfilerException(e);
		}
		return profiler;
	}
}
