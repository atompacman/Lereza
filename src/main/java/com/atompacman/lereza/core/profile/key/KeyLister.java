package com.atompacman.lereza.core.profile.key;

public class KeyLister {
//	
//	public List<Key> listPossibleKeys(List<Tone> tones) {
//		List<Key> possibleKeys = new ArrayList<Key>();
//		List<List<Key>> possibleKeysForEachTone = new ArrayList<List<Key>>();
//		
//		for (Tone tone : tones) {
//			possibleKeysForEachTone.add(listPossibleKeys(tone));
//		}
//		
//		for (Key key : possibleKeysForEachTone.get(0)) {
//			boolean isPossibleWithEachTone = true;
//			
//			for (List<Key> possibleTones : possibleKeysForEachTone) {
//				if (!possibleTones.contains(key)) {
//					isPossibleWithEachTone = false;
//					break;
//				}
//			}
//			if (isPossibleWithEachTone) {
//				possibleKeys.add(key);
//			}
//		}
//		return possibleKeys;
//	}
//	
//	public List<Key> listPossibleKeys(Tone tone) {
//		List<Key> possibleKeys = new ArrayList<Key>();
//
//		for (Tone scaleTone : Tone.values()) {
//			Key key = new Key(scaleTone, Quality.MAJOR);
//			if (key.contains(tone)) {
//				possibleKeys.add(key);
//			}
//		}
//		for (Tone scaleTone : Tone.values()) {
//			Key key = new Key(scaleTone, Quality.MINOR);
//			if (key.contains(tone)) {
//				possibleKeys.add(key);
//			}
//		}
//		return possibleKeys;
//	}
}
