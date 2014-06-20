package atompacman.lereza.core.container.part;

import java.util.ArrayList;
import java.util.List;

import atompacman.leraza.midi.container.MIDINote;
import atompacman.lereza.core.container.measure.MonophonicMeasure;
import atompacman.lereza.core.solfege.Pitch;
import atompacman.lereza.core.solfege.RythmicSignature;

public class Voice extends Part {

	private List<MonophonicMeasure> measures;

	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////
	
	public Voice(RythmicSignature rythmicSignature) {
		super(rythmicSignature);
		this.measures = new ArrayList<MonophonicMeasure>();
	}

	
	//////////////////////////////
	//       ADD NOTATION       //
	//////////////////////////////
	
	public void addNote(MIDINote note) {
		boolean suspendedNote = false;
		Pitch previousPitch = null;
		
		if (measures.isEmpty()) {
			previousPitch = null;
			measures.add(new MonophonicMeasure(rythmicSignature));
		} else if (lastMeasure().isEmpty()) {
			previousPitch = measures.get(measures.size() - 2).getLastNote().getPitch();
		} else if (lastMeasure().isFull()) {
			previousPitch = lastMeasure().getLastNote().getPitch();
			measures.add(new MonophonicMeasure(rythmicSignature));
		} else {
			if (lastMeasure().getLastNote() != null) {
				previousPitch = lastMeasure().getLastNote().getPitch();
			} else {
				previousPitch = null;
			}
		}
		while (!lastMeasure().noteFitsIntoMeasure(note.getLength())) {
			int remainingSpace = lastMeasure().getRemainingSpace() * lastMeasure().getRythmicSignature().getValueOfShortestNote();
			int originalLength = note.getLength();
			note.setLength(remainingSpace);
			if (suspendedNote) {
				lastMeasure().addSuspendedNote(note);
			} else {
				lastMeasure().addNote(note, previousPitch);
			}
			suspendedNote = true;
			measures.add(new MonophonicMeasure(rythmicSignature));
			note.setLength(originalLength - remainingSpace);
		}
		if (suspendedNote) {
			lastMeasure().addSuspendedNote(note);
		} else {
			lastMeasure().addNote(note, previousPitch);
		}
	}
	
	public void addRest(int midiLength) {
		if (measures.isEmpty() || lastMeasure().isFull()) {
			measures.add(new MonophonicMeasure(rythmicSignature));
		}
		while (!lastMeasure().noteFitsIntoMeasure(midiLength)) {
			int remainingSpace = lastMeasure().getRemainingSpace() * lastMeasure().getRythmicSignature().getValueOfShortestNote();
			lastMeasure().addRest(remainingSpace);
			measures.add(new MonophonicMeasure(rythmicSignature));
			midiLength -= remainingSpace;
		}
		lastMeasure().addRest(midiLength);
	}

	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////
	
	public MonophonicMeasure lastMeasure() {
		if (measures.size() != 0) {
			return measures.get(measures.size() - 1);
		} else {
			return new MonophonicMeasure(rythmicSignature);
		}
	}
	
	public MonophonicMeasure beforeLastMeasure() {
		if (measures.size() > 1) {
			return measures.get(measures.size() - 2);
		} else {
			return new MonophonicMeasure(rythmicSignature);
		}
	}

	public MonophonicMeasure getMeasure(int measureIndex){
		return measures.get(measureIndex);
	}
}
