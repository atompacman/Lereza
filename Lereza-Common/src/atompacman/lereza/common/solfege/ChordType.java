package atompacman.lereza.common.solfege;

import java.util.Arrays;
import java.util.List;

import atompacman.lereza.common.solfege.quality.FifthQuality;
import atompacman.lereza.common.solfege.quality.IntervalQuality;
import atompacman.lereza.common.solfege.quality.SecondQuality;
import atompacman.lereza.common.solfege.quality.ThirdQuality;

public enum ChordType {
	
	MINOR (Arrays.asList(
			ThirdQuality.MINOR,
			FifthQuality.PERFECT)), 
	MAJOR (Arrays.asList(
			ThirdQuality.MAJOR,
			FifthQuality.PERFECT)),
	
	DIMINISHED (Arrays.asList(
			ThirdQuality.MINOR,
			FifthQuality.DIMINISHED));
	

	List<? extends IntervalQuality> intervalQualities = Arrays.asList(SecondQuality.MAJOR, ThirdQuality.MINOR);

	
	private ChordType(List<? extends IntervalQuality> intervalQualities) {
		this.intervalQualities = intervalQualities;
	}
	
	public String toString() {
		switch(this) {
		case MINOR:		 return "";
		case MAJOR:		 return "m";
		case DIMINISHED: return "dim";
		default:		 return "?";
		}
	}
}
