package com.atompacman.lereza.profile;

import java.lang.reflect.InvocationTargetException;

import com.atompacman.lereza.api.ProfileManager;
import com.atompacman.lereza.exception.DatabaseException;
import com.atompacman.lereza.exception.ProfileManagerException;
import com.atompacman.lereza.piece.container.Piece;
import com.atompacman.lereza.resources.context.Context;
import com.atompacman.lereza.resources.context.ContextElementType;
import com.atompacman.lereza.resources.database.Database;

public class ProfileManagerImpl implements ProfileManager {

	//------------ PROFILE ------------\\

	public void profile(int caseID) throws DatabaseException, ProfileManagerException {
		Piece piece = DatabaseImpl.getPiece(caseID);
		ProfiledPiece profiledPiece = profile(piece);
		DatabaseImpl.setProfiledPiece(caseID, profiledPiece);
	}

	protected ProfiledPiece profile(Piece piece) throws ProfileManagerException {
		Profiler profiler = getAppropriateProfiler(piece);
		Profile profile = profiler.profile(piece);
		return new ProfiledPiece(piece, profile);
	}
	
	@SuppressWarnings("unchecked")
	protected Profiler getAppropriateProfiler(Piece piece) throws ProfileManagerException {
		Profiler profiler = null;

		try {
			Context context = piece.getMIDIFile().getFileInfos().getContext();
			String formName = context.valueOf(ContextElementType.FORM);
			String profilerName = formName + "Profiler";
			String packageRoot = this.getClass().getPackage().getName();
			String lowerCaseName = formName.substring(0, 1).toLowerCase() + formName.substring(1);
			String formClassRoot = packageRoot + "." + lowerCaseName;
			String profilerClassName = formClassRoot + "." + profilerName;
			Class<?> uncastedClass = Class.forName(profilerClassName);
			Class<? extends Profiler> profilerClass = (Class<? extends Profiler>) uncastedClass;
			profiler = profilerClass.getConstructor().newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | 
				IllegalArgumentException | InvocationTargetException | 
				NoSuchMethodException | SecurityException e) {
			throw new ProfileManagerException("Error getting profile and profiler class for piece "
					+ "\"" + piece.getMIDIFile().getFileInfos().getTitle() + "\":", e);
		}
		
		return profiler;
	}
}
