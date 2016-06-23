package com.atompacman.lereza.core.piece;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.atompacman.lereza.core.piece.PolyphonicNoteNode.TiedNoteStatus;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.lereza.core.theory.RhythmValue;
import com.atompacman.lereza.core.theory.TimeSignature;
import com.atompacman.toolkat.test.AbstractTest;

public final class TestBarBuilder extends AbstractTest {

    //
    //  ~  LIMIT CASES  ~  //
    //
    
    @Test
    public void LimitCase_Build_NoEntries_EmptyBar() {
        TimeSignature ts = TimeSignature.STANDARD_4_4;
        BarBuilder builder = BarBuilder.of(ts);
        
        PolyphonicBar output = builder.build();
        
        assertTrue(output instanceof MonophonicBar);
        MonophonicBar bar = (MonophonicBar) output;
        
        assertEquals(ts.timeunitsInABar(), bar.timeunitLength());
        
        for (int i = 0; i < bar.timeunitLength(); ++i) {
            MonophonicBarSlice slice = bar.getSlice(i);
            
            // Slice must only contain a rest
            assertTrue(slice.isRest());
            
            // Only first slice should have the beginning whole rest node
            assertEquals(slice.getBeginningNodes().numNodes() == 0, i != 0);
        }
    }
    
    //
    //  ~  SCENARII  ~  //
    //

    @Test
    public void Scenarii_Monophonic_ValidArgs_ValidReturnValue() throws Exception {
        // Parse scenario file
        Deque<String> lines = new LinkedList<>(FileUtils.readLines(loadResource()));
        
        // For each scenario
        while (!lines.isEmpty()) {
            // Create builder
            String testName = lines.pop();
            String timeSig = lines.pop();
            String[] nums = timeSig.split(" ");
            TimeSignature sign = TimeSignature.of(Integer.parseInt(nums[0]), 
                                                  Integer.parseInt(nums[1]));

            BarBuilder builder = BarBuilder.of(sign);
            
            // Parse note entries
            int currTU = 0;
            String line;
            Pitch[] tu2Pitch = new Pitch[sign.timeunitsInABar()];
            List<RhythmValue> separatedValueSeq = new LinkedList<>();
            List<Integer> mergedValueSeq = new LinkedList<>();
            
            while (!lines.isEmpty() && !(line = lines.pop()).isEmpty()) {
                // Parse rest
                int restLen = Integer.parseInt(line);
                if (restLen != 0) {
                    mergedValueSeq.add(restLen);
                }
                
                // Save rest info
                for (RhythmValue val : PartBuilder.splitIntoRhythmValues(currTU, currTU + restLen)){
                    separatedValueSeq.add(val);
                    for (int i = 0; i < val.toTimeunit(); ++i) {
                        tu2Pitch[currTU++] = null;
                    }
                }
                
                // Parse note
                String[] parts = lines.pop().split(" ");
                Pitch pitch = Pitch.of(parts[0]);
                int noteLen = Integer.parseInt(parts[1]);
                if (noteLen != 0) {
                    mergedValueSeq.add(noteLen);
                }
                
                // Add entry to builder
                builder.add(pitch, Byte.MAX_VALUE, currTU, noteLen);
                
                // Save note info
                for (RhythmValue val : PartBuilder.splitIntoRhythmValues(currTU, currTU + noteLen)){
                    separatedValueSeq.add(val);
                    for (int i = 0; i < val.toTimeunit(); ++i) {
                        tu2Pitch[currTU++] = pitch;
                    }
                }
            }
            
            // Save last rest info
            if (sign.timeunitsInABar() - currTU != 0) {
                mergedValueSeq.add(sign.timeunitsInABar() - currTU);
            }
            for (RhythmValue val: PartBuilder.splitIntoRhythmValues(currTU,sign.timeunitsInABar())){
                separatedValueSeq.add(val);
                for (int i = 0; i < val.toTimeunit(); ++i) {
                    tu2Pitch[currTU++] = null;
                }
            }
            
            // Build bar
            MonophonicBar bar;
            try {
                PolyphonicBar output = builder.build();
                assertTrue(output instanceof MonophonicBar);
                bar = (MonophonicBar) output;
            } catch (Throwable e) {
                throw new Exception("Failed scenario \"" + testName + "\"", e);
            }

            // Global assertions
            assertTrue(bar.hasPlayingNote());
            assertTrue(bar.hasBeginningNote());
            assertEquals(sign.timeunitsInABar(), bar.timeunitLength());
            
            // Check slices
            for (int i = 0; i < bar.timeunitLength(); ++i) {
                MonophonicBarSlice slice = bar.getSlice(i);
                MonophonicNoteNode node = slice.getNode();
                if (tu2Pitch[i] == null) {
                    assertTrue(slice.isRest());
                    assertTrue(node.isRest());
                } else {
                    assertTrue(!node.isRest());
                    assertEquals(tu2Pitch[i], node.getNote().get().getPitch());
                }
            }
            
            // Check neighborhoods
            Optional<MonophonicNoteNode> node = bar.getFirstNode(true);
            Optional<MonophonicNoteNode> last = Optional.empty();
            int i = 0;
            while (node.isPresent()) {
                last = node;
                assertEquals(separatedValueSeq.get(i++), node.get().getRhythmValue());
                node = node.get().getNoteNeighbourhood(TiedNoteStatus.AS_SEPARATE_NOTES)
                                 .getNextNode();
            }
            while (last.isPresent()) {
                assertEquals(separatedValueSeq.get(--i), last.get().getRhythmValue());
                last = last.get().getNoteNeighbourhood(TiedNoteStatus.AS_SEPARATE_NOTES)
                                 .getPreviousNode();
            }
            
            node = bar.getFirstNode(false);
            while (node.isPresent()) {
                last = node;
                assertEquals((int)mergedValueSeq.get(i++),node.get().totalTiedNoteTimeunitLength());
                node = node.get().getNoteNeighbourhood(TiedNoteStatus.MERGE_TOGETHER)
                                 .getNextNode();
            }
            while (last.isPresent()) {
                assertEquals((int)mergedValueSeq.get(--i), last.get().totalTiedNoteTimeunitLength());
                last = last.get().getNoteNeighbourhood(TiedNoteStatus.MERGE_TOGETHER)
                                 .getPreviousNode();
            }
        }
    }
    
