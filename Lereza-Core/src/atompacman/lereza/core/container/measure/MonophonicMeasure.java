package atompacman.lereza.core.container.measure;

import java.util.ArrayList;
import java.util.List;

import atompacman.leraza.midi.container.MIDINote;
import atompacman.lereza.core.container.notation.Notation;
import atompacman.lereza.core.container.notation.Note;
import atompacman.lereza.core.container.notation.Rest;
import atompacman.lereza.core.solfege.Articulation;
import atompacman.lereza.core.solfege.Pitch;
import atompacman.lereza.core.solfege.RythmicSignature;
import atompacman.lereza.core.solfege.Value;

public class MonophonicMeasure extends Measure {

	private List<Notation> notes;


	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////

	public MonophonicMeasure(RythmicSignature rythmicSignature) {
		super(rythmicSignature);
		this.notes = new ArrayList<Notation>();
	}


	//////////////////////////////
	//      ADDING NOTATION     //
	//////////////////////////////

	public void addNote(MIDINote note, Pitch previousPitch) {
		int length = note.getLength() / rythmicSignature.getValueOfShortestNote();		
		List<Value> values = Value.splitIntoValues(length);

		Note playedNote = new Note(note.getNote(), values.get(0), previousPitch, note.isStaccato() ? Articulation.STACCATO : Articulation.NORMAL);

		if (previousPitch == null) {
			playedNote.setToFirstNoteOfVoice();
		}

		notes.add(playedNote);

		for (int i = 1; i < Math.pow(2, values.get(0).ordinal()); ++i) {
			notes.add(new Note(note.getNote(), values.get(0), null, note.isStaccato() ? Articulation.STACCATO : Articulation.NORMAL));
		}

		for (int j = 1; j < values.size(); ++j) {
			for (int i = 0; i < Math.pow(2, values.get(j).ordinal()); ++i) {
				notes.add(new Note(note.getNote(), values.get(j), null, note.isStaccato() ? Articulation.STACCATO : Articulation.NORMAL));
			}
		}
	}

	public void addSuspendedNote(MIDINote note) {
		int length = note.getLength() / rythmicSignature.getValueOfShortestNote();
		List<Value> values = Value.splitIntoValues(length);

		for (Value value : values) {
			for (int i = 0; i < Math.pow(2, value.ordinal()); ++i) {
				notes.add(new Note(note.getNote(), value, null));
			}
		}
	}

	public void addRest(int length) {
		int restLength = length / rythmicSignature.getValueOfShortestNote();
		List<Value> values = Value.splitIntoValues(restLength);

		for (Value value : values) {
			Rest firstNoteOfRest = new Rest(value);
			firstNoteOfRest.setToBeginningOfRest();
			notes.add(firstNoteOfRest);
			for (int i = 1; i < Math.pow(2, value.ordinal()); ++i) {
				notes.add(new Rest(value));
			}
		}
	}


	//////////////////////////////
	//      STATUS CHECKER      //
	//////////////////////////////

	public boolean noteFitsIntoMeasure(int noteLength) {
		return (noteLength / rythmicSignature.getValueOfShortestNote() + notes.size()) <= maxNbNotes();
	}

	public int maxNbNotes() {
		return rythmicSignature.getMeter().getNumerator() * rythmicSignature.getMidiBeatNoteValue() / rythmicSignature.getValueOfShortestNote();
	}

	public int getRemainingSpace() {
		return maxNbNotes() - notes.size();
	}

	public boolean isFull() {
		return notes.size() == maxNbNotes();
	}

	public boolean isEmpty() {
		return notes.isEmpty();
	}


	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////

	public Note getLastNote() {
		for (int i = notes.size() - 1; i != 0; --i){
			if (notes.get(i) instanceof Note) {
				return (Note) notes.get(i);
			}
		}
		return null;
	}

	public List<Notation> getNotes(){
		return notes;
	}


	//////////////////////////////
	//        TO STRING         //
	//////////////////////////////

	public String toString() {
		StringBuilder partition = createEmptyPartition();
		
		int lineLength = (int)(1.5 * maxNbNotes()) + 7;
		int nb32thNotes = notes.size() / 2;
		boolean trillIsOn = false;
		
		for (int i = 0; i < nb32thNotes; ++i) {
			Notation notation = notes.get(2 * i);
			
			if (notation.isRealNote()) {
				int horiPos = i * 3 + 3;
				if (notation instanceof Note) {
					int height = 30 - ((Note) notation).getPitch().heightOnPartition();
					int pos = height * lineLength + horiPos;
					if (notation.getValue().equals(Value.SIXTYFORTH)) {
						if (trillIsOn) {
							partition.replace(pos, pos + 3, String.format("%3s",  "~~~"));
						} else {
							partition.replace(pos, pos + 3, String.format("%3s", notation.toString()));
							trillIsOn = true;
						}
					} else {
						partition.replace(pos, pos + 3, String.format("%3s", notation.toString()));
						trillIsOn = false;
					}
				} else {
					int pos = 10 * lineLength + horiPos;
					partition.replace(pos, pos + 3, String.format("%3s", notation.toString()));

					pos = 22 * lineLength + horiPos;
					partition.replace(pos, pos + 3, String.format("%3s", notation.toString()));
				}
			}
		}
		return "\n" + partition.toString();
	}
	
	private StringBuilder createEmptyPartition() {
		final String LINE = "---";
		int lineLength = maxNbNotes() / 2 + 2;
		StringBuilder partition = new StringBuilder();

		for (int line = 0; line < 6; ++line) {
			for (int i = 0; i < lineLength; ++i) {
				partition.append("   ");
			}
			partition.append('\n');
		}
		for (int line = 0; line < 5; ++line) {
			for (int i = 0; i < lineLength; ++i) {
				partition.append(LINE);
			}
			partition.append('\n');
			for (int i = 0; i < lineLength; ++i) {
				partition.append("   ");
			}
			partition.append('\n');
		}
		for (int line = 0; line < 2; ++line) {
			for (int i = 0; i < lineLength; ++i) {
				partition.append("   ");
			}
			partition.append('\n');
		}
		for (int line = 0; line < 5; ++line) {
			for (int i = 0; i < lineLength; ++i) {
				partition.append(LINE);
			}
			partition.append('\n');
			for (int i = 0; i < lineLength; ++i) {
				partition.append("   ");
			}
			partition.append('\n');
		}
		for (int line = 0; line < 3; ++line) {
			for (int i = 0; i < lineLength; ++i) {
				partition.append("   ");
			}
			partition.append('\n');
		}
		
		return partition;
	}
	
	public String fusionToPartition(String s2) {
		int lineLength = (int) (1.5 * maxNbNotes()) + 7;
		String s1 = this.toString();
		StringBuilder output = new StringBuilder();
				
		for (int i = 0; i < 30; ++i) {
			output.append(s1.substring(lineLength * i + 1, lineLength * (i + 1)));
			if ((i > 5 && i < 15) || (i > 17 && i < 27)) {
				output.append("|");
			}
			output.append(s2.substring(lineLength * i + 1, lineLength * (i + 1)) + "\n");
		}
		return "\n" + output.toString();
	}
}
