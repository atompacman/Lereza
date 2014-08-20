package com.atompacman.lereza.core.composition.tool;

import java.util.HashMap;
import java.util.Map;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.common.solfege.Composer;
import com.atompacman.lereza.common.solfege.Context;
import com.atompacman.lereza.common.solfege.Context.Form;
import com.atompacman.lereza.core.composition.LibraryAPI;
import com.atompacman.lereza.core.composition.container.Composition;
import com.atompacman.lereza.core.composition.container.CompositionSet;
import com.atompacman.lereza.core.piece.tool.PieceBuilder;

import atompacman.leraza.midi.io.MidiFileReader;

public class Library implements LibraryAPI {

	private static Map<Composer, Map<String, CompositionSet>> byComposer;
	private static Map<Context, Map<String, Composition>> 	   byContext;
	
	static {
		byComposer = new HashMap<Composer, Map<String, CompositionSet>>();
		byContext = new HashMap<Context, Map<String, Composition>>();
	}

	
	//////////////////////////////
	//      ADD COMPOSITION     //
	//////////////////////////////

	public void addComposition(String filePath, String compositionName, String composerName, Form musicalForm, String compositionSetName) {
		if (Log.infos() && Log.title("Library", 0));
		
		Composer composer = Composer.get(composerName);
		
		if (byComposer.get(composer) != null && byComposer.get(composer).get(compositionName) != null) {
			if (Log.error() && Log.print("Cannot add the composition \"" + compositionName + "\" in the library as it already contains one with that name.")){}
			return;
		}
		
		if (byComposer.get(composer) == null) {
			byComposer.put(composer, new HashMap<String, CompositionSet>());
		}
		if (byComposer.get(composer).get(compositionSetName) == null) {
			byComposer.get(composer).put(compositionSetName, new CompositionSet(compositionSetName, composer));
		}
		CompositionSet set = byComposer.get(composer).get(compositionSetName);
		Context context = new Context(musicalForm);
		Composition composition = new Composition(compositionName, set, context, PieceBuilder.getPiece(filePath), MidiFileReader.getMidiFile(filePath));
		set.add(composition);
		
		if (byContext.get(context) == null) {
			byContext.put(context, new HashMap<String, Composition>());
		}
		if (byContext.get(context).get(compositionName) != null) {
			if (Log.error() && Log.print("Replacing the older composition named \"" + compositionName + "\" for context \"" + context.toString() + "\"."));
		}
		byContext.get(context).put(compositionName, composition);
		
		if (Log.extra() && Log.print("Added composition:"));
		composition.printDescription();
	}
	
	
	//////////////////////////////
	//      GET COMPOSITION     //
	//////////////////////////////
	
	public static Composition getComposition(String compositionName, String composerName, String compositionSetName) {
		Composer composer = Composer.get(composerName);
		if (byComposer.get(composer) == null) {
			throw new IllegalArgumentException("No such composer as \"" + composerName + "\" in the library.");
		}
		if (byComposer.get(composer).get(compositionSetName) == null) {
			throw new IllegalArgumentException("No such song as \"" + compositionName + "\" from composer \"" + composerName + "\" in the library.");
		}
		return byComposer.get(composer).get(compositionSetName).get(compositionName);
	}
	
	public static Composition getComposition(String compositionName, String composerName) {
		Composer composer = Composer.get(composerName);
		if (byComposer.get(composer) == null) {
			throw new IllegalArgumentException("No such composer as \"" + composerName + "\" in the library.");
		}
		for (CompositionSet set : byComposer.get(composer).values()) {
			Composition composition = set.get(compositionName);
			if (composition != null) {
				return composition;
			}
		}
		throw new IllegalArgumentException("No such song as \"" + compositionName + "\" from composer \"" + composerName + "\" in the library.");
	}
}
