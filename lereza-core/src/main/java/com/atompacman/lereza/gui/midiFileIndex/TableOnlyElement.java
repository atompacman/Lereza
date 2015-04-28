package com.atompacman.lereza.gui.midiFileIndex;

import java.awt.Component;

import javax.swing.JLabel;

import com.atompacman.lereza.resources.context.ContextElementType;

public class TableOnlyElement extends IndexElement {
	
	//======================================= METHODS ============================================\\

	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

	TableOnlyElement(ContextElementType type, int columnWidth) {
		super(type, columnWidth);
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public JLabel getLabel() {
		return null;
	}
	
	public Component getInputComponent() {
		return null;
	}
	
	public String getTextContent() {
		return null;
	}
}
