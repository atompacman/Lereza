package atompacman.lereza.prototype.probStructures;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Prob3DmatrixBuilder<TYPE_A extends Enum<TYPE_A>, TYPE_B extends Enum<TYPE_B>, TYPE_C extends Enum<TYPE_C>> {

	private Map<TYPE_A, Map<TYPE_B, Map<TYPE_C, Integer>>> count3Dmatrix;
	private int totalCount;
	
	
	public Prob3DmatrixBuilder() {
		this.count3Dmatrix = new HashMap<TYPE_A, Map<TYPE_B, Map<TYPE_C, Integer>>>();
	}
	
	public void increment(TYPE_A keyA, TYPE_B keyB, TYPE_C keyC) {
		if (count3Dmatrix.get(keyA) == null) {
			count3Dmatrix.put(keyA, new HashMap<TYPE_B, Map<TYPE_C, Integer>>());
		}
		if (count3Dmatrix.get(keyA).get(keyB) == null) {
			count3Dmatrix.get(keyA).put(keyB, new HashMap<TYPE_C, Integer>());
		}
		if (count3Dmatrix.get(keyA).get(keyB).get(keyC) == null) {
			count3Dmatrix.get(keyA).get(keyB).put(keyC, 0);
		}
		int count = count3Dmatrix.get(keyA).get(keyB).get(keyC);
		count3Dmatrix.get(keyA).get(keyB).put(keyC, count + 1);
		
		++totalCount;
	}
	
	public Prob3Dmatrix<TYPE_A, TYPE_B, TYPE_C> createProb3Dmatrix() {
		HashMap<TYPE_A, Map<TYPE_B, Map<TYPE_C, Prob>>> tempMap = new HashMap<TYPE_A, Map<TYPE_B, Map<TYPE_C, Prob>>>();
		
		for (Entry<TYPE_A, Map<TYPE_B, Map<TYPE_C, Integer>>> entryA : count3Dmatrix.entrySet()) {
			TYPE_A keyA = entryA.getKey();
			for (Entry<TYPE_B, Map<TYPE_C, Integer>> entryB : entryA.getValue().entrySet()) {
				TYPE_B keyB = entryB.getKey();
				for (Entry<TYPE_C, Integer> entryC : entryB.getValue().entrySet()) {
					TYPE_C keyC = entryC.getKey();
					
					if (tempMap.get(keyA) == null) {
						tempMap.put(keyA, new HashMap<TYPE_B, Map<TYPE_C, Prob>>());
					}
					if (tempMap.get(keyA).get(keyB) == null) {
						tempMap.get(keyA).put(keyB, new HashMap<TYPE_C, Prob>());
					}
					tempMap.get(keyA).get(keyB).put(keyC, new Prob(entryC.getValue(), totalCount));
				}
			}
		}
		return new Prob3Dmatrix<TYPE_A, TYPE_B, TYPE_C>(tempMap);
	}
}
