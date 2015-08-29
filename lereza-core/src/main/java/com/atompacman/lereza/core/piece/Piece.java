package com.atompacman.lereza.core.piece;

import java.util.List;

import com.atompacman.lereza.core.midi.sequence.MIDISequence;
import com.atompacman.lereza.core.solfege.RythmicSignature;

public final class Piece<T extends Stack<?>> implements PieceComponent {

    //======================================= FIELDS =============================================\\

    private final List<Part<T>>		parts;
    private final RythmicSignature 	rythmicSign;
    private final MIDISequence 		midiSeq;



    //======================================= METHODS ============================================\\

    //---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

    Piece(List<Part<T>> parts, RythmicSignature rythmicSign, MIDISequence midiSeq) {
        this.parts = parts;
        this.rythmicSign = rythmicSign;
        this.midiSeq = midiSeq;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public Part<T> getPart(int index) {
        if (index >= parts.size()) {
            throw new IllegalArgumentException("Cannot get part num. " + 
                    index + ": Piece only has " + parts.size() + " parts.");
        }
        return parts.get(index);
    }

    public RythmicSignature getRythmicSignature() {
        return rythmicSign;
    }

    public MIDISequence getMIDISequence() {
        return midiSeq;
    }


    //---------------------------------------- STATE ---------------------------------------------\\

    public int numParts() {
        return parts.size();
    }

    public int numNotes() {
        int sum = 0;
        for (Part<T> part : parts) {
            sum += part.numNotes();
        }
        return sum;
    }

    public boolean hasAssociatedMIDISequence() {
        return midiSeq != null;
    }


    //-------------------------------------- TO STRING -------------------------------------------\\

    public String toStaccato() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.size(); ++i) {
            sb.append('V').append(i).append(' ').append(parts.get(i).toStaccato()).append(' ');
        }
        return sb.toString();
    }
}
