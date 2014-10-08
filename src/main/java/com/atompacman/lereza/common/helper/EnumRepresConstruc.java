package com.atompacman.lereza.common.helper;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class EnumRepresConstruc<A> {

	private Class<A> clazz;
	private List<Constructor<A>> constructors;


	//------------ CONSTRUCTORS ------------\\

	public EnumRepresConstruc(Class<A> clazz) {
		this.clazz = clazz;
		detectEnumBasedConstructors();
	}

	@SuppressWarnings("unchecked")
	private void detectEnumBasedConstructors() {
		this.constructors = new ArrayList<Constructor<A>>();

		for (Constructor<A> constructor : (Constructor<A>[]) clazz.getConstructors()) {
			boolean typesAreAllEnums = false;
			for (Class<?> clazz : constructor.getParameterTypes()) {
				typesAreAllEnums  = true;
				if (!(clazz.isEnum())) {
					typesAreAllEnums = false;
					break;
				}
			}
			if (typesAreAllEnums) {
				constructors.add(constructor);
			}
		}
		if (constructors.isEmpty()) {
			throw new RuntimeException("\"" + clazz.getSimpleName() 
					+ "\" has no constructors based only on enum values.");
		}
	}


	//------------ NEW INSTANCE ------------\\

	public A newInstance(String repres) {
		try {
			for (Constructor<A> constructor : constructors) {
				Class<?>[] paramTypes = constructor.getParameterTypes();
				int nbParams = paramTypes.length;
				String copy = repres;
				Object[] args = new Object[nbParams];
				for (int i = 0; i < nbParams; ++i) {
					Class<?> enumClass = constructor.getParameterTypes()[i];
					Object enumCnst = findMatchingEnumCnst(enumClass, copy);
					args[i] = enumCnst;
					copy = copy.substring(enumCnst.toString().length());
				}
				if (copy.isEmpty()) {
					return constructor.newInstance(args);
				}
			}
			throw new Exception();
		} catch (Exception e) {
			throw new IllegalArgumentException("\"" + repres + "\" is not a valid "
					+ "representation of a \"" + clazz.getSimpleName() + "\" object.", e);
		}
	}
	
	private static Object findMatchingEnumCnst(Class<?> enumClass, String repres) throws Exception {
		Object betterMatch = null;
		
		for (Object cnst : enumClass.getEnumConstants()) {
			String cnstRepres = cnst.toString();
			if (repres.indexOf(cnstRepres) == 0) {
				if (betterMatch == null || cnstRepres.length() > betterMatch.toString().length()) {
					betterMatch = cnst;
				}
			}
		}
		if (betterMatch == null) {
			throw new Exception("No enum constant of class \"" + enumClass.getSimpleName()
					+ "\" is represented by \"" + repres + "\".");
		}
		return betterMatch;
	}
}
