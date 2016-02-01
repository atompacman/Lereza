package com.atompacman.lereza.core.generator.advisor;

import java.util.HashMap;
import java.util.Map;

public abstract class Council<A extends Advisor> {

    //===================================== INNER TYPES ==========================================\\

    @FunctionalInterface
    protected interface AdviceGetter<A extends Advisor, T> {
        public Advice<T> getAdvice(A advisor);
    }
    
    @FunctionalInterface
    protected interface ObjToDoubleConverter<T> {
        public double convert(T obj);
    }
    
    @FunctionalInterface
    protected interface DoubleToObjConverter<T> {
        public T convert(double val);
    }
    
    
    
    //======================================= FIELDS =============================================\\

    private Map<Class<? extends A>, A> advisors;



    //======================================= METHODS ============================================\\

    //--------------------------------- PUBLIC CONSTRUCTORS --------------------------------------\\

    public Council() {
        this.advisors = new HashMap<>();
    }


    //----------------------------------- COMPILE ADVICES ----------------------------------------\\
    
    protected <T> T compileAdvices(AdviceGetter<A,T>       adviceGetter,
                                   ObjToDoubleConverter<T> objToDouble, 
                                   DoubleToObjConverter<T> doubleToObj) {
        
        double weightedMean = 0;
        double weightCumul = 0;

        for (A advisor : advisors.values()) {
            Advice<T> advice = adviceGetter.getAdvice(advisor);
            double weight = advice.getConfidence();
            weightedMean += weight * objToDouble.convert(advice.getAdvisedValue());
            weightCumul += weight;
        }
        
        return doubleToObj.convert(weightedMean / weightCumul);
    } 
    
    protected <T extends Number> double compileAdvices(AdviceGetter<A,T> adviceGetter) {
        double weightedMean = 0;
        double weightCumul = 0;

        for (A advisor : advisors.values()) {
            Advice<T> advice = adviceGetter.getAdvice(advisor);
            double weight = advice.getConfidence();
            weightedMean += weight * advice.getAdvisedValue().doubleValue();
            weightCumul += weight;
        }
        
        return weightedMean / weightCumul;
    }
    
    
    //----------------------------------------- ADD ----------------------------------------------\\

    @SuppressWarnings("unchecked")
    public void addAdvisor(A advisor) {
        Class<A> clazz = (Class<A>) advisor.getClass();
        if (advisors.put(clazz, advisor) != null) {
            throw new IllegalArgumentException("A \"" + clazz.getSimpleName() + "\" "
                    + "was already added to current " + getClass().getSimpleName());
        }
    }
}
