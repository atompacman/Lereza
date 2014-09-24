package com.atompacman.lereza.api.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.atompacman.atomLog.Log;
import com.atompacman.atomLog.Log.Verbose;
import com.atompacman.lereza.api.Wizard;
import com.atompacman.lereza.common.solfege.Context.Form;
import com.atompacman.lereza.core.profile.tool.DataChart.Importance;

public class SystemTester {

	public static final String TEST_CASE_PATH_CORRECTION = "../Data/";
	public static final String TEST_CASES_FILEPATH = TEST_CASE_PATH_CORRECTION + "TestCases.xml";
	public static final String TEST_ROUTINES_FILEPATH = TEST_CASE_PATH_CORRECTION + "TestRoutines.xml";
	public static final String TEST_CASE_ID_SEPARATOR = ",";

	public static List<TestCase> testCases;
	public static List<TestRoutine> testRoutines;


	//------------ MAIN ------------\\

	public static void main(String args[]) {
		Wizard.init(Verbose.EXTRA);

		if(Log.infos() && Log.title("Lereza System Tester", 0));
		
		readTestCases();
		readTestRoutines();

		for (TestRoutine testRoutine : testRoutines) {
			if(Log.infos() && Log.title(testRoutine.getName(), 1));
			for (TestCase testCase : testRoutine.getTestCases()) {
				if(Log.infos() && Log.title(testCase.getFilePath(), 3));
				
				String filePath = testCase.getFilePath();
				String title = testCase.getTitle();
				String artist = testCase.getArtist();
				String compositionSet = testCase.getCompositionSet();
				Form form = testCase.getContext().getForm();

				Wizard.midiFileReader.read(filePath);
				Wizard.pieceBuilder.build(filePath);
				Wizard.library.addComposition(filePath, title, artist, form, compositionSet);
				Wizard.profileManager.profile(title, artist, compositionSet);
				Wizard.profileManager.printReport(title, artist, Importance.VERY_LOW);
			}
		}
	}

	
	//------------ READ ------------\\

	private static void readTestCases() {
		testCases = new ArrayList<TestCase>();

		XMLNodeContent level1 = new XMLNodeContent(
				"TestCases");
		XMLNodeContent level2 = new XMLNodeContent(
				"File", "id", "url", "title", "artist", "set", "genre", "subgenre", "form");
		level1.addChildNode(level2);

		new SimpleXMLParser().parse(TEST_CASES_FILEPATH, level1);

		List<XMLNodeContent> testFileDesc = level1.getChildNodes().get("File");
		testFileDesc.remove(0);
		for (XMLNodeContent testFile : testFileDesc) {
			Map<String, String> testAtt = testFile.getAttributes();
			if (Integer.parseInt(testAtt.get("id")) != testCases.size() + 1) {
				throw new XMLParserException("Problem with test file ids. "
						+ "Last good id was \"" + testCases.size() + "\".");
			}
			testCases.add(new TestCase(
					TEST_CASE_PATH_CORRECTION + testAtt.get("url"),
					testAtt.get("title"),
					testAtt.get("artist"),
					testAtt.get("set"),
					testAtt.get("genre"),
					testAtt.get("subgenre"),
					testAtt.get("form")));
		}
	}

	private static void readTestRoutines() {
		testRoutines = new ArrayList<TestRoutine>();

		XMLNodeContent level1 = new XMLNodeContent("TestRoutines");
		XMLNodeContent level2 = new XMLNodeContent("SystemTests");
		XMLNodeContent level3 = new XMLNodeContent("Routine", "id", "name", "files");
		level1.addChildNode(level2);
		level2.addChildNode(level3);

		new SimpleXMLParser().parse(TEST_ROUTINES_FILEPATH, level1);

		List<XMLNodeContent> testFileDesc = level2.getChildNodes().get("Routine");
		testFileDesc.remove(0);
		for (XMLNodeContent testFile : testFileDesc) {
			Map<String, String> testAtt = testFile.getAttributes();
			if (Integer.parseInt(testAtt.get("id")) != testRoutines.size() + 1) {
				throw new XMLParserException("Problem with test routine IDs. "
						+ "Last good id was \"" + testRoutines.size() + "\".");
			}
			TestRoutine routine = new TestRoutine(testAtt.get("name"));			
			String[] testCasesIDs = testAtt.get("files").split(TEST_CASE_ID_SEPARATOR);
			for (String testCaseID : testCasesIDs) {
				try {
					routine.addTestCase(testCases.get(Integer.parseInt(testCaseID) - 1));
				} catch (NumberFormatException e) {
					throw new XMLParserException("Formatting error in attribute \"files\" of node \"Routine\"."
							+ " ID separator is \"" + TEST_CASE_ID_SEPARATOR + "\".");
				}
			}
			testRoutines.add(routine);
		}
	}
}