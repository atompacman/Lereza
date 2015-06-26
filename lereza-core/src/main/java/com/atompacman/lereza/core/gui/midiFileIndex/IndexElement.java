package com.atompacman.lereza.core.gui.midiFileIndex;

import java.awt.Component;

import javax.swing.JLabel;

import com.atompacman.lereza.core.resources.context.ContextElementType;
import com.atompacman.lereza.core.resources.context.ContextElementType.DatabaseStatus;

public abstract class IndexElement {
	
	//======================================= FIELDS =============================================\\

	protected ContextElementType elementType;
	private   int				 columnWidth;
	

	
	//==================================== STATIC METHODS ========================================\\

	//---------------------------------- STATIC CONSTRUCTOR --------------------------------------\\

	public static IndexElement create(ContextElementType type, int columnWidth) {
		if (type.getDatabaseStatus() != DatabaseStatus.PRIMARY_KEY) {
			throw new IllegalArgumentException("Only primary keys don't have a component width.");
		}
		return new TableOnlyElement(type, columnWidth);
	}
	
	public static IndexElement create(ContextElementType type, 
			int columnWidth, int componentWidth) {
		switch (type.getDatabaseStatus()) {
		case TEXT_FIELD:
			return new TextFieldElement(type, columnWidth, componentWidth);
		case SINGLE_PARENT:	case MULTIPLE_PARENTS: case NO_PARENT:			
			return new ComboBoxElement(type, columnWidth, componentWidth);
		case URL_FIELD:
			return new NoLabelTextFieldElement(type, columnWidth, componentWidth);
		default:
			throw new IllegalArgumentException("Primary keys don't have a component width.");
		}
	}
	
	
	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

	IndexElement(ContextElementType type, int columnWidth) {
		this.elementType = type;
		this.columnWidth = columnWidth;
	}


	//--------------------------------------- GETTERS --------------------------------------------\\

	public ContextElementType getContextElementType() {
		return elementType;
	}
	
	public String getName() {
		return elementType.formattedName();
	}
	
	public int getQueryColumn() {
		return elementType.getDatabaseQueryColumn();
	}

	public int getColumnWidth() {
		return columnWidth;
	}

	
	
	//=================================== ABSTRACT METHODS =======================================\\

	//--------------------------------------- GETTERS --------------------------------------------\\

	public abstract JLabel getLabel();

	public abstract Component getInputComponent();
	
	public abstract String getTextContent();
}