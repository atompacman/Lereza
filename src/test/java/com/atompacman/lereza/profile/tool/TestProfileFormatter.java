package com.atompacman.lereza.profile.tool;

import org.junit.Test;

import com.atompacman.lereza.profile.fugue.FugueProfile;
import com.atompacman.lereza.profile.harmonic.HarmonicProfile;
import com.atompacman.lereza.profile.harmonic.PolyphonicProfile;
import com.atompacman.lereza.profile.melodic.MelodicProfile;
import com.atompacman.lereza.profile.tool.ProfileReportFormatter;
import com.atompacman.lereza.profile.tool.DataChart.Alignement;
import com.atompacman.lereza.profile.tool.DataChart.Importance;

public class TestProfileFormatter {

	@Test
	public void basicChart() {
		ProfileReportFormatter formatter = new ProfileReportFormatter(FugueProfile.class);
		
		formatter.addChart("Single Chart / Default Properties / Single Line Data", Importance.NORMAL);
		formatter.selectChart("Single Chart / Default Properties / Single Line Data");
		formatter.setDataAt(0, 2, "YOLO");
		formatter.setDataAt(3, 1, "WOLOLO");
		formatter.setDataAt(3, 3, "AYOYOYO");
		formatter.setDataAt(5, 4, "CAYOCOCO");
		
		formatter.print(Importance.NORMAL);
	}
	
	@Test
	public void multipleLineData() {
		ProfileReportFormatter formatter = new ProfileReportFormatter(FugueProfile.class);
		
		formatter.addChart("Multiple Line Data", Importance.NORMAL);
		formatter.selectChart("Multiple Line Data");
		formatter.setDataAt(1, 1, "YOLO", "banane");
		formatter.setDataAt(2, 1, "WOLOLO");
		formatter.setDataAt(3, 1, "AYOYOYO");
		formatter.setDataAt(2, 2, "CAYOCOCO", "fromage", "friture");
		
		formatter.print(Importance.NORMAL);
	}
	
	@Test
	public void multipleChart() {
		ProfileReportFormatter formatter = new ProfileReportFormatter(FugueProfile.class);
		
		formatter.addChart("Multiple Charts", Importance.NORMAL);
		formatter.selectChart("Multiple Charts");
		formatter.setDataAt(1, 1, "YOLO", "banane");
		formatter.setDataAt(2, 1, "WOLOLO");
		
		formatter.addChart("Multiple Charts2", Importance.NORMAL);
		formatter.selectChart("Multiple Charts2");
		formatter.setDataAt(0, 1, "YOLO", "banane");
		formatter.setDataAt(1, 1, "WOLOLO");
		
		
		formatter.addChildProfile(HarmonicProfile.class);
		formatter.selectChildProfile(HarmonicProfile.class);
		
		formatter.addChart("Multiple Charts", Importance.NORMAL);
		formatter.selectChart("Multiple Charts");
		formatter.setDataAt(2, 2, "YOLO", "banane");
		
		
		formatter.selectParentProfile();
		formatter.addChildProfile(PolyphonicProfile.class);
		formatter.selectChildProfile(PolyphonicProfile.class);
		
		formatter.addChart("Multiple Charts", Importance.NORMAL);
		formatter.selectChart("Multiple Charts");
		formatter.setDataAt(2, 2, "YOLO", "banane");
		
		formatter.addChart("Multiple Charts 2", Importance.NORMAL);
		formatter.selectChart("Multiple Charts 2");
		formatter.setDataAt(2, 2, "YOLO", "banane");
		
		
		formatter.selectParentProfile();
		formatter.addChildProfile(MelodicProfile.class);
		formatter.selectChildProfile(MelodicProfile.class);
		
		formatter.addChart("Multiple Charts", Importance.NORMAL);
		formatter.selectChart("Multiple Charts");
		formatter.setDataAt(2, 2, "YOLO", "banane");
		
		formatter.print(Importance.NORMAL);
	}
	
