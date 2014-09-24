package com.atompacman.lereza.core.composition.container;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.common.solfege.Composer;
import com.atompacman.lereza.common.solfege.Context;
import com.atompacman.lereza.core.piece.container.Piece;
import com.atompacman.lereza.core.profile.Profile;

import atompacman.leraza.midi.container.MidiFile;

public class Composition {

	private String title;
	private CompositionSet set;
	private Context context;

	private Piece piece;
	private MidiFile file;
	private Profile profile;


	//------------ CONSTRUCTORS ------------\\

	public Composition(String title, CompositionSet set, 
			Context context, Piece piece, MidiFile file) {
		this.title = title;
		this.set = set;
		this.context = context;
		this.piece = piece;
		this.file = file;
		if (Log.infos() && Log.print("Composition \"" + title + 
				"\" created from the midi file \"" + file.getFilePath() + "\"."));
	}


	//------------ SETTERS ------------\\

	public void setProfile(Profile profile) {
		this.profile = profile;
	}


	//------------GETTERS ------------\\

	public String getTitle() {
		return title;
	}

	public Composer getComposer() {
		return set.getComposer();
	}

	public CompositionSet getCompositionSet() {
		return set;
	}

	public Context getContext() {
		return context;
	}

	public Piece getPiece() {
		return piece;
	}

	public MidiFile getFile() {
		return file;
	}

	public Profile getMainProfile() {
		return profile;
	}


	//------------ DESCRIPTION ------------\\

	public List<String> getDescription() {
		List<String> lines = new ArrayList<String>();

		int squareLength = file.getFilePath().length() + 1;
		StringBuilder dottedLine = new StringBuilder();
		for (int i = 0; i < 16 + squareLength; ++i) {
			dottedLine.append(':');
		}
		lines.add(dottedLine.toString());
		lines.add(String.format("::%9s | %-" + squareLength + "s::", "TITLE", title));
		lines.add(String.format("::%9s | %-" + squareLength + "s::", "COMPOSER", getComposer().getName()));
		lines.add(String.format("::%9s | %-" + squareLength + "s::", "SET", set.getName()));
		lines.add(String.format("::%9s | %-" + squareLength + "s::", "CONTEXT", context.toString()));
		lines.add(String.format("::%9s | %-" + squareLength + "s::", "FILEPATH", file.getFilePath()));
		lines.add(dottedLine.toString());

		return lines;
	}

	public void printDescription() {
		for (String line : getDescription()) {
			if (Log.extra() && Log.print(line));
		}
	}
}