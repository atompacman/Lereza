package com.atompacman.lereza.core.pieceGEWD;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Arrays;
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

    ImmutableMap<Pitch, T> notes;
    Optional<T>            rest;
    
    
    //
    //  ~  INIT  ~  //
    //

    private NoteNodeSet(T rest) {
        this.notes = ImmutableMap.of();
        this.rest  = Optional.of(rest);
    }
    
    private NoteNodeSet(ImmutableMap<Pitch, T> nodes) {
        this.notes = nodes;
        this.rest  = Optional.empty();
    }
    
    public static <T extends PolyphonicNoteNode> NoteNodeSet<T> empty() {
        return new NoteNodeSet<>(ImmutableMap.of());
    }

    public static <T extends PolyphonicNoteNode> NoteNodeSet<T> of(T node) {
        return node.isRest() ? new NoteNodeSet<T>(node) : of(ImmutableSet.of(node));
    }
    
    public static <T extends PolyphonicNoteNode> NoteNodeSet<T> of(Set<T> nodes) {       
        if (nodes.size() == 1) {
            T node = nodes.iterator().next();
            if (node.isRest()) {
                return new NoteNodeSet<>(nodes.iterator().next());
            }
        }
        
        Map<Pitch, T> perPitch = new HashMap<>();
        for (T node : nodes) {
            checkArgument(!node.isRest());
            checkArgument(perPitch.put(node.getNote().get().getPitch(), node) == null);
        }
        return new NoteNodeSet<>(ImmutableMap.copyOf(perPitch));
    }
    
    
    //
    //  ~  ADD  ~  //
    //

    public static <T extends PolyphonicNoteNode> NoteNodeSet<T> addNodeToSet(T              node,
                                                                             NoteNodeSet<T> set) {
        checkArgument(!set.isRest());
        Set<T> newSet = new HashSet<T>(set.notes.values());
        checkArgument(newSet.add(node));
        return NoteNodeSet.of(newSet);
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //

    public Optional<T> getNode(Pitch pitch) {
        return Optional.ofNullable(notes.get(pitch));
    }

    public Optional<T> getRest() {
        return rest;
    }
    
    @Implement
    public Iterator<T> iterator() {
        return (isRest() ? Arrays.asList(rest.get()) : notes.values()).iterator();
    }

    
    //
    //  ~  STATE  ~  //
    //

    public boolean isRest() {
        return rest.isPresent();
    }
    
    public boolean isEmpty() {
        return numNodes() == 0;
    }
    
    public int numNodes() {
        return isRest() ? 1 : notes.size();
    }
}
