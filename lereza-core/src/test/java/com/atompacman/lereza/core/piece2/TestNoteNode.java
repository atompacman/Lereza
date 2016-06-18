package com.atompacman.lereza.core.piece2;

import org.junit.Test;

import com.atompacman.lereza.core.piece2.AbstractNoteNode.TemporalRelationship;
import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.RythmnValue;
import com.google.common.collect.ImmutableSet;

public final class TestNoteNode {

    @Test
    public void lol() {
        GraphNoteNode a = new GraphNoteNode(Note.of("C#3", RythmnValue.QUARTER));
        GraphNoteNode b = new GraphNoteNode(Note.of("Db3", RythmnValue.HALF));
        a.connectTo(TemporalRelationship.RIGHT_AFTER, b);
        
        ImmutableSet<HomophonicNoteNode> nbb = a.getNodesEndingAfter();
        
        NoteNode pnn = a;
        nbb = pnn.getNodesEndingAfter();
        
        AbstractNoteNode<GraphNoteNode> nn = a;
        nbb = nn.getNodesEndingAfter();
        
        HomophonicNoteNode hbn = nbb.iterator().next();
        hbn.getClass();
    }
}
