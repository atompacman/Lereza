package atompacman.lereza.song.container;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.container.MidiNote;
import atompacman.lereza.common.solfege.RythmicSignature;
import atompacman.lereza.common.solfege.Value;
import atompacman.lereza.song.Parameters;
import atompacman.lereza.song.container.Note.NoteStatus;

public class Measure {

	private List<Set<Note>> notes;
	private RythmicSignature rythmicSignature;
	
	private int number;
	private boolean isEmpty;
			
	
	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////

	public Measure(RythmicSignature rythmicSignature, int number) {
		this.notes = new ArrayList<Set<Note>>();
		for (int i = 0; i < rythmicSignature.getMeasureTimeunitLength(); ++i) {
			notes.add(new HashSet<Note>());
		}
		this.rythmicSignature = rythmicSignature;
		this.isEmpty = true;
		this.number = number;
	}
	
	
	//////////////////////////////
	//         ADD NOTE         //
	//////////////////////////////
	
	public void addNote(MidiNote note, Value value, int timeunit) {
		addNote(note, value, timeunit, false);
	}
	
	public void addTiedNote(MidiNote note, Value value, int timeunit) {
		addNote(note, value, timeunit, true);
	}
	
	public void addNote(MidiNote note, Value value, int timeunit, boolean noteIsTied) {
		int finalTimeunit = timeunit + value.toTimeunit();
		
		Note firstNote = new Note(note.getNote(), value, noteIsTied ? NoteStatus.TIED_NOTE_START : NoteStatus.NOTE_START);

		notes.get(timeunit).add(firstNote);
		Log.extra(String.format("Adding note %4s of length %2d at timeunit %4d of measure no.%d", firstNote.toString(), firstNote.getValue().toTimeunit(), timeunit, number));		
		
		for (int i = timeunit + 1; i < finalTimeunit; ++i) {
			Note newNote = new Note(note.getNote(), value, noteIsTied ? NoteStatus.TIED_NOTE_CONTINUATION : NoteStatus.NOTE_CONTINUATION);
			notes.get(i).add(newNote);
		}
		isEmpty = false;
	}
	
	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////

	public RythmicSignature getRythmicSignature() {
		return rythmicSignature;
	}

	public List<Set<Note>> getNotes() {
		return notes;
	}

	public boolean isEmpty() {
		return isEmpty;
	}
	
	
	//////////////////////////////
	//          PRINT           //
	//////////////////////////////
	
	public List<String> toStringList(boolean completedMeasure) {
		List<String> partition = createEmptyPartition();
		int linesForRests[] = {Parameters.TOP_SECTION_HEIGHT + 2, Parameters.TOP_SECTION_HEIGHT + 6, Parameters.TOP_SECTION_HEIGHT + 
				Parameters.MIDDLE_SECTION_HEIGHT + 11, Parameters.TOP_SECTION_HEIGHT + Parameters.MIDDLE_SECTION_HEIGHT + 15};
		
		for (int i = 0; i < notes.size(); ++i) {
			Set<Note> timeunitNotes = notes.get(i);
			
			for (Note aNote : timeunitNotes) {
				int heightOnPartition = Parameters.NOTE_HEIGHT_CORRECTION - aNote.getPitch().getHeightForPartition();
				
				if (aNote.startSomething()) {
					partition.set(heightOnPartition, addNoteToLine(aNote, i, partition.get(heightOnPartition)));
				}
			}
			if (Parameters.SHOW_RESTS && timeunitNotes.isEmpty() && (areOtherNotesToPlaceFrom(i) || completedMeasure)) {
				for (int j = 0; j < linesForRests.length; ++j) {
					int height = linesForRests[j];
					String line = partition.get(height);
					partition.set(height, line.substring(0, i) + Parameters.REST_CHAR + line.substring(i + 1));
				}
			}
		}
		return partition;
	}
	
	private String addNoteToLine(Note note, int timeunit, String line) {
		int noteLength = note.toString().length();
		int middleOfNoteString = noteLength / 2;
		int beginningIndex = Parameters.BORDER_LENGTH / 2 + timeunit * Parameters.LENGTH_PER_TIMEUNIT - middleOfNoteString;
		int endingIndex = beginningIndex + noteLength;
		return line.substring(0, beginningIndex) + note.toString() + line.substring(endingIndex);
	}
	
	private boolean areOtherNotesToPlaceFrom(int timestamp) {
		for (int i = timestamp; i < notes.size(); ++i) {
			for (Note note : notes.get(i)) {
				if (note.startSomething()) {
					return true;
				}
			}
		}
		return false;
	}
	
	private List<String> createEmptyPartition() {
		List<String> partition = new ArrayList<String>();
		
		for (int i = 0; i < Parameters.TOP_SECTION_HEIGHT; ++i) {
			partition.add(createEmptyLine());
		}
		
		for (int i = 0; i < 4; ++i) {
			partition.add(createLine());
			partition.add(createEmptyLine());
		}
		partition.add(createLine());
		
		String toNumber = createEmptyLine();
		toNumber = String.format(" %-4d", number) + toNumber.substring(5);
		
		partition.add(toNumber);
		
		for (int i = 0; i < Parameters.MIDDLE_SECTION_HEIGHT - 1; ++i) {
			partition.add(createEmptyLine());
		}
		
		for (int i = 0; i < 4; ++i) {
			partition.add(createLine());
			partition.add(createEmptyLine());
		}
		partition.add(createLine());
		
		for (int i = 0; i < Parameters.BOTTOM_SECTION_HEIGHT; ++i) {
			partition.add(createEmptyLine());
		}
		
		return partition;
	}
	
	private String createEmptyLine() {
		int lineLength = rythmicSignature.getMeasureTimeunitLength() * Parameters.LENGTH_PER_TIMEUNIT  + Parameters.BORDER_LENGTH;
		StringBuilder builder = new StringBuilder();
		
		for (int j = 0; j < lineLength; ++j) {
			builder.append(' ');
		}
		return builder.toString();
	}
	
	private String createLine() {
		int lineLength = rythmicSignature.getMeasureTimeunitLength() * Parameters.LENGTH_PER_TIMEUNIT  + Parameters.BORDER_LENGTH;
		StringBuilder builder = new StringBuilder();
		
		for (int j = 0; j < lineLength; ++j) {
			builder.append('-');
		}
		return builder.toString();
	}
}
