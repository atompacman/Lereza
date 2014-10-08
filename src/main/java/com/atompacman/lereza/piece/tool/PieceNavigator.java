package com.atompacman.lereza.piece.tool;

import java.util.Set;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.exception.PieceNavigatorException;
import com.atompacman.lereza.piece.container.OldBar;
import com.atompacman.lereza.piece.container.Note;
import com.atompacman.lereza.piece.container.Part;
import com.atompacman.lereza.piece.container.Piece;

public class PieceNavigator {

	private Piece piece;

	private int part;
	private int bar;
	private int timeunitInBar;

	private int noteInBar;
	private int noteInPart;

	
	//------------ CONSTRUCTORS ------------\\s

	public PieceNavigator(Piece piece) {
		if (piece.getNbParts() == 0) {
			throw new PieceNavigatorException("Cannot navigate in a piece with no parts.");
		}
		this.piece = piece;
		this.part = 0;
		this.bar = 0;
		this.timeunitInBar = 0;
		this.noteInBar = 0;
		this.noteInPart = 0;

		goToBeginningOfPiece();
	}


	//------------ PART ------------\\

	public void goToNextPart() {
		if (part == piece.getNbParts() - 1) {
			throw new PieceNavigatorException("Cannot go to next part: "
					+ "current part is the last one.");
		}
		++part;
		bar = 0;
		timeunitInBar = 0;
		noteInBar = 0;
		noteInPart = 0;
	}

	public void goToPreviousPart() {
		if (part == 0) {
			throw new PieceNavigatorException("Cannot go to previous part: "
					+ "current part is the first one.");
		}
		--part;
		bar = 0;
		timeunitInBar = 0;
		noteInPart = 0;
		noteInBar = 0;
	}

	public void goToPart(int partNo) {
		if (partNo >= piece.getNbParts() || partNo < 0) {
			throw new PieceNavigatorException("Cannot go to part \"" + partNo + "\": "
					+ "the current piece only has " + piece.getNbParts() + " parts.");
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
		bar = 0;
		timeunitInBar = 0;
		noteInPart = 0;
		noteInBar = 0;

		while (getCurrentPart().isEmpty()) {
			try {
				goToNextPart();
			} catch (PieceNavigatorException e) {
				throw new PieceNavigatorException("Cannot go to first unempty part: "
						+ "there are no unempty parts.", e);
			}
		}
	}

	public final Part getCurrentPart() {
		return piece.getPartNo(part);
	}

	public boolean endOfPart() {
		return bar == getCurrentPart().getNbBars();
	}


	//------------ MEASURE ------------\\

	public void goToNextBar() {
		if (endOfPart()) {
			throw new PieceNavigatorException("Cannot go to next bar: "
					+ "current bar is the last one.");
		}
		noteInPart -= noteInBar;
		noteInPart += countNotesInBar();
		++bar;
		timeunitInBar = 0;
		noteInBar = 0;
	}

	public void goToPreviousBar() {
		if (bar == 0) {
			throw new PieceNavigatorException("Cannot go to previous bar: "
					+ "current bar is the first one.");
		}
		noteInPart -= noteInBar;
		--bar;
		noteInPart -= countNotesInBar();
		timeunitInBar = 0;
		noteInBar = 0;
	}

	public void goToBar(int barNo) {
		if (barNo >= getCurrentPart().getNbBars() || barNo < 0) {
			throw new PieceNavigatorException("Cannot go to bar \"" + barNo + "\": the "
					+ "current part only has " + getCurrentPart().getNbBars() + " bars.");
		}
		while (barNo > bar) {
			goToNextBar();
		}
		while (barNo < bar) {
			goToPreviousBar();
		}
	}

	public void goToFirstUnemptyBar() {
		bar = 0;
		timeunitInBar = 0;
		noteInBar = 0;
		noteInPart = 0;

		while (getCurrentBar().isEmpty()) {
			try {
				goToNextBar();
			} catch (PieceNavigatorException e) {
				throw new PieceNavigatorException("Cannot go to first unempty bar of part " 
						+ part + ": there are no unempty bars.", e);
			}
		}
	}

	public final OldBar getCurrentBar() {
		return getCurrentPart().getBarNo(bar);
	}

	public int getBarInPart() {
		return bar;
	}


	//------------ TIMEUNIT ------------\\

	public void goToNextTimeunit() {
		++timeunitInBar;
		if (timeunitInBar < piece.getRythmicSignature().getBarTimeunitLength()) {
			if (currentNotesStartsSomething()) {
				++noteInBar;
				++noteInPart;
			}
		} else {
			goToNextBar();
		}
	}

	public void goToPreviousTimeunit() {
		if (timeunitInBar != 0) {
			if (currentNotesStartsSomething()) {
				--noteInBar;
				--noteInPart;
			}
			--timeunitInBar;
		} else {
			goToPreviousBar();
		}
	}

	public void goToTimeunitNo(int timeunitNo) {
		if (timeunitNo >= getCurrentNoteStack().size() || timeunitNo < 0) {
			throw new PieceNavigatorException("Cannot go to timeunit \"" + timeunitNo + "\": the "
					+ "current bar only has " + getCurrentNoteStack().size() + " timeunits.");
		}
		while (timeunitNo > timeunitInBar) {
			goToNextTimeunit();
		}
		while (timeunitNo < timeunitInBar) {
			goToPreviousTimeunit();
		}
	}

	public int getCurrentTimestamp() {
		return bar * piece.getRythmicSignature().getBarTimeunitLength() + timeunitInBar;
	}

	public final Set<Note> getCurrentTimeunitNoteStack() {
		return piece.getPartNo(part).getBarNo(bar).getNotes().get(timeunitInBar);
	}


	//------------ NOTE ------------\\

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
		if (noteNo >= countNotesInBar() || noteNo < 0) {
			throw new PieceNavigatorException("Cannot go to note \"" + noteNo + "\": "
					+ "the current bar only has " + countNotesInBar() + " notes.");
		}
		while (noteNo > noteInBar) {
			goToNextNote();
		}
		while (noteNo < noteInBar) {
			goToPreviousNote();
		}
	}

