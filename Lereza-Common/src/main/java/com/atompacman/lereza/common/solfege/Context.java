package com.atompacman.lereza.common.solfege;

import java.util.ArrayList;
import java.util.List;

public class Context {
	
	private Class<? extends Subgenre> genre;
	private Class<? extends Form> subGenre;
	private Form form;
	
	
	public interface Subgenre {}
	
	public interface Form {}
	
	public enum Genre {;
	
		public enum BAROQUE implements Subgenre {;
		
			public enum EARLY_BAROQUE 		implements Form { FUGUE; }
			public enum MIDDLE_BAROQUE 		implements Form { FUGUE; }
			public enum HIGH_BAROQUE		implements Form { FUGUE; }
		}
		
		public enum METAL implements Subgenre {;
		
			public enum BLACK_METAL 		implements Form {}
			public enum DEATH_METAL 		implements Form {}
			public enum DJENT 				implements Form {}
			public enum HAIR_METAL 			implements Form {}
			public enum HARDCORE 			implements Form {}
			public enum MATHCORE 			implements Form {}
			public enum MELODIC_DEATH_METAL implements Form {}
			public enum METALCORE 			implements Form {}
			public enum NU_METAL 			implements Form {}
			public enum PROGRESSIVE_METAL 	implements Form {}
			public enum THRASH_METAL		implements Form {}
		}
	
		public enum TRADITIONNAL implements Subgenre {;
		
			public enum NURSERY_RHYME 		implements Form { ROUND; }
		}
	}
	

	//------------ CONSTRUCTORS ------------\\

	@SuppressWarnings("unchecked")
	public Context(Form form) {
		this.form = form;
		this.subGenre = (Class<? extends Form>) form.getClass();
		this.genre = (Class<? extends Subgenre>) form.getClass().getEnclosingClass();
	}
	
	
	//------------ STATIC CONSTRUCTORS ------------\\

	public static Context valueOf(String genreName, String subgenreName, String formName) {
		for (Class<?> genre : Genre.class.getDeclaredClasses()) {
			if (!genre.getSimpleName().equals(genreName)) {
				continue;
			}
			for (Class<?> subgenre : genre.getDeclaredClasses()) {
				if (!subgenre.getSimpleName().equals(subgenreName)) {
					continue;
				}
				for (Object form : subgenre.getEnumConstants()) {
					if (form.toString().equals(formName)) {
						return new Context((Form) form);
					}
				}
			}
		}
		throw new IllegalArgumentException("No available context from \""
				+ genreName + "/" + subgenreName + "/" + formName + "\".");
	}
	
	
	//------------ GETTERS ------------\\

	public Class<? extends Subgenre> getGenre() {
		return genre;
	}
	
	public Class<? extends Form> getSubGenre() {
		return subGenre;
	}

	public Form getForm() {
		return form;
	}

	
	//------------ FIND SIMILAR FORMS ------------\\

	public List<Context> findSimilarForms() {
		List<Context> contexts = new ArrayList<Context>();
		
		for (Class<?> genre : Genre.class.getDeclaredClasses()) {
			for (Class<?> subgenre : genre.getDeclaredClasses()) {
				for (Object form : subgenre.getEnumConstants()) {
					if (this.form.toString().equals(form.toString())) {
						Context similarContext = new Context((Form) form);
						if (!toString().equals(similarContext.toString())) {
							contexts.add(similarContext);
						}
					}
				}
			}
		}
		return contexts;
	}
	
	
	//------------ STRING ------------\\

	public String toString() {
		return genre.getSimpleName() + "/" + subGenre.getSimpleName() + "/" + form.toString();
	}
}
