package com.atompacman.lereza.piece.tool;

import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.piece.PieceTestsHelper;

public class TestPieceFormatter {
	
	private static boolean PRINT_TO_CONSOLE = true;
	
	
	//**********************************************************************************************
	//                                 GENERATE TEXT PARTITION FROM BAR
	//**********************************************************************************************
	/**
	 * <h1> Check empty bar partition formatting </h1>
	 * <i> Qualitative test </i> <p>
	 * 
	 * Qualitative test that only prints the output of the method in order of the programmer to 
	 * validate its behavior. <p>
	 * 
	 * Target: {@link PieceFormatter#generateTextPartition(Bar)
	 */
	@Test
	public void emptyPartition() {
		print(PieceFormatter.generateTextPartition(PieceTestsHelper.emptyBar()));
	}
	
	/**
	 * A helper method that prints a list of string to console.
	 * @param barPartition
	 */
	private static void print(List<String> barPartition) {
		if (PRINT_TO_CONSOLE) {
			for (String line : barPartition) {
				System.out.println(line);
			}
		}
	}
}