	public void goToFirstNoteOfBar() {
		while (noteInBar < 1) {
			goToNextTimeunit();
		}
		while (noteInBar >= 1 && !currentNotesStartsSomething()) {
			goToPreviousTimeunit();
		}
	}

	public void goToBeginningOfPiece() {
		goToFirstUnemptyPart();
		goToFirstUnemptyBar();
		goToFirstNoteOfBar();
	}

	public boolean currentNotesStartsSomething() {
		return countNotesInNoteStack(getCurrentNoteStack()) > 0;
	}

	public int getNoteNoInPart() {
		return noteInPart;
	}

	public final Set<Note> getCurrentNoteStack() {
		return getCurrentBar().getNotes().get(timeunitInBar);
	}

	public final Note getFirstNoteOfStack() {
		return getCurrentBar().getNotes().get(timeunitInBar).iterator().next();
	}


	//------------ PMT ------------\\

	public PMT getPMT() {
		return new PMT(part, bar, timeunitInBar);
	}


	//------------ COUNT ------------\\

	public int countNotesInBar() {
		int notesInBar = 0;

		OldBar bar = getCurrentBar();

		if (bar.isEmpty()) {
			return 0;
		}

		for (Set<Note> noteSet : bar.getNotes()) {
			if (countNotesInNoteStack(noteSet) != 0) {
				++notesInBar;
			}
		}
		return notesInBar;
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


	//------------ OTHER ------------\\

	public void printLocation() {
		if (Log.infos() && Log.print(getFullLocation()));
	}

	public String getFullLocation() {
		return String.format("PieceNavigator - " + getPMT().toString() + 
				" - Part %1d - Bar %3d - Timestamp %5d - Note in part %3d - "
				+ "Note in bar %2d - Timeunit in bar %2d", 
				part, bar, getCurrentTimestamp(), noteInPart, noteInBar, timeunitInBar);
	}
}
