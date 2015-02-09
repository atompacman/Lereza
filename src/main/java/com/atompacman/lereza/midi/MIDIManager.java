package com.atompacman.lereza.midi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Sequencer.SyncMode;
import javax.sound.midi.Synthesizer;

import com.atompacman.atomlog.Log;
import com.atompacman.lereza.api.Device;
import com.atompacman.lereza.api.Wizard;
import com.atompacman.lereza.exception.MIDIDeviceException;
import com.atompacman.toolkat.exception.Throw;

public class MIDIManager implements Device {

	//====================================== SINGLETON ===========================================\\

	private static class InstanceHolder {
		private static final MIDIManager instance = new MIDIManager();
	}

	public static MIDIManager getInstance() {
		return InstanceHolder.instance;
	}



	//====================================== CONSTANTS ===========================================\\

	public static final int DEFAULT_TEMPO = -1;

	private static final int INFO_LIST_SPACING = 18;


	
	//======================================= FIELDS =============================================\\

	private Map<String, MidiDevice>			availableDevices;
	private Map<MIDIDeviceType, MidiDevice> selectedDevices;

	private boolean isRecording;
	private boolean isPlaying;
	
	

	//======================================= METHODS ============================================\\

	//---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

	private MIDIManager() {
		if (Log.infos() && Log.title("MIDI Device Manager initialization", 0));
		autoSelectDevices();
		this.isRecording = false;
		this.isPlaying = false;
	}


	//------------------------------------ SELECT DEVICES ----------------------------------------\\

	public void autoSelectDevices() {
		refreshAvailableDevices();

		if (Log.infos() && Log.title("Auto-selecting MIDI devices", 1));

		selectedDevices	 = new EnumMap<MIDIDeviceType, MidiDevice>(MIDIDeviceType.class);
		String deviceID = null;

		for (MIDIDeviceType type : MIDIDeviceType.values()) {
			try {
				deviceID = Wizard.getString(type.getIDParameter());
				MidiDevice device = availableDevices.get(deviceID);

				if (device == null) {
					if (Log.warng() && Log.print("No MIDI device called \"" + deviceID + "\" was "
							+ "found on the system. Using system's default " + type.getName()+"."));
					device = type.getDefaultDevice();
					if (device == null) {
						Throw.aRuntime(MIDIDeviceException.class, "No default MIDI "
								+ "" + type.getName() + " defined by the system");
					}
				}

				Class<? extends MidiDevice> representedInterface = type.getInterface();

				if (representedInterface != null && 
						!representedInterface.isAssignableFrom(device.getClass())) {
					Throw.aRuntime(MIDIDeviceException.class, "Device should implement \"" + 
							representedInterface.getSimpleName() + "\" to be a " + type.getName());
				}

				if (type.mustHaveMIDIIN() && device.getMaxReceivers() == 0) {
					Throw.aRuntime(MIDIDeviceException.class, 
							"Device must provide MIDI IN connections");
				}

				if (type.mustHaveMIDIOUT() && device.getMaxTransmitters() == 0) {
					Throw.aRuntime(MIDIDeviceException.class, 
							"Device must provide MIDI OUT connections");
				}

				if (!device.isOpen()) {
					try {
						device.open();
					} catch (MidiUnavailableException e) {
						Throw.aRuntime(MIDIDeviceException.class, "Error opening the device", e);
					}
				}
				if (Log.extra() && Log.print("Choosing \"" + deviceID(device.getDeviceInfo()) 
						+ "\" as " + type.getName(), 1));

				selectedDevices.put(type, device);
			} catch (MIDIDeviceException e) {
				Throw.aRuntime(MIDIDeviceException.class, "Could not open the MIDI "+ type.getName() 
						+ " pointed by the config file (ID = \"" + deviceID + "\")", e);
			}
		}

		connectDevices();
	}

	private void refreshAvailableDevices() {
		if (Log.infos() && Log.title("Loading available MIDI devices", 1));

		availableDevices = new HashMap<String, MidiDevice>();
		String deviceID = null;
		try {
			for (Info info : MidiSystem.getMidiDeviceInfo()) {
				deviceID = info.getName();
				availableDevices.put(deviceID(info), MidiSystem.getMidiDevice(info));
				if (Log.infos() && Log.print(deviceID(info) + " detected."));
			}
		} catch (MidiUnavailableException e) {
			Throw.aRuntime(MIDIDeviceException.class, "Could not "
					+ "load MIDI device \"" + deviceID + "\"", e);
		}
	}

