package com.atompacman.lereza.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;

import com.atompacman.configuana.App;
import com.atompacman.configuana.Cmd;
import com.atompacman.configuana.Param;
import com.atompacman.configuana.Settings;
import com.atompacman.configuana.StrictParam;
import com.atompacman.toolkat.misc.Log;
import com.atompacman.toolkat.misc.StringHelper;
import com.atompacman.toolkat.module.Module;

public class Wizard extends App {

    //==================================== STATIC FIELDS =========================================\\

    private static App app;
    private static Settings currSettings;
    private static Map<Class<? extends Module>, Module> modules;



    //======================================= METHODS ============================================\\

    //------------------------------ CONFIGUANA INITIALIZATION -----------------------------------\\

    public void init() {
        init(this, false);
    }

    public List<Class<? extends Cmd<?,?>>> getCmdClasses() {
        return new ArrayList<>();
    }

    public List<Class<? extends StrictParam>> getParamsClasses() {
        return Arrays.asList(Parameters.class);
    }



    //==================================== STATIC METHODS ========================================\\

    //------------------------------------ INITIALIZATION ----------------------------------------\\

    public static void manualInit() {
        init(new Wizard(), true);
    }

    private static void init(Wizard app, boolean manualInit) {
        if (Wizard.app != null) {
            return;
        }
        Log.title(Level.INFO, "Lereza Wizard initialization");

        Wizard.app = app;
        if (!manualInit) {
            Wizard.currSettings = app.getDefaultProfile();
        }
        Wizard.modules = new HashMap<Class<? extends Module>, Module>();
    }


    //-------------------------------------- GET MODULE ------------------------------------------\\

    @SuppressWarnings("unchecked")
    public static <T extends Module> T getModule(Class<T> moduleClass) {
        assertInit();
        T module = (T) modules.get(moduleClass);

        if (module == null) {
            String split = StringHelper.splitCamelCase(moduleClass.getSimpleName(), false);
            Log.title(Level.INFO, split + " initialization");
            try {
                Method instanceGetter = moduleClass.getMethod("getInstance", (Class<?>[]) null);
                module = (T) instanceGetter.invoke(null, (Object[]) null);
            } catch (Exception e) {
                throw new RuntimeException("Could not initialize module "
                        + "\"" + moduleClass.getSimpleName() + "\"", e);	
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