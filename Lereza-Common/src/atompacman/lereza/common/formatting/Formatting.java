package atompacman.lereza.common.formatting;

public class Formatting {

	private static final int  LINE_LENGTH = 97;
	private static final char LINE_CHAR = '=';
	
	
	public static String lineSeparation(String title, int spacesBetweenDots) {
		StringBuilder line = new StringBuilder();
		line.append(LINE_CHAR);
		
		while ((line.length() + spacesBetweenDots) < LINE_LENGTH) {
			for (int i = 0; i < spacesBetweenDots; ++i) {
				line.append(' ');
			}
			line.append(LINE_CHAR);
		}
		while (line.length() < LINE_LENGTH - 1) {
			line.append(' ');
		}
		if (line.length() == LINE_LENGTH - 1) {
			line.append(LINE_CHAR);
		}
		
		if (title != null && title.length() != 0) {
			title = " " + title + " ";
		} else {
			title = "";
		}
		int titleLength = title.length();
		int titleStartPos = (LINE_LENGTH - titleLength + 1) / 2;
		
		if (titleStartPos < 0) {
			line.replace(0, title.length() - 1, title.substring(1));
		} else {
			line.replace(titleStartPos, titleStartPos + titleLength, title);
		}
		
		return line.toString();
	}
	
	public static String lineSeparation(int spacesBetweenDots) {
		return lineSeparation("", spacesBetweenDots);
	}
}
