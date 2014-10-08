package com.atompacman.lereza.common.helper;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.helper.EnumRepresConstruc;
import com.atompacman.lereza.common.solfege.NoteLetter;
import com.atompacman.lereza.common.solfege.Octave;
import com.atompacman.lereza.common.solfege.Tone;

public class TestEnumRepresConstructor {

	//**********************************************************************************************
	//                            INTERNAL ENUM REPRESENTATION CONSTRUCTOR
	//**********************************************************************************************
	
	public static class DummyToneA {
		public NoteLetter letter;
		public Octave octave;
		
		private static EnumRepresConstruc<DummyToneA> a = 
				new EnumRepresConstruc<DummyToneA>(DummyToneA.class);
		
		public DummyToneA(NoteLetter letter, Octave octave) {
			this.letter = letter;
			this.octave = octave;
		}
		
		public static DummyToneA valueOf(String repres) {
			return a.newInstance(repres);
		}
		
		public boolean equals(Object o) {
			return letter == ((DummyToneA)o).letter && octave == ((DummyToneA)o).octave;
		}
	}

	@Test
	public void testIntegratedERC() {
		DummyToneA a = DummyToneA.valueOf("A5");
		DummyToneA b = new DummyToneA(NoteLetter.A, Octave.FIVE);
		assertTrue(a.equals(b));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void missingInformationDetection() {
		DummyToneA.valueOf("A");
	}
	
	
	//**********************************************************************************************
	//                            EXTERNAL ENUM REPRESENTATION CONSTRUCTOR
	//**********************************************************************************************
	
	public static class DummyToneB {
		public NoteLetter letter;
		public Octave octave;
		
		
		public DummyToneB(NoteLetter letter, Octave octave) {
			this.letter = letter;
			this.octave = octave;
		}

		public boolean equals(Object o) {
			return letter == ((DummyToneB)o).letter && octave == ((DummyToneB)o).octave;
		}
	}
	
	@Test
	public void testExternalERC() {
		EnumRepresConstruc<DummyToneB> constructor = 
				new EnumRepresConstruc<DummyToneB>(DummyToneB.class);
		DummyToneB a = constructor.newInstance("A5");
		DummyToneB b = new DummyToneB(NoteLetter.A, Octave.FIVE);
		assertTrue(a.equals(b));
	}
	
	
	//**********************************************************************************************
	//                            LACK OF ENUM-BASED CONSTRUCTOR
	//**********************************************************************************************
	
	public static class DummyToneC {
		public NoteLetter letter;
		
		private static EnumRepresConstruc<DummyToneC> a = 
				new EnumRepresConstruc<DummyToneC>(DummyToneC.class);
		
		
		public DummyToneC(Tone t) {
			this.letter = t.getNote();
		}
		
		public static DummyToneC valueOf(String repres) {
			return a.newInstance(repres);
		}
	}
		
	@Test (expected = ExceptionInInitializerError.class)
	public void detectLackOfEnumBasedConstruc() {
		DummyToneC.valueOf("%");
	}
}
