package atompacman.lereza.prototype.probStructures;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ProbVector<TYPE extends Enum<TYPE>> {

	private Map<TYPE, Prob> probVector;
	
	
	protected ProbVector(HashMap<TYPE, Prob> hashMap) {
		this.probVector = new EnumMap<TYPE, Prob>(hashMap);
	}
	
	public Prob getProb(TYPE key) {
		return probVector.get(key);
	}
	
	public double getProbValue(TYPE key) {
		return getProb(key).getProb();
	}
	
	public TYPE randomElement() {
		double rand = Math.random();
		double total = 0;
		
		for (Entry<TYPE, Prob> entry : probVector.entrySet()) {
			total += entry.getValue().getProb();
			if (rand <= total) {
				return entry.getKey();
			}
		}
		return null;
	}
}
