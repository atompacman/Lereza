package com.atompacman.lereza.core.piece2;

import static com.google.common.base.Preconditions.checkElementIndex;

import java.util.List;

import com.atompacman.lereza.core.theory.RythmnValue;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class Bar<T extends NoteNode<T>> {

    //
    //  ~  FIELDS  ~  //
    //

    public abstract ImmutableList<BarSlice<T>> getSlices();
    
    
    //
    //  ~  INIT  ~  //
    //

    Bar<T> of(List<BarSlice<T>> slices) {
        return new AutoValue_Bar<T>(ImmutableList.copyOf(slices));
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //

    public BarSlice<T> getSlice(int timeunit) {
        ImmutableList<BarSlice<T>> slices = getSlices();
        checkElementIndex(timeunit, slices.size(), "Cannot access bar slices at "
                + "timeunit \"" + timeunit + "\": bar is only " + slices.size() + " timunits long");
        return slices.get(timeunit);
    }
    
    
    //
    //  ~  STATE  ~  //
    //

    public boolean hasPlayingNote() {
        for (BarSlice<T> slice : getSlices()) {
            if (!slice.isRest()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasBeginningNote() {
        for (BarSlice<T> slice : getSlices()) {
            if (slice.hasBeginningNote()) {
                return true;
            }
        }
        return false;
    }
    
    public int getTimeunitLength() {
        return getSlices().size();
    }
    
    
    //
    //  ~  SERIALIZATION  ~  //
    //

    public String toStaccato() {
        StringBuilder sb = new StringBuilder();
        int rests = 0;
        
        for (BarSlice<T> slice : getSlices()) {
            if (slice.hasBeginningNote()) {
                if (rests > 0) {
                    sb.append('R');
                    for (RythmnValue value : RythmnValue.splitIntoValues(rests)) {
                        sb.append(value.toStaccato());
                    }
                    sb.append(' ');
                }
                sb.append(slice.toStaccato()).append(' ');
                rests = 0;

            } else if (!slice.hasBeginningNote()){
                ++rests;
            }
        }
        return sb.toString();
    }
}
