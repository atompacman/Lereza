package com.atompacman.lereza.core.analysis;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.atompacman.lereza.core.solfege.Degree;

public class IntervalSequenceDistribution {

    //===================================== INNER TYPES ==========================================\\

    private static class Node {

        //===================================== FIELDS ===========================================\\

        private final Map<Degree, Node> children;
        private int num;



        //===================================== METHODS ==========================================\\

        //-------------------------------- PUBLIC CONSTRUCTOR ------------------------------------\\

        public Node() {
            this.children = new HashMap<>();
            this.num = 0;
        }


        //--------------------------------------- ADD --------------------------------------------\\

        public void addIntervalSequence(LinkedList<Degree> seq) {
            ++num;
            if (seq.isEmpty()) {
                return;
            }
            Degree degree = seq.pollFirst();
            Node child = children.get(degree);
            if (child == null) {
                child = new Node();
                children.put(degree, child);
            }
            child.addIntervalSequence(seq);
        }


        //------------------------------------- GETTERS ------------------------------------------\\

        public Node getNode(LinkedList<Degree> seq) {
            Node child = children.get(seq.pollFirst());
            return seq.isEmpty() || child == null ? child : child.getNode(seq);
        }
    }



    //======================================= FIELDS =============================================\\

    private final Node root;
    private final int  seqLen;



    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public IntervalSequenceDistribution(int seqLen) {
        if (seqLen < 2) {
            throw new IllegalArgumentException("Interval sequence length must be greater than 2");
        }
        this.root = new Node();
        this.seqLen = seqLen;
    }


    //----------------------------------------- ADD ----------------------------------------------\\

    public void addIntervalSequence(List<Degree> seq) {
        if (seq.size() != seqLen) {
            throw new IllegalArgumentException("Interval sequence"
                    + " must contain " + seqLen + " degrees");
        }
        root.addIntervalSequence(new LinkedList<Degree>(seq));
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public int getNumOccurrences(List<Degree> seq) {
        if (seq.isEmpty()) {
            throw new IllegalArgumentException("Interval sequence cannot be empty");
        }
        if (seq.size() > seqLen) {
            throw new IllegalArgumentException("Interval sequence cannot "
                    + "be longer than maximum sequence (" + seqLen + ")");
        }
        Node node = root.getNode(new LinkedList<Degree>(seq));
        return node == null ? 0 : node.num;
    }

    public int getTotalNumIntervals() {
        return root.num;
    }
}
