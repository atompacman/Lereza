package com.atompacman.lereza.piece;


abstract class PieceComponentBuilder<T extends PieceComponent> {

	//======================================= FIELDS =============================================\\

	protected PieceBuilderSupervisor supervisor;



	//======================================= METHODS ============================================\\

	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

	PieceComponentBuilder(PieceBuilderSupervisor supervisor) {
		this.supervisor = supervisor;
	}

	final T build() {
		T component = buildComponent();
		reset();
		return component;
	}

	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public PieceBuilderSupervisor getSupervisor() {
		return supervisor;
	}
	
	
	
	//=================================== ABSTRACT METHODS =======================================\\

	protected abstract T buildComponent();

	protected abstract void reset();
}