	@Test
	public void customAlignement() {
		ProfileReportFormatter formatter = new ProfileReportFormatter(FugueProfile.class);
		
		formatter.addChart("Custom Alignement", Importance.NORMAL);
		formatter.selectChart("Custom Alignement");
		formatter.changeChartProperties().setCellAlignement(0, 1, Alignement.RIGHT);
		formatter.changeChartProperties().setColumnAlignement(1, Alignement.CENTER);
		formatter.changeChartProperties().setRowAlignement(2, Alignement.RIGHT);
		formatter.setDataAt(0, 0, "YOLO");
		formatter.setDataAt(0, 1, "WOLOLO");
		formatter.setDataAt(0, 2, "AYOYOYO");
		formatter.setDataAt(0, 3, "CAYOCOCOOOOOOOOOOO");
		formatter.setDataAt(1, 0, "YOLO");
		formatter.setDataAt(1, 1, "WOLOLO");
		formatter.setDataAt(1, 2, "AYOYOYO");
		formatter.setDataAt(1, 3, "CAYOCOCOOOOOOOOOOO");
		
		formatter.print(Importance.NORMAL);
	}
	
	@Test
	public void customBorders() {
		ProfileReportFormatter formatter = new ProfileReportFormatter(FugueProfile.class);
		
		formatter.addChart("Custom Alignement", Importance.NORMAL);
		formatter.selectChart("Custom Alignement");
		formatter.changeChartProperties().setVertBorder(2, "+");
		formatter.changeChartProperties().setHorizBorder(1, "_");
		formatter.changeChartProperties().setHorizBorder(4, "- ");
		formatter.setDataAt(0, 0, "YOLO");
		formatter.setDataAt(0, 1, "WOLOLO");
		formatter.setDataAt(0, 2, "AYOYOYO", "poulet", "poulet");
		formatter.setDataAt(0, 3, "CAYOCOCOOOOOOOOOOO");
		formatter.setDataAt(1, 0, "YOLO");
		formatter.setDataAt(1, 1, "WOLOLO");
		formatter.setDataAt(1, 2, "AYOYOYO");
		formatter.setDataAt(1, 3, "CAYOCOCOOOOOOOOOOO");
		
		formatter.print(Importance.NORMAL);
	}
	
	@Test
	public void completeTest() {
		ProfileReportFormatter formatter = new ProfileReportFormatter(FugueProfile.class);
		
		formatter.addChart("Custom Alignement", Importance.NORMAL);
		formatter.selectChart("Custom Alignement");
		formatter.setDataAt(0, 0, "UNIT");
		formatter.setDataAt(0, 1, "Warrior", "(offensive", "unit)");
		formatter.setDataAt(0, 2, "Archer", "(offensive", "unit)");
		formatter.setDataAt(0, 3, "Spearman", "(defensive", "unit)");
		formatter.setDataAt(0, 4, "Swordsman", "(offensive", "unit)");
		formatter.setDataAt(1, 0, "Attack");
		formatter.setDataAt(1, 1, "1");
		formatter.setDataAt(1, 2, "2");
		formatter.setDataAt(1, 3, "1");
		formatter.setDataAt(1, 4, "3");
		formatter.setDataAt(2, 0, "Defence");
		formatter.setDataAt(2, 1, "1");
		formatter.setDataAt(2, 2, "1");
		formatter.setDataAt(2, 3, "2");
		formatter.setDataAt(2, 4, "2");
		formatter.changeChartProperties().setCellAlignement(0, 0, Alignement.CENTER);
		formatter.changeChartProperties().setColumnAlignement(2, Alignement.RIGHT);
		formatter.changeChartProperties().setHorizBorder(1, "-");
		formatter.changeChartProperties().setPadding(3);
		formatter.changeChartProperties().setRowAlignement(2, Alignement.CENTER);;
		formatter.changeChartProperties().setVertBorder(1, "|:.");
		
		formatter.print(Importance.NORMAL);
	}
}
