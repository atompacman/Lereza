/**
 * <h1> Musical structure representations </h1>
 * 
 * <h2> A two-dimensional class hierarchy </h2>
 * 
 * Musical structure classes are all immutable (and hence, created via builder classes) and 
 * implement the {@link com.atompacman.lereza.core.piece2.MusicalStructure MusicalStructure} 
 * interface. Those structures follow a 2D hierarchy where the first axis is the {@link com.
 * atompacman.lereza.core.piece2.StructuralHierarchyRank structural hierarchy} and the second axis 
 * is the {@link com.atompacman.lereza.core.piece2.ComplexityHierarchyRank complexity hierarchy}. 
 * The relationship between classes on the first axis is composition (a part is made of bars) and on 
 * the second axis is inheritance (a  <i> monophonic </i> structure is also <i> homophonic</i>). 
 * Classes are named so that the prefix is the position in the <i> complexity hierarchy</i> 
 * (<code>Polyphonic*</code>) and the suffix is the position in the <i> structural hierarchy</i> 
 * (<code>*Bar</code>).
 * 
 * <h2> Complexity hierarchy </h2>
 * At the heart of every musical structure is the concept of interconnected {@link com.atompacman.
 * lereza.core.theory.Note Notes}. Notes can have many different {@link com.atompacman.lereza.core.
 * piece2.AbstractNoteNode.TemporalRelationship temporal relationships} between them, and which 
 * type of relationship is allowed in a certain structure depends on its rank in the <i> complexity 
 * hierarchy</i> :
 * <p>
 * 
 * <ul>
 * <li>
 * {@link com.atompacman.lereza.core.piece2.ComplexityHierarchyRank#POLYPHONIC Polyphonic} <br> 
 * <i> Any number </i> of notes can be playing simultaneously (ex.: piano, harp). <br> 
 * <pre>    ↑  <br> extends </pre>
 * </li>
 * <li>
 * {@link com.atompacman.lereza.core.piece2.ComplexityHierarchyRank#HOMOPHONIC Homophonic} <br> 
 * <i> Any number </i> of notes can be playing simultaneously, but groups of simultaneous notes 
 * must <i> always </i> start and end simultaneously (ex.: playing straight chords produces music 
 * that is "homophonic" in this sense - which is not really what homophonic means in music theory).
 * <pre>    ↑  <br> extends </pre>
 * </li>
 * <li>
 * {@link com.atompacman.lereza.core.piece2.ComplexityHierarchyRank#MONOPHONIC Monophonic} <br> 
 * <i> At most </i> one note can be playing at all time (ex.: trumpet, human voice).
 * </li>
 * </ul>
 *
 * To better understand why inheritance works in this direction, one must think of these complexity 
 * levels in terms of programming interfaces rather than musical textures. For example, polyphonic 
 * musical structures are definitely <i> not </i> monophonic API-wise because we expect to get an
 * unlimited number of notes playing at any time, whereas with monophonic structures, at most one 
 * note is possible at any time. The problem can be solved the other way around because a monophonic 
 * structure that is treated as polyphonic can simply return a list containing a single note.
 * <p>
 * 
 * Classes that are lower in this hierarchy are more efficient and easier to use, because they 
 * define "shortcut" methods that are more restrictive and therefore reduce the amount of work 
 * needed to manipulate them.
 * 
 * <h2> Structural hierarchy </h2>
 * Not all levels in the <i> structural hierarchy </i> have the same inheritance complexity. The
 * {@link com.atompacman.lereza.core.piece2.Piece Piece} class "family" has no inheritance at all,
 * some families have a linear class hierarchy with three levels (one class per <i> complexity 
 * level</i>) and some have a more complex inheritance trees with 7 classes in them (but with only
 * three public interfaces) .
 * <p>
 * 
 * <ul>
 * <li>
 * {@link com.atompacman.lereza.core.piece2.Piece Piece} <br>
 * Simple, unpolymorphic top-level class that simply as an iterable list of {@link com.atompacman.
 * lereza.core.piece2.PolyphonicPart parts}. Those can be of any <i> complexity </i>.
 * </li><br>
 * <li>
 * {@link com.atompacman.lereza.core.piece2.StructuralHierarchyRank#PART Part} <br>
 * Three-level linear hierarchy of classes that acts as lists of {@link com.atompacman.lereza.core.
 * piece2.PolyphonicBar bars}. Those are <i> always of the same complexity level </i> as their 
 * parent part.
 * </li><br>
 * <li>
 * {@link com.atompacman.lereza.core.piece2.StructuralHierarchyRank#BAR Bar} <br>
 * Three-level linear hierarchy of classes that acts as lists of {@link com.atompacman.lereza.core.
 * piece2.PolyphonicBarSlice bar slices}. Those are <i> always of the same complexity level </i> as 
 * their parent bar.
 * </li><br>
 * <li>
 * {@link com.atompacman.lereza.core.piece2.StructuralHierarchyRank#BAR_SLICE BarSlice} <br>
 * Complex 7-member hierarchy of classes that contains the {@link com.atompacman.lereza.core.piece2.
 *com.atompacman.lereza.core.piece2.NoteNodeSet sets} of {@link com.atompacman.lereza.core.piece2.PolyphonicNoteNode note nodes} that 
 * contains the notes that are playing at a specific time. Those nodes are <i> always of the same 
 * complexity level </i> as their parent bar slice.
 * </li><br>
 * <li>
 * {@link com.atompacman.lereza.core.piece2.StructuralHierarchyRank#NOTE_NODE NoteNode} <br>
 * Complex 7-member hierarchy of classes that represents either a note or a rest in a graph of nodes 
 * linked together via various {@link com.atompacman.lereza.core.piece2.AbstractNoteNode.
 * TemporalRelationship temporal relationships}. This is where the <i> complexity hierarchy</i> 
 * actually matters: Possible relationships are restricted the lower you go in the hierarchy. 
 * </li><br>
 * </ul>
 * 
 * <h2> Builders </h2>
 * Since all those 21 data classes are immutable, builder classes must be used to instantiate them.
 * There is one builder class per <i> structural level</i>. When building, builders are responsible 
 * for choosing the right <i> complexity level</i> depending on the input it received (it always 
 * aims for the lowest level possible in the hierarchy).
 */
package com.atompacman.lereza.core.piece2;