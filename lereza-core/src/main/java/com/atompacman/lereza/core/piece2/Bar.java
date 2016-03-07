package com.atompacman.lereza.core.piece2;

import static com.google.common.base.Preconditions.checkElementIndex;

import java.util.List;

import com.atompacman.lereza.core.theory.RythmnValue;
import com.google.common.collect.ImmutableList;

abstract class Bar<T extends BarSlice<?>> {

    //
    //  ~  FIELDS  ~  //
    //

    private final ImmutableList<T> slices;


    //
    //  ~  INIT  ~  //
    //

    protected Bar(List<T> slices) {
        this.slices = ImmutableList.copyOf(slices);
    }


    //
    //  ~  GETTERS  ~  //
    //

    public T getSlice(int timeunit) {
        checkElementIndex(timeunit, slices.size(), "Cannot access bar slices at timeunit "
                + "\"" + timeunit + "\": bar is only " + slices.size() + " timunits long");
        return slices.get(timeunit);
    }


    //
    //  ~  STATE  ~  //
    //

    public boolean hasPlayingNote() {
        for (T slice : slices) {
            if (!slice.isRest()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasBeginningNote() {
        for (T slice : slices) {
            if (slice.hasBeginningNote()) {
                return true;
            }
        }
        return false;
    }

    public int getTimeunitLength() {
        return slices.size();
    }


    //
    //  ~  SERIALIZATION  ~  //
    //

    public String toStaccato() {
        StringBuilder sb = new StringBuilder();
        int rests = 0;

        for (T slice : slices) {
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
