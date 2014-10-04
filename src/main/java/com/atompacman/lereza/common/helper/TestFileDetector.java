package com.atompacman.lereza.common.helper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.atompacman.lereza.Parameters;
import com.atompacman.lereza.exception.TestFileDetectorException;

/**
 * <h1> Tool to find test file paths </h1>
 * <i> Used by test classes to automatically find the appropriate paths of files that are needed in 
 * a test method. </i><p>
 * 
 * The path to the directory containing the requested test file(s) is obtained as follow: <p>
 * (1) Get the canonical class name containing the method that is calling this class. <br>
 * (2) Remove {@value#TO_REMOVE_FROM_CANO} from the beginning. <br>
 * (3) Replace all "." by the correct file separator. <br>
 * (4) Insert the test directory at the beginning, as defined in {@link Parameters}. <p>
 * 
 * We look inside the resolved directory for either: <p>
 * 
 * (a) The file whose name (without extension) is equal to the name of the calling method. <br>
 * (see: {@link #detectSingleFileForCurrentTest}) <br>
 * (b) Files with names beginning by the name of the calling method directly followed by 
 * "{@value#SECTION_DELIM}"(which is used to distinguish between the multiple files than can be 
 * needed for a single test) and some tag. In this case, the path to every detected file is added to 
 * a map that links the substring after the "{@value#SECTION_DELIM}" in the file name to its 
 * complete file path. <br>
 * (see: {@link #detectAllFilesForCurrentTest})
 */

public class TestFileDetector {

	private static final String TO_REMOVE_FROM_CANO = "com.atompacman.lereza";
	private static final char SECTION_DELIM = '-';
	private static final int CLIENT_CODE_STACK_INDEX;

	static {
		int i = 0;
		for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
			++i;
			if (ste.getClassName().equals(TestFileDetector.class.getName())) {
				break;
			}
		}
		CLIENT_CODE_STACK_INDEX = i;
	}

	
	public static String detectSingleFileForCurrentTest() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StackTraceElement currSTE = stackTrace[CLIENT_CODE_STACK_INDEX];

		Map<String, String> testFilePath = detectFilesForCurrMethod(currSTE);

		if (testFilePath.size() > 1) {
			throw new TestFileDetectorException("More than one file with name beginning by "
					+ currSTE.getMethodName()	+ "\" was found.");
		}
		return testFilePath.values().iterator().next();
	}

	public static Map<String, String> detectAllFilesForCurrentTest() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StackTraceElement currSTE = stackTrace[CLIENT_CODE_STACK_INDEX];

		return detectFilesForCurrMethod(currSTE);
	}

	private static Map<String, String> detectFilesForCurrMethod(StackTraceElement ste) {
		String methodName = ste.getMethodName();
		File dir = resolveDirectory(ste);
		String pathPrefix = dir.getPath() + File.separatorChar;

		Map<String, String> testFilePaths = new HashMap<String, String>();

		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				continue;
			}
			String fileName = file.getName();
			int extensionIndex = fileName.indexOf('.');
			if (extensionIndex != -1) {
				fileName = fileName.substring(0, extensionIndex);
			}
			if (!fileName.startsWith(methodName)) {
				continue;
			}
			if (fileName.length() == methodName.length()) {
				if (testFilePaths.containsKey("")) {
					throw new TestFileDetectorException("Multiple test files "
							+ "with name \"" + fileName + "\" in \"" + dir	+ "\".");
				}
				testFilePaths.put("", pathPrefix + file.getName());
				continue;
			}
			if (fileName.charAt(methodName.length()) != SECTION_DELIM) {
				continue;
			}
			int delimIndex = fileName.indexOf(SECTION_DELIM);

			if (delimIndex != fileName.lastIndexOf(SECTION_DELIM)) {
				throw new TestFileDetectorException("Test file name must not have "
						+ "multiple \"" + SECTION_DELIM	+ "\" characters in its name.");
			}
			String tagSection = fileName.substring(delimIndex);

			if (testFilePaths.containsKey(tagSection)) {
				throw new TestFileDetectorException("Multiple test files "
						+ "with name \"" + fileName + "\" in \"" + dir	+ "\".");
			}
			testFilePaths.put(tagSection, pathPrefix + file.getName());
		}

		if (testFilePaths.size() == 0) {
			throw new TestFileDetectorException("No test files with name beginning by "
					+ "\"" + methodName + "\" in directory \"" + dir.getPath() + "\".");
		}

		return testFilePaths;
	}

	private static File resolveDirectory(StackTraceElement ste) {
		String packagePath = ste.getClassName();
		if (packagePath.indexOf(TO_REMOVE_FROM_CANO) != 0) {
			throw new TestFileDetectorException("Calling method is not from the \"" + 
					TO_REMOVE_FROM_CANO + "\" package and therefore is not supported.");
		}
		packagePath = packagePath.substring(TO_REMOVE_FROM_CANO.length() + 1);
		packagePath = packagePath.replace('.', File.separatorChar);

		String directory = Parameters.TEST_DIRECTORY + packagePath;

		File currDir = new File(directory);

		if (!currDir.exists()) {
			throw new TestFileDetectorException("Test file directory "
					+ "\"" + currDir + "\" does not exist.");
		}
		return currDir;
	}
}
