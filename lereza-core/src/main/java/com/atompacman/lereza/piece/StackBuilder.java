package com.atompacman.lereza.piece;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.atompacman.lereza.solfege.Dynamic;
import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.Value;

public class StackBuilder extends PieceComponentBuilder<Stack<Note>> {

	//===================================== INNER TYPES ==========================================\\

	private class NoteEntry {
		
		//===================================== FIELDS ===========================================\\

		public Pitch pitch;
		public Value value;
		public byte velocity;
		public boolean isTied;
		
		
		
		//===================================== METHODS ==========================================\\

		//-------------------------------- PUBLIC CONSTRUCTOR ------------------------------------\\
		
		public NoteEntry(Pitch pitch, Value value, byte velocity, boolean isTied) {
			this.pitch = pitch;
			this.value = value;
			this.velocity = velocity;
			this.isTied = isTied;
		}
	}
	
	
	
	//======================================= FIELDS =============================================\\

	private final List<NoteEntry> startingNotes;
	private final List<NoteEntry> startedNotes;
	
	private Value currValue;
	private byte currVelocity;
	
	

	//======================================= METHODS ============================================\\

	//------------------------------- PUBLIC STATIC CONSTRUCTOR ----------------------------------\\

	public static StackBuilder create() {
		return new StackBuilder(new PieceBuilderSupervisor());
	}
	
	
	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\
	
	StackBuilder(PieceBuilderSupervisor supervisor) {
		super(supervisor);
		this.startingNotes = new LinkedList<>();
		this.startedNotes = new LinkedList<>();
	}


	//----------------------------------------- ADD ----------------------------------------------\\

	public StackBuilder add(Pitch pitch) {
		startingNotes.add(new NoteEntry(pitch, currValue, currVelocity, false));
		return this;
	}

	public StackBuilder add(Pitch pitch, Value value, byte velocity) {
		return value(value).velocity(velocity).add(pitch);
	}
	
	public StackBuilder value(Value value) {
		this.currValue = value;
		return this;
	}
	
	public StackBuilder velocity(byte velocity) {
		this.currVelocity = velocity;
		return this;
	}
	
	public void addStarted(Pitch pitch, Value value, byte velocity, boolean isTied) {
		startedNotes.add(new NoteEntry(pitch, value, velocity, isTied));
	}

	
	//---------------------------------------- BUILD ---------------------------------------------\\

	public Stack<Note> buildComponent() {
		Map<Pitch, NoteEntry> startingNotes = removeOverlappStartingNotes();
		Map<Pitch, NoteEntry> startedNotes = removeOverlappStartedNotes(startingNotes);
		Dynamic dynamic = computeNoteStackDynamic(startingNotes, startedNotes);
		return new Stack<Note>(toNoteMap(startingNotes), toNoteMap(startedNotes), dynamic);
	}
	
	private Map<Pitch, NoteEntry> removeOverlappStartingNotes() {
		Map<Pitch, NoteEntry> startingNotesMap = new HashMap<>();
		for (NoteEntry startingEntry : startingNotes) {
			putEntryInMap(startingEntry, startingNotesMap, "starting");
		}
		return startingNotesMap;
	}

	private Map<Pitch, NoteEntry> removeOverlappStartedNotes(Map<Pitch, NoteEntry> startingNote) {
		Map<Pitch, NoteEntry> startedNotesMap = new HashMap<>();
		for (NoteEntry startedEntry : startedNotes) {
			putEntryInMap(startedEntry, startedNotesMap, "started");
			if (startingNote.containsKey(startedEntry)) {
				// TODO add an anomaly
			}
		}
		return startedNotesMap;
	}
	
	private static void putEntryInMap(NoteEntry entry, Map<Pitch, NoteEntry> map, String mapName) {
		if (map.put(entry.pitch,  entry) != null) {
			// TODO Add an anomaly
//			throw new IllegalArgumentException("A note of pitch " + note.getPitch().toString() + 
//					" was already added to the " + mapName + " note stack.");
		}
	}

	/** TODO can have different algos. Here we do the avrg on starting notes */
	private static Dynamic computeNoteStackDynamic(Map<Pitch, NoteEntry> startingNotes, 
												   Map<Pitch, NoteEntry> startedNotes) {
		
		int velocitySum = 0;
		
		for (NoteEntry entry : startingNotes.values()) {
			velocitySum += entry.velocity;
		}
		
		return Dynamic.valueOf((int)Math.rint((double)velocitySum / (double)startingNotes.size()));
	}
	
	private static Map<Pitch, Note> toNoteMap(Map<Pitch, NoteEntry> entries) {
		Map<Pitch, Note> notes = new HashMap<>();
		for (NoteEntry entry : entries.values()) {
			notes.put(entry.pitch, Note.valueOf(entry.pitch, entry.value, entry.isTied));
		}
		return notes;
	}
	
	
	//---------------------------------------- RESET ---------------------------------------------\\

	public void reset() {
		startingNotes.clear();
		startedNotes.clear();
	}
}
