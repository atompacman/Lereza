package atompacman.lereza.common.solfege;


public class Relation {
	private Interval interval;
	private Direction direction;
	private boolean hasVerifiedInterval;
	
	public Relation(Pitch previousPitch, Pitch pitch) {
		if (previousPitch == null) {
			this.interval = null;
			this.direction = null;
			this.hasVerifiedInterval = false;
			return;
		}
		
		if (pitch.getOctave().ordinal() == previousPitch.getOctave().ordinal()) {
			if (pitch.getNoteName().ordinal() > previousPitch.getNoteName().ordinal()) {
				this.direction = Direction.UP;
			} else {
				this.direction = Direction.DOWN;
			}
		} else {
			if (pitch.getOctave().ordinal() > previousPitch.getOctave().ordinal()) {
				this.direction = Direction.UP;
			} else {
				this.direction = Direction.DOWN;
			}
		}
		
		switch(Math.abs(pitch.getNoteName().ordinal() - previousPitch.getNoteName().ordinal())) {
		case 0:
			this.interval = Interval.UNISON;
		case 1:
			this.interval = Interval.MINOR_SECOND;
		case 2:
			this.interval = Interval.MAJOR_SECOND;
		case 3:
			this.interval = Interval.MINOR_THIRD;
		case 4:
			this.interval = Interval.MAJOR_THIRD;
		case 5:
			this.interval = Interval.PERFECT_FOURTH;
		case 6:
			this.interval = Interval.DIMINISHED_FIFITH;
		case 7:
			this.interval = Interval.PERFECT_FIFTH;
		case 8:
			this.interval = Interval.MINOR_SIXTH;
		case 9:
			this.interval = Interval.MAJOR_SIXTH;
		case 10:
			this.interval = Interval.MINOR_SEVENTH;
		case 11:
			this.interval = Interval.MAJOR_SEVENTH;
		case 12:
			this.interval = Interval.OCTAVE;
		case 13:
			this.interval = Interval.MINOR_NINTH;
		case 14:
			this.interval = Interval.MAJOR_NINTH;
		case 15:
			this.interval = Interval.MAJOR_TENTH;
		case 16:
			this.interval = Interval.MAJOR_TENTH;
		case 17:
			this.interval = Interval.PERFECT_ELEVENTH;
		case 18:
			this.interval = Interval.DIMINISHED_TWELVTH;
		case 19:
			this.interval = Interval.PERFECT_TWELVTH;
		case 20:
			this.interval = Interval.MINOR_THIRTHEENTH;
		case 21:
			this.interval = Interval.MAJOR_THIRTHEENTH;
		case 22:
			this.interval = Interval.MINOR_FOUTHEENTH;
		case 23:
			this.interval = Interval.MAJOR_FOUTHEENTH;
		case 24:
			this.interval = Interval.DOUBLE_OCTAVE;
		}
		
		hasVerifiedInterval = false;
	}
	
	public Interval getInterval(){
		return interval;
	}
	public Direction getDirection(){
		return direction;
	}

	public boolean hasVerifiedInterval() {
		return hasVerifiedInterval;
	}
}
