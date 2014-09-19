package com.atompacman.lereza.api;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.core.composition.LibraryAPI;
import com.atompacman.lereza.core.composition.tool.Library;
import com.atompacman.lereza.core.piece.PieceBuilderAPI;
import com.atompacman.lereza.core.piece.tool.PieceBuilder;
import com.atompacman.lereza.core.profile.ProfileManager;
import com.atompacman.lereza.core.profile.ProfileManagerAPI;

import atompacman.leraza.midi.api.MidiFilePlayerAPI;
import atompacman.leraza.midi.api.MidiFileReaderAPI;
import atompacman.leraza.midi.io.MidiFilePlayer;
import atompacman.leraza.midi.io.MidiFileReader;

public class Wizard {

	public static MidiFileReaderAPI midiFileReader;
	public static MidiFilePlayerAPI	midiFilePlayer;
	public static PieceBuilderAPI	pieceBuilder;
	public static LibraryAPI		library;
	public static ProfileManagerAPI profileManager;

	private static Date wizardInitializationTime;


	//////////////////////////////
	//     INITIALIZATION       //
	//////////////////////////////

	public static void initialize() {
		if (Log.infos() && Log.title("Lereza Wizard", 0));
		getInitializationTime();
		initializeModuleAPIs();
	}

	private static void getInitializationTime() {
		wizardInitializationTime = new Date();
		SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat timestamp  = new SimpleDateFormat("HH:mm:ss");
		if (Log.infos() && Log.print("Lereza Wizard initialized on " 
				+ date.format(wizardInitializationTime) + " at " 
				+ timestamp.format(wizardInitializationTime) + "."));
	}

	private static void initializeModuleAPIs() {
		midiFileReader = new MidiFileReader();
		midiFilePlayer = MidiFilePlayer.getPlayer();
		pieceBuilder = new PieceBuilder();
		library = new Library();
		profileManager = new ProfileManager();
	}
}