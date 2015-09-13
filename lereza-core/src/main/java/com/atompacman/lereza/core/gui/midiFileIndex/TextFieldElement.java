package com.atompacman.lereza.core.gui.midiFileIndex;

import javax.swing.JLabel;

import com.atompacman.lereza.core.db.context.ContextElementType;

public class TextFieldElement extends NoLabelTextFieldElement {

	//======================================= FIELDS =============================================\\

	private JLabel label;
	
	

	//======================================= METHODS ============================================\\

	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

	TextFieldElement(ContextElementType type, int columnWidth, int componentWidth) {
		super(type, columnWidth, componentWidth);
		this.label = new JLabel(type.formattedName().toUpperCase());
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public JLabel getLabel() {
		return label;
	}
}
