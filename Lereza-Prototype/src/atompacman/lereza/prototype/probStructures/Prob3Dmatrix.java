package atompacman.lereza.prototype.probStructures;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Prob3Dmatrix<TYPE_A extends Enum<TYPE_A>, TYPE_B extends Enum<TYPE_B>, TYPE_C extends Enum<TYPE_C>> {

	private Map<TYPE_A, Map<TYPE_B, Map<TYPE_C, Prob>>> prob3Dmatrix;
	
	
	protected Prob3Dmatrix(HashMap<TYPE_A, Map<TYPE_B, Map<TYPE_C, Prob>>> hashMap) {
		this.prob3Dmatrix = new EnumMap<TYPE_A, Map<TYPE_B, Map<TYPE_C, Prob>>>(hashMap);
	}
	
	public Prob getProb(TYPE_A keyA, TYPE_B keyB, TYPE_C keyC) {
		if (prob3Dmatrix.get(keyA) == null) {
			return new Prob(0, 0);
		}
		if (prob3Dmatrix.get(keyA).get(keyB) == null) {
			return new Prob(0, 0);
		}
		if (prob3Dmatrix.get(keyA).get(keyB).get(keyC) == null) {
			return new Prob(0, 0);
		}
		return prob3Dmatrix.get(keyA).get(keyB).get(keyC);
	}
	
	public double getProbValue(TYPE_A keyA, TYPE_B keyB, TYPE_C keyC) {
		return getProb(keyA, keyB, keyC).getProb();
	}
	
	public boolean keepEntry(TYPE_A keyA, TYPE_B keyB, TYPE_C keyC) {
		double prob = getProb(keyA, keyB, keyC).getProb();
		if (Math.random() <= prob) {
			return true;
		}
		return false;
	}
}
