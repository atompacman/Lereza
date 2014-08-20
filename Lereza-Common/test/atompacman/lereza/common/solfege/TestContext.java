package atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.Context;
import com.atompacman.lereza.common.solfege.Context.Genre;

public class TestContext {
	
	@Test
	public void testContext() {
		Context context = new Context(Genre.BAROQUE.HIGH_BAROQUE.FUGUE);
		
		assertTrue(context.toString().equals("BAROQUE/HIGH_BAROQUE/FUGUE"));
		String answer = "[BAROQUE/EARLY_BAROQUE/FUGUE, BAROQUE/MIDDLE_BAROQUE/FUGUE]";
		assertTrue(context.findSimilarForms().toString().equals(answer));
	}
}
