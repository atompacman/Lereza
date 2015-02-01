package com.atompacman.lereza.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atompacman.atomlog.Log;
import com.atompacman.configuana.App;
import com.atompacman.configuana.Cmd;
import com.atompacman.configuana.Settings;
import com.atompacman.configuana.param.Param;
import com.atompacman.lereza.Parameters;
import com.atompacman.lereza.Parameters.MIDI.FilePlayer;
import com.atompacman.lereza.api.cmd.PlayMIDIFile;
import com.atompacman.lereza.db.DatabaseImpl;
import com.atompacman.lereza.exception.UnimplementedFeatureException;
import com.atompacman.lereza.exception.UninitializedDeviceException;
import com.atompacman.lereza.midi.MIDIManager;
import com.atompacman.lereza.piece.tool.PieceBuilderImpl;
import com.atompacman.lereza.profile.ProfileManagerImpl;
import com.atompacman.toolkat.exception.Throw;

public class Wizard extends App {

	//==================================== STATIC FIELDS =========================================\\

	private static App 									app;
	private static Map<Class<? extends Device>, Device> devices;
	private static Settings 							currSettings;



	//======================================= METHODS ============================================\\

	//------------------------------ CONFIGUANA INITIALIZATION -----------------------------------\\

	public void init() {
		init(app);
	}

	public List<Class<? extends Cmd<?>>> getCmdClasses() {
		List<Class<? extends Cmd<?>>> cmdClasses = new ArrayList<>();
		cmdClasses.add(PlayMIDIFile.class);
		return cmdClasses;
	}

	public List<Class<? extends Param>> getParamsClasses() {
		List<Class<? extends Param>> paramClasses = new ArrayList<>();
		paramClasses.add(Parameters.class);
		return paramClasses;
	}



	//==================================== STATIC METHODS ========================================\\

	//------------------------------------ INITIALIZATION ----------------------------------------\\

	private static void init(App app) {
		if (Wizard.app != null) {
			return;
		}
		if (Log.vital() && Log.title("Lereza Wizard initialization"));
		Wizard.app = app;
		Wizard.devices = new HashMap<Class<? extends Device>, Device>();
		Wizard.currSettings = app.getDefaultProfile();
	}

	@SuppressWarnings("unchecked")
	public static <D extends Device> D initDevice(Class<D> deviceClass) {
		assertInit();
		if (devices.containsKey(deviceClass)) {
			throw new RuntimeException(deviceClass.getSimpleName() 
					+ " can only be initialized once");
		}

		D device = null;

		if (deviceClass == PieceBuilder.class) {
			device = (D) new PieceBuilderImpl();
		}
		else if (deviceClass == ProfileManager.class) {
			device = (D) new ProfileManagerImpl();
		}
		else if (deviceClass == Database.class) {
			device = (D) DatabaseImpl.getInstance();
		} 
		else if (deviceClass == MIDIManager.class) {
			device = (D) MIDIManager.getInstance();
		} 
		else {
			Throw.aRuntime(UnimplementedFeatureException.class, "Unimplemented initialization of "
					+ "a device of class \"" + deviceClass.getSimpleName() + "\"");	
		}
		devices.put(deviceClass, device);
		return device;
	}


	//--------------------------------------- SHUTDOWN -------------------------------------------\\

	public static void shutdown() {
		assertInit();
		Log.writeFinalLogs();
		for (Device device : devices.values()) {
			device.shutdown();
		}
	}


	//-------------------------------------- GET DEVICE ------------------------------------------\\

	@SuppressWarnings("unchecked")
	public static <D extends Device> D getDevice(Class<D> deviceClass) {
		assertInit();
		D device = (D) devices.get(deviceClass);
		if (device == null) {
			Throw.aRuntime(UninitializedDeviceException.class, deviceClass.getSimpleName() 
					+ " was not initialized");
		}
		return device;
	}


	//-------------------------------------- PARAMETERS ------------------------------------------\\

	public static <P extends Param> Object get(P param) {
		assertInit();
		return currSettings.get(param);
	}

	public static <P extends Param> String getString(P param) {
		assertInit();
		return currSettings.getString(param);
	}

	public static <P extends Param> int getInt(P param) {
		assertInit();
		return currSettings.getInt(param);
	}

	public static <P extends Param> double getDouble(P param) {
		assertInit();
		return currSettings.getDouble(param);
	}

	public static <P extends Param> long getLong(P param) {
		assertInit();
		return currSettings.getLong(param);
	}

	public static <P extends Param> boolean getBoolean(P param) {
		assertInit();
		return currSettings.getBoolean(param);
	}


	//-------------------------------------- ASSERT INIT -----------------------------------------\\

	private static void assertInit() {
		if (app == null) {
			throw new RuntimeException("Uninitialized Wizard.");
		}
	}
}