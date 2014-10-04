package com.atompacman.lereza.api;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.atompacman.atomLog.Log;
import com.atompacman.atomLog.Log.Verbose;
import com.atompacman.lereza.common.database.Database;
import com.atompacman.lereza.core.piece.tool.PieceBuilder;
import com.atompacman.lereza.core.profile.ProfileManager;
import com.atompacman.lereza.exception.DatabaseException;
import com.atompacman.lereza.exception.LerezaWizardInitException;
import com.atompacman.lereza.midi.MIDIFilePlayer;
import com.atompacman.lereza.midi.MIDIFileReader;

public class Wizard {

	public static MIDIFileReaderAPI midiFileReader;
	public static MIDIFilePlayerAPI	midiFilePlayer;
	public static PieceBuilderAPI	pieceBuilder;
	public static ProfileManagerAPI profileManager;

	private static Date wizardInitializationTime;


	//------------ INITIALIZATION ------------\\

	public static void init() throws LerezaWizardInitException {
		init(Verbose.INFOS);
	}

	public static void init(Verbose verbose) throws LerezaWizardInitException {
		Log.setVerbose(verbose);
		if (Log.infos() && Log.title("Lereza Wizard", 0));
		saveInitializationTime();
		initEssentialModuleAPIs();
		
		try {
			Database.initialize();
		} catch (DatabaseException e) {
			throw new LerezaWizardInitException("Failed to initialize Lereza Wizard: ", e);
		}
	}
	
	private static void saveInitializationTime() {
		wizardInitializationTime = new Date();
		SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat timestamp  = new SimpleDateFormat("HH:mm:ss");
		if (Log.infos() && Log.print("Lereza Wizard initialized on " 
				+ date.format(wizardInitializationTime) + " at " 
				+ timestamp.format(wizardInitializationTime) + "."));
	}

	private static void initEssentialModuleAPIs() {
		midiFileReader 	= new MIDIFileReader();
		pieceBuilder	= new PieceBuilder();
		profileManager 	= new ProfileManager();
	}
	
	
	//------------ INITIALIZE PLAYER ------------\\

	public static void initMIDIFilePlayer() {
		midiFilePlayer 	= MIDIFilePlayer.getPlayer();
	}
}