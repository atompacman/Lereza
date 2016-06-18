package com.atompacman.lereza.core.piece2;

import java.util.Optional;

import org.apache.logging.log4j.Level;

import com.atompacman.toolkat.task.TaskLogger;

public abstract class Builder<T> {

    //
    //  ~  FIELDS  ~  //
    //

    protected TaskLogger taskLogger;


    //
    //  ~  INIT  ~  //
    //

    protected Builder(Optional<TaskLogger> taskLogger) {
        this.taskLogger = taskLogger.isPresent() ? taskLogger.get() : new TaskLogger();
        this.taskLogger.setVerboseLevel(Level.TRACE);
    }


    //
    //  ~  BUILD  ~  //
    //

    protected abstract T buildImpl();

    protected abstract void reset();

    public final T build() {
        T t = buildImpl();
        taskLogger.reset();
        reset();
        return t;
    }
}