	private void connectDevices() {
		if (Log.infos() && Log.title("Device connection", 1));
		connectDevices(MIDIDeviceType.KEYBOARD, MIDIDeviceType.SYNTHESIZER);
		connectDevices(MIDIDeviceType.KEYBOARD, MIDIDeviceType.SEQUENCER);
		connectDevices(MIDIDeviceType.SEQUENCER, MIDIDeviceType.SYNTHESIZER);
	}

	private void connectDevices(MIDIDeviceType from, MIDIDeviceType to) {
		if (Log.extra() && Log.print("Connecting " + from.getName() + " to " + to.getName() + "."));

		MidiDevice fromDevice = getDevice(from);
		MidiDevice toDevice   = getDevice(to);

		try {
			fromDevice.getTransmitter().setReceiver(toDevice.getReceiver());
		} catch (MidiUnavailableException e) {
			Throw.aRuntime(MIDIDeviceException.class, "Could not connect " + from.getName()
					+ "\"" + fromDevice.getDeviceInfo().getName() + "\" to " + to.getName()
					+ "\"" + toDevice.getDeviceInfo().getName() + "\"", e);
		}
	}

	private static String deviceID(Info deviceInfo) {
		return deviceInfo.getName() + " (" + deviceInfo.getDescription() + ")";
	}


	//--------------------------------------- SEQUENCE -------------------------------------------\\

	public void createNewSequence(int nbTracks) {
		try {
			setCurrentSequence(new Sequence(Sequence.PPQ, 64, nbTracks));
		} catch (InvalidMidiDataException e) {
			Throw.aRuntime(MIDIDeviceException.class, "Could not create a new MIDI sequence", e);
		}
	}

	public void loadSequence(String filePath) {
		try {
			setCurrentSequence(MidiSystem.getSequence(new File(filePath)));
		} catch (InvalidMidiDataException | IOException e) {
			Throw.aRuntime(MIDIDeviceException.class, "Could not load "
					+ "MIDI sequence at \"" + filePath + "\"", e);
		}
	}

	private void setCurrentSequence(Sequence sequence) {
		stopRecordingAndPlayback();
		try {
			((Sequencer) getDevice(MIDIDeviceType.SEQUENCER)).setSequence(sequence);
		} catch (InvalidMidiDataException e) {
			Throw.aRuntime(MIDIDeviceException.class, "Invalid MIDI sequence", e);
		}
	}


	//--------------------------------------- PLAYBACK -------------------------------------------\\

	public void startPlayback(int[] enabledTracks, long microsecondPos, float bpm, boolean loop) {
		Sequencer sequencer = (Sequencer) getDevice(MIDIDeviceType.SEQUENCER);
		for (int track : enabledTracks) {
			sequencer.setTrackSolo(track, true);
		}
		sequencer.setMicrosecondPosition(microsecondPos);
		if (bpm != DEFAULT_TEMPO) {
			sequencer.setTempoInBPM(bpm);
		}
		if (loop) {
			sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
		}
		startPlayback();
	}

	public void startPlayback() {
		stopRecordingAndPlayback();
		((Sequencer) getDevice(MIDIDeviceType.SEQUENCER)).start();
		isPlaying = true;
	}

	public void stopPlayback() {
		if (isPlaying) {
			((Sequencer) getDevice(MIDIDeviceType.SEQUENCER)).stop();
			isPlaying = false;
		}
	}


	//---------------------------------------- RECORD --------------------------------------------\\

	public void startRecording(int trackIndex) {
		stopRecordingAndPlayback();
		Sequencer sequencer = (Sequencer) getDevice(MIDIDeviceType.SEQUENCER);
		sequencer.recordEnable(sequencer.getSequence().getTracks()[trackIndex], -1);
		sequencer.startRecording();
		isRecording = true;
	}

	public void stopRecording() {
		if (isRecording) {
			((Sequencer) getDevice(MIDIDeviceType.SEQUENCER)).stopRecording();
			isRecording = false;
		}
	}

	public void stopRecordingAndPlayback() {
		stopRecording();
		stopPlayback();
	}


	//------------------------------------- SAVE TO FILE -----------------------------------------\\

