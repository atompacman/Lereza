package com.atompacman.lereza.generator.advisor;

public class Advice<T> {

    //======================================= FIELDS =============================================\\

    private final T      value;
    private final double confidence;



    //======================================= METHODS ============================================\\

    //--------------------------------- PUBLIC CONSTRUCTORS --------------------------------------\\

    public Advice(T value) {
        this.value      = value;
        this.confidence = 1.0;
    }
    
    public Advice(T value, double confidence) {
        this.value      = value;
        this.confidence = confidence;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public T getAdvisedValue() {
        return value;
    }

    public double getConfidence() {
        return confidence;
    }
}
