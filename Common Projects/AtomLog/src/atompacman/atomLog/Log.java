package atompacman.atomLog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

	public enum Verbose{ OFF, ESSENTIAL, NORMAL, UNIMPORTANT }

	private static final int CLASSNAME_LENGTH = 30;
	private static final char CLASSNAME_FILLER = '=';
	
	private static Verbose verbose = Verbose.NORMAL;
	
	
	//////////////////////////////
	//      PRINT MESSAGES      //
	//////////////////////////////
	
	public static void essentialMsg(String message) {
		if (verbose.ordinal() >= Verbose.ESSENTIAL.ordinal()){
			System.out.println(timestamp() + " " + className() + " {VITAL} : " + message);
		}
	}
	
	public static void normalMsg(String message) {
		if (verbose.ordinal() >= Verbose.NORMAL.ordinal()){
			System.out.println(timestamp() + " " + className() + " {INFOS} : " + message);
		}
	}
	
	public static void unimportantMsg(String message) {
		if (verbose.ordinal() >= Verbose.UNIMPORTANT.ordinal()){
			System.out.println(timestamp() + " " + className() + " {EXTRA} : " + message);
		}
	}
	
	public static void errorMsg(String message) {
		System.out.println(timestamp() + " " + className() + " {ERROR} : " + message);
	}
	
	public static void warningMsg(String message){
		if (verbose.ordinal() >= Verbose.ESSENTIAL.ordinal()) {
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
		SimpleDateFormat timeFormat = new SimpleDateFormat("[dd/MM/yyyy][HH:mm:ss:SSS]");
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
		
		return String.format("%-30s", stringBuilder.toString());
	}
}
