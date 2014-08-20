package com.atompacman.lereza.core.piece.tool;

import java.util.Set;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.core.exception.PieceNavigatorException;
import com.atompacman.lereza.core.piece.container.Measure;
import com.atompacman.lereza.core.piece.container.Note;
import com.atompacman.lereza.core.piece.container.Part;
import com.atompacman.lereza.core.piece.container.Piece;

public class PieceNavigator {

	private Piece piece;

	private int part;
	private int measure;
	private int timeunitInMeasure;

	private int noteInMeasure;
	private int noteInPart;


	public PieceNavigator(Piece piece) {
		if (piece.getNbParts() == 0) {
			throw new PieceNavigatorException("Cannot navigate in a piece with no parts.");
		}
		this.piece = piece;
		this.part = 0;
		this.measure = 0;
		this.timeunitInMeasure = 0;
		this.noteInMeasure = 0;
		this.noteInPart = 0;

		goToBeginningOfPiece();
	}


	//////////////////////////////
	//           PART           //
	//////////////////////////////

	public void goToNextPart() {
		if (part == piece.getNbParts() - 1) {
			throw new PieceNavigatorException("Cannot go to next part: current part is the last one.");
		}
		++part;
		measure = 0;
		timeunitInMeasure = 0;
		noteInMeasure = 0;
		noteInPart = 0;
	}

	public void goToPreviousPart() {
		if (part == 0) {
			throw new PieceNavigatorException("Cannot go to previous part: current part is the first one.");
		}
		--part;
		measure = 0;
		timeunitInMeasure = 0;
		noteInPart = 0;
		noteInMeasure = 0;
	}

	public void goToPart(int partNo) {
		if (partNo >= piece.getNbParts() || partNo < 0) {
			throw new PieceNavigatorException("Cannot go to part \"" + partNo + "\": the current piece only has " + piece.getNbParts() + " parts.");
		}
		while (partNo > part) {
			goToNextPart();
		}
		while (partNo < part) {
			goToPreviousPart();
		}
	}

	public void goToFirstUnemptyPart() {
		part = 0;
		measure = 0;
		timeunitInMeasure = 0;
		noteInPart = 0;
		noteInMeasure = 0;

		while (getCurrentPart().isEmpty()) {
			try {
				goToNextPart();
			} catch (PieceNavigatorException e) {
				throw new PieceNavigatorException("Cannot go to first unempty part: there are no unempty parts.", e);
			}
		}
	}

	public final Part getCurrentPart() {
		return piece.getPartNo(part);
	}
	
	public boolean endOfPart() {
		return measure == getCurrentPart().getNbMeasures();
	}
	

	//////////////////////////////
	//         MEASURE          //
	//////////////////////////////

	public void goToNextMeasure() {
		if (endOfPart()) {
			throw new PieceNavigatorException("Cannot go to next measure: current measure is the last one.");
		}
		noteInPart -= noteInMeasure;
		noteInPart += countNotesInMeasure();
		++measure;
		timeunitInMeasure = 0;
		noteInMeasure = 0;
	}

	public void goToPreviousMeasure() {
		if (measure == 0) {
			throw new PieceNavigatorException("Cannot go to previous measure: current measure is the first one.");
		}
		noteInPart -= noteInMeasure;
		--measure;
		noteInPart -= countNotesInMeasure();
		timeunitInMeasure = 0;
		noteInMeasure = 0;
	}

	public void goToMeasure(int measureNo) {
		if (measureNo >= getCurrentPart().getNbMeasures() || measureNo < 0) {
			throw new PieceNavigatorException("Cannot go to measure \"" + measureNo + "\": the current part only has " + getCurrentPart().getNbMeasures() + " measures.");
		}
		while (measureNo > measure) {
			goToNextMeasure();
		}
		while (measureNo < measure) {
			goToPreviousMeasure();
		}
	}

	public void goToFirstUnemptyMeasure() {
		measure = 0;
		timeunitInMeasure = 0;
		noteInMeasure = 0;
		noteInPart = 0;

		while (getCurrentMeasure().isEmpty()) {
			try {
				goToNextMeasure();
			} catch (PieceNavigatorException e) {
				throw new PieceNavigatorException("Cannot go to first unempty measure of part " + part + ": there are no unempty measures.", e);
			}
		}
	}

	public final Measure getCurrentMeasure() {
		return getCurrentPart().getMeasureNo(measure);
	}

	public int getMeasureInPart() {
		return measure;
	}


	//////////////////////////////
	//         TIMEUNIT         //
	//////////////////////////////

