package com.atompacman.lereza.api;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.atomLog.Log;
import com.atompacman.atomLog.Log.Verbose;
import com.atompacman.lereza.Parameters;
import com.atompacman.lereza.api.Wizard;
import com.atompacman.lereza.common.xml.NodeContent;
import com.atompacman.lereza.common.xml.NodeStructure;
import com.atompacman.lereza.common.xml.SimpleXMLParser;
import com.atompacman.lereza.exception.DatabaseException;
import com.atompacman.lereza.exception.LerezaWizardInitException;
import com.atompacman.lereza.exception.MIDIFileIndexParserException;
import com.atompacman.lereza.exception.MIDIFileReaderException;
import com.atompacman.lereza.exception.XMLParserException;

public class SystemTester {

	private static final String   ROOT_NODE_NAME 		  = "TestRoutines";
	private static final String   SYSTEM_TESTS_NODE_NAME  = "SystemTests";
	private static final String   ROUTINE_NODE_NAME 	  = "Routine";
	private static final String[] ROUTINE_NODE_ATT 		  = {"id", "name", "files"};
	private static final String   TEST_CASE_ID_SEPARATOR  = ",";


	//------------ LAUNCH SYSTEM TESTS ------------\\

	public static void main(String args[]) throws MIDIFileIndexParserException, XMLParserException, 
	LerezaWizardInitException, MIDIFileReaderException, DatabaseException {
		Wizard.init(Verbose.EXTRA);

		if(Log.infos() && Log.title("Lereza System Tester", 0));

		List<TestRoutine> testRoutines = readTestRoutines();

		for (TestRoutine testRoutine : testRoutines) {
			if(Log.infos() && Log.title(testRoutine.getName(), 1));
			for (Integer testCaseID : testRoutine.getTestCasesID()) {
				if(Log.infos() && Log.title("Test case ID: " + testCaseID, 3));
				Wizard.midiFileReader.read(testCaseID);
				Wizard.pieceBuilder.build(testCaseID);
//				Wizard.profileManager.profile(title, artist, compositionSet);
//				Wizard.profileManager.printReport(title, artist, Importance.VERY_LOW);
			}
		}
	}

	
	//------------ READ TEST ROUTINES ------------\\

	private static List<TestRoutine> readTestRoutines() throws XMLParserException, 
	MIDIFileIndexParserException, DatabaseException {
		List<TestRoutine> testRoutines = new ArrayList<TestRoutine>();

		NodeStructure level1 = new NodeStructure(ROOT_NODE_NAME);
		NodeStructure level2 = new NodeStructure(SYSTEM_TESTS_NODE_NAME);
		NodeStructure level3 = new NodeStructure(ROUTINE_NODE_NAME, ROUTINE_NODE_ATT);
		level1.addChildNode(level2);
		level2.addChildNode(level3);

		NodeContent contentRoot = SimpleXMLParser.parse(Parameters.TEST_ROUTINES_XML_PATH, level1);

		for (NodeContent routineNode : contentRoot.getChildNodes().get(0).getChildNodes()) {
			int routineID = Integer.parseInt(routineNode.getAttributeValue(ROUTINE_NODE_ATT[0]));
			if (routineID != testRoutines.size() + 1) {
				throw new XMLParserException("Test routine IDs are not in a linear order. "
						+ "Last good id was \"" + testRoutines.size() + "\".");
			}
			String routineName = routineNode.getAttributeValue(ROUTINE_NODE_ATT[1]);
			TestRoutine routine = new TestRoutine(routineName);

			String allTestCasesIDs = routineNode.getAttributeValue(ROUTINE_NODE_ATT[2]);
			String[] testCasesIDs = allTestCasesIDs.split(TEST_CASE_ID_SEPARATOR);

			for (String testCaseID : testCasesIDs) {
				try {
					routine.addTestCase(Integer.parseInt(testCaseID));
				} catch (NumberFormatException e) {
					throw new XMLParserException("Formatting error in attribute \"files\" of node "
							+ "\"Routine\". ID separator is \"" + TEST_CASE_ID_SEPARATOR + "\".");
				}
			}
			testRoutines.add(routine);
		}
		
		return testRoutines;
	}
}