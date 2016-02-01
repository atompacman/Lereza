package com.atompacman.lereza.core.analysis.grouper;

import java.lang.reflect.Method;

import com.atompacman.lereza.core.analysis.AnalysisManager;
import com.atompacman.lereza.core.analysis.Analyzer;
import com.atompacman.lereza.core.analysis.SplitterMethod;
import com.atompacman.lereza.core.piece.Bar;
import com.atompacman.lereza.core.piece.NoteStack;
import com.atompacman.lereza.core.piece.Part;
import com.atompacman.lereza.core.piece.Piece;

public final class BuiltinSubstructureMethods extends Analyzer {

    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    private BuiltinSubstructureMethods(AnalysisManager manager) { 
        super(manager); 
    }
    
    
    //---------------------------------------- SPLIT ---------------------------------------------\\

    @SplitterMethod
    public Part[] split(Piece piece) {
        return (Part[]) piece.getParts().toArray();
    }
    
    @SplitterMethod
    public Bar[] split(Part part) {
        return (Bar[]) part.getBars().toArray();
    }
    
    @SplitterMethod
    public NoteStack[] split(Bar bar) {
        return (NoteStack[]) bar.getNoteStacks().toArray();
    }
    
    public static void main(String[] args) throws NoSuchMethodException, SecurityException {
        Method m = BuiltinSubstructureMethods.class.getMethod("split", Bar.class);
        System.out.println(m.getReturnType().getComponentType());
    }
}
