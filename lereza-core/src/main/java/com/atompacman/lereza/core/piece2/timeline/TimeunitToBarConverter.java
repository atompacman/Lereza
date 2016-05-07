package com.atompacman.lereza.core.piece2.timeline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.atompacman.lereza.core.theory.TimeSignature;

import java.util.TreeMap;

public class TimeunitToBarConverter extends PiecePropertyTimeline<Integer> {
    
    //
    //  ~  FIELDS  ~  //
    //

    
    private final List<Integer> barToTu;
    

    //
    //  ~  INIT  ~  //
    //

    public static TimeunitToBarConverter of(int pieceLengthTU) {
        return of(TimeSignature.STANDARD_4_4, pieceLengthTU);
    }
    
    @SuppressWarnings("serial")
    public static TimeunitToBarConverter of(TimeSignature timeSign, int pieceLengthTU) {
        return of(new TreeMap<Integer, TimeSignature>(){{ put(0, timeSign); }}, pieceLengthTU);
    }
    
    public static TimeunitToBarConverter of(TreeMap<Integer, TimeSignature> timeSignChangesTU,
                                            int                             pieceLengthTU) {
        
        return new TimeunitToBarConverter(timeSignChangesTU, pieceLengthTU);
    }
    
    protected TimeunitToBarConverter(TreeMap<Integer, TimeSignature> timeSignChangesTU,
                                     int                             pieceLengthTU) {
        
        super(pieceLengthTU);
        
        this.barToTu = new ArrayList<>();

        if (timeSignChangesTU.isEmpty()) {
            throw new IllegalArgumentException("Expecting at least one time signature change");
        }
        
        Iterator<Entry<Integer, TimeSignature>> it = timeSignChangesTU.entrySet().iterator();
        Entry<Integer, TimeSignature> entry = it.next();
        int           tu       = entry.getKey();
        int           bar      = 0;
        TimeSignature timeSign = entry.getValue();
        
        if (entry.getKey() != 0) {
            throw new IllegalArgumentException("Expecting a time signature change at timeunit 0");

        }
        propertyChanges.put(tu, bar);
        barToTu.add(tu);
        
        while (it.hasNext()) {
            entry = it.next();
            while (tu < entry.getKey()) {
                tu += timeSign.timeunitsInABar();
                checkTimeunitRange(tu);
                propertyChanges.put(tu, ++bar);
                barToTu.add(tu);
            }
            if (tu > entry.getKey() && tu != pieceLengthTU) {
                throw new IllegalArgumentException("Time signature "
                        + "change is not at the begining of a bar");
            }
            timeSign = entry.getValue();
        }
    }
    
    
    //
    //  ~  CONVERT  ~  //
    //

    public int convertTuToBar(int tu) {
        return getValueAtTimeunit(tu);
    }
    
    public int convertBarToTu(int bar) {
        if (bar < 0 || bar >= propertyChanges.size()) {
            throw new IllegalArgumentException("Bar (" + bar + ") is not within piece range");
        }
        return barToTu.get(bar);
    }
    
    
    //
    //  ~  STATE  ~  //
    //

    public boolean isABarBeginning(int tu) {
        return propertyChanges.get(tu) != null;
    }

    public int getBarLengthTU(int tu) {
        return getBarLengthTUFromBar(convertTuToBar(tu));
    }
    
    public int getBarLengthTUFromBar(int bar) {
        if (bar == barToTu.size() - 1) {
            return pieceLengthTU - barToTu.get(bar);
        } else {
            return barToTu.get(bar + 1) - barToTu.get(bar);
        }
    }
    
    public int getNumBars() {
        return barToTu.size();
    }
}
