package com.atompacman.lereza.core.pieceGEWD;

import java.util.List;
import java.util.Optional;

public final class MonophonicBar extends HomophonicBar {

    //
    //  ~  INIT  ~  //
    //

    @SuppressWarnings({ "unchecked", "rawtypes" })
    MonophonicBar(List<MonophonicBarSlice> slices) {
        super((List) slices);
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //

    @Override
    public MonophonicBarSlice getSlice(int timeunit) {
        return (MonophonicBarSlice) super.getSlice(timeunit);
    }
    
    public Optional<MonophonicNoteNode> getFirstNode(boolean doAcceptTiedNotes) {
        for (int i = 0; i < timeunitLength(); ++i) {
            MonophonicBarSlice slice = getSlice(i);
            if (slice.hasBeginningNodes()) {
                MonophonicNoteNode node = slice.getNode();
                if (!(!doAcceptTiedNotes && node.isTied())) {
                    return Optional.of(node);
                }
            }
        }
        return Optional.empty();
    }
    
    
    //
    //  ~  SERIALIZATION  ~  //
    //

    @Override
    public String toStaccato() {
        StringBuilder sb = new StringBuilder();
        slices.stream().filter (e -> e.hasBeginningNodes())
                       .forEach(e -> sb.append(e.toStaccato()).append(' '));
        return sb.toString();
    }
}
