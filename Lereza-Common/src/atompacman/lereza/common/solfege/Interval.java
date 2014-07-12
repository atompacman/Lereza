package atompacman.lereza.common.solfege;

public enum Interval {
	
	UNISON,
	MINOR_SECOND, MAJOR_SECOND,
	MINOR_THIRD, MAJOR_THIRD, 
	DIMINISHED_FORTH, PERFECT_FOURTH, AUGMENTED_FOURTH,
	DIMINISHED_FIFTH, PERFECT_FIFTH, AUGMENTED_FIFTH,
	MINOR_SIXTH, MAJOR_SIXTH,
	MINOR_SEVENTH, MAJOR_SEVENTH,
	OCTAVE,
	MINOR_NINTH, NINTH, MAJOR_NINTH,
	MINOR_TENTH, MAJOR_TENTH, 
	DIMINISHED_ELEVENTH, PERFECT_ELEVENTH, AUGMENTED_ELEVENTH,
	DIMINISHED_TWELVTH, PERFECT_TWELVTH, AUGMENTED_TWELVTH,
	MINOR_THIRTHEENTH, MAJOR_THIRTHEENTH,
	MINOR_FOUTHEENTH, MAJOR_FOUTHEENTH,
	DOUBLE_OCTAVE,
	
	DESC_MINOR_SECOND, DESC_MAJOR_SECOND,
	DESC_MINOR_THIRD, DESC_MAJOR_THIRD, 
	DESC_DIMINISHED_FORTH, DESC_PERFECT_FOURTH, DESC_AUGMENTED_FOURTH,
	DESC_DIMINISHED_FIFTH, DESC_PERFECT_FIFTH, DESC_AUGMENTED_FIFTH,
	DESC_MINOR_SIXTH, DESC_MAJOR_SIXTH,
	DESC_MINOR_SEVENTH, DESC_MAJOR_SEVENTH,
	DESC_OCTAVE,
	DESC_MINOR_NINTH, DESC_NINTH, DESC_MAJOR_NINTH,
	DESC_MINOR_TENTH, DESC_MAJOR_TENTH, 
	DESC_DIMINISHED_ELEVENTH, DESC_PERFECT_ELEVENTH, DESC_AUGMENTED_ELEVENTH,
	DESC_DIMINISHED_TWELVTH, DESC_PERFECT_TWELVTH, DESC_AUGMENTED_TWELVTH,
	DESC_MINOR_THIRTHEENTH, DESC_MAJOR_THIRTHEENTH,
	DESC_MINOR_FOUTHEENTH, DESC_MAJOR_FOUTHEENTH,
	DESC_DOUBLE_OCTAVE;
	
	
	public boolean isDescending() {
		return this.name().substring(0, 4).equalsIgnoreCase("DESC");
	}
	
	public boolean isAscending() {
		return !isDescending();
	}
	
	public static Interval getSimpleInterval(Pitch a, Pitch b) {
		int semiTones = 0;
		int octaves = b.getOctave().ordinal() - a.getOctave().ordinal();
		semiTones += octaves * 12;
		
		switch(a.getTone().getAlteration()) {
		case FLAT:  semiTones += 1; break;
		case SHARP: semiTones -= 1; break;
		default: break;
		}
		switch(b.getTone().getAlteration()) {
		case FLAT:  semiTones -= 1; break;
		case SHARP: semiTones += 1; break;
		default: break;
		}
		switch(a.getTone().getNote()) {
		case D:  semiTones -= 2;  break;
		case E:  semiTones -= 4;  break;
		case F:  semiTones -= 5;  break;
		case G:  semiTones -= 7;  break;
		case A:  semiTones -= 9;  break;
		case B:  semiTones -= 11; break;
		default: semiTones += 0;  break;
		}
		switch(b.getTone().getNote()) {
		case D:  semiTones += 2;  break;
		case E:  semiTones += 4;  break;
		case F:  semiTones += 5;  break;
		case G:  semiTones += 7;  break;
		case A:  semiTones += 9;  break;
		case B:  semiTones += 11; break;
		default: semiTones += 0;  break;
		}
		switch(semiTones) {
		case 0:  return Interval.UNISON;
		case 1:  return Interval.MINOR_SECOND;
		case 2:  return Interval.MAJOR_SECOND;
		case 3:  return Interval.MINOR_THIRD;
		case 4:  return Interval.MAJOR_THIRD;
		case 5:  return Interval.PERFECT_FOURTH;
		case 6:  return Interval.DIMINISHED_FIFTH;
		case 7:  return Interval.PERFECT_FIFTH;
		case 8:  return Interval.MINOR_SIXTH;
		case 9:  return Interval.MAJOR_SIXTH;
		case 10: return Interval.MINOR_SEVENTH;
		case 11: return Interval.MAJOR_SEVENTH;
		case 12: return Interval.OCTAVE;
		case 13: return Interval.MINOR_NINTH;
		case 14: return Interval.MAJOR_NINTH;
		case 15: return Interval.MINOR_TENTH;
		case 16: return Interval.MAJOR_TENTH;
		case 17: return Interval.PERFECT_ELEVENTH;
		case 18: return Interval.DIMINISHED_TWELVTH;
		case 19: return Interval.PERFECT_TWELVTH;
		case 20: return Interval.MINOR_THIRTHEENTH;
		case 21: return Interval.MAJOR_THIRTHEENTH;
		case 22: return Interval.MINOR_FOUTHEENTH;
		case 23: return Interval.MAJOR_FOUTHEENTH;
		case 24: return Interval.DOUBLE_OCTAVE;
		
		case -1:  return Interval.DESC_MINOR_SECOND;
		case -2:  return Interval.DESC_MAJOR_SECOND;
		case -3:  return Interval.DESC_MINOR_THIRD;
		case -4:  return Interval.DESC_MAJOR_THIRD;
		case -5:  return Interval.DESC_PERFECT_FOURTH;
		case -6:  return Interval.DESC_DIMINISHED_FIFTH;
		case -7:  return Interval.DESC_PERFECT_FIFTH;
		case -8:  return Interval.DESC_MINOR_SIXTH;
		case -9:  return Interval.DESC_MAJOR_SIXTH;
		case -10: return Interval.DESC_MINOR_SEVENTH;
		case -11: return Interval.DESC_MAJOR_SEVENTH;
		case -12: return Interval.DESC_OCTAVE;
		case -13: return Interval.DESC_MINOR_NINTH;
		case -14: return Interval.DESC_MAJOR_NINTH;
		case -15: return Interval.DESC_MINOR_TENTH;
		case -16: return Interval.DESC_MAJOR_TENTH;
		case -17: return Interval.DESC_PERFECT_ELEVENTH;
		case -18: return Interval.DESC_DIMINISHED_TWELVTH;
		case -19: return Interval.DESC_PERFECT_TWELVTH;
		case -20: return Interval.DESC_MINOR_THIRTHEENTH;
		case -21: return Interval.DESC_MAJOR_THIRTHEENTH;
		case -22: return Interval.DESC_MINOR_FOUTHEENTH;
		case -23: return Interval.DESC_MAJOR_FOUTHEENTH;
		case -24: return Interval.DESC_DOUBLE_OCTAVE;
		
		default : return null;
		}
	}
}
