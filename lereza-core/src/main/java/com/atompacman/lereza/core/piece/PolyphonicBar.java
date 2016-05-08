package com.atompacman.lereza.core.piece;

import static com.google.common.base.Preconditions.checkElementIndex;

import java.util.List;

import com.atompacman.lereza.core.theory.RythmnValue;
import com.atompacman.toolkat.annotations.DerivableFrom;
import com.google.common.collect.ImmutableList;

public class PolyphonicBar implements MusicalStructure {

    //
    //  ~  FIELDS  ~  //
    //

    protected final ImmutableList<PolyphonicBarSlice> slices;

    @DerivableFrom("slices")
    private final boolean hasPlayingNote;
    @DerivableFrom("slices")
    private final boolean hasBeginningNote;

    
    //
    //  ~  INIT  ~  //
    //

    PolyphonicBar(List<PolyphonicBarSlice> slices) {
        this.slices = ImmutableList.copyOf(slices);

        this.hasPlayingNote   = slices.stream().anyMatch(slice -> !slice.isRest());
        this.hasBeginningNote = slices.stream().anyMatch(slice -> slice.hasBeginningNodes());;
    }


    //
    //  ~  GETTERS  ~  //
    //

    public PolyphonicBarSlice getSlice(int timeunit) {
        checkElementIndex(timeunit, slices.size(), "Cannot access bar slices at timeunit "
                + "\"" + timeunit + "\": bar is only " + slices.size() + " timunits long");
        return slices.get(timeunit);
    }
    
    
    //
    //  ~  STATE  ~  //
    //

    public boolean hasPlayingNote() {
        return hasPlayingNote;
    }

    public boolean hasBeginningNote() {
        return hasBeginningNote;
    }

    public int timeunitLength() {
        return slices.size();
    }


    //
    //  ~  SERIALIZATION  ~  //
    //

    public String toStaccato() {
        StringBuilder sb = new StringBuilder();
        int rests = 0;

        for (PolyphonicBarSlice slice : slices) {
            if (slice.hasBeginningNodes()) {
                if (rests > 0) {
                    sb.append('R');
                    for (RythmnValue value : RythmnValue.splitIntoValues(rests)) {
                        sb.append(value.toStaccato());
                    }
                    sb.append(' ');
                }
                sb.append(slice.toStaccato()).append(' ');
                rests = 0;

            } else if (!slice.hasBeginningNodes()){
                ++rests;
            }
        }
        return sb.toString();
    }
}
