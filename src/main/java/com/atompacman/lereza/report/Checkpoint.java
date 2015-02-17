package com.atompacman.lereza.report;

public interface Checkpoint {

	//===================================== INNER TYPES ==========================================\\

	public class Info {
		
		//===================================== FIELDS ===========================================\\

		private final String 	name;
		private final boolean 	isMandatory;



		//===================================== METHODS ==========================================\\

		//-------------------------------- PUBLIC CONSTRUCTOR ------------------------------------\\

		public Info(String name, boolean isMandatory) {
			this.name = name;
			this.isMandatory = isMandatory;
		}
		
		
		//------------------------------------- GETTERS ------------------------------------------\\

		public String getName() {
			return name;
		}

		public boolean isMandatory() {
			return isMandatory;
		}
	}

	
	
	//=================================== ABSTRACT METHODS =======================================\\
	
	Info info();
}
