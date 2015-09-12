package com.atompacman.lereza.generator.advisor;

import java.util.List;
import java.time.Duration;

import com.atompacman.lereza.core.piece.Note;

public abstract class PhraseLayerAdvisor extends Advisor {

    //=================================== ABSTRACT METHODS =======================================\\

    //--------------------------------------- ADVICE ---------------------------------------------\\

    public abstract Advice<Duration> adviceNextNote(List<Note> prevNotes);
}
