package com.atompacman.lereza.core.profile.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataChart {

	private static final Alignement DEFAULT_ALIGNEMENT = Alignement.LEFT;
	private static final String DEFAULT_HORIZ_BORDERS = "—";
	private static final String DEFAULT_VERT_BORDERS = "|";
	private static final int DEFAULT_PADDING = 1;
	private static final char CORNER_CHAR = '+';

	public enum Alignement {
		LEFT, CENTER, RIGHT;
	}
	
	public enum Importance {
		VERY_LOW, LOW, NORMAL, HIGH, VERY_HIGH;
	}

	private static class Cell {
		protected List<String> data;
		protected Alignement alignement;

		public Cell() {
			this(new ArrayList<String>());
		}
		
		public Cell(List<String> data) {
			this.data = data;
			if (data.isEmpty()) {
				this.data.add("");
			}
			this.alignement = null;
		}

		public int heigth() {
			return Math.max(1, data.size());
		}

		public int length() {
			int length = 0;
			for (String line : data) {
				if (line.length() > length) {
					length = line.length();
				}
			}
			return length + 2 * cellPadding;
		}

		public String toString() {
			String output = "";
			for (String line : data) {
				output += line + '/';
			}
			return output.substring(0, output.length() - 1);
		}
	}

	
	private String chartName;
	private List<List<Cell>> data;
	private Importance importance;
	
	private Alignement chartAlignement;
	private Map<Integer, Alignement> rowAlignements;
	private Map<Integer, Alignement> columnAlignements;
	
	private String chartHorizBorders;
	private String chartVertBorders;
	private Map<Integer, String> horizBorders;
	private Map<Integer, String> vertBorders;
	
	private static int cellPadding = DEFAULT_PADDING;

	
	//////////////////////////////
	//        CONSTRUCTOR       //
	//////////////////////////////
	
	protected DataChart(String chartName, Importance importance) {
		this.chartName = chartName;
		this.data = new ArrayList<List<Cell>>();
		this.importance = importance;
		this.chartAlignement = DEFAULT_ALIGNEMENT;
		this.rowAlignements = new HashMap<Integer, Alignement>();
		this.columnAlignements = new HashMap<Integer, Alignement>();
		this.chartHorizBorders = DEFAULT_HORIZ_BORDERS;
		this.chartVertBorders = DEFAULT_VERT_BORDERS;
		this.horizBorders = new HashMap<Integer, String>();
		this.vertBorders = new HashMap<Integer, String>();
	}

	
	//////////////////////////////
	//         SET DATA         //
	//////////////////////////////

	protected void setDataAt(int x, int y, List<String> newData) {
		enlargeChart(x, y);
		data.get(y).get(x).data = newData;
	}
	
	private void enlargeChart(int x, int y) {
		int futureHeight = Math.max(y + 1, data.size());
		int futureLength = Math.max(x + 1, data.isEmpty() ? 0 : data.get(0).size());

		for (int yy = 0; yy < futureHeight; ++yy) {
			if (yy > data.size() - 1) {
				data.add(new ArrayList<Cell>());
			}
			for (int xx = data.get(yy).size(); xx < futureLength; ++xx) {
				data.get(yy).add(new Cell());
			}
		}
	}


	//////////////////////////////
	//        ALIGNEMENT        //
	//////////////////////////////

	public void setChartAlignement(Alignement alignement) {
		chartAlignement = alignement;
	}
	
	public void setRowAlignement(int row, Alignement alignement) {
		rowAlignements.put(row, alignement);
	}

	public void setColumnAlignement(int column, Alignement alignement) {
		columnAlignements.put(column, alignement);
	}

	public void setCellAlignement(int x, int y, Alignement alignement) {
		enlargeChart(x, y);
		data.get(y).get(x).alignement = alignement;
	}


	//////////////////////////////
	//         BORDERS          //
	//////////////////////////////

	public void setChartBorders(String horizLinePattern, String vertLinePattern) {
		chartHorizBorders = horizLinePattern;
		chartVertBorders = vertLinePattern;
	}
	
	public void setHorizBorder(int row, String horizLinePattern) {
		horizBorders.put(row, horizLinePattern);
	}

	public void setVertBorder(int column, String vertLinePattern) {
		vertBorders.put(column, vertLinePattern);
	}

	
	//////////////////////////////
	//         PADDING          //
	//////////////////////////////
	
	public void setPadding(int padding) {
		cellPadding = padding;
	}
	

	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////
	
	public Importance getImportance() {
		return importance;
	}
	
	public String getName() {
		return chartName;
	}
	
	
	//////////////////////////////
	//      GENERATE CHART      //
	//////////////////////////////

	protected List<String> generateChart(List<Integer> level) {
		List<String> lines = new ArrayList<String>();

		for (int y = 0; y < data.size(); ++y) {
			lines.add(ProfileReportFormatter.levelIndentation(level) + generateHorizLine(y));
			for (int yy = 0; yy < getRowHeight(y); ++yy) {
				lines.add(ProfileReportFormatter.levelIndentation(level) + generateDataLine(y, yy));
			}
		}
		lines.add(ProfileReportFormatter.levelIndentation(level) + generateHorizLine(data.size()));

		return lines;
	}

	private String generateHorizLine(int y) {
		StringBuilder builder = new StringBuilder();

		for (int x = 0; x < data.get(0).size(); ++x) {
			builder.append(CORNER_CHAR);
			int columnLength = getColumnLength(x);
			String horizBorderPattern = getHorizBorderPattern(x, y);
			for (int i = 0; i < columnLength; ++i) {
				builder.append(horizBorderPattern.charAt(i % horizBorderPattern.length()));
			}
		}
		builder.append(CORNER_CHAR);

		return builder.toString();
	}

	private String generateDataLine(int y, int yy) {
		StringBuilder builder = new StringBuilder();

		for (int x = 0; x < data.get(0).size(); ++x) {
			String vertBorderPattern = getVertBorderPattern(x, y);
			builder.append(vertBorderPattern.charAt(yy % vertBorderPattern.length()));
			int columnLength = getColumnLength(x);
			builder.append(generateDataLine(x , y, yy, columnLength));
		}
		String vertBorderPattern = getVertBorderPattern(data.get(y).size(), y);
		builder.append(vertBorderPattern.charAt(yy % vertBorderPattern.length()));

		return builder.toString();
	}

	private String generateDataLine(int x, int y, int yy, int columnLength) {
		StringBuilder builder = new StringBuilder();

		if (yy > data.get(y).get(x).heigth() - 1) {
			for (int i = 0; i < columnLength; ++i) {
				builder.append(' ');
			}
			return builder.toString();
		}
		
		String dataLine = data.get(y).get(x).data.get(yy);

		int nbSpaces = columnLength - dataLine.length() - 2 * cellPadding;
		int nbSpacesLeft = cellPadding;
		int nbSpacesRight = cellPadding;

		switch(getCellAlignement(x, y)) {
		case CENTER:
			nbSpacesLeft += nbSpaces / 2;
			nbSpacesRight +=  nbSpaces / 2 + (nbSpaces % 2 == 0 ? 0 : 1);
			break;
		case LEFT:
			nbSpacesRight += nbSpaces;
			break;
		case RIGHT:
			nbSpacesLeft += nbSpaces;
			break;
		}
		for (int i = 0; i < nbSpacesLeft; ++i) {
			builder.append(' ');
		}
		builder.append(dataLine);

		for (int i = 0; i < nbSpacesRight; ++i) {
			builder.append(' ');
		}

		return builder.toString();
	}

	private int getColumnLength(int x) {
		int maxLength = 0;

		for (int y = 0; y < data.size(); ++y) {
			int length = data.get(y).get(x).length();
			if (length > maxLength) {
				maxLength = length;
			}
		}
		return maxLength;
	}

	private int getRowHeight(int y) {
		int maxHeight = 0;
		for (int x = 0; x < data.get(y).size(); ++x) {
			int height = data.get(y).get(x).heigth();
			if (height > maxHeight) {
				maxHeight = height;
			}
		}
		return maxHeight;
	}

	private Alignement getCellAlignement(int x, int y) {
		Alignement alignement = chartAlignement;
		
		if (rowAlignements.get(y) != null) {
			alignement = rowAlignements.get(y);
		}
		if (columnAlignements.get(x) != null) {
			alignement = columnAlignements.get(x);
		}
		if (data.get(y).get(x).alignement != null) {
			alignement = data.get(y).get(x).alignement;
		}
		return alignement;
	}
	
	private String getHorizBorderPattern(int x, int y) {
		return horizBorders.get(y) == null ? chartHorizBorders : horizBorders.get(y);
	}

	private String getVertBorderPattern(int x, int y) {
		return vertBorders.get(x) == null ? chartVertBorders : vertBorders.get(x);
	}
}
