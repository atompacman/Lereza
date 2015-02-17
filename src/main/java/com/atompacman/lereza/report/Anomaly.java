package com.atompacman.lereza.report;

import com.atompacman.lereza.api.Module;

public interface Anomaly {

	//===================================== INNER TYPES ==========================================\\

	public class Info {

		//=================================== INNER TYPES ========================================\\

		public enum Impact {
			NONE, MINIMAL, MODERATE, CRITIC, FATAL;
		}

		public enum Recoverability {
			UNKNOWN, TRIVIAL, NORMAL, HARD, IMPOSSIBLE;
		}



		//===================================== FIELDS ===========================================\\

		private final String name;
		private final String description;
		private final String consequences;

		private final Impact 			impact;
		private final Recoverability 	recoverability;

		private final Class<? extends Module> assocModule;



		//===================================== METHODS ==========================================\\

		//-------------------------------- PUBLIC CONSTRUCTOR ------------------------------------\\

		public Info(String name, String description, String consequences, Impact impact,
				Recoverability recoverability, Class<? extends Module> assocModule) {

			this.name = name;
			this.description = description;
			this.consequences = consequences;

			this.impact = impact;
			this.recoverability = recoverability;

			this.assocModule = assocModule;
		}


		//------------------------------------- GETTERS ------------------------------------------\\

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public String getConsequences() {
			return consequences;
		}

		public Impact getImpact() {
			return impact;
		}

		public Recoverability getRecoverability() {
			return recoverability;
		}

		public Class<? extends Module> getAssociatedModule() {
			return assocModule;
		}
	}

	
	
	//=================================== ABSTRACT METHODS =======================================\\

	Info info();
}