package com.atompacman.lereza.kpf;

import org.junit.Test;

import com.atompacman.toolkat.test.TextInputBasedTest;

public class TestKeyPathFinder {

    //====================================== CONSTANTS ===========================================\\

    private static final String TEST_LIST_FILE = "tests.txt";
    
    
    
    //================================== FUNCTIONNAL TESTS =======================================\\
    
    @Test
    public void testKeyPathFinder() {
        TextInputBasedTest.launchTestsWithExpectedOutput(TEST_LIST_FILE, 
                s -> "");
    }
}
