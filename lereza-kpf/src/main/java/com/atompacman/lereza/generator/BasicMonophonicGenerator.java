package com.atompacman.lereza.generator;

import com.atompacman.lereza.core.piece.Note;
import com.atompacman.lereza.core.piece.Piece;
import com.atompacman.lereza.core.piece.PieceBuilder;
import com.atompacman.lereza.core.piece.Stack;
import com.atompacman.lereza.core.solfege.RythmicSignature;

public class BasicMonophonicGenerator {

    public Piece<Stack<Note>> generate() {
        PieceBuilder pieceBuilder = new PieceBuilder(RythmicSignature.STANDARD_4_4);
        
    }
}
