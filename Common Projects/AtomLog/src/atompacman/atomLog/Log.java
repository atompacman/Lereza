package atompacman.atomLog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

	public enum Verbose{ OFF, VITAL, INFOS, EXTRA }

	private static final int CLASSNAME_LENGTH = 32;
	private static final char CLASSNAME_FILLER = '=';
	
	private static Verbose verbose = Verbose.INFOS;
	
	
	//////////////////////////////
	//      PRINT MESSAGES      //
	//////////////////////////////
	
	public static void vital(String message) {
		if (verbose.ordinal() >= Verbose.VITAL.ordinal()){
			System.out.println(timestamp() + " " + className() + " {VITAL} : " + message);
		}
	}
	
	public static void infos(String message) {
		if (verbose.ordinal() >= Verbose.INFOS.ordinal()){
			System.out.println(timestamp() + " " + className() + " {INFOS} : " + message);
		}
	}
	
	public static void extra(String message) {
		if (verbose.ordinal() >= Verbose.EXTRA.ordinal()){
			System.out.println(timestamp() + " " + className() + " {EXTRA} : " + message);
		}
	}
	
	public static void error(String message) {
		System.out.println(timestamp() + " " + className() + " {ERROR} : " + message);
	}
	
	public static void warng(String message){
		if (verbose.ordinal() >= Verbose.VITAL.ordinal()) {
			System.out.println(timestamp() + " " + className() + " {WARNG} : " + message);
		}
	}
	
	//////////////////////////////
	//       SET VERBOSE        //
	//////////////////////////////
	
	public static void setVerbose(Verbose newVerbose){
		verbose = newVerbose;
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
}
