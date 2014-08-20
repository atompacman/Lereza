package atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.ChordType;
import com.atompacman.lereza.common.solfege.Degree;
import com.atompacman.lereza.common.solfege.ScaleDegree;
import com.atompacman.lereza.common.solfege.ScaleType;

public class TestScaleType {

	@Test
	public void testDegrees() {
		Degree scaleDegree = ScaleType.MAJOR.getDegrees().get(2);
		Degree expected = new Degree(ScaleDegree.III, ChordType.valueOf("m7"));
		assertTrue(scaleDegree.equals(expected));
	}
}
