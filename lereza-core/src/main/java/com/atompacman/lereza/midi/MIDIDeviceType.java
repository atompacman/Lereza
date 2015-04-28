package com.atompacman.lereza.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

import com.atompacman.configuana.param.Param;
import com.atompacman.lereza.Parameters.MIDI.Devices;
import com.atompacman.lereza.exception.MIDIDeviceException;
import com.atompacman.toolkat.exception.Throw;

public enum MIDIDeviceType {

	KEYBOARD 	(Devices.KEYBOARD_CONTROLLER_ID, false, true, null),
	SEQUENCER 	(Devices.SEQUENCER_ID, true, true, Sequencer.class),
	SYNTHESIZER (Devices.SYNTHESIZER_ID, true, false, null);
	
	
	
	//======================================= FIELDS =============================================\\

	private Param 						idParam;
	private boolean 					mustHaveMIDIIN;
	private boolean 					mustHaveMIDIOUT;
	private Class<? extends MidiDevice> interfaze;
	private MidiDevice 					defaultDevice;
	
	
	
	//======================================= METHODS ============================================\\

	//---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

	private MIDIDeviceType(Param idParam, boolean mustHaveMIDIIN, 
			boolean mustHaveMIDIOUT, Class<? extends MidiDevice> interfaze) {
		
		this.idParam = idParam;
		this.mustHaveMIDIIN = mustHaveMIDIIN;
		this.mustHaveMIDIOUT = mustHaveMIDIOUT;
		this.interfaze = interfaze;
		
		try {
			switch((Devices) idParam) {
			case SEQUENCER_ID: 		defaultDevice = MidiSystem.getSequencer(); 		break;
			case SYNTHESIZER_ID: 	defaultDevice = MidiSystem.getSynthesizer(); 	break;
			default:				defaultDevice = MidiSystem.getSequencer();      break;
			}
		} catch (MidiUnavailableException e) {
			Throw.aRuntime(MIDIDeviceException.class, "Error opening "
					+ "MIDI system's default " + getName());
		}
	}
	
	
	//======================================= METHODS ============================================\\

	public String getName() {
		return name().toLowerCase();
	}
	
	public Param getIDParameter() {
		return idParam;
	}
	
	public boolean mustHaveMIDIIN() {
		return mustHaveMIDIIN;
	}
	
	public boolean mustHaveMIDIOUT() {
		return mustHaveMIDIOUT;
	}
	
	public Class<? extends MidiDevice> getInterface() {
		return interfaze;
	}

	public MidiDevice getDefaultDevice() {
		return defaultDevice;
	}
}
