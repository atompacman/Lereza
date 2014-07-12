package atompacman.lereza.prototype.probStructures;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ProbVectorBuilder<TYPE extends Enum<TYPE>> {

	private Map<TYPE, Integer> countVector;
	private int totalCount;
	
	
	public ProbVectorBuilder() {
		this.countVector = new HashMap<TYPE, Integer>();
		this.totalCount = 0;
	}
	
	public void increment(TYPE key) {
		Integer count = countVector.get(key);
		if (count == null) {
			countVector.put(key, 1);
		} else {
			countVector.put(key, count + 1);
		}
		++totalCount;
	}
	
	public ProbVector<TYPE> createProbVector() {
		HashMap<TYPE, Prob> tempMap = new HashMap<TYPE, Prob>();
		for (Entry<TYPE, Integer> entry : countVector.entrySet()) {
			tempMap.put(entry.getKey(), new Prob(entry.getValue(), totalCount));
		}
		return new ProbVector<TYPE>(tempMap);
	}
}
