package com.atompacman.lereza.pluggin.builtin.interval;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.core.theory.Degree;
import com.atompacman.lereza.pluggin.builtin.interval.IntervalSequenceDistribution;
import com.atompacman.toolkat.test.AbstractTest;

public class TestIntervalSequenceDistribution extends AbstractTest {

    //====================================== DEMO TESTS ==========================================\\

    @Test
    public void demoTest() {
        IntervalSequenceDistribution isd = new IntervalSequenceDistribution(2);
        
        LinkedList<Degree> list = new LinkedList<>();
        
        list.add(Degree.of("I"));
        list.add(Degree.of("ii"));
        isd.addIntervalSequence(list);
        list.clear();
        
        list.add(Degree.of("iii"));
        list.add(Degree.of("IV"));
        isd.addIntervalSequence(list);
        list.clear();
        
        list.add(Degree.of("I"));
        list.add(Degree.of("iii"));
        isd.addIntervalSequence(list);
        list.clear();
        
        list.add(Degree.of("iii"));
        list.add(Degree.of("IV"));
        isd.addIntervalSequence(list);
        list.clear();
        
        // Assertions
        assertEquals(4, isd.getTotalNumIntervals());
        assertEquals(1, isd.getNumOccurrences(createList("I", "ii")));
        assertEquals(1, isd.getNumOccurrences(createList("I", "iii")));
        assertEquals(2, isd.getNumOccurrences(createList("iii", "IV")));
        assertEquals(2, isd.getNumOccurrences(createList("I")));
        assertEquals(0, isd.getNumOccurrences(createList("ii", "iii")));
    }
    
    
    //================================== INCORRECT USE CASES =====================================\\

    @Test
    public void cannotCreateADistributionOfSequenceOfLengthLessThanTwo() {
        expect("Interval sequence length must be greater than 2");
        new IntervalSequenceDistribution(1);
    }
    
    @Test
    public void cannotAddSequenceOfLengthDifferentThanThatOfDistribution() {
        expect("Interval sequence must contain 7 degrees");
        new IntervalSequenceDistribution(7).addIntervalSequence(createList("ii", "VI", "I"));
    }
    
    @Test
    public void cannotGetNumOfOccurrencesOfSeqLongerThanDistribution() {
        expect("Interval sequence cannot be longer than maximum sequence (2)");
        new IntervalSequenceDistribution(2).getNumOccurrences(createList("II", "vii", "II7"));
    }
    
    
    //======================================= HELPERS ============================================\\

    private static List<Degree> createList(String...degrees) {
        LinkedList<Degree> list = new LinkedList<>();
        for (String degreeStr : degrees) {
            list.add(Degree.of(degreeStr));
        }
        return list;
    }
}
