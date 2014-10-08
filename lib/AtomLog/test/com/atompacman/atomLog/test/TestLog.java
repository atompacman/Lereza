package com.atompacman.atomLog.test;

import org.junit.Test;

import com.atompacman.atomLog.Log;
import com.atompacman.atomLog.Log.Verbose;

public class TestLog {

	@Test
	public void testLog() {
		Log.setVerbose(Verbose.INFOS);
		
		if(Log.infos() && Log.print("<- Must be infos"));
		
		if(Log.warng() && Log.title(4));
		
		if(Log.vital() && Log.title("GOGLU", 4));
		
		if(Log.extra() && Log.print("MUST NOT PRINT"));
	}
}
