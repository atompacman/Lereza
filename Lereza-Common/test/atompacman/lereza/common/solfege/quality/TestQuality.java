package atompacman.lereza.common.solfege.quality;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.quality.AdvancedQuality;
import com.atompacman.lereza.common.solfege.quality.Quality;

public class TestQuality {

	@Test
	public void testRepresentations() {
		Quality minor = Quality.MINOR;
		AdvancedQuality diminished = AdvancedQuality.DIMINISHED;
		
		assertTrue("Minor quality representation is not \"m\"", minor.toString().equals("m"));
		assertTrue("Error parsing minor quality", diminished.equals(AdvancedQuality.parseQuality("dim")));
	}
	
	@Test
	public void testSemitoneModifiers() {
		Quality major = Quality.MAJOR;
		AdvancedQuality augmented = AdvancedQuality.AUGMENTED;
		
		assertTrue("Major quality semitone modifier is not \"0.5\"", major.semitoneModifier() == 0.5);
		assertTrue("Augmented quality semitone modifier is not \"1.0\"", augmented.semitoneModifier() == 1.0);
	}
}
