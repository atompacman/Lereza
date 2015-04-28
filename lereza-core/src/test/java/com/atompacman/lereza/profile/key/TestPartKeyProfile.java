package com.atompacman.lereza.profile.key;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.atompacman.lereza.solfege.Key;
import com.atompacman.lereza.solfege.Tone;
import com.atompacman.lereza.solfege.quality.Quality;

public class TestPartKeyProfile {

	//------------ GETTERS / SETTERS ------------\\

	@Test
	public void testKeyChangeInsertion() {
		PartKeyProfile profile = new PartKeyProfile(0, 170);
		profile.addKeyChange(0, 3);
		profile.addKeyChange(10, 4);
		profile.addKeyChange(155, 7);
		
		assertEquals(Key.valueOf("A"), profile.getKeyAt(0));
		assertEquals(Key.valueOf("E"), profile.getKeyAt(10));
		assertEquals(Key.valueOf("E"), profile.getKeyAt(105));
		assertEquals(Key.valueOf("C#"), profile.getKeyAt(169));
		
		try {
			profile.getKeyAt(171);
		} catch (IllegalArgumentException e) {
			try {
				profile.getKeyAt(-3);
			} catch (IllegalArgumentException x) {
				return;
			}
		}
		fail("Exception should have been triggered.");
	}
	
	@Test
	public void testPossibleNotesForKeyMethods() {
		PartKeyProfile profile = new PartKeyProfile(8, 8);
		profile.incrementPossibleNotesForKey(11);
		profile.incrementPossibleNotesForKey(11);
		profile.incrementPossibleNotesForKey(11);
		profile.incrementPossibleNotesForKey(10);
		profile.incrementPossibleNotesForKey(9);
		profile.incrementPossibleNotesForKey(9);
		profile.incrementPossibleNotesForKey(8);
		profile.incrementPossibleNotesForKey(7);
		
		assertEquals(3, profile.getNbPossiblesNotesForKey(Key.valueOf("E#")));
		assertEquals(1, profile.getNbPossiblesNotesForKey(Key.valueOf("A#")));
		assertEquals(2, profile.getNbPossiblesNotesForKey(Key.valueOf("D#")));
		assertEquals(0, profile.getNbPossiblesNotesForKey(Key.valueOf("Cb")));
		Key key = Key.valueOf(Tone.valueOf("B#"), Quality.MINOR);
		assertEquals(2, profile.getNbPossiblesNotesForKey(key));

	}
	
	
	//------------ PRIVATE UTILS ------------\\

	@Test
	public void testToKey() {
		assertEquals(Key.valueOf("Cm"), PartKeyProfile.toKey(-3, Quality.MINOR));
	}
}