	public void saveSequenceToFile(String filePath) {
		stopRecordingAndPlayback();
		Sequence sequence = ((Sequencer) getDevice(MIDIDeviceType.SEQUENCER)).getSequence();
		try {
			FileOutputStream fos = new FileOutputStream(filePath);
			MidiSystem.write(sequence, MidiSystem.getMidiFileTypes(sequence)[0], fos);
		} catch (Exception e) {
			Throw.aRuntime(MIDIDeviceException.class, "Could not save "
					+ "a MIDI sequence to \"" + filePath + "\"", e);
		}
	}


	//---------------------------------------- GETTERS -------------------------------------------\\

	private MidiDevice getDevice(MIDIDeviceType type) {
		MidiDevice device = selectedDevices.get(type);
		if (device == null) {
			Throw.aRuntime(MIDIDeviceException.class, "No MIDI " + 
					type.getName() + " was selected");
		}
		return device;
	}

	public Sequence getSequence() {
		stopRecordingAndPlayback();
		return ((Sequencer) getDevice(MIDIDeviceType.SEQUENCER)).getSequence();
	}


	//--------------------------------------- SHUTDOWN -------------------------------------------\\

	public void shutdown() {
		stopRecordingAndPlayback();
		for (MidiDevice device : selectedDevices.values()) {
			if (device.isOpen()) {
				device.close();
			}
		}
	}


	//------------------------------------ PRINT DEVICE INFO -------------------------------------\\

	public void printAvailableDevicesInfo() {
		for (MidiDevice device : availableDevices.values()) {
			if (Log.infos() && Log.line(2));
			printInfo(device);
		}
		if (Log.infos() && Log.line(2));
	}

	private static void printInfo(MidiDevice device) {
		if (device instanceof Synthesizer) {
			printSynthesizerInfo((Synthesizer) device);
		} else if (device instanceof Sequencer) {
			printSequencerInfo((Sequencer) device);
		} else {
			printBasicDeviceInfo(device);
		}
	}

	private static void printSynthesizerInfo(Synthesizer synth) {
		printBasicDeviceInfo(synth);
		printInfoItem("Latency", 			synth.getLatency());
		printInfoItem("Nb loaded instr.", 	synth.getLoadedInstruments().length);
		printInfoItem("Max polyphony", 		synth.getMaxPolyphony());
		printInfoItem("Default soundbank", 	synth.getDefaultSoundbank().getName());
	}

	private static void printSequencerInfo(Sequencer sequencer) {
		printBasicDeviceInfo(sequencer);

		SyncMode[] masterSyncModes = sequencer.getMasterSyncModes();
		printInfoItem("Master sync modes", masterSyncModes[0]);
		for (int i = 1; i < masterSyncModes.length; ++i) {
			printInfoItem("", masterSyncModes[i]);
		}

		SyncMode[] slaveSyncModes = sequencer.getSlaveSyncModes();
		printInfoItem("Slave sync modes", slaveSyncModes[0]);
		for (int i = 1; i < slaveSyncModes.length; ++i) {
			printInfoItem("", slaveSyncModes[i]);
		}
	}

	private static void printBasicDeviceInfo(MidiDevice device) {
		Info info = device.getDeviceInfo();
		Class<?> clazz = device.getClass();
		Class<?> superclass = clazz.getSuperclass();
		Class<?>[] interfaces = clazz.getInterfaces();

		printInfoItem("Name", 			info.getName());
		printInfoItem("Description", 	info.getDescription());
		printInfoItem("Version", 		info.getVersion());
		printInfoItem("Vendor", 		info.getVendor());
		printInfoItem("Class", 			clazz.getSimpleName());
		printInfoItem("Superclass", 	superclass.getSimpleName());

		if (interfaces.length == 0) {
			printInfoItem("Interfaces", "None");
		} else {
			printInfoItem("Interfaces", interfaces[0].getSimpleName());
			for (int i = 1; i < interfaces.length; ++i) {
				printInfoItem("", interfaces[i].getSimpleName());
			}
		}
		printInfoItem("Max IN Ports", 	device.getMaxReceivers() == -1 ? 
				"unlimited" : device.getMaxReceivers());
		printInfoItem("Max OUT Ports", 	device.getMaxTransmitters() == -1 ? 
				"unlimited" : device.getMaxTransmitters());
	}

	private static void printInfoItem(String infoType, Object infoItem) {
		if (Log.infos() && Log.print("%-" + INFO_LIST_SPACING + "s: %s", infoType, infoItem));
	}
}