    // TODO MOVE OUTTA HERE
    /**
    @Test
    public void Scenario_StandardMonophonic_ValidArgs_ValidReturnValue() throws IOException {
        TimeSignature timeSign = TimeSignature.of(3, 4);

        BarBuilder builder = BarBuilder.of(timeSign);
        
        builder.velocity((byte) 50)
               .pos(0)
               .length(RhythmValue.EIGHTH.toTimeunit())
               .add(Pitch.of("Bb4"))
               
               .pos(RhythmValue.EIGHTH.toTimeunit())
               .length(RhythmValue.QUARTER.toTimeunit())
               .add(Pitch.of("D#5"))
               
               .pos(RhythmValue.HALF.toTimeunit())
               .length(RhythmValue.EIGHTH.toTimeunit())
               .add(Pitch.of("Ab4"))
               
               .pos(RhythmValue.HALF.toTimeunit() + RhythmValue.EIGHTH.toTimeunit())
               .length(RhythmValue.EIGHTH.toTimeunit())
               .add(Pitch.of("G4"));
        
        PolyphonicBar output = builder.build();
        assertTrue(output instanceof MonophonicBar);
        MonophonicBar bar = (MonophonicBar) output;
        
        // TODO remove
        Optional<MonophonicNoteNode> node = bar.getFirstNode(true);
        Optional<MonophonicNoteNode> last = Optional.empty();
        while (node.isPresent()) {
            last = node;
            System.out.println(node.get().toStaccato());
            node = node.get().getNoteNeighbourhood(TiedNoteStatus.AS_SEPARATE_NOTES).getNextNode();
        }
        while (last.isPresent()) {
            System.out.println(last.get().toStaccato());
            last = last.get().getNoteNeighbourhood(TiedNoteStatus.AS_SEPARATE_NOTES).getPreviousNode();
        }
        
        System.out.println();
        
        node = bar.getFirstNode(false);
        while (node.isPresent()) {
            System.out.println(node.get().toStaccato());
            node = node.get().getNoteNeighbourhood(TiedNoteStatus.MERGE_TOGETHER).getNextNode();
        }
        assertEqualsGroundTruth(output.toStaccato(), new File("yolo.mid"));
    }
    
    private static void assertEqualsGroundTruth(String staccato, File path) throws IOException {
        byte[] groundTruth = FileUtils.readFileToByteArray(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(2000);
        StaccatoUtils.writeToOutput(staccato, baos);
        baos.flush();
        assertArrayEquals(groundTruth, baos.toByteArray());
    }
    */
    int lol;
}
