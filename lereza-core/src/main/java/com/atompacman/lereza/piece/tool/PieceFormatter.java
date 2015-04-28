package com.atompacman.lereza.piece.tool;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.piece.container.Bar;
import com.atompacman.lereza.piece.container.Note;

public class PieceFormatter {
	
	private static final int TIMEUNIT_WIDTH 		= 2;
	private static final int BORDER_LENGTH			= 4;
	private static final int TOP_SECTION_HEIGHT 	= 6;
	private static final int BOTTOM_SECTION_HEIGHT  = 5;
	private static final int NOTE_HEIGHT_CORRECTION = -30;
	
	private static List<StringBuilder> currBar;
	private static int lineLength;
	private static int currBarNo;
	
	
	//------------ BAR ------------\\
	
	public static List<String> generateTextPartition(Bar bar) {
		currBar = new ArrayList<StringBuilder>();
		int timeunitInBar = bar.getRythmicSignature().timeunitsInABar();
		lineLength = (timeunitInBar + 2 * BORDER_LENGTH) * TIMEUNIT_WIDTH;
		currBarNo = bar.getNo();
		
		createEmptyPartition();
		fillWithNotes(bar);
		
		List<String> partition = new ArrayList<String>();
		for (StringBuilder lineBuilder : currBar) {
			partition.add(lineBuilder.toString());
		}
		return partition;
	}

	private static void createEmptyPartition() {
		addEmptyLines(TOP_SECTION_HEIGHT);
		addPartitionLines();
		addMiddleSection();
		addPartitionLines();
		addEmptyLines(BOTTOM_SECTION_HEIGHT);
	}

	private static void addMiddleSection() {
		StringBuilder numberLine = createLine(' ');
		numberLine = numberLine.replace(5, 6, String.format(" %-4d", currBarNo));
		currBar.add(numberLine);
		addEmptyLines(2);
	}
	
	private static void addPartitionLines() {
		for (int i = 0; i < 4; ++i) {
			currBar.add(createLine('-'));
			currBar.add(createLine(' '));
		}
		currBar.add(createLine('-'));
	}

	private static void addEmptyLines(int nb) {
		for (int i = 0; i < nb; ++i) {
			currBar.add(createLine(' '));
		}
	}

	private static StringBuilder createLine(char character) {
		StringBuilder builder = new StringBuilder();
		for (int j = 0; j < lineLength; ++j) {
			builder.append(character);
		}
		return builder;
	}

	private static void fillWithNotes(Bar bar) {
		int timeunitsLength = bar.getRythmicSignature().timeunitsInABar();
		
		for (int i = 0; i < timeunitsLength; ++i) {
			for (Note note : bar.getNotesStartingAt(i)) {
				int noteHeight = - (note.getPitch().diatonicToneValue() + NOTE_HEIGHT_CORRECTION);
				
				if (noteHeight > 0 && noteHeight < currBar.size()) {
					String noteRepres = note.toString();
					int x0 = i * TIMEUNIT_WIDTH + BORDER_LENGTH;
					if (note.isTied()) {
						x0 -= 1;
					}
					int xf = x0 + noteRepres.length();
					currBar.get(noteHeight).replace(x0, xf, noteRepres);
				} else {
					currBar.get(0).replace(0, 3, "!!!");
				}
			}
		}
	}
}
