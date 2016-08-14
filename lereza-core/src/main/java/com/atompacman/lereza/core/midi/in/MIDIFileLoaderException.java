package com.atompacman.lereza.core.midi.in;

@SuppressWarnings("serial")
public final class MIDIFileLoaderException extends Exception {

    //
    //  ~  INIT  ~  //
    //

    MIDIFileLoaderException() {
        super();
    }

    MIDIFileLoaderException(String message) {
        super(message);
    }

    MIDIFileLoaderException(String message, Throwable cause) {
        super(message, cause);
    }

    MIDIFileLoaderException(Throwable cause) {
        super(cause);
    }
}