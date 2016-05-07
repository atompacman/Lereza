package com.atompacman.lereza.core.piece2;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.toolkat.annotations.Implement;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public final class NoteNodeSet<T extends PolyphonicNoteNode> implements Iterable<T> {

    //
    //  ~  FIELDS  ~  //
    //

    protected ImmutableMap<Pitch, T> nodes;
    
    
    //
    //  ~  INIT  ~  //
    //

    private NoteNodeSet(ImmutableMap<Pitch, T> nodes) {
        this.nodes = nodes;
    }
    
    public static <T extends PolyphonicNoteNode> NoteNodeSet<T> of() {
        return new NoteNodeSet<>(ImmutableMap.of());
    }

    public static <T extends PolyphonicNoteNode> NoteNodeSet<T> of(T node) {
        return of(ImmutableSet.of(node));
    }
    
    public static <T extends PolyphonicNoteNode> NoteNodeSet<T> of(Set<T> nodes) {
        Map<Pitch, T> perPitch = new HashMap<>();
        for (T node : nodes) {
            checkArgument(node.getNote().isPresent());
            checkArgument(perPitch.put(node.getNote().get().getPitch(), node) == null);
        }
        return new NoteNodeSet<>(ImmutableMap.copyOf(perPitch));
    }
    
    
    //
    //  ~  ADD  ~  //
    //

    public static <T extends PolyphonicNoteNode> NoteNodeSet<T> addNodeToSet(T              node,
                                                                             NoteNodeSet<T> set){
        Set<T> newSet = new HashSet<T>(set.nodes.values());
        checkArgument(newSet.add(node));
        return NoteNodeSet.of(newSet);
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //

    public Optional<T> getNode(Pitch pitch) {
        return Optional.ofNullable(nodes.get(pitch));
    }

    @Implement
    public Iterator<T> iterator() {
        return nodes.values().iterator();
    }

    
    //
    //  ~  STATUS  ~  //
    //

    public boolean isEmpty() {
        return nodes.isEmpty();
    }
    
    public int numNodes() {
        return nodes.size();
    }
}
