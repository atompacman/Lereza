package com.atompacman.lereza.core.analysis;

public abstract class MusicalStructure implements AnalysisComponent {

    //
    //  ~  STATIC FIELDS  ~  //
    //
    
    private static long currId = 0;
    
    
    //
    //  ~  FIELDS ~  //
    //
    
    private long id;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    protected MusicalStructure() {
        this.id = currId++;
    }
    
    
    //
    //  ~  GETTERS ~  //
    //
    
    public long getID() {
        return id;
    }


    //
    //  ~  EQUALS  ~  //
    //
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MusicalStructure other = (MusicalStructure) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
