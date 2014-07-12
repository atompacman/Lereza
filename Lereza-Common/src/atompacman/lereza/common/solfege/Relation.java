package atompacman.lereza.common.solfege;

import java.util.Arrays;
import java.util.List;


public class Relation {

	private Interval interval;
	private List<Value> value;
	private Degree degreeA;
	private Degree degreeB;
	
	
	public Relation(Interval interval, List<Value> value, Degree degreeA, Degree degreeB) {
		this.interval = interval;
		this.value = value;
		this.degreeA = degreeA;
		this.degreeB = degreeB;
	}
	
	public Relation(Interval interval, Value value, Degree degreeA, Degree degreeB) {
		this(interval, Arrays.asList(value), degreeA, degreeB);
	}
	
	public Interval getInterval() {
		return interval;
	}
	
	public List<Value> getValue() {
		return value;
	}
	
	public Degree getDegreeA() {
		return degreeA;
	}
	
	public Degree getDegreeB() {
		return degreeB;
	}
}
