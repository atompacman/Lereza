package com.atompacman.lereza.piece.tool;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.piece.container.Bar;

public class PieceFormatter {
	
	private static final int TIMEUNIT_WIDTH 		= 1;
	private static final int BORDER_LENGTH			= 4;
	private static final int TOP_SECTION_HEIGHT 	= 6;
	private static final int BOTTOM_SECTION_HEIGHT  = 5;
	
	private static List<String> currBar;
	private static int lineLength;
	private static int currBarNo;
	
	
	//------------ BAR ------------\\
	
	public static List<String> generateTextPartition(Bar bar) {
		currBar = new ArrayList<String>();
		int timeunitInBar = bar.getRythmicSignature().timeunitsInABar();
		lineLength = (timeunitInBar + 2 * BORDER_LENGTH) * TIMEUNIT_WIDTH;
		currBarNo = bar.getNo();
		
		createEmptyPartition();
		fillWithNotes(bar);
		
		return currBar;
	}

	private static void createEmptyPartition() {
		addEmptyLines(TOP_SECTION_HEIGHT);
		addPartitionLines();
		addMiddleSection();
		addPartitionLines();
		addEmptyLines(BOTTOM_SECTION_HEIGHT);
	}

	private static void addMiddleSection() {
		String toNumber = createLine(' ');
		toNumber = String.format(" %-4d", currBarNo) + toNumber.substring(5);
		currBar.add(toNumber);
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

	private static String createLine(char character) {
		StringBuilder builder = new StringBuilder();
		for (int j = 0; j < lineLength; ++j) {
			builder.append(character);
		}
		return builder.toString();
	}

	private static void fillWithNotes(Bar bar) {
		
	}
}