	public void goToNextTimeunit() {
		++timeunitInMeasure;
		if (timeunitInMeasure < piece.getRythmicSignature().getMeasureTimeunitLength()) {
			if (currentNotesStartsSomething()) {
				++noteInMeasure;
				++noteInPart;
			}
		} else {
			goToNextMeasure();
		}
	}

	public void goToPreviousTimeunit() {
		if (timeunitInMeasure != 0) {
			if (currentNotesStartsSomething()) {
				--noteInMeasure;
				--noteInPart;
			}
			--timeunitInMeasure;
		} else {
			goToPreviousMeasure();
		}
	}

	public void goToTimeunitNo(int timeunitNo) {
		if (timeunitNo >= getCurrentNoteStack().size() || timeunitNo < 0) {
			throw new PieceNavigatorException("Cannot go to timeunit \"" + timeunitNo + "\": the current measure only has " + getCurrentNoteStack().size() + " timeunits.");
		}
		while (timeunitNo > timeunitInMeasure) {
			goToNextTimeunit();
		}
		while (timeunitNo < timeunitInMeasure) {
			goToPreviousTimeunit();
		}
	}

	public int getCurrentTimestamp() {
		return measure * piece.getRythmicSignature().getMeasureTimeunitLength() + timeunitInMeasure;
	}

	public final Set<Note> getCurrentTimeunitNoteStack() {
		return piece.getPartNo(part).getMeasureNo(measure).getNotes().get(timeunitInMeasure);
	}


	//////////////////////////////
	//           NOTE           //
	//////////////////////////////

	public void goToNextNote() {
		goToNextTimeunit();
		while (!endOfPart() && !currentNotesStartsSomething()) {
			goToNextTimeunit();
		}
	}

	public void goToPreviousNote() {
		goToPreviousTimeunit();
		while (!currentNotesStartsSomething()) {
			goToPreviousTimeunit();
		}
	}

	public void goToNoteNo(int noteNo) {
		if (noteNo >= countNotesInMeasure() || noteNo < 0) {
			throw new PieceNavigatorException("Cannot go to note \"" + noteNo + "\": the current measure only has " + countNotesInMeasure() + " notes.");
		}
		while (noteNo > noteInMeasure) {
			goToNextNote();
		}
		while (noteNo < noteInMeasure) {
			goToPreviousNote();
		}
	}

	public void goToFirstNoteOfMeasure() {
		while (noteInMeasure < 1) {
			goToNextTimeunit();
		}
		while (noteInMeasure >= 1 && !currentNotesStartsSomething()) {
			goToPreviousTimeunit();
		}
	}

	public void goToBeginningOfPiece() {
		goToFirstUnemptyPart();
		goToFirstUnemptyMeasure();
		goToFirstNoteOfMeasure();
	}

	public boolean currentNotesStartsSomething() {
		return countNotesInNoteStack(getCurrentNoteStack()) > 0;
	}

	public int getNoteNoInPart() {
		return noteInPart;
	}

	public final Set<Note> getCurrentNoteStack() {
		return getCurrentMeasure().getNotes().get(timeunitInMeasure);
	}

	public final Note getFirstNoteOfStack() {
		return getCurrentMeasure().getNotes().get(timeunitInMeasure).iterator().next();
	}


	//////////////////////////////
	//          OTHER           //
	//////////////////////////////
	
	public PMT getPMT() {
		return new PMT(part, measure, timeunitInMeasure);
	}
	
	
	//////////////////////////////
	//          COUNT           //
	//////////////////////////////

	public int countNotesInMeasure() {
		int notesInMeasure = 0;

		Measure measure = getCurrentMeasure();

		if (measure.isEmpty()) {
			return 0;
		}

		for (Set<Note> noteSet : measure.getNotes()) {
			if (countNotesInNoteStack(noteSet) != 0) {
				++notesInMeasure;
			}
		}
		return notesInMeasure;
	}

	public int countNotesInNoteStack(Set<Note> noteSet) {
		int notesInNoteSet = 0;

		for (Note note : noteSet) {
			if (note.startSomething()) {
				++notesInNoteSet;
			}
		}
		return notesInNoteSet;
	}

	//////////////////////////////
	//          OTHER           //
	//////////////////////////////

	public void printLocation() {
		if (Log.infos() && Log.print(getFullLocation()));
	}

	public String getFullLocation() {
		return String.format("PieceNavigator - " + getPMT().toString() + " - Part %1d - Measure %3d - Timestamp %5d - Note in part %3d - Note in measure %2d - Timeunit in measure %2d", 
				part, measure, getCurrentTimestamp(), noteInPart, noteInMeasure, timeunitInMeasure);
	}
}
