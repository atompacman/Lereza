package com.atompacman.lereza.core.generator.advisor;

import java.time.Duration;
import java.util.List;

import com.atompacman.lereza.core.piece.Note;

public abstract class PhraseLayerAdvisor extends Advisor {

    //=================================== ABSTRACT METHODS =======================================\\

    //--------------------------------------- ADVICE ---------------------------------------------\\

    public abstract Advice<Duration> adviceNextNote(List<Note> prevNotes);
}
