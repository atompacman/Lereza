package com.atompacman.lereza.gui;

import java.awt.GridLayout;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.atompacman.lereza.db.Login;

public class LoginDialog {
	
	//==================================== STATIC METHODS ========================================\\
	
	//------------------------------------ ASK FOR LOGIN -----------------------------------------\\

	public static Login askForLogin() {
		return askForLogin("Login", null);
	}
	
	public static Login askForLogin(String windowName) {
		return askForLogin(windowName, null);
	}
	
	public static Login askForLogin(String windowName, String imageIconPath) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,2));
		panel.add(new JLabel("Username:"));
		JTextField usernameField = new JTextField();
		panel.add(usernameField);
		panel.add(new JLabel("Password:"));
		JPasswordField passwordField = new JPasswordField();
		panel.add(passwordField);
		
		int selected = JOptionPane.CANCEL_OPTION;
		
		if (imageIconPath == null) {
			selected = JOptionPane.showConfirmDialog(null, panel, windowName, 
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		} else {
			Icon icon = new ImageIcon(imageIconPath);
			selected = JOptionPane.showConfirmDialog(null, panel, windowName, 
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
		}
		
		if (selected == JOptionPane.CANCEL_OPTION) {
			return null;
		}

		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());
		
		return new Login(username, password);
	}
}
