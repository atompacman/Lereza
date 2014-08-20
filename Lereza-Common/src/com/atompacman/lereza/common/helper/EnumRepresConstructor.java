package com.atompacman.lereza.common.helper;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class EnumRepresConstructor<A> {

	private Class<A> clazz;
	private List<Constructor<A>> constructors;


	//------------ CONSTRUCTORS ------------\\

	public EnumRepresConstructor(Class<A> clazz) {
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
		for (Constructor<A> constructor : constructors) {
			String copy = repres;
			Object[] args = new Object[constructor.getParameterTypes().length];
			for (int i = 0; i <  constructor.getParameterTypes().length; ++i) {
				Class<?> enumClass = constructor.getParameterTypes()[i];
				for (Object cnst : enumClass.getEnumConstants()) {
					String cnstRepres = cnst.toString();
					if (copy.indexOf(cnstRepres) == 0) {
						args[i] = cnst;
						copy = copy.substring(cnstRepres.length());
						break;
					}
				}
			}
			if (copy.isEmpty()) {
				try {
					return constructor.newInstance(args);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		throw new IllegalArgumentException("\"" + repres + "\" is not a valid representation of a \""
				+ clazz.getSimpleName() + "\" object.");
	}
}
