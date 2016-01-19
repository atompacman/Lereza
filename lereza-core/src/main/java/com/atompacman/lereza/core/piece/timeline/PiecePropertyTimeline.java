package com.atompacman.lereza.core.piece.timeline;

import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class PiecePropertyTimeline<T> {

    //======================================= FIELDS =============================================\\

    protected final TreeMap<Integer, T> propertyChanges;
    protected final int                 pieceLengthTU;
    
    
    
    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public PiecePropertyTimeline(int pieceLengthTU) {
        this.propertyChanges = new TreeMap<>();
        this.pieceLengthTU   = pieceLengthTU;
    }
    
    public PiecePropertyTimeline(TreeMap<Integer, T> propertyChangesTU, int pieceLengthTU) {
        this(pieceLengthTU);
        addAllPropertyChanges(propertyChangesTU);
    }
    
    
    //----------------------------------------- ADD ----------------------------------------------\\

    public void addPropertyChangeAtTU(int tu, T value) {
        checkTimeunitRange(tu);
        if (propertyChanges.put(tu, value) != null) {
            throw new IllegalArgumentException("A value was already specified at timeunit " + tu);
        }
    }

    protected void checkTimeunitRange(int tu) {
        if (tu < 0 || tu >= pieceLengthTU) {
            throw new IllegalArgumentException("Timeunit (" + tu + ") is not within piece range");
        }
    }
    
    public void addAllPropertyChanges(TreeMap<Integer, T> propertyChangesTU) {
        for (Entry<Integer, T> entry : propertyChangesTU.entrySet()) {
            addPropertyChangeAtTU(entry.getKey(), entry.getValue());
        }
    }
    
    
    //--------------------------------------- GETTERS --------------------------------------------\\

    public T getValueAtTimeunit(int tu) {
        checkTimeunitRange(tu);
        return propertyChanges.floorEntry(tu).getValue();
    }
    
    public Set<Integer> getTimeunits() {
        return propertyChanges.keySet();
    }
}
