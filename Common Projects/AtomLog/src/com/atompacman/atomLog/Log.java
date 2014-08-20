package com.atompacman.atomLog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

	public enum Verbose { OFF, ERROR, VITAL, WARNG, INFOS, EXTRA }

	private static final int CLASSNAME_LENGTH = 32;
	private static final char CLASSNAME_FILLER = '=';

	private static final int  TITLE_LINE_LENGTH = 97;
	private static final char TITLE_LINE_CHAR = '=';

	private static Verbose fixedVerbose = Verbose.INFOS;
	private static Verbose calledVerbose = Verbose.INFOS;


	//////////////////////////////
	//          PRINT           //
	//////////////////////////////

	public static boolean print(String message) {
		System.out.println(timestamp() + " " + className() + " {" + calledVerbose.name() + "} : " + message);
		return true;
	}

	public static boolean title(String title, int spacesBetweenDots) {
		System.out.println(timestamp() + " " + className() + " {" + calledVerbose.name() + "} : " + generateTitle(title, spacesBetweenDots));
		return true;
	}

	public static boolean title(int spacesBetweenDots) {
		System.out.println(timestamp() + " " + className() + " {" + calledVerbose.name() + "} : " + generateTitle("", spacesBetweenDots));
		return true;
	}

	//////////////////////////////
	//       SET VERBOSE        //
	//////////////////////////////

	public static void setVerbose(Verbose newVerbose){
		fixedVerbose = newVerbose;
	}


	//////////////////////////////
	//       CALL VERBOSE       //
	//////////////////////////////

	public static boolean error() {
		calledVerbose = Verbose.ERROR;
		return fixedVerbose.ordinal() >= Verbose.ERROR.ordinal();
	}
	
	public static boolean vital() {
		calledVerbose = Verbose.VITAL;
		return fixedVerbose.ordinal() >= Verbose.VITAL.ordinal();
	}
	
	public static boolean warng() {
		calledVerbose = Verbose.WARNG;
		return fixedVerbose.ordinal() >= Verbose.WARNG.ordinal();
	}

	public static boolean infos() {
		calledVerbose = Verbose.INFOS;
		return fixedVerbose.ordinal() >= Verbose.INFOS.ordinal();
	}

	public static boolean extra() {
		calledVerbose = Verbose.EXTRA;
		return fixedVerbose.ordinal() >= Verbose.EXTRA.ordinal();
	}


	//////////////////////////////
	//     PRIVATE METHODS      //
	//////////////////////////////

	private static String timestamp(){
		Date timestamp = new Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat("[HH:mm:ss:SSS]");
		return timeFormat.format(timestamp);
	}

	private static String className() {
		String fullStackTraceElement = new Throwable().getStackTrace()[2].toString();
		String className = fullStackTraceElement.substring(fullStackTraceElement.indexOf('('), fullStackTraceElement.indexOf(')') + 1);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(className);
		stringBuilder.append(' ');

		for (int i = stringBuilder.length(); i < CLASSNAME_LENGTH; ++i) {
			stringBuilder.append(CLASSNAME_FILLER);
		}

		return stringBuilder.toString();
	}

	private static String generateTitle(String title, int spacesBetweenDots) {
		StringBuilder line = new StringBuilder();

		line.append(TITLE_LINE_CHAR);

		while ((line.length() + spacesBetweenDots) < TITLE_LINE_LENGTH) {
			for (int i = 0; i < spacesBetweenDots; ++i) {
				line.append(' ');
			}
			line.append(TITLE_LINE_CHAR);
		}
		while (line.length() < TITLE_LINE_LENGTH - 1) {
			line.append(' ');
		}
		if (line.length() == TITLE_LINE_LENGTH - 1) {
			line.append(TITLE_LINE_CHAR);
		}

		if (title != null && title.length() != 0) {
			title = " " + title + " ";
		} else {
			title = "";
		}
		int titleLength = title.length();
		int titleStartPos = (TITLE_LINE_LENGTH - titleLength + 1) / 2;

		if (titleStartPos < 0) {
			line.replace(0, title.length() - 1, title.substring(1));
		} else {
			line.replace(titleStartPos, titleStartPos + titleLength, title);
		}

		return line.toString();
	}
}
