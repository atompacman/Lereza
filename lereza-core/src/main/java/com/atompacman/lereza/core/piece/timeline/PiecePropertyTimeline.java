package com.atompacman.lereza.core.piece.timeline;

import static com.google.common.base.Preconditions.checkElementIndex;

import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

abstract class PiecePropertyTimeline<T> {

    //
    //  ~  FIELDS  ~  //
    //
    
    protected final TreeMap<Integer, T> propertyChanges;
    protected final int                 pieceLengthTU;


    //
    //  ~  INIT  ~  //
    //

    protected PiecePropertyTimeline(int pieceLengthTU) {
        this.propertyChanges = new TreeMap<>();
        this.pieceLengthTU   = pieceLengthTU;
    }
    
    
    //
    //  ~  ADD  ~  //
    //

    public void addPropertyChangeAtTU(int tu, T value) {
        checkElementIndex(pieceLengthTU, tu, "Timeunit (" + tu + ") is not within piece range");
        if (propertyChanges.put(tu, value) != null) {
            throw new IllegalArgumentException("A value was already specified at timeunit " + tu);
        }
    }

    public void addAllPropertyChanges(TreeMap<Integer, T> propertyChangesTU) {
        for (Entry<Integer, T> entry : propertyChangesTU.entrySet()) {
            addPropertyChangeAtTU(entry.getKey(), entry.getValue());
        }
    }
    

    //
    //  ~  GETTERS  ~  //
    //

    public T getValueAtTimeunit(int tu) {
        checkElementIndex(pieceLengthTU, tu, "Timeunit (" + tu + ") is not within piece range");
        return propertyChanges.floorEntry(tu).getValue();
    }
    
    public Set<Integer> getTimeunits() {
        return propertyChanges.keySet();
    }
    
    
    //
    //  ~  STATE  ~  //
    //
    
    public int pieceLengthTU() {
        return pieceLengthTU;
    }
}
