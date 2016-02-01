package com.atompacman.lereza.core.analysis.grouper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.atompacman.lereza.core.piece.Part;

public final class IndividualPartsGrouper implements PartGrouper {

    public List<List<Part>> group(List<Part> parts) {
        List<List<Part>> groups = new LinkedList<>();
        for (Part part : parts) {
            groups.add(Arrays.asList(part));
        }
        return groups;
    }

    public String getPartGroupingName() {
        return "Individual parts";
    }
}
