package com.atompacman.lereza.core.midi.realtime;

import java.util.LinkedHashSet;
import java.util.Set;

import com.atompacman.lereza.core.theory.Semitones;
import com.atompacman.lereza.core.theory.Tone;

public abstract class PlayingTonesListener extends PressedKeysListener {

    //=================================== ABSTRACT METHODS =======================================\\

    //--------------------------------------- UPDATE ---------------------------------------------\\

    public abstract void update(Set<Tone> tones);
    
    
    
    //======================================= METHODS ============================================\\

    //--------------------------------------- UPDATE ---------------------------------------------\\

    protected void update(boolean[] heldKeys) {
        Set<Tone> tones = new LinkedHashSet<>();
        for (int i = 0; i < Semitones.IN_OCTAVE; ++i) {
            for (int j = 0; j < heldKeys.length - Semitones.IN_OCTAVE; j += Semitones.IN_OCTAVE) {
                if (heldKeys[j+i]) {
                    tones.add(Tone.thatIsMoreCommonForSemitoneValue(i));
                    break;
                }
            }
        }
        update(tones);
    }
}
