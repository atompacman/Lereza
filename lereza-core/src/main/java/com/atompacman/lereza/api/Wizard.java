package com.atompacman.lereza.api;

import java.lang.reflect.Method;
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
import com.atompacman.lereza.api.cmd.PlayMIDIFile;
import com.atompacman.lereza.exception.UnimplementedFeatureException;
import com.atompacman.toolkat.exception.Throw;
import com.atompacman.toolkat.misc.StringHelper;

public class Wizard extends App {

	//==================================== STATIC FIELDS =========================================\\

	private static App 									app;
	private static Map<Class<? extends Module>, Module> modules;
	private static Settings 							currSettings;

	

	//======================================= METHODS ============================================\\

	//------------------------------ CONFIGUANA INITIALIZATION -----------------------------------\\

	public void init() {
		init(this);
	}

	public List<Class<? extends Cmd<?,?>>> getCmdClasses() {
		List<Class<? extends Cmd<?,?>>> cmdClasses = new ArrayList<>();
		cmdClasses.add(PlayMIDIFile.class);
		return cmdClasses;
	}

	public List<Class<? extends Param>> getParamsClasses() {
		List<Class<? extends Param>> paramClasses = new ArrayList<>();
		paramClasses.add(Parameters.class);
		return paramClasses;
	}

	
	//--------------------------------- CONFIGUANA SHUTDOWN --------------------------------------\\

	public void shutdown() {
		for (Module module : modules.values()) {
			module.shutdown();
		}
	}


	
	//==================================== STATIC METHODS ========================================\\

	//------------------------------------ INITIALIZATION ----------------------------------------\\

	private static void init(Wizard app) {
		if (Wizard.app != null) {
			return;
		}
		if (Log.vital() && Log.title("Lereza Wizard initialization"));
		Wizard.app = app;
		Wizard.modules = new HashMap<Class<? extends Module>, Module>();
		Wizard.currSettings = app.getDefaultProfile();
	}


	//-------------------------------------- GET DEVICE ------------------------------------------\\

	@SuppressWarnings("unchecked")
	public static <T extends Module> T getModule(Class<T> moduleClass) {
		assertInit();
		T module = (T) modules.get(moduleClass);
		
		if (module == null) {
			if (Log.infos() && Log.title(StringHelper.splitCamelCase(
					moduleClass.getSimpleName(), false) + " initialization", 0));
			try {
				Method instanceGetter = moduleClass.getMethod("getInstance", (Class<?>[]) null);
				module = (T) instanceGetter.invoke(null, (Object[]) null);
			} catch (Exception e) {
				Throw.aRuntime(UnimplementedFeatureException.class, "Could not initialize "
						+ "module \"" + moduleClass.getSimpleName() + "\"", e);	
			}
			
			modules.put(moduleClass, module);
		}
		
		return module;
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